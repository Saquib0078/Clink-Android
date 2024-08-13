package com.nirmiteepublic.clink.functions.viewmanagers;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.nirmiteepublic.clink.R;

public class PegaFragment extends Fragment {

    private int cyan;
    private int selection;

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cyan = ContextCompat.getColor(requireContext(), R.color.cyan_process);
    }

    public void setWindowCyan() {
        if (selection != 0) {
            selection = 0;
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            changeColor(ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#FFFFFF"), cyan));
        }
    }

    public void setWindowWhite() {
        if (selection == 0) {
            selection = 1;
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            changeColor(ValueAnimator.ofObject(new ArgbEvaluator(), cyan, Color.parseColor("#FFFFFF")));
        }
    }

    /**
     * Change the status bar color with animation
     */
    public void changeColor(ValueAnimator valueAnimator) {
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(animation -> requireActivity().getWindow().setStatusBarColor((int) animation.getAnimatedValue()));
        valueAnimator.start();
    }


    public void onPegaActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }


}
