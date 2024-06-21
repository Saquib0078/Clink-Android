package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityCreateTaskBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskActivity extends AppCompatActivity {
    Bitmap bitmap;
    String SelectedDate;
    String SelectedTime;
    String radioButtonValue;
    byte[] byteArray;
    ActivityCreateTaskBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.radiogrp.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.limited) {
                radioButtonValue = "Limited Users";
                binding.invite.setVisibility(View.VISIBLE);
//                binding.selectedUsers.setVisibility(View.VISIBLE);
                binding.invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.invite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(CreateTaskActivity.this, FilterUserActivity.class);
                                intent.putExtra("taskActivity", true);
                                intent.putExtra("taskType", "task");
                                intent.putExtra("title", binding.editMeetName.getText().toString());
                                intent.putExtra("body", binding.editMeetDesc.getText().toString());
                                intent.putExtra("imageUrl", byteArray);
                                startActivity(intent);
                            }
                        });
                    }
                });
            } else if (checkedId == R.id.openall) {
                radioButtonValue = "Open for All";
                binding.invite.setVisibility(View.GONE);

            } else {
                radioButtonValue = "Default Value";
            }
        });

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
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                ConvertImage();
                finish();
            }
        });

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });


        binding.btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

    }

    private void ConvertImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
             byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // Create RequestBody for text data
            RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.editMeetName.getText().toString());
            RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.editMeetDesc.getText().toString());
            RequestBody dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), SelectedDate);
            RequestBody timeRequestBody = RequestBody.create(MediaType.parse("text/plain"), SelectedTime);
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
            RequestBody radioRequestBody = RequestBody.create(MediaType.parse("text/plain"), radioButtonValue);

            // Create MultipartBody.Part for the image
            MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageID", "image.jpg", imageRequestBody);

            // Assuming you have a Retrofit instance named retrofit
            RetrofitClient.getInstance(this).getApiInterfaces().publishTask(
                    meetNameRequestBody,
                    meetDescriptionRequestBody,
                    dateRequestBody,
                    timeRequestBody,
                    radioRequestBody,
                    imageID
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                      if (response.isSuccessful()){
                          binding.progressBar.setVisibility(View.GONE);
                          Toast.makeText(CreateTaskActivity.this, "Task Uploaded", Toast.LENGTH_SHORT).show();
                      }else {
                          binding.progressBar.setVisibility(View.GONE);

                          Toast.makeText(CreateTaskActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                      }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(CreateTaskActivity.this, "Upload Failed"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void showTimePicker() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog and show it
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Convert 24-hour format to 12-hour format with AM/PM
                String amPm;
                if (hourOfDay >= 12) {
                    amPm = "PM";
                    hourOfDay -= 12;
                } else {
                    amPm = "AM";
                }

                // Adjust 12-hour format when hour is 0
                if (hourOfDay == 0) {
                    hourOfDay = 12;
                }

                SelectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm);
                binding.btnTimePicker.setText(SelectedTime);
            }
        }, hourOfDay, minute, false);

        timePickerDialog.show();

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
}