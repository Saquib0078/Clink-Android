package com.nirmiteepublic.clink.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.nirmiteepublic.clink.adapters.pagers.ViewPagerAdapter;
import com.nirmiteepublic.clink.databinding.FragmentMeetBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;

public class MeetFragment extends PegaFragment {

    FragmentMeetBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMeetBinding.inflate(inflater, container, false);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);


        return binding.getRoot();
    }
}