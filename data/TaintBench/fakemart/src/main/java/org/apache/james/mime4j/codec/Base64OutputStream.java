package org.apache.james.mime4j.codec;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class Base64OutputStream extends FilterOutputStream {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64OutputStream.class.desiredAssertionStatus() ? true : $assertionsDisabled);
    private static final Set<Byte> BASE64_CHARS = new HashSet();
    private static final byte BASE64_PAD = (byte) 61;
    static final byte[] BASE64_TABLE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
    private static final byte[] CRLF_SEPARATOR = new byte[]{(byte) 13, (byte) 10};
    private static final int DEFAULT_LINE_LENGTH = 76;
    private static final int ENCODED_BUFFER_SIZE = 2048;
    private static final int MASK_6BITS = 63;
    private boolean closed;
    private int data;
    private final byte[] encoded;
    private final int lineLength;
    private int linePosition;
    private final byte[] lineSeparator;
    private int modulus;
    private int position;
    private final byte[] singleByte;

    static {
        for (byte b : BASE64_TABLE) {
            BASE64_CHARS.add(Byte.valueOf(b));
        }
        BASE64_CHARS.add(Byte.valueOf(BASE64_PAD));
    }

    public Base64OutputStream(OutputStream out) {
        this(out, DEFAULT_LINE_LENGTH, CRLF_SEPARATOR);
    }

    public Base64OutputStream(OutputStream out, int lineLength) {
        this(out, lineLength, CRLF_SEPARATOR);
    }

    public Base64OutputStream(OutputStream out, int lineLength, byte[] lineSeparator) {
        super(out);
        this.singleByte = new byte[1];
        this.closed = $assertionsDisabled;
        this.position = 0;
        this.data = 0;
        this.modulus = 0;
        this.linePosition = 0;
        if (out == null) {
            throw new IllegalArgumentException();
        } else if (lineLength < 0) {
            throw new IllegalArgumentException();
        } else {
            checkLineSeparator(lineSeparator);
            this.lineLength = lineLength;
            this.lineSeparator = new byte[lineSeparator.length];
            System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
            this.encoded = new byte[ENCODED_BUFFER_SIZE];
        }
    }

    public final void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        }
        this.singleByte[0] = (byte) b;
        write0(this.singleByte, 0, 1);
    }

    public final void write(byte[] buffer) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (buffer.length != 0) {
            write0(buffer, 0, buffer.length);
        }
    }

    public final void write(byte[] buffer, int offset, int length) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (offset < 0 || length < 0 || offset + length > buffer.length) {
            throw new IndexOutOfBoundsException();
        } else if (length != 0) {
            write0(buffer, offset, offset + length);
        }
    }

    public void flush() throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        }
        flush0();
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            close0();
        }
    }

    private void write0(byte[] buffer, int from, int to) throws IOException {
        for (int i = from; i < to; i++) {
            this.data = (this.data << 8) | (buffer[i] & 255);
            int i2 = this.modulus + 1;
            this.modulus = i2;
            if (i2 == 3) {
                byte[] bArr;
                int i3;
                this.modulus = 0;
                if (this.lineLength > 0 && this.linePosition >= this.lineLength) {
                    this.linePosition = 0;
                    if (this.encoded.length - this.position < this.lineSeparator.length) {
                        flush0();
                    }
                    for (byte ls : this.lineSeparator) {
                        bArr = this.encoded;
                        i3 = this.position;
                        this.position = i3 + 1;
                        bArr[i3] = ls;
                    }
                }
                if (this.encoded.length - this.position < 4) {
                    flush0();
                }
                bArr = this.encoded;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = BASE64_TABLE[(this.data >> 18) & MASK_6BITS];
                bArr = this.encoded;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = BASE64_TABLE[(this.data >> 12) & MASK_6BITS];
                bArr = this.encoded;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = BASE64_TABLE[(this.data >> 6) & MASK_6BITS];
                bArr = this.encoded;
                i3 = this.position;
                this.position = i3 + 1;
                bArr[i3] = BASE64_TABLE[this.data & MASK_6BITS];
                this.linePosition += 4;
            }
        }
    }

    private void flush0() throws IOException {
        if (this.position > 0) {
            this.out.write(this.encoded, 0, this.position);
            this.position = 0;
        }
    }

    private void close0() throws IOException {
        if (this.modulus != 0) {
            writePad();
        }
        if (this.lineLength > 0 && this.linePosition > 0) {
            writeLineSeparator();
        }
        flush0();
    }

    private void writePad() throws IOException {
        if (this.lineLength > 0 && this.linePosition >= this.lineLength) {
            writeLineSeparator();
        }
        if (this.encoded.length - this.position < 4) {
            flush0();
        }
        byte[] bArr;
        int i;
        if (this.modulus == 1) {
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_TABLE[(this.data >> 2) & MASK_6BITS];
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_TABLE[(this.data << 4) & MASK_6BITS];
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_PAD;
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_PAD;
        } else if ($assertionsDisabled || this.modulus == 2) {
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_TABLE[(this.data >> 10) & MASK_6BITS];
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_TABLE[(this.data >> 4) & MASK_6BITS];
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_TABLE[(this.data << 2) & MASK_6BITS];
            bArr = this.encoded;
            i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_PAD;
        } else {
            throw new AssertionError();
        }
        this.linePosition += 4;
    }

    private void writeLineSeparator() throws IOException {
        this.linePosition = 0;
        if (this.encoded.length - this.position < this.lineSeparator.length) {
            flush0();
        }
        for (byte ls : this.lineSeparator) {
            byte[] bArr = this.encoded;
            int i = this.position;
            this.position = i + 1;
            bArr[i] = ls;
        }
    }

    private void checkLineSeparator(byte[] lineSeparator) {
        if (lineSeparator.length > ENCODED_BUFFER_SIZE) {
            throw new IllegalArgumentException("line separator length exceeds 2048");
        }
        for (byte b : lineSeparator) {
            if (BASE64_CHARS.contains(Byte.valueOf(b))) {
                throw new IllegalArgumentException("line separator must not contain base64 character '" + ((char) (b & 255)) + "'");
            }
        }
    }
}
