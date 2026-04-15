package com.address.core;

import android.os.Environment;

public class Log {
    private static String _path = "/mnt/sdcard/test.txt";

    public static void init() {
        _path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xBot.log.txt";
    }

    public static void write(String text) {
        android.util.Log.w("xBotDebug", text);
    }
}
