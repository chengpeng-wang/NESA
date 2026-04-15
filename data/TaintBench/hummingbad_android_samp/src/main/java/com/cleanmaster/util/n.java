package com.cleanmaster.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/* compiled from: MemInfoReader */
public class n {
    byte[] a = new byte[1024];
    private long b = 1073741824;
    private long c = 67108864;
    private long d = 67108864;

    private boolean a(byte[] bArr, int i, String str) {
        int length = str.length();
        if (i + length >= bArr.length) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (bArr[i + i2] != str.charAt(i2)) {
                return false;
            }
        }
        return true;
    }

    private long a(byte[] bArr, int i) {
        while (i < bArr.length && bArr[i] != (byte) 10) {
            if (bArr[i] < (byte) 48 || bArr[i] > (byte) 57) {
                i++;
            } else {
                int i2 = i + 1;
                while (i2 < bArr.length && bArr[i2] >= (byte) 48 && bArr[i2] <= (byte) 57) {
                    i2++;
                }
                return ((long) Integer.parseInt(new String(bArr, 0, i, i2 - i))) * 1024;
            }
        }
        return 0;
    }

    public void a() {
        int i = 0;
        try {
            this.b = 0;
            this.c = 0;
            this.d = 0;
            FileInputStream fileInputStream = new FileInputStream("/proc/meminfo");
            int read = fileInputStream.read(this.a);
            fileInputStream.close();
            int length = this.a.length;
            int i2 = 0;
            while (i < read && i2 < 3) {
                if (a(this.a, i, "MemTotal")) {
                    i += 8;
                    this.b = a(this.a, i);
                    i2++;
                } else if (a(this.a, i, "MemFree")) {
                    i += 7;
                    this.c = a(this.a, i);
                    i2++;
                } else if (a(this.a, i, "Cached")) {
                    i += 6;
                    this.d = a(this.a, i);
                    i2++;
                }
                while (i < length && this.a[i] != (byte) 10) {
                    i++;
                }
                i++;
            }
        } catch (FileNotFoundException | IOException e) {
        }
    }

    public long b() {
        return this.b;
    }

    public long c() {
        return this.c;
    }

    public long d() {
        return this.d;
    }
}
