package com.nirmiteepublic.clink.ui.activity.actions;

import android.os.Bundle;

import com.nirmiteepublic.clink.databinding.ActivityOtpBinding;
import com.nirmiteepublic.clink.functions.helpers.Prefs;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;

public class OtpActivity extends PegaAppCompatActivity {

    ActivityOtpBinding binding;

    int invalidOTPCount = 0;

    /**
     * How long user have to wait to resend otp
     */
    long timerCount = 0;

    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeSecond();
        setAsLastActivity();

        prefs = new Prefs(this);


        if (getIntent().hasExtra("data")) {
            binding.email.setText(getIntent().getStringExtra("e"));
        } else {
            binding.email.setText(prefs.getEmail());
        }
    }
}
//        binding.changeEmail.setOnClickListener(v -> new AlertDialog.Builder(this, R.style.alertDialog).setTitle("Are You Sure?").setMessage("Do you really want to change the email?").setPositiveButton("Confirm", (dialog, which) -> {
//            dialog.dismiss();
//            try {
//                closeEmail();
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show());
//        PegaOTPViewCreator pegaOTPViewCreator = new PegaOTPViewCreator(binding.otpContainer, binding.keyBoardContainer, this);
//        pegaOTPViewCreator.createKeyBoard();
//
//        /* Create 5 OTP input box with  */
//        pegaOTPViewCreator.createOTPView(5);
//
//        /* Callback will trigger after user entered the OTP */
//        pegaOTPViewCreator.setOnCompleteListener(() -> {
//            try {
//                validateOTP(pegaOTPViewCreator);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        });

/* Start the countdown timer to resend OTP */
//        startCountDownTimer();

//        PegaAnimationUtils.fadeUp(binding.container, new ArrayList<>(), this, null);

