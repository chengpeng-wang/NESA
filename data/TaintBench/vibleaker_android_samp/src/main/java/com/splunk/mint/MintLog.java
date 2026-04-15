package com.splunk.mint;

import android.util.Log;

public class MintLog {
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
        ActionLog.createLog(tag + ": " + msg, MintLogLevel.Debug).save(new DataSaver());
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
        ActionLog.createLog(tag + ": " + msg, MintLogLevel.Error).save(new DataSaver());
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
        ActionLog.createLog(tag + ": " + msg, MintLogLevel.Info).save(new DataSaver());
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
        ActionLog.createLog(tag + ": " + msg, MintLogLevel.Verbose).save(new DataSaver());
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
        ActionLog.createLog(tag + ": " + msg, MintLogLevel.Warning).save(new DataSaver());
    }
}
