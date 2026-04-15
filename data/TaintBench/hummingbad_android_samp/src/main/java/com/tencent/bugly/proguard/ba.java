package com.tencent.bugly.proguard;

/* compiled from: BUGLY */
public final class ba extends m implements Cloneable {
    public String a = "";
    public String b = "";

    public void a(l lVar) {
        lVar.a(this.a, 0);
        lVar.a(this.b, 1);
    }

    public void a(k kVar) {
        this.a = kVar.a(0, true);
        this.b = kVar.a(1, true);
    }

    public void a(StringBuilder stringBuilder, int i) {
    }
}
