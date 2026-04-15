package com.cleanmaster.service;

import com.picksinit.c;

/* compiled from: LocalStorage */
public class a {
    private static a a = null;

    public static a a() {
        if (a == null) {
            a = new a();
        }
        return a;
    }

    public static long a(int i) {
        return com.umeng.analytics.a.m * ((long) i);
    }

    public static String b() {
        return c.getInstance().getContext().getSharedPreferences("misc", 0).getString("user-agent", null);
    }

    public static void a(String str) {
        if (str != null) {
            c.getInstance().getContext().getSharedPreferences("misc", 0).edit().putString("user-agent", str).commit();
        }
    }
}
