package com.visticsolution.posterbanao.classes;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onesignal.OneSignal;
import com.visticsolution.posterbanao.network.FcmReceiver;


public class App extends Application {

    public static App app;
    private static final String ONESIGNAL_APP_ID = "cea89e87-b211-477c-90c8-84dccf8e77de";

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        if (Functions.getSharedPreference(this).getBoolean(Variables.NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        FcmReceiver.initFCM(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();

    }

    public static int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) app.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    public static int getColumnWidth(int column, float grid_padding) {
        Resources r = app.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth() - ((column + 1) * padding)) / column);
    }

    public static Display getDefaultDisplay() {
        WindowManager wm = (WindowManager) app.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display;
    }

//    private HttpProxyCacheServer proxy;
//    public static HttpProxyCacheServer getProxy(Context context) {
//        App app = (App) context.getApplicationContext();
//        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
//    }
//
//    private HttpProxyCacheServer newProxy() {
//        return new HttpProxyCacheServer.Builder(this)
//                .maxCacheSize(1024 * 1024 * 1024)
//                .maxCacheFilesCount(20)
//                .cacheDirectory(new File(Functions.getAppFolder(this)+"videoCache"))
//                .build();
//    }
}
