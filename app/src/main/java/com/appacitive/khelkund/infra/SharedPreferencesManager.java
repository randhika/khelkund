package com.appacitive.khelkund.infra;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sathley on 3/24/2015.
 */
public class SharedPreferencesManager {

    public static void  WriteUserId(String userId)
    {
        WriteKeyValue("user_id", userId);
    }

    public static String ReadUserId()
    {
        return ReadKeyValue("user_id");
    }

    public static void WriteKeyValue(String key, String value)
    {
        SharedPreferences sharedPreferences = KhelkundApplication.getAppContext().getSharedPreferences("khelkund", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String ReadKeyValue(String key)
    {
        SharedPreferences sharedPreferences = KhelkundApplication.getAppContext().getSharedPreferences("khelkund", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static void RemoveUserId() {
        SharedPreferences sharedPreferences = KhelkundApplication.getAppContext().getSharedPreferences("khelkund", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.commit();
    }
}
