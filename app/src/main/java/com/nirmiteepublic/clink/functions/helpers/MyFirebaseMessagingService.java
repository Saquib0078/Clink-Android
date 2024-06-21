package com.nirmiteepublic.clink.functions.helpers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.ui.activity.MainActivity;
import com.nirmiteepublic.clink.ui.activity.pages.MeetdescryptionActivity;
import com.nirmiteepublic.clink.ui.activity.pages.TaskDescryptionActivity;

import java.util.Map;

import javax.annotation.Nullable;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "C-Link-Channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null) {

            String meetingType = message.getData().get("meetingType");
            if ("meeting".equals(meetingType) || "task".equals(meetingType)) {
                String title = message.getNotification().getTitle();
                String body = message.getNotification().getBody();
                String imageUrl = message.getData().get("imageUrl");

                showNotification(imageUrl, title, body, meetingType);
            }

            SharedPreferences prefs = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("notificationStatus", true).apply();
        }
    }

    private void showNotification(String imageUrl, String title, String body, String meetingType) {
        // Create a notification channel (only needs to be done once)
        createNotificationChannel();

        // Create an Intent for the notification tap action
        Intent intent;
        if ("meeting".equals(meetingType)) {
            intent = new Intent(this, MeetdescryptionActivity.class);
        } else if ("task".equals(meetingType)) {
            intent = new Intent(this, TaskDescryptionActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,  // Request code, can be 0
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Load the image using Glide
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Build your notification with the loaded image
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.broadcast)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)  // Set the PendingIntent here
                                .setAutoCancel(true)
                                .setLargeIcon(resource)
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource).bigLargeIcon(null));

                        // Show the notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
                        if (ActivityCompat.checkSelfPermission(MyFirebaseMessagingService.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Handle permission request and result
                            return;
                        }
                        notificationManager.notify(123, builder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Perform any additional operations if needed
                    }
                });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "C-Link Channel";
            String description = "Channel for C-Link notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}