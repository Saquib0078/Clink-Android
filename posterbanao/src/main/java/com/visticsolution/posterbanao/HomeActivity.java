package com.visticsolution.posterbanao;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.ads.InterstitialAdapter;
import com.visticsolution.posterbanao.databinding.ActivityHomeBinding;
import com.visticsolution.posterbanao.navfragment.BrandingFragment;
import com.visticsolution.posterbanao.navfragment.CreateFragment;
import com.visticsolution.posterbanao.navfragment.HomeFragment;
import com.visticsolution.posterbanao.navfragment.ProfileFragment;
import com.visticsolution.posterbanao.navfragment.GreetingFragment;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Activity context;
    InterstitialAdapter interstitialAdapter;
    ActivityHomeBinding binding;

    public static String action,action_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.setLocale(Functions.getSharedPreference(this).getString(Variables.APP_LANGUAGE_CODE,Variables.DEFAULT_LANGUAGE_CODE), this, HomeActivity.class,false);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        interstitialAdapter = new InterstitialAdapter(this);
        context = this;
        showFragmentOnFrame(new HomeFragment(true));
        setupTabIcons();
        checkNotificationIntent();

        try {
            Glide.with(context).load(R.drawable.acreate).into(binding.createImg);
        }catch (Exception e){}
    }

    private void checkNotificationIntent() {
        try {
            if (action != null){
                if (action.equals(Constants.CATEGORY)){
                    context.startActivity(new Intent(context, OpenPostActivity.class).putExtra("title",getString(R.string.app_name)).putExtra("type","category").putExtra("item_id",action_item));
                }else if (action.equals(Constants.SUBSCRIPTION)){
                    startActivity(new Intent(context, PremiumActivity.class));
                }else if (action.equals(Constants.URL)){
                    Intent intent = new Intent(context, WebviewA.class);
                    intent.putExtra("url", action_item);
                    intent.putExtra("title", getString(R.string.app_name));
                    startActivity(intent);
                }
            }
        }catch (Exception e){}

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


    boolean isHome = true;
    @Override
    public void onBackPressed() {
        if (isHome){
            super.onBackPressed();
        }else {
            binding.homeBtn.performClick();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (interstitialAdapter != null){
            if (InterstitialAdapter.isLoaded()){
                InterstitialAdapter.showAds();
            }else{
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
            Functions.showToast(context,"Functions.showToast(context,Error! Can't replace fragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}