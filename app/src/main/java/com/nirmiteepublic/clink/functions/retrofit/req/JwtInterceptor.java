package com.nirmiteepublic.clink.functions.retrofit.req;

import android.content.Context;

import androidx.annotation.NonNull;

import com.nirmiteepublic.clink.functions.helpers.Prefs;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    private final Prefs prefs;

    public JwtInterceptor(Context context) {
        prefs = new Prefs(context);
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (prefs.getJWT().equals(Prefs.NO_DATA)) {
            return chain.proceed(originalRequest);
        }

        Request modifiedRequest = originalRequest.newBuilder().header("Authorization", "Bearer " + prefs.getJWT()).build();

        return chain.proceed(modifiedRequest);
    }
}

