package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

class XMLCtor extends IdFunctionObject {
    private static final int Id_defaultSettings = 1;
    private static final int Id_ignoreComments = 1;
    private static final int Id_ignoreProcessingInstructions = 2;
    private static final int Id_ignoreWhitespace = 3;
    private static final int Id_prettyIndent = 4;
    private static final int Id_prettyPrinting = 5;
    private static final int Id_setSettings = 3;
    private static final int Id_settings = 2;
    private static final int MAX_FUNCTION_ID = 3;
    private static final int MAX_INSTANCE_ID = 5;
    private static final Object XMLCTOR_TAG = "XMLCtor";
    static final long serialVersionUID = -8708195078359817341L;
    private XmlProcessor options;

    XMLCtor(XML xml, Object tag, int id, int arity) {
        super(xml, tag, id, arity);
        this.options = xml.getProcessor();
        activatePrototypeMap(3);
    }

    private void writeSetting(Scriptable target) {
        for (int i = 1; i <= 5; i++) {
            int id = super.getMaxInstanceId() + i;
            ScriptableObject.putProperty(target, getInstanceIdName(id), getInstanceIdValue(id));
        }
    }

    /* JADX WARNING: Missing block: B:11:0x0026, code skipped:
            setInstanceIdValue(r1, r3);
     */
    private void readSettings(org.mozilla.javascript.Scriptable r6) {
        /*
        r5 = this;
        r0 = 1;
    L_0x0001:
        r4 = 5;
        if (r0 > r4) goto L_0x002f;
    L_0x0004:
        r4 = super.getMaxInstanceId();
        r1 = r4 + r0;
        r2 = r5.getInstanceIdName(r1);
        r3 = org.mozilla.javascript.ScriptableObject.getProperty(r6, r2);
        r4 = org.mozilla.javascript.Scriptable.NOT_FOUND;
        if (r3 != r4) goto L_0x0019;
    L_0x0016:
        r0 = r0 + 1;
        goto L_0x0001;
    L_0x0019:
        switch(r0) {
            case 1: goto L_0x0022;
            case 2: goto L_0x0022;
            case 3: goto L_0x0022;
            case 4: goto L_0x002a;
            case 5: goto L_0x0022;
            default: goto L_0x001c;
        };
    L_0x001c:
        r4 = new java.lang.IllegalStateException;
        r4.<init>();
        throw r4;
    L_0x0022:
        r4 = r3 instanceof java.lang.Boolean;
        if (r4 == 0) goto L_0x0016;
    L_0x0026:
        r5.setInstanceIdValue(r1, r3);
        goto L_0x0016;
    L_0x002a:
        r4 = r3 instanceof java.lang.Number;
        if (r4 != 0) goto L_0x0026;
    L_0x002e:
        goto L_0x0016;
    L_0x002f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.xmlimpl.XMLCtor.readSettings(org.mozilla.javascript.Scriptable):void");
    }

    /* access modifiers changed from: protected */
    public int getMaxInstanceId() {
        return super.getMaxInstanceId() + 5;
    }

    /* access modifiers changed from: protected */
    public int findInstanceIdInfo(String s) {
        int id = 0;
        String X = null;
        switch (s.length()) {
            case 12:
                X = "prettyIndent";
                id = 4;
                break;
            case 14:
                int c = s.charAt(0);
                if (c != 105) {
                    if (c == 112) {
                        X = "prettyPrinting";
                        id = 5;
                        break;
                    }
                }
                X = "ignoreComments";
                id = 1;
                break;
                break;
            case 16:
                X = "ignoreWhitespace";
                id = 3;
                break;
            case 28:
                X = "ignoreProcessingInstructions";
                id = 2;
                break;
        }
        if (!(X == null || X == s || X.equals(s))) {
            id = 0;
        }
        if (id == 0) {
            return super.findInstanceIdInfo(s);
        }
        switch (id) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return IdScriptableObject.instanceIdInfo(6, super.getMaxInstanceId() + id);
            default:
                throw new IllegalStateException();
        }
    }

    /* access modifiers changed from: protected */
    public String getInstanceIdName(int id) {
        switch (id - super.getMaxInstanceId()) {
            case 1:
                return "ignoreComments";
            case 2:
                return "ignoreProcessingInstructions";
            case 3:
                return "ignoreWhitespace";
            case 4:
                return "prettyIndent";
            case 5:
                return "prettyPrinting";
            default:
                return super.getInstanceIdName(id);
        }
    }

    /* access modifiers changed from: protected */
    public Object getInstanceIdValue(int id) {
        switch (id - super.getMaxInstanceId()) {
            case 1:
                return ScriptRuntime.wrapBoolean(this.options.isIgnoreComments());
            case 2:
                return ScriptRuntime.wrapBoolean(this.options.isIgnoreProcessingInstructions());
            case 3:
                return ScriptRuntime.wrapBoolean(this.options.isIgnoreWhitespace());
            case 4:
                return ScriptRuntime.wrapInt(this.options.getPrettyIndent());
            case 5:
                return ScriptRuntime.wrapBoolean(this.options.isPrettyPrinting());
            default:
                return super.getInstanceIdValue(id);
        }
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdValue(int id, Object value) {
        switch (id - super.getMaxInstanceId()) {
            case 1:
                this.options.setIgnoreComments(ScriptRuntime.toBoolean(value));
                return;
            case 2:
                this.options.setIgnoreProcessingInstructions(ScriptRuntime.toBoolean(value));
                return;
            case 3:
                this.options.setIgnoreWhitespace(ScriptRuntime.toBoolean(value));
                return;
            case 4:
                this.options.setPrettyIndent(ScriptRuntime.toInt32(value));
                return;
            case 5:
                this.options.setPrettyPrinting(ScriptRuntime.toBoolean(value));
                return;
            default:
                super.setInstanceIdValue(id, value);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int s_length = s.length();
        if (s_length == 8) {
            X = "settings";
            id = 2;
        } else if (s_length == 11) {
            X = "setSettings";
            id = 3;
        } else if (s_length == 15) {
            X = "defaultSettings";
            id = 1;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        int arity;
        String s;
        switch (id) {
            case 1:
                arity = 0;
                s = "defaultSettings";
                break;
            case 2:
                arity = 0;
                s = "settings";
                break;
            case 3:
                arity = 1;
                s = "setSettings";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(XMLCTOR_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(XMLCTOR_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        Object obj;
        switch (id) {
            case 1:
                this.options.setDefault();
                obj = cx.newObject(scope);
                writeSetting(obj);
                return obj;
            case 2:
                obj = cx.newObject(scope);
                writeSetting(obj);
                return obj;
            case 3:
                if (args.length == 0 || args[0] == null || args[0] == Undefined.instance) {
                    this.options.setDefault();
                } else if (args[0] instanceof Scriptable) {
                    readSettings((Scriptable) args[0]);
                }
                return Undefined.instance;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    public boolean hasInstance(Scriptable instance) {
        return (instance instanceof XML) || (instance instanceof XMLList);
    }
}
