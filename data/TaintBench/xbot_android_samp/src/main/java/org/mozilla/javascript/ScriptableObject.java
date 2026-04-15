package org.mozilla.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.mozilla.javascript.TopLevel.Builtins;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;
import org.mozilla.javascript.annotations.JSStaticFunction;
import org.mozilla.javascript.debug.DebuggableObject;

public abstract class ScriptableObject implements Scriptable, Serializable, DebuggableObject, ConstProperties {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final int CONST = 13;
    public static final int DONTENUM = 2;
    public static final int EMPTY = 0;
    private static final Method GET_ARRAY_LENGTH;
    private static final int INITIAL_SLOT_SIZE = 4;
    public static final int PERMANENT = 4;
    public static final int READONLY = 1;
    private static final int SLOT_CONVERT_ACCESSOR_TO_DATA = 5;
    private static final int SLOT_MODIFY = 2;
    private static final int SLOT_MODIFY_CONST = 3;
    private static final int SLOT_MODIFY_GETTER_SETTER = 4;
    private static final int SLOT_QUERY = 1;
    public static final int UNINITIALIZED_CONST = 8;
    static final long serialVersionUID = 2829861078851942586L;
    private volatile Map<Object, Object> associatedValues;
    private int count;
    private transient ExternalArrayData externalData;
    private transient Slot firstAdded;
    private boolean isExtensible = true;
    private transient Slot lastAdded;
    private Scriptable parentScopeObject;
    private Scriptable prototypeObject;
    private transient Slot[] slots;

    private static class Slot implements Serializable {
        private static final long serialVersionUID = -6090581677123995491L;
        /* access modifiers changed from: private|volatile */
        public volatile short attributes;
        int indexOrHash;
        String name;
        transient Slot next;
        volatile transient Slot orderedNext;
        volatile Object value;
        volatile transient boolean wasDeleted;

        Slot(String name, int indexOrHash, int attributes) {
            this.name = name;
            this.indexOrHash = indexOrHash;
            this.attributes = (short) attributes;
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            if (this.name != null) {
                this.indexOrHash = this.name.hashCode();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean setValue(Object value, Scriptable owner, Scriptable start) {
            if ((this.attributes & 1) != 0) {
                return true;
            }
            if (owner != start) {
                return false;
            }
            this.value = value;
            return true;
        }

        /* access modifiers changed from: 0000 */
        public Object getValue(Scriptable start) {
            return this.value;
        }

        /* access modifiers changed from: 0000 */
        public int getAttributes() {
            return this.attributes;
        }

        /* access modifiers changed from: declared_synchronized */
        public synchronized void setAttributes(int value) {
            ScriptableObject.checkValidAttributes(value);
            this.attributes = (short) value;
        }

        /* access modifiers changed from: 0000 */
        public void markDeleted() {
            this.wasDeleted = true;
            this.value = null;
            this.name = null;
        }

        /* access modifiers changed from: 0000 */
        public ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
            return ScriptableObject.buildDataDescriptor(scope, this.value, this.attributes);
        }
    }

    private static final class GetterSlot extends Slot {
        static final long serialVersionUID = -4900574849788797588L;
        Object getter;
        Object setter;

        GetterSlot(String name, int indexOrHash, int attributes) {
            super(name, indexOrHash, attributes);
        }

        /* access modifiers changed from: 0000 */
        public ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
            boolean z;
            boolean z2 = true;
            int attr = getAttributes();
            ScriptableObject desc = new NativeObject();
            ScriptRuntime.setBuiltinProtoAndParent(desc, scope, Builtins.Object);
            String str = "enumerable";
            if ((attr & 2) == 0) {
                z = true;
            } else {
                z = false;
            }
            desc.defineProperty(str, Boolean.valueOf(z), 0);
            String str2 = "configurable";
            if ((attr & 4) != 0) {
                z2 = false;
            }
            desc.defineProperty(str2, Boolean.valueOf(z2), 0);
            if (this.getter != null) {
                desc.defineProperty("get", this.getter, 0);
            }
            if (this.setter != null) {
                desc.defineProperty("set", this.setter, 0);
            }
            return desc;
        }

        /* access modifiers changed from: 0000 */
        public boolean setValue(Object value, Scriptable owner, Scriptable start) {
            if (this.setter != null) {
                Context cx = Context.getContext();
                if (this.setter instanceof MemberBox) {
                    Scriptable setterThis;
                    Object[] args;
                    MemberBox nativeSetter = this.setter;
                    Class<?>[] pTypes = nativeSetter.argTypes;
                    Object actualArg = FunctionObject.convertArg(cx, start, value, FunctionObject.getTypeTag(pTypes[pTypes.length - 1]));
                    if (nativeSetter.delegateTo == null) {
                        setterThis = start;
                        args = new Object[]{actualArg};
                    } else {
                        setterThis = nativeSetter.delegateTo;
                        args = new Object[]{start, actualArg};
                    }
                    nativeSetter.invoke(setterThis, args);
                } else if (this.setter instanceof Function) {
                    Function f = this.setter;
                    f.call(cx, f.getParentScope(), start, new Object[]{value});
                }
                return true;
            } else if (this.getter == null) {
                return super.setValue(value, owner, start);
            } else {
                if (!Context.getContext().hasFeature(11)) {
                    return true;
                }
                throw ScriptRuntime.typeError1("msg.set.prop.no.setter", this.name);
            }
        }

        /* access modifiers changed from: 0000 */
        public Object getValue(Scriptable start) {
            if (this.getter != null) {
                if (this.getter instanceof MemberBox) {
                    Scriptable getterThis;
                    Object[] args;
                    MemberBox nativeGetter = this.getter;
                    if (nativeGetter.delegateTo == null) {
                        getterThis = start;
                        args = ScriptRuntime.emptyArgs;
                    } else {
                        getterThis = nativeGetter.delegateTo;
                        args = new Object[]{start};
                    }
                    return nativeGetter.invoke(getterThis, args);
                } else if (this.getter instanceof Function) {
                    Function f = this.getter;
                    return f.call(Context.getContext(), f.getParentScope(), start, ScriptRuntime.emptyArgs);
                }
            }
            Object val = this.value;
            if (!(val instanceof LazilyLoadedCtor)) {
                return val;
            }
            LazilyLoadedCtor initializer = (LazilyLoadedCtor) val;
            try {
                initializer.init();
                return val;
            } finally {
                val = initializer.getValue();
                this.value = val;
            }
        }

        /* access modifiers changed from: 0000 */
        public void markDeleted() {
            super.markDeleted();
            this.getter = null;
            this.setter = null;
        }
    }

    private static class RelinkedSlot extends Slot {
        final Slot slot;

        RelinkedSlot(Slot slot) {
            super(slot.name, slot.indexOrHash, slot.attributes);
            this.slot = ScriptableObject.unwrapSlot(slot);
        }

        /* access modifiers changed from: 0000 */
        public boolean setValue(Object value, Scriptable owner, Scriptable start) {
            return this.slot.setValue(value, owner, start);
        }

        /* access modifiers changed from: 0000 */
        public Object getValue(Scriptable start) {
            return this.slot.getValue(start);
        }

        /* access modifiers changed from: 0000 */
        public ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
            return this.slot.getPropertyDescriptor(cx, scope);
        }

        /* access modifiers changed from: 0000 */
        public int getAttributes() {
            return this.slot.getAttributes();
        }

        /* access modifiers changed from: 0000 */
        public void setAttributes(int value) {
            this.slot.setAttributes(value);
        }

