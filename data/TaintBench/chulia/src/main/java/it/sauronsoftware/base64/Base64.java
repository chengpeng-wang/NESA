package it.sauronsoftware.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Base64 {
    public static String encode(String str) throws RuntimeException {
        try {
            return new String(encode(str.getBytes()), "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("ASCII is not supported!", e);
        }
    }

    public static String encode(String str, String str2) throws RuntimeException {
        try {
            try {
                return new String(encode(str.getBytes(str2)), "ASCII");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("ASCII is not supported!", e);
            }
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Unsupported charset: " + str2, e2);
        }
    }

    public static String decode(String str) throws RuntimeException {
        try {
            return new String(decode(str.getBytes("ASCII")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("ASCII is not supported!", e);
        }
    }

    public static String decode(String str, String str2) throws RuntimeException {
        try {
            try {
                return new String(decode(str.getBytes("ASCII")), str2);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported charset: " + str2, e);
            }
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("ASCII is not supported!", e2);
        }
    }

    public static byte[] encode(byte[] bArr) throws RuntimeException {
        return encode(bArr, 0);
    }

    public static byte[] encode(byte[] bArr, int i) throws RuntimeException {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encode(byteArrayInputStream, byteArrayOutputStream, i);
            try {
                byteArrayInputStream.close();
            } catch (Throwable th) {
            }
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        } catch (Throwable th3) {
            try {
                byteArrayInputStream.close();
            } catch (Throwable th4) {
            }
            byteArrayOutputStream.close();
        }
    }

    public static byte[] decode(byte[] bArr) throws RuntimeException {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            decode(byteArrayInputStream, byteArrayOutputStream);
            try {
                byteArrayInputStream.close();
            } catch (Throwable th) {
            }
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        } catch (Throwable th3) {
            try {
                byteArrayInputStream.close();
            } catch (Throwable th4) {
            }
            byteArrayOutputStream.close();
        }
    }

    public static void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
        encode(inputStream, outputStream, 0);
    }

    public static void encode(InputStream inputStream, OutputStream outputStream, int i) throws IOException {
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, i);
        copy(inputStream, base64OutputStream);
        base64OutputStream.commit();
    }

    public static void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(new Base64InputStream(inputStream), outputStream);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x001d A:{SYNTHETIC, Splitter:B:16:0x001d} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0022 A:{SYNTHETIC, Splitter:B:19:0x0022} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001d A:{SYNTHETIC, Splitter:B:16:0x001d} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0022 A:{SYNTHETIC, Splitter:B:19:0x0022} */
    public static void encode(java.io.File r4, java.io.File r5, int r6) throws java.io.IOException {
        /*
        r2 = 0;
        r3 = new java.io.FileInputStream;	 Catch:{ all -> 0x0019 }
        r3.<init>(r4);	 Catch:{ all -> 0x0019 }
        r1 = new java.io.FileOutputStream;	 Catch:{ all -> 0x002e }
        r1.<init>(r5);	 Catch:{ all -> 0x002e }
        encode(r3, r1, r6);	 Catch:{ all -> 0x0032 }
        if (r1 == 0) goto L_0x0013;
    L_0x0010:
        r1.close();	 Catch:{ Throwable -> 0x0026 }
    L_0x0013:
        if (r3 == 0) goto L_0x0018;
    L_0x0015:
        r3.close();	 Catch:{ Throwable -> 0x0028 }
    L_0x0018:
        return;
    L_0x0019:
        r0 = move-exception;
        r1 = r2;
    L_0x001b:
        if (r1 == 0) goto L_0x0020;
    L_0x001d:
        r1.close();	 Catch:{ Throwable -> 0x002a }
    L_0x0020:
        if (r2 == 0) goto L_0x0025;
    L_0x0022:
        r2.close();	 Catch:{ Throwable -> 0x002c }
    L_0x0025:
        throw r0;
    L_0x0026:
        r0 = move-exception;
        goto L_0x0013;
    L_0x0028:
        r0 = move-exception;
        goto L_0x0018;
    L_0x002a:
        r1 = move-exception;
        goto L_0x0020;
    L_0x002c:
        r1 = move-exception;
        goto L_0x0025;
    L_0x002e:
        r0 = move-exception;
        r1 = r2;
        r2 = r3;
        goto L_0x001b;
    L_0x0032:
        r0 = move-exception;
        r2 = r3;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.base64.Base64.encode(java.io.File, java.io.File, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x001d A:{SYNTHETIC, Splitter:B:16:0x001d} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0022 A:{SYNTHETIC, Splitter:B:19:0x0022} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001d A:{SYNTHETIC, Splitter:B:16:0x001d} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0022 A:{SYNTHETIC, Splitter:B:19:0x0022} */
    public static void encode(java.io.File r4, java.io.File r5) throws java.io.IOException {
        /*
        r2 = 0;
        r3 = new java.io.FileInputStream;	 Catch:{ all -> 0x0019 }
        r3.<init>(r4);	 Catch:{ all -> 0x0019 }
        r1 = new java.io.FileOutputStream;	 Catch:{ all -> 0x002e }
        r1.<init>(r5);	 Catch:{ all -> 0x002e }
        encode(r3, r1);	 Catch:{ all -> 0x0032 }
        if (r1 == 0) goto L_0x0013;
    L_0x0010:
        r1.close();	 Catch:{ Throwable -> 0x0026 }
    L_0x0013:
        if (r3 == 0) goto L_0x0018;
    L_0x0015:
        r3.close();	 Catch:{ Throwable -> 0x0028 }
    L_0x0018:
        return;
    L_0x0019:
        r0 = move-exception;
        r1 = r2;
    L_0x001b:
        if (r1 == 0) goto L_0x0020;
    L_0x001d:
        r1.close();	 Catch:{ Throwable -> 0x002a }
    L_0x0020:
        if (r2 == 0) goto L_0x0025;
    L_0x0022:
        r2.close();	 Catch:{ Throwable -> 0x002c }
    L_0x0025:
        throw r0;
    L_0x0026:
        r0 = move-exception;
        goto L_0x0013;
    L_0x0028:
        r0 = move-exception;
        goto L_0x0018;
    L_0x002a:
        r1 = move-exception;
        goto L_0x0020;
    L_0x002c:
        r1 = move-exception;
        goto L_0x0025;
    L_0x002e:
        r0 = move-exception;
        r1 = r2;
        r2 = r3;
        goto L_0x001b;
    L_0x0032:
        r0 = move-exception;
        r2 = r3;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.base64.Base64.encode(java.io.File, java.io.File):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x001d A:{SYNTHETIC, Splitter:B:16:0x001d} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0022 A:{SYNTHETIC, Splitter:B:19:0x0022} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001d A:{SYNTHETIC, Splitter:B:16:0x001d} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0022 A:{SYNTHETIC, Splitter:B:19:0x0022} */
    public static void decode(java.io.File r4, java.io.File r5) throws java.io.IOException {
        /*
        r2 = 0;
        r3 = new java.io.FileInputStream;	 Catch:{ all -> 0x0019 }
        r3.<init>(r4);	 Catch:{ all -> 0x0019 }
        r1 = new java.io.FileOutputStream;	 Catch:{ all -> 0x002e }
        r1.<init>(r5);	 Catch:{ all -> 0x002e }
        decode(r3, r1);	 Catch:{ all -> 0x0032 }
        if (r1 == 0) goto L_0x0013;
    L_0x0010:
        r1.close();	 Catch:{ Throwable -> 0x0026 }
    L_0x0013:
        if (r3 == 0) goto L_0x0018;
    L_0x0015:
        r3.close();	 Catch:{ Throwable -> 0x0028 }
    L_0x0018:
        return;
    L_0x0019:
        r0 = move-exception;
        r1 = r2;
    L_0x001b:
        if (r1 == 0) goto L_0x0020;
    L_0x001d:
        r1.close();	 Catch:{ Throwable -> 0x002a }
    L_0x0020:
        if (r2 == 0) goto L_0x0025;
    L_0x0022:
        r2.close();	 Catch:{ Throwable -> 0x002c }
    L_0x0025:
        throw r0;
    L_0x0026:
        r0 = move-exception;
        goto L_0x0013;
    L_0x0028:
        r0 = move-exception;
        goto L_0x0018;
    L_0x002a:
        r1 = move-exception;
        goto L_0x0020;
    L_0x002c:
        r1 = move-exception;
        goto L_0x0025;
    L_0x002e:
        r0 = move-exception;
        r1 = r2;
        r2 = r3;
        goto L_0x001b;
    L_0x0032:
        r0 = move-exception;
        r2 = r3;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.base64.Base64.decode(java.io.File, java.io.File):void");
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }
}
