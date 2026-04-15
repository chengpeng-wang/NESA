package com.example.chathook.utils;

import android.util.Log;

public class MActivityManagerNative {
    public static Object getDefault() {
        Object object = null;
        try {
            object = Class.forName("android.app.ActivityManagerNative").getDeclaredMethod("getDefault", null).invoke(null, null);
            Log.i("yincc", " ActivityManagerNative getDefault = " + object);
            return object;
        } catch (Exception e) {
            Log.e("yincc", " ActivityManagerNative exc = " + e.getMessage());
            e.printStackTrace();
            return object;
        }
    }
}
