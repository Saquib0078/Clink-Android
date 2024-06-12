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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.ui.activity.MainActivity;
import com.nirmiteepublic.clink.ui.activity.pages.CreateMeetActivity;
import com.nirmiteepublic.clink.ui.activity.pages.FilterUserActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.annotation.Nullable;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "C-Link-Channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null) {
            Map<String, String> data = message.getData();
            String title = message.getNotification().getTitle(); // Get notification title
            String body = message.getNotification().getBody();
            String imageUrl = data.get("imageUrl");

//            String type = data.get("msgtype");
//
//            System.out.println(type);

            showNotification(imageUrl,title,body);

            SharedPreferences prefs = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("notificationStatus", true).apply();
            getFirebaseMessage(message.getNotification().getTitle(), message.getNotification().getBody(),imageUrl);

        }

    }

    public void getFirebaseMessage(String title, String msg,String img) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "C-Link")
                .setSmallIcon(R.drawable.broadcast)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true);
        NotificationManagerCompat compat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            return;
        }
        compat.notify(101, builder.build());
    }

    private void showNotification(String imageUrl ,String title ,String body) {
        // Create a notification channel (only needs to be done once)
        createNotificationChannel();

        // Create an Intent for the notification tap action
        Intent intent = new Intent(this, MainActivity.class);
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
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
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
