package com.pegalite.popups;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.pegalite.popups.databinding.ProgressDialogBinding;

public class PegaProgressDialog {
    private final Activity context;

    Dialog dialog;
    ProgressDialogBinding binding;

    public PegaProgressDialog(Activity context) {
        this.context = context;
    }
//TODO
    public void ShowProgress(DialogData data) {
//        dialog = new Dialog(context);
////        binding = ProgressDialogBinding.inflate(LayoutInflater.from(context));
////        binding = ProgressDialogBinding.inflate(context.getLayoutInflater());
////        dialog.setContentView(binding.getRoot());
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        if (data.equals(DialogData.FINISH_ON_CANCEL)) {
//            dialog.setOnCancelListener(dialog -> {
//                dismiss();
//                context.finish();
//            });
//        } else if (data.equals(DialogData.UN_CANCELABLE)) {
//            dialog.setCancelable(false);
//        }
//        if (!context.isFinishing()) {
//            dialog.create();
//            dialog.show();
//        }
    }

    public void setMessage(String msg) {
//        if (dialog != null) {
////            binding.msg.setText(msg);
//        }
    }


    public boolean isShowing() {
//        if (dialog == null) {
//            return false;
//        } else {
//            return dialog.isShowing();
//        }
        return  false;
    }

    public void dismiss() {
//        if (isShowing()) {
//            dialog.dismiss();
//        }
    }


}
