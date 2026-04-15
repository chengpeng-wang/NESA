package com.flurry.sdk;

public final class u {
    private static u c;
    public volatile String a = null;
    public volatile String b = null;

    private u() {
    }

    public static synchronized u a() {
        u uVar;
        synchronized (u.class) {
            if (c == null) {
                c = new u();
            }
            uVar = c;
        }
        return uVar;
    }

    public static boolean b() {
        return ((Boolean) li.a().a("UseHttps")).booleanValue();
    }
}
