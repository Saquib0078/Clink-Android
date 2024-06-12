package com.visticsolution.posterbanao.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.databinding.FragmentPickFromBinding;


public class PickFromFragment extends DialogFragment {

    String title = "";
    DialogCallback callBack;

    public PickFromFragment(String title, DialogCallback callBack) {
        this.title = title;
        this.callBack = callBack;
    }

    FragmentPickFromBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPickFromBinding.inflate(getLayoutInflater());
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setAttributes(getLayoutParams(getDialog()));

        binding.titleTv.setText(title);
        binding.fromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onGallery();
                dismiss();
            }
        });
        binding.fromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCamera();
                dismiss();
            }
        });
        binding.cencelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callBack.onCencel();
            }
        });
    }

    public interface DialogCallback{
        void onCencel();
        void onGallery();
        void onCamera();
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