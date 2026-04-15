package org.mozilla.javascript;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class NativeJavaMethod extends BaseFunction {
    private static final int PREFERENCE_AMBIGUOUS = 3;
    private static final int PREFERENCE_EQUAL = 0;
    private static final int PREFERENCE_FIRST_ARG = 1;
    private static final int PREFERENCE_SECOND_ARG = 2;
    private static final boolean debug = false;
    static final long serialVersionUID = -3440381785576412928L;
    private String functionName;
    MemberBox[] methods;
    private transient CopyOnWriteArrayList<ResolvedOverload> overloadCache;

    NativeJavaMethod(MemberBox[] methods) {
        this.functionName = methods[0].getName();
        this.methods = methods;
    }

    NativeJavaMethod(MemberBox[] methods, String name) {
        this.functionName = name;
        this.methods = methods;
    }

    NativeJavaMethod(MemberBox method, String name) {
        this.functionName = name;
        this.methods = new MemberBox[]{method};
    }

    public NativeJavaMethod(Method method, String name) {
        this(new MemberBox(method), name);
    }

    public String getFunctionName() {
        return this.functionName;
    }

    static String scriptSignature(Object[] values) {
        StringBuilder sig = new StringBuilder();
        for (int i = 0; i != values.length; i++) {
            String s;
            Object value = values[i];
            if (value == null) {
                s = "null";
            } else if (value instanceof Boolean) {
                s = "boolean";
            } else if (value instanceof String) {
                s = "string";
            } else if (value instanceof Number) {
                s = "number";
            } else if (!(value instanceof Scriptable)) {
                s = JavaMembers.javaSignature(value.getClass());
            } else if (value instanceof Undefined) {
                s = "undefined";
            } else if (value instanceof Wrapper) {
                s = ((Wrapper) value).unwrap().getClass().getName();
            } else if (value instanceof Function) {
                s = "function";
            } else {
                s = "object";
            }
            if (i != 0) {
                sig.append(',');
            }
            sig.append(s);
        }
        return sig.toString();
    }

    /* access modifiers changed from: 0000 */
    public String decompile(int indent, int flags) {
        StringBuilder sb = new StringBuilder();
        boolean justbody = (flags & 1) != 0;
        if (!justbody) {
            sb.append("function ");
            sb.append(getFunctionName());
            sb.append("() {");
        }
        sb.append("/*\n");
        sb.append(toString());
        sb.append(justbody ? "*/\n" : "*/}\n");
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int N = this.methods.length;
        for (int i = 0; i != N; i++) {
            if (this.methods[i].isMethod()) {
                Method method = this.methods[i].method();
                sb.append(JavaMembers.javaSignature(method.getReturnType()));
                sb.append(' ');
                sb.append(method.getName());
            } else {
                sb.append(this.methods[i].getName());
            }
            sb.append(JavaMembers.liveConnectSignature(this.methods[i].argTypes));
            sb.append(10);
        }
        return sb.toString();
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (this.methods.length == 0) {
            throw new RuntimeException("No methods defined for call");
        }
        int index = findCachedFunction(cx, args);
        if (index < 0) {
            throw Context.reportRuntimeError1("msg.java.no_such_method", this.methods[0].method().getDeclaringClass().getName() + '.' + getFunctionName() + '(' + scriptSignature(args) + ')');
        }
        Object javaObject;
        MemberBox meth = this.methods[index];
        Class<?>[] argTypes = meth.argTypes;
        int i;
        if (meth.vararg) {
            Object varArgs;
            Object[] newArgs = new Object[argTypes.length];
            for (i = 0; i < argTypes.length - 1; i++) {
                newArgs[i] = Context.jsToJava(args[i], argTypes[i]);
            }
            if (args.length == argTypes.length && (args[args.length - 1] == null || (args[args.length - 1] instanceof NativeArray) || (args[args.length - 1] instanceof NativeJavaArray))) {
                varArgs = Context.jsToJava(args[args.length - 1], argTypes[argTypes.length - 1]);
            } else {
                Class<?> componentType = argTypes[argTypes.length - 1].getComponentType();
                varArgs = Array.newInstance(componentType, (args.length - argTypes.length) + 1);
                for (i = 0; i < Array.getLength(varArgs); i++) {
                    Array.set(varArgs, i, Context.jsToJava(args[(argTypes.length - 1) + i], componentType));
                }
            }
            newArgs[argTypes.length - 1] = varArgs;
            args = newArgs;
        } else {
            Object[] origArgs = args;
            for (i = 0; i < args.length; i++) {
                Object arg = args[i];
                Object coerced = Context.jsToJava(arg, argTypes[i]);
                if (coerced != arg) {
                    if (origArgs == args) {
                        args = (Object[]) args.clone();
                    }
                    args[i] = coerced;
                }
            }
        }
        if (meth.isStatic()) {
            javaObject = null;
        } else {
            Class<?> c = meth.getDeclaringClass();
            for (Scriptable o = thisObj; o != null; o = o.getPrototype()) {
                if (o instanceof Wrapper) {
                    javaObject = ((Wrapper) o).unwrap();
                    if (c.isInstance(javaObject)) {
                    }
                }
            }
            throw Context.reportRuntimeError3("msg.nonjava.method", getFunctionName(), ScriptRuntime.toString((Object) thisObj), c.getName());
        }
        Object retval = meth.invoke(javaObject, args);
        Class<?> staticType = meth.method().getReturnType();
        Object wrapped = cx.getWrapFactory().wrap(cx, scope, retval, staticType);
        if (wrapped == null && staticType == Void.TYPE) {
            return Undefined.instance;
        }
        return wrapped;
    }

    /* access modifiers changed from: 0000 */
    public int findCachedFunction(Context cx, Object[] args) {
        if (this.methods.length <= 1) {
            return findFunction(cx, this.methods, args);
        }
        ResolvedOverload ovl;
        if (this.overloadCache != null) {
            Iterator it = this.overloadCache.iterator();
            while (it.hasNext()) {
                ovl = (ResolvedOverload) it.next();
                if (ovl.matches(args)) {
                    return ovl.index;
                }
            }
        }
        this.overloadCache = new CopyOnWriteArrayList();
        int index = findFunction(cx, this.methods, args);
        if (this.overloadCache.size() >= this.methods.length * 2) {
            return index;
        }
        synchronized (this.overloadCache) {
            ovl = new ResolvedOverload(args, index);
            if (!this.overloadCache.contains(ovl)) {
                this.overloadCache.add(0, ovl);
            }
        }
        return index;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x009e  */
    static int findFunction(org.mozilla.javascript.Context r25, org.mozilla.javascript.MemberBox[] r26, java.lang.Object[] r27) {
        /*
        r0 = r26;
        r0 = r0.length;
        r22 = r0;
        if (r22 != 0) goto L_0x0009;
    L_0x0007:
        r13 = -1;
    L_0x0008:
        return r13;
    L_0x0009:
        r0 = r26;
        r0 = r0.length;
        r22 = r0;
        r23 = 1;
        r0 = r22;
        r1 = r23;
        if (r0 != r1) goto L_0x0056;
    L_0x0016:
        r22 = 0;
        r17 = r26[r22];
        r0 = r17;
        r6 = r0.argTypes;
        r5 = r6.length;
        r0 = r17;
        r0 = r0.vararg;
        r22 = r0;
        if (r22 == 0) goto L_0x0034;
    L_0x0027:
        r5 = r5 + -1;
        r0 = r27;
        r0 = r0.length;
        r22 = r0;
        r0 = r22;
        if (r5 <= r0) goto L_0x003f;
    L_0x0032:
        r13 = -1;
        goto L_0x0008;
    L_0x0034:
        r0 = r27;
        r0 = r0.length;
        r22 = r0;
        r0 = r22;
        if (r5 == r0) goto L_0x003f;
    L_0x003d:
        r13 = -1;
        goto L_0x0008;
    L_0x003f:
        r16 = 0;
    L_0x0041:
        r0 = r16;
        if (r0 == r5) goto L_0x0054;
    L_0x0045:
        r22 = r27[r16];
        r23 = r6[r16];
        r22 = org.mozilla.javascript.NativeJavaObject.canConvert(r22, r23);
        if (r22 != 0) goto L_0x0051;
    L_0x004f:
        r13 = -1;
        goto L_0x0008;
    L_0x0051:
        r16 = r16 + 1;
        goto L_0x0041;
    L_0x0054:
        r13 = 0;
        goto L_0x0008;
    L_0x0056:
        r13 = -1;
        r11 = 0;
        r12 = 0;
        r15 = 0;
    L_0x005a:
        r0 = r26;
        r0 = r0.length;
        r22 = r0;
        r0 = r22;
        if (r15 >= r0) goto L_0x0176;
    L_0x0063:
        r17 = r26[r15];
        r0 = r17;
        r6 = r0.argTypes;
        r5 = r6.length;
        r0 = r17;
        r0 = r0.vararg;
        r22 = r0;
        if (r22 == 0) goto L_0x0080;
    L_0x0072:
        r5 = r5 + -1;
        r0 = r27;
        r0 = r0.length;
        r22 = r0;
        r0 = r22;
        if (r5 <= r0) goto L_0x0089;
    L_0x007d:
        r15 = r15 + 1;
        goto L_0x005a;
    L_0x0080:
        r0 = r27;
        r0 = r0.length;
        r22 = r0;
        r0 = r22;
        if (r5 != r0) goto L_0x007d;
    L_0x0089:
        r16 = 0;
    L_0x008b:
        r0 = r16;
        if (r0 >= r5) goto L_0x009c;
    L_0x008f:
        r22 = r27[r16];
        r23 = r6[r16];
        r22 = org.mozilla.javascript.NativeJavaObject.canConvert(r22, r23);
        if (r22 == 0) goto L_0x007d;
    L_0x0099:
        r16 = r16 + 1;
        goto L_0x008b;
    L_0x009c:
        if (r13 >= 0) goto L_0x00a0;
    L_0x009e:
        r13 = r15;
        goto L_0x007d;
    L_0x00a0:
        r9 = 0;
        r21 = 0;
        r16 = -1;
    L_0x00a5:
        r0 = r16;
        if (r0 == r12) goto L_0x0113;
    L_0x00a9:
        r22 = -1;
        r0 = r16;
        r1 = r22;
        if (r0 != r1) goto L_0x00eb;
    L_0x00b1:
        r8 = r13;
    L_0x00b2:
        r7 = r26[r8];
        r22 = 13;
        r0 = r25;
        r1 = r22;
        r22 = r0.hasFeature(r1);
        if (r22 == 0) goto L_0x00f1;
    L_0x00c0:
        r22 = r7.member();
        r22 = r22.getModifiers();
        r22 = r22 & 1;
        r23 = r17.member();
        r23 = r23.getModifiers();
        r23 = r23 & 1;
        r0 = r22;
        r1 = r23;
        if (r0 == r1) goto L_0x00f1;
    L_0x00da:
        r22 = r7.member();
        r22 = r22.getModifiers();
        r22 = r22 & 1;
        if (r22 != 0) goto L_0x00ee;
    L_0x00e6:
        r9 = r9 + 1;
    L_0x00e8:
        r16 = r16 + 1;
        goto L_0x00a5;
    L_0x00eb:
        r8 = r11[r16];
        goto L_0x00b2;
    L_0x00ee:
        r21 = r21 + 1;
        goto L_0x00e8;
    L_0x00f1:
        r0 = r17;
        r0 = r0.vararg;
        r22 = r0;
        r0 = r7.argTypes;
        r23 = r0;
        r0 = r7.vararg;
        r24 = r0;
        r0 = r27;
        r1 = r22;
        r2 = r23;
        r3 = r24;
        r20 = preferSignature(r0, r6, r1, r2, r3);
        r22 = 3;
        r0 = r20;
        r1 = r22;
        if (r0 != r1) goto L_0x011d;
    L_0x0113:
        r22 = r12 + 1;
        r0 = r22;
        if (r9 != r0) goto L_0x015b;
    L_0x0119:
        r13 = r15;
        r12 = 0;
        goto L_0x007d;
    L_0x011d:
        r22 = 1;
        r0 = r20;
        r1 = r22;
        if (r0 != r1) goto L_0x0128;
    L_0x0125:
        r9 = r9 + 1;
        goto L_0x00e8;
    L_0x0128:
        r22 = 2;
        r0 = r20;
        r1 = r22;
        if (r0 != r1) goto L_0x0133;
    L_0x0130:
        r21 = r21 + 1;
        goto L_0x00e8;
    L_0x0133:
        if (r20 == 0) goto L_0x0138;
    L_0x0135:
        org.mozilla.javascript.Kit.codeBug();
    L_0x0138:
        r22 = r7.isStatic();
        if (r22 == 0) goto L_0x007d;
    L_0x013e:
        r22 = r7.getDeclaringClass();
        r23 = r17.getDeclaringClass();
        r22 = r22.isAssignableFrom(r23);
        if (r22 == 0) goto L_0x007d;
    L_0x014c:
        r22 = -1;
        r0 = r16;
        r1 = r22;
        if (r0 != r1) goto L_0x0157;
    L_0x0154:
        r13 = r15;
        goto L_0x007d;
    L_0x0157:
        r11[r16] = r15;
        goto L_0x007d;
    L_0x015b:
        r22 = r12 + 1;
        r0 = r21;
        r1 = r22;
        if (r0 == r1) goto L_0x007d;
    L_0x0163:
        if (r11 != 0) goto L_0x0170;
    L_0x0165:
        r0 = r26;
        r0 = r0.length;
        r22 = r0;
        r22 = r22 + -1;
        r0 = r22;
        r11 = new int[r0];
    L_0x0170:
        r11[r12] = r15;
        r12 = r12 + 1;
        goto L_0x007d;
    L_0x0176:
        if (r13 >= 0) goto L_0x017b;
    L_0x0178:
        r13 = -1;
        goto L_0x0008;
    L_0x017b:
        if (r12 == 0) goto L_0x0008;
    L_0x017d:
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r16 = -1;
    L_0x0184:
        r0 = r16;
        if (r0 == r12) goto L_0x01a9;
    L_0x0188:
        r22 = -1;
        r0 = r16;
        r1 = r22;
        if (r0 != r1) goto L_0x01a6;
    L_0x0190:
        r8 = r13;
    L_0x0191:
        r22 = "\n    ";
        r0 = r22;
        r10.append(r0);
        r22 = r26[r8];
        r22 = r22.toJavaDeclaration();
        r0 = r22;
        r10.append(r0);
        r16 = r16 + 1;
        goto L_0x0184;
    L_0x01a6:
        r8 = r11[r16];
        goto L_0x0191;
    L_0x01a9:
        r14 = r26[r13];
        r19 = r14.getName();
        r22 = r14.getDeclaringClass();
        r18 = r22.getName();
        r22 = 0;
        r22 = r26[r22];
        r22 = r22.isCtor();
        if (r22 == 0) goto L_0x01d8;
    L_0x01c1:
        r22 = "msg.constructor.ambiguous";
        r23 = scriptSignature(r27);
        r24 = r10.toString();
        r0 = r22;
        r1 = r19;
        r2 = r23;
        r3 = r24;
        r22 = org.mozilla.javascript.Context.reportRuntimeError3(r0, r1, r2, r3);
        throw r22;
    L_0x01d8:
        r22 = "msg.method.ambiguous";
        r23 = scriptSignature(r27);
        r24 = r10.toString();
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r3 = r23;
        r4 = r24;
        r22 = org.mozilla.javascript.Context.reportRuntimeError4(r0, r1, r2, r3, r4);
        throw r22;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeJavaMethod.findFunction(org.mozilla.javascript.Context, org.mozilla.javascript.MemberBox[], java.lang.Object[]):int");
    }

    private static int preferSignature(Object[] args, Class<?>[] sig1, boolean vararg1, Class<?>[] sig2, boolean vararg2) {
        int totalPreference = 0;
        int j = 0;
        while (j < args.length) {
            Class<?> type1 = (!vararg1 || j < sig1.length) ? sig1[j] : sig1[sig1.length - 1];
            Class<?> type2 = (!vararg2 || j < sig2.length) ? sig2[j] : sig2[sig2.length - 1];
            if (type1 != type2) {
                int preference;
                Object arg = args[j];
                int rank1 = NativeJavaObject.getConversionWeight(arg, type1);
                int rank2 = NativeJavaObject.getConversionWeight(arg, type2);
                if (rank1 < rank2) {
                    preference = 1;
                } else if (rank1 > rank2) {
                    preference = 2;
                } else if (rank1 != 0) {
                    preference = 3;
                } else if (type1.isAssignableFrom(type2)) {
                    preference = 2;
                } else if (type2.isAssignableFrom(type1)) {
                    preference = 1;
                } else {
                    preference = 3;
                }
                totalPreference |= preference;
                if (totalPreference == 3) {
                    break;
                }
            }
            j++;
        }
        return totalPreference;
    }

    private static void printDebug(String msg, MemberBox member, Object[] args) {
    }
}
