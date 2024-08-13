package com.visticsolution.posterbanao.dialog;

import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.databinding.FragmentPaymentTypeDialogBinding;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.viewmodel.HomeViewModel;

public class PaymentTypeDialogFragment extends BottomSheetDialogFragment {


    String type = "";
    int discount = 0;
    CallBack callBack;
    Activity context;
    FragmentPaymentTypeDialogBinding binding;
    HomeViewModel homeViewModel;
    boolean promocode;

    public PaymentTypeDialogFragment(boolean promocode, CallBack callBack) {
        this.callBack = callBack;
        this.promocode = promocode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentTypeDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        if (!promocode){
            binding.ptv.setVisibility(View.GONE);
            binding.play.setVisibility(View.GONE);
        }
        if (Functions.getSharedPreference(context).getString(Variables.PAYTM, "").equals("true")) {
            binding.paytmRb.setVisibility(View.VISIBLE);
        }
        if (Functions.getSharedPreference(context).getString(Variables.CCAVENUE, "").equals("true")) {
            binding.ccavenueRb.setVisibility(View.VISIBLE);
        }
        if (Functions.getSharedPreference(context).getString(Variables.RAZORPAY, "").equals("true")) {
            binding.razorpayRb.setVisibility(View.VISIBLE);
        }
        if (Functions.getSharedPreference(context).getString(Variables.STRIPE, "").equals("true")) {
            binding.stripeRb.setVisibility(View.VISIBLE);
        }
        if (Functions.getSharedPreference(context).getString("offline_payment", "").equals("true")) {
            binding.offlineRb.setVisibility(View.VISIBLE);
        }
        if (Functions.getSharedPreference(context).getString("instamojo", "").equals("true")) {
            binding.instamojoRb.setVisibility(View.VISIBLE);
        }

        binding.cheakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.showLoader(context);
                if (binding.codeEt.getText().toString().isEmpty()){
                    Functions.cancelLoader();
                    Toast.makeText(context, R.string.please_enter_promo, Toast.LENGTH_SHORT).show();
                }else {
                    homeViewModel.cheakPromo(binding.codeEt.getText().toString()).observe(getViewLifecycleOwner(), new Observer<HomeResponse>() {
                        @Override
                        public void onChanged(HomeResponse homeResponse) {
                            Functions.cancelLoader();
                            if (homeResponse != null){
                                if (homeResponse.code == SUCCESS){
                                    binding.successTv.setVisibility(View.VISIBLE);
                                    discount = Integer.parseInt(homeResponse.promocode.getDiscount());
                                }else {
                                    discount = 0;
                                    binding.successTv.setVisibility(View.GONE);
                                    Functions.showToast(context,homeResponse.message);
                                }
                            }else{
                                Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.paytm_rb) {
                    type = "paytm";
                } else if (i == R.id.razorpay_rb) {
                    type = "razorpay";
                } else if (i == R.id.stripe_rb) {
                    type = "stripe";
                } else if (i == R.id.offline_rb) {
                    type = "offline";
                } else if (i == R.id.instamojo_rb) {
                    type = "instamojo";
                } else if (i == R.id.ccavenue_rb) {
                    type = "ccavenue";
                }
            }
        });
        binding.cencelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("")) {
                    Toast.makeText(getContext(), "Please select payment type", Toast.LENGTH_SHORT).show();
                } else {
                    callBack.getResponse(type,binding.codeEt.getText().toString(),discount);
                    dismiss();
                }
            }
        });
    }
}