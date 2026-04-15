package org.springframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class ReflectionUtils {
    public static FieldFilter COPYABLE_FIELDS = new FieldFilter() {
        public boolean matches(Field field) {
            return (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) ? false : true;
        }
    };
    public static MethodFilter NON_BRIDGED_METHODS = new MethodFilter() {
        public boolean matches(Method method) {
            return !method.isBridge();
        }
    };
    public static MethodFilter USER_DECLARED_METHODS = new MethodFilter() {
        public boolean matches(Method method) {
            return (method.isBridge() || method.getDeclaringClass() == Object.class) ? false : true;
        }
    };
    private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap(256);

    public interface MethodCallback {
        void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
    }

    public interface FieldCallback {
        void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    public interface FieldFilter {
        boolean matches(Field field);
    }

    public interface MethodFilter {
        boolean matches(Method method);
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "Class must not be null");
        boolean z = (name == null && type == null) ? false : true;
        Assert.isTrue(z, "Either name or type of the field must be specified");
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            for (Field field : searchType.getDeclaredFields()) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, new Class[0]);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            for (Method method : searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType)) {
                if (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Should never get here");
        }
    }

    public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
        return invokeJdbcMethod(method, target, new Object[0]);
    }

    public static Object invokeJdbcMethod(Method method, Object target, Object... args) throws SQLException {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
        } catch (InvocationTargetException ex2) {
            if (ex2.getTargetException() instanceof SQLException) {
                throw ((SQLException) ex2.getTargetException());
            }
            handleInvocationTargetException(ex2);
        }
        throw new IllegalStateException("Should never get here");
    }

    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        } else if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
        } else {
            if (ex instanceof InvocationTargetException) {
                handleInvocationTargetException((InvocationTargetException) ex);
            }
            if (ex instanceof RuntimeException) {
                throw ((RuntimeException) ex);
            }
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static void handleInvocationTargetException(InvocationTargetException ex) {
        rethrowRuntimeException(ex.getTargetException());
    }

    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw ((RuntimeException) ex);
        } else if (ex instanceof Error) {
            throw ((Error) ex);
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static void rethrowException(Throwable ex) throws Exception {
        if (ex instanceof Exception) {
            throw ((Exception) ex);
        } else if (ex instanceof Error) {
            throw ((Error) ex);
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static boolean declaresException(Method method, Class<?> exceptionType) {
        Assert.notNull(method, "Method must not be null");
        for (Class<?> declaredException : method.getExceptionTypes()) {
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPublicStaticFinal(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    public static boolean isEqualsMethod(Method method) {
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 1 && paramTypes[0] == Object.class) {
            return true;
        }
        return false;
    }

    public static boolean isHashCodeMethod(Method method) {
        return method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0;
    }

    public static boolean isToStringMethod(Method method) {
        return method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0;
    }

    public static boolean isObjectMethod(Method method) {
        if (method == null) {
            return false;
        }
        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static void doWithMethods(Class<?> clazz, MethodCallback mc) throws IllegalArgumentException {
        doWithMethods(clazz, mc, null);
    }

    public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf) throws IllegalArgumentException {
        for (Method method : getDeclaredMethods(clazz)) {
            if (mf == null || mf.matches(method)) {
                try {
                    mc.doWith(method);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Shouldn't be illegal to access method '" + method.getName() + "': " + ex);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            doWithMethods(clazz.getSuperclass(), mc, mf);
        } else if (clazz.isInterface()) {
            for (Class<?> superIfc : clazz.getInterfaces()) {
                doWithMethods(superIfc, mc, mf);
            }
        }
    }

    public static Method[] getAllDeclaredMethods(Class<?> leafClass) throws IllegalArgumentException {
        final List<Method> methods = new ArrayList(32);
        doWithMethods(leafClass, new MethodCallback() {
            public void doWith(Method method) {
                methods.add(method);
            }
        });
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) throws IllegalArgumentException {
        final List<Method> methods = new ArrayList(32);
        doWithMethods(leafClass, new MethodCallback() {
            /* JADX WARNING: Removed duplicated region for block: B:13:0x004b  */
            /* JADX WARNING: Removed duplicated region for block: B:23:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:15:0x0052  */
            public void doWith(java.lang.reflect.Method r7) {
                /*
                r6 = this;
                r2 = 0;
                r3 = 0;
                r4 = r0;
                r1 = r4.iterator();
            L_0x0008:
                r4 = r1.hasNext();
                if (r4 == 0) goto L_0x0049;
            L_0x000e:
                r0 = r1.next();
                r0 = (java.lang.reflect.Method) r0;
                r4 = r7.getName();
                r5 = r0.getName();
                r4 = r4.equals(r5);
                if (r4 == 0) goto L_0x0008;
            L_0x0022:
                r4 = r7.getParameterTypes();
                r5 = r0.getParameterTypes();
                r4 = java.util.Arrays.equals(r4, r5);
                if (r4 == 0) goto L_0x0008;
            L_0x0030:
                r4 = r0.getReturnType();
                r5 = r7.getReturnType();
                if (r4 == r5) goto L_0x0058;
            L_0x003a:
                r4 = r0.getReturnType();
                r5 = r7.getReturnType();
                r4 = r4.isAssignableFrom(r5);
                if (r4 == 0) goto L_0x0058;
            L_0x0048:
                r3 = r0;
            L_0x0049:
                if (r3 == 0) goto L_0x0050;
            L_0x004b:
                r4 = r0;
                r4.remove(r3);
            L_0x0050:
                if (r2 != 0) goto L_0x0057;
            L_0x0052:
                r4 = r0;
                r4.add(r7);
            L_0x0057:
                return;
            L_0x0058:
                r2 = 1;
                goto L_0x0049;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.springframework.util.ReflectionUtils$AnonymousClass2.doWith(java.lang.reflect.Method):void");
            }
        });
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    private static Method[] getDeclaredMethods(Class<?> clazz) {
        Method[] result = (Method[]) declaredMethodsCache.get(clazz);
        if (result != null) {
            return result;
        }
        result = clazz.getDeclaredMethods();
        declaredMethodsCache.put(clazz, result);
        return result;
    }

    public static void doWithFields(Class<?> clazz, FieldCallback fc) throws IllegalArgumentException {
        doWithFields(clazz, fc, null);
    }

    public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff) throws IllegalArgumentException {
        Class<?> targetClass = clazz;
        do {
            for (Field field : targetClass.getDeclaredFields()) {
                if (ff == null || ff.matches(field)) {
                    try {
                        fc.doWith(field);
                    } catch (IllegalAccessException ex) {
                        throw new IllegalStateException("Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
                    }
                }
            }
            targetClass = targetClass.getSuperclass();
            if (targetClass == null) {
                return;
            }
        } while (targetClass != Object.class);
    }

    public static void shallowCopyFieldState(final Object src, final Object dest) throws IllegalArgumentException {
        if (src == null) {
            throw new IllegalArgumentException("Source for field copy cannot be null");
        } else if (dest == null) {
            throw new IllegalArgumentException("Destination for field copy cannot be null");
        } else if (src.getClass().isAssignableFrom(dest.getClass())) {
            doWithFields(src.getClass(), new FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    field.set(dest, field.get(src));
                }
            }, COPYABLE_FIELDS);
        } else {
            throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
        }
    }
}
