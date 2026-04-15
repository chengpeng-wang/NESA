package com.esotericsoftware.kryo.util;

import com.esotericsoftware.minlog.Log;

public class Util {
    public static boolean isAndroid = true;

    static {
        try {
            Class.forName("android.os.Process");
        } catch (Exception e) {
        }
    }

    public static Class getWrapperClass(Class cls) {
        if (cls == Integer.TYPE) {
            return Integer.class;
        }
        if (cls == Float.TYPE) {
            return Float.class;
        }
        if (cls == Boolean.TYPE) {
            return Boolean.class;
        }
        if (cls == Long.TYPE) {
            return Long.class;
        }
        if (cls == Byte.TYPE) {
            return Byte.class;
        }
        if (cls == Character.TYPE) {
            return Character.class;
        }
        if (cls == Short.TYPE) {
            return Short.class;
        }
        return Double.class;
    }

    public static boolean isWrapperClass(Class cls) {
        return cls == Integer.class || cls == Float.class || cls == Boolean.class || cls == Long.class || cls == Byte.class || cls == Character.class || cls == Short.class || cls == Double.class;
    }

    public static void log(String str, Object obj) {
        if (obj != null) {
            Class cls = obj.getClass();
            if (!cls.isPrimitive() && cls != Boolean.class && cls != Byte.class && cls != Character.class && cls != Short.class && cls != Integer.class && cls != Long.class && cls != Float.class && cls != Double.class && cls != String.class) {
                Log.debug("kryo", str + ": " + string(obj));
            }
        }
    }

    public static String string(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class cls = obj.getClass();
        if (cls.isArray()) {
            return className(cls);
        }
        try {
            if (cls.getMethod("toString", new Class[0]).getDeclaringClass() == Object.class) {
                return cls.getSimpleName();
            }
        } catch (Exception e) {
        }
        return String.valueOf(obj);
    }

    public static String className(Class cls) {
        if (cls.isArray()) {
            Class elementClass = getElementClass(cls);
            StringBuilder stringBuilder = new StringBuilder(16);
            int dimensionCount = getDimensionCount(cls);
            for (int i = 0; i < dimensionCount; i++) {
                stringBuilder.append("[]");
            }
            return className(elementClass) + stringBuilder;
        } else if (cls.isPrimitive() || cls == Object.class || cls == Boolean.class || cls == Byte.class || cls == Character.class || cls == Short.class || cls == Integer.class || cls == Long.class || cls == Float.class || cls == Double.class || cls == String.class) {
            return cls.getSimpleName();
        } else {
            return cls.getName();
        }
    }

    public static int getDimensionCount(Class cls) {
        int i = 0;
        for (Class componentType = cls.getComponentType(); componentType != null; componentType = componentType.getComponentType()) {
            i++;
        }
        return i;
    }

    public static Class getElementClass(Class cls) {
        while (cls.getComponentType() != null) {
            cls = cls.getComponentType();
        }
        return cls;
    }
}
