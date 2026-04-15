package org.mozilla.javascript;

public final class NativeContinuation extends IdScriptableObject implements Function {
    private static final Object FTAG = "Continuation";
    private static final int Id_constructor = 1;
    private static final int MAX_PROTOTYPE_ID = 1;
    static final long serialVersionUID = 1794167133757605367L;
    private Object implementation;

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        new NativeContinuation().exportAsJSClass(1, scope, sealed);
    }

    public Object getImplementation() {
        return this.implementation;
    }

    public void initImplementation(Object implementation) {
        this.implementation = implementation;
    }

    public String getClassName() {
        return "Continuation";
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        throw Context.reportRuntimeError("Direct call is not supported");
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return Interpreter.restartContinuation(this, cx, scope, args);
    }

    public static boolean isContinuationConstructor(IdFunctionObject f) {
        if (f.hasTag(FTAG) && f.methodId() == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        switch (id) {
            case 1:
                initPrototypeMethod(FTAG, id, "constructor", 0);
                return;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(FTAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        switch (id) {
            case 1:
                throw Context.reportRuntimeError("Direct call is not supported");
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        if (s.length() == 11) {
            X = "constructor";
            id = 1;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
