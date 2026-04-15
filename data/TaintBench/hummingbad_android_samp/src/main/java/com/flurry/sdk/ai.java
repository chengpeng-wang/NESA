package com.flurry.sdk;

import java.lang.ref.WeakReference;

final class ai implements ik {
    final /* synthetic */ ag a;

    ai(ag agVar) {
        this.a = agVar;
    }

    public final /* synthetic */ void notify(ij ijVar) {
        aa aaVar = (aa) ijVar;
        if (aaVar.b == this.a.a && aaVar.a != null) {
            this.a.n = new WeakReference(aaVar.a);
            this.a.a(this.a.n);
        }
    }
}
