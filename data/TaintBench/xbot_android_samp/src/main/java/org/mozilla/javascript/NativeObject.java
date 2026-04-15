package org.mozilla.javascript;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class NativeObject extends IdScriptableObject implements Map {
    private static final int ConstructorId_create = -9;
    private static final int ConstructorId_defineProperties = -8;
    private static final int ConstructorId_defineProperty = -5;
    private static final int ConstructorId_freeze = -13;
    private static final int ConstructorId_getOwnPropertyDescriptor = -4;
    private static final int ConstructorId_getOwnPropertyNames = -3;
    private static final int ConstructorId_getPrototypeOf = -1;
    private static final int ConstructorId_isExtensible = -6;
    private static final int ConstructorId_isFrozen = -11;
    private static final int ConstructorId_isSealed = -10;
    private static final int ConstructorId_keys = -2;
    private static final int ConstructorId_preventExtensions = -7;
    private static final int ConstructorId_seal = -12;
    private static final int Id___defineGetter__ = 9;
    private static final int Id___defineSetter__ = 10;
    private static final int Id___lookupGetter__ = 11;
    private static final int Id___lookupSetter__ = 12;
    private static final int Id_constructor = 1;
    private static final int Id_hasOwnProperty = 5;
    private static final int Id_isPrototypeOf = 7;
    private static final int Id_propertyIsEnumerable = 6;
    private static final int Id_toLocaleString = 3;
    private static final int Id_toSource = 8;
    private static final int Id_toString = 2;
    private static final int Id_valueOf = 4;
    private static final int MAX_PROTOTYPE_ID = 12;
    private static final Object OBJECT_TAG = "Object";
    static final long serialVersionUID = -6345305608474346996L;

    class EntrySet extends AbstractSet<Entry<Object, Object>> {
        EntrySet() {
        }

        public Iterator<Entry<Object, Object>> iterator() {
            return new Iterator<Entry<Object, Object>>() {
                Object[] ids = NativeObject.this.getIds();
                int index = 0;
                Object key = null;

                public boolean hasNext() {
                    return this.index < this.ids.length;
                }

                public Entry<Object, Object> next() {
                    Object[] objArr = this.ids;
                    int i = this.index;
                    this.index = i + 1;
                    final Object ekey = objArr[i];
                    this.key = ekey;
                    final Object value = NativeObject.this.get(this.key);
                    return new Entry<Object, Object>() {
                        public Object getKey() {
                            return ekey;
                        }

                        public Object getValue() {
                            return value;
                        }

                        public Object setValue(Object value) {
                            throw new UnsupportedOperationException();
                        }

                        public boolean equals(Object other) {
                            if (!(other instanceof Entry)) {
                                return false;
                            }
                            Entry<?, ?> e = (Entry) other;
                            if (ekey == null) {
                                if (e.getKey() != null) {
                                    return false;
                                }
                            } else if (!ekey.equals(e.getKey())) {
                                return false;
                            }
                            if (value == null) {
                                if (e.getValue() != null) {
                                    return false;
                                }
                            } else if (!value.equals(e.getValue())) {
                                return false;
                            }
                            return true;
                        }

                        public int hashCode() {
                            int i = 0;
                            int hashCode = ekey == null ? 0 : ekey.hashCode();
                            if (value != null) {
                                i = value.hashCode();
                            }
                            return hashCode ^ i;
                        }

                        public String toString() {
                            return ekey + "=" + value;
                        }
                    };
                }

                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    }
                    NativeObject.this.remove(this.key);
                    this.key = null;
                }
            };
        }

        public int size() {
            return NativeObject.this.size();
        }
    }

    class KeySet extends AbstractSet<Object> {
        KeySet() {
        }

        public boolean contains(Object key) {
            return NativeObject.this.containsKey(key);
        }

        public Iterator<Object> iterator() {
            return new Iterator<Object>() {
                Object[] ids = NativeObject.this.getIds();
                int index = 0;
                Object key;

                public boolean hasNext() {
                    return this.index < this.ids.length;
                }

                public Object next() {
                    try {
                        Object[] objArr = this.ids;
                        int i = this.index;
                        this.index = i + 1;
                        Object obj = objArr[i];
                        this.key = obj;
                        return obj;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        this.key = null;
                        throw new NoSuchElementException();
                    }
                }

                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    }
                    NativeObject.this.remove(this.key);
                    this.key = null;
                }
            };
        }

        public int size() {
            return NativeObject.this.size();
        }
    }

    class ValueCollection extends AbstractCollection<Object> {
        ValueCollection() {
        }

        public Iterator<Object> iterator() {
            return new Iterator<Object>() {
                Object[] ids = NativeObject.this.getIds();
                int index = 0;
                Object key;

                public boolean hasNext() {
                    return this.index < this.ids.length;
                }

                public Object next() {
                    NativeObject nativeObject = NativeObject.this;
                    Object[] objArr = this.ids;
                    int i = this.index;
                    this.index = i + 1;
                    Object obj = objArr[i];
                    this.key = obj;
                    return nativeObject.get(obj);
                }

                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    }
                    NativeObject.this.remove(this.key);
                    this.key = null;
                }
            };
        }

        public int size() {
            return NativeObject.this.size();
        }
    }

    static void init(Scriptable scope, boolean sealed) {
        new NativeObject().exportAsJSClass(12, scope, sealed);
    }

    public String getClassName() {
        return "Object";
    }

    public String toString() {
        return ScriptRuntime.defaultObjectToString(this);
    }

    /* access modifiers changed from: protected */
    public void fillConstructorProperties(IdFunctionObject ctor) {
        addIdFunctionProperty(ctor, OBJECT_TAG, -1, "getPrototypeOf", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, -2, "keys", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, -3, "getOwnPropertyNames", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_getOwnPropertyDescriptor, "getOwnPropertyDescriptor", 2);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_defineProperty, "defineProperty", 3);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_isExtensible, "isExtensible", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_preventExtensions, "preventExtensions", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_defineProperties, "defineProperties", 2);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_create, "create", 2);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_isSealed, "isSealed", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_isFrozen, "isFrozen", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_seal, "seal", 1);
        addIdFunctionProperty(ctor, OBJECT_TAG, ConstructorId_freeze, "freeze", 1);
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
                arity = 0;
                s = "toString";
                break;
            case 3:
                arity = 0;
                s = "toLocaleString";
                break;
            case 4:
                arity = 0;
                s = "valueOf";
                break;
            case 5:
                arity = 1;
                s = "hasOwnProperty";
                break;
            case 6:
                arity = 1;
                s = "propertyIsEnumerable";
                break;
            case 7:
                arity = 1;
                s = "isPrototypeOf";
                break;
            case 8:
                arity = 0;
                s = "toSource";
                break;
            case 9:
                arity = 2;
                s = "__defineGetter__";
                break;
            case 10:
                arity = 2;
                s = "__defineSetter__";
                break;
            case 11:
                arity = 1;
                s = "__lookupGetter__";
                break;
            case 12:
                arity = 1;
                s = "__lookupSetter__";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(OBJECT_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(OBJECT_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        Object arg;
        ScriptableObject obj;
        Scriptable desc;
        Object[] ids;
        int i;
        String s;
        boolean result;
        int index;
        Scriptable v;
        ScriptableObject so;
        String name;
        switch (id) {
            case ConstructorId_freeze /*-13*/:
                if (args.length < 1) {
                    arg = Undefined.instance;
                } else {
                    arg = args[0];
                }
                obj = ScriptableObject.ensureScriptableObject(arg);
                for (Object name2 : obj.getAllIds()) {
                    desc = obj.getOwnPropertyDescriptor(cx, name2);
                    if (isDataDescriptor(desc) && Boolean.TRUE.equals(desc.get("writable"))) {
                        desc.put("writable", desc, (Object) Boolean.FALSE);
                    }
                    if (Boolean.TRUE.equals(desc.get("configurable"))) {
                        desc.put("configurable", desc, (Object) Boolean.FALSE);
                    }
                    obj.defineOwnProperty(cx, name2, desc, false);
                }
                obj.preventExtensions();
                return obj;
            case ConstructorId_seal /*-12*/:
                obj = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]);
                for (Object name22 : obj.getAllIds()) {
                    desc = obj.getOwnPropertyDescriptor(cx, name22);
                    if (Boolean.TRUE.equals(desc.get("configurable"))) {
                        desc.put("configurable", desc, (Object) Boolean.FALSE);
                        obj.defineOwnProperty(cx, name22, desc, false);
                    }
                }
                obj.preventExtensions();
                return obj;
            case ConstructorId_isFrozen /*-11*/:
                obj = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]);
                if (obj.isExtensible()) {
                    return Boolean.FALSE;
                }
                for (Object name222 : obj.getAllIds()) {
                    ScriptableObject desc2 = obj.getOwnPropertyDescriptor(cx, name222);
                    if (Boolean.TRUE.equals(desc2.get("configurable"))) {
                        return Boolean.FALSE;
                    }
                    if (isDataDescriptor(desc2) && Boolean.TRUE.equals(desc2.get("writable"))) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            case ConstructorId_isSealed /*-10*/:
                obj = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]);
                if (obj.isExtensible()) {
                    return Boolean.FALSE;
                }
                for (Object name2222 : obj.getAllIds()) {
                    if (Boolean.TRUE.equals(obj.getOwnPropertyDescriptor(cx, name2222).get("configurable"))) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            case ConstructorId_create /*-9*/:
                arg = args.length < 1 ? Undefined.instance : args[0];
                Scriptable obj2 = arg == null ? null : ScriptableObject.ensureScriptable(arg);
                ScriptableObject newObject = new NativeObject();
                newObject.setParentScope(getParentScope());
                newObject.setPrototype(obj2);
                if (args.length > 1 && args[1] != Undefined.instance) {
                    newObject.defineOwnProperties(cx, ScriptableObject.ensureScriptableObject(Context.toObject(args[1], getParentScope())));
                }
                return newObject;
            case ConstructorId_defineProperties /*-8*/:
                obj = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]);
                obj.defineOwnProperties(cx, ScriptableObject.ensureScriptableObject(Context.toObject(args.length < 2 ? Undefined.instance : args[1], getParentScope())));
                return obj;
            case ConstructorId_preventExtensions /*-7*/:
                Object obj3 = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]);
                obj3.preventExtensions();
                return obj3;
            case ConstructorId_isExtensible /*-6*/:
                return Boolean.valueOf(ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]).isExtensible());
            case ConstructorId_defineProperty /*-5*/:
                obj = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]);
                obj.defineOwnProperty(cx, args.length < 2 ? Undefined.instance : args[1], ScriptableObject.ensureScriptableObject(args.length < 3 ? Undefined.instance : args[2]));
                return obj;
            case ConstructorId_getOwnPropertyDescriptor /*-4*/:
                desc = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]).getOwnPropertyDescriptor(cx, ScriptRuntime.toString(args.length < 2 ? Undefined.instance : args[1]));
                if (desc == null) {
                    desc = Undefined.instance;
                }
                return desc;
            case -3:
                ids = ScriptableObject.ensureScriptableObject(args.length < 1 ? Undefined.instance : args[0]).getAllIds();
                for (i = 0; i < ids.length; i++) {
                    ids[i] = ScriptRuntime.toString(ids[i]);
                }
                return cx.newArray(scope, ids);
            case -2:
                ids = ScriptableObject.ensureScriptable(args.length < 1 ? Undefined.instance : args[0]).getIds();
                for (i = 0; i < ids.length; i++) {
                    ids[i] = ScriptRuntime.toString(ids[i]);
                }
                return cx.newArray(scope, ids);
            case -1:
                return ScriptableObject.ensureScriptable(args.length < 1 ? Undefined.instance : args[0]).getPrototype();
            case 1:
                if (thisObj != null) {
                    return f.construct(cx, scope, args);
                }
                if (args.length == 0 || args[0] == null || args[0] == Undefined.instance) {
                    return new NativeObject();
                }
                return ScriptRuntime.toObject(cx, scope, args[0]);
            case 2:
                if (!cx.hasFeature(4)) {
                    return ScriptRuntime.defaultObjectToString(thisObj);
                }
                s = ScriptRuntime.defaultObjectToSource(cx, scope, thisObj, args);
                int L = s.length();
                if (L != 0 && s.charAt(0) == '(' && s.charAt(L - 1) == ')') {
                    s = s.substring(1, L - 1);
                }
                return s;
            case 3:
                Callable toString = ScriptableObject.getProperty(thisObj, "toString");
                if (toString instanceof Callable) {
                    return toString.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
                }
                throw ScriptRuntime.notFunctionError(toString);
            case 4:
                return thisObj;
            case 5:
                s = ScriptRuntime.toStringIdOrIndex(cx, args.length < 1 ? Undefined.instance : args[0]);
                if (s == null) {
                    result = thisObj.has(ScriptRuntime.lastIndexResult(cx), thisObj);
                } else {
                    result = thisObj.has(s, thisObj);
                }
                return ScriptRuntime.wrapBoolean(result);
            case 6:
                s = ScriptRuntime.toStringIdOrIndex(cx, args.length < 1 ? Undefined.instance : args[0]);
                if (s == null) {
                    index = ScriptRuntime.lastIndexResult(cx);
                    result = thisObj.has(index, thisObj);
                    if (result && (thisObj instanceof ScriptableObject)) {
                        result = (((ScriptableObject) thisObj).getAttributes(index) & 2) == 0;
                    }
                } else {
                    result = thisObj.has(s, thisObj);
                    if (result && (thisObj instanceof ScriptableObject)) {
                        result = (((ScriptableObject) thisObj).getAttributes(s) & 2) == 0;
                    }
                }
                return ScriptRuntime.wrapBoolean(result);
            case 7:
                result = false;
                if (args.length != 0 && (args[0] instanceof Scriptable)) {
                    v = (Scriptable) args[0];
                    do {
                        v = v.getPrototype();
                        if (v == thisObj) {
                            result = true;
                        }
                    } while (v != null);
                }
                return ScriptRuntime.wrapBoolean(result);
            case 8:
                return ScriptRuntime.defaultObjectToSource(cx, scope, thisObj, args);
            case 9:
            case 10:
                if (args.length < 2 || !(args[1] instanceof Callable)) {
                    throw ScriptRuntime.notFunctionError(args.length >= 2 ? args[1] : Undefined.instance);
                } else if (thisObj instanceof ScriptableObject) {
                    so = (ScriptableObject) thisObj;
                    name = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
                    if (name != null) {
                        index = 0;
                    } else {
                        index = ScriptRuntime.lastIndexResult(cx);
                    }
                    so.setGetterOrSetter(name, index, args[1], id == 10);
                    if (so instanceof NativeArray) {
                        ((NativeArray) so).setDenseOnly(false);
                    }
                    return Undefined.instance;
                } else {
                    throw Context.reportRuntimeError2("msg.extend.scriptable", thisObj.getClass().getName(), String.valueOf(args[0]));
                }
            case 11:
            case 12:
                if (args.length < 1 || !(thisObj instanceof ScriptableObject)) {
                    return Undefined.instance;
                }
                Object gs;
                so = (ScriptableObject) thisObj;
                name = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
                if (name != null) {
                    index = 0;
                } else {
                    index = ScriptRuntime.lastIndexResult(cx);
                }
                boolean isSetter = id == 12;
                while (true) {
                    gs = so.getGetterOrSetter(name, index, isSetter);
                    if (gs == null) {
                        v = so.getPrototype();
                        if (v != null && (v instanceof ScriptableObject)) {
                            so = (ScriptableObject) v;
                        }
                    }
                }
                if (gs != null) {
                    return gs;
                }
                return Undefined.instance;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return has((String) key, this);
        }
        if (key instanceof Number) {
            return has(((Number) key).intValue(), (Scriptable) this);
        }
        return false;
    }

    public boolean containsValue(Object value) {
        for (Object obj : values()) {
            if (value == obj || (value != null && value.equals(obj))) {
                return true;
            }
        }
        return false;
    }

    public Object remove(Object key) {
        Object value = get(key);
        if (key instanceof String) {
            delete((String) key);
        } else if (key instanceof Number) {
            delete(((Number) key).intValue());
        }
        return value;
    }

    public Set<Object> keySet() {
        return new KeySet();
    }

    public Collection<Object> values() {
        return new ValueCollection();
    }

    public Set<Entry<Object, Object>> entrySet() {
        return new EntrySet();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int c;
        switch (s.length()) {
            case 7:
                X = "valueOf";
                id = 4;
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
                id = 8;
                break;
                break;
            case 11:
                X = "constructor";
                id = 1;
                break;
            case 13:
                X = "isPrototypeOf";
                id = 7;
                break;
            case 14:
                c = s.charAt(0);
                if (c != 104) {
                    if (c == 116) {
                        X = "toLocaleString";
                        id = 3;
                        break;
                    }
                }
                X = "hasOwnProperty";
                id = 5;
                break;
                break;
            case 16:
                c = s.charAt(2);
                if (c != 100) {
                    if (c == 108) {
                        c = s.charAt(8);
                        if (c != 71) {
                            if (c == 83) {
                                X = "__lookupSetter__";
                                id = 12;
                                break;
                            }
                        }
                        X = "__lookupGetter__";
                        id = 11;
                        break;
                    }
                }
                c = s.charAt(8);
                if (c != 71) {
                    if (c == 83) {
                        X = "__defineSetter__";
                        id = 10;
                        break;
                    }
                }
                X = "__defineGetter__";
                id = 9;
                break;
                break;
            case 20:
                X = "propertyIsEnumerable";
                id = 6;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
