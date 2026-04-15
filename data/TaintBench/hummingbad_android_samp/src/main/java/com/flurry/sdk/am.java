package com.flurry.sdk;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class am {
    private static final String a = am.class.getSimpleName();
    private final ik b = new an(this);
    private final ik c = new ao(this);
    private final TreeSet d = new TreeSet();

    public am() {
        il.a().a("com.flurry.android.sdk.AssetCacheManagerStatusEvent", this.b);
        il.a().a("com.flurry.android.impl.ads.FreqCapEvent", this.c);
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(dj djVar) {
        if (djVar != null) {
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                ct ctVar = (ct) it.next();
                List<ec> list = ctVar.a.e;
                if (list != null) {
                    for (ec ecVar : list) {
                        if (djVar.a.equals(ecVar.a) && djVar.b.equals(ecVar.b)) {
                            iw.a(3, a, "Removed frequency capped ad unit -- adspace: " + ctVar.a.b);
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    private synchronized void c() {
        Iterator it = this.d.iterator();
        while (it.hasNext()) {
            ct ctVar = (ct) it.next();
            if (!lt.a(ctVar.a.c)) {
                iw.a(3, a, "Removed expired ad unit -- adspace: " + ctVar.a.b);
                it.remove();
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void d() {
        hz.a.b(new ap(this, new ArrayList(this.d)));
    }

    public final synchronized int a() {
        c();
        return this.d.size();
    }

    public final synchronized void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                ct ctVar = (ct) it.next();
                if (ctVar.a.g.equals(str)) {
                    iw.a(3, a, "Removed grouped ad unit -- adspace: " + ctVar.a.b);
                    it.remove();
                }
            }
        }
    }

    public final synchronized void a(Collection collection) {
        if (collection != null) {
            this.d.addAll(collection);
        }
    }

    public final synchronized List b() {
        ArrayList arrayList;
        arrayList = new ArrayList();
        ct ctVar = (ct) this.d.pollFirst();
        if (ctVar != null) {
            arrayList.add(ctVar);
            String str = ctVar.a.g;
            if (!TextUtils.isEmpty(str)) {
                Iterator it = this.d.iterator();
                while (it.hasNext()) {
                    ctVar = (ct) it.next();
                    if (!str.equals(ctVar.a.g)) {
                        break;
                    }
                    arrayList.add(ctVar);
                    it.remove();
                }
            }
        }
        return arrayList;
    }
}
