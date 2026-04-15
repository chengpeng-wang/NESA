package com.tencent.bugly.proguard;

import com.umeng.analytics.onlineconfig.a;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public final class ay extends m implements Cloneable {
    static final /* synthetic */ boolean A;
    static byte[] y = new byte[1];
    static Map<String, String> z = new HashMap();
    public int a = 0;
    public String b = "";
    public String c = "";
    public String d = "";
    public String e = "";
    public String f = "";
    public int g = 0;
    public byte[] h = null;
    public String i = "";
    public String j = "";
    public Map<String, String> k = null;
    public String l = "";
    public long m = 0;
    public String n = "";
    public String o = "";
    public String p = "";
    public long q = 0;
    public String r = "";
    public String s = "";
    public String t = "";
    public String u = "";
    public String v = "";
    public String w = "";
    public String x = "";

    static {
        boolean z;
        if (ay.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        A = z;
        y[0] = (byte) 0;
        z.put("", "");
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        ay ayVar = (ay) o;
        if (n.a(this.a, ayVar.a) && n.a(this.b, ayVar.b) && n.a(this.c, ayVar.c) && n.a(this.d, ayVar.d) && n.a(this.e, ayVar.e) && n.a(this.f, ayVar.f) && n.a(this.g, ayVar.g) && n.a(this.h, ayVar.h) && n.a(this.i, ayVar.i) && n.a(this.j, ayVar.j) && n.a(this.k, ayVar.k) && n.a(this.l, ayVar.l) && n.a(this.m, ayVar.m) && n.a(this.n, ayVar.n) && n.a(this.o, ayVar.o) && n.a(this.p, ayVar.p) && n.a(this.q, ayVar.q) && n.a(this.r, ayVar.r) && n.a(this.s, ayVar.s) && n.a(this.t, ayVar.t) && n.a(this.u, ayVar.u) && n.a(this.v, ayVar.v) && n.a(this.w, ayVar.w) && n.a(this.x, ayVar.x)) {
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
            if (A) {
                return obj;
            }
            throw new AssertionError();
        }
    }

    public void a(l lVar) {
        lVar.a(this.a, 0);
        lVar.a(this.b, 1);
        lVar.a(this.c, 2);
        lVar.a(this.d, 3);
        if (this.e != null) {
            lVar.a(this.e, 4);
        }
        lVar.a(this.f, 5);
        lVar.a(this.g, 6);
        lVar.a(this.h, 7);
        if (this.i != null) {
            lVar.a(this.i, 8);
        }
        if (this.j != null) {
            lVar.a(this.j, 9);
        }
        if (this.k != null) {
            lVar.a(this.k, 10);
        }
        if (this.l != null) {
            lVar.a(this.l, 11);
        }
        lVar.a(this.m, 12);
        if (this.n != null) {
            lVar.a(this.n, 13);
        }
        if (this.o != null) {
            lVar.a(this.o, 14);
        }
        if (this.p != null) {
            lVar.a(this.p, 15);
        }
        lVar.a(this.q, 16);
        if (this.r != null) {
            lVar.a(this.r, 17);
        }
        if (this.s != null) {
            lVar.a(this.s, 18);
        }
        if (this.t != null) {
            lVar.a(this.t, 19);
        }
        if (this.u != null) {
            lVar.a(this.u, 20);
        }
        if (this.v != null) {
            lVar.a(this.v, 21);
        }
        if (this.w != null) {
            lVar.a(this.w, 22);
        }
        if (this.x != null) {
            lVar.a(this.x, 23);
        }
    }

    public void a(k kVar) {
        this.a = kVar.a(this.a, 0, true);
        this.b = kVar.a(1, true);
        this.c = kVar.a(2, true);
        this.d = kVar.a(3, true);
        this.e = kVar.a(4, false);
        this.f = kVar.a(5, true);
        this.g = kVar.a(this.g, 6, true);
        this.h = kVar.a(y, 7, true);
        this.i = kVar.a(8, false);
        this.j = kVar.a(9, false);
        this.k = (Map) kVar.a(z, 10, false);
        this.l = kVar.a(11, false);
        this.m = kVar.a(this.m, 12, false);
        this.n = kVar.a(13, false);
        this.o = kVar.a(14, false);
        this.p = kVar.a(15, false);
        this.q = kVar.a(this.q, 16, false);
        this.r = kVar.a(17, false);
        this.s = kVar.a(18, false);
        this.t = kVar.a(19, false);
        this.u = kVar.a(20, false);
        this.v = kVar.a(21, false);
        this.w = kVar.a(22, false);
        this.x = kVar.a(23, false);
    }

    public void a(StringBuilder stringBuilder, int i) {
        i iVar = new i(stringBuilder, i);
        iVar.a(this.a, "platformId");
        iVar.a(this.b, "prodId");
        iVar.a(this.c, "bundleId");
        iVar.a(this.d, "version");
        iVar.a(this.e, a.c);
        iVar.a(this.f, "sdkVer");
        iVar.a(this.g, "cmd");
        iVar.a(this.h, "sBuffer");
        iVar.a(this.i, "model");
        iVar.a(this.j, "osVer");
        iVar.a(this.k, "reserved");
        iVar.a(this.l, "sessionId");
        iVar.a(this.m, "strategylastUpdateTime");
        iVar.a(this.n, "sdkId");
        iVar.a(this.o, "deviceId");
        iVar.a(this.p, "apn");
        iVar.a(this.q, "uploadTime");
        iVar.a(this.r, "imei");
        iVar.a(this.s, "qimei");
        iVar.a(this.t, "imsi");
        iVar.a(this.u, "mac");
        iVar.a(this.v, "androidId");
        iVar.a(this.w, "networkType");
        iVar.a(this.x, "idfv");
    }
}
