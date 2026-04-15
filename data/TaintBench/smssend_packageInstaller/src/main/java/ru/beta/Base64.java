package ru.beta;

import java.io.UnsupportedEncodingException;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64.class.desiredAssertionStatus() ? true : $assertionsDisabled);
    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    static abstract class Coder {
        public int op;
        public byte[] output;

        public abstract int maxOutputSize(int i);

        public abstract boolean process(byte[] bArr, int i, int i2, boolean z);

        Coder() {
        }
    }

    static class Decoder extends Coder {
        private static final int[] DECODE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private final int[] alphabet;
        private int state;
        private int value;

        public Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        public int maxOutputSize(int len) {
            return ((len * 3) / 4) + 10;
        }

        /* JADX WARNING: Removed duplicated region for block: B:64:0x005c A:{SYNTHETIC} */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x0102  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x005f  */
        public boolean process(byte[] r12, int r13, int r14, boolean r15) {
            /*
            r11 = this;
            r9 = r11.state;
            r10 = 6;
            if (r9 != r10) goto L_0x0007;
        L_0x0005:
            r9 = 0;
        L_0x0006:
            return r9;
        L_0x0007:
            r5 = r13;
            r14 = r14 + r13;
            r7 = r11.state;
            r8 = r11.value;
            r2 = 0;
            r4 = r11.output;
            r0 = r11.alphabet;
        L_0x0012:
            if (r5 >= r14) goto L_0x0133;
        L_0x0014:
            if (r7 != 0) goto L_0x0067;
        L_0x0016:
            r9 = r5 + 4;
            if (r9 > r14) goto L_0x005a;
        L_0x001a:
            r9 = r12[r5];
            r9 = r9 & 255;
            r9 = r0[r9];
            r9 = r9 << 18;
            r10 = r5 + 1;
            r10 = r12[r10];
            r10 = r10 & 255;
            r10 = r0[r10];
            r10 = r10 << 12;
            r9 = r9 | r10;
            r10 = r5 + 2;
            r10 = r12[r10];
            r10 = r10 & 255;
            r10 = r0[r10];
            r10 = r10 << 6;
            r9 = r9 | r10;
            r10 = r5 + 3;
            r10 = r12[r10];
            r10 = r10 & 255;
            r10 = r0[r10];
            r8 = r9 | r10;
            if (r8 < 0) goto L_0x005a;
        L_0x0044:
            r9 = r2 + 2;
            r10 = (byte) r8;
            r4[r9] = r10;
            r9 = r2 + 1;
            r10 = r8 >> 8;
            r10 = (byte) r10;
            r4[r9] = r10;
            r9 = r8 >> 16;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r2 + 3;
            r5 = r5 + 4;
            goto L_0x0016;
        L_0x005a:
            if (r5 < r14) goto L_0x0067;
        L_0x005c:
            r3 = r2;
        L_0x005d:
            if (r15 != 0) goto L_0x0102;
        L_0x005f:
            r11.state = r7;
            r11.value = r8;
            r11.op = r3;
            r9 = 1;
            goto L_0x0006;
        L_0x0067:
            r6 = r5 + 1;
            r9 = r12[r5];
            r9 = r9 & 255;
            r1 = r0[r9];
            switch(r7) {
                case 0: goto L_0x0074;
                case 1: goto L_0x0082;
                case 2: goto L_0x0094;
                case 3: goto L_0x00b3;
                case 4: goto L_0x00ea;
                case 5: goto L_0x00f9;
                default: goto L_0x0072;
            };
        L_0x0072:
            r5 = r6;
            goto L_0x0012;
        L_0x0074:
            if (r1 < 0) goto L_0x007a;
        L_0x0076:
            r8 = r1;
            r7 = r7 + 1;
            goto L_0x0072;
        L_0x007a:
            r9 = -1;
            if (r1 == r9) goto L_0x0072;
        L_0x007d:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0082:
            if (r1 < 0) goto L_0x008b;
        L_0x0084:
            r9 = r8 << 6;
            r8 = r9 | r1;
            r7 = r7 + 1;
            goto L_0x0072;
        L_0x008b:
            r9 = -1;
            if (r1 == r9) goto L_0x0072;
        L_0x008e:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0094:
            if (r1 < 0) goto L_0x009d;
        L_0x0096:
            r9 = r8 << 6;
            r8 = r9 | r1;
            r7 = r7 + 1;
            goto L_0x0072;
        L_0x009d:
            r9 = -2;
            if (r1 != r9) goto L_0x00aa;
        L_0x00a0:
            r3 = r2 + 1;
            r9 = r8 >> 4;
            r9 = (byte) r9;
            r4[r2] = r9;
            r7 = 4;
            r2 = r3;
            goto L_0x0072;
        L_0x00aa:
            r9 = -1;
            if (r1 == r9) goto L_0x0072;
        L_0x00ad:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x00b3:
            if (r1 < 0) goto L_0x00ce;
        L_0x00b5:
            r9 = r8 << 6;
            r8 = r9 | r1;
            r9 = r2 + 2;
            r10 = (byte) r8;
            r4[r9] = r10;
            r9 = r2 + 1;
            r10 = r8 >> 8;
            r10 = (byte) r10;
            r4[r9] = r10;
            r9 = r8 >> 16;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r2 + 3;
            r7 = 0;
            goto L_0x0072;
        L_0x00ce:
            r9 = -2;
            if (r1 != r9) goto L_0x00e1;
        L_0x00d1:
            r9 = r2 + 1;
            r10 = r8 >> 2;
            r10 = (byte) r10;
            r4[r9] = r10;
            r9 = r8 >> 10;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r2 + 2;
            r7 = 5;
            goto L_0x0072;
        L_0x00e1:
            r9 = -1;
            if (r1 == r9) goto L_0x0072;
        L_0x00e4:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x00ea:
            r9 = -2;
            if (r1 != r9) goto L_0x00f0;
        L_0x00ed:
            r7 = r7 + 1;
            goto L_0x0072;
        L_0x00f0:
            r9 = -1;
            if (r1 == r9) goto L_0x0072;
        L_0x00f3:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x00f9:
            r9 = -1;
            if (r1 == r9) goto L_0x0072;
        L_0x00fc:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0102:
            switch(r7) {
                case 0: goto L_0x010d;
                case 1: goto L_0x010f;
                case 2: goto L_0x0115;
                case 3: goto L_0x011d;
                case 4: goto L_0x012d;
                default: goto L_0x0105;
            };
        L_0x0105:
            r2 = r3;
        L_0x0106:
            r11.state = r7;
            r11.op = r2;
            r9 = 1;
            goto L_0x0006;
        L_0x010d:
            r2 = r3;
            goto L_0x0106;
        L_0x010f:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0115:
            r2 = r3 + 1;
            r9 = r8 >> 4;
            r9 = (byte) r9;
            r4[r3] = r9;
            goto L_0x0106;
        L_0x011d:
            r2 = r3 + 1;
            r9 = r8 >> 10;
            r9 = (byte) r9;
            r4[r3] = r9;
            r3 = r2 + 1;
            r9 = r8 >> 2;
            r9 = (byte) r9;
            r4[r2] = r9;
            r2 = r3;
            goto L_0x0106;
        L_0x012d:
            r9 = 6;
            r11.state = r9;
            r9 = 0;
            goto L_0x0006;
        L_0x0133:
            r3 = r2;
            goto L_0x005d;
            */
            throw new UnsupportedOperationException("Method not decompiled: ru.beta.Base64$Decoder.process(byte[], int, int, boolean):boolean");
        }
    }

    static class Encoder extends Coder {
        static final /* synthetic */ boolean $assertionsDisabled;
        private static final byte[] ENCODE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        private static final byte[] ENCODE_WEBSAFE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        public static final int LINE_GROUPS = 19;
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        static {
            boolean z;
            if (Base64.class.desiredAssertionStatus()) {
                z = $assertionsDisabled;
            } else {
                z = true;
            }
            $assertionsDisabled = z;
        }

        public Encoder(int flags, byte[] output) {
            boolean z = true;
            this.output = output;
            this.do_padding = (flags & 1) == 0 ? true : $assertionsDisabled;
            this.do_newline = (flags & 2) == 0 ? true : $assertionsDisabled;
            if ((flags & 4) == 0) {
                z = $assertionsDisabled;
            }
            this.do_cr = z;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }

        public int maxOutputSize(int len) {
            return ((len * 8) / 5) + 10;
        }

        /* JADX WARNING: Removed duplicated region for block: B:42:0x0159  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0058  */
        /* JADX WARNING: Removed duplicated region for block: B:82:0x020c  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x00fe  */
        public boolean process(byte[] r15, int r16, int r17, boolean r18) {
            /*
            r14 = this;
            r1 = r14.alphabet;
            r5 = r14.output;
            r3 = 0;
            r2 = r14.count;
            r6 = r16;
            r17 = r17 + r16;
            r10 = -1;
            r11 = r14.tailLen;
            switch(r11) {
                case 0: goto L_0x0011;
                case 1: goto L_0x00b0;
                case 2: goto L_0x00d5;
                default: goto L_0x0011;
            };
        L_0x0011:
            r11 = -1;
            if (r10 == r11) goto L_0x0247;
        L_0x0014:
            r4 = r3 + 1;
            r11 = r10 >> 18;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r3 = r4 + 1;
            r11 = r10 >> 12;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = r10 >> 6;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r3 = r4 + 1;
            r11 = r10 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r2 = r2 + -1;
            if (r2 != 0) goto L_0x0247;
        L_0x003e:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x0049;
        L_0x0042:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x0049:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
            r2 = 19;
            r7 = r6;
        L_0x0052:
            r11 = r7 + 3;
            r0 = r17;
            if (r11 > r0) goto L_0x00fc;
        L_0x0058:
            r11 = r15[r7];
            r11 = r11 & 255;
            r11 = r11 << 16;
            r12 = r7 + 1;
            r12 = r15[r12];
            r12 = r12 & 255;
            r12 = r12 << 8;
            r11 = r11 | r12;
            r12 = r7 + 2;
            r12 = r15[r12];
            r12 = r12 & 255;
            r10 = r11 | r12;
            r11 = r10 >> 18;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r11 = r4 + 1;
            r12 = r10 >> 12;
            r12 = r12 & 63;
            r12 = r1[r12];
            r5[r11] = r12;
            r11 = r4 + 2;
            r12 = r10 >> 6;
            r12 = r12 & 63;
            r12 = r1[r12];
            r5[r11] = r12;
            r11 = r4 + 3;
            r12 = r10 & 63;
            r12 = r1[r12];
            r5[r11] = r12;
            r6 = r7 + 3;
            r3 = r4 + 4;
            r2 = r2 + -1;
            if (r2 != 0) goto L_0x0247;
        L_0x009b:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x00a6;
        L_0x009f:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x00a6:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
            r2 = 19;
            r7 = r6;
            goto L_0x0052;
        L_0x00b0:
            r11 = r6 + 2;
            r0 = r17;
            if (r11 > r0) goto L_0x0011;
        L_0x00b6:
            r11 = r14.tail;
            r12 = 0;
            r11 = r11[r12];
            r11 = r11 & 255;
            r11 = r11 << 16;
            r7 = r6 + 1;
            r12 = r15[r6];
            r12 = r12 & 255;
            r12 = r12 << 8;
            r11 = r11 | r12;
            r6 = r7 + 1;
            r12 = r15[r7];
            r12 = r12 & 255;
            r10 = r11 | r12;
            r11 = 0;
            r14.tailLen = r11;
            goto L_0x0011;
        L_0x00d5:
            r11 = r6 + 1;
            r0 = r17;
            if (r11 > r0) goto L_0x0011;
        L_0x00db:
            r11 = r14.tail;
            r12 = 0;
            r11 = r11[r12];
            r11 = r11 & 255;
            r11 = r11 << 16;
            r12 = r14.tail;
            r13 = 1;
            r12 = r12[r13];
            r12 = r12 & 255;
            r12 = r12 << 8;
            r11 = r11 | r12;
            r7 = r6 + 1;
            r12 = r15[r6];
            r12 = r12 & 255;
            r10 = r11 | r12;
            r11 = 0;
            r14.tailLen = r11;
            r6 = r7;
            goto L_0x0011;
        L_0x00fc:
            if (r18 == 0) goto L_0x020c;
        L_0x00fe:
            r11 = r14.tailLen;
            r11 = r7 - r11;
            r12 = r17 + -1;
            if (r11 != r12) goto L_0x0168;
        L_0x0106:
            r8 = 0;
            r11 = r14.tailLen;
            if (r11 <= 0) goto L_0x0163;
        L_0x010b:
            r11 = r14.tail;
            r9 = r8 + 1;
            r11 = r11[r8];
            r8 = r9;
            r6 = r7;
        L_0x0113:
            r11 = r11 & 255;
            r10 = r11 << 4;
            r11 = r14.tailLen;
            r11 = r11 - r8;
            r14.tailLen = r11;
            r3 = r4 + 1;
            r11 = r10 >> 6;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = r10 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r11 = r14.do_padding;
            if (r11 == 0) goto L_0x013e;
        L_0x0132:
            r3 = r4 + 1;
            r11 = 61;
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = 61;
            r5[r3] = r11;
        L_0x013e:
            r3 = r4;
            r11 = r14.do_newline;
            if (r11 == 0) goto L_0x0155;
        L_0x0143:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x014e;
        L_0x0147:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x014e:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
        L_0x0154:
            r3 = r4;
        L_0x0155:
            r11 = $assertionsDisabled;
            if (r11 != 0) goto L_0x01fe;
        L_0x0159:
            r11 = r14.tailLen;
            if (r11 == 0) goto L_0x01fe;
        L_0x015d:
            r11 = new java.lang.AssertionError;
            r11.<init>();
            throw r11;
        L_0x0163:
            r6 = r7 + 1;
            r11 = r15[r7];
            goto L_0x0113;
        L_0x0168:
            r11 = r14.tailLen;
            r11 = r7 - r11;
            r12 = r17 + -2;
            if (r11 != r12) goto L_0x01e0;
        L_0x0170:
            r8 = 0;
            r11 = r14.tailLen;
            r12 = 1;
            if (r11 <= r12) goto L_0x01d5;
        L_0x0176:
            r11 = r14.tail;
            r9 = r8 + 1;
            r11 = r11[r8];
            r8 = r9;
            r6 = r7;
        L_0x017e:
            r11 = r11 & 255;
            r12 = r11 << 10;
            r11 = r14.tailLen;
            if (r11 <= 0) goto L_0x01da;
        L_0x0186:
            r11 = r14.tail;
            r9 = r8 + 1;
            r11 = r11[r8];
            r8 = r9;
        L_0x018d:
            r11 = r11 & 255;
            r11 = r11 << 2;
            r10 = r12 | r11;
            r11 = r14.tailLen;
            r11 = r11 - r8;
            r14.tailLen = r11;
            r3 = r4 + 1;
            r11 = r10 >> 12;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r4 = r3 + 1;
            r11 = r10 >> 6;
            r11 = r11 & 63;
            r11 = r1[r11];
            r5[r3] = r11;
            r3 = r4 + 1;
            r11 = r10 & 63;
            r11 = r1[r11];
            r5[r4] = r11;
            r11 = r14.do_padding;
            if (r11 == 0) goto L_0x01bf;
        L_0x01b8:
            r4 = r3 + 1;
            r11 = 61;
            r5[r3] = r11;
            r3 = r4;
        L_0x01bf:
            r11 = r14.do_newline;
            if (r11 == 0) goto L_0x0155;
        L_0x01c3:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x01ce;
        L_0x01c7:
            r4 = r3 + 1;
            r11 = 13;
            r5[r3] = r11;
            r3 = r4;
        L_0x01ce:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
            goto L_0x0154;
        L_0x01d5:
            r6 = r7 + 1;
            r11 = r15[r7];
            goto L_0x017e;
        L_0x01da:
            r7 = r6 + 1;
            r11 = r15[r6];
            r6 = r7;
            goto L_0x018d;
        L_0x01e0:
            r11 = r14.do_newline;
            if (r11 == 0) goto L_0x01fa;
        L_0x01e4:
            if (r4 <= 0) goto L_0x01fa;
        L_0x01e6:
            r11 = 19;
            if (r2 == r11) goto L_0x01fa;
        L_0x01ea:
            r11 = r14.do_cr;
            if (r11 == 0) goto L_0x0245;
        L_0x01ee:
            r3 = r4 + 1;
            r11 = 13;
            r5[r4] = r11;
        L_0x01f4:
            r4 = r3 + 1;
            r11 = 10;
            r5[r3] = r11;
        L_0x01fa:
            r6 = r7;
            r3 = r4;
            goto L_0x0155;
        L_0x01fe:
            r11 = $assertionsDisabled;
            if (r11 != 0) goto L_0x021e;
        L_0x0202:
            r0 = r17;
            if (r6 == r0) goto L_0x021e;
        L_0x0206:
            r11 = new java.lang.AssertionError;
            r11.<init>();
            throw r11;
        L_0x020c:
            r11 = r17 + -1;
            if (r7 != r11) goto L_0x0224;
        L_0x0210:
            r11 = r14.tail;
            r12 = r14.tailLen;
            r13 = r12 + 1;
            r14.tailLen = r13;
            r13 = r15[r7];
            r11[r12] = r13;
            r6 = r7;
            r3 = r4;
        L_0x021e:
            r14.op = r3;
            r14.count = r2;
            r11 = 1;
            return r11;
        L_0x0224:
            r11 = r17 + -2;
            if (r7 != r11) goto L_0x0242;
        L_0x0228:
            r11 = r14.tail;
            r12 = r14.tailLen;
            r13 = r12 + 1;
            r14.tailLen = r13;
            r13 = r15[r7];
            r11[r12] = r13;
            r11 = r14.tail;
            r12 = r14.tailLen;
            r13 = r12 + 1;
            r14.tailLen = r13;
            r13 = r7 + 1;
            r13 = r15[r13];
            r11[r12] = r13;
        L_0x0242:
            r6 = r7;
            r3 = r4;
            goto L_0x021e;
        L_0x0245:
            r3 = r4;
            goto L_0x01f4;
        L_0x0247:
            r7 = r6;
            r4 = r3;
            goto L_0x0052;
            */
            throw new UnsupportedOperationException("Method not decompiled: ru.beta.Base64$Encoder.process(byte[], int, int, boolean):boolean");
        }
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[((len * 3) / 4)]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (decoder.op == decoder.output.length) {
            return decoder.output;
        } else {
            byte[] temp = new byte[decoder.op];
            System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
            return temp;
        }
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        Encoder encoder = new Encoder(flags, null);
        int output_len = (len / 3) * 4;
        if (!encoder.do_padding) {
            switch (len % 3) {
                case 1:
                    output_len += 2;
                    break;
                case 2:
                    output_len += 3;
                    break;
            }
        } else if (len % 3 > 0) {
            output_len += 4;
        }
        if (encoder.do_newline && len > 0) {
            int i;
            int i2 = ((len - 1) / 57) + 1;
            if (encoder.do_cr) {
                i = 2;
            } else {
                i = 1;
            }
            output_len += i * i2;
        }
        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);
        if ($assertionsDisabled || encoder.op == output_len) {
            return encoder.output;
        }
        throw new AssertionError();
    }

    private Base64() {
    }
}
