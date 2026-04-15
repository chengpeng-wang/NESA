package org.mozilla.javascript;

import org.mozilla.javascript.debug.DebuggableScript;

final class InterpretedFunction extends NativeFunction implements Script {
    static final long serialVersionUID = 541475680333911468L;
    InterpreterData idata;
    SecurityController securityController;
    Object securityDomain;

    private InterpretedFunction(InterpreterData idata, Object staticSecurityDomain) {
        Object dynamicDomain;
        this.idata = idata;
        SecurityController sc = Context.getContext().getSecurityController();
        if (sc != null) {
            dynamicDomain = sc.getDynamicSecurityDomain(staticSecurityDomain);
        } else if (staticSecurityDomain != null) {
            throw new IllegalArgumentException();
        } else {
            dynamicDomain = null;
        }
        this.securityController = sc;
        this.securityDomain = dynamicDomain;
    }

    private InterpretedFunction(InterpretedFunction parent, int index) {
        this.idata = parent.idata.itsNestedFunctions[index];
        this.securityController = parent.securityController;
        this.securityDomain = parent.securityDomain;
    }

    static InterpretedFunction createScript(InterpreterData idata, Object staticSecurityDomain) {
        return new InterpretedFunction(idata, staticSecurityDomain);
    }

    static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpreterData idata, Object staticSecurityDomain) {
        InterpretedFunction f = new InterpretedFunction(idata, staticSecurityDomain);
        f.initScriptFunction(cx, scope);
        return f;
    }

    static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index) {
        InterpretedFunction f = new InterpretedFunction(parent, index);
        f.initScriptFunction(cx, scope);
        return f;
    }

    public String getFunctionName() {
        return this.idata.itsName == null ? "" : this.idata.itsName;
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (ScriptRuntime.hasTopCall(cx)) {
            return Interpreter.interpret(this, cx, scope, thisObj, args);
        }
        return ScriptRuntime.doTopCall(this, cx, scope, thisObj, args);
    }

    public Object exec(Context cx, Scriptable scope) {
        if (!isScript()) {
            throw new IllegalStateException();
        } else if (ScriptRuntime.hasTopCall(cx)) {
            return Interpreter.interpret(this, cx, scope, scope, ScriptRuntime.emptyArgs);
        } else {
            return ScriptRuntime.doTopCall(this, cx, scope, scope, ScriptRuntime.emptyArgs);
        }
    }

    public boolean isScript() {
        return this.idata.itsFunctionType == 0;
    }

    public String getEncodedSource() {
        return Interpreter.getEncodedSource(this.idata);
    }

    public DebuggableScript getDebuggableView() {
        return this.idata;
    }

    public Object resumeGenerator(Context cx, Scriptable scope, int operation, Object state, Object value) {
        return Interpreter.resumeGenerator(cx, scope, operation, state, value);
    }

    /* access modifiers changed from: protected */
    public int getLanguageVersion() {
        return this.idata.languageVersion;
    }

    /* access modifiers changed from: protected */
    public int getParamCount() {
        return this.idata.argCount;
    }

    /* access modifiers changed from: protected */
    public int getParamAndVarCount() {
        return this.idata.argNames.length;
    }

    /* access modifiers changed from: protected */
    public String getParamOrVarName(int index) {
        return this.idata.argNames[index];
    }

    /* access modifiers changed from: protected */
    public boolean getParamOrVarConst(int index) {
        return this.idata.argIsConst[index];
    }
}
