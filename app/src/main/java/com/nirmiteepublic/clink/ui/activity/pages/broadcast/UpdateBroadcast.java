package com.nirmiteepublic.clink.ui.activity.pages.broadcast;

import static com.visticsolution.posterbanao.editor.View.StickerView.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ActivityUpdateBroadcastBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBroadcast extends AppCompatActivity {
    ActivityUpdateBroadcastBinding binding;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBroadcastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        if (intent != null) {
            String desc = intent.getStringExtra("desc");
            String type = intent.getStringExtra("type");
            String broadcast = intent.getStringExtra("broadcastId");
            boolean isImage = intent.getBooleanExtra("isImage", false);
            Toast.makeText(this, "" + broadcast, Toast.LENGTH_SHORT).show();
            binding.description.setText(desc);
            binding.publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateBroadcasts(broadcast);

                }
            });

            if (isImage) {
                Glide.with(this)
                        .load(RetrofitClient.BROADCAST_URL_1 + "/" + broadcast + "." + type)
                        .into(binding.image);
                Toast.makeText(this, "" + RetrofitClient.BROADCAST_URL_1 + broadcast + "." + type, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Video", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void UpdateBroadcasts(String id) {
        MultipartBody.Part imagePart = null;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            imagePart = MultipartBody.Part.createFormData("media", "image.jpg", imageRequestBody);
        }

        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), binding.description.getText().toString());

        RetrofitClient.getInstance(this).getApiInterfaces().UpdateBroadcast(id,desc,imagePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateBroadcast.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Update failed", t);
                Toast.makeText(UpdateBroadcast.this, "Update failed: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleErrorResponse(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                Log.e(TAG, "Error response: " + errorBody);
                Toast.makeText(UpdateBroadcast.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Error reading error body", e);
                Toast.makeText(UpdateBroadcast.this, "Error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(UpdateBroadcast.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }


}