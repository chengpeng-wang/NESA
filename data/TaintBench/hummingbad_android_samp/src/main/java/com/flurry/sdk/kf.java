package com.flurry.sdk;

public final class kf extends lz {
    final /* synthetic */ String a;
    final /* synthetic */ jx b;

    public kf(jx jxVar, String str) {
        this.b = jxVar;
        this.a = str;
    }

    public final void safeRun() {
        if (!this.b.c.remove(this.a)) {
            iw.a(6, this.b.b, "Internal error. Block with id = " + this.a + " was not in progress state");
        }
    }
}
