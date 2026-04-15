package com.trilead.ssh2.packets;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class TypesWriter {
    byte[] arr = new byte[256];
    int pos = 0;

    private void resize(int len) {
        byte[] new_arr = new byte[len];
        System.arraycopy(this.arr, 0, new_arr, 0, this.arr.length);
        this.arr = new_arr;
    }

    public int length() {
        return this.pos;
    }

    public byte[] getBytes() {
        byte[] dst = new byte[this.pos];
        System.arraycopy(this.arr, 0, dst, 0, this.pos);
        return dst;
    }

    public void getBytes(byte[] dst) {
        System.arraycopy(this.arr, 0, dst, 0, this.pos);
    }

    public void writeUINT32(int val, int off) {
        if (off + 4 > this.arr.length) {
            resize(off + 32);
        }
        int i = off + 1;
        this.arr[off] = (byte) (val >> 24);
        off = i + 1;
        this.arr[i] = (byte) (val >> 16);
        i = off + 1;
        this.arr[off] = (byte) (val >> 8);
        off = i + 1;
        this.arr[i] = (byte) val;
    }

    public void writeUINT32(int val) {
        writeUINT32(val, this.pos);
        this.pos += 4;
    }

    public void writeUINT64(long val) {
        if (this.pos + 8 > this.arr.length) {
            resize(this.arr.length + 32);
        }
        byte[] bArr = this.arr;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val >> 56));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val >> 48));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val >> 40));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val >> 32));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val >> 24));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val >> 16));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val >> 8));
        bArr = this.arr;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) val);
    }

    public void writeBoolean(boolean v) {
        if (this.pos + 1 > this.arr.length) {
            resize(this.arr.length + 32);
        }
        byte[] bArr = this.arr;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = v ? (byte) 1 : (byte) 0;
    }

    public void writeByte(int v, int off) {
        if (off + 1 > this.arr.length) {
            resize(off + 32);
        }
        this.arr[off] = (byte) v;
    }

    public void writeByte(int v) {
        writeByte(v, this.pos);
        this.pos++;
    }

    public void writeMPInt(BigInteger b) {
        byte[] raw = b.toByteArray();
        if (raw.length == 1 && raw[0] == (byte) 0) {
            writeUINT32(0);
        } else {
            writeString(raw, 0, raw.length);
        }
    }

    public void writeBytes(byte[] buff) {
        writeBytes(buff, 0, buff.length);
    }

    public void writeBytes(byte[] buff, int off, int len) {
        if (this.pos + len > this.arr.length) {
            resize((this.arr.length + len) + 32);
        }
        System.arraycopy(buff, off, this.arr, this.pos, len);
        this.pos += len;
    }

    public void writeString(byte[] buff, int off, int len) {
        writeUINT32(len);
        writeBytes(buff, off, len);
    }

    public void writeString(String v) {
        byte[] b;
        try {
            b = v.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            b = v.getBytes();
        }
        writeUINT32(b.length);
        writeBytes(b, 0, b.length);
    }

    public void writeString(String v, String charsetName) throws UnsupportedEncodingException {
        byte[] b = charsetName == null ? v.getBytes() : v.getBytes(charsetName);
        writeUINT32(b.length);
        writeBytes(b, 0, b.length);
    }

    public void writeNameList(String[] v) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < v.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(v[i]);
        }
        writeString(sb.toString());
    }
}
