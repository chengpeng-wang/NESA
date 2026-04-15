package com.tencent.bugly.proguard;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public final class bb extends m implements Cloneable {
    static ba k = new ba();
    static Map<String, String> l = new HashMap();
    static final /* synthetic */ boolean m;
    public boolean a = true;
    public boolean b = true;
    public boolean c = true;
    public String d = "";
    public String e = "";
    public ba f = null;
    public Map<String, String> g = null;
    public long h = 0;
    public String i = "";
    public String j = "";

    static {
        boolean z;
        if (bb.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        m = z;
        l.put("", "");
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        bb bbVar = (bb) o;
        if (n.a(this.a, bbVar.a) && n.a(this.b, bbVar.b) && n.a(this.c, bbVar.c) && n.a(this.d, bbVar.d) && n.a(this.e, bbVar.e) && n.a(this.f, bbVar.f) && n.a(this.g, bbVar.g) && n.a(this.h, bbVar.h) && n.a(this.i, bbVar.i) && n.a(this.j, bbVar.j)) {
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
            if (m) {
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
        lVar.a(this.h, 7);
        if (this.i != null) {
            lVar.a(this.i, 8);
        }
        if (this.j != null) {
            lVar.a(this.j, 9);
        }
    }

    public void a(k kVar) {
        this.a = kVar.a(this.a, 0, true);
        this.b = kVar.a(this.b, 1, true);
        this.c = kVar.a(this.c, 2, true);
        this.d = kVar.a(3, false);
        this.e = kVar.a(4, false);
        this.f = (ba) kVar.a(k, 5, false);
        this.g = (Map) kVar.a(l, 6, false);
        this.h = kVar.a(this.h, 7, false);
        this.i = kVar.a(8, false);
        this.j = kVar.a(9, false);
    }

    public void a(StringBuilder stringBuilder, int i) {
        i iVar = new i(stringBuilder, i);
        iVar.a(this.a, "enable");
        iVar.a(this.b, "enableUserInfo");
        iVar.a(this.c, "enableQuery");
        iVar.a(this.d, MASTNativeAdConstants.RESPONSE_URL);
        iVar.a(this.e, "expUrl");
        iVar.a(this.f, "security");
        iVar.a(this.g, "valueMap");
        iVar.a(this.h, "strategylastUpdateTime");
        iVar.a(this.i, "httpsUrl");
        iVar.a(this.j, "httpsExpUrl");
    }
}
