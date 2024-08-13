package com.visticsolution.posterbanao.navfragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import com.visticsolution.posterbanao.PremiumActivity;
import com.visticsolution.posterbanao.TransactionActivity;
import com.visticsolution.posterbanao.account.EditProfileActivity;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.MyPostsActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.WebviewA;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.databinding.FragmentProfileBinding;
import com.visticsolution.posterbanao.fragments.MyBussinessFragment;
import com.visticsolution.posterbanao.fragments.ReferFragment;
import com.visticsolution.posterbanao.fragments.WithdrawFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {

    }

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        Functions.fadeIn(binding.getRoot(), getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.usernameTv.setText(Functions.getSharedPreference(getContext()).getString(Variables.NAME,""));
        binding.emailTv.setText(Functions.getSharedPreference(getContext()).getString(Variables.U_EMAIL,""));

        try {
            BindingAdaptet.setImageUrl(binding.userPic,Functions.getSharedPreference(getContext()).getString(Variables.P_PIC,""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),EditProfileActivity.class));
            }
        });
        binding.walletTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TransactionActivity.class));
            }
        });
        binding.myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyPostsActivity.class));
            }
        });
        binding.activeBusinessName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyBussinessFragment().show(getChildFragmentManager(),"");
            }
        });

        if(Functions.getSharedPreference(getContext()).getString("refer_earn","").equals("false")){
            binding.referBtn.setVisibility(View.GONE);
        }
        binding.referBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReferFragment();
            }
        });

        binding.withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWithdrawFragment();
            }
        });

        binding.rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.rateApp(getContext());
            }
        });
        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebviewA.class);
                intent.putExtra("title", getString(R.string.privacy_policy));
                startActivity(intent);
            }
        });
        binding.termsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebviewA.class);
                intent.putExtra("title", getString(R.string.terms_service));
                startActivity(intent);
            }
        });

        binding.unlockBtn.setOnClickListener(v ->{
            showPremiumFragment();
        });

        binding.logOut.setOnClickListener(v ->{
            Functions.logOut(getActivity());
        });

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(
                animate("scaleX", 0.65f, 1),
                animate("scaleY", 0.65f, 1),
                animate("alpha", 0, 1)
        );
        mAnimatorSet.start();
    }

    private ObjectAnimator animate(String style, float... values) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.unlockBtn, style, values).setDuration(1200);
        objectAnimator.setInterpolator(new BounceInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animation.setStartDelay(1000);
                animation.start();
            }
        });
        return objectAnimator;
    }

    private void showPremiumFragment() {
        startActivity(new Intent(getContext(), PremiumActivity.class));
    }

    private void showWithdrawFragment() {
        WithdrawFragment comment_f = new WithdrawFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(android.R.id.content, comment_f).commit();
    }

    private void showReferFragment() {
        ReferFragment comment_f = new ReferFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(android.R.id.content, comment_f).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.activeBusinessName.setText(Functions.getSharedPreference(getContext()).getString(Variables.ACTIVE_PROFILE_NAME,getString(R.string.add_bussiness)));
        binding.usernameTv.setText(Functions.getSharedPreference(getContext()).getString(Variables.NAME,""));
        binding.emailTv.setText(Functions.getSharedPreference(getContext()).getString(Variables.U_EMAIL,""));
        try {
            BindingAdaptet.setImageUrl(binding.userPic,Functions.getSharedPreference(getContext()).getString(Variables.P_PIC,""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Functions.IsPremiumEnable(getContext())) {
            binding.premiumProgressLay.setVisibility(View.VISIBLE);
            binding.premiumBuyLay.setVisibility(View.GONE);
            binding.planName.setText("Plan - "+Functions.getSharedPreference(getContext()).getString(Variables.SUB_NAME,""));
            binding.startDate.setText(Functions.getSharedPreference(getContext()).getString(Variables.SUB_DATE,""));
            binding.endDate.setText(Functions.getSharedPreference(getContext()).getString(Variables.SUB_END_DATE,""));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date startDate = df.parse(Functions.getSharedPreference(getContext()).getString(Variables.SUB_DATE,""));
                Date endDate = df.parse(Functions.getSharedPreference(getContext()).getString(Variables.SUB_END_DATE,""));
                binding.durationProgress.setProgress(Functions.getSubsIntervel(startDate,endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            binding.premiumProgressLay.setVisibility(View.GONE);
            binding.premiumBuyLay.setVisibility(View.VISIBLE);
        }
    }
}