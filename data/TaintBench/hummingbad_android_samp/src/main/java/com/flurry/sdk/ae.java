package com.flurry.sdk;

import android.content.Context;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

public class ae {
    private static final String a = ae.class.getSimpleName();
    private final SparseArray b = new SparseArray();
    private final ig c = new ig(new WeakHashMap());

    private synchronized List d(Context context) {
        return context == null ? Collections.emptyList() : new ArrayList(this.c.a(context));
    }

    public final synchronized void a(Context context) {
        if (context != null) {
            for (af b : this.c.a(context)) {
                b.b();
            }
        }
    }

    public final synchronized void a(Context context, af afVar) {
        if (!(context == null || afVar == null)) {
            this.b.put(afVar.d(), afVar);
            this.c.a((Object) context, (Object) afVar);
        }
    }

    public final synchronized void b(Context context) {
        if (context != null) {
            for (af c : this.c.a(context.getApplicationContext())) {
                c.c();
            }
        }
    }

    public final synchronized boolean b(Context context, af afVar) {
        boolean z;
        if (context == null || afVar == null) {
            z = false;
        } else {
            this.b.remove(afVar.d());
            z = this.c.b(context, afVar);
        }
        return z;
    }

    public final synchronized void c(Context context) {
        if (context != null) {
            for (af a : d(context)) {
                a.a();
            }
        }
    }
}
