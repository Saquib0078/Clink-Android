package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityCreateMeetBinding;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.SharedPreferenceUtils;
import com.nirmiteepublic.clink.models.MeetModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import retrofit2.Response;

public class CreateMeetActivity extends AppCompatActivity {
    private static final String TAG = "CreateMeetActivity";
    private static final int REQUEST_CODE_FILTER_USERS = 1001;
    String SelectedDate;
    String SelectedTime;
    Bitmap bitmap;
    String radioButtonValue = "Open for All"; // Default value
    ActivityCreateMeetBinding binding;
    MultipartBody.Part imageID;
    List<String> numbers;
    SharedPreferenceUtils sharedPreferenceUtils;
    boolean EditMode;
    String meetId;
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
                        Toast.makeText(CreateMeetActivity.this, "Error loading image: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateMeetBinding.inflate(getLayoutInflater());
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
            EditMode = intent.getBooleanExtra("EDIT", false);
            meetId = intent.getStringExtra("meetid");
        }


        if (EditMode) {
            binding.upload.setText("Update Meeting");
            PegaProgress.showProgressBar(this);
            binding.radiogrp.setVisibility(View.VISIBLE);
            loadExistingMeetData();
        }


        setupRadioGroup();
        setupImageUpload();
        setupDateTimePickers();
        setupUploadButton();
    }

    private void loadExistingMeetData() {
        RetrofitClient.getInstance(this).getApiInterfaces().getMeetById(meetId).enqueue(new Callback<MeetModel>() {
            @Override
            public void onResponse(Call<MeetModel> call, Response<MeetModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MeetModel meetModel = response.body();
                    PegaProgress.hideProgressBar();

                    binding.editMeetDesc.setText(meetModel.getMeetDescription());
                    binding.editMeetName.setText(meetModel.getMeetName());
                    binding.btnDatePicker.setText(meetModel.getDate());
                    binding.btnTimePicker.setText(meetModel.getTime());

                    if ("Limited Users".equals(meetModel.getRadioButtonValue())) {
                        binding.limited.setChecked(true);
                        radioButtonValue = "Limited Users";
                        binding.invite.setVisibility(View.VISIBLE);
                    } else {
                        binding.openall.setChecked(true);
                        radioButtonValue = "Open for All";
                        binding.invite.setVisibility(View.GONE);
                    }
                    Glide.with(CreateMeetActivity.this)
                            .load(RetrofitClient.MEETIMAGE_BASE_URL + meetModel.getImageID())
                            .into(binding.img);
                    bitmap = getBitmapFromImageView(binding.img);

                }
            }

            @Override
            public void onFailure(Call<MeetModel> call, Throwable t) {
                Log.e(TAG, "Failed to load meet data", t);
                PegaProgress.hideProgressBar();

                Toast.makeText(CreateMeetActivity.this, "Failed to load meet data: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.upload.setOnClickListener(v -> updateMeet(meetId));
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            File imageFile = saveBitmapToFile(bitmap);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(CreateMeetActivity.this, FilterUserActivity.class);
            intent.putExtra("meetActivity", true);
            intent.putExtra("taskType", "meeting");
            intent.putExtra("title", binding.editMeetName.getText().toString());
            intent.putExtra("body", binding.editMeetDesc.getText().toString());
            intent.putExtra("imageUrl", imageFile.getAbsolutePath());
            intent.putExtra("date", SelectedDate);
            intent.putExtra("related_ID", meetId);
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
            if (EditMode) {
                updateMeet(meetId);
            } else {
                ConvertImage();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateMeetActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    SelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    binding.btnDatePicker.setText(SelectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                CreateMeetActivity.this,
                (view, hourOfDay, minute) -> {
                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int hour = hourOfDay % 12;
                    hour = hour == 0 ? 12 : hour;
                    SelectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);
                    binding.btnTimePicker.setText(SelectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void ConvertImage() {
        if (bitmap == null ||
                binding.editMeetName.getText().toString().trim().isEmpty() ||
                binding.editMeetDesc.getText().toString().trim().isEmpty() ||
                binding.btnDatePicker.getText().toString().trim().isEmpty() ||
                binding.btnTimePicker.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String selectedUsers = sharedPreferenceUtils.getString("selectedUsers", "");
        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.editMeetName.getText().toString());
        RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.editMeetDesc.getText().toString());
        RequestBody dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.btnDatePicker.getText().toString());
        RequestBody timeRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.btnTimePicker.getText().toString());
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
        RequestBody radioRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.limited.isChecked() ? "Limited Users" : "Open for All");
//        RequestBody selectedUsersRequestBody = RequestBody.create(MediaType.parse("text/plain"), selectedUsers);
        List<String> users = Arrays.asList(selectedUsers.split(","));
        imageID = MultipartBody.Part.createFormData("imageID", "image.jpg", imageRequestBody);

        RetrofitClient.getInstance(this).getApiInterfaces().publishMeet(
                meetNameRequestBody,
                meetDescriptionRequestBody,
                dateRequestBody,
                timeRequestBody,
                radioRequestBody,
                imageID, users
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                PegaProgress.hideProgressBar();

                if (response.isSuccessful()) {

                    PegaProgress.hideProgressBar();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    sharedPreferenceUtils.putString("selectedUsers", "");
                  if(radioButtonValue.equals("Open for All")){
                      sendNotificationToAllUsers(
                              binding.editMeetName.getText().toString(),
                              binding.editMeetDesc.getText().toString(),
                              bitmap,
                              numbers
                      );
                  }
                    finish();
                    Toast.makeText(CreateMeetActivity.this, "Meeting Uploaded Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                PegaProgress.hideProgressBar();

                Log.e(TAG, "Upload failed", t);
                Toast.makeText(CreateMeetActivity.this, "Upload failed: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMeet(String meetId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        PegaProgress.showProgressBar(this);

        // Capture the current radio button selection
        radioButtonValue = binding.limited.isChecked() ? "Limited Users" : "Open for All";
        MultipartBody.Part imagePart = null;
        if (bitmap != null) {
            // Only create imagePart if a new image was selected
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            imagePart = MultipartBody.Part.createFormData("imageID", "image.jpg", imageRequestBody);
        }

        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.editMeetName.getText().toString());
        RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.editMeetDesc.getText().toString());
        RequestBody dateRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.btnDatePicker.getText().toString());
        RequestBody timeRequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.btnTimePicker.getText().toString());
        RequestBody radioRequestBody = RequestBody.create(MediaType.parse("text/plain"), radioButtonValue);

        RetrofitClient.getInstance(this).getApiInterfaces().updateMeet(
                meetId,
                meetNameRequestBody,
                meetDescriptionRequestBody,
                dateRequestBody,
                timeRequestBody,
                radioRequestBody,
                imagePart  // This can be null if no new image was selected
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                PegaProgress.hideProgressBar();

                if (response.isSuccessful()) {

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedMeetId", meetId);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    Toast.makeText(CreateMeetActivity.this, "Meeting Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                PegaProgress.hideProgressBar();
                Log.e(TAG, "Update failed", t);
                Toast.makeText(CreateMeetActivity.this, "Update failed: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    private void handleErrorResponse(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                PegaProgress.hideProgressBar();

                String errorBody = response.errorBody().string();
                Log.e(TAG, "Error response: " + errorBody);
                Toast.makeText(CreateMeetActivity.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Error reading error body", e);
                Toast.makeText(CreateMeetActivity.this, "Error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CreateMeetActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendNotificationToAllUsers(String title, String body, Bitmap bitmap,List<String> phoneNumbers) {
        if (bitmap == null) {
            Toast.makeText(CreateMeetActivity.this, "Image is not available", Toast.LENGTH_SHORT).show();
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



        RetrofitClient.getInstance(CreateMeetActivity.this).getApiInterfaces().sendNotification(
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
                    Toast.makeText(CreateMeetActivity.this, "Notification Sent Successfully to All Users", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateMeetActivity.this, "Failed to Send Notification to All Users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateMeetActivity.this, "Failed to send notification: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}