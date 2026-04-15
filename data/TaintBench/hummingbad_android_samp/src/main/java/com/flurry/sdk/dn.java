package com.flurry.sdk;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public final class dn {
    private static final String a = dn.class.getSimpleName();
    private final ig b = new ig();

    public static boolean a(long j) {
        return j <= System.currentTimeMillis();
    }

    private synchronized void b(String str) {
        this.b.b(str);
    }

    public final synchronized List a() {
        return new ArrayList(this.b.b());
    }

    public final synchronized List a(String str) {
        return new ArrayList(this.b.a(str));
    }

    public final synchronized void a(dj djVar) {
        if (djVar != null) {
            if (!(djVar.a == null || TextUtils.isEmpty(djVar.b))) {
                a(djVar.a, djVar.b);
                if (djVar.f != -1) {
                    this.b.a(djVar.b, (Object) djVar);
                }
            }
        }
    }

    public final synchronized void a(ed edVar, String str) {
        if (edVar != null) {
            if (!TextUtils.isEmpty(str)) {
                for (Object obj : this.b.a(str)) {
                    if (obj.a.equals(edVar)) {
                        break;
                    }
                }
                Object obj2 = null;
                if (obj2 != null) {
                    this.b.b(str, obj2);
                }
            }
        }
    }

    public final synchronized void b() {
        for (dj djVar : a()) {
            if (a(djVar.d)) {
                iw.a(3, a, "expiring freq cap for id: " + djVar.b + " capType:" + djVar.a + " expiration: " + djVar.d + " epoch" + System.currentTimeMillis());
                b(djVar.b);
            }
        }
    }
}
