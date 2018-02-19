package com.nestletime.utils;

import android.util.Log;



/**
 * Created by Deepak Saini on 13-02-2018.
 */
public class MyLogs {

    public static void i(String tag, String value) {
        if (!StringUtils.isNullOrEmpty(value))
            Log.i(tag, value);
    }

    public static void e(String tag, String value) {
        if (!StringUtils.isNullOrEmpty(value))
            Log.e(tag, value);
    }

    public static void d(String tag, String value) {
        if (!StringUtils.isNullOrEmpty(value))
            Log.d(tag, value);
    }

    public static void d(String value) {
        if (!StringUtils.isNullOrEmpty(value))
            Log.d("=====", value);
    }
}
