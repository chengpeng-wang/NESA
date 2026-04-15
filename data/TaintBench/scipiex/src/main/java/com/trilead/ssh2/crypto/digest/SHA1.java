package com.trilead.ssh2.crypto.digest;

public final class SHA1 implements Digest {
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private long currentLen;
    private int currentPos;
    private final int[] w = new int[80];

    public SHA1() {
        reset();
    }

    public final int getDigestLength() {
        return 20;
    }

    public final void reset() {
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.H4 = -1009589776;
        this.currentPos = 0;
        this.currentLen = 0;
    }

    public final void update(byte[] b) {
        update(b, 0, b.length);
    }

    /* JADX WARNING: Missing block: B:6:0x0018, code skipped:
            if (r15 >= 8) goto L_0x00f8;
     */
    /* JADX WARNING: Missing block: B:7:0x001a, code skipped:
            if (r15 < 0) goto L_0x0173;
     */
    /* JADX WARNING: Missing block: B:22:0x00f8, code skipped:
            r14 = r1 + 1;
            r1 = r14 + 1;
            r14 = r1 + 1;
            r1 = r14 + 1;
            r12.w[r12.currentPos >> 2] = ((((r13[r1] & 255) << 24) | ((r13[r14] & 255) << 16)) | ((r13[r1] & 255) << 8)) | (r13[r14] & 255);
            r12.currentPos += 4;
     */
    /* JADX WARNING: Missing block: B:23:0x0129, code skipped:
            if (r12.currentPos != 64) goto L_0x0130;
     */
    /* JADX WARNING: Missing block: B:24:0x012b, code skipped:
            perform();
            r12.currentPos = 0;
     */
    /* JADX WARNING: Missing block: B:25:0x0130, code skipped:
            r14 = r1 + 1;
            r1 = r14 + 1;
            r14 = r1 + 1;
            r1 = r14 + 1;
            r12.w[r12.currentPos >> 2] = ((((r13[r1] & 255) << 24) | ((r13[r14] & 255) << 16)) | ((r13[r1] & 255) << 8)) | (r13[r14] & 255);
            r12.currentPos += 4;
     */
    /* JADX WARNING: Missing block: B:26:0x0161, code skipped:
            if (r12.currentPos != 64) goto L_0x0168;
     */
    /* JADX WARNING: Missing block: B:27:0x0163, code skipped:
            perform();
            r12.currentPos = 0;
     */
    /* JADX WARNING: Missing block: B:28:0x0168, code skipped:
            r12.currentLen += 64;
            r15 = r15 - 8;
     */
    /* JADX WARNING: Missing block: B:29:0x0173, code skipped:
            r14 = r1 + 1;
            r1 = r14 + 1;
            r14 = r1 + 1;
            r1 = r14 + 1;
            r12.w[r12.currentPos >> 2] = ((((r13[r1] & 255) << 24) | ((r13[r14] & 255) << 16)) | ((r13[r1] & 255) << 8)) | (r13[r14] & 255);
            r15 = r15 - 4;
            r12.currentPos += 4;
            r12.currentLen += 32;
     */
    /* JADX WARNING: Missing block: B:30:0x01ab, code skipped:
            if (r12.currentPos != 64) goto L_0x001a;
     */
    /* JADX WARNING: Missing block: B:31:0x01ad, code skipped:
            perform();
            r12.currentPos = 0;
     */
    /* JADX WARNING: Missing block: B:36:0x01e2, code skipped:
            r1 = r14;
     */
    public final void update(byte[] r13, int r14, int r15) {
        /*
        r12 = this;
        r10 = 32;
        r8 = 8;
        r7 = 64;
        r6 = 0;
        r2 = 4;
        if (r15 < r2) goto L_0x01e5;
    L_0x000a:
        r2 = r12.currentPos;
        r0 = r2 >> 2;
        r2 = r12.currentPos;
        r2 = r2 & 3;
        switch(r2) {
            case 0: goto L_0x001f;
            case 1: goto L_0x005c;
            case 2: goto L_0x0099;
            case 3: goto L_0x00ce;
            default: goto L_0x0015;
        };
    L_0x0015:
        r1 = r14;
    L_0x0016:
        r2 = 8;
        if (r15 >= r2) goto L_0x00f8;
    L_0x001a:
        if (r15 < 0) goto L_0x0173;
    L_0x001c:
        if (r15 > 0) goto L_0x01b4;
    L_0x001e:
        return;
    L_0x001f:
        r2 = r12.w;
        r1 = r14 + 1;
        r3 = r13[r14];
        r3 = r3 & 255;
        r3 = r3 << 24;
        r14 = r1 + 1;
        r4 = r13[r1];
        r4 = r4 & 255;
        r4 = r4 << 16;
        r3 = r3 | r4;
        r1 = r14 + 1;
        r4 = r13[r14];
        r4 = r4 & 255;
        r4 = r4 << 8;
        r3 = r3 | r4;
        r14 = r1 + 1;
        r4 = r13[r1];
        r4 = r4 & 255;
        r3 = r3 | r4;
        r2[r0] = r3;
        r15 = r15 + -4;
        r2 = r12.currentPos;
        r2 = r2 + 4;
        r12.currentPos = r2;
        r2 = r12.currentLen;
        r2 = r2 + r10;
        r12.currentLen = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x01e2;
    L_0x0055:
        r12.perform();
        r12.currentPos = r6;
        r1 = r14;
        goto L_0x0016;
    L_0x005c:
        r2 = r12.w;
        r3 = r12.w;
        r3 = r3[r0];
        r3 = r3 << 24;
        r1 = r14 + 1;
        r4 = r13[r14];
        r4 = r4 & 255;
        r4 = r4 << 16;
        r14 = r1 + 1;
        r5 = r13[r1];
        r5 = r5 & 255;
        r5 = r5 << 8;
        r4 = r4 | r5;
        r1 = r14 + 1;
        r5 = r13[r14];
        r5 = r5 & 255;
        r4 = r4 | r5;
        r3 = r3 | r4;
        r2[r0] = r3;
        r15 = r15 + -3;
        r2 = r12.currentPos;
        r2 = r2 + 3;
        r12.currentPos = r2;
        r2 = r12.currentLen;
        r4 = 24;
        r2 = r2 + r4;
        r12.currentLen = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x0016;
    L_0x0092:
        r12.perform();
        r12.currentPos = r6;
        goto L_0x0016;
    L_0x0099:
        r2 = r12.w;
        r3 = r12.w;
        r3 = r3[r0];
        r3 = r3 << 16;
        r1 = r14 + 1;
        r4 = r13[r14];
        r4 = r4 & 255;
        r4 = r4 << 8;
        r14 = r1 + 1;
        r5 = r13[r1];
        r5 = r5 & 255;
        r4 = r4 | r5;
        r3 = r3 | r4;
        r2[r0] = r3;
        r15 = r15 + -2;
        r2 = r12.currentPos;
        r2 = r2 + 2;
        r12.currentPos = r2;
        r2 = r12.currentLen;
        r4 = 16;
        r2 = r2 + r4;
        r12.currentLen = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x01e2;
    L_0x00c6:
        r12.perform();
        r12.currentPos = r6;
        r1 = r14;
        goto L_0x0016;
    L_0x00ce:
        r2 = r12.w;
        r3 = r12.w;
        r3 = r3[r0];
        r3 = r3 << 8;
        r1 = r14 + 1;
        r4 = r13[r14];
        r4 = r4 & 255;
        r3 = r3 | r4;
        r2[r0] = r3;
        r15 = r15 + -1;
        r2 = r12.currentPos;
        r2 = r2 + 1;
        r12.currentPos = r2;
        r2 = r12.currentLen;
        r2 = r2 + r8;
        r12.currentLen = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x0016;
    L_0x00f0:
        r12.perform();
        r12.currentPos = r6;
        r14 = r1;
        goto L_0x0015;
    L_0x00f8:
        r2 = r12.w;
        r3 = r12.currentPos;
        r3 = r3 >> 2;
        r14 = r1 + 1;
        r4 = r13[r1];
        r4 = r4 & 255;
        r4 = r4 << 24;
        r1 = r14 + 1;
        r5 = r13[r14];
        r5 = r5 & 255;
        r5 = r5 << 16;
        r4 = r4 | r5;
        r14 = r1 + 1;
        r5 = r13[r1];
        r5 = r5 & 255;
        r5 = r5 << 8;
        r4 = r4 | r5;
        r1 = r14 + 1;
        r5 = r13[r14];
        r5 = r5 & 255;
        r4 = r4 | r5;
        r2[r3] = r4;
        r2 = r12.currentPos;
        r2 = r2 + 4;
        r12.currentPos = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x0130;
    L_0x012b:
        r12.perform();
        r12.currentPos = r6;
    L_0x0130:
        r2 = r12.w;
        r3 = r12.currentPos;
        r3 = r3 >> 2;
        r14 = r1 + 1;
        r4 = r13[r1];
        r4 = r4 & 255;
        r4 = r4 << 24;
        r1 = r14 + 1;
        r5 = r13[r14];
        r5 = r5 & 255;
        r5 = r5 << 16;
        r4 = r4 | r5;
        r14 = r1 + 1;
        r5 = r13[r1];
        r5 = r5 & 255;
        r5 = r5 << 8;
        r4 = r4 | r5;
        r1 = r14 + 1;
        r5 = r13[r14];
        r5 = r5 & 255;
        r4 = r4 | r5;
        r2[r3] = r4;
        r2 = r12.currentPos;
        r2 = r2 + 4;
        r12.currentPos = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x0168;
    L_0x0163:
        r12.perform();
        r12.currentPos = r6;
    L_0x0168:
        r2 = r12.currentLen;
        r4 = 64;
        r2 = r2 + r4;
        r12.currentLen = r2;
        r15 = r15 + -8;
        goto L_0x0016;
    L_0x0173:
        r2 = r12.w;
        r3 = r12.currentPos;
        r3 = r3 >> 2;
        r14 = r1 + 1;
        r4 = r13[r1];
        r4 = r4 & 255;
        r4 = r4 << 24;
        r1 = r14 + 1;
        r5 = r13[r14];
        r5 = r5 & 255;
        r5 = r5 << 16;
        r4 = r4 | r5;
        r14 = r1 + 1;
        r5 = r13[r1];
        r5 = r5 & 255;
        r5 = r5 << 8;
        r4 = r4 | r5;
        r1 = r14 + 1;
        r5 = r13[r14];
        r5 = r5 & 255;
        r4 = r4 | r5;
        r2[r3] = r4;
        r15 = r15 + -4;
        r2 = r12.currentPos;
        r2 = r2 + 4;
        r12.currentPos = r2;
        r2 = r12.currentLen;
        r2 = r2 + r10;
        r12.currentLen = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x001a;
    L_0x01ad:
        r12.perform();
        r12.currentPos = r6;
        goto L_0x001a;
    L_0x01b4:
        r2 = r12.currentPos;
        r0 = r2 >> 2;
        r2 = r12.w;
        r3 = r12.w;
        r3 = r3[r0];
        r3 = r3 << 8;
        r14 = r1 + 1;
        r4 = r13[r1];
        r4 = r4 & 255;
        r3 = r3 | r4;
        r2[r0] = r3;
        r2 = r12.currentLen;
        r2 = r2 + r8;
        r12.currentLen = r2;
        r2 = r12.currentPos;
        r2 = r2 + 1;
        r12.currentPos = r2;
        r2 = r12.currentPos;
        if (r2 != r7) goto L_0x01dd;
    L_0x01d8:
        r12.perform();
        r12.currentPos = r6;
    L_0x01dd:
        r15 = r15 + -1;
        r1 = r14;
        goto L_0x001c;
    L_0x01e2:
        r1 = r14;
        goto L_0x0016;
    L_0x01e5:
        r1 = r14;
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.crypto.digest.SHA1.update(byte[], int, int):void");
    }

    public final void update(byte b) {
        int idx = this.currentPos >> 2;
        this.w[idx] = (this.w[idx] << 8) | (b & 255);
        this.currentLen += 8;
        this.currentPos++;
        if (this.currentPos == 64) {
            perform();
            this.currentPos = 0;
        }
    }

    private final void putInt(byte[] b, int pos, int val) {
        b[pos] = (byte) (val >> 24);
        b[pos + 1] = (byte) (val >> 16);
        b[pos + 2] = (byte) (val >> 8);
        b[pos + 3] = (byte) val;
    }

    public final void digest(byte[] out) {
        digest(out, 0);
    }

    public final void digest(byte[] out, int off) {
        int idx = this.currentPos >> 2;
        this.w[idx] = ((this.w[idx] << 8) | 128) << ((3 - (this.currentPos & 3)) << 3);
        this.currentPos = (this.currentPos & -4) + 4;
        if (this.currentPos == 64) {
            this.currentPos = 0;
            perform();
        } else if (this.currentPos == 60) {
            this.currentPos = 0;
            this.w[15] = 0;
            perform();
        }
        for (int i = this.currentPos >> 2; i < 14; i++) {
            this.w[i] = 0;
        }
        this.w[14] = (int) (this.currentLen >> 32);
        this.w[15] = (int) this.currentLen;
        perform();
        putInt(out, off, this.H0);
        putInt(out, off + 4, this.H1);
        putInt(out, off + 8, this.H2);
        putInt(out, off + 12, this.H3);
        putInt(out, off + 16, this.H4);
        reset();
    }

    private final void perform() {
        for (int t = 16; t < 80; t++) {
            int x = ((this.w[t - 3] ^ this.w[t - 8]) ^ this.w[t - 14]) ^ this.w[t - 16];
            this.w[t] = (x << 1) | (x >>> 31);
        }
        int A = this.H0;
        int B = this.H1;
        int C = this.H2;
        int D = this.H3;
        int E = this.H4 + (((((A << 5) | (A >>> 27)) + ((B & C) | ((B ^ -1) & D))) + this.w[0]) + 1518500249);
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A & B) | ((A ^ -1) & C))) + this.w[1]) + 1518500249;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E & A) | ((E ^ -1) & B))) + this.w[2]) + 1518500249;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D & E) | ((D ^ -1) & A))) + this.w[3]) + 1518500249;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C & D) | ((C ^ -1) & E))) + this.w[4]) + 1518500249;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B & C) | ((B ^ -1) & D))) + this.w[5]) + 1518500249;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A & B) | ((A ^ -1) & C))) + this.w[6]) + 1518500249;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E & A) | ((E ^ -1) & B))) + this.w[7]) + 1518500249;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D & E) | ((D ^ -1) & A))) + this.w[8]) + 1518500249;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C & D) | ((C ^ -1) & E))) + this.w[9]) + 1518500249;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B & C) | ((B ^ -1) & D))) + this.w[10]) + 1518500249;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A & B) | ((A ^ -1) & C))) + this.w[11]) + 1518500249;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E & A) | ((E ^ -1) & B))) + this.w[12]) + 1518500249;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D & E) | ((D ^ -1) & A))) + this.w[13]) + 1518500249;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C & D) | ((C ^ -1) & E))) + this.w[14]) + 1518500249;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B & C) | ((B ^ -1) & D))) + this.w[15]) + 1518500249;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A & B) | ((A ^ -1) & C))) + this.w[16]) + 1518500249;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E & A) | ((E ^ -1) & B))) + this.w[17]) + 1518500249;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D & E) | ((D ^ -1) & A))) + this.w[18]) + 1518500249;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C & D) | ((C ^ -1) & E))) + this.w[19]) + 1518500249;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[20]) + 1859775393;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[21]) + 1859775393;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[22]) + 1859775393;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[23]) + 1859775393;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[24]) + 1859775393;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[25]) + 1859775393;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[26]) + 1859775393;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[27]) + 1859775393;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[28]) + 1859775393;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[29]) + 1859775393;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[30]) + 1859775393;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[31]) + 1859775393;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[32]) + 1859775393;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[33]) + 1859775393;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[34]) + 1859775393;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[35]) + 1859775393;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[36]) + 1859775393;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[37]) + 1859775393;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[38]) + 1859775393;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[39]) + 1859775393;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + (((B & C) | (B & D)) | (C & D))) + this.w[40]) - 1894007588;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + (((A & B) | (A & C)) | (B & C))) + this.w[41]) - 1894007588;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + (((E & A) | (E & B)) | (A & B))) + this.w[42]) - 1894007588;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + (((D & E) | (D & A)) | (E & A))) + this.w[43]) - 1894007588;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + (((C & D) | (C & E)) | (D & E))) + this.w[44]) - 1894007588;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + (((B & C) | (B & D)) | (C & D))) + this.w[45]) - 1894007588;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + (((A & B) | (A & C)) | (B & C))) + this.w[46]) - 1894007588;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + (((E & A) | (E & B)) | (A & B))) + this.w[47]) - 1894007588;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + (((D & E) | (D & A)) | (E & A))) + this.w[48]) - 1894007588;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + (((C & D) | (C & E)) | (D & E))) + this.w[49]) - 1894007588;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + (((B & C) | (B & D)) | (C & D))) + this.w[50]) - 1894007588;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + (((A & B) | (A & C)) | (B & C))) + this.w[51]) - 1894007588;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + (((E & A) | (E & B)) | (A & B))) + this.w[52]) - 1894007588;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + (((D & E) | (D & A)) | (E & A))) + this.w[53]) - 1894007588;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + (((C & D) | (C & E)) | (D & E))) + this.w[54]) - 1894007588;
        C = (C << 30) | (C >>> 2);
        E = (((((A << 5) | (A >>> 27)) + E) + (((B & C) | (B & D)) | (C & D))) + this.w[55]) - 1894007588;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + (((A & B) | (A & C)) | (B & C))) + this.w[56]) - 1894007588;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + (((E & A) | (E & B)) | (A & B))) + this.w[57]) - 1894007588;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + (((D & E) | (D & A)) | (E & A))) + this.w[58]) - 1894007588;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + (((C & D) | (C & E)) | (D & E))) + this.w[59]) - 1894007588;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[60]) - 899497514;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[61]) - 899497514;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[62]) - 899497514;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[63]) - 899497514;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[64]) - 899497514;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[65]) - 899497514;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[66]) - 899497514;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[67]) - 899497514;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[68]) - 899497514;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[69]) - 899497514;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[70]) - 899497514;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[71]) - 899497514;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[72]) - 899497514;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[73]) - 899497514;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[74]) - 899497514;
        C = (C << 30) | (C >>> 2);
        E += ((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + this.w[75]) - 899497514;
        B = (B << 30) | (B >>> 2);
        D += ((((E << 5) | (E >>> 27)) + ((A ^ B) ^ C)) + this.w[76]) - 899497514;
        A = (A << 30) | (A >>> 2);
        C += ((((D << 5) | (D >>> 27)) + ((E ^ A) ^ B)) + this.w[77]) - 899497514;
        E = (E << 30) | (E >>> 2);
        B += ((((C << 5) | (C >>> 27)) + ((D ^ E) ^ A)) + this.w[78]) - 899497514;
        D = (D << 30) | (D >>> 2);
        A += ((((B << 5) | (B >>> 27)) + ((C ^ D) ^ E)) + this.w[79]) - 899497514;
        C = (C << 30) | (C >>> 2);
        this.H0 += A;
        this.H1 += B;
        this.H2 += C;
        this.H3 += D;
        this.H4 += E;
    }

    private static final String toHexString(byte[] b) {
        String hexChar = "0123456789ABCDEF";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append("0123456789ABCDEF".charAt((b[i] >> 4) & 15));
            sb.append("0123456789ABCDEF".charAt(b[i] & 15));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SHA1 sha = new SHA1();
        byte[] dig1 = new byte[20];
        byte[] dig2 = new byte[20];
        byte[] dig3 = new byte[20];
        sha.update("abc".getBytes());
        sha.digest(dig1);
        sha.update("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes());
        sha.digest(dig2);
        for (int i = 0; i < 1000000; i++) {
            sha.update((byte) 97);
        }
        sha.digest(dig3);
        String dig1_res = toHexString(dig1);
        String dig2_res = toHexString(dig2);
        String dig3_res = toHexString(dig3);
        String dig2_ref = "84983E441C3BD26EBAAE4AA1F95129E5E54670F1";
        String dig3_ref = "34AA973CD4C4DAA4F61EEB2BDBAD27316534016F";
        if (dig1_res.equals("A9993E364706816ABA3E25717850C26C9CD0D89D")) {
            System.out.println("SHA-1 Test 1 OK.");
        } else {
            System.out.println("SHA-1 Test 1 FAILED.");
        }
        if (dig2_res.equals(dig2_ref)) {
            System.out.println("SHA-1 Test 2 OK.");
        } else {
            System.out.println("SHA-1 Test 2 FAILED.");
        }
        if (dig3_res.equals(dig3_ref)) {
            System.out.println("SHA-1 Test 3 OK.");
        } else {
            System.out.println("SHA-1 Test 3 FAILED.");
        }
        if (dig3_res.equals(dig3_ref)) {
            System.out.println("SHA-1 Test 3 OK.");
        } else {
            System.out.println("SHA-1 Test 3 FAILED.");
        }
    }
}
