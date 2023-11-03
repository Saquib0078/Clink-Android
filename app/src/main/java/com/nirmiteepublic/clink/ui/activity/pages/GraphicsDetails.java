package com.nirmiteepublic.clink.ui.activity.pages;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ActivityGraphicsDetailsBinding;

public class GraphicsDetails extends AppCompatActivity {
    ActivityGraphicsDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphicsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String imageUrl = getIntent().getStringExtra("image_url");
        Glide.with(this)
                .load(imageUrl)
                .into(binding.imageview);



    }
}