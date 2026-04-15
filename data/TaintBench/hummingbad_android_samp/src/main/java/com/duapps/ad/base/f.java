package com.duapps.ad.base;

import android.util.Log;

/* compiled from: LogHelper */
public final class f {
    private static boolean a = false;

    public static boolean a() {
        return false;
    }

    public static void a(String str, String str2) {
    }

    public static void b(String str, String str2) {
    }

    public static void c(String str, String str2) {
    }

    public static void a(String str, String str2, Throwable th) {
    }

    public static void d(String str, String str2) {
        Log.e("DX-Toolbox", e(str, str2));
    }

    public static void b(String str, String str2, Throwable th) {
        Log.e("DX-Toolbox", e(str, str2), th);
    }

    private static String e(String str, String str2) {
        return new StringBuffer("{").append(Thread.currentThread().getName()).append("}[").append(str).append("] ").append(str2).toString();
    }
}
