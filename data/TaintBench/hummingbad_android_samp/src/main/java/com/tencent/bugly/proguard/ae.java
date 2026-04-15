package com.tencent.bugly.proguard;

import java.lang.reflect.Field;

/* compiled from: BUGLY */
public class ae {
    public static void a(Class<?> cls, String str, Object obj, Object obj2) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj2, obj);
        } catch (Exception e) {
        }
    }
}
