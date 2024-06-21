package com.nirmiteepublic.clink.ui.activity.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityGetNetworkDetailBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.CategoryList;
import com.pegalite.popups.PegaProgressDialog;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNetworkDetail extends AppCompatActivity {
    ActivityGetNetworkDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetNetworkDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

   binding.progressBar.setVisibility(View.VISIBLE);



        Intent intent = getIntent();
        String id = intent.getStringExtra("userId");
        getUsersById(id);
    }


    private void getUsersById(String id) {
        RetrofitClient.getInstance(this).getApiInterfaces().getNetworkDetail(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        binding.progressBar.setVisibility(View.GONE);

                        String responseBody = null;
                        if (response != null) {
                            responseBody = response.body().string();
                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

                            if (jsonObject.has("findNum") && jsonObject.has("user")) {
                                JsonObject findNumObject = jsonObject.getAsJsonObject("findNum");
                                JsonObject findNumObject1 = jsonObject.getAsJsonObject("user");

                                // Extract values with null checks
                                String fname = findNumObject1.has("fName") ? findNumObject1.getAsJsonPrimitive("fName").getAsString() : "";
                                String lname = findNumObject1.has("lName") ? findNumObject1.getAsJsonPrimitive("lName").getAsString() : "";
                                String lang = findNumObject.has("lang") ? findNumObject.getAsJsonPrimitive("lang").getAsString() : "";
                                String edu = findNumObject.has("edu") ? findNumObject.getAsJsonPrimitive("edu").getAsString() : "";
                                String intr = findNumObject.has("intr") ? findNumObject.getAsJsonPrimitive("intr").getAsString() : "";
                                String num = findNumObject.has("num") ? findNumObject.getAsJsonPrimitive("num").getAsString() : "";
                                String dist = findNumObject.has("dist") ? findNumObject.getAsJsonPrimitive("dist").getAsString() : "";
                                String teh = findNumObject.has("teh") ? findNumObject.getAsJsonPrimitive("teh").getAsString() : "";
                                String vill = findNumObject.has("vill") ? findNumObject.getAsJsonPrimitive("vill").getAsString() : "";
                                String lMark = findNumObject.has("lMark") ? findNumObject.getAsJsonPrimitive("lMark").getAsString() : "";
                                String wpn = findNumObject.has("wpn") ? findNumObject.getAsJsonPrimitive("wpn").getAsString() : "";
                                String fb = findNumObject.has("fb") ? findNumObject.getAsJsonPrimitive("fb").getAsString() : "";
                                String insta = findNumObject.has("insta") ? findNumObject.getAsJsonPrimitive("insta").getAsString() : "";
                                String ward = findNumObject.has("ward") ? findNumObject.getAsJsonPrimitive("ward").getAsString() : "";
                                String id = findNumObject.has("_id") ? findNumObject.getAsJsonPrimitive("_id").getAsString() : "";
                                String dp = findNumObject1.has("dp") ? findNumObject1.getAsJsonPrimitive("dp").getAsString() : "";
                                Toast.makeText(GetNetworkDetail.this, dp, Toast.LENGTH_SHORT).show();
                                UserUtils.setSecondaryUserid(id);
                                binding.fname.setText(fname);
                                binding.lname.setText(lname);
                                binding.district.setText(dist);
                                binding.tehsil.setText(teh);
                                binding.village.setText(vill);
                                binding.whatsapp.setText(wpn);
                                binding.area.setText(lMark);
                                binding.education.setText(edu);
                                binding.intrest.setText(intr);
                                binding.mobile.setText(num);
                                binding.language.setText(lang);
                                binding.facebook.setText(fb);
                                binding.insta.setText(insta);
                                binding.ward.setText(ward);


                                Glide.with(GetNetworkDetail.this)
                                        .load(RetrofitClient.VIDEO_BASE_URL+dp)
                                        .placeholder(R.drawable.default_image)
                                        .into(binding.profileImage);
                            } else {
                                // Handle the case when "findNum" or "data" is not present in the JSON
                                // You can set default values or show an error message as needed
                            }
                        } else {
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(GetNetworkDetail.this, "No user Found", Toast.LENGTH_SHORT).show();

                        }
                    } catch (IOException e) {
                        binding.progressBar.setVisibility(View.GONE);

                        Toast.makeText(GetNetworkDetail.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(GetNetworkDetail.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });


    }

    // Function to load JSON from asset


}