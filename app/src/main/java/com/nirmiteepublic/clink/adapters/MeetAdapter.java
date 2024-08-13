package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.viewholders.ItemMeetViewHolder;
import com.nirmiteepublic.clink.databinding.ItemMeetBinding;
import com.nirmiteepublic.clink.functions.helpers.Prefs;
import com.nirmiteepublic.clink.functions.utils.Meetingutils;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.MeetModel;
import com.nirmiteepublic.clink.ui.activity.pages.MeetdescryptionActivity;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MeetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MeetModel> meetModelList;
    Context context;
    private Prefs prefs;
    private int lastPosition = -1;

    //    Meetingutils meetingutils;
//    URL serverurl;
//    https://jitsi.github.io/handbook/docs/community/community-instances/
    public MeetAdapter(List<MeetModel> meetModelList) {
        this.meetModelList = meetModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        prefs = new Prefs(context);
//        meetingutils = new Meetingutils();
//        try {
//            serverurl = new URL("https://allo.bim.land/");
//            String jwt= prefs.getJWT();
//
//            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                    .setServerURL(serverurl)
//                    .build();
//            JitsiMeet.setDefaultConferenceOptions(options);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        return new ItemMeetViewHolder(ItemMeetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), meetModelList.get(viewType), this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemMeetViewHolder) holder).bind();
        MeetModel model = meetModelList.get(position);
        

        String meetid = model.getId();
        boolean isLive= model.isLive();

//        if(isLive){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String roomName = meetid;
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Join Jitsi Meet");
//                    builder.setMessage("Do you want to join the meeting?");
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Launch JitsiMeetActivity
//                            JitsiMeetUserInfo jitsiMeetUserInfo = new JitsiMeetUserInfo();
//                            jitsiMeetUserInfo.setDisplayName(UserUtils.getUserFirstName());
//
//
//                            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                                    .setRoom(roomName)
//                                    .setFeatureFlag("invite-enabled", false)
//                                    .setAudioMuted(true)
//                                    .setVideoMuted(true)
//                                    .setFeatureFlag("invite.enabled",false)
//                                    .setUserInfo(jitsiMeetUserInfo)
//                                    .build();
//                            JitsiMeetActivity.launch(context, options);
//                        }
//                    });
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            ((Activity) context).finish();
//
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                }
//            });
//        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MeetdescryptionActivity.class);
                    intent.putExtra("meetName",model.getMeetName());
                    intent.putExtra("meetImage",model.getImageID());
                    intent.putExtra("meetDate",model.getDate());
                    intent.putExtra("meetDescryption",model.getMeetDescription());
                    intent.putExtra("meetTime",model.getTime());
                    intent.putExtra("meetTime",model.getTime());
                    intent.putExtra("isLive",model.isLive());
                    intent.putExtra("meetid",model.getId());
                    context.startActivity(intent);
//                    Toast.makeText(context, model.getMeetName()+" "+"is Completed at"+" "+model.getTime()+" "+"on"+" "+model.getDate(), Toast.LENGTH_SHORT).show();

                }
            });

            setAnimation(holder.itemView,position);
//            holder.itemView.setEnabled(false);
        }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return meetModelList.size();
    }


    public void removeItem(int position) {
        if (position >= 0 && position < meetModelList.size()) {
            meetModelList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, meetModelList.size());
        }
    }


    private void setAnimation(View viewToAnimate,int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
