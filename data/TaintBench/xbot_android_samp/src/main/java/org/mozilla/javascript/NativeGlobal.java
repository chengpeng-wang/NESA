package org.mozilla.javascript;

import java.io.Serializable;
import org.mozilla.classfile.ByteCode;
import org.mozilla.javascript.TopLevel.Builtins;
import org.mozilla.javascript.xml.XMLLib;
import org.objectweb.asm.Opcodes;

public class NativeGlobal implements Serializable, IdFunctionCall {
    private static final Object FTAG = "Global";
    private static final int INVALID_UTF8 = Integer.MAX_VALUE;
    private static final int Id_decodeURI = 1;
    private static final int Id_decodeURIComponent = 2;
    private static final int Id_encodeURI = 3;
    private static final int Id_encodeURIComponent = 4;
    private static final int Id_escape = 5;
    private static final int Id_eval = 6;
    private static final int Id_isFinite = 7;
    private static final int Id_isNaN = 8;
    private static final int Id_isXMLName = 9;
    private static final int Id_new_CommonError = 14;
    private static final int Id_parseFloat = 10;
    private static final int Id_parseInt = 11;
    private static final int Id_unescape = 12;
    private static final int Id_uneval = 13;
    private static final int LAST_SCOPE_FUNCTION_ID = 13;
    private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";
    static final long serialVersionUID = 6080442165748707530L;

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        String name;
        NativeGlobal obj = new NativeGlobal();
        for (int id = 1; id <= 13; id++) {
            int arity = 1;
            switch (id) {
                case 1:
                    name = "decodeURI";
                    break;
                case 2:
                    name = "decodeURIComponent";
                    break;
                case 3:
                    name = "encodeURI";
                    break;
                case 4:
                    name = "encodeURIComponent";
                    break;
                case 5:
                    name = "escape";
                    break;
                case 6:
                    name = "eval";
                    break;
                case 7:
                    name = "isFinite";
                    break;
                case 8:
                    name = "isNaN";
                    break;
                case 9:
                    name = "isXMLName";
                    break;
                case 10:
                    name = "parseFloat";
                    break;
                case 11:
                    name = "parseInt";
                    arity = 2;
                    break;
                case 12:
                    name = "unescape";
                    break;
                case 13:
                    name = "uneval";
                    break;
                default:
                    throw Kit.codeBug();
            }
            IdFunctionObject f = new IdFunctionObject(obj, FTAG, id, name, arity, scope);
            if (sealed) {
                f.sealObject();
            }
            f.exportAsScopeProperty();
        }
        ScriptableObject.defineProperty(scope, "NaN", ScriptRuntime.NaNobj, 7);
        ScriptableObject.defineProperty(scope, "Infinity", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
        ScriptableObject.defineProperty(scope, "undefined", Undefined.instance, 7);
        for (NativeErrors error : NativeErrors.values()) {
            if (error != NativeErrors.Error) {
                name = error.name();
                ScriptableObject errorProto = (ScriptableObject) ScriptRuntime.newBuiltinObject(cx, scope, Builtins.Error, ScriptRuntime.emptyArgs);
                errorProto.put("name", (Scriptable) errorProto, (Object) name);
                errorProto.put("message", (Scriptable) errorProto, (Object) "");
                Object ctor = new IdFunctionObject(obj, FTAG, 14, name, 1, scope);
                ctor.markAsConstructor(errorProto);
                errorProto.put("constructor", (Scriptable) errorProto, ctor);
                errorProto.setAttributes("constructor", 2);
                if (sealed) {
                    errorProto.sealObject();
                    ctor.sealObject();
                }
                ctor.exportAsScopeProperty();
            }
        }
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (f.hasTag(FTAG)) {
            int methodId = f.methodId();
            boolean result;
            double d;
            switch (methodId) {
                case 1:
                case 2:
                    boolean z;
                    String str = ScriptRuntime.toString(args, 0);
                    if (methodId == 1) {
                        z = true;
                    } else {
                        z = false;
                    }
                    return decode(str, z);
                case 3:
                case 4:
                    return encode(ScriptRuntime.toString(args, 0), methodId == 3);
                case 5:
                    return js_escape(args);
                case 6:
                    return js_eval(cx, scope, args);
                case 7:
                    if (args.length < 1) {
                        result = false;
                    } else {
                        d = ScriptRuntime.toNumber(args[0]);
                        result = (d != d || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY) ? false : true;
                    }
                    return ScriptRuntime.wrapBoolean(result);
                case 8:
                    if (args.length < 1) {
                        result = true;
                    } else {
                        d = ScriptRuntime.toNumber(args[0]);
                        result = d != d;
                    }
                    return ScriptRuntime.wrapBoolean(result);
                case 9:
                    return ScriptRuntime.wrapBoolean(XMLLib.extractFromScope(scope).isXMLName(cx, args.length == 0 ? Undefined.instance : args[0]));
                case 10:
                    return js_parseFloat(args);
                case 11:
                    return js_parseInt(args);
                case 12:
                    return js_unescape(args);
                case 13:
                    return ScriptRuntime.uneval(cx, scope, args.length != 0 ? args[0] : Undefined.instance);
                case 14:
                    return NativeError.make(cx, scope, f, args);
            }
        }
        throw f.unknown();
    }

