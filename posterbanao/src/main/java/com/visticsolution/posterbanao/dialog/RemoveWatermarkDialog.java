package com.visticsolution.posterbanao.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.FragmentRemoveWatermarkDialogBinding;


public class RemoveWatermarkDialog extends BottomSheetDialogFragment {

    public interface OnClickItem{
        void watchVideAd();
        void buySinglePost();
        void goPremium();
    }

    OnClickItem onClickItem;
    public RemoveWatermarkDialog(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }


    FragmentRemoveWatermarkDialogBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRemoveWatermarkDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Functions.getSharedPreference(getContext()).getString("buy_singal_post","").equals("false")){
            binding.buySinglePost.setVisibility(View.GONE);
        }
        if (Functions.getSharedPreference(getContext()).getString("watch_and_remove_watermark","").equals("false")){
            binding.watchVideoAd.setVisibility(View.GONE);
        }

        if (Functions.getSharedPreference(getContext()).getString("show_ads","").equals("false")){
            binding.watchVideoAd.setVisibility(View.GONE);
        }

        if (Functions.getSharedPreference(getContext()).getString("show_admob_rewarded","").equals("false")){
            binding.watchVideoAd.setVisibility(View.GONE);
        }

        binding.watchVideoAd.setOnClickListener(v ->{
            onClickItem.watchVideAd();
            dismiss();
        });
        binding.buySinglePost.setOnClickListener(v ->{
            onClickItem.buySinglePost();
            dismiss();
        });
        binding.goPremium.setOnClickListener(v ->{
            onClickItem.goPremium();
            dismiss();
        });
    }
}