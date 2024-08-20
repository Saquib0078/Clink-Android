package com.nirmiteepublic.clink.functions.retrofit.req;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    /* For Live Testing */
//    private static final String BASE_URL = "https://guided-chow-ghastly.ngrok-free.app/";

    /* For Real Use*/

    /* For local Use*/
    //
//    https://clink-production.up.railway.app/
    //"http://192.168.1.9:3000/"
//    public static final String BASE_URL = "http://192.168.1.8:3000/"; latesttttttttttttttt
    //118.139.167.71
//    public static final String BASE_URL = "http://networkdrive.info:8000/";
    public static final String BASE_URL = "http://networkdrive.info:8000/";
    public static final String MEDIA_BASE_URL = BASE_URL + "media/";
    public static final String USER_BASE_URL = BASE_URL + "user/";
    //    private static final String BASE_URL = "https://clink-production.up.railway.app/";
    public static final String PROFILE_IMAGE = USER_BASE_URL + "getUsermedia/";
    public static final String VIDEO_BASE_URL = BASE_URL + "user/" + "getUsermedia/";
    public static final String MEETING_BASE_URL = BASE_URL + "meeting/";
    public static final String MEETIMAGE_BASE_URL = MEETING_BASE_URL + "getmeeting/";
    public static final String BROADCAST_URL = BASE_URL + "user/getUsermedia/";
    public static final String BROADCAST_URL_1 = BASE_URL + "user/getBroadcastMedia/";
    public static final String TASK_BASE_URL = BASE_URL + "task/";
    public static final String TASKIMAGE_BASE_URL = TASK_BASE_URL + "getTask/";
    private static RetrofitClient retrofitClient;
    private final ApiInterfaces apiInterfaces;

    public RetrofitClient(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new JwtInterceptor(context));

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();

        apiInterfaces = retrofit.create(ApiInterfaces.class);
    }

    public static RetrofitClient getRetrofitClient() {
        return retrofitClient;
    }

    public static void setRetrofitClient(RetrofitClient retrofitClient) {
        RetrofitClient.retrofitClient = retrofitClient;
    }

    public static RetrofitClient getInstance(Context context) {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient(context);
        }
        return retrofitClient;
    }

    public ApiInterfaces getApiInterfaces() {
        return apiInterfaces;
    }

}
