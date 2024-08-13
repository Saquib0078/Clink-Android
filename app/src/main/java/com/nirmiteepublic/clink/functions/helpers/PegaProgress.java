package com.nirmiteepublic.clink.functions.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.nirmiteepublic.clink.R;

import java.net.URL;

public class PegaProgress {


    private static AlertDialog progressDialog;

    public static void showProgressBar(Context context) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false); // Make the dialog non-cancelable

            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.progress_dialog, null);
            builder.setView(dialogView);

            progressDialog = builder.create();

            // Set the background of the dialog to transparent
            if (progressDialog.getWindow() != null) {
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            progressDialog.show();
        }
    }

    public static void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
