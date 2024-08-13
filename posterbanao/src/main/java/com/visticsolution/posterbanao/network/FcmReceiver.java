//package com.visticsolution.posterbanao.network;
//
//import android.annotation.SuppressLint;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.util.Log;
//import android.widget.RemoteViews;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.visticsolution.posterbanao.MainActivity;
//import com.visticsolution.posterbanao.R;
//import com.visticsolution.posterbanao.classes.Functions;
//import com.visticsolution.posterbanao.classes.Variables;
//
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//import java.security.SecureRandom;
//import java.util.Map;
//import java.util.Random;
//
//import javax.net.ssl.HttpsURLConnection;
//
//@SuppressLint("MissingFirebaseInstanceTokenRefresh")
//public class FcmReceiver extends FirebaseMessagingService {
//    private String bigpicture = null;
//    public static final String CHANNEL_ID = "notify";
//    String title, message;
//    public static String TV_CHANNEL_ID = "";
//    public static String TV_CAT_ID = "";
//    public static String external_link = "false";
//
//    @Override
//    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
//        if (!s.equals(Functions.getDeviceToken(getApplicationContext()))) {
//            Functions.setDeviceToken(getApplicationContext(),s);
//            if (Functions.isLogin(getApplicationContext())){
//                Functions.updateDeviceTokenToServer(Functions.getDeviceToken(getApplicationContext()),s);
//            }
//        }
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (remoteMessage.getData().size() > 0) {
//            Map<String, String> params = remoteMessage.getData();
//            JSONObject object = new JSONObject(params);
//
//            try {
//                title = object.getString("title");
//                message = object.getString("message");
//                TV_CAT_ID = object.getString("cat_id");
//                bigpicture = object.getString("big_picture");
//            } catch (Exception e) {
//                Log.e("Notify Error", "Exception: " + e.getMessage());
//            }
//
//            if (bigpicture != null && !bigpicture.equals("")){
//                sendNotification(title, message, external_link);
//            }
//        }
//    }
//
//    @SuppressLint("WrongConstant")
//    private void sendNotification(String title, String msg, String url) {
//        FcmReceiver.createNotificationChannel(this);
//        Intent intent;
//        if (!url.equals("false") && !url.trim().isEmpty()) {
//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(url));
//        } else {
//            intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        Bitmap posterBitmap = getBitmapFromURL(bigpicture);
//
//        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.custome_notification_expend);
//
//        expandedView.setImageViewBitmap(R.id.iv,posterBitmap);
//
//        Log.d("setImageViewUri___",Functions.getSharedPreference(this).getString("P_PATH",""));
//        if (new File(Functions.getSharedPreference(this).getString("P_PATH","")).exists()){
//            expandedView.setImageViewUri(R.id.profilePic,Uri.parse(Functions.getSharedPreference(this).getString("P_PATH","")));
//        }else {
//            expandedView.setImageViewResource(R.id.profilePic,R.drawable.logo);
//        }
//
//        expandedView.setTextViewText(R.id.description,title+" "+msg);
//        expandedView.setTextViewText(R.id.user_name,Functions.getSharedPreference(this).getString(Variables.NAME,getString(R.string.app_name)));
//        expandedView.setTextViewText(R.id.designation,Functions.getSharedPreference(this).getString(Variables.U_DESIGNATION,getString(R.string.app_name)));
//        expandedView.setTextViewText(R.id.number,Functions.getSharedPreference(this).getString(Variables.NUMBER,getString(R.string.app_name)));
//
//        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.custome_notification_collapse);
//        collapsedView.setImageViewBitmap(R.id.iv,posterBitmap);
//        collapsedView.setTextViewText(R.id.title,title);
//        collapsedView.setTextViewText(R.id.description,msg);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setSmallIcon(R.drawable.logo)
//                .setSound(uri)
//                .setColor(getColour())
//                .setAutoCancel(true)
//                .setLights(Color.RED, 800, 800)
//                .setContentText("wrf")
//                .setCustomContentView(collapsedView)
//                .setCustomBigContentView(expandedView)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(contentIntent)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());
//
////        mBuilder.setSmallIcon(getNotificationIcon(mBuilder));
////        try {
////            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo));
////        } catch (Exception e) {
////            //Toast.makeText(getApplicationContext(), "errror large- " + e.getMessage(), Toast.LENGTH_LONG).show();
////        }
//
//        mBuilder.setContentTitle("wtf");
//        mBuilder.setTicker(message);
//
////        if (bigpicture != null) {
////            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(bigpicture)));
////        } else {
////            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
////        }
//        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        mNotificationManager.notify(createRandomCode(7), mBuilder.build());
//    }
//
//    @SuppressLint("ObsoleteSdkInt")
//    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setColor(getColour());
//            return R.drawable.logo;
//        } else {
//            return R.drawable.logo;
//        }
//    }
//
//    private int getColour() {
//        return getResources().getColor(R.color.app_color);
//    }
//
//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            InputStream input;
//            URL url = new URL(src);
//
//            URLConnection conn = new URL(src).openConnection();
//            if (conn instanceof HttpsURLConnection) {
//                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                input = connection.getInputStream();
//            } else {
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                input = connection.getInputStream();
//            }
//            return BitmapFactory.decodeStream(input);
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public int createRandomCode(int codeLength) {
//        char[] chars = "1234567890".toCharArray();
//        StringBuilder sb = new StringBuilder();
//        Random random = new SecureRandom();
//        for (int i = 0; i < codeLength; i++) {
//            char c = chars[random.nextInt(chars.length)];
//            sb.append(c);
//        }
//        return Integer.parseInt(sb.toString());
//    }
//
//    public static void createNotificationChannel(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "All Notifications";
//            String description = "Important Notifications";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(FcmReceiver.CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    public static void initFCM(Context context) {
//        //todo NEED TO FIX
////        FirebaseMessaging.getInstance().getToken()
////                .addOnCompleteListener(task -> {
////                    if (task.isSuccessful()){
////                        String token = task.getResult();
////                        if (!token.equals(Functions.getDeviceToken(context))) {
////                            Functions.setDeviceToken(context, token);
////                            if (Functions.isLogin(context)){
////                                Functions.updateDeviceTokenToServer(Functions.getUID(context), token);
////                            }
////                        }
////                        Log.d("Firebase Token", token);
////                    }
////                });
//    }
//
//}