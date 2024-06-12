package com.nirmiteepublic.clink.ui.activity.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityProfileBinding;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;

import java.util.Objects;

public class ProfileActivity extends PegaAppCompatActivity {

    ActivityProfileBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBackWithRightAnim();
        setWindowThemeThird();

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.name.setText(UserUtils.getUserFirstName() + " " + UserUtils.getUserLastName());
        binding.number.setText("+91 " + UserUtils.getUserNumber());

        binding.edit.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.profileImageContainer, Objects.requireNonNull(ViewCompat.getTransitionName(binding.profileImageContainer)));
            startActivity(new Intent(this, EditProfileActivity.class), options.toBundle());
        });
    }

    @Override
    public void onBackPressed() {
        binding.profileImageContainer.setTransitionName(null);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}