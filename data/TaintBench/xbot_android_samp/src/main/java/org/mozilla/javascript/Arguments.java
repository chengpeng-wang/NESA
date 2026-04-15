package org.mozilla.javascript;

final class Arguments extends IdScriptableObject {
    private static final String FTAG = "Arguments";
    private static final int Id_callee = 1;
    private static final int Id_caller = 3;
    private static final int Id_length = 2;
    private static final int MAX_INSTANCE_ID = 3;
    static final long serialVersionUID = 4275508002492040609L;
    private NativeCall activation;
    private Object[] args;
    private int calleeAttr = 2;
    private Object calleeObj;
    private int callerAttr = 2;
    private Object callerObj;
    private int lengthAttr = 2;
    private Object lengthObj;

    public Arguments(NativeCall activation) {
        this.activation = activation;
        Scriptable parent = activation.getParentScope();
        setParentScope(parent);
        setPrototype(ScriptableObject.getObjectPrototype(parent));
        this.args = activation.originalArgs;
        this.lengthObj = Integer.valueOf(this.args.length);
        NativeFunction f = activation.function;
        this.calleeObj = f;
        int version = f.getLanguageVersion();
        if (version > 130 || version == 0) {
            this.callerObj = NOT_FOUND;
        } else {
            this.callerObj = null;
        }
    }

    public String getClassName() {
        return FTAG;
    }

    private Object arg(int index) {
        if (index < 0 || this.args.length <= index) {
            return NOT_FOUND;
        }
        return this.args[index];
    }

    private void putIntoActivation(int index, Object value) {
        this.activation.put(this.activation.function.getParamOrVarName(index), this.activation, value);
    }

    private Object getFromActivation(int index) {
        return this.activation.get(this.activation.function.getParamOrVarName(index), this.activation);
    }

    private void replaceArg(int index, Object value) {
        if (sharedWithActivation(index)) {
            putIntoActivation(index, value);
        }
        synchronized (this) {
            if (this.args == this.activation.originalArgs) {
                this.args = (Object[]) this.args.clone();
            }
            this.args[index] = value;
        }
    }

    private void removeArg(int index) {
        synchronized (this) {
            if (this.args[index] != NOT_FOUND) {
                if (this.args == this.activation.originalArgs) {
                    this.args = (Object[]) this.args.clone();
                }
                this.args[index] = NOT_FOUND;
            }
        }
    }

    public boolean has(int index, Scriptable start) {
        if (arg(index) != NOT_FOUND) {
            return true;
        }
        return super.has(index, start);
    }

    public Object get(int index, Scriptable start) {
        Object value = arg(index);
        if (value == NOT_FOUND) {
            return super.get(index, start);
        }
        if (sharedWithActivation(index)) {
            return getFromActivation(index);
        }
        return value;
    }

