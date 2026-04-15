package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

public class NativeUint8Array extends NativeTypedArrayView<Integer> {
    private static final String CLASS_NAME = "Uint8Array";
    private static final long serialVersionUID = -3349419704390398895L;

    public NativeUint8Array(NativeArrayBuffer ab, int off, int len) {
        super(ab, off, len, len);
    }

    public NativeUint8Array(int len) {
        this(new NativeArrayBuffer(len), 0, len);
    }

    public String getClassName() {
        return CLASS_NAME;
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        new NativeUint8Array().exportAsJSClass(4, scope, sealed);
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
        return new NativeUint8Array(ab, off, len);
    }

    public int getBytesPerElement() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
        if (thisObj instanceof NativeUint8Array) {
            return (NativeUint8Array) thisObj;
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    /* access modifiers changed from: protected */
    public Object js_get(int index) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        return ByteIo.readUint8(this.arrayBuffer.buffer, this.offset + index);
    }

    /* access modifiers changed from: protected */
    public Object js_set(int index, Object c) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        ByteIo.writeUint8(this.arrayBuffer.buffer, this.offset + index, Conversions.toUint8(c));
        return null;
    }

    public Integer get(int i) {
        if (!checkIndex(i)) {
            return (Integer) js_get(i);
        }
        throw new IndexOutOfBoundsException();
    }

    public Integer set(int i, Integer aByte) {
        if (!checkIndex(i)) {
            return (Integer) js_set(i, aByte);
        }
        throw new IndexOutOfBoundsException();
    }
}
