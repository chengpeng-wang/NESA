package org.mozilla.javascript;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassCache implements Serializable {
    private static final Object AKEY = "ClassCache";
    private static final long serialVersionUID = -8866246036237312215L;
    private Scriptable associatedScope;
    private volatile boolean cachingIsEnabled = true;
    private transient Map<JavaAdapterSignature, Class<?>> classAdapterCache;
    private transient Map<Class<?>, JavaMembers> classTable;
    private int generatedClassSerial;
    private transient Map<Class<?>, Object> interfaceAdapterCache;

    public static ClassCache get(Scriptable scope) {
        ClassCache cache = (ClassCache) ScriptableObject.getTopScopeValue(scope, AKEY);
        if (cache != null) {
            return cache;
        }
        throw new RuntimeException("Can't find top level scope for ClassCache.get");
    }

    public boolean associate(ScriptableObject topScope) {
        if (topScope.getParentScope() != null) {
            throw new IllegalArgumentException();
        } else if (this != topScope.associateValue(AKEY, this)) {
            return false;
        } else {
            this.associatedScope = topScope;
            return true;
        }
    }

    public synchronized void clearCaches() {
        this.classTable = null;
        this.classAdapterCache = null;
        this.interfaceAdapterCache = null;
    }

    public final boolean isCachingEnabled() {
        return this.cachingIsEnabled;
    }

    public synchronized void setCachingEnabled(boolean enabled) {
        if (enabled != this.cachingIsEnabled) {
            if (!enabled) {
                clearCaches();
            }
            this.cachingIsEnabled = enabled;
        }
    }

    /* access modifiers changed from: 0000 */
    public Map<Class<?>, JavaMembers> getClassCacheMap() {
        if (this.classTable == null) {
            this.classTable = new ConcurrentHashMap(16, 0.75f, 1);
        }
        return this.classTable;
    }

    /* access modifiers changed from: 0000 */
    public Map<JavaAdapterSignature, Class<?>> getInterfaceAdapterCacheMap() {
        if (this.classAdapterCache == null) {
            this.classAdapterCache = new ConcurrentHashMap(16, 0.75f, 1);
        }
        return this.classAdapterCache;
    }

    @Deprecated
    public boolean isInvokerOptimizationEnabled() {
        return false;
    }

    @Deprecated
    public synchronized void setInvokerOptimizationEnabled(boolean enabled) {
    }

    public final synchronized int newClassSerialNumber() {
        int i;
        i = this.generatedClassSerial + 1;
        this.generatedClassSerial = i;
        return i;
    }

    /* access modifiers changed from: 0000 */
    public Object getInterfaceAdapter(Class<?> cl) {
        if (this.interfaceAdapterCache == null) {
            return null;
        }
        return this.interfaceAdapterCache.get(cl);
    }

    /* access modifiers changed from: declared_synchronized */
    public synchronized void cacheInterfaceAdapter(Class<?> cl, Object iadapter) {
        if (this.cachingIsEnabled) {
            if (this.interfaceAdapterCache == null) {
                this.interfaceAdapterCache = new ConcurrentHashMap(16, 0.75f, 1);
            }
            this.interfaceAdapterCache.put(cl, iadapter);
        }
    }

    /* access modifiers changed from: 0000 */
    public Scriptable getAssociatedScope() {
        return this.associatedScope;
    }
}
