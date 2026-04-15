package com.dsifakf.aoakmnq;

import android.os.AsyncTask;
import android.os.Build.VERSION;

public class Connect2 {
    public static void CheckMultiThSupp(AsyncTask aT) {
        if (VERSION.SDK_INT >= 11) {
            aT.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        } else {
            aT.execute(null);
        }
    }
}
