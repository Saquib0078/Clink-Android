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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.material.imageview.ShapeableImageView;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.FrameAdapter;
import com.nirmiteepublic.clink.functions.listeners.FrameListener;
import com.nirmiteepublic.clink.functions.utils.ApiManager;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.utils.Utils;
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

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//shareImageBroadcast();
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(selectedImage)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                binding.image.setImageBitmap(resource);
                                bitmap=resource;
                            }
                        });

                ActivityCompat.requestPermissions(SelectedImageActivity.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },1);
                Uri uri1=getImageUri(getApplicationContext(),bitmap);

                Intent dsPhotoEditorIntent = new Intent(SelectedImageActivity.this, DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(uri1);
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Pico");
                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                startActivityForResult(dsPhotoEditorIntent, 200);

            }
        });


        setWindowThemeSecond();
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectedImageActivity.this, UpdateFrameActivity.class));
            }
        });


        ApiManager.getLiveMeetings(SelectedImageActivity.this, new ApiManager.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                // Handle the successful API response
                try {
                    // Assuming the API returns a JSON array with the key "meeting"
                    if (response.has("meeting")) {
                        JSONArray meetingsArray = response.getJSONArray("meeting");

                        // Process each meeting in the array
                        for (int i = 0; i < meetingsArray.length(); i++) {
                            JSONObject meetingObject = meetingsArray.getJSONObject(i);

                            // Retrieve details from the meeting object
                            System.out.println(meetingObject);

                            // Check if the key exists before retrieving its value
                            String meetName = meetingObject.getString("meetName");
                            String meetDescription = meetingObject.getString("meetDescription");
                            String date = meetingObject.getString("date");
                            String time = meetingObject.getString("time");
                            String imageID = meetingObject.getString("imageID");
                            boolean live = meetingObject.getBoolean("live");
                            String id = meetingObject.getString("_id");
                            String image = Utils.loadMeetingImage(imageID);

                            url = image;
                            taskModelList.add(new MeetModel(meetName, meetDescription, date, time, image, id, live));

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError(String errorMessage) {
                // Handle errors
                Toast.makeText(SelectedImageActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


        Glide.with(this)
                .load(selectedImage)
                .into(binding.image);

        List<FrameModel> frameData = new ArrayList<>();
//        frameData.add(new FrameModel(R.drawable.aimimframe2, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_1, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_2, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_3, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_4, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_5, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_6, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_7, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_7, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_9, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_10, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_11, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_12, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_13, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_14, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_15, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_16, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_17, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_18, "hello", "user"));
        frameData.add(new FrameModel(R.drawable.frame_19, "hello", "user"));
//           frameData.add(new FrameModel());
//
        binding.btnsave.setOnClickListener(view ->
        {
            onSaveButtonClick(binding.constraintlayout);
        });
        ViewTreeObserver viewTreeObserver = binding.constraintlayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Remove the listener to avoid multiple calls
                    binding.constraintlayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Now that the layout is measured, capture and save the view
//                    onSaveButtonClick(null);
                }
            });
        }


        FrameAdapter adapter = new FrameAdapter(this, frameData, new FrameListener() {
            @Override
            public void onFrameSelected(FrameModel frameModel, int position) {
                switch (position) {
                    case 0: {

                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.VISIBLE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        String customFirstName = UserUtils.getFRAMENAME(); // Replace with your custom details
                        String customLastName = UserUtils.getFRAMEADD(); // Replace with your custom details
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();

                        TextView tvName = findViewById(R.id.tvname);
                        TextView tvDetails = findViewById(R.id.tvdetails);


                        if (tvName != null && tvDetails != null) {
                            // Check if customFirstName is not null, set it; otherwise, check adminFirstName
                            String nameText = (customFirstName != null) ? customFirstName : (adminFirstName != null) ? adminFirstName : "DefaultFirstName";

                            // Check if customLastName is not null, set it; otherwise, check adminLastName
                            String detailsText = (customLastName != null) ? customLastName : (adminLastName != null) ? adminLastName : "DefaultLastName";

                            tvName.setText(nameText);
                            tvDetails.setText(detailsText);
                        }


                        break;

                    }
                    case 1: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.VISIBLE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();

                        TextView tvName = findViewById(R.id.tvnameb);
                        TextView tvDetails = findViewById(R.id.tvdetailsb);

                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);
                        }
                        break;
                    }
                    case 2: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.VISIBLE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();

                        TextView tvName = findViewById(R.id.tvnames);
                        TextView tvDetails = findViewById(R.id.tvdetailss);

                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);
                        }
                        break;
                    }

                    case 3: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();

                        TextView tvName = findViewById(R.id.tvnameg);
                        TextView tvDetails = findViewById(R.id.tvdetailsg);

                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);
                        }
                        break;
                    }
                    case 4: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();

                        TextView tvName = findViewById(R.id.tvnamess);
                        TextView tvDetails = findViewById(R.id.tvdetailsss);

                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);
                        }
                        break;
                    }
                    case 5: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        Log.d("CHAGAN", "url" + url);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvnamepy);
                        TextView tvDetails = findViewById(R.id.tvdetailspy);
                        CircleImageView dp = findViewById(R.id.profile_imagepy);
                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);

                        }
                        break;
                    }
                    case 6: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvnameps);
                        TextView tvDetails = findViewById(R.id.tvdetailsps);
                        CircleImageView dp = findViewById(R.id.profile_imagepyy);
                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);

                        }
                        break;
                    }
                    case 7: {


                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf7);
                        TextView tvDetails = findViewById(R.id.tvf71);
                        CircleImageView dp = findViewById(R.id.profile_imagepf7);
                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);

                        }
                        break;
                    }
                    case 8: {
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l9).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf9);
                        TextView tvDetails = findViewById(R.id.tvf91);
                        CircleImageView dp = findViewById(R.id.profile_imagef9pi);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 9: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.VISIBLE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf10);
                        TextView tvDetails = findViewById(R.id.tvf101);
                        ShapeableImageView dp = findViewById(R.id.ivf10);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 10: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.VISIBLE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf11);
                        TextView tvDetails = findViewById(R.id.tvf111);
                        ShapeableImageView dp = findViewById(R.id.ivf11);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 11: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.VISIBLE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf12);
                        TextView tvDetails = findViewById(R.id.tvf121);
                        ShapeableImageView dp = findViewById(R.id.ivf12);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 12: {
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.VISIBLE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf12);
                        TextView tvDetails = findViewById(R.id.tvf121);
                        ShapeableImageView dp = findViewById(R.id.ivf12);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 13: {
                        findViewById(R.id.l17).setVisibility(View.GONE);

                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf14);
                        TextView tvDetails = findViewById(R.id.tvf141);
                        ImageView dp = findViewById(R.id.ivf14);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 15: {
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l16).setVisibility(View.VISIBLE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l17).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf16);
                        TextView tvDetails = findViewById(R.id.tvf161);
                        ImageView dp = findViewById(R.id.ivf16);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 16: {

                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l17).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf17);
                        TextView tvDetails = findViewById(R.id.tvf171);
                        ImageView dp = findViewById(R.id.ivf17);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 17: {
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf18);
                        TextView tvDetails = findViewById(R.id.tvf181);
                        ImageView dp = findViewById(R.id.ivf18);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                    case 18: {
                        findViewById(R.id.l16).setVisibility(View.GONE);
                        findViewById(R.id.l11).setVisibility(View.GONE);
                        findViewById(R.id.l12).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l18).setVisibility(View.GONE);
                        findViewById(R.id.l17).setVisibility(View.GONE);
                        findViewById(R.id.l14).setVisibility(View.GONE);

                        findViewById(R.id.l9).setVisibility(View.GONE);
                        findViewById(R.id.l10).setVisibility(View.GONE);
                        findViewById(R.id.l8).setVisibility(View.GONE);
                        findViewById(R.id.l1).setVisibility(View.GONE);
                        findViewById(R.id.l6).setVisibility(View.GONE);
                        findViewById(R.id.l7).setVisibility(View.GONE);
                        findViewById(R.id.l5).setVisibility(View.GONE);
                        findViewById(R.id.l2).setVisibility(View.GONE);
                        findViewById(R.id.l3).setVisibility(View.GONE);
                        findViewById(R.id.l4).setVisibility(View.GONE);
                        findViewById(R.id.l19).setVisibility(View.VISIBLE);
                        String adminFirstName = UserUtils.getFRAMENAME();
                        String adminLastName = UserUtils.getFRAMEADD();
                        String adminDP = UserUtils.getUserDp();
                        TextView tvName = findViewById(R.id.tvf19);
                        TextView tvDetails = findViewById(R.id.tvf191);
                        ShapeableImageView dp = findViewById(R.id.ivf19);

                        Glide.with(SelectedImageActivity.this)
                                .load(UserUtils.getUserDp())
                                .into(dp);


                        if (tvName != null && tvDetails != null) {
                            tvName.setText(adminFirstName);
                            tvDetails.setText(adminLastName);


                        }
                        break;
                    }
                }


                Glide.with(SelectedImageActivity.this)
                        .load(frameModel.getThumbnail())
                        .thumbnail(0.5f)
                        .into(binding.img);
                binding.img.setScaleX(1f);
                binding.img.setScaleY(1f);
            }
        });

