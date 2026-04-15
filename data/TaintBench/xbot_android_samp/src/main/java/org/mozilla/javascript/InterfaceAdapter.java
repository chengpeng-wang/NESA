package org.mozilla.javascript;

import java.lang.reflect.Method;

public class InterfaceAdapter {
    private final Object proxyHelper;

    static Object create(Context cx, Class<?> cl, ScriptableObject object) {
        if (cl.isInterface()) {
            Scriptable topScope = ScriptRuntime.getTopCallScope(cx);
            ClassCache cache = ClassCache.get(topScope);
            InterfaceAdapter adapter = (InterfaceAdapter) cache.getInterfaceAdapter(cl);
            ContextFactory cf = cx.getFactory();
            if (adapter == null) {
                Method[] methods = cl.getMethods();
                if (object instanceof Callable) {
                    int length = methods.length;
                    if (length == 0) {
                        throw Context.reportRuntimeError1("msg.no.empty.interface.conversion", cl.getName());
                    } else if (length > 1) {
                        String methodName = methods[0].getName();
                        int i = 1;
                        while (i < length) {
                            if (methodName.equals(methods[i].getName())) {
                                i++;
                            } else {
                                throw Context.reportRuntimeError1("msg.no.function.interface.conversion", cl.getName());
                            }
                        }
                    }
                }
                adapter = new InterfaceAdapter(cf, cl);
                cache.cacheInterfaceAdapter(cl, adapter);
            }
            return VMBridge.instance.newInterfaceProxy(adapter.proxyHelper, cf, adapter, object, topScope);
        }
        throw new IllegalArgumentException();
    }

    private InterfaceAdapter(ContextFactory cf, Class<?> cl) {
        this.proxyHelper = VMBridge.instance.getInterfaceProxyHelper(cf, new Class[]{cl});
    }

    public Object invoke(ContextFactory cf, Object target, Scriptable topScope, Object thisObject, Method method, Object[] args) {
        final Object obj = target;
        final Scriptable scriptable = topScope;
        final Object obj2 = thisObject;
        final Method method2 = method;
        final Object[] objArr = args;
        return cf.call(new ContextAction() {
            public Object run(Context cx) {
                return InterfaceAdapter.this.invokeImpl(cx, obj, scriptable, obj2, method2, objArr);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public Object invokeImpl(Context cx, Object target, Scriptable topScope, Object thisObject, Method method, Object[] args) {
        Callable function;
        if (target instanceof Callable) {
            function = (Callable) target;
        } else {
            Scriptable s = (Scriptable) target;
            String methodName = method.getName();
            Object value = ScriptableObject.getProperty(s, methodName);
            if (value == ScriptableObject.NOT_FOUND) {
                Context.reportWarning(ScriptRuntime.getMessage1("msg.undefined.function.interface", methodName));
                Class<?> resultType = method.getReturnType();
                if (resultType == Void.TYPE) {
                    return null;
                }
                return Context.jsToJava(null, resultType);
            } else if (value instanceof Callable) {
                function = (Callable) value;
            } else {
                throw Context.reportRuntimeError1("msg.not.function.interface", methodName);
            }
        }
        WrapFactory wf = cx.getWrapFactory();
        if (args == null) {
            args = ScriptRuntime.emptyArgs;
        } else {
            int N = args.length;
            for (int i = 0; i != N; i++) {
                Object arg = args[i];
                if (!((arg instanceof String) || (arg instanceof Number) || (arg instanceof Boolean))) {
                    args[i] = wf.wrap(cx, topScope, arg, null);
                }
            }
        }
        Object result = function.call(cx, topScope, wf.wrapAsJavaObject(cx, topScope, thisObject, null), args);
        Class<?> javaResultType = method.getReturnType();
        if (javaResultType == Void.TYPE) {
            return null;
        }
        return Context.jsToJava(result, javaResultType);
    }
}
