package com.tencent.bugly.proguard;

import com.mb.bdapp.db.DuAd;

/* compiled from: BUGLY */
public final class az extends m implements Cloneable {
    static byte[] h = new byte[1];
    static final /* synthetic */ boolean i;
    public byte a = (byte) 0;
    public int b = 0;
    public byte[] c = null;
    public String d = "";
    public long e = 0;
    public String f = "";
    public String g = "";

    static {
        boolean z;
        if (az.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        i = z;
        h[0] = (byte) 0;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        az azVar = (az) o;
        if (n.a(this.a, azVar.a) && n.a(this.b, azVar.b) && n.a(this.c, azVar.c) && n.a(this.d, azVar.d) && n.a(this.e, azVar.e) && n.a(this.f, azVar.f) && n.a(this.g, azVar.g)) {
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
            if (i) {
                return obj;
            }
            throw new AssertionError();
        }
    }

    public void a(l lVar) {
        lVar.b(this.a, 0);
        lVar.a(this.b, 1);
        if (this.c != null) {
            lVar.a(this.c, 2);
        }
        if (this.d != null) {
            lVar.a(this.d, 3);
        }
        lVar.a(this.e, 4);
        if (this.f != null) {
            lVar.a(this.f, 5);
        }
        if (this.g != null) {
            lVar.a(this.g, 6);
        }
    }

    public void a(k kVar) {
        this.a = kVar.a(this.a, 0, true);
        this.b = kVar.a(this.b, 1, true);
        this.c = kVar.a(h, 2, false);
        this.d = kVar.a(3, false);
        this.e = kVar.a(this.e, 4, false);
        this.f = kVar.a(5, false);
        this.g = kVar.a(6, false);
    }

    public void a(StringBuilder stringBuilder, int i) {
        i iVar = new i(stringBuilder, i);
        iVar.a(this.a, "result");
        iVar.a(this.b, "cmd");
        iVar.a(this.c, "sBuffer");
        iVar.a(this.d, "gatewayIp");
        iVar.a(this.e, "serverTime");
        iVar.a(this.f, DuAd.DB_STATUS);
        iVar.a(this.g, "qimei");
    }
}
