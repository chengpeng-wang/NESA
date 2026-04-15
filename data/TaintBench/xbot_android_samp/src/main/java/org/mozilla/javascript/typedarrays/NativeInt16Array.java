package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

public class NativeInt16Array extends NativeTypedArrayView<Short> {
    private static final int BYTES_PER_ELEMENT = 2;
    private static final String CLASS_NAME = "Int16Array";
    private static final long serialVersionUID = -8592870435287581398L;

    public NativeInt16Array(NativeArrayBuffer ab, int off, int len) {
        super(ab, off, len, len * 2);
    }

    public NativeInt16Array(int len) {
        this(new NativeArrayBuffer(len * 2), 0, len);
    }

    public String getClassName() {
        return CLASS_NAME;
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        new NativeInt16Array().exportAsJSClass(4, scope, sealed);
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
        return new NativeInt16Array(ab, off, len);
    }

    public int getBytesPerElement() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
        if (thisObj instanceof NativeInt16Array) {
            return (NativeInt16Array) thisObj;
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    /* access modifiers changed from: protected */
    public Object js_get(int index) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        return ByteIo.readInt16(this.arrayBuffer.buffer, (index * 2) + this.offset, false);
    }

    /* access modifiers changed from: protected */
    public Object js_set(int index, Object c) {
        if (checkIndex(index)) {
            return Undefined.instance;
        }
        ByteIo.writeInt16(this.arrayBuffer.buffer, (index * 2) + this.offset, Conversions.toInt16(c), false);
        return null;
    }

    public Short get(int i) {
        if (!checkIndex(i)) {
            return (Short) js_get(i);
        }
        throw new IndexOutOfBoundsException();
    }

    public Short set(int i, Short aByte) {
        if (!checkIndex(i)) {
            return (Short) js_set(i, aByte);
        }
        throw new IndexOutOfBoundsException();
    }
}
