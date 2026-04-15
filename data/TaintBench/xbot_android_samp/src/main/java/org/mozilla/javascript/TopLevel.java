package org.mozilla.javascript;

import java.util.EnumMap;

public class TopLevel extends IdScriptableObject {
    static final /* synthetic */ boolean $assertionsDisabled = (!TopLevel.class.desiredAssertionStatus());
    static final long serialVersionUID = -4648046356662472260L;
    private EnumMap<Builtins, BaseFunction> ctors;
    private EnumMap<NativeErrors, BaseFunction> errors;

    public enum Builtins {
        Object,
        Array,
        Function,
        String,
        Number,
        Boolean,
        RegExp,
        Error
    }

    enum NativeErrors {
        Error,
        EvalError,
        RangeError,
        ReferenceError,
        SyntaxError,
        TypeError,
        URIError,
        InternalError,
        JavaException
    }

    public String getClassName() {
        return "global";
    }

    public void cacheBuiltins() {
        Object value;
        int i = 0;
        this.ctors = new EnumMap(Builtins.class);
        for (Builtins builtin : Builtins.values()) {
            value = ScriptableObject.getProperty((Scriptable) this, builtin.name());
            if (value instanceof BaseFunction) {
                this.ctors.put(builtin, (BaseFunction) value);
            }
        }
        this.errors = new EnumMap(NativeErrors.class);
        NativeErrors[] values = NativeErrors.values();
        int length = values.length;
        while (i < length) {
            NativeErrors error = values[i];
            value = ScriptableObject.getProperty((Scriptable) this, error.name());
            if (value instanceof BaseFunction) {
                this.errors.put(error, (BaseFunction) value);
            }
            i++;
        }
    }

    public static Function getBuiltinCtor(Context cx, Scriptable scope, Builtins type) {
        if ($assertionsDisabled || scope.getParentScope() == null) {
            if (scope instanceof TopLevel) {
                Function result = ((TopLevel) scope).getBuiltinCtor(type);
                if (result != null) {
                    return result;
                }
            }
            return ScriptRuntime.getExistingCtor(cx, scope, type.name());
        }
        throw new AssertionError();
    }

    static Function getNativeErrorCtor(Context cx, Scriptable scope, NativeErrors type) {
        if ($assertionsDisabled || scope.getParentScope() == null) {
            if (scope instanceof TopLevel) {
                Function result = ((TopLevel) scope).getNativeErrorCtor(type);
                if (result != null) {
                    return result;
                }
            }
            return ScriptRuntime.getExistingCtor(cx, scope, type.name());
        }
        throw new AssertionError();
    }

    public static Scriptable getBuiltinPrototype(Scriptable scope, Builtins type) {
        if ($assertionsDisabled || scope.getParentScope() == null) {
            if (scope instanceof TopLevel) {
                Scriptable result = ((TopLevel) scope).getBuiltinPrototype(type);
                if (result != null) {
                    return result;
                }
            }
            return ScriptableObject.getClassPrototype(scope, type.name());
        }
        throw new AssertionError();
    }

    public BaseFunction getBuiltinCtor(Builtins type) {
        return this.ctors != null ? (BaseFunction) this.ctors.get(type) : null;
    }

    /* access modifiers changed from: 0000 */
    public BaseFunction getNativeErrorCtor(NativeErrors type) {
        return this.errors != null ? (BaseFunction) this.errors.get(type) : null;
    }

    public Scriptable getBuiltinPrototype(Builtins type) {
        Object proto;
        BaseFunction func = getBuiltinCtor(type);
        if (func != null) {
            proto = func.getPrototypeProperty();
        } else {
            proto = null;
        }
        if (proto instanceof Scriptable) {
            return (Scriptable) proto;
        }
        return null;
    }
}
