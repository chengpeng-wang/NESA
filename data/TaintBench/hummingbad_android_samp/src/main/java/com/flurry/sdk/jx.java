package com.flurry.sdk;

import java.util.HashSet;
import java.util.Set;

public abstract class jx {
    public final String b;
    public final Set c = new HashSet();
    public kk d;
    public String e = "defaultDataKey_";

    public jx(String str) {
        this.b = str;
        il.a().a("com.flurry.android.sdk.NetworkStateEvent", new jy(this));
        hz.a.b(new jz(this));
    }

    public abstract void a(byte[] bArr, String str, String str2);

    public final void b() {
        hz.a.b(new kb(this));
    }

    /* access modifiers changed from: final */
    public final boolean c() {
        return !(this.c.size() <= 5);
    }
}
