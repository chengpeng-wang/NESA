package com.trilead.ssh2.crypto;

import java.io.IOException;
import java.math.BigInteger;

public class SimpleDERReader {
    byte[] buffer;
    int count;
    int pos;

    public SimpleDERReader(byte[] b) {
        resetInput(b);
    }

    public SimpleDERReader(byte[] b, int off, int len) {
        resetInput(b, off, len);
    }

    public void resetInput(byte[] b) {
        resetInput(b, 0, b.length);
    }

    public void resetInput(byte[] b, int off, int len) {
        this.buffer = b;
        this.pos = off;
        this.count = len;
    }

    private byte readByte() throws IOException {
        if (this.count <= 0) {
            throw new IOException("DER byte array: out of data");
        }
        this.count--;
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i];
    }

    private byte[] readBytes(int len) throws IOException {
        if (len > this.count) {
            throw new IOException("DER byte array: out of data");
        }
        byte[] b = new byte[len];
        System.arraycopy(this.buffer, this.pos, b, 0, len);
        this.pos += len;
        this.count -= len;
        return b;
    }

    public int available() {
        return this.count;
    }

    private int readLength() throws IOException {
        int len = readByte() & 255;
        if ((len & 128) == 0) {
            return len;
        }
        int remain = len & 127;
        if (remain == 0) {
            return -1;
        }
        len = 0;
        while (remain > 0) {
            len = (len << 8) | (readByte() & 255);
            remain--;
        }
        return len;
    }

    public int ignoreNextObject() throws IOException {
        int type = readByte() & 255;
        int len = readLength();
        if (len < 0 || len > available()) {
            throw new IOException("Illegal len in DER object (" + len + ")");
        }
        readBytes(len);
        return type;
    }

    public BigInteger readInt() throws IOException {
        int type = readByte() & 255;
        if (type != 2) {
            throw new IOException("Expected DER Integer, but found type " + type);
        }
        int len = readLength();
        if (len >= 0 && len <= available()) {
            return new BigInteger(readBytes(len));
        }
        throw new IOException("Illegal len in DER object (" + len + ")");
    }

    public byte[] readSequenceAsByteArray() throws IOException {
        int type = readByte() & 255;
        if (type != 48) {
            throw new IOException("Expected DER Sequence, but found type " + type);
        }
        int len = readLength();
        if (len >= 0 && len <= available()) {
            return readBytes(len);
        }
        throw new IOException("Illegal len in DER object (" + len + ")");
    }

    public byte[] readOctetString() throws IOException {
        int type = readByte() & 255;
        if (type != 4) {
            throw new IOException("Expected DER Octetstring, but found type " + type);
        }
        int len = readLength();
        if (len >= 0 && len <= available()) {
            return readBytes(len);
        }
        throw new IOException("Illegal len in DER object (" + len + ")");
    }
}
