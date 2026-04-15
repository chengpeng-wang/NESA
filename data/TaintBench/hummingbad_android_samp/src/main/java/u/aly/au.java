package u.aly;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* compiled from: DeflaterHelper */
public class au {
    public static int a;

    public static byte[] a(String str, String str2) throws IOException {
        if (av.d(str)) {
            return null;
        }
        return a(str.getBytes(str2));
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
    public static byte[] a(byte[] r6) throws java.io.IOException {
        /*
        r0 = 0;
        r4 = 0;
        if (r6 == 0) goto L_0x0007;
    L_0x0004:
        r1 = r6.length;
        if (r1 > 0) goto L_0x0008;
    L_0x0007:
        return r0;
    L_0x0008:
        r2 = new java.util.zip.Deflater;
        r2.<init>();
        r2.setInput(r6);
        r2.finish();
        r1 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r3 = new byte[r1];
        a = r4;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ all -> 0x0046 }
        r1.<init>();	 Catch:{ all -> 0x0046 }
    L_0x001e:
        r0 = r2.finished();	 Catch:{ all -> 0x003f }
        if (r0 == 0) goto L_0x0031;
    L_0x0024:
        r2.end();	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x002c;
    L_0x0029:
        r1.close();
    L_0x002c:
        r0 = r1.toByteArray();
        goto L_0x0007;
    L_0x0031:
        r0 = r2.deflate(r3);	 Catch:{ all -> 0x003f }
        r4 = a;	 Catch:{ all -> 0x003f }
        r4 = r4 + r0;
        a = r4;	 Catch:{ all -> 0x003f }
        r4 = 0;
        r1.write(r3, r4, r0);	 Catch:{ all -> 0x003f }
        goto L_0x001e;
    L_0x003f:
        r0 = move-exception;
    L_0x0040:
        if (r1 == 0) goto L_0x0045;
    L_0x0042:
        r1.close();
    L_0x0045:
        throw r0;
    L_0x0046:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0040;
        */
        throw new UnsupportedOperationException("Method not decompiled: u.aly.au.a(byte[]):byte[]");
    }

    public static String a(byte[] bArr, String str) throws UnsupportedEncodingException, DataFormatException {
        byte[] b = b(bArr);
        if (b != null) {
            return new String(b, str);
        }
        return null;
    }

    public static byte[] b(byte[] bArr) throws UnsupportedEncodingException, DataFormatException {
        int i = 0;
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        Inflater inflater = new Inflater();
        inflater.setInput(bArr, 0, bArr.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr2 = new byte[1024];
        while (!inflater.needsInput()) {
            int inflate = inflater.inflate(bArr2);
            byteArrayOutputStream.write(bArr2, i, inflate);
            i += inflate;
        }
        inflater.end();
        return byteArrayOutputStream.toByteArray();
    }
}
