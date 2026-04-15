package org.mozilla.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjArray implements Serializable {
    private static final int FIELDS_STORE_SIZE = 5;
    static final long serialVersionUID = 4174889037736658296L;
    private transient Object[] data;
    private transient Object f0;
    private transient Object f1;
    private transient Object f2;
    private transient Object f3;
    private transient Object f4;
    private boolean sealed;
    private int size;

    public final boolean isSealed() {
        return this.sealed;
    }

    public final void seal() {
        this.sealed = true;
    }

    public final boolean isEmpty() {
        return this.size == 0;
    }

    public final int size() {
        return this.size;
    }

    public final void setSize(int newSize) {
        if (newSize < 0) {
            throw new IllegalArgumentException();
        } else if (this.sealed) {
            throw onSeledMutation();
        } else {
            int N = this.size;
            if (newSize < N) {
                for (int i = newSize; i != N; i++) {
                    setImpl(i, null);
                }
            } else if (newSize > N && newSize > 5) {
                ensureCapacity(newSize);
            }
            this.size = newSize;
        }
    }

    public final Object get(int index) {
        if (index >= 0 && index < this.size) {
            return getImpl(index);
        }
        throw onInvalidIndex(index, this.size);
    }

    public final void set(int index, Object value) {
        if (index < 0 || index >= this.size) {
            throw onInvalidIndex(index, this.size);
        } else if (this.sealed) {
            throw onSeledMutation();
        } else {
            setImpl(index, value);
        }
    }

    private Object getImpl(int index) {
        switch (index) {
            case 0:
                return this.f0;
            case 1:
                return this.f1;
            case 2:
                return this.f2;
            case 3:
                return this.f3;
            case 4:
                return this.f4;
            default:
                return this.data[index - 5];
        }
    }

    private void setImpl(int index, Object value) {
        switch (index) {
            case 0:
                this.f0 = value;
                return;
            case 1:
                this.f1 = value;
                return;
            case 2:
                this.f2 = value;
                return;
            case 3:
                this.f3 = value;
                return;
            case 4:
                this.f4 = value;
                return;
            default:
                this.data[index - 5] = value;
                return;
        }
    }

    public int indexOf(Object obj) {
        int N = this.size;
        for (int i = 0; i != N; i++) {
            Object current = getImpl(i);
            if (current == obj) {
                return i;
            }
            if (current != null && current.equals(obj)) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        int i = this.size;
        while (i != 0) {
            i--;
            Object current = getImpl(i);
            if (current == obj || (current != null && current.equals(obj))) {
                return i;
            }
        }
        return -1;
    }

    public final Object peek() {
        int N = this.size;
        if (N != 0) {
            return getImpl(N - 1);
        }
        throw onEmptyStackTopRead();
    }

    public final Object pop() {
        if (this.sealed) {
            throw onSeledMutation();
        }
        Object top;
        int N = this.size - 1;
        switch (N) {
            case -1:
                throw onEmptyStackTopRead();
            case 0:
                top = this.f0;
                this.f0 = null;
                break;
            case 1:
                top = this.f1;
                this.f1 = null;
                break;
            case 2:
                top = this.f2;
                this.f2 = null;
                break;
            case 3:
                top = this.f3;
                this.f3 = null;
                break;
            case 4:
                top = this.f4;
                this.f4 = null;
                break;
            default:
                top = this.data[N - 5];
                this.data[N - 5] = null;
                break;
        }
        this.size = N;
        return top;
    }

    public final void push(Object value) {
        add(value);
    }

    public final void add(Object value) {
        if (this.sealed) {
            throw onSeledMutation();
        }
        int N = this.size;
        if (N >= 5) {
            ensureCapacity(N + 1);
        }
        this.size = N + 1;
        setImpl(N, value);
    }

    public final void add(int r8, java.lang.Object r9) {
        /*
        r7 = this;
        r0 = r7.size;
        if (r8 < 0) goto L_0x0006;
    L_0x0004:
        if (r8 <= r0) goto L_0x000d;
    L_0x0006:
        r2 = r0 + 1;
        r2 = onInvalidIndex(r8, r2);
        throw r2;
    L_0x000d:
        r2 = r7.sealed;
        if (r2 == 0) goto L_0x0016;
    L_0x0011:
        r2 = onSeledMutation();
        throw r2;
    L_0x0016:
        switch(r8) {
            case 0: goto L_0x003a;
            case 1: goto L_0x0044;
            case 2: goto L_0x004f;
            case 3: goto L_0x005a;
            case 4: goto L_0x0065;
            default: goto L_0x0019;
        };
    L_0x0019:
        r2 = r0 + 1;
        r7.ensureCapacity(r2);
        if (r8 == r0) goto L_0x002f;
    L_0x0020:
        r2 = r7.data;
        r3 = r8 + -5;
        r4 = r7.data;
        r5 = r8 + -5;
        r5 = r5 + 1;
        r6 = r0 - r8;
        java.lang.System.arraycopy(r2, r3, r4, r5, r6);
    L_0x002f:
        r2 = r7.data;
        r3 = r8 + -5;
        r2[r3] = r9;
    L_0x0035:
        r2 = r0 + 1;
        r7.size = r2;
        return;
    L_0x003a:
        if (r0 != 0) goto L_0x003f;
    L_0x003c:
        r7.f0 = r9;
        goto L_0x0035;
    L_0x003f:
        r1 = r7.f0;
        r7.f0 = r9;
        r9 = r1;
    L_0x0044:
        r2 = 1;
        if (r0 != r2) goto L_0x004a;
    L_0x0047:
        r7.f1 = r9;
        goto L_0x0035;
    L_0x004a:
        r1 = r7.f1;
        r7.f1 = r9;
        r9 = r1;
    L_0x004f:
        r2 = 2;
        if (r0 != r2) goto L_0x0055;
    L_0x0052:
        r7.f2 = r9;
        goto L_0x0035;
    L_0x0055:
        r1 = r7.f2;
        r7.f2 = r9;
        r9 = r1;
    L_0x005a:
        r2 = 3;
        if (r0 != r2) goto L_0x0060;
    L_0x005d:
        r7.f3 = r9;
        goto L_0x0035;
    L_0x0060:
        r1 = r7.f3;
        r7.f3 = r9;
        r9 = r1;
    L_0x0065:
        r2 = 4;
        if (r0 != r2) goto L_0x006b;
    L_0x0068:
        r7.f4 = r9;
        goto L_0x0035;
    L_0x006b:
        r1 = r7.f4;
        r7.f4 = r9;
        r9 = r1;
        r8 = 5;
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.ObjArray.add(int, java.lang.Object):void");
    }

    public final void remove(int r8) {
        /*
        r7 = this;
        r6 = 0;
        r0 = r7.size;
        if (r8 < 0) goto L_0x0007;
    L_0x0005:
        if (r8 < r0) goto L_0x000c;
    L_0x0007:
        r1 = onInvalidIndex(r8, r0);
        throw r1;
    L_0x000c:
        r1 = r7.sealed;
        if (r1 == 0) goto L_0x0015;
    L_0x0010:
        r1 = onSeledMutation();
        throw r1;
    L_0x0015:
        r0 = r0 + -1;
        switch(r8) {
            case 0: goto L_0x0034;
            case 1: goto L_0x003d;
            case 2: goto L_0x0047;
            case 3: goto L_0x0051;
            case 4: goto L_0x005b;
            default: goto L_0x001a;
        };
    L_0x001a:
        if (r8 == r0) goto L_0x002b;
    L_0x001c:
        r1 = r7.data;
        r2 = r8 + -5;
        r2 = r2 + 1;
        r3 = r7.data;
        r4 = r8 + -5;
        r5 = r0 - r8;
        java.lang.System.arraycopy(r1, r2, r3, r4, r5);
    L_0x002b:
        r1 = r7.data;
        r2 = r0 + -5;
        r1[r2] = r6;
    L_0x0031:
        r7.size = r0;
        return;
    L_0x0034:
        if (r0 != 0) goto L_0x0039;
    L_0x0036:
        r7.f0 = r6;
        goto L_0x0031;
    L_0x0039:
        r1 = r7.f1;
        r7.f0 = r1;
    L_0x003d:
        r1 = 1;
        if (r0 != r1) goto L_0x0043;
    L_0x0040:
        r7.f1 = r6;
        goto L_0x0031;
    L_0x0043:
        r1 = r7.f2;
        r7.f1 = r1;
    L_0x0047:
        r1 = 2;
        if (r0 != r1) goto L_0x004d;
    L_0x004a:
        r7.f2 = r6;
        goto L_0x0031;
    L_0x004d:
        r1 = r7.f3;
        r7.f2 = r1;
    L_0x0051:
        r1 = 3;
        if (r0 != r1) goto L_0x0057;
    L_0x0054:
        r7.f3 = r6;
        goto L_0x0031;
    L_0x0057:
        r1 = r7.f4;
        r7.f3 = r1;
    L_0x005b:
        r1 = 4;
        if (r0 != r1) goto L_0x0061;
    L_0x005e:
        r7.f4 = r6;
        goto L_0x0031;
    L_0x0061:
        r1 = r7.data;
        r2 = 0;
        r1 = r1[r2];
        r7.f4 = r1;
        r8 = 5;
        goto L_0x001a;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.ObjArray.remove(int):void");
    }

    public final void clear() {
        if (this.sealed) {
            throw onSeledMutation();
        }
        int N = this.size;
        for (int i = 0; i != N; i++) {
            setImpl(i, null);
        }
        this.size = 0;
    }

    public final Object[] toArray() {
        Object[] array = new Object[this.size];
        toArray(array, 0);
        return array;
    }

    public final void toArray(Object[] array) {
        toArray(array, 0);
    }

    /* JADX WARNING: Missing block: B:3:0x000f, code skipped:
            r6[r7 + 4] = r5.f4;
     */
    /* JADX WARNING: Missing block: B:4:0x0015, code skipped:
            r6[r7 + 3] = r5.f3;
     */
    /* JADX WARNING: Missing block: B:5:0x001b, code skipped:
            r6[r7 + 2] = r5.f2;
     */
    /* JADX WARNING: Missing block: B:6:0x0021, code skipped:
            r6[r7 + 1] = r5.f1;
     */
    /* JADX WARNING: Missing block: B:7:0x0027, code skipped:
            r6[r7 + 0] = r5.f0;
     */
    /* JADX WARNING: Missing block: B:8:?, code skipped:
            return;
     */
    public final void toArray(java.lang.Object[] r6, int r7) {
        /*
        r5 = this;
        r0 = r5.size;
        switch(r0) {
            case 0: goto L_0x002d;
            case 1: goto L_0x0027;
            case 2: goto L_0x0021;
            case 3: goto L_0x001b;
            case 4: goto L_0x0015;
            case 5: goto L_0x000f;
            default: goto L_0x0005;
        };
    L_0x0005:
        r1 = r5.data;
        r2 = 0;
        r3 = r7 + 5;
        r4 = r0 + -5;
        java.lang.System.arraycopy(r1, r2, r6, r3, r4);
    L_0x000f:
        r1 = r7 + 4;
        r2 = r5.f4;
        r6[r1] = r2;
    L_0x0015:
        r1 = r7 + 3;
        r2 = r5.f3;
        r6[r1] = r2;
    L_0x001b:
        r1 = r7 + 2;
        r2 = r5.f2;
        r6[r1] = r2;
    L_0x0021:
        r1 = r7 + 1;
        r2 = r5.f1;
        r6[r1] = r2;
    L_0x0027:
        r1 = r7 + 0;
        r2 = r5.f0;
        r6[r1] = r2;
    L_0x002d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.ObjArray.toArray(java.lang.Object[], int):void");
    }

    private void ensureCapacity(int minimalCapacity) {
        int required = minimalCapacity - 5;
        int alloc;
        if (required <= 0) {
            throw new IllegalArgumentException();
        } else if (this.data == null) {
            alloc = 10;
            if (10 < required) {
                alloc = required;
            }
            this.data = new Object[alloc];
        } else {
            alloc = this.data.length;
            if (alloc < required) {
                if (alloc <= 5) {
                    alloc = 10;
                } else {
                    alloc *= 2;
                }
                if (alloc < required) {
                    alloc = required;
                }
                Object[] tmp = new Object[alloc];
                if (this.size > 5) {
                    System.arraycopy(this.data, 0, tmp, 0, this.size - 5);
                }
                this.data = tmp;
            }
        }
    }

    private static RuntimeException onInvalidIndex(int index, int upperBound) {
        throw new IndexOutOfBoundsException(index + " ∉ [0, " + upperBound + ')');
    }

    private static RuntimeException onEmptyStackTopRead() {
        throw new RuntimeException("Empty stack");
    }

    private static RuntimeException onSeledMutation() {
        throw new IllegalStateException("Attempt to modify sealed array");
    }

    private void writeObject(ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();
        int N = this.size;
        for (int i = 0; i != N; i++) {
            os.writeObject(getImpl(i));
        }
    }

    private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        int N = this.size;
        if (N > 5) {
            this.data = new Object[(N - 5)];
        }
        for (int i = 0; i != N; i++) {
            setImpl(i, is.readObject());
        }
    }
}
