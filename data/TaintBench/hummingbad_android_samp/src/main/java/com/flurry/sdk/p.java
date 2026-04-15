package com.flurry.sdk;

final class p implements ik {
    final /* synthetic */ n a;

    p(n nVar) {
        this.a = nVar;
    }

    public final /* synthetic */ void notify(ij ijVar) {
        fk fkVar = (fk) ijVar;
        synchronized (this.a) {
            if (this.a.h == null) {
                this.a.h = fkVar.a;
                this.a.a((((long) this.a.h.b) * 1024) * 1204, ((long) this.a.h.c) * 1024);
                return;
            }
        }
    }
}
