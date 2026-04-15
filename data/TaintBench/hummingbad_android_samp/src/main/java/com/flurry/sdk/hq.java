package com.flurry.sdk;

final class hq implements ik {
    final /* synthetic */ hp a;

    hq(hp hpVar) {
        this.a = hpVar;
    }

    public final /* synthetic */ void notify(ij ijVar) {
        if (this.a.h > 0 && this.a.h < System.currentTimeMillis()) {
            iw.a(4, hp.a, "No location received in 90 seconds , stopping LocationManager");
            hp.b(this.a);
        }
    }
}
