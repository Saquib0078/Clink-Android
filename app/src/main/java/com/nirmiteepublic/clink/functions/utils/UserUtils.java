package com.nirmiteepublic.clink.functions.utils;

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
