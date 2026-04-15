package com.flurry.sdk;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ly {
    private final Object a;
    private final String b;
    private Class c;
    private final List d = new ArrayList();
    private final List e = new ArrayList();
    private boolean f;

    public ly(Object obj, String str) {
        this.a = obj;
        this.b = str;
        this.c = obj != null ? obj.getClass() : null;
    }

    public final ly a(Class cls) {
        this.f = true;
        this.c = cls;
        return this;
    }

    public final ly a(Class cls, Object obj) {
        this.d.add(cls);
        this.e.add(obj);
        return this;
    }

    public final Object a() {
        Method a = lx.a(this.c, this.b, (Class[]) this.d.toArray(new Class[this.d.size()]));
        Object[] toArray = this.e.toArray();
        return this.f ? a.invoke(null, toArray) : a.invoke(this.a, toArray);
    }
}
