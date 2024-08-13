package com.nirmiteepublic.clink.ui.activity.pages;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.BroadcastModel;
import com.nirmiteepublic.clink.models.FrameModel;
import com.nirmiteepublic.clink.models.MeetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class

SelectedImageActivity extends PegaAppCompatActivity {
    private String url;
    private MeetModel meetModel;
    com.nirmiteepublic.clink.databinding.ActivitySelectedImageBinding binding;
    private List<FrameModel> frameData;
    Bitmap bitmap;
    Uri uri;
    List<MeetModel> taskModelList = new ArrayList<>();
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.nirmiteepublic.clink.databinding.ActivitySelectedImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(SelectedImageActivity.this);
        Intent intent = getIntent();

        String selectedImage = intent.getStringExtra("selectedImage");
    }
}