package com.sun.mail.iap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResponseInputStream {
    private static final int incrementSlop = 16;
    private static final int maxIncrement = 262144;
    private static final int minIncrement = 256;
    private BufferedInputStream bin;

    public ResponseInputStream(InputStream in) {
        this.bin = new BufferedInputStream(in, 2048);
    }

    public ByteArray readResponse() throws IOException {
        return readResponse(null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0024 A:{SYNTHETIC} */
    public com.sun.mail.iap.ByteArray readResponse(com.sun.mail.iap.ByteArray r15) throws java.io.IOException {
        /*
        r14 = this;
        if (r15 != 0) goto L_0x000e;
    L_0x0002:
        r15 = new com.sun.mail.iap.ByteArray;
        r11 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r11 = new byte[r11];
        r12 = 0;
        r13 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r15.m287init(r11, r12, r13);
    L_0x000e:
        r3 = r15.getBytes();
        r8 = 0;
    L_0x0013:
        r2 = 0;
        r6 = 0;
        r9 = r8;
    L_0x0016:
        if (r6 != 0) goto L_0x0021;
    L_0x0018:
        r11 = r14.bin;
        r2 = r11.read();
        r11 = -1;
        if (r2 != r11) goto L_0x002a;
    L_0x0021:
        r11 = -1;
        if (r2 != r11) goto L_0x0051;
    L_0x0024:
        r11 = new java.io.IOException;
        r11.<init>();
        throw r11;
    L_0x002a:
        switch(r2) {
            case 10: goto L_0x0045;
            default: goto L_0x002d;
        };
    L_0x002d:
        r11 = r3.length;
        if (r9 < r11) goto L_0x003e;
    L_0x0030:
        r10 = r3.length;
        r11 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        if (r10 <= r11) goto L_0x0037;
    L_0x0035:
        r10 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
    L_0x0037:
        r15.grow(r10);
        r3 = r15.getBytes();
    L_0x003e:
        r8 = r9 + 1;
        r11 = (byte) r2;
        r3[r9] = r11;
        r9 = r8;
        goto L_0x0016;
    L_0x0045:
        if (r9 <= 0) goto L_0x002d;
    L_0x0047:
        r11 = r9 + -1;
        r11 = r3[r11];
        r12 = 13;
        if (r11 != r12) goto L_0x002d;
    L_0x004f:
        r6 = 1;
        goto L_0x002d;
    L_0x0051:
        r11 = 5;
        if (r9 < r11) goto L_0x005c;
    L_0x0054:
        r11 = r9 + -3;
        r11 = r3[r11];
        r12 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r11 == r12) goto L_0x0060;
    L_0x005c:
        r15.setCount(r9);
        return r15;
    L_0x0060:
        r7 = r9 + -4;
    L_0x0062:
        if (r7 >= 0) goto L_0x0094;
    L_0x0064:
        if (r7 < 0) goto L_0x005c;
    L_0x0066:
        r4 = 0;
        r11 = r7 + 1;
        r12 = r9 + -3;
        r4 = com.sun.mail.util.ASCIIUtility.parseInt(r3, r11, r12);	 Catch:{ NumberFormatException -> 0x009d }
        if (r4 <= 0) goto L_0x00a5;
    L_0x0071:
        r11 = r3.length;
        r1 = r11 - r9;
        r11 = r4 + 16;
        if (r11 <= r1) goto L_0x00a3;
    L_0x0078:
        r11 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r12 = r4 + 16;
        r12 = r12 - r1;
        if (r11 <= r12) goto L_0x009f;
    L_0x007f:
        r11 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
    L_0x0081:
        r15.grow(r11);
        r3 = r15.getBytes();
        r8 = r9;
    L_0x0089:
        if (r4 <= 0) goto L_0x0013;
    L_0x008b:
        r11 = r14.bin;
        r0 = r11.read(r3, r8, r4);
        r4 = r4 - r0;
        r8 = r8 + r0;
        goto L_0x0089;
    L_0x0094:
        r11 = r3[r7];
        r12 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r11 == r12) goto L_0x0064;
    L_0x009a:
        r7 = r7 + -1;
        goto L_0x0062;
    L_0x009d:
        r5 = move-exception;
        goto L_0x005c;
    L_0x009f:
        r11 = r4 + 16;
        r11 = r11 - r1;
        goto L_0x0081;
    L_0x00a3:
        r8 = r9;
        goto L_0x0089;
    L_0x00a5:
        r8 = r9;
        goto L_0x0013;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.iap.ResponseInputStream.readResponse(com.sun.mail.iap.ByteArray):com.sun.mail.iap.ByteArray");
    }
}
