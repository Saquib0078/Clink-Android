package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityEditProfileBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.ui.fragments.OptionsFragment;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends PegaAppCompatActivity {

    ActivityEditProfileBinding binding;
    private PegaProgressDialog progressDialog;

    String selectedGender;
    Bitmap bitmap;
    String selectBooth;
    String SelectedDate;
    String selectedEducation;
    String selectedIntrest;
    String selectedLanguage;
    private OptionsFragment optionsFragment;
    private static final int REQUEST_SELECT_DISTRICT = 1;
    private static final int REQUEST_CODE_TEHSIL = 2;

    private static final int REQUEST_CODE_EDUCATION = 4;
    private static final int REQUEST_CODE_VILLAGE = 3;
    private static final int REQUEST_CODE_INTREST = 5;
    private static final int REQUEST_CODE_LANGUAGE = 6;
    String selectedDistrict;
    String selectedTehsil;
    String selectedVillage;

    String dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new PegaProgressDialog(this);

        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        binding.nName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, BoothActivity.class);

                startActivityForResult(intent, 2000);
            }
        });

        binding.district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, DistrictActivity.class);

                startActivityForResult(intent, 1);

            }

        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                EditProfileActivity.this,
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.gender.setAdapter(adapter);

        // Handle item selection
        binding.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Exclude the hint from processing
                if (position != 0) {
                     selectedGender = parentView.getItemAtPosition(position).toString();
                    // You can perform actions based on the selected gender
                    Toast.makeText(EditProfileActivity.this, "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        binding.education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, EducationActivity.class);

                startActivityForResult(intent, 1200);
            }
        });
        binding.intrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, IntrestActivity.class);

                startActivityForResult(intent, REQUEST_CODE_INTREST);
            }
        });
        binding.language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, LanguageActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LANGUAGE);
            }
        });


        binding.tehsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditProfileActivity.this, TehsilActivity.class);

                intent.putExtra("selectedDistrict", selectedDistrict);
                startActivityForResult(intent, 2);
            }

        });


        binding.village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, VillageActivity.class);
                intent.putExtra("selectedDistrict", selectedDistrict);
                intent.putExtra("selectedTehsil", selectedTehsil);
                startActivityForResult(intent, 3);
            }

        });

        getUserData();
        binding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                UpdateUser();
                ConvertImage();
            }
        });


        setWindowThemeThird();
        binding.back.setOnClickListener(v -> onBackPressed());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: // Edit District
                    handleEditDistrictResult(data);
                    break;
                case 2: // Edit Tehsil
                    handleEditTehsilResult(data);
                    break;
                case 3: // Edit Village
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
            binding.nName.setText(selectBooth);
        }
    }

    private void handleEditDistrictResult(Intent data) {
        // Handle the result for editing district
        if (data != null && data.hasExtra("selectedDistrict")) {
            selectedDistrict = data.getStringExtra("selectedDistrict");
            binding.district.setText(selectedDistrict);
            binding.Tehsil.setVisibility(View.VISIBLE);
        }
    }

    private void handleEditTehsilResult(Intent data) {
        // Handle the result for editing tehsil
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

        String lang = binding.language.getText().toString();
        String num = binding.mobile.getText().toString();
        String edu = binding.education.getText().toString();
        String intr = binding.intrest.getText().toString();
        String dist = binding.district.getText().toString();
        String teh = binding.tehsil.getText().toString();
        String lMark = binding.intrest.getText().toString();
        String vill = binding.village.getText().toString();
        String ward = binding.ward.getText().toString();
        String booth = binding.nName.getText().toString();
        String insta = binding.insta.getText().toString();
        String fb = binding.facebook.getText().toString();
        String wpn = binding.whatsapp.getText().toString();
        String bio = binding.bio.getText().toString();

        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("lang", lang);
        updatedFields.put("vill", vill);
        updatedFields.put("num", num);
        updatedFields.put("edu", edu);
        updatedFields.put("intr", intr);
        updatedFields.put("wpn", wpn);
        updatedFields.put("dist", dist);
        updatedFields.put("teh", teh);
        updatedFields.put("lmark", lMark);
        updatedFields.put("booth", booth);
        updatedFields.put("ward", ward);
        updatedFields.put("insta", insta);
        updatedFields.put("fb", fb);
        updatedFields.put("bio", bio);
        updatedFields.put("dob", SelectedDate);
        updatedFields.put("gender", selectedGender);




        RetrofitClient.getInstance(this).getApiInterfaces().updateUserById(UserUtils.getSecondaryUserid(), updatedFields).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    hideProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        hideProgressDialog();
                        String res = response.errorBody().string();
                    } catch (IOException e) {
                        hideProgressDialog();
                        throw new RuntimeException(e);
                    }
                    hideProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(EditProfileActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            String fName = binding.fname.getText().toString();
            String lName = binding.lname.getText().toString();

            RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), fName);
            RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), lName);

            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), Base64.decode(base64Image, Base64.DEFAULT));

            // Create MultipartBody.Part for the image
            MultipartBody.Part imageID = MultipartBody.Part.createFormData("Image", "image.jpg", imageRequestBody);

            // Assuming you have a Retrofit instance named retrofit
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
                        // Profile updated successfully
                        Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error response
                        try {
                            String errorResponse = response.errorBody().string();
                            System.out.println(errorResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(EditProfileActivity.this, "Error: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle failure
                    hideProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            // If bitmap is null, update the user's information without the image
            String fName = binding.fname.getText().toString();
            String lName = binding.lname.getText().toString();

            // Create RequestBody for text data
            RequestBody meetNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), fName);
            RequestBody meetDescriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), lName);

            // Assuming you have a Retrofit instance named retrofit
            RetrofitClient.getInstance(this).getApiInterfaces().updateUserById2(
                    UserUtils.getUserId(),
                    meetNameRequestBody,
                    meetDescriptionRequestBody
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        hideProgressDialog();

                        // Profile updated successfully
                        Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error response
                        try {
                            String errorResponse = response.errorBody().string();
                            System.out.println(errorResponse);
                        } catch (IOException e) {
                            hideProgressDialog();

                            e.printStackTrace();
                        }
                        Toast.makeText(EditProfileActivity.this, "Error: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle failure
                    hideProgressDialog();

                    Toast.makeText(EditProfileActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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





    private void getUserData() {
        RetrofitClient.getInstance(this).getApiInterfaces().getUser(UserUtils.getUserNumber()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseBody = null;
                if (response.code() == 200) {
                    hideProgressDialog();

                    try {
                        responseBody = response.body().string();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        JsonObject findNumObject = jsonObject.getAsJsonObject("findNum");
                        JsonObject findNumObject1 = jsonObject.getAsJsonObject("user");
                        String booth = findNumObject.getAsJsonPrimitive("booth") != null ? findNumObject.getAsJsonPrimitive("booth").getAsString() : "";
                        String fname = findNumObject1.getAsJsonPrimitive("fName").getAsString();
                        String lname = findNumObject1.getAsJsonPrimitive("lName").getAsString();
                        String lang = findNumObject.getAsJsonPrimitive("lang") != null ? findNumObject.getAsJsonPrimitive("lang").getAsString() : "";
                        String edu = findNumObject.getAsJsonPrimitive("edu") != null ? findNumObject.getAsJsonPrimitive("edu").getAsString() : "";
                        String intr = findNumObject.getAsJsonPrimitive("intr") != null ? findNumObject.getAsJsonPrimitive("intr").getAsString() : "";
                        String num = findNumObject.getAsJsonPrimitive("num") != null ? findNumObject.getAsJsonPrimitive("num").getAsString() : "";
                        String FrameName = findNumObject.getAsJsonPrimitive("FrameName") != null ? findNumObject.getAsJsonPrimitive("FrameName").getAsString() : "";
                        String FrameAdd = findNumObject.getAsJsonPrimitive("FrameAdd") != null ? findNumObject.getAsJsonPrimitive("FrameAdd").getAsString() : "";
                        String Image = findNumObject.getAsJsonPrimitive("Image") != null ? findNumObject.getAsJsonPrimitive("Image").getAsString() : "";
                        String distr = findNumObject.getAsJsonPrimitive("dist") != null ? findNumObject.getAsJsonPrimitive("dist").getAsString() : "";
                        String teh = findNumObject.getAsJsonPrimitive("teh") != null ? findNumObject.getAsJsonPrimitive("teh").getAsString() : "";
                        String vill = findNumObject.getAsJsonPrimitive("vill") != null ? findNumObject.getAsJsonPrimitive("vill").getAsString() : "";
                        String lMark = findNumObject.getAsJsonPrimitive("lMark") != null ? findNumObject.getAsJsonPrimitive("lMark").getAsString() : "";
                        String wpn = findNumObject.getAsJsonPrimitive("wpn") != null ? findNumObject.getAsJsonPrimitive("wpn").getAsString() : "";
                        String fb = findNumObject.getAsJsonPrimitive("fb") != null ? findNumObject.getAsJsonPrimitive("fb").getAsString() : "";
                        String insta = findNumObject.getAsJsonPrimitive("insta") != null ? findNumObject.getAsJsonPrimitive("insta").getAsString() : "";
                        String ward = findNumObject.getAsJsonPrimitive("ward") != null ? findNumObject.getAsJsonPrimitive("ward").getAsString() : "";
                        String id = findNumObject.getAsJsonPrimitive("_id") != null ? findNumObject.getAsJsonPrimitive("_id").getAsString() : "";
                        String bio = findNumObject.getAsJsonPrimitive("bio") != null ? findNumObject.getAsJsonPrimitive("bio").getAsString() : "";
                        String dob = findNumObject.getAsJsonPrimitive("dob") != null ? findNumObject.getAsJsonPrimitive("dob").getAsString() : "";
                        String gender = findNumObject.getAsJsonPrimitive("gender") != null ? findNumObject.getAsJsonPrimitive("gender").getAsString() : "";

//                        Intent intent=getIntent();
//                        if(intent!=null){
//                            dist =  intent.getStringExtra("selected");
//                            binding.selected.setText(dist);
//                        }

                        UserUtils.setFRAMEADD(FrameAdd);
                        UserUtils.setFRAMENAME(FrameName);
                        UserUtils.setUserDp(Image);
                        System.out.println(responseBody);
                        UserUtils.setSecondaryUserid(id);
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
                        binding.nName.setText(booth);
                        binding.bio.setText(bio);
                        binding.dob.setText(dob);

                        Glide.with(EditProfileActivity.this)
                                .load(UserUtils.getPROFILEIMAGE())
                                .placeholder(R.drawable.default_image) // Set placeholder image resource
                                .into(binding.profileImage);


                    } catch (IOException e) {
                        hideProgressDialog();

                        Toast.makeText(EditProfileActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();                    }

                } else {
                    hideProgressDialog();

                    Toast.makeText(EditProfileActivity.this, "No Data Present", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();

            }
        });
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


    }

    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Handle the selected date
                // You can update a TextView or do any other action here
                SelectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                binding.dob.setText(SelectedDate);
                // Update your UI or perform an action with the selected date
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

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


