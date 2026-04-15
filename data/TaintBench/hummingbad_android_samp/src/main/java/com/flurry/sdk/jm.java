package com.flurry.sdk;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public final class jm extends WeakReference {
    public jm(Object obj) {
        super(obj);
    }

    public final boolean equals(Object obj) {
        Object obj2 = get();
        return obj instanceof Reference ? obj2.equals(((Reference) obj).get()) : obj2.equals(obj);
    }

    public final int hashCode() {
        Object obj = get();
        return obj == null ? super.hashCode() : obj.hashCode();
    }
}
