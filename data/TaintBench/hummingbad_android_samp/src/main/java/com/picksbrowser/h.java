package com.picksbrowser;

import java.lang.reflect.Method;

/* compiled from: Reflection */
public final class h {
    public static Method a(Class cls, String str, Class... clsArr) {
        while (cls != null) {
            try {
                return cls.getDeclaredMethod(str, clsArr);
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }
}