// Add RecyclerView to the FrameLayout

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvConstraintTools.setLayoutManager(layoutManager);
        binding.rvConstraintTools.setAdapter(adapter);


//        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
//        stub.setLayoutResource(R.layout.orange_simple);
//        View inflated = stub.inflate();
//        String adminFirstName = UserUtils.getUserFirstName();
//        String adminLastName = UserUtils.getUserLastName();
//        String adminDP=UserUtils.getUserDp();
//        tvName = findViewById(R.id.tvname);
//        tvDetails = findViewById(R.id.tvdetails);
//        etName = findViewById(R.id.etname);
//        etDetails = findViewById(R.id.etdetails);
//        tvName.setText(adminFirstName);
//        tvDetails.setText(adminLastName);
//        etName.setOnEditorActionListener(
//                new EditText.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                                actionId == EditorInfo.IME_ACTION_DONE ||
//                                event != null &&
//                                        event.getAction() == KeyEvent.ACTION_DOWN &&
//                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                            if (event == null || !event.isShiftPressed()) {
//
//
//                                // Set the EditText text to the current TextView text
//                                etName.setText(tvName.getText());
//
//                                // Set focus to the EditText and show the keyboard
//                                etName.requestFocus();
//                                tvName.setVisibility(View.VISIBLE);
//                                etName.setVisibility(View.GONE);
//                                return true; // consume.
//                            }
//                        }
//                        return false; // pass on to other listeners.
//                    }
//                }
//        );
//        etDetails.setOnEditorActionListener(
//                new EditText.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                                actionId == EditorInfo.IME_ACTION_DONE ||
//                                event != null &&
//                                        event.getAction() == KeyEvent.ACTION_DOWN &&
//                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                            if (event == null || !event.isShiftPressed()) {
//
//
//                                // Set the EditText text to the current TextView text
//                                etDetails.setText(tvDetails.getText());
//
//                                // Set focus to the EditText and show the keyboard
//                                etDetails.requestFocus();
//                                tvDetails.setVisibility(View.VISIBLE);
//                                etDetails.setVisibility(View.GONE);
//                                return true; // consume.
//                            }
//                        }
//                        return false; // pass on to other listeners.
//                    }
//                }
//        );
//        tvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggleVisibility(tvName, etName);
//            }
//        });
//        tvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggleVisibility(tvName, etName);
//            }
//        });
//        tvDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggleVisibility(tvDetails, etDetails);
//            }
//        });
//
//        // Set TextWatcher to update TextView text when EditText changes
//        etName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                tvName.setText(editable.toString());
//            }
//        });
//
//        etDetails.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                tvDetails.setText(editable.toString());
//            }
//        });
//
//    }
//    private void toggleVisibility(TextView textView, EditText editText) {
//        // Toggle visibility of TextView and EditText
//        textView.setVisibility(View.GONE);
//        editText.setVisibility(View.VISIBLE);
//
//        // Set the EditText text to the current TextView text
//        editText.setText(textView.getText());
//
//        // Set focus to the EditText and show the keyboard
//        editText.requestFocus();
//    }

