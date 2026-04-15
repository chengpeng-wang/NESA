package com.cleanmaster.base.util.misc;

import java.util.Map;

/* compiled from: ArraySet */
class b extends d {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    /* access modifiers changed from: protected */
    public int a() {
        return this.a.g;
    }

    /* access modifiers changed from: protected */
    public Object a(int i, int i2) {
        return this.a.f[i];
    }

    /* access modifiers changed from: protected */
    public int a(Object obj) {
        return obj == null ? this.a.a() : this.a.a(obj, obj.hashCode());
    }

    /* access modifiers changed from: protected */
    public Map b() {
        throw new UnsupportedOperationException("not a map");
    }

    /* access modifiers changed from: protected */
    public void a(int i) {
        this.a.c(i);
    }

    /* access modifiers changed from: protected */
    public void c() {
        this.a.clear();
    }
}
