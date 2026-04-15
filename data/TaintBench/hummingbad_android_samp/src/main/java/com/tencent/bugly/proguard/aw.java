package com.tencent.bugly.proguard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public final class aw extends m implements Cloneable {
    static Map<String, String> A = new HashMap();
    static Map<String, String> B = new HashMap();
    static final /* synthetic */ boolean C;
    static Map<String, String> u = new HashMap();
    static au v = new au();
    static at w = new at();
    static ArrayList<at> x = new ArrayList();
    static ArrayList<at> y = new ArrayList();
    static ArrayList<av> z = new ArrayList();
    public String a = "";
    public long b = 0;
    public String c = "";
    public String d = "";
    public String e = "";
    public String f = "";
    public String g = "";
    public Map<String, String> h = null;
    public String i = "";
    public au j = null;
    public int k = 0;
    public String l = "";
    public String m = "";
    public at n = null;
    public ArrayList<at> o = null;
    public ArrayList<at> p = null;
    public ArrayList<av> q = null;
    public Map<String, String> r = null;
    public Map<String, String> s = null;
    public String t = "";

    static {
        boolean z;
        if (aw.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        C = z;
        u.put("", "");
        x.add(new at());
        y.add(new at());
        z.add(new av());
        A.put("", "");
        B.put("", "");
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        aw awVar = (aw) o;
        if (n.a(this.a, awVar.a) && n.a(this.b, awVar.b) && n.a(this.c, awVar.c) && n.a(this.d, awVar.d) && n.a(this.e, awVar.e) && n.a(this.f, awVar.f) && n.a(this.g, awVar.g) && n.a(this.h, awVar.h) && n.a(this.i, awVar.i) && n.a(this.j, awVar.j) && n.a(this.k, awVar.k) && n.a(this.l, awVar.l) && n.a(this.m, awVar.m) && n.a(this.n, awVar.n) && n.a(this.o, awVar.o) && n.a(this.p, awVar.p) && n.a(this.q, awVar.q) && n.a(this.r, awVar.r) && n.a(this.s, awVar.s) && n.a(this.t, awVar.t)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        try {
            throw new Exception("Need define key first!");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Object clone() {
        Object obj = null;
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            if (C) {
                return obj;
            }
            throw new AssertionError();
        }
    }

    public void a(l lVar) {
        lVar.a(this.a, 0);
        lVar.a(this.b, 1);
        lVar.a(this.c, 2);
        if (this.d != null) {
            lVar.a(this.d, 3);
        }
        if (this.e != null) {
            lVar.a(this.e, 4);
        }
        if (this.f != null) {
            lVar.a(this.f, 5);
        }
        if (this.g != null) {
            lVar.a(this.g, 6);
        }
        if (this.h != null) {
            lVar.a(this.h, 7);
        }
        if (this.i != null) {
            lVar.a(this.i, 8);
        }
        if (this.j != null) {
            lVar.a(this.j, 9);
        }
        lVar.a(this.k, 10);
        if (this.l != null) {
            lVar.a(this.l, 11);
        }
        if (this.m != null) {
            lVar.a(this.m, 12);
        }
        if (this.n != null) {
            lVar.a(this.n, 13);
        }
        if (this.o != null) {
            lVar.a(this.o, 14);
        }
        if (this.p != null) {
            lVar.a(this.p, 15);
        }
        if (this.q != null) {
            lVar.a(this.q, 16);
        }
        if (this.r != null) {
            lVar.a(this.r, 17);
        }
        if (this.s != null) {
            lVar.a(this.s, 18);
        }
        if (this.t != null) {
            lVar.a(this.t, 19);
        }
    }

    public void a(k kVar) {
        this.a = kVar.a(0, true);
        this.b = kVar.a(this.b, 1, true);
        this.c = kVar.a(2, true);
        this.d = kVar.a(3, false);
        this.e = kVar.a(4, false);
        this.f = kVar.a(5, false);
        this.g = kVar.a(6, false);
        this.h = (Map) kVar.a(u, 7, false);
        this.i = kVar.a(8, false);
        this.j = (au) kVar.a(v, 9, false);
        this.k = kVar.a(this.k, 10, false);
        this.l = kVar.a(11, false);
        this.m = kVar.a(12, false);
        this.n = (at) kVar.a(w, 13, false);
        this.o = (ArrayList) kVar.a(x, 14, false);
        this.p = (ArrayList) kVar.a(y, 15, false);
        this.q = (ArrayList) kVar.a(z, 16, false);
        this.r = (Map) kVar.a(A, 17, false);
        this.s = (Map) kVar.a(B, 18, false);
        this.t = kVar.a(19, false);
    }

    public void a(StringBuilder stringBuilder, int i) {
        i iVar = new i(stringBuilder, i);
        iVar.a(this.a, "type");
        iVar.a(this.b, "crashTime");
        iVar.a(this.c, "expName");
        iVar.a(this.d, "expMessage");
        iVar.a(this.e, "expAddr");
        iVar.a(this.f, "crashThread");
        iVar.a(this.g, "callStack");
        iVar.a(this.h, "allStacks");
        iVar.a(this.i, "expuid");
        iVar.a(this.j, "session");
        iVar.a(this.k, "crashCount");
        iVar.a(this.l, "userid");
        iVar.a(this.m, "deviceId");
        iVar.a(this.n, "appInfo");
        iVar.a(this.o, "libInfos");
        iVar.a(this.p, "plugins");
        iVar.a(this.q, "attaches");
        iVar.a(this.r, "valueMap");
        iVar.a(this.s, "userMap");
        iVar.a(this.t, "gatewayIp");
    }
}
