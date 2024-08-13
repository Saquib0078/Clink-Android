//TODO LOGIN FLOW
/*
package com.visticsolution.posterbanao.account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.FragmentWhatsappLoginBinding;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.WhatsappOtpResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhatsappLoginFragment extends Fragment {


    public WhatsappLoginFragment() {
        // Required empty public constructor
    }

    FragmentWhatsappLoginBinding binding;
    Activity context;
    CountryCodePicker ccp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWhatsappLoginBinding.inflate(getLayoutInflater());
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
        binding.numberEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (binding.numberEt.getText().toString().length() > 9){
                        Functions.showLoader(context);
                        getWhatsappOtp(ccp.getSelectedCountryCodeWithPlus() + binding.numberEt.getText().toString());
                    }else{
                        Functions.showToast(context,getString(R.string.please_enter_currect_number));
                    }
                    return true;
                }
                return false;
            }
        });
        binding.submitBtn.setOnClickListener(view1 -> {
            if (binding.numberEt.getText().toString().length() > 9){
                Functions.showLoader(context);
                getWhatsappOtp(ccp.getSelectedCountryCodeWithPlus() + binding.numberEt.getText().toString());
            }else{
                Functions.showToast(context,getString(R.string.please_enter_currect_number));
            }
        });
    }

    String whatsappOTP;
    private void getWhatsappOtp(String number) {
        ApiClient.getRetrofit().create(ApiService.class).createWhatsappOtp(
                Constants.API_KEY,number).enqueue(new Callback<WhatsappOtpResponse>() {
            @Override
            public void onResponse(Call<WhatsappOtpResponse> call, Response<WhatsappOtpResponse> response) {
                Functions.cancelLoader();
                if (response.body() !=null) {
                    if (response.body().code == 200) {
                        whatsappOTP = response.body().getOtp();
                        Functions.showToast(context,context.getString(R.string.otp_send_success));
                        Functions.cancelLoader();

                        OtpVerifyDialogFragment comment_f = new OtpVerifyDialogFragment("whatsapp",whatsappOTP,ccp.getSelectedCountryCodeWithPlus()+binding.numberEt.getText().toString());
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
                        transaction.addToBackStack(null);
                        transaction.replace(android.R.id.content, comment_f).commit();
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
}*/
