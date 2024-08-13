package com.visticsolution.posterbanao.dialog;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.FragmentInquiryBinding;
import com.visticsolution.posterbanao.model.ServicesModel;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

public class InquiryFragment extends DialogFragment {

    public InquiryFragment() {}

    FragmentInquiryBinding binding;
    public static ServicesModel model = new ServicesModel();
    UserViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInquiryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setAttributes(getLayoutParams(getDialog()));
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.title.setText(model.getTitle());
        BindingAdaptet.setImageUrl(binding.image,model.getThumb_url());
        binding.offerPrice.setText(getString(R.string.currency)+" "+model.getNew_price());
        binding.priceTv.setText(getString(R.string.currency)+" "+model.getOld_price());
        binding.priceTv.setPaintFlags(binding.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }


    public void submit() {
        if (binding.numberEt.getText().toString().length() < 9){
            Toast.makeText(getContext(), getString(R.string.please_enter_currect_number), Toast.LENGTH_SHORT).show();
        }else if (binding.messageEt.getText().toString().length() < 10){
            Toast.makeText(getContext(), getString(R.string.message_is_very_short), Toast.LENGTH_SHORT).show();
        }else{
            Functions.showLoader(getContext());
            viewModel.addInquiry(Functions.getUID(getContext()),model.getId(),binding.numberEt.getText().toString(),binding.messageEt.getText().toString()).observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
                @Override
                public void onChanged(UserResponse userResponse) {
                    Functions.cancelLoader();
                    if (userResponse != null){
                        new CustomeDialogFragment(
                                getString(R.string.sucsess),
                                userResponse.message,
                                DialogType.SUCCESS,
                                false,
                                false,
                                false,
                                new CustomeDialogFragment.DialogCallback() {
                                    @Override
                                    public void onCencel() {
                                    }
                                    @Override
                                    public void onSubmit() {
                                    }
                                    @Override
                                    public void onDismiss() {

                                    }
                                    @Override
                                    public void onComplete(Dialog dialog) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismiss();
                                            }
                                        });
                                    }
                                }
                        ).show(getChildFragmentManager(),"");
                    }
                }
            });
        }
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }
}