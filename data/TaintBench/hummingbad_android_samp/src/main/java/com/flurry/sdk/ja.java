package com.flurry.sdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.xbill.DNS.Type;

public class ja {
    private static final String a = ja.class.getSimpleName();
    private static final byte[] b = new byte[]{(byte) 113, (byte) -92, (byte) -8, (byte) 125, (byte) 121, (byte) 107, (byte) -65, (byte) -61, (byte) -74, (byte) -114, (byte) -32, (byte) 0, (byte) -57, (byte) -87, (byte) -35, (byte) -56, (byte) -6, (byte) -52, (byte) 51, (byte) 126, (byte) -104, (byte) 49, (byte) 79, (byte) -52, (byte) 118, (byte) -84, (byte) 99, (byte) -52, (byte) -14, (byte) -126, (byte) -27, (byte) -64};
    private final String c;
    private final lc d;

    public ja(String str, lc lcVar) {
        this.c = str;
        this.d = lcVar;
    }

    public static int a(byte[] bArr) {
        if (bArr == null) {
            return 0;
        }
        ih ihVar = new ih();
        ihVar.update(bArr);
        return ihVar.a();
    }

    private static void c(byte[] bArr) {
        if (bArr != null) {
            int length = bArr.length;
            int length2 = b.length;
            for (int i = 0; i < length; i++) {
                bArr[i] = (byte) ((bArr[i] ^ b[i % length2]) ^ ((i * 31) % Type.IXFR));
            }
        }
    }

    private static void d(byte[] bArr) {
        c(bArr);
    }

    public final byte[] a(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.d.a(byteArrayOutputStream, obj);
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        iw.a(3, a, "Encoding " + this.c + ": " + new String(toByteArray));
        ky kyVar = new ky(new kw());
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        kyVar.a(byteArrayOutputStream2, toByteArray);
        toByteArray = byteArrayOutputStream2.toByteArray();
        c(toByteArray);
        return toByteArray;
    }

    public final Object b(byte[] bArr) {
        if (bArr == null) {
            throw new IOException("Decoding: " + this.c + ": Nothing to decode");
        }
        d(bArr);
        byte[] bArr2 = (byte[]) new ky(new kw()).a(new ByteArrayInputStream(bArr));
        iw.a(3, a, "Decoding: " + this.c + ": " + new String(bArr2));
        return this.d.a(new ByteArrayInputStream(bArr2));
    }
}
