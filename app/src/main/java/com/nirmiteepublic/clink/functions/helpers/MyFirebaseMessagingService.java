package com.nirmiteepublic.clink.functions.helpers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

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
import com.nirmiteepublic.clink.ui.fragments.TaskFragment;
import com.nirmiteepublic.clink.ui.fragments.viewpager.MeetingFragment;

import java.util.Objects;

import javax.annotation.Nullable;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "C-Link-Channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData() != null) {

            String meetingType = message.getData().get("meetingType");
            if ("meeting".equals(meetingType) || "task".equals(meetingType) || Objects.equals(meetingType, "normal")) {
                String title = message.getData().get("title");
                String body = message.getData().get("body");
                String imageUrl = message.getData().get("imageUrl");
                String relatedId = message.getData().get("relatedId");

                showNotification(imageUrl, title, body, meetingType, relatedId);

                SharedPreferences prefs = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
                prefs.edit().putBoolean("notificationStatus", true).apply();
                prefs.edit().putString("last_notification_id", message.getData().get("relatedId")).apply();
                prefs.edit().putString("last_notification_type", meetingType).apply();
                prefs.edit().putString("last_notification_description", body).apply();
                prefs.edit().putString("last_notification_title", title).apply();
                prefs.edit().putString("last_notification_image", imageUrl).apply();
            }


        }
    }

    private void showNotification(String imageUrl, String title, String body, String meetingType, String relatedId) {
        // Create a notification channel (only needs to be done once)
        createNotificationChannel();

        // Create an Intent for the notification tap action
        Intent intent = new Intent(this, MainActivity.class);  // Default intent
        if ("meeting".equals(meetingType)) {
            if (relatedId != null && !relatedId.isEmpty()) {
                intent = new Intent(this, MeetdescryptionActivity.class);
            }
            intent.putExtra("meetImage", imageUrl);
            intent.putExtra("meetDescryption", body);
            intent.putExtra("meetTime", title);
            intent.putExtra("meetid", relatedId);
            intent.putExtra("type", meetingType);
        } else if ("task".equals(meetingType)) {
            if (relatedId != null && !relatedId.isEmpty()) {
                intent = new Intent(this, TaskDescryptionActivity.class);
            }
            intent.putExtra("taskID", relatedId);
            intent.putExtra("imageID", imageUrl);
            intent.putExtra("type", meetingType);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,  // Request code, can be 0
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Build your notification with the loaded image
                        buildNotification(resource, pendingIntent, title, body);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Perform any additional operations if needed
                    }

                    @Override
                    public void onLoadFailed(@androidx.annotation.Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        buildNotification(null, pendingIntent, title, body);
                    }
                });
    }

    private void buildNotification(@NonNull Bitmap resource, PendingIntent pendingIntent, String title, String body) {

        Uri customSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.iphone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.network)

                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)  // Set the PendingIntent here
                .setAutoCancel(true)
                .setLargeIcon(resource)
                .setSound(customSoundUri)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource).bigLargeIcon(null));

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
        if (ActivityCompat.checkSelfPermission(MyFirebaseMessagingService.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Handle permission request and result
            return;
        }
        notificationManager.notify(123, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "C-Link Channel";
            String description = "Channel for C-Link notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.iphone), null);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}