//        binding.resendOTP.setOnClickListener(v -> {
//            if (binding.resendOTP.getText().toString().equals("You can now request to resend otp")) {
//                try {
////                    resendOTP();
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            } else {
//
//                PegaAnimationUtils.shake(binding.resendOTP);
//
//                /* Vibrate the device once */
//                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
//                }
//
//            }
//        });
//    }

//    private void closeEmail() throws JSONException {
//        prefs.clearAll();
////        startActivity(new Intent(OtpActivity.this, SplashActivity.class));
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        finish();
//    }
//
//    private void validateOTP(PegaOTPViewCreator pegaOTPViewCreator) throws JSONException {
//        PegaProgressDialog progressDialog = new PegaProgressDialog(this);
//        progressDialog.ShowProgress(DialogData.UN_CANCELABLE);
//
//        Call<ResponseBody> call;
//        if (getIntent().hasExtra("data")) {
//            call = RetrofitClient.getInstance().getApiInterfaces().resetPassword(RequestBody.create(MediaType.parse("application/json"), new JSONObject().put("e", getIntent().getStringExtra("e")).put("np", getIntent().getStringExtra("p")).put("otp", pegaOTPViewCreator.getOTP()).toString()));
//        } else {
//            call = RetrofitClient.getInstance().getApiInterfaces().validateOTP(RequestBody.create(MediaType.parse("application/json"), new JSONObject().put("token", prefs.getJWT()).put("igo", true).put("otp", pegaOTPViewCreator.getOTP()).toString()));
//        }
//
//        PegaResponseManager responseManager = new PegaResponseManager();
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                progressDialog.dismiss();
//                pegaOTPViewCreator.resetOTP();
//
//                responseManager.manageOnResponse(response, new PegaResponseCallback(OtpActivity.this) {
//                    @Override
//                    public void onSuccess(@Nullable JSONObject data) {
//                        if (getIntent().hasExtra("data")) {
//                            Toast.makeText(OtpActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
//                        }
//                        startActivity(new Intent(OtpActivity.this, SplashActivity.class));
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        finish();
//                    }
//
//                    @Override
//                    public void onOTPExpired() {
//                        super.onOTPExpired();
//                        new AlertDialog.Builder(OtpActivity.this).setTitle("OTP Expired").setMessage("The otp has been expired. try to send new otp again").setCancelable(false).setPositiveButton("Send", (dialog, which) -> {
//                            dialog.dismiss();
//                            try {
//                                resendOTP();
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }).setNegativeButton("Exit", (dialog, which) -> {
//                            dialog.dismiss();
//                            finish();
//                        }).show();
//                    }
//
//                    @Override
//                    public void onInvalidOTP() {
//                        super.onInvalidOTP();
//                        invalidOTP();
//
//                    }
//                });
//            }
//
////            private void invalidOTP() {
////                if (invalidOTPCount > 1) {
////                    /* User have entered invalid OTP 3 times */
////                    PegaAnimationUtils.fadeDown(binding.container, new ArrayList<>(), OtpActivity.this, null);
////                    PegaInvalidOTPDialog invalidOTPDialog = new PegaInvalidOTPDialog(OtpActivity.this);
////                    invalidOTPDialog.show(DialogData.UN_CANCELABLE);
////                } else {
////                    /* User have entered invalid OTP */
////
////                    invalidOTPCount++;
////
////                    /* Reset the OTP input box */
////                    pegaOTPViewCreator.resetOTP();
////
////                    PegaAnimationUtils.shake(binding.otpContainer);
////                    /* Vibrate the device once */
////                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                        vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
////                    }
////
////                }
////            }
//
////            @Override
////            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
////                progressDialog.dismiss();
////                pegaOTPViewCreator.resetOTP();
////                responseManager.manageOnFailure(t, new PegaResponseCallback(OtpActivity.this) {
////                });
////            }
////        });
////
////    }
//
//
////    private void resendOTP() throws JSONException {
////        PegaProgressDialog progressDialog = new PegaProgressDialog(this);
////        progressDialog.ShowProgress(DialogData.UN_CANCELABLE);
////
////        JSONObject object = new JSONObject();
////
////        Call<ResponseBody> call;
////        if (getIntent().hasExtra("data")) {
////            object.put("e", getIntent().getStringExtra("e"));
////            call = RetrofitClient.getInstance().getApiInterfaces().resendOTPByEmail(RequestBody.create(MediaType.parse("application/json"), object.toString()));
////        } else {
////            object.put("token", prefs.getJWT()).put("igo", true);
////            call = RetrofitClient.getInstance().getApiInterfaces().resendOTP(RequestBody.create(MediaType.parse("application/json"), object.toString()));
////        }
////
////        PegaResponseManager responseManager = new PegaResponseManager();
////        call.enqueue(new Callback<ResponseBody>() {
////            @Override
////            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
////                progressDialog.dismiss();
////                responseManager.manageOnResponse(response, new PegaResponseCallback(OtpActivity.this) {
////                    @Override
////                    public void onSuccess(@Nullable JSONObject data) {
////                        binding.lottieAnimation.playAnimation();
////                        timerCount = 4;
////                        startCountDownTimer();
////
////                        Toast.makeText(OtpActivity.this, "OTP sent Successfully", Toast.LENGTH_SHORT).show();
////                    }
////
////                    @Override
////                    public void onError(Throwable t) {
////                        super.onError(t);
////                        PegaAnimationUtils.showToast(OtpActivity.this, "Unable to resend otp");
////                    }
////                });
////            }
////
////            @Override
////            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
////                progressDialog.dismiss();
////                responseManager.manageOnFailure(t, new PegaResponseCallback(OtpActivity.this) {
////                });
////            }
////        });
////
////    }
//
//    @SuppressLint("SetTextI18n")
//    private void startCountDownTimer() {
//        new CountDownTimer((timerCount * 60 * 1000) + 30000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                long minutes = (millisUntilFinished / 1000) / 60;
//
//                long seconds = (millisUntilFinished / 1000) % 60;
//
//                binding.resendOTP.setText("You can request to resend otp in : " + String.format(Locale.ROOT, "%d:%02d", minutes, seconds));
//
//            }
//
//            @Override
//            public void onFinish() {
//                binding.resendOTP.setText("You can now request to resend otp");
//            }
//        }.start();
//    }
//
//}