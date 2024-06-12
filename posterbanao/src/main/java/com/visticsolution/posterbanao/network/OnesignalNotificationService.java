package com.visticsolution.posterbanao.network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;
import com.visticsolution.posterbanao.HomeActivity;
import com.visticsolution.posterbanao.MainActivity;
import com.visticsolution.posterbanao.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("unused")
public class OnesignalNotificationService implements OneSignal.OSRemoteNotificationReceivedHandler {

    public String action = "";
    public String action_item = "";
    public Context context;
    public static final String CHANNEL_ID = "iqueen_channel";
    public static final int NOTIFICATION_ID = 1;


    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent osNotificationReceivedEvent) {
        this.context = context;

        OSNotification notification = osNotificationReceivedEvent.getNotification();

        OSMutableNotification mutableNotification = notification.mutableCopy();
        mutableNotification.setExtender(builder -> {
            // Sets the accent color to Green on Android 5+ devices.
            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
            builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            // Sets the notification Title to Red
            Spannable spannableTitle = new SpannableString(notification.getTitle());
            spannableTitle.setSpan(new ForegroundColorSpan(Color.RED), 0, notification.getTitle().length(), 0);
            builder.setContentTitle(spannableTitle);
            // Sets the notification Body to Blue
            Spannable spannableBody = new SpannableString(notification.getBody());
            spannableBody.setSpan(new ForegroundColorSpan(Color.BLUE), 0, notification.getBody().length(), 0);
            builder.setContentText(spannableBody);
            //Force remove push from Notification Center after 30 seconds
            builder.setTimeoutAfter(30000);
            return builder;
        });
        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        JSONObject data = notification.getAdditionalData();
        try {
            action = data.getString("action");
            action_item = data.getString("action_item");
        } catch (JSONException e) {
            Log.d("OnesignalNotificationService__",e.getMessage());
        }
        osNotificationReceivedEvent.complete(null);
        sendNotification(notification);
    }

    private void sendNotification(OSNotification notification) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;

        intent = new Intent(context, MainActivity.class);
        HomeActivity.action = action;
        HomeActivity.action_item = action_item;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "QuotesPush";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(uri)
                .setLights(Color.RED, 800, 800)
                .setContentText(notification.getBody())
                .setChannelId(CHANNEL_ID);

        mBuilder.setSmallIcon(getNotificationIcon(mBuilder));

        try {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "errror large- " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (notification.getTitle().trim().isEmpty()) {
            mBuilder.setContentTitle(context.getString(R.string.app_name));
            mBuilder.setTicker(context.getString(R.string.app_name));
        } else {
            mBuilder.setContentTitle(notification.getTitle());
            mBuilder.setTicker(notification.getTitle());
        }

        if (notification.getBigPicture() != null) {
            mBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        }

        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColour());
        }
        return R.mipmap.ic_launcher;
    }

    private int getColour() {
        return 0x8b5630;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            InputStream input;
            if (src.contains("https://")) {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } else {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            }
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }
}
