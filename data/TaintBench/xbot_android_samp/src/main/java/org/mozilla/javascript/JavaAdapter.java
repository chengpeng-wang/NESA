package org.mozilla.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import org.mozilla.classfile.ClassFileWriter;
import org.mozilla.javascript.ObjToIntMap.Iterator;

public final class JavaAdapter implements IdFunctionCall {
    private static final Object FTAG = "JavaAdapter";
    private static final int Id_JavaAdapter = 1;

    static class JavaAdapterSignature {
        Class<?>[] interfaces;
        ObjToIntMap names;
        Class<?> superClass;

        JavaAdapterSignature(Class<?> superClass, Class<?>[] interfaces, ObjToIntMap names) {
            this.superClass = superClass;
            this.interfaces = interfaces;
            this.names = names;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof JavaAdapterSignature)) {
                return false;
            }
            JavaAdapterSignature sig = (JavaAdapterSignature) obj;
            if (this.superClass != sig.superClass) {
                return false;
            }
            if (this.interfaces != sig.interfaces) {
                if (this.interfaces.length != sig.interfaces.length) {
                    return false;
                }
                for (int i = 0; i < this.interfaces.length; i++) {
                    if (this.interfaces[i] != sig.interfaces[i]) {
                        return false;
                    }
                }
            }
            if (this.names.size() != sig.names.size()) {
                return false;
            }
            Iterator iter = new Iterator(this.names);
            iter.start();
            while (!iter.done()) {
                String name = (String) iter.getKey();
                int arity = iter.getValue();
                if (arity != sig.names.get(name, arity + 1)) {
                    return false;
                }
                iter.next();
            }
            return true;
        }

        public int hashCode() {
            return (this.superClass.hashCode() + Arrays.hashCode(this.interfaces)) ^ this.names.size();
        }
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        IdFunctionObject ctor = new IdFunctionObject(new JavaAdapter(), FTAG, 1, "JavaAdapter", 1, scope);
        ctor.markAsConstructor(null);
        if (sealed) {
            ctor.sealObject();
        }
        ctor.exportAsScopeProperty();
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (f.hasTag(FTAG) && f.methodId() == 1) {
            return js_createAdapter(cx, scope, args);
        }
        throw f.unknown();
    }

    public static Object convertResult(Object result, Class<?> c) {
        if (result != Undefined.instance || c == ScriptRuntime.ObjectClass || c == ScriptRuntime.StringClass) {
            return Context.jsToJava(result, c);
        }
        return null;
    }

    public static Scriptable createAdapterWrapper(Scriptable obj, Object adapter) {
        NativeJavaObject res = new NativeJavaObject(ScriptableObject.getTopLevelScope(obj), adapter, null, true);
        res.setPrototype(obj);
        return res;
    }

    public static Object getAdapterSelf(Class<?> adapterClass, Object adapter) throws NoSuchFieldException, IllegalAccessException {
        return adapterClass.getDeclaredField("self").get(adapter);
    }

    static Object js_createAdapter(Context cx, Scriptable scope, Object[] args) {
        int N = args.length;
        if (N == 0) {
            throw ScriptRuntime.typeError0("msg.adapter.zero.args");
        }
        int classCount = 0;
        while (classCount < N - 1) {
            Object arg = args[classCount];
            if (arg instanceof NativeObject) {
                break;
            } else if (arg instanceof NativeJavaClass) {
                classCount++;
            } else {
                throw ScriptRuntime.typeError2("msg.not.java.class.arg", String.valueOf(classCount), ScriptRuntime.toString(arg));
            }
        }
        Class<?> superClass = null;
        Object intfs = new Class[classCount];
        int interfaceCount = 0;
        int i = 0;
        while (true) {
            int interfaceCount2 = interfaceCount;
            if (i < classCount) {
                Class<?> c = ((NativeJavaClass) args[i]).getClassObject();
                if (c.isInterface()) {
                    interfaceCount = interfaceCount2 + 1;
                    intfs[interfaceCount2] = c;
                } else if (superClass != null) {
                    throw ScriptRuntime.typeError2("msg.only.one.super", superClass.getName(), c.getName());
                } else {
                    superClass = c;
                    interfaceCount = interfaceCount2;
                }
                i++;
            } else {
                Object adapter;
                if (superClass == null) {
                    superClass = ScriptRuntime.ObjectClass;
                }
                Object interfaces = new Class[interfaceCount2];
                System.arraycopy(intfs, 0, interfaces, 0, interfaceCount2);
                Scriptable obj = ScriptableObject.ensureScriptable(args[classCount]);
                Class<?> adapterClass = getAdapterClass(scope, superClass, interfaces, obj);
                int argsCount = (N - classCount) - 1;
                Object[] ctorArgs;
                if (argsCount > 0) {
                    try {
                        ctorArgs = new Object[(argsCount + 2)];
                        ctorArgs[0] = obj;
                        ctorArgs[1] = cx.getFactory();
                        System.arraycopy(args, classCount + 1, ctorArgs, 2, argsCount);
                        NativeJavaMethod ctors = new NativeJavaClass(scope, adapterClass, true).members.ctors;
                        int index = ctors.findCachedFunction(cx, ctorArgs);
                        if (index < 0) {
                            throw Context.reportRuntimeError2("msg.no.java.ctor", adapterClass.getName(), NativeJavaMethod.scriptSignature(args));
                        }
                        adapter = NativeJavaClass.constructInternal(ctorArgs, ctors.methods[index]);
                    } catch (Exception ex) {
                        throw Context.throwAsScriptRuntimeEx(ex);
                    }
                }
                ctorArgs = new Object[]{obj, cx.getFactory()};
                adapter = adapterClass.getConstructor(new Class[]{ScriptRuntime.ScriptableClass, ScriptRuntime.ContextFactoryClass}).newInstance(ctorArgs);
                Object self = getAdapterSelf(adapterClass, adapter);
                if (self instanceof Wrapper) {
                    Object unwrapped = ((Wrapper) self).unwrap();
                    if (unwrapped instanceof Scriptable) {
                        if (!(unwrapped instanceof ScriptableObject)) {
                            return unwrapped;
                        }
                        ScriptRuntime.setObjectProtoAndParent((ScriptableObject) unwrapped, scope);
                        return unwrapped;
                    }
                }
                return self;
            }
        }
    }

    public static void writeAdapterObject(Object javaObject, ObjectOutputStream out) throws IOException {
        Class<?> cl = javaObject.getClass();
        out.writeObject(cl.getSuperclass().getName());
        Class<?>[] interfaces = cl.getInterfaces();
        String[] interfaceNames = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaceNames[i] = interfaces[i].getName();
        }
        out.writeObject(interfaceNames);
        try {
            out.writeObject(cl.getField("delegee").get(javaObject));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IOException();
        }
    }

    public static Object readAdapterObject(Scriptable self, ObjectInputStream in) throws IOException, ClassNotFoundException {
        ContextFactory factory;
        Context cx = Context.getCurrentContext();
        if (cx != null) {
            factory = cx.getFactory();
        } else {
            factory = null;
        }
        Class<?> superClass = Class.forName((String) in.readObject());
        String[] interfaceNames = (String[]) in.readObject();
        Class<?>[] interfaces = new Class[interfaceNames.length];
        for (int i = 0; i < interfaceNames.length; i++) {
            interfaces[i] = Class.forName(interfaceNames[i]);
        }
        try {
            return getAdapterClass(self, superClass, interfaces, (Scriptable) in.readObject()).getConstructor(new Class[]{ScriptRuntime.ContextFactoryClass, ScriptRuntime.ScriptableClass, ScriptRuntime.ScriptableClass}).newInstance(new Object[]{factory, delegee, self});
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new ClassNotFoundException("adapter");
        }
    }

    private static ObjToIntMap getObjectFunctionNames(Scriptable obj) {
        Object[] ids = ScriptableObject.getPropertyIds(obj);
        ObjToIntMap map = new ObjToIntMap(ids.length);
        for (int i = 0; i != ids.length; i++) {
            if (ids[i] instanceof String) {
                String id = ids[i];
                Function value = ScriptableObject.getProperty(obj, id);
                if (value instanceof Function) {
                    int length = ScriptRuntime.toInt32(ScriptableObject.getProperty((Scriptable) value, "length"));
                    if (length < 0) {
                        length = 0;
                    }
                    map.put(id, length);
                }
            }
        }
        return map;
    }

    private static Class<?> getAdapterClass(Scriptable scope, Class<?> superClass, Class<?>[] interfaces, Scriptable obj) {
        ClassCache cache = ClassCache.get(scope);
        Map<JavaAdapterSignature, Class<?>> generated = cache.getInterfaceAdapterCacheMap();
        ObjToIntMap names = getObjectFunctionNames(obj);
        JavaAdapterSignature sig = new JavaAdapterSignature(superClass, interfaces, names);
        Class<?> adapterClass = (Class) generated.get(sig);
        if (adapterClass == null) {
            String adapterName = "adapter" + cache.newClassSerialNumber();
            adapterClass = loadAdapterClass(adapterName, createAdapterCode(names, adapterName, superClass, interfaces, null));
            if (cache.isCachingEnabled()) {
                generated.put(sig, adapterClass);
            }
        }
        return adapterClass;
    }

    public static byte[] createAdapterCode(ObjToIntMap functionNames, String adapterName, Class<?> superClass, Class<?>[] interfaces, String scriptClassName) {
        int i;
        Method[] methods;
        String methodName;
        Class<?>[] argTypes;
        String methodKey;
        ClassFileWriter cfw = new ClassFileWriter(adapterName, superClass.getName(), "<adapter>");
        cfw.addField("factory", "Lorg/mozilla/javascript/ContextFactory;", (short) 17);
        cfw.addField("delegee", "Lorg/mozilla/javascript/Scriptable;", (short) 17);
        cfw.addField("self", "Lorg/mozilla/javascript/Scriptable;", (short) 17);
        int interfacesCount = interfaces == null ? 0 : interfaces.length;
        for (i = 0; i < interfacesCount; i++) {
            if (interfaces[i] != null) {
                cfw.addInterface(interfaces[i].getName());
            }
        }
        String superName = superClass.getName().replace('.', '/');
        for (Constructor<?> ctor : superClass.getDeclaredConstructors()) {
            int mod = ctor.getModifiers();
            if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                generateCtor(cfw, adapterName, superName, ctor);
            }
        }
        generateSerialCtor(cfw, adapterName, superName);
        if (scriptClassName != null) {
            generateEmptyCtor(cfw, adapterName, superName, scriptClassName);
        }
        ObjToIntMap generatedOverrides = new ObjToIntMap();
        ObjToIntMap generatedMethods = new ObjToIntMap();
        for (i = 0; i < interfacesCount; i++) {
            methods = interfaces[i].getMethods();
            for (Method method : methods) {
                int mods = method.getModifiers();
                if (!(Modifier.isStatic(mods) || Modifier.isFinal(mods))) {
                    methodName = method.getName();
                    argTypes = method.getParameterTypes();
                    if (!functionNames.has(methodName)) {
                        try {
                            superClass.getMethod(methodName, argTypes);
                        } catch (NoSuchMethodException e) {
                        }
                    }
                    methodKey = methodName + getMethodSignature(method, argTypes);
                    if (!generatedOverrides.has(methodKey)) {
                        generateMethod(cfw, adapterName, methodName, argTypes, method.getReturnType(), true);
                        generatedOverrides.put(methodKey, 0);
                        generatedMethods.put(methodName, 0);
                    }
                }
            }
        }
        methods = getOverridableMethods(superClass);
        for (Method method2 : methods) {
            boolean isAbstractMethod = Modifier.isAbstract(method2.getModifiers());
            methodName = method2.getName();
            if (isAbstractMethod || functionNames.has(methodName)) {
                argTypes = method2.getParameterTypes();
                String methodSignature = getMethodSignature(method2, argTypes);
                methodKey = methodName + methodSignature;
                if (!generatedOverrides.has(methodKey)) {
                    generateMethod(cfw, adapterName, methodName, argTypes, method2.getReturnType(), true);
                    generatedOverrides.put(methodKey, 0);
                    generatedMethods.put(methodName, 0);
                    if (!isAbstractMethod) {
                        generateSuper(cfw, adapterName, superName, methodName, methodSignature, argTypes, method2.getReturnType());
                    }
                }
            }
        }
        Iterator iterator = new Iterator(functionNames);
        iterator.start();
        while (!iterator.done()) {
            String functionName = (String) iterator.getKey();
            if (!generatedMethods.has(functionName)) {
                int length = iterator.getValue();
                Class<?>[] parms = new Class[length];
                for (int k = 0; k < length; k++) {
                    parms[k] = ScriptRuntime.ObjectClass;
                }
                generateMethod(cfw, adapterName, functionName, parms, ScriptRuntime.ObjectClass, false);
            }
            iterator.next();
        }
        return cfw.toByteArray();
    }

    static Method[] getOverridableMethods(Class<?> clazz) {
        Class<?> c;
        ArrayList<Method> list = new ArrayList();
        HashSet<String> skip = new HashSet();
        for (c = clazz; c != null; c = c.getSuperclass()) {
            appendOverridableMethods(c, list, skip);
        }
        for (c = clazz; c != null; c = c.getSuperclass()) {
            for (Class<?> intf : c.getInterfaces()) {
                appendOverridableMethods(intf, list, skip);
            }
        }
        return (Method[]) list.toArray(new Method[list.size()]);
    }

    private static void appendOverridableMethods(Class<?> c, ArrayList<Method> list, HashSet<String> skip) {
        Method[] methods = c.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodKey = methods[i].getName() + getMethodSignature(methods[i], methods[i].getParameterTypes());
            if (!skip.contains(methodKey)) {
                int mods = methods[i].getModifiers();
                if (!Modifier.isStatic(mods)) {
                    if (Modifier.isFinal(mods)) {
                        skip.add(methodKey);
                    } else if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
                        list.add(methods[i]);
                        skip.add(methodKey);
                    }
                }
            }
        }
    }

    static Class<?> loadAdapterClass(String className, byte[] classBytes) {
        Object staticDomain;
        Class<?> domainClass = SecurityController.getStaticSecurityDomainClass();
        if (domainClass == CodeSource.class || domainClass == ProtectionDomain.class) {
            ProtectionDomain protectionDomain = SecurityUtilities.getScriptProtectionDomain();
            if (protectionDomain == null) {
                protectionDomain = JavaAdapter.class.getProtectionDomain();
            }
            if (domainClass == CodeSource.class) {
                staticDomain = protectionDomain == null ? null : protectionDomain.getCodeSource();
            } else {
                ProtectionDomain staticDomain2 = protectionDomain;
            }
        } else {
            staticDomain2 = null;
        }
        GeneratedClassLoader loader = SecurityController.createLoader(null, staticDomain2);
        Class<?> result = loader.defineClass(className, classBytes);
        loader.linkClass(result);
        return result;
    }

    public static Function getFunction(Scriptable obj, String functionName) {
        Object x = ScriptableObject.getProperty(obj, functionName);
        if (x == Scriptable.NOT_FOUND) {
            return null;
        }
        if (x instanceof Function) {
            return (Function) x;
        }
        throw ScriptRuntime.notFunctionError(x, functionName);
    }

    public static Object callMethod(ContextFactory factory, Scriptable thisObj, Function f, Object[] args, long argsToWrap) {
        if (f == null) {
            return null;
        }
        if (factory == null) {
            factory = ContextFactory.getGlobal();
        }
        Scriptable scope = f.getParentScope();
        if (argsToWrap == 0) {
            return Context.call(factory, f, scope, thisObj, args);
        }
        Context cx = Context.getCurrentContext();
        if (cx != null) {
            return doCall(cx, scope, thisObj, f, args, argsToWrap);
        }
        final Scriptable scriptable = scope;
        final Scriptable scriptable2 = thisObj;
        final Function function = f;
        final Object[] objArr = args;
        final long j = argsToWrap;
        return factory.call(new ContextAction() {
            public Object run(Context cx) {
                return JavaAdapter.doCall(cx, scriptable, scriptable2, function, objArr, j);
            }
        });
    }

    /* access modifiers changed from: private|static */
    public static Object doCall(Context cx, Scriptable scope, Scriptable thisObj, Function f, Object[] args, long argsToWrap) {
        for (int i = 0; i != args.length; i++) {
            if (0 != (((long) (1 << i)) & argsToWrap)) {
                Object arg = args[i];
                if (!(arg instanceof Scriptable)) {
                    args[i] = cx.getWrapFactory().wrap(cx, scope, arg, null);
                }
            }
        }
        return f.call(cx, scope, thisObj, args);
    }

    public static Scriptable runScript(final Script script) {
        return (Scriptable) ContextFactory.getGlobal().call(new ContextAction() {
            public Object run(Context cx) {
                ScriptableObject global = ScriptRuntime.getGlobal(cx);
                script.exec(cx, global);
                return global;
            }
        });
    }

    private static void generateCtor(ClassFileWriter cfw, String adapterName, String superName, Constructor<?> superCtor) {
        short locals = (short) 3;
        Class<?>[] parameters = superCtor.getParameterTypes();
        if (parameters.length == 0) {
            cfw.startMethod("<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/ContextFactory;)V", (short) 1);
            cfw.add(42);
            cfw.addInvoke(183, superName, "<init>", "()V");
        } else {
            StringBuilder sig = new StringBuilder("(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/ContextFactory;");
            int marker = sig.length();
            for (Class<?> c : parameters) {
                appendTypeString(sig, c);
            }
            sig.append(")V");
            cfw.startMethod("<init>", sig.toString(), (short) 1);
            cfw.add(42);
            short paramOffset = (short) 3;
            for (Class<?> parameter : parameters) {
                paramOffset = (short) (generatePushParam(cfw, paramOffset, parameter) + paramOffset);
            }
            locals = paramOffset;
            sig.delete(1, marker);
            cfw.addInvoke(183, superName, "<init>", sig.toString());
        }
        cfw.add(42);
        cfw.add(43);
        cfw.add(181, adapterName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        cfw.add(42);
        cfw.add(44);
        cfw.add(181, adapterName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        cfw.add(42);
        cfw.add(43);
        cfw.add(42);
        cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "createAdapterWrapper", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        cfw.add(181, adapterName, "self", "Lorg/mozilla/javascript/Scriptable;");
        cfw.add(177);
        cfw.stopMethod(locals);
    }

    private static void generateSerialCtor(ClassFileWriter cfw, String adapterName, String superName) {
        cfw.startMethod("<init>", "(Lorg/mozilla/javascript/ContextFactory;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;)V", (short) 1);
        cfw.add(42);
        cfw.addInvoke(183, superName, "<init>", "()V");
        cfw.add(42);
        cfw.add(43);
        cfw.add(181, adapterName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        cfw.add(42);
        cfw.add(44);
        cfw.add(181, adapterName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        cfw.add(42);
        cfw.add(45);
        cfw.add(181, adapterName, "self", "Lorg/mozilla/javascript/Scriptable;");
        cfw.add(177);
        cfw.stopMethod((short) 4);
    }

    private static void generateEmptyCtor(ClassFileWriter cfw, String adapterName, String superName, String scriptClassName) {
        cfw.startMethod("<init>", "()V", (short) 1);
        cfw.add(42);
        cfw.addInvoke(183, superName, "<init>", "()V");
        cfw.add(42);
        cfw.add(1);
        cfw.add(181, adapterName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        cfw.add(187, scriptClassName);
        cfw.add(89);
        cfw.addInvoke(183, scriptClassName, "<init>", "()V");
        cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "runScript", "(Lorg/mozilla/javascript/Script;)Lorg/mozilla/javascript/Scriptable;");
        cfw.add(76);
        cfw.add(42);
        cfw.add(43);
        cfw.add(181, adapterName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        cfw.add(42);
        cfw.add(43);
        cfw.add(42);
        cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "createAdapterWrapper", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        cfw.add(181, adapterName, "self", "Lorg/mozilla/javascript/Scriptable;");
        cfw.add(177);
        cfw.stopMethod((short) 2);
    }

    static void generatePushWrappedArgs(ClassFileWriter cfw, Class<?>[] argTypes, int arrayLength) {
        cfw.addPush(arrayLength);
        cfw.add(189, "java/lang/Object");
        int paramOffset = 1;
        for (int i = 0; i != argTypes.length; i++) {
            cfw.add(89);
            cfw.addPush(i);
            paramOffset += generateWrapArg(cfw, paramOffset, argTypes[i]);
            cfw.add(83);
        }
    }

    private static int generateWrapArg(ClassFileWriter cfw, int paramOffset, Class<?> argType) {
        int size = 1;
        if (!argType.isPrimitive()) {
            cfw.add(25, paramOffset);
        } else if (argType == Boolean.TYPE) {
            cfw.add(187, "java/lang/Boolean");
            cfw.add(89);
            cfw.add(21, paramOffset);
            cfw.addInvoke(183, "java/lang/Boolean", "<init>", "(Z)V");
        } else if (argType == Character.TYPE) {
            cfw.add(21, paramOffset);
            cfw.addInvoke(184, "java/lang/String", "valueOf", "(C)Ljava/lang/String;");
        } else {
            cfw.add(187, "java/lang/Double");
            cfw.add(89);
            switch (argType.getName().charAt(0)) {
                case 'b':
                case 'i':
                case 's':
                    cfw.add(21, paramOffset);
                    cfw.add(135);
                    break;
                case 'd':
                    cfw.add(24, paramOffset);
                    size = 2;
                    break;
                case 'f':
                    cfw.add(23, paramOffset);
                    cfw.add(141);
                    break;
                case 'l':
                    cfw.add(22, paramOffset);
                    cfw.add(138);
                    size = 2;
                    break;
            }
            cfw.addInvoke(183, "java/lang/Double", "<init>", "(D)V");
        }
        return size;
    }

    static void generateReturnResult(ClassFileWriter cfw, Class<?> retType, boolean callConvertResult) {
        if (retType == Void.TYPE) {
            cfw.add(87);
            cfw.add(177);
        } else if (retType == Boolean.TYPE) {
            cfw.addInvoke(184, "org/mozilla/javascript/Context", "toBoolean", "(Ljava/lang/Object;)Z");
            cfw.add(172);
        } else if (retType == Character.TYPE) {
            cfw.addInvoke(184, "org/mozilla/javascript/Context", "toString", "(Ljava/lang/Object;)Ljava/lang/String;");
            cfw.add(3);
            cfw.addInvoke(182, "java/lang/String", "charAt", "(I)C");
            cfw.add(172);
        } else if (retType.isPrimitive()) {
            cfw.addInvoke(184, "org/mozilla/javascript/Context", "toNumber", "(Ljava/lang/Object;)D");
            switch (retType.getName().charAt(0)) {
                case 'b':
                case 'i':
                case 's':
                    cfw.add(142);
                    cfw.add(172);
                    return;
                case 'd':
                    cfw.add(175);
                    return;
                case 'f':
                    cfw.add(144);
                    cfw.add(174);
                    return;
                case 'l':
                    cfw.add(143);
                    cfw.add(173);
                    return;
                default:
                    throw new RuntimeException("Unexpected return type " + retType.toString());
            }
        } else {
            String retTypeStr = retType.getName();
            if (callConvertResult) {
                cfw.addLoadConstant(retTypeStr);
                cfw.addInvoke(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
                cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "convertResult", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
            }
            cfw.add(192, retTypeStr);
            cfw.add(176);
        }
    }

    private static void generateMethod(ClassFileWriter cfw, String genName, String methodName, Class<?>[] parms, Class<?> returnType, boolean convertResult) {
        StringBuilder sb = new StringBuilder();
        int paramsEnd = appendMethodSignature(parms, returnType, sb);
        cfw.startMethod(methodName, sb.toString(), (short) 1);
        cfw.add(42);
        cfw.add(180, genName, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        cfw.add(42);
        cfw.add(180, genName, "self", "Lorg/mozilla/javascript/Scriptable;");
        cfw.add(42);
        cfw.add(180, genName, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        cfw.addPush(methodName);
        cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "getFunction", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/Function;");
        generatePushWrappedArgs(cfw, parms, parms.length);
        if (parms.length > 64) {
            throw Context.reportRuntimeError0("JavaAdapter can not subclass methods with more then 64 arguments.");
        }
        long convertionMask = 0;
        for (int i = 0; i != parms.length; i++) {
            if (!parms[i].isPrimitive()) {
                convertionMask |= (long) (1 << i);
            }
        }
        cfw.addPush(convertionMask);
        cfw.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "callMethod", "(Lorg/mozilla/javascript/ContextFactory;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Function;[Ljava/lang/Object;J)Ljava/lang/Object;");
        generateReturnResult(cfw, returnType, convertResult);
        cfw.stopMethod((short) paramsEnd);
    }

    private static int generatePushParam(ClassFileWriter cfw, int paramOffset, Class<?> paramType) {
        if (paramType.isPrimitive()) {
            switch (paramType.getName().charAt(0)) {
                case 'b':
                case 'c':
                case 'i':
                case 's':
                case 'z':
                    cfw.addILoad(paramOffset);
                    return 1;
                case 'd':
                    cfw.addDLoad(paramOffset);
                    return 2;
                case 'f':
                    cfw.addFLoad(paramOffset);
                    return 1;
                case 'l':
                    cfw.addLLoad(paramOffset);
                    return 2;
                default:
                    throw Kit.codeBug();
            }
        }
        cfw.addALoad(paramOffset);
        return 1;
    }

    private static void generatePopResult(ClassFileWriter cfw, Class<?> retType) {
        if (retType.isPrimitive()) {
            switch (retType.getName().charAt(0)) {
                case 'b':
                case 'c':
                case 'i':
                case 's':
                case 'z':
                    cfw.add(172);
                    return;
                case 'd':
                    cfw.add(175);
                    return;
                case 'f':
                    cfw.add(174);
                    return;
                case 'l':
                    cfw.add(173);
                    return;
                default:
                    return;
            }
        }
        cfw.add(176);
    }

    private static void generateSuper(ClassFileWriter cfw, String genName, String superName, String methodName, String methodSignature, Class<?>[] parms, Class<?> returnType) {
        int i = 0;
        cfw.startMethod("super$" + methodName, methodSignature, (short) 1);
        cfw.add(25, 0);
        int paramOffset = 1;
        while (i < parms.length) {
            paramOffset += generatePushParam(cfw, paramOffset, parms[i]);
            i++;
        }
        cfw.addInvoke(183, superName, methodName, methodSignature);
        Class<?> retType = returnType;
        if (retType.equals(Void.TYPE)) {
            cfw.add(177);
        } else {
            generatePopResult(cfw, retType);
        }
        cfw.stopMethod((short) (paramOffset + 1));
    }

    private static String getMethodSignature(Method method, Class<?>[] argTypes) {
        StringBuilder sb = new StringBuilder();
        appendMethodSignature(argTypes, method.getReturnType(), sb);
        return sb.toString();
    }

    static int appendMethodSignature(Class<?>[] argTypes, Class<?> returnType, StringBuilder sb) {
        sb.append('(');
        int firstLocal = argTypes.length + 1;
        for (Class<?> type : argTypes) {
            appendTypeString(sb, type);
            if (type == Long.TYPE || type == Double.TYPE) {
                firstLocal++;
            }
        }
        sb.append(')');
        appendTypeString(sb, returnType);
        return firstLocal;
    }

    private static StringBuilder appendTypeString(StringBuilder sb, Class<?> type) {
        while (type.isArray()) {
            sb.append('[');
            type = type.getComponentType();
        }
        if (type.isPrimitive()) {
            char typeLetter;
            if (type == Boolean.TYPE) {
                typeLetter = 'Z';
            } else if (type == Long.TYPE) {
                typeLetter = 'J';
            } else {
                typeLetter = Character.toUpperCase(type.getName().charAt(0));
            }
            sb.append(typeLetter);
        } else {
            sb.append('L');
            sb.append(type.getName().replace('.', '/'));
            sb.append(';');
        }
        return sb;
    }

    static int[] getArgsToConvert(Class<?>[] argTypes) {
        int i;
        int count = 0;
        for (i = 0; i != argTypes.length; i++) {
            if (!argTypes[i].isPrimitive()) {
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        int[] array = new int[count];
        count = 0;
        for (i = 0; i != argTypes.length; i++) {
            if (!argTypes[i].isPrimitive()) {
                int count2 = count + 1;
                array[count] = i;
                count = count2;
            }
        }
        return array;
    }
}
