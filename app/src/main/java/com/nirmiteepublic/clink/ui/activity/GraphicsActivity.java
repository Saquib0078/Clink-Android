package com.nirmiteepublic.clink.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nirmiteepublic.clink.databinding.ActivityGraphicsBinding;

public class GraphicsActivity extends AppCompatActivity {

    ActivityGraphicsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphicsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}