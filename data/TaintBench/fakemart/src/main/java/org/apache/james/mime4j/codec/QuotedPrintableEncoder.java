package org.apache.james.mime4j.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class QuotedPrintableEncoder {
    private static final byte CR = (byte) 13;
    private static final byte EQUALS = (byte) 61;
    private static final byte[] HEX_DIGITS = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};
    private static final byte LF = (byte) 10;
    private static final byte QUOTED_PRINTABLE_LAST_PLAIN = (byte) 126;
    private static final int QUOTED_PRINTABLE_MAX_LINE_LENGTH = 76;
    private static final int QUOTED_PRINTABLE_OCTETS_PER_ESCAPE = 3;
    private static final byte SPACE = (byte) 32;
    private static final byte TAB = (byte) 9;
    private final boolean binary;
    private final byte[] inBuffer;
    private int nextSoftBreak = 77;
    private OutputStream out = null;
    private final byte[] outBuffer;
    private int outputIndex = 0;
    private boolean pendingCR;
    private boolean pendingSpace;
    private boolean pendingTab;

    public QuotedPrintableEncoder(int bufferSize, boolean binary) {
        this.inBuffer = new byte[bufferSize];
        this.outBuffer = new byte[(bufferSize * 3)];
        this.binary = binary;
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
    }

    /* access modifiers changed from: 0000 */
    public void initEncoding(OutputStream out) {
        this.out = out;
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
        this.nextSoftBreak = 77;
    }

    /* access modifiers changed from: 0000 */
    public void encodeChunk(byte[] buffer, int off, int len) throws IOException {
        for (int inputIndex = off; inputIndex < len + off; inputIndex++) {
            encode(buffer[inputIndex]);
        }
    }

    /* access modifiers changed from: 0000 */
    public void completeEncoding() throws IOException {
        writePending();
        flushOutput();
    }

    public void encode(InputStream in, OutputStream out) throws IOException {
        initEncoding(out);
        while (true) {
            int inputLength = in.read(this.inBuffer);
            if (inputLength > -1) {
                encodeChunk(this.inBuffer, 0, inputLength);
            } else {
                completeEncoding();
                return;
            }
        }
    }

    private void writePending() throws IOException {
        if (this.pendingSpace) {
            plain(SPACE);
        } else if (this.pendingTab) {
            plain(TAB);
        } else if (this.pendingCR) {
            plain(CR);
        }
        clearPending();
    }

    private void clearPending() throws IOException {
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
    }

    private void encode(byte next) throws IOException {
        if (next == LF) {
            if (this.binary) {
                writePending();
                escape(next);
            } else if (this.pendingCR) {
                if (this.pendingSpace) {
                    escape(SPACE);
                } else if (this.pendingTab) {
                    escape(TAB);
                }
                lineBreak();
                clearPending();
            } else {
                writePending();
                plain(next);
            }
        } else if (next != CR) {
            writePending();
            if (next == SPACE) {
                if (this.binary) {
                    escape(next);
                } else {
                    this.pendingSpace = true;
                }
            } else if (next == TAB) {
                if (this.binary) {
                    escape(next);
                } else {
                    this.pendingTab = true;
                }
            } else if (next < SPACE) {
                escape(next);
            } else if (next > QUOTED_PRINTABLE_LAST_PLAIN) {
                escape(next);
            } else if (next == EQUALS) {
                escape(next);
            } else {
                plain(next);
            }
        } else if (this.binary) {
            escape(next);
        } else {
            this.pendingCR = true;
        }
    }

    private void plain(byte next) throws IOException {
        int i = this.nextSoftBreak - 1;
        this.nextSoftBreak = i;
        if (i <= 1) {
            softBreak();
        }
        write(next);
    }

    private void escape(byte next) throws IOException {
        int i = this.nextSoftBreak - 1;
        this.nextSoftBreak = i;
        if (i <= 3) {
            softBreak();
        }
        int nextUnsigned = next & 255;
        write(EQUALS);
        this.nextSoftBreak--;
        write(HEX_DIGITS[nextUnsigned >> 4]);
        this.nextSoftBreak--;
        write(HEX_DIGITS[nextUnsigned % 16]);
    }

    private void write(byte next) throws IOException {
        byte[] bArr = this.outBuffer;
        int i = this.outputIndex;
        this.outputIndex = i + 1;
        bArr[i] = next;
        if (this.outputIndex >= this.outBuffer.length) {
            flushOutput();
        }
    }

    private void softBreak() throws IOException {
        write(EQUALS);
        lineBreak();
    }

    private void lineBreak() throws IOException {
        write(CR);
        write(LF);
        this.nextSoftBreak = QUOTED_PRINTABLE_MAX_LINE_LENGTH;
    }

    /* access modifiers changed from: 0000 */
    public void flushOutput() throws IOException {
        if (this.outputIndex < this.outBuffer.length) {
            this.out.write(this.outBuffer, 0, this.outputIndex);
        } else {
            this.out.write(this.outBuffer);
        }
        this.outputIndex = 0;
    }
}
