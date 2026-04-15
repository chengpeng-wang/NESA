package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.IOException;
import java.io.InputStream;
import org.mozilla.classfile.ByteCode;
import org.objectweb.asm.Opcodes;

public class Input extends InputStream {
    private byte[] buffer;
    private int capacity;
    private char[] chars;
    private InputStream inputStream;
    private int limit;
    private int position;
    private int total;

    public Input() {
        this.chars = new char[32];
    }

    public Input(int i) {
        this.chars = new char[32];
        this.capacity = i;
        this.buffer = new byte[i];
    }

    public Input(byte[] bArr) {
        this.chars = new char[32];
        setBuffer(bArr, 0, bArr.length);
    }

    public Input(byte[] bArr, int i, int i2) {
        this.chars = new char[32];
        setBuffer(bArr, i, i2);
    }

    public Input(InputStream inputStream) {
        this((int) Opcodes.ACC_SYNTHETIC);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        }
        this.inputStream = inputStream;
    }

    public Input(InputStream inputStream, int i) {
        this(i);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        }
        this.inputStream = inputStream;
    }

    public void setBuffer(byte[] bArr) {
        setBuffer(bArr, 0, bArr.length);
    }

    public void setBuffer(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        this.buffer = bArr;
        this.position = i;
        this.limit = i2;
        this.capacity = bArr.length;
        this.total = 0;
        this.inputStream = null;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.limit = 0;
        rewind();
    }

    public int total() {
        return this.total + this.position;
    }

    public void setTotal(int i) {
        this.total = i;
    }

    public int position() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public int limit() {
        return this.limit;
    }

    public void setLimit(int i) {
        this.limit = i;
    }

    public void rewind() {
        this.position = 0;
        this.total = 0;
    }

    public void skip(int i) throws KryoException {
        int min = Math.min(this.limit - this.position, i);
        while (true) {
            this.position += min;
            i -= min;
            if (i != 0) {
                min = Math.min(i, this.capacity);
                require(min);
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public int fill(byte[] bArr, int i, int i2) throws KryoException {
        if (this.inputStream == null) {
            return -1;
        }
        try {
            return this.inputStream.read(bArr, i, i2);
        } catch (IOException e) {
            throw new KryoException(e);
        }
    }

    private int require(int i) throws KryoException {
        int i2 = this.limit - this.position;
        if (i2 < i) {
            if (i > this.capacity) {
                throw new KryoException("Buffer too small: capacity: " + this.capacity + ", required: " + i);
            }
            System.arraycopy(this.buffer, this.position, this.buffer, 0, i2);
            this.total += this.position;
            this.position = 0;
            do {
                int fill = fill(this.buffer, i2, this.capacity - i2);
                if (fill != -1) {
                    i2 += fill;
                } else if (i2 >= i) {
                    this.limit = i2;
                } else {
                    throw new KryoException("Buffer underflow.");
                }
                this.limit = i2;
            } while (i2 < i);
            this.limit = i2;
        }
        return i2;
    }

    private int optional(int i) throws KryoException {
        int i2 = this.limit - this.position;
        if (i2 >= i) {
            return i;
        }
        int min = Math.min(i, this.capacity);
        System.arraycopy(this.buffer, this.position, this.buffer, 0, i2);
        this.total += this.position;
        this.position = 0;
        do {
            int fill = fill(this.buffer, i2, this.capacity - i2);
            if (fill == -1) {
                break;
            }
            i2 += fill;
        } while (i2 < min);
        this.limit = i2;
        return i2 == 0 ? -1 : Math.min(i2, min);
    }

    public int read() throws KryoException {
        if (optional(1) == 0) {
            return -1;
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & ByteCode.IMPDEP2;
    }

    public int read(byte[] bArr) throws KryoException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws KryoException {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        int min = Math.min(this.limit - this.position, i2);
        int i3 = i2;
        do {
            System.arraycopy(this.buffer, this.position, bArr, i, min);
            this.position += min;
            i3 -= min;
            if (i3 == 0) {
                break;
            }
            i += min;
            min = optional(i3);
            if (min == -1) {
                if (i2 == i3) {
                    return -1;
                }
            }
        } while (this.position != this.limit);
        return i2 - i3;
    }

    public long skip(long j) throws KryoException {
        long j2 = j;
        while (j2 > 0) {
            int max = Math.max(Integer.MAX_VALUE, (int) j2);
            skip(max);
            j2 -= (long) max;
        }
        return j;
    }

    public void close() throws KryoException {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public byte readByte() throws KryoException {
        require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i];
    }

    public int readByteUnsigned() throws KryoException {
        require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & ByteCode.IMPDEP2;
    }

    public byte[] readBytes(int i) throws KryoException {
        byte[] bArr = new byte[i];
        readBytes(bArr, 0, i);
        return bArr;
    }

    public void readBytes(byte[] bArr) throws KryoException {
        readBytes(bArr, 0, bArr.length);
    }

    public void readBytes(byte[] bArr, int i, int i2) throws KryoException {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        int min = Math.min(this.limit - this.position, i2);
        while (true) {
            System.arraycopy(this.buffer, this.position, bArr, i, min);
            this.position += min;
            i2 -= min;
            if (i2 != 0) {
                i += min;
                min = Math.min(i2, this.capacity);
                require(min);
            } else {
                return;
            }
        }
    }

    public int readInt() throws KryoException {
        require(4);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 4;
        return (bArr[i + 3] & ByteCode.IMPDEP2) | ((((bArr[i] & ByteCode.IMPDEP2) << 24) | ((bArr[i + 1] & ByteCode.IMPDEP2) << 16)) | ((bArr[i + 2] & ByteCode.IMPDEP2) << 8));
    }

    public int readInt(boolean z) throws KryoException {
        if (require(1) < 5) {
            return readInt_slow(z);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        byte b = bArr[i];
        int i2 = b & 127;
        if ((b & 128) != 0) {
            byte[] bArr2 = this.buffer;
            int i3 = this.position;
            this.position = i3 + 1;
            byte b2 = bArr2[i3];
            i2 |= (b2 & 127) << 7;
            if ((b2 & 128) != 0) {
                i3 = this.position;
                this.position = i3 + 1;
                b2 = bArr2[i3];
                i2 |= (b2 & 127) << 14;
                if ((b2 & 128) != 0) {
                    i3 = this.position;
                    this.position = i3 + 1;
                    b2 = bArr2[i3];
                    i2 |= (b2 & 127) << 21;
                    if ((b2 & 128) != 0) {
                        i3 = this.position;
                        this.position = i3 + 1;
                        i2 |= (bArr2[i3] & 127) << 28;
                    }
                }
            }
        }
        if (z) {
            return i2;
        }
        return (-(i2 & 1)) ^ (i2 >>> 1);
    }

    private int readInt_slow(boolean z) {
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        byte b = bArr[i];
        int i2 = b & 127;
        if ((b & 128) != 0) {
            require(1);
            byte[] bArr2 = this.buffer;
            int i3 = this.position;
            this.position = i3 + 1;
            byte b2 = bArr2[i3];
            i2 |= (b2 & 127) << 7;
            if ((b2 & 128) != 0) {
                require(1);
                i3 = this.position;
                this.position = i3 + 1;
                b2 = bArr2[i3];
                i2 |= (b2 & 127) << 14;
                if ((b2 & 128) != 0) {
                    require(1);
                    i3 = this.position;
                    this.position = i3 + 1;
                    b2 = bArr2[i3];
                    i2 |= (b2 & 127) << 21;
                    if ((b2 & 128) != 0) {
                        require(1);
                        i3 = this.position;
                        this.position = i3 + 1;
                        i2 |= (bArr2[i3] & 127) << 28;
                    }
                }
            }
        }
        if (z) {
            return i2;
        }
        return (-(i2 & 1)) ^ (i2 >>> 1);
    }

    public boolean canReadInt() throws KryoException {
        if (this.limit - this.position >= 5) {
            return true;
        }
        if (optional(5) <= 0) {
            return false;
        }
        int i = this.position;
        int i2 = i + 1;
        if ((this.buffer[i] & 128) == 0) {
            return true;
        }
        if (i2 == this.limit) {
            return false;
        }
        int i3 = i2 + 1;
        if ((this.buffer[i2] & 128) == 0) {
            return true;
        }
        if (i3 == this.limit) {
            return false;
        }
        i2 = i3 + 1;
        if ((this.buffer[i3] & 128) == 0) {
            return true;
        }
        if (i2 == this.limit) {
            return false;
        }
        i3 = i2 + 1;
        if ((this.buffer[i2] & 128) == 0 || i3 != this.limit) {
            return true;
        }
        return false;
    }

    public boolean canReadLong() throws KryoException {
        if (this.limit - this.position >= 9) {
            return true;
        }
        if (optional(5) <= 0) {
            return false;
        }
        int i = this.position;
        int i2 = i + 1;
        if ((this.buffer[i] & 128) == 0) {
            return true;
        }
        if (i2 == this.limit) {
            return false;
        }
        int i3 = i2 + 1;
        if ((this.buffer[i2] & 128) == 0) {
            return true;
        }
        if (i3 == this.limit) {
            return false;
        }
        i2 = i3 + 1;
        if ((this.buffer[i3] & 128) == 0) {
            return true;
        }
        if (i2 == this.limit) {
            return false;
        }
        i3 = i2 + 1;
        if ((this.buffer[i2] & 128) == 0) {
            return true;
        }
        if (i3 == this.limit) {
            return false;
        }
        i2 = i3 + 1;
        if ((this.buffer[i3] & 128) == 0) {
            return true;
        }
        if (i2 == this.limit) {
            return false;
        }
        i3 = i2 + 1;
        if ((this.buffer[i2] & 128) == 0) {
            return true;
        }
        if (i3 == this.limit) {
            return false;
        }
        i2 = i3 + 1;
        if ((this.buffer[i3] & 128) == 0) {
            return true;
        }
        if (i2 == this.limit) {
            return false;
        }
        i3 = i2 + 1;
        if ((this.buffer[i2] & 128) == 0 || i3 != this.limit) {
            return true;
        }
        return false;
    }

    public String readString() {
        int require = require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        byte b = bArr[i];
        if ((b & 128) == 0) {
            return readAscii();
        }
        require = require >= 5 ? readUtf8Length(b) : readUtf8Length_slow(b);
        switch (require) {
            case 0:
                return null;
            case 1:
                return "";
            default:
                int i2 = require - 1;
                if (this.chars.length < i2) {
                    this.chars = new char[i2];
                }
                readUtf8(i2);
                return new String(this.chars, 0, i2);
        }
    }

    private int readUtf8Length(int i) {
        int i2 = i & 63;
        if ((i & 64) == 0) {
            return i2;
        }
        byte[] bArr = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        byte b = bArr[i3];
        i2 |= (b & 127) << 6;
        if ((b & 128) == 0) {
            return i2;
        }
        i3 = this.position;
        this.position = i3 + 1;
        b = bArr[i3];
        i2 |= (b & 127) << 13;
        if ((b & 128) == 0) {
            return i2;
        }
        i3 = this.position;
        this.position = i3 + 1;
        b = bArr[i3];
        i2 |= (b & 127) << 20;
        if ((b & 128) == 0) {
            return i2;
        }
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | ((bArr[i3] & 127) << 27);
    }

    private int readUtf8Length_slow(int i) {
        int i2 = i & 63;
        if ((i & 64) == 0) {
            return i2;
        }
        require(1);
        byte[] bArr = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        byte b = bArr[i3];
        i2 |= (b & 127) << 6;
        if ((b & 128) == 0) {
            return i2;
        }
        require(1);
        i3 = this.position;
        this.position = i3 + 1;
        b = bArr[i3];
        i2 |= (b & 127) << 13;
        if ((b & 128) == 0) {
            return i2;
        }
        require(1);
        i3 = this.position;
        this.position = i3 + 1;
        b = bArr[i3];
        i2 |= (b & 127) << 20;
        if ((b & 128) == 0) {
            return i2;
        }
        require(1);
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | ((bArr[i3] & 127) << 27);
    }

    private void readUtf8(int i) {
        byte[] bArr = this.buffer;
        char[] cArr = this.chars;
        int min = Math.min(require(1), i);
        int i2 = this.position;
        int i3 = 0;
        while (i3 < min) {
            int i4 = i2 + 1;
            byte b = bArr[i2];
            if (b < (byte) 0) {
                i2 = i4 - 1;
                break;
            }
            i2 = i3 + 1;
            cArr[i3] = (char) b;
            i3 = i2;
            i2 = i4;
        }
        this.position = i2;
        if (i3 < i) {
            readUtf8_slow(i, i3);
        }
    }

    private void readUtf8_slow(int i, int i2) {
        char[] cArr = this.chars;
        byte[] bArr = this.buffer;
        while (i2 < i) {
            if (this.position == this.limit) {
                require(1);
            }
            int i3 = this.position;
            this.position = i3 + 1;
            i3 = bArr[i3] & ByteCode.IMPDEP2;
            int i4;
            switch (i3 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    cArr[i2] = (char) i3;
                    break;
                case 12:
                case 13:
                    if (this.position == this.limit) {
                        require(1);
                    }
                    i3 = (i3 & 31) << 6;
                    i4 = this.position;
                    this.position = i4 + 1;
                    cArr[i2] = (char) (i3 | (bArr[i4] & 63));
                    break;
                case 14:
                    require(2);
                    i3 = (i3 & 15) << 12;
                    i4 = this.position;
                    this.position = i4 + 1;
                    i3 |= (bArr[i4] & 63) << 6;
                    i4 = this.position;
                    this.position = i4 + 1;
                    cArr[i2] = (char) (i3 | (bArr[i4] & 63));
                    break;
                default:
                    break;
            }
            i2++;
        }
    }

    private String readAscii() {
        byte[] bArr = this.buffer;
        int i = this.position;
        int i2 = i - 1;
        int i3 = this.limit;
        while (i != i3) {
            int i4 = i + 1;
            if ((bArr[i] & 128) != 0) {
                i = i4 - 1;
                bArr[i] = (byte) (bArr[i] & 127);
                String str = new String(bArr, 0, i2, i4 - i2);
                i2 = i4 - 1;
                bArr[i2] = (byte) (bArr[i2] | 128);
                this.position = i4;
                return str;
            }
            i = i4;
        }
        return readAscii_slow();
    }

    private String readAscii_slow() {
        this.position--;
        int i = this.limit - this.position;
        if (i > this.chars.length) {
            this.chars = new char[(i * 2)];
        }
        Object obj = this.chars;
        byte[] bArr = this.buffer;
        int i2 = this.position;
        int i3 = this.limit;
        int i4 = i2;
        i2 = 0;
        while (i4 < i3) {
            obj[i2] = (char) bArr[i4];
            i4++;
            i2++;
        }
        this.position = this.limit;
        while (true) {
            require(1);
            i2 = this.position;
            this.position = i2 + 1;
            byte b = bArr[i2];
            if (i == obj.length) {
                Object obj2 = new char[(i * 2)];
                System.arraycopy(obj, 0, obj2, 0, i);
                this.chars = obj2;
                obj = obj2;
            }
            if ((b & 128) == 128) {
                i2 = i + 1;
                obj[i] = (char) (b & 127);
                return new String(obj, 0, i2);
            }
            i2 = i + 1;
            obj[i] = (char) b;
            i = i2;
        }
    }

    public StringBuilder readStringBuilder() {
        int require = require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        byte b = bArr[i];
        if ((b & 128) == 0) {
            return new StringBuilder(readAscii());
        }
        require = require >= 5 ? readUtf8Length(b) : readUtf8Length_slow(b);
        switch (require) {
            case 0:
                return null;
            case 1:
                return new StringBuilder("");
            default:
                int i2 = require - 1;
                if (this.chars.length < i2) {
                    this.chars = new char[i2];
                }
                readUtf8(i2);
                StringBuilder stringBuilder = new StringBuilder(i2);
                stringBuilder.append(this.chars, 0, i2);
                return stringBuilder;
        }
    }

    public float readFloat() throws KryoException {
        return Float.intBitsToFloat(readInt());
    }

    public float readFloat(float f, boolean z) throws KryoException {
        return ((float) readInt(z)) / f;
    }

    public short readShort() throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & ByteCode.IMPDEP2) << 8;
        byte[] bArr2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        return (short) (i2 | (bArr2[i3] & ByteCode.IMPDEP2));
    }

    public int readShortUnsigned() throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & ByteCode.IMPDEP2) << 8;
        byte[] bArr2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        return i2 | (bArr2[i3] & ByteCode.IMPDEP2);
    }

    public long readLong() throws KryoException {
        require(8);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        long j = ((long) bArr[i]) << 56;
        int i2 = this.position;
        this.position = i2 + 1;
        j |= ((long) (bArr[i2] & ByteCode.IMPDEP2)) << 48;
        i2 = this.position;
        this.position = i2 + 1;
        j |= ((long) (bArr[i2] & ByteCode.IMPDEP2)) << 40;
        i2 = this.position;
        this.position = i2 + 1;
        j |= ((long) (bArr[i2] & ByteCode.IMPDEP2)) << 32;
        i2 = this.position;
        this.position = i2 + 1;
        j |= ((long) (bArr[i2] & ByteCode.IMPDEP2)) << 24;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (long) ((bArr[i2] & ByteCode.IMPDEP2) << 16);
        i2 = this.position;
        this.position = i2 + 1;
        j |= (long) ((bArr[i2] & ByteCode.IMPDEP2) << 8);
        i2 = this.position;
        this.position = i2 + 1;
        return j | ((long) (bArr[i2] & ByteCode.IMPDEP2));
    }

    public long readLong(boolean z) throws KryoException {
        if (require(1) < 9) {
            return readLong_slow(z);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        byte b = bArr[i];
        long j = (long) (b & 127);
        if ((b & 128) != 0) {
            byte[] bArr2 = this.buffer;
            int i2 = this.position;
            this.position = i2 + 1;
            byte b2 = bArr2[i2];
            j |= (long) ((b2 & 127) << 7);
            if ((b2 & 128) != 0) {
                i2 = this.position;
                this.position = i2 + 1;
                b2 = bArr2[i2];
                j |= (long) ((b2 & 127) << 14);
                if ((b2 & 128) != 0) {
                    i2 = this.position;
                    this.position = i2 + 1;
                    b2 = bArr2[i2];
                    j |= (long) ((b2 & 127) << 21);
                    if ((b2 & 128) != 0) {
                        i2 = this.position;
                        this.position = i2 + 1;
                        b2 = bArr2[i2];
                        j |= ((long) (b2 & 127)) << 28;
                        if ((b2 & 128) != 0) {
                            i2 = this.position;
                            this.position = i2 + 1;
                            b2 = bArr2[i2];
                            j |= ((long) (b2 & 127)) << 35;
                            if ((b2 & 128) != 0) {
                                i2 = this.position;
                                this.position = i2 + 1;
                                b2 = bArr2[i2];
                                j |= ((long) (b2 & 127)) << 42;
                                if ((b2 & 128) != 0) {
                                    i2 = this.position;
                                    this.position = i2 + 1;
                                    b2 = bArr2[i2];
                                    j |= ((long) (b2 & 127)) << 49;
                                    if ((b2 & 128) != 0) {
                                        i2 = this.position;
                                        this.position = i2 + 1;
                                        j |= ((long) bArr2[i2]) << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (z) {
            return j;
        }
        return (-(j & 1)) ^ (j >>> 1);
    }

    private long readLong_slow(boolean z) {
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        byte b = bArr[i];
        long j = (long) (b & 127);
        if ((b & 128) != 0) {
            require(1);
            byte[] bArr2 = this.buffer;
            int i2 = this.position;
            this.position = i2 + 1;
            byte b2 = bArr2[i2];
            j |= (long) ((b2 & 127) << 7);
            if ((b2 & 128) != 0) {
                require(1);
                i2 = this.position;
                this.position = i2 + 1;
                b2 = bArr2[i2];
                j |= (long) ((b2 & 127) << 14);
                if ((b2 & 128) != 0) {
                    require(1);
                    i2 = this.position;
                    this.position = i2 + 1;
                    b2 = bArr2[i2];
                    j |= (long) ((b2 & 127) << 21);
                    if ((b2 & 128) != 0) {
                        require(1);
                        i2 = this.position;
                        this.position = i2 + 1;
                        b2 = bArr2[i2];
                        j |= ((long) (b2 & 127)) << 28;
                        if ((b2 & 128) != 0) {
                            require(1);
                            i2 = this.position;
                            this.position = i2 + 1;
                            b2 = bArr2[i2];
                            j |= ((long) (b2 & 127)) << 35;
                            if ((b2 & 128) != 0) {
                                require(1);
                                i2 = this.position;
                                this.position = i2 + 1;
                                b2 = bArr2[i2];
                                j |= ((long) (b2 & 127)) << 42;
                                if ((b2 & 128) != 0) {
                                    require(1);
                                    i2 = this.position;
                                    this.position = i2 + 1;
                                    b2 = bArr2[i2];
                                    j |= ((long) (b2 & 127)) << 49;
                                    if ((b2 & 128) != 0) {
                                        require(1);
                                        i2 = this.position;
                                        this.position = i2 + 1;
                                        j |= ((long) bArr2[i2]) << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (z) {
            return j;
        }
        return (-(j & 1)) ^ (j >>> 1);
    }

    public boolean readBoolean() throws KryoException {
        require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        if (bArr[i] == (byte) 1) {
            return true;
        }
        return false;
    }

    public char readChar() throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & ByteCode.IMPDEP2) << 8;
        byte[] bArr2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        return (char) (i2 | (bArr2[i3] & ByteCode.IMPDEP2));
    }

    public double readDouble() throws KryoException {
        return Double.longBitsToDouble(readLong());
    }

    public double readDouble(double d, boolean z) throws KryoException {
        return ((double) readLong(z)) / d;
    }
}
