package com.cleanmaster.base.util.misc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* compiled from: MapCollections */
final class f implements Set {
    final /* synthetic */ d a;

    f(d dVar) {
        this.a = dVar;
    }

    public final boolean add(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final boolean addAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    public final void clear() {
        this.a.c();
    }

    public final boolean contains(Object object) {
        return this.a.a(object) >= 0;
    }

    public final boolean containsAll(Collection collection) {
        return d.a(this.a.b(), collection);
    }

    public final boolean isEmpty() {
        return this.a.a() == 0;
    }

    public final Iterator iterator() {
        return new e(this.a, 0);
    }

    public final boolean remove(Object object) {
        int a = this.a.a(object);
        if (a < 0) {
            return false;
        }
        this.a.a(a);
        return true;
    }

    public final boolean removeAll(Collection collection) {
        return d.b(this.a.b(), collection);
    }

    public final boolean retainAll(Collection collection) {
        return d.c(this.a.b(), collection);
    }

    public final int size() {
        return this.a.a();
    }

    public final Object[] toArray() {
        return this.a.b(0);
    }

    public final Object[] toArray(Object[] array) {
        return this.a.a(array, 0);
    }

    public final boolean equals(Object object) {
        return d.a((Set) this, object);
    }

    public final int hashCode() {
        int i = 0;
        for (int a = this.a.a() - 1; a >= 0; a--) {
            Object a2 = this.a.a(a, 0);
            i += a2 == null ? 0 : a2.hashCode();
        }
        return i;
    }
}
