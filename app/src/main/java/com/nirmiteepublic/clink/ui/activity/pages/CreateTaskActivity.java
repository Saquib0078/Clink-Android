package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityCreateTaskBinding;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.SharedPreferenceUtils;
import com.nirmiteepublic.clink.models.Task;
import com.nirmiteepublic.clink.models.TaskModelResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    List<String> numbers;
    String radioButtonValue; // Default value
    ActivityCreateTaskBinding binding;
    SharedPreferenceUtils sharedPreferenceUtils;

    boolean EditTask;
    String taskId;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this);


        RetrofitClient.getInstance(this).getApiInterfaces().getPhoneNumbers().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    numbers = response.body();
                    if (numbers != null) {
                        // Print the numbers array to the console
                        for (String number : numbers) {
                            System.out.println(number);
                        }
                    } else {
                        System.out.println("No numbers found");
                    }
                } else {
                    System.out.println("Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                t.printStackTrace();

            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            EditTask = intent.getBooleanExtra("EDITTASK", false);
            taskId = intent.getStringExtra("TaskId");
        }


        if (EditTask) {
            binding.upload.setText("Update Task");
            PegaProgress.showProgressBar(this);
            binding.radiogrp.setVisibility(View.VISIBLE);
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
                if (response.isSuccessful() && response.body() != null) {
                    PegaProgress.hideProgressBar();

                    Task taskModel = response.body().getTask();
                    binding.editMeetDesc.setText(taskModel.getTaskDescription());
                    binding.editMeetName.setText(taskModel.getTaskName());
                    binding.btnDatePicker.setText(taskModel.getDate());

                    if ("Limited Users".equals(taskModel.getRadioButtonValue())) {
                        binding.limited.setChecked(true);
                        radioButtonValue = "Limited Users";
                        binding.invite.setVisibility(View.VISIBLE);
                    } else {
                        binding.openall.setChecked(true);
                        radioButtonValue = "Open for All";
                        binding.invite.setVisibility(View.GONE);
                    }
                    if (taskModel.getTaskUrl() != null) {
                        binding.taskUrl.setText(taskModel.getTaskUrl());

                    }
                    binding.btnTimePicker.setText(taskModel.getTime());
                    Glide.with(CreateTaskActivity.this)
                            .load(RetrofitClient.TASKIMAGE_BASE_URL + taskModel.getImageID())
                            .into(binding.img);

                } else {
                    PegaProgress.hideProgressBar();

                    Toast.makeText(CreateTaskActivity.this, "Failed to load task data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskModelResponse> call, Throwable t) {
                Log.e(TAG, "Failed to load task data", t);
                PegaProgress.hideProgressBar();

                Toast.makeText(CreateTaskActivity.this, "Failed to load task data: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap getBitmapFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        // Handle the case where the drawable is not a BitmapDrawable (e.g., use a default bitmap or return null)
        return null;
    }

    private void setupRadioGroup() {
        binding.radiogrp.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.limited) {
                radioButtonValue = "Limited Users";
                binding.invite.setVisibility(View.VISIBLE);
                try {
                    binding.invite.setOnClickListener(this::handleInviteClick);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                radioButtonValue = "Open for All";
                binding.invite.setVisibility(View.GONE);
            }
        });
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            File cacheDir = getCacheDir();
            File tempFile = new File(cacheDir, "temp_image.png");
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.close();
            return tempFile;
        } catch (Exception e) {
            Log.d("File", e.getMessage());
        }

        return null;
    }

    private void handleInviteClick(View view) {
        bitmap = getBitmapFromImageView(binding.img);

        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
            File imageFile = saveBitmapToFile(bitmap);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(CreateTaskActivity.this, FilterUserActivity.class);
            intent.putExtra("taskActivity", true);
            intent.putExtra("taskType", "task");
            intent.putExtra("title", binding.editMeetName.getText().toString());
            intent.putExtra("body", binding.editMeetDesc.getText().toString());
            intent.putExtra("imageUrl", imageFile.getAbsolutePath());
            intent.putExtra("related_ID", taskId);
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

        if (bitmap == null ||
                binding.editMeetName.getText().toString().trim().isEmpty() ||
                binding.editMeetDesc.getText().toString().trim().isEmpty() ||
                binding.btnDatePicker.getText().toString().trim().isEmpty() ||
                binding.btnTimePicker.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskName = binding.editMeetName.getText().toString().trim();
        String taskDescription = binding.editMeetDesc.getText().toString().trim();
        String date = binding.btnDatePicker.getText().toString().trim();
        String time = binding.btnTimePicker.getText().toString().trim();
        String taskUrl = binding.taskUrl.getText().toString().trim();
        if (!taskUrl.isEmpty()) {
            if (!PegaProgress.isValidUrl(taskUrl)) {
                binding.taskUrl.setError("Please enter a valid URL");
                return;
            }
        }
        if (taskDescription.isEmpty() || date.isEmpty() || time.isEmpty()) {
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
        String selectedUsers = sharedPreferenceUtils.getString("selectedUsers", "");
        RequestBody taskNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), taskName);
        RequestBody taskDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), taskDescription);
        RequestBody dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody timeRequestBody = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody taskurlBody = RequestBody.create(MediaType.parse("text/plain"), taskUrl);
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
        RequestBody radioRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.limited.isChecked() ? "Limited Users" : "Open for All");
        List<String> users = Arrays.asList(selectedUsers.split(","));

        MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageID", "image.jpg", imageRequestBody);

        RetrofitClient.getInstance(this).getApiInterfaces().publishTask(
                taskNameRequestBody,
                taskDescriptionRequestBody,
                dateRequestBody,
                timeRequestBody,
                taskurlBody,
                radioRequestBody,
                imageID, users
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    PegaProgress.hideProgressBar();
                    Toast.makeText(CreateTaskActivity.this, "Task Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    if(radioButtonValue.equals("Open for All")){
                        sendNotificationToAllUsers(
                                binding.editMeetName.getText().toString(),
                                binding.editMeetDesc.getText().toString(),
                                bitmap,
                                numbers
                        );
                    }
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    sharedPreferenceUtils.putString("selectedUsers", "");
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Upload failed", t);
                PegaProgress.hideProgressBar();

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
        String taskUrl = binding.taskUrl.getText().toString().trim();
        String time = binding.btnTimePicker.getText().toString().trim();

        if (!taskUrl.isEmpty()) {
            if (!PegaProgress.isValidUrl(taskUrl)) {
                binding.taskUrl.setError("Please enter a valid URL");
                return;
            }
        }
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
        RequestBody taskurlBody = RequestBody.create(MediaType.parse("text/plain"), taskUrl);

        RetrofitClient.getInstance(this).getApiInterfaces().updateTask(
                taskId,
                taskNameRequestBody,
                taskDescriptionRequestBody,
                dateRequestBody,
                timeRequestBody,
                taskurlBody,
                radioRequestBody,
                imagePart
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                PegaProgress.hideProgressBar();

                if (response.isSuccessful()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedTaskId", taskId);
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(CreateTaskActivity.this, "Task Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                PegaProgress.hideProgressBar();

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
                PegaProgress.hideProgressBar();

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

    private void sendNotificationToAllUsers(String title, String body, Bitmap bitmap,List<String> phoneNumbers) {
        if (bitmap == null) {
            Toast.makeText(CreateTaskActivity.this, "Image is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), body);
        RequestBody meetingTypeRequestBody = RequestBody.create(MediaType.parse("text/plain"), "normal");
        RequestBody relatedIDRequestBody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody meetDateRequestBody = RequestBody.create(MediaType.parse("text/plain"),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
        MultipartBody.Part imageID = MultipartBody.Part.createFormData("imageUrl", "image.jpg", imageRequestBody);



        RetrofitClient.getInstance(CreateTaskActivity.this).getApiInterfaces().sendNotification(
                meetNameRequestBody,
                meetDescriptionRequestBody,
                phoneNumbers,
                imageID,
                meetingTypeRequestBody,
                relatedIDRequestBody,
                meetDateRequestBody
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateTaskActivity.this, "Notification Sent Successfully to All Users", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateTaskActivity.this, "Failed to Send Notification to All Users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateTaskActivity.this, "Failed to send notification: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}