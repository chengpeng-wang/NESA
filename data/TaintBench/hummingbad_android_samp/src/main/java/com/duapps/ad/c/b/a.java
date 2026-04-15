package com.duapps.ad.c.b;

import android.os.Handler;
import android.os.Looper;

/* compiled from: ThreadUtils */
public final class a {
    private static Handler a;

    public static boolean a() {
        return Looper.getMainLooper().equals(Looper.myLooper());
    }

    static {
        a = null;
        a = new Handler(Looper.getMainLooper());
    }

    public static void a(Runnable runnable) {
        a.post(runnable);
    }
}
