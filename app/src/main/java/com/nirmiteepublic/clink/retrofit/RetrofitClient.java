package com.nirmiteepublic.clink.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    /* For Live Testing */
//    private static final String BASE_URL = "http://192.168.1.7:3000/";

    /* For Real Use*/
    private static final String BASE_URL = new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 99, 97, 114, 101, 101, 114, 115, 97, 116, 104, 105, 46, 97, 110, 100, 114, 111, 99, 108, 117, 115, 116, 101, 114, 46, 99, 111, 109, 47});

    /* For local Use*/
//    private static final String BASE_URL = "https://guided-chow-ghastly.ngrok-free.app/";

    public static final String IMAGE_BASE_URL = BASE_URL + "getImage/";
    public static final String PDF_BASE_URL = BASE_URL + "getPdf/";
    private static RetrofitClient retrofitClient;
    private final ApiInterfaces apiInterfaces;

    public RetrofitClient() {


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiInterfaces = retrofit.create(ApiInterfaces.class);
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public ApiInterfaces getApiInterfaces() {
        return apiInterfaces;
    }

}
