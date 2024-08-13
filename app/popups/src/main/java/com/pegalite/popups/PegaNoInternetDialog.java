package com.pegalite.popups;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class PegaNoInternetDialog {
    private final Activity context;

    Dialog dialog;

    public PegaNoInternetDialog(Activity context) {
        this.context = context;
    }

    public void show() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.no_internet_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);

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
