package com.nirmiteepublic.clink.ui.activity.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ActivityEditImageBinding;

public class EditImageActivity extends AppCompatActivity {

    ActivityEditImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();

//        String selectedImage = intent.getStringExtra("selectedImage");
//        Glide.with(this)
//                .load(selectedImage)
//                .into(binding.image);

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditImageActivity.this, SelectedImageActivity.class));
            }
        });

        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}