package com.flurry.sdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ct implements Comparable {
    private static int e;
    public final dv a;
    public final List b = new ArrayList();
    public final Map c = new HashMap();
    public int d;
    private final int f;

    public ct(dv dvVar) {
        int i = e + 1;
        e = i;
        this.f = i;
        this.a = dvVar;
        i = 0;
        while (true) {
            int i2 = i;
            if (i2 < dvVar.d.size()) {
                this.b.add(new cu());
                String str = ((dq) dvVar.d.get(i2)).f;
                Map map = this.c;
                n a = n.a();
                map.put(str, a.i != null ? a.i.a(str) : null);
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    private boolean c(int i) {
        return !b(i).isEmpty();
    }

    public final dq a(int i) {
        List list = this.a.d;
        return (list == null || list.size() <= i) ? null : (dq) list.get(i);
    }

    public final String a() {
        return ((dq) this.a.d.get(this.d)).f;
    }

    public final List b() {
        return this.a.w != null ? this.a.w.b : Collections.emptyList();
    }

    public final List b(int i) {
        if (i < 0 || i >= this.b.size()) {
            return Collections.emptyList();
        }
        cu cuVar = (cu) this.b.get(i);
        return cuVar.a == null ? Collections.emptyList() : cuVar.a;
    }

    public final dh c() {
        int i = this.d;
        for (dh dhVar : dh.values()) {
            String dhVar2 = dhVar.toString();
            dq a = a(i);
            if (dhVar2.equals(a != null ? a.d.d : null)) {
                return dhVar;
            }
        }
        return dh.UNKNOWN;
    }

    public final /* bridge */ /* synthetic */ int compareTo(Object obj) {
        ct ctVar = (ct) obj;
        return this.f > ctVar.f ? 1 : this.f < ctVar.f ? -1 : 0;
    }

    public final boolean d() {
        return c().equals(dh.TAKEOVER);
    }

    public final boolean e() {
        List list = this.a.d;
        int i = 0;
        while (i < list.size()) {
            Object obj = bl.values()[((dq) list.get(i)).g];
            if ((bl.CACHE_ONLY.equals(obj) || bl.CACHE_OR_STREAM.equals(obj)) && c(i)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public final boolean f() {
        List list = this.a.d;
        int i = 0;
        while (i < list.size()) {
            if (bl.CACHE_ONLY.equals(bl.values()[((dq) list.get(i)).g]) && c(i)) {
                return true;
            }
            i++;
        }
        return false;
    }
}
