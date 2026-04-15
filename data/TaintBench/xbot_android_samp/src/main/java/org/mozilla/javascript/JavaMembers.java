package org.mozilla.javascript;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class JavaMembers {
    private Class<?> cl;
    NativeJavaMethod ctors;
    private Map<String, FieldAndMethods> fieldAndMethods;
    private Map<String, Object> members;
    private Map<String, FieldAndMethods> staticFieldAndMethods;
    private Map<String, Object> staticMembers;

    private static final class MethodSignature {
        private final Class<?>[] args;
        private final String name;

        private MethodSignature(String name, Class<?>[] args) {
            this.name = name;
            this.args = args;
        }

        MethodSignature(Method method) {
            this(method.getName(), method.getParameterTypes());
        }

        public boolean equals(Object o) {
            if (!(o instanceof MethodSignature)) {
                return false;
            }
            MethodSignature ms = (MethodSignature) o;
            if (ms.name.equals(this.name) && Arrays.equals(this.args, ms.args)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.name.hashCode() ^ this.args.length;
        }
    }

    JavaMembers(Scriptable scope, Class<?> cl) {
        this(scope, cl, false);
    }

    JavaMembers(Scriptable scope, Class<?> cl, boolean includeProtected) {
        try {
            Context cx = ContextFactory.getGlobal().enterContext();
            ClassShutter shutter = cx.getClassShutter();
            if (shutter == null || shutter.visibleToScripts(cl.getName())) {
                this.members = new HashMap();
                this.staticMembers = new HashMap();
                this.cl = cl;
                reflect(scope, includeProtected, cx.hasFeature(13));
                return;
            }
            throw Context.reportRuntimeError1("msg.access.prohibited", cl.getName());
        } finally {
            Context.exit();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean has(String name, boolean isStatic) {
        if ((isStatic ? this.staticMembers : this.members).get(name) == null && findExplicitFunction(name, isStatic) == null) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public Object get(Scriptable scope, String name, Object javaObject, boolean isStatic) {
        Object member = (isStatic ? this.staticMembers : this.members).get(name);
        if (!isStatic && member == null) {
            member = this.staticMembers.get(name);
        }
        if (member == null) {
            member = getExplicitFunction(scope, name, javaObject, isStatic);
            if (member == null) {
                return Scriptable.NOT_FOUND;
            }
        }
        if (member instanceof Scriptable) {
            return member;
        }
        Context cx = Context.getContext();
        try {
            Object rval;
            Class<?> type;
            if (member instanceof BeanProperty) {
                BeanProperty bp = (BeanProperty) member;
                if (bp.getter == null) {
                    return Scriptable.NOT_FOUND;
                }
                rval = bp.getter.invoke(javaObject, Context.emptyArgs);
                type = bp.getter.method().getReturnType();
            } else {
                Field field = (Field) member;
                if (isStatic) {
                    javaObject = null;
                }
                rval = field.get(javaObject);
                type = field.getType();
            }
            return cx.getWrapFactory().wrap(cx, ScriptableObject.getTopLevelScope(scope), rval, type);
        } catch (Exception ex) {
            throw Context.throwAsScriptRuntimeEx(ex);
        }
    }

    /* access modifiers changed from: 0000 */
    public void put(Scriptable scope, String name, Object javaObject, Object value, boolean isStatic) {
        Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
        BeanProperty member = ht.get(name);
        if (!isStatic && member == null) {
            member = this.staticMembers.get(name);
        }
        if (member == null) {
            throw reportMemberNotFound(name);
        }
        if (member instanceof FieldAndMethods) {
            member = ((FieldAndMethods) ht.get(name)).field;
        }
        if (member instanceof BeanProperty) {
            BeanProperty bp = member;
            if (bp.setter == null) {
                throw reportMemberNotFound(name);
            } else if (bp.setters == null || value == null) {
                Object[] args = new Object[1];
                args[0] = Context.jsToJava(value, bp.setter.argTypes[0]);
                try {
                    bp.setter.invoke(javaObject, args);
                } catch (Exception ex) {
                    throw Context.throwAsScriptRuntimeEx(ex);
                }
            } else {
                bp.setters.call(Context.getContext(), ScriptableObject.getTopLevelScope(scope), scope, new Object[]{value});
            }
        } else if (member instanceof Field) {
            Field field = (Field) member;
            try {
                field.set(javaObject, Context.jsToJava(value, field.getType()));
            } catch (IllegalAccessException accessEx) {
                if ((field.getModifiers() & 16) == 0) {
                    throw Context.throwAsScriptRuntimeEx(accessEx);
                }
            } catch (IllegalArgumentException e) {
                throw Context.reportRuntimeError3("msg.java.internal.field.type", value.getClass().getName(), field, javaObject.getClass().getName());
            }
        } else {
            throw Context.reportRuntimeError1(member == null ? "msg.java.internal.private" : "msg.java.method.assign", name);
        }
    }

    /* access modifiers changed from: 0000 */
    public Object[] getIds(boolean isStatic) {
        Map<String, Object> map = isStatic ? this.staticMembers : this.members;
        return map.keySet().toArray(new Object[map.size()]);
    }

    static String javaSignature(Class<?> type) {
        if (!type.isArray()) {
            return type.getName();
        }
        int arrayDimension = 0;
        do {
            arrayDimension++;
            type = type.getComponentType();
        } while (type.isArray());
        String name = type.getName();
        String suffix = "[]";
        if (arrayDimension == 1) {
            return name.concat(suffix);
        }
        StringBuilder sb = new StringBuilder(name.length() + (suffix.length() * arrayDimension));
        sb.append(name);
        while (arrayDimension != 0) {
            arrayDimension--;
            sb.append(suffix);
        }
        return sb.toString();
    }

    static String liveConnectSignature(Class<?>[] argTypes) {
        int N = argTypes.length;
        if (N == 0) {
            return "()";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i != N; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(javaSignature(argTypes[i]));
        }
        sb.append(')');
        return sb.toString();
    }

    private MemberBox findExplicitFunction(String name, boolean isStatic) {
        int sigStart = name.indexOf(40);
        if (sigStart < 0) {
            return null;
        }
        Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
        MemberBox[] methodsOrCtors = null;
        boolean isCtor = isStatic && sigStart == 0;
        if (isCtor) {
            methodsOrCtors = this.ctors.methods;
        } else {
            String trueName = name.substring(0, sigStart);
            NativeJavaMethod obj = ht.get(trueName);
            if (!isStatic && obj == null) {
                obj = this.staticMembers.get(trueName);
            }
            if (obj instanceof NativeJavaMethod) {
                methodsOrCtors = obj.methods;
            }
        }
        if (methodsOrCtors != null) {
            for (MemberBox methodsOrCtor : methodsOrCtors) {
                String sig = liveConnectSignature(methodsOrCtor.argTypes);
                if (sig.length() + sigStart == name.length() && name.regionMatches(sigStart, sig, 0, sig.length())) {
                    return methodsOrCtor;
                }
            }
        }
        return null;
    }

    private Object getExplicitFunction(Scriptable scope, String name, Object javaObject, boolean isStatic) {
        Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
        MemberBox methodOrCtor = findExplicitFunction(name, isStatic);
        if (methodOrCtor == null) {
            return null;
        }
        Scriptable prototype = ScriptableObject.getFunctionPrototype(scope);
        if (methodOrCtor.isCtor()) {
            NativeJavaConstructor fun = new NativeJavaConstructor(methodOrCtor);
            fun.setPrototype(prototype);
            NativeJavaConstructor member = fun;
            ht.put(name, fun);
            return member;
        }
        Object member2 = ht.get(methodOrCtor.getName());
        if (!(member2 instanceof NativeJavaMethod) || ((NativeJavaMethod) member2).methods.length <= 1) {
            return member2;
        }
        NativeJavaMethod fun2 = new NativeJavaMethod(methodOrCtor, name);
        fun2.setPrototype(prototype);
        ht.put(name, fun2);
        return fun2;
    }

    private static Method[] discoverAccessibleMethods(Class<?> clazz, boolean includeProtected, boolean includePrivate) {
        Map<MethodSignature, Method> map = new HashMap();
        discoverAccessibleMethods(clazz, map, includeProtected, includePrivate);
        return (Method[]) map.values().toArray(new Method[map.size()]);
    }

    private static void discoverAccessibleMethods(Class<?> clazz, Map<MethodSignature, Method> map, boolean includeProtected, boolean includePrivate) {
        int i = 0;
        if (Modifier.isPublic(clazz.getModifiers()) || includePrivate) {
            MethodSignature sig;
            if (includeProtected || includePrivate) {
                while (clazz != null) {
                    try {
                        for (Method method : clazz.getDeclaredMethods()) {
                            int mods = method.getModifiers();
                            if (Modifier.isPublic(mods) || Modifier.isProtected(mods) || includePrivate) {
                                sig = new MethodSignature(method);
                                if (!map.containsKey(sig)) {
                                    if (includePrivate && !method.isAccessible()) {
                                        method.setAccessible(true);
                                    }
                                    map.put(sig, method);
                                }
                            }
                        }
                        clazz = clazz.getSuperclass();
                    } catch (SecurityException e) {
                        try {
                            for (Method method2 : clazz.getMethods()) {
                                sig = new MethodSignature(method2);
                                if (!map.containsKey(sig)) {
                                    map.put(sig, method2);
                                }
                            }
                            return;
                        } catch (SecurityException e2) {
                            Context.reportWarning("Could not discover accessible methods of class " + clazz.getName() + " due to lack of privileges, " + "attemping superclasses/interfaces.");
                        }
                    }
                }
                return;
            }
            for (Method method22 : clazz.getMethods()) {
                sig = new MethodSignature(method22);
                if (!map.containsKey(sig)) {
                    map.put(sig, method22);
                }
            }
            return;
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        int length = interfaces.length;
        while (i < length) {
            discoverAccessibleMethods(interfaces[i], map, includeProtected, includePrivate);
            i++;
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            discoverAccessibleMethods(superclass, map, includeProtected, includePrivate);
        }
    }

    private void reflect(Scriptable scope, boolean includeProtected, boolean includePrivate) {
        Map<String, Object> ht;
        String name;
        Object value;
        ObjArray overloadedMethods;
        int i;
        boolean isStatic;
        for (Method method : discoverAccessibleMethods(this.cl, includeProtected, includePrivate)) {
            ht = Modifier.isStatic(method.getModifiers()) ? this.staticMembers : this.members;
            name = method.getName();
            value = ht.get(name);
            if (value == null) {
                ht.put(name, method);
            } else {
                if (value instanceof ObjArray) {
                    overloadedMethods = (ObjArray) value;
                } else {
                    if (!(value instanceof Method)) {
                        Kit.codeBug();
                    }
                    overloadedMethods = new ObjArray();
                    overloadedMethods.add(value);
                    ht.put(name, overloadedMethods);
                }
                overloadedMethods.add(method);
            }
        }
        int tableCursor = 0;
        while (tableCursor != 2) {
            ht = tableCursor == 0 ? this.staticMembers : this.members;
            for (Entry<String, Object> entry : ht.entrySet()) {
                MemberBox[] methodBoxes;
                value = entry.getValue();
                if (value instanceof Method) {
                    methodBoxes = new MemberBox[]{new MemberBox((Method) value)};
                } else {
                    overloadedMethods = (ObjArray) value;
                    int N = overloadedMethods.size();
                    if (N < 2) {
                        Kit.codeBug();
                    }
                    methodBoxes = new MemberBox[N];
                    for (i = 0; i != N; i++) {
                        methodBoxes[i] = new MemberBox((Method) overloadedMethods.get(i));
                    }
                }
                BaseFunction nativeJavaMethod = new NativeJavaMethod(methodBoxes);
                if (scope != null) {
                    ScriptRuntime.setFunctionProtoAndParent(nativeJavaMethod, scope);
                }
                ht.put(entry.getKey(), nativeJavaMethod);
            }
            tableCursor++;
        }
        for (Field field : getAccessibleFields(includeProtected, includePrivate)) {
            name = field.getName();
            try {
                isStatic = Modifier.isStatic(field.getModifiers());
                ht = isStatic ? this.staticMembers : this.members;
                Object member = ht.get(name);
                if (member == null) {
                    ht.put(name, field);
                } else if (member instanceof NativeJavaMethod) {
                    FieldAndMethods fam = new FieldAndMethods(scope, ((NativeJavaMethod) member).methods, field);
                    Map<String, FieldAndMethods> fmht = isStatic ? this.staticFieldAndMethods : this.fieldAndMethods;
                    if (fmht == null) {
                        fmht = new HashMap();
                        if (isStatic) {
                            this.staticFieldAndMethods = fmht;
                        } else {
                            this.fieldAndMethods = fmht;
                        }
                    }
                    fmht.put(name, fam);
                    ht.put(name, fam);
                } else if (!(member instanceof Field)) {
                    Kit.codeBug();
                } else if (((Field) member).getDeclaringClass().isAssignableFrom(field.getDeclaringClass())) {
                    ht.put(name, field);
                }
            } catch (SecurityException e) {
                Context.reportWarning("Could not access field " + name + " of class " + this.cl.getName() + " due to lack of privileges.");
            }
        }
        tableCursor = 0;
        while (tableCursor != 2) {
            isStatic = tableCursor == 0;
            if (isStatic) {
                ht = this.staticMembers;
            } else {
                ht = this.members;
            }
            Map<String, BeanProperty> toAdd = new HashMap();
            for (String name2 : ht.keySet()) {
                boolean memberIsGetMethod = name2.startsWith("get");
                boolean memberIsSetMethod = name2.startsWith("set");
                boolean memberIsIsMethod = name2.startsWith("is");
                if (memberIsGetMethod || memberIsIsMethod || memberIsSetMethod) {
                    String nameComponent = name2.substring(memberIsIsMethod ? 2 : 3);
                    if (nameComponent.length() != 0) {
                        String beanPropertyName = nameComponent;
                        char ch0 = nameComponent.charAt(0);
                        if (Character.isUpperCase(ch0)) {
                            if (nameComponent.length() == 1) {
                                beanPropertyName = nameComponent.toLowerCase();
                            } else if (!Character.isUpperCase(nameComponent.charAt(1))) {
                                beanPropertyName = Character.toLowerCase(ch0) + nameComponent.substring(1);
                            }
                        }
                        if (!toAdd.containsKey(beanPropertyName)) {
                            Object v = ht.get(beanPropertyName);
                            if (v == null || (includePrivate && (v instanceof Member) && Modifier.isPrivate(((Member) v).getModifiers()))) {
                                MemberBox getter = findGetter(isStatic, ht, "get", nameComponent);
                                if (getter == null) {
                                    getter = findGetter(isStatic, ht, "is", nameComponent);
                                }
                                MemberBox setter = null;
                                NativeJavaMethod setters = null;
                                String setterName = "set".concat(nameComponent);
                                if (ht.containsKey(setterName)) {
                                    NativeJavaMethod member2 = ht.get(setterName);
                                    if (member2 instanceof NativeJavaMethod) {
                                        NativeJavaMethod njmSet = member2;
                                        if (getter != null) {
                                            setter = extractSetMethod(getter.method().getReturnType(), njmSet.methods, isStatic);
                                        } else {
                                            setter = extractSetMethod(njmSet.methods, isStatic);
                                        }
                                        if (njmSet.methods.length > 1) {
                                            setters = njmSet;
                                        }
                                    }
                                }
                                toAdd.put(beanPropertyName, new BeanProperty(getter, setter, setters));
                            }
                        }
                    }
                }
            }
            for (String key : toAdd.keySet()) {
                ht.put(key, toAdd.get(key));
            }
            tableCursor++;
        }
        Constructor<?>[] constructors = getAccessibleConstructors(includePrivate);
        MemberBox[] ctorMembers = new MemberBox[constructors.length];
        for (i = 0; i != constructors.length; i++) {
            ctorMembers[i] = new MemberBox(constructors[i]);
        }
        this.ctors = new NativeJavaMethod(ctorMembers, this.cl.getSimpleName());
    }

    private Constructor<?>[] getAccessibleConstructors(boolean includePrivate) {
        if (includePrivate && this.cl != ScriptRuntime.ClassClass) {
            try {
                Constructor<?>[] cons = this.cl.getDeclaredConstructors();
                AccessibleObject.setAccessible(cons, true);
                return cons;
            } catch (SecurityException e) {
                Context.reportWarning("Could not access constructor  of class " + this.cl.getName() + " due to lack of privileges.");
            }
        }
        return this.cl.getConstructors();
    }

    private Field[] getAccessibleFields(boolean includeProtected, boolean includePrivate) {
        if (includePrivate || includeProtected) {
            try {
                List<Field> fieldsList = new ArrayList();
                for (Class<?> currentClass = this.cl; currentClass != null; currentClass = currentClass.getSuperclass()) {
                    for (Field field : currentClass.getDeclaredFields()) {
                        int mod = field.getModifiers();
                        if (includePrivate || Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            fieldsList.add(field);
                        }
                    }
                }
                return (Field[]) fieldsList.toArray(new Field[fieldsList.size()]);
            } catch (SecurityException e) {
            }
        }
        return this.cl.getFields();
    }

    private MemberBox findGetter(boolean isStatic, Map<String, Object> ht, String prefix, String propertyName) {
        String getterName = prefix.concat(propertyName);
        if (ht.containsKey(getterName)) {
            NativeJavaMethod member = ht.get(getterName);
            if (member instanceof NativeJavaMethod) {
                return extractGetMethod(member.methods, isStatic);
            }
        }
        return null;
    }

    private static MemberBox extractGetMethod(MemberBox[] methods, boolean isStatic) {
        int length = methods.length;
        int i = 0;
        while (i < length) {
            MemberBox method = methods[i];
            if (method.argTypes.length != 0 || (isStatic && !method.isStatic())) {
                i++;
            } else {
                if (method.method().getReturnType() != Void.TYPE) {
                    return method;
                }
                return null;
            }
        }
        return null;
    }

    private static MemberBox extractSetMethod(Class<?> type, MemberBox[] methods, boolean isStatic) {
        for (int pass = 1; pass <= 2; pass++) {
            for (MemberBox method : methods) {
                if (!isStatic || method.isStatic()) {
                    Class<?>[] params = method.argTypes;
                    if (params.length != 1) {
                        continue;
                    } else if (pass != 1) {
                        if (pass != 2) {
                            Kit.codeBug();
                        }
                        if (params[0].isAssignableFrom(type)) {
                            return method;
                        }
                    } else if (params[0] == type) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    private static MemberBox extractSetMethod(MemberBox[] methods, boolean isStatic) {
        for (MemberBox method : methods) {
            if ((!isStatic || method.isStatic()) && method.method().getReturnType() == Void.TYPE && method.argTypes.length == 1) {
                return method;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public Map<String, FieldAndMethods> getFieldAndMethodsObjects(Scriptable scope, Object javaObject, boolean isStatic) {
        Map<String, FieldAndMethods> ht = isStatic ? this.staticFieldAndMethods : this.fieldAndMethods;
        if (ht == null) {
            return null;
        }
        Map<String, FieldAndMethods> result = new HashMap(ht.size());
        for (FieldAndMethods fam : ht.values()) {
            FieldAndMethods famNew = new FieldAndMethods(scope, fam.methods, fam.field);
            famNew.javaObject = javaObject;
            result.put(fam.field.getName(), famNew);
        }
        return result;
    }

    static JavaMembers lookupClass(Scriptable scope, Class<?> dynamicType, Class<?> staticType, boolean includeProtected) {
        ClassCache cache = ClassCache.get(scope);
        Map<Class<?>, JavaMembers> ct = cache.getClassCacheMap();
        Class<?> cl = dynamicType;
        while (true) {
            JavaMembers members = (JavaMembers) ct.get(cl);
            if (members != null) {
                if (cl != dynamicType) {
                    ct.put(dynamicType, members);
                }
                return members;
            }
            try {
                members = new JavaMembers(cache.getAssociatedScope(), cl, includeProtected);
                if (cache.isCachingEnabled()) {
                    ct.put(cl, members);
                    if (cl != dynamicType) {
                        ct.put(dynamicType, members);
                    }
                }
                return members;
            } catch (SecurityException e) {
                if (staticType == null || !staticType.isInterface()) {
                    Class<?> parent = cl.getSuperclass();
                    if (parent == null) {
                        if (cl.isInterface()) {
                            parent = ScriptRuntime.ObjectClass;
                        } else {
                            throw e;
                        }
                    }
                    cl = parent;
                } else {
                    cl = staticType;
                    staticType = null;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public RuntimeException reportMemberNotFound(String memberName) {
        return Context.reportRuntimeError2("msg.java.member.not.found", this.cl.getName(), memberName);
    }
}
