package com.flurry.sdk;

import java.lang.reflect.Method;

public final class lx {
    static Method a(Class cls, String str, Class... clsArr) {
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
