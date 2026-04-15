package com.flurry.sdk;

import android.content.Context;
import android.os.Build.VERSION;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class iy {
    private static final String a = iy.class.getSimpleName();
    private static final Map b = new LinkedHashMap();
    private final Map c = new LinkedHashMap();

    public static void a(Class cls) {
        synchronized (b) {
            b.put(cls, new ix(cls));
        }
    }

    public final synchronized void a(Context context) {
        if (context == null) {
            iw.a(5, a, "Null context.");
        } else {
            synchronized (b) {
                ArrayList<ix> arrayList = new ArrayList(b.values());
            }
            for (ix ixVar : arrayList) {
                try {
                    Object obj = (ixVar.a == null || VERSION.SDK_INT < ixVar.b) ? null : 1;
                    if (obj != null) {
                        iz izVar = (iz) ixVar.a.newInstance();
                        izVar.a(context);
                        this.c.put(ixVar.a, izVar);
                    }
                } catch (Exception e) {
                    iw.a(5, a, "Flurry Module for class " + ixVar.a + " is not available:", e);
                }
            }
            ie.a();
        }
    }

    public final iz b(Class cls) {
        if (cls == null) {
            return null;
        }
        iz izVar;
        synchronized (this.c) {
            izVar = (iz) this.c.get(cls);
        }
        if (izVar != null) {
            return izVar;
        }
        throw new IllegalStateException("Module was not registered/initialized. " + cls);
    }
}
