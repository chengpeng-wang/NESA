package org.mozilla.javascript;

final class NativeMath extends IdScriptableObject {
    private static final int Id_E = 20;
    private static final int Id_LN10 = 22;
    private static final int Id_LN2 = 23;
    private static final int Id_LOG10E = 25;
    private static final int Id_LOG2E = 24;
    private static final int Id_PI = 21;
    private static final int Id_SQRT1_2 = 26;
    private static final int Id_SQRT2 = 27;
    private static final int Id_abs = 2;
    private static final int Id_acos = 3;
    private static final int Id_asin = 4;
    private static final int Id_atan = 5;
    private static final int Id_atan2 = 6;
    private static final int Id_ceil = 7;
    private static final int Id_cos = 8;
    private static final int Id_exp = 9;
    private static final int Id_floor = 10;
    private static final int Id_log = 11;
    private static final int Id_max = 12;
    private static final int Id_min = 13;
    private static final int Id_pow = 14;
    private static final int Id_random = 15;
    private static final int Id_round = 16;
    private static final int Id_sin = 17;
    private static final int Id_sqrt = 18;
    private static final int Id_tan = 19;
    private static final int Id_toSource = 1;
    private static final int LAST_METHOD_ID = 19;
    private static final Object MATH_TAG = "Math";
    private static final int MAX_ID = 27;
    static final long serialVersionUID = -8838847185801131569L;

    static void init(Scriptable scope, boolean sealed) {
        NativeMath obj = new NativeMath();
        obj.activatePrototypeMap(27);
        obj.setPrototype(ScriptableObject.getObjectPrototype(scope));
        obj.setParentScope(scope);
        if (sealed) {
            obj.sealObject();
        }
        ScriptableObject.defineProperty(scope, "Math", obj, 2);
    }

    private NativeMath() {
    }

