package org.mozilla.javascript;

public class IdFunctionObject extends BaseFunction {
    static final long serialVersionUID = -5332312783643935019L;
    private int arity;
    private String functionName;
    private final IdFunctionCall idcall;
    private final int methodId;
    private final Object tag;
    private boolean useCallAsConstructor;

    public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, int arity) {
        if (arity < 0) {
            throw new IllegalArgumentException();
        }
        this.idcall = idcall;
        this.tag = tag;
        this.methodId = id;
        this.arity = arity;
        if (arity < 0) {
            throw new IllegalArgumentException();
        }
    }

    public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, String name, int arity, Scriptable scope) {
        super(scope, null);
        if (arity < 0) {
            throw new IllegalArgumentException();
        } else if (name == null) {
            throw new IllegalArgumentException();
        } else {
            this.idcall = idcall;
            this.tag = tag;
            this.methodId = id;
            this.arity = arity;
            this.functionName = name;
        }
    }

    public void initFunction(String name, Scriptable scope) {
        if (name == null) {
            throw new IllegalArgumentException();
        } else if (scope == null) {
            throw new IllegalArgumentException();
        } else {
            this.functionName = name;
            setParentScope(scope);
        }
    }

    public final boolean hasTag(Object tag) {
        if (tag == null) {
            return this.tag == null;
        } else {
            return tag.equals(this.tag);
        }
    }

    public final int methodId() {
        return this.methodId;
    }

    public final void markAsConstructor(Scriptable prototypeProperty) {
        this.useCallAsConstructor = true;
        setImmunePrototypeProperty(prototypeProperty);
    }

    public final void addAsProperty(Scriptable target) {
        ScriptableObject.defineProperty(target, this.functionName, this, 2);
    }

    public void exportAsScopeProperty() {
        addAsProperty(getParentScope());
    }

    public Scriptable getPrototype() {
        Scriptable proto = super.getPrototype();
        if (proto != null) {
            return proto;
        }
        proto = ScriptableObject.getFunctionPrototype(getParentScope());
        setPrototype(proto);
        return proto;
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return this.idcall.execIdCall(this, cx, scope, thisObj, args);
    }

    public Scriptable createObject(Context cx, Scriptable scope) {
        if (this.useCallAsConstructor) {
            return null;
        }
        throw ScriptRuntime.typeError1("msg.not.ctor", this.functionName);
    }

    /* access modifiers changed from: 0000 */
    public String decompile(int indent, int flags) {
        StringBuilder sb = new StringBuilder();
        boolean justbody = (flags & 1) != 0;
        if (!justbody) {
            sb.append("function ");
            sb.append(getFunctionName());
            sb.append("() { ");
        }
        sb.append("[native code for ");
        if (this.idcall instanceof Scriptable) {
            sb.append(this.idcall.getClassName());
            sb.append('.');
        }
        sb.append(getFunctionName());
        sb.append(", arity=");
        sb.append(getArity());
        sb.append(justbody ? "]\n" : "] }\n");
        return sb.toString();
    }

    public int getArity() {
        return this.arity;
    }

    public int getLength() {
        return getArity();
    }

    public String getFunctionName() {
        return this.functionName == null ? "" : this.functionName;
    }

    public final RuntimeException unknown() {
        return new IllegalArgumentException("BAD FUNCTION ID=" + this.methodId + " MASTER=" + this.idcall);
    }
}
