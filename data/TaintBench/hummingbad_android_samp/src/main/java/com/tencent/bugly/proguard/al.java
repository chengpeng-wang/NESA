package com.tencent.bugly.proguard;

/* compiled from: BUGLY */
public class al {
    private static an a = null;
    private static ao b = null;

    public static am a(int i) {
        if (i == 1) {
            return b();
        }
        if (i == 2) {
            return a();
        }
        return null;
    }

    public static am a() {
        return new an();
    }

    public static am b() {
        return new ao();
    }
}
