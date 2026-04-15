package org.mozilla.javascript;

final class NativeNumber extends IdScriptableObject {
    private static final int Id_constructor = 1;
    private static final int Id_toExponential = 7;
    private static final int Id_toFixed = 6;
    private static final int Id_toLocaleString = 3;
    private static final int Id_toPrecision = 8;
    private static final int Id_toSource = 4;
    private static final int Id_toString = 2;
    private static final int Id_valueOf = 5;
    private static final int MAX_PRECISION = 100;
    private static final int MAX_PROTOTYPE_ID = 8;
    private static final Object NUMBER_TAG = "Number";
    static final long serialVersionUID = 3504516769741512101L;
    private double doubleValue;

    static void init(Scriptable scope, boolean sealed) {
        new NativeNumber(0.0d).exportAsJSClass(8, scope, sealed);
    }

    NativeNumber(double number) {
        this.doubleValue = number;
    }

    public String getClassName() {
        return "Number";
    }

    /* access modifiers changed from: protected */
    public void fillConstructorProperties(IdFunctionObject ctor) {
        ctor.defineProperty("NaN", (Object) ScriptRuntime.NaNobj, 7);
        ctor.defineProperty("POSITIVE_INFINITY", (Object) ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
        ctor.defineProperty("NEGATIVE_INFINITY", (Object) ScriptRuntime.wrapNumber(Double.NEGATIVE_INFINITY), 7);
        ctor.defineProperty("MAX_VALUE", (Object) ScriptRuntime.wrapNumber(Double.MAX_VALUE), 7);
        ctor.defineProperty("MIN_VALUE", (Object) ScriptRuntime.wrapNumber(Double.MIN_VALUE), 7);
        super.fillConstructorProperties(ctor);
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
                arity = 1;
                s = "toString";
                break;
            case 3:
                arity = 1;
                s = "toLocaleString";
                break;
            case 4:
                arity = 0;
                s = "toSource";
                break;
            case 5:
                arity = 0;
                s = "valueOf";
                break;
            case 6:
                arity = 1;
                s = "toFixed";
                break;
            case 7:
                arity = 1;
                s = "toExponential";
                break;
            case 8:
                arity = 1;
                s = "toPrecision";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(NUMBER_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(NUMBER_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        if (id == 1) {
            double val = args.length >= 1 ? ScriptRuntime.toNumber(args[0]) : 0.0d;
            if (thisObj == null) {
                return new NativeNumber(val);
            }
            return ScriptRuntime.wrapNumber(val);
        } else if (thisObj instanceof NativeNumber) {
            double value = ((NativeNumber) thisObj).doubleValue;
            switch (id) {
                case 2:
                case 3:
                    int base;
                    if (args.length == 0 || args[0] == Undefined.instance) {
                        base = 10;
                    } else {
                        base = ScriptRuntime.toInt32(args[0]);
                    }
                    return ScriptRuntime.numberToString(value, base);
                case 4:
                    return "(new Number(" + ScriptRuntime.toString(value) + "))";
                case 5:
                    return ScriptRuntime.wrapNumber(value);
                case 6:
                    return num_to(value, args, 2, 2, -20, 0);
                case 7:
                    if (Double.isNaN(value)) {
                        return "NaN";
                    }
                    if (!Double.isInfinite(value)) {
                        return num_to(value, args, 1, 3, 0, 1);
                    }
                    if (value >= 0.0d) {
                        return "Infinity";
                    }
                    return "-Infinity";
                case 8:
                    if (args.length == 0 || args[0] == Undefined.instance) {
                        return ScriptRuntime.numberToString(value, 10);
                    }
                    if (Double.isNaN(value)) {
                        return "NaN";
                    }
                    if (!Double.isInfinite(value)) {
                        return num_to(value, args, 0, 4, 1, 0);
                    }
                    if (value >= 0.0d) {
                        return "Infinity";
                    }
                    return "-Infinity";
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
        } else {
            throw IdScriptableObject.incompatibleCallError(f);
        }
    }

    public String toString() {
        return ScriptRuntime.numberToString(this.doubleValue, 10);
    }

    private static String num_to(double val, Object[] args, int zeroArgMode, int oneArgMode, int precisionMin, int precisionOffset) {
        int precision;
        if (args.length == 0) {
            precision = 0;
            oneArgMode = zeroArgMode;
        } else {
            double p = ScriptRuntime.toInteger(args[0]);
            if (p < ((double) precisionMin) || p > 100.0d) {
                throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage1("msg.bad.precision", ScriptRuntime.toString(args[0])));
            }
            precision = ScriptRuntime.toInt32(p);
        }
        StringBuilder sb = new StringBuilder();
        DToA.JS_dtostr(sb, oneArgMode, precision + precisionOffset, val);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int c;
        switch (s.length()) {
            case 7:
                c = s.charAt(0);
                if (c != 116) {
                    if (c == 118) {
                        X = "valueOf";
                        id = 5;
                        break;
                    }
                }
                X = "toFixed";
                id = 6;
                break;
                break;
            case 8:
                c = s.charAt(3);
                if (c != 111) {
                    if (c == 116) {
                        X = "toString";
                        id = 2;
                        break;
                    }
                }
                X = "toSource";
                id = 4;
                break;
                break;
            case 11:
                c = s.charAt(0);
                if (c != 99) {
                    if (c == 116) {
                        X = "toPrecision";
                        id = 8;
                        break;
                    }
                }
                X = "constructor";
                id = 1;
                break;
                break;
            case 13:
                X = "toExponential";
                id = 7;
                break;
            case 14:
                X = "toLocaleString";
                id = 3;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
