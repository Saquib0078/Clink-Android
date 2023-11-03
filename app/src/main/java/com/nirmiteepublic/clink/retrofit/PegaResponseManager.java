package com.nirmiteepublic.clink.retrofit;


import com.nirmiteepublic.clink.retrofit.utility.UpdateData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Sahil The Geek,
 * Date : 27-08-2023.
 *
 * <p>This {@link PegaResponseManager} manages the default responses from server</p>
 */
public class PegaResponseManager {

    /**
     * Manages the response from the server
     */
    public void manageOnResponse(Response<ResponseBody> response, PegaResponseCallback callback) {
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

                    String code = responseObject.getString("code");

                    switch (code) {
                        case "001":
                            callback.onEmailExists();
                            break;
                        case "002":
                            callback.onInvalidReferCode();
                            break;
                        case "003":
                            callback.onEmailNotExists();
                            break;
                        case "004":
                            callback.onIncorrectPassword();
                            break;
                        case "005":
                            callback.onAccountBanned();
                            break;
                        case "006":
                            callback.onAppError();
                            break;
                        case "007":
                            callback.onSessionExpired();
                            break;
                        case "008":
                            callback.onInvalidEmail();
                            break;
                        case "009":
                        case "010":
                        case "013":
                            callback.onInvalidOTP();
                            break;
                        case "011":
                            callback.onUnverifiedAccount();
                            break;
                        case "012":
                            callback.onOTPExpired();
                            break;
                        case "014":
                            callback.onMaintenance(responseObject.getString("msg"));
                            break;
                        case "015":
                            JSONObject data = responseObject.getJSONObject("data");
                            callback.onUpdate(new UpdateData(data.getString("u"), data.getString("v")));
                            break;
                        case "016":
                            callback.onUnexpectedResponse();
                            break;
                        case "017":
                            callback.onAlreadyUsedTransactionID();
                            break;
                        case "018":
                            callback.onAlreadyReferProgramJoined();
                            break;
                        case "019":
                            callback.onReferProgramNotJoined();
                            break;
                        case "020":
                            callback.onInsufficientMoney();
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
                        callback.onSuccess(responseObject.getJSONArray("data"));
                        return;
                    }
                    callback.onSuccess((JSONObject) null);
                    callback.onSuccess((JSONArray) null);
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
    public void manageOnFailure(Throwable throwable, PegaResponseCallback callback) {
        callback.onError(throwable);
    }
}
