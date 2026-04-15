package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

public class NativeUint32Array extends NativeTypedArrayView<Long> {
    private static final int BYTES_PER_ELEMENT = 4;
    private static final String CLASS_NAME = "Uint32Array";
    private static final long serialVersionUID = -7987831421954144244L;

    public NativeUint32Array(NativeArrayBuffer ab, int off, int len) {
        super(ab, off, len, len * 4);
    }

    public NativeUint32Array(int len) {
        this(new NativeArrayBuffer(len * 4), 0, len);
    }

    public String getClassName() {
        return CLASS_NAME;
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        new NativeUint32Array().exportAsJSClass(4, scope, sealed);
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
        return new NativeUint32Array(ab, off, len);
    }

    public int getBytesPerElement() {
        return 4;
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
        if (thisObj instanceof NativeUint32Array) {
            return (NativeUint32Array) thisObj;
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    /* access modifiers changed from: protected */
    public Object js_get(int index) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        return ByteIo.readUint32(this.arrayBuffer.buffer, (index * 4) + this.offset, false);
    }

    /* access modifiers changed from: protected */
    public Object js_set(int index, Object c) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        ByteIo.writeUint32(this.arrayBuffer.buffer, (index * 4) + this.offset, Conversions.toUint32(c), false);
        return null;
    }

    public Long get(int i) {
        if (!checkIndex(i)) {
            return (Long) js_get(i);
        }
        throw new IndexOutOfBoundsException();
    }

    public Long set(int i, Long aByte) {
        if (!checkIndex(i)) {
            return (Long) js_set(i, aByte);
        }
        throw new IndexOutOfBoundsException();
    }
}
