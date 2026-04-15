package com.trilead.ssh2.crypto.cipher;

import com.trilead.ssh2.sftp.AttribFlags;

public class DES implements BlockCipher {
    static short[] Df_Key = new short[]{(short) 1, (short) 35, (short) 69, (short) 103, (short) 137, (short) 171, (short) 205, (short) 239, (short) 254, (short) 220, (short) 186, (short) 152, (short) 118, (short) 84, (short) 50, (short) 16, (short) 137, (short) 171, (short) 205, (short) 239, (short) 1, (short) 35, (short) 69, (short) 103};
    static int[] SP1;
    static int[] SP2;
    static int[] SP3;
    static int[] SP4;
    static int[] SP5;
    static int[] SP6;
    static int[] SP7;
    static int[] SP8;
    static int[] bigbyte = new int[]{8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, AttribFlags.SSH_FILEXFER_ATTR_CTIME, AttribFlags.SSH_FILEXFER_ATTR_UNTRANSLATED_NAME, AttribFlags.SSH_FILEXFER_ATTR_LINK_COUNT, AttribFlags.SSH_FILEXFER_ATTR_MIME_TYPE, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
    static short[] bytebit = new short[]{(short) 128, (short) 64, (short) 32, (short) 16, (short) 8, (short) 4, (short) 2, (short) 1};
    static byte[] pc1;
    static byte[] pc2;
    static byte[] totrot = new byte[]{(byte) 1, (byte) 2, (byte) 4, (byte) 6, (byte) 8, (byte) 10, (byte) 12, (byte) 14, (byte) 15, (byte) 17, (byte) 19, (byte) 21, (byte) 23, (byte) 25, (byte) 27, (byte) 28};
    private int[] workingKey = null;

    public void init(boolean encrypting, byte[] key) {
        this.workingKey = generateWorkingKey(encrypting, key, 0);
    }

    public String getAlgorithmName() {
        return "DES";
    }

    public int getBlockSize() {
        return 8;
    }

    public void transformBlock(byte[] in, int inOff, byte[] out, int outOff) {
        if (this.workingKey == null) {
            throw new IllegalStateException("DES engine not initialised!");
        }
        desFunc(this.workingKey, in, inOff, out, outOff);
    }

    public void reset() {
    }

    static {
        byte[] bArr = new byte[56];
        bArr[0] = (byte) 56;
        bArr[1] = (byte) 48;
        bArr[2] = (byte) 40;
        bArr[3] = (byte) 32;
        bArr[4] = (byte) 24;
        bArr[5] = (byte) 16;
        bArr[6] = (byte) 8;
        bArr[8] = (byte) 57;
        bArr[9] = (byte) 49;
        bArr[10] = (byte) 41;
        bArr[11] = (byte) 33;
        bArr[12] = (byte) 25;
        bArr[13] = (byte) 17;
        bArr[14] = (byte) 9;
        bArr[15] = (byte) 1;
        bArr[16] = (byte) 58;
        bArr[17] = (byte) 50;
        bArr[18] = (byte) 42;
        bArr[19] = (byte) 34;
        bArr[20] = (byte) 26;
        bArr[21] = (byte) 18;
        bArr[22] = (byte) 10;
        bArr[23] = (byte) 2;
        bArr[24] = (byte) 59;
        bArr[25] = (byte) 51;
        bArr[26] = (byte) 43;
        bArr[27] = (byte) 35;
        bArr[28] = (byte) 62;
        bArr[29] = (byte) 54;
        bArr[30] = (byte) 46;
        bArr[31] = (byte) 38;
        bArr[32] = (byte) 30;
        bArr[33] = (byte) 22;
        bArr[34] = (byte) 14;
        bArr[35] = (byte) 6;
        bArr[36] = (byte) 61;
        bArr[37] = (byte) 53;
        bArr[38] = (byte) 45;
        bArr[39] = (byte) 37;
        bArr[40] = (byte) 29;
        bArr[41] = (byte) 21;
        bArr[42] = (byte) 13;
        bArr[43] = (byte) 5;
        bArr[44] = (byte) 60;
        bArr[45] = (byte) 52;
        bArr[46] = (byte) 44;
        bArr[47] = (byte) 36;
        bArr[48] = (byte) 28;
        bArr[49] = (byte) 20;
        bArr[50] = (byte) 12;
        bArr[51] = (byte) 4;
        bArr[52] = (byte) 27;
        bArr[53] = (byte) 19;
        bArr[54] = (byte) 11;
        bArr[55] = (byte) 3;
        pc1 = bArr;
        bArr = new byte[48];
        bArr[0] = (byte) 13;
        bArr[1] = (byte) 16;
        bArr[2] = (byte) 10;
        bArr[3] = (byte) 23;
        bArr[5] = (byte) 4;
        bArr[6] = (byte) 2;
        bArr[7] = (byte) 27;
        bArr[8] = (byte) 14;
        bArr[9] = (byte) 5;
        bArr[10] = (byte) 20;
        bArr[11] = (byte) 9;
        bArr[12] = (byte) 22;
        bArr[13] = (byte) 18;
        bArr[14] = (byte) 11;
        bArr[15] = (byte) 3;
        bArr[16] = (byte) 25;
        bArr[17] = (byte) 7;
        bArr[18] = (byte) 15;
        bArr[19] = (byte) 6;
        bArr[20] = (byte) 26;
        bArr[21] = (byte) 19;
        bArr[22] = (byte) 12;
        bArr[23] = (byte) 1;
        bArr[24] = (byte) 40;
        bArr[25] = (byte) 51;
        bArr[26] = (byte) 30;
        bArr[27] = (byte) 36;
        bArr[28] = (byte) 46;
        bArr[29] = (byte) 54;
        bArr[30] = (byte) 29;
        bArr[31] = (byte) 39;
        bArr[32] = (byte) 50;
        bArr[33] = (byte) 44;
        bArr[34] = (byte) 32;
        bArr[35] = (byte) 47;
        bArr[36] = (byte) 43;
        bArr[37] = (byte) 48;
        bArr[38] = (byte) 38;
        bArr[39] = (byte) 55;
        bArr[40] = (byte) 33;
        bArr[41] = (byte) 52;
        bArr[42] = (byte) 45;
        bArr[43] = (byte) 41;
        bArr[44] = (byte) 49;
        bArr[45] = (byte) 35;
        bArr[46] = (byte) 28;
        bArr[47] = (byte) 31;
        pc2 = bArr;
        r0 = new int[64];
        SP1 = r0;
        r0 = new int[64];
        SP2 = r0;
        r0 = new int[64];
        SP3 = r0;
        r0 = new int[64];
        SP4 = r0;
        r0 = new int[64];
        SP5 = r0;
        r0 = new int[64];
        SP6 = r0;
        r0 = new int[64];
        SP7 = r0;
        r0 = new int[64];
        r0[0] = 268439616;
        r0[1] = AttribFlags.SSH_FILEXFER_ATTR_MIME_TYPE;
        r0[2] = 262144;
        r0[3] = 268701760;
        r0[4] = 268435456;
        r0[5] = 268439616;
        r0[6] = 64;
        r0[7] = 268435456;
        r0[8] = 262208;
        r0[9] = 268697600;
        r0[10] = 268701760;
        r0[11] = 266240;
        r0[12] = 268701696;
        r0[13] = 266304;
        r0[14] = AttribFlags.SSH_FILEXFER_ATTR_MIME_TYPE;
        r0[15] = 64;
        r0[16] = 268697600;
        r0[17] = 268435520;
        r0[18] = 268439552;
        r0[19] = 4160;
        r0[20] = 266240;
        r0[21] = 262208;
        r0[22] = 268697664;
        r0[23] = 268701696;
        r0[24] = 4160;
        r0[27] = 268697664;
        r0[28] = 268435520;
        r0[29] = 268439552;
        r0[30] = 266304;
        r0[31] = 262144;
        r0[32] = 266304;
        r0[33] = 262144;
        r0[34] = 268701696;
        r0[35] = AttribFlags.SSH_FILEXFER_ATTR_MIME_TYPE;
        r0[36] = 64;
        r0[37] = 268697664;
        r0[38] = AttribFlags.SSH_FILEXFER_ATTR_MIME_TYPE;
        r0[39] = 266304;
        r0[40] = 268439552;
        r0[41] = 64;
        r0[42] = 268435520;
        r0[43] = 268697600;
        r0[44] = 268697664;
        r0[45] = 268435456;
        r0[46] = 262144;
        r0[47] = 268439616;
        r0[49] = 268701760;
        r0[50] = 262208;
        r0[51] = 268435520;
        r0[52] = 268697600;
        r0[53] = 268439552;
        r0[54] = 268439616;
        r0[56] = 268701760;
        r0[57] = 266240;
        r0[58] = 266240;
        r0[59] = 4160;
        r0[60] = 4160;
        r0[61] = 262208;
        r0[62] = 268435456;
        r0[63] = 268701696;
        SP8 = r0;
    }

    /* access modifiers changed from: protected */
    public int[] generateWorkingKey(boolean encrypting, byte[] key, int off) {
        int j;
        int l;
        int i;
        int[] newKey = new int[32];
        boolean[] pc1m = new boolean[56];
        boolean[] pcr = new boolean[56];
        for (j = 0; j < 56; j++) {
            l = pc1[j];
            pc1m[j] = (key[(l >>> 3) + off] & bytebit[l & 7]) != 0;
        }
        for (i = 0; i < 16; i++) {
            int m;
            if (encrypting) {
                m = i << 1;
            } else {
                m = (15 - i) << 1;
            }
            int n = m + 1;
            newKey[n] = 0;
            newKey[m] = 0;
            for (j = 0; j < 28; j++) {
                l = j + totrot[i];
                if (l < 28) {
                    pcr[j] = pc1m[l];
                } else {
                    pcr[j] = pc1m[l - 28];
                }
            }
            for (j = 28; j < 56; j++) {
                l = j + totrot[i];
                if (l < 56) {
                    pcr[j] = pc1m[l];
                } else {
                    pcr[j] = pc1m[l - 28];
                }
            }
            for (j = 0; j < 24; j++) {
                if (pcr[pc2[j]]) {
                    newKey[m] = newKey[m] | bigbyte[j];
                }
                if (pcr[pc2[j + 24]]) {
                    newKey[n] = newKey[n] | bigbyte[j];
                }
            }
        }
        for (i = 0; i != 32; i += 2) {
            int i1 = newKey[i];
            int i2 = newKey[i + 1];
            newKey[i] = ((((16515072 & i1) << 6) | ((i1 & 4032) << 10)) | ((16515072 & i2) >>> 10)) | ((i2 & 4032) >>> 6);
            newKey[i + 1] = ((((258048 & i1) << 12) | ((i1 & 63) << 16)) | ((258048 & i2) >>> 4)) | (i2 & 63);
        }
        return newKey;
    }

    /* access modifiers changed from: protected */
    public void desFunc(int[] wKey, byte[] in, int inOff, byte[] out, int outOff) {
        int left = ((((in[inOff + 0] & 255) << 24) | ((in[inOff + 1] & 255) << 16)) | ((in[inOff + 2] & 255) << 8)) | (in[inOff + 3] & 255);
        int right = ((((in[inOff + 4] & 255) << 24) | ((in[inOff + 5] & 255) << 16)) | ((in[inOff + 6] & 255) << 8)) | (in[inOff + 7] & 255);
        int work = ((left >>> 4) ^ right) & 252645135;
        right ^= work;
        left ^= work << 4;
        work = ((left >>> 16) ^ right) & 65535;
        right ^= work;
        left ^= work << 16;
        work = ((right >>> 2) ^ left) & 858993459;
        left ^= work;
        right ^= work << 2;
        work = ((right >>> 8) ^ left) & 16711935;
        left ^= work;
        right ^= work << 8;
        right = ((right << 1) | ((right >>> 31) & 1)) & -1;
        work = (left ^ right) & -1431655766;
        left ^= work;
        right ^= work;
        left = ((left << 1) | ((left >>> 31) & 1)) & -1;
        for (int round = 0; round < 8; round++) {
            work = ((right << 28) | (right >>> 4)) ^ wKey[(round * 4) + 0];
            int fval = ((SP7[work & 63] | SP5[(work >>> 8) & 63]) | SP3[(work >>> 16) & 63]) | SP1[(work >>> 24) & 63];
            work = right ^ wKey[(round * 4) + 1];
            left ^= (((fval | SP8[work & 63]) | SP6[(work >>> 8) & 63]) | SP4[(work >>> 16) & 63]) | SP2[(work >>> 24) & 63];
            work = ((left << 28) | (left >>> 4)) ^ wKey[(round * 4) + 2];
            fval = ((SP7[work & 63] | SP5[(work >>> 8) & 63]) | SP3[(work >>> 16) & 63]) | SP1[(work >>> 24) & 63];
            work = left ^ wKey[(round * 4) + 3];
            right ^= (((fval | SP8[work & 63]) | SP6[(work >>> 8) & 63]) | SP4[(work >>> 16) & 63]) | SP2[(work >>> 24) & 63];
        }
        right = (right << 31) | (right >>> 1);
        work = (left ^ right) & -1431655766;
        left ^= work;
        right ^= work;
        left = (left << 31) | (left >>> 1);
        work = ((left >>> 8) ^ right) & 16711935;
        right ^= work;
        left ^= work << 8;
        work = ((left >>> 2) ^ right) & 858993459;
        right ^= work;
        left ^= work << 2;
        work = ((right >>> 16) ^ left) & 65535;
        left ^= work;
        right ^= work << 16;
        work = ((right >>> 4) ^ left) & 252645135;
        left ^= work;
        right ^= work << 4;
        out[outOff + 0] = (byte) ((right >>> 24) & 255);
        out[outOff + 1] = (byte) ((right >>> 16) & 255);
        out[outOff + 2] = (byte) ((right >>> 8) & 255);
        out[outOff + 3] = (byte) (right & 255);
        out[outOff + 4] = (byte) ((left >>> 24) & 255);
        out[outOff + 5] = (byte) ((left >>> 16) & 255);
        out[outOff + 6] = (byte) ((left >>> 8) & 255);
        out[outOff + 7] = (byte) (left & 255);
    }
}
