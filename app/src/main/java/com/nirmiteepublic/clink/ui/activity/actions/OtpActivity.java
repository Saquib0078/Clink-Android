package com.nirmiteepublic.clink.ui.activity.actions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityOtpBinding;
import com.nirmiteepublic.clink.functions.helpers.Prefs;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaOTPViewCreator;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaInvalidOTPDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends PegaAppCompatActivity {


    ActivityOtpBinding binding;

    int invalidOTPCount = 0;

    /**
     * How long user have to wait to resend otp
     */
    long timerCount = 0;

    Prefs prefs;

    private JSONObject data;

    private PegaOTPViewCreator pegaOTPViewCreator;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeThird();
        setBackWithRightAnim();

        prefs = new Prefs(this);

        if (!getIntent().hasExtra("data")) {
            /* Invalid Activity Opening */
            System.exit(0);
            return;
        }

        initData();


        pegaOTPViewCreator = new PegaOTPViewCreator(binding.otpContainer, binding.keyBoardContainer, this);
        pegaOTPViewCreator.createKeyBoard();

        /* Create 5 OTP input box with  */
        pegaOTPViewCreator.createOTPView(5);

        /* Callback will trigger after user entered the OTP */
        pegaOTPViewCreator.setOnCompleteListener((otp) -> {
            try {
                verifyOTP(otp);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        /* Start the countdown timer to resend OTP */
        startCountDownTimer();

        PegaAnimationUtils.fadeUp(binding.container, new ArrayList<>(), this, null);

    }

    private void verifyOTP(String otp) throws JSONException {

        JSONObject tempData = new JSONObject(data.toString());

        Call<ResponseBody> call;
        String action = tempData.getString("action");

        tempData.remove("action");
        tempData.put("otp", otp);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), tempData.toString());

        if (action.equals("registration")) {
            call = RetrofitClient.getInstance(this).getApiInterfaces().register(requestBody);
        } else {
            call = RetrofitClient.getInstance(this).getApiInterfaces().login(requestBody);
        }

        call.enqueue(new PegaResponseManager(new PegaCallback(this) {
            @Override
            public void onInvalidOTP() {
                super.onInvalidOTP();
                invalidOTP();
            }

            @Override
            public void onOTPExpired() {
                super.onOTPExpired();
                pegaOTPViewCreator.resetOTP();
                new AlertDialog.Builder(OtpActivity.this).setTitle("OTP Expired!").setMessage("The otp has been expired. try to send new otp again").setCancelable(false).setPositiveButton("ReSend", (dialog, which) -> {
                    dialog.dismiss();
                    resendOTP();
                }).setNegativeButton("Exit", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
            }

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                if (data != null) {
                    try {
                        prefs.setJWT(data.getString("token"));

                        if (action.equals("registration")) {
                            openActivityWithRightAnim(new Intent(getActivity(), UserDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                            return;
                        }
                        reopenApp();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
    }

    private void resendOTP() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data.toString());
        RetrofitClient.getInstance(this).getApiInterfaces().resendOTP(requestBody).enqueue(new PegaResponseManager(new PegaCallback(this) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                invalidOTPCount = 0; /* Reset Invalid OTP Count */

                binding.lottieAnimation.playAnimation();
                timerCount = 4;
                startCountDownTimer();
            }
        }));

    }

    private void invalidOTP() {
        if (invalidOTPCount > 1) {
            /* User have entered invalid OTP 3 times */
            PegaAnimationUtils.fadeDown(binding.container, new ArrayList<>(), OtpActivity.this, null);
            PegaInvalidOTPDialog invalidOTPDialog = new PegaInvalidOTPDialog(OtpActivity.this);
            invalidOTPDialog.show(DialogData.UN_CANCELABLE);
        } else {
            /* User have entered invalid OTP */
            invalidOTPCount++;

            /* Reset the OTP input box */
            pegaOTPViewCreator.resetOTP();

            PegaAnimationUtils.shake(binding.otpContainer);

            /* Vibrate the device once */
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
            }

        }
    }

    private void initData() {
        try {
            data = new JSONObject(getIntent().getStringExtra("data"));
            initBasics();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }




    private void initBasics() throws JSONException {
        binding.number.setText(data.getString("num"));

        binding.changeEmail.setOnClickListener(v -> new AlertDialog.Builder(this, R.style.alertDialog).setTitle("Are You Sure?").setMessage("Do you really want to change the number?").setPositiveButton("Confirm", (dialog, which) -> {
            dialog.dismiss();
            try {
                closeEmail();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show());

        binding.resendOTP.setOnClickListener(v -> {
            if (binding.resendOTP.getText().toString().equals("You can now request to resend otp")) {
                resendOTP();
            } else {

                PegaAnimationUtils.shake(binding.resendOTP);

                /* Vibrate the device once */
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
                }

            }
        });

    }

    private void closeEmail() throws JSONException {
        prefs.clearAll();
        onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void startCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer((timerCount * 60 * 1000) + 30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;

                long seconds = (millisUntilFinished / 1000) % 60;

                binding.resendOTP.setText("You can request to resend otp in : " + String.format(Locale.ROOT, "%d:%02d", minutes, seconds));

            }

            @Override
            public void onFinish() {
                binding.resendOTP.setText("You can now request to resend otp");
            }
        }.start();
    }


}