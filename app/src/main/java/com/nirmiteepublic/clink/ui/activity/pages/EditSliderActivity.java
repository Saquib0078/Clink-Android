package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.EditSliderAdapter;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.SliderImageResponse;
import com.nirmiteepublic.clink.models.SlidersItem;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSliderActivity extends PegaAppCompatActivity {
    String sliderID;
    Bitmap bitmap;
    AlertDialog alertDialog;
    ImageView imageView;
    private PegaProgressDialog progressDialog;


    com.nirmiteepublic.clink.databinding.ActivityEditSliderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.nirmiteepublic.clink.databinding.ActivityEditSliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new PegaProgressDialog(this);

        setWindowThemeSecond();


// Inside your onCreate method or onViewCreated method for fragments
        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatButton uploadButton, addButton;
                // Inflate the custom layout for the AlertDialog
                View dialogView = LayoutInflater.from(EditSliderActivity.this).inflate(R.layout.add_slider_alertdialog, null);
                imageView = dialogView.findViewById(R.id.img);
                uploadButton = dialogView.findViewById(R.id.btnUploadImage);
                addButton = dialogView.findViewById(R.id.send);

                uploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncher.launch(intent);
                    }
                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgressDialog();
                        ConvertImage();

                    }
                });

                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(EditSliderActivity.this);
                builder.setView(dialogView);


                alertDialog = builder.create();
                alertDialog.show();
            }
        });


        RetrofitClient.getInstance(this).getApiInterfaces().getSlider().enqueue(new Callback<SliderImageResponse>() {
            @Override
            public void onResponse(Call<SliderImageResponse> call, retrofit2.Response<SliderImageResponse> response) {
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    List<String> sliderUrls = new ArrayList<>();

                    SliderImageResponse sliderResponse = response.body();
                    Gson gson = new Gson();

                    String jsonResponse = gson.toJson(sliderResponse);
                    System.out.println(jsonResponse);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonResponse);
                    } catch (JSONException e) {
                        Toast.makeText(EditSliderActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONArray slidersArray = jsonObject.getJSONArray("sliders");
                        for (int i = 0; i < slidersArray.length(); i++) {
                            JSONObject sliderObj = slidersArray.getJSONObject(i);
                            sliderID = sliderObj.getString("_id");
                            String image = sliderObj.getString("slider");
                            String imageUrl = RetrofitClient.BASE_URL + "graphics/getgraphics/" + image;
                            sliderUrls.add(image);
                        }
                    } catch (JSONException e) {
                        hideProgressDialog();
                        Toast.makeText(EditSliderActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        initImageSlider(new JSONArray(sliderUrls));
                    } catch (JSONException e) {
                        hideProgressDialog();
                        Toast.makeText(EditSliderActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SliderImageResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(EditSliderActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initImageSlider(JSONArray data) throws JSONException {
        List<SlidersItem> sliderImages = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            sliderImages.add(new SlidersItem(data.getString(i)));
        }

        EditSliderAdapter adapter = new EditSliderAdapter(sliderImages);
        binding.recview.setAdapter(adapter);
    }


    private void ConvertImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);


            // Create RequestBody for text data

            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));

            // Create MultipartBody.Part for the image
            MultipartBody.Part imageID = MultipartBody.Part.createFormData("slider", "image.jpg", imageRequestBody);

            // Assuming you have a Retrofit instance named retrofit
            RetrofitClient.getInstance(this).getApiInterfaces().CreateSlider(
                    imageID
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        hideProgressDialog();
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditSliderActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String res = response.errorBody().string();
                            System.out.println(res);
                            binding.progressBar.setVisibility(View.GONE);

                        } catch (IOException e) {
                            binding.progressBar.setVisibility(View.GONE);
                            hideProgressDialog();
                            Toast.makeText(EditSliderActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                        Toast.makeText(EditSliderActivity.this, "Error" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Toast.makeText(EditSliderActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    Toast.makeText(EditSliderActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }
    });


    private void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.ShowProgress(DialogData.UN_CANCELABLE); // Show the progress dialog
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss(); // Dismiss the progress dialog
        }
    }
}