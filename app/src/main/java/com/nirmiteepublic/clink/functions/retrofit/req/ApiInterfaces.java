package com.nirmiteepublic.clink.functions.retrofit.req;


import com.google.gson.JsonObject;
import com.nirmiteepublic.clink.models.ApiResponse;
import com.nirmiteepublic.clink.models.ChipBtnResponse;
import com.nirmiteepublic.clink.models.GraphicModelResponse;
import com.nirmiteepublic.clink.models.GraphicsModelGson;
import com.nirmiteepublic.clink.models.MeetModel;
import com.nirmiteepublic.clink.models.MergedUserResponse;
import com.nirmiteepublic.clink.models.NotificationModel;
import com.nirmiteepublic.clink.models.OtpResponse;
import com.nirmiteepublic.clink.models.Response;
import com.nirmiteepublic.clink.models.RoleResponse;
import com.nirmiteepublic.clink.models.ShortenRequest;
import com.nirmiteepublic.clink.models.ShortenResponse;
import com.nirmiteepublic.clink.models.SliderImageResponse;
import com.nirmiteepublic.clink.models.TaskModelResponse;
import com.nirmiteepublic.clink.models.UserCount;
import com.nirmiteepublic.clink.models.UserCountResponse;
import com.nirmiteepublic.clink.models.UserModelPrimary;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaces {


    @Headers("Content-Type: application/json")
    @GET("/user/getRole")
    Call<RoleResponse> getAllRoles();

    @Headers("Content-Type: application/json")
    @DELETE("/user/deleteRole/{id}")
    Call<Void> deleteRole(@Path("id") String roleId);

    @Headers("Content-Type: application/json")
    @POST("/user/postRole")
    Call<ResponseBody> createRole(@Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("/app/appStatus/v/{version}")
    Call<ResponseBody> appStatus(@Path("version") int version);


    @Headers("Content-Type: application/json")
    @POST("user/api/shorten")
    Call<ShortenResponse> shortenUrl(@Body ShortenRequest request);

    @Headers("Content-Type: application/json")
    @POST("/user/auth/checkRegister")
    Call<ResponseBody> checkRegister(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getUserDetails/{id}")
    Call<ResponseBody> getUser(@Path("id") String userId);

    @Headers("Content-Type: application/json")
    @GET("/user/getnum")
    Call<List<String>> getPhoneNumbers();
    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getNetworkUser/{id}")
    Call<ResponseBody> getNetworkDetail(@Path("id") String userId);

    @Headers("Content-Type: application/json")
    @GET("/graphics/getGraphicsMedia")
    Call<GraphicsModelGson> getGraphics();


    @Headers("Content-Type: application/json")
    @GET("/user/getOtp")
    Call<OtpResponse> getOtps();

    @Multipart
    @POST("/meeting/meeting")
    Call<ResponseBody> uploadMeetDetails(
            @Part("meetName") RequestBody meetName,
            @Part("meetDescription") RequestBody meetDescription,
            @Part("date") RequestBody date,
            @Part("time") RequestBody time,
            @Part MultipartBody.Part imageID
    );

    @Headers("Content-Type: application/json")
    @PUT("/meeting/updateLive/{id}")
    Call<ResponseBody> updateLive(
            @Path("id") String meetid,
            @Body Map<String, Boolean> live
    );

    @Headers("Content-Type: application/json")
    @GET("/meeting/liveMeetings/{id}")
    Call<MeetModel> getMeetById(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getUsers")
    Call<ApiResponse> getUSers();


    @Headers("Content-Type: application/json")
    @GET("/user/getUserAll")
    Call<MergedUserResponse> getUSER();

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getNotification")
    Call<NotificationModel> getNotification();

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getNetworks")
    Call<ResponseBody> getNotificationUsers();

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/user-counts")
    Call<UserCount> getUserLocation();

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/TotalUsers")
    Call<UserCountResponse> getUserCount();

    @Headers("Content-Type: application/json")
    @POST("/user/auth/register")
    Call<ResponseBody> register(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @PUT("/user/controllers/user/{id}")
    Call<ResponseBody> updateUserById(
            @Path("id") String userId,
            @Body Map<String, Object> updatedFields
    );

    @Multipart
    @PUT("/user/controllers/users/{id}")
    Call<ResponseBody> updateUserById2(@Path("id") String userId,
                                       @Part("fName") RequestBody fName,
                                       @Part("lName") RequestBody lName,
                                       @Part MultipartBody.Part Image);

    @Multipart
    @PUT("/user/controllers/users/{id}")
    Call<ResponseBody> updateUserById2(@Path("id") String userId,
                                       @Part("fName") RequestBody fName,
                                       @Part("lName") RequestBody lName);

    @Headers("Content-Type: application/json")
    @POST("/user/auth/login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/user/auth/sessionLogin")
    Call<ResponseBody> sessionLogin();

    @Headers("Content-Type: application/json")
    @POST("/user/auth/actions/resendOTP")
    Call<ResponseBody> resendOTP(@Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("/user/controllers/setUserInfo")
    Call<ResponseBody> setUserInfo(@Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("/user/controllers/getBroadcasts/{skip}")
    Call<ResponseBody> getBroadcasts(@Path("skip") int skip);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/deleteBroadcast/{broadcastID}")
    Call<ResponseBody> deleteBroadcast(@Path("broadcastID") String broadcastID);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/pinBroadcast/{broadcastID}")
    Call<ResponseBody> pinBroadcast(@Path("broadcastID") String broadcastID);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/unpinBroadcast/{broadcastID}")
    Call<ResponseBody> unpinBroadcast(@Path("broadcastID") String broadcastID);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/likeBroadcast/{broadcastID}")
    Call<ResponseBody> likeBroadcast(@Path("broadcastID") String broadcastID);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/removeLikeBroadcast/{broadcastID}")
    Call<ResponseBody> removeLikeBroadcast(@Path("broadcastID") String broadcastID);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/getNetworks/{skip}")
    Call<ResponseBody> getNetworks(@Path("skip") int skip);


    @Headers("Content-Type: application/json")
    @POST("/user/controllers/getBroadcastComments/{broadcastID}/{skip}")
    Call<ResponseBody> getBroadcastComments(@Path("broadcastID") String broadcastID, @Path("skip") int skip);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/getBroadcastCommentReplies/{broadcastID}/{commentID}/{skip}")
    Call<ResponseBody> getBroadcastCommentReplies(@Path("broadcastID") String broadcastID, @Path("commentID") String commentID, @Path("skip") int skip);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/commentBroadcast")
    Call<ResponseBody> commentBroadcast(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/replyCommentBroadcast")
    Call<ResponseBody> replyCommentBroadcast(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/user/controllers/deleteCommentBroadcast/{broadcastID}/{commentID}")
    Call<ResponseBody> deleteCommentBroadcast(@Path("broadcastID") String broadcastID, @Path("commentID") String commentID);

    @Multipart
    @POST("/user/controllers/publishBroadcast/{type}")
    Call<ResponseBody> publishBroadcast(
            @Path("type") String type,
            @Part("broadcastUrl") RequestBody broadcastUrl,
            @Part List<MultipartBody.Part> media,
            @Part("description") RequestBody description);


    @Multipart
    @POST("/meeting/meeting")
    Call<ResponseBody> publishMeet(
            @Part("meetName") RequestBody meetName,
            @Part("meetDescription") RequestBody meetDescription,
            @Part("date") RequestBody date,
            @Part("time") RequestBody time,
            @Part("radioButtonValue") RequestBody radioButtonValue,
            @Part MultipartBody.Part imageID,
            @Part("limitedUsers") List<String> phoneNumbers


    );

    @Multipart
    @PUT("/meeting/updateMeet/{id}")
    Call<ResponseBody> updateMeet(
            @Path("id") String id,
            @Part("meetName") RequestBody meetName,
            @Part("meetDescription") RequestBody meetDescription,
            @Part("date") RequestBody date,
            @Part("time") RequestBody time,
            @Part("radioButtonValue") RequestBody radioButtonValue,
            @Part MultipartBody.Part imageID
    );

    @Multipart
    @PUT("/task/task/{id}")
    Call<ResponseBody> updateTask(
            @Path("id") String id,
            @Part("taskName") RequestBody meetName,
            @Part("taskDescription") RequestBody meetDescription,
            @Part("date") RequestBody date,
            @Part("time") RequestBody time,
            @Part("taskUrl") RequestBody taskUrl,
            @Part("radioButtonValue") RequestBody radioButtonValue,
            @Part MultipartBody.Part imageID
    );

    @Multipart
    @POST("/task/task")
    Call<ResponseBody> publishTask(
            @Part("taskName") RequestBody taskName,
            @Part("taskDescription") RequestBody taskDescription,
            @Part("date") RequestBody date,
            @Part("time") RequestBody time,
            @Part("taskUrl") RequestBody taskUrl,
            @Part("radioButtonValue") RequestBody radioButtonValue,
            @Part MultipartBody.Part imageID,
            @Part("limitedUsers") List<String> users);

    @Headers("Content-Type: application/json")
    @POST("/task/controllers/getTaskComments/{taskID}/{skip}")
    Call<ResponseBody> getTaskComments(@Path("taskID") String taskID, @Path("skip") int skip);

    @Multipart
    @POST("/user/controllers/send-notification")
    Call<ResponseBody> sendNotification(
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("phoneNumbers") List<String> phoneNumbers,
            @Part MultipartBody.Part imageUrl,
            @Part("meetingType") RequestBody meetingType,
            @Part("relatedId") RequestBody meetId,
            @Part("meetingDate") RequestBody meetDateRequestBody);

    @Headers("Content-Type: application/json")
    @POST("/task/controllers/getBroadcastCommentReplies/{taskID}/{commentID}/{skip}")
    Call<ResponseBody> getTaskCommentReplies(@Path("taskID") String taskID, @Path("commentID") String commentID, @Path("skip") int skip);

    @Headers("Content-Type: application/json")
    @POST("/task/controllers/commentTask")
    Call<ResponseBody> commentTask(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/task/controllers/replyCommentTask")
    Call<ResponseBody> replyCommentTask(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("/task/controllers/deleteCommentTask/{taskID}/{commentID}")
    Call<ResponseBody> deleteCommentTask(@Path("taskID") String taskID, @Path("commentID") String commentID);

    @Headers("Content-Type: application/json")
    @GET("/user/getUserAll")
    Call<JsonObject> queryUsers(
            @Query("dist") String dist,
            @Query("teh") String teh,
            @Query("vill") String vill,
            @Query("booth") String booth,
            @Query("minAge") String minAge,
            @Query("maxAge") String maxAge,
            @Query("gender") String gender,
            @Query("dob") String dob,
            @Query("role") String role
    );

    @Headers("Content-Type: application/json")
    @GET("/task/tasks/{taskID}")
    Call<TaskModelResponse> getTaskById(@Path("taskID") String taskID);

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getPrimaryUser/{userId}")
    Call<UserModelPrimary> getPrimaryUser(@Path("userId") String userId);

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getUserDetails/{num}")
    Call<ResponseBody> getUserData(@Path("num") String num);


    @Headers("Content-Type: application/json")
    @GET("/user/getBroadcast/{id}")
    Call<ResponseBody> getBroadcastById(@Path("id") String num);

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getUserAccepted")
    Call<Response> getUserAccepted();

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getUserRejected")
    Call<Response> getUserRejected();

    @Headers("Content-Type: application/json")
    @PUT("/user/controllers/users/{id}")
    Call<Void> updateUserRole
            (@Path("id") String userId,
             @Body Map<String, Object> roleMap);

    @Headers("Content-Type: application/json")
    @GET("/user/controllers/getallUser")
    Call<Response> getallUser();

    @Headers("Content-Type: application/json")
    @GET("/graphics/getchipButtonList")
    Call<ChipBtnResponse> getChipBtns();

    @Headers("Content-Type: application/json")
    @GET("/graphics/getSlider")
    Call<SliderImageResponse> getSlider();

    @Headers("Content-Type: application/json")
    @GET("/graphics/getGraphicsMedia")
    Call<GraphicModelResponse> getGraphicMdel();

    @Headers("Content-Type: application/json")
    @DELETE("/graphics/controllers/Graphic/{graphicsId}")
    Call<ResponseBody> deleteGraphics(@Path("graphicsId") String graphicsId);

    @Headers("Content-Type: application/json")
    @DELETE("/graphics/controllers/slider/{sliderId}")
    Call<GraphicModelResponse> deleteSlider(@Path("sliderId") String sliderId);

    @Multipart
    @PUT("graphics/controllers/Graphic/{graphicsId}")
    Call<ResponseBody> UpdateGraphics(
            @Path("graphicsId") String graphicsId,
            @Part("title") RequestBody title,
            @Part MultipartBody.Part imageUrl

    );

    @Multipart
    @PUT("graphics/controllers/slider/{sliderId}")
    Call<ResponseBody> UpdateSlider(
            @Path("sliderId") String sliderId,
            @Part MultipartBody.Part imageUrl

    );

    @Multipart
    @PUT("/user/controllers/publishBroadcast/{broadcastID}")
    Call<ResponseBody> UpdateBroadcast(
            @Path("broadcastID") String broadcastID,
            @Part("broadcastUrl") RequestBody broadcastUrl,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part imageID
    );


    @Headers("Content-Type: application/json")
    @DELETE("graphics/controllers/slider/{sliderId}")
    Call<Void> DeleteSlider(
            @Path("sliderId") String sliderId

    );

    @Headers("Content-Type: application/json")
    @DELETE("meeting/deleteMeet/{id}")
    Call<ResponseBody> DeleteMeet(
            @Path("id") String id

    );

    @Headers("Content-Type: application/json")
    @DELETE("task/tasks/{id}")
    Call<ResponseBody> DeleteTask(
            @Path("id") String id

    );

    @Multipart
    @POST("graphics/controllers/slider")
    Call<ResponseBody> CreateSlider(
            @Part MultipartBody.Part slider
    );

    @Headers("Content-Type: application/json")
    @DELETE("graphics/controllers/AnyGraphicImage/{id}")
    Call<Void> deleteGraphicObject(@Path("id") String id, @Body String GraphicId);

    @Multipart
    @POST("graphics/controllers/createGraphics")
    Call<ResponseBody> CreateGraphics(
            @Part("title") RequestBody title,
            @Part("type") RequestBody type,
            @Part("date") RequestBody date,
            @Part MultipartBody.Part imageUrl

    );

    @Headers("Content-Type: application/json")
    @POST("graphics/controllers/chipButtonList")
    Call<Void> AddChipBtns(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @PUT("graphics/controllers/chipButtonList/{chipButtonListId}")
    Call<Void> EditChipBtns(@Path("chipButtonListId") String chipButtonListId, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @DELETE("graphics/controllers/chipButtonList/{chipButtonListId}")
    Call<Void> DeleteChipBtns(@Path("chipButtonListId") String chipButtonListId);

}
