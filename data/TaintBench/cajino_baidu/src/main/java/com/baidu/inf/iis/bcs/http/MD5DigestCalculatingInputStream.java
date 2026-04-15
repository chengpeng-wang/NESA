package com.baidu.inf.iis.bcs.http;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5DigestCalculatingInputStream extends FilterInputStream {
    private MessageDigest digest = MessageDigest.getInstance("MD5");

    public MD5DigestCalculatingInputStream(InputStream inputStream) throws NoSuchAlgorithmException {
        super(inputStream);
    }

    public byte[] getMd5Digest() {
        return this.digest.digest();
    }

    public int read() throws IOException {
        int read = this.in.read();
        if (read != -1) {
            this.digest.update((byte) read);
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.in.read(bArr, i, i2);
        if (read != -1) {
            this.digest.update(bArr, i, read);
        }
        return read;
    }

    public synchronized void reset() throws IOException {
        try {
            this.digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        this.in.reset();
    }
}
