package com.flurry.sdk;

public final class ke extends lz {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ jx c;

    public ke(jx jxVar, String str, String str2) {
        this.c = jxVar;
        this.a = str;
        this.b = str2;
    }

    public final void safeRun() {
        if (!this.c.d.a(this.a, this.b)) {
            iw.a(6, this.c.b, "Internal error. Block wasn't deleted with id = " + this.a);
        }
        if (!this.c.c.remove(this.a)) {
            iw.a(6, this.c.b, "Internal error. Block with id = " + this.a + " was not in progress state");
        }
    }
}
