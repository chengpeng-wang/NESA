package com.cleanmaster.base.util.misc;

import java.util.Iterator;

/* compiled from: MapCollections */
final class e implements Iterator {
    final int a;
    int b;
    int c;
    boolean d = false;
    final /* synthetic */ d e;

    e(d dVar, int i) {
        this.e = dVar;
        this.a = i;
        this.b = dVar.a();
    }

    public final boolean hasNext() {
        return this.c < this.b;
    }

    public final Object next() {
        Object a = this.e.a(this.c, this.a);
        this.c++;
        this.d = true;
        return a;
    }

    public final void remove() {
        if (this.d) {
            this.c--;
            this.b--;
            this.d = false;
            this.e.a(this.c);
            return;
        }
        throw new IllegalStateException();
    }
}
