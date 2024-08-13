package com.nirmiteepublic.clink.functions.retrofit.res;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.helpers.Prefs;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.ui.activity.SplashActivity;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;
import com.pegalite.popups.PegaUpdateDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Sahil The Geek,
 * Date : 17-07-2022.
 *
 * <p>This {@link  PegaCallback} class contains server responses and errors</p>
 */
public abstract class PegaCallback {
    private final Activity activity;
    private final Prefs prefs;
    PegaProgressDialog progressDialog;
    private boolean createProgress = true;

    protected PegaCallback(Activity activity) {
        this.activity = activity;
        prefs = new Prefs(activity);
        progressDialog = new PegaProgressDialog(activity);
        progressDialog.ShowProgress(DialogData.UN_CANCELABLE);
    }

    protected PegaCallback(Activity activity, boolean createProgress) {
        this.activity = activity;
        this.createProgress = createProgress;
        prefs = new Prefs(activity);
    }


    /**
     * Gets Called on <strong>Response</strong> event from server Response
     */
    public void onResponse() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * Gets Called on <strong>success</strong> event from server with Nullable {@link JSONObject}
     */
    public void onSuccess(@Nullable JSONObject... data) throws JSONException {

    }

    /**
     * Gets Called on <strong>success</strong> event from server with Nullable {@link JSONObject}
     */
    public void onSuccess(@Nullable JSONObject data) {

    }

    /**
     * Gets Called on <strong>success</strong> event from server with Nullable {@link JSONArray}
     */
    public void onSuccess(@Nullable JSONArray data) {

    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 000
     */
    public void onMissingParameter() {
        PegaAnimationUtils.showToast(activity, "Missing Parameters");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 001
     */
    public void onInvalidNumber() {
        PegaAnimationUtils.showToast(activity, "Invalid Number");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 002
     */
    public void onInvalidName() {
        PegaAnimationUtils.showToast(activity, "Invalid Name");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 003
     */
    public void onInvalidOTP() {
        PegaAnimationUtils.showToast(activity, "Invalid OTP");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 101
     */
    public void onNumberExists() {
        PegaAnimationUtils.showToast(activity, "Number Already Exists");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 102
     */
    public void onNumberNotExists() {
        PegaAnimationUtils.showToast(activity, "Number Not Found");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 201
     */
    public void onAccountNotCompleted() {
        PegaAnimationUtils.showToast(activity, "Please Complete Your Account First");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 202
     */
    public void onAccountNotApproved() {
        PegaAnimationUtils.showToast(activity, "Account Not Approved Yet");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 203
     */
    public void onAccountBanned() {
        accountTask("isAccountBanned", "Your Account Has Been Banned");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 301
     */
    public void onOTPExpired() {
        PegaAnimationUtils.showToast(activity, "Otp Expired");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 302
     */
    public void onSessionExpired() {
        accountTask("isSessionExpired", "Your session has been expired");
    }


    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 900
     */
    public void onMaintenance(JSONObject data) throws JSONException {
        PegaAnimationUtils.showToast(activity, "App Under Maintenance");

        /* Default Maintenance Message */
        String msg = "App Under Maintenance. Please Wait for a Better and Enhanced User Experience. We apologize for any inconvenience and appreciate your patience as we work diligently to improve our services. Thank you for your understanding.";

        if (data != null && data.has("msg")) {
            msg = data.getString("msg");
        }

        new AlertDialog.Builder(activity, R.style.alertDialog).setTitle("Attention!").setMessage(msg).setCancelable(false).setPositiveButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            activity.finish();
        }).show();
    }


    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 901
     */
    public void onUpdateRequired(@Nullable JSONObject data) throws JSONException {
        PegaAnimationUtils.showToast(activity, "Update Required");
        PegaUpdateDialog updateDialog = new PegaUpdateDialog(activity);
        if (data != null) {
            updateDialog.ShowUpdate(data.getString("latestVersion"));
            return;
        }
        updateDialog.ShowUpdate(null);

    }


    /**
     * <p>Gets Called on <strong>error</strong> event from server </p>
     * <p>response : error</p>
     */
    public void onError() {
        Toast.makeText(activity, "Opps Error", Toast.LENGTH_SHORT).show();
    }

    /**
     * <p>Gets Called on <strong>error</strong> event from server </p>
     * <p>response : error</p>
     */
    public void onError(Throwable t) {
        t.printStackTrace();
        System.out.println("error msg " + t.getMessage());
        String message = t.getMessage();
        if (t instanceof IOException && message != null) {
            if (message.contains("Unable to resolve host")) {
                // Server not reachable
                showErrorDialog("Server not reachable. Please check your internet connection.");
            } else if (message.contains("timed out")) {
                // Request timed out
                showErrorDialog("Request timed out. Please try again later.");
            } else {
                // Network error (other)
                showErrorDialog("Network error. Please check your internet connection.");
            }
        } else {
            // Handle other errors (e.g., parsing error)
            showErrorDialog("An unexpected error occurred. Please try again later.");
        }
    }

    /**
     * <p>Gets Called on <strong>error</strong> event from server </p>
     * <p>response : error</p>
     */
    public void onError(int responseCode) {
        if (responseCode >= 500) {
            // Server error (5xx)
            showErrorDialog("Server error. Please try again later.");
        } else if (responseCode == 404) {
            // Not Found (404) - Resource not found on the server
            showErrorDialog("Server is not reachable at this moment. Please try again later.");
        } else if (responseCode >= 400) {
            // Client error (4xx)
            showErrorDialog("Client error. Please check your request.");
        }
    }

    /**
     * <p>Gets Called on any error happens in the app </p>
     */
    public void onAppError() {

    }

    /**
     * <p>Gets Called when server returns an invalid or unexpected response </p>
     */
    public void onUnexpectedResponse() {
        Toast.makeText(activity, "Unexpected response from the server", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(activity, R.style.alertDialog).setTitle("Error").setMessage("Unexpected Response from the server").setCancelable(false).setPositiveButton("Close", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(activity, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
        }).show();
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.alertDialog);
        builder.setTitle("Error!");
        builder.setMessage(message);
        builder.setPositiveButton("Okay", (dialog, which) -> {
            dialog.dismiss();
            activity.finish();
        });
        builder.create().show();
    }


    public void reopenApp() {
        openClear(SplashActivity.class);
    }

    public void openClear(Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Internal method to delete the cache and send to {@link SplashActivity}
     */
    private void accountTask(String extra, String msg) {
        PegaAnimationUtils.showToast(activity, msg);

        prefs.clearAll();

        Intent intent = new Intent(activity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(extra, true);
        activity.startActivity(intent);
        activity.finish();
    }

    public Activity getActivity() {
        return activity;
    }
}
