package com.nirmiteepublic.clink.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.helpers.Prefs;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.JSONUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.ui.activity.actions.UserDetailsActivity;
import com.nirmiteepublic.clink.ui.activity.auth.LoginActivity;
import com.nirmiteepublic.clink.ui.activity.auth.RegistrationActivity;
import com.visticsolution.posterbanao.HomeActivity;

import org.json.JSONObject;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends PegaAppCompatActivity {

    private Prefs prefs;
    private boolean isSessionExpired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        prefs = new Prefs(this);

        setWindowThemeThird();

        if (getIntent().hasExtra("isSessionExpired")) {
            isSessionExpired = true;
        }

//        prefs.clearAll();
        checkApp();
//        openActivityWithRightAnim(HomeActivity.class);
    }

    private void checkApp() {
        RetrofitClient.getInstance(this).getApiInterfaces().appStatus(getAppVersion()).enqueue(new PegaResponseManager(new PegaCallback(this, false) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                if (prefs.checkLogin()) {
                    sessionLogin(data);
                } else {
                    if (isSessionExpired) {
                        openActivityWithRightAnim(LoginActivity.class);
                    } else {
                        openActivityWithRightAnim(RegistrationActivity.class);
                    }
                    finish();
                }
            }
        }));
    }

    private void sessionLogin(JSONObject appData) {
        RetrofitClient.getInstance(this).getApiInterfaces().sessionLogin().enqueue(new PegaResponseManager(new PegaCallback(this) {
            @Override
            public void onAccountNotCompleted() {
                super.onAccountNotCompleted();
                openActivityWithRightAnim(UserDetailsActivity.class);
                finish();
            }

            @Override
            public void onAccountBanned() {
                new AlertDialog.Builder(getActivity()).setTitle("Your Account Banned").setMessage("Due To Your InApropriate Activity You Are Banned").setCancelable(false).setPositiveButton("Okay", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
                super.onAccountBanned();
            }

            @Override
            public void onAccountNotApproved() {
                new AlertDialog.Builder(getActivity()).setTitle("Notice!").setMessage("Your account is currently under review; we appreciate your patience and will notify you promptly upon completion of the approval process.").setCancelable(false).setPositiveButton("Okay", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
                super.onAccountNotApproved();
            }

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                if (data != null) {
                    JSONUtils.mergeJSON(appData, data);
                    System.out.println(data);
                    openActivityWithRightAnim(new Intent(getActivity(), MainActivity.class).putExtra("data", appData.toString()));
                    finish();
                }
            }
        }));
    }


    private int getAppVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}