    public String getClassName() {
        return "Math";
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        String name;
        if (id <= 19) {
            int arity;
            switch (id) {
                case 1:
                    arity = 0;
                    name = "toSource";
                    break;
                case 2:
                    arity = 1;
                    name = "abs";
                    break;
                case 3:
                    arity = 1;
                    name = "acos";
                    break;
                case 4:
                    arity = 1;
                    name = "asin";
                    break;
                case 5:
                    arity = 1;
                    name = "atan";
                    break;
                case 6:
                    arity = 2;
                    name = "atan2";
                    break;
                case 7:
                    arity = 1;
                    name = "ceil";
                    break;
                case 8:
                    arity = 1;
                    name = "cos";
                    break;
                case 9:
                    arity = 1;
                    name = "exp";
                    break;
                case 10:
                    arity = 1;
                    name = "floor";
                    break;
                case 11:
                    arity = 1;
                    name = "log";
                    break;
                case 12:
                    arity = 2;
                    name = "max";
                    break;
                case 13:
                    arity = 2;
                    name = "min";
                    break;
                case 14:
                    arity = 2;
                    name = "pow";
                    break;
                case 15:
                    arity = 0;
                    name = "random";
                    break;
                case 16:
                    arity = 1;
                    name = "round";
                    break;
                case 17:
                    arity = 1;
                    name = "sin";
                    break;
                case 18:
                    arity = 1;
                    name = "sqrt";
                    break;
                case 19:
                    arity = 1;
                    name = "tan";
                    break;
                default:
                    throw new IllegalStateException(String.valueOf(id));
            }
            initPrototypeMethod(MATH_TAG, id, name, arity);
            return;
        }
        double x;
        switch (id) {
            case 20:
                x = 2.718281828459045d;
                name = "E";
                break;
            case 21:
                x = 3.141592653589793d;
                name = "PI";
                break;
            case 22:
                x = 2.302585092994046d;
                name = "LN10";
                break;
            case 23:
                x = 0.6931471805599453d;
                name = "LN2";
                break;
            case 24:
                x = 1.4426950408889634d;
                name = "LOG2E";
                break;
            case 25:
                x = 0.4342944819032518d;
                name = "LOG10E";
                break;
            case 26:
                x = 0.7071067811865476d;
                name = "SQRT1_2";
                break;
            case 27:
                x = 1.4142135623730951d;
                name = "SQRT2";
                break;
            default:
                throw new IllegalStateException(String.valueOf(id));
        }
        initPrototypeValue(id, name, ScriptRuntime.wrapNumber(x), 7);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(MATH_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        double x;
        int methodId = f.methodId();
        switch (methodId) {
            case 1:
                return "Math";
            case 2:
                x = ScriptRuntime.toNumber(args, 0);
                if (x != 0.0d) {
                    if (x < 0.0d) {
                        x = -x;
                        break;
                    }
                }
                x = 0.0d;
                break;
                break;
            case 3:
            case 4:
                x = ScriptRuntime.toNumber(args, 0);
                if (x == x && -1.0d <= x && x <= 1.0d) {
                    if (methodId != 3) {
                        x = Math.asin(x);
                        break;
                    }
                    x = Math.acos(x);
                    break;
                }
                x = Double.NaN;
                break;
                break;
            case 5:
                x = Math.atan(ScriptRuntime.toNumber(args, 0));
                break;
            case 6:
                x = Math.atan2(ScriptRuntime.toNumber(args, 0), ScriptRuntime.toNumber(args, 1));
                break;
            case 7:
                x = Math.ceil(ScriptRuntime.toNumber(args, 0));
                break;
            case 8:
                x = ScriptRuntime.toNumber(args, 0);
                if (x != Double.POSITIVE_INFINITY && x != Double.NEGATIVE_INFINITY) {
                    x = Math.cos(x);
                    break;
                }
                x = Double.NaN;
                break;
            case 9:
                x = ScriptRuntime.toNumber(args, 0);
                if (x != Double.POSITIVE_INFINITY) {
                    if (x != Double.NEGATIVE_INFINITY) {
                        x = Math.exp(x);
                        break;
                    }
                    x = 0.0d;
                    break;
                }
                break;
            case 10:
                x = Math.floor(ScriptRuntime.toNumber(args, 0));
                break;
            case 11:
                x = ScriptRuntime.toNumber(args, 0);
                if (x >= 0.0d) {
                    x = Math.log(x);
                    break;
                }
                x = Double.NaN;
                break;
            case 12:
            case 13:
                x = methodId == 12 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                for (int i = 0; i != args.length; i++) {
                    double d = ScriptRuntime.toNumber(args[i]);
                    if (d != d) {
                        x = d;
                        break;
                    }
                    if (methodId == 12) {
                        x = Math.max(x, d);
                    } else {
                        x = Math.min(x, d);
                    }
                }
                break;
            case 14:
                x = js_pow(ScriptRuntime.toNumber(args, 0), ScriptRuntime.toNumber(args, 1));
                break;
            case 15:
                x = Math.random();
                break;
            case 16:
                x = ScriptRuntime.toNumber(args, 0);
                if (!(x != x || x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY)) {
                    long l = Math.round(x);
                    if (l == 0) {
                        if (x >= 0.0d) {
                            if (x != 0.0d) {
                                x = 0.0d;
                                break;
                            }
                        }
                        x = ScriptRuntime.negativeZero;
                        break;
                    }
                    x = (double) l;
                    break;
                }
                break;
            case 17:
                x = ScriptRuntime.toNumber(args, 0);
                if (x != Double.POSITIVE_INFINITY && x != Double.NEGATIVE_INFINITY) {
                    x = Math.sin(x);
                    break;
                }
                x = Double.NaN;
                break;
                break;
            case 18:
                x = Math.sqrt(ScriptRuntime.toNumber(args, 0));
                break;
            case 19:
                x = Math.tan(ScriptRuntime.toNumber(args, 0));
                break;
            default:
                throw new IllegalStateException(String.valueOf(methodId));
        }
        return ScriptRuntime.wrapNumber(x);
    }

    private double js_pow(double x, double y) {
        if (y != y) {
            return y;
        }
        if (y == 0.0d) {
            return 1.0d;
        }
        long y_long;
        if (x != 0.0d) {
            double result = Math.pow(x, y);
            if (result == result) {
                return result;
            }
            if (y == Double.POSITIVE_INFINITY) {
                if (x < -1.0d || 1.0d < x) {
                    return Double.POSITIVE_INFINITY;
                }
                if (-1.0d >= x || x >= 1.0d) {
                    return result;
                }
                return 0.0d;
            } else if (y == Double.NEGATIVE_INFINITY) {
                if (x < -1.0d || 1.0d < x) {
                    return 0.0d;
                }
                if (-1.0d >= x || x >= 1.0d) {
                    return result;
                }
                return Double.POSITIVE_INFINITY;
            } else if (x == Double.POSITIVE_INFINITY) {
                return y > 0.0d ? Double.POSITIVE_INFINITY : 0.0d;
            } else {
                if (x != Double.NEGATIVE_INFINITY) {
                    return result;
                }
                y_long = (long) y;
                return (((double) y_long) != y || (1 & y_long) == 0) ? y > 0.0d ? Double.POSITIVE_INFINITY : 0.0d : y > 0.0d ? Double.NEGATIVE_INFINITY : -0.0d;
            }
        } else if (1.0d / x > 0.0d) {
            return y > 0.0d ? 0.0d : Double.POSITIVE_INFINITY;
        } else {
            y_long = (long) y;
            return (((double) y_long) != y || (1 & y_long) == 0) ? y > 0.0d ? 0.0d : Double.POSITIVE_INFINITY : y > 0.0d ? -0.0d : Double.NEGATIVE_INFINITY;
        }
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int c;
        switch (s.length()) {
            case 1:
                if (s.charAt(0) == 'E') {
                    return 20;
                }
                break;
            case 2:
                if (s.charAt(0) == 'P' && s.charAt(1) == 'I') {
                    return 21;
                }
            case 3:
                switch (s.charAt(0)) {
                    case 'L':
                        if (s.charAt(2) == '2' && s.charAt(1) == 'N') {
                            return 23;
                        }
                    case 'a':
                        if (s.charAt(2) == 's' && s.charAt(1) == 'b') {
                            return 2;
                        }
                    case 'c':
                        if (s.charAt(2) == 's' && s.charAt(1) == 'o') {
                            return 8;
                        }
                    case 'e':
                        if (s.charAt(2) == 'p' && s.charAt(1) == 'x') {
                            return 9;
                        }
                    case 'l':
                        if (s.charAt(2) == 'g' && s.charAt(1) == 'o') {
                            return 11;
                        }
                    case 'm':
                        c = s.charAt(2);
                        if (c == 110) {
                            if (s.charAt(1) == 'i') {
                                return 13;
                            }
                        } else if (c == 120 && s.charAt(1) == 'a') {
                            return 12;
                        }
                        break;
                    case 'p':
                        if (s.charAt(2) == 'w' && s.charAt(1) == 'o') {
                            return 14;
                        }
                    case 's':
                        if (s.charAt(2) == 'n' && s.charAt(1) == 'i') {
                            return 17;
                        }
                    case 't':
                        if (s.charAt(2) == 'n' && s.charAt(1) == 'a') {
                            return 19;
                        }
                }
                break;
            case 4:
                switch (s.charAt(1)) {
                    case 'N':
                        X = "LN10";
                        id = 22;
                        break;
                    case 'c':
                        X = "acos";
                        id = 3;
                        break;
                    case 'e':
                        X = "ceil";
                        id = 7;
                        break;
                    case 'q':
                        X = "sqrt";
                        id = 18;
                        break;
                    case 's':
                        X = "asin";
                        id = 4;
                        break;
                    case 't':
                        X = "atan";
                        id = 5;
                        break;
                }
                break;
            case 5:
                switch (s.charAt(0)) {
                    case 'L':
                        X = "LOG2E";
                        id = 24;
                        break;
                    case 'S':
                        X = "SQRT2";
                        id = 27;
                        break;
                    case 'a':
                        X = "atan2";
                        id = 6;
                        break;
                    case 'f':
                        X = "floor";
                        id = 10;
                        break;
                    case 'r':
                        X = "round";
                        id = 16;
                        break;
                }
                break;
            case 6:
                c = s.charAt(0);
                if (c != 76) {
                    if (c == 114) {
                        X = "random";
                        id = 15;
                        break;
                    }
                }
                X = "LOG10E";
                id = 25;
                break;
                break;
            case 7:
                X = "SQRT1_2";
                id = 26;
                break;
            case 8:
                X = "toSource";
                id = 1;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
