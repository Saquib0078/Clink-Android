package com.nirmiteepublic.clink.ui.activity.pages.broadcast;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;

public class FullImageActivity extends PegaAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        setWindowThemeBlack();

        ImageView fullImageView = findViewById(R.id.fullImageView);
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .fitCenter()
                    .into(fullImageView);
        }
    }
}