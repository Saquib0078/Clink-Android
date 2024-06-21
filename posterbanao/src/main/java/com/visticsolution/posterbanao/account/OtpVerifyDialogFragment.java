package com.visticsolution.posterbanao.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//TODO LOGIN FLOW
/*import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;*/
import com.visticsolution.posterbanao.HomeActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.FragmentOtpVerifyDialogBinding;
import com.visticsolution.posterbanao.model.UserModel;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.responses.WhatsappOtpResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerifyDialogFragment extends Fragment {


    public OtpVerifyDialogFragment(String type,String vId, String num) {
        otpType = type;
        number = num;
        verificationId = vId;
    }

    Activity context;
    FragmentOtpVerifyDialogBinding binding;
    String otpType;
    String number;

    String verificationId;
    //TODO LOGIN FLOW
    /*    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;*/
    LoginModel loginmodel = new LoginModel();
    UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOtpVerifyDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        //TODO LOGIN FLOW
//        mAuth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (otpType.equals("phone")){
            binding.messageTv.setText("Check your SMS messages. We've send you the OTP at "+number);
        }else{
            binding.messageTv.setText("Check your Whatsapp Chat. We've send you the OTP at "+number);
        }
        //TODO LOGIN FLOW

/*        binding.recentBtn.setOnClickListener(view1 -> {
            if (otpType.equals("phone")){
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        getActivity(),               // Activity (for callback binding)
                        mCallbacks);
            }else{
                getWhatsappOtp(number);
            }
        });*/
        //TODO LOGIN FLOW

 /*       mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;

                Functions.showToast(context,context.getString(R.string.otp_send_success));
                Functions.cancelLoader();
                startCountdown();
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                verifyAuth(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Functions.cancelLoader();
            }
        };
*/
        //TODO LOGIN FLOW

/*
        binding.submitBtn.setOnClickListener(view1 -> {
            String otp = binding.otpView.getOtp();
            if (otp.length() > 5){
                Functions.showLoader(context);
                if (otpType.equals("phone")){
                    verifyAuth(PhoneAuthProvider.getCredential(verificationId, otp));
                }else{
                    if (verificationId.equals(otp)){
                        binding.otpView.showSuccess();
                        loginmodel.phoneNo = number;
                        insertLoginData("whatsapp", "whatsapp");
                    }else {
                        Functions.cancelLoader();
                        Toast.makeText(context, getString(R.string.wrong_otp), Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                binding.otpView.showError();
                Toast.makeText(context, getString(R.string.enter_currect_otp), Toast.LENGTH_SHORT).show();
            }
        });
*/

        startCountdown();
    }

    private void insertLoginData(String socialId, String social) {
        Functions.showLoader(context);
        if (!loginmodel.lname.equals("")){
            loginmodel.fname = loginmodel.fname+" "+loginmodel.lname;
        }

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("social_id", socialId);
            parameters.put("social", social);
            parameters.put("auth_token", loginmodel.authTokon);
            parameters.put("email", loginmodel.email);
            parameters.put("number", loginmodel.phoneNo);
            parameters.put("profile_pic", loginmodel.picture);
            parameters.put("name", loginmodel.fname);
            parameters.put("device_token", Functions.getDeviceToken(context));
        } catch (Exception e) {
            e.printStackTrace();
        }

        userViewModel.login(parameters).observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse != null){
                    Functions.cancelLoader();
                    if (userResponse.code == Constants.SUCCESS){
                        parseLoginData(userResponse.userModel);
                    }else {
                        Functions.showToast(context,userResponse.message);
                    }
                }
            }
        });
    }

    public void parseLoginData(UserModel userModel) {
        Functions.saveUserData(userModel, context);
        isTimerRunning = false;
        if (userModel.getName() == null || userModel.getName().equals("null") ||
                userModel.getState() == null || userModel.getState().equals("") ||
                userModel.getDistrict() == null || userModel.getDistrict().equals("")){

            startActivity(new Intent(context, EditProfileActivity.class).putExtra("fromlogin",true));

        }else {
            Intent intent=new Intent(context, HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void getWhatsappOtp(String number) {
        ApiClient.getRetrofit().create(ApiService.class).createWhatsappOtp(
                Constants.API_KEY,number).enqueue(new Callback<WhatsappOtpResponse>() {
            @Override
            public void onResponse(Call<WhatsappOtpResponse> call, Response<WhatsappOtpResponse> response) {
                Functions.cancelLoader();
                if (response.body() !=null) {
                    if (response.body().code == 200) {
                        verificationId = response.body().getOtp();

                        Functions.showToast(context,context.getString(R.string.otp_send_success));
                        Functions.cancelLoader();
                        startCountdown();
                    }else {
                        Functions.showToast(context,response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<WhatsappOtpResponse> call, Throwable t) {
                Functions.showToast(context,t.getMessage());
            }
        });
    }

    //TODO LOGIN FLOW
/*
    private void verifyAuth(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Functions.cancelLoader();
                if (task.isSuccessful()) {
                    loginmodel.phoneNo = number;
                    insertLoginData(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), "phone");
                } else {
                    binding.otpView.showError();
                    Toast.makeText(getActivity(), "The verification code you have been entered incorrect !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/

    @Override
    public void onStop() {
        super.onStop();
        isTimerRunning = false;
    }

    int sec = 30;
    boolean isTimerRunning = false;
    private void startCountdown() {
        sec = 30;
        isTimerRunning = true;
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    if (isTimerRunning){
                        sec--;
                        binding.recentBtn.setEnabled(false);
                        binding.recentBtn.setText(getString(R.string.resend_otp)+" ("+sec+")");
                    }
                }catch (Exception e){}
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                if (getContext() != null){
                    binding.recentBtn.setText(getString(R.string.resend_otp));
                    binding.recentBtn.setEnabled(true);
                }
            }
        }.start();
    }
}