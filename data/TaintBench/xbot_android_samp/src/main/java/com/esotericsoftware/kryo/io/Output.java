package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.IOException;
import java.io.OutputStream;
import org.objectweb.asm.Opcodes;

public class Output extends OutputStream {
    private byte[] buffer;
    private int capacity;
    private int maxCapacity;
    private OutputStream outputStream;
    private int position;
    private int total;

    public Output(int i) {
        this(i, i);
    }

    public Output(int i, int i2) {
        if (i2 < -1) {
            throw new IllegalArgumentException("maxBufferSize cannot be < -1: " + i2);
        }
        this.capacity = i;
        if (i2 == -1) {
            i2 = Integer.MAX_VALUE;
        }
        this.maxCapacity = i2;
        this.buffer = new byte[i];
    }

    public Output(byte[] bArr) {
        this(bArr, bArr.length);
    }

    public Output(byte[] bArr, int i) {
        if (bArr == null) {
            throw new IllegalArgumentException("buffer cannot be null.");
        }
        setBuffer(bArr, i);
    }

    public Output(OutputStream outputStream) {
        this((int) Opcodes.ACC_SYNTHETIC, (int) Opcodes.ACC_SYNTHETIC);
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        }
        this.outputStream = outputStream;
    }

    public Output(OutputStream outputStream, int i) {
        this(i, i);
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        }
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.position = 0;
        this.total = 0;
    }

    public void setBuffer(byte[] bArr) {
        setBuffer(bArr, bArr.length);
    }

    public void setBuffer(byte[] bArr, int i) {
        if (bArr == null) {
            throw new IllegalArgumentException("buffer cannot be null.");
        } else if (i < -1) {
            throw new IllegalArgumentException("maxBufferSize cannot be < -1: " + i);
        } else {
            this.buffer = bArr;
            if (i == -1) {
                i = Integer.MAX_VALUE;
            }
            this.maxCapacity = i;
            this.capacity = bArr.length;
            this.position = 0;
            this.total = 0;
            this.outputStream = null;
        }
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public byte[] toBytes() {
        byte[] bArr = new byte[this.position];
        System.arraycopy(this.buffer, 0, bArr, 0, this.position);
        return bArr;
    }

    public int position() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public int total() {
        return this.total + this.position;
    }

    public void clear() {
        this.position = 0;
        this.total = 0;
    }

    private boolean require(int i) throws KryoException {
        if (this.capacity - this.position >= i) {
            return false;
        }
        if (i > this.maxCapacity) {
            throw new KryoException("Buffer overflow. Max capacity: " + this.maxCapacity + ", required: " + i);
        }
        flush();
        while (this.capacity - this.position < i) {
            if (this.capacity == this.maxCapacity) {
                throw new KryoException("Buffer overflow. Available: " + (this.capacity - this.position) + ", required: " + i);
            }
            this.capacity = Math.min(this.capacity * 2, this.maxCapacity);
            if (this.capacity < 0) {
                this.capacity = this.maxCapacity;
            }
            byte[] bArr = new byte[this.capacity];
            System.arraycopy(this.buffer, 0, bArr, 0, this.position);
            this.buffer = bArr;
        }
        return true;
    }

    public void flush() throws KryoException {
        if (this.outputStream != null) {
            try {
                this.outputStream.write(this.buffer, 0, this.position);
                this.total += this.position;
                this.position = 0;
            } catch (IOException e) {
                throw new KryoException(e);
            }
        }
    }

    public void close() throws KryoException {
        flush();
        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public void write(int i) throws KryoException {
        if (this.position == this.capacity) {
            require(1);
        }
        byte[] bArr = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) i;
    }

    public void write(byte[] bArr) throws KryoException {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        writeBytes(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws KryoException {
        writeBytes(bArr, i, i2);
    }

    public void writeByte(byte b) throws KryoException {
        if (this.position == this.capacity) {
            require(1);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = b;
    }

    public void writeByte(int i) throws KryoException {
        if (this.position == this.capacity) {
            require(1);
        }
        byte[] bArr = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) i;
    }

    public void writeBytes(byte[] bArr) throws KryoException {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        writeBytes(bArr, 0, bArr.length);
    }

    public void writeBytes(byte[] bArr, int i, int i2) throws KryoException {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        int min = Math.min(this.capacity - this.position, i2);
        while (true) {
            System.arraycopy(bArr, i, this.buffer, this.position, min);
            this.position += min;
            i2 -= min;
            if (i2 != 0) {
                i += min;
                min = Math.min(this.capacity, i2);
                require(min);
            } else {
                return;
            }
        }
    }

    public void writeInt(int i) throws KryoException {
        require(4);
        byte[] bArr = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) (i >> 24);
        i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) (i >> 16);
        i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) (i >> 8);
        i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) i;
    }

    public int writeInt(int i, boolean z) throws KryoException {
        if (!z) {
            i = (i << 1) ^ (i >> 31);
        }
        int i2;
        byte[] bArr;
        int i3;
        if ((i >>> 7) == 0) {
            require(1);
            byte[] bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) i;
            return 1;
        } else if ((i >>> 14) == 0) {
            require(2);
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i & 127) | 128);
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) (i >>> 7);
            return 2;
        } else if ((i >>> 21) == 0) {
            require(3);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i & 127) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i >>> 7) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) (i >>> 14);
            return 3;
        } else if ((i >>> 28) == 0) {
            require(4);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i & 127) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i >>> 7) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i >>> 14) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) (i >>> 21);
            return 4;
        } else {
            require(5);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i & 127) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i >>> 7) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i >>> 14) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) ((i >>> 21) | 128);
            bArr = this.buffer;
            i3 = this.position;
            this.position = i3 + 1;
            bArr[i3] = (byte) (i >>> 28);
            return 5;
        }
    }

    public void writeString(String str) throws KryoException {
        int i = 1;
        int i2 = 0;
        if (str == null) {
            writeByte(128);
            return;
        }
        int length = str.length();
        if (length == 0) {
            writeByte(129);
            return;
        }
        int i3;
        if (length <= 1 || length >= 64) {
            i = 0;
        } else {
            for (i3 = 0; i3 < length; i3++) {
                if (str.charAt(i3) > 127) {
                    i = 0;
                    break;
                }
            }
        }
        if (i != 0) {
            if (this.capacity - this.position < length) {
                writeAscii_slow(str, length);
            } else {
                str.getBytes(0, length, this.buffer, this.position);
                this.position += length;
            }
            byte[] bArr = this.buffer;
            i = this.position - 1;
            bArr[i] = (byte) (bArr[i] | 128);
            return;
        }
        writeUtf8Length(length + 1);
        if (this.capacity - this.position >= length) {
            byte[] bArr2 = this.buffer;
            i = this.position;
            while (i2 < length) {
                char charAt = str.charAt(i2);
                if (charAt > 127) {
                    break;
                }
                i3 = i + 1;
                bArr2[i] = (byte) charAt;
                i2++;
                i = i3;
            }
            this.position = i;
        }
        if (i2 < length) {
            writeString_slow(str, length, i2);
        }
    }

    public void writeString(CharSequence charSequence) throws KryoException {
        if (charSequence == null) {
            writeByte(128);
            return;
        }
        int length = charSequence.length();
        if (length == 0) {
            writeByte(129);
            return;
        }
        writeUtf8Length(length + 1);
        int i = 0;
        if (this.capacity - this.position >= length) {
            byte[] bArr = this.buffer;
            int i2 = this.position;
            while (i < length) {
                char charAt = charSequence.charAt(i);
                if (charAt > 127) {
                    break;
                }
                int i3 = i2 + 1;
                bArr[i2] = (byte) charAt;
                i++;
                i2 = i3;
            }
            this.position = i2;
        }
        if (i < length) {
            writeString_slow(charSequence, length, i);
        }
    }

    public void writeAscii(String str) throws KryoException {
        if (str == null) {
            writeByte(128);
            return;
        }
        int length = str.length();
        if (length == 0) {
            writeByte(129);
            return;
        }
        if (this.capacity - this.position < length) {
            writeAscii_slow(str, length);
        } else {
            str.getBytes(0, length, this.buffer, this.position);
            this.position = length + this.position;
        }
        byte[] bArr = this.buffer;
        int i = this.position - 1;
        bArr[i] = (byte) (bArr[i] | 128);
    }

    private void writeUtf8Length(int i) {
        byte[] bArr;
        int i2;
        if ((i >>> 6) == 0) {
            require(1);
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) (i | 128);
        } else if ((i >>> 13) == 0) {
            require(2);
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i | 64) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) (i >>> 6);
        } else if ((i >>> 20) == 0) {
            require(3);
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i | 64) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i >>> 6) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) (i >>> 13);
        } else if ((i >>> 27) == 0) {
            require(4);
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i | 64) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i >>> 6) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i >>> 13) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) (i >>> 20);
        } else {
            require(5);
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i | 64) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i >>> 6) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i >>> 13) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) ((i >>> 20) | 128);
            i2 = this.position;
            this.position = i2 + 1;
            bArr[i2] = (byte) (i >>> 27);
        }
    }

    private void writeString_slow(CharSequence charSequence, int i, int i2) {
        while (i2 < i) {
            if (this.position == this.capacity) {
                require(Math.min(this.capacity, i - i2));
            }
            char charAt = charSequence.charAt(i2);
            byte[] bArr;
            int i3;
            if (charAt <= 127) {
                bArr = this.buffer;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = (byte) charAt;
            } else if (charAt > 2047) {
                bArr = this.buffer;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = (byte) (((charAt >> 12) & 15) | 224);
                require(2);
                bArr = this.buffer;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = (byte) (((charAt >> 6) & 63) | 128);
                bArr = this.buffer;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = (byte) ((charAt & 63) | 128);
            } else {
                bArr = this.buffer;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = (byte) (((charAt >> 6) & 31) | 192);
                require(1);
                bArr = this.buffer;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = (byte) ((charAt & 63) | 128);
            }
            i2++;
        }
    }

    private void writeAscii_slow(String str, int i) throws KryoException {
        byte[] bArr = this.buffer;
        int i2 = 0;
        int min = Math.min(i, this.capacity - this.position);
        while (i2 < i) {
            str.getBytes(i2, i2 + min, bArr, this.position);
            i2 += min;
            this.position = min + this.position;
            min = Math.min(i - i2, this.capacity);
            if (require(min)) {
                bArr = this.buffer;
            }
        }
    }

    public void writeFloat(float f) throws KryoException {
        writeInt(Float.floatToIntBits(f));
    }

    public int writeFloat(float f, float f2, boolean z) throws KryoException {
        return writeInt((int) (f * f2), z);
    }

    public void writeShort(int i) throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        bArr = this.buffer;
        i2 = this.position;
        this.position = i2 + 1;
        bArr[i2] = (byte) i;
    }

    public void writeLong(long j) throws KryoException {
        require(8);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) (j >>> 56));
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) (j >>> 48));
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) (j >>> 40));
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) (j >>> 32));
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) (j >>> 24));
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) (j >>> 16));
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) (j >>> 8));
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) ((int) j);
    }

    public int writeLong(long j, boolean z) throws KryoException {
        if (!z) {
            j = (j << 1) ^ (j >> 63);
        }
        byte[] bArr;
        int i;
        byte[] bArr2;
        int i2;
        if ((j >>> 7) == 0) {
            require(1);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) j);
            return 1;
        } else if ((j >>> 14) == 0) {
            require(2);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) (j >>> 7));
            return 2;
        } else if ((j >>> 21) == 0) {
            require(3);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (j >>> 14));
            return 3;
        } else if ((j >>> 28) == 0) {
            require(4);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (j >>> 21));
            return 4;
        } else if ((j >>> 35) == 0) {
            require(5);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (j >>> 28));
            return 5;
        } else if ((j >>> 42) == 0) {
            require(6);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 28) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (j >>> 35));
            return 6;
        } else if ((j >>> 49) == 0) {
            require(7);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 7) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 14) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 21) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 28) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 35) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) (j >>> 42));
            return 7;
        } else if ((j >>> 56) == 0) {
            require(8);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 28) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 35) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 42) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (j >>> 49));
            return 8;
        } else {
            require(9);
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j & 127) | 128));
            bArr2 = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = (byte) ((int) ((j >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 28) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 35) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 42) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((j >>> 49) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (j >>> 56));
            return 9;
        }
    }

    public void writeBoolean(boolean z) throws KryoException {
        int i = 1;
        require(1);
        byte[] bArr = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        if (!z) {
            i = 0;
        }
        bArr[i2] = (byte) i;
    }

    public void writeChar(char c) throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) (c >>> 8);
        bArr = this.buffer;
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) c;
    }

    public void writeDouble(double d) throws KryoException {
        writeLong(Double.doubleToLongBits(d));
    }

    public int writeDouble(double d, double d2, boolean z) throws KryoException {
        return writeLong((long) (d * d2), z);
    }

    public static int intLength(int i, boolean z) {
        if (!z) {
            i = (i << 1) ^ (i >> 31);
        }
        if ((i >>> 7) == 0) {
            return 1;
        }
        if ((i >>> 14) == 0) {
            return 2;
        }
        if ((i >>> 21) == 0) {
            return 3;
        }
        if ((i >>> 28) == 0) {
            return 4;
        }
        return 5;
    }

    public static int longLength(long j, boolean z) {
        if (!z) {
            j = (j << 1) ^ (j >> 63);
        }
        if ((j >>> 7) == 0) {
            return 1;
        }
        if ((j >>> 14) == 0) {
            return 2;
        }
        if ((j >>> 21) == 0) {
            return 3;
        }
        if ((j >>> 28) == 0) {
            return 4;
        }
        if ((j >>> 35) == 0) {
            return 5;
        }
        if ((j >>> 42) == 0) {
            return 6;
        }
        if ((j >>> 49) == 0) {
            return 7;
        }
        if ((j >>> 56) == 0) {
            return 8;
        }
        return 9;
    }
}
