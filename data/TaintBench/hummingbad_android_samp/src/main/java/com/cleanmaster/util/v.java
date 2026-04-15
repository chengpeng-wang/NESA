package com.cleanmaster.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/* compiled from: ThreadHelper */
public class v {
    static final /* synthetic */ boolean a = (!v.class.desiredAssertionStatus());
    private static final Object b = new Object();
    private static boolean c = false;
    private static Handler d = null;
    private static boolean e = true;
    private static HandlerThread f;
    private static Handler g;

    private static void b() {
        if (f == null) {
            HandlerThread handlerThread = new HandlerThread("ResumeThread");
            f = handlerThread;
            handlerThread.start();
            g = new Handler(f.getLooper());
        }
    }

    public static void a(Runnable runnable) {
        b();
        g.post(runnable);
    }

    public static void a(Runnable runnable, long j) {
        b();
        g.postDelayed(runnable, j);
    }

    public static Handler a() {
        Handler handler;
        synchronized (b) {
            if (d == null) {
                if (c) {
                    throw new RuntimeException("Did not yet override the UI thread");
                }
                d = new Handler(Looper.getMainLooper());
            }
            handler = d;
        }
        return handler;
    }

    public static void b(Runnable runnable) {
        a().post(runnable);
    }
}
