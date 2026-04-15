package com.splunk.mint.network.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Delegator {
    private final Object delegate;
    private final Object source;
    /* access modifiers changed from: private|final */
    public final Class superclass;

    public class DelegatorMethodFinder {
        private final Method method;

        public DelegatorMethodFinder(String methodName, Class<?>... parameterTypes) {
            try {
                this.method = Delegator.this.superclass.getDeclaredMethod(methodName, parameterTypes);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new DelegationException(e2);
            }
        }

        public <T> T invoke(Object... parameters) {
            return Delegator.this.invoke0(this.method, parameters);
        }
    }

    public Delegator(Object source, Class superclass, Object delegate) {
        this.source = source;
        this.superclass = superclass;
        this.delegate = delegate;
    }

    public Delegator(Object source, Class superclass, String delegateClassName) {
        try {
            this.source = source;
            this.superclass = superclass;
            Constructor delegateConstructor = Class.forName(delegateClassName).getDeclaredConstructor(new Class[0]);
            delegateConstructor.setAccessible(true);
            this.delegate = delegateConstructor.newInstance(new Object[0]);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new DelegationException("Could not make delegate object", e2);
        }
    }

    public final <T> T invoke(Object... args) {
        try {
            return invoke0(findMethod(extractMethodName(), args), args);
        } catch (NoSuchMethodException e) {
            throw new DelegationException(e);
        }
    }

    /* access modifiers changed from: private */
    public Object invoke0(Method method, Object[] args) {
        try {
            writeFields(this.superclass, this.source, this.delegate);
            method.setAccessible(true);
            Object result = method.invoke(this.delegate, args);
            writeFields(this.superclass, this.delegate, this.source);
            return result;
        } catch (RuntimeException e) {
            throw e;
        } catch (InvocationTargetException e2) {
            throw new DelegationException(e2.getCause());
        } catch (Exception e3) {
            throw new DelegationException(e3);
        }
    }

    private void writeFields(Class clazz, Object from, Object to) throws Exception {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            field.set(to, field.get(from));
        }
    }

    private String extractMethodName() {
        return new Throwable().getStackTrace()[2].getMethodName();
    }

    private Method findMethod(String methodName, Object[] args) throws NoSuchMethodException {
        Class<?> clazz = this.superclass;
        if (args.length == 0) {
            return clazz.getDeclaredMethod(methodName, new Class[0]);
        }
        Method match = null;
        for (Method method : ReflectionUtil.getAllMethods(clazz)) {
            if (method.getName().equals(methodName)) {
                Class<?>[] classes = method.getParameterTypes();
                if (classes.length == args.length) {
                    for (int i = 0; i < classes.length; i++) {
                        if (!convertPrimitiveClass(classes[i]).isInstance(args[i])) {
                            break;
                        }
                    }
                    if (match == null) {
                        match = method;
                    } else {
                        throw new DelegationException("Duplicate matches");
                    }
                }
                continue;
            }
        }
        if (match != null) {
            return match;
        }
        throw new DelegationException("Could not find method: " + methodName);
    }

    private Class<?> convertPrimitiveClass(Class<?> primitive) {
        if (!primitive.isPrimitive()) {
            return primitive;
        }
        if (primitive == Integer.TYPE) {
            return Integer.class;
        }
        if (primitive == Boolean.TYPE) {
            return Boolean.class;
        }
        if (primitive == Float.TYPE) {
            return Float.class;
        }
        if (primitive == Long.TYPE) {
            return Long.class;
        }
        if (primitive == Double.TYPE) {
            return Double.class;
        }
        if (primitive == Short.TYPE) {
            return Short.class;
        }
        if (primitive == Byte.TYPE) {
            return Byte.class;
        }
        if (primitive == Character.TYPE) {
            return Character.class;
        }
        return primitive;
    }

    public DelegatorMethodFinder delegateTo(String methodName, Class<?>... parameters) {
        return new DelegatorMethodFinder(methodName, parameters);
    }
}
