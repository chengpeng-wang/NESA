package com.flurry.sdk;

import android.content.Context;
import android.os.SystemClock;

public class hg implements iz {
    public volatile long a = 0;
    public volatile long b = 0;
    public volatile long c = 0;

    public static synchronized hg a() {
        hg hgVar;
        synchronized (hg.class) {
            hgVar = (hg) hz.a.a(hg.class);
        }
        return hgVar;
    }

    public static hu b() {
        return ht.a().b();
    }

    public final void a(Context context) {
        il.a();
        ln.a();
        li.a();
        ib.a();
        ht.a();
        hi.a();
        hp.a();
        hi.a();
        hw.a();
        hy.a();
        this.a = System.currentTimeMillis();
        this.b = SystemClock.elapsedRealtime();
        hz.a.b(new hh(this));
    }
}
