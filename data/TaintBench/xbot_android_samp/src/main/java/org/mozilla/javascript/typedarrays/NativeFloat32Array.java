package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

public class NativeFloat32Array extends NativeTypedArrayView<Float> {
    private static final int BYTES_PER_ELEMENT = 4;
    private static final String CLASS_NAME = "Float32Array";
    private static final long serialVersionUID = -8963461831950499340L;

    public NativeFloat32Array(NativeArrayBuffer ab, int off, int len) {
        super(ab, off, len, len * 4);
    }

    public NativeFloat32Array(int len) {
        this(new NativeArrayBuffer(len * 4), 0, len);
    }

    public String getClassName() {
        return CLASS_NAME;
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        new NativeFloat32Array().exportAsJSClass(4, scope, sealed);
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
        return new NativeFloat32Array(ab, off, len);
    }

    public int getBytesPerElement() {
        return 4;
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
        if (thisObj instanceof NativeFloat32Array) {
            return (NativeFloat32Array) thisObj;
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    /* access modifiers changed from: protected */
    public Object js_get(int index) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        return ByteIo.readFloat32(this.arrayBuffer.buffer, (index * 4) + this.offset, false);
    }

    /* access modifiers changed from: protected */
    public Object js_set(int index, Object c) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        ByteIo.writeFloat32(this.arrayBuffer.buffer, (index * 4) + this.offset, ScriptRuntime.toNumber(c), false);
        return null;
    }

    public Float get(int i) {
        if (!checkIndex(i)) {
            return (Float) js_get(i);
        }
        throw new IndexOutOfBoundsException();
    }

    public Float set(int i, Float aByte) {
        if (!checkIndex(i)) {
            return (Float) js_set(i, aByte);
        }
        throw new IndexOutOfBoundsException();
    }
}
