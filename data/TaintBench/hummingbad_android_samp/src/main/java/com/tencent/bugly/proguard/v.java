package com.tencent.bugly.proguard;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: BUGLY */
public class v {
    private static SharedPreferences a;
    private static String b;
    private static String c = "ip";
    private static String d;
    private static String e = "qim";

    public static synchronized void a(Context context) {
        synchronized (v.class) {
            a = context.getSharedPreferences("bugly_data", 0);
        }
    }

    public static synchronized void a(az azVar) {
        synchronized (v.class) {
            if (azVar != null) {
                if (!(azVar.d == null || b == azVar.d)) {
                    a.edit().putString(c, azVar.d).apply();
                    z.c("[response] update gatewayIp: %s", azVar.d);
                }
                if (!(azVar.g == null || d == azVar.g)) {
                    a.edit().putString(e, azVar.g).apply();
                    z.c("[response] update qimei: %s", azVar.g);
                }
            }
        }
    }

    public static synchronized String a() {
        String str;
        synchronized (v.class) {
            try {
                if (b != null) {
                    str = b;
                } else {
                    str = a.getString(c, "");
                }
            } catch (Throwable th) {
                str = "";
            }
        }
        return str;
    }

    public static synchronized String b() {
        String str;
        synchronized (v.class) {
            try {
                if (d != null) {
                    str = d;
                } else {
                    str = a.getString(e, "");
                }
            } catch (Throwable th) {
                str = "";
            }
        }
        return str;
    }
}
