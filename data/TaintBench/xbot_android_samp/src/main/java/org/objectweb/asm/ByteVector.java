package org.objectweb.asm;

public class ByteVector {
    byte[] a;
    int b;

    public ByteVector() {
        this.a = new byte[64];
    }

    public ByteVector(int i) {
        this.a = new byte[i];
    }

    private void a(int i) {
        int length = this.a.length * 2;
        int i2 = this.b + i;
        if (length <= i2) {
            length = i2;
        }
        byte[] bArr = new byte[length];
        System.arraycopy(this.a, 0, bArr, 0, this.b);
        this.a = bArr;
    }

    /* access modifiers changed from: 0000 */
    public ByteVector a(int i, int i2) {
        int i3 = this.b;
        if (i3 + 2 > this.a.length) {
            a(2);
        }
        byte[] bArr = this.a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        i3 = i4 + 1;
        bArr[i4] = (byte) i2;
        this.b = i3;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public ByteVector b(int i, int i2) {
        int i3 = this.b;
        if (i3 + 3 > this.a.length) {
            a(3);
        }
        byte[] bArr = this.a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        i3 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        i4 = i3 + 1;
        bArr[i3] = (byte) i2;
        this.b = i4;
        return this;
    }

    public ByteVector putByte(int i) {
        int i2 = this.b;
        if (i2 + 1 > this.a.length) {
            a(1);
        }
        int i3 = i2 + 1;
        this.a[i2] = (byte) i;
        this.b = i3;
        return this;
    }

    public ByteVector putByteArray(byte[] bArr, int i, int i2) {
        if (this.b + i2 > this.a.length) {
            a(i2);
        }
        if (bArr != null) {
            System.arraycopy(bArr, i, this.a, this.b, i2);
        }
        this.b += i2;
        return this;
    }

    public ByteVector putInt(int i) {
        int i2 = this.b;
        if (i2 + 4 > this.a.length) {
            a(4);
        }
        byte[] bArr = this.a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) i;
        this.b = i2;
        return this;
    }

    public ByteVector putLong(long j) {
        int i = this.b;
        if (i + 8 > this.a.length) {
            a(8);
        }
        byte[] bArr = this.a;
        int i2 = (int) (j >>> 32);
        int i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        i = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 8);
        i = i3 + 1;
        bArr[i3] = (byte) i2;
        i2 = (int) j;
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        i = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 8);
        i = i3 + 1;
        bArr[i3] = (byte) i2;
        this.b = i;
        return this;
    }

    public ByteVector putShort(int i) {
        int i2 = this.b;
        if (i2 + 2 > this.a.length) {
            a(2);
        }
        byte[] bArr = this.a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) i;
        this.b = i2;
        return this;
    }

    public ByteVector putUTF8(String str) {
        int length = str.length();
        int i = this.b;
        if ((i + 2) + length > this.a.length) {
            a(length + 2);
        }
        byte[] bArr = this.a;
        int i2 = i + 1;
        bArr[i] = (byte) (length >>> 8);
        int i3 = i2 + 1;
        bArr[i2] = (byte) length;
        i2 = 0;
        while (i2 < length) {
            char charAt = str.charAt(i2);
            if (charAt < 1 || charAt > 127) {
                byte[] bArr2;
                i = i2;
                for (int i4 = i2; i4 < length; i4++) {
                    char charAt2 = str.charAt(i4);
                    i = (charAt2 < 1 || charAt2 > 127) ? charAt2 > 2047 ? i + 3 : i + 2 : i + 1;
                }
                bArr[this.b] = (byte) (i >>> 8);
                bArr[this.b + 1] = (byte) i;
                if ((this.b + 2) + i > bArr.length) {
                    this.b = i3;
                    a(i + 2);
                    bArr2 = this.a;
                } else {
                    bArr2 = bArr;
                }
                while (i2 < length) {
                    int i5;
                    charAt = str.charAt(i2);
                    if (charAt >= 1 && charAt <= 127) {
                        i5 = i3 + 1;
                        bArr2[i3] = (byte) charAt;
                    } else if (charAt > 2047) {
                        i5 = i3 + 1;
                        bArr2[i3] = (byte) (((charAt >> 12) & 15) | 224);
                        i3 = i5 + 1;
                        bArr2[i5] = (byte) (((charAt >> 6) & 63) | 128);
                        i5 = i3 + 1;
                        bArr2[i3] = (byte) ((charAt & 63) | 128);
                    } else {
                        int i6 = i3 + 1;
                        bArr2[i3] = (byte) (((charAt >> 6) & 31) | 192);
                        i5 = i6 + 1;
                        bArr2[i6] = (byte) ((charAt & 63) | 128);
                    }
                    i2++;
                    i3 = i5;
                }
                i = i3;
                this.b = i;
                return this;
            }
            i = i3 + 1;
            bArr[i3] = (byte) charAt;
            i2++;
            i3 = i;
        }
        i = i3;
        this.b = i;
        return this;
    }
}
