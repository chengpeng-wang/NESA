package com.mvlove.util;

import android.util.Log;

public class LogUtil {
    private static final boolean SHOW_LOG = true;
    private static String TAG = "EI";

    public static void info(String msg) {
        Log.i(TAG, msg);
    }

    public static void info(String msg, Throwable tr) {
        Log.i(TAG, msg, tr);
    }

    public static void debug(String msg) {
        Log.d(TAG, msg);
    }

    public static void debug(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static void debug(String msg, Throwable tr) {
        Log.d(TAG, msg, tr);
    }

    public static void verbose(String msg) {
        Log.v(TAG, msg);
    }

    public static void verbose(String msg, Throwable tr) {
        Log.v(TAG, msg, tr);
    }

    public static void warn(String msg) {
        Log.w(TAG, msg);
    }

    public static void warn(String msg, Throwable tr) {
        Log.w(TAG, msg, tr);
    }

    public static void error(Object msg) {
        Log.e(TAG, msg);
        Log.println(0, TAG, msg);
    }

    public static void error(String msg, Throwable tr) {
        Log.e(TAG, msg, tr);
    }

    public static void println(String msg) {
        System.out.println(msg);
    }
}
