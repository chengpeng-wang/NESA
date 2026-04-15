package org.mozilla.javascript;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

public final class LazilyLoadedCtor implements Serializable {
    private static final int STATE_BEFORE_INIT = 0;
    private static final int STATE_INITIALIZING = 1;
    private static final int STATE_WITH_VALUE = 2;
    private static final long serialVersionUID = 1;
    private final String className;
    private Object initializedValue;
    private final boolean privileged;
    private final String propertyName;
    private final ScriptableObject scope;
    private final boolean sealed;
    private int state;

    public LazilyLoadedCtor(ScriptableObject scope, String propertyName, String className, boolean sealed) {
        this(scope, propertyName, className, sealed, false);
    }

    LazilyLoadedCtor(ScriptableObject scope, String propertyName, String className, boolean sealed, boolean privileged) {
        this.scope = scope;
        this.propertyName = propertyName;
        this.className = className;
        this.sealed = sealed;
        this.privileged = privileged;
        this.state = 0;
        scope.addLazilyInitializedValue(propertyName, 0, this, 2);
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        synchronized (this) {
            if (this.state == 1) {
                throw new IllegalStateException("Recursive initialization for " + this.propertyName);
            }
            if (this.state == 0) {
                this.state = 1;
                Object obj = Scriptable.NOT_FOUND;
                try {
                    obj = buildValue();
                } finally {
                    this.initializedValue = obj;
                    this.state = 2;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Object getValue() {
        if (this.state == 2) {
            return this.initializedValue;
        }
        throw new IllegalStateException(this.propertyName);
    }

    private Object buildValue() {
        if (this.privileged) {
            return AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    return LazilyLoadedCtor.this.buildValue0();
                }
            });
        }
        return buildValue0();
    }

    /* access modifiers changed from: private */
    public Object buildValue0() {
        Class<? extends Scriptable> cl = cast(Kit.classOrNull(this.className));
        if (cl != null) {
            try {
                BaseFunction value = ScriptableObject.buildClassCtor(this.scope, cl, this.sealed, false);
                if (value != null) {
                    return value;
                }
                value = this.scope.get(this.propertyName, this.scope);
                if (value != Scriptable.NOT_FOUND) {
                    return value;
                }
            } catch (InvocationTargetException ex) {
                Throwable target = ex.getTargetException();
                if (target instanceof RuntimeException) {
                    throw ((RuntimeException) target);
                }
            } catch (IllegalAccessException | InstantiationException | SecurityException | RhinoException e) {
            }
        }
        return Scriptable.NOT_FOUND;
    }

    private Class<? extends Scriptable> cast(Class<?> cl) {
        return cl;
    }
}
