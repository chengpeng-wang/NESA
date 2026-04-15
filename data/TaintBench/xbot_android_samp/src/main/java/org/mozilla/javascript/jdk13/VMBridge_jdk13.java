package org.mozilla.javascript.jdk13;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.InterfaceAdapter;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.VMBridge;

public class VMBridge_jdk13 extends VMBridge {
    private ThreadLocal<Object[]> contextLocal = new ThreadLocal();

    /* access modifiers changed from: protected */
    public Object getThreadContextHelper() {
        Object[] storage = (Object[]) this.contextLocal.get();
        if (storage != null) {
            return storage;
        }
        storage = new Object[1];
        this.contextLocal.set(storage);
        return storage;
    }

    /* access modifiers changed from: protected */
    public Context getContext(Object contextHelper) {
        return (Context) ((Object[]) contextHelper)[0];
    }

    /* access modifiers changed from: protected */
    public void setContext(Object contextHelper, Context cx) {
        ((Object[]) contextHelper)[0] = cx;
    }

    /* access modifiers changed from: protected */
    public ClassLoader getCurrentThreadClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /* access modifiers changed from: protected */
    public boolean tryToMakeAccessible(Object accessibleObject) {
        if (!(accessibleObject instanceof AccessibleObject)) {
            return false;
        }
        AccessibleObject accessible = (AccessibleObject) accessibleObject;
        if (accessible.isAccessible()) {
            return true;
        }
        try {
            accessible.setAccessible(true);
        } catch (Exception e) {
        }
        return accessible.isAccessible();
    }

    /* access modifiers changed from: protected */
    public Object getInterfaceProxyHelper(ContextFactory cf, Class<?>[] interfaces) {
        try {
            return Proxy.getProxyClass(interfaces[0].getClassLoader(), interfaces).getConstructor(new Class[]{InvocationHandler.class});
        } catch (NoSuchMethodException ex) {
            throw Kit.initCause(new IllegalStateException(), ex);
        }
    }

    /* access modifiers changed from: protected */
    public Object newInterfaceProxy(Object proxyHelper, ContextFactory cf, InterfaceAdapter adapter, Object target, Scriptable topScope) {
        final Object obj = target;
        final InterfaceAdapter interfaceAdapter = adapter;
        final ContextFactory contextFactory = cf;
        final Scriptable scriptable = topScope;
        try {
            return ((Constructor) proxyHelper).newInstance(new Object[]{new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) {
                    boolean z = false;
                    if (method.getDeclaringClass() == Object.class) {
                        String methodName = method.getName();
                        if (methodName.equals("equals")) {
                            if (proxy == args[0]) {
                                z = true;
                            }
                            return Boolean.valueOf(z);
                        } else if (methodName.equals("hashCode")) {
                            return Integer.valueOf(obj.hashCode());
                        } else {
                            if (methodName.equals("toString")) {
                                return "Proxy[" + obj.toString() + "]";
                            }
                        }
                    }
                    return interfaceAdapter.invoke(contextFactory, obj, scriptable, proxy, method, args);
                }
            }});
        } catch (InvocationTargetException ex) {
            throw Context.throwAsScriptRuntimeEx(ex);
        } catch (IllegalAccessException ex2) {
            throw Kit.initCause(new IllegalStateException(), ex2);
        } catch (InstantiationException ex3) {
            throw Kit.initCause(new IllegalStateException(), ex3);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isVarArgs(Member member) {
        return false;
    }
}
