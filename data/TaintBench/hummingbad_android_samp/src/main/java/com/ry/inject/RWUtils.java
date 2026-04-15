package com.ry.inject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RWUtils {
    private static final int BUFF_SIZE = 4096;

    /* JADX WARNING: Removed duplicated region for block: B:33:0x005e A:{SYNTHETIC, Splitter:B:33:0x005e} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0063 A:{SYNTHETIC, Splitter:B:36:0x0063} */
    public static final boolean decode(java.io.InputStream r9, java.io.File r10) throws java.io.IOException {
        /*
        r7 = 0;
        if (r9 == 0) goto L_0x0005;
    L_0x0003:
        if (r10 != 0) goto L_0x0006;
    L_0x0005:
        return r7;
    L_0x0006:
        r7 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = new byte[r7];
        r6 = new java.io.File;
        r7 = new java.lang.StringBuilder;
        r8 = r10.getPath();
        r8 = java.lang.String.valueOf(r8);
        r7.<init>(r8);
        r8 = ".tmp";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.<init>(r7);
        r4 = 0;
        r10.delete();	 Catch:{ all -> 0x0082 }
        r5 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0082 }
        r5.<init>(r6);	 Catch:{ all -> 0x0082 }
    L_0x002f:
        r3 = r9.read(r0);	 Catch:{ all -> 0x0059 }
        if (r3 >= 0) goto L_0x004e;
    L_0x0035:
        r5.close();	 Catch:{ all -> 0x0059 }
        r4 = 0;
        r7 = r6.renameTo(r10);	 Catch:{ all -> 0x0082 }
        r0 = 0;
        if (r9 == 0) goto L_0x0043;
    L_0x0040:
        r9.close();	 Catch:{ IOException -> 0x0073 }
    L_0x0043:
        if (r4 == 0) goto L_0x0005;
    L_0x0045:
        r4.close();	 Catch:{ IOException -> 0x0049 }
        goto L_0x0005;
    L_0x0049:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0005;
    L_0x004e:
        r2 = 0;
    L_0x004f:
        if (r2 < r3) goto L_0x0067;
    L_0x0051:
        r7 = 0;
        r5.write(r0, r7, r3);	 Catch:{ all -> 0x0059 }
        r5.flush();	 Catch:{ all -> 0x0059 }
        goto L_0x002f;
    L_0x0059:
        r7 = move-exception;
        r4 = r5;
    L_0x005b:
        r0 = 0;
        if (r9 == 0) goto L_0x0061;
    L_0x005e:
        r9.close();	 Catch:{ IOException -> 0x0078 }
    L_0x0061:
        if (r4 == 0) goto L_0x0066;
    L_0x0063:
        r4.close();	 Catch:{ IOException -> 0x007d }
    L_0x0066:
        throw r7;
    L_0x0067:
        r7 = r0[r2];	 Catch:{ all -> 0x0059 }
        r7 = r7 & 255;
        r7 = r7 ^ 85;
        r7 = (byte) r7;	 Catch:{ all -> 0x0059 }
        r0[r2] = r7;	 Catch:{ all -> 0x0059 }
        r2 = r2 + 1;
        goto L_0x004f;
    L_0x0073:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0043;
    L_0x0078:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0061;
    L_0x007d:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0066;
    L_0x0082:
        r7 = move-exception;
        goto L_0x005b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ry.inject.RWUtils.decode(java.io.InputStream, java.io.File):boolean");
    }

    public static final boolean write(String context, File target) throws IOException {
        return write(context.getBytes(), target);
    }

    public static final boolean write(byte[] context, File target) throws IOException {
        return write(new ByteArrayInputStream(context), target);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x005b A:{SYNTHETIC, Splitter:B:31:0x005b} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0060 A:{SYNTHETIC, Splitter:B:34:0x0060} */
    public static final boolean write(java.io.InputStream r8, java.io.File r9) throws java.io.IOException {
        /*
        r6 = 0;
        if (r8 == 0) goto L_0x0005;
    L_0x0003:
        if (r9 != 0) goto L_0x0006;
    L_0x0005:
        return r6;
    L_0x0006:
        r6 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = new byte[r6];
        r5 = new java.io.File;
        r6 = new java.lang.StringBuilder;
        r7 = r9.getPath();
        r7 = java.lang.String.valueOf(r7);
        r6.<init>(r7);
        r7 = ".tmp";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r5.<init>(r6);
        r3 = 0;
        r9.delete();	 Catch:{ all -> 0x0073 }
        r4 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0073 }
        r4.<init>(r5);	 Catch:{ all -> 0x0073 }
    L_0x002f:
        r2 = r8.read(r0);	 Catch:{ all -> 0x0056 }
        if (r2 >= 0) goto L_0x004e;
    L_0x0035:
        r4.close();	 Catch:{ all -> 0x0056 }
        r3 = 0;
        r6 = r5.renameTo(r9);	 Catch:{ all -> 0x0073 }
        r0 = 0;
        if (r8 == 0) goto L_0x0043;
    L_0x0040:
        r8.close();	 Catch:{ IOException -> 0x0064 }
    L_0x0043:
        if (r3 == 0) goto L_0x0005;
    L_0x0045:
        r3.close();	 Catch:{ IOException -> 0x0049 }
        goto L_0x0005;
    L_0x0049:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0005;
    L_0x004e:
        r6 = 0;
        r4.write(r0, r6, r2);	 Catch:{ all -> 0x0056 }
        r4.flush();	 Catch:{ all -> 0x0056 }
        goto L_0x002f;
    L_0x0056:
        r6 = move-exception;
        r3 = r4;
    L_0x0058:
        r0 = 0;
        if (r8 == 0) goto L_0x005e;
    L_0x005b:
        r8.close();	 Catch:{ IOException -> 0x0069 }
    L_0x005e:
        if (r3 == 0) goto L_0x0063;
    L_0x0060:
        r3.close();	 Catch:{ IOException -> 0x006e }
    L_0x0063:
        throw r6;
    L_0x0064:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0043;
    L_0x0069:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x005e;
    L_0x006e:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0063;
    L_0x0073:
        r6 = move-exception;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ry.inject.RWUtils.write(java.io.InputStream, java.io.File):boolean");
    }

    public static final byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        while (true) {
            try {
                int m = in.read(b);
                if (m < 0) {
                    break;
                }
                out.write(b, 0, m);
                out.flush();
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        byte[] toByteArray = out.toByteArray();
        if (in != null) {
            try {
                in.close();
            } catch (IOException e22) {
                e22.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e222) {
                e222.printStackTrace();
            }
        }
        return toByteArray;
    }
}
