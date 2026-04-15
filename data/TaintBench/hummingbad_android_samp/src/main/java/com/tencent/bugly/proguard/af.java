package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Build;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: BUGLY */
public class af {
    protected static af b = null;
    private static Context c;
    protected boolean a;

    public static synchronized af a(Context context) {
        af afVar;
        synchronized (af.class) {
            if (b == null) {
                c = context;
                b = new af();
            }
            afVar = b;
        }
        return afVar;
    }

    protected af() {
        this.a = false;
        this.a = true;
        if (!b() && !c() && !d()) {
            this.a = false;
        }
    }

    public synchronized boolean a() {
        return this.a;
    }

    public static boolean b() {
        String str = Build.TAGS;
        if (str == null || !str.contains("test-keys")) {
            return false;
        }
        return true;
    }

    public static boolean c() {
        try {
            if (new File("/system/app/Superuser.apk").exists()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean d() {
        ArrayList a = ag.a(c, new String[]{"/system/bin/sh", "-c", "type su"});
        if (a == null || a.size() <= 0) {
            return false;
        }
        Iterator it = a.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            z.c(str, new Object[0]);
            if (str.contains("not found")) {
                return false;
            }
        }
        return true;
    }
}