        /* access modifiers changed from: 0000 */
        public void markDeleted() {
            super.markDeleted();
            this.slot.markDeleted();
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(this.slot);
        }
    }

    public abstract String getClassName();

    static {
        boolean z = false;
        if (!ScriptableObject.class.desiredAssertionStatus()) {
            z = true;
        }
        $assertionsDisabled = z;
        try {
            GET_ARRAY_LENGTH = ScriptableObject.class.getMethod("getExternalArrayLength", new Class[0]);
        } catch (NoSuchMethodException nsm) {
            throw new RuntimeException(nsm);
        }
    }

    protected static ScriptableObject buildDataDescriptor(Scriptable scope, Object value, int attributes) {
        boolean z;
        boolean z2 = true;
        ScriptableObject desc = new NativeObject();
        ScriptRuntime.setBuiltinProtoAndParent(desc, scope, Builtins.Object);
        desc.defineProperty("value", value, 0);
        String str = "writable";
        if ((attributes & 1) == 0) {
            z = true;
        } else {
            z = false;
        }
        desc.defineProperty(str, Boolean.valueOf(z), 0);
        str = "enumerable";
        if ((attributes & 2) == 0) {
            z = true;
        } else {
            z = false;
        }
        desc.defineProperty(str, Boolean.valueOf(z), 0);
        String str2 = "configurable";
        if ((attributes & 4) != 0) {
            z2 = false;
        }
        desc.defineProperty(str2, Boolean.valueOf(z2), 0);
        return desc;
    }

    static void checkValidAttributes(int attributes) {
        if ((attributes & -16) != 0) {
            throw new IllegalArgumentException(String.valueOf(attributes));
        }
    }

    public ScriptableObject(Scriptable scope, Scriptable prototype) {
        if (scope == null) {
            throw new IllegalArgumentException();
        }
        this.parentScopeObject = scope;
        this.prototypeObject = prototype;
    }

    public String getTypeOf() {
        return avoidObjectDetection() ? "undefined" : "object";
    }

    public boolean has(String name, Scriptable start) {
        return getSlot(name, 0, 1) != null;
    }

    public boolean has(int index, Scriptable start) {
        if (this.externalData != null) {
            if (index < this.externalData.getArrayLength()) {
                return true;
            }
            return false;
        } else if (getSlot(null, index, 1) == null) {
            return false;
        } else {
            return true;
        }
    }

    public Object get(String name, Scriptable start) {
        Slot slot = getSlot(name, 0, 1);
        if (slot == null) {
            return Scriptable.NOT_FOUND;
        }
        return slot.getValue(start);
    }

    public Object get(int index, Scriptable start) {
        if (this.externalData == null) {
            Slot slot = getSlot(null, index, 1);
            if (slot == null) {
                return Scriptable.NOT_FOUND;
            }
            return slot.getValue(start);
        } else if (index < this.externalData.getArrayLength()) {
            return this.externalData.getArrayElement(index);
        } else {
            return Scriptable.NOT_FOUND;
        }
    }

    public void put(String name, Scriptable start, Object value) {
        if (!putImpl(name, 0, start, value)) {
            if (start == this) {
                throw Kit.codeBug();
            }
            start.put(name, start, value);
        }
    }

    public void put(int index, Scriptable start, Object value) {
        if (this.externalData != null) {
            if (index < this.externalData.getArrayLength()) {
                this.externalData.setArrayElement(index, value);
                return;
            }
            throw new JavaScriptException(ScriptRuntime.newNativeError(Context.getCurrentContext(), this, NativeErrors.RangeError, new Object[]{"External array index out of bounds "}), null, 0);
        } else if (!putImpl(null, index, start, value)) {
            if (start == this) {
                throw Kit.codeBug();
            }
            start.put(index, start, value);
        }
    }

    public void delete(String name) {
        checkNotSealed(name, 0);
        removeSlot(name, 0);
    }

    public void delete(int index) {
        checkNotSealed(null, index);
        removeSlot(null, index);
    }

    public void putConst(String name, Scriptable start, Object value) {
        if (!putConstImpl(name, 0, start, value, 1)) {
            if (start == this) {
                throw Kit.codeBug();
            } else if (start instanceof ConstProperties) {
                ((ConstProperties) start).putConst(name, start, value);
            } else {
                start.put(name, start, value);
            }
        }
    }

    public void defineConst(String name, Scriptable start) {
        if (!putConstImpl(name, 0, start, Undefined.instance, 8)) {
            if (start == this) {
                throw Kit.codeBug();
            } else if (start instanceof ConstProperties) {
                ((ConstProperties) start).defineConst(name, start);
            }
        }
    }

    public boolean isConst(String name) {
        boolean z = true;
        Slot slot = getSlot(name, 0, 1);
        if (slot == null) {
            return false;
        }
        if ((slot.getAttributes() & 5) != 5) {
            z = false;
        }
        return z;
    }

    @Deprecated
    public final int getAttributes(String name, Scriptable start) {
        return getAttributes(name);
    }

    @Deprecated
    public final int getAttributes(int index, Scriptable start) {
        return getAttributes(index);
    }

    @Deprecated
    public final void setAttributes(String name, Scriptable start, int attributes) {
        setAttributes(name, attributes);
    }

    @Deprecated
    public void setAttributes(int index, Scriptable start, int attributes) {
        setAttributes(index, attributes);
    }

    public int getAttributes(String name) {
        return findAttributeSlot(name, 0, 1).getAttributes();
    }

    public int getAttributes(int index) {
        return findAttributeSlot(null, index, 1).getAttributes();
    }

    public void setAttributes(String name, int attributes) {
        checkNotSealed(name, 0);
        findAttributeSlot(name, 0, 2).setAttributes(attributes);
    }

    public void setAttributes(int index, int attributes) {
        checkNotSealed(null, index);
        findAttributeSlot(null, index, 2).setAttributes(attributes);
    }

    public void setGetterOrSetter(String name, int index, Callable getterOrSetter, boolean isSetter) {
        setGetterOrSetter(name, index, getterOrSetter, isSetter, false);
    }

    private void setGetterOrSetter(String name, int index, Callable getterOrSetter, boolean isSetter, boolean force) {
        if (name == null || index == 0) {
            GetterSlot gslot;
            if (!force) {
                checkNotSealed(name, index);
            }
            if (isExtensible()) {
                gslot = (GetterSlot) getSlot(name, index, 4);
            } else {
                Slot slot = unwrapSlot(getSlot(name, index, 1));
                if (slot instanceof GetterSlot) {
                    gslot = (GetterSlot) slot;
                } else {
                    return;
                }
            }
            if (force || (gslot.getAttributes() & 1) == 0) {
                if (isSetter) {
                    gslot.setter = getterOrSetter;
                } else {
                    gslot.getter = getterOrSetter;
                }
                gslot.value = Undefined.instance;
                return;
            }
            throw Context.reportRuntimeError1("msg.modify.readonly", name);
        }
        throw new IllegalArgumentException(name);
    }

    public Object getGetterOrSetter(String name, int index, boolean isSetter) {
        if (name == null || index == 0) {
            Slot slot = unwrapSlot(getSlot(name, index, 1));
            if (slot == null) {
                return null;
            }
            if (!(slot instanceof GetterSlot)) {
                return Undefined.instance;
            }
            GetterSlot gslot = (GetterSlot) slot;
            Object result = isSetter ? gslot.setter : gslot.getter;
            if (result == null) {
                return Undefined.instance;
            }
            return result;
        }
        throw new IllegalArgumentException(name);
    }

    /* access modifiers changed from: protected */
    public boolean isGetterOrSetter(String name, int index, boolean setter) {
        Slot slot = unwrapSlot(getSlot(name, index, 1));
        if (slot instanceof GetterSlot) {
            if (setter && ((GetterSlot) slot).setter != null) {
                return true;
            }
            if (!(setter || ((GetterSlot) slot).getter == null)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void addLazilyInitializedValue(String name, int index, LazilyLoadedCtor init, int attributes) {
        if (name == null || index == 0) {
            checkNotSealed(name, index);
            GetterSlot gslot = (GetterSlot) getSlot(name, index, 4);
            gslot.setAttributes(attributes);
            gslot.getter = null;
            gslot.setter = null;
            gslot.value = init;
            return;
        }
        throw new IllegalArgumentException(name);
    }

    public void setExternalArrayData(ExternalArrayData array) {
        this.externalData = array;
        if (array == null) {
            delete("length");
            return;
        }
        defineProperty("length", null, GET_ARRAY_LENGTH, null, 3);
    }

    public ExternalArrayData getExternalArrayData() {
        return this.externalData;
    }

    public Object getExternalArrayLength() {
        return Integer.valueOf(this.externalData == null ? 0 : this.externalData.getArrayLength());
    }

    public Scriptable getPrototype() {
        return this.prototypeObject;
    }

    public void setPrototype(Scriptable m) {
        this.prototypeObject = m;
    }

    public Scriptable getParentScope() {
        return this.parentScopeObject;
    }

    public void setParentScope(Scriptable m) {
        this.parentScopeObject = m;
    }

    public Object[] getIds() {
        return getIds(false);
    }

    public Object[] getAllIds() {
        return getIds(true);
    }

    public Object getDefaultValue(Class<?> typeHint) {
        return getDefaultValue(this, typeHint);
    }

    public static Object getDefaultValue(Scriptable object, Class<?> typeHint) {
        Context cx = null;
        int i = 0;
        while (i < 2) {
            String methodName;
            Object[] args;
            boolean tryToString = typeHint == ScriptRuntime.StringClass ? i == 0 : i == 1;
            if (tryToString) {
                methodName = "toString";
                args = ScriptRuntime.emptyArgs;
            } else {
                String hint;
                methodName = "valueOf";
                args = new Object[1];
                if (typeHint == null) {
                    hint = "undefined";
                } else if (typeHint == ScriptRuntime.StringClass) {
                    hint = "string";
                } else if (typeHint == ScriptRuntime.ScriptableClass) {
                    hint = "object";
                } else if (typeHint == ScriptRuntime.FunctionClass) {
                    hint = "function";
                } else if (typeHint == ScriptRuntime.BooleanClass || typeHint == Boolean.TYPE) {
                    hint = "boolean";
                } else if (typeHint == ScriptRuntime.NumberClass || typeHint == ScriptRuntime.ByteClass || typeHint == Byte.TYPE || typeHint == ScriptRuntime.ShortClass || typeHint == Short.TYPE || typeHint == ScriptRuntime.IntegerClass || typeHint == Integer.TYPE || typeHint == ScriptRuntime.FloatClass || typeHint == Float.TYPE || typeHint == ScriptRuntime.DoubleClass || typeHint == Double.TYPE) {
                    hint = "number";
                } else {
                    throw Context.reportRuntimeError1("msg.invalid.type", typeHint.toString());
                }
                args[0] = hint;
            }
            Function v = getProperty(object, methodName);
            if (v instanceof Function) {
                Function fun = v;
                if (cx == null) {
                    cx = Context.getContext();
                }
                Object v2 = fun.call(cx, fun.getParentScope(), object, args);
                if (v2 == null) {
                    continue;
                } else if (!(v2 instanceof Scriptable) || typeHint == ScriptRuntime.ScriptableClass || typeHint == ScriptRuntime.FunctionClass) {
                    return v2;
                } else {
                    if (tryToString && (v2 instanceof Wrapper)) {
                        Object u = ((Wrapper) v2).unwrap();
                        if (u instanceof String) {
                            return u;
                        }
                    }
                }
            }
            i++;
        }
        throw ScriptRuntime.typeError1("msg.default.value", typeHint == null ? "undefined" : typeHint.getName());
    }

    public boolean hasInstance(Scriptable instance) {
        return ScriptRuntime.jsDelegatesTo(instance, this);
    }

    public boolean avoidObjectDetection() {
        return false;
    }

    /* access modifiers changed from: protected */
    public Object equivalentValues(Object value) {
        return this == value ? Boolean.TRUE : Scriptable.NOT_FOUND;
    }

    public static <T extends Scriptable> void defineClass(Scriptable scope, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        defineClass(scope, clazz, false, false);
    }

    public static <T extends Scriptable> void defineClass(Scriptable scope, Class<T> clazz, boolean sealed) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        defineClass(scope, clazz, sealed, false);
    }

    public static <T extends Scriptable> String defineClass(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        BaseFunction ctor = buildClassCtor(scope, clazz, sealed, mapInheritance);
        if (ctor == null) {
            return null;
        }
        String name = ctor.getClassPrototype().getClassName();
        defineProperty(scope, name, ctor, 2);
        return name;
    }

    static <T extends Scriptable> BaseFunction buildClassCtor(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        int i;
        Method method;
        Class<?>[] parmTypes;
        AccessibleObject[] methods = FunctionObject.getMethodList(clazz);
        for (Method method2 : methods) {
            if (method2.getName().equals("init")) {
                parmTypes = method2.getParameterTypes();
                if (parmTypes.length == 3 && parmTypes[0] == ScriptRuntime.ContextClass && parmTypes[1] == ScriptRuntime.ScriptableClass && parmTypes[2] == Boolean.TYPE && Modifier.isStatic(method2.getModifiers())) {
                    Object[] args = new Object[3];
                    args[0] = Context.getContext();
                    args[1] = scope;
                    args[2] = sealed ? Boolean.TRUE : Boolean.FALSE;
                    method2.invoke(null, args);
                    return null;
                } else if (parmTypes.length == 1 && parmTypes[0] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method2.getModifiers())) {
                    method2.invoke(null, new Object[]{scope});
                    return null;
                }
            }
        }
        AccessibleObject[] ctors = clazz.getConstructors();
        Constructor<?> protoCtor = null;
        for (i = 0; i < ctors.length; i++) {
            if (ctors[i].getParameterTypes().length == 0) {
                protoCtor = ctors[i];
                break;
            }
        }
        if (protoCtor == null) {
            throw Context.reportRuntimeError1("msg.zero.arg.ctor", clazz.getName());
        }
        String name;
        Scriptable proto = (Scriptable) protoCtor.newInstance(ScriptRuntime.emptyArgs);
        String className = proto.getClassName();
        Object existing = getProperty(getTopLevelScope(scope), className);
        if (existing instanceof BaseFunction) {
            Object existingProto = ((BaseFunction) existing).getPrototypeProperty();
            if (existingProto != null) {
                if (clazz.equals(existingProto.getClass())) {
                    return (BaseFunction) existing;
                }
            }
        }
        Scriptable superProto = null;
        if (mapInheritance) {
            Class<? super T> superClass = clazz.getSuperclass();
            if (ScriptRuntime.ScriptableClass.isAssignableFrom(superClass) && !Modifier.isAbstract(superClass.getModifiers())) {
                name = defineClass(scope, extendsScriptable(superClass), sealed, mapInheritance);
                if (name != null) {
                    superProto = getClassPrototype(scope, name);
                }
            }
        }
        if (superProto == null) {
            superProto = getObjectPrototype(scope);
        }
        proto.setPrototype(superProto);
        String functionPrefix = "jsFunction_";
        String staticFunctionPrefix = "jsStaticFunction_";
        String getterPrefix = "jsGet_";
        String setterPrefix = "jsSet_";
        String ctorName = "jsConstructor";
        Method ctorMember = findAnnotatedMember(methods, JSConstructor.class);
        if (ctorMember == null) {
            ctorMember = findAnnotatedMember(ctors, JSConstructor.class);
        }
        if (ctorMember == null) {
            ctorMember = FunctionObject.findSingleMethod(methods, "jsConstructor");
        }
        if (ctorMember == null) {
            if (ctors.length == 1) {
                ctorMember = ctors[0];
            } else if (ctors.length == 2) {
                if (ctors[0].getParameterTypes().length == 0) {
                    ctorMember = ctors[1];
                } else if (ctors[1].getParameterTypes().length == 0) {
                    ctorMember = ctors[0];
                }
            }
            if (ctorMember == null) {
                throw Context.reportRuntimeError1("msg.ctor.multiple.parms", clazz.getName());
            }
        }
        BaseFunction ctor = new FunctionObject(className, ctorMember, scope);
        if (ctor.isVarArgsMethod()) {
            throw Context.reportRuntimeError1("msg.varargs.ctor", ctorMember.getName());
        }
        ctor.initAsConstructor(scope, proto);
        Method finishInit = null;
        HashSet<String> staticNames = new HashSet();
        HashSet<String> instanceNames = new HashSet();
        int length = methods.length;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < length) {
                method2 = methods[i3];
                if (method2 != ctorMember) {
                    name = method2.getName();
                    if (name.equals("finishInit")) {
                        parmTypes = method2.getParameterTypes();
                        if (parmTypes.length == 3 && parmTypes[0] == ScriptRuntime.ScriptableClass && parmTypes[1] == FunctionObject.class && parmTypes[2] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method2.getModifiers())) {
                            finishInit = method2;
                        }
                    }
                    if (name.indexOf(36) == -1 && !name.equals("jsConstructor")) {
                        HashSet<String> names;
                        Annotation annotation = null;
                        String prefix = null;
                        if (method2.isAnnotationPresent(JSFunction.class)) {
                            annotation = method2.getAnnotation(JSFunction.class);
                        } else if (method2.isAnnotationPresent(JSStaticFunction.class)) {
                            annotation = method2.getAnnotation(JSStaticFunction.class);
                        } else if (method2.isAnnotationPresent(JSGetter.class)) {
                            annotation = method2.getAnnotation(JSGetter.class);
                        } else if (method2.isAnnotationPresent(JSSetter.class)) {
                            continue;
                        }
                        if (annotation == null) {
                            if (name.startsWith("jsFunction_")) {
                                prefix = "jsFunction_";
                            } else if (name.startsWith("jsStaticFunction_")) {
                                prefix = "jsStaticFunction_";
                            } else if (name.startsWith("jsGet_")) {
                                prefix = "jsGet_";
                            } else if (annotation == null) {
                                continue;
                            }
                        }
                        boolean isStatic = (annotation instanceof JSStaticFunction) || prefix == "jsStaticFunction_";
                        if (isStatic) {
                            names = staticNames;
                        } else {
                            names = instanceNames;
                        }
                        String propName = getPropertyName(name, prefix, annotation);
                        if (names.contains(propName)) {
                            throw Context.reportRuntimeError2("duplicate.defineClass.name", name, propName);
                        }
                        names.add(propName);
                        name = propName;
                        if ((annotation instanceof JSGetter) || prefix == "jsGet_") {
                            if (proto instanceof ScriptableObject) {
                                Method setter = findSetterMethod(methods, name, "jsSet_");
                                ((ScriptableObject) proto).defineProperty(name, null, method2, setter, (setter != null ? 0 : 1) | 6);
                            } else {
                                throw Context.reportRuntimeError2("msg.extend.scriptable", proto.getClass().toString(), name);
                            }
                        } else if (!isStatic || Modifier.isStatic(method2.getModifiers())) {
                            FunctionObject functionObject = new FunctionObject(name, method2, proto);
                            if (functionObject.isVarArgsConstructor()) {
                                throw Context.reportRuntimeError1("msg.varargs.fun", ctorMember.getName());
                            }
                            Scriptable scriptable;
                            if (isStatic) {
                                scriptable = ctor;
                            } else {
                                scriptable = proto;
                            }
                            defineProperty(scriptable, name, functionObject, 2);
                            if (sealed) {
                                functionObject.sealObject();
                            }
                        } else {
                            throw Context.reportRuntimeError("jsStaticFunction must be used with static method.");
                        }
                    }
                }
                i2 = i3 + 1;
            } else {
                if (finishInit != null) {
                    finishInit.invoke(null, new Object[]{scope, ctor, proto});
                }
                if (sealed) {
                    ctor.sealObject();
                    if (proto instanceof ScriptableObject) {
                        ((ScriptableObject) proto).sealObject();
                    }
                }
                return ctor;
            }
        }
    }

    private static Member findAnnotatedMember(AccessibleObject[] members, Class<? extends Annotation> annotation) {
        for (AccessibleObject member : members) {
            if (member.isAnnotationPresent(annotation)) {
                return (Member) member;
            }
        }
        return null;
    }

    private static Method findSetterMethod(Method[] methods, String name, String prefix) {
        Method method;
        int i = 0;
        String newStyleName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method method2 : methods) {
            JSSetter annotation = (JSSetter) method2.getAnnotation(JSSetter.class);
            if (annotation != null) {
                if (name.equals(annotation.value())) {
                    return method2;
                }
                if ("".equals(annotation.value()) && newStyleName.equals(method2.getName())) {
                    return method2;
                }
            }
        }
        String oldStyleName = prefix + name;
        int length = methods.length;
        while (i < length) {
            method2 = methods[i];
            if (oldStyleName.equals(method2.getName())) {
                return method2;
            }
            i++;
        }
        return null;
    }

    private static String getPropertyName(String methodName, String prefix, Annotation annotation) {
        if (prefix != null) {
            return methodName.substring(prefix.length());
        }
        String propName = null;
        if (annotation instanceof JSGetter) {
            propName = ((JSGetter) annotation).value();
            if ((propName == null || propName.length() == 0) && methodName.length() > 3 && methodName.startsWith("get")) {
                propName = methodName.substring(3);
                if (Character.isUpperCase(propName.charAt(0))) {
                    if (propName.length() == 1) {
                        propName = propName.toLowerCase();
                    } else if (!Character.isUpperCase(propName.charAt(1))) {
                        propName = Character.toLowerCase(propName.charAt(0)) + propName.substring(1);
                    }
                }
            }
        } else if (annotation instanceof JSFunction) {
            propName = ((JSFunction) annotation).value();
        } else if (annotation instanceof JSStaticFunction) {
            propName = ((JSStaticFunction) annotation).value();
        }
        if (propName == null || propName.length() == 0) {
            return methodName;
        }
        return propName;
    }

    private static <T extends Scriptable> Class<T> extendsScriptable(Class<?> c) {
        return ScriptRuntime.ScriptableClass.isAssignableFrom(c) ? c : null;
    }

    public void defineProperty(String propertyName, Object value, int attributes) {
        checkNotSealed(propertyName, 0);
        put(propertyName, (Scriptable) this, value);
        setAttributes(propertyName, attributes);
    }

    public static void defineProperty(Scriptable destination, String propertyName, Object value, int attributes) {
        if (destination instanceof ScriptableObject) {
            ((ScriptableObject) destination).defineProperty(propertyName, value, attributes);
        } else {
            destination.put(propertyName, destination, value);
        }
    }

    public static void defineConstProperty(Scriptable destination, String propertyName) {
        if (destination instanceof ConstProperties) {
            ((ConstProperties) destination).defineConst(propertyName, destination);
        } else {
            defineProperty(destination, propertyName, Undefined.instance, 13);
        }
    }

    public void defineProperty(String propertyName, Class<?> clazz, int attributes) {
        int length = propertyName.length();
        if (length == 0) {
            throw new IllegalArgumentException();
        }
        Method method;
        char[] buf = new char[(length + 3)];
        propertyName.getChars(0, length, buf, 3);
        buf[3] = Character.toUpperCase(buf[3]);
        buf[0] = 'g';
        buf[1] = 'e';
        buf[2] = 't';
        String getterName = new String(buf);
        buf[0] = 's';
        String setterName = new String(buf);
        Method[] methods = FunctionObject.getMethodList(clazz);
        Method getter = FunctionObject.findSingleMethod(methods, getterName);
        Method setter = FunctionObject.findSingleMethod(methods, setterName);
        if (setter == null) {
            attributes |= 1;
        }
        if (setter == null) {
            method = null;
        } else {
            method = setter;
        }
        defineProperty(propertyName, null, getter, method, attributes);
    }

    public void defineProperty(String propertyName, Object delegateTo, Method getter, Method setter, int attributes) {
        boolean delegatedForm;
        String errorId;
        Class<?>[] parmTypes;
        Class argType;
        MemberBox getterBox = null;
        if (getter != null) {
            getterBox = new MemberBox(getter);
            if (Modifier.isStatic(getter.getModifiers())) {
                delegatedForm = true;
                getterBox.delegateTo = Void.TYPE;
            } else {
                delegatedForm = delegateTo != null;
                getterBox.delegateTo = delegateTo;
            }
            errorId = null;
            parmTypes = getter.getParameterTypes();
            if (parmTypes.length == 0) {
                if (delegatedForm) {
                    errorId = "msg.obj.getter.parms";
                }
            } else if (parmTypes.length == 1) {
                argType = parmTypes[0];
                if (argType != ScriptRuntime.ScriptableClass && argType != ScriptRuntime.ScriptableObjectClass) {
                    errorId = "msg.bad.getter.parms";
                } else if (!delegatedForm) {
                    errorId = "msg.bad.getter.parms";
                }
            } else {
                errorId = "msg.bad.getter.parms";
            }
            if (errorId != null) {
                throw Context.reportRuntimeError1(errorId, getter.toString());
            }
        }
        MemberBox setterBox = null;
        if (setter != null) {
            if (setter.getReturnType() != Void.TYPE) {
                throw Context.reportRuntimeError1("msg.setter.return", setter.toString());
            }
            setterBox = new MemberBox(setter);
            if (Modifier.isStatic(setter.getModifiers())) {
                delegatedForm = true;
                setterBox.delegateTo = Void.TYPE;
            } else {
                delegatedForm = delegateTo != null;
                setterBox.delegateTo = delegateTo;
            }
            errorId = null;
            parmTypes = setter.getParameterTypes();
            if (parmTypes.length == 1) {
                if (delegatedForm) {
                    errorId = "msg.setter2.expected";
                }
            } else if (parmTypes.length == 2) {
                argType = parmTypes[0];
                if (argType != ScriptRuntime.ScriptableClass && argType != ScriptRuntime.ScriptableObjectClass) {
                    errorId = "msg.setter2.parms";
                } else if (!delegatedForm) {
                    errorId = "msg.setter1.parms";
                }
            } else {
                errorId = "msg.setter.parms";
            }
            if (errorId != null) {
                throw Context.reportRuntimeError1(errorId, setter.toString());
            }
        }
        GetterSlot gslot = (GetterSlot) getSlot(propertyName, 0, 4);
        gslot.setAttributes(attributes);
        gslot.getter = getterBox;
        gslot.setter = setterBox;
    }

    public void defineOwnProperties(Context cx, ScriptableObject props) {
        int i;
        Object[] ids = props.getIds();
        ScriptableObject[] descs = new ScriptableObject[ids.length];
        int len = ids.length;
        for (i = 0; i < len; i++) {
            ScriptableObject desc = ensureScriptableObject(ScriptRuntime.getObjectElem((Scriptable) props, ids[i], cx));
            checkPropertyDefinition(desc);
            descs[i] = desc;
        }
        len = ids.length;
        for (i = 0; i < len; i++) {
            defineOwnProperty(cx, ids[i], descs[i]);
        }
    }

    public void defineOwnProperty(Context cx, Object id, ScriptableObject desc) {
        checkPropertyDefinition(desc);
        defineOwnProperty(cx, id, desc, true);
    }

    /* access modifiers changed from: protected */
    public void defineOwnProperty(Context cx, Object id, ScriptableObject desc, boolean checkValid) {
        int attributes;
        Slot slot = getSlot(cx, id, 1);
        boolean isNew = slot == null;
        if (checkValid) {
            ScriptableObject current;
            if (slot == null) {
                current = null;
            } else {
                current = slot.getPropertyDescriptor(cx, this);
            }
            checkPropertyChange(ScriptRuntime.toString(id), current, desc);
        }
        boolean isAccessor = isAccessorDescriptor(desc);
        if (slot == null) {
            slot = getSlot(cx, id, isAccessor ? 4 : 2);
            attributes = applyDescriptorToAttributeBitset(7, desc);
        } else {
            attributes = applyDescriptorToAttributeBitset(slot.getAttributes(), desc);
        }
        slot = unwrapSlot(slot);
        if (isAccessor) {
            if (!(slot instanceof GetterSlot)) {
                slot = getSlot(cx, id, 4);
            }
            GetterSlot gslot = (GetterSlot) slot;
            Object getter = getProperty((Scriptable) desc, "get");
            if (getter != NOT_FOUND) {
                gslot.getter = getter;
            }
            Object setter = getProperty((Scriptable) desc, "set");
            if (setter != NOT_FOUND) {
                gslot.setter = setter;
            }
            gslot.value = Undefined.instance;
            gslot.setAttributes(attributes);
            return;
        }
        if ((slot instanceof GetterSlot) && isDataDescriptor(desc)) {
            slot = getSlot(cx, id, 5);
        }
        Object value = getProperty((Scriptable) desc, "value");
        if (value != NOT_FOUND) {
            slot.value = value;
        } else if (isNew) {
            slot.value = Undefined.instance;
        }
        slot.setAttributes(attributes);
    }

    /* access modifiers changed from: protected */
    public void checkPropertyDefinition(ScriptableObject desc) {
        Object getter = getProperty((Scriptable) desc, "get");
        if (getter == NOT_FOUND || getter == Undefined.instance || (getter instanceof Callable)) {
            Object setter = getProperty((Scriptable) desc, "set");
            if (setter != NOT_FOUND && setter != Undefined.instance && !(setter instanceof Callable)) {
                throw ScriptRuntime.notFunctionError(setter);
            } else if (isDataDescriptor(desc) && isAccessorDescriptor(desc)) {
                throw ScriptRuntime.typeError0("msg.both.data.and.accessor.desc");
            } else {
                return;
            }
        }
        throw ScriptRuntime.notFunctionError(getter);
    }

    /* access modifiers changed from: protected */
    public void checkPropertyChange(String id, ScriptableObject current, ScriptableObject desc) {
        if (current == null) {
            if (!isExtensible()) {
                throw ScriptRuntime.typeError0("msg.not.extensible");
            }
        } else if (!isFalse(current.get("configurable", (Scriptable) current))) {
        } else {
            if (isTrue(getProperty((Scriptable) desc, "configurable"))) {
                throw ScriptRuntime.typeError1("msg.change.configurable.false.to.true", id);
            } else if (isTrue(current.get("enumerable", (Scriptable) current)) != isTrue(getProperty((Scriptable) desc, "enumerable"))) {
                throw ScriptRuntime.typeError1("msg.change.enumerable.with.configurable.false", id);
            } else {
                boolean isData = isDataDescriptor(desc);
                boolean isAccessor = isAccessorDescriptor(desc);
                if (!isData && !isAccessor) {
                    return;
                }
                if (isData && isDataDescriptor(current)) {
                    if (!isFalse(current.get("writable", (Scriptable) current))) {
                        return;
                    }
                    if (isTrue(getProperty((Scriptable) desc, "writable"))) {
                        throw ScriptRuntime.typeError1("msg.change.writable.false.to.true.with.configurable.false", id);
                    } else if (!sameValue(getProperty((Scriptable) desc, "value"), current.get("value", (Scriptable) current))) {
                        throw ScriptRuntime.typeError1("msg.change.value.with.writable.false", id);
                    }
                } else if (isAccessor && isAccessorDescriptor(current)) {
                    if (!sameValue(getProperty((Scriptable) desc, "set"), current.get("set", (Scriptable) current))) {
                        throw ScriptRuntime.typeError1("msg.change.setter.with.configurable.false", id);
                    } else if (!sameValue(getProperty((Scriptable) desc, "get"), current.get("get", (Scriptable) current))) {
                        throw ScriptRuntime.typeError1("msg.change.getter.with.configurable.false", id);
                    }
                } else if (isDataDescriptor(current)) {
                    throw ScriptRuntime.typeError1("msg.change.property.data.to.accessor.with.configurable.false", id);
                } else {
                    throw ScriptRuntime.typeError1("msg.change.property.accessor.to.data.with.configurable.false", id);
                }
            }
        }
    }

    protected static boolean isTrue(Object value) {
        return value != NOT_FOUND && ScriptRuntime.toBoolean(value);
    }

    protected static boolean isFalse(Object value) {
        return !isTrue(value);
    }

    /* access modifiers changed from: protected */
    public boolean sameValue(Object newValue, Object currentValue) {
        if (newValue == NOT_FOUND) {
            return true;
        }
        if (currentValue == NOT_FOUND) {
            currentValue = Undefined.instance;
        }
        if ((currentValue instanceof Number) && (newValue instanceof Number)) {
            double d1 = ((Number) currentValue).doubleValue();
            double d2 = ((Number) newValue).doubleValue();
            if (Double.isNaN(d1) && Double.isNaN(d2)) {
                return true;
            }
            if (d1 == 0.0d && Double.doubleToLongBits(d1) != Double.doubleToLongBits(d2)) {
                return false;
            }
        }
        return ScriptRuntime.shallowEq(currentValue, newValue);
    }

    /* access modifiers changed from: protected */
    public int applyDescriptorToAttributeBitset(int attributes, ScriptableObject desc) {
        Object enumerable = getProperty((Scriptable) desc, "enumerable");
        if (enumerable != NOT_FOUND) {
            attributes = ScriptRuntime.toBoolean(enumerable) ? attributes & -3 : attributes | 2;
        }
        Object writable = getProperty((Scriptable) desc, "writable");
        if (writable != NOT_FOUND) {
            attributes = ScriptRuntime.toBoolean(writable) ? attributes & -2 : attributes | 1;
        }
        Object configurable = getProperty((Scriptable) desc, "configurable");
        if (configurable != NOT_FOUND) {
            return ScriptRuntime.toBoolean(configurable) ? attributes & -5 : attributes | 4;
        } else {
            return attributes;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isDataDescriptor(ScriptableObject desc) {
        return hasProperty((Scriptable) desc, "value") || hasProperty((Scriptable) desc, "writable");
    }

    /* access modifiers changed from: protected */
    public boolean isAccessorDescriptor(ScriptableObject desc) {
        return hasProperty((Scriptable) desc, "get") || hasProperty((Scriptable) desc, "set");
    }

    /* access modifiers changed from: protected */
    public boolean isGenericDescriptor(ScriptableObject desc) {
        return (isDataDescriptor(desc) || isAccessorDescriptor(desc)) ? false : true;
    }

    protected static Scriptable ensureScriptable(Object arg) {
        if (arg instanceof Scriptable) {
            return (Scriptable) arg;
        }
        throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(arg));
    }

    protected static ScriptableObject ensureScriptableObject(Object arg) {
        if (arg instanceof ScriptableObject) {
            return (ScriptableObject) arg;
        }
        throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(arg));
    }

    public void defineFunctionProperties(String[] names, Class<?> clazz, int attributes) {
        Method[] methods = FunctionObject.getMethodList(clazz);
        for (String name : names) {
            Method m = FunctionObject.findSingleMethod(methods, name);
            if (m == null) {
                throw Context.reportRuntimeError2("msg.method.not.found", name, clazz.getName());
            }
            defineProperty(name, new FunctionObject(name, m, this), attributes);
        }
    }

    public static Scriptable getObjectPrototype(Scriptable scope) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), Builtins.Object);
    }

    public static Scriptable getFunctionPrototype(Scriptable scope) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), Builtins.Function);
    }

    public static Scriptable getArrayPrototype(Scriptable scope) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), Builtins.Array);
    }

    public static Scriptable getClassPrototype(Scriptable scope, String className) {
        Object proto;
        Scriptable ctor = getProperty(getTopLevelScope(scope), className);
        if (ctor instanceof BaseFunction) {
            proto = ((BaseFunction) ctor).getPrototypeProperty();
        } else if (!(ctor instanceof Scriptable)) {
            return null;
        } else {
            Scriptable ctorObj = ctor;
            proto = ctorObj.get("prototype", ctorObj);
        }
        if (proto instanceof Scriptable) {
            return (Scriptable) proto;
        }
        return null;
    }

    public static Scriptable getTopLevelScope(Scriptable obj) {
        while (true) {
            Scriptable parent = obj.getParentScope();
            if (parent == null) {
                return obj;
            }
            obj = parent;
        }
    }

    public boolean isExtensible() {
        return this.isExtensible;
    }

    public void preventExtensions() {
        this.isExtensible = false;
    }

    public synchronized void sealObject() {
        if (this.count >= 0) {
            for (Slot slot = this.firstAdded; slot != null; slot = slot.orderedNext) {
                Object value = slot.value;
                if (value instanceof LazilyLoadedCtor) {
                    LazilyLoadedCtor initializer = (LazilyLoadedCtor) value;
                    try {
                        initializer.init();
                        slot.value = initializer.getValue();
                    } catch (Throwable th) {
                        slot.value = initializer.getValue();
                    }
                }
            }
            this.count ^= -1;
        }
    }

    public final boolean isSealed() {
        return this.count < 0;
    }

    private void checkNotSealed(String name, int index) {
        if (isSealed()) {
            throw Context.reportRuntimeError1("msg.modify.sealed", name != null ? name : Integer.toString(index));
        }
    }

    public static Object getProperty(Scriptable obj, String name) {
        Object result;
        Scriptable start = obj;
        do {
            result = obj.get(name, start);
            if (result != Scriptable.NOT_FOUND) {
                break;
            }
            obj = obj.getPrototype();
        } while (obj != null);
        return result;
    }

    public static <T> T getTypedProperty(Scriptable s, int index, Class<T> type) {
        Object val = getProperty(s, index);
        if (val == Scriptable.NOT_FOUND) {
            val = null;
        }
        return type.cast(Context.jsToJava(val, type));
    }

    public static Object getProperty(Scriptable obj, int index) {
        Object result;
        Scriptable start = obj;
        do {
            result = obj.get(index, start);
            if (result != Scriptable.NOT_FOUND) {
                break;
            }
            obj = obj.getPrototype();
        } while (obj != null);
        return result;
    }

    public static <T> T getTypedProperty(Scriptable s, String name, Class<T> type) {
        Object val = getProperty(s, name);
        if (val == Scriptable.NOT_FOUND) {
            val = null;
        }
        return type.cast(Context.jsToJava(val, type));
    }

    public static boolean hasProperty(Scriptable obj, String name) {
        return getBase(obj, name) != null;
    }

    public static void redefineProperty(Scriptable obj, String name, boolean isConst) {
        Scriptable base = getBase(obj, name);
        if (base != null) {
            if ((base instanceof ConstProperties) && ((ConstProperties) base).isConst(name)) {
                throw ScriptRuntime.typeError1("msg.const.redecl", name);
            } else if (isConst) {
                throw ScriptRuntime.typeError1("msg.var.redecl", name);
            }
        }
    }

    public static boolean hasProperty(Scriptable obj, int index) {
        return getBase(obj, index) != null;
    }

    public static void putProperty(Scriptable obj, String name, Object value) {
        Scriptable base = getBase(obj, name);
        if (base == null) {
            base = obj;
        }
        base.put(name, obj, value);
    }

    public static void putConstProperty(Scriptable obj, String name, Object value) {
        Scriptable base = getBase(obj, name);
        if (base == null) {
            base = obj;
        }
        if (base instanceof ConstProperties) {
            ((ConstProperties) base).putConst(name, obj, value);
        }
    }

    public static void putProperty(Scriptable obj, int index, Object value) {
        Scriptable base = getBase(obj, index);
        if (base == null) {
            base = obj;
        }
        base.put(index, obj, value);
    }

    public static boolean deleteProperty(Scriptable obj, String name) {
        Scriptable base = getBase(obj, name);
        if (base == null) {
            return true;
        }
        base.delete(name);
        if (base.has(name, obj)) {
            return false;
        }
        return true;
    }

    public static boolean deleteProperty(Scriptable obj, int index) {
        Scriptable base = getBase(obj, index);
        if (base == null) {
            return true;
        }
        base.delete(index);
        if (base.has(index, obj)) {
            return false;
        }
        return true;
    }

    public static Object[] getPropertyIds(Scriptable obj) {
        if (obj == null) {
            return ScriptRuntime.emptyArgs;
        }
        Object[] result = obj.getIds();
        ObjToIntMap map = null;
        while (true) {
            obj = obj.getPrototype();
            if (obj == null) {
                break;
            }
            Object[] ids = obj.getIds();
            if (ids.length != 0) {
                int i;
                if (map == null) {
                    if (result.length == 0) {
                        result = ids;
                    } else {
                        map = new ObjToIntMap(result.length + ids.length);
                        for (i = 0; i != result.length; i++) {
                            map.intern(result[i]);
                        }
                        result = null;
                    }
                }
                for (i = 0; i != ids.length; i++) {
                    map.intern(ids[i]);
                }
            }
        }
        if (map != null) {
            return map.getKeys();
        }
        return result;
    }

    public static Object callMethod(Scriptable obj, String methodName, Object[] args) {
        return callMethod(null, obj, methodName, args);
    }

    public static Object callMethod(Context cx, Scriptable obj, String methodName, Object[] args) {
        Function funObj = getProperty(obj, methodName);
        if (funObj instanceof Function) {
            Function fun = funObj;
            Scriptable scope = getTopLevelScope(obj);
            if (cx != null) {
                return fun.call(cx, scope, obj, args);
            }
            return Context.call(null, fun, scope, obj, args);
        }
        throw ScriptRuntime.notFunctionError(obj, methodName);
    }

    private static Scriptable getBase(Scriptable obj, String name) {
        while (!obj.has(name, obj)) {
            obj = obj.getPrototype();
            if (obj == null) {
                break;
            }
        }
        return obj;
    }

    private static Scriptable getBase(Scriptable obj, int index) {
        while (!obj.has(index, obj)) {
            obj = obj.getPrototype();
            if (obj == null) {
                break;
            }
        }
        return obj;
    }

    public final Object getAssociatedValue(Object key) {
        Map<Object, Object> h = this.associatedValues;
        if (h == null) {
            return null;
        }
        return h.get(key);
    }

    public static Object getTopScopeValue(Scriptable scope, Object key) {
        scope = getTopLevelScope(scope);
        do {
            if (scope instanceof ScriptableObject) {
                Object value = ((ScriptableObject) scope).getAssociatedValue(key);
                if (value != null) {
                    return value;
                }
            }
            scope = scope.getPrototype();
        } while (scope != null);
        return null;
    }

    public final synchronized Object associateValue(Object key, Object value) {
        Map<Object, Object> h;
        if (value == null) {
            throw new IllegalArgumentException();
        }
        h = this.associatedValues;
        if (h == null) {
            h = new HashMap();
            this.associatedValues = h;
        }
        return Kit.initHash(h, key, value);
    }

    private boolean putImpl(String name, int index, Scriptable start, Object value) {
        Slot slot;
        if (this != start) {
            slot = getSlot(name, index, 1);
            if (slot == null) {
                return false;
            }
        } else if (this.isExtensible) {
            if (this.count < 0) {
                checkNotSealed(name, index);
            }
            slot = getSlot(name, index, 2);
        } else {
            slot = getSlot(name, index, 1);
            if (slot == null) {
                return true;
            }
        }
        return slot.setValue(value, this, start);
    }

    private boolean putConstImpl(String name, int index, Scriptable start, Object value, int constFlag) {
        if ($assertionsDisabled || constFlag != 0) {
            Slot slot;
            if (this != start) {
                slot = getSlot(name, index, 1);
                if (slot == null) {
                    return false;
                }
            } else if (isExtensible()) {
                checkNotSealed(name, index);
                slot = unwrapSlot(getSlot(name, index, 3));
                int attr = slot.getAttributes();
                if ((attr & 1) == 0) {
                    throw Context.reportRuntimeError1("msg.var.redecl", name);
                } else if ((attr & 8) == 0) {
                    return true;
                } else {
                    slot.value = value;
                    if (constFlag == 8) {
                        return true;
                    }
                    slot.setAttributes(attr & -9);
                    return true;
                }
            } else {
                slot = getSlot(name, index, 1);
                if (slot == null) {
                    return true;
                }
            }
            return slot.setValue(value, this, start);
        }
        throw new AssertionError();
    }

    private Slot findAttributeSlot(String name, int index, int accessType) {
        Slot slot = getSlot(name, index, accessType);
        if (slot != null) {
            return slot;
        }
        throw Context.reportRuntimeError1("msg.prop.not.found", name != null ? name : Integer.toString(index));
    }

    /* access modifiers changed from: private|static */
    public static Slot unwrapSlot(Slot slot) {
        return slot instanceof RelinkedSlot ? ((RelinkedSlot) slot).slot : slot;
    }

    private Slot getSlot(String name, int index, int accessType) {
        Slot[] slotsLocalRef = this.slots;
        if (slotsLocalRef == null && accessType == 1) {
            return null;
        }
        int indexOrHash;
        if (name != null) {
            indexOrHash = name.hashCode();
        } else {
            indexOrHash = index;
        }
        if (slotsLocalRef != null) {
            Slot slot = slotsLocalRef[getSlotIndex(slotsLocalRef.length, indexOrHash)];
            while (slot != null) {
                String sname = slot.name;
                if (indexOrHash == slot.indexOrHash && (sname == name || (name != null && name.equals(sname)))) {
                    break;
                }
                slot = slot.next;
            }
            switch (accessType) {
                case 1:
                    return slot;
                case 2:
                case 3:
                    if (slot != null) {
                        return slot;
                    }
                    break;
                case 4:
                    slot = unwrapSlot(slot);
                    if (slot instanceof GetterSlot) {
                        return slot;
                    }
                    break;
                case 5:
                    slot = unwrapSlot(slot);
                    if (!(slot instanceof GetterSlot)) {
                        return slot;
                    }
                    break;
            }
        }
        return createSlot(name, indexOrHash, accessType);
    }

    private synchronized Slot createSlot(String name, int indexOrHash, int accessType) {
        Slot newSlot;
        int insertPos;
        Slot[] slotsLocalRef = this.slots;
        if (this.count == 0) {
            slotsLocalRef = new Slot[4];
            this.slots = slotsLocalRef;
            insertPos = getSlotIndex(slotsLocalRef.length, indexOrHash);
        } else {
            insertPos = getSlotIndex(slotsLocalRef.length, indexOrHash);
            Slot prev = slotsLocalRef[insertPos];
            Slot slot = prev;
            while (slot != null && (slot.indexOrHash != indexOrHash || (slot.name != name && (name == null || !name.equals(slot.name))))) {
                prev = slot;
                slot = slot.next;
            }
            if (slot != null) {
                Slot inner = unwrapSlot(slot);
                if (accessType == 4 && !(inner instanceof GetterSlot)) {
                    newSlot = new GetterSlot(name, indexOrHash, inner.getAttributes());
                } else if (accessType == 5 && (inner instanceof GetterSlot)) {
                    newSlot = new Slot(name, indexOrHash, inner.getAttributes());
                } else {
                    newSlot = accessType == 3 ? null : inner;
                }
                newSlot.value = inner.value;
                newSlot.next = slot.next;
                if (this.lastAdded != null) {
                    this.lastAdded.orderedNext = newSlot;
                }
                if (this.firstAdded == null) {
                    this.firstAdded = newSlot;
                }
                this.lastAdded = newSlot;
                if (prev == slot) {
                    slotsLocalRef[insertPos] = newSlot;
                } else {
                    prev.next = newSlot;
                }
                slot.markDeleted();
            } else if ((this.count + 1) * 4 > slotsLocalRef.length * 3) {
                slotsLocalRef = new Slot[(slotsLocalRef.length * 2)];
                copyTable(this.slots, slotsLocalRef, this.count);
                this.slots = slotsLocalRef;
                insertPos = getSlotIndex(slotsLocalRef.length, indexOrHash);
            }
        }
        newSlot = accessType == 4 ? new GetterSlot(name, indexOrHash, 0) : new Slot(name, indexOrHash, 0);
        if (accessType == 3) {
            newSlot.setAttributes(13);
        }
        this.count++;
        if (this.lastAdded != null) {
            this.lastAdded.orderedNext = newSlot;
        }
        if (this.firstAdded == null) {
            this.firstAdded = newSlot;
        }
        this.lastAdded = newSlot;
        addKnownAbsentSlot(slotsLocalRef, newSlot, insertPos);
        return newSlot;
    }

    private synchronized void removeSlot(String name, int index) {
        int indexOrHash;
        if (name != null) {
            indexOrHash = name.hashCode();
        } else {
            indexOrHash = index;
        }
        Slot[] slotsLocalRef = this.slots;
        if (this.count != 0) {
            int slotIndex = getSlotIndex(slotsLocalRef.length, indexOrHash);
            Slot prev = slotsLocalRef[slotIndex];
            Slot slot = prev;
            while (slot != null && (slot.indexOrHash != indexOrHash || (slot.name != name && (name == null || !name.equals(slot.name))))) {
                prev = slot;
                slot = slot.next;
            }
            if (slot != null && (slot.getAttributes() & 4) == 0) {
                this.count--;
                if (prev == slot) {
                    slotsLocalRef[slotIndex] = slot.next;
                } else {
                    prev.next = slot.next;
                }
                Slot deleted = unwrapSlot(slot);
                if (deleted == this.firstAdded) {
                    prev = null;
                    this.firstAdded = deleted.orderedNext;
                } else {
                    prev = this.firstAdded;
                    while (prev.orderedNext != deleted) {
                        prev = prev.orderedNext;
                    }
                    prev.orderedNext = deleted.orderedNext;
                }
                if (deleted == this.lastAdded) {
                    this.lastAdded = prev;
                }
                slot.markDeleted();
            }
        }
    }

    private static int getSlotIndex(int tableSize, int indexOrHash) {
        return (tableSize - 1) & indexOrHash;
    }

    private static void copyTable(Slot[] oldSlots, Slot[] newSlots, int count) {
        if (count == 0) {
            throw Kit.codeBug();
        }
        int tableSize = newSlots.length;
        int i = oldSlots.length;
        while (true) {
            i--;
            Slot slot = oldSlots[i];
            while (slot != null) {
                addKnownAbsentSlot(newSlots, slot.next == null ? slot : new RelinkedSlot(slot), getSlotIndex(tableSize, slot.indexOrHash));
                slot = slot.next;
                count--;
                if (count == 0) {
                    return;
                }
            }
        }
    }

    private static void addKnownAbsentSlot(Slot[] slots, Slot slot, int insertPos) {
        if (slots[insertPos] == null) {
            slots[insertPos] = slot;
            return;
        }
        Slot prev = slots[insertPos];
        Slot next = prev.next;
        while (next != null) {
            prev = next;
            next = prev.next;
        }
        prev.next = slot;
    }

    /* access modifiers changed from: 0000 */
    public Object[] getIds(boolean getAll) {
        Object[] a;
        Slot[] s = this.slots;
        int externalLen = this.externalData == null ? 0 : this.externalData.getArrayLength();
        if (externalLen == 0) {
            a = ScriptRuntime.emptyArgs;
        } else {
            a = new Object[externalLen];
            for (int i = 0; i < externalLen; i++) {
                a[i] = Integer.valueOf(i);
            }
        }
        if (s == null) {
            return a;
        }
        int c;
        int c2 = externalLen;
        Slot slot = this.firstAdded;
        while (slot != null && slot.wasDeleted) {
            slot = slot.orderedNext;
        }
        while (true) {
            c = c2;
            if (slot == null) {
                break;
            }
            if (getAll || (slot.getAttributes() & 2) == 0) {
                Integer num;
                if (c == externalLen) {
                    Object[] oldA = a;
                    a = new Object[(s.length + externalLen)];
                    if (oldA != null) {
                        System.arraycopy(oldA, 0, a, 0, externalLen);
                    }
                }
                c2 = c + 1;
                if (slot.name != null) {
                    num = slot.name;
                } else {
                    num = Integer.valueOf(slot.indexOrHash);
                }
                a[c] = num;
            } else {
                c2 = c;
            }
            slot = slot.orderedNext;
            while (slot != null && slot.wasDeleted) {
                slot = slot.orderedNext;
            }
        }
        if (c == a.length + externalLen) {
            return a;
        }
        Object[] result = new Object[c];
        System.arraycopy(a, 0, result, 0, c);
        return result;
    }

    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        int objectsCount = this.count;
        if (objectsCount < 0) {
            objectsCount ^= -1;
        }
        if (objectsCount == 0) {
            out.writeInt(0);
        } else {
            out.writeInt(this.slots.length);
            Slot slot = this.firstAdded;
            while (slot != null && slot.wasDeleted) {
                slot = slot.orderedNext;
            }
            this.firstAdded = slot;
            while (slot != null) {
                out.writeObject(slot);
                Slot next = slot.orderedNext;
                while (next != null && next.wasDeleted) {
                    next = next.orderedNext;
                }
                slot.orderedNext = next;
                slot = next;
            }
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int tableSize = in.readInt();
        if (tableSize != 0) {
            if (((tableSize - 1) & tableSize) != 0) {
                if (tableSize > 1073741824) {
                    throw new RuntimeException("Property table overflow");
                }
                int newSize = 4;
                while (newSize < tableSize) {
                    newSize <<= 1;
                }
                tableSize = newSize;
            }
            this.slots = new Slot[tableSize];
            int objectsCount = this.count;
            if (objectsCount < 0) {
                objectsCount ^= -1;
            }
            Slot prev = null;
            for (int i = 0; i != objectsCount; i++) {
                this.lastAdded = (Slot) in.readObject();
                if (i == 0) {
                    this.firstAdded = this.lastAdded;
                } else {
                    prev.orderedNext = this.lastAdded;
                }
                addKnownAbsentSlot(this.slots, this.lastAdded, getSlotIndex(tableSize, this.lastAdded.indexOrHash));
                prev = this.lastAdded;
            }
        }
    }

    /* access modifiers changed from: protected */
    public ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
        Slot slot = getSlot(cx, id, 1);
        if (slot == null) {
            return null;
        }
        Scriptable thisR;
        Scriptable scope = getParentScope();
        if (scope != null) {
            thisR = scope;
        }
        return slot.getPropertyDescriptor(cx, thisR);
    }

    /* access modifiers changed from: protected */
    public Slot getSlot(Context cx, Object id, int accessType) {
        String name = ScriptRuntime.toStringIdOrIndex(cx, id);
        if (name == null) {
            return getSlot(null, ScriptRuntime.lastIndexResult(cx), accessType);
        }
        return getSlot(name, 0, accessType);
    }

    public int size() {
        return this.count < 0 ? this.count ^ -1 : this.count;
    }

    public boolean isEmpty() {
        return this.count == 0 || this.count == -1;
    }

    public Object get(Object key) {
        Object value = null;
        if (key instanceof String) {
            value = get((String) key, (Scriptable) this);
        } else if (key instanceof Number) {
            value = get(((Number) key).intValue(), (Scriptable) this);
        }
        if (value == Scriptable.NOT_FOUND || value == Undefined.instance) {
            return null;
        }
        if (value instanceof Wrapper) {
            return ((Wrapper) value).unwrap();
        }
        return value;
    }
}
