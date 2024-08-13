package com.nirmiteepublic.clink.functions.viewmanagers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.helpers.ConnectivityReceiver;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.pegalite.popups.PegaNoInternetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil The Geek
 * Date : 10-06-2022.
 *
 * <p>This {@link  PegaAppCompatActivity} adds extra functionality in the activity</p>
 */
public class PegaAppCompatActivity extends AppCompatActivity {

    private final List<Dialog> dialogList = new ArrayList<>();
    public boolean isInternetAvailable = true, receiverRegistered = false;
    private boolean isLastActivity = false, backWithRightAnim = false;
    private ConnectivityReceiver connectivityReceiver;
    private PegaNoInternetDialog pegaNoInternetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectivityReceiver = new ConnectivityReceiver(isConnected -> {

            onConnectivityChanged(isConnected);
            isInternetAvailable = isConnected;

            if (!isConnected) {
                pegaNoInternetDialog = new PegaNoInternetDialog(this);
                pegaNoInternetDialog.show();
                PegaAnimationUtils.showToast(this, "No Internet Connection");
            } else {
                if (pegaNoInternetDialog != null && pegaNoInternetDialog.isShowing()) {
                    pegaNoInternetDialog.dismiss();
                }
            }
        });

    }

    public Activity getActivity() {
        return this;
    }

    public Window getDefaultWindow() {
        /* For Window Color Adjustments */
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return window;
    }

    public void setWindowThemeThird() {
        setWindowColors(R.color.white);
    }

    public void setWindowThemeSecond() {
        /* For Window Color Adjustments */
        Window window = getDefaultWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.cyan_process));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        manageStatusBarColor(window, R.color.cyan_process);
    }



    public void setWindowThemeBlack() {
        /* For Window Color Adjustments */
        Window window = getDefaultWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blackShade));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        manageStatusBarColor(window, R.color.blackShade);
    }

    public void setWindowThemeMain() {
        /* For Window Color Adjustments */
        Window window = getDefaultWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.off_white));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        manageStatusBarColor(window, R.color.off_white);
    }

    public void setColorCyan() {
        /* For Window Color Adjustments */
        Window window = getDefaultWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.off_white));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        manageStatusBarColor(window, R.color.cyan_process);
    }

    private void setWindowColors(int color) {
        Window window = getDefaultWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, color));
        window.setNavigationBarColor(ContextCompat.getColor(this, color));
        manageStatusBarColor(window, color);
    }

    private void manageStatusBarColor(Window window, int color) {
        if (checkIfDarkFamily(color) && color != R.color.white && color != R.color.off_white) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public boolean checkIfDarkFamily(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        double brightness = (0.299 * red + 0.587 * green + 0.114 * blue) / 255;
        double threshold = 0.5;
        return brightness < threshold;
    }

    public void setBackWithRightAnim() {
        backWithRightAnim = true;
    }

    public boolean isInternetAvailable() {
        return isInternetAvailable;
    }

    public void onConnectivityChanged(boolean isConnected) {
    }

    public void addDialogToDestroyList(Dialog dialog) {
        dialogList.add(dialog);
    }

    public void removeDialogFromDestroyList(Dialog dialog) {
        dialogList.remove(dialog);
    }

    private void registerReceiver() {
        if (!receiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectivityReceiver, intentFilter);
            receiverRegistered = true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiverRegistered) {
            unregisterReceiver(connectivityReceiver);
            receiverRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        if (pegaNoInternetDialog != null && pegaNoInternetDialog.isShowing()) {
            pegaNoInternetDialog.dismiss();
        }
        if (receiverRegistered) {
            unregisterReceiver(connectivityReceiver);
            receiverRegistered = false;
        }
        for (int i = 0; i < dialogList.size(); i++) {
            Dialog dialog = dialogList.get(i);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        super.onDestroy();
    }

    /**
     * When the Activity will be set as last activity if user press back button the app will ask confirmation to exit
     */
    public void setAsLastActivity() {
        isLastActivity = true;
    }

    public void unsetAsLastActivity() {
        isLastActivity = false;
    }

    @Override
    public void onBackPressed() {
        if (isLastActivity) {
            new AlertDialog.Builder(this, R.style.alertDialog).setTitle("Are you sure?").setMessage("Do you really want to exit").setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).setPositiveButton("Exit", (dialog, which) -> {
                dialog.dismiss();
                finish();
            }).show();
            return;
        }
        super.onBackPressed();
        if (backWithRightAnim) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }

    public void finishAfterPegaTransition() {
        new Handler().postDelayed(this::finish, 1000);
    }


    public void openActivityWithRightAnim(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openResultActivityWithRightAnim(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(this, cls), requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openActivityWithRightAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openActivityWithLeftAnim(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onRestartRequest() {
    }

    @Override
    protected void onStop() {
        new Instrumentation().callActivityOnSaveInstanceState(this, new Bundle());
        super.onStop();
    }
}
