package org.mozilla.javascript;

public final class NativeCall extends IdScriptableObject {
    private static final Object CALL_TAG = "Call";
    private static final int Id_constructor = 1;
    private static final int MAX_PROTOTYPE_ID = 1;
    static final long serialVersionUID = -7471457301304454454L;
    NativeFunction function;
    Object[] originalArgs;
    transient NativeCall parentActivationCall;

    static void init(Scriptable scope, boolean sealed) {
        new NativeCall().exportAsJSClass(1, scope, sealed);
    }

    NativeCall() {
    }

    NativeCall(NativeFunction function, Scriptable scope, Object[] args) {
        Object[] objArr;
        int i;
        this.function = function;
        setParentScope(scope);
        if (args == null) {
            objArr = ScriptRuntime.emptyArgs;
        } else {
            objArr = args;
        }
        this.originalArgs = objArr;
        int paramAndVarCount = function.getParamAndVarCount();
        int paramCount = function.getParamCount();
        if (paramAndVarCount != 0) {
            i = 0;
            while (i < paramCount) {
                defineProperty(function.getParamOrVarName(i), i < args.length ? args[i] : Undefined.instance, 4);
                i++;
            }
        }
        if (!super.has("arguments", this)) {
            defineProperty("arguments", (Object) new Arguments(this), 4);
        }
        if (paramAndVarCount != 0) {
            for (i = paramCount; i < paramAndVarCount; i++) {
                String name = function.getParamOrVarName(i);
                if (!super.has(name, this)) {
                    if (function.getParamOrVarConst(i)) {
                        defineProperty(name, Undefined.instance, 13);
                    } else {
                        defineProperty(name, Undefined.instance, 4);
                    }
                }
            }
        }
    }

    public String getClassName() {
        return "Call";
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        return s.equals("constructor") ? 1 : 0;
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        if (id == 1) {
            initPrototypeMethod(CALL_TAG, id, "constructor", 1);
            return;
        }
        throw new IllegalArgumentException(String.valueOf(id));
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(CALL_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        if (id != 1) {
            throw new IllegalArgumentException(String.valueOf(id));
        } else if (thisObj != null) {
            throw Context.reportRuntimeError1("msg.only.from.new", "Call");
        } else {
            ScriptRuntime.checkDeprecated(cx, "Call");
            Object result = new NativeCall();
            result.setPrototype(ScriptableObject.getObjectPrototype(scope));
            return result;
        }
    }
}
