package com.google.tagmanager.protobuf;

final class Utf8 {
    public static final int COMPLETE = 0;
    public static final int MALFORMED = -1;

    private Utf8() {
    }

    public static boolean isValidUtf8(byte[] bytes) {
        return isValidUtf8(bytes, 0, bytes.length);
    }

    public static boolean isValidUtf8(byte[] bytes, int index, int limit) {
        return partialIsValidUtf8(bytes, index, limit) == 0;
    }

    /* JADX WARNING: Missing block: B:26:0x0044, code skipped:
            if (r10[r3] > (byte) -65) goto L_0x0046;
     */
    /* JADX WARNING: Missing block: B:44:0x007f, code skipped:
            if (r10[r3] > (byte) -65) goto L_0x0081;
     */
    public static int partialIsValidUtf8(int r9, byte[] r10, int r11, int r12) {
        /*
        r8 = -32;
        r6 = -96;
        r4 = -1;
        r7 = -65;
        if (r9 == 0) goto L_0x0084;
    L_0x0009:
        if (r11 < r12) goto L_0x000c;
    L_0x000b:
        return r9;
    L_0x000c:
        r0 = (byte) r9;
        if (r0 >= r8) goto L_0x001c;
    L_0x000f:
        r5 = -62;
        if (r0 < r5) goto L_0x001a;
    L_0x0013:
        r3 = r11 + 1;
        r5 = r10[r11];
        if (r5 <= r7) goto L_0x0083;
    L_0x0019:
        r11 = r3;
    L_0x001a:
        r9 = r4;
        goto L_0x000b;
    L_0x001c:
        r5 = -16;
        if (r0 >= r5) goto L_0x0048;
    L_0x0020:
        r5 = r9 >> 8;
        r5 = r5 ^ -1;
        r1 = (byte) r5;
        if (r1 != 0) goto L_0x0033;
    L_0x0027:
        r3 = r11 + 1;
        r1 = r10[r11];
        if (r3 < r12) goto L_0x0034;
    L_0x002d:
        r9 = incompleteStateFor(r0, r1);
        r11 = r3;
        goto L_0x000b;
    L_0x0033:
        r3 = r11;
    L_0x0034:
        if (r1 > r7) goto L_0x008b;
    L_0x0036:
        if (r0 != r8) goto L_0x003a;
    L_0x0038:
        if (r1 < r6) goto L_0x008b;
    L_0x003a:
        r5 = -19;
        if (r0 != r5) goto L_0x0040;
    L_0x003e:
        if (r1 >= r6) goto L_0x008b;
    L_0x0040:
        r11 = r3 + 1;
        r5 = r10[r3];
        if (r5 <= r7) goto L_0x0084;
    L_0x0046:
        r9 = r4;
        goto L_0x000b;
    L_0x0048:
        r5 = r9 >> 8;
        r5 = r5 ^ -1;
        r1 = (byte) r5;
        r2 = 0;
        if (r1 != 0) goto L_0x005c;
    L_0x0050:
        r3 = r11 + 1;
        r1 = r10[r11];
        if (r3 < r12) goto L_0x0060;
    L_0x0056:
        r9 = incompleteStateFor(r0, r1);
        r11 = r3;
        goto L_0x000b;
    L_0x005c:
        r5 = r9 >> 16;
        r2 = (byte) r5;
        r3 = r11;
    L_0x0060:
        if (r2 != 0) goto L_0x006e;
    L_0x0062:
        r11 = r3 + 1;
        r2 = r10[r3];
        if (r11 < r12) goto L_0x006d;
    L_0x0068:
        r9 = incompleteStateFor(r0, r1, r2);
        goto L_0x000b;
    L_0x006d:
        r3 = r11;
    L_0x006e:
        if (r1 > r7) goto L_0x0089;
    L_0x0070:
        r5 = r0 << 28;
        r6 = r1 + 112;
        r5 = r5 + r6;
        r5 = r5 >> 30;
        if (r5 != 0) goto L_0x0089;
    L_0x0079:
        if (r2 > r7) goto L_0x0089;
    L_0x007b:
        r11 = r3 + 1;
        r5 = r10[r3];
        if (r5 <= r7) goto L_0x0084;
    L_0x0081:
        r9 = r4;
        goto L_0x000b;
    L_0x0083:
        r11 = r3;
    L_0x0084:
        r9 = partialIsValidUtf8(r10, r11, r12);
        goto L_0x000b;
    L_0x0089:
        r11 = r3;
        goto L_0x0081;
    L_0x008b:
        r11 = r3;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.protobuf.Utf8.partialIsValidUtf8(int, byte[], int, int):int");
    }

    public static int partialIsValidUtf8(byte[] bytes, int index, int limit) {
        while (index < limit && bytes[index] >= (byte) 0) {
            index++;
        }
        return index >= limit ? 0 : partialIsValidUtf8NonAscii(bytes, index, limit);
    }

    private static int partialIsValidUtf8NonAscii(byte[] bytes, int index, int limit) {
        int index2 = index;
        while (index2 < limit) {
            index = index2 + 1;
            int byte1 = bytes[index2];
            if (byte1 < 0) {
                int byte2;
                if (byte1 < -32) {
                    if (index >= limit) {
                        return byte1;
                    }
                    if (byte1 >= -62) {
                        index2 = index + 1;
                        if (bytes[index] > (byte) -65) {
                            index = index2;
                        }
                    }
                    return -1;
                } else if (byte1 < -16) {
                    if (index >= limit - 1) {
                        return incompleteStateFor(bytes, index, limit);
                    }
                    index2 = index + 1;
                    byte2 = bytes[index];
                    if (byte2 > -65 || ((byte1 == -32 && byte2 < -96) || (byte1 == -19 && byte2 >= -96))) {
                        index = index2;
                    } else {
                        index = index2 + 1;
                        if (bytes[index2] > (byte) -65) {
                        }
                        index2 = index;
                    }
                    return -1;
                } else if (index >= limit - 2) {
                    return incompleteStateFor(bytes, index, limit);
                } else {
                    index2 = index + 1;
                    byte2 = bytes[index];
                    if (byte2 <= -65 && (((byte1 << 28) + (byte2 + 112)) >> 30) == 0) {
                        index = index2 + 1;
                        if (bytes[index2] > (byte) -65) {
                            return -1;
                        }
                        index2 = index + 1;
                        if (bytes[index] > (byte) -65) {
                        }
                    }
                    index = index2;
                    return -1;
                }
                index = index2;
                index2 = index;
            } else {
                index2 = index;
            }
        }
        index = index2;
        return 0;
    }

    private static int incompleteStateFor(int byte1) {
        return byte1 > -12 ? -1 : byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2) {
        return (byte1 > -12 || byte2 > -65) ? -1 : (byte2 << 8) ^ byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2, int byte3) {
        return (byte1 > -12 || byte2 > -65 || byte3 > -65) ? -1 : ((byte2 << 8) ^ byte1) ^ (byte3 << 16);
    }

    private static int incompleteStateFor(byte[] bytes, int index, int limit) {
        int byte1 = bytes[index - 1];
        switch (limit - index) {
            case 0:
                return incompleteStateFor(byte1);
            case 1:
                return incompleteStateFor(byte1, bytes[index]);
            case 2:
                return incompleteStateFor(byte1, bytes[index], bytes[index + 1]);
            default:
                throw new AssertionError();
        }
    }
}