//    private List<FrameModel> generateFrameData() {
//        List<FrameModel> frameData = new ArrayList<>();
//        // Add data for each frame, you may load texts from resources or dynamically generate them
//        for (int i = 1; i <= 19; i++) {
//            int frameResource = getResources().getIdentifier("frame_" + i, "drawable", getPackageName());
//            String frameText = "Ajit";
//            String frameText2 = "Pune,India";
////            String image = "Frame " + i;
//
//
//            frameData.add(new FrameModel(frameResource, frameText, frameText2));
//        }
//        return frameData;
//    }
//    public void onItemClick(FrameModel frameModel) {
//        // Handle item click
//      binding.overlayImageView.setImageResource(frameModel.getFrameResource());
//        binding.overlayImageView.setVisibility(ImageView.VISIBLE);
//        Toast.makeText(this, "Frame clicked: " + frameModel.getFrameText(), Toast.LENGTH_SHORT).show();


    }


    public void onSaveButtonClick(View view) {
        // Check if the view has dimensions
        Bitmap bitmap;
        if (binding.constraintlayout.getWidth() > 0 && binding.constraintlayout.getHeight() > 0) {
            // Capture the content of the ConstraintLayout as a Bitmap
            bitmap = captureView(binding.constraintlayout);

            // Save the Bitmap to the gallery
            saveBitmapToGallery(bitmap, "CLink");

            // Inform the user that the image has been saved
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case when the view has zero dimensions
            Toast.makeText(this, "View has zero dimensions", Toast.LENGTH_SHORT).show();
        }

// Function to save a Bitmap to the gallery
    }


    private Bitmap captureView(View view) {
        // Create a Bitmap with the same dimensions as the view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a Canvas using the Bitmap
        view.draw(new Canvas(bitmap));

        return bitmap;
    }

    private Uri getImageUri(Context context,Bitmap bitmap) {
        bitmap=((BitmapDrawable)binding.image.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path=MediaStore.Images.Media.insertImage(SelectedImageActivity.this.getContentResolver(),bitmap, UUID.randomUUID().toString()+".jpg","clink");
        return Uri.parse(path);

    }

    private void saveBitmapToGallery(Bitmap bitmap, String fileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Get the Uri of the newly created image file in the MediaStore
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            // Open an OutputStream to the Uri to write the bitmap data
            OutputStream outputStream = getContentResolver().openOutputStream(uri);

            // Compress and write the bitmap data to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // Close the OutputStream
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBitmapToStorage(Bitmap bitmap, String filename) {
        // Get the directory for the DCIM folder
        File dcimDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CLink");

        try {
            // Make sure the directory exists
            dcimDir.mkdirs();

            // Create a unique filename using current timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String uniqueFilename = "IMG_" + timeStamp + ".png";

            // Create a File object for the image
            File file = new File(dcimDir, uniqueFilename);

            // Create a FileOutputStream to write the bitmap to the file
            FileOutputStream fos = new FileOutputStream(file);

            // Compress the bitmap to PNG format
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            // Close the FileOutputStream
            fos.close();

            // Show a toast message indicating that the image has been saved
            Toast.makeText(this, "Image saved in DCIM/CLink/" + uniqueFilename, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }


    // Assuming you receive the image Uri in onActivityResult


    private void shareImage(File imageFile) {
        // Create an Intent with ACTION_SEND
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // Set the type of data to be shared
        shareIntent.setType("image/*");

        // Put the image URI as EXTRA_STREAM
        Uri imageUri = FileProvider.getUriForFile(this, "com.your.package.name.fileprovider", imageFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        // Optionally, set a subject for the sharing
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Image");

        // Create a chooser to let the user choose where to share
        Intent chooserIntent = Intent.createChooser(shareIntent, "Share Image");

        // Check if there are apps that can handle the sharing intent
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            // Start the chooser activity
            startActivity(chooserIntent);
        } else {
            // Handle the case where no apps can handle the sharing intent
            Toast.makeText(this, "No apps can handle this action", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImageBroadcast() {
        binding.image.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(binding.image.getDrawingCache());
        binding.image.setDrawingCacheEnabled(false);
        Random random = new Random();

        // Generate a random integer within the range [0, 100)
        int randomNumber = random.nextInt(10000000);
        BroadcastModel model = new BroadcastModel();
        File imagePath = new File(getCacheDir(), randomNumber + "");
        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imagePath);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("image/*");

        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Image");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this image!");

        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }


}
