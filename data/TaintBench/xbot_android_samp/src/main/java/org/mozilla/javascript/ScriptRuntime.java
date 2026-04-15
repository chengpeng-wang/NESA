package org.mozilla.javascript;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.mozilla.javascript.TopLevel.Builtins;
import org.mozilla.javascript.typedarrays.NativeArrayBuffer;
import org.mozilla.javascript.typedarrays.NativeDataView;
import org.mozilla.javascript.v8dtoa.DoubleConversion;
import org.mozilla.javascript.v8dtoa.FastDtoa;
import org.mozilla.javascript.xml.XMLLib;
import org.mozilla.javascript.xml.XMLObject;

public class ScriptRuntime {
    public static final Class<?> BooleanClass = Kit.classOrNull("java.lang.Boolean");
    public static final Class<?> ByteClass = Kit.classOrNull("java.lang.Byte");
    public static final Class<?> CharacterClass = Kit.classOrNull("java.lang.Character");
    public static final Class<?> ClassClass = Kit.classOrNull("java.lang.Class");
    public static final Class<?> ContextClass = Kit.classOrNull("org.mozilla.javascript.Context");
    public static final Class<?> ContextFactoryClass = Kit.classOrNull("org.mozilla.javascript.ContextFactory");
    private static final String DEFAULT_NS_TAG = "__default_namespace__";
    public static final Class<?> DateClass = Kit.classOrNull("java.util.Date");
    public static final Class<?> DoubleClass = Kit.classOrNull("java.lang.Double");
    public static final int ENUMERATE_ARRAY = 2;
    public static final int ENUMERATE_ARRAY_NO_ITERATOR = 5;
    public static final int ENUMERATE_KEYS = 0;
    public static final int ENUMERATE_KEYS_NO_ITERATOR = 3;
    public static final int ENUMERATE_VALUES = 1;
    public static final int ENUMERATE_VALUES_NO_ITERATOR = 4;
    public static final Class<?> FloatClass = Kit.classOrNull("java.lang.Float");
    public static final Class<?> FunctionClass = Kit.classOrNull("org.mozilla.javascript.Function");
    public static final Class<?> IntegerClass = Kit.classOrNull("java.lang.Integer");
    private static final Object LIBRARY_SCOPE_KEY = "LIBRARY_SCOPE";
    public static final Class<?> LongClass = Kit.classOrNull("java.lang.Long");
    public static final double NaN = Double.longBitsToDouble(9221120237041090560L);
    public static final Double NaNobj = new Double(NaN);
    public static final Class<?> NumberClass = Kit.classOrNull("java.lang.Number");
    public static final Class<?> ObjectClass = Kit.classOrNull("java.lang.Object");
    public static Locale ROOT_LOCALE = new Locale("");
    public static final Class<Scriptable> ScriptableClass = Scriptable.class;
    public static final Class<?> ScriptableObjectClass = Kit.classOrNull("org.mozilla.javascript.ScriptableObject");
    public static final Class<?> ShortClass = Kit.classOrNull("java.lang.Short");
    public static final Class<?> StringClass = Kit.classOrNull("java.lang.String");
    public static final Object[] emptyArgs = new Object[0];
    public static final String[] emptyStrings = new String[0];
    public static MessageProvider messageProvider = new DefaultMessageProvider();
    public static final double negativeZero = Double.longBitsToDouble(Long.MIN_VALUE);

    private static class IdEnumeration implements Serializable {
        private static final long serialVersionUID = 1;
        Object currentId;
        boolean enumNumbers;
        int enumType;
        Object[] ids;
        int index;
        Scriptable iterator;
        Scriptable obj;
        ObjToIntMap used;

        private IdEnumeration() {
        }

        /* synthetic */ IdEnumeration(AnonymousClass1 x0) {
            this();
        }
    }

    public interface MessageProvider {
        String getMessage(String str, Object[] objArr);
    }

    private static class DefaultMessageProvider implements MessageProvider {
        private DefaultMessageProvider() {
        }

        /* synthetic */ DefaultMessageProvider(AnonymousClass1 x0) {
            this();
        }

        public String getMessage(String messageId, Object[] arguments) {
            String defaultResource = "org.mozilla.javascript.resources.Messages";
            Context cx = Context.getCurrentContext();
            try {
                return new MessageFormat(ResourceBundle.getBundle("org.mozilla.javascript.resources.Messages", cx != null ? cx.getLocale() : Locale.getDefault()).getString(messageId)).format(arguments);
            } catch (MissingResourceException e) {
                throw new RuntimeException("no message resource found for message property " + messageId);
            }
        }
    }

    static class NoSuchMethodShim implements Callable {
        String methodName;
        Callable noSuchMethodMethod;

