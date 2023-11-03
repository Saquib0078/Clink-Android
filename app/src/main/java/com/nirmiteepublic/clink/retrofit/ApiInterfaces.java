package com.nirmiteepublic.clink.retrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterfaces {

    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/auth/register")
    Call<ResponseBody> register(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/appStatus")
    Call<ResponseBody> appStatus(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/auth/checkEmail")
    Call<ResponseBody> checkEmail(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/auth/validateOTP")
    Call<ResponseBody> validateOTP(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/auth/resendOTP")
    Call<ResponseBody> resendOTP(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/auth/resendOTPByEmail")
    Call<ResponseBody> resendOTPByEmail(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/auth/resetPassword")
    Call<ResponseBody> resetPassword(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/getComments")
    Call<ResponseBody> getComments(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/addComment")
    Call<ResponseBody> addComment(@Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("/getReferProgramDetails")
    Call<ResponseBody> getReferProgramDetails(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/validateAndSetReferCode")
    Call<ResponseBody> validateAndSetReferCode(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/saveTransactionId")
    Call<ResponseBody> saveTransactionId(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/getReferDetails")
    Call<ResponseBody> getReferDetails(@Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("/getTransactionHistory")
    Call<ResponseBody> getTransactionHistory(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/getWithdrawDetails")
    Call<ResponseBody> getWithdrawDetails(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/withdrawUsingUpi")
    Call<ResponseBody> withdrawUsingUpi(@Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("/getDefaultOffers")
    Call<ResponseBody> getDefaultOffers(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/getOwnOffers")
    Call<ResponseBody> getOwnOffers(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/getFriendsOffers")
    Call<ResponseBody> getFriendsOffers(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/setOffer")
    Call<ResponseBody> setOffer(@Body RequestBody requestBody);


}
