package org.apache.http.util;

import android.support.v4.view.MotionEventCompat;
import java.io.Serializable;
import org.apache.http.protocol.HTTP;

public final class CharArrayBuffer implements Serializable {
    private static final long serialVersionUID = -6208952725094867135L;
    private char[] buffer;
    private int len;

    public CharArrayBuffer(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Buffer capacity may not be negative");
        }
        this.buffer = new char[capacity];
    }

    private void expand(int newlen) {
        char[] newbuffer = new char[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }

    public void append(char[] b, int off, int len) {
        if (b != null) {
            if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length) {
                throw new IndexOutOfBoundsException(new StringBuffer().append("off: ").append(off).append(" len: ").append(len).append(" b.length: ").append(b.length).toString());
            } else if (len != 0) {
                int newlen = this.len + len;
                if (newlen > this.buffer.length) {
                    expand(newlen);
                }
                System.arraycopy(b, off, this.buffer, this.len, len);
                this.len = newlen;
            }
        }
    }

    public void append(String str) {
        if (str == null) {
            str = "null";
        }
        int strlen = str.length();
        int newlen = this.len + strlen;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        str.getChars(0, strlen, this.buffer, this.len);
        this.len = newlen;
    }

    public void append(CharArrayBuffer b, int off, int len) {
        if (b != null) {
            append(b.buffer, off, len);
        }
    }

    public void append(CharArrayBuffer b) {
        if (b != null) {
            append(b.buffer, 0, b.len);
        }
    }

    public void append(char ch) {
        int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        this.buffer[this.len] = ch;
        this.len = newlen;
    }

    public void append(byte[] b, int off, int len) {
        if (b != null) {
            if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length) {
                throw new IndexOutOfBoundsException(new StringBuffer().append("off: ").append(off).append(" len: ").append(len).append(" b.length: ").append(b.length).toString());
            } else if (len != 0) {
                int oldlen = this.len;
                int newlen = oldlen + len;
                if (newlen > this.buffer.length) {
                    expand(newlen);
                }
                int i1 = off;
                for (int i2 = oldlen; i2 < newlen; i2++) {
                    this.buffer[i2] = (char) (b[i1] & MotionEventCompat.ACTION_MASK);
                    i1++;
                }
                this.len = newlen;
            }
        }
    }

    public void append(ByteArrayBuffer b, int off, int len) {
        if (b != null) {
            append(b.buffer(), off, len);
        }
    }

    public void append(Object obj) {
        append(String.valueOf(obj));
    }

    public void clear() {
        this.len = 0;
    }

    public char[] toCharArray() {
        char[] b = new char[this.len];
        if (this.len > 0) {
            System.arraycopy(this.buffer, 0, b, 0, this.len);
        }
        return b;
    }

    public char charAt(int i) {
        return this.buffer[i];
    }

    public char[] buffer() {
        return this.buffer;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public int length() {
        return this.len;
    }

    public void ensureCapacity(int required) {
        if (required > 0 && required > this.buffer.length - this.len) {
            expand(this.len + required);
        }
    }

    public void setLength(int len) {
        if (len < 0 || len > this.buffer.length) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("len: ").append(len).append(" < 0 or > buffer len: ").append(this.buffer.length).toString());
        }
        this.len = len;
    }

    public boolean isEmpty() {
        return this.len == 0;
    }

    public boolean isFull() {
        return this.len == this.buffer.length;
    }

    public int indexOf(int ch, int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex > this.len) {
            endIndex = this.len;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        for (int i = beginIndex; i < endIndex; i++) {
            if (this.buffer[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(int ch) {
        return indexOf(ch, 0, this.len);
    }

    public String substring(int beginIndex, int endIndex) {
        return new String(this.buffer, beginIndex, endIndex - beginIndex);
    }

    public String substringTrimmed(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("Negative beginIndex: ").append(beginIndex).toString());
        } else if (endIndex > this.len) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("endIndex: ").append(endIndex).append(" > length: ").append(this.len).toString());
        } else if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("beginIndex: ").append(beginIndex).append(" > endIndex: ").append(endIndex).toString());
        } else {
            while (beginIndex < endIndex && HTTP.isWhitespace(this.buffer[beginIndex])) {
                beginIndex++;
            }
            while (endIndex > beginIndex && HTTP.isWhitespace(this.buffer[endIndex - 1])) {
                endIndex--;
            }
            return new String(this.buffer, beginIndex, endIndex - beginIndex);
        }
    }

    public String toString() {
        return new String(this.buffer, 0, this.len);
    }
}
