package com.splunk.mint;

import android.util.Log;

public class Logger {
    public static void logInfo(String string) {
        if (Mint.DEBUG && string != null) {
            Log.i("Mint", string);
        }
    }

    public static void logWarning(String string) {
        if (string != null) {
            Log.w("Mint", string);
        }
    }

    public static void logError(String string) {
        if (string != null) {
            Log.e("Mint", string);
        }
    }
}
