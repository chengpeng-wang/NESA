package com.splunk.mint.network.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public final class ReflectionUtil {
    public static final boolean includeObject = false;

    public static Constructor<?> findConstructor(String className, Class<?>[] expectedParams) throws ClassNotFoundException {
        for (Constructor<?> c : Class.forName(className).getDeclaredConstructors()) {
            Class<?>[] paramTypes = c.getParameterTypes();
            if (paramTypes.length == expectedParams.length) {
                boolean match = true;
                for (int i = 0; i < paramTypes.length; i++) {
                    if (!paramTypes[i].equals(expectedParams[i])) {
                        match = false;
                    }
                }
                if (match) {
                    return c;
                }
            }
        }
        return null;
    }

    public static Set<Class<?>> getAllSuperTypes(Class<?> type) {
        Set<Class<?>> result = new HashSet();
        if (!(type == null || type.equals(Object.class))) {
            result.add(type);
            result.addAll(getAllSuperTypes(type.getSuperclass()));
            for (Class<?> ifc : type.getInterfaces()) {
                result.addAll(getAllSuperTypes(ifc));
            }
        }
        return result;
    }

    public static Set<Method> getAllMethods(Class<?> type) {
        Set<Method> result = new HashSet();
        for (Class<?> t : getAllSuperTypes(type)) {
            for (Method m : t.getDeclaredMethods()) {
                result.add(m);
            }
        }
        return result;
    }

    public static String extractCallingMethod(String[] excludes) {
        for (StackTraceElement e : new Throwable().getStackTrace()) {
            if (!inExcluded(e.getClassName(), excludes)) {
                return e.getClassName() + "." + e.getMethodName() + ":" + e.getLineNumber();
            }
        }
        return null;
    }

    public static boolean callingClassAnyOf(String[] classNames) {
        for (StackTraceElement e : new Throwable().getStackTrace()) {
            if (contains(e.getClassName(), classNames)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean contains(String className, String[] classes) {
        for (String s : classes) {
            if (className.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private static final boolean inExcluded(String className, String[] excludes) {
        for (String s : excludes) {
            if (className.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
