package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.Opcodes;

public class InputChunked extends Input {
    private int chunkSize = -1;

    public InputChunked() {
        super((int) Opcodes.ACC_STRICT);
    }

    public InputChunked(int i) {
        super(i);
    }

    public InputChunked(InputStream inputStream) {
        super(inputStream, Opcodes.ACC_STRICT);
    }

    public InputChunked(InputStream inputStream, int i) {
        super(inputStream, i);
    }

    public void setInputStream(InputStream inputStream) {
        super.setInputStream(inputStream);
        this.chunkSize = -1;
    }

    public void setBuffer(byte[] bArr, int i, int i2) {
        super.setBuffer(bArr, i, i2);
        this.chunkSize = -1;
    }

    public void rewind() {
        super.rewind();
        this.chunkSize = -1;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:8:0x0021, code skipped:
            if (r2.chunkSize == 0) goto L_0x001e;
     */
    public int fill(byte[] r3, int r4, int r5) throws com.esotericsoftware.kryo.KryoException {
        /*
        r2 = this;
        r0 = -1;
        r1 = r2.chunkSize;
        if (r1 != r0) goto L_0x001f;
    L_0x0005:
        r2.readChunkSize();
    L_0x0008:
        r0 = r2.chunkSize;
        r0 = java.lang.Math.min(r0, r5);
        r0 = super.fill(r3, r4, r0);
        r1 = r2.chunkSize;
        r1 = r1 - r0;
        r2.chunkSize = r1;
        r1 = r2.chunkSize;
        if (r1 != 0) goto L_0x001e;
    L_0x001b:
        r2.readChunkSize();
    L_0x001e:
        return r0;
    L_0x001f:
        r1 = r2.chunkSize;
        if (r1 != 0) goto L_0x0008;
    L_0x0023:
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryo.io.InputChunked.fill(byte[], int, int):int");
    }

    private void readChunkSize() {
        int i = 0;
        try {
            InputStream inputStream = getInputStream();
            for (int i2 = 0; i2 < 32; i2 += 7) {
                int read = inputStream.read();
                if (read == -1) {
                    throw new KryoException("Buffer underflow.");
                }
                i |= (read & 127) << i2;
                if ((read & 128) == 0) {
                    this.chunkSize = i;
                    return;
                }
            }
            throw new KryoException("Malformed integer.");
        } catch (IOException e) {
            throw new KryoException(e);
        }
    }

    public void nextChunks() {
        if (this.chunkSize == -1) {
            readChunkSize();
        }
        while (this.chunkSize > 0) {
            skip(this.chunkSize);
        }
        this.chunkSize = -1;
    }
}
