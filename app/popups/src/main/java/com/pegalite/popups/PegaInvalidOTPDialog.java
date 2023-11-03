package com.pegalite.popups;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class PegaInvalidOTPDialog {
    private final Activity context;

    Dialog dialog;

    public PegaInvalidOTPDialog(Activity context) {
        this.context = context;
    }

    public void show(DialogData data) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.invalid_otp_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (data.equals(DialogData.FINISH_ON_CANCEL)) {
            dialog.setOnCancelListener(dialog -> {
                dismiss();
                context.finish();
            });
        } else if (data.equals(DialogData.UN_CANCELABLE)) {
            dialog.setCancelable(false);
        }
        dialog.create();
        dialog.show();
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
