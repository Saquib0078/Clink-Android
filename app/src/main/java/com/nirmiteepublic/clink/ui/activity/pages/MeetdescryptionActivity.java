package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityMeetdescryptionBinding;
import com.nirmiteepublic.clink.functions.utils.Meetingutils;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class MeetdescryptionActivity extends PegaAppCompatActivity {

    ActivityMeetdescryptionBinding binding;
    String meetId;

    Meetingutils meetingutils;
    URL serverurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMeetdescryptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();


        setWindowThemeSecond();

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent=new Intent(MeetdescryptionActivity.this,CreateMeetActivity.class);
                sendIntent.putExtra("EDIT",true);
                sendIntent.putExtra("meetid",meetId);
                startActivity(sendIntent);
            }
        });

        if(intent !=null){
            String meetName=intent.getStringExtra("meetName");
            String meetTime=intent.getStringExtra("meetTime");
            String meetDescryption=intent.getStringExtra("meetDescryption");
            String meetDate=intent.getStringExtra("meetDate");
            String meetImage=intent.getStringExtra("meetImage");
            boolean isLive=intent.getBooleanExtra("isLive",false);
             meetId=intent.getStringExtra("meetid");
            if(isLive==true){
                binding.btnJoin.setVisibility(View.VISIBLE);
                binding.btncomplete.setVisibility(View.GONE);
            }else {
                binding.btncomplete.setVisibility(View.VISIBLE);
                binding.btnJoin.setVisibility(View.GONE);
            }
            binding.title.setText(meetName);
            binding.date.setText(meetTime);
            binding.descryption.setText(meetDescryption);
            binding.btncomplete.setText("Meeting is Ended At"+" "+meetDate);
            Glide.with(this)
                    .load(meetImage)
                    .placeholder(R.drawable.meet)
                    .into(binding.bannerimage);
        }

        meetingutils = new Meetingutils();
        try {
            serverurl = new URL("https://allo.bim.land/");
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverurl)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(options);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        binding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = meetId;

                AlertDialog.Builder builder = new AlertDialog.Builder(MeetdescryptionActivity.this);
                builder.setTitle("Join Clink Meeting");
                builder.setMessage("Do you want to join the meeting?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Launch JitsiMeetActivity
                        JitsiMeetUserInfo jitsiMeetUserInfo = new JitsiMeetUserInfo();
                        jitsiMeetUserInfo.setDisplayName(UserUtils.getUserFirstName());


                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                .setRoom(roomName)
                                .setFeatureFlag("invite-enabled", false)
                                .setAudioMuted(true)
                                .setVideoMuted(true)
                                .setFeatureFlag("invite.enabled",false)
                                .setUserInfo(jitsiMeetUserInfo)
                                .build();
                        JitsiMeetActivity.launch(MeetdescryptionActivity.this, options);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }
}
