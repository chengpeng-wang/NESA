package com.baidu.inf.iis.bcs.http;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RepeatableInputStream extends InputStream {
    private static final Log log = LogFactory.getLog(RepeatableInputStream.class);
    private byte[] buffer = null;
    private int bufferOffset = 0;
    private int bufferSize = 0;
    private long bytesReadPastMark = 0;
    private InputStream is = null;

    public RepeatableInputStream(InputStream inputStream, int i) {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        this.is = inputStream;
        this.bufferSize = i;
        this.buffer = new byte[this.bufferSize];
        if (log.isDebugEnabled()) {
            log.debug("Underlying input stream will be repeatable up to " + this.buffer.length + " bytes");
        }
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public void close() throws IOException {
        this.is.close();
    }

    public InputStream getWrappedInputStream() {
        return this.is;
    }

    public synchronized void mark(int i) {
        if (log.isDebugEnabled()) {
            log.debug("Input stream marked at " + this.bytesReadPastMark + " bytes");
        }
        if (this.bytesReadPastMark > ((long) this.bufferSize) || this.buffer == null) {
            this.bufferOffset = 0;
            this.bytesReadPastMark = 0;
            this.buffer = new byte[this.bufferSize];
        } else {
            byte[] bArr = new byte[this.bufferSize];
            System.arraycopy(this.buffer, this.bufferOffset, bArr, 0, (int) (this.bytesReadPastMark - ((long) this.bufferOffset)));
            this.buffer = bArr;
            this.bytesReadPastMark -= (long) this.bufferOffset;
            this.bufferOffset = 0;
        }
    }

    public boolean markSupported() {
        return true;
    }

    public int read() throws IOException {
        byte[] bArr = new byte[1];
        int read = read(bArr);
        if (read != -1) {
            return bArr[0];
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read;
        byte[] bArr2 = new byte[i2];
        if (((long) this.bufferOffset) >= this.bytesReadPastMark || this.buffer == null) {
            read = this.is.read(bArr2);
            if (read > 0) {
                if (this.bytesReadPastMark + ((long) read) <= ((long) this.bufferSize)) {
                    System.arraycopy(bArr2, 0, this.buffer, (int) this.bytesReadPastMark, read);
                    this.bufferOffset += read;
                } else if (this.buffer != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Buffer size " + this.bufferSize + " has been exceeded and the input stream " + "will not be repeatable until the next mark. Freeing buffer memory");
                    }
                    this.buffer = null;
                }
                System.arraycopy(bArr2, 0, bArr, i, read);
                this.bytesReadPastMark += (long) read;
            }
        } else {
            read = bArr2.length;
            if (((long) (this.bufferOffset + read)) > this.bytesReadPastMark) {
                read = ((int) this.bytesReadPastMark) - this.bufferOffset;
            }
            System.arraycopy(this.buffer, this.bufferOffset, bArr, i, read);
            this.bufferOffset += read;
        }
        return read;
    }

    public void reset() throws IOException {
        if (this.bytesReadPastMark <= ((long) this.bufferSize)) {
            if (log.isDebugEnabled()) {
                log.debug("Reset after reading " + this.bytesReadPastMark + " bytes.");
            }
            this.bufferOffset = 0;
            return;
        }
        throw new IOException("Input stream cannot be reset as " + this.bytesReadPastMark + " bytes have been written, exceeding the available buffer size of " + this.bufferSize);
    }
}
