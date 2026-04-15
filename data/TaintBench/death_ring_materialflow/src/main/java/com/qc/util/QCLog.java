package com.qc.util;

import android.util.Log;

public class QCLog {
    private static boolean LogSwitch = false;

    public static void verbose(String tag, String text) {
        if (LogSwitch) {
            Log.w(tag, text);
        }
    }

    public static void debug(Object obj, String text) {
        if (LogSwitch && obj != null) {
            debug(obj.getClass().getSimpleName(), text);
        }
    }

    public static void debug(String tag, String text) {
        if (LogSwitch) {
            Log.d(tag, text);
        }
    }

    public static void info(String tag, String text) {
        if (LogSwitch) {
            Log.i(tag, text);
        }
    }

    public static void warn(String tag, String text) {
        if (LogSwitch) {
            Log.w(tag, text);
        }
    }

    public static void warn(String tag, String text, Throwable throwable) {
        if (LogSwitch) {
            Log.w(tag, text, throwable);
        }
    }

    public static void error(String tag, String text) {
        if (LogSwitch) {
            Log.e(tag, text);
        }
    }

    public static void error(String tag, String text, Throwable throwable) {
        if (LogSwitch) {
            Log.e(tag, text, throwable);
        }
    }
}