        NoSuchMethodShim(Callable noSuchMethodMethod, String methodName) {
            this.noSuchMethodMethod = noSuchMethodMethod;
            this.methodName = methodName;
        }

        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            return this.noSuchMethodMethod.call(cx, scope, thisObj, new Object[]{this.methodName, ScriptRuntime.newArrayLiteral(args, null, cx, scope)});
        }
    }

    protected ScriptRuntime() {
    }

    @Deprecated
    public static BaseFunction typeErrorThrower() {
        return typeErrorThrower(Context.getCurrentContext());
    }

    public static BaseFunction typeErrorThrower(Context cx) {
        if (cx.typeErrorThrower == null) {
            BaseFunction thrower = new BaseFunction() {
                static final long serialVersionUID = -5891740962154902286L;

                public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    throw ScriptRuntime.typeError0("msg.op.not.allowed");
                }

                public int getLength() {
                    return 0;
                }
            };
            setFunctionProtoAndParent(thrower, cx.topCallScope);
            thrower.preventExtensions();
            cx.typeErrorThrower = thrower;
        }
        return cx.typeErrorThrower;
    }

    public static boolean isRhinoRuntimeType(Class<?> cl) {
        boolean z = false;
        if (!cl.isPrimitive()) {
            if (cl == StringClass || cl == BooleanClass || NumberClass.isAssignableFrom(cl) || ScriptableClass.isAssignableFrom(cl)) {
                z = true;
            }
            return z;
        } else if (cl != Character.TYPE) {
            return true;
        } else {
            return false;
        }
    }

    public static ScriptableObject initSafeStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
        if (scope == null) {
            scope = new NativeObject();
        }
        scope.associateValue(LIBRARY_SCOPE_KEY, scope);
        new ClassCache().associate(scope);
        BaseFunction.init(scope, sealed);
        NativeObject.init(scope, sealed);
        Scriptable objectProto = ScriptableObject.getObjectPrototype(scope);
        ScriptableObject.getClassPrototype(scope, "Function").setPrototype(objectProto);
        if (scope.getPrototype() == null) {
            scope.setPrototype(objectProto);
        }
        NativeError.init(scope, sealed);
        NativeGlobal.init(cx, scope, sealed);
        NativeArray.init(scope, sealed);
        if (cx.getOptimizationLevel() > 0) {
            NativeArray.setMaximumInitialCapacity(200000);
        }
        NativeString.init(scope, sealed);
        NativeBoolean.init(scope, sealed);
        NativeNumber.init(scope, sealed);
        NativeDate.init(scope, sealed);
        NativeMath.init(scope, sealed);
        NativeJSON.init(scope, sealed);
        NativeWith.init(scope, sealed);
        NativeCall.init(scope, sealed);
        NativeScript.init(scope, sealed);
        NativeIterator.init(scope, sealed);
        boolean withXml = cx.hasFeature(6) && cx.getE4xImplementationFactory() != null;
        LazilyLoadedCtor lazilyLoadedCtor = new LazilyLoadedCtor(scope, "RegExp", "org.mozilla.javascript.regexp.NativeRegExp", sealed, true);
        lazilyLoadedCtor = new LazilyLoadedCtor(scope, "Continuation", "org.mozilla.javascript.NativeContinuation", sealed, true);
        if (withXml) {
            String xmlImpl = cx.getE4xImplementationFactory().getImplementationClassName();
            lazilyLoadedCtor = new LazilyLoadedCtor(scope, "XML", xmlImpl, sealed, true);
            lazilyLoadedCtor = new LazilyLoadedCtor(scope, "XMLList", xmlImpl, sealed, true);
            lazilyLoadedCtor = new LazilyLoadedCtor(scope, "Namespace", xmlImpl, sealed, true);
            lazilyLoadedCtor = new LazilyLoadedCtor(scope, "QName", xmlImpl, sealed, true);
        }
        if (cx.getLanguageVersion() >= 180 && cx.hasFeature(14)) {
            LazilyLoadedCtor lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, NativeArrayBuffer.CLASS_NAME, "org.mozilla.javascript.typedarrays.NativeArrayBuffer", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Int8Array", "org.mozilla.javascript.typedarrays.NativeInt8Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Uint8Array", "org.mozilla.javascript.typedarrays.NativeUint8Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Uint8ClampedArray", "org.mozilla.javascript.typedarrays.NativeUint8ClampedArray", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Int16Array", "org.mozilla.javascript.typedarrays.NativeInt16Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Uint16Array", "org.mozilla.javascript.typedarrays.NativeUint16Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Int32Array", "org.mozilla.javascript.typedarrays.NativeInt32Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Uint32Array", "org.mozilla.javascript.typedarrays.NativeUint32Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Float32Array", "org.mozilla.javascript.typedarrays.NativeFloat32Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, "Float64Array", "org.mozilla.javascript.typedarrays.NativeFloat64Array", sealed, true);
            lazilyLoadedCtor2 = new LazilyLoadedCtor(scope, NativeDataView.CLASS_NAME, "org.mozilla.javascript.typedarrays.NativeDataView", sealed, true);
        }
        if (scope instanceof TopLevel) {
            ((TopLevel) scope).cacheBuiltins();
        }
        return scope;
    }

    public static ScriptableObject initStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
        ScriptableObject s = initSafeStandardObjects(cx, scope, sealed);
        LazilyLoadedCtor lazilyLoadedCtor = new LazilyLoadedCtor(s, "Packages", "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
        lazilyLoadedCtor = new LazilyLoadedCtor(s, "getClass", "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
        lazilyLoadedCtor = new LazilyLoadedCtor(s, "JavaAdapter", "org.mozilla.javascript.JavaAdapter", sealed, true);
        lazilyLoadedCtor = new LazilyLoadedCtor(s, "JavaImporter", "org.mozilla.javascript.ImporterTopLevel", sealed, true);
        for (String packageName : getTopPackageNames()) {
            lazilyLoadedCtor = new LazilyLoadedCtor(s, packageName, "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
        }
        return s;
    }

    static String[] getTopPackageNames() {
        if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
            return new String[]{"java", "javax", "org", "com", "edu", "net", "android"};
        }
        return new String[]{"java", "javax", "org", "com", "edu", "net"};
    }

    public static ScriptableObject getLibraryScopeOrNull(Scriptable scope) {
        return (ScriptableObject) ScriptableObject.getTopScopeValue(scope, LIBRARY_SCOPE_KEY);
    }

    public static boolean isJSLineTerminator(int c) {
        if ((57296 & c) != 0) {
            return false;
        }
        if (c == 10 || c == 13 || c == 8232 || c == 8233) {
            return true;
        }
        return false;
    }

    public static boolean isJSWhitespaceOrLineTerminator(int c) {
        return isStrWhiteSpaceChar(c) || isJSLineTerminator(c);
    }

    static boolean isStrWhiteSpaceChar(int c) {
        switch (c) {
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 32:
            case 160:
            case 8232:
            case 8233:
            case 65279:
                return true;
            default:
                return Character.getType(c) == 12;
        }
    }

    public static Boolean wrapBoolean(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Integer wrapInt(int i) {
        return Integer.valueOf(i);
    }

    public static Number wrapNumber(double x) {
        if (x != x) {
            return NaNobj;
        }
        return new Double(x);
    }

    public static boolean toBoolean(Object val) {
        while (!(val instanceof Boolean)) {
            if (val == null || val == Undefined.instance) {
                return false;
            }
            if (val instanceof CharSequence) {
                return ((CharSequence) val).length() != 0;
            } else if (val instanceof Number) {
                double d = ((Number) val).doubleValue();
                if (d != d || d == 0.0d) {
                    return false;
                }
                return true;
            } else if (!(val instanceof Scriptable)) {
                warnAboutNonJSObject(val);
                return true;
            } else if ((val instanceof ScriptableObject) && ((ScriptableObject) val).avoidObjectDetection()) {
                return false;
            } else {
                if (Context.getContext().isVersionECMA1()) {
                    return true;
                }
                val = ((Scriptable) val).getDefaultValue(BooleanClass);
                if (val instanceof Scriptable) {
                    throw errorWithClassName("msg.primitive.expected", val);
                }
            }
        }
        return ((Boolean) val).booleanValue();
    }

    public static double toNumber(Object val) {
        while (!(val instanceof Number)) {
            if (val == null) {
                return 0.0d;
            }
            if (val == Undefined.instance) {
                return NaN;
            }
            if (val instanceof String) {
                return toNumber((String) val);
            }
            if (val instanceof CharSequence) {
                return toNumber(val.toString());
            }
            if (val instanceof Boolean) {
                if (((Boolean) val).booleanValue()) {
                    return 1.0d;
                }
                return 0.0d;
            } else if (val instanceof Scriptable) {
                val = ((Scriptable) val).getDefaultValue(NumberClass);
                if (val instanceof Scriptable) {
                    throw errorWithClassName("msg.primitive.expected", val);
                }
            } else {
                warnAboutNonJSObject(val);
                return NaN;
            }
        }
        return ((Number) val).doubleValue();
    }

    public static double toNumber(Object[] args, int index) {
        return index < args.length ? toNumber(args[index]) : NaN;
    }

    static double stringToNumber(String s, int start, int radix) {
        char digitMax = '9';
        char lowerCaseBound = 'a';
        char upperCaseBound = 'A';
        int len = s.length();
        if (radix < 10) {
            digitMax = (char) ((radix + 48) - 1);
        }
        if (radix > 10) {
            lowerCaseBound = (char) ((radix + 97) - 10);
            upperCaseBound = (char) ((radix + 65) - 10);
        }
        double sum = 0.0d;
        int end = start;
        while (end < len) {
            int newDigit;
            char c = s.charAt(end);
            if ('0' <= c && c <= digitMax) {
                newDigit = c - 48;
            } else if ('a' > c || c >= lowerCaseBound) {
                if ('A' > c || c >= upperCaseBound) {
                    break;
                }
                newDigit = (c - 65) + 10;
            } else {
                newDigit = (c - 97) + 10;
            }
            sum = (((double) radix) * sum) + ((double) newDigit);
            end++;
        }
        if (start == end) {
            return NaN;
        }
        if (sum >= 9.007199254740992E15d) {
            if (radix == 10) {
                try {
                    return Double.parseDouble(s.substring(start, end));
                } catch (NumberFormatException e) {
                    return NaN;
                }
            } else if (radix == 2 || radix == 4 || radix == 8 || radix == 16 || radix == 32) {
                int bitShiftInChar = 1;
                int digit = 0;
                int state = 0;
                int exactBitsLimit = 53;
                double factor = 0.0d;
                boolean bit53 = false;
                boolean bit54 = false;
                while (true) {
                    int start2 = start;
                    if (bitShiftInChar != 1) {
                        start = start2;
                    } else if (start2 == end) {
                        switch (state) {
                            case 0:
                                sum = 0.0d;
                                start = start2;
                                break;
                            case 1:
                            case 2:
                                break;
                            case 3:
                                if ((bit54 & bit53) != 0) {
                                    sum += 1.0d;
                                }
                                sum *= factor;
                                start = start2;
                                break;
                            case 4:
                                if (bit54) {
                                    sum += 1.0d;
                                }
                                sum *= factor;
                                start = start2;
                                break;
                            default:
                                start = start2;
                                break;
                        }
                    } else {
                        start = start2 + 1;
                        digit = s.charAt(start2);
                        if (48 <= digit && digit <= 57) {
                            digit -= 48;
                        } else if (97 > digit || digit > 122) {
                            digit -= 55;
                        } else {
                            digit -= 87;
                        }
                        bitShiftInChar = radix;
                    }
                    bitShiftInChar >>= 1;
                    boolean bit = (digit & bitShiftInChar) != 0;
                    switch (state) {
                        case 0:
                            if (bit) {
                                exactBitsLimit--;
                                sum = 1.0d;
                                state = 1;
                                break;
                            }
                            continue;
                        case 1:
                            sum *= 2.0d;
                            if (bit) {
                                sum += 1.0d;
                            }
                            exactBitsLimit--;
                            if (exactBitsLimit == 0) {
                                bit53 = bit;
                                state = 2;
                                break;
                            }
                            continue;
                        case 2:
                            bit54 = bit;
                            factor = 2.0d;
                            state = 3;
                            continue;
                        case 3:
                            if (bit) {
                                state = 4;
                                break;
                            }
                            break;
                        case 4:
                            break;
                        default:
                            break;
                    }
                    factor *= 2.0d;
                }
            }
        }
        return sum;
    }

    public static double toNumber(String s) {
        int len = s.length();
        int start = 0;
        while (start != len) {
            char startChar = s.charAt(start);
            if (isStrWhiteSpaceChar(startChar)) {
                start++;
            } else {
                char endChar;
                if (startChar == '0') {
                    if (start + 2 < len) {
                        int c1 = s.charAt(start + 1);
                        if (c1 == 120 || c1 == 88) {
                            return stringToNumber(s, start + 2, 16);
                        }
                    }
                } else if ((startChar == '+' || startChar == '-') && start + 3 < len && s.charAt(start + 1) == '0') {
                    int c2 = s.charAt(start + 2);
                    if (c2 == 120 || c2 == 88) {
                        double val = stringToNumber(s, start + 3, 16);
                        if (startChar == '-') {
                            return -val;
                        }
                        return val;
                    }
                }
                int end = len - 1;
                while (true) {
                    endChar = s.charAt(end);
                    if (!isStrWhiteSpaceChar(endChar)) {
                        break;
                    }
                    end--;
                }
                if (endChar == 'y') {
                    if (startChar == '+' || startChar == '-') {
                        start++;
                    }
                    if (start + 7 != end || !s.regionMatches(start, "Infinity", 0, 8)) {
                        return NaN;
                    }
                    return startChar == '-' ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                }
                String sub = s.substring(start, end + 1);
                for (int i = sub.length() - 1; i >= 0; i--) {
                    char c = sub.charAt(i);
                    if (('0' > c || c > '9') && c != '.' && c != 'e' && c != 'E' && c != '+' && c != '-') {
                        return NaN;
                    }
                }
                try {
                    return Double.parseDouble(sub);
                } catch (NumberFormatException e) {
                    return NaN;
                }
            }
        }
        return 0.0d;
    }

    public static Object[] padArguments(Object[] args, int count) {
        if (count < args.length) {
            return args;
        }
        Object[] result = new Object[count];
        int i = 0;
        while (i < args.length) {
            result[i] = args[i];
            i++;
        }
        while (i < count) {
            result[i] = Undefined.instance;
            i++;
        }
        return result;
    }

    public static String escapeString(String s) {
        return escapeString(s, '\"');
    }

    public static String escapeString(String s, char escapeQuote) {
        if (!(escapeQuote == '\"' || escapeQuote == '\'')) {
            Kit.codeBug();
        }
        StringBuilder sb = null;
        int L = s.length();
        for (int i = 0; i != L; i++) {
            char c = s.charAt(i);
            if (' ' > c || c > '~' || c == escapeQuote || c == '\\') {
                if (sb == null) {
                    sb = new StringBuilder(L + 3);
                    sb.append(s);
                    sb.setLength(i);
                }
                int escape = -1;
                switch (c) {
                    case 8:
                        escape = 98;
                        break;
                    case 9:
                        escape = 116;
                        break;
                    case 10:
                        escape = 110;
                        break;
                    case 11:
                        escape = 118;
                        break;
                    case 12:
                        escape = 102;
                        break;
                    case 13:
                        escape = 114;
                        break;
                    case ' ':
                        escape = 32;
                        break;
                    case '\\':
                        escape = 92;
                        break;
                }
                if (escape >= 0) {
                    sb.append('\\');
                    sb.append((char) escape);
                } else if (c == escapeQuote) {
                    sb.append('\\');
                    sb.append(escapeQuote);
                } else {
                    int hexSize;
                    if (c < 256) {
                        sb.append("\\x");
                        hexSize = 2;
                    } else {
                        sb.append("\\u");
                        hexSize = 4;
                    }
                    for (int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4) {
                        int digit = (c >> shift) & 15;
                        sb.append((char) (digit < 10 ? digit + 48 : digit + 87));
                    }
                }
            } else if (sb != null) {
                sb.append((char) c);
            }
        }
        return sb == null ? s : sb.toString();
    }

    static boolean isValidIdentifierName(String s) {
        int L = s.length();
        if (L == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
            return false;
        }
        for (int i = 1; i != L; i++) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                return false;
            }
        }
        if (TokenStream.isKeyword(s)) {
            return false;
        }
        return true;
    }

    public static CharSequence toCharSequence(Object val) {
        if (val instanceof NativeString) {
            return ((NativeString) val).toCharSequence();
        }
        return val instanceof CharSequence ? (CharSequence) val : toString(val);
    }

    public static String toString(Object val) {
        while (val != null) {
            if (val == Undefined.instance) {
                return "undefined";
            }
            if (val instanceof String) {
                return (String) val;
            }
            if (val instanceof CharSequence) {
                return val.toString();
            }
            if (val instanceof Number) {
                return numberToString(((Number) val).doubleValue(), 10);
            }
            if (!(val instanceof Scriptable)) {
                return val.toString();
            }
            val = ((Scriptable) val).getDefaultValue(StringClass);
            if (val instanceof Scriptable) {
                throw errorWithClassName("msg.primitive.expected", val);
            }
        }
        return "null";
    }

    static String defaultObjectToString(Scriptable obj) {
        return "[object " + obj.getClassName() + ']';
    }

    public static String toString(Object[] args, int index) {
        return index < args.length ? toString(args[index]) : "undefined";
    }

    public static String toString(double val) {
        return numberToString(val, 10);
    }

    public static String numberToString(double d, int base) {
        if (base < 2 || base > 36) {
            throw Context.reportRuntimeError1("msg.bad.radix", Integer.toString(base));
        } else if (d != d) {
            return "NaN";
        } else {
            if (d == Double.POSITIVE_INFINITY) {
                return "Infinity";
            }
            if (d == Double.NEGATIVE_INFINITY) {
                return "-Infinity";
            }
            if (d == 0.0d) {
                return "0";
            }
            if (base != 10) {
                return DToA.JS_dtobasestr(base, d);
            }
            String result = FastDtoa.numberToString(d);
            if (result != null) {
                return result;
            }
            StringBuilder buffer = new StringBuilder();
            DToA.JS_dtostr(buffer, 0, 0, d);
            return buffer.toString();
        }
    }

    static String uneval(Context cx, Scriptable scope, Object value) {
        if (value == null) {
            return "null";
        }
        if (value == Undefined.instance) {
            return "undefined";
        }
        if (value instanceof CharSequence) {
            String escaped = escapeString(value.toString());
            StringBuilder sb = new StringBuilder(escaped.length() + 2);
            sb.append('\"');
            sb.append(escaped);
            sb.append('\"');
            return sb.toString();
        } else if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            if (d != 0.0d || 1.0d / d >= 0.0d) {
                return toString(d);
            }
            return "-0";
        } else if (value instanceof Boolean) {
            return toString(value);
        } else {
            if (value instanceof Scriptable) {
                Scriptable obj = (Scriptable) value;
                if (ScriptableObject.hasProperty(obj, "toSource")) {
                    Function v = ScriptableObject.getProperty(obj, "toSource");
                    if (v instanceof Function) {
                        return toString(v.call(cx, scope, obj, emptyArgs));
                    }
                }
                return toString(value);
            }
            warnAboutNonJSObject(value);
            return value.toString();
        }
    }

    static String defaultObjectToSource(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        boolean toplevel;
        boolean iterating;
        if (cx.iterating == null) {
            toplevel = true;
            iterating = false;
            cx.iterating = new ObjToIntMap(31);
        } else {
            toplevel = false;
            iterating = cx.iterating.has(thisObj);
        }
        StringBuilder result = new StringBuilder(128);
        if (toplevel) {
            result.append("(");
        }
        result.append('{');
        if (!iterating) {
            try {
                cx.iterating.intern(thisObj);
                Object[] ids = thisObj.getIds();
                for (int i = 0; i < ids.length; i++) {
                    Object id = ids[i];
                    Object value;
                    if (id instanceof Integer) {
                        int intId = ((Integer) id).intValue();
                        value = thisObj.get(intId, thisObj);
                        if (value != Scriptable.NOT_FOUND) {
                            if (i > 0) {
                                result.append(", ");
                            }
                            result.append(intId);
                            result.append(':');
                            result.append(uneval(cx, scope, value));
                        }
                    } else {
                        String strId = (String) id;
                        value = thisObj.get(strId, thisObj);
                        if (value != Scriptable.NOT_FOUND) {
                            if (i > 0) {
                                result.append(", ");
                            }
                            if (isValidIdentifierName(strId)) {
                                result.append(strId);
                            } else {
                                result.append('\'');
                                result.append(escapeString(strId, '\''));
                                result.append('\'');
                            }
                            result.append(':');
                            result.append(uneval(cx, scope, value));
                        }
                    }
                }
            } catch (Throwable th) {
                if (toplevel) {
                    cx.iterating = null;
                }
            }
        }
        if (toplevel) {
            cx.iterating = null;
        }
        result.append('}');
        if (toplevel) {
            result.append(')');
        }
        return result.toString();
    }

    public static Scriptable toObject(Scriptable scope, Object val) {
        if (val instanceof Scriptable) {
            return (Scriptable) val;
        }
        return toObject(Context.getContext(), scope, val);
    }

    @Deprecated
    public static Scriptable toObjectOrNull(Context cx, Object obj) {
        if (obj instanceof Scriptable) {
            return (Scriptable) obj;
        }
        if (obj == null || obj == Undefined.instance) {
            return null;
        }
        return toObject(cx, getTopCallScope(cx), obj);
    }

    public static Scriptable toObjectOrNull(Context cx, Object obj, Scriptable scope) {
        if (obj instanceof Scriptable) {
            return (Scriptable) obj;
        }
        if (obj == null || obj == Undefined.instance) {
            return null;
        }
        return toObject(cx, scope, obj);
    }

    @Deprecated
    public static Scriptable toObject(Scriptable scope, Object val, Class<?> cls) {
        if (val instanceof Scriptable) {
            return (Scriptable) val;
        }
        return toObject(Context.getContext(), scope, val);
    }

    public static Scriptable toObject(Context cx, Scriptable scope, Object val) {
        if (val instanceof Scriptable) {
            return (Scriptable) val;
        }
        Scriptable result;
        if (val instanceof CharSequence) {
            result = new NativeString((CharSequence) val);
            setBuiltinProtoAndParent(result, scope, Builtins.String);
            return result;
        } else if (val instanceof Number) {
            result = new NativeNumber(((Number) val).doubleValue());
            setBuiltinProtoAndParent(result, scope, Builtins.Number);
            return result;
        } else if (val instanceof Boolean) {
            result = new NativeBoolean(((Boolean) val).booleanValue());
            setBuiltinProtoAndParent(result, scope, Builtins.Boolean);
            return result;
        } else if (val == null) {
            throw typeError0("msg.null.to.object");
        } else if (val == Undefined.instance) {
            throw typeError0("msg.undef.to.object");
        } else {
            Object wrapped = cx.getWrapFactory().wrap(cx, scope, val, null);
            if (wrapped instanceof Scriptable) {
                return (Scriptable) wrapped;
            }
            throw errorWithClassName("msg.invalid.type", val);
        }
    }

    @Deprecated
    public static Scriptable toObject(Context cx, Scriptable scope, Object val, Class<?> cls) {
        return toObject(cx, scope, val);
    }

    @Deprecated
    public static Object call(Context cx, Object fun, Object thisArg, Object[] args, Scriptable scope) {
        if (fun instanceof Function) {
            Function function = (Function) fun;
            Scriptable thisObj = toObjectOrNull(cx, thisArg, scope);
            if (thisObj != null) {
                return function.call(cx, scope, thisObj, args);
            }
            throw undefCallError(thisObj, "function");
        }
        throw notFunctionError(toString(fun));
    }

    public static Scriptable newObject(Context cx, Scriptable scope, String constructorName, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = getExistingCtor(cx, scope, constructorName);
        if (args == null) {
            args = emptyArgs;
        }
        return ctor.construct(cx, scope, args);
    }

    public static Scriptable newBuiltinObject(Context cx, Scriptable scope, Builtins type, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = TopLevel.getBuiltinCtor(cx, scope, type);
        if (args == null) {
            args = emptyArgs;
        }
        return ctor.construct(cx, scope, args);
    }

    static Scriptable newNativeError(Context cx, Scriptable scope, NativeErrors type, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = TopLevel.getNativeErrorCtor(cx, scope, type);
        if (args == null) {
            args = emptyArgs;
        }
        return ctor.construct(cx, scope, args);
    }

    public static double toInteger(Object val) {
        return toInteger(toNumber(val));
    }

    public static double toInteger(double d) {
        if (d != d) {
            return 0.0d;
        }
        if (d == 0.0d || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY) {
            return d;
        }
        if (d > 0.0d) {
            return Math.floor(d);
        }
        return Math.ceil(d);
    }

    public static double toInteger(Object[] args, int index) {
        return index < args.length ? toInteger(args[index]) : 0.0d;
    }

    public static int toInt32(Object val) {
        if (val instanceof Integer) {
            return ((Integer) val).intValue();
        }
        return toInt32(toNumber(val));
    }

    public static int toInt32(Object[] args, int index) {
        return index < args.length ? toInt32(args[index]) : 0;
    }

    public static int toInt32(double d) {
        return DoubleConversion.doubleToInt32(d);
    }

    public static long toUint32(double d) {
        return ((long) DoubleConversion.doubleToInt32(d)) & 4294967295L;
    }

    public static long toUint32(Object val) {
        return toUint32(toNumber(val));
    }

    public static char toUint16(Object val) {
        return (char) DoubleConversion.doubleToInt32(toNumber(val));
    }

    public static Object setDefaultNamespace(Object namespace, Context cx) {
        Scriptable scope = cx.currentActivationCall;
        if (scope == null) {
            scope = getTopCallScope(cx);
        }
        Object ns = currentXMLLib(cx).toDefaultXmlNamespace(cx, namespace);
        if (scope.has(DEFAULT_NS_TAG, scope)) {
            scope.put(DEFAULT_NS_TAG, scope, ns);
        } else {
            ScriptableObject.defineProperty(scope, DEFAULT_NS_TAG, ns, 6);
        }
        return Undefined.instance;
    }

    public static Object searchDefaultNamespace(Context cx) {
        Object nsObject;
        Scriptable scope = cx.currentActivationCall;
        if (scope == null) {
            scope = getTopCallScope(cx);
        }
        while (true) {
            Scriptable parent = scope.getParentScope();
            if (parent == null) {
                nsObject = ScriptableObject.getProperty(scope, DEFAULT_NS_TAG);
                if (nsObject == Scriptable.NOT_FOUND) {
                    return null;
                }
            } else {
                nsObject = scope.get(DEFAULT_NS_TAG, scope);
                if (nsObject != Scriptable.NOT_FOUND) {
                    break;
                }
                scope = parent;
            }
        }
        return nsObject;
    }

    public static Object getTopLevelProp(Scriptable scope, String id) {
        return ScriptableObject.getProperty(ScriptableObject.getTopLevelScope(scope), id);
    }

    static Function getExistingCtor(Context cx, Scriptable scope, String constructorName) {
        Object ctorVal = ScriptableObject.getProperty(scope, constructorName);
        if (ctorVal instanceof Function) {
            return (Function) ctorVal;
        }
        if (ctorVal == Scriptable.NOT_FOUND) {
            throw Context.reportRuntimeError1("msg.ctor.not.found", constructorName);
        }
        throw Context.reportRuntimeError1("msg.not.ctor", constructorName);
    }

    public static long indexFromString(String str) {
        int len = str.length();
        if (len <= 0) {
            return -1;
        }
        int i = 0;
        boolean negate = false;
        int c = str.charAt(0);
        if (c == 45 && len > 1) {
            c = str.charAt(1);
            if (c == 48) {
                return -1;
            }
            i = 1;
            negate = true;
        }
        c -= 48;
        if (c < 0 || c > 9) {
            return -1;
        }
        if (len > (negate ? 11 : 10)) {
            return -1;
        }
        int index = -c;
        int oldIndex = 0;
        i++;
        if (index != 0) {
            while (i != len) {
                c = str.charAt(i) - 48;
                if (c < 0 || c > 9) {
                    break;
                }
                oldIndex = index;
                index = (index * 10) - c;
                i++;
            }
        }
        if (i != len) {
            return -1;
        }
        if (oldIndex <= -214748364) {
            if (oldIndex != -214748364) {
                return -1;
            }
            if (c > (negate ? 8 : 7)) {
                return -1;
            }
        }
        if (!negate) {
            index = -index;
        }
        return 4294967295L & ((long) index);
    }

    public static long testUint32String(String str) {
        long j = 0;
        int len = str.length();
        if (1 > len || len > 10) {
            return -1;
        }
        int c = str.charAt(0) - 48;
        if (c == 0) {
            if (len != 1) {
                j = -1;
            }
            return j;
        } else if (1 > c || c > 9) {
            return -1;
        } else {
            long v = (long) c;
            for (int i = 1; i != len; i++) {
                c = str.charAt(i) - 48;
                if (c < 0 || c > 9) {
                    return -1;
                }
                v = (10 * v) + ((long) c);
            }
            if ((v >>> 32) == 0) {
                return v;
            }
            return -1;
        }
    }

    static Object getIndexObject(String s) {
        long indexTest = indexFromString(s);
        if (indexTest >= 0) {
            return Integer.valueOf((int) indexTest);
        }
        return s;
    }

    static Object getIndexObject(double d) {
        int i = (int) d;
        if (((double) i) == d) {
            return Integer.valueOf(i);
        }
        return toString(d);
    }

    static String toStringIdOrIndex(Context cx, Object id) {
        if (id instanceof Number) {
            double d = ((Number) id).doubleValue();
            int index = (int) d;
            if (((double) index) != d) {
                return toString(id);
            }
            storeIndexResult(cx, index);
            return null;
        }
        String s;
        if (id instanceof String) {
            s = (String) id;
        } else {
            s = toString(id);
        }
        long indexTest = indexFromString(s);
        if (indexTest < 0) {
            return s;
        }
        storeIndexResult(cx, (int) indexTest);
        return null;
    }

    @Deprecated
    public static Object getObjectElem(Object obj, Object elem, Context cx) {
        return getObjectElem(obj, elem, cx, getTopCallScope(cx));
    }

    public static Object getObjectElem(Object obj, Object elem, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj != null) {
            return getObjectElem(sobj, elem, cx);
        }
        throw undefReadError(obj, elem);
    }

    public static Object getObjectElem(Scriptable obj, Object elem, Context cx) {
        Object result;
        if (obj instanceof XMLObject) {
            result = ((XMLObject) obj).get(cx, elem);
        } else {
            String s = toStringIdOrIndex(cx, elem);
            if (s == null) {
                result = ScriptableObject.getProperty(obj, lastIndexResult(cx));
            } else {
                result = ScriptableObject.getProperty(obj, s);
            }
        }
        if (result == Scriptable.NOT_FOUND) {
            return Undefined.instance;
        }
        return result;
    }

    @Deprecated
    public static Object getObjectProp(Object obj, String property, Context cx) {
        return getObjectProp(obj, property, cx, getTopCallScope(cx));
    }

    public static Object getObjectProp(Object obj, String property, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj != null) {
            return getObjectProp(sobj, property, cx);
        }
        throw undefReadError(obj, property);
    }

    public static Object getObjectProp(Scriptable obj, String property, Context cx) {
        Object result = ScriptableObject.getProperty(obj, property);
        if (result != Scriptable.NOT_FOUND) {
            return result;
        }
        if (cx.hasFeature(11)) {
            Context.reportWarning(getMessage1("msg.ref.undefined.prop", property));
        }
        return Undefined.instance;
    }

    @Deprecated
    public static Object getObjectPropNoWarn(Object obj, String property, Context cx) {
        return getObjectPropNoWarn(obj, property, cx, getTopCallScope(cx));
    }

    public static Object getObjectPropNoWarn(Object obj, String property, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(obj, property);
        }
        Object result = ScriptableObject.getProperty(sobj, property);
        if (result == Scriptable.NOT_FOUND) {
            return Undefined.instance;
        }
        return result;
    }

    @Deprecated
    public static Object getObjectIndex(Object obj, double dblIndex, Context cx) {
        return getObjectIndex(obj, dblIndex, cx, getTopCallScope(cx));
    }

    public static Object getObjectIndex(Object obj, double dblIndex, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(obj, toString(dblIndex));
        }
        int index = (int) dblIndex;
        if (((double) index) == dblIndex) {
            return getObjectIndex(sobj, index, cx);
        }
        return getObjectProp(sobj, toString(dblIndex), cx);
    }

    public static Object getObjectIndex(Scriptable obj, int index, Context cx) {
        Object result = ScriptableObject.getProperty(obj, index);
        if (result == Scriptable.NOT_FOUND) {
            return Undefined.instance;
        }
        return result;
    }

    @Deprecated
    public static Object setObjectElem(Object obj, Object elem, Object value, Context cx) {
        return setObjectElem(obj, elem, value, cx, getTopCallScope(cx));
    }

    public static Object setObjectElem(Object obj, Object elem, Object value, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj != null) {
            return setObjectElem(sobj, elem, value, cx);
        }
        throw undefWriteError(obj, elem, value);
    }

    public static Object setObjectElem(Scriptable obj, Object elem, Object value, Context cx) {
        if (obj instanceof XMLObject) {
            ((XMLObject) obj).put(cx, elem, value);
        } else {
            String s = toStringIdOrIndex(cx, elem);
            if (s == null) {
                ScriptableObject.putProperty(obj, lastIndexResult(cx), value);
            } else {
                ScriptableObject.putProperty(obj, s, value);
            }
        }
        return value;
    }

    @Deprecated
    public static Object setObjectProp(Object obj, String property, Object value, Context cx) {
        return setObjectProp(obj, property, value, cx, getTopCallScope(cx));
    }

    public static Object setObjectProp(Object obj, String property, Object value, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj != null) {
            return setObjectProp(sobj, property, value, cx);
        }
        throw undefWriteError(obj, property, value);
    }

    public static Object setObjectProp(Scriptable obj, String property, Object value, Context cx) {
        ScriptableObject.putProperty(obj, property, value);
        return value;
    }

    @Deprecated
    public static Object setObjectIndex(Object obj, double dblIndex, Object value, Context cx) {
        return setObjectIndex(obj, dblIndex, value, cx, getTopCallScope(cx));
    }

    public static Object setObjectIndex(Object obj, double dblIndex, Object value, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefWriteError(obj, String.valueOf(dblIndex), value);
        }
        int index = (int) dblIndex;
        if (((double) index) == dblIndex) {
            return setObjectIndex(sobj, index, value, cx);
        }
        return setObjectProp(sobj, toString(dblIndex), value, cx);
    }

    public static Object setObjectIndex(Scriptable obj, int index, Object value, Context cx) {
        ScriptableObject.putProperty(obj, index, value);
        return value;
    }

    public static boolean deleteObjectElem(Scriptable target, Object elem, Context cx) {
        String s = toStringIdOrIndex(cx, elem);
        if (s == null) {
            int index = lastIndexResult(cx);
            target.delete(index);
            if (target.has(index, target)) {
                return false;
            }
            return true;
        }
        target.delete(s);
        if (target.has(s, target)) {
            return false;
        }
        return true;
    }

    public static boolean hasObjectElem(Scriptable target, Object elem, Context cx) {
        String s = toStringIdOrIndex(cx, elem);
        if (s == null) {
            return ScriptableObject.hasProperty(target, lastIndexResult(cx));
        }
        return ScriptableObject.hasProperty(target, s);
    }

    public static Object refGet(Ref ref, Context cx) {
        return ref.get(cx);
    }

    @Deprecated
    public static Object refSet(Ref ref, Object value, Context cx) {
        return refSet(ref, value, cx, getTopCallScope(cx));
    }

    public static Object refSet(Ref ref, Object value, Context cx, Scriptable scope) {
        return ref.set(cx, scope, value);
    }

    public static Object refDel(Ref ref, Context cx) {
        return wrapBoolean(ref.delete(cx));
    }

    static boolean isSpecialProperty(String s) {
        return s.equals("__proto__") || s.equals("__parent__");
    }

    @Deprecated
    public static Ref specialRef(Object obj, String specialProperty, Context cx) {
        return specialRef(obj, specialProperty, cx, getTopCallScope(cx));
    }

    public static Ref specialRef(Object obj, String specialProperty, Context cx, Scriptable scope) {
        return SpecialRef.createSpecial(cx, scope, obj, specialProperty);
    }

    @Deprecated
    public static Object delete(Object obj, Object id, Context cx) {
        return delete(obj, id, cx, false);
    }

    @Deprecated
    public static Object delete(Object obj, Object id, Context cx, boolean isName) {
        return delete(obj, id, cx, getTopCallScope(cx), isName);
    }

    public static Object delete(Object obj, Object id, Context cx, Scriptable scope, boolean isName) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj != null) {
            return wrapBoolean(deleteObjectElem(sobj, id, cx));
        }
        if (isName) {
            return Boolean.TRUE;
        }
        throw undefDeleteError(obj, id);
    }

    public static Object name(Context cx, Scriptable scope, String name) {
        Scriptable parent = scope.getParentScope();
        if (parent != null) {
            return nameOrFunction(cx, scope, parent, name, false);
        }
        Object topScopeName = topScopeName(cx, scope, name);
        if (topScopeName != Scriptable.NOT_FOUND) {
            return topScopeName;
        }
        throw notFoundError(scope, name);
    }

    private static Object nameOrFunction(Context cx, Scriptable scope, Scriptable parentScope, String name, boolean asFunctionCall) {
        Object result;
        Scriptable thisObj = scope;
        XMLObject firstXMLObject = null;
        do {
            if (scope instanceof NativeWith) {
                Scriptable withObj = scope.getPrototype();
                if (withObj instanceof XMLObject) {
                    XMLObject xmlObj = (XMLObject) withObj;
                    if (xmlObj.has(name, xmlObj)) {
                        thisObj = xmlObj;
                        result = xmlObj.get(name, xmlObj);
                        break;
                    }
                    if (firstXMLObject == null) {
                        firstXMLObject = xmlObj;
                    }
                    scope = parentScope;
                    parentScope = parentScope.getParentScope();
                } else {
                    result = ScriptableObject.getProperty(withObj, name);
                    if (result != Scriptable.NOT_FOUND) {
                        thisObj = withObj;
                        break;
                    }
                    scope = parentScope;
                    parentScope = parentScope.getParentScope();
                }
            } else if (scope instanceof NativeCall) {
                result = scope.get(name, scope);
                if (result != Scriptable.NOT_FOUND) {
                    if (asFunctionCall) {
                        thisObj = ScriptableObject.getTopLevelScope(parentScope);
                    }
                }
                scope = parentScope;
                parentScope = parentScope.getParentScope();
            } else {
                result = ScriptableObject.getProperty(scope, name);
                if (result != Scriptable.NOT_FOUND) {
                    thisObj = scope;
                    break;
                }
                scope = parentScope;
                parentScope = parentScope.getParentScope();
            }
        } while (parentScope != null);
        result = topScopeName(cx, scope, name);
        if (result == Scriptable.NOT_FOUND) {
            if (firstXMLObject == null || asFunctionCall) {
                throw notFoundError(scope, name);
            }
            result = firstXMLObject.get(name, firstXMLObject);
        }
        thisObj = scope;
        if (asFunctionCall) {
            if (result instanceof Callable) {
                storeScriptable(cx, thisObj);
            } else {
                throw notFunctionError(result, name);
            }
        }
        return result;
    }

    private static Object topScopeName(Context cx, Scriptable scope, String name) {
        if (cx.useDynamicScope) {
            scope = checkDynamicScope(cx.topCallScope, scope);
        }
        return ScriptableObject.getProperty(scope, name);
    }

    public static Scriptable bind(Context cx, Scriptable scope, String id) {
        Scriptable firstXMLObject = null;
        Scriptable parent = scope.getParentScope();
        if (parent != null) {
            while (scope instanceof NativeWith) {
                Scriptable withObj = scope.getPrototype();
                if (withObj instanceof XMLObject) {
                    XMLObject xmlObject = (XMLObject) withObj;
                    if (xmlObject.has(cx, id)) {
                        return xmlObject;
                    }
                    if (firstXMLObject == null) {
                        firstXMLObject = xmlObject;
                    }
                } else if (ScriptableObject.hasProperty(withObj, id)) {
                    return withObj;
                }
                scope = parent;
                parent = parent.getParentScope();
                if (parent == null) {
                    break;
                }
            }
            while (!ScriptableObject.hasProperty(scope, id)) {
                scope = parent;
                parent = parent.getParentScope();
                if (parent == null) {
                }
            }
            return scope;
        }
        if (cx.useDynamicScope) {
            scope = checkDynamicScope(cx.topCallScope, scope);
        }
        if (ScriptableObject.hasProperty(scope, id)) {
            return scope;
        }
        return firstXMLObject;
    }

    public static Object setName(Scriptable bound, Object value, Context cx, Scriptable scope, String id) {
        if (bound != null) {
            ScriptableObject.putProperty(bound, id, value);
        } else {
            if (cx.hasFeature(11) || cx.hasFeature(8)) {
                Context.reportWarning(getMessage1("msg.assn.create.strict", id));
            }
            bound = ScriptableObject.getTopLevelScope(scope);
            if (cx.useDynamicScope) {
                bound = checkDynamicScope(cx.topCallScope, bound);
            }
            bound.put(id, bound, value);
        }
        return value;
    }

    public static Object strictSetName(Scriptable bound, Object value, Context cx, Scriptable scope, String id) {
        if (bound != null) {
            ScriptableObject.putProperty(bound, id, value);
            return value;
        }
        throw constructError("ReferenceError", "Assignment to undefined \"" + id + "\" in strict mode");
    }

    public static Object setConst(Scriptable bound, Object value, Context cx, String id) {
        if (bound instanceof XMLObject) {
            bound.put(id, bound, value);
        } else {
            ScriptableObject.putConstProperty(bound, id, value);
        }
        return value;
    }

    public static Scriptable toIterator(Context cx, Scriptable scope, Scriptable obj, boolean keyOnly) {
        if (!ScriptableObject.hasProperty(obj, NativeIterator.ITERATOR_PROPERTY_NAME)) {
            return null;
        }
        Callable v = ScriptableObject.getProperty(obj, NativeIterator.ITERATOR_PROPERTY_NAME);
        if (v instanceof Callable) {
            Callable f = v;
            Object[] args = new Object[1];
            args[0] = keyOnly ? Boolean.TRUE : Boolean.FALSE;
            Object v2 = f.call(cx, scope, obj, args);
            if (v2 instanceof Scriptable) {
                return (Scriptable) v2;
            }
            throw typeError0("msg.iterator.primitive");
        }
        throw typeError0("msg.invalid.iterator");
    }

    @Deprecated
    public static Object enumInit(Object value, Context cx, boolean enumValues) {
        return enumInit(value, cx, enumValues ? 1 : 0);
    }

    @Deprecated
    public static Object enumInit(Object value, Context cx, int enumType) {
        return enumInit(value, cx, getTopCallScope(cx), enumType);
    }

    public static Object enumInit(Object value, Context cx, Scriptable scope, int enumType) {
        IdEnumeration x = new IdEnumeration();
        x.obj = toObjectOrNull(cx, value, scope);
        if (x.obj != null) {
            x.enumType = enumType;
            x.iterator = null;
            if (!(enumType == 3 || enumType == 4 || enumType == 5)) {
                x.iterator = toIterator(cx, x.obj.getParentScope(), x.obj, enumType == 0);
            }
            if (x.iterator == null) {
                enumChangeObject(x);
            }
        }
        return x;
    }

    public static void setEnumNumbers(Object enumObj, boolean enumNumbers) {
        ((IdEnumeration) enumObj).enumNumbers = enumNumbers;
    }

    public static Boolean enumNext(Object enumObj) {
        IdEnumeration x = (IdEnumeration) enumObj;
        if (x.iterator != null) {
            Callable v = ScriptableObject.getProperty(x.iterator, "next");
            if (!(v instanceof Callable)) {
                return Boolean.FALSE;
            }
            try {
                x.currentId = v.call(Context.getContext(), x.iterator.getParentScope(), x.iterator, emptyArgs);
                return Boolean.TRUE;
            } catch (JavaScriptException e) {
                if (e.getValue() instanceof StopIteration) {
                    return Boolean.FALSE;
                }
                throw e;
            }
        }
        while (x.obj != null) {
            if (x.index == x.ids.length) {
                x.obj = x.obj.getPrototype();
                enumChangeObject(x);
            } else {
                Object[] objArr = x.ids;
                int i = x.index;
                x.index = i + 1;
                String id = objArr[i];
                if (x.used == null || !x.used.has(id)) {
                    if (id instanceof String) {
                        String strId = id;
                        if (x.obj.has(strId, x.obj)) {
                            x.currentId = strId;
                        }
                    } else {
                        int intId = ((Number) id).intValue();
                        if (x.obj.has(intId, x.obj)) {
                            Object valueOf;
                            if (x.enumNumbers) {
                                valueOf = Integer.valueOf(intId);
                            } else {
                                valueOf = String.valueOf(intId);
                            }
                            x.currentId = valueOf;
                        }
                    }
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public static Object enumId(Object enumObj, Context cx) {
        IdEnumeration x = (IdEnumeration) enumObj;
        if (x.iterator != null) {
            return x.currentId;
        }
        switch (x.enumType) {
            case 0:
            case 3:
                return x.currentId;
            case 1:
            case 4:
                return enumValue(enumObj, cx);
            case 2:
            case 5:
                return cx.newArray(ScriptableObject.getTopLevelScope(x.obj), new Object[]{x.currentId, enumValue(enumObj, cx)});
            default:
                throw Kit.codeBug();
        }
    }

    public static Object enumValue(Object enumObj, Context cx) {
        IdEnumeration x = (IdEnumeration) enumObj;
        String s = toStringIdOrIndex(cx, x.currentId);
        if (s != null) {
            return x.obj.get(s, x.obj);
        }
        return x.obj.get(lastIndexResult(cx), x.obj);
    }

    private static void enumChangeObject(IdEnumeration x) {
        Object[] ids = null;
        while (x.obj != null) {
            ids = x.obj.getIds();
            if (ids.length != 0) {
                break;
            }
            x.obj = x.obj.getPrototype();
        }
        if (!(x.obj == null || x.ids == null)) {
            Object[] previous = x.ids;
            int L = previous.length;
            if (x.used == null) {
                x.used = new ObjToIntMap(L);
            }
            for (int i = 0; i != L; i++) {
                x.used.intern(previous[i]);
            }
        }
        x.ids = ids;
        x.index = 0;
    }

    public static Callable getNameFunctionAndThis(String name, Context cx, Scriptable scope) {
        Scriptable parent = scope.getParentScope();
        if (parent != null) {
            return (Callable) nameOrFunction(cx, scope, parent, name, true);
        }
        Object result = topScopeName(cx, scope, name);
        if (result instanceof Callable) {
            storeScriptable(cx, scope);
            return (Callable) result;
        } else if (result == Scriptable.NOT_FOUND) {
            throw notFoundError(scope, name);
        } else {
            throw notFunctionError(result, name);
        }
    }

    @Deprecated
    public static Callable getElemFunctionAndThis(Object obj, Object elem, Context cx) {
        return getElemFunctionAndThis(obj, elem, cx, getTopCallScope(cx));
    }

    public static Callable getElemFunctionAndThis(Object obj, Object elem, Context cx, Scriptable scope) {
        String str = toStringIdOrIndex(cx, elem);
        if (str != null) {
            return getPropFunctionAndThis(obj, str, cx, scope);
        }
        int index = lastIndexResult(cx);
        Scriptable thisObj = toObjectOrNull(cx, obj, scope);
        if (thisObj == null) {
            throw undefCallError(obj, String.valueOf(index));
        }
        Object value = ScriptableObject.getProperty(thisObj, index);
        if (value instanceof Callable) {
            storeScriptable(cx, thisObj);
            return (Callable) value;
        }
        throw notFunctionError(value, elem);
    }

    @Deprecated
    public static Callable getPropFunctionAndThis(Object obj, String property, Context cx) {
        return getPropFunctionAndThis(obj, property, cx, getTopCallScope(cx));
    }

    public static Callable getPropFunctionAndThis(Object obj, String property, Context cx, Scriptable scope) {
        return getPropFunctionAndThisHelper(obj, property, cx, toObjectOrNull(cx, obj, scope));
    }

    private static Callable getPropFunctionAndThisHelper(Object obj, String property, Context cx, Scriptable thisObj) {
        if (thisObj == null) {
            throw undefCallError(obj, property);
        }
        Object property2 = ScriptableObject.getProperty(thisObj, property);
        if (!(property2 instanceof Callable)) {
            Object noSuchMethod = ScriptableObject.getProperty(thisObj, "__noSuchMethod__");
            if (noSuchMethod instanceof Callable) {
                property2 = new NoSuchMethodShim((Callable) noSuchMethod, property);
            }
        }
        if (property2 instanceof Callable) {
            storeScriptable(cx, thisObj);
            return (Callable) property2;
        }
        throw notFunctionError(thisObj, property2, property);
    }

    public static Callable getValueFunctionAndThis(Object value, Context cx) {
        if (value instanceof Callable) {
            Callable f = (Callable) value;
            Scriptable thisObj = null;
            if (f instanceof Scriptable) {
                thisObj = ((Scriptable) f).getParentScope();
            }
            if (thisObj == null) {
                if (cx.topCallScope == null) {
                    throw new IllegalStateException();
                }
                thisObj = cx.topCallScope;
            }
            if (!(thisObj.getParentScope() == null || (thisObj instanceof NativeWith) || !(thisObj instanceof NativeCall))) {
                thisObj = ScriptableObject.getTopLevelScope(thisObj);
            }
            storeScriptable(cx, thisObj);
            return f;
        }
        throw notFunctionError(value);
    }

    public static Ref callRef(Callable function, Scriptable thisObj, Object[] args, Context cx) {
        if (function instanceof RefCallable) {
            RefCallable rfunction = (RefCallable) function;
            Ref ref = rfunction.refCall(cx, thisObj, args);
            if (ref != null) {
                return ref;
            }
            throw new IllegalStateException(rfunction.getClass().getName() + ".refCall() returned null");
        }
        throw constructError("ReferenceError", getMessage1("msg.no.ref.from.function", toString((Object) function)));
    }

    public static Scriptable newObject(Object fun, Context cx, Scriptable scope, Object[] args) {
        if (fun instanceof Function) {
            return ((Function) fun).construct(cx, scope, args);
        }
        throw notFunctionError(fun);
    }

    public static Object callSpecial(Context cx, Callable fun, Scriptable thisObj, Object[] args, Scriptable scope, Scriptable callerThis, int callType, String filename, int lineNumber) {
        if (callType == 1) {
            if (thisObj.getParentScope() == null && NativeGlobal.isEvalFunction(fun)) {
                return evalSpecial(cx, scope, callerThis, args, filename, lineNumber);
            }
        } else if (callType != 2) {
            throw Kit.codeBug();
        } else if (NativeWith.isWithFunction(fun)) {
            throw Context.reportRuntimeError1("msg.only.from.new", "With");
        }
        return fun.call(cx, scope, thisObj, args);
    }

    public static Object newSpecial(Context cx, Object fun, Object[] args, Scriptable scope, int callType) {
        if (callType == 1) {
            if (NativeGlobal.isEvalFunction(fun)) {
                throw typeError1("msg.not.ctor", "eval");
            }
        } else if (callType != 2) {
            throw Kit.codeBug();
        } else if (NativeWith.isWithFunction(fun)) {
            return NativeWith.newWithSpecial(cx, scope, args);
        }
        return newObject(fun, cx, scope, args);
    }

    public static Object applyOrCall(boolean isApply, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        Object[] callArgs;
        int L = args.length;
        Callable function = getCallable(thisObj);
        Scriptable callThis = null;
        if (L != 0) {
            callThis = toObjectOrNull(cx, args[0], scope);
        }
        if (callThis == null) {
            callThis = getTopCallScope(cx);
        }
        if (isApply) {
            if (L <= 1) {
                callArgs = emptyArgs;
            } else {
                callArgs = getApplyArguments(cx, args[1]);
            }
        } else if (L <= 1) {
            callArgs = emptyArgs;
        } else {
            callArgs = new Object[(L - 1)];
            System.arraycopy(args, 1, callArgs, 0, L - 1);
        }
        return function.call(cx, scope, callThis, callArgs);
    }

    static Object[] getApplyArguments(Context cx, Object arg1) {
        if (arg1 == null || arg1 == Undefined.instance) {
            return emptyArgs;
        }
        if ((arg1 instanceof NativeArray) || (arg1 instanceof Arguments)) {
            return cx.getElements((Scriptable) arg1);
        }
        throw typeError0("msg.arg.isnt.array");
    }

    static Callable getCallable(Scriptable thisObj) {
        if (thisObj instanceof Callable) {
            return (Callable) thisObj;
        }
        Object value = thisObj.getDefaultValue(FunctionClass);
        if (value instanceof Callable) {
            return (Callable) value;
        }
        throw notFunctionError(value, thisObj);
    }

    public static Object evalSpecial(Context cx, Scriptable scope, Object thisArg, Object[] args, String filename, int lineNumber) {
        if (args.length < 1) {
            return Undefined.instance;
        }
        Object x = args[0];
        if (x instanceof CharSequence) {
            if (filename == null) {
                int[] linep = new int[1];
                filename = Context.getSourcePositionFromStack(linep);
                if (filename != null) {
                    lineNumber = linep[0];
                } else {
                    filename = "";
                }
            }
            String sourceName = makeUrlForGeneratedScript(true, filename, lineNumber);
            ErrorReporter reporter = DefaultErrorReporter.forEval(cx.getErrorReporter());
            Evaluator evaluator = Context.createInterpreter();
            if (evaluator == null) {
                throw new JavaScriptException("Interpreter not present", filename, lineNumber);
            }
            Script script = cx.compileString(x.toString(), evaluator, reporter, sourceName, 1, null);
            evaluator.setEvalScriptFlag(script);
            return ((Callable) script).call(cx, scope, (Scriptable) thisArg, emptyArgs);
        } else if (cx.hasFeature(11) || cx.hasFeature(9)) {
            throw Context.reportRuntimeError0("msg.eval.nonstring.strict");
        } else {
            Context.reportWarning(getMessage0("msg.eval.nonstring"));
            return x;
        }
    }

    public static String typeof(Object value) {
        if (value == null) {
            return "object";
        }
        if (value == Undefined.instance) {
            return "undefined";
        }
        if (value instanceof ScriptableObject) {
            return ((ScriptableObject) value).getTypeOf();
        }
        if (value instanceof Scriptable) {
            return value instanceof Callable ? "function" : "object";
        } else {
            if (value instanceof CharSequence) {
                return "string";
            }
            if (value instanceof Number) {
                return "number";
            }
            if (value instanceof Boolean) {
                return "boolean";
            }
            throw errorWithClassName("msg.invalid.type", value);
        }
    }

    public static String typeofName(Scriptable scope, String id) {
        Context cx = Context.getContext();
        Scriptable val = bind(cx, scope, id);
        if (val == null) {
            return "undefined";
        }
        return typeof(getObjectProp(val, id, cx));
    }

    public static Object add(Object val1, Object val2, Context cx) {
        if ((val1 instanceof Number) && (val2 instanceof Number)) {
            return wrapNumber(((Number) val1).doubleValue() + ((Number) val2).doubleValue());
        }
        Object test;
        if (val1 instanceof XMLObject) {
            test = ((XMLObject) val1).addValues(cx, true, val2);
            if (test != Scriptable.NOT_FOUND) {
                return test;
            }
        }
        if (val2 instanceof XMLObject) {
            test = ((XMLObject) val2).addValues(cx, false, val1);
            if (test != Scriptable.NOT_FOUND) {
                return test;
            }
        }
        if (val1 instanceof Scriptable) {
            val1 = ((Scriptable) val1).getDefaultValue(null);
        }
        if (val2 instanceof Scriptable) {
            val2 = ((Scriptable) val2).getDefaultValue(null);
        }
        if ((val1 instanceof CharSequence) || (val2 instanceof CharSequence)) {
            return new ConsString(toCharSequence(val1), toCharSequence(val2));
        }
        if ((val1 instanceof Number) && (val2 instanceof Number)) {
            return wrapNumber(((Number) val1).doubleValue() + ((Number) val2).doubleValue());
        }
        return wrapNumber(toNumber(val1) + toNumber(val2));
    }

    public static CharSequence add(CharSequence val1, Object val2) {
        return new ConsString(val1, toCharSequence(val2));
    }

    public static CharSequence add(Object val1, CharSequence val2) {
        return new ConsString(toCharSequence(val1), val2);
    }

    @Deprecated
    public static Object nameIncrDecr(Scriptable scopeChain, String id, int incrDecrMask) {
        return nameIncrDecr(scopeChain, id, Context.getContext(), incrDecrMask);
    }

    public static Object nameIncrDecr(Scriptable scopeChain, String id, Context cx, int incrDecrMask) {
        do {
            if (cx.useDynamicScope && scopeChain.getParentScope() == null) {
                scopeChain = checkDynamicScope(cx.topCallScope, scopeChain);
            }
            Scriptable target = scopeChain;
            do {
                if ((target instanceof NativeWith) && (target.getPrototype() instanceof XMLObject)) {
                    break;
                }
                Object value = target.get(id, scopeChain);
                if (value != Scriptable.NOT_FOUND) {
                    return doScriptableIncrDecr(target, id, scopeChain, value, incrDecrMask);
                }
                target = target.getPrototype();
            } while (target != null);
            scopeChain = scopeChain.getParentScope();
        } while (scopeChain != null);
        throw notFoundError(scopeChain, id);
    }

    @Deprecated
    public static Object propIncrDecr(Object obj, String id, Context cx, int incrDecrMask) {
        return propIncrDecr(obj, id, cx, getTopCallScope(cx), incrDecrMask);
    }

    public static Object propIncrDecr(Object obj, String id, Context cx, Scriptable scope, int incrDecrMask) {
        Scriptable start = toObjectOrNull(cx, obj, scope);
        if (start == null) {
            throw undefReadError(obj, id);
        }
        Scriptable target = start;
        do {
            Object value = target.get(id, start);
            if (value != Scriptable.NOT_FOUND) {
                return doScriptableIncrDecr(target, id, start, value, incrDecrMask);
            }
            target = target.getPrototype();
        } while (target != null);
        start.put(id, start, NaNobj);
        return NaNobj;
    }

    private static Object doScriptableIncrDecr(Scriptable target, String id, Scriptable protoChainStart, Object value, int incrDecrMask) {
        double number;
        boolean post = (incrDecrMask & 2) != 0;
        if (value instanceof Number) {
            number = ((Number) value).doubleValue();
        } else {
            number = toNumber(value);
            if (post) {
                value = wrapNumber(number);
            }
        }
        if ((incrDecrMask & 1) == 0) {
            number += 1.0d;
        } else {
            number -= 1.0d;
        }
        Object result = wrapNumber(number);
        target.put(id, protoChainStart, result);
        if (post) {
            return value;
        }
        return result;
    }

    @Deprecated
    public static Object elemIncrDecr(Object obj, Object index, Context cx, int incrDecrMask) {
        return elemIncrDecr(obj, index, cx, getTopCallScope(cx), incrDecrMask);
    }

    public static Object elemIncrDecr(Object obj, Object index, Context cx, Scriptable scope, int incrDecrMask) {
        double number;
        Object value = getObjectElem(obj, index, cx, scope);
        boolean post = (incrDecrMask & 2) != 0;
        if (value instanceof Number) {
            number = ((Number) value).doubleValue();
        } else {
            number = toNumber(value);
            if (post) {
                value = wrapNumber(number);
            }
        }
        if ((incrDecrMask & 1) == 0) {
            number += 1.0d;
        } else {
            number -= 1.0d;
        }
        Number result = wrapNumber(number);
        setObjectElem(obj, index, result, cx, scope);
        if (post) {
            return value;
        }
        return result;
    }

    @Deprecated
    public static Object refIncrDecr(Ref ref, Context cx, int incrDecrMask) {
        return refIncrDecr(ref, cx, getTopCallScope(cx), incrDecrMask);
    }

    public static Object refIncrDecr(Ref ref, Context cx, Scriptable scope, int incrDecrMask) {
        double number;
        Object value = ref.get(cx);
        boolean post = (incrDecrMask & 2) != 0;
        if (value instanceof Number) {
            number = ((Number) value).doubleValue();
        } else {
            number = toNumber(value);
            if (post) {
                value = wrapNumber(number);
            }
        }
        if ((incrDecrMask & 1) == 0) {
            number += 1.0d;
        } else {
            number -= 1.0d;
        }
        Number result = wrapNumber(number);
        ref.set(cx, scope, result);
        if (post) {
            return value;
        }
        return result;
    }

    public static Object toPrimitive(Object val) {
        return toPrimitive(val, null);
    }

    public static Object toPrimitive(Object val, Class<?> typeHint) {
        if (!(val instanceof Scriptable)) {
            return val;
        }
        Object result = ((Scriptable) val).getDefaultValue(typeHint);
        if (!(result instanceof Scriptable)) {
            return result;
        }
        throw typeError0("msg.bad.default.value");
    }

    public static boolean eq(Object x, Object y) {
        boolean z = true;
        Object test;
        if (x == null || x == Undefined.instance) {
            if (y == null || y == Undefined.instance) {
                return true;
            }
            if (!(y instanceof ScriptableObject)) {
                return false;
            }
            test = ((ScriptableObject) y).equivalentValues(x);
            if (test != Scriptable.NOT_FOUND) {
                return ((Boolean) test).booleanValue();
            }
            return false;
        } else if (x instanceof Number) {
            return eqNumber(((Number) x).doubleValue(), y);
        } else {
            if (x == y) {
                return true;
            }
            if (x instanceof CharSequence) {
                return eqString((CharSequence) x, y);
            }
            if (x instanceof Boolean) {
                boolean b = ((Boolean) x).booleanValue();
                if (y instanceof Boolean) {
                    return b == ((Boolean) y).booleanValue();
                }
                if (y instanceof ScriptableObject) {
                    test = ((ScriptableObject) y).equivalentValues(x);
                    if (test != Scriptable.NOT_FOUND) {
                        return ((Boolean) test).booleanValue();
                    }
                }
                return eqNumber(b ? 1.0d : 0.0d, y);
            } else if (!(x instanceof Scriptable)) {
                warnAboutNonJSObject(x);
                if (x != y) {
                    z = false;
                }
                return z;
            } else if (y instanceof Scriptable) {
                if (x instanceof ScriptableObject) {
                    test = ((ScriptableObject) x).equivalentValues(y);
                    if (test != Scriptable.NOT_FOUND) {
                        return ((Boolean) test).booleanValue();
                    }
                }
                if (y instanceof ScriptableObject) {
                    test = ((ScriptableObject) y).equivalentValues(x);
                    if (test != Scriptable.NOT_FOUND) {
                        return ((Boolean) test).booleanValue();
                    }
                }
                if (!(x instanceof Wrapper) || !(y instanceof Wrapper)) {
                    return false;
                }
                Object unwrappedX = ((Wrapper) x).unwrap();
                Object unwrappedY = ((Wrapper) y).unwrap();
                if (unwrappedX == unwrappedY || (isPrimitive(unwrappedX) && isPrimitive(unwrappedY) && eq(unwrappedX, unwrappedY))) {
                    return true;
                }
                return false;
            } else if (y instanceof Boolean) {
                double d;
                if (x instanceof ScriptableObject) {
                    test = ((ScriptableObject) x).equivalentValues(y);
                    if (test != Scriptable.NOT_FOUND) {
                        return ((Boolean) test).booleanValue();
                    }
                }
                if (((Boolean) y).booleanValue()) {
                    d = 1.0d;
                } else {
                    d = 0.0d;
                }
                return eqNumber(d, x);
            } else if (y instanceof Number) {
                return eqNumber(((Number) y).doubleValue(), x);
            } else {
                if (y instanceof CharSequence) {
                    return eqString((CharSequence) y, x);
                }
                return false;
            }
        }
    }

    public static boolean isPrimitive(Object obj) {
        return obj == null || obj == Undefined.instance || (obj instanceof Number) || (obj instanceof String) || (obj instanceof Boolean);
    }

    static boolean eqNumber(double x, Object y) {
        while (y != null && y != Undefined.instance) {
            if (y instanceof Number) {
                return x == ((Number) y).doubleValue();
            } else if (y instanceof CharSequence) {
                if (x != toNumber(y)) {
                    return false;
                }
                return true;
            } else if (y instanceof Boolean) {
                if (x != (((Boolean) y).booleanValue() ? 1.0d : 0.0d)) {
                    return false;
                }
                return true;
            } else if (y instanceof Scriptable) {
                if (y instanceof ScriptableObject) {
                    Object test = ((ScriptableObject) y).equivalentValues(wrapNumber(x));
                    if (test != Scriptable.NOT_FOUND) {
                        return ((Boolean) test).booleanValue();
                    }
                }
                y = toPrimitive(y);
            } else {
                warnAboutNonJSObject(y);
                return false;
            }
        }
        return false;
    }

    private static boolean eqString(CharSequence x, Object y) {
        CharSequence y2;
        while (y2 != null && y2 != Undefined.instance) {
            if (y2 instanceof CharSequence) {
                CharSequence c = y2;
                boolean z = x.length() == c.length() && x.toString().equals(c.toString());
                return z;
            } else if (y2 instanceof Number) {
                if (toNumber(x.toString()) != ((Number) y2).doubleValue()) {
                    return false;
                }
                return true;
            } else if (y2 instanceof Boolean) {
                if (toNumber(x.toString()) != (((Boolean) y2).booleanValue() ? 1.0d : 0.0d)) {
                    return false;
                }
                return true;
            } else if (y2 instanceof Scriptable) {
                if (y2 instanceof ScriptableObject) {
                    Object test = ((ScriptableObject) y2).equivalentValues(x.toString());
                    if (test != Scriptable.NOT_FOUND) {
                        return ((Boolean) test).booleanValue();
                    }
                }
                y2 = toPrimitive(y2);
            } else {
                warnAboutNonJSObject(y2);
                return false;
            }
        }
        return false;
    }

    public static boolean shallowEq(Object x, Object y) {
        if (x == y) {
            if (!(x instanceof Number)) {
                return true;
            }
            double d = ((Number) x).doubleValue();
            if (d != d) {
                return false;
            }
            return true;
        } else if (x == null || x == Undefined.instance) {
            return false;
        } else {
            if (x instanceof Number) {
                if (y instanceof Number) {
                    if (((Number) x).doubleValue() != ((Number) y).doubleValue()) {
                        return false;
                    }
                    return true;
                }
            } else if (x instanceof CharSequence) {
                if (y instanceof CharSequence) {
                    return x.toString().equals(y.toString());
                }
            } else if (x instanceof Boolean) {
                if (y instanceof Boolean) {
                    return x.equals(y);
                }
            } else if (!(x instanceof Scriptable)) {
                warnAboutNonJSObject(x);
                if (x != y) {
                    return false;
                }
                return true;
            } else if ((x instanceof Wrapper) && (y instanceof Wrapper)) {
                if (((Wrapper) x).unwrap() != ((Wrapper) y).unwrap()) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    public static boolean instanceOf(Object a, Object b, Context cx) {
        if (!(b instanceof Scriptable)) {
            throw typeError0("msg.instanceof.not.object");
        } else if (a instanceof Scriptable) {
            return ((Scriptable) b).hasInstance((Scriptable) a);
        } else {
            return false;
        }
    }

    public static boolean jsDelegatesTo(Scriptable lhs, Scriptable rhs) {
        for (Scriptable proto = lhs.getPrototype(); proto != null; proto = proto.getPrototype()) {
            if (proto.equals(rhs)) {
                return true;
            }
        }
        return false;
    }

    public static boolean in(Object a, Object b, Context cx) {
        if (b instanceof Scriptable) {
            return hasObjectElem((Scriptable) b, a, cx);
        }
        throw typeError0("msg.in.not.object");
    }

    public static boolean cmp_LT(Object val1, Object val2) {
        double d1;
        double d2;
        boolean z = true;
        if ((val1 instanceof Number) && (val2 instanceof Number)) {
            d1 = ((Number) val1).doubleValue();
            d2 = ((Number) val2).doubleValue();
        } else {
            if (val1 instanceof Scriptable) {
                val1 = ((Scriptable) val1).getDefaultValue(NumberClass);
            }
            if (val2 instanceof Scriptable) {
                val2 = ((Scriptable) val2).getDefaultValue(NumberClass);
            }
            if ((val1 instanceof CharSequence) && (val2 instanceof CharSequence)) {
                return val1.toString().compareTo(val2.toString()) < 0;
            } else {
                d1 = toNumber(val1);
                d2 = toNumber(val2);
            }
        }
        if (d1 >= d2) {
            z = false;
        }
        return z;
    }

    public static boolean cmp_LE(Object val1, Object val2) {
        double d1;
        double d2;
        boolean z = true;
        if ((val1 instanceof Number) && (val2 instanceof Number)) {
            d1 = ((Number) val1).doubleValue();
            d2 = ((Number) val2).doubleValue();
        } else {
            if (val1 instanceof Scriptable) {
                val1 = ((Scriptable) val1).getDefaultValue(NumberClass);
            }
            if (val2 instanceof Scriptable) {
                val2 = ((Scriptable) val2).getDefaultValue(NumberClass);
            }
            if ((val1 instanceof CharSequence) && (val2 instanceof CharSequence)) {
                return val1.toString().compareTo(val2.toString()) <= 0;
            } else {
                d1 = toNumber(val1);
                d2 = toNumber(val2);
            }
        }
        if (d1 > d2) {
            z = false;
        }
        return z;
    }

    public static ScriptableObject getGlobal(Context cx) {
        String GLOBAL_CLASS = "org.mozilla.javascript.tools.shell.Global";
        Class<?> globalClass = Kit.classOrNull("org.mozilla.javascript.tools.shell.Global");
        if (globalClass != null) {
            try {
                return (ScriptableObject) globalClass.getConstructor(new Class[]{ContextClass}).newInstance(new Object[]{cx});
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
        return new ImporterTopLevel(cx);
    }

    public static boolean hasTopCall(Context cx) {
        return cx.topCallScope != null;
    }

    public static Scriptable getTopCallScope(Context cx) {
        Scriptable scope = cx.topCallScope;
        if (scope != null) {
            return scope;
        }
        throw new IllegalStateException();
    }

    public static Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (scope == null) {
            throw new IllegalArgumentException();
        } else if (cx.topCallScope != null) {
            throw new IllegalStateException();
        } else {
            cx.topCallScope = ScriptableObject.getTopLevelScope(scope);
            cx.useDynamicScope = cx.hasFeature(7);
            try {
                Object result = cx.getFactory().doTopCall(callable, cx, scope, thisObj, args);
                cx.topCallScope = null;
                cx.cachedXMLLib = null;
                if (cx.currentActivationCall == null) {
                    return result;
                }
                throw new IllegalStateException();
            } catch (Throwable th) {
                cx.topCallScope = null;
                cx.cachedXMLLib = null;
                if (cx.currentActivationCall != null) {
                    IllegalStateException illegalStateException = new IllegalStateException();
                }
            }
        }
    }

    static Scriptable checkDynamicScope(Scriptable possibleDynamicScope, Scriptable staticTopScope) {
        if (possibleDynamicScope == staticTopScope) {
            return possibleDynamicScope;
        }
        Scriptable proto = possibleDynamicScope;
        do {
            proto = proto.getPrototype();
            if (proto == staticTopScope) {
                return possibleDynamicScope;
            }
        } while (proto != null);
        return staticTopScope;
    }

    public static void addInstructionCount(Context cx, int instructionsToAdd) {
        cx.instructionCount += instructionsToAdd;
        if (cx.instructionCount > cx.instructionThreshold) {
            cx.observeInstructionCount(cx.instructionCount);
            cx.instructionCount = 0;
        }
    }

    public static void initScript(NativeFunction funObj, Scriptable thisObj, Context cx, Scriptable scope, boolean evalScript) {
        if (cx.topCallScope == null) {
            throw new IllegalStateException();
        }
        int varCount = funObj.getParamAndVarCount();
        if (varCount != 0) {
            Scriptable varScope = scope;
            while (varScope instanceof NativeWith) {
                varScope = varScope.getParentScope();
            }
            int i = varCount;
            while (true) {
                int i2 = i;
                i = i2 - 1;
                if (i2 != 0) {
                    String name = funObj.getParamOrVarName(i);
                    boolean isConst = funObj.getParamOrVarConst(i);
                    if (ScriptableObject.hasProperty(scope, name)) {
                        ScriptableObject.redefineProperty(scope, name, isConst);
                    } else if (isConst) {
                        ScriptableObject.defineConstProperty(varScope, name);
                    } else if (evalScript) {
                        varScope.put(name, varScope, Undefined.instance);
                    } else {
                        ScriptableObject.defineProperty(varScope, name, Undefined.instance, 4);
                    }
                } else {
                    return;
                }
            }
        }
    }

    public static Scriptable createFunctionActivation(NativeFunction funObj, Scriptable scope, Object[] args) {
        return new NativeCall(funObj, scope, args);
    }

    public static void enterActivationFunction(Context cx, Scriptable scope) {
        if (cx.topCallScope == null) {
            throw new IllegalStateException();
        }
        NativeCall call = (NativeCall) scope;
        call.parentActivationCall = cx.currentActivationCall;
        cx.currentActivationCall = call;
    }

    public static void exitActivationFunction(Context cx) {
        NativeCall call = cx.currentActivationCall;
        cx.currentActivationCall = call.parentActivationCall;
        call.parentActivationCall = null;
    }

    static NativeCall findFunctionActivation(Context cx, Function f) {
        for (NativeCall call = cx.currentActivationCall; call != null; call = call.parentActivationCall) {
            if (call.function == f) {
                return call;
            }
        }
        return null;
    }

    public static Scriptable newCatchScope(Throwable t, Scriptable lastCatchScope, String exceptionName, Context cx, Scriptable scope) {
        boolean cacheObj;
        Object obj;
        if (t instanceof JavaScriptException) {
            cacheObj = false;
            obj = ((JavaScriptException) t).getValue();
        } else {
            cacheObj = true;
            if (lastCatchScope != null) {
                obj = ((NativeObject) lastCatchScope).getAssociatedValue(t);
                if (obj == null) {
                    Kit.codeBug();
                }
            } else {
                RhinoException re;
                NativeErrors type;
                String errorMsg;
                Throwable javaException = null;
                RhinoException ee;
                if (t instanceof EcmaError) {
                    ee = (EcmaError) t;
                    re = ee;
                    type = NativeErrors.valueOf(ee.getName());
                    errorMsg = ee.getErrorMessage();
                } else if (t instanceof WrappedException) {
                    RhinoException we = (WrappedException) t;
                    re = we;
                    javaException = we.getWrappedException();
                    type = NativeErrors.JavaException;
                    errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
                } else if (t instanceof EvaluatorException) {
                    ee = (EvaluatorException) t;
                    re = ee;
                    type = NativeErrors.InternalError;
                    errorMsg = ee.getMessage();
                } else if (cx.hasFeature(13)) {
                    re = new WrappedException(t);
                    type = NativeErrors.JavaException;
                    errorMsg = t.toString();
                } else {
                    throw Kit.codeBug();
                }
                String sourceUri = re.sourceName();
                if (sourceUri == null) {
                    sourceUri = "";
                }
                Scriptable errorObject = newNativeError(cx, scope, type, re.lineNumber() > 0 ? new Object[]{errorMsg, sourceUri, Integer.valueOf(re.lineNumber())} : new Object[]{errorMsg, sourceUri});
                if (errorObject instanceof NativeError) {
                    ((NativeError) errorObject).setStackProvider(re);
                }
                if (javaException != null && isVisible(cx, javaException)) {
                    ScriptableObject.defineProperty(errorObject, "javaException", cx.getWrapFactory().wrap(cx, scope, javaException, null), 5);
                }
                if (isVisible(cx, re)) {
                    ScriptableObject.defineProperty(errorObject, "rhinoException", cx.getWrapFactory().wrap(cx, scope, re, null), 5);
                }
                Scriptable obj2 = errorObject;
            }
        }
        NativeObject catchScopeObject = new NativeObject();
        catchScopeObject.defineProperty(exceptionName, obj2, 4);
        if (isVisible(cx, t)) {
            catchScopeObject.defineProperty("__exception__", Context.javaToJS(t, scope), 6);
        }
        if (cacheObj) {
            catchScopeObject.associateValue(t, obj2);
        }
        return catchScopeObject;
    }

    public static Scriptable wrapException(Throwable t, Scriptable scope, Context cx) {
        RhinoException re;
        Object errorName;
        String errorMsg;
        Throwable javaException = null;
        RhinoException ee;
        if (t instanceof EcmaError) {
            ee = (EcmaError) t;
            re = ee;
            errorName = ee.getName();
            errorMsg = ee.getErrorMessage();
        } else if (t instanceof WrappedException) {
            RhinoException we = (WrappedException) t;
            re = we;
            javaException = we.getWrappedException();
            errorName = "JavaException";
            errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
        } else if (t instanceof EvaluatorException) {
            ee = (EvaluatorException) t;
            re = ee;
            errorName = "InternalError";
            errorMsg = ee.getMessage();
        } else if (cx.hasFeature(13)) {
            re = new WrappedException(t);
            errorName = "JavaException";
            errorMsg = t.toString();
        } else {
            throw Kit.codeBug();
        }
        String sourceUri = re.sourceName();
        if (sourceUri == null) {
            sourceUri = "";
        }
        Scriptable errorObject = cx.newObject(scope, errorName, re.lineNumber() > 0 ? new Object[]{errorMsg, sourceUri, Integer.valueOf(re.lineNumber())} : new Object[]{errorMsg, sourceUri});
        ScriptableObject.putProperty(errorObject, "name", errorName);
        if (errorObject instanceof NativeError) {
            ((NativeError) errorObject).setStackProvider(re);
        }
        if (javaException != null && isVisible(cx, javaException)) {
            ScriptableObject.defineProperty(errorObject, "javaException", cx.getWrapFactory().wrap(cx, scope, javaException, null), 5);
        }
        if (isVisible(cx, re)) {
            ScriptableObject.defineProperty(errorObject, "rhinoException", cx.getWrapFactory().wrap(cx, scope, re, null), 5);
        }
        return errorObject;
    }

    private static boolean isVisible(Context cx, Object obj) {
        ClassShutter shutter = cx.getClassShutter();
        return shutter == null || shutter.visibleToScripts(obj.getClass().getName());
    }

    public static Scriptable enterWith(Object obj, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw typeError1("msg.undef.with", toString(obj));
        } else if (sobj instanceof XMLObject) {
            return ((XMLObject) sobj).enterWith(scope);
        } else {
            return new NativeWith(scope, sobj);
        }
    }

    public static Scriptable leaveWith(Scriptable scope) {
        return ((NativeWith) scope).getParentScope();
    }

    public static Scriptable enterDotQuery(Object value, Scriptable scope) {
        if (value instanceof XMLObject) {
            return ((XMLObject) value).enterDotQuery(scope);
        }
        throw notXmlError(value);
    }

    public static Object updateDotQuery(boolean value, Scriptable scope) {
        return ((NativeWith) scope).updateDotQuery(value);
    }

    public static Scriptable leaveDotQuery(Scriptable scope) {
        return ((NativeWith) scope).getParentScope();
    }

    public static void setFunctionProtoAndParent(BaseFunction fn, Scriptable scope) {
        fn.setParentScope(scope);
        fn.setPrototype(ScriptableObject.getFunctionPrototype(scope));
    }

    public static void setObjectProtoAndParent(ScriptableObject object, Scriptable scope) {
        scope = ScriptableObject.getTopLevelScope(scope);
        object.setParentScope(scope);
        object.setPrototype(ScriptableObject.getClassPrototype(scope, object.getClassName()));
    }

    public static void setBuiltinProtoAndParent(ScriptableObject object, Scriptable scope, Builtins type) {
        scope = ScriptableObject.getTopLevelScope(scope);
        object.setParentScope(scope);
        object.setPrototype(TopLevel.getBuiltinPrototype(scope, type));
    }

    public static void initFunction(Context cx, Scriptable scope, NativeFunction function, int type, boolean fromEvalCode) {
        String name;
        if (type == 1) {
            name = function.getFunctionName();
            if (name != null && name.length() != 0) {
                if (fromEvalCode) {
                    scope.put(name, scope, (Object) function);
                } else {
                    ScriptableObject.defineProperty(scope, name, function, 4);
                }
            }
        } else if (type == 3) {
            name = function.getFunctionName();
            if (name != null && name.length() != 0) {
                while (scope instanceof NativeWith) {
                    scope = scope.getParentScope();
                }
                scope.put(name, scope, (Object) function);
            }
        } else {
            throw Kit.codeBug();
        }
    }

    public static Scriptable newArrayLiteral(Object[] objects, int[] skipIndices, Context cx, Scriptable scope) {
        int count = objects.length;
        int skipCount = 0;
        if (skipIndices != null) {
            skipCount = skipIndices.length;
        }
        int length = count + skipCount;
        int skip;
        int i;
        int j;
        if (length <= 1 || skipCount * 2 >= length) {
            Scriptable array = cx.newArray(scope, length);
            skip = 0;
            i = 0;
            j = 0;
            while (i != length) {
                if (skip == skipCount || skipIndices[skip] != i) {
                    ScriptableObject.putProperty(array, i, objects[j]);
                    j++;
                } else {
                    skip++;
                }
                i++;
            }
            return array;
        }
        Object[] sparse;
        if (skipCount == 0) {
            sparse = objects;
        } else {
            sparse = new Object[length];
            skip = 0;
            i = 0;
            j = 0;
            while (i != length) {
                if (skip == skipCount || skipIndices[skip] != i) {
                    sparse[i] = objects[j];
                    j++;
                } else {
                    sparse[i] = Scriptable.NOT_FOUND;
                    skip++;
                }
                i++;
            }
        }
        return cx.newArray(scope, sparse);
    }

    @Deprecated
    public static Scriptable newObjectLiteral(Object[] propertyIds, Object[] propertyValues, Context cx, Scriptable scope) {
        return newObjectLiteral(propertyIds, propertyValues, null, cx, scope);
    }

    public static Scriptable newObjectLiteral(Object[] propertyIds, Object[] propertyValues, int[] getterSetters, Context cx, Scriptable scope) {
        Scriptable object = cx.newObject(scope);
        int i = 0;
        int end = propertyIds.length;
        while (i != end) {
            Object id = propertyIds[i];
            int getterSetter = getterSetters == null ? 0 : getterSetters[i];
            Object value = propertyValues[i];
            if (!(id instanceof String)) {
                object.put(((Integer) id).intValue(), object, value);
            } else if (getterSetter != 0) {
                ((ScriptableObject) object).setGetterOrSetter((String) id, 0, (Callable) value, getterSetter == 1);
            } else if (isSpecialProperty((String) id)) {
                specialRef(object, (String) id, cx, scope).set(cx, scope, value);
            } else {
                object.put((String) id, object, value);
            }
            i++;
        }
        return object;
    }

    public static boolean isArrayObject(Object obj) {
        return (obj instanceof NativeArray) || (obj instanceof Arguments);
    }

    public static Object[] getArrayElements(Scriptable object) {
        long longLen = NativeArray.getLengthProperty(Context.getContext(), object);
        if (longLen > 2147483647L) {
            throw new IllegalArgumentException();
        }
        int len = (int) longLen;
        if (len == 0) {
            return emptyArgs;
        }
        Object[] result = new Object[len];
        for (int i = 0; i < len; i++) {
            Object elem = ScriptableObject.getProperty(object, i);
            if (elem == Scriptable.NOT_FOUND) {
                elem = Undefined.instance;
            }
            result[i] = elem;
        }
        return result;
    }

    static void checkDeprecated(Context cx, String name) {
        int version = cx.getLanguageVersion();
        if (version >= 140 || version == 0) {
            String msg = getMessage1("msg.deprec.ctor", name);
            if (version == 0) {
                Context.reportWarning(msg);
                return;
            }
            throw Context.reportRuntimeError(msg);
        }
    }

    public static String getMessage0(String messageId) {
        return getMessage(messageId, null);
    }

    public static String getMessage1(String messageId, Object arg1) {
        return getMessage(messageId, new Object[]{arg1});
    }

    public static String getMessage2(String messageId, Object arg1, Object arg2) {
        return getMessage(messageId, new Object[]{arg1, arg2});
    }

    public static String getMessage3(String messageId, Object arg1, Object arg2, Object arg3) {
        return getMessage(messageId, new Object[]{arg1, arg2, arg3});
    }

    public static String getMessage4(String messageId, Object arg1, Object arg2, Object arg3, Object arg4) {
        return getMessage(messageId, new Object[]{arg1, arg2, arg3, arg4});
    }

    public static String getMessage(String messageId, Object[] arguments) {
        return messageProvider.getMessage(messageId, arguments);
    }

    public static EcmaError constructError(String error, String message) {
        int[] linep = new int[1];
        return constructError(error, message, Context.getSourcePositionFromStack(linep), linep[0], null, 0);
    }

    public static EcmaError constructError(String error, String message, int lineNumberDelta) {
        int[] linep = new int[1];
        String filename = Context.getSourcePositionFromStack(linep);
        if (linep[0] != 0) {
            linep[0] = linep[0] + lineNumberDelta;
        }
        return constructError(error, message, filename, linep[0], null, 0);
    }

    public static EcmaError constructError(String error, String message, String sourceName, int lineNumber, String lineSource, int columnNumber) {
        return new EcmaError(error, message, sourceName, lineNumber, lineSource, columnNumber);
    }

    public static EcmaError typeError(String message) {
        return constructError("TypeError", message);
    }

    public static EcmaError typeError0(String messageId) {
        return typeError(getMessage0(messageId));
    }

    public static EcmaError typeError1(String messageId, String arg1) {
        return typeError(getMessage1(messageId, arg1));
    }

    public static EcmaError typeError2(String messageId, String arg1, String arg2) {
        return typeError(getMessage2(messageId, arg1, arg2));
    }

    public static EcmaError typeError3(String messageId, String arg1, String arg2, String arg3) {
        return typeError(getMessage3(messageId, arg1, arg2, arg3));
    }

    public static RuntimeException undefReadError(Object object, Object id) {
        return typeError2("msg.undef.prop.read", toString(object), toString(id));
    }

    public static RuntimeException undefCallError(Object object, Object id) {
        return typeError2("msg.undef.method.call", toString(object), toString(id));
    }

    public static RuntimeException undefWriteError(Object object, Object id, Object value) {
        return typeError3("msg.undef.prop.write", toString(object), toString(id), toString(value));
    }

    private static RuntimeException undefDeleteError(Object object, Object id) {
        throw typeError2("msg.undef.prop.delete", toString(object), toString(id));
    }

    public static RuntimeException notFoundError(Scriptable object, String property) {
        throw constructError("ReferenceError", getMessage1("msg.is.not.defined", property));
    }

    public static RuntimeException notFunctionError(Object value) {
        return notFunctionError(value, value);
    }

    public static RuntimeException notFunctionError(Object value, Object messageHelper) {
        String msg;
        if (messageHelper == null) {
            msg = "null";
        } else {
            msg = messageHelper.toString();
        }
        if (value == Scriptable.NOT_FOUND) {
            return typeError1("msg.function.not.found", msg);
        }
        return typeError2("msg.isnt.function", msg, typeof(value));
    }

    public static RuntimeException notFunctionError(Object obj, Object value, String propertyName) {
        String objString = toString(obj);
        if (obj instanceof NativeFunction) {
            int curly = objString.indexOf(123, objString.indexOf(41));
            if (curly > -1) {
                objString = objString.substring(0, curly + 1) + "...}";
            }
        }
        if (value == Scriptable.NOT_FOUND) {
            return typeError2("msg.function.not.found.in", propertyName, objString);
        }
        return typeError3("msg.isnt.function.in", propertyName, objString, typeof(value));
    }

    private static RuntimeException notXmlError(Object value) {
        throw typeError1("msg.isnt.xml.object", toString(value));
    }

    private static void warnAboutNonJSObject(Object nonJSObject) {
        String message = "RHINO USAGE WARNING: Missed Context.javaToJS() conversion:\nRhino runtime detected object " + nonJSObject + " of class " + nonJSObject.getClass().getName() + " where it expected String, Number, Boolean or Scriptable instance. Please check your code for missing Context.javaToJS() call.";
        Context.reportWarning(message);
        System.err.println(message);
    }

    public static RegExpProxy getRegExpProxy(Context cx) {
        return cx.getRegExpProxy();
    }

    public static void setRegExpProxy(Context cx, RegExpProxy proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException();
        }
        cx.regExpProxy = proxy;
    }

    public static RegExpProxy checkRegExpProxy(Context cx) {
        RegExpProxy result = getRegExpProxy(cx);
        if (result != null) {
            return result;
        }
        throw Context.reportRuntimeError0("msg.no.regexp");
    }

    public static Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled) {
        return cx.getRegExpProxy().wrapRegExp(cx, scope, compiled);
    }

    private static XMLLib currentXMLLib(Context cx) {
        if (cx.topCallScope == null) {
            throw new IllegalStateException();
        }
        XMLLib xmlLib = cx.cachedXMLLib;
        if (xmlLib == null) {
            xmlLib = XMLLib.extractFromScope(cx.topCallScope);
            if (xmlLib == null) {
                throw new IllegalStateException();
            }
            cx.cachedXMLLib = xmlLib;
        }
        return xmlLib;
    }

    public static String escapeAttributeValue(Object value, Context cx) {
        return currentXMLLib(cx).escapeAttributeValue(value);
    }

    public static String escapeTextValue(Object value, Context cx) {
        return currentXMLLib(cx).escapeTextValue(value);
    }

    public static Ref memberRef(Object obj, Object elem, Context cx, int memberTypeFlags) {
        if (obj instanceof XMLObject) {
            return ((XMLObject) obj).memberRef(cx, elem, memberTypeFlags);
        }
        throw notXmlError(obj);
    }

    public static Ref memberRef(Object obj, Object namespace, Object elem, Context cx, int memberTypeFlags) {
        if (obj instanceof XMLObject) {
            return ((XMLObject) obj).memberRef(cx, namespace, elem, memberTypeFlags);
        }
        throw notXmlError(obj);
    }

    public static Ref nameRef(Object name, Context cx, Scriptable scope, int memberTypeFlags) {
        return currentXMLLib(cx).nameRef(cx, name, scope, memberTypeFlags);
    }

    public static Ref nameRef(Object namespace, Object name, Context cx, Scriptable scope, int memberTypeFlags) {
        return currentXMLLib(cx).nameRef(cx, namespace, name, scope, memberTypeFlags);
    }

    private static void storeIndexResult(Context cx, int index) {
        cx.scratchIndex = index;
    }

    static int lastIndexResult(Context cx) {
        return cx.scratchIndex;
    }

    public static void storeUint32Result(Context cx, long value) {
        if ((value >>> 32) != 0) {
            throw new IllegalArgumentException();
        }
        cx.scratchUint32 = value;
    }

    public static long lastUint32Result(Context cx) {
        long value = cx.scratchUint32;
        if ((value >>> 32) == 0) {
            return value;
        }
        throw new IllegalStateException();
    }

    private static void storeScriptable(Context cx, Scriptable value) {
        if (cx.scratchScriptable != null) {
            throw new IllegalStateException();
        }
        cx.scratchScriptable = value;
    }

    public static Scriptable lastStoredScriptable(Context cx) {
        Scriptable result = cx.scratchScriptable;
        cx.scratchScriptable = null;
        return result;
    }

    static String makeUrlForGeneratedScript(boolean isEval, String masterScriptUrl, int masterScriptLine) {
        if (isEval) {
            return masterScriptUrl + '#' + masterScriptLine + "(eval)";
        }
        return masterScriptUrl + '#' + masterScriptLine + "(Function)";
    }

    static boolean isGeneratedScript(String sourceUrl) {
        return sourceUrl.indexOf("(eval)") >= 0 || sourceUrl.indexOf("(Function)") >= 0;
    }

    private static RuntimeException errorWithClassName(String msg, Object val) {
        return Context.reportRuntimeError1(msg, val.getClass().getName());
    }

    public static JavaScriptException throwError(Context cx, Scriptable scope, String message) {
        int[] linep = new int[]{0};
        return new JavaScriptException(newBuiltinObject(cx, scope, Builtins.Error, new Object[]{message, filename, Integer.valueOf(linep[0])}), Context.getSourcePositionFromStack(linep), linep[0]);
    }
}
