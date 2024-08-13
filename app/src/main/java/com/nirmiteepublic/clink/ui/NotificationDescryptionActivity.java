package com.nirmiteepublic.clink.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;

public class NotificationDescryptionActivity extends PegaAppCompatActivity {
    com.nirmiteepublic.clink.databinding.ActivityNotificationDescryptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.nirmiteepublic.clink.databinding.ActivityNotificationDescryptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeSecond();

        Intent intent = getIntent();
        if (intent != null) {
            String header = intent.getStringExtra("header");
            String imageUrl = intent.getStringExtra("imageUrl");
            String body = intent.getStringExtra("body");
            String title = intent.getStringExtra("title");

            if (header == null || header.equals("normal")) {
                binding.headerTitle.setText("General Notification");
            } else if(header == null || header.equals("meet")){
                binding.headerTitle.setText("Meeting");
            }else if(header == null || header.equals("task")){
                binding.headerTitle.setText("Task");
            }

            if (body != null) {
                binding.descryption.setText(body);
            }

            if (title != null) {
                binding.title.setText(title);
            }

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(binding.bannerimage);
            }
        }

    }
}