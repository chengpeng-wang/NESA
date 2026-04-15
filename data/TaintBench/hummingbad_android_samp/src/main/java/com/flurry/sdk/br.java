package com.flurry.sdk;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

final class br {
    final String a;
    final long[] b;
    boolean c;
    bp d;
    long e;
    final /* synthetic */ bm f;

    private br(bm bmVar, String str) {
        this.f = bmVar;
        this.a = str;
        this.b = new long[bmVar.h];
    }

    /* synthetic */ br(bm bmVar, String str, byte b) {
        this(bmVar, str);
    }

    private static IOException b(String[] strArr) {
        throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
    }

    public final File a(int i) {
        return new File(this.f.b, this.a + "." + i);
    }

    public final String a() {
        StringBuilder stringBuilder = new StringBuilder();
        for (long append : this.b) {
            stringBuilder.append(' ').append(append);
        }
        return stringBuilder.toString();
    }

    /* access modifiers changed from: final */
    public final void a(String[] strArr) {
        if (strArr.length != this.f.h) {
            throw b(strArr);
        }
        int i = 0;
        while (i < strArr.length) {
            try {
                this.b[i] = Long.parseLong(strArr[i]);
                i++;
            } catch (NumberFormatException e) {
                throw b(strArr);
            }
        }
    }

    public final File b(int i) {
        return new File(this.f.b, this.a + "." + i + ".tmp");
    }
}
