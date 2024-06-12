package com.nirmiteepublic.clink.functions.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static String NO_DATA = "no-data";
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Prefs(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("app-data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setPref(String name, String value) {
        editor.putString(name, value);
        editor.commit();
    }

    public void setPref(String name, int value) {
        editor.putInt(name, value);
        editor.commit();
    }

    public String getPref(String name) {
        return sharedPreferences.getString(name, NO_DATA);
    }

    public int getPrefInt(String name) {
        return sharedPreferences.getInt(name, 0);
    }

    public String getJWT() {
        return sharedPreferences.getString("jwt", NO_DATA);
    }

    public void setJWT(String value) {
        editor.putString("jwt", value);
        editor.commit();
    }


    public String getEmail() {
        return sharedPreferences.getString("email", NO_DATA);
    }

    public void setEmail(String value) {
        editor.putString("email", value);
        editor.commit();
    }

    public boolean checkLogin() {
        return !getJWT().equals(Prefs.NO_DATA);
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

}
