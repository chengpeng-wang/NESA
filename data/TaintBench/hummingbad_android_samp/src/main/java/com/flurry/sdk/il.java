package com.flurry.sdk;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class il {
    private static il a = null;
    private final ig b = new ig();
    private final ig c = new ig();

    private il() {
    }

    public static synchronized il a() {
        il ilVar;
        synchronized (il.class) {
            if (a == null) {
                a = new il();
            }
            ilVar = a;
        }
        return ilVar;
    }

    private synchronized List a(String str) {
        List emptyList;
        if (TextUtils.isEmpty(str)) {
            emptyList = Collections.emptyList();
        } else {
            ArrayList arrayList = new ArrayList();
            Iterator it = this.b.a(str).iterator();
            while (it.hasNext()) {
                ik ikVar = (ik) ((jm) it.next()).get();
                if (ikVar == null) {
                    it.remove();
                } else {
                    arrayList.add(ikVar);
                }
            }
            Object emptyList2 = arrayList;
        }
        return emptyList2;
    }

    public final void a(ij ijVar) {
        if (ijVar != null) {
            for (ik imVar : a(ijVar.d)) {
                hz.a.b(new im(this, imVar, ijVar));
            }
        }
    }

    public final synchronized void a(ik ikVar) {
        if (ikVar != null) {
            jm jmVar = new jm(ikVar);
            for (String b : this.c.a(jmVar)) {
                this.b.b(b, jmVar);
            }
            this.c.b(jmVar);
        }
    }

    public final synchronized void a(String str, ik ikVar) {
        Object obj = null;
        synchronized (this) {
            if (!(TextUtils.isEmpty(str) || ikVar == null)) {
                Object jmVar = new jm(ikVar);
                List a = this.b.a((Object) str, false);
                if (a != null && a.contains(jmVar)) {
                    obj = 1;
                }
                if (obj == null) {
                    this.b.a((Object) str, jmVar);
                    this.c.a(jmVar, (Object) str);
                }
            }
        }
    }

    public final synchronized int b() {
        return TextUtils.isEmpty("com.flurry.android.sdk.TickEvent") ? 0 : this.b.a("com.flurry.android.sdk.TickEvent").size();
    }

    public final synchronized void b(String str, ik ikVar) {
        if (!TextUtils.isEmpty(str)) {
            jm jmVar = new jm(ikVar);
            this.b.b(str, jmVar);
            this.c.b(jmVar, str);
        }
    }
}
