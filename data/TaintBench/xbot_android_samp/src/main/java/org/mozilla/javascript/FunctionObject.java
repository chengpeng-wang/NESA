package org.mozilla.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FunctionObject extends BaseFunction {
    public static final int JAVA_BOOLEAN_TYPE = 3;
    public static final int JAVA_DOUBLE_TYPE = 4;
    public static final int JAVA_INT_TYPE = 2;
    public static final int JAVA_OBJECT_TYPE = 6;
    public static final int JAVA_SCRIPTABLE_TYPE = 5;
    public static final int JAVA_STRING_TYPE = 1;
    public static final int JAVA_UNSUPPORTED_TYPE = 0;
    private static final short VARARGS_CTOR = (short) -2;
    private static final short VARARGS_METHOD = (short) -1;
    private static boolean sawSecurityException = false;
    static final long serialVersionUID = -5332312783643935019L;
    private String functionName;
    private transient boolean hasVoidReturn;
    private boolean isStatic;
    MemberBox member;
    private int parmsLength;
    private transient int returnTypeTag;
    private transient byte[] typeTags;

    public FunctionObject(String name, Member methodOrConstructor, Scriptable scope) {
        if (methodOrConstructor instanceof Constructor) {
            this.member = new MemberBox((Constructor) methodOrConstructor);
            this.isStatic = true;
        } else {
            this.member = new MemberBox((Method) methodOrConstructor);
            this.isStatic = this.member.isStatic();
        }
        String methodName = this.member.getName();
        this.functionName = name;
        Class<?>[] types = this.member.argTypes;
        int arity = types.length;
        if (arity != 4 || (!types[1].isArray() && !types[2].isArray())) {
            this.parmsLength = arity;
            if (arity > 0) {
                this.typeTags = new byte[arity];
                for (int i = 0; i != arity; i++) {
                    int tag = getTypeTag(types[i]);
                    if (tag == 0) {
                        throw Context.reportRuntimeError2("msg.bad.parms", types[i].getName(), methodName);
                    }
                    this.typeTags[i] = (byte) tag;
                }
            }
        } else if (types[1].isArray()) {
            if (this.isStatic && types[0] == ScriptRuntime.ContextClass && types[1].getComponentType() == ScriptRuntime.ObjectClass && types[2] == ScriptRuntime.FunctionClass && types[3] == Boolean.TYPE) {
                this.parmsLength = -2;
            } else {
                throw Context.reportRuntimeError1("msg.varargs.ctor", methodName);
            }
        } else if (this.isStatic && types[0] == ScriptRuntime.ContextClass && types[1] == ScriptRuntime.ScriptableClass && types[2].getComponentType() == ScriptRuntime.ObjectClass && types[3] == ScriptRuntime.FunctionClass) {
            this.parmsLength = -1;
        } else {
            throw Context.reportRuntimeError1("msg.varargs.fun", methodName);
        }
        if (this.member.isMethod()) {
            Class<?> returnType = this.member.method().getReturnType();
            if (returnType == Void.TYPE) {
                this.hasVoidReturn = true;
            } else {
                this.returnTypeTag = getTypeTag(returnType);
            }
        } else {
            Class<?> ctorType = this.member.getDeclaringClass();
            if (!ScriptRuntime.ScriptableClass.isAssignableFrom(ctorType)) {
                throw Context.reportRuntimeError1("msg.bad.ctor.return", ctorType.getName());
            }
        }
        ScriptRuntime.setFunctionProtoAndParent(this, scope);
    }

    public static int getTypeTag(Class<?> type) {
        if (type == ScriptRuntime.StringClass) {
            return 1;
        }
        if (type == ScriptRuntime.IntegerClass || type == Integer.TYPE) {
            return 2;
        }
        if (type == ScriptRuntime.BooleanClass || type == Boolean.TYPE) {
            return 3;
        }
        if (type == ScriptRuntime.DoubleClass || type == Double.TYPE) {
            return 4;
        }
        if (ScriptRuntime.ScriptableClass.isAssignableFrom(type)) {
            return 5;
        }
        if (type == ScriptRuntime.ObjectClass) {
            return 6;
        }
        return 0;
    }

    public static Object convertArg(Context cx, Scriptable scope, Object arg, int typeTag) {
        switch (typeTag) {
            case 1:
                if (arg instanceof String) {
                    return arg;
                }
                return ScriptRuntime.toString(arg);
            case 2:
                if (arg instanceof Integer) {
                    return arg;
                }
                return Integer.valueOf(ScriptRuntime.toInt32(arg));
            case 3:
                if (arg instanceof Boolean) {
                    return arg;
                }
                return ScriptRuntime.toBoolean(arg) ? Boolean.TRUE : Boolean.FALSE;
            case 4:
                if (arg instanceof Double) {
                    return arg;
                }
                return new Double(ScriptRuntime.toNumber(arg));
            case 5:
                return ScriptRuntime.toObjectOrNull(cx, arg, scope);
            case 6:
                return arg;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getArity() {
        return this.parmsLength < 0 ? 1 : this.parmsLength;
    }

    public int getLength() {
        return getArity();
    }

    public String getFunctionName() {
        return this.functionName == null ? "" : this.functionName;
    }

    public Member getMethodOrConstructor() {
        if (this.member.isMethod()) {
            return this.member.method();
        }
        return this.member.ctor();
    }

    static Method findSingleMethod(Method[] methods, String name) {
        Method found = null;
        int N = methods.length;
        for (int i = 0; i != N; i++) {
            Method method = methods[i];
            if (method != null && name.equals(method.getName())) {
                if (found != null) {
                    throw Context.reportRuntimeError2("msg.no.overload", name, method.getDeclaringClass().getName());
                }
                found = method;
            }
        }
        return found;
    }

    static Method[] getMethodList(Class<?> clazz) {
        Method[] methods = null;
        try {
            if (!sawSecurityException) {
                methods = clazz.getDeclaredMethods();
            }
        } catch (SecurityException e) {
            sawSecurityException = true;
        }
        if (methods == null) {
            methods = clazz.getMethods();
        }
        int count = 0;
        int i = 0;
        while (i < methods.length) {
            if (sawSecurityException ? methods[i].getDeclaringClass() != clazz : !Modifier.isPublic(methods[i].getModifiers())) {
                methods[i] = null;
            } else {
                count++;
            }
            i++;
        }
        Method[] result = new Method[count];
        int j = 0;
        for (i = 0; i < methods.length; i++) {
            if (methods[i] != null) {
                int j2 = j + 1;
                result[j] = methods[i];
                j = j2;
            }
        }
        return result;
    }

    public void addAsConstructor(Scriptable scope, Scriptable prototype) {
        initAsConstructor(scope, prototype);
        ScriptableObject.defineProperty(scope, prototype.getClassName(), this, 2);
    }

    /* access modifiers changed from: 0000 */
    public void initAsConstructor(Scriptable scope, Scriptable prototype) {
        ScriptRuntime.setFunctionProtoAndParent(this, scope);
        setImmunePrototypeProperty(prototype);
        prototype.setParentScope(this);
        ScriptableObject.defineProperty(prototype, "constructor", this, 7);
        setParentScope(scope);
    }

    @Deprecated
    public static Object convertArg(Context cx, Scriptable scope, Object arg, Class<?> desired) {
        int tag = getTypeTag(desired);
        if (tag != 0) {
            return convertArg(cx, scope, arg, tag);
        }
        throw Context.reportRuntimeError1("msg.cant.convert", desired.getName());
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        int i;
        Object result;
        boolean checkMethodResult = false;
        int argsLength = args.length;
        for (i = 0; i < argsLength; i++) {
            if (args[i] instanceof ConsString) {
                args[i] = args[i].toString();
            }
        }
        Object[] invokeArgs;
        if (this.parmsLength >= 0) {
            if (!this.isStatic) {
                Class<?> clazz = this.member.getDeclaringClass();
                if (!clazz.isInstance(thisObj)) {
                    boolean compatible = false;
                    if (thisObj == scope) {
                        Scriptable parentScope = getParentScope();
                        if (scope != parentScope) {
                            compatible = clazz.isInstance(parentScope);
                            if (compatible) {
                                thisObj = parentScope;
                            }
                        }
                    }
                    if (!compatible) {
                        throw ScriptRuntime.typeError1("msg.incompat.call", this.functionName);
                    }
                }
            }
            if (this.parmsLength == argsLength) {
                invokeArgs = args;
                for (i = 0; i != this.parmsLength; i++) {
                    Object arg = args[i];
                    Object converted = convertArg(cx, scope, arg, this.typeTags[i]);
                    if (arg != converted) {
                        if (invokeArgs == args) {
                            invokeArgs = (Object[]) args.clone();
                        }
                        invokeArgs[i] = converted;
                    }
                }
            } else if (this.parmsLength == 0) {
                invokeArgs = ScriptRuntime.emptyArgs;
            } else {
                invokeArgs = new Object[this.parmsLength];
                i = 0;
                while (i != this.parmsLength) {
                    invokeArgs[i] = convertArg(cx, scope, i < argsLength ? args[i] : Undefined.instance, this.typeTags[i]);
                    i++;
                }
            }
            if (this.member.isMethod()) {
                result = this.member.invoke(thisObj, invokeArgs);
                checkMethodResult = true;
            } else {
                result = this.member.newInstance(invokeArgs);
            }
        } else if (this.parmsLength == -1) {
            result = this.member.invoke(null, new Object[]{cx, thisObj, args, this});
            checkMethodResult = true;
        } else {
            Boolean b = thisObj == null ? Boolean.TRUE : Boolean.FALSE;
            invokeArgs = new Object[]{cx, args, this, b};
            if (this.member.isCtor()) {
                result = this.member.newInstance(invokeArgs);
            } else {
                result = this.member.invoke(null, invokeArgs);
            }
        }
        if (!checkMethodResult) {
            return result;
        }
        if (this.hasVoidReturn) {
            return Undefined.instance;
        }
        if (this.returnTypeTag == 0) {
            return cx.getWrapFactory().wrap(cx, scope, result, null);
        }
        return result;
    }

    public Scriptable createObject(Context cx, Scriptable scope) {
        if (this.member.isCtor() || this.parmsLength == -2) {
            return null;
        }
        try {
            Scriptable result = (Scriptable) this.member.getDeclaringClass().newInstance();
            result.setPrototype(getClassPrototype());
            result.setParentScope(getParentScope());
            return result;
        } catch (Exception ex) {
            throw Context.throwAsScriptRuntimeEx(ex);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isVarArgsMethod() {
        return this.parmsLength == -1;
    }

    /* access modifiers changed from: 0000 */
    public boolean isVarArgsConstructor() {
        return this.parmsLength == -2;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (this.parmsLength > 0) {
            Class<?>[] types = this.member.argTypes;
            this.typeTags = new byte[this.parmsLength];
            for (int i = 0; i != this.parmsLength; i++) {
                this.typeTags[i] = (byte) getTypeTag(types[i]);
            }
        }
        if (this.member.isMethod()) {
            Class<?> returnType = this.member.method().getReturnType();
            if (returnType == Void.TYPE) {
                this.hasVoidReturn = true;
            } else {
                this.returnTypeTag = getTypeTag(returnType);
            }
        }
    }
}
