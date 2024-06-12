package com.nirmiteepublic.clink.ui.activity.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersDetail extends AppCompatActivity {
    com.nirmiteepublic.clink.databinding.ActivityUserDetails2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.nirmiteepublic.clink.databinding.ActivityUserDetails2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




    }


}