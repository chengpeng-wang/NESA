package com.trilead.ssh2.crypto.cipher;

public class CTRMode implements BlockCipher {
    byte[] X;
    byte[] Xenc;
    BlockCipher bc;
    int blockSize;
    int count = 0;
    boolean doEncrypt;

    public void init(boolean forEncryption, byte[] key) {
    }

    public CTRMode(BlockCipher tc, byte[] iv, boolean doEnc) throws IllegalArgumentException {
        this.bc = tc;
        this.blockSize = this.bc.getBlockSize();
        this.doEncrypt = doEnc;
        if (this.blockSize != iv.length) {
            throw new IllegalArgumentException("IV must be " + this.blockSize + " bytes long! (currently " + iv.length + ")");
        }
        this.X = new byte[this.blockSize];
        this.Xenc = new byte[this.blockSize];
        System.arraycopy(iv, 0, this.X, 0, this.blockSize);
    }

    public final int getBlockSize() {
        return this.blockSize;
    }

    public final void transformBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        int i;
        this.bc.transformBlock(this.X, 0, this.Xenc, 0);
        for (i = 0; i < this.blockSize; i++) {
            dst[dstoff + i] = (byte) (src[srcoff + i] ^ this.Xenc[i]);
        }
        i = this.blockSize - 1;
        while (i >= 0) {
            byte[] bArr = this.X;
            bArr[i] = (byte) (bArr[i] + 1);
            if (this.X[i] == (byte) 0) {
                i--;
            } else {
                return;
            }
        }
    }
}
