package com.trilead.ssh2.crypto.cipher;

public class CBCMode implements BlockCipher {
    int blockSize;
    byte[] cbc_vector;
    boolean doEncrypt;
    BlockCipher tc;
    byte[] tmp_vector;

    public void init(boolean forEncryption, byte[] key) {
    }

    public CBCMode(BlockCipher tc, byte[] iv, boolean doEncrypt) throws IllegalArgumentException {
        this.tc = tc;
        this.blockSize = tc.getBlockSize();
        this.doEncrypt = doEncrypt;
        if (this.blockSize != iv.length) {
            throw new IllegalArgumentException("IV must be " + this.blockSize + " bytes long! (currently " + iv.length + ")");
        }
        this.cbc_vector = new byte[this.blockSize];
        this.tmp_vector = new byte[this.blockSize];
        System.arraycopy(iv, 0, this.cbc_vector, 0, this.blockSize);
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    private void encryptBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        for (int i = 0; i < this.blockSize; i++) {
            byte[] bArr = this.cbc_vector;
            bArr[i] = (byte) (bArr[i] ^ src[srcoff + i]);
        }
        this.tc.transformBlock(this.cbc_vector, 0, dst, dstoff);
        System.arraycopy(dst, dstoff, this.cbc_vector, 0, this.blockSize);
    }

    private void decryptBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        System.arraycopy(src, srcoff, this.tmp_vector, 0, this.blockSize);
        this.tc.transformBlock(src, srcoff, dst, dstoff);
        for (int i = 0; i < this.blockSize; i++) {
            int i2 = dstoff + i;
            dst[i2] = (byte) (dst[i2] ^ this.cbc_vector[i]);
        }
        byte[] swap = this.cbc_vector;
        this.cbc_vector = this.tmp_vector;
        this.tmp_vector = swap;
    }

    public void transformBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        if (this.doEncrypt) {
            encryptBlock(src, srcoff, dst, dstoff);
        } else {
            decryptBlock(src, srcoff, dst, dstoff);
        }
    }
}
