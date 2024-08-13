package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityEditProfileBinding;
import com.nirmiteepublic.clink.functions.helpers.PegaProgress;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends PegaAppCompatActivity {

    ActivityEditProfileBinding binding;

    String selectedGender;
    Bitmap bitmap;
    String selectBooth;
    String SelectedDate;
    String selectedEducation;
    String selectedIntrest;
    String selectedLanguage;
    String selectedDistrict;
    String selectedTehsil;
    String selectedVillage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
        setupSpinners();
        getUserData();
        showProgressDialog();

        setWindowThemeThird();
        binding.back.setOnClickListener(v -> onBackPressed());
    }

    private void setupListeners() {
        binding.dob.setOnClickListener(v -> showDatePicker());
        binding.booth.setOnClickListener(v -> startActivityForResult(new Intent(EditProfileActivity.this, BoothActivity.class), 2000));
        binding.district.setOnClickListener(v -> startActivityForResult(new Intent(EditProfileActivity.this, DistrictActivity.class), 1));
        binding.profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });
        binding.education.setOnClickListener(v -> startActivityForResult(new Intent(EditProfileActivity.this, EducationActivity.class), 1200));
        binding.intrest.setOnClickListener(v -> startActivityForResult(new Intent(EditProfileActivity.this, IntrestActivity.class), 5));
        binding.language.setOnClickListener(v -> startActivityForResult(new Intent(EditProfileActivity.this, LanguageActivity.class), 6));
        binding.tehsil.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, TehsilActivity.class);
            intent.putExtra("selectedDistrict", selectedDistrict);
            startActivityForResult(intent, 2);
        });
        binding.village.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, VillageActivity.class);
            intent.putExtra("selectedDistrict", selectedDistrict);
            intent.putExtra("selectedTehsil", selectedTehsil);
            startActivityForResult(intent, 3);
        });
        binding.Update.setOnClickListener(v -> {
            showProgressDialog();
            UpdateUser();
            ConvertImage();
        });
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                EditProfileActivity.this,
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.gender.setAdapter(adapter);
        binding.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {
                    selectedGender = parentView.getItemAtPosition(position).toString();
                    Toast.makeText(EditProfileActivity.this, "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    handleEditDistrictResult(data);
                    break;
                case 2:
                    handleEditTehsilResult(data);
                    break;
                case 3:
                    handleEditVillageResult(data);
                    break;
                case 2000:
                    handleBoothSelect(data);
                    break;
                case 5:
                    handleIntrest(data);
                    break;
                case 6:
                    handleLanguage(data);
                    break;
                case 1200:
                    handleEducation(data);
                    break;
            }
        }
    }

    private void handleIntrest(Intent data) {
        if (data != null && data.hasExtra("selectedIntrest")) {
            selectedIntrest = data.getStringExtra("selectedIntrest");
            binding.intrest.setText(selectedIntrest);
        }
    }

    private void handleEducation(Intent data) {
        if (data != null) {
            selectedEducation = data.getStringExtra("selectedEducation");
            binding.education.setText(selectedEducation);
        }
    }

    private void handleLanguage(Intent data) {
        if (data != null && data.hasExtra("selectedLanguage")) {
            selectedLanguage = data.getStringExtra("selectedLanguage");
            binding.language.setText(selectedLanguage);
        }
    }

    private void handleBoothSelect(Intent data) {
        if (data != null && data.hasExtra("selectBooth")) {
            selectBooth = data.getStringExtra("selectBooth");
            binding.booth.setText(selectBooth);
        }
    }

    private void handleEditDistrictResult(Intent data) {
        if (data != null && data.hasExtra("selectedDistrict")) {
            selectedDistrict = data.getStringExtra("selectedDistrict");
            binding.district.setText(selectedDistrict);
            binding.Tehsil.setVisibility(View.VISIBLE);
        }
    }

    private void handleEditTehsilResult(Intent data) {
        if (data != null && data.hasExtra("selectedTehsil")) {
            selectedTehsil = data.getStringExtra("selectedTehsil");
            binding.tehsil.setText(selectedTehsil);
            binding.Village.setVisibility(View.VISIBLE);
        }
    }

    private void handleEditVillageResult(Intent data) {
        if (data != null && data.hasExtra("selectedVillage")) {
            selectedVillage = data.getStringExtra("selectedVillage");
            binding.village.setText(selectedVillage);
        }
    }

    private void UpdateUser() {
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("lang", binding.language.getText().toString());
        updatedFields.put("vill", binding.village.getText().toString());
        updatedFields.put("num", binding.mobile.getText().toString());
        updatedFields.put("edu", binding.education.getText().toString());
        updatedFields.put("intr", binding.intrest.getText().toString());
        updatedFields.put("wpn", binding.whatsapp.getText().toString());
        updatedFields.put("dist", binding.district.getText().toString());
        updatedFields.put("teh", binding.tehsil.getText().toString());
        updatedFields.put("lmark", binding.intrest.getText().toString());
        updatedFields.put("booth", binding.booth.getText().toString());
        updatedFields.put("ward", binding.ward.getText().toString());
        updatedFields.put("insta", binding.insta.getText().toString());
        updatedFields.put("fb", binding.facebook.getText().toString());
        updatedFields.put("bio", binding.bio.getText().toString());
        updatedFields.put("dob", SelectedDate);
        updatedFields.put("gender", selectedGender);

        RetrofitClient.getInstance(this).getApiInterfaces().updateUserById(UserUtils.getSecondaryUserid(), updatedFields).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    hideProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    hideProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(EditProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ConvertImage() {
        if (bitmap == null) {
            // If no new image is selected, just return
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        String fName = binding.fname.getText().toString();
        String lName = binding.lname.getText().toString();

        RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), fName);
        RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), lName);
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));
        MultipartBody.Part imageID = MultipartBody.Part.createFormData("Image", "image.jpg", imageRequestBody);

        RetrofitClient.getInstance(this).getApiInterfaces().updateUserById2(
                UserUtils.getUserId(),
                meetNameRequestBody,
                meetDescriptionRequestBody,
                imageID
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    Snackbar.make(findViewById(android.R.id.content), "Profile Updated Successfully", Snackbar.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    hideProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "Error: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
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

    private void getUserData() {
        RetrofitClient.getInstance(this).getApiInterfaces().getUser(UserUtils.getUserNumber()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        JsonObject findNumObject = jsonObject.getAsJsonObject("data");

                        String fname = findNumObject.getAsJsonPrimitive("fName").getAsString();
                        String lname = findNumObject.getAsJsonPrimitive("lName").getAsString();
                        String lang = findNumObject.has("lang") ? findNumObject.getAsJsonPrimitive("lang").getAsString() : "";
                        String dob = findNumObject.has("dob") ? findNumObject.getAsJsonPrimitive("dob").getAsString() : "";
                        String bio = findNumObject.has("bio") ? findNumObject.getAsJsonPrimitive("bio").getAsString() : "";
                        String booth = findNumObject.has("booth") ? findNumObject.getAsJsonPrimitive("booth").getAsString() : "";
                        String edu = findNumObject.has("edu") ? findNumObject.getAsJsonPrimitive("edu").getAsString() : "";
                        String intr = findNumObject.has("intr") ? findNumObject.getAsJsonPrimitive("intr").getAsString() : "";
                        String num = findNumObject.has("num") ? findNumObject.getAsJsonPrimitive("num").getAsString() : "";
                        String Image = findNumObject.has("dp") ? findNumObject.getAsJsonPrimitive("dp").getAsString() : "";
                        String distr = findNumObject.has("dist") ? findNumObject.getAsJsonPrimitive("dist").getAsString() : "";
                        String teh = findNumObject.has("teh") ? findNumObject.getAsJsonPrimitive("teh").getAsString() : "";
                        String vill = findNumObject.has("vill") ? findNumObject.getAsJsonPrimitive("vill").getAsString() : "";
                        String lMark = findNumObject.has("lMark") ? findNumObject.getAsJsonPrimitive("lMark").getAsString() : "";
                        String wpn = findNumObject.has("wpn") ? findNumObject.getAsJsonPrimitive("wpn").getAsString() : "";
                        String fb = findNumObject.has("fb") ? findNumObject.getAsJsonPrimitive("fb").getAsString() : "";
                        String insta = findNumObject.has("insta") ? findNumObject.getAsJsonPrimitive("insta").getAsString() : "";
                        String ward = findNumObject.has("ward") ? findNumObject.getAsJsonPrimitive("ward").getAsString() : "";
                        String id = findNumObject.has("_id") ? findNumObject.getAsJsonPrimitive("_id").getAsString() : "";

                        UserUtils.setUserDp(Image);
                        UserUtils.setSecondaryUserid(id);

                        if (binding.profileImage != null) {
                            Glide.with(EditProfileActivity.this)
                                    .load(RetrofitClient.PROFILE_IMAGE + Image)
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(binding.profileImage);
                        }

                        binding.fname.setText(fname);
                        binding.lname.setText(lname);
                        binding.tehsil.setText(teh);
                        binding.village.setText(vill);
                        binding.whatsapp.setText(wpn);
                        binding.area.setText(lMark);
                        binding.district.setText(distr);
                        binding.education.setText(edu);
                        binding.intrest.setText(intr);
                        binding.mobile.setText(num);
                        binding.language.setText(lang);
                        binding.facebook.setText(fb);
                        binding.insta.setText(insta);
                        binding.ward.setText(ward);
                        binding.dob.setText(dob);
                        binding.bio.setText(bio);
                        binding.booth.setText(booth);

                    } catch (IOException e) {
                        hideProgressDialog();
                        Toast.makeText(EditProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "No Data Present", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        binding.dob.setText(SelectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void showProgressDialog() {
        PegaProgress.showProgressBar(this);
    }

    private void hideProgressDialog() {
      PegaProgress.hideProgressBar();
    }
}