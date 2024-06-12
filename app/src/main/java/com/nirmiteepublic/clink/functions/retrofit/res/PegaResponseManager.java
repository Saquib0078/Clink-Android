package com.nirmiteepublic.clink.functions.retrofit.res;


import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sahil The Geek,
 * Date : 27-08-2023.
 *
 * <p>This {@link PegaResponseManager} manages the default responses from server</p>
 */
public class PegaResponseManager implements Callback<ResponseBody> {

    private final PegaCallback callback;

    public PegaResponseManager(PegaCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        callback.onResponse();
        manageOnResponse(response);
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        callback.onResponse();
        manageOnFailure(t);
    }

    /**
     * Manages the response from the server
     */
    public void manageOnResponse(Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
            try {
                JSONObject responseObject = new JSONObject(response.body().string());

                if (!responseObject.has("status")) {
                    callback.onUnexpectedResponse();
                    return;
                }

                String status = responseObject.getString("status");

                if (status.equals("failed")) {
                    if (!responseObject.has("code")) {
                        callback.onUnexpectedResponse();
                        return;
                    }

                    JSONObject data = responseObject.has("data") ? responseObject.getJSONObject("data") : null;

                    String code = responseObject.getString("code");

                    switch (code) {
                        case "000":
                            callback.onMissingParameter();
                            break;
                        case "001":
                            callback.onInvalidNumber();
                            break;
                        case "002":
                            callback.onInvalidName();
                            break;
                        case "003":
                            callback.onInvalidOTP();
                            break;
                        case "101":
                            callback.onNumberExists();
                            break;
                        case "102":
                            callback.onNumberNotExists();
                            break;
                        case "201":
                            callback.onAccountNotCompleted();
                            break;
                        case "202":
                            callback.onAccountNotApproved();
                            break;
                        case "203":
                            callback.onAccountBanned();
                            break;
                        case "301":
                            callback.onOTPExpired();
                            break;
                        case "302":
                            callback.onSessionExpired();
                            break;
                        case "900":
                            callback.onMaintenance(data);
                            break;
                        case "901":
                            callback.onUpdateRequired(data);
                            break;
                        default:
                            callback.onUnexpectedResponse();
                            break;
                    }

                    return;
                }

                if (status.equals("success")) {
                    if (responseObject.has("data")) {
                        if (responseObject.get("data") instanceof JSONObject) {
                            callback.onSuccess(responseObject.getJSONObject("data"));
                            return;
                        }
                        JSONArray data = responseObject.getJSONArray("data");
                        List<JSONObject> jsonObjectList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            jsonObjectList.add(data.getJSONObject(i));
                        }
                        callback.onSuccess(jsonObjectList.toArray(new JSONObject[0]));
                        callback.onSuccess(responseObject.getJSONArray("data"));

                        if (data.length() > 0) {
                            callback.onSuccess(data.getJSONObject(0));
                            return;
                        }
                        callback.onSuccess((JSONObject) null);
                        return;
                    }
                    callback.onSuccess((JSONObject) null);
                    callback.onSuccess((JSONArray) null);
                    callback.onSuccess(null, null);
                    return;
                }

                callback.onError();

            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            callback.onError(response.code());
        }
    }

    /**
     * Handles Errors
     */
    public void manageOnFailure(Throwable throwable) {
        callback.onError(throwable);
    }


}
