package com.trilead.ssh2.packets;

import com.trilead.ssh2.util.Tokenizer;
import java.io.IOException;
import java.math.BigInteger;

public class TypesReader {
    byte[] arr;
    int max = 0;
    int pos = 0;

    public TypesReader(byte[] arr) {
        this.arr = arr;
        this.pos = 0;
        this.max = arr.length;
    }

    public TypesReader(byte[] arr, int off) {
        this.arr = arr;
        this.pos = off;
        this.max = arr.length;
        if (this.pos < 0 || this.pos > arr.length) {
            throw new IllegalArgumentException("Illegal offset.");
        }
    }

    public TypesReader(byte[] arr, int off, int len) {
        this.arr = arr;
        this.pos = off;
        this.max = off + len;
        if (this.pos < 0 || this.pos > arr.length) {
            throw new IllegalArgumentException("Illegal offset.");
        } else if (this.max < 0 || this.max > arr.length) {
            throw new IllegalArgumentException("Illegal length.");
        }
    }

    public int readByte() throws IOException {
        if (this.pos >= this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    public byte[] readBytes(int len) throws IOException {
        if (this.pos + len > this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] res = new byte[len];
        System.arraycopy(this.arr, this.pos, res, 0, len);
        this.pos += len;
        return res;
    }

    public void readBytes(byte[] dst, int off, int len) throws IOException {
        if (this.pos + len > this.max) {
            throw new IOException("Packet too short.");
        }
        System.arraycopy(this.arr, this.pos, dst, off, len);
        this.pos += len;
    }

    public boolean readBoolean() throws IOException {
        if (this.pos >= this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] != (byte) 0;
    }

    public int readUINT32() throws IOException {
        if (this.pos + 4 > this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        int i = this.pos;
        this.pos = i + 1;
        int i2 = (bArr[i] & 255) << 24;
        byte[] bArr2 = this.arr;
        int i3 = this.pos;
        this.pos = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 16;
        bArr2 = this.arr;
        i3 = this.pos;
        this.pos = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.arr;
        i3 = this.pos;
        this.pos = i3 + 1;
        return i2 | (bArr2[i3] & 255);
    }

    public long readUINT64() throws IOException {
        if (this.pos + 8 > this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        int i = this.pos;
        this.pos = i + 1;
        int i2 = (bArr[i] & 255) << 24;
        byte[] bArr2 = this.arr;
        int i3 = this.pos;
        this.pos = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 16;
        bArr2 = this.arr;
        i3 = this.pos;
        this.pos = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.arr;
        i3 = this.pos;
        this.pos = i3 + 1;
        long high = (long) (i2 | (bArr2[i3] & 255));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        i2 = (bArr[i] & 255) << 24;
        bArr2 = this.arr;
        i3 = this.pos;
        this.pos = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 16;
        bArr2 = this.arr;
        i3 = this.pos;
        this.pos = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.arr;
        i3 = this.pos;
        this.pos = i3 + 1;
        return (high << 32) | (4294967295L & ((long) (i2 | (bArr2[i3] & 255))));
    }

    public BigInteger readMPINT() throws IOException {
        byte[] raw = readByteString();
        if (raw.length == 0) {
            return BigInteger.ZERO;
        }
        return new BigInteger(raw);
    }

    public byte[] readByteString() throws IOException {
        int len = readUINT32();
        if (this.pos + len > this.max) {
            throw new IOException("Malformed SSH byte string.");
        }
        byte[] res = new byte[len];
        System.arraycopy(this.arr, this.pos, res, 0, len);
        this.pos += len;
        return res;
    }

    public String readString(String charsetName) throws IOException {
        int len = readUINT32();
        if (this.pos + len > this.max) {
            throw new IOException("Malformed SSH string.");
        }
        String res = charsetName == null ? new String(this.arr, this.pos, len) : new String(this.arr, this.pos, len, charsetName);
        this.pos += len;
        return res;
    }

    public String readString() throws IOException {
        int len = readUINT32();
        if (this.pos + len > this.max) {
            throw new IOException("Malformed SSH string.");
        }
        String res = new String(this.arr, this.pos, len, "ISO-8859-1");
        this.pos += len;
        return res;
    }

    public String[] readNameList() throws IOException {
        return Tokenizer.parseTokens(readString(), ',');
    }

    public int remain() {
        return this.max - this.pos;
    }
}
