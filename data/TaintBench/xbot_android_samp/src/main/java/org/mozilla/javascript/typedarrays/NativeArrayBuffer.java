package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

public class NativeArrayBuffer extends IdScriptableObject {
    public static final String CLASS_NAME = "ArrayBuffer";
    private static final int ConstructorId_isView = -3;
    private static final byte[] EMPTY_BUF = new byte[0];
    public static final NativeArrayBuffer EMPTY_BUFFER = new NativeArrayBuffer();
    private static final int Id_byteLength = 1;
    private static final int Id_constructor = 1;
    private static final int Id_isView = 3;
    private static final int Id_slice = 2;
    private static final int MAX_INSTANCE_ID = 1;
    private static final int MAX_PROTOTYPE_ID = 3;
    private static final long serialVersionUID = 3110411773054879549L;
    final byte[] buffer;

    public String getClassName() {
        return CLASS_NAME;
    }

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        new NativeArrayBuffer().exportAsJSClass(3, scope, sealed);
    }

    public NativeArrayBuffer() {
        this.buffer = EMPTY_BUF;
    }

    public NativeArrayBuffer(int len) {
        if (len < 0) {
            throw ScriptRuntime.constructError("RangeError", "Negative array length " + len);
        } else if (len == 0) {
            this.buffer = EMPTY_BUF;
        } else {
            this.buffer = new byte[len];
        }
    }

    public int getLength() {
        return this.buffer.length;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public NativeArrayBuffer slice(int s, int e) {
        int length = this.buffer.length;
        if (e < 0) {
            e += this.buffer.length;
        }
        int end = Math.max(0, Math.min(length, e));
        if (s < 0) {
            s += this.buffer.length;
        }
        int start = Math.min(end, Math.max(0, s));
        int len = end - start;
        NativeArrayBuffer newBuf = new NativeArrayBuffer(len);
        System.arraycopy(this.buffer, start, newBuf.buffer, 0, len);
        return newBuf;
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        boolean z = true;
        if (!f.hasTag(CLASS_NAME)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        switch (id) {
            case -3:
                if (!(isArg(args, 0) && (args[0] instanceof NativeArrayBufferView))) {
                    z = false;
                }
                return Boolean.valueOf(z);
            case 1:
                int length;
                if (isArg(args, 0)) {
                    length = ScriptRuntime.toInt32(args[0]);
                } else {
                    length = 0;
                }
                return new NativeArrayBuffer(length);
            case 2:
                int start;
                int end;
                NativeArrayBuffer self = realThis(thisObj, f);
                if (isArg(args, 0)) {
                    start = ScriptRuntime.toInt32(args[0]);
                } else {
                    start = 0;
                }
                if (isArg(args, 1)) {
                    end = ScriptRuntime.toInt32(args[1]);
                } else {
                    end = self.buffer.length;
                }
                return self.slice(start, end);
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    private static NativeArrayBuffer realThis(Scriptable thisObj, IdFunctionObject f) {
        if (thisObj instanceof NativeArrayBuffer) {
            return (NativeArrayBuffer) thisObj;
        }
        throw IdScriptableObject.incompatibleCallError(f);
    }

    private static boolean isArg(Object[] args, int i) {
        return args.length > i && !Undefined.instance.equals(args[i]);
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
                arity = 1;
                s = "slice";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(CLASS_NAME, id, s, arity);
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int s_length = s.length();
        if (s_length == 5) {
            X = "slice";
            id = 2;
        } else if (s_length == 6) {
            X = "isView";
            id = 3;
        } else if (s_length == 11) {
            X = "constructor";
            id = 1;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void fillConstructorProperties(IdFunctionObject ctor) {
        addIdFunctionProperty(ctor, CLASS_NAME, -3, "isView", 1);
    }

    /* access modifiers changed from: protected */
    public int getMaxInstanceId() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public String getInstanceIdName(int id) {
        if (id == 1) {
            return "byteLength";
        }
        return super.getInstanceIdName(id);
    }

    /* access modifiers changed from: protected */
    public Object getInstanceIdValue(int id) {
        if (id == 1) {
            return ScriptRuntime.wrapInt(this.buffer.length);
        }
        return super.getInstanceIdValue(id);
    }

    /* access modifiers changed from: protected */
    public int findInstanceIdInfo(String s) {
        if ("byteLength".equals(s)) {
            return IdScriptableObject.instanceIdInfo(5, 1);
        }
        return super.findInstanceIdInfo(s);
    }
}
