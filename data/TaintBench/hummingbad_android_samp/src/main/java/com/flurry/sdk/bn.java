package com.flurry.sdk;

import java.util.concurrent.Callable;

final class bn implements Callable {
    final /* synthetic */ bm a;

    bn(bm bmVar) {
        this.a = bmVar;
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public Void call() {
        synchronized (this.a) {
            if (this.a.j == null) {
            } else {
                this.a.g();
                if (this.a.e()) {
                    this.a.d();
                    this.a.l = 0;
                }
            }
        }
        return null;
    }
}
