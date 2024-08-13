package com.nirmiteepublic.clink.functions.utils;
import android.content.Context;
import android.content.SharedPreferences;
/*
public class UserUtils {
    private static String USER_FIRST_NAME;
    private static String USER_LAST_NAME;
    private static String USER_NUMBER;
    private static String USER_DP;
    private static String USER_ROLE;
    private static String GREETING;
    private static String RADIO;

    private static String PROFILEIMAGE;



    public static String getPROFILEIMAGE() {
        return PROFILEIMAGE;
    }

    public static void setPROFILEIMAGE(String PROFILEIMAGE) {
        UserUtils.PROFILEIMAGE = PROFILEIMAGE;
    }

    public static String getRADIO() {
        return RADIO;
    }

    public static void setRADIO(String RADIO) {
        UserUtils.RADIO = RADIO;
    }

    private static String TASK_ID;
    private static String SECONDARY_USERID;
    private static String FRAMENAME;
    private static String FRAMEADD;

    public static String getFRAMENAME() {
        return FRAMENAME;
    }

    public static void setFRAMENAME(String FRAMENAME) {
        UserUtils.FRAMENAME = FRAMENAME;
    }

    public static String getFRAMEADD() {
        return FRAMEADD;
    }

    public static void setFRAMEADD(String FRAMEADD) {
        UserUtils.FRAMEADD = FRAMEADD;
    }

    public static String getSecondaryUserid() {
        return SECONDARY_USERID;
    }

    public static void setSecondaryUserid(String secondaryUserid) {
        SECONDARY_USERID = secondaryUserid;
    }

    public static String getTaskId() {
        return TASK_ID;
    }

    public static void setTaskId(String taskId) {
        TASK_ID = taskId;
    }

    public static String getUserId() {
        return USER_ID;
    }

    public static void setUserId(String userId) {
        USER_ID = userId;

    }

    private static String USER_ID;

    public static String getUserFirstName() {
        return USER_FIRST_NAME;
    }

    public static void setUserFirstName(String userFirstName) {
        USER_FIRST_NAME = userFirstName;
    }

    public static String getUserLastName() {
        return USER_LAST_NAME;
    }

    public static void setUserLastName(String userLastName) {
        USER_LAST_NAME = userLastName;
    }

    public static String getUserNumber() {
        return USER_NUMBER;
    }

    public static void setUserNumber(String userNumber) {
        USER_NUMBER = userNumber;
    }

    public static String getUserDp() {
        return USER_DP;
    }

    public static void setUserDp(String userDp) {
        USER_DP = userDp;
    }

    public static UserRoles getUserRole() {
        switch (USER_ROLE) {
            case "1":
                return UserRoles.USER;
            case "2":
                return UserRoles.CO_ADMIN;
            case "3":
                return UserRoles.SUPER_CO_ADMIN;
            default:
                return UserRoles.UNSPECIFIED;
        }
    }

    public static void setUserRole(String userRole) {
        USER_ROLE = userRole;
    }

    public static String getGreeting() {
        return GREETING;
    }

    public static void setGreeting(String GREETING) {
        UserUtils.GREETING = GREETING;
    }

    public static boolean isAdmin() {
        return UserUtils.getUserRole() == UserRoles.CO_ADMIN || UserUtils.getUserRole() == UserRoles.SUPER_CO_ADMIN;
    }

    public static boolean isSuperAdmin() {
        return UserUtils.getUserRole() == UserRoles.SUPER_CO_ADMIN;
    }
}
*/
 public class UserUtils {
    private static SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "user_prefs";

    private static final String USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String USER_LAST_NAME = "USER_LAST_NAME";
    private static final String USER_NUMBER = "USER_NUMBER";
    private static final String USER_DP = "USER_DP";
    private static final String USER_ROLE = "USER_ROLE";
    private static final String GREETING = "GREETING";
    private static final String RADIO = "RADIO";
    private static final String PROFILEIMAGE = "PROFILEIMAGE";
    private static final String TASK_ID = "TASK_ID";
    private static final String SECONDARY_USERID = "SECONDARY_USERID";
    private static final String FRAMENAME = "FRAMENAME";
    private static final String FRAMEADD = "FRAMEADD";
    private static final String USER_ID = "USER_ID";

    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
    }

    public static String getUserFirstName() {
        return sharedPreferences.getString(USER_FIRST_NAME, null);
    }

    public static void setUserFirstName(String userFirstName) {
        sharedPreferences.edit().putString(USER_FIRST_NAME, userFirstName).apply();
    }

    public static String getUserLastName() {
        return sharedPreferences.getString(USER_LAST_NAME, null);
    }

    public static void setUserLastName(String userLastName) {
        sharedPreferences.edit().putString(USER_LAST_NAME, userLastName).apply();
    }

    public static String getUserNumber() {
        return sharedPreferences.getString(USER_NUMBER, null);
    }

    public static void setUserNumber(String userNumber) {
        sharedPreferences.edit().putString(USER_NUMBER, userNumber).apply();
    }

    public static String getUserDp() {
        return sharedPreferences.getString(USER_DP, null);
    }

    public static void setUserDp(String userDp) {
        sharedPreferences.edit().putString(USER_DP, userDp).apply();
    }

    public static UserRoles getUserRole() {
        String userRole = sharedPreferences.getString(USER_ROLE, null);
        if (userRole == null) return UserRoles.UNSPECIFIED;
        switch (userRole) {
            case "1":
                return UserRoles.USER;
            case "2":
                return UserRoles.CO_ADMIN;
            case "3":
                return UserRoles.SUPER_CO_ADMIN;
            default:
                return UserRoles.UNSPECIFIED;
        }
    }

    public static void setUserRole(String userRole) {
        sharedPreferences.edit().putString(USER_ROLE, userRole).apply();
    }

    public static String getGreeting() {
        return sharedPreferences.getString(GREETING, null);
    }

    public static void setGreeting(String greeting) {
        sharedPreferences.edit().putString(GREETING, greeting).apply();
    }

    public static String getRADIO() {
        return sharedPreferences.getString(RADIO, null);
    }

    public static void setRADIO(String radio) {
        sharedPreferences.edit().putString(RADIO, radio).apply();
    }

    public static String getProfileImage() {
        return sharedPreferences.getString(PROFILEIMAGE, null);
    }

    public static void setProfileImage(String profileImage) {
        sharedPreferences.edit().putString(PROFILEIMAGE, profileImage).apply();
    }

    public static String getTaskId() {
        return sharedPreferences.getString(TASK_ID, null);
    }

    public static void setTaskId(String taskId) {
        sharedPreferences.edit().putString(TASK_ID, taskId).apply();
    }

    public static String getSecondaryUserid() {
        return sharedPreferences.getString(SECONDARY_USERID, null);
    }

    public static void setSecondaryUserid(String secondaryUserid) {
        sharedPreferences.edit().putString(SECONDARY_USERID, secondaryUserid).apply();
    }

    public static String getFRAMENAME() {
        return sharedPreferences.getString(FRAMENAME, null);
    }

    public static void setFRAMENAME(String frameName) {
        sharedPreferences.edit().putString(FRAMENAME, frameName).apply();
    }

    public static String getFRAMEADD() {
        return sharedPreferences.getString(FRAMEADD, null);
    }

    public static void setFRAMEADD(String frameAdd) {
        sharedPreferences.edit().putString(FRAMEADD, frameAdd).apply();
    }

    public static String getUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public static void setUserId(String userId) {
        sharedPreferences.edit().putString(USER_ID, userId).apply();
    }

    public static boolean isAdmin() {
        return getUserRole() == UserRoles.CO_ADMIN || getUserRole() == UserRoles.SUPER_CO_ADMIN;
    }

    public static boolean isSuperAdmin() {
        return getUserRole() == UserRoles.SUPER_CO_ADMIN;
    }
}

