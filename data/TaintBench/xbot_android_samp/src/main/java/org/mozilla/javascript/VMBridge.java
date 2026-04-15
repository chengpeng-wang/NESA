package org.mozilla.javascript;

import java.lang.reflect.Member;
import java.util.Iterator;

public abstract class VMBridge {
    static final VMBridge instance = makeInstance();

    public abstract Context getContext(Object obj);

    public abstract ClassLoader getCurrentThreadClassLoader();

    public abstract Object getThreadContextHelper();

    public abstract boolean isVarArgs(Member member);

    public abstract void setContext(Object obj, Context context);

    public abstract boolean tryToMakeAccessible(Object obj);

    private static VMBridge makeInstance() {
        String[] classNames = new String[]{"org.mozilla.javascript.VMBridge_custom", "org.mozilla.javascript.jdk15.VMBridge_jdk15", "org.mozilla.javascript.jdk13.VMBridge_jdk13", "org.mozilla.javascript.jdk11.VMBridge_jdk11"};
        for (int i = 0; i != classNames.length; i++) {
            Class<?> cl = Kit.classOrNull(classNames[i]);
            if (cl != null) {
                VMBridge bridge = (VMBridge) Kit.newInstanceOrNull(cl);
                if (bridge != null) {
                    return bridge;
                }
            }
        }
        throw new IllegalStateException("Failed to create VMBridge instance");
    }

    /* access modifiers changed from: protected */
    public Object getInterfaceProxyHelper(ContextFactory cf, Class<?>[] clsArr) {
        throw Context.reportRuntimeError("VMBridge.getInterfaceProxyHelper is not supported");
    }

    /* access modifiers changed from: protected */
    public Object newInterfaceProxy(Object proxyHelper, ContextFactory cf, InterfaceAdapter adapter, Object target, Scriptable topScope) {
        throw Context.reportRuntimeError("VMBridge.newInterfaceProxy is not supported");
    }

    public Iterator<?> getJavaIterator(Context cx, Scriptable scope, Object obj) {
        if (!(obj instanceof Wrapper)) {
            return null;
        }
        Iterator<?> unwrapped = ((Wrapper) obj).unwrap();
        if (unwrapped instanceof Iterator) {
            return unwrapped;
        }
        return null;
    }
}
