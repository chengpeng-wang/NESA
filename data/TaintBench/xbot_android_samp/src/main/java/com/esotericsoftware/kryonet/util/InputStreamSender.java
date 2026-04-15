package com.esotericsoftware.kryonet.util;

import com.esotericsoftware.kryonet.KryoNetException;
import java.io.IOException;
import java.io.InputStream;

public abstract class InputStreamSender extends TcpIdleSender {
    private final byte[] chunk;
    private final InputStream input;

    public abstract Object next(byte[] bArr);

    public InputStreamSender(InputStream inputStream, int i) {
        this.input = inputStream;
        this.chunk = new byte[i];
    }

    /* access modifiers changed from: protected|final */
    public final Object next() {
        int i = 0;
        while (i < this.chunk.length) {
            try {
                int read = this.input.read(this.chunk, i, this.chunk.length - i);
                if (read >= 0) {
                    i += read;
                } else if (i == 0) {
                    return null;
                } else {
                    byte[] bArr = new byte[i];
                    System.arraycopy(this.chunk, 0, bArr, 0, i);
                    return next(bArr);
                }
            } catch (IOException e) {
                throw new KryoNetException(e);
            }
        }
        return next(this.chunk);
    }
}
