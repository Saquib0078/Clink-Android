package com.nirmiteepublic.clink.ui.activity.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityLoginBinding;
import com.nirmiteepublic.clink.functions.helpers.CredentialsValidator;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.ui.activity.actions.OtpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends PegaAppCompatActivity {

    ActivityLoginBinding binding;
    private CredentialsValidator credentialsValidator;


    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (validateData(false)) {
                binding.login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.sign_enabled));
            } else {
                binding.login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.sign_disabled));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setWindowThemeThird();


        credentialsValidator = new CredentialsValidator(this);
        binding.number.addTextChangedListener(textWatcher);

        binding.openRegister.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.numberContainer, Objects.requireNonNull(ViewCompat.getTransitionName(binding.numberContainer)));
            startActivity(new Intent(this, RegistrationActivity.class), options.toBundle());
        });

        binding.login.setOnClickListener(v -> {
            if (validateData(true)) {
                String number = binding.number.getText().toString().trim().replace("+91", "");
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                String fcmToken = task.getResult();
                                Intent intent=new Intent();
                                intent.putExtra("fcmToken",fcmToken);
//                                Toast.makeText(this, ""+fcmToken, Toast.LENGTH_SHORT).show();
//                                sendFcmTokenToServer(fcmToken);
                            } else {
                                // Handle the error
                                Log.e("FCM", "Failed to get token");
                            }
                        });
                try {
                    JSONObject data = new JSONObject();
                    data.put("num", number);
                    data.put("action", "login");
                    sendOTP(data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }




        private void sendOTP(JSONObject mainData) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), mainData.toString());
        RetrofitClient.getInstance(this).getApiInterfaces().login(requestBody).enqueue(new PegaResponseManager(new PegaCallback(this) {
            @Override
            public void onNumberNotExists() {
                super.onNumberNotExists();
                PegaAnimationUtils.shake(binding.numberContainer);
            }

            @Override
            public void onInvalidNumber() {
                super.onInvalidNumber();
                PegaAnimationUtils.shake(binding.numberContainer);
            }

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                openActivityWithRightAnim(new Intent(getActivity(), OtpActivity.class).putExtra("data", mainData.toString()));
            }
        }));
    }

    /**
     * Validate all user input data
     */
    private boolean validateData(boolean showToast) {
        String number = binding.number.getText().toString().trim();

        if (showToast) {
            if (!credentialsValidator.validateNumber(number, true)) {
                PegaAnimationUtils.shake(binding.numberContainer);
                return false;
            }

            return true;
        } else {
            return credentialsValidator.validateNumber(number, false);
        }

    }
//

//    private void UpdatePrimaryUser(String fcmToken,String userId) {
//
//        Map<String, Object> updatedFields = new HashMap<>();
//        updatedFields.put("fcmToken", fcmToken);
//        RetrofitClient.getInstance(this).getApiInterfaces().updateUserById2(UserUtils.getUserId(), updatedFields).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() == 200) {
//                    Toast.makeText(LoginActivity.this, "Token Updated Successfully", Toast.LENGTH_SHORT).show();
//                    System.out.println(UserUtils.getUserId());
//                } else {
//                    Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(LoginActivity.this, ""+UserUtils.getUserId(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }


}