package com.flurry.sdk;

import android.text.TextUtils;
import android.util.Log;

public final class iw {
    private static int a = 5;
    private static boolean b = false;

    public static int a() {
        return a;
    }

    public static void a(int i) {
        a = i;
    }

    public static void a(int i, String str, String str2) {
        c(i, str, str2);
    }

    public static void a(int i, String str, String str2, Throwable th) {
        c(i, str, str2 + 10 + Log.getStackTraceString(th));
    }

    public static void a(String str) {
        b(4, kk.a, str);
    }

    public static void a(String str, String str2) {
        b(3, str, str2);
    }

    public static void a(String str, String str2, Throwable th) {
        b(6, str, str2 + 10 + Log.getStackTraceString(th));
    }

    public static void a(boolean z) {
        b = z;
    }

    private static void b(int i, String str, String str2) {
        if (a <= i) {
            d(i, str, str2);
        }
    }

    public static void b(String str) {
        b(2, j.a, str);
    }

    public static void b(String str, String str2) {
        b(6, str, str2);
    }

    public static boolean b() {
        return b;
    }

    private static void c(int i, String str, String str2) {
        if (b) {
            d(i, str, str2);
        }
    }

    public static void c(String str, String str2) {
        b(5, str, str2);
    }

    private static void d(int i, String str, String str2) {
        if (!b) {
            str = "Flurry";
        }
        int length = TextUtils.isEmpty(str2) ? 0 : str2.length();
        int i2 = 0;
        while (i2 < length) {
            int i3 = 4000 > length - i2 ? length : i2 + 4000;
            if (Log.println(i, str, str2.substring(i2, i3)) > 0) {
                i2 = i3;
            } else {
                return;
            }
        }
    }
}
