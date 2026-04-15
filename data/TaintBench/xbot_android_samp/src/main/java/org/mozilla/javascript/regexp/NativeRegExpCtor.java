package org.mozilla.javascript.regexp;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.TopLevel.Builtins;
import org.mozilla.javascript.Undefined;

class NativeRegExpCtor extends BaseFunction {
    private static final int DOLLAR_ID_BASE = 12;
    private static final int Id_AMPERSAND = 6;
    private static final int Id_BACK_QUOTE = 10;
    private static final int Id_DOLLAR_1 = 13;
    private static final int Id_DOLLAR_2 = 14;
    private static final int Id_DOLLAR_3 = 15;
    private static final int Id_DOLLAR_4 = 16;
    private static final int Id_DOLLAR_5 = 17;
    private static final int Id_DOLLAR_6 = 18;
    private static final int Id_DOLLAR_7 = 19;
    private static final int Id_DOLLAR_8 = 20;
    private static final int Id_DOLLAR_9 = 21;
    private static final int Id_PLUS = 8;
    private static final int Id_QUOTE = 12;
    private static final int Id_STAR = 2;
    private static final int Id_UNDERSCORE = 4;
    private static final int Id_input = 3;
    private static final int Id_lastMatch = 5;
    private static final int Id_lastParen = 7;
    private static final int Id_leftContext = 9;
    private static final int Id_multiline = 1;
    private static final int Id_rightContext = 11;
    private static final int MAX_INSTANCE_ID = 21;
    static final long serialVersionUID = -5733330028285400526L;
    private int inputAttr = 4;
    private int multilineAttr = 4;
    private int starAttr = 4;
    private int underscoreAttr = 4;

    NativeRegExpCtor() {
    }

    public String getFunctionName() {
        return "RegExp";
    }

    public int getLength() {
        return 2;
    }

