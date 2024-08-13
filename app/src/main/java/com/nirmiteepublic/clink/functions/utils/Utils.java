package com.nirmiteepublic.clink.functions.utils;

import android.annotation.SuppressLint;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {



    public static String loadGraphicImage(String id) {
        return RetrofitClient.BASE_URL + "graphics/getgraphics/";
    }

    public static String loadBroadcastMedia(String id, String type) {
        return RetrofitClient.USER_BASE_URL + "getUsermedia/" + id + "." + type;
    }
    public static String loadBroadcastMedia(String id) {
        return RetrofitClient.USER_BASE_URL + "getUsermedia/" + id;
    }

    public static String loadUserProfile(String id) {
        return RetrofitClient.PROFILE_IMAGE;
    }
    public static String loadMeetingImage(String id) {
        return RetrofitClient.MEETIMAGE_BASE_URL + id;
    }
    public static String USerImage(String id) {
        return RetrofitClient.USER_BASE_URL+"/user/" + id;
    }
    public static String loadTaskImg(String id) {
        return RetrofitClient.TASKIMAGE_BASE_URL + id;
    }
    public static String loadTaskImage(String id) {
        return id;
    }
    public static String loadmeetImage(String id) {
        return id;
    }
    public static String loadUserImage(String id) {
        return id;
    }


    @SuppressLint("SimpleDateFormat")
    public static String getTimeAgo(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");
        try {
            Date inputDate = sdf.parse(date);
            if (inputDate == null) {
                return date;
            }
            return TimeAgo.using(inputDate.getTime());
        } catch (ParseException e) {
            return date;
        }
    }
}
