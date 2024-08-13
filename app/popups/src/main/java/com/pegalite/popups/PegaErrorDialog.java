package com.pegalite.popups;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import com.pegalite.popups.databinding.ErrorDialogBinding;
import com.pegalite.popups.listeners.PegaAnimationEndListener;

public class PegaErrorDialog {
    private final Activity context;
    private final DialogData dialogData;
    Dialog dialog;
    ErrorDialogBinding binding;

    public PegaErrorDialog(Activity context, DialogData dialogData) {
        this.context = context;
        this.dialogData = dialogData;
    }

    public void show(PegaAnimationEndListener listener) {
        dialog = new Dialog(context);
        binding = ErrorDialogBinding.inflate(context.getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (dialogData.equals(DialogData.FINISH_ON_CANCEL)) {
            dialog.setOnCancelListener(dialog -> {
                dismiss();
                context.finish();
            });
        } else if (dialogData.equals(DialogData.UN_CANCELABLE)) {
            dialog.setCancelable(false);
        }
        if (!context.isFinishing()) {
            dialog.create();
            dialog.show();
        }

        new Handler().postDelayed(listener::end, 2850);
    }

    public void setMessage(String msg) {
        if (dialog != null) {
            binding.msg.setText(msg);
        }
    }


    public boolean isShowing() {
        if (dialog == null) {
            return false;
        } else {
            return dialog.isShowing();
        }
    }

    public void dismiss() {
        if (isShowing()) {
            dialog.dismiss();
        }
    }


}
