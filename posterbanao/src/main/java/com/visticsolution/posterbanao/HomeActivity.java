package com.visticsolution.posterbanao;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.visticsolution.posterbanao.account.EditProfileActivity;
import com.visticsolution.posterbanao.account.LoginModel;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.ads.InterstitialAdapter;
import com.visticsolution.posterbanao.databinding.ActivityHomeBinding;
import com.visticsolution.posterbanao.model.UserModel;
import com.visticsolution.posterbanao.navfragment.BrandingFragment;
import com.visticsolution.posterbanao.navfragment.CreateFragment;
import com.visticsolution.posterbanao.navfragment.HomeFragment;
import com.visticsolution.posterbanao.navfragment.ProfileFragment;
import com.visticsolution.posterbanao.navfragment.GreetingFragment;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static String action, action_item;
    Activity context;
    InterstitialAdapter interstitialAdapter;
    ActivityHomeBinding binding;
    UserViewModel userViewModel;
    boolean isHome = true;
    String user_id;
    String db;
    String phone;
    String fName;
    String lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.setLocale(Functions.getSharedPreference(this).getString(Variables.APP_LANGUAGE_CODE, Variables.DEFAULT_LANGUAGE_CODE), this, HomeActivity.class, false);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        context = this;
        getIntentData();
        Log.d("TAG____>", "insertLoginData: before login data" + Functions.getUID(this));
        if (!Functions.isLogin(this)) {
            insertLoginData();
        } else showFragmentOnFrame(new HomeFragment(true));
        interstitialAdapter = new InterstitialAdapter(this);


        setupTabIcons();
        checkNotificationIntent();

        try {
            Glide.with(context).load(R.drawable.acreate).into(binding.createImg);
        } catch (Exception e) {
        }
    }

    private void getIntentData() {
        Intent intentData = getIntent();
        user_id = intentData.getStringExtra("user_id");
        db = intentData.getStringExtra("profile_picture");
        phone = intentData.getStringExtra("phone");
        fName = intentData.getStringExtra("fName");
        lName = intentData.getStringExtra("lName");
    }

    private void checkNotificationIntent() {
        try {
            if (action != null) {
                if (action.equals(Constants.CATEGORY)) {
                    context.startActivity(new Intent(context, OpenPostActivity.class).putExtra("title", getString(R.string.app_name)).putExtra("type", "category").putExtra("item_id", action_item));
                } else if (action.equals(Constants.SUBSCRIPTION)) {
                    startActivity(new Intent(context, PremiumActivity.class));
                } else if (action.equals(Constants.URL)) {
                    Intent intent = new Intent(context, WebviewA.class);
                    intent.putExtra("url", action_item);
                    intent.putExtra("title", getString(R.string.app_name));
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
        }

    }

    private void showAddBusinessLanguage() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_user_type);
        dialog.setCancelable(false);
//        dialog.getWindow().setAttributes(getLayoutParams(dialog));
        TextView businessBtn = dialog.findViewById(R.id.businessBtn);
        TextView politicalBtn = dialog.findViewById(R.id.politicalBtn);
        TextView skipBtn = dialog.findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(view -> {

        });
        businessBtn.setOnClickListener(view -> {
            dialog.dismiss();
//            showCategoryFragment("business");
        });
        politicalBtn.setOnClickListener(view -> {
            dialog.dismiss();
//            showCategoryFragment("political");
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupTabIcons() {
        binding.homeBtn.setOnClickListener(this);
        binding.searchBtn.setOnClickListener(this);
        binding.createImg.setOnClickListener(this);
        binding.premiumBtn.setOnClickListener(this);
        binding.profileBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (isHome) {
            super.onBackPressed();
        } else {
            binding.homeBtn.performClick();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (interstitialAdapter != null) {
            if (InterstitialAdapter.isLoaded()) {
                InterstitialAdapter.showAds();
            } else {
                InterstitialAdapter.LoadAds();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.home_btn) {
            isHome = true;
            binding.homeBtn.getBackground().setTintList(null);
            binding.searchBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.createBtn.getBackground().setTint(getResources().getColor(R.color.graycolor));
            binding.premiumBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.profileBtn.getBackground().setTint(getResources().getColor(R.color.transparent));

            setActiveColor(binding.homeImg, binding.homeTxt);
            setDeactiveColor(binding.greetingImg, binding.greetingTv);
            setDeactiveColor(binding.brandingImg, binding.brandingTv);
            setDeactiveColor(binding.profileImg, binding.profileTv);

            showFragmentOnFrame(new HomeFragment(false));
        } else if (id == R.id.search_btn) {
            isHome = false;

            binding.homeBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.searchBtn.getBackground().setTintList(null);
            binding.createBtn.getBackground().setTint(getResources().getColor(R.color.graycolor));
            binding.premiumBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.profileBtn.getBackground().setTint(getResources().getColor(R.color.transparent));

            setDeactiveColor(binding.homeImg, binding.homeTxt);
            setActiveColor(binding.greetingImg, binding.greetingTv);
            setDeactiveColor(binding.brandingImg, binding.brandingTv);
            setDeactiveColor(binding.profileImg, binding.profileTv);

            showFragmentOnFrame(new GreetingFragment());
        } else if (id == R.id.createImg) {
            isHome = false;

            binding.homeBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.searchBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.createBtn.getBackground().setTint(getResources().getColor(R.color.app_color));
            binding.premiumBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.profileBtn.getBackground().setTint(getResources().getColor(R.color.transparent));


            setDeactiveColor(binding.homeImg, binding.homeTxt);
            setDeactiveColor(binding.greetingImg, binding.greetingTv);
            setDeactiveColor(binding.brandingImg, binding.brandingTv);
            setDeactiveColor(binding.profileImg, binding.profileTv);

            showFragmentOnFrame(new CreateFragment());
        } else if (id == R.id.premium_btn) {
            isHome = false;

            binding.homeBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.searchBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.createBtn.getBackground().setTint(getResources().getColor(R.color.graycolor));
            binding.premiumBtn.getBackground().setTintList(null);
            binding.profileBtn.getBackground().setTint(getResources().getColor(R.color.transparent));

            setDeactiveColor(binding.homeImg, binding.homeTxt);
            setDeactiveColor(binding.greetingImg, binding.greetingTv);
            setActiveColor(binding.brandingImg, binding.brandingTv);
            setDeactiveColor(binding.profileImg, binding.profileTv);

            showFragmentOnFrame(new BrandingFragment());
        } else if (id == R.id.profile_btn) {
            isHome = false;

            binding.homeBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.searchBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.createBtn.getBackground().setTint(getResources().getColor(R.color.editor_controller_bg));
            binding.premiumBtn.getBackground().setTint(getResources().getColor(R.color.transparent));
            binding.profileBtn.getBackground().setTintList(null);

            setDeactiveColor(binding.homeImg, binding.homeTxt);
            setDeactiveColor(binding.greetingImg, binding.greetingTv);
            setDeactiveColor(binding.brandingImg, binding.brandingTv);
            setActiveColor(binding.profileImg, binding.profileTv);

            showFragmentOnFrame(new ProfileFragment());
        }
    }

    private void setActiveColor(ImageView imageView, TextView textView) {
        textView.setTextColor(getColor(R.color.white));
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void setDeactiveColor(ImageView imageView, TextView textView) {
        textView.setTextColor(getColor(R.color.textColor));
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.textColor), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void showFragmentOnFrame(Fragment fragment) {
        try {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_framelayout, fragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Functions.showToast(context, "Functions.showToast(context,Error! Can't replace fragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void insertLoginData() {
        Log.d("TAG____>", "insertLoginData: inside login data");
        LoginModel loginmodel = new LoginModel();
        Functions.showLoader(context);
        if (!loginmodel.lname.equals("")) {
            loginmodel.fname = loginmodel.fname + " " + loginmodel.lname;
        }

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("social_id", "phone");
            parameters.put("social", "phone");
            parameters.put("user_id", user_id);
            parameters.put("auth_token", loginmodel.authTokon);
            parameters.put("email", loginmodel.email);
            parameters.put("number", phone);
            parameters.put("profile_pic", db);
            parameters.put("name", fName);
//            parameters.put("device_token", Functions.getDeviceToken(context));
            parameters.put("device_token", "1234");
        } catch (Exception e) {
            Log.d("TAG____>", "insertLoginData: inside login data parameter exception");
            e.printStackTrace();
        }

        userViewModel.login(parameters).observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {

                if (userResponse != null) {
                    Log.d("TAG____>", "insertLoginData: inside response" + userResponse.message);

                    Functions.cancelLoader();
                    if (userResponse.code == Constants.SUCCESS) {
                        parseLoginData(userResponse.userModel);
                        showFragmentOnFrame(new HomeFragment(true));
                    } else {
                        Functions.showToast(context, userResponse.message);
                    }
                } else {
                    Log.d("TAG____>", "insertLoginData: inside response is null" + userResponse.message);

                }
            }
        });
    }


    public void parseLoginData(UserModel userModel) {
        Functions.saveUserData(userModel, context);
    }
}