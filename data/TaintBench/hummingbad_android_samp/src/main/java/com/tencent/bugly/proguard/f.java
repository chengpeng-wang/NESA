package com.tencent.bugly.proguard;

import com.mb.bdapp.db.DuAd;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public final class f extends m {
    static byte[] k = null;
    static Map<String, String> l = null;
    static final /* synthetic */ boolean m;
    public short a = (short) 0;
    public byte b = (byte) 0;
    public int c = 0;
    public int d = 0;
    public String e = null;
    public String f = null;
    public byte[] g;
    public int h = 0;
    public Map<String, String> i;
    public Map<String, String> j;

    static {
        boolean z;
        if (f.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        m = z;
    }

    public boolean equals(Object o) {
        f fVar = (f) o;
        if (n.a(1, fVar.a) && n.a(1, fVar.b) && n.a(1, fVar.c) && n.a(1, fVar.d) && n.a(Integer.valueOf(1), fVar.e) && n.a(Integer.valueOf(1), fVar.f) && n.a(Integer.valueOf(1), fVar.g) && n.a(1, fVar.h) && n.a(Integer.valueOf(1), fVar.i) && n.a(Integer.valueOf(1), fVar.j)) {
            return true;
        }
        return false;
    }

    public Object clone() {
        Object obj = null;
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            if (m) {
                return obj;
            }
            throw new AssertionError();
        }
    }

    public void a(l lVar) {
        lVar.a(this.a, 1);
        lVar.b(this.b, 2);
        lVar.a(this.c, 3);
        lVar.a(this.d, 4);
        lVar.a(this.e, 5);
        lVar.a(this.f, 6);
        lVar.a(this.g, 7);
        lVar.a(this.h, 8);
        lVar.a(this.i, 9);
        lVar.a(this.j, 10);
    }

    public void a(k kVar) {
        try {
            this.a = kVar.a(this.a, 1, true);
            this.b = kVar.a(this.b, 2, true);
            this.c = kVar.a(this.c, 3, true);
            this.d = kVar.a(this.d, 4, true);
            this.e = kVar.a(5, true);
            this.f = kVar.a(6, true);
            if (k == null) {
                k = new byte[]{(byte) 0};
            }
            this.g = kVar.a(k, 7, true);
            this.h = kVar.a(this.h, 8, true);
            if (l == null) {
                l = new HashMap();
                l.put("", "");
            }
            this.i = (Map) kVar.a(l, 9, true);
            if (l == null) {
                l = new HashMap();
                l.put("", "");
            }
            this.j = (Map) kVar.a(l, 10, true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RequestPacket decode error " + g.a(this.g));
            throw new RuntimeException(e);
        }
    }

    public void a(StringBuilder stringBuilder, int i) {
        i iVar = new i(stringBuilder, i);
        iVar.a(this.a, "iVersion");
        iVar.a(this.b, "cPacketType");
        iVar.a(this.c, "iMessageType");
        iVar.a(this.d, "iRequestId");
        iVar.a(this.e, "sServantName");
        iVar.a(this.f, "sFuncName");
        iVar.a(this.g, "sBuffer");
        iVar.a(this.h, "iTimeout");
        iVar.a(this.i, "context");
        iVar.a(this.j, DuAd.DB_STATUS);
    }
}
