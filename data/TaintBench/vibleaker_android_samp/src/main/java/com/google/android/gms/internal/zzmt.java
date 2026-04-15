package com.google.android.gms.internal;

import android.os.SystemClock;

public final class zzmt implements zzmq {
    private static zzmt zzaoa;

    public static synchronized zzmq zzsc() {
        zzmt zzmt;
        synchronized (zzmt.class) {
            if (zzaoa == null) {
                zzaoa = new zzmt();
            }
            zzmt = zzaoa;
        }
        return zzmt;
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public long nanoTime() {
        return System.nanoTime();
    }
}
