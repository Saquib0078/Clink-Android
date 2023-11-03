package com.nirmiteepublic.clink.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.FragmentPersonalMeetingBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;

public class PersonalMeetingFragment extends PegaFragment {

  FragmentPersonalMeetingBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding=FragmentPersonalMeetingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}