package com.google.games.stores.util;

import android.util.Log;

public class Logger {
    private static int DEBUG = 2;
    private static int ERROR = 5;
    private static int INFO = 3;
    private static int LOGLEVEL = 0;
    private static int VERBOSE = 1;
    private static int WARN = 4;

    public static void v(String tag, String msg) {
        if (LOGLEVEL > VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOGLEVEL > DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LOGLEVEL > INFO) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOGLEVEL > WARN) {
            Log.v(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOGLEVEL > ERROR) {
            Log.v(tag, msg);
        }
    }
}
