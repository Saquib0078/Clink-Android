package com.pegalite.popups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import com.pegalite.popups.databinding.UpdateDialogBinding;

public class PegaUpdateDialog {
    Activity context;
    Dialog dialog;

    UpdateDialogBinding binding;

    public PegaUpdateDialog(Activity context) {
        this.context = context;
    }

    public void ShowUpdate(String versionName, String url) {
        dialog = new Dialog(context);
        binding = UpdateDialogBinding.inflate(context.getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.versionName.setText(versionName);
        binding.update.setOnClickListener(view -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))));
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
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
