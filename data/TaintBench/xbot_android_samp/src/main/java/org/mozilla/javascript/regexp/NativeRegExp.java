package org.mozilla.javascript.regexp;

import org.mozilla.classfile.ByteCode;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.TopLevel.Builtins;
import org.mozilla.javascript.Undefined;
import org.objectweb.asm.signature.SignatureVisitor;

public class NativeRegExp extends IdScriptableObject implements Function {
    static final /* synthetic */ boolean $assertionsDisabled = (!NativeRegExp.class.desiredAssertionStatus());
    private static final int ANCHOR_BOL = -2;
    private static final int INDEX_LEN = 2;
    private static final int Id_compile = 1;
    private static final int Id_exec = 4;
    private static final int Id_global = 3;
    private static final int Id_ignoreCase = 4;
    private static final int Id_lastIndex = 1;
    private static final int Id_multiline = 5;
    private static final int Id_prefix = 6;
    private static final int Id_source = 2;
    private static final int Id_test = 5;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    public static final int JSREG_FOLD = 2;
    public static final int JSREG_GLOB = 1;
    public static final int JSREG_MULTILINE = 4;
    public static final int MATCH = 1;
    private static final int MAX_INSTANCE_ID = 5;
    private static final int MAX_PROTOTYPE_ID = 6;
    public static final int PREFIX = 2;
    private static final Object REGEXP_TAG = new Object();
    private static final byte REOP_ALNUM = (byte) 9;
    private static final byte REOP_ALT = (byte) 31;
    private static final byte REOP_ALTPREREQ = (byte) 53;
    private static final byte REOP_ALTPREREQ2 = (byte) 55;
    private static final byte REOP_ALTPREREQi = (byte) 54;
    private static final byte REOP_ASSERT = (byte) 41;
    private static final byte REOP_ASSERTNOTTEST = (byte) 44;
    private static final byte REOP_ASSERTTEST = (byte) 43;
    private static final byte REOP_ASSERT_NOT = (byte) 42;
    private static final byte REOP_BACKREF = (byte) 13;
    private static final byte REOP_BOL = (byte) 2;
    private static final byte REOP_CLASS = (byte) 22;
    private static final byte REOP_DIGIT = (byte) 7;
    private static final byte REOP_DOT = (byte) 6;
    private static final byte REOP_EMPTY = (byte) 1;
    private static final byte REOP_END = (byte) 57;
    private static final byte REOP_ENDCHILD = (byte) 49;
    private static final byte REOP_EOL = (byte) 3;
    private static final byte REOP_FLAT = (byte) 14;
    private static final byte REOP_FLAT1 = (byte) 15;
    private static final byte REOP_FLAT1i = (byte) 17;
    private static final byte REOP_FLATi = (byte) 16;
    private static final byte REOP_JUMP = (byte) 32;
    private static final byte REOP_LPAREN = (byte) 29;
    private static final byte REOP_MINIMALOPT = (byte) 47;
    private static final byte REOP_MINIMALPLUS = (byte) 46;
    private static final byte REOP_MINIMALQUANT = (byte) 48;
    private static final byte REOP_MINIMALREPEAT = (byte) 52;
    private static final byte REOP_MINIMALSTAR = (byte) 45;
    private static final byte REOP_NCLASS = (byte) 23;
    private static final byte REOP_NONALNUM = (byte) 10;
    private static final byte REOP_NONDIGIT = (byte) 8;
    private static final byte REOP_NONSPACE = (byte) 12;
    private static final byte REOP_OPT = (byte) 28;
    private static final byte REOP_PLUS = (byte) 27;
    private static final byte REOP_QUANT = (byte) 25;
    private static final byte REOP_REPEAT = (byte) 51;
    private static final byte REOP_RPAREN = (byte) 30;
    private static final byte REOP_SIMPLE_END = (byte) 23;
    private static final byte REOP_SIMPLE_START = (byte) 1;
    private static final byte REOP_SPACE = (byte) 11;
    private static final byte REOP_STAR = (byte) 26;
    private static final byte REOP_UCFLAT1 = (byte) 18;
    private static final byte REOP_UCFLAT1i = (byte) 19;
    private static final byte REOP_WBDRY = (byte) 4;
    private static final byte REOP_WNONBDRY = (byte) 5;
    public static final int TEST = 0;
    private static final boolean debug = false;
    static final long serialVersionUID = 4965263491464903264L;
    Object lastIndex = Double.valueOf(0.0d);
    private int lastIndexAttr = 6;
    private RECompiled re;

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        NativeRegExp proto = new NativeRegExp();
        proto.re = compileRE(cx, "", null, false);
        proto.activatePrototypeMap(6);
        proto.setParentScope(scope);
        proto.setPrototype(ScriptableObject.getObjectPrototype(scope));
        NativeRegExpCtor ctor = new NativeRegExpCtor();
        proto.defineProperty("constructor", (Object) ctor, 2);
        ScriptRuntime.setFunctionProtoAndParent(ctor, scope);
        ctor.setImmunePrototypeProperty(proto);
        if (sealed) {
            proto.sealObject();
            ctor.sealObject();
        }
        ScriptableObject.defineProperty(scope, "RegExp", ctor, 2);
    }

    NativeRegExp(Scriptable scope, RECompiled regexpCompiled) {
        this.re = regexpCompiled;
        this.lastIndex = Double.valueOf(0.0d);
        ScriptRuntime.setBuiltinProtoAndParent(this, scope, Builtins.RegExp);
    }

    public String getClassName() {
        return "RegExp";
    }

    public String getTypeOf() {
        return "object";
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return execSub(cx, scope, args, 1);
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        return (Scriptable) execSub(cx, scope, args, 1);
    }

    /* access modifiers changed from: 0000 */
    public Scriptable compile(Context cx, Scriptable scope, Object[] args) {
        if (args.length <= 0 || !(args[0] instanceof NativeRegExp)) {
            String s = (args.length == 0 || (args[0] instanceof Undefined)) ? "" : escapeRegExp(args[0]);
            String global = (args.length <= 1 || args[1] == Undefined.instance) ? null : ScriptRuntime.toString(args[1]);
            this.re = compileRE(cx, s, global, false);
            this.lastIndex = Double.valueOf(0.0d);
        } else if (args.length <= 1 || args[1] == Undefined.instance) {
            NativeRegExp thatObj = args[0];
            this.re = thatObj.re;
            this.lastIndex = thatObj.lastIndex;
        } else {
            throw ScriptRuntime.typeError0("msg.bad.regexp.compile");
        }
        return this;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('/');
        if (this.re.source.length != 0) {
            buf.append(this.re.source);
        } else {
            buf.append("(?:)");
        }
        buf.append('/');
        if ((this.re.flags & 1) != 0) {
            buf.append('g');
        }
        if ((this.re.flags & 2) != 0) {
            buf.append('i');
        }
        if ((this.re.flags & 4) != 0) {
            buf.append('m');
        }
        return buf.toString();
    }

    NativeRegExp() {
    }

    private static RegExpImpl getImpl(Context cx) {
        return (RegExpImpl) ScriptRuntime.getRegExpProxy(cx);
    }

    private static String escapeRegExp(Object src) {
        String s = ScriptRuntime.toString(src);
        StringBuilder sb = null;
        int start = 0;
        int slash = s.indexOf(47);
        while (slash > -1) {
            if (slash == start || s.charAt(slash - 1) != '\\') {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(s, start, slash);
                sb.append("\\/");
                start = slash + 1;
            }
            slash = s.indexOf(47, slash + 1);
        }
        if (sb == null) {
            return s;
        }
        sb.append(s, start, s.length());
        return sb.toString();
    }

    private Object execSub(Context cx, Scriptable scopeObj, Object[] args, int matchType) {
        String str;
        RegExpImpl reImpl = getImpl(cx);
        if (args.length == 0) {
            str = reImpl.input;
            if (str == null) {
                str = ScriptRuntime.toString(Undefined.instance);
            }
        } else {
            str = ScriptRuntime.toString(args[0]);
        }
        double d = 0.0d;
        if ((this.re.flags & 1) != 0) {
            d = ScriptRuntime.toInteger(this.lastIndex);
        }
        if (d < 0.0d || ((double) str.length()) < d) {
            this.lastIndex = Double.valueOf(0.0d);
            return null;
        }
        int[] indexp = new int[]{(int) d};
        Object rval = executeRegExp(cx, scopeObj, reImpl, str, indexp, matchType);
        if ((this.re.flags & 1) == 0) {
            return rval;
        }
        double d2 = (rval == null || rval == Undefined.instance) ? 0.0d : (double) indexp[0];
        this.lastIndex = Double.valueOf(d2);
        return rval;
    }

    static RECompiled compileRE(Context cx, String str, String global, boolean flat) {
        RECompiled regexp = new RECompiled(str);
        int length = str.length();
        int flags = 0;
        if (global != null) {
            for (int i = 0; i < global.length(); i++) {
                char c = global.charAt(i);
                int f = 0;
                if (c == 'g') {
                    f = 1;
                } else if (c == 'i') {
                    f = 2;
                } else if (c == 'm') {
                    f = 4;
                } else {
                    reportError("msg.invalid.re.flag", String.valueOf(c));
                }
                if ((flags & f) != 0) {
                    reportError("msg.invalid.re.flag", String.valueOf(c));
                }
                flags |= f;
            }
        }
        regexp.flags = flags;
        CompilerState state = new CompilerState(cx, regexp.source, length, flags);
        if (flat && length > 0) {
            state.result = new RENode(REOP_FLAT);
            state.result.chr = state.cpbegin[0];
            state.result.length = length;
            state.result.flatIndex = 0;
            state.progLength += 5;
        } else if (!parseDisjunction(state)) {
            return null;
        } else {
            if (state.maxBackReference > state.parenCount) {
                state = new CompilerState(cx, regexp.source, length, flags);
                state.backReferenceLimit = state.parenCount;
                if (!parseDisjunction(state)) {
                    return null;
                }
            }
        }
        regexp.program = new byte[(state.progLength + 1)];
        if (state.classCount != 0) {
            regexp.classList = new RECharSet[state.classCount];
            regexp.classCount = state.classCount;
        }
        int emitREBytecode = emitREBytecode(state, regexp, 0, state.result);
        int endPC = emitREBytecode + 1;
        regexp.program[emitREBytecode] = REOP_END;
        regexp.parenCount = state.parenCount;
        switch (regexp.program[0]) {
            case (byte) 2:
                regexp.anchorCh = -2;
                return regexp;
            case (byte) 14:
            case (byte) 16:
                regexp.anchorCh = regexp.source[getIndex(regexp.program, 1)];
                return regexp;
            case (byte) 15:
            case (byte) 17:
                regexp.anchorCh = (char) (regexp.program[1] & ByteCode.IMPDEP2);
                return regexp;
            case (byte) 18:
            case (byte) 19:
                regexp.anchorCh = (char) getIndex(regexp.program, 1);
                return regexp;
            case (byte) 31:
                RENode n = state.result;
                if (n.kid.op != REOP_BOL || n.kid2.op != REOP_BOL) {
                    return regexp;
                }
                regexp.anchorCh = -2;
                return regexp;
            default:
                return regexp;
        }
    }

    static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private static boolean isWord(char c) {
        return ('a' <= c && c <= 'z') || (('A' <= c && c <= 'Z') || isDigit(c) || c == '_');
    }

    private static boolean isControlLetter(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }

    private static boolean isLineTerm(char c) {
        return ScriptRuntime.isJSLineTerminator(c);
    }

    private static boolean isREWhiteSpace(int c) {
        return ScriptRuntime.isJSWhitespaceOrLineTerminator(c);
    }

    private static char upcase(char ch) {
        if (ch >= 128) {
            char cu = Character.toUpperCase(ch);
            return cu >= 128 ? cu : ch;
        } else if ('a' > ch || ch > 'z') {
            return ch;
        } else {
            return (char) (ch - 32);
        }
    }

    private static char downcase(char ch) {
        if (ch >= 128) {
            char cl = Character.toLowerCase(ch);
            return cl >= 128 ? cl : ch;
        } else if ('A' > ch || ch > 'Z') {
            return ch;
        } else {
            return (char) (ch + 32);
        }
    }

    private static int toASCIIHexDigit(int c) {
        if (c < 48) {
            return -1;
        }
        if (c <= 57) {
            return c - 48;
        }
        c |= 32;
        if (97 > c || c > 102) {
            return -1;
        }
        return (c - 97) + 10;
    }

    private static boolean parseDisjunction(CompilerState state) {
        if (!parseAlternative(state)) {
            return false;
        }
        char[] source = state.cpbegin;
        int index = state.cp;
        if (index != source.length && source[index] == '|') {
            state.cp++;
            RENode result = new RENode(REOP_ALT);
            result.kid = state.result;
            if (!parseDisjunction(state)) {
                return false;
            }
            result.kid2 = state.result;
            state.result = result;
            if (result.kid.op == REOP_FLAT && result.kid2.op == REOP_FLAT) {
                result.op = (state.flags & 2) == 0 ? REOP_ALTPREREQ : REOP_ALTPREREQi;
                result.chr = result.kid.chr;
                result.index = result.kid2.chr;
                state.progLength += 13;
            } else if (result.kid.op == REOP_CLASS && result.kid.index < 256 && result.kid2.op == REOP_FLAT && (state.flags & 2) == 0) {
                result.op = REOP_ALTPREREQ2;
                result.chr = result.kid2.chr;
                result.index = result.kid.index;
                state.progLength += 13;
            } else if (result.kid.op == REOP_FLAT && result.kid2.op == REOP_CLASS && result.kid2.index < 256 && (state.flags & 2) == 0) {
                result.op = REOP_ALTPREREQ2;
                result.chr = result.kid.chr;
                result.index = result.kid2.index;
                state.progLength += 13;
            } else {
                state.progLength += 9;
            }
        }
        return true;
    }

    private static boolean parseAlternative(CompilerState state) {
        RENode headTerm = null;
        RENode tailTerm = null;
        char[] source = state.cpbegin;
        while (state.cp != state.cpend && source[state.cp] != '|' && (state.parenNesting == 0 || source[state.cp] != ')')) {
            if (!parseTerm(state)) {
                return false;
            }
            if (headTerm == null) {
                headTerm = state.result;
                tailTerm = headTerm;
            } else {
                tailTerm.next = state.result;
            }
            while (tailTerm.next != null) {
                tailTerm = tailTerm.next;
            }
        }
        if (headTerm == null) {
            state.result = new RENode((byte) 1);
            return true;
        }
        state.result = headTerm;
        return true;
    }

    private static boolean calculateBitmapSize(CompilerState state, RENode target, char[] src, int index, int end) {
        char rangeStart = 0;
        int max = 0;
        boolean inRange = false;
        target.bmsize = 0;
        target.sense = true;
        if (index == end) {
            return true;
        }
        if (src[index] == '^') {
            index++;
            target.sense = false;
        }
        while (true) {
            int index2 = index;
            if (index2 != end) {
                int localMax;
                char localMax2;
                int nDigits = 2;
                switch (src[index2]) {
                    case '\\':
                        int n;
                        int i;
                        index = index2 + 1;
                        index2 = index + 1;
                        char c = src[index];
                        switch (c) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                                n = c - 48;
                                c = src[index2];
                                if ('0' > c || c > '7') {
                                    index = index2;
                                } else {
                                    index = index2 + 1;
                                    n = (n * 8) + (c - 48);
                                    c = src[index];
                                    if ('0' <= c && c <= '7') {
                                        index++;
                                        i = (n * 8) + (c - 48);
                                        if (i <= ByteCode.IMPDEP2) {
                                            n = i;
                                        } else {
                                            index--;
                                        }
                                    }
                                }
                                localMax2 = n;
                                break;
                            case 'D':
                            case 'S':
                            case 'W':
                            case 's':
                            case 'w':
                                if (inRange) {
                                    reportError("msg.bad.range", "");
                                    index = index2;
                                    return false;
                                }
                                target.bmsize = 65536;
                                index = index2;
                                return true;
                            case 'b':
                                localMax2 = 8;
                                index = index2;
                                break;
                            case 'c':
                                if (index2 >= end || !isControlLetter(src[index2])) {
                                    index = index2 - 1;
                                } else {
                                    index = index2 + 1;
                                    localMax2 = (char) (src[index2] & 31);
                                }
                                localMax2 = 92;
                                break;
                            case 'd':
                                if (!inRange) {
                                    localMax2 = 57;
                                    index = index2;
                                    break;
                                }
                                reportError("msg.bad.range", "");
                                index = index2;
                                return false;
                            case 'f':
                                localMax2 = 12;
                                index = index2;
                                break;
                            case 'n':
                                localMax2 = 10;
                                index = index2;
                                break;
                            case 'r':
                                localMax2 = 13;
                                index = index2;
                                break;
                            case 't':
                                localMax2 = 9;
                                index = index2;
                                break;
                            case 'u':
                                nDigits = 2 + 2;
                                break;
                            case 'v':
                                localMax2 = 11;
                                index = index2;
                                break;
                            case 'x':
                                break;
                            default:
                                localMax2 = c;
                                index = index2;
                                break;
                        }
                        n = 0;
                        i = 0;
                        while (i < nDigits && index2 < end) {
                            index = index2 + 1;
                            n = Kit.xDigitToInt(src[index2], n);
                            if (n < 0) {
                                index -= i + 1;
                                n = 92;
                                localMax2 = n;
                                break;
                            }
                            i++;
                            index2 = index;
                        }
                        index = index2;
                        localMax2 = n;
                        break;
                    default:
                        index = index2 + 1;
                        localMax2 = src[index2];
                        break;
                }
                if (inRange) {
                    if (rangeStart > localMax2) {
                        reportError("msg.bad.range", "");
                        return false;
                    }
                    inRange = false;
                } else if (index < end - 1 && src[index] == SignatureVisitor.SUPER) {
                    index++;
                    inRange = true;
                    rangeStart = (char) localMax2;
                }
                if ((state.flags & 2) != 0) {
                    char cu = upcase((char) localMax2);
                    char cd = downcase((char) localMax2);
                    if (cu >= cd) {
                        localMax2 = cu;
                    } else {
                        localMax2 = cd;
                    }
                }
                if (localMax2 > max) {
                    max = localMax2;
                }
            } else {
                target.bmsize = max + 1;
                index = index2;
                return true;
            }
        }
    }

    private static void doFlat(CompilerState state, char c) {
        state.result = new RENode(REOP_FLAT);
        state.result.chr = c;
        state.result.length = 1;
        state.result.flatIndex = -1;
        state.progLength += 3;
    }

    private static int getDecimalValue(char c, CompilerState state, int maxValue, String overflowMessageId) {
        boolean overflow = false;
        int start = state.cp;
        char[] src = state.cpbegin;
        int value = c - 48;
        while (state.cp != state.cpend) {
            c = src[state.cp];
            if (!isDigit(c)) {
                break;
            }
            if (!overflow) {
                int v = (value * 10) + (c - 48);
                if (v < maxValue) {
                    value = v;
                } else {
                    overflow = true;
                    value = maxValue;
                }
            }
            state.cp++;
        }
        if (overflow) {
            reportError(overflowMessageId, String.valueOf(src, start, state.cp - start));
        }
        return value;
    }

    /* JADX WARNING: Removed duplicated region for block: B:97:0x04fa  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x04ae  */
    private static boolean parseTerm(org.mozilla.javascript.regexp.CompilerState r21) {
        /*
        r0 = r21;
        r15 = r0.cpbegin;
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r19 = r18 + 1;
        r0 = r19;
        r1 = r21;
        r1.cp = r0;
        r4 = r15[r18];
        r11 = 2;
        r0 = r21;
        r13 = r0.parenCount;
        switch(r4) {
            case 36: goto L_0x009c;
            case 40: goto L_0x0428;
            case 41: goto L_0x0555;
            case 42: goto L_0x065a;
            case 43: goto L_0x065a;
            case 46: goto L_0x063d;
            case 63: goto L_0x065a;
            case 91: goto L_0x0560;
            case 92: goto L_0x00ba;
            case 94: goto L_0x007e;
            default: goto L_0x001c;
        };
    L_0x001c:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 14;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r18;
        r0.chr = r4;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 1;
        r0 = r19;
        r1 = r18;
        r1.length = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r21;
        r0 = r0.cp;
        r19 = r0;
        r19 = r19 + -1;
        r0 = r19;
        r1 = r18;
        r1.flatIndex = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 3;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
    L_0x0063:
        r0 = r21;
        r0 = r0.result;
        r16 = r0;
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 != r1) goto L_0x0671;
    L_0x007b:
        r18 = 1;
    L_0x007d:
        return r18;
    L_0x007e:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 2;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r18 = 1;
        goto L_0x007d;
    L_0x009c:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 3;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r18 = 1;
        goto L_0x007d;
    L_0x00ba:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x041d;
    L_0x00cc:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r19 = r18 + 1;
        r0 = r19;
        r1 = r21;
        r1.cp = r0;
        r4 = r15[r18];
        switch(r4) {
            case 48: goto L_0x0166;
            case 49: goto L_0x01bd;
            case 50: goto L_0x01bd;
            case 51: goto L_0x01bd;
            case 52: goto L_0x01bd;
            case 53: goto L_0x01bd;
            case 54: goto L_0x01bd;
            case 55: goto L_0x01bd;
            case 56: goto L_0x01bd;
            case 57: goto L_0x01bd;
            case 66: goto L_0x0147;
            case 68: goto L_0x038c;
            case 83: goto L_0x03c6;
            case 87: goto L_0x0400;
            case 98: goto L_0x0128;
            case 99: goto L_0x02c9;
            case 100: goto L_0x036f;
            case 102: goto L_0x029c;
            case 110: goto L_0x02a5;
            case 114: goto L_0x02ae;
            case 115: goto L_0x03a9;
            case 116: goto L_0x02b7;
            case 117: goto L_0x0316;
            case 118: goto L_0x02c0;
            case 119: goto L_0x03e3;
            case 120: goto L_0x0318;
            default: goto L_0x00df;
        };
    L_0x00df:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 14;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r18;
        r0.chr = r4;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 1;
        r0 = r19;
        r1 = r18;
        r1.length = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r21;
        r0 = r0.cp;
        r19 = r0;
        r19 = r19 + -1;
        r0 = r19;
        r1 = r18;
        r1.flatIndex = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 3;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x0128:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 4;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r18 = 1;
        goto L_0x007d;
    L_0x0147:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 5;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r18 = 1;
        goto L_0x007d;
    L_0x0166:
        r0 = r21;
        r0 = r0.cx;
        r18 = r0;
        r19 = "msg.bad.backref";
        r20 = "";
        reportWarning(r18, r19, r20);
        r12 = 0;
    L_0x0174:
        r18 = 32;
        r0 = r18;
        if (r12 >= r0) goto L_0x01b5;
    L_0x017a:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x01b5;
    L_0x018c:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r4 = r15[r18];
        r18 = 48;
        r0 = r18;
        if (r4 < r0) goto L_0x01b5;
    L_0x019a:
        r18 = 55;
        r0 = r18;
        if (r4 > r0) goto L_0x01b5;
    L_0x01a0:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r18 = r12 * 8;
        r19 = r4 + -48;
        r12 = r18 + r19;
        goto L_0x0174;
    L_0x01b5:
        r4 = (char) r12;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x01bd:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r17 = r18 + -1;
        r18 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r19 = "msg.overlarge.backref";
        r0 = r21;
        r1 = r18;
        r2 = r19;
        r12 = getDecimalValue(r4, r0, r1, r2);
        r0 = r21;
        r0 = r0.backReferenceLimit;
        r18 = r0;
        r0 = r18;
        if (r12 <= r0) goto L_0x01eb;
    L_0x01de:
        r0 = r21;
        r0 = r0.cx;
        r18 = r0;
        r19 = "msg.bad.backref";
        r20 = "";
        reportWarning(r18, r19, r20);
    L_0x01eb:
        r0 = r21;
        r0 = r0.backReferenceLimit;
        r18 = r0;
        r0 = r18;
        if (r12 <= r0) goto L_0x0263;
    L_0x01f5:
        r0 = r17;
        r1 = r21;
        r1.cp = r0;
        r18 = 56;
        r0 = r18;
        if (r4 < r0) goto L_0x020a;
    L_0x0201:
        r4 = 92;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x020a:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r12 = r4 + -48;
    L_0x021a:
        r18 = 32;
        r0 = r18;
        if (r12 >= r0) goto L_0x025b;
    L_0x0220:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x025b;
    L_0x0232:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r4 = r15[r18];
        r18 = 48;
        r0 = r18;
        if (r4 < r0) goto L_0x025b;
    L_0x0240:
        r18 = 55;
        r0 = r18;
        if (r4 > r0) goto L_0x025b;
    L_0x0246:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r18 = r12 * 8;
        r19 = r4 + -48;
        r12 = r18 + r19;
        goto L_0x021a;
    L_0x025b:
        r4 = (char) r12;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x0263:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 13;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = r12 + -1;
        r0 = r19;
        r1 = r18;
        r1.parenIndex = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 3;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r0 = r21;
        r0 = r0.maxBackReference;
        r18 = r0;
        r0 = r18;
        if (r0 >= r12) goto L_0x0063;
    L_0x0296:
        r0 = r21;
        r0.maxBackReference = r12;
        goto L_0x0063;
    L_0x029c:
        r4 = 12;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x02a5:
        r4 = 10;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x02ae:
        r4 = 13;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x02b7:
        r4 = 9;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x02c0:
        r4 = 11;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x02c9:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x0305;
    L_0x02db:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r15[r18];
        r18 = isControlLetter(r18);
        if (r18 == 0) goto L_0x0305;
    L_0x02e9:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r19 = r18 + 1;
        r0 = r19;
        r1 = r21;
        r1.cp = r0;
        r18 = r15[r18];
        r18 = r18 & 31;
        r0 = r18;
        r4 = (char) r0;
    L_0x02fe:
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x0305:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + -1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r4 = 92;
        goto L_0x02fe;
    L_0x0316:
        r11 = r11 + 2;
    L_0x0318:
        r10 = 0;
        r6 = 0;
    L_0x031a:
        if (r6 >= r11) goto L_0x0364;
    L_0x031c:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x0364;
    L_0x032e:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r19 = r18 + 1;
        r0 = r19;
        r1 = r21;
        r1.cp = r0;
        r4 = r15[r18];
        r10 = org.mozilla.javascript.Kit.xDigitToInt(r4, r10);
        if (r10 >= 0) goto L_0x036c;
    L_0x0344:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r19 = r6 + 2;
        r18 = r18 - r19;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r19 = r18 + 1;
        r0 = r19;
        r1 = r21;
        r1.cp = r0;
        r10 = r15[r18];
    L_0x0364:
        r4 = (char) r10;
        r0 = r21;
        doFlat(r0, r4);
        goto L_0x0063;
    L_0x036c:
        r6 = r6 + 1;
        goto L_0x031a;
    L_0x036f:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 7;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x038c:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 8;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x03a9:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 11;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x03c6:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 12;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x03e3:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 9;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x0400:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 10;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x041d:
        r18 = "msg.trail.backslash";
        r19 = "";
        reportError(r18, r19);
        r18 = 0;
        goto L_0x007d;
    L_0x0428:
        r14 = 0;
        r0 = r21;
        r0 = r0.cp;
        r17 = r0;
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x04d0;
    L_0x0443:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r15[r18];
        r19 = 63;
        r0 = r18;
        r1 = r19;
        if (r0 != r1) goto L_0x04d0;
    L_0x0453:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r4 = r15[r18];
        r18 = 61;
        r0 = r18;
        if (r4 == r0) goto L_0x046f;
    L_0x0463:
        r18 = 33;
        r0 = r18;
        if (r4 == r0) goto L_0x046f;
    L_0x0469:
        r18 = 58;
        r0 = r18;
        if (r4 != r0) goto L_0x04d0;
    L_0x046f:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 2;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r18 = 61;
        r0 = r18;
        if (r4 != r0) goto L_0x04b2;
    L_0x0483:
        r14 = new org.mozilla.javascript.regexp.RENode;
        r18 = 41;
        r0 = r18;
        r14.m528init(r0);
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 4;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
    L_0x049a:
        r0 = r21;
        r0 = r0.parenNesting;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.parenNesting = r0;
        r18 = parseDisjunction(r21);
        if (r18 != 0) goto L_0x04fa;
    L_0x04ae:
        r18 = 0;
        goto L_0x007d;
    L_0x04b2:
        r18 = 33;
        r0 = r18;
        if (r4 != r0) goto L_0x049a;
    L_0x04b8:
        r14 = new org.mozilla.javascript.regexp.RENode;
        r18 = 42;
        r0 = r18;
        r14.m528init(r0);
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 4;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x049a;
    L_0x04d0:
        r14 = new org.mozilla.javascript.regexp.RENode;
        r18 = 29;
        r0 = r18;
        r14.m528init(r0);
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 6;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r0 = r21;
        r0 = r0.parenCount;
        r18 = r0;
        r19 = r18 + 1;
        r0 = r19;
        r1 = r21;
        r1.parenCount = r0;
        r0 = r18;
        r14.parenIndex = r0;
        goto L_0x049a;
    L_0x04fa:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 == r1) goto L_0x051c;
    L_0x050c:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r15[r18];
        r19 = 41;
        r0 = r18;
        r1 = r19;
        if (r0 == r1) goto L_0x0527;
    L_0x051c:
        r18 = "msg.unterm.paren";
        r19 = "";
        reportError(r18, r19);
        r18 = 0;
        goto L_0x007d;
    L_0x0527:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r0 = r21;
        r0 = r0.parenNesting;
        r18 = r0;
        r18 = r18 + -1;
        r0 = r18;
        r1 = r21;
        r1.parenNesting = r0;
        if (r14 == 0) goto L_0x0063;
    L_0x0545:
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r18;
        r14.kid = r0;
        r0 = r21;
        r0.result = r14;
        goto L_0x0063;
    L_0x0555:
        r18 = "msg.re.unmatched.right.paren";
        r19 = "";
        reportError(r18, r19);
        r18 = 0;
        goto L_0x007d;
    L_0x0560:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 22;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.cp;
        r17 = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r17;
        r1 = r18;
        r1.startIndex = r0;
    L_0x057f:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 != r1) goto L_0x059c;
    L_0x0591:
        r18 = "msg.unterm.class";
        r19 = "";
        reportError(r18, r19);
        r18 = 0;
        goto L_0x007d;
    L_0x059c:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r15[r18];
        r19 = 92;
        r0 = r18;
        r1 = r19;
        if (r0 != r1) goto L_0x05c9;
    L_0x05ac:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
    L_0x05ba:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        goto L_0x057f;
    L_0x05c9:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r15[r18];
        r19 = 93;
        r0 = r18;
        r1 = r19;
        if (r0 != r1) goto L_0x05ba;
    L_0x05d9:
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r21;
        r0 = r0.cp;
        r19 = r0;
        r19 = r19 - r17;
        r0 = r19;
        r1 = r18;
        r1.kidlen = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r21;
        r0 = r0.classCount;
        r19 = r0;
        r20 = r19 + 1;
        r0 = r20;
        r1 = r21;
        r1.classCount = r0;
        r0 = r19;
        r1 = r18;
        r1.index = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r21;
        r0 = r0.cp;
        r19 = r0;
        r20 = r19 + 1;
        r0 = r20;
        r1 = r21;
        r1.cp = r0;
        r0 = r21;
        r1 = r18;
        r2 = r17;
        r3 = r19;
        r18 = calculateBitmapSize(r0, r1, r15, r2, r3);
        if (r18 != 0) goto L_0x062d;
    L_0x0629:
        r18 = 0;
        goto L_0x007d;
    L_0x062d:
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 3;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x063d:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 6;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        goto L_0x0063;
    L_0x065a:
        r18 = "msg.bad.quant";
        r0 = r21;
        r0 = r0.cp;
        r19 = r0;
        r19 = r19 + -1;
        r19 = r15[r19];
        r19 = java.lang.String.valueOf(r19);
        reportError(r18, r19);
        r18 = 0;
        goto L_0x007d;
    L_0x0671:
        r5 = 0;
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r15[r18];
        switch(r18) {
            case 42: goto L_0x06bc;
            case 43: goto L_0x0683;
            case 63: goto L_0x06f5;
            case 123: goto L_0x072f;
            default: goto L_0x067d;
        };
    L_0x067d:
        if (r5 != 0) goto L_0x0816;
    L_0x067f:
        r18 = 1;
        goto L_0x007d;
    L_0x0683:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 25;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 1;
        r0 = r19;
        r1 = r18;
        r1.min = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = -1;
        r0 = r19;
        r1 = r18;
        r1.max = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 8;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r5 = 1;
        goto L_0x067d;
    L_0x06bc:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 25;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 0;
        r0 = r19;
        r1 = r18;
        r1.min = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = -1;
        r0 = r19;
        r1 = r18;
        r1.max = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 8;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r5 = 1;
        goto L_0x067d;
    L_0x06f5:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 25;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 0;
        r0 = r19;
        r1 = r18;
        r1.min = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 1;
        r0 = r19;
        r1 = r18;
        r1.max = r0;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 8;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r5 = 1;
        goto L_0x067d;
    L_0x072f:
        r9 = 0;
        r8 = -1;
        r0 = r21;
        r7 = r0.cp;
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r0 = r15.length;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x080e;
    L_0x074c:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r4 = r15[r18];
        r18 = isDigit(r4);
        if (r18 == 0) goto L_0x080e;
    L_0x075a:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r18 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r19 = "msg.overlarge.min";
        r0 = r21;
        r1 = r18;
        r2 = r19;
        r9 = getDecimalValue(r4, r0, r1, r2);
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r4 = r15[r18];
        r18 = 44;
        r0 = r18;
        if (r4 != r0) goto L_0x07d7;
    L_0x0785:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r4 = r15[r18];
        r18 = isDigit(r4);
        if (r18 == 0) goto L_0x07d8;
    L_0x079b:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r18 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r19 = "msg.overlarge.max";
        r0 = r21;
        r1 = r18;
        r2 = r19;
        r8 = getDecimalValue(r4, r0, r1, r2);
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r4 = r15[r18];
        if (r9 <= r8) goto L_0x07d8;
    L_0x07c2:
        r18 = "msg.max.lt.min";
        r0 = r21;
        r0 = r0.cp;
        r19 = r0;
        r19 = r15[r19];
        r19 = java.lang.String.valueOf(r19);
        reportError(r18, r19);
        r18 = 0;
        goto L_0x007d;
    L_0x07d7:
        r8 = r9;
    L_0x07d8:
        r18 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r0 = r18;
        if (r4 != r0) goto L_0x080e;
    L_0x07de:
        r18 = new org.mozilla.javascript.regexp.RENode;
        r19 = 25;
        r18.m528init(r19);
        r0 = r18;
        r1 = r21;
        r1.result = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r18;
        r0.min = r9;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r18;
        r0.max = r8;
        r0 = r21;
        r0 = r0.progLength;
        r18 = r0;
        r18 = r18 + 12;
        r0 = r18;
        r1 = r21;
        r1.progLength = r0;
        r5 = 1;
    L_0x080e:
        if (r5 != 0) goto L_0x067d;
    L_0x0810:
        r0 = r21;
        r0.cp = r7;
        goto L_0x067d;
    L_0x0816:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r16;
        r1 = r18;
        r1.kid = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r18;
        r0.parenIndex = r13;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r0 = r21;
        r0 = r0.parenCount;
        r19 = r0;
        r19 = r19 - r13;
        r0 = r19;
        r1 = r18;
        r1.parenCount = r0;
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r0 = r21;
        r0 = r0.cpend;
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 >= r1) goto L_0x0890;
    L_0x0860:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r15[r18];
        r19 = 63;
        r0 = r18;
        r1 = r19;
        if (r0 != r1) goto L_0x0890;
    L_0x0870:
        r0 = r21;
        r0 = r0.cp;
        r18 = r0;
        r18 = r18 + 1;
        r0 = r18;
        r1 = r21;
        r1.cp = r0;
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 0;
        r0 = r19;
        r1 = r18;
        r1.greedy = r0;
    L_0x088c:
        r18 = 1;
        goto L_0x007d;
    L_0x0890:
        r0 = r21;
        r0 = r0.result;
        r18 = r0;
        r19 = 1;
        r0 = r19;
        r1 = r18;
        r1.greedy = r0;
        goto L_0x088c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.regexp.NativeRegExp.parseTerm(org.mozilla.javascript.regexp.CompilerState):boolean");
    }

    private static void resolveForwardJump(byte[] array, int from, int pc) {
        if (from > pc) {
            throw Kit.codeBug();
        }
        addIndex(array, from, pc - from);
    }

    private static int getOffset(byte[] array, int pc) {
        return getIndex(array, pc);
    }

    private static int addIndex(byte[] array, int pc, int index) {
        if (index < 0) {
            throw Kit.codeBug();
        } else if (index > 65535) {
            throw Context.reportRuntimeError("Too complex regexp");
        } else {
            array[pc] = (byte) (index >> 8);
            array[pc + 1] = (byte) index;
            return pc + 2;
        }
    }

    private static int getIndex(byte[] array, int pc) {
        return ((array[pc] & ByteCode.IMPDEP2) << 8) | (array[pc + 1] & ByteCode.IMPDEP2);
    }

    private static int emitREBytecode(CompilerState state, RECompiled re, int pc, RENode t) {
        byte[] program = re.program;
        int pc2 = pc;
        while (t != null) {
            int nextTermFixup;
            pc = pc2 + 1;
            program[pc2] = t.op;
            switch (t.op) {
                case (byte) 1:
                    pc--;
                    continue;
                case (byte) 13:
                    pc = addIndex(program, pc, t.parenIndex);
                    continue;
                case (byte) 14:
                    if (t.flatIndex != -1) {
                        while (t.next != null && t.next.op == REOP_FLAT && t.flatIndex + t.length == t.next.flatIndex) {
                            t.length += t.next.length;
                            t.next = t.next.next;
                        }
                    }
                    if (t.flatIndex == -1 || t.length <= 1) {
                        if (t.chr >= 256) {
                            if ((state.flags & 2) != 0) {
                                program[pc - 1] = REOP_UCFLAT1i;
                            } else {
                                program[pc - 1] = REOP_UCFLAT1;
                            }
                            pc = addIndex(program, pc, t.chr);
                            break;
                        }
                        if ((state.flags & 2) != 0) {
                            program[pc - 1] = REOP_FLAT1i;
                        } else {
                            program[pc - 1] = REOP_FLAT1;
                        }
                        pc2 = pc + 1;
                        program[pc] = (byte) t.chr;
                        pc = pc2;
                        break;
                    }
                    if ((state.flags & 2) != 0) {
                        program[pc - 1] = REOP_FLATi;
                    } else {
                        program[pc - 1] = REOP_FLAT;
                    }
                    pc = addIndex(program, addIndex(program, pc, t.flatIndex), t.length);
                    continue;
                case (byte) 22:
                    if (!t.sense) {
                        program[pc - 1] = (byte) 23;
                    }
                    pc = addIndex(program, pc, t.index);
                    re.classList[t.index] = new RECharSet(t.bmsize, t.startIndex, t.kidlen, t.sense);
                    continue;
                case (byte) 25:
                    if (t.min == 0 && t.max == -1) {
                        byte b;
                        int i = pc - 1;
                        if (t.greedy) {
                            b = REOP_STAR;
                        } else {
                            b = REOP_MINIMALSTAR;
                        }
                        program[i] = b;
                    } else if (t.min == 0 && t.max == 1) {
                        program[pc - 1] = t.greedy ? REOP_OPT : REOP_MINIMALOPT;
                    } else if (t.min == 1 && t.max == -1) {
                        program[pc - 1] = t.greedy ? REOP_PLUS : REOP_MINIMALPLUS;
                    } else {
                        if (!t.greedy) {
                            program[pc - 1] = REOP_MINIMALQUANT;
                        }
                        pc = addIndex(program, addIndex(program, pc, t.min), t.max + 1);
                    }
                    pc = addIndex(program, addIndex(program, pc, t.parenCount), t.parenIndex);
                    nextTermFixup = pc;
                    pc = emitREBytecode(state, re, pc + 2, t.kid);
                    pc2 = pc + 1;
                    program[pc] = REOP_ENDCHILD;
                    resolveForwardJump(program, nextTermFixup, pc2);
                    pc = pc2;
                    continue;
                case (byte) 29:
                    pc = emitREBytecode(state, re, addIndex(program, pc, t.parenIndex), t.kid);
                    pc2 = pc + 1;
                    program[pc] = REOP_RPAREN;
                    pc = addIndex(program, pc2, t.parenIndex);
                    continue;
                case (byte) 31:
                    break;
                case (byte) 41:
                    nextTermFixup = pc;
                    pc = emitREBytecode(state, re, pc + 2, t.kid);
                    pc2 = pc + 1;
                    program[pc] = REOP_ASSERTTEST;
                    resolveForwardJump(program, nextTermFixup, pc2);
                    pc = pc2;
                    continue;
                case (byte) 42:
                    nextTermFixup = pc;
                    pc = emitREBytecode(state, re, pc + 2, t.kid);
                    pc2 = pc + 1;
                    program[pc] = REOP_ASSERTNOTTEST;
                    resolveForwardJump(program, nextTermFixup, pc2);
                    pc = pc2;
                    continue;
                case (byte) 53:
                case (byte) 54:
                case (byte) 55:
                    int upcase;
                    boolean ignoreCase = t.op == REOP_ALTPREREQi;
                    addIndex(program, pc, ignoreCase ? upcase(t.chr) : t.chr);
                    pc += 2;
                    if (ignoreCase) {
                        upcase = upcase((char) t.index);
                    } else {
                        upcase = t.index;
                    }
                    addIndex(program, pc, upcase);
                    pc += 2;
                    break;
                default:
                    break;
            }
            RENode nextAlt = t.kid2;
            int nextAltFixup = pc;
            pc = emitREBytecode(state, re, pc + 2, t.kid);
            pc2 = pc + 1;
            program[pc] = REOP_JUMP;
            nextTermFixup = pc2;
            pc = pc2 + 2;
            resolveForwardJump(program, nextAltFixup, pc);
            pc = emitREBytecode(state, re, pc, nextAlt);
            pc2 = pc + 1;
            program[pc] = REOP_JUMP;
            nextAltFixup = pc2;
            pc = pc2 + 2;
            resolveForwardJump(program, nextTermFixup, pc);
            resolveForwardJump(program, nextAltFixup, pc);
            t = t.next;
            pc2 = pc;
        }
        return pc2;
    }

    private static void pushProgState(REGlobalData gData, int min, int max, int cp, REBackTrackData backTrackLastToSave, int continuationOp, int continuationPc) {
        gData.stateStackTop = new REProgState(gData.stateStackTop, min, max, cp, backTrackLastToSave, continuationOp, continuationPc);
    }

    private static REProgState popProgState(REGlobalData gData) {
        REProgState state = gData.stateStackTop;
        gData.stateStackTop = state.previous;
        return state;
    }

    private static void pushBackTrackState(REGlobalData gData, byte op, int pc) {
        REProgState state = gData.stateStackTop;
        gData.backTrackStackTop = new REBackTrackData(gData, op, pc, gData.cp, state.continuationOp, state.continuationPc);
    }

    private static void pushBackTrackState(REGlobalData gData, byte op, int pc, int cp, int continuationOp, int continuationPc) {
        gData.backTrackStackTop = new REBackTrackData(gData, op, pc, cp, continuationOp, continuationPc);
    }

    private static boolean flatNMatcher(REGlobalData gData, int matchChars, int length, String input, int end) {
        if (gData.cp + length > end) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (gData.regexp.source[matchChars + i] != input.charAt(gData.cp + i)) {
                return false;
            }
        }
        gData.cp += length;
        return true;
    }

    private static boolean flatNIMatcher(REGlobalData gData, int matchChars, int length, String input, int end) {
        if (gData.cp + length > end) {
            return false;
        }
        char[] source = gData.regexp.source;
        for (int i = 0; i < length; i++) {
            char c1 = source[matchChars + i];
            char c2 = input.charAt(gData.cp + i);
            if (c1 != c2 && upcase(c1) != upcase(c2)) {
                return false;
            }
        }
        gData.cp += length;
        return true;
    }

    private static boolean backrefMatcher(REGlobalData gData, int parenIndex, String input, int end) {
        if (gData.parens == null || parenIndex >= gData.parens.length) {
            return false;
        }
        int parenContent = gData.parensIndex(parenIndex);
        if (parenContent == -1) {
            return true;
        }
        int len = gData.parensLength(parenIndex);
        if (gData.cp + len > end) {
            return false;
        }
        if ((gData.regexp.flags & 2) != 0) {
            for (int i = 0; i < len; i++) {
                char c1 = input.charAt(parenContent + i);
                char c2 = input.charAt(gData.cp + i);
                if (c1 != c2 && upcase(c1) != upcase(c2)) {
                    return false;
                }
            }
        } else if (!input.regionMatches(parenContent, input, gData.cp, len)) {
            return false;
        }
        gData.cp += len;
        return true;
    }

    private static void addCharacterToCharSet(RECharSet cs, char c) {
        int byteIndex = c / 8;
        if (c >= cs.length) {
            throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
        }
        byte[] bArr = cs.bits;
        bArr[byteIndex] = (byte) (bArr[byteIndex] | (1 << (c & 7)));
    }

    private static void addCharacterRangeToCharSet(RECharSet cs, char c1, char c2) {
        int byteIndex1 = c1 / 8;
        int byteIndex2 = c2 / 8;
        if (c2 >= cs.length || c1 > c2) {
            throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
        }
        c1 = (char) (c1 & 7);
        c2 = (char) (c2 & 7);
        byte[] bArr;
        if (byteIndex1 == byteIndex2) {
            bArr = cs.bits;
            bArr[byteIndex1] = (byte) (bArr[byteIndex1] | ((ByteCode.IMPDEP2 >> (7 - (c2 - c1))) << c1));
            return;
        }
        bArr = cs.bits;
        bArr[byteIndex1] = (byte) (bArr[byteIndex1] | (ByteCode.IMPDEP2 << c1));
        for (int i = byteIndex1 + 1; i < byteIndex2; i++) {
            cs.bits[i] = (byte) -1;
        }
        bArr = cs.bits;
        bArr[byteIndex2] = (byte) (bArr[byteIndex2] | (ByteCode.IMPDEP2 >> (7 - c2)));
    }

    private static void processCharSet(REGlobalData gData, RECharSet charSet) {
        synchronized (charSet) {
            if (!charSet.converted) {
                processCharSetImpl(gData, charSet);
                charSet.converted = true;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0052  */
    private static void processCharSetImpl(org.mozilla.javascript.regexp.REGlobalData r19, org.mozilla.javascript.regexp.RECharSet r20) {
        /*
        r0 = r20;
        r13 = r0.startIndex;
        r0 = r20;
        r0 = r0.strlength;
        r17 = r0;
        r7 = r13 + r17;
        r12 = 0;
        r9 = 0;
        r0 = r20;
        r0 = r0.length;
        r17 = r0;
        r17 = r17 + 7;
        r3 = r17 / 8;
        r0 = new byte[r3];
        r17 = r0;
        r0 = r17;
        r1 = r20;
        r1.bits = r0;
        if (r13 != r7) goto L_0x0025;
    L_0x0024:
        return;
    L_0x0025:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r17 = r17[r13];
        r18 = 94;
        r0 = r17;
        r1 = r18;
        if (r0 != r1) goto L_0x0092;
    L_0x003b:
        r17 = $assertionsDisabled;
        if (r17 != 0) goto L_0x004d;
    L_0x003f:
        r0 = r20;
        r0 = r0.sense;
        r17 = r0;
        if (r17 == 0) goto L_0x004d;
    L_0x0047:
        r17 = new java.lang.AssertionError;
        r17.<init>();
        throw r17;
    L_0x004d:
        r13 = r13 + 1;
        r14 = r13;
    L_0x0050:
        if (r14 == r7) goto L_0x02bb;
    L_0x0052:
        r11 = 2;
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r17 = r17[r14];
        switch(r17) {
            case 92: goto L_0x00a4;
            default: goto L_0x0064;
        };
    L_0x0064:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r13 = r14 + 1;
        r15 = r17[r14];
    L_0x0074:
        if (r9 == 0) goto L_0x026c;
    L_0x0076:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.flags;
        r17 = r0;
        r17 = r17 & 2;
        if (r17 == 0) goto L_0x0266;
    L_0x0086:
        r17 = $assertionsDisabled;
        if (r17 != 0) goto L_0x0239;
    L_0x008a:
        if (r12 <= r15) goto L_0x0239;
    L_0x008c:
        r17 = new java.lang.AssertionError;
        r17.<init>();
        throw r17;
    L_0x0092:
        r17 = $assertionsDisabled;
        if (r17 != 0) goto L_0x02be;
    L_0x0096:
        r0 = r20;
        r0 = r0.sense;
        r17 = r0;
        if (r17 != 0) goto L_0x02be;
    L_0x009e:
        r17 = new java.lang.AssertionError;
        r17.<init>();
        throw r17;
    L_0x00a4:
        r13 = r14 + 1;
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r14 = r13 + 1;
        r4 = r17[r13];
        switch(r4) {
            case 48: goto L_0x0136;
            case 49: goto L_0x0136;
            case 50: goto L_0x0136;
            case 51: goto L_0x0136;
            case 52: goto L_0x0136;
            case 53: goto L_0x0136;
            case 54: goto L_0x0136;
            case 55: goto L_0x0136;
            case 68: goto L_0x0198;
            case 83: goto L_0x01dc;
            case 87: goto L_0x0219;
            case 98: goto L_0x00bc;
            case 99: goto L_0x00d4;
            case 100: goto L_0x0189;
            case 102: goto L_0x00c0;
            case 110: goto L_0x00c4;
            case 114: goto L_0x00c8;
            case 115: goto L_0x01bf;
            case 116: goto L_0x00cc;
            case 117: goto L_0x0107;
            case 118: goto L_0x00d0;
            case 119: goto L_0x01f9;
            case 120: goto L_0x0109;
            default: goto L_0x00b9;
        };
    L_0x00b9:
        r15 = r4;
        r13 = r14;
        goto L_0x0074;
    L_0x00bc:
        r15 = 8;
        r13 = r14;
        goto L_0x0074;
    L_0x00c0:
        r15 = 12;
        r13 = r14;
        goto L_0x0074;
    L_0x00c4:
        r15 = 10;
        r13 = r14;
        goto L_0x0074;
    L_0x00c8:
        r15 = 13;
        r13 = r14;
        goto L_0x0074;
    L_0x00cc:
        r15 = 9;
        r13 = r14;
        goto L_0x0074;
    L_0x00d0:
        r15 = 11;
        r13 = r14;
        goto L_0x0074;
    L_0x00d4:
        if (r14 >= r7) goto L_0x0101;
    L_0x00d6:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r17 = r17[r14];
        r17 = isControlLetter(r17);
        if (r17 == 0) goto L_0x0101;
    L_0x00ea:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r13 = r14 + 1;
        r17 = r17[r14];
        r17 = r17 & 31;
        r0 = r17;
        r15 = (char) r0;
        goto L_0x0074;
    L_0x0101:
        r13 = r14 + -1;
        r15 = 92;
        goto L_0x0074;
    L_0x0107:
        r11 = r11 + 2;
    L_0x0109:
        r10 = 0;
        r8 = 0;
    L_0x010b:
        if (r8 >= r11) goto L_0x02c4;
    L_0x010d:
        if (r14 >= r7) goto L_0x02c4;
    L_0x010f:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r13 = r14 + 1;
        r4 = r17[r14];
        r6 = toASCIIHexDigit(r4);
        if (r6 >= 0) goto L_0x012e;
    L_0x0125:
        r17 = r8 + 1;
        r13 = r13 - r17;
        r10 = 92;
    L_0x012b:
        r15 = (char) r10;
        goto L_0x0074;
    L_0x012e:
        r17 = r10 << 4;
        r10 = r17 | r6;
        r8 = r8 + 1;
        r14 = r13;
        goto L_0x010b;
    L_0x0136:
        r10 = r4 + -48;
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r4 = r17[r14];
        r17 = 48;
        r0 = r17;
        if (r0 > r4) goto L_0x02c1;
    L_0x014c:
        r17 = 55;
        r0 = r17;
        if (r4 > r0) goto L_0x02c1;
    L_0x0152:
        r13 = r14 + 1;
        r17 = r10 * 8;
        r18 = r4 + -48;
        r10 = r17 + r18;
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r4 = r17[r13];
        r17 = 48;
        r0 = r17;
        if (r0 > r4) goto L_0x0183;
    L_0x016e:
        r17 = 55;
        r0 = r17;
        if (r4 > r0) goto L_0x0183;
    L_0x0174:
        r13 = r13 + 1;
        r17 = r10 * 8;
        r18 = r4 + -48;
        r8 = r17 + r18;
        r17 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r0 = r17;
        if (r8 > r0) goto L_0x0186;
    L_0x0182:
        r10 = r8;
    L_0x0183:
        r15 = (char) r10;
        goto L_0x0074;
    L_0x0186:
        r13 = r13 + -1;
        goto L_0x0183;
    L_0x0189:
        r17 = 48;
        r18 = 57;
        r0 = r20;
        r1 = r17;
        r2 = r18;
        addCharacterRangeToCharSet(r0, r1, r2);
        goto L_0x0050;
    L_0x0198:
        r17 = 0;
        r18 = 47;
        r0 = r20;
        r1 = r17;
        r2 = r18;
        addCharacterRangeToCharSet(r0, r1, r2);
        r17 = 58;
        r0 = r20;
        r0 = r0.length;
        r18 = r0;
        r18 = r18 + -1;
        r0 = r18;
        r0 = (char) r0;
        r18 = r0;
        r0 = r20;
        r1 = r17;
        r2 = r18;
        addCharacterRangeToCharSet(r0, r1, r2);
        goto L_0x0050;
    L_0x01bf:
        r0 = r20;
        r0 = r0.length;
        r17 = r0;
        r8 = r17 + -1;
    L_0x01c7:
        if (r8 < 0) goto L_0x0050;
    L_0x01c9:
        r17 = isREWhiteSpace(r8);
        if (r17 == 0) goto L_0x01d9;
    L_0x01cf:
        r0 = (char) r8;
        r17 = r0;
        r0 = r20;
        r1 = r17;
        addCharacterToCharSet(r0, r1);
    L_0x01d9:
        r8 = r8 + -1;
        goto L_0x01c7;
    L_0x01dc:
        r0 = r20;
        r0 = r0.length;
        r17 = r0;
        r8 = r17 + -1;
    L_0x01e4:
        if (r8 < 0) goto L_0x0050;
    L_0x01e6:
        r17 = isREWhiteSpace(r8);
        if (r17 != 0) goto L_0x01f6;
    L_0x01ec:
        r0 = (char) r8;
        r17 = r0;
        r0 = r20;
        r1 = r17;
        addCharacterToCharSet(r0, r1);
    L_0x01f6:
        r8 = r8 + -1;
        goto L_0x01e4;
    L_0x01f9:
        r0 = r20;
        r0 = r0.length;
        r17 = r0;
        r8 = r17 + -1;
    L_0x0201:
        if (r8 < 0) goto L_0x0050;
    L_0x0203:
        r0 = (char) r8;
        r17 = r0;
        r17 = isWord(r17);
        if (r17 == 0) goto L_0x0216;
    L_0x020c:
        r0 = (char) r8;
        r17 = r0;
        r0 = r20;
        r1 = r17;
        addCharacterToCharSet(r0, r1);
    L_0x0216:
        r8 = r8 + -1;
        goto L_0x0201;
    L_0x0219:
        r0 = r20;
        r0 = r0.length;
        r17 = r0;
        r8 = r17 + -1;
    L_0x0221:
        if (r8 < 0) goto L_0x0050;
    L_0x0223:
        r0 = (char) r8;
        r17 = r0;
        r17 = isWord(r17);
        if (r17 != 0) goto L_0x0236;
    L_0x022c:
        r0 = (char) r8;
        r17 = r0;
        r0 = r20;
        r1 = r17;
        addCharacterToCharSet(r0, r1);
    L_0x0236:
        r8 = r8 + -1;
        goto L_0x0221;
    L_0x0239:
        r4 = r12;
    L_0x023a:
        if (r4 > r15) goto L_0x0262;
    L_0x023c:
        r0 = r20;
        addCharacterToCharSet(r0, r4);
        r16 = upcase(r4);
        r5 = downcase(r4);
        r0 = r16;
        if (r4 == r0) goto L_0x0254;
    L_0x024d:
        r0 = r20;
        r1 = r16;
        addCharacterToCharSet(r0, r1);
    L_0x0254:
        if (r4 == r5) goto L_0x025b;
    L_0x0256:
        r0 = r20;
        addCharacterToCharSet(r0, r5);
    L_0x025b:
        r17 = r4 + 1;
        r0 = r17;
        r4 = (char) r0;
        if (r4 != 0) goto L_0x023a;
    L_0x0262:
        r9 = 0;
        r14 = r13;
        goto L_0x0050;
    L_0x0266:
        r0 = r20;
        addCharacterRangeToCharSet(r0, r12, r15);
        goto L_0x0262;
    L_0x026c:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.flags;
        r17 = r0;
        r17 = r17 & 2;
        if (r17 == 0) goto L_0x02b5;
    L_0x027c:
        r17 = upcase(r15);
        r0 = r20;
        r1 = r17;
        addCharacterToCharSet(r0, r1);
        r17 = downcase(r15);
        r0 = r20;
        r1 = r17;
        addCharacterToCharSet(r0, r1);
    L_0x0292:
        r17 = r7 + -1;
        r0 = r17;
        if (r13 >= r0) goto L_0x02be;
    L_0x0298:
        r0 = r19;
        r0 = r0.regexp;
        r17 = r0;
        r0 = r17;
        r0 = r0.source;
        r17 = r0;
        r17 = r17[r13];
        r18 = 45;
        r0 = r17;
        r1 = r18;
        if (r0 != r1) goto L_0x02be;
    L_0x02ae:
        r13 = r13 + 1;
        r9 = 1;
        r12 = r15;
        r14 = r13;
        goto L_0x0050;
    L_0x02b5:
        r0 = r20;
        addCharacterToCharSet(r0, r15);
        goto L_0x0292;
    L_0x02bb:
        r13 = r14;
        goto L_0x0024;
    L_0x02be:
        r14 = r13;
        goto L_0x0050;
    L_0x02c1:
        r13 = r14;
        goto L_0x0183;
    L_0x02c4:
        r13 = r14;
        goto L_0x012b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.regexp.NativeRegExp.processCharSetImpl(org.mozilla.javascript.regexp.REGlobalData, org.mozilla.javascript.regexp.RECharSet):void");
    }

    private static boolean classMatcher(REGlobalData gData, RECharSet charSet, char ch) {
        int i = 1;
        if (!charSet.converted) {
            processCharSet(gData, charSet);
        }
        int byteIndex = ch >> 3;
        if (!(charSet.length == 0 || ch >= charSet.length || (charSet.bits[byteIndex] & (1 << (ch & 7))) == 0)) {
            i = 0;
        }
        return i ^ charSet.sense;
    }

    private static boolean reopIsSimple(int op) {
        return op >= 1 && op <= 23;
    }

    /* JADX WARNING: Missing block: B:117:0x024e, code skipped:
            r16 = r7;
     */
    private static int simpleMatch(org.mozilla.javascript.regexp.REGlobalData r12, java.lang.String r13, int r14, byte[] r15, int r16, int r17, boolean r18) {
        /*
        r8 = 0;
        r9 = r12.cp;
        switch(r14) {
            case 1: goto L_0x000b;
            case 2: goto L_0x0013;
            case 3: goto L_0x002b;
            case 4: goto L_0x0043;
            case 5: goto L_0x0070;
            case 6: goto L_0x009e;
            case 7: goto L_0x00b9;
            case 8: goto L_0x00d4;
            case 9: goto L_0x00ef;
            case 10: goto L_0x010a;
            case 11: goto L_0x0125;
            case 12: goto L_0x0140;
            case 13: goto L_0x015b;
            case 14: goto L_0x0169;
            case 15: goto L_0x017d;
            case 16: goto L_0x019d;
            case 17: goto L_0x01b1;
            case 18: goto L_0x01db;
            case 19: goto L_0x01f9;
            case 20: goto L_0x0006;
            case 21: goto L_0x0006;
            case 22: goto L_0x0221;
            case 23: goto L_0x0221;
            default: goto L_0x0006;
        };
    L_0x0006:
        r10 = org.mozilla.javascript.Kit.codeBug();
        throw r10;
    L_0x000b:
        r8 = 1;
    L_0x000c:
        if (r8 == 0) goto L_0x0248;
    L_0x000e:
        if (r18 != 0) goto L_0x0012;
    L_0x0010:
        r12.cp = r9;
    L_0x0012:
        return r16;
    L_0x0013:
        r10 = r12.cp;
        if (r10 == 0) goto L_0x0029;
    L_0x0017:
        r10 = r12.multiline;
        if (r10 == 0) goto L_0x000c;
    L_0x001b:
        r10 = r12.cp;
        r10 = r10 + -1;
        r10 = r13.charAt(r10);
        r10 = isLineTerm(r10);
        if (r10 == 0) goto L_0x000c;
    L_0x0029:
        r8 = 1;
        goto L_0x000c;
    L_0x002b:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x0041;
    L_0x0031:
        r10 = r12.multiline;
        if (r10 == 0) goto L_0x000c;
    L_0x0035:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isLineTerm(r10);
        if (r10 == 0) goto L_0x000c;
    L_0x0041:
        r8 = 1;
        goto L_0x000c;
    L_0x0043:
        r10 = r12.cp;
        if (r10 == 0) goto L_0x0055;
    L_0x0047:
        r10 = r12.cp;
        r10 = r10 + -1;
        r10 = r13.charAt(r10);
        r10 = isWord(r10);
        if (r10 != 0) goto L_0x006c;
    L_0x0055:
        r10 = 1;
    L_0x0056:
        r11 = r12.cp;
        r0 = r17;
        if (r11 >= r0) goto L_0x0068;
    L_0x005c:
        r11 = r12.cp;
        r11 = r13.charAt(r11);
        r11 = isWord(r11);
        if (r11 != 0) goto L_0x006e;
    L_0x0068:
        r11 = 1;
    L_0x0069:
        r8 = r10 ^ r11;
        goto L_0x000c;
    L_0x006c:
        r10 = 0;
        goto L_0x0056;
    L_0x006e:
        r11 = 0;
        goto L_0x0069;
    L_0x0070:
        r10 = r12.cp;
        if (r10 == 0) goto L_0x0082;
    L_0x0074:
        r10 = r12.cp;
        r10 = r10 + -1;
        r10 = r13.charAt(r10);
        r10 = isWord(r10);
        if (r10 != 0) goto L_0x009a;
    L_0x0082:
        r10 = 1;
    L_0x0083:
        r11 = r12.cp;
        r0 = r17;
        if (r11 >= r0) goto L_0x009c;
    L_0x0089:
        r11 = r12.cp;
        r11 = r13.charAt(r11);
        r11 = isWord(r11);
        if (r11 == 0) goto L_0x009c;
    L_0x0095:
        r11 = 1;
    L_0x0096:
        r8 = r10 ^ r11;
        goto L_0x000c;
    L_0x009a:
        r10 = 0;
        goto L_0x0083;
    L_0x009c:
        r11 = 0;
        goto L_0x0096;
    L_0x009e:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x00a4:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isLineTerm(r10);
        if (r10 != 0) goto L_0x000c;
    L_0x00b0:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x00b9:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x00bf:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isDigit(r10);
        if (r10 == 0) goto L_0x000c;
    L_0x00cb:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x00d4:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x00da:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isDigit(r10);
        if (r10 != 0) goto L_0x000c;
    L_0x00e6:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x00ef:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x00f5:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isWord(r10);
        if (r10 == 0) goto L_0x000c;
    L_0x0101:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x010a:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x0110:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isWord(r10);
        if (r10 != 0) goto L_0x000c;
    L_0x011c:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x0125:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x012b:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isREWhiteSpace(r10);
        if (r10 == 0) goto L_0x000c;
    L_0x0137:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x0140:
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x0146:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        r10 = isREWhiteSpace(r10);
        if (r10 != 0) goto L_0x000c;
    L_0x0152:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x015b:
        r6 = getIndex(r15, r16);
        r16 = r16 + 2;
        r0 = r17;
        r8 = backrefMatcher(r12, r6, r13, r0);
        goto L_0x000c;
    L_0x0169:
        r5 = getIndex(r15, r16);
        r16 = r16 + 2;
        r3 = getIndex(r15, r16);
        r16 = r16 + 2;
        r0 = r17;
        r8 = flatNMatcher(r12, r5, r3, r13, r0);
        goto L_0x000c;
    L_0x017d:
        r7 = r16 + 1;
        r10 = r15[r16];
        r10 = r10 & 255;
        r4 = (char) r10;
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x024e;
    L_0x018a:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        if (r10 != r4) goto L_0x024e;
    L_0x0192:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        r16 = r7;
        goto L_0x000c;
    L_0x019d:
        r5 = getIndex(r15, r16);
        r16 = r16 + 2;
        r3 = getIndex(r15, r16);
        r16 = r16 + 2;
        r0 = r17;
        r8 = flatNIMatcher(r12, r5, r3, r13, r0);
        goto L_0x000c;
    L_0x01b1:
        r7 = r16 + 1;
        r10 = r15[r16];
        r10 = r10 & 255;
        r4 = (char) r10;
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x024e;
    L_0x01be:
        r10 = r12.cp;
        r1 = r13.charAt(r10);
        if (r4 == r1) goto L_0x01d0;
    L_0x01c6:
        r10 = upcase(r4);
        r11 = upcase(r1);
        if (r10 != r11) goto L_0x01d7;
    L_0x01d0:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
    L_0x01d7:
        r16 = r7;
        goto L_0x000c;
    L_0x01db:
        r10 = getIndex(r15, r16);
        r4 = (char) r10;
        r16 = r16 + 2;
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x01e8:
        r10 = r12.cp;
        r10 = r13.charAt(r10);
        if (r10 != r4) goto L_0x000c;
    L_0x01f0:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x01f9:
        r10 = getIndex(r15, r16);
        r4 = (char) r10;
        r16 = r16 + 2;
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x0206:
        r10 = r12.cp;
        r1 = r13.charAt(r10);
        if (r4 == r1) goto L_0x0218;
    L_0x020e:
        r10 = upcase(r4);
        r11 = upcase(r1);
        if (r10 != r11) goto L_0x000c;
    L_0x0218:
        r8 = 1;
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        goto L_0x000c;
    L_0x0221:
        r2 = getIndex(r15, r16);
        r16 = r16 + 2;
        r10 = r12.cp;
        r0 = r17;
        if (r10 == r0) goto L_0x000c;
    L_0x022d:
        r10 = r12.regexp;
        r10 = r10.classList;
        r10 = r10[r2];
        r11 = r12.cp;
        r11 = r13.charAt(r11);
        r10 = classMatcher(r12, r10, r11);
        if (r10 == 0) goto L_0x000c;
    L_0x023f:
        r10 = r12.cp;
        r10 = r10 + 1;
        r12.cp = r10;
        r8 = 1;
        goto L_0x000c;
    L_0x0248:
        r12.cp = r9;
        r16 = -1;
        goto L_0x0012;
    L_0x024e:
        r16 = r7;
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.regexp.NativeRegExp.simpleMatch(org.mozilla.javascript.regexp.REGlobalData, java.lang.String, int, byte[], int, int, boolean):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:166:0x0542  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x0542  */
    /* JADX WARNING: Missing block: B:13:0x0058, code skipped:
            r40 = r7 + getOffset(r6, r7);
            r7 = r7 + 2;
            r12 = r7 + 1;
            r5 = r6[r7];
            r16 = r46.cp;
     */
    /* JADX WARNING: Missing block: B:14:0x006e, code skipped:
            if (reopIsSimple(r5) == false) goto L_0x0155;
     */
    /* JADX WARNING: Missing block: B:15:0x0070, code skipped:
            r35 = simpleMatch(r46, r47, r5, r6, r12, r48, true);
     */
    /* JADX WARNING: Missing block: B:16:0x007d, code skipped:
            if (r35 >= 0) goto L_0x014c;
     */
    /* JADX WARNING: Missing block: B:17:0x007f, code skipped:
            r15 = r40 + 1;
            r5 = r6[r40];
            r7 = r15;
     */
    /* JADX WARNING: Missing block: B:52:0x014c, code skipped:
            r44 = true;
            r7 = r35;
            r12 = r7 + 1;
            r5 = r6[r7];
     */
    /* JADX WARNING: Missing block: B:53:0x0155, code skipped:
            r7 = r12;
            pushBackTrackState(r46, r6[r40], r40 + 1, r16, r17, r18);
     */
    /* JADX WARNING: Missing block: B:82:0x0284, code skipped:
            r20 = 0;
            r21 = -1;
     */
    /* JADX WARNING: Missing block: B:83:0x0288, code skipped:
            pushProgState(r46, r20, r21, r46.cp, null, r17, r18);
     */
    /* JADX WARNING: Missing block: B:84:0x0299, code skipped:
            if (r33 == false) goto L_0x02ce;
     */
    /* JADX WARNING: Missing block: B:85:0x029b, code skipped:
            pushBackTrackState(r46, REOP_REPEAT, r7);
            r17 = 51;
            r18 = r7;
            r7 = r7 + 6;
            r12 = r7 + 1;
            r5 = r6[r7];
            r7 = r12;
     */
    /* JADX WARNING: Missing block: B:87:0x02b1, code skipped:
            r20 = 1;
            r21 = -1;
     */
    /* JADX WARNING: Missing block: B:89:0x02b8, code skipped:
            r20 = 0;
            r21 = 1;
     */
    /* JADX WARNING: Missing block: B:91:0x02bf, code skipped:
            r20 = getOffset(r6, r7);
            r7 = r7 + 2;
            r21 = getOffset(r6, r7) - 1;
            r7 = r7 + 2;
     */
    /* JADX WARNING: Missing block: B:92:0x02ce, code skipped:
            if (r20 == 0) goto L_0x02dd;
     */
    /* JADX WARNING: Missing block: B:93:0x02d0, code skipped:
            r17 = 52;
            r18 = r7;
            r7 = r7 + 6;
            r12 = r7 + 1;
            r5 = r6[r7];
            r7 = r12;
     */
    /* JADX WARNING: Missing block: B:94:0x02dd, code skipped:
            pushBackTrackState(r46, REOP_MINIMALREPEAT, r7);
            popProgState(r46);
            r7 = r7 + 4;
            r7 = r7 + getOffset(r6, r7);
            r12 = r7 + 1;
            r5 = r6[r7];
            r7 = r12;
     */
    private static boolean executeREBytecode(org.mozilla.javascript.regexp.REGlobalData r46, java.lang.String r47, int r48) {
        /*
        r12 = 0;
        r0 = r46;
        r3 = r0.regexp;
        r6 = r3.program;
        r17 = 57;
        r18 = 0;
        r44 = 0;
        r7 = r12 + 1;
        r5 = r6[r12];
        r0 = r46;
        r3 = r0.regexp;
        r3 = r3.anchorCh;
        if (r3 >= 0) goto L_0x0084;
    L_0x0019:
        r3 = reopIsSimple(r5);
        if (r3 == 0) goto L_0x0084;
    L_0x001f:
        r29 = 0;
    L_0x0021:
        r0 = r46;
        r3 = r0.cp;
        r0 = r48;
        if (r3 > r0) goto L_0x003f;
    L_0x0029:
        r9 = 1;
        r3 = r46;
        r4 = r47;
        r8 = r48;
        r35 = simpleMatch(r3, r4, r5, r6, r7, r8, r9);
        if (r35 < 0) goto L_0x0043;
    L_0x0036:
        r29 = 1;
        r7 = r35;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
    L_0x003f:
        if (r29 != 0) goto L_0x0084;
    L_0x0041:
        r3 = 0;
    L_0x0042:
        return r3;
    L_0x0043:
        r0 = r46;
        r3 = r0.skipped;
        r3 = r3 + 1;
        r0 = r46;
        r0.skipped = r3;
        r0 = r46;
        r3 = r0.cp;
        r3 = r3 + 1;
        r0 = r46;
        r0.cp = r3;
        goto L_0x0021;
    L_0x0058:
        r3 = getOffset(r6, r7);
        r40 = r7 + r3;
        r7 = r7 + 2;
        r12 = r7 + 1;
        r5 = r6[r7];
        r0 = r46;
        r0 = r0.cp;
        r16 = r0;
        r3 = reopIsSimple(r5);
        if (r3 == 0) goto L_0x0550;
    L_0x0070:
        r14 = 1;
        r8 = r46;
        r9 = r47;
        r10 = r5;
        r11 = r6;
        r13 = r48;
        r35 = simpleMatch(r8, r9, r10, r11, r12, r13, r14);
        if (r35 >= 0) goto L_0x014c;
    L_0x007f:
        r15 = r40 + 1;
        r5 = r6[r40];
        r7 = r15;
    L_0x0084:
        r3 = reopIsSimple(r5);
        if (r3 == 0) goto L_0x00e0;
    L_0x008a:
        r9 = 1;
        r3 = r46;
        r4 = r47;
        r8 = r48;
        r35 = simpleMatch(r3, r4, r5, r6, r7, r8, r9);
        if (r35 < 0) goto L_0x00dd;
    L_0x0097:
        r44 = 1;
    L_0x0099:
        if (r44 == 0) goto L_0x009d;
    L_0x009b:
        r7 = r35;
    L_0x009d:
        r12 = r7;
    L_0x009e:
        if (r44 != 0) goto L_0x0542;
    L_0x00a0:
        r0 = r46;
        r0 = r0.backTrackStackTop;
        r30 = r0;
        if (r30 == 0) goto L_0x053e;
    L_0x00a8:
        r0 = r30;
        r3 = r0.previous;
        r0 = r46;
        r0.backTrackStackTop = r3;
        r0 = r30;
        r3 = r0.parens;
        r0 = r46;
        r0.parens = r3;
        r0 = r30;
        r3 = r0.cp;
        r0 = r46;
        r0.cp = r3;
        r0 = r30;
        r3 = r0.stateStackTop;
        r0 = r46;
        r0.stateStackTop = r3;
        r0 = r30;
        r0 = r0.continuationOp;
        r17 = r0;
        r0 = r30;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r30;
        r7 = r0.pc;
        r0 = r30;
        r5 = r0.op;
        goto L_0x0084;
    L_0x00dd:
        r44 = 0;
        goto L_0x0099;
    L_0x00e0:
        switch(r5) {
            case 25: goto L_0x0278;
            case 26: goto L_0x0278;
            case 27: goto L_0x0278;
            case 28: goto L_0x0278;
            case 29: goto L_0x016d;
            case 30: goto L_0x0186;
            case 31: goto L_0x0058;
            case 32: goto L_0x0160;
            case 33: goto L_0x00e3;
            case 34: goto L_0x00e3;
            case 35: goto L_0x00e3;
            case 36: goto L_0x00e3;
            case 37: goto L_0x00e3;
            case 38: goto L_0x00e3;
            case 39: goto L_0x00e3;
            case 40: goto L_0x00e3;
            case 41: goto L_0x01aa;
            case 42: goto L_0x01f7;
            case 43: goto L_0x024a;
            case 44: goto L_0x024a;
            case 45: goto L_0x0278;
            case 46: goto L_0x0278;
            case 47: goto L_0x0278;
            case 48: goto L_0x0278;
            case 49: goto L_0x02f5;
            case 50: goto L_0x00e3;
            case 51: goto L_0x02fd;
            case 52: goto L_0x0427;
            case 53: goto L_0x00ea;
            case 54: goto L_0x00ea;
            case 55: goto L_0x00ea;
            case 56: goto L_0x00e3;
            case 57: goto L_0x053b;
            default: goto L_0x00e3;
        };
    L_0x00e3:
        r3 = "invalid bytecode";
        r3 = org.mozilla.javascript.Kit.codeBug(r3);
        throw r3;
    L_0x00ea:
        r3 = getIndex(r6, r7);
        r0 = (char) r3;
        r36 = r0;
        r7 = r7 + 2;
        r3 = getIndex(r6, r7);
        r0 = (char) r3;
        r37 = r0;
        r7 = r7 + 2;
        r0 = r46;
        r3 = r0.cp;
        r0 = r48;
        if (r3 != r0) goto L_0x0108;
    L_0x0104:
        r44 = 0;
        r12 = r7;
        goto L_0x009e;
    L_0x0108:
        r0 = r46;
        r3 = r0.cp;
        r0 = r47;
        r31 = r0.charAt(r3);
        r3 = 55;
        if (r5 != r3) goto L_0x0133;
    L_0x0116:
        r0 = r31;
        r1 = r36;
        if (r0 == r1) goto L_0x0058;
    L_0x011c:
        r0 = r46;
        r3 = r0.regexp;
        r3 = r3.classList;
        r3 = r3[r37];
        r0 = r46;
        r1 = r31;
        r3 = classMatcher(r0, r3, r1);
        if (r3 != 0) goto L_0x0058;
    L_0x012e:
        r44 = 0;
        r12 = r7;
        goto L_0x009e;
    L_0x0133:
        r3 = 54;
        if (r5 != r3) goto L_0x013b;
    L_0x0137:
        r31 = upcase(r31);
    L_0x013b:
        r0 = r31;
        r1 = r36;
        if (r0 == r1) goto L_0x0058;
    L_0x0141:
        r0 = r31;
        r1 = r37;
        if (r0 == r1) goto L_0x0058;
    L_0x0147:
        r44 = 0;
        r12 = r7;
        goto L_0x009e;
    L_0x014c:
        r44 = 1;
        r7 = r35;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
    L_0x0155:
        r15 = r40 + 1;
        r14 = r6[r40];
        r13 = r46;
        pushBackTrackState(r13, r14, r15, r16, r17, r18);
        goto L_0x0084;
    L_0x0160:
        r41 = getOffset(r6, r7);
        r7 = r7 + r41;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x016d:
        r43 = getIndex(r6, r7);
        r7 = r7 + 2;
        r0 = r46;
        r3 = r0.cp;
        r4 = 0;
        r0 = r46;
        r1 = r43;
        r0.setParens(r1, r3, r4);
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x0186:
        r43 = getIndex(r6, r7);
        r7 = r7 + 2;
        r0 = r46;
        r1 = r43;
        r32 = r0.parensIndex(r1);
        r0 = r46;
        r3 = r0.cp;
        r3 = r3 - r32;
        r0 = r46;
        r1 = r43;
        r2 = r32;
        r0.setParens(r1, r2, r3);
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x01aa:
        r3 = getIndex(r6, r7);
        r15 = r7 + r3;
        r7 = r7 + 2;
        r12 = r7 + 1;
        r5 = r6[r7];
        r3 = reopIsSimple(r5);
        if (r3 == 0) goto L_0x01d4;
    L_0x01bc:
        r25 = 0;
        r19 = r46;
        r20 = r47;
        r21 = r5;
        r22 = r6;
        r23 = r12;
        r24 = r48;
        r3 = simpleMatch(r19, r20, r21, r22, r23, r24, r25);
        if (r3 >= 0) goto L_0x01d4;
    L_0x01d0:
        r44 = 0;
        goto L_0x009e;
    L_0x01d4:
        r20 = 0;
        r21 = 0;
        r0 = r46;
        r0 = r0.cp;
        r22 = r0;
        r0 = r46;
        r0 = r0.backTrackStackTop;
        r23 = r0;
        r19 = r46;
        r24 = r17;
        r25 = r18;
        pushProgState(r19, r20, r21, r22, r23, r24, r25);
        r3 = 43;
        r0 = r46;
        pushBackTrackState(r0, r3, r15);
        r7 = r12;
        goto L_0x0084;
    L_0x01f7:
        r3 = getIndex(r6, r7);
        r15 = r7 + r3;
        r7 = r7 + 2;
        r12 = r7 + 1;
        r5 = r6[r7];
        r3 = reopIsSimple(r5);
        if (r3 == 0) goto L_0x0227;
    L_0x0209:
        r25 = 0;
        r19 = r46;
        r20 = r47;
        r21 = r5;
        r22 = r6;
        r23 = r12;
        r24 = r48;
        r35 = simpleMatch(r19, r20, r21, r22, r23, r24, r25);
        if (r35 < 0) goto L_0x0227;
    L_0x021d:
        r3 = r6[r35];
        r4 = 44;
        if (r3 != r4) goto L_0x0227;
    L_0x0223:
        r44 = 0;
        goto L_0x009e;
    L_0x0227:
        r20 = 0;
        r21 = 0;
        r0 = r46;
        r0 = r0.cp;
        r22 = r0;
        r0 = r46;
        r0 = r0.backTrackStackTop;
        r23 = r0;
        r19 = r46;
        r24 = r17;
        r25 = r18;
        pushProgState(r19, r20, r21, r22, r23, r24, r25);
        r3 = 44;
        r0 = r46;
        pushBackTrackState(r0, r3, r15);
        r7 = r12;
        goto L_0x0084;
    L_0x024a:
        r45 = popProgState(r46);
        r0 = r45;
        r3 = r0.index;
        r0 = r46;
        r0.cp = r3;
        r0 = r45;
        r3 = r0.backTrack;
        r0 = r46;
        r0.backTrackStackTop = r3;
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r3 = 44;
        if (r5 != r3) goto L_0x0272;
    L_0x026e:
        if (r44 != 0) goto L_0x0275;
    L_0x0270:
        r44 = 1;
    L_0x0272:
        r12 = r7;
        goto L_0x009e;
    L_0x0275:
        r44 = 0;
        goto L_0x0272;
    L_0x0278:
        r33 = 0;
        switch(r5) {
            case 25: goto L_0x02bd;
            case 26: goto L_0x0282;
            case 27: goto L_0x02af;
            case 28: goto L_0x02b6;
            case 45: goto L_0x0284;
            case 46: goto L_0x02b1;
            case 47: goto L_0x02b8;
            case 48: goto L_0x02bf;
            default: goto L_0x027d;
        };
    L_0x027d:
        r3 = org.mozilla.javascript.Kit.codeBug();
        throw r3;
    L_0x0282:
        r33 = 1;
    L_0x0284:
        r20 = 0;
        r21 = -1;
    L_0x0288:
        r0 = r46;
        r0 = r0.cp;
        r22 = r0;
        r23 = 0;
        r19 = r46;
        r24 = r17;
        r25 = r18;
        pushProgState(r19, r20, r21, r22, r23, r24, r25);
        if (r33 == 0) goto L_0x02ce;
    L_0x029b:
        r3 = 51;
        r0 = r46;
        pushBackTrackState(r0, r3, r7);
        r17 = 51;
        r18 = r7;
        r7 = r7 + 6;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x02af:
        r33 = 1;
    L_0x02b1:
        r20 = 1;
        r21 = -1;
        goto L_0x0288;
    L_0x02b6:
        r33 = 1;
    L_0x02b8:
        r20 = 0;
        r21 = 1;
        goto L_0x0288;
    L_0x02bd:
        r33 = 1;
    L_0x02bf:
        r20 = getOffset(r6, r7);
        r7 = r7 + 2;
        r3 = getOffset(r6, r7);
        r21 = r3 + -1;
        r7 = r7 + 2;
        goto L_0x0288;
    L_0x02ce:
        if (r20 == 0) goto L_0x02dd;
    L_0x02d0:
        r17 = 52;
        r18 = r7;
        r7 = r7 + 6;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x02dd:
        r3 = 52;
        r0 = r46;
        pushBackTrackState(r0, r3, r7);
        popProgState(r46);
        r7 = r7 + 4;
        r3 = getOffset(r6, r7);
        r7 = r7 + r3;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x02f5:
        r44 = 1;
        r7 = r18;
        r5 = r17;
        goto L_0x0084;
    L_0x02fd:
        r45 = popProgState(r46);
        if (r44 != 0) goto L_0x0321;
    L_0x0303:
        r0 = r45;
        r3 = r0.min;
        if (r3 != 0) goto L_0x030b;
    L_0x0309:
        r44 = 1;
    L_0x030b:
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r7 = r7 + 4;
        r3 = getOffset(r6, r7);
        r7 = r7 + r3;
        r12 = r7;
        goto L_0x009e;
    L_0x0321:
        r0 = r45;
        r3 = r0.min;
        if (r3 != 0) goto L_0x0349;
    L_0x0327:
        r0 = r46;
        r3 = r0.cp;
        r0 = r45;
        r4 = r0.index;
        if (r3 != r4) goto L_0x0349;
    L_0x0331:
        r44 = 0;
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r7 = r7 + 4;
        r3 = getOffset(r6, r7);
        r7 = r7 + r3;
        r12 = r7;
        goto L_0x009e;
    L_0x0349:
        r0 = r45;
        r0 = r0.min;
        r23 = r0;
        r0 = r45;
        r0 = r0.max;
        r24 = r0;
        if (r23 == 0) goto L_0x054c;
    L_0x0357:
        r23 = r23 + -1;
        r39 = r23;
    L_0x035b:
        r3 = -1;
        r0 = r24;
        if (r0 == r3) goto L_0x0548;
    L_0x0360:
        r24 = r24 + -1;
        r38 = r24;
    L_0x0364:
        if (r38 != 0) goto L_0x037e;
    L_0x0366:
        r44 = 1;
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r7 = r7 + 4;
        r3 = getOffset(r6, r7);
        r7 = r7 + r3;
        r12 = r7;
        goto L_0x009e;
    L_0x037e:
        r15 = r7 + 6;
        r14 = r6[r15];
        r0 = r46;
        r0 = r0.cp;
        r16 = r0;
        r3 = reopIsSimple(r14);
        if (r3 == 0) goto L_0x03c5;
    L_0x038e:
        r15 = r15 + 1;
        r28 = 1;
        r22 = r46;
        r23 = r47;
        r24 = r14;
        r25 = r6;
        r26 = r15;
        r27 = r48;
        r35 = simpleMatch(r22, r23, r24, r25, r26, r27, r28);
        if (r35 >= 0) goto L_0x03c1;
    L_0x03a4:
        if (r39 != 0) goto L_0x03be;
    L_0x03a6:
        r44 = 1;
    L_0x03a8:
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r7 = r7 + 4;
        r3 = getOffset(r6, r7);
        r7 = r7 + r3;
        r12 = r7;
        goto L_0x009e;
    L_0x03be:
        r44 = 0;
        goto L_0x03a8;
    L_0x03c1:
        r44 = 1;
        r15 = r35;
    L_0x03c5:
        r17 = 51;
        r18 = r7;
        r26 = 0;
        r0 = r45;
        r0 = r0.continuationOp;
        r27 = r0;
        r0 = r45;
        r0 = r0.continuationPc;
        r28 = r0;
        r22 = r46;
        r23 = r39;
        r24 = r38;
        r25 = r16;
        pushProgState(r22, r23, r24, r25, r26, r27, r28);
        if (r39 != 0) goto L_0x0419;
    L_0x03e4:
        r23 = 51;
        r0 = r45;
        r0 = r0.continuationOp;
        r26 = r0;
        r0 = r45;
        r0 = r0.continuationPc;
        r27 = r0;
        r22 = r46;
        r24 = r7;
        r25 = r16;
        pushBackTrackState(r22, r23, r24, r25, r26, r27);
        r42 = getIndex(r6, r7);
        r3 = r7 + 2;
        r43 = getIndex(r6, r3);
        r34 = 0;
    L_0x0407:
        r0 = r34;
        r1 = r42;
        if (r0 >= r1) goto L_0x0419;
    L_0x040d:
        r3 = r43 + r34;
        r4 = -1;
        r8 = 0;
        r0 = r46;
        r0.setParens(r3, r4, r8);
        r34 = r34 + 1;
        goto L_0x0407;
    L_0x0419:
        r3 = r6[r15];
        r4 = 49;
        if (r3 == r4) goto L_0x02fd;
    L_0x041f:
        r7 = r15;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x0427:
        r45 = popProgState(r46);
        if (r44 != 0) goto L_0x0499;
    L_0x042d:
        r0 = r45;
        r3 = r0.max;
        r4 = -1;
        if (r3 == r4) goto L_0x043a;
    L_0x0434:
        r0 = r45;
        r3 = r0.max;
        if (r3 <= 0) goto L_0x048a;
    L_0x043a:
        r0 = r45;
        r0 = r0.min;
        r23 = r0;
        r0 = r45;
        r0 = r0.max;
        r24 = r0;
        r0 = r46;
        r0 = r0.cp;
        r25 = r0;
        r26 = 0;
        r0 = r45;
        r0 = r0.continuationOp;
        r27 = r0;
        r0 = r45;
        r0 = r0.continuationPc;
        r28 = r0;
        r22 = r46;
        pushProgState(r22, r23, r24, r25, r26, r27, r28);
        r17 = 52;
        r18 = r7;
        r42 = getIndex(r6, r7);
        r7 = r7 + 2;
        r43 = getIndex(r6, r7);
        r7 = r7 + 4;
        r34 = 0;
    L_0x0471:
        r0 = r34;
        r1 = r42;
        if (r0 >= r1) goto L_0x0483;
    L_0x0477:
        r3 = r43 + r34;
        r4 = -1;
        r8 = 0;
        r0 = r46;
        r0.setParens(r3, r4, r8);
        r34 = r34 + 1;
        goto L_0x0471;
    L_0x0483:
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x048a:
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r12 = r7;
        goto L_0x009e;
    L_0x0499:
        r0 = r45;
        r3 = r0.min;
        if (r3 != 0) goto L_0x04ba;
    L_0x049f:
        r0 = r46;
        r3 = r0.cp;
        r0 = r45;
        r4 = r0.index;
        if (r3 != r4) goto L_0x04ba;
    L_0x04a9:
        r44 = 0;
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r12 = r7;
        goto L_0x009e;
    L_0x04ba:
        r0 = r45;
        r0 = r0.min;
        r23 = r0;
        r0 = r45;
        r0 = r0.max;
        r24 = r0;
        if (r23 == 0) goto L_0x04ca;
    L_0x04c8:
        r23 = r23 + -1;
    L_0x04ca:
        r3 = -1;
        r0 = r24;
        if (r0 == r3) goto L_0x04d1;
    L_0x04cf:
        r24 = r24 + -1;
    L_0x04d1:
        r0 = r46;
        r0 = r0.cp;
        r25 = r0;
        r26 = 0;
        r0 = r45;
        r0 = r0.continuationOp;
        r27 = r0;
        r0 = r45;
        r0 = r0.continuationPc;
        r28 = r0;
        r22 = r46;
        pushProgState(r22, r23, r24, r25, r26, r27, r28);
        if (r23 == 0) goto L_0x0517;
    L_0x04ec:
        r17 = 52;
        r18 = r7;
        r42 = getIndex(r6, r7);
        r7 = r7 + 2;
        r43 = getIndex(r6, r7);
        r7 = r7 + 4;
        r34 = 0;
    L_0x04fe:
        r0 = r34;
        r1 = r42;
        if (r0 >= r1) goto L_0x0510;
    L_0x0504:
        r3 = r43 + r34;
        r4 = -1;
        r8 = 0;
        r0 = r46;
        r0.setParens(r3, r4, r8);
        r34 = r34 + 1;
        goto L_0x04fe;
    L_0x0510:
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x0517:
        r0 = r45;
        r0 = r0.continuationPc;
        r18 = r0;
        r0 = r45;
        r0 = r0.continuationOp;
        r17 = r0;
        r3 = 52;
        r0 = r46;
        pushBackTrackState(r0, r3, r7);
        popProgState(r46);
        r7 = r7 + 4;
        r3 = getOffset(r6, r7);
        r7 = r7 + r3;
        r12 = r7 + 1;
        r5 = r6[r7];
        r7 = r12;
        goto L_0x0084;
    L_0x053b:
        r3 = 1;
        goto L_0x0042;
    L_0x053e:
        r3 = 0;
        r7 = r12;
        goto L_0x0042;
    L_0x0542:
        r7 = r12 + 1;
        r5 = r6[r12];
        goto L_0x0084;
    L_0x0548:
        r38 = r24;
        goto L_0x0364;
    L_0x054c:
        r39 = r23;
        goto L_0x035b;
    L_0x0550:
        r7 = r12;
        goto L_0x0155;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.regexp.NativeRegExp.executeREBytecode(org.mozilla.javascript.regexp.REGlobalData, java.lang.String, int):boolean");
    }

    private static boolean matchRegExp(REGlobalData gData, RECompiled re, String input, int start, int end, boolean multiline) {
        if (re.parenCount != 0) {
            gData.parens = new long[re.parenCount];
        } else {
            gData.parens = null;
        }
        gData.backTrackStackTop = null;
        gData.stateStackTop = null;
        boolean z = multiline || (re.flags & 4) != 0;
        gData.multiline = z;
        gData.regexp = re;
        char anchorCh = gData.regexp.anchorCh;
        int i = start;
        while (i <= end) {
            if (anchorCh >= 0) {
                while (i != end) {
                    char matchCh = input.charAt(i);
                    if (matchCh != anchorCh && ((gData.regexp.flags & 2) == 0 || upcase(matchCh) != upcase((char) anchorCh))) {
                        i++;
                    }
                }
                return false;
            }
            gData.cp = i;
            gData.skipped = i - start;
            for (int j = 0; j < re.parenCount; j++) {
                gData.parens[j] = -1;
            }
            boolean result = executeREBytecode(gData, input, end);
            gData.backTrackStackTop = null;
            gData.stateStackTop = null;
            if (result) {
                return true;
            }
            if (anchorCh != 65534 || gData.multiline) {
                i = (start + gData.skipped) + 1;
            } else {
                gData.skipped = end;
                return false;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public Object executeRegExp(Context cx, Scriptable scope, RegExpImpl res, String str, int[] indexp, int matchType) {
        REGlobalData gData = new REGlobalData();
        int start = indexp[0];
        int end = str.length();
        if (start > end) {
            start = end;
        }
        if (matchRegExp(gData, this.re, str, start, end, res.multiline)) {
            Object result;
            Scriptable obj;
            int index = gData.cp;
            indexp[0] = index;
            int ep = index;
            int matchlen = ep - (gData.skipped + start);
            index -= matchlen;
            if (matchType == 0) {
                result = Boolean.TRUE;
                obj = null;
            } else {
                result = cx.newArray(scope, 0);
                obj = (Scriptable) result;
                obj.put(0, obj, str.substring(index, index + matchlen));
            }
            if (this.re.parenCount == 0) {
                res.parens = null;
                res.lastParen = SubString.emptySubString;
            } else {
                SubString parsub = null;
                res.parens = new SubString[this.re.parenCount];
                for (int num = 0; num < this.re.parenCount; num++) {
                    int cap_index = gData.parensIndex(num);
                    if (cap_index != -1) {
                        SubString subString = new SubString(str, cap_index, gData.parensLength(num));
                        res.parens[num] = subString;
                        if (matchType != 0) {
                            obj.put(num + 1, obj, subString.toString());
                        }
                    } else if (matchType != 0) {
                        obj.put(num + 1, obj, Undefined.instance);
                    }
                }
                res.lastParen = parsub;
            }
            if (matchType != 0) {
                obj.put("index", obj, Integer.valueOf(gData.skipped + start));
                obj.put("input", obj, (Object) str);
            }
            if (res.lastMatch == null) {
                res.lastMatch = new SubString();
                res.leftContext = new SubString();
                res.rightContext = new SubString();
            }
            res.lastMatch.str = str;
            res.lastMatch.index = index;
            res.lastMatch.length = matchlen;
            res.leftContext.str = str;
            if (cx.getLanguageVersion() == 120) {
                res.leftContext.index = start;
                res.leftContext.length = gData.skipped;
            } else {
                res.leftContext.index = 0;
                res.leftContext.length = gData.skipped + start;
            }
            res.rightContext.str = str;
            res.rightContext.index = ep;
            res.rightContext.length = end - ep;
            return result;
        } else if (matchType != 2) {
            return null;
        } else {
            return Undefined.instance;
        }
    }

    /* access modifiers changed from: 0000 */
    public int getFlags() {
        return this.re.flags;
    }

    private static void reportWarning(Context cx, String messageId, String arg) {
        if (cx.hasFeature(11)) {
            Context.reportWarning(ScriptRuntime.getMessage1(messageId, arg));
        }
    }

    private static void reportError(String messageId, String arg) {
        throw ScriptRuntime.constructError("SyntaxError", ScriptRuntime.getMessage1(messageId, arg));
    }

    /* access modifiers changed from: protected */
    public int getMaxInstanceId() {
        return 5;
    }

    /* access modifiers changed from: protected */
    public int findInstanceIdInfo(String s) {
        int id = 0;
        String X = null;
        int s_length = s.length();
        int c;
        if (s_length == 6) {
            c = s.charAt(0);
            if (c == 103) {
                X = "global";
                id = 3;
            } else if (c == 115) {
                X = "source";
                id = 2;
            }
        } else if (s_length == 9) {
            c = s.charAt(0);
            if (c == 108) {
                X = "lastIndex";
                id = 1;
            } else if (c == 109) {
                X = "multiline";
                id = 5;
            }
        } else if (s_length == 10) {
            X = "ignoreCase";
            id = 4;
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
                attr = this.lastIndexAttr;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                attr = 7;
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
                return "lastIndex";
            case 2:
                return "source";
            case 3:
                return "global";
            case 4:
                return "ignoreCase";
            case 5:
                return "multiline";
            default:
                return super.getInstanceIdName(id);
        }
    }

    /* access modifiers changed from: protected */
    public Object getInstanceIdValue(int id) {
        boolean z = true;
        switch (id) {
            case 1:
                return this.lastIndex;
            case 2:
                return new String(this.re.source);
            case 3:
                if ((this.re.flags & 1) == 0) {
                    z = false;
                }
                return ScriptRuntime.wrapBoolean(z);
            case 4:
                if ((this.re.flags & 2) == 0) {
                    z = false;
                }
                return ScriptRuntime.wrapBoolean(z);
            case 5:
                if ((this.re.flags & 4) == 0) {
                    z = false;
                }
                return ScriptRuntime.wrapBoolean(z);
            default:
                return super.getInstanceIdValue(id);
        }
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdValue(int id, Object value) {
        switch (id) {
            case 1:
                this.lastIndex = value;
                return;
            case 2:
            case 3:
            case 4:
            case 5:
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
                this.lastIndexAttr = attr;
                return;
            default:
                super.setInstanceIdAttributes(id, attr);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        int arity;
        String s;
        switch (id) {
            case 1:
                arity = 2;
                s = "compile";
                break;
            case 2:
                arity = 0;
                s = "toString";
                break;
            case 3:
                arity = 0;
                s = "toSource";
                break;
            case 4:
                arity = 1;
                s = "exec";
                break;
            case 5:
                arity = 1;
                s = "test";
                break;
            case 6:
                arity = 1;
                s = "prefix";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(REGEXP_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(REGEXP_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        switch (id) {
            case 1:
                return realThis(thisObj, f).compile(cx, scope, args);
            case 2:
            case 3:
                return realThis(thisObj, f).toString();
            case 4:
                return realThis(thisObj, f).execSub(cx, scope, args, 1);
            case 5:
                return Boolean.TRUE.equals(realThis(thisObj, f).execSub(cx, scope, args, 0)) ? Boolean.TRUE : Boolean.FALSE;
            case 6:
                return realThis(thisObj, f).execSub(cx, scope, args, 2);
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    private static NativeRegExp realThis(Scriptable thisObj, IdFunctionObject f) {
        if (thisObj instanceof NativeRegExp) {
            return (NativeRegExp) thisObj;
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int c;
        switch (s.length()) {
            case 4:
                c = s.charAt(0);
                if (c != 101) {
                    if (c == 116) {
                        X = "test";
                        id = 5;
                        break;
                    }
                }
                X = "exec";
                id = 4;
                break;
                break;
            case 6:
                X = "prefix";
                id = 6;
                break;
            case 7:
                X = "compile";
                id = 1;
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
                id = 3;
                break;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
