package com.nirmiteepublic.clink.ui.activity.pages.broadcast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityPublishBroadcastBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaCallback;
import com.nirmiteepublic.clink.functions.retrofit.res.PegaResponseManager;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PublishBroadcastActivity extends PegaAppCompatActivity {

    ActivityPublishBroadcastBinding binding;
    private Uri uri;
    private String filePath;
    private boolean isVideo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublishBroadcastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setWindowThemeThird();
        setBackWithRightAnim();

        binding.back.setOnClickListener(v -> onBackPressed());

        if (!getIntent().hasExtra("uri")) {
            finish();
            return;
        }

        loadData();
        loadMedia();

        binding.publish.setOnClickListener(v -> {
            String description = binding.description.getText().toString().trim();
            if (description.isEmpty()) {
                Toast.makeText(this, "Please give a description about this Broadcast", Toast.LENGTH_SHORT).show();
                return;
            }

            showProgressDialog();
            publishBroadcast(description);
        });

    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Publishing Broadcast...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void publishBroadcast(String description) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("media", file.getName(), requestFile);

        RequestBody descriptionPart = RequestBody.create(MediaType.parse("multipart/form-data"), description);

        RetrofitClient.getInstance(this).getApiInterfaces()
                .publishBroadcast("broadcasts", filePart, descriptionPart)
                .enqueue(new PegaResponseManager(new PegaCallback(this) {
                    @Override
                    public void onSuccess(@Nullable JSONObject data) {
                        dismissProgressDialog();
                        Toast.makeText(PublishBroadcastActivity.this, "Broadcast Published Successfully", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, new Intent().putExtra("status", true));
                        finish();
                    }

                }));
    }

    private void loadData() {
        uri = Uri.parse(getIntent().getStringExtra("uri"));
        filePath = getIntent().getStringExtra("filePath");
        isVideo = getIntent().getStringExtra("type").startsWith("video");
    }

    private void loadMedia() {
        if (!isVideo) {
            binding.image.setVisibility(View.VISIBLE);
            Glide.with(this).load(uri).placeholder(R.drawable.round_image_placeholder).into(binding.image);
        } else {
            binding.image.setVisibility(View.GONE);
            binding.video.setVisibility(View.VISIBLE);
            binding.video.setVideoURI(uri);
            binding.video.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                binding.video.start();
            });
        }
    }
}
