package org.mozilla.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class IdScriptableObject extends ScriptableObject implements IdFunctionCall {
    private transient PrototypeValues prototypeValues;

    private static final class PrototypeValues implements Serializable {
        private static final int NAME_SLOT = 1;
        private static final int SLOT_SPAN = 2;
        static final long serialVersionUID = 3038645279153854371L;
        private short[] attributeArray;
        private IdFunctionObject constructor;
        private short constructorAttrs;
        int constructorId;
        private int maxId;
        private IdScriptableObject obj;
        private Object[] valueArray;

        PrototypeValues(IdScriptableObject obj, int maxId) {
            if (obj == null) {
                throw new IllegalArgumentException();
            } else if (maxId < 1) {
                throw new IllegalArgumentException();
            } else {
                this.obj = obj;
                this.maxId = maxId;
            }
        }

        /* access modifiers changed from: final */
        public final int getMaxId() {
            return this.maxId;
        }

        /* access modifiers changed from: final */
        public final void initValue(int id, String name, Object value, int attributes) {
            if (1 > id || id > this.maxId) {
                throw new IllegalArgumentException();
            } else if (name == null) {
                throw new IllegalArgumentException();
            } else if (value == Scriptable.NOT_FOUND) {
                throw new IllegalArgumentException();
            } else {
                ScriptableObject.checkValidAttributes(attributes);
                if (this.obj.findPrototypeId(name) != id) {
                    throw new IllegalArgumentException(name);
                } else if (id != this.constructorId) {
                    initSlot(id, name, value, attributes);
                } else if (value instanceof IdFunctionObject) {
                    this.constructor = (IdFunctionObject) value;
                    this.constructorAttrs = (short) attributes;
                } else {
                    throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
                }
            }
        }

        private void initSlot(int id, String name, Object value, int attributes) {
            Object[] array = this.valueArray;
            if (array == null) {
                throw new IllegalStateException();
            }
            if (value == null) {
                value = UniqueTag.NULL_VALUE;
            }
            int index = (id - 1) * 2;
            synchronized (this) {
                if (array[index] == null) {
                    array[index] = value;
                    array[index + 1] = name;
                    this.attributeArray[id - 1] = (short) attributes;
                } else if (!name.equals(array[index + 1])) {
                    throw new IllegalStateException();
                }
            }
        }

        /* access modifiers changed from: final */
        public final IdFunctionObject createPrecachedConstructor() {
            if (this.constructorId != 0) {
                throw new IllegalStateException();
            }
            this.constructorId = this.obj.findPrototypeId("constructor");
            if (this.constructorId == 0) {
                throw new IllegalStateException("No id for constructor property");
            }
            this.obj.initPrototypeId(this.constructorId);
            if (this.constructor == null) {
                throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId() did not " + "initialize id=" + this.constructorId);
            }
            this.constructor.initFunction(this.obj.getClassName(), ScriptableObject.getTopLevelScope(this.obj));
            this.constructor.markAsConstructor(this.obj);
            return this.constructor;
        }

        /* access modifiers changed from: final */
        public final int findId(String name) {
            return this.obj.findPrototypeId(name);
        }

        /* access modifiers changed from: final */
        public final boolean has(int id) {
            Object[] array = this.valueArray;
            if (array == null) {
                return true;
            }
            Object value = array[(id - 1) * 2];
            if (value == null || value != Scriptable.NOT_FOUND) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: final */
        public final Object get(int id) {
            UniqueTag value = ensureId(id);
            if (value == UniqueTag.NULL_VALUE) {
                return null;
            }
            return value;
        }

        /* access modifiers changed from: final */
        public final void set(int id, Scriptable start, Object value) {
            if (value == Scriptable.NOT_FOUND) {
                throw new IllegalArgumentException();
            }
            ensureId(id);
            if ((this.attributeArray[id - 1] & 1) != 0) {
                return;
            }
            if (start == this.obj) {
                if (value == null) {
                    value = UniqueTag.NULL_VALUE;
                }
                int valueSlot = (id - 1) * 2;
                synchronized (this) {
                    this.valueArray[valueSlot] = value;
                }
                return;
            }
            start.put(this.valueArray[((id - 1) * 2) + 1], start, value);
        }

        /* access modifiers changed from: final */
        public final void delete(int id) {
            ensureId(id);
            if ((this.attributeArray[id - 1] & 4) == 0) {
                int valueSlot = (id - 1) * 2;
                synchronized (this) {
                    this.valueArray[valueSlot] = Scriptable.NOT_FOUND;
                    this.attributeArray[id - 1] = (short) 0;
                }
            }
        }

        /* access modifiers changed from: final */
        public final int getAttributes(int id) {
            ensureId(id);
            return this.attributeArray[id - 1];
        }

        /* access modifiers changed from: final */
        public final void setAttributes(int id, int attributes) {
            ScriptableObject.checkValidAttributes(attributes);
            ensureId(id);
            synchronized (this) {
                this.attributeArray[id - 1] = (short) attributes;
            }
        }

        /* access modifiers changed from: final */
        public final Object[] getNames(boolean getAll, Object[] extraEntries) {
            Object[] names = null;
            int count = 0;
            int id = 1;
            while (id <= this.maxId) {
                Object value = ensureId(id);
                if ((getAll || (this.attributeArray[id - 1] & 2) == 0) && value != Scriptable.NOT_FOUND) {
                    String name = this.valueArray[((id - 1) * 2) + 1];
                    if (names == null) {
                        names = new Object[this.maxId];
                    }
                    int count2 = count + 1;
                    names[count] = name;
                    count = count2;
                }
                id++;
            }
            if (count == 0) {
                return extraEntries;
            }
            Object[] tmp;
            if (extraEntries == null || extraEntries.length == 0) {
                if (count != names.length) {
                    tmp = new Object[count];
                    System.arraycopy(names, 0, tmp, 0, count);
                    names = tmp;
                }
                return names;
            }
            int extra = extraEntries.length;
            tmp = new Object[(extra + count)];
            System.arraycopy(extraEntries, 0, tmp, 0, extra);
            System.arraycopy(names, 0, tmp, extra, count);
            return tmp;
        }

        private Object ensureId(int id) {
            Object[] array = this.valueArray;
            if (array == null) {
                synchronized (this) {
                    array = this.valueArray;
                    if (array == null) {
                        array = new Object[(this.maxId * 2)];
                        this.valueArray = array;
                        this.attributeArray = new short[this.maxId];
                    }
                }
            }
            int valueSlot = (id - 1) * 2;
            Object value = array[valueSlot];
            if (value == null) {
                if (id == this.constructorId) {
                    initSlot(this.constructorId, "constructor", this.constructor, this.constructorAttrs);
                    this.constructor = null;
                } else {
                    this.obj.initPrototypeId(id);
                }
                value = array[valueSlot];
                if (value == null) {
                    throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId(int id) " + "did not initialize id=" + id);
                }
            }
            return value;
        }
    }

    public IdScriptableObject(Scriptable scope, Scriptable prototype) {
        super(scope, prototype);
    }

    /* access modifiers changed from: protected|final */
    public final boolean defaultHas(String name) {
        return super.has(name, (Scriptable) this);
    }

    /* access modifiers changed from: protected|final */
    public final Object defaultGet(String name) {
        return super.get(name, (Scriptable) this);
    }

    /* access modifiers changed from: protected|final */
    public final void defaultPut(String name, Object value) {
        super.put(name, (Scriptable) this, value);
    }

    public boolean has(String name, Scriptable start) {
        int info = findInstanceIdInfo(name);
        if (info == 0) {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    return this.prototypeValues.has(id);
                }
            }
            return super.has(name, start);
        } else if (((info >>> 16) & 4) != 0) {
            return true;
        } else {
            if (NOT_FOUND == getInstanceIdValue(info & 65535)) {
                return false;
            }
            return true;
        }
    }

    public Object get(String name, Scriptable start) {
        Object value = super.get(name, start);
        if (value != NOT_FOUND) {
            return value;
        }
        int info = findInstanceIdInfo(name);
        if (info != 0) {
            value = getInstanceIdValue(info & 65535);
            if (value != NOT_FOUND) {
                return value;
            }
        }
        if (this.prototypeValues != null) {
            int id = this.prototypeValues.findId(name);
            if (id != 0) {
                value = this.prototypeValues.get(id);
                if (value != NOT_FOUND) {
                    return value;
                }
            }
        }
        return NOT_FOUND;
    }

    public void put(String name, Scriptable start, Object value) {
        int info = findInstanceIdInfo(name);
        if (info == 0) {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    if (start == this && isSealed()) {
                        throw Context.reportRuntimeError1("msg.modify.sealed", name);
                    }
                    this.prototypeValues.set(id, start, value);
                    return;
                }
            }
            super.put(name, start, value);
        } else if (start == this && isSealed()) {
            throw Context.reportRuntimeError1("msg.modify.sealed", name);
        } else if (((info >>> 16) & 1) != 0) {
        } else {
            if (start == this) {
                setInstanceIdValue(info & 65535, value);
            } else {
                start.put(name, start, value);
            }
        }
    }

    public void delete(String name) {
        int info = findInstanceIdInfo(name);
        if (info == 0 || isSealed()) {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    if (!isSealed()) {
                        this.prototypeValues.delete(id);
                        return;
                    }
                    return;
                }
            }
            super.delete(name);
        } else if (((info >>> 16) & 4) == 0) {
            setInstanceIdValue(info & 65535, NOT_FOUND);
        }
    }

    public int getAttributes(String name) {
        int info = findInstanceIdInfo(name);
        if (info != 0) {
            return info >>> 16;
        }
        if (this.prototypeValues != null) {
            int id = this.prototypeValues.findId(name);
            if (id != 0) {
                return this.prototypeValues.getAttributes(id);
            }
        }
        return super.getAttributes(name);
    }

    public void setAttributes(String name, int attributes) {
        ScriptableObject.checkValidAttributes(attributes);
        int info = findInstanceIdInfo(name);
        int id;
        if (info != 0) {
            id = info & 65535;
            if (attributes != (info >>> 16)) {
                setInstanceIdAttributes(id, attributes);
                return;
            }
            return;
        }
        if (this.prototypeValues != null) {
            id = this.prototypeValues.findId(name);
            if (id != 0) {
                this.prototypeValues.setAttributes(id, attributes);
                return;
            }
        }
        super.setAttributes(name, attributes);
    }

    /* access modifiers changed from: 0000 */
    public Object[] getIds(boolean getAll) {
        Object[] result = super.getIds(getAll);
        if (this.prototypeValues != null) {
            result = this.prototypeValues.getNames(getAll, result);
        }
        int maxInstanceId = getMaxInstanceId();
        if (maxInstanceId == 0) {
            return result;
        }
        Object[] ids = null;
        int id = maxInstanceId;
        int count = 0;
        while (id != 0) {
            int count2;
            String name = getInstanceIdName(id);
            int info = findInstanceIdInfo(name);
            if (info != 0) {
                int attr = info >>> 16;
                if ((attr & 4) == 0 && NOT_FOUND == getInstanceIdValue(id)) {
                    count2 = count;
                    id--;
                    count = count2;
                } else if (getAll || (attr & 2) == 0) {
                    if (count == 0) {
                        ids = new Object[id];
                    }
                    count2 = count + 1;
                    ids[count] = name;
                    id--;
                    count = count2;
                }
            }
            count2 = count;
            id--;
            count = count2;
        }
        if (count == 0) {
            return result;
        }
        if (result.length == 0 && ids.length == count) {
            return ids;
        }
        Object[] tmp = new Object[(result.length + count)];
        System.arraycopy(result, 0, tmp, 0, result.length);
        System.arraycopy(ids, 0, tmp, result.length, count);
        return tmp;
    }

    /* access modifiers changed from: protected */
    public int getMaxInstanceId() {
        return 0;
    }

    protected static int instanceIdInfo(int attributes, int id) {
        return (attributes << 16) | id;
    }

    /* access modifiers changed from: protected */
    public int findInstanceIdInfo(String name) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getInstanceIdName(int id) {
        throw new IllegalArgumentException(String.valueOf(id));
    }

    /* access modifiers changed from: protected */
    public Object getInstanceIdValue(int id) {
        throw new IllegalStateException(String.valueOf(id));
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdValue(int id, Object value) {
        throw new IllegalStateException(String.valueOf(id));
    }

    /* access modifiers changed from: protected */
    public void setInstanceIdAttributes(int id, int attr) {
        throw ScriptRuntime.constructError("InternalError", "Changing attributes not supported for " + getClassName() + " " + getInstanceIdName(id) + " property");
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        throw f.unknown();
    }

    public final IdFunctionObject exportAsJSClass(int maxPrototypeId, Scriptable scope, boolean sealed) {
        if (!(scope == this || scope == null)) {
            setParentScope(scope);
            setPrototype(ScriptableObject.getObjectPrototype(scope));
        }
        activatePrototypeMap(maxPrototypeId);
        IdFunctionObject ctor = this.prototypeValues.createPrecachedConstructor();
        if (sealed) {
            sealObject();
        }
        fillConstructorProperties(ctor);
        if (sealed) {
            ctor.sealObject();
        }
        ctor.exportAsScopeProperty();
        return ctor;
    }

    public final boolean hasPrototypeMap() {
        return this.prototypeValues != null;
    }

    public final void activatePrototypeMap(int maxPrototypeId) {
        PrototypeValues values = new PrototypeValues(this, maxPrototypeId);
        synchronized (this) {
            if (this.prototypeValues != null) {
                throw new IllegalStateException();
            }
            this.prototypeValues = values;
        }
    }

    public final void initPrototypeMethod(Object tag, int id, String name, int arity) {
        this.prototypeValues.initValue(id, name, newIdFunction(tag, id, name, arity, ScriptableObject.getTopLevelScope(this)), 2);
    }

    public final void initPrototypeConstructor(IdFunctionObject f) {
        int id = this.prototypeValues.constructorId;
        if (id == 0) {
            throw new IllegalStateException();
        } else if (f.methodId() != id) {
            throw new IllegalArgumentException();
        } else {
            if (isSealed()) {
                f.sealObject();
            }
            this.prototypeValues.initValue(id, "constructor", f, 2);
        }
    }

    public final void initPrototypeValue(int id, String name, Object value, int attributes) {
        this.prototypeValues.initValue(id, name, value, attributes);
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        throw new IllegalStateException(String.valueOf(id));
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String name) {
        throw new IllegalStateException(name);
    }

    /* access modifiers changed from: protected */
    public void fillConstructorProperties(IdFunctionObject ctor) {
    }

    /* access modifiers changed from: protected */
    public void addIdFunctionProperty(Scriptable obj, Object tag, int id, String name, int arity) {
        newIdFunction(tag, id, name, arity, ScriptableObject.getTopLevelScope(obj)).addAsProperty(obj);
    }

    protected static EcmaError incompatibleCallError(IdFunctionObject f) {
        throw ScriptRuntime.typeError1("msg.incompat.call", f.getFunctionName());
    }

    private IdFunctionObject newIdFunction(Object tag, int id, String name, int arity, Scriptable scope) {
        IdFunctionObject f = new IdFunctionObject(this, tag, id, name, arity, scope);
        if (isSealed()) {
            f.sealObject();
        }
        return f;
    }

    public void defineOwnProperty(Context cx, Object key, ScriptableObject desc) {
        if (key instanceof String) {
            int id;
            int attr;
            Object value;
            String name = (String) key;
            int info = findInstanceIdInfo(name);
            if (info != 0) {
                id = info & 65535;
                if (isAccessorDescriptor(desc)) {
                    delete(id);
                } else {
                    checkPropertyDefinition(desc);
                    checkPropertyChange(name, getOwnPropertyDescriptor(cx, key), desc);
                    attr = info >>> 16;
                    value = ScriptableObject.getProperty((Scriptable) desc, "value");
                    if (!(value == NOT_FOUND || (attr & 1) != 0 || sameValue(value, getInstanceIdValue(id)))) {
                        setInstanceIdValue(id, value);
                    }
                    setAttributes(name, applyDescriptorToAttributeBitset(attr, desc));
                    return;
                }
            }
            if (this.prototypeValues != null) {
                id = this.prototypeValues.findId(name);
                if (id != 0) {
                    if (isAccessorDescriptor(desc)) {
                        this.prototypeValues.delete(id);
                    } else {
                        checkPropertyDefinition(desc);
                        checkPropertyChange(name, getOwnPropertyDescriptor(cx, key), desc);
                        attr = this.prototypeValues.getAttributes(id);
                        value = ScriptableObject.getProperty((Scriptable) desc, "value");
                        if (!(value == NOT_FOUND || (attr & 1) != 0 || sameValue(value, this.prototypeValues.get(id)))) {
                            this.prototypeValues.set(id, this, value);
                        }
                        this.prototypeValues.setAttributes(id, applyDescriptorToAttributeBitset(attr, desc));
                        return;
                    }
                }
            }
        }
        super.defineOwnProperty(cx, key, desc);
    }

    /* access modifiers changed from: protected */
    public ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
        ScriptableObject desc = super.getOwnPropertyDescriptor(cx, id);
        if (desc == null && (id instanceof String)) {
            return getBuiltInDescriptor((String) id);
        }
        return desc;
    }

    private ScriptableObject getBuiltInDescriptor(String name) {
        Scriptable scope = getParentScope();
        if (scope == null) {
            scope = this;
        }
        int info = findInstanceIdInfo(name);
        if (info != 0) {
            return ScriptableObject.buildDataDescriptor(scope, getInstanceIdValue(info & 65535), info >>> 16);
        }
        if (this.prototypeValues != null) {
            int id = this.prototypeValues.findId(name);
            if (id != 0) {
                return ScriptableObject.buildDataDescriptor(scope, this.prototypeValues.get(id), this.prototypeValues.getAttributes(id));
            }
        }
        return null;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int maxPrototypeId = stream.readInt();
        if (maxPrototypeId != 0) {
            activatePrototypeMap(maxPrototypeId);
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        int maxPrototypeId = 0;
        if (this.prototypeValues != null) {
            maxPrototypeId = this.prototypeValues.getMaxId();
        }
        stream.writeInt(maxPrototypeId);
    }
}
