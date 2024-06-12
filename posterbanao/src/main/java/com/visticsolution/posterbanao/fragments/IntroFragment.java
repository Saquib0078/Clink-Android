package com.visticsolution.posterbanao.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visticsolution.posterbanao.R;

public class IntroFragment extends Fragment {


    String posi = "0";
    public IntroFragment(String s) {
        posi = s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (posi.equals("1")){
            view.findViewById(R.id.position_first).setVisibility(View.VISIBLE);
        }else if (posi.equals("2")){
            view.findViewById(R.id.position_second).setVisibility(View.VISIBLE);
        }else if (posi.equals("3")){
            view.findViewById(R.id.position_third).setVisibility(View.VISIBLE);
        }
    }
}