package com.visticsolution.posterbanao;

import static com.visticsolution.posterbanao.classes.Constants.SUCCESS;
import static com.visticsolution.posterbanao.classes.Constants.languageList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.visticsolution.posterbanao.account.EditProfileActivity;
//import com.visticsolution.posterbanao.account.LoginFragment;
import com.visticsolution.posterbanao.classes.App;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.editor.model.ServerData;
import com.visticsolution.posterbanao.fragments.SplashFragment;
import com.visticsolution.posterbanao.model.LanguageModel;
import com.visticsolution.posterbanao.model.SettingModel;
import com.visticsolution.posterbanao.customview.NonSwipeableViewPager;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.LanguageViewModel;
import com.visticsolution.posterbanao.viewmodel.SettingsViewModel;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<ServerData> fontArraylist = new ArrayList<>();
    SharedPreferences sharedPreferences;
    NonSwipeableViewPager viewPager;
    private SettingsViewModel settingsViewModel;
    private LanguageViewModel languageViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.setLocale(Functions.getSharedPreference(MainActivity.this).getString(Variables.APP_LANGUAGE_CODE, Variables.DEFAULT_LANGUAGE_CODE), this, MainActivity.class, false);
        setContentView(R.layout.activity_main);

        App.app.onCreate();
        sharedPreferences = getSharedPreferences(Variables.PREF_NAME, MODE_PRIVATE);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equals("login")) {
            viewPager.setCurrentItem(1);
        }

        getSetting();

//        HashKey For Facebook Login
//        Functions.PrintHashKey(this);
    }

    private void getSetting() {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.getAllSetting().observe(this, new Observer<List<SettingModel>>() {
            @Override
            public void onChanged(List<SettingModel> settingModels) {
                if (settingModels != null) {
                    for (int i = 0; i < settingModels.size(); i++) {
                        sharedPreferences.edit().putString(settingModels.get(i).field, settingModels.get(i).value).apply();
                    }
                    cheakUpdate();
                    getLangauges();
                } else {
                    new CustomeDialogFragment(
                            getString(R.string.api_error),
                            getString(R.string.please_config_api),
                            DialogType.ERROR, true, true, true,
                            new CustomeDialogFragment.DialogCallback() {
                                @Override
                                public void onCencel() {
                                }

                                @Override
                                public void onSubmit() {
                                    getSetting();
                                }

                                @Override
                                public void onComplete(Dialog dialog) {
                                }

                                @Override
                                public void onDismiss() {
                                }
                            }).show(getSupportFragmentManager(), "");
                }
            }
        });
    }

    private void getLangauges() {
        languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);
        languageViewModel.getData().observe(this, new Observer<List<LanguageModel>>() {
            @Override
            public void onChanged(List<LanguageModel> languageModels) {
                if (languageModels != null) {
                    languageList.clear();
                    languageList.addAll(languageModels);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void cheakUpdate() {
        if (sharedPreferences.getString(Variables.SHOW_UPDATE_DIALOG, "").equals("true")) {
//TODO GET DATA FROM BUILD CONFIG
//            if (!sharedPreferences.getString(Variables.APP_VERSION_CODE,""+BuildConfig.VERSION_CODE).equals(""+BuildConfig.VERSION_CODE)){
            if (!sharedPreferences.getString(Variables.APP_VERSION_CODE, "1.0").equals("1.0")) {
                new CustomeDialogFragment(
                        getString(R.string.app_update),
                        getString(R.string.please_update_app),
                        DialogType.WARNING, true, sharedPreferences.getString(Variables.FORCE_UPDATE, "").equals("false"), true,
                        new CustomeDialogFragment.DialogCallback() {
                            @Override
                            public void onCencel() {
                            }

                            @Override
                            public void onSubmit() {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sharedPreferences.getString(Variables.APP_LINK, ""))));
                            }

                            @Override
                            public void onComplete(Dialog dialog) {
                            }

                            @Override
                            public void onDismiss() {
                                if (Functions.getSharedPreference(MainActivity.this).getBoolean(Variables.IS_LOGIN, false)) {
                                    getUserProfileData();
                                } else {
                                    startTimer();
                                }
                            }
                        }).show(getSupportFragmentManager(), "");
            } else {
                if (Functions.getSharedPreference(MainActivity.this).getBoolean(Variables.IS_LOGIN, false)) {
                    getUserProfileData();
                } else {
                    startTimer();
                }
            }
        } else {
            if (Functions.getSharedPreference(MainActivity.this).getBoolean(Variables.IS_LOGIN, false)) {
                getUserProfileData();
            } else {
                startTimer();
            }
        }
    }

    private void getUserProfileData() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserProfile(Functions.getUID(this)).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if (userResponse != null) {
                    if (userResponse.code == SUCCESS) {
                        Functions.saveUserData(userResponse.getUserModel(), MainActivity.this);
                        startTimer();
                    } else {
                        new CustomeDialogFragment(
                                getString(R.string.alert),
                                userResponse.message,
                                DialogType.ERROR,
                                false,
                                false,
                                true,
                                new CustomeDialogFragment.DialogCallback() {
                                    @Override
                                    public void onCencel() {
                                    }

                                    @Override
                                    public void onSubmit() {
                                        viewPager.setCurrentItem(1);
                                    }

                                    @Override
                                    public void onDismiss() {
                                    }

                                    @Override
                                    public void onComplete(Dialog dialog) {
                                    }
                                }
                        ).show(getSupportFragmentManager(), "");
                    }
                }
            }
        });
    }

    private void startTimer() {
        if (Functions.getSharedPreference(MainActivity.this).getBoolean(Variables.IS_LOGIN, false)) {
            if (Functions.getSharedPreference(MainActivity.this).getString(Variables.NAME, "").equals("") || Functions.getSharedPreference(MainActivity.this).getString(Variables.NAME, "").equals("null")) {
                startActivity(new Intent(this, EditProfileActivity.class));
            } else if (Functions.getSharedPreference(MainActivity.this).getBoolean(Variables.IS_FIRST_TIME, true)) {
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                finish();
            } else {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        } else {
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class Adapter extends FragmentPagerAdapter {

        public Adapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SplashFragment();
                case 1:
//                    return new LoginFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}