package com.trilead.ssh2.crypto.digest;

public interface Digest {
    void digest(byte[] bArr);

    void digest(byte[] bArr, int i);

    int getDigestLength();

    void reset();

    void update(byte b);

    void update(byte[] bArr);

    void update(byte[] bArr, int i, int i2);
}
