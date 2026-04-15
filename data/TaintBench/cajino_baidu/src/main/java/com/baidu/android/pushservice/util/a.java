package com.baidu.android.pushservice.util;

import android.support.v4.view.MotionEventCompat;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.math.BigInteger;

public class a {
    byte[] a = new byte[8];
    private DataInputStream b;

    public a(InputStream inputStream) {
        this.b = new DataInputStream(inputStream);
    }

    private int a(int i) {
        int i2 = 0;
        while (i2 < i) {
            int read = this.b.read(this.a, i2, i - i2);
            if (read == -1) {
                return read;
            }
            i2 += read;
        }
        return i2;
    }

    public final int a() {
        if (a(4) >= 0) {
            return ((((this.a[3] & MotionEventCompat.ACTION_MASK) << 24) | ((this.a[2] & MotionEventCompat.ACTION_MASK) << 16)) | ((this.a[1] & MotionEventCompat.ACTION_MASK) << 8)) | (this.a[0] & MotionEventCompat.ACTION_MASK);
        }
        throw new EOFException();
    }

    public final void a(byte[] bArr) {
        this.b.readFully(bArr, 0, bArr.length);
    }

    public final short b() {
        if (a(2) >= 0) {
            return (short) (((this.a[1] & MotionEventCompat.ACTION_MASK) << 8) | (this.a[0] & MotionEventCompat.ACTION_MASK));
        }
        throw new EOFException();
    }

    public final b c() {
        if (a(8) < 0) {
            throw new EOFException();
        }
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = this.a[7 - i];
        }
        String bigInteger = new BigInteger(1, bArr).toString();
        long j = ((((long) (((((this.a[7] & MotionEventCompat.ACTION_MASK) << 24) | ((this.a[6] & MotionEventCompat.ACTION_MASK) << 16)) | ((this.a[5] & MotionEventCompat.ACTION_MASK) << 8)) | (this.a[4] & MotionEventCompat.ACTION_MASK))) & 4294967295L) << 32) | (((long) ((this.a[0] & MotionEventCompat.ACTION_MASK) | ((((this.a[3] & MotionEventCompat.ACTION_MASK) << 24) | ((this.a[2] & MotionEventCompat.ACTION_MASK) << 16)) | ((this.a[1] & MotionEventCompat.ACTION_MASK) << 8)))) & 4294967295L);
        b bVar = new b();
        bVar.a = bigInteger;
        bVar.b = j;
        return bVar;
    }
}
