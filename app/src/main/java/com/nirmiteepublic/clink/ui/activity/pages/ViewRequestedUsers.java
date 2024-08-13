package com.nirmiteepublic.clink.ui.activity.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityViewRequestedUsersBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.pegalite.popups.DialogData;
import com.pegalite.popups.PegaProgressDialog;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRequestedUsers extends AppCompatActivity {
    ActivityViewRequestedUsersBinding binding;
    private PegaProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewRequestedUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new PegaProgressDialog(this);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String userId = intent.getStringExtra("id");
            if (userId != null) {
                // Use the userId as needed
                getUserData(userId);
                showProgressDialog();
            } else {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                // Handle case where userId is not passed in the Intent
            }
        }


    }

    private void getUserData(String userId) {
        RetrofitClient.getInstance(this).getApiInterfaces().getUser(userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseBody = null;
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    try {
                        responseBody = response.body().string();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        System.out.println(jsonObject);
                        Log.d("data", String.valueOf(jsonObject));

//                        JsonObject findNumObject = jsonObject.getAsJsonObject("findNum");
                        JsonObject findNumObject = jsonObject.getAsJsonObject("data");
                        String fname = findNumObject.getAsJsonPrimitive("fName").getAsString();
                        String lname = findNumObject.getAsJsonPrimitive("lName").getAsString();
                        String lang = findNumObject.getAsJsonPrimitive("lang") != null ? findNumObject.getAsJsonPrimitive("lang").getAsString() : "";
                        String edu = findNumObject.getAsJsonPrimitive("edu") != null ? findNumObject.getAsJsonPrimitive("edu").getAsString() : "";
                        String intr = findNumObject.getAsJsonPrimitive("intr") != null ? findNumObject.getAsJsonPrimitive("intr").getAsString() : "";
                        String num = findNumObject.getAsJsonPrimitive("num") != null ? findNumObject.getAsJsonPrimitive("num").getAsString() : "";
                        String FrameName = findNumObject.getAsJsonPrimitive("FrameName") != null ? findNumObject.getAsJsonPrimitive("FrameName").getAsString() : "";
                        String FrameAdd = findNumObject.getAsJsonPrimitive("FrameAdd") != null ? findNumObject.getAsJsonPrimitive("FrameAdd").getAsString() : "";
                        String Image = findNumObject.getAsJsonPrimitive("dp") != null ? findNumObject.getAsJsonPrimitive("dp").getAsString() : "";
                        String distr = findNumObject.getAsJsonPrimitive("dist") != null ? findNumObject.getAsJsonPrimitive("dist").getAsString() : "";
                        String teh = findNumObject.getAsJsonPrimitive("teh") != null ? findNumObject.getAsJsonPrimitive("teh").getAsString() : "";
                        String vill = findNumObject.getAsJsonPrimitive("vill") != null ? findNumObject.getAsJsonPrimitive("vill").getAsString() : "";
                        String lMark = findNumObject.getAsJsonPrimitive("lMark") != null ? findNumObject.getAsJsonPrimitive("lMark").getAsString() : "";
                        String wpn = findNumObject.getAsJsonPrimitive("wpn") != null ? findNumObject.getAsJsonPrimitive("wpn").getAsString() : "";
                        String fb = findNumObject.getAsJsonPrimitive("fb") != null ? findNumObject.getAsJsonPrimitive("fb").getAsString() : "";
                        String insta = findNumObject.getAsJsonPrimitive("insta") != null ? findNumObject.getAsJsonPrimitive("insta").getAsString() : "";
                        String ward = findNumObject.getAsJsonPrimitive("ward") != null ? findNumObject.getAsJsonPrimitive("ward").getAsString() : "";
                        String id = findNumObject.getAsJsonPrimitive("_id") != null ? findNumObject.getAsJsonPrimitive("_id").getAsString() : "";
                        String dob = findNumObject.getAsJsonPrimitive("dob") != null ? findNumObject.getAsJsonPrimitive("dob").getAsString() : "";
                        String bio = findNumObject.getAsJsonPrimitive("bio") != null ? findNumObject.getAsJsonPrimitive("bio").getAsString() : "";
                        String booth = findNumObject.getAsJsonPrimitive("booth") != null ? findNumObject.getAsJsonPrimitive("booth").getAsString() : "";
////                        Intent intent=getIntent();
////                        if(intent!=null){
////                            dist =  intent.getStringExtra("selected");
////                            binding.selected.setText(dist);
////                        }
//
//                        UserUtils.setFRAMEADD(FrameAdd);
//                        UserUtils.setFRAMENAME(FrameName);
//                        UserUtils.setUserDp(Image);
//                        System.out.println(responseBody);
//                        UserUtils.setSecondaryUserid(id);
                        if (binding.profileImage != null) {
                            Toast.makeText(ViewRequestedUsers.this, Image, Toast.LENGTH_SHORT).show();
                            Glide.with(ViewRequestedUsers.this)
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
                        binding.booth.setText(booth);
                        binding.bio.setText(bio);


                    } catch (IOException e) {
                        hideProgressDialog();
                        Toast.makeText(ViewRequestedUsers.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    hideProgressDialog();
                    Toast.makeText(ViewRequestedUsers.this, "No Data Present", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(ViewRequestedUsers.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

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