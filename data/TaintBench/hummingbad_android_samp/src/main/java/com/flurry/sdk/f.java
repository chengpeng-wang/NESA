package com.flurry.sdk;

import java.util.TreeMap;

public class f {
    private static f a;
    private static final String b = f.class.getSimpleName();
    private final TreeMap c = new TreeMap();

    public static synchronized f a() {
        f fVar;
        synchronized (f.class) {
            if (a == null) {
                a = new f();
            }
            fVar = a;
        }
        return fVar;
    }

    public final void a(String str) {
        synchronized (this.c) {
            Integer num = (Integer) this.c.get(str);
            this.c.put(str, Integer.valueOf(num != null ? num.intValue() + 1 : 1));
        }
    }
}
