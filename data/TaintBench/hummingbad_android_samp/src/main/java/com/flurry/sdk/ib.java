package com.flurry.sdk;

import java.util.HashMap;

public class ib {
    private static ib a;
    private static final HashMap b = new HashMap();
    private static final String c = ib.class.getSimpleName();

    public static synchronized ib a() {
        ib ibVar;
        synchronized (ib.class) {
            if (a == null) {
                a = new ib();
            }
            ibVar = a;
        }
        return ibVar;
    }

    public final synchronized HashMap b() {
        HashMap hashMap;
        synchronized (b) {
            hashMap = new HashMap(b);
        }
        return hashMap;
    }
}
