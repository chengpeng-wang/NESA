package org.mozilla.javascript;

class SpecialRef extends Ref {
    private static final int SPECIAL_NONE = 0;
    private static final int SPECIAL_PARENT = 2;
    private static final int SPECIAL_PROTO = 1;
    static final long serialVersionUID = -7521596632456797847L;
    private String name;
    private Scriptable target;
    private int type;

    private SpecialRef(Scriptable target, int type, String name) {
        this.target = target;
        this.type = type;
        this.name = name;
    }

    static Ref createSpecial(Context cx, Scriptable scope, Object object, String name) {
        Scriptable target = ScriptRuntime.toObjectOrNull(cx, object, scope);
        if (target == null) {
            throw ScriptRuntime.undefReadError(object, name);
        }
        int type;
        if (name.equals("__proto__")) {
            type = 1;
        } else if (name.equals("__parent__")) {
            type = 2;
        } else {
            throw new IllegalArgumentException(name);
        }
        if (!cx.hasFeature(5)) {
            type = 0;
        }
        return new SpecialRef(target, type, name);
    }

    public Object get(Context cx) {
        switch (this.type) {
            case 0:
                return ScriptRuntime.getObjectProp(this.target, this.name, cx);
            case 1:
                return this.target.getPrototype();
            case 2:
                return this.target.getParentScope();
            default:
                throw Kit.codeBug();
        }
    }

    @Deprecated
    public Object set(Context cx, Object value) {
        throw new IllegalStateException();
    }

    public Object set(Context cx, Scriptable scope, Object value) {
        switch (this.type) {
            case 0:
                return ScriptRuntime.setObjectProp(this.target, this.name, value, cx);
            case 1:
            case 2:
                Scriptable obj = ScriptRuntime.toObjectOrNull(cx, value, scope);
                if (obj != null) {
                    Scriptable search = obj;
                    while (search != this.target) {
                        if (this.type == 1) {
                            search = search.getPrototype();
                            continue;
                        } else {
                            search = search.getParentScope();
                            continue;
                        }
                        if (search == null) {
                        }
                    }
                    throw Context.reportRuntimeError1("msg.cyclic.value", this.name);
                }
                if (this.type == 1) {
                    this.target.setPrototype(obj);
                    return obj;
                }
                this.target.setParentScope(obj);
                return obj;
            default:
                throw Kit.codeBug();
        }
    }

    public boolean has(Context cx) {
        if (this.type == 0) {
            return ScriptRuntime.hasObjectElem(this.target, this.name, cx);
        }
        return true;
    }

    public boolean delete(Context cx) {
        if (this.type == 0) {
            return ScriptRuntime.deleteObjectElem(this.target, this.name, cx);
        }
        return false;
    }
}
