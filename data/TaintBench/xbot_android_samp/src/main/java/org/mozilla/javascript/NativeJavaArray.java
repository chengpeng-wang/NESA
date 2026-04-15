package org.mozilla.javascript;

import java.lang.reflect.Array;

public class NativeJavaArray extends NativeJavaObject {
    static final long serialVersionUID = -924022554283675333L;
    Object array;
    Class<?> cls;
    int length;

    public String getClassName() {
        return "JavaArray";
    }

    public static NativeJavaArray wrap(Scriptable scope, Object array) {
        return new NativeJavaArray(scope, array);
    }

    public Object unwrap() {
        return this.array;
    }

    public NativeJavaArray(Scriptable scope, Object array) {
        super(scope, null, ScriptRuntime.ObjectClass);
        Class<?> cl = array.getClass();
        if (cl.isArray()) {
            this.array = array;
            this.length = Array.getLength(array);
            this.cls = cl.getComponentType();
            return;
        }
        throw new RuntimeException("Array expected");
    }

    public boolean has(String id, Scriptable start) {
        return id.equals("length") || super.has(id, start);
    }

    public boolean has(int index, Scriptable start) {
        return index >= 0 && index < this.length;
    }

    public Object get(String id, Scriptable start) {
        if (id.equals("length")) {
            return Integer.valueOf(this.length);
        }
        Object result = super.get(id, start);
        if (result != NOT_FOUND || ScriptableObject.hasProperty(getPrototype(), id)) {
            return result;
        }
        throw Context.reportRuntimeError2("msg.java.member.not.found", this.array.getClass().getName(), id);
    }

    public Object get(int index, Scriptable start) {
        if (index < 0 || index >= this.length) {
            return Undefined.instance;
        }
        Context cx = Context.getContext();
        return cx.getWrapFactory().wrap(cx, this, Array.get(this.array, index), this.cls);
    }

    public void put(String id, Scriptable start, Object value) {
        if (!id.equals("length")) {
            throw Context.reportRuntimeError1("msg.java.array.member.not.found", id);
        }
    }

    public void put(int index, Scriptable start, Object value) {
        if (index < 0 || index >= this.length) {
            throw Context.reportRuntimeError2("msg.java.array.index.out.of.bounds", String.valueOf(index), String.valueOf(this.length - 1));
        }
        Array.set(this.array, index, Context.jsToJava(value, this.cls));
    }

    public Object getDefaultValue(Class<?> hint) {
        if (hint == null || hint == ScriptRuntime.StringClass) {
            return this.array.toString();
        }
        if (hint == ScriptRuntime.BooleanClass) {
            return Boolean.TRUE;
        }
        if (hint == ScriptRuntime.NumberClass) {
            return ScriptRuntime.NaNobj;
        }
        return this;
    }

    public Object[] getIds() {
        Object[] result = new Object[this.length];
        int i = this.length;
        while (true) {
            i--;
            if (i < 0) {
                return result;
            }
            result[i] = Integer.valueOf(i);
        }
    }

    public boolean hasInstance(Scriptable value) {
        if (!(value instanceof Wrapper)) {
            return false;
        }
        return this.cls.isInstance(((Wrapper) value).unwrap());
    }

    public Scriptable getPrototype() {
        if (this.prototype == null) {
            this.prototype = ScriptableObject.getArrayPrototype(getParentScope());
        }
        return this.prototype;
    }
}
