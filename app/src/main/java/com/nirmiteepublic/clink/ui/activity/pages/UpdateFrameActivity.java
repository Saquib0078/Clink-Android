package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityUpdateFrameBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;

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

public class UpdateFrameActivity extends AppCompatActivity {
    ActivityUpdateFrameBinding binding;
    String FrameAdd;
    String FrameName;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateFrameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.name.setText(UserUtils.getFRAMENAME());
        binding.address.setText(UserUtils.getFRAMEADD());
        Glide.with(this)
                .load(UserUtils.getUserDp())
                .placeholder(R.drawable.default_image) // Set placeholder image resource
                .into(binding.profileImage);
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
        binding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConvertImage();
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });


    }


    private void ConvertImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // Create RequestBody for text data
            String name = binding.name.getText().toString();
            String add = binding.address.getText().toString();

            RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), add);

            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));

            // Create MultipartBody.Part for the image
            MultipartBody.Part imageID = MultipartBody.Part.createFormData("Image", "image.jpg", imageRequestBody);
            Toast.makeText(this, ""+UserUtils.getUserId(), Toast.LENGTH_SHORT).show();
            // Assuming you have a Retrofit instance named retrofit
            RetrofitClient.getInstance(this).getApiInterfaces().updateFrame(
                    UserUtils.getUserId(),
                    meetNameRequestBody,
                    meetDescriptionRequestBody,
                    imageID
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        getuser();
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(UpdateFrameActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String res = response.errorBody().string();
                            System.out.println(res);
                            binding.progressBar.setVisibility(View.GONE);

                        } catch (IOException e) {
                            binding.progressBar.setVisibility(View.GONE);

                            throw new RuntimeException(e);
                        }

                        Toast.makeText(UpdateFrameActivity.this, "Error" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(UpdateFrameActivity.this, "" + t.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    binding.profileImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    });

    private void getuser() {
        RetrofitClient.getInstance(this).getApiInterfaces().getUser(UserUtils.getUserNumber()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        JSONObject dataObject = jsonObject.getJSONObject("user"); // Access the "data" object

                        String fName = dataObject.optString("fName");
                        String dp = dataObject.optString("fName");

                        String Image = dataObject.optString("Image");
                        FrameName = dataObject.optString("FrameName");
                        FrameAdd = dataObject.optString("FrameAdd");
                        UserUtils.setFRAMEADD(FrameAdd);
                        UserUtils.setFRAMENAME(FrameName);
                        UserUtils.setUserDp(RetrofitClient.USER_BASE_URL + "getUsermedia/" + Image);

                    } catch (IOException e) {
                        Toast.makeText(UpdateFrameActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(UpdateFrameActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UpdateFrameActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}