package com.trilead.ssh2.crypto.cipher;

public class DESede extends DES {
    private boolean encrypt;
    private int[] key1 = null;
    private int[] key2 = null;
    private int[] key3 = null;

    public void init(boolean encrypting, byte[] key) {
        boolean z = false;
        this.key1 = generateWorkingKey(encrypting, key, 0);
        if (!encrypting) {
            z = true;
        }
        this.key2 = generateWorkingKey(z, key, 8);
        this.key3 = generateWorkingKey(encrypting, key, 16);
        this.encrypt = encrypting;
    }

    public String getAlgorithmName() {
        return "DESede";
    }

    public int getBlockSize() {
        return 8;
    }

    public void transformBlock(byte[] in, int inOff, byte[] out, int outOff) {
        if (this.key1 == null) {
            throw new IllegalStateException("DESede engine not initialised!");
        } else if (this.encrypt) {
            desFunc(this.key1, in, inOff, out, outOff);
            desFunc(this.key2, out, outOff, out, outOff);
            desFunc(this.key3, out, outOff, out, outOff);
        } else {
            desFunc(this.key3, in, inOff, out, outOff);
            desFunc(this.key2, out, outOff, out, outOff);
            desFunc(this.key1, out, outOff, out, outOff);
        }
    }

    public void reset() {
    }
}
