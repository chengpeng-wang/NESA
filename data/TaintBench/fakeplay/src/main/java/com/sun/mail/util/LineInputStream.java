package com.sun.mail.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class LineInputStream extends FilterInputStream {
    private char[] lineBuffer = null;

    public LineInputStream(InputStream in) {
        super(in);
    }

    public String readLine() throws IOException {
        int c1;
        InputStream in = this.in;
        char[] buf = this.lineBuffer;
        if (buf == null) {
            buf = new char[128];
            this.lineBuffer = buf;
        }
        int room = buf.length;
        int offset = 0;
        while (true) {
            c1 = in.read();
            if (c1 == -1 || c1 == 10) {
                break;
            } else if (c1 == 13) {
                int c2 = in.read();
                if (c2 == 13) {
                    c2 = in.read();
                }
                if (c2 != 10) {
                    if (!(in instanceof PushbackInputStream)) {
                        InputStream in2 = new PushbackInputStream(in);
                        this.in = in2;
                        in = in2;
                    }
                    ((PushbackInputStream) in).unread(c2);
                }
            } else {
                room--;
                if (room < 0) {
                    buf = new char[(offset + 128)];
                    room = (buf.length - offset) - 1;
                    System.arraycopy(this.lineBuffer, 0, buf, 0, offset);
                    this.lineBuffer = buf;
                }
                int offset2 = offset + 1;
                buf[offset] = (char) c1;
                offset = offset2;
            }
        }
        if (c1 == -1 && offset == 0) {
            return null;
        }
        return String.copyValueOf(buf, 0, offset);
    }
}