    /* JADX WARNING: Missing block: B:11:0x0028, code skipped:
            if (r5 != false) goto L_0x002a;
     */
    private java.lang.Object js_parseInt(java.lang.Object[] r12) {
        /*
        r11 = this;
        r9 = 0;
        r7 = org.mozilla.javascript.ScriptRuntime.toString(r12, r9);
        r9 = 1;
        r6 = org.mozilla.javascript.ScriptRuntime.toInt32(r12, r9);
        r4 = r7.length();
        if (r4 != 0) goto L_0x0013;
    L_0x0010:
        r9 = org.mozilla.javascript.ScriptRuntime.NaNobj;
    L_0x0012:
        return r9;
    L_0x0013:
        r5 = 0;
        r8 = 0;
    L_0x0015:
        r1 = r7.charAt(r8);
        r9 = org.mozilla.javascript.ScriptRuntime.isStrWhiteSpaceChar(r1);
        if (r9 != 0) goto L_0x0060;
    L_0x001f:
        r9 = 43;
        if (r1 == r9) goto L_0x002a;
    L_0x0023:
        r9 = 45;
        if (r1 != r9) goto L_0x0065;
    L_0x0027:
        r5 = 1;
    L_0x0028:
        if (r5 == 0) goto L_0x002c;
    L_0x002a:
        r8 = r8 + 1;
    L_0x002c:
        r0 = -1;
        if (r6 != 0) goto L_0x0067;
    L_0x002f:
        r6 = -1;
    L_0x0030:
        r9 = -1;
        if (r6 != r9) goto L_0x0054;
    L_0x0033:
        r6 = 10;
        r9 = r4 - r8;
        r10 = 1;
        if (r9 <= r10) goto L_0x0054;
    L_0x003a:
        r9 = r7.charAt(r8);
        r10 = 48;
        if (r9 != r10) goto L_0x0054;
    L_0x0042:
        r9 = r8 + 1;
        r1 = r7.charAt(r9);
        r9 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        if (r1 == r9) goto L_0x0050;
    L_0x004c:
        r9 = 88;
        if (r1 != r9) goto L_0x0093;
    L_0x0050:
        r6 = 16;
        r8 = r8 + 2;
    L_0x0054:
        r2 = org.mozilla.javascript.ScriptRuntime.stringToNumber(r7, r8, r6);
        if (r5 == 0) goto L_0x005b;
    L_0x005a:
        r2 = -r2;
    L_0x005b:
        r9 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r2);
        goto L_0x0012;
    L_0x0060:
        r8 = r8 + 1;
        if (r8 < r4) goto L_0x0015;
    L_0x0064:
        goto L_0x001f;
    L_0x0065:
        r5 = 0;
        goto L_0x0028;
    L_0x0067:
        r9 = 2;
        if (r6 < r9) goto L_0x006e;
    L_0x006a:
        r9 = 36;
        if (r6 <= r9) goto L_0x0071;
    L_0x006e:
        r9 = org.mozilla.javascript.ScriptRuntime.NaNobj;
        goto L_0x0012;
    L_0x0071:
        r9 = 16;
        if (r6 != r9) goto L_0x0030;
    L_0x0075:
        r9 = r4 - r8;
        r10 = 1;
        if (r9 <= r10) goto L_0x0030;
    L_0x007a:
        r9 = r7.charAt(r8);
        r10 = 48;
        if (r9 != r10) goto L_0x0030;
    L_0x0082:
        r9 = r8 + 1;
        r1 = r7.charAt(r9);
        r9 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        if (r1 == r9) goto L_0x0090;
    L_0x008c:
        r9 = 88;
        if (r1 != r9) goto L_0x0030;
    L_0x0090:
        r8 = r8 + 2;
        goto L_0x0030;
    L_0x0093:
        r9 = 48;
        if (r9 > r1) goto L_0x0054;
    L_0x0097:
        r9 = 57;
        if (r1 > r9) goto L_0x0054;
    L_0x009b:
        r6 = 8;
        r8 = r8 + 1;
        goto L_0x0054;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeGlobal.js_parseInt(java.lang.Object[]):java.lang.Object");
    }

    /* JADX WARNING: Missing block: B:43:0x0080, code skipped:
            r7 = r7 + 1;
     */
    private java.lang.Object js_parseFloat(java.lang.Object[] r15) {
        /*
        r14 = this;
        r11 = r15.length;
        r12 = 1;
        if (r11 >= r12) goto L_0x0007;
    L_0x0004:
        r11 = org.mozilla.javascript.ScriptRuntime.NaNobj;
    L_0x0006:
        return r11;
    L_0x0007:
        r11 = 0;
        r11 = r15[r11];
        r9 = org.mozilla.javascript.ScriptRuntime.toString(r11);
        r8 = r9.length();
        r10 = 0;
    L_0x0013:
        if (r10 != r8) goto L_0x0018;
    L_0x0015:
        r11 = org.mozilla.javascript.ScriptRuntime.NaNobj;
        goto L_0x0006;
    L_0x0018:
        r0 = r9.charAt(r10);
        r11 = org.mozilla.javascript.ScriptRuntime.isStrWhiteSpaceChar(r0);
        if (r11 != 0) goto L_0x0032;
    L_0x0022:
        r7 = r10;
        r11 = 43;
        if (r0 == r11) goto L_0x002b;
    L_0x0027:
        r11 = 45;
        if (r0 != r11) goto L_0x0039;
    L_0x002b:
        r7 = r7 + 1;
        if (r7 != r8) goto L_0x0035;
    L_0x002f:
        r11 = org.mozilla.javascript.ScriptRuntime.NaNobj;
        goto L_0x0006;
    L_0x0032:
        r10 = r10 + 1;
        goto L_0x0013;
    L_0x0035:
        r0 = r9.charAt(r7);
    L_0x0039:
        r11 = 73;
        if (r0 != r11) goto L_0x0061;
    L_0x003d:
        r11 = r7 + 8;
        if (r11 > r8) goto L_0x005e;
    L_0x0041:
        r11 = "Infinity";
        r12 = 0;
        r13 = 8;
        r11 = r9.regionMatches(r7, r11, r12, r13);
        if (r11 == 0) goto L_0x005e;
    L_0x004c:
        r11 = r9.charAt(r10);
        r12 = 45;
        if (r11 != r12) goto L_0x005b;
    L_0x0054:
        r1 = -4503599627370496; // 0xfff0000000000000 float:0.0 double:-Infinity;
    L_0x0056:
        r11 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r1);
        goto L_0x0006;
    L_0x005b:
        r1 = 9218868437227405312; // 0x7ff0000000000000 float:0.0 double:Infinity;
        goto L_0x0056;
    L_0x005e:
        r11 = org.mozilla.javascript.ScriptRuntime.NaNobj;
        goto L_0x0006;
    L_0x0061:
        r3 = -1;
        r5 = -1;
        r6 = 0;
    L_0x0064:
        if (r7 >= r8) goto L_0x006d;
    L_0x0066:
        r11 = r9.charAt(r7);
        switch(r11) {
            case 43: goto L_0x008c;
            case 45: goto L_0x008c;
            case 46: goto L_0x007c;
            case 48: goto L_0x0097;
            case 49: goto L_0x0097;
            case 50: goto L_0x0097;
            case 51: goto L_0x0097;
            case 52: goto L_0x0097;
            case 53: goto L_0x0097;
            case 54: goto L_0x0097;
            case 55: goto L_0x0097;
            case 56: goto L_0x0097;
            case 57: goto L_0x0097;
            case 69: goto L_0x0083;
            case 101: goto L_0x0083;
            default: goto L_0x006d;
        };
    L_0x006d:
        r11 = -1;
        if (r5 == r11) goto L_0x0073;
    L_0x0070:
        if (r6 != 0) goto L_0x0073;
    L_0x0072:
        r7 = r5;
    L_0x0073:
        r9 = r9.substring(r10, r7);
        r11 = java.lang.Double.valueOf(r9);	 Catch:{ NumberFormatException -> 0x009c }
        goto L_0x0006;
    L_0x007c:
        r11 = -1;
        if (r3 != r11) goto L_0x006d;
    L_0x007f:
        r3 = r7;
    L_0x0080:
        r7 = r7 + 1;
        goto L_0x0064;
    L_0x0083:
        r11 = -1;
        if (r5 != r11) goto L_0x006d;
    L_0x0086:
        r11 = r8 + -1;
        if (r7 == r11) goto L_0x006d;
    L_0x008a:
        r5 = r7;
        goto L_0x0080;
    L_0x008c:
        r11 = r7 + -1;
        if (r5 != r11) goto L_0x006d;
    L_0x0090:
        r11 = r8 + -1;
        if (r7 != r11) goto L_0x0080;
    L_0x0094:
        r7 = r7 + -1;
        goto L_0x006d;
    L_0x0097:
        r11 = -1;
        if (r5 == r11) goto L_0x0080;
    L_0x009a:
        r6 = 1;
        goto L_0x0080;
    L_0x009c:
        r4 = move-exception;
        r11 = org.mozilla.javascript.ScriptRuntime.NaNobj;
        goto L_0x0006;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeGlobal.js_parseFloat(java.lang.Object[]):java.lang.Object");
    }

    /* JADX WARNING: Missing block: B:7:0x0031, code skipped:
            if ((r13 & -8) == 0) goto L_0x003a;
     */
    private java.lang.Object js_escape(java.lang.Object[] r20) {
        /*
        r19 = this;
        r4 = 1;
        r5 = 2;
        r3 = 4;
        r17 = 0;
        r0 = r20;
        r1 = r17;
        r14 = org.mozilla.javascript.ScriptRuntime.toString(r0, r1);
        r13 = 7;
        r0 = r20;
        r0 = r0.length;
        r17 = r0;
        r18 = 1;
        r0 = r17;
        r1 = r18;
        if (r0 <= r1) goto L_0x003a;
    L_0x001b:
        r17 = 1;
        r17 = r20[r17];
        r7 = org.mozilla.javascript.ScriptRuntime.toNumber(r17);
        r17 = (r7 > r7 ? 1 : (r7 == r7 ? 0 : -1));
        if (r17 != 0) goto L_0x0033;
    L_0x0027:
        r13 = (int) r7;
        r0 = (double) r13;
        r17 = r0;
        r17 = (r17 > r7 ? 1 : (r17 == r7 ? 0 : -1));
        if (r17 != 0) goto L_0x0033;
    L_0x002f:
        r17 = r13 & -8;
        if (r17 == 0) goto L_0x003a;
    L_0x0033:
        r17 = "msg.bad.esc.mask";
        r17 = org.mozilla.javascript.Context.reportRuntimeError0(r17);
        throw r17;
    L_0x003a:
        r15 = 0;
        r12 = 0;
        r2 = r14.length();
    L_0x0040:
        if (r12 == r2) goto L_0x010a;
    L_0x0042:
        r6 = r14.charAt(r12);
        if (r13 == 0) goto L_0x00a7;
    L_0x0048:
        r17 = 48;
        r0 = r17;
        if (r6 < r0) goto L_0x0054;
    L_0x004e:
        r17 = 57;
        r0 = r17;
        if (r6 <= r0) goto L_0x009a;
    L_0x0054:
        r17 = 65;
        r0 = r17;
        if (r6 < r0) goto L_0x0060;
    L_0x005a:
        r17 = 90;
        r0 = r17;
        if (r6 <= r0) goto L_0x009a;
    L_0x0060:
        r17 = 97;
        r0 = r17;
        if (r6 < r0) goto L_0x006c;
    L_0x0066:
        r17 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        r0 = r17;
        if (r6 <= r0) goto L_0x009a;
    L_0x006c:
        r17 = 64;
        r0 = r17;
        if (r6 == r0) goto L_0x009a;
    L_0x0072:
        r17 = 42;
        r0 = r17;
        if (r6 == r0) goto L_0x009a;
    L_0x0078:
        r17 = 95;
        r0 = r17;
        if (r6 == r0) goto L_0x009a;
    L_0x007e:
        r17 = 45;
        r0 = r17;
        if (r6 == r0) goto L_0x009a;
    L_0x0084:
        r17 = 46;
        r0 = r17;
        if (r6 == r0) goto L_0x009a;
    L_0x008a:
        r17 = r13 & 4;
        if (r17 == 0) goto L_0x00a7;
    L_0x008e:
        r17 = 47;
        r0 = r17;
        if (r6 == r0) goto L_0x009a;
    L_0x0094:
        r17 = 43;
        r0 = r17;
        if (r6 != r0) goto L_0x00a7;
    L_0x009a:
        if (r15 == 0) goto L_0x00a4;
    L_0x009c:
        r0 = (char) r6;
        r17 = r0;
        r0 = r17;
        r15.append(r0);
    L_0x00a4:
        r12 = r12 + 1;
        goto L_0x0040;
    L_0x00a7:
        if (r15 != 0) goto L_0x00b8;
    L_0x00a9:
        r15 = new java.lang.StringBuilder;
        r17 = r2 + 3;
        r0 = r17;
        r15.<init>(r0);
        r15.append(r14);
        r15.setLength(r12);
    L_0x00b8:
        r17 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r0 = r17;
        if (r6 >= r0) goto L_0x00f7;
    L_0x00be:
        r17 = 32;
        r0 = r17;
        if (r6 != r0) goto L_0x00d2;
    L_0x00c4:
        r17 = 2;
        r0 = r17;
        if (r13 != r0) goto L_0x00d2;
    L_0x00ca:
        r17 = 43;
        r0 = r17;
        r15.append(r0);
        goto L_0x00a4;
    L_0x00d2:
        r17 = 37;
        r0 = r17;
        r15.append(r0);
        r11 = 2;
    L_0x00da:
        r17 = r11 + -1;
        r16 = r17 * 4;
    L_0x00de:
        if (r16 < 0) goto L_0x00a4;
    L_0x00e0:
        r17 = r6 >> r16;
        r9 = r17 & 15;
        r17 = 10;
        r0 = r17;
        if (r9 >= r0) goto L_0x0107;
    L_0x00ea:
        r10 = r9 + 48;
    L_0x00ec:
        r0 = (char) r10;
        r17 = r0;
        r0 = r17;
        r15.append(r0);
        r16 = r16 + -4;
        goto L_0x00de;
    L_0x00f7:
        r17 = 37;
        r0 = r17;
        r15.append(r0);
        r17 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r0 = r17;
        r15.append(r0);
        r11 = 4;
        goto L_0x00da;
    L_0x0107:
        r10 = r9 + 55;
        goto L_0x00ec;
    L_0x010a:
        if (r15 != 0) goto L_0x010d;
    L_0x010c:
        return r14;
    L_0x010d:
        r14 = r15.toString();
        goto L_0x010c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeGlobal.js_escape(java.lang.Object[]):java.lang.Object");
    }

    private Object js_unescape(Object[] args) {
        String s = ScriptRuntime.toString(args, 0);
        int firstEscapePos = s.indexOf(37);
        if (firstEscapePos < 0) {
            return s;
        }
        int L = s.length();
        char[] buf = s.toCharArray();
        int destination = firstEscapePos;
        int k = firstEscapePos;
        while (k != L) {
            char c = buf[k];
            k++;
            if (c == '%' && k != L) {
                int start;
                int end;
                if (buf[k] == 'u') {
                    start = k + 1;
                    end = k + 5;
                } else {
                    start = k;
                    end = k + 2;
                }
                if (end <= L) {
                    int x = 0;
                    for (int i = start; i != end; i++) {
                        x = Kit.xDigitToInt(buf[i], x);
                    }
                    if (x >= 0) {
                        c = (char) x;
                        k = end;
                    }
                }
            }
            buf[destination] = c;
            destination++;
        }
        return new String(buf, 0, destination);
    }

    private Object js_eval(Context cx, Scriptable scope, Object[] args) {
        Scriptable global = ScriptableObject.getTopLevelScope(scope);
        return ScriptRuntime.evalSpecial(cx, global, global, args, "eval code", 1);
    }

    static boolean isEvalFunction(Object functionObj) {
        if (functionObj instanceof IdFunctionObject) {
            IdFunctionObject function = (IdFunctionObject) functionObj;
            if (function.hasTag(FTAG) && function.methodId() == 6) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static EcmaError constructError(Context cx, String error, String message, Scriptable scope) {
        return ScriptRuntime.constructError(error, message);
    }

    @Deprecated
    public static EcmaError constructError(Context cx, String error, String message, Scriptable scope, String sourceName, int lineNumber, int columnNumber, String lineSource) {
        return ScriptRuntime.constructError(error, message, sourceName, lineNumber, lineSource, columnNumber);
    }

    private static String encode(String str, boolean fullUri) {
        byte[] utf8buf = null;
        StringBuilder sb = null;
        int k = 0;
        int length = str.length();
        while (k != length) {
            char C = str.charAt(k);
            if (!encodeUnescaped(C, fullUri)) {
                if (sb == null) {
                    sb = new StringBuilder(length + 3);
                    sb.append(str);
                    sb.setLength(k);
                    utf8buf = new byte[6];
                }
                if (56320 > C || C > 57343) {
                    int V;
                    if (C < 55296 || 56319 < C) {
                        V = C;
                    } else {
                        k++;
                        if (k == length) {
                            throw uriError();
                        }
                        char C2 = str.charAt(k);
                        if (56320 > C2 || C2 > 57343) {
                            throw uriError();
                        }
                        V = (((C - 55296) << 10) + (C2 - 56320)) + 65536;
                    }
                    int L = oneUcs4ToUtf8Char(utf8buf, V);
                    for (int j = 0; j < L; j++) {
                        int d = utf8buf[j] & ByteCode.IMPDEP2;
                        sb.append('%');
                        sb.append(toHexChar(d >>> 4));
                        sb.append(toHexChar(d & 15));
                    }
                } else {
                    throw uriError();
                }
            } else if (sb != null) {
                sb.append(C);
            }
            k++;
        }
        return sb == null ? str : sb.toString();
    }

    private static char toHexChar(int i) {
        if ((i >> 4) != 0) {
            Kit.codeBug();
        }
        return (char) (i < 10 ? i + 48 : (i - 10) + 65);
    }

    private static int unHex(char c) {
        if ('A' <= c && c <= 'F') {
            return (c - 65) + 10;
        }
        if ('a' <= c && c <= 'f') {
            return (c - 97) + 10;
        }
        if ('0' > c || c > '9') {
            return -1;
        }
        return c - 48;
    }

    private static int unHex(char c1, char c2) {
        int i1 = unHex(c1);
        int i2 = unHex(c2);
        if (i1 < 0 || i2 < 0) {
            return -1;
        }
        return (i1 << 4) | i2;
    }

    private static String decode(String str, boolean fullUri) {
        char[] buf = null;
        int k = 0;
        int length = str.length();
        int bufTop = 0;
        while (k != length) {
            int bufTop2;
            char C = str.charAt(k);
            if (C != '%') {
                if (buf != null) {
                    bufTop2 = bufTop + 1;
                    buf[bufTop] = C;
                } else {
                    bufTop2 = bufTop;
                }
                k++;
            } else {
                if (buf == null) {
                    buf = new char[length];
                    str.getChars(0, k, buf, 0);
                    bufTop = k;
                }
                int start = k;
                if (k + 3 > length) {
                    throw uriError();
                }
                int B = unHex(str.charAt(k + 1), str.charAt(k + 2));
                if (B < 0) {
                    throw uriError();
                }
                k += 3;
                if ((B & 128) == 0) {
                    C = (char) B;
                } else if ((B & 192) == 128) {
                    throw uriError();
                } else {
                    int utf8Tail;
                    int ucs4Char;
                    int minUcs4Char;
                    if ((B & 32) == 0) {
                        utf8Tail = 1;
                        ucs4Char = B & 31;
                        minUcs4Char = 128;
                    } else if ((B & 16) == 0) {
                        utf8Tail = 2;
                        ucs4Char = B & 15;
                        minUcs4Char = Opcodes.ACC_STRICT;
                    } else if ((B & 8) == 0) {
                        utf8Tail = 3;
                        ucs4Char = B & 7;
                        minUcs4Char = 65536;
                    } else if ((B & 4) == 0) {
                        utf8Tail = 4;
                        ucs4Char = B & 3;
                        minUcs4Char = 2097152;
                    } else if ((B & 2) == 0) {
                        utf8Tail = 5;
                        ucs4Char = B & 1;
                        minUcs4Char = 67108864;
                    } else {
                        throw uriError();
                    }
                    if ((utf8Tail * 3) + k > length) {
                        throw uriError();
                    }
                    for (int j = 0; j != utf8Tail; j++) {
                        if (str.charAt(k) != '%') {
                            throw uriError();
                        }
                        B = unHex(str.charAt(k + 1), str.charAt(k + 2));
                        if (B < 0 || (B & 192) != 128) {
                            throw uriError();
                        }
                        ucs4Char = (ucs4Char << 6) | (B & 63);
                        k += 3;
                    }
                    if (ucs4Char < minUcs4Char || (ucs4Char >= 55296 && ucs4Char <= 57343)) {
                        ucs4Char = INVALID_UTF8;
                    } else if (ucs4Char == 65534 || ucs4Char == 65535) {
                        ucs4Char = 65533;
                    }
                    if (ucs4Char >= 65536) {
                        ucs4Char -= 65536;
                        if (ucs4Char > 1048575) {
                            throw uriError();
                        }
                        C = (char) ((ucs4Char & 1023) + 56320);
                        bufTop2 = bufTop + 1;
                        buf[bufTop] = (char) ((ucs4Char >>> 10) + 55296);
                        bufTop = bufTop2;
                    } else {
                        C = (char) ucs4Char;
                    }
                }
                if (!fullUri || URI_DECODE_RESERVED.indexOf(C) < 0) {
                    bufTop2 = bufTop + 1;
                    buf[bufTop] = C;
                } else {
                    int x = start;
                    while (x != k) {
                        bufTop2 = bufTop + 1;
                        buf[bufTop] = str.charAt(x);
                        x++;
                        bufTop = bufTop2;
                    }
                    bufTop2 = bufTop;
                }
            }
            bufTop = bufTop2;
        }
        return buf == null ? str : new String(buf, 0, bufTop);
    }

    private static boolean encodeUnescaped(char c, boolean fullUri) {
        if ('A' <= c && c <= 'Z') {
            return true;
        }
        if ('a' <= c && c <= 'z') {
            return true;
        }
        if (('0' <= c && c <= '9') || "-_.!~*'()".indexOf(c) >= 0) {
            return true;
        }
        if (!fullUri) {
            return false;
        }
        if (URI_DECODE_RESERVED.indexOf(c) < 0) {
            return false;
        }
        return true;
    }

    private static EcmaError uriError() {
        return ScriptRuntime.constructError("URIError", ScriptRuntime.getMessage0("msg.bad.uri"));
    }

    private static int oneUcs4ToUtf8Char(byte[] utf8Buffer, int ucs4Char) {
        int utf8Length = 1;
        if ((ucs4Char & -128) == 0) {
            utf8Buffer[0] = (byte) ucs4Char;
        } else {
            int a = ucs4Char >>> 11;
            utf8Length = 2;
            while (a != 0) {
                a >>>= 5;
                utf8Length++;
            }
            int i = utf8Length;
            while (true) {
                i--;
                if (i <= 0) {
                    break;
                }
                utf8Buffer[i] = (byte) ((ucs4Char & 63) | 128);
                ucs4Char >>>= 6;
            }
            utf8Buffer[0] = (byte) ((256 - (1 << (8 - utf8Length))) + ucs4Char);
        }
        return utf8Length;
    }
}
