package com.nirmiteepublic.clink.functions.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    private static final String PREF_NAME = "my_shared_preferences";
    private static SharedPreferenceUtils instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceUtils(context);
        }
        return instance;
    }

    // Save a string value to SharedPreferences
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // Retrieve a string value from SharedPreferences
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Save an integer value to SharedPreferences
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    // Retrieve an integer value from SharedPreferences
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Save a boolean value to SharedPreferences
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Retrieve a boolean value from SharedPreferences
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Save a float value to SharedPreferences
    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    // Retrieve a float value from SharedPreferences
    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    // Save a long value to SharedPreferences
    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    // Retrieve a long value from SharedPreferences
    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    // Remove a value from SharedPreferences
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Clear all data from SharedPreferences
    public void clear() {
        editor.clear();
        editor.apply();
    }
}
