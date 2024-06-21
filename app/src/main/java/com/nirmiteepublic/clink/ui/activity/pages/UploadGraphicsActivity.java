package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.nirmiteepublic.clink.databinding.ActivityUploadGraphicsBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadGraphicsActivity extends AppCompatActivity {
    ActivityUploadGraphicsBinding binding;
    Bitmap bitmap;
    private PegaProgressDialog progressDialog;
    String SelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadGraphicsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        progressDialog = new PegaProgressDialog(this);
        binding.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                ConvertImage();
            }
        });

    }

    private void ConvertImage() {
        if (bitmap == null) {
            // Handle the case when bitmap is null (image is not present)
            // For example, you can directly call the API without including the image
            uploadDataWithoutImage();
            return;
        }

        // Convert bitmap to base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Create RequestBody for text data
        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.title.getText().toString());
        RequestBody TypeRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.type.getText().toString());
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), SelectedDate);

        // Create RequestBody for image data
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));

        // Create MultipartBody.Part for the image
        MultipartBody.Part imageID = MultipartBody.Part.createFormData("graphicModelList", "image.jpg", imageRequestBody);

        // Assuming you have a Retrofit instance named retrofit
        RetrofitClient.getInstance(this).getApiInterfaces().CreateGraphics(

                meetNameRequestBody,
                TypeRequestBody,
                date,
                imageID
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadGraphicsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String res = response.errorBody().string();
                        System.out.println(res);
                        binding.progressBar.setVisibility(View.GONE);
                    } catch (IOException e) {
                        binding.progressBar.setVisibility(View.GONE);
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(UploadGraphicsActivity.this, "Error" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(UploadGraphicsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to handle uploading data without an image
    private void uploadDataWithoutImage() {
        // Create RequestBody for text data
        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.title.getText().toString());
        RequestBody TypeRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.type.getText().toString());
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), SelectedDate);

        // Assuming you have a Retrofit instance named retrofit
        RetrofitClient.getInstance(this).getApiInterfaces().CreateGraphics(
                meetNameRequestBody, TypeRequestBody,date, null

        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadGraphicsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String res = response.errorBody().string();
                        System.out.println(res);
                        binding.progressBar.setVisibility(View.GONE);
                    } catch (IOException e) {
                        binding.progressBar.setVisibility(View.GONE);
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(UploadGraphicsActivity.this, "Error" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(UploadGraphicsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    binding.img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
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

    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(UploadGraphicsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Handle the selected date
                // You can update a TextView or do any other action here
                SelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                binding.btnDatePicker.setText(SelectedDate);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

}