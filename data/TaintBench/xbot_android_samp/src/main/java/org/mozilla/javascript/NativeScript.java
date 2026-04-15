package org.mozilla.javascript;

class NativeScript extends BaseFunction {
    private static final int Id_compile = 3;
    private static final int Id_constructor = 1;
    private static final int Id_exec = 4;
    private static final int Id_toString = 2;
    private static final int MAX_PROTOTYPE_ID = 4;
    private static final Object SCRIPT_TAG = "Script";
    static final long serialVersionUID = -6795101161980121700L;
    private Script script;

    static void init(Scriptable scope, boolean sealed) {
        new NativeScript(null).exportAsJSClass(4, scope, sealed);
    }

    private NativeScript(Script script) {
        this.script = script;
    }

    public String getClassName() {
        return "Script";
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (this.script != null) {
            return this.script.exec(cx, scope);
        }
        return Undefined.instance;
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        throw Context.reportRuntimeError0("msg.script.is.not.constructor");
    }

    public int getLength() {
        return 0;
    }

    public int getArity() {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public String decompile(int indent, int flags) {
        if (this.script instanceof NativeFunction) {
            return ((NativeFunction) this.script).decompile(indent, flags);
        }
        return super.decompile(indent, flags);
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        int arity;
        String s;
        switch (id) {
            case 1:
                arity = 1;
                s = "constructor";
                break;
            case 2:
                arity = 0;
                s = "toString";
                break;
            case 3:
                arity = 1;
                s = "compile";
                break;
            case 4:
                arity = 0;
                s = "exec";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(SCRIPT_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(SCRIPT_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        switch (id) {
            case 1:
                String source;
                if (args.length == 0) {
                    source = "";
                } else {
                    source = ScriptRuntime.toString(args[0]);
                }
                Object nscript = new NativeScript(compile(cx, source));
                ScriptRuntime.setObjectProtoAndParent(nscript, scope);
                return nscript;
            case 2:
                Script realScript = realThis(thisObj, f).script;
                if (realScript == null) {
                    return "";
                }
                return cx.decompileScript(realScript, 0);
            case 3:
                NativeScript real = realThis(thisObj, f);
                real.script = compile(cx, ScriptRuntime.toString(args, 0));
                return real;
            case 4:
                throw Context.reportRuntimeError1("msg.cant.call.indirect", "exec");
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    private static NativeScript realThis(Scriptable thisObj, IdFunctionObject f) {
        if (thisObj instanceof NativeScript) {
            return (NativeScript) thisObj;
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    private static Script compile(Context cx, String source) {
        int[] linep = new int[]{0};
        String filename = Context.getSourcePositionFromStack(linep);
        if (filename == null) {
            filename = "<Script object>";
            linep[0] = 1;
        }
        return cx.compileString(source, null, DefaultErrorReporter.forEval(cx.getErrorReporter()), filename, linep[0], null);
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        switch (s.length()) {
            case 4:
                X = "exec";
                id = 4;
                break;
            case 7:
                X = "compile";
                id = 3;
                break;
            case 8:
                X = "toString";
                id = 2;
                break;
            case 11:
                X = "constructor";
                id = 1;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
