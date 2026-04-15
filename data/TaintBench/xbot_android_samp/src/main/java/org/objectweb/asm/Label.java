package org.objectweb.asm;

import org.mozilla.classfile.ByteCode;

public class Label {
    int a;
    int b;
    int c;
    private int d;
    private int[] e;
    int f;
    int g;
    Frame h;
    Label i;
    public Object info;
    Edge j;
    Label k;

    private void a(int i, int i2) {
        int[] iArr;
        if (this.e == null) {
            this.e = new int[6];
        }
        if (this.d >= this.e.length) {
            iArr = new int[(this.e.length + 6)];
            System.arraycopy(this.e, 0, iArr, 0, this.e.length);
            this.e = iArr;
        }
        iArr = this.e;
        int i3 = this.d;
        this.d = i3 + 1;
        iArr[i3] = i;
        iArr = this.e;
        i3 = this.d;
        this.d = i3 + 1;
        iArr[i3] = i2;
    }

    /* access modifiers changed from: 0000 */
    public Label a() {
        return this.h == null ? this : this.h.b;
    }

    /* access modifiers changed from: 0000 */
    public void a(long j, int i) {
        if ((this.a & Opcodes.ACC_ABSTRACT) == 0) {
            this.a |= Opcodes.ACC_ABSTRACT;
            this.e = new int[(((i - 1) / 32) + 1)];
        }
        int[] iArr = this.e;
        int i2 = (int) (j >>> 32);
        iArr[i2] = iArr[i2] | ((int) j);
    }

    /* access modifiers changed from: 0000 */
    public void a(MethodWriter methodWriter, ByteVector byteVector, int i, boolean z) {
        if ((this.a & 2) == 0) {
            if (z) {
                a(-1 - i, byteVector.b);
                byteVector.putInt(-1);
                return;
            }
            a(i, byteVector.b);
            byteVector.putShort(-1);
        } else if (z) {
            byteVector.putInt(this.c - i);
        } else {
            byteVector.putShort(this.c - i);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean a(long j) {
        return ((this.a & Opcodes.ACC_ABSTRACT) == 0 || (this.e[(int) (j >>> 32)] & ((int) j)) == 0) ? false : true;
    }

    /* access modifiers changed from: 0000 */
    public boolean a(Label label) {
        if ((this.a & Opcodes.ACC_ABSTRACT) == 0 || (label.a & Opcodes.ACC_ABSTRACT) == 0) {
            return false;
        }
        for (int i = 0; i < this.e.length; i++) {
            if ((this.e[i] & label.e[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean a(MethodWriter methodWriter, int i, byte[] bArr) {
        int i2 = 0;
        this.a |= 2;
        this.c = i;
        boolean z = false;
        while (i2 < this.d) {
            int i3 = i2 + 1;
            int i4 = this.e[i2];
            i2 = i3 + 1;
            i3 = this.e[i3];
            int i5;
            if (i4 >= 0) {
                i4 = i - i4;
                if (i4 < -32768 || i4 > 32767) {
                    int i6 = bArr[i3 - 1] & ByteCode.IMPDEP2;
                    if (i6 <= 168) {
                        bArr[i3 - 1] = (byte) (i6 + 49);
                    } else {
                        bArr[i3 - 1] = (byte) (i6 + 20);
                    }
                    z = true;
                }
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            } else {
                i4 = (i4 + i) + 1;
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 24);
                i3 = i5 + 1;
                bArr[i5] = (byte) (i4 >>> 16);
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            }
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    public void b(Label label, long j, int i) {
        while (this != null) {
            Label label2 = this.k;
            this.k = null;
            if (label != null) {
                if ((this.a & Opcodes.ACC_STRICT) != 0) {
                    this = label2;
                } else {
                    this.a |= Opcodes.ACC_STRICT;
                    if (!((this.a & 256) == 0 || this.a(label))) {
                        Edge edge = new Edge();
                        edge.a = this.f;
                        edge.b = label.j.b;
                        edge.c = this.j;
                        this.j = edge;
                    }
                }
            } else if (this.a(j)) {
                this = label2;
            } else {
                this.a(j, i);
            }
            Label label3 = label2;
            Edge edge2 = this.j;
            while (edge2 != null) {
                if (((this.a & 128) == 0 || edge2 != this.j.c) && edge2.b.k == null) {
                    edge2.b.k = label3;
                    label3 = edge2.b;
                }
                edge2 = edge2.c;
            }
            this = label3;
        }
    }

    public int getOffset() {
        if ((this.a & 2) != 0) {
            return this.c;
        }
        throw new IllegalStateException("Label offset position has not been resolved yet");
    }

    public String toString() {
        return new StringBuffer().append("L").append(System.identityHashCode(this)).toString();
    }
}
