package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityMeetdescryptionBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.Meetingutils;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.MeetModel;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetdescryptionActivity extends PegaAppCompatActivity {

    private static final String TAG = "MeetdescryptionActivity";
    private static final int EDIT_MEET_REQUEST_CODE = 1001;

    ActivityMeetdescryptionBinding binding;
    String meetId;
    Meetingutils meetingutils;
    URL serverurl;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeetdescryptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UserUtils.init(this);

        setWindowThemeSecond();

         intent = getIntent();
        boolean meetMode = intent.getBooleanExtra("meet", false);

        if (meetMode) {
            binding.btncomplete.setVisibility(View.GONE);
            String relatedId = getIntent().getStringExtra("relatedId");
            Toast.makeText(this, ""+relatedId, Toast.LENGTH_SHORT).show();

            getMeet(relatedId);
        } else {
            setupMeetingDetails(intent);
        }

        if (UserUtils.isSuperAdmin()) {
            binding.edit.setVisibility(View.VISIBLE);
        }


        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  // Log the meetId before opening the edit activity
                if (meetId != null && !meetId.isEmpty()) {
                    Intent sendIntent = new Intent(MeetdescryptionActivity.this, CreateMeetActivity.class);
                    sendIntent.putExtra("EDIT", true);
                    sendIntent.putExtra("meetid", meetId);
                    startActivityForResult(sendIntent, EDIT_MEET_REQUEST_CODE);
                } else {
                    Toast.makeText(MeetdescryptionActivity.this, "Invalid meet ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setupJitsiMeet();
        setupJoinButton();
    }

    private void getMeet(String id) {
        RetrofitClient.getInstance(this).getApiInterfaces().getMeetById(id).enqueue(new Callback<MeetModel>() {
            @Override
            public void onResponse(Call<MeetModel> call, Response<MeetModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MeetModel meetModel = response.body();
                    updateUI(meetModel);

                    // Log the JSON response
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(meetModel);
                    Log.d(TAG, "JSON Response: " + jsonString);
                } else {
                    Log.e(TAG, "Error response: " + response.message());
                    Toast.makeText(MeetdescryptionActivity.this, "Failed to get meet details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MeetModel> call, Throwable t) {
                Log.e(TAG, "Network error: ", t);
                Toast.makeText(MeetdescryptionActivity.this, "Network error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(MeetModel meetModel) {
        binding.title.setText(meetModel.getMeetName());
        binding.descryption.setText(meetModel.getMeetDescription());
        binding.date.setText(meetModel.getDate() + " " + meetModel.getTime());

        Glide.with(this)
                .load(RetrofitClient.MEETIMAGE_BASE_URL + meetModel.getImageID())
                .placeholder(R.drawable.meet)
                .into(binding.bannerimage);

        meetId = intent.getStringExtra("meetid"); // Ensure meetId is updated here
        updateMeetingStatus(meetModel);

        // Log the meetId for debugging
        Log.d(TAG, "Updated meetId: " + meetId);
    }
    private void updateMeetingStatus(MeetModel meetModel) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String completeDateTime = meetModel.getDate() + " " + meetModel.getTime();

        try {
            Date meetingDate = dateTimeFormat.parse(completeDateTime);
            Date currentDate = new Date();

            if (meetingDate != null) {
                if (meetingDate.before(currentDate)) {
                    binding.btncomplete.setText("Meeting is Ended At " + completeDateTime);
                    binding.btncomplete.setVisibility(View.VISIBLE);
                    binding.btnJoin.setVisibility(View.GONE);
                } else {
                    binding.btncomplete.setText("Meeting will Start At " + completeDateTime);
                    binding.btncomplete.setVisibility(View.VISIBLE);
                    binding.btnJoin.setVisibility(View.GONE);
                }
            }
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing error: ", e);
        }
    }

    private void setupMeetingDetails(Intent intent) {
        String meetName = intent.getStringExtra("meetName");
        String meetTime = intent.getStringExtra("meetTime");
        String meetDescryption = intent.getStringExtra("meetDescryption");
        String meetDate = intent.getStringExtra("meetDate");
        String meetImage = intent.getStringExtra("meetImage");
        boolean isLive = intent.getBooleanExtra("isLive", false);
        meetId = intent.getStringExtra("meetid");

        binding.title.setText(meetName);
        binding.date.setText(meetTime);
        binding.descryption.setText(meetDescryption);

        Glide.with(this)
                .load(meetImage)
                .placeholder(R.drawable.meet)
                .into(binding.bannerimage);

        updateMeetingStatusFromIntent(isLive, meetDate, meetTime);
    }

    private void updateMeetingStatusFromIntent(boolean isLive, String meetDate, String meetTime) {
        if (isLive) {
            binding.btnJoin.setVisibility(View.VISIBLE);
            binding.btncomplete.setVisibility(View.GONE);
        } else {
            binding.btncomplete.setVisibility(View.VISIBLE);
            binding.btnJoin.setVisibility(View.GONE);
            updateMeetingStatusText(meetDate, meetTime);
        }
    }

    private void updateMeetingStatusText(String meetDate, String meetTime) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String completeDateTime = meetDate + " " + meetTime;

        try {
            Date meetingDate = dateTimeFormat.parse(completeDateTime);
            Date currentDate = new Date();

            if (meetingDate != null) {
                if (meetingDate.before(currentDate)) {
                    binding.btncomplete.setText("Meeting is Ended At " + completeDateTime);
                } else {
                    binding.btncomplete.setText("Meeting will Start At " + completeDateTime);
                }
            }
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing error: ", e);
        }
    }

    private void setupJitsiMeet() {
        meetingutils = new Meetingutils();
        try {
            serverurl = new URL("https://allo.bim.land/");
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverurl)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(options);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: ", e);
        }
    }

    private void setupJoinButton() {
        binding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MeetdescryptionActivity.this);
                builder.setTitle("Join Clink Meeting");
                builder.setMessage("Do you want to join the meeting?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        launchJitsiMeet();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void launchJitsiMeet() {
        JitsiMeetUserInfo jitsiMeetUserInfo = new JitsiMeetUserInfo();
        jitsiMeetUserInfo.setDisplayName(UserUtils.getUserFirstName());

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom(meetId)
                .setFeatureFlag("invite-enabled", false)
                .setAudioMuted(true)
                .setVideoMuted(true)
                .setFeatureFlag("invite.enabled", false)
                .setUserInfo(jitsiMeetUserInfo)
                .build();
        JitsiMeetActivity.launch(MeetdescryptionActivity.this, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_MEET_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("updatedMeetId")) {
                String updatedMeetId = data.getStringExtra("updatedMeetId");
                meetId = updatedMeetId;  // Update the meetId here as well
                getMeet(updatedMeetId);

                // Log the updated meetId for debugging
                Log.d(TAG, "onActivityResult - Updated meetId: " + meetId);
            }
        }
    }
}