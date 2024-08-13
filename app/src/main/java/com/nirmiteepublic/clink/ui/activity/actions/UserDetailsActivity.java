package com.nirmiteepublic.clink.ui.activity.actions;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.OptionsPagerAdapter;
import com.nirmiteepublic.clink.databinding.ActivityUserDetailsBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionRequestType;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsCallBackListener;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsRequestCallBackListener;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.utils.PegaAnimationUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.ui.fragments.OptionsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserDetailsActivity extends PegaAppCompatActivity {

    private final TextView[] dots = new TextView[4];
    private final JSONObject mainData = new JSONObject();
    ActivityUserDetailsBinding binding;
    private OptionsFragment optionsFragment;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeThird();
        setAsLastActivity();

        OptionsPagerAdapter optionsPagerAdapter = new OptionsPagerAdapter(this, new OptionsCallBackListener() {
            @Override
            public void onContinue(int index, JSONObject data) {
                addToMainData(data);
                if (binding.slideView.getCurrentItem() == 3) {
                    setData();
                    return;
                }
                binding.slideView.setCurrentItem((binding.slideView.getCurrentItem() + 1));
            }

            @Override
            public void onOptionsRequest(OptionRequestType type, String title, String preData, OptionsRequestCallBackListener optionsRequestCallBackListener) {
                openOptionsFragment(type, title, preData, optionsRequestCallBackListener);
            }
        });

        binding.continueNext.setOnClickListener(v -> optionsPagerAdapter.onNext(binding.slideView.getCurrentItem()));

        binding.slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.back.setVisibility(View.GONE);
                } else {
                    binding.back.setVisibility(View.VISIBLE);
                }
                /* Update the dots with current position */
                initDots(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        binding.slideView.setAdapter(optionsPagerAdapter);

        binding.slideView.setOnTouchListener((v, event) -> true);
        binding.slideView.setOffscreenPageLimit(4);

        binding.back.setOnClickListener(v -> onBackPressed());

        initDots(0);

    }

    private void setData() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), mainData.toString());
        RetrofitClient.getInstance(this).getApiInterfaces().setUserInfo(requestBody).enqueue(new PegaResponseManager(new PegaCallback(this) {
            @Override
            public void onSuccess(@Nullable JSONObject data) {
                PegaAnimationUtils.showToast(getActivity(), "Your Details Saved Successfully");
                reopenApp();
            }
        }));
    }

    private void addToMainData(JSONObject data) {
        try {
            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                mainData.put(key, data.getString(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void openOptionsFragment(OptionRequestType type, String title, String preData, OptionsRequestCallBackListener optionsRequestCallBackListener) {
        optionsFragment = new OptionsFragment(type, title, preData, optionsRequestCallBackListener);
        binding.fragmentContainer.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragmentContainer, optionsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        if (optionsFragment != null && optionsFragment.isResumed() && optionsFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).remove(optionsFragment).commit();
            return;
        }
        if (binding.slideView.getCurrentItem() != 0) {
            binding.slideView.setCurrentItem((binding.slideView.getCurrentItem() - 1));
            return;
        }
        super.onBackPressed();
    }

    /**
     * This function manages the dots & set the fot blue of given position
     */
    private void initDots(int position) {
        binding.dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(HtmlCompat.fromHtml("&#8226", HtmlCompat.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(55);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.fade));
            binding.dots.addView(dots[i]);
        }
        dots[position].setTextColor(ContextCompat.getColor(this, R.color.royal_blue));
    }
}