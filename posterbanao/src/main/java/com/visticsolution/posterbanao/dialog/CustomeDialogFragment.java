package com.visticsolution.posterbanao.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.databinding.FragmentCustomeBinding;

import java.util.Timer;
import java.util.TimerTask;

public class CustomeDialogFragment extends DialogFragment {

    String title,message;
    DialogCallback callback;
    DialogType dialogType;
    FragmentCustomeBinding binding;
    boolean showcencel = false;
    boolean cancelable = false;
    boolean showbutton = true;

    public CustomeDialogFragment(String title, String message,DialogType dialogType,boolean showcencel,boolean cancelable,boolean showbutton, DialogCallback callback) {
        this.title = title;
        this.message = message;
        this.dialogType = dialogType;
        this.showcencel = showcencel;
        this.cancelable = cancelable;
        this.showbutton = showbutton;
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = FragmentCustomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setAttributes(getLayoutParams(getDialog()));
        setCancelable(cancelable);

        binding.titleTv.setText(title);
        binding.messageTv.setText(message);

        switch (dialogType){
            case SUCCESS:
                binding.titleTv.setBackgroundColor(getContext().getColor(R.color.success));
                binding.animationView.setAnimation(R.raw.success);
                binding.okBtn.setText(getString(R.string.done));
                break;
            case WARNING:
                binding.titleTv.setBackgroundColor(getContext().getColor(R.color.warning));
                binding.animationView.setAnimation(R.raw.warning);
                binding.okBtn.setText(getString(R.string.submit));
                break;
            case ERROR:
                binding.titleTv.setBackgroundColor(getContext().getColor(R.color.error));
                binding.animationView.setAnimation(R.raw.error);
                binding.okBtn.setText(getString(R.string.try_again));
                break;
        }

        if (!showbutton){
            binding.cencelBtn.setVisibility(View.GONE);
            binding.okBtn.setVisibility(View.GONE);
        }

        if (!showcencel || !cancelable){
            binding.cencelBtn.setVisibility(View.GONE);
            setCancelable(false);
        }

        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callback.onSubmit();
            }
        });
        binding.cencelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callback.onCencel();
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                callback.onComplete(getDialog());
            }
        },2000);
    }

    public interface DialogCallback{
        void onCencel();
        void onSubmit();
        void onDismiss();
        void onComplete(Dialog dialog);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        callback.onDismiss();
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