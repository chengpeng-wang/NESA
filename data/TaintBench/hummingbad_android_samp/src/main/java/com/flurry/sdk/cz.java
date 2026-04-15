package com.flurry.sdk;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public final class cz {
    private static int e = 1;
    public int a;
    public long b;
    public String c;
    public List d;

    private cz() {
    }

    /* synthetic */ cz(byte b) {
        this();
    }

    public cz(String str) {
        int i = e;
        e = i + 1;
        this.a = i;
        this.b = hg.a().a;
        this.c = str;
        this.d = new ArrayList();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof cz)) {
            return false;
        }
        cz czVar = (cz) obj;
        if (this.a == czVar.a && this.b == czVar.b && TextUtils.equals(this.c, czVar.c)) {
            if (this.d == czVar.d) {
                return true;
            }
            if (this.d != null && this.d.equals(czVar.d)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = (int) (((long) (this.a ^ 17)) ^ this.b);
        if (this.c != null) {
            i ^= this.c.hashCode();
        }
        return this.d != null ? i ^ this.d.hashCode() : i;
    }
}
