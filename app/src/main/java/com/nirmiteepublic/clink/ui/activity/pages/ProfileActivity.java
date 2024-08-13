package com.nirmiteepublic.clink.ui.activity.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityProfileBinding;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends PegaAppCompatActivity {
    private static final int EDIT_PROFILE_REQUEST = 70204;
    ActivityProfileBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UserUtils.init(this);

        setBackWithRightAnim();
        setWindowThemeThird();
        binding.back.setOnClickListener(v -> onBackPressed());

        updateProfileUI();

        binding.edit.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    binding.profileImageContainer,
                    Objects.requireNonNull(ViewCompat.getTransitionName(binding.profileImageContainer))
            );
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST, options.toBundle());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            updateProfileUI();
        }
    }

    private void updateProfileUI() {
        showProgressDialog();
        RetrofitClient.getInstance(this).getApiInterfaces().getUser(UserUtils.getUserNumber()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        JsonObject findNumObject = jsonObject.getAsJsonObject("data");

                        String fname = findNumObject.getAsJsonPrimitive("fName").getAsString();
                        String lname = findNumObject.getAsJsonPrimitive("lName").getAsString();
                        String num = findNumObject.has("num") ? findNumObject.getAsJsonPrimitive("num").getAsString() : "";
                        String Image = findNumObject.has("dp") ? findNumObject.getAsJsonPrimitive("dp").getAsString() : "";

                        UserUtils.setUserFirstName(fname);
                        UserUtils.setUserLastName(lname);
                        UserUtils.setUserNumber(num);
                        UserUtils.setUserDp(Image);

                        binding.name.setText(fname + " " + lname);
                        binding.number.setText("+91 " + num);

                        Glide.with(ProfileActivity.this)
                                .load(RetrofitClient.PROFILE_IMAGE + Image)
                                .placeholder(R.drawable.default_image)
                                .error(R.drawable.default_image)
                                .into(binding.profileImage);
                        hideProgressDialog();
                    } catch (IOException e) {
                        hideProgressDialog();
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideProgressDialog();
                    Toast.makeText(ProfileActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        binding.profileImageContainer.setTransitionName(null);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void showProgressDialog() {
        PegaProgress.showProgressBar(this);
    }

    private void hideProgressDialog() {
        PegaProgress.hideProgressBar();
    }
}