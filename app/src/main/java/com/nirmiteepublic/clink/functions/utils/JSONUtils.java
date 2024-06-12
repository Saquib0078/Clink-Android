package com.nirmiteepublic.clink.functions.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONUtils {
    public static void mergeJSON(JSONObject main, JSONObject data) {
        try {
            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                main.put(key, data.getString(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
