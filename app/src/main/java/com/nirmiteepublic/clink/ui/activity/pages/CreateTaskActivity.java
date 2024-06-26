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
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityCreateTaskBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.Task;
import com.nirmiteepublic.clink.models.TaskModel;
import com.nirmiteepublic.clink.models.TaskModelResponse;

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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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
import retrofit2.HttpException;
import retrofit2.Response;

public class CreateTaskActivity extends AppCompatActivity {
    private static final String TAG = "CreateTaskActivity";
    Bitmap bitmap;
    String SelectedDate;
    String SelectedTime;
    String radioButtonValue = "Open for All"; // Default value
    ActivityCreateTaskBinding binding;

    boolean EditTask;
    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            EditTask = intent.getBooleanExtra("EDITTASK", false);
            taskId = intent.getStringExtra("TaskId");
        }

        if (EditTask) {
            binding.radiogrp.setVisibility(View.GONE);
            loadExistingTaskData();
        }

        setupRadioGroup();
        setupImageUpload();
        setupDateTimePickers();
        setupUploadButton();
    }

    private void loadExistingTaskData() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_LONG).show();
            return;
        }

        RetrofitClient.getInstance(this).getApiInterfaces().getTaskById(taskId).enqueue(new Callback<TaskModelResponse>() {
            @Override
            public void onResponse(Call<TaskModelResponse> call, Response<TaskModelResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    Task taskModel = response.body().getTask();
                    binding.editMeetDesc.setText(taskModel.getTaskDescription());
                    binding.editMeetName.setText(taskModel.getTaskName());
                    binding.btnDatePicker.setText(taskModel.getTime());
                    binding.btnTimePicker.setText(taskModel.getDate());
                    Glide.with(CreateTaskActivity.this)
                            .load(RetrofitClient.TASKIMAGE_BASE_URL + taskModel.getImageID())
                            .into(binding.img);
                } else {
                    Toast.makeText(CreateTaskActivity.this, "Failed to load task data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskModelResponse> call, Throwable t) {
                Log.e(TAG, "Failed to load task data", t);
                Toast.makeText(CreateTaskActivity.this, "Failed to load task data: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRadioGroup() {
        binding.radiogrp.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.limited) {
                radioButtonValue = "Limited Users";
                binding.invite.setVisibility(View.VISIBLE);
                binding.invite.setOnClickListener(this::handleInviteClick);
            } else {
                radioButtonValue = "Open for All";
                binding.invite.setVisibility(View.GONE);
            }
        });
    }

    private void handleInviteClick(View view) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(CreateTaskActivity.this, FilterUserActivity.class);
            intent.putExtra("taskActivity", true);
            intent.putExtra("taskType", "task");
            intent.putExtra("title", binding.editMeetName.getText().toString());
            intent.putExtra("body", binding.editMeetDesc.getText().toString());
            intent.putExtra("imageUrl", byteArray);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupImageUpload() {
        binding.btnUploadImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });
    }

    private void setupDateTimePickers() {
        binding.btnDatePicker.setOnClickListener(view -> showDatePicker());
        binding.btnTimePicker.setOnClickListener(view -> showTimePicker());
    }

    private void setupUploadButton() {
        binding.upload.setOnClickListener(view -> {
            if (EditTask) {
                updateTask(taskId);
            } else {
                publishTask();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void publishTask() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_LONG).show();
            return;
        }

        if (bitmap == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskName = binding.editMeetName.getText().toString().trim();
        String taskDescription = binding.editMeetDesc.getText().toString().trim();
        String date = binding.btnDatePicker.getText().toString().trim();
        String time = binding.btnTimePicker.getText().toString().trim();

        if (taskName.isEmpty() || taskDescription.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Log.d(TAG, "Uploading task: " + taskName);
        Log.d(TAG, "Description: " + taskDescription);
        Log.d(TAG, "Date: " + date);
        Log.d(TAG, "Time: " + time);
        Log.d(TAG, "Image size: " + byteArray.length + " bytes");

        RequestBody taskNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), taskName);
        RequestBody taskDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), taskDescription);
        RequestBody dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody timeRequestBody = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
        RequestBody radioRequestBody = RequestBody.create(MediaType.parse("text/plain"), radioButtonValue);

        MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageID", "image.jpg", imageRequestBody);

        RetrofitClient.getInstance(this).getApiInterfaces().publishTask(
                taskNameRequestBody,
                taskDescriptionRequestBody,
                dateRequestBody,
                timeRequestBody,
                radioRequestBody,
                imageID
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(CreateTaskActivity.this, "Task Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Upload failed", t);
                String errorMessage = "Upload failed: ";
                if (t instanceof IOException) {
                    errorMessage += "Network error. Please check your internet connection.";
                } else if (t instanceof HttpException) {
                    errorMessage += "Server error. Please try again later.";
                } else {
                    errorMessage += t.getLocalizedMessage();
                }
                Toast.makeText(CreateTaskActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTask(String taskId) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_LONG).show();
            return;
        }

        String taskName = binding.editMeetName.getText().toString().trim();
        String taskDescription = binding.editMeetDesc.getText().toString().trim();
        String date = binding.btnDatePicker.getText().toString().trim();
        String time = binding.btnTimePicker.getText().toString().trim();

        if (taskName.isEmpty() || taskDescription.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        MultipartBody.Part imagePart = null;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            imagePart = MultipartBody.Part.createFormData("imageID", "image.jpg", imageRequestBody);
        }

        RequestBody taskNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), taskName);
        RequestBody taskDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), taskDescription);
        RequestBody dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody timeRequestBody = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody radioRequestBody = RequestBody.create(MediaType.parse("text/plain"), radioButtonValue);

        RetrofitClient.getInstance(this).getApiInterfaces().updateTask(
                taskId,
                taskNameRequestBody,
                taskDescriptionRequestBody,
                dateRequestBody,
                timeRequestBody,
                radioRequestBody,
                imagePart
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(CreateTaskActivity.this, "Task Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Update failed", t);
                String errorMessage = "Update failed: ";
                if (t instanceof IOException) {
                    errorMessage += "Network error. Please check your internet connection.";
                } else if (t instanceof HttpException) {
                    errorMessage += "Server error. Please try again later.";
                } else {
                    errorMessage += t.getLocalizedMessage();
                }
                Toast.makeText(CreateTaskActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleErrorResponse(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                Log.e(TAG, "Error response: " + errorBody);
                Toast.makeText(CreateTaskActivity.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Error reading error body", e);
                Toast.makeText(CreateTaskActivity.this, "Error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CreateTaskActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    SelectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;
                    binding.btnDatePicker.setText(SelectedDate);
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTaskActivity.this,
                (view, hourOfDay1, minute1) -> {
                    String amPm = hourOfDay1 >= 12 ? "PM" : "AM";
                    int hour = hourOfDay1 % 12;
                    hour = hour == 0 ? 12 : hour;
                    SelectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute1, amPm);
                    binding.btnTimePicker.setText(SelectedTime);
                }, hourOfDay, minute, false);

        timePickerDialog.show();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        binding.img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.e(TAG, "Error loading image", e);
                        Toast.makeText(CreateTaskActivity.this, "Error loading image: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
}