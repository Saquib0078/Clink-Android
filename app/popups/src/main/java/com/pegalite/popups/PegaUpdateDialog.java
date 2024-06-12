package com.pegalite.popups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;

import com.pegalite.popups.databinding.UpdateDialogBinding;

public class PegaUpdateDialog {
    Activity context;
    UpdateDialogBinding binding;
    private Dialog dialog;

    public PegaUpdateDialog(Activity context) {
        this.context = context;
    }

    public void ShowUpdate(String version) {
        dialog = new Dialog(context);
        binding = UpdateDialogBinding.inflate(context.getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (version == null) {
            binding.msgC.setVisibility(View.GONE);
        } else {
            binding.versionName.setText(version);
        }
        binding.update.setOnClickListener(view -> update());
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

    private void update() {
        String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException activityNotFoundException) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
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
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
