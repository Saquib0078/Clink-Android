package com.visticsolution.posterbanao.responses;

import com.google.gson.annotations.SerializedName;

public class StripeResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("publishableKey")
    public String publishableKey;

    @SerializedName("clientSecret")
    public String clientSecret;

    @SerializedName("ephemeralKey")
    public String ephemeralKey;

    @SerializedName("customer")
    public String customer;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
