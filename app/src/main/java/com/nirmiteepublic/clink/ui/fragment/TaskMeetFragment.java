package com.nirmiteepublic.clink.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirmiteepublic.clink.adapters.pagers.TaskMeetAdapter;
import com.nirmiteepublic.clink.adapters.pagers.ViewPagerAdapter;
import com.nirmiteepublic.clink.databinding.FragmentTaskMeetBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.TaskMeetModel;

import java.util.ArrayList;
import java.util.List;


public class TaskMeetFragment extends PegaFragment {
    FragmentTaskMeetBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskMeetBinding.inflate(inflater, container, false);
        ViewPagerAdapter adapter=new ViewPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}