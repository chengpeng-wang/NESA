package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class BufferedLineReaderInputStream extends LineReaderInputStream {
    private byte[] buffer;
    private int buflen;
    private int bufpos;
    private final int maxLineLen;
    private boolean truncated;

    public BufferedLineReaderInputStream(InputStream instream, int buffersize, int maxLineLen) {
        super(instream);
        if (instream == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        } else if (buffersize <= 0) {
            throw new IllegalArgumentException("Buffer size may not be negative or zero");
        } else {
            this.buffer = new byte[buffersize];
            this.bufpos = 0;
            this.buflen = 0;
            this.maxLineLen = maxLineLen;
            this.truncated = false;
        }
    }

    public BufferedLineReaderInputStream(InputStream instream, int buffersize) {
        this(instream, buffersize, -1);
    }

    private void expand(int newlen) {
        byte[] newbuffer = new byte[newlen];
        int len = this.buflen - this.bufpos;
        if (len > 0) {
            System.arraycopy(this.buffer, this.bufpos, newbuffer, this.bufpos, len);
        }
        this.buffer = newbuffer;
    }

    public void ensureCapacity(int len) {
        if (len > this.buffer.length) {
            expand(len);
        }
    }

    public int fillBuffer() throws IOException {
        if (this.bufpos > 0) {
            int len = this.buflen - this.bufpos;
            if (len > 0) {
                System.arraycopy(this.buffer, this.bufpos, this.buffer, 0, len);
            }
            this.bufpos = 0;
            this.buflen = len;
        }
        int off = this.buflen;
        int l = this.in.read(this.buffer, off, this.buffer.length - off);
        if (l == -1) {
            return -1;
        }
        this.buflen = off + l;
        return l;
    }

    public boolean hasBufferedData() {
        return this.bufpos < this.buflen;
    }

    public void truncate() {
        clear();
        this.truncated = true;
    }

    public int read() throws IOException {
        if (this.truncated) {
            return -1;
        }
        while (!hasBufferedData()) {
            if (fillBuffer() == -1) {
                return -1;
            }
        }
        byte[] bArr = this.buffer;
        int i = this.bufpos;
        this.bufpos = i + 1;
        return bArr[i] & 255;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.truncated) {
            return -1;
        }
        if (b == null) {
            return 0;
        }
        while (!hasBufferedData()) {
            if (fillBuffer() == -1) {
                return -1;
            }
        }
        int chunk = this.buflen - this.bufpos;
        if (chunk > len) {
            chunk = len;
        }
        System.arraycopy(this.buffer, this.bufpos, b, off, chunk);
        this.bufpos += chunk;
        return chunk;
    }

    public int read(byte[] b) throws IOException {
        if (this.truncated) {
            return -1;
        }
        if (b != null) {
            return read(b, 0, b.length);
        }
        return 0;
    }

    public boolean markSupported() {
        return false;
    }

    public int readLine(ByteArrayBuffer dst) throws IOException {
        if (dst == null) {
            throw new IllegalArgumentException("Buffer may not be null");
        } else if (this.truncated) {
            return -1;
        } else {
            int total = 0;
            boolean found = false;
            int bytesRead = 0;
            while (!found) {
                int chunk;
                if (!hasBufferedData()) {
                    bytesRead = fillBuffer();
                    if (bytesRead == -1) {
                        break;
                    }
                }
                int i = indexOf((byte) 10);
                if (i != -1) {
                    found = true;
                    chunk = (i + 1) - pos();
                } else {
                    chunk = length();
                }
                if (chunk > 0) {
                    dst.append(buf(), pos(), chunk);
                    skip(chunk);
                    total += chunk;
                }
                if (this.maxLineLen > 0 && dst.length() >= this.maxLineLen) {
                    throw new MaxLineLimitException("Maximum line length limit exceeded");
                }
            }
            if (total == 0 && bytesRead == -1) {
                return -1;
            }
            return total;
        }
    }

    public int indexOf(byte[] pattern, int off, int len) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern may not be null");
        } else if (off < this.bufpos || len < 0 || off + len > this.buflen) {
            throw new IndexOutOfBoundsException();
        } else if (len < pattern.length) {
            return -1;
        } else {
            int i;
            int[] shiftTable = new int[256];
            for (i = 0; i < shiftTable.length; i++) {
                shiftTable[i] = pattern.length + 1;
            }
            for (i = 0; i < pattern.length; i++) {
                shiftTable[pattern[i] & 255] = pattern.length - i;
            }
            int j = 0;
            while (j <= len - pattern.length) {
                int cur = off + j;
                boolean match = true;
                for (i = 0; i < pattern.length; i++) {
                    if (this.buffer[cur + i] != pattern[i]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return cur;
                }
                int pos = cur + pattern.length;
                if (pos >= this.buffer.length) {
                    break;
                }
                j += shiftTable[this.buffer[pos] & 255];
            }
            return -1;
        }
    }

    public int indexOf(byte[] pattern) {
        return indexOf(pattern, this.bufpos, this.buflen - this.bufpos);
    }

    public int indexOf(byte b, int off, int len) {
        if (off < this.bufpos || len < 0 || off + len > this.buflen) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = off; i < off + len; i++) {
            if (this.buffer[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(byte b) {
        return indexOf(b, this.bufpos, this.buflen - this.bufpos);
    }

    public byte charAt(int pos) {
        if (pos >= this.bufpos && pos <= this.buflen) {
            return this.buffer[pos];
        }
        throw new IndexOutOfBoundsException();
    }

    public byte[] buf() {
        return this.buffer;
    }

    public int pos() {
        return this.bufpos;
    }

    public int limit() {
        return this.buflen;
    }

    public int length() {
        return this.buflen - this.bufpos;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public int skip(int n) {
        int chunk = Math.min(n, this.buflen - this.bufpos);
        this.bufpos += chunk;
        return chunk;
    }

    public void clear() {
        this.bufpos = 0;
        this.buflen = 0;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[pos: ");
        buffer.append(this.bufpos);
        buffer.append("]");
        buffer.append("[limit: ");
        buffer.append(this.buflen);
        buffer.append("]");
        buffer.append("[");
        for (int i = this.bufpos; i < this.buflen; i++) {
            buffer.append((char) this.buffer[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }
}
