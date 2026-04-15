package com.flurry.sdk;

final class o implements ik {
    final /* synthetic */ n a;

    o(n nVar) {
        this.a = nVar;
    }

    public final /* synthetic */ void notify(ij ijVar) {
        ic icVar = (ic) ijVar;
        if (id.kPaused.equals(icVar.b)) {
            this.a.b.a(icVar.a);
        } else if (id.kResumed.equals(icVar.b)) {
            this.a.b.b(icVar.a);
        } else if (id.kDestroyed.equals(icVar.b)) {
            this.a.b.c(icVar.a);
        }
    }
}
