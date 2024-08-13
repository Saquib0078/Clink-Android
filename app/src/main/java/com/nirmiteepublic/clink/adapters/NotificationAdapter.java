package com.nirmiteepublic.clink.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemNotificationBinding;
import com.nirmiteepublic.clink.models.NotificationItem;
import com.nirmiteepublic.clink.ui.NotificationDescryptionActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<NotificationItem> models;
    Context context;

    public NotificationAdapter(List<NotificationItem> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new NotificationAdapter.ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationItem notificationModel = models.get(position);
        holder.binding.userName.setText(notificationModel.getTitle());
        holder.binding.suggestion.setText(notificationModel.getBody());

      String date=  convertToIndianFormat(notificationModel.getCreatedAt());
        holder.binding.time.setText(date);



        if (notificationModel.getMeetingType().equals("task")) {
            holder.binding.type.setText("Task");

        } else if (notificationModel.getMeetingType().equals("meeting")) {
            holder.binding.type.setText("Meeting");

        }

        if (notificationModel.getMeetingType().equals("normal")) {
            holder.binding.type.setText("General Notification");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotificationDescryptionActivity.class);
                intent.putExtra("title", notificationModel.getTitle());
                intent.putExtra("body", notificationModel.getBody());
                intent.putExtra("imageUrl", notificationModel.getImageUrl());
                intent.putExtra("header", notificationModel.getMeetingType());
                context.startActivity(intent);

//                if (notificationModel.getMeetingType().equals("meeting")) {
//                    Intent intent = new Intent(context, MeetdescryptionActivity.class);
//                    intent.putExtra("relatedId", notificationModel.getRelatedId());
//                    intent.putExtra("meet", true);
//                    context.startActivity(intent);
//
//                } else if (notificationModel.getMeetingType().equals("task")) {
//                    Intent intent = new Intent(context, TaskDescryptionActivity.class);
//                    intent.putExtra("relatedId", notificationModel.getRelatedId());
//                    context.startActivity(intent);
//                } else if(notificationModel.getId().equals(null) ){
//                    Toast.makeText(context, "Cant Describe", Toast.LENGTH_SHORT).show();
//                }

            }
        });
        Glide.with(context)
                .load(notificationModel.getImageUrl())
                .placeholder(R.drawable.clinklogo) // Replace R.drawable.default_image with your default image resource
                .error(R.drawable.clinklogo)
                .into(holder.binding.view);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationBinding binding;

        public ViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static String convertToIndianFormat(String isoDateString) {
        try {
            // Create a SimpleDateFormat for parsing the input
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Parse the input string to a Date object
            Date date = inputFormat.parse(isoDateString);

            // Create a SimpleDateFormat for the output in Indian format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy 'at' hh:mm:ss a", new Locale("en", "IN"));
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            // Format the date to the desired output
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Date";
        }
    }

}