    public int getArity() {
        return 2;
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length > 0 && (args[0] instanceof NativeRegExp) && (args.length == 1 || args[1] == Undefined.instance)) {
            return args[0];
        }
        return construct(cx, scope, args);
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        NativeRegExp re = new NativeRegExp();
        re.compile(cx, scope, args);
        ScriptRuntime.setBuiltinProtoAndParent(re, scope, Builtins.RegExp);
        return re;
    }

    private static RegExpImpl getImpl() {
        return (RegExpImpl) ScriptRuntime.getRegExpProxy(Context.getCurrentContext());
    }

    /* access modifiers changed from: protected */
    public int getMaxInstanceId() {
        return super.getMaxInstanceId() + 21;
    }

    /* access modifiers changed from: protected */
    public int findInstanceIdInfo(java.lang.String r8) {
        /*
        r7 = this;
        r6 = 36;
        r5 = 0;
        r3 = 0;
        r0 = 0;
        r4 = r8.length();
        switch(r4) {
            case 2: goto L_0x001e;
            case 3: goto L_0x000c;
            case 4: goto L_0x000c;
            case 5: goto L_0x00ae;
            case 6: goto L_0x000c;
            case 7: goto L_0x000c;
            case 8: goto L_0x000c;
            case 9: goto L_0x00b3;
            case 10: goto L_0x000c;
            case 11: goto L_0x00d3;
            case 12: goto L_0x00d9;
            default: goto L_0x000c;
        };
    L_0x000c:
        if (r0 == 0) goto L_0x0017;
    L_0x000e:
        if (r0 == r8) goto L_0x0017;
    L_0x0010:
        r4 = r0.equals(r8);
        if (r4 != 0) goto L_0x0017;
    L_0x0016:
        r3 = 0;
    L_0x0017:
        if (r3 != 0) goto L_0x00df;
    L_0x0019:
        r4 = super.findInstanceIdInfo(r8);
    L_0x001d:
        return r4;
    L_0x001e:
        r4 = 1;
        r4 = r8.charAt(r4);
        switch(r4) {
            case 38: goto L_0x0027;
            case 39: goto L_0x002f;
            case 42: goto L_0x0038;
            case 43: goto L_0x0040;
            case 49: goto L_0x0049;
            case 50: goto L_0x0052;
            case 51: goto L_0x005b;
            case 52: goto L_0x0064;
            case 53: goto L_0x006d;
            case 54: goto L_0x0076;
            case 55: goto L_0x007f;
            case 56: goto L_0x0088;
            case 57: goto L_0x0091;
            case 95: goto L_0x009b;
            case 96: goto L_0x00a4;
            default: goto L_0x0026;
        };
    L_0x0026:
        goto L_0x000c;
    L_0x0027:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x002d:
        r3 = 6;
        goto L_0x0017;
    L_0x002f:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x0035:
        r3 = 12;
        goto L_0x0017;
    L_0x0038:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x003e:
        r3 = 2;
        goto L_0x0017;
    L_0x0040:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x0046:
        r3 = 8;
        goto L_0x0017;
    L_0x0049:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x004f:
        r3 = 13;
        goto L_0x0017;
    L_0x0052:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x0058:
        r3 = 14;
        goto L_0x0017;
    L_0x005b:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x0061:
        r3 = 15;
        goto L_0x0017;
    L_0x0064:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x006a:
        r3 = 16;
        goto L_0x0017;
    L_0x006d:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x0073:
        r3 = 17;
        goto L_0x0017;
    L_0x0076:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x007c:
        r3 = 18;
        goto L_0x0017;
    L_0x007f:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x0085:
        r3 = 19;
        goto L_0x0017;
    L_0x0088:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x008e:
        r3 = 20;
        goto L_0x0017;
    L_0x0091:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x0097:
        r3 = 21;
        goto L_0x0017;
    L_0x009b:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x00a1:
        r3 = 4;
        goto L_0x0017;
    L_0x00a4:
        r4 = r8.charAt(r5);
        if (r4 != r6) goto L_0x000c;
    L_0x00aa:
        r3 = 10;
        goto L_0x0017;
    L_0x00ae:
        r0 = "input";
        r3 = 3;
        goto L_0x000c;
    L_0x00b3:
        r4 = 4;
        r2 = r8.charAt(r4);
        r4 = 77;
        if (r2 != r4) goto L_0x00c1;
    L_0x00bc:
        r0 = "lastMatch";
        r3 = 5;
        goto L_0x000c;
    L_0x00c1:
        r4 = 80;
        if (r2 != r4) goto L_0x00ca;
    L_0x00c5:
        r0 = "lastParen";
        r3 = 7;
        goto L_0x000c;
    L_0x00ca:
        r4 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        if (r2 != r4) goto L_0x000c;
    L_0x00ce:
        r0 = "multiline";
        r3 = 1;
        goto L_0x000c;
    L_0x00d3:
        r0 = "leftContext";
        r3 = 9;
        goto L_0x000c;
    L_0x00d9:
        r0 = "rightContext";
        r3 = 11;
        goto L_0x000c;
    L_0x00df:
        switch(r3) {
            case 1: goto L_0x00ee;
            case 2: goto L_0x00f1;
            case 3: goto L_0x00f4;
            case 4: goto L_0x00f7;
            default: goto L_0x00e2;
        };
    L_0x00e2:
        r1 = 5;
    L_0x00e3:
        r4 = super.getMaxInstanceId();
        r4 = r4 + r3;
        r4 = org.mozilla.javascript.IdScriptableObject.instanceIdInfo(r1, r4);
        goto L_0x001d;
    L_0x00ee:
        r1 = r7.multilineAttr;
        goto L_0x00e3;
    L_0x00f1:
        r1 = r7.starAttr;
        goto L_0x00e3;
    L_0x00f4:
        r1 = r7.inputAttr;
        goto L_0x00e3;
    L_0x00f7:
        r1 = r7.underscoreAttr;
        goto L_0x00e3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.regexp.NativeRegExpCtor.findInstanceIdInfo(java.lang.String):int");
    }

    /* access modifiers changed from: protected */
    public String getInstanceIdName(int id) {
        int shifted = id - super.getMaxInstanceId();
        if (1 > shifted || shifted > 21) {
            return super.getInstanceIdName(id);
        }
        switch (shifted) {
            case 1:
                return "multiline";
            case 2:
                return "$*";
            case 3:
                return "input";
            case 4:
                return "$_";
            case 5:
                return "lastMatch";
            case 6:
                return "$&";
            case 7:
                return "lastParen";
            case 8:
                return "$+";
            case 9:
                return "leftContext";
            case 10:
                return "$`";
            case 11:
                return "rightContext";
            case 12:
                return "$'";
            default:
                int substring_number = (shifted - 12) - 1;
                return new String(new char[]{'$', (char) (substring_number + 49)});
        }
    }

    /* access modifiers changed from: protected */
    public Object getInstanceIdValue(int id) {
        int shifted = id - super.getMaxInstanceId();
        if (1 > shifted || shifted > 21) {
            return super.getInstanceIdValue(id);
        }
        SubString stringResult;
        RegExpImpl impl = getImpl();
        switch (shifted) {
            case 1:
            case 2:
                return ScriptRuntime.wrapBoolean(impl.multiline);
            case 3:
            case 4:
                stringResult = impl.input;
                break;
            case 5:
            case 6:
                stringResult = impl.lastMatch;
                break;
            case 7:
            case 8:
                stringResult = impl.lastParen;
                break;
            case 9:
            case 10:
                stringResult = impl.leftContext;
                break;
            case 11:
            case 12:
                stringResult = impl.rightContext;
                break;
            default:
                stringResult = impl.getParenSubString((shifted - 12) - 1);
                break;
        }
        return stringResult == null ? "" : stringResult.toString();
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdValue(int id, Object value) {
        int shifted = id - super.getMaxInstanceId();
        switch (shifted) {
            case 1:
            case 2:
                getImpl().multiline = ScriptRuntime.toBoolean(value);
                return;
            case 3:
            case 4:
                getImpl().input = ScriptRuntime.toString(value);
                return;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return;
            default:
                int substring_number = (shifted - 12) - 1;
                if (substring_number < 0 || substring_number > 8) {
                    super.setInstanceIdValue(id, value);
                    return;
                }
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdAttributes(int id, int attr) {
        int shifted = id - super.getMaxInstanceId();
        switch (shifted) {
            case 1:
                this.multilineAttr = attr;
                return;
            case 2:
                this.starAttr = attr;
                return;
            case 3:
                this.inputAttr = attr;
                return;
            case 4:
                this.underscoreAttr = attr;
                return;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return;
            default:
                int substring_number = (shifted - 12) - 1;
                if (substring_number < 0 || substring_number > 8) {
                    super.setInstanceIdAttributes(id, attr);
                    return;
                }
                return;
        }
    }
}
