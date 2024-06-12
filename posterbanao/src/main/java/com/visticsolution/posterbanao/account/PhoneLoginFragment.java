package com.visticsolution.posterbanao.account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//TODO LOGIN FLOW
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.visticsolution.posterbanao.databinding.FragmentPhoneLoginBinding;

public class PhoneLoginFragment extends Fragment {

    public PhoneLoginFragment() {
    }

    FragmentPhoneLoginBinding binding;
    String verificationId;
    //TODO LOGIN FLOW
    //    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    CountryCodePicker ccp;
    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhoneLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        ccp = new CountryCodePicker(view.getContext());
        ccp.setCountryForNameCode(ccp.getDefaultCountryNameCode());
        binding.countryCode.setText(ccp.getDefaultCountryCodeWithPlus());
        binding.countryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencountry();
            }
        });
        //TODO LOGIN FLOW
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onCodeAutoRetrievalTimeOut(String s) {
//                super.onCodeAutoRetrievalTimeOut(s);
//                if (getContext() != null) {
//                    Toast.makeText(getContext(), "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//                verificationId = s;
//                Toast.makeText(context, context.getString(R.string.otp_send_success), Toast.LENGTH_SHORT).show();
//                Functions.cancelLoader();
//
//                OtpVerifyDialogFragment comment_f = new OtpVerifyDialogFragment("phone",verificationId,ccp.getSelectedCountryCodeWithPlus()+binding.numberEt.getText().toString());
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
//                transaction.addToBackStack(null);
//                transaction.replace(android.R.id.content, comment_f).commit();
//            }
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                Functions.cancelLoader();
//            }
//        };
//        binding.numberEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_GO) {
//                    if (binding.numberEt.getText().toString().length() > 9){
//                        Functions.showLoader(context);
//                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                                ccp.getSelectedCountryCodeWithPlus()+binding.numberEt.getText().toString(),        // Phone number to verify
//                                60,                 // Timeout duration
//                                TimeUnit.SECONDS,   // Unit of timeout
//                                getActivity(),               // Activity (for callback binding)
//                                mCallbacks);
//                    }else{
//                        Functions.showToast(context,getString(R.string.please_enter_currect_number));
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO LOGIN FLOW
//                if (binding.numberEt.getText().toString().length() > 9){
//                    Functions.showLoader(context);
//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                            ccp.getSelectedCountryCodeWithPlus()+binding.numberEt.getText().toString(),        // Phone number to verify
//                            60,                 // Timeout duration
//                            TimeUnit.SECONDS,   // Unit of timeout
//                            getActivity(),               // Activity (for callback binding)
//                            mCallbacks);
//                }else{
//                    Functions.showToast(context,getString(R.string.please_enter_currect_number));
//                }
            }
        });


    }

    @SuppressLint("WrongConstant")
    public void opencountry() {
        ccp.showCountryCodePickerDialog();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                binding.countryCode.setText(ccp.getSelectedCountryCodeWithPlus());
            }
        });
    }

}