package com.cmcm.adsdk.requestconfig.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import com.cmcm.adsdk.requestconfig.log.a;

/* compiled from: PerferenceUtil */
public final class b {
    private static Context a;
    private static SharedPreferences b;
    private static String c;

    public static void a(Context context, String str) {
        a = context;
        c = String.format("%s_%s", new Object[]{"cmadsdk", str});
    }

    public static synchronized String a(String str) {
        String string;
        synchronized (b.class) {
            try {
                if (b == null) {
                    b = a.getSharedPreferences(c, 0);
                }
                string = b.getString("config_cache", str);
            } catch (Exception e) {
                a.b("PerferenceUtil", "get cache json error..." + e.getMessage());
                string = "";
            }
        }
        return string;
    }

    public static synchronized void b(String str) {
        synchronized (b.class) {
            try {
                if (b == null) {
                    b = a.getSharedPreferences(c, 0);
                }
                Editor edit = b.edit();
                edit.putString("config_cache", str);
                a(edit);
            } catch (Exception e) {
                a.b("PerferenceUtil", "save cache json error..." + e.getMessage());
            }
        }
        return;
    }

    private static void a(Editor editor) {
        if (VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void a(String str, boolean z) {
        if (b == null) {
            b = a.getSharedPreferences(c, 0);
        }
        Editor edit = b.edit();
        edit.putBoolean(str, z);
        a(edit);
    }

    public static boolean b(String str, boolean z) {
        if (b == null) {
            b = a.getSharedPreferences(c, 0);
        }
        return b.getBoolean(str, false);
    }

    public static void a(String str, int i) {
        if (b == null) {
            b = a.getSharedPreferences(c, 0);
        }
        Editor edit = b.edit();
        edit.putInt(str, i);
        a(edit);
    }

    public static int b(String str, int i) {
        if (b == null) {
            b = a.getSharedPreferences(c, 0);
        }
        return b.getInt(str, 0);
    }

    public static void a(String str, long j) {
        if (b == null) {
            b = a.getSharedPreferences(c, 0);
        }
        Editor edit = b.edit();
        edit.putLong(str, j);
        a(edit);
    }

    public static long b(String str, long j) {
        if (b == null) {
            b = a.getSharedPreferences(c, 0);
        }
        return b.getLong(str, 0);
    }
}
