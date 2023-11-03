//package com.nirmiteepublic.clink.functions.helpers;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import com.bmlntech.mycareersaathi.sarthi.R;
//import com.bmlntech.mycareersaathi.sarthi.ui.activity.pages.SplashActivity;
//import com.google.firebase.messaging.RemoteMessage;
//
//import java.util.Objects;
//
//public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        sendNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody());
//    }
//
//    @Override
//    public void onNewToken(@NonNull String token) {
//        super.onNewToken(token);
//    }
//
//    private void sendNotification(String title, String messageBody) {
//
//        Intent intent = new Intent(this, SplashActivity.class);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        String channelId = "fcm_default_channel";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId).setContentTitle(title).setContentText(messageBody).setAutoCancel(true).setSmallIcon(R.drawable.app_logo).setSound(defaultSoundUri).setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        }
//
//    }
//
//
//}
