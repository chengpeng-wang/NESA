package com.flurry.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

public final class hz {
    public static hz a;
    public final Context b;
    public final Handler c;
    public final String d;
    private final Handler e = new Handler(Looper.getMainLooper());
    private final HandlerThread f = new HandlerThread("FlurryAgent");
    private final iy g;

    private hz(Context context, String str) {
        this.b = context.getApplicationContext();
        this.f.start();
        this.c = new Handler(this.f.getLooper());
        this.d = str;
        this.g = new iy();
    }

    public static synchronized void a(Context context, String str) {
        synchronized (hz.class) {
            if (a != null) {
                if (!a.d.equals(str)) {
                    throw new IllegalStateException("Only one API key per application is supported!");
                }
            } else if (context == null) {
                throw new IllegalArgumentException("Context cannot be null");
            } else if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("API key must be specified");
            } else {
                hz hzVar = new hz(context, str);
                a = hzVar;
                hzVar.g.a(context);
            }
        }
    }

    public final iz a(Class cls) {
        return this.g.b(cls);
    }

    public final void a(Runnable runnable) {
        this.e.post(runnable);
    }

    public final void a(Runnable runnable, long j) {
        if (runnable != null) {
            this.c.postDelayed(runnable, j);
        }
    }

    public final void b(Runnable runnable) {
        this.c.post(runnable);
    }
}
