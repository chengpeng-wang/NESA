package com.flurry.sdk;

import android.os.Build;
import android.os.Build.VERSION;

public class hw implements lk {
    private static hw a;
    private static final String b = hw.class.getSimpleName();
    private String c;

    private hw() {
        li a = li.a();
        this.c = (String) a.a("VersionName");
        a.a("VersionName", (lk) this);
        iw.a(4, b, "initSettings, VersionName = " + this.c);
    }

    public static synchronized hw a() {
        hw hwVar;
        synchronized (hw.class) {
            if (a == null) {
                a = new hw();
            }
            hwVar = a;
        }
        return hwVar;
    }

    public static String b() {
        return VERSION.RELEASE;
    }

    public static String c() {
        return Build.DEVICE;
    }

    public final void a(String str, Object obj) {
        if (str.equals("VersionName")) {
            this.c = (String) obj;
            iw.a(4, b, "onSettingUpdate, VersionName = " + this.c);
            return;
        }
        iw.a(6, b, "onSettingUpdate internal error!");
    }
}
