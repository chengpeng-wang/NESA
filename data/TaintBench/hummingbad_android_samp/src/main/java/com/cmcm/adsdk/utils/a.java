package com.cmcm.adsdk.utils;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;

/* compiled from: BackgroundHandler */
public final class a {
    public static final Handler a = new Handler(b.getLooper());
    private static HandlerThread b;

    static {
        HandlerThread handlerThread = new HandlerThread("BackgroundHandler", 1);
        b = handlerThread;
        handlerThread.start();
        if (!b.isAlive()) {
            com.cmcm.adsdk.requestconfig.log.a.c("BackgroundHandler", "sLooperThread has died, renew a HandlerThread instance");
            b.interrupt();
            HandlerThread handlerThread2 = new HandlerThread("BackgroundHandler", 1);
            b = handlerThread2;
            handlerThread2.start();
        }
    }

    public static <T> void a(AsyncTask<T, ?, ?> asyncTask, T... tArr) {
        if (VERSION.SDK_INT >= 11) {
            asyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, tArr);
        } else {
            asyncTask.execute(tArr);
        }
    }
}
