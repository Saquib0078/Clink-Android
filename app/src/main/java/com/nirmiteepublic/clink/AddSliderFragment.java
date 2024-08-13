package com.nirmiteepublic.clink;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nirmiteepublic.clink.databinding.FragmentAddSliderBinding;

public class AddSliderFragment extends BottomSheetDialogFragment {
FragmentAddSliderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentAddSliderBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}