package com.nirmiteepublic.clink.retrofit;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.helpers.Prefs;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.retrofit.utility.UpdateData;
import com.nirmiteepublic.clink.ui.activity.actions.OtpActivity;
import com.pegalite.popups.PegaUpdateDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Sahil The Geek,
 * Date : 17-07-2022.
 *
 * <p>This {@link  PegaResponseCallback} class contains server responses and errors</p>
 */
public abstract class PegaResponseCallback {
    private final Activity activity;
    private final Prefs prefs;

    protected PegaResponseCallback(Activity activity) {
        this.activity = activity;
        prefs = new Prefs(activity);
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
     * code : 002
     */
    public void onInvalidReferCode() {
        PegaAnimationUtils.showToast(activity, "Invalid ReferCode");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 011
     */
    public void onUnverifiedAccount() {
        PegaAnimationUtils.showToast(activity, "You Haven't Verified Your Account Yet");
        Intent intent = new Intent(activity, OtpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 012
     */
    public void onOTPExpired() {
        PegaAnimationUtils.showToast(activity, "Otp Expired");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 009, 010, 013
     */
    public void onInvalidOTP() {
        PegaAnimationUtils.showToast(activity, "Invalid OTP");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 008
     */
    public void onInvalidEmail() {
        PegaAnimationUtils.showToast(activity, "Invalid Email");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 001
     */
    public void onEmailExists() {

    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 003
     */
    public void onEmailNotExists() {

    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 004
     */
    public void onIncorrectPassword() {
        PegaAnimationUtils.showToast(activity, "Incorrect Password");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 005
     */
    public void onAccountBanned() {
        accountTask("isAccountBanned", "Your Account Has Been Banned");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 007
     */
    public void onSessionExpired() {
        accountTask("isSessionExpired", "Your session has been expired");
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 014
     */
    public void onMaintenance(String msg) {
        Toast.makeText(activity, "App Under Maintenance", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(activity).setTitle("Attention!").setMessage(msg).setCancelable(false).setPositiveButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            activity.finish();
        }).show();
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 015
     */
    public void onUpdate(UpdateData updateData) {
        PegaUpdateDialog updateDialog = new PegaUpdateDialog(activity);
        updateDialog.ShowUpdate(updateData.getVersion(), updateData.getUrl());
    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 017
     */
    public void onAlreadyUsedTransactionID() {

    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 018
     */
    public void onAlreadyReferProgramJoined() {

    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 019
     */
    public void onReferProgramNotJoined() {

    }

    /**
     * <p>Gets Called on <strong>failed</strong> event from server </p>
     * <p>response : failed,</p>
     * code : 020
     */
    public void onInsufficientMoney() {

    }

    /**
     * <p>Gets Called on <strong>error</strong> event from server </p>
     * <p>response : error</p>
     */
    public void onError() {

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
//            Intent intent = new Intent(activity, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//            activity.finish();
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

    /**
     *
     */
    private void accountTask(String extra, String msg) {
        PegaAnimationUtils.showToast(activity, msg);

        prefs.clearAll();

//        Intent intent = new Intent(activity, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(extra, true);
//        activity.startActivity(intent);
//        activity.finish();
    }

}
