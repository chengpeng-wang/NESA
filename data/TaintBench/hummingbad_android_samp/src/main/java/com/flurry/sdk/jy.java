package com.flurry.sdk;

final class jy implements ik {
    final /* synthetic */ jx a;

    jy(jx jxVar) {
        this.a = jxVar;
    }

    public final /* synthetic */ void notify(ij ijVar) {
        hs hsVar = (hs) ijVar;
        iw.a(4, this.a.b, "onNetworkStateChanged : isNetworkEnable = " + hsVar.a);
        if (hsVar.a) {
            this.a.b();
        }
    }
}