    private boolean sharedWithActivation(int index) {
        NativeFunction f = this.activation.function;
        int definedCount = f.getParamCount();
        if (index >= definedCount) {
            return false;
        }
        if (index < definedCount - 1) {
            String argName = f.getParamOrVarName(index);
            for (int i = index + 1; i < definedCount; i++) {
                if (argName.equals(f.getParamOrVarName(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void put(int index, Scriptable start, Object value) {
        if (arg(index) == NOT_FOUND) {
            super.put(index, start, value);
        } else {
            replaceArg(index, value);
        }
    }

    public void delete(int index) {
        if (index >= 0 && index < this.args.length) {
            removeArg(index);
        }
        super.delete(index);
    }

    /* access modifiers changed from: protected */
    public int getMaxInstanceId() {
        return 3;
    }

    /* access modifiers changed from: protected */
    public int findInstanceIdInfo(String s) {
        int id = 0;
        String X = null;
        if (s.length() == 6) {
            int c = s.charAt(5);
            if (c == 101) {
                X = "callee";
                id = 1;
            } else if (c == 104) {
                X = "length";
                id = 2;
            } else if (c == 114) {
                X = "caller";
                id = 3;
            }
        }
        if (!(X == null || X == s || X.equals(s))) {
            id = 0;
        }
        if (id == 0) {
            return super.findInstanceIdInfo(s);
        }
        int attr;
        switch (id) {
            case 1:
                attr = this.calleeAttr;
                break;
            case 2:
                attr = this.lengthAttr;
                break;
            case 3:
                attr = this.callerAttr;
                break;
            default:
                throw new IllegalStateException();
        }
        return IdScriptableObject.instanceIdInfo(attr, id);
    }

    /* access modifiers changed from: protected */
    public String getInstanceIdName(int id) {
        switch (id) {
            case 1:
                return "callee";
            case 2:
                return "length";
            case 3:
                return "caller";
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public Object getInstanceIdValue(int id) {
        switch (id) {
            case 1:
                return this.calleeObj;
            case 2:
                return this.lengthObj;
            case 3:
                UniqueTag value = this.callerObj;
                if (value == UniqueTag.NULL_VALUE) {
                    return null;
                }
                if (value != null) {
                    return value;
                }
                NativeCall caller = this.activation.parentActivationCall;
                if (caller != null) {
                    return caller.get("arguments", caller);
                }
                return value;
            default:
                return super.getInstanceIdValue(id);
        }
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdValue(int id, Object value) {
        switch (id) {
            case 1:
                this.calleeObj = value;
                return;
            case 2:
                this.lengthObj = value;
                return;
            case 3:
                if (value == null) {
                    value = UniqueTag.NULL_VALUE;
                }
                this.callerObj = value;
                return;
            default:
                super.setInstanceIdValue(id, value);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdAttributes(int id, int attr) {
        switch (id) {
            case 1:
                this.calleeAttr = attr;
                return;
            case 2:
                this.lengthAttr = attr;
                return;
            case 3:
                this.callerAttr = attr;
                return;
            default:
                super.setInstanceIdAttributes(id, attr);
                return;
        }
    }

    /* access modifiers changed from: 0000 */
    public Object[] getIds(boolean getAll) {
        Object[] ids = super.getIds(getAll);
        if (this.args.length != 0) {
            int i;
            boolean[] present = new boolean[this.args.length];
            int extraCount = this.args.length;
            for (i = 0; i != ids.length; i++) {
                Object id = ids[i];
                if (id instanceof Integer) {
                    int index = ((Integer) id).intValue();
                    if (index >= 0 && index < this.args.length && !present[index]) {
                        present[index] = true;
                        extraCount--;
                    }
                }
            }
            if (!getAll) {
                i = 0;
                while (i < present.length) {
                    if (!present[i] && super.has(i, (Scriptable) this)) {
                        present[i] = true;
                        extraCount--;
                    }
                    i++;
                }
            }
            if (extraCount != 0) {
                Object[] tmp = new Object[(ids.length + extraCount)];
                System.arraycopy(ids, 0, tmp, extraCount, ids.length);
                ids = tmp;
                int offset = 0;
                i = 0;
                while (i != this.args.length) {
                    if (present == null || !present[i]) {
                        ids[offset] = Integer.valueOf(i);
                        offset++;
                    }
                    i++;
                }
                if (offset != extraCount) {
                    Kit.codeBug();
                }
            }
        }
        return ids;
    }

    /* access modifiers changed from: protected */
    public ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
        double d = ScriptRuntime.toNumber(id);
        int index = (int) d;
        if (d != ((double) index)) {
            return super.getOwnPropertyDescriptor(cx, id);
        }
        Object value = arg(index);
        if (value == NOT_FOUND) {
            return super.getOwnPropertyDescriptor(cx, id);
        }
        if (sharedWithActivation(index)) {
            value = getFromActivation(index);
        }
        if (super.has(index, (Scriptable) this)) {
            Scriptable desc = super.getOwnPropertyDescriptor(cx, id);
            desc.put("value", desc, value);
            return desc;
        }
        Scriptable scope = getParentScope();
        if (scope == null) {
            scope = this;
        }
        return ScriptableObject.buildDataDescriptor(scope, value, 0);
    }

    /* access modifiers changed from: protected */
    public void defineOwnProperty(Context cx, Object id, ScriptableObject desc, boolean checkValid) {
        super.defineOwnProperty(cx, id, desc, checkValid);
        double d = ScriptRuntime.toNumber(id);
        int index = (int) d;
        if (d != ((double) index) || arg(index) == NOT_FOUND) {
            return;
        }
        if (isAccessorDescriptor(desc)) {
            removeArg(index);
            return;
        }
        Object newValue = ScriptableObject.getProperty((Scriptable) desc, "value");
        if (newValue != NOT_FOUND) {
            replaceArg(index, newValue);
            if (ScriptableObject.isFalse(ScriptableObject.getProperty((Scriptable) desc, "writable"))) {
                removeArg(index);
            }
        }
    }
}
