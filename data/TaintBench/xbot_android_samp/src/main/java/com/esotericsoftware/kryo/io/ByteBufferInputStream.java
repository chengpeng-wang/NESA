package com.esotericsoftware.kryo.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
    private ByteBuffer byteBuffer;

    public ByteBufferInputStream(int i) {
        this(ByteBuffer.allocate(i));
        this.byteBuffer.flip();
    }

    public ByteBufferInputStream(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int read() throws IOException {
        if (this.byteBuffer.hasRemaining()) {
            return this.byteBuffer.get();
        }
        return -1;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int min = Math.min(this.byteBuffer.remaining(), i2);
        if (min == 0) {
            return -1;
        }
        this.byteBuffer.get(bArr, i, min);
        return min;
    }

    public int available() throws IOException {
        return this.byteBuffer.remaining();
    }
}
