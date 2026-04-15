package org.apache.james.mime4j.codec;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Base64InputStream extends InputStream {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64InputStream.class.desiredAssertionStatus() ? true : $assertionsDisabled);
    private static final int[] BASE64_DECODE = new int[256];
    private static final byte BASE64_PAD = (byte) 61;
    private static final int ENCODED_BUFFER_SIZE = 1536;
    private static final int EOF = -1;
    private static Log log = LogFactory.getLog(Base64InputStream.class);
    private boolean closed;
    private final byte[] encoded;
    private boolean eof;
    private final InputStream in;
    private int position;
    private final ByteQueue q;
    private final byte[] singleByte;
    private int size;
    private boolean strict;

    static {
        int i;
        for (i = 0; i < 256; i++) {
            BASE64_DECODE[i] = -1;
        }
        for (i = 0; i < Base64OutputStream.BASE64_TABLE.length; i++) {
            BASE64_DECODE[Base64OutputStream.BASE64_TABLE[i] & 255] = i;
        }
    }

    public Base64InputStream(InputStream in) {
        this(in, $assertionsDisabled);
    }

    public Base64InputStream(InputStream in, boolean strict) {
        this.singleByte = new byte[1];
        this.closed = $assertionsDisabled;
        this.encoded = new byte[ENCODED_BUFFER_SIZE];
        this.position = 0;
        this.size = 0;
        this.q = new ByteQueue();
        if (in == null) {
            throw new IllegalArgumentException();
        }
        this.in = in;
        this.strict = strict;
    }

    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        }
        int bytes;
        do {
            bytes = read0(this.singleByte, 0, 1);
            if (bytes == -1) {
                return -1;
            }
        } while (bytes != 1);
        return this.singleByte[0] & 255;
    }

    public int read(byte[] buffer) throws IOException {
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (buffer.length == 0) {
            return 0;
        } else {
            return read0(buffer, 0, buffer.length);
        }
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (offset < 0 || length < 0 || offset + length > buffer.length) {
            throw new IndexOutOfBoundsException();
        } else if (length == 0) {
            return 0;
        } else {
            return read0(buffer, offset, offset + length);
        }
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
        }
    }

    private int read0(byte[] buffer, int from, int to) throws IOException {
        int index;
        int index2 = from;
        int count = this.q.count();
        while (true) {
            int qCount = count;
            index = index2;
            count = qCount - 1;
            if (qCount > 0 && index < to) {
                index2 = index + 1;
                buffer[index] = this.q.dequeue();
            }
        }
        if (this.eof) {
            int i;
            if (index == from) {
                i = -1;
            } else {
                i = index - from;
            }
            index2 = index;
            return i;
        }
        int data = 0;
        int sextets = 0;
        index2 = index;
        while (index2 < to) {
            while (this.position == this.size) {
                int n = this.in.read(this.encoded, 0, this.encoded.length);
                if (n == -1) {
                    this.eof = true;
                    if (sextets != 0) {
                        handleUnexpectedEof(sextets);
                    }
                    return index2 == from ? -1 : index2 - from;
                } else if (n > 0) {
                    this.position = 0;
                    this.size = n;
                } else if (!($assertionsDisabled || n == 0)) {
                    throw new AssertionError();
                }
            }
            while (this.position < this.size && index2 < to) {
                byte[] bArr = this.encoded;
                int i2 = this.position;
                this.position = i2 + 1;
                int value = bArr[i2] & 255;
                if (value == 61) {
                    return decodePad(data, sextets, buffer, index2, to) - from;
                }
                int decoded = BASE64_DECODE[value];
                if (decoded >= 0) {
                    data = (data << 6) | decoded;
                    sextets++;
                    if (sextets == 4) {
                        sextets = 0;
                        byte b1 = (byte) (data >>> 16);
                        byte b2 = (byte) (data >>> 8);
                        byte b3 = (byte) data;
                        if (index2 < to - 2) {
                            index = index2 + 1;
                            buffer[index2] = b1;
                            index2 = index + 1;
                            buffer[index] = b2;
                            index = index2 + 1;
                            buffer[index2] = b3;
                            index2 = index;
                        } else {
                            if (index2 < to - 1) {
                                index = index2 + 1;
                                buffer[index2] = b1;
                                index2 = index + 1;
                                buffer[index] = b2;
                                this.q.enqueue(b3);
                            } else if (index2 < to) {
                                index = index2 + 1;
                                buffer[index2] = b1;
                                this.q.enqueue(b2);
                                this.q.enqueue(b3);
                                index2 = index;
                            } else {
                                this.q.enqueue(b1);
                                this.q.enqueue(b2);
                                this.q.enqueue(b3);
                            }
                            if ($assertionsDisabled || index == to) {
                                return to - from;
                            }
                            throw new AssertionError();
                        }
                    }
                    continue;
                }
            }
        }
        if (!$assertionsDisabled && sextets != 0) {
            throw new AssertionError();
        } else if ($assertionsDisabled || index2 == to) {
            return to - from;
        } else {
            throw new AssertionError();
        }
    }

    private int decodePad(int data, int sextets, byte[] buffer, int index, int end) throws IOException {
        this.eof = true;
        int index2;
        if (sextets == 2) {
            byte b = (byte) (data >>> 4);
            if (index < end) {
                index2 = index + 1;
                buffer[index] = b;
                return index2;
            }
            this.q.enqueue(b);
            return index;
        } else if (sextets == 3) {
            byte b1 = (byte) (data >>> 10);
            byte b2 = (byte) ((data >>> 2) & 255);
            if (index < end - 1) {
                index2 = index + 1;
                buffer[index] = b1;
                index = index2 + 1;
                buffer[index2] = b2;
                return index;
            } else if (index < end) {
                index2 = index + 1;
                buffer[index] = b1;
                this.q.enqueue(b2);
                return index2;
            } else {
                this.q.enqueue(b1);
                this.q.enqueue(b2);
                return index;
            }
        } else {
            handleUnexpecedPad(sextets);
            return index;
        }
    }

    private void handleUnexpectedEof(int sextets) throws IOException {
        if (this.strict) {
            throw new IOException("unexpected end of file");
        }
        log.warn("unexpected end of file; dropping " + sextets + " sextet(s)");
    }

    private void handleUnexpecedPad(int sextets) throws IOException {
        if (this.strict) {
            throw new IOException("unexpected padding character");
        }
        log.warn("unexpected padding character; dropping " + sextets + " sextet(s)");
    }
}
