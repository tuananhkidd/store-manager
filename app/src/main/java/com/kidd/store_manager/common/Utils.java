package com.kidd.store_manager.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TuanAnhKid on 3/30/2018.
 */

public class Utils {
    public static void setSharePreferenceValues(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("bookstore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharePreferenceValues(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("bookstore", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
