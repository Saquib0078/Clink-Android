package com.visticsolution.posterbanao.classes;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.visticsolution.posterbanao.dialog.CustomeDialogFragment;
import com.visticsolution.posterbanao.dialog.DialogType;
import com.visticsolution.posterbanao.model.UserModel;
//import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
//import com.omega_r.libs.omegaintentbuilder.downloader.DownloadCallback;
//import com.omega_r.libs.omegaintentbuilder.handlers.ContextIntentHandler;
import com.visticsolution.posterbanao.BuildConfig;
import com.visticsolution.posterbanao.MainActivity;
import com.visticsolution.posterbanao.model.BussinessModel;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.SimpleResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;


public class Functions {

    public static Dialog dialog;

    public static void showLoader(Context context) {
        try {
            if (dialog != null) {
                cancelLoader();
                dialog = null;
            }
            {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.item_dialog_loading_view);
                dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_bg));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
            }

        } catch (Exception e) {
            Log.d(Constants.tag, "Exception : " + e);
        }
    }




    public static void loaderError(Callback callback) {
        try {
            if (dialog != null || dialog.isShowing()) {
                dialog.findViewById(R.id.loader).setVisibility(View.GONE);
                dialog.findViewById(R.id.tryagainLay).setVisibility(View.VISIBLE);
                dialog.findViewById(R.id.tryAgainBtn).setOnClickListener(view -> {
                    callback.Responce("click");
                });
                dialog.setCancelable(true);
            }else{
                dialog.findViewById(R.id.loader).setVisibility(View.VISIBLE);
                dialog.findViewById(R.id.tryagainLay).setVisibility(View.GONE);
                dialog.setCancelable(false);
            }
        } catch (Exception e) {
            Log.d(Constants.tag, "Exception : " + e);
        }
    }

    public static void cancelLoader() {
        try {
            if (dialog != null || dialog.isShowing()) {
                dialog.cancel();
            }
        } catch (Exception e) {
            Log.d(Constants.tag, "Exception : " + e);
        }
    }

    public static void PrintHashKey(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d(Constants.tag, "KeyHash : " + hashKey);
            }
        } catch (Exception e) {
        }
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static HashMap<String, String> getHeaders(Context context) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("User-Id", getSharedPreference(context).getString(Variables.U_ID, null));
        headers.put("Auth-Token", getSharedPreference(context).getString(Variables.AUTH_TOKEN, null));
        headers.put("device", "android");
        headers.put("version", "1.0");
        headers.put("ip", getSharedPreference(context).getString(Variables.DEVICE_IP, null));
        headers.put("device-token", getSharedPreference(context).getString(Variables.DEVICE_TOKEN, null));
        return headers;
    }

    public static SharedPreferences getSharedPreference(Context context) {
        if (Variables.sharedPreferences == null)
            Variables.sharedPreferences = context.getSharedPreferences(Variables.PREF_NAME, MODE_PRIVATE);
        return Variables.sharedPreferences;
    }

    public static boolean setDeviceToken(Context context, String value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Variables.PREF_NAME, MODE_PRIVATE).edit();
        edit.putString(Variables.DEVICE_TOKEN, value);
        return edit.commit();
    }

    public static String getDeviceToken(Context context) {
        return context.getSharedPreferences(Variables.PREF_NAME, MODE_PRIVATE).getString(Variables.DEVICE_TOKEN, "");
    }

    public static String getUID(Context ctx) {
        return getSharedPreference(ctx).getString(Variables.U_ID, "3");
    }

    public static String getItemBaseUrl(String url) {
        if (url != null && !url.contains(Variables.http)) {
            url = Constants.BASE_URL + url;
        }
        return url;
    }

    public static String removeSpecialChar(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static void showToast(Activity context, String msg) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.item_toast, (ViewGroup) context.findViewById(R.id.toast_container));
        TextView tv = (TextView) layout.findViewById(R.id.toast_text);
        tv.setText(msg);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    public static String getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return "blocked";
            }
            return "denied";
        }
        return "granted";
    }

    public static File createImageFile(Context context) throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.ENGLISH).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }


    public static void showPermissionSetting(Activity context, String message) {
        new CustomeDialogFragment(context.getString(R.string.permission_alert),
                message,
                DialogType.WARNING,
                true,
                false,
                true,
                new CustomeDialogFragment.DialogCallback() {
                    @Override
                    public void onCencel() {
                    }

                    @Override
                    public void onSubmit() {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onDismiss() {
                    }

                    @Override
                    public void onComplete(Dialog dialog) {

                    }
                });
    }

    public static String bitmapToBase64(Bitmap imagebitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.PNG, 99, baos);
        byte[] byteArray = baos.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

    public static void setLocale(String lang, Activity context, Class<?> className, boolean isRefresh) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = new Configuration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        context.onConfigurationChanged(conf);
        if (isRefresh) {
            updateActivity(context, className);
        }
    }

    public static void updateActivity(Activity context, Class<?> className) {
        Intent intent = new Intent(context, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getAppFolder(Context activity) {
        return activity.getExternalFilesDir(null).getPath() + "/";
    }

    public static boolean IsPremiumEnable(Context context) {
        return getSharedPreference(context).getString(Variables.SUB_NAME, "0").length() > 4;
    }

    public static boolean isLogin(Context context) {
        return getSharedPreference(context).getBoolean(Variables.IS_LOGIN, false);
    }

    public static void saveUserData(UserModel userObject, Context context) {
        getSharedPreference(context).edit().putBoolean(Variables.IS_LOGIN, true).apply();
        getSharedPreference(context).edit().putString(Variables.U_ID, userObject.getId()).apply();
        getSharedPreference(context).edit().putString(Variables.NAME, userObject.getName()).apply();
        getSharedPreference(context).edit().putString(Variables.P_PIC, userObject.getProfile_pic()).apply();
        getSharedPreference(context).edit().putString(Variables.U_EMAIL, userObject.getEmail()).apply();
        getSharedPreference(context).edit().putString(Variables.NUMBER, userObject.getNumber()).apply();
        getSharedPreference(context).edit().putString(Variables.U_DESIGNATION, userObject.getDesignation()).apply();

        getSharedPreference(context).edit().putString(Variables.BALANCE, userObject.getBalance()).apply();
        getSharedPreference(context).edit().putString(Variables.REFERD, userObject.getRefered()).apply();
        getSharedPreference(context).edit().putString(Variables.REFER_ID, userObject.getRefer_id()).apply();
        getSharedPreference(context).edit().putString(Variables.STATE, userObject.getState()).apply();
        getSharedPreference(context).edit().putString(Variables.DISTRICT, userObject.getDistrict()).apply();
        getSharedPreference(context).edit().putString(Variables.CATEGORY_ID, userObject.getCategory_id()).apply();

        getSharedPreference(context).edit().putString(Variables.SUB_ID, userObject.getSubscription_price()).apply();
        getSharedPreference(context).edit().putString(Variables.SUB_PRICE, userObject.getSubscription_price()).apply();
        getSharedPreference(context).edit().putString(Variables.SUB_NAME, userObject.getSubscription_name()).apply();
        getSharedPreference(context).edit().putString(Variables.SUB_DATE, userObject.getSubscription_date()).apply();
        getSharedPreference(context).edit().putString(Variables.SUB_END_DATE, userObject.getSubscription_end_date()).apply();

        getSharedPreference(context).edit().putString(Variables.ACTIVE_AT, userObject.getActive_at()).apply();
        getSharedPreference(context).edit().putInt(Variables.DAILY_LIMIT, userObject.getDaily_limit()).apply();

        getSharedPreference(context).edit().putInt(Variables.FESTIVAL, userObject.getFestival()).apply();
        getSharedPreference(context).edit().putInt(Variables.BUSINESS, userObject.getBusiness()).apply();
        getSharedPreference(context).edit().putInt(Variables.POLITICAL, userObject.getPolitical()).apply();
        getSharedPreference(context).edit().putInt(Variables.VIDEO, userObject.getVideo()).apply();

        getSharedPreference(context).edit().putInt(Variables.POSTS_LIMIT, userObject.getPosts_limit()).apply();
        getSharedPreference(context).edit().putInt(Variables.BUSINESS_LIMIT, userObject.getBusiness_limit()).apply();
        getSharedPreference(context).edit().putInt(Variables.POLITICAL_LIMIT, userObject.getPolitical_limit()).apply();

        getSharedPreference(context).edit().putString(Variables.U_SOCIAL_ID, userObject.getSocial_id()).apply();
        getSharedPreference(context).edit().putString(Variables.SOCIAL, userObject.getSocial()).apply();
        getSharedPreference(context).edit().putString(Variables.AUTH_TOKEN, userObject.getAuth_token()).apply();
        getSharedPreference(context).edit().putString(Variables.DEVICE_TOKEN, userObject.getDevice_token()).apply();
        getSharedPreference(context).edit().putString(Variables.CREATED, userObject.getCreated_at()).apply();
    }


    public static void saveBussinessData(BussinessModel bussiness, Context context) {
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_NAME, bussiness.getCompany()).apply();
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_TYPE, "Business").apply();
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_CATEGORY_NAME, bussiness.getCategory().getName()).apply();
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_CATEGORY_ID, bussiness.getCategory().getId()).apply();

        getSharedPreference(context).edit().putString(Variables.BUSSINESS_ID, bussiness.getId()).apply();
        getSharedPreference(context).edit().putString(Variables.BUSSINESS_NAME, bussiness.getCompany()).apply();
        getSharedPreference(context).edit().putString(Variables.BUSSINESS_OWNER, bussiness.getName()).apply();
        getSharedPreference(context).edit().putString(Variables.BUSSINESS_EMAIL, bussiness.getEmail()).apply();
        getSharedPreference(context).edit().putString(Variables.BUSSINESS_NUMBER, bussiness.getNumber()).apply();
        getSharedPreference(context).edit().putString(Variables.BUSSINESS_ADDRESS, bussiness.getAddress()).apply();
        getSharedPreference(context).edit().putString(Variables.WEBSITE, bussiness.getWebsite()).apply();
        getSharedPreference(context).edit().putString(Variables.BUSSINESS_LOGO, bussiness.getImage()).apply();
        getSharedPreference(context).edit().putString(Variables.BUSSINESS_ABOUT, bussiness.getAbout()).apply();
        getSharedPreference(context).edit().putString(Variables.INSTAGRAM, bussiness.getInstagram()).apply();
        getSharedPreference(context).edit().putString(Variables.YOUTUBE, bussiness.getYoutube()).apply();
        getSharedPreference(context).edit().putString(Variables.FACEBOOK, bussiness.getFacebook()).apply();
        getSharedPreference(context).edit().putString(Variables.WHATSAPP, bussiness.getWhatsapp()).apply();
    }

    public static void savePoliticalData(BussinessModel bussiness, Context context) {
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_TYPE, "Political").apply();
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_NAME, bussiness.getName()).apply();
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_CATEGORY_NAME, bussiness.getCategory().getName()).apply();
        getSharedPreference(context).edit().putString(Variables.ACTIVE_PROFILE_CATEGORY_ID, bussiness.getCategory().getId()).apply();

        getSharedPreference(context).edit().putString(Variables.POLITICAL_ID, bussiness.getId()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_NAME, bussiness.getName()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_EMAIL, bussiness.getEmail()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_DESIGNATION, bussiness.getDesignation()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_NUMBER, bussiness.getNumber()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_LOGO, bussiness.getImage()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_INSTAGRAM, bussiness.getInstagram()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_YOUTUBE, bussiness.getYoutube()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_FACEBOOK, bussiness.getFacebook()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_TWITTER, bussiness.getTwitter()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_WHATSAPP, bussiness.getWhatsapp()).apply();

        getSharedPreference(context).edit().putString(Variables.POLITICAL_CATEGORY_ID, bussiness.getCategory().getId()).apply();
        getSharedPreference(context).edit().putString(Variables.POLITICAL_CATEGORY_LOGO, bussiness.getCategory().getImage()).apply();
    }

    public static void shareApp(Context context) {
        Functions.showLoader(context);
        File directory = new File(getAppFolder(context) + Variables.APP_HIDED_FOLDER);
        String filename = System.currentTimeMillis() + ".jpg";
        AndroidNetworking.download(getItemBaseUrl(getSharedPreference(context).getString("share_image_url", "")), directory.getPath(), filename).build().startDownload(new DownloadListener() {
            public void onDownloadComplete() {
                Functions.cancelLoader();
                String shareText = getSharedPreference(context).getString("share_text", "").replace("REFER_CODE", getSharedPreference(context).getString(Variables.REFER_ID, ""));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, "com.nirmiteepublic.clink" + ".provider", new File(directory.getPath() + "/" + filename)));
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
                context.startActivity(intent);
            }

            public void onError(ANError aNError) {
                Functions.cancelLoader();
            }
        });
    }

    public static void rateApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void logOut(Activity context) {
        new AlertDialog.Builder(context)
                .setMessage("Do you really want to logout ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreference(context).edit().clear().apply();
                        context.startActivity(new Intent(context, MainActivity.class));
                        context.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static String getFormatedDate(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static long getDuration(Context context, String path) {
        MediaPlayer mMediaPlayer = null;
        long duration = 0;
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, Uri.parse(path));
            mMediaPlayer.prepare();
            duration = mMediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.release();
            }
        }
        return duration;
    }

    public static String saveImage(Bitmap paramBitmap, Context context) {
        File directory = new File(getAppFolder(context) + Variables.APP_HIDED_FOLDER);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, System.currentTimeMillis() + ".png");
        if (file.exists()) {
            file.delete();
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            return file.getAbsolutePath();
        } catch (Exception e) {
            return "";
        }
    }

    public static int getSubsIntervel(Date startDate, Date endDate) {
        long totalDuration = endDate.getTime() - startDate.getTime();
        Date currentTime = new Date();
        long currentDuration = currentTime.getTime() - startDate.getTime();

        final double percentage = currentDuration / ((double) totalDuration);
        int finalPr = (int) (percentage * 100);

        return finalPr;
    }

    public static void downloadMusicFile(Context context, String musicPath, Callback callback) {
        File directory = new File(getAppFolder(context) + Variables.APP_HIDED_FOLDER);
        String filename = System.currentTimeMillis() + ".mp3";
        AndroidNetworking.download(musicPath, directory.getPath(), filename).build().startDownload(new DownloadListener() {
            public void onDownloadComplete() {
                callback.Responce(directory.getPath() + "/" + filename);
            }

            public void onError(ANError aNError) {
                callback.Responce(null);
            }
        });
    }


    public static void downloadVideoFile(Context context, String musicPath, Callback callback) {
        File directory = new File(getAppFolder(context) + Variables.APP_HIDED_FOLDER);
        String filename = System.currentTimeMillis() + ".mp4";
        AndroidNetworking.download(musicPath, directory.getPath(), filename).build().startDownload(new DownloadListener() {
            public void onDownloadComplete() {
                callback.Responce(directory.getPath() + "/" + filename);
            }

            public void onError(ANError aNError) {
                callback.Responce(null);
            }
        });
    }

    public static void fadeIn(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghjiklmnopqrstuvwxyz".length());
            builder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghjiklmnopqrstuvwxyz".charAt(character));
        }
        return builder.toString();
    }

    public static Bitmap viewToBitmap(View view) {
        Bitmap createBitmap = null;
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        try {
            createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(createBitmap));
            return createBitmap;
        } catch (Exception e) {
            return createBitmap;
        } finally {
            view.destroyDrawingCache();
        }
    }

    public static void updateDeviceTokenToServer(String user_id, String token) {
        ApiClient.getRetrofit().create(ApiService.class).updateDeviceToken(Constants.API_KEY, user_id, token)
                .enqueue(new retrofit2.Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {

                    }
                });
    }
}
