package com.nirmiteepublic.clink.functions.utils;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiManager {
    private static RequestQueue requestQueue;

    private static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static void getLiveMeetings(Context context, String jwt, final VolleyCallback callback) {
        String url = RetrofitClient.MEETING_BASE_URL + "liveMeetings/";

        // Make the API call using Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the API response
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        callback.onError(error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add headers including the JWT token
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + jwt);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        getRequestQueue(context).add(jsonRequest);
    }

    public static void getTask(Context context, String jwt, final VolleyCallback callback) {
        String url = RetrofitClient.TASK_BASE_URL + "getTask/";

        // Make the API call using Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the API response
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        callback.onError(error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add headers including the JWT token
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + jwt);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        getRequestQueue(context).add(jsonRequest);
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject response);

        void onError(String errorMessage);
    }
}
