package com.trilead.ssh2.channel;

import java.io.IOException;
import java.io.OutputStream;

public final class ChannelOutputStream extends OutputStream {
    Channel c;
    boolean isClosed = false;

    ChannelOutputStream(Channel c) {
        this.c = c;
    }

    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }

    public void close() throws IOException {
        if (!this.isClosed) {
            this.isClosed = true;
            this.c.cm.sendEOF(this.c);
        }
    }

    public void flush() throws IOException {
        if (this.isClosed) {
            throw new IOException("This OutputStream is closed.");
        }
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (this.isClosed) {
            throw new IOException("This OutputStream is closed.");
        } else if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || off + len > b.length || off + len < 0 || off > b.length) {
            throw new IndexOutOfBoundsException();
        } else if (len != 0) {
            this.c.cm.sendData(this.c, b, off, len);
        }
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }
}
