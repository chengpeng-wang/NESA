package com.picksbrowser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* compiled from: Reflection */
public final class i {
    private final Object a;
    private final String b;
    private Class c;
    private List d = new ArrayList();
    private List e = new ArrayList();
    private boolean f;
    private boolean g;

    public i(Object obj, String str) {
        this.a = obj;
        this.b = str;
        this.c = obj != null ? obj.getClass() : null;
    }

    public final i a() {
        this.f = true;
        return this;
    }

    public final Object b() {
        Method a = h.a(this.c, this.b, (Class[]) this.d.toArray(new Class[this.d.size()]));
        if (this.f) {
            a.setAccessible(true);
        }
        Object[] toArray = this.e.toArray();
        if (this.g) {
            return a.invoke(null, toArray);
        }
        return a.invoke(this.a, toArray);
    }
}
