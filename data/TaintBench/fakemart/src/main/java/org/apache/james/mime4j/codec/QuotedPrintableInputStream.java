package org.apache.james.mime4j.codec;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuotedPrintableInputStream extends InputStream {
    private static Log log = LogFactory.getLog(QuotedPrintableInputStream.class);
    ByteQueue byteq = new ByteQueue();
    private boolean closed = false;
    ByteQueue pushbackq = new ByteQueue();
    private byte state = (byte) 0;
    private InputStream stream;

    public QuotedPrintableInputStream(InputStream stream) {
        this.stream = stream;
    }

    public void close() throws IOException {
        this.closed = true;
    }

    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("QuotedPrintableInputStream has been closed");
        }
        fillBuffer();
        if (this.byteq.count() == 0) {
            return -1;
        }
        int val = this.byteq.dequeue();
        return val < 0 ? val & 255 : val;
    }

    private void populatePushbackQueue() throws IOException {
        if (this.pushbackq.count() == 0) {
            while (true) {
                int i = this.stream.read();
                switch (i) {
                    case -1:
                        this.pushbackq.clear();
                        return;
                    case 9:
                    case 32:
                        this.pushbackq.enqueue((byte) i);
                    case 10:
                    case 13:
                        this.pushbackq.clear();
                        this.pushbackq.enqueue((byte) i);
                        return;
                    default:
                        this.pushbackq.enqueue((byte) i);
                        return;
                }
            }
        }
    }

    private void fillBuffer() throws IOException {
        byte msdChar = (byte) 0;
        while (this.byteq.count() == 0) {
            if (this.pushbackq.count() == 0) {
                populatePushbackQueue();
                if (this.pushbackq.count() == 0) {
                    return;
                }
            }
            byte b = this.pushbackq.dequeue();
            switch (this.state) {
                case (byte) 0:
                    if (b == (byte) 61) {
                        this.state = (byte) 1;
                        break;
                    } else {
                        this.byteq.enqueue(b);
                        break;
                    }
                case (byte) 1:
                    if (b != (byte) 13) {
                        if ((b < (byte) 48 || b > (byte) 57) && ((b < (byte) 65 || b > (byte) 70) && (b < (byte) 97 || b > (byte) 102))) {
                            if (b != (byte) 61) {
                                if (log.isWarnEnabled()) {
                                    log.warn("Malformed MIME; expected \\r or [0-9A-Z], got " + b);
                                }
                                this.state = (byte) 0;
                                this.byteq.enqueue((byte) 61);
                                this.byteq.enqueue(b);
                                break;
                            }
                            if (log.isWarnEnabled()) {
                                log.warn("Malformed MIME; got ==");
                            }
                            this.byteq.enqueue((byte) 61);
                            break;
                        }
                        this.state = (byte) 3;
                        msdChar = b;
                        break;
                    }
                    this.state = (byte) 2;
                    break;
                    break;
                case (byte) 2:
                    if (b != (byte) 10) {
                        if (log.isWarnEnabled()) {
                            log.warn("Malformed MIME; expected 10, got " + b);
                        }
                        this.state = (byte) 0;
                        this.byteq.enqueue((byte) 61);
                        this.byteq.enqueue((byte) 13);
                        this.byteq.enqueue(b);
                        break;
                    }
                    this.state = (byte) 0;
                    break;
                case (byte) 3:
                    if ((b >= (byte) 48 && b <= (byte) 57) || ((b >= (byte) 65 && b <= (byte) 70) || (b >= (byte) 97 && b <= (byte) 102))) {
                        byte msd = asciiCharToNumericValue(msdChar);
                        byte low = asciiCharToNumericValue(b);
                        this.state = (byte) 0;
                        this.byteq.enqueue((byte) ((msd << 4) | low));
                        break;
                    }
                    if (log.isWarnEnabled()) {
                        log.warn("Malformed MIME; expected [0-9A-Z], got " + b);
                    }
                    this.state = (byte) 0;
                    this.byteq.enqueue((byte) 61);
                    this.byteq.enqueue(msdChar);
                    this.byteq.enqueue(b);
                    break;
                default:
                    log.error("Illegal state: " + this.state);
                    this.state = (byte) 0;
                    this.byteq.enqueue(b);
                    break;
            }
        }
    }

    private byte asciiCharToNumericValue(byte c) {
        if (c >= (byte) 48 && c <= (byte) 57) {
            return (byte) (c - 48);
        }
        if (c >= (byte) 65 && c <= (byte) 90) {
            return (byte) ((c - 65) + 10);
        }
        if (c >= (byte) 97 && c <= (byte) 122) {
            return (byte) ((c - 97) + 10);
        }
        throw new IllegalArgumentException(((char) c) + " is not a hexadecimal digit");
    }
}
