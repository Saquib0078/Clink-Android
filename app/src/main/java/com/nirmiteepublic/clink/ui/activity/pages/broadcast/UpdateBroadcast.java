package com.nirmiteepublic.clink.ui.activity.pages.broadcast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.databinding.ActivityUpdateBroadcastBinding;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String TAG = "UpdateBroadcast";
    ActivityUpdateBroadcastBinding binding;

    private Bitmap bitmap;
    private String broadcastID;
    private String type;
    private String description;
    private String broadcastUrl = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBroadcastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            broadcastID = intent.getStringExtra("broadcastId");
            boolean isImage = intent.getBooleanExtra("isImage", false);
            if (broadcastID != null && !broadcastID.isEmpty()) {
                Toast.makeText(this, "Broadcast ID: " + broadcastID, Toast.LENGTH_SHORT).show();
                getBroadcast(broadcastID);

                binding.publish.setOnClickListener(v -> UpdateBroadcasts(broadcastID));

                if (isImage) {
                    binding.image.setVisibility(View.VISIBLE);
                } else {
                    binding.image.setVisibility(View.GONE);

                    binding.video.setVisibility(View.VISIBLE);

                    binding.video.setVideoURI(Uri.parse(RetrofitClient.BROADCAST_URL_1 + broadcastID + "." + type)); // Replace videoUrl with your actual video URL
                    binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // Start playback when video is prepared
                            mp.setLooping(true); // Looping the video
                            binding.video.start();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "No broadcast ID provided", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No data provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void UpdateBroadcasts(String id) {
        if (id == null || id.isEmpty()) {
            Toast.makeText(this, "Invalid broadcast ID", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        MultipartBody.Part imagePart = null;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            imagePart = MultipartBody.Part.createFormData("media", "image.jpg", imageRequestBody);
        }

        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), binding.description.getText().toString());
        String broadcastUrl=binding.broadcastUrl.getText().toString();

        if (!PegaProgress.isValidUrl(broadcastUrl)) {
            // Handle invalid URL
            binding.broadcastUrl.setError("Please enter a valid URL");
            return;
        }
        RequestBody broadcastUrlPart = RequestBody.create(MediaType.parse("text/plain"),broadcastUrl );

        RetrofitClient.getInstance(this).getApiInterfaces().UpdateBroadcast(id, broadcastUrlPart, desc, imagePart).enqueue(new Callback<ResponseBody>() {
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

    private void getBroadcast(String id) {
        if (id == null || id.isEmpty()) {
            Toast.makeText(this, "Invalid broadcast ID", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getInstance(this).getApiInterfaces().getBroadcastById(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String jsonString = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonString);

                        String broadcastId = jsonObject.getString("_id");
                        broadcastID = jsonObject.getString("broadcastID");
                        String num = jsonObject.getString("num");
                        description = jsonObject.optString("description", ""); // Add this line
                        try {
                            broadcastUrl = jsonObject.getString("broadcastUrl");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int likes = jsonObject.getInt("likes");
                        int comments = jsonObject.getInt("comments");
                        String time = jsonObject.getString("time");
                        type = jsonObject.getString("type");

                        updateUI(broadcastId, broadcastID, num, description, likes, comments, time, type, broadcastUrl);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(UpdateBroadcast.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UpdateBroadcast.this, "Network error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(String broadcastId, String broadcastID, String num, String description, int likes, int comments, String time, String type, String broadcastUrl) {
        binding.description.setText(description);
        binding.broadcastUrl.setText(broadcastUrl);

        if ("jpg".equals(type) || "png".equals(type)) {
            Glide.with(this)
                    .load(RetrofitClient.BROADCAST_URL_1 + broadcastID + "." + type)
                    .into(binding.image);
            binding.image.setVisibility(View.VISIBLE);
        } else if ("mp4".equals(type)) {

            binding.image.setVisibility(View.GONE);

            binding.video.setVideoURI(Uri.parse(RetrofitClient.BROADCAST_URL_1 + broadcastID + "." + type)); // Replace videoUrl with your actual video URL
            binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true); // Looping the video
                    binding.video.start();
                }
            });
            // Set up your video view
        }

    }
}