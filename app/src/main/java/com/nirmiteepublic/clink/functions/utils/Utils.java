package com.nirmiteepublic.clink.functions.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utils {
    public static JSONObject ALL_CAREER_OPTIONS, PDF_VIDEO_DATA;
    public static JSONArray SLIDER_IMAGE = new JSONArray().put("https://cdn.pixabay.com/photo/2023/09/04/19/10/butterfly-8233505_1280.jpg")
            .put("https://cdn.pixabay.com/photo/2023/04/19/19/46/gosling-7938451_1280.jpg")
            .put("https://cdn.pixabay.com/photo/2023/04/19/19/46/gosling-7938451_1280.jpg")
            .put("https://cdn.pixabay.com/photo/2023/04/19/19/46/gosling-7938451_1280.jpg");

    public static String name, email, supportNumber;

    public static int REFER_CODE_LENGTH = 10;

    public static String REFERRAL_CODE, REFER_CODE, REFER_BONUS, APP_REFER_CODE, REAl_APP_REFER_CODE;

    public static boolean IS_REFER_PROGRAM_JOINED = false, ADD_OWN_OFFER_ENABLED = true;
}
