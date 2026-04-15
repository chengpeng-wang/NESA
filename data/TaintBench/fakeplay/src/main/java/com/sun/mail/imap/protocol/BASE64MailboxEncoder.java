package com.sun.mail.imap.protocol;

import android.support.v4.view.MotionEventCompat;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

public class BASE64MailboxEncoder {
    private static final char[] pem_array = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', ','};
    protected byte[] buffer = new byte[4];
    protected int bufsize = 0;
    protected Writer out = null;
    protected boolean started = false;

    public static String encode(String original) {
        BASE64MailboxEncoder base64stream = null;
        boolean changedString = false;
        CharArrayWriter writer = new CharArrayWriter(length);
        for (char current : original.toCharArray()) {
            if (current < ' ' || current > '~') {
                if (base64stream == null) {
                    base64stream = new BASE64MailboxEncoder(writer);
                    changedString = true;
                }
                base64stream.write(current);
            } else {
                if (base64stream != null) {
                    base64stream.flush();
                }
                if (current == '&') {
                    changedString = true;
                    writer.write(38);
                    writer.write(45);
                } else {
                    writer.write(current);
                }
            }
        }
        if (base64stream != null) {
            base64stream.flush();
        }
        if (changedString) {
            return writer.toString();
        }
        return original;
    }

    public BASE64MailboxEncoder(Writer what) {
        this.out = what;
    }

    public void write(int c) {
        try {
            if (!this.started) {
                this.started = true;
                this.out.write(38);
            }
            byte[] bArr = this.buffer;
            int i = this.bufsize;
            this.bufsize = i + 1;
            bArr[i] = (byte) (c >> 8);
            bArr = this.buffer;
            i = this.bufsize;
            this.bufsize = i + 1;
            bArr[i] = (byte) (c & MotionEventCompat.ACTION_MASK);
            if (this.bufsize >= 3) {
                encode();
                this.bufsize -= 3;
            }
        } catch (IOException e) {
        }
    }

    public void flush() {
        try {
            if (this.bufsize > 0) {
                encode();
                this.bufsize = 0;
            }
            if (this.started) {
                this.out.write(45);
                this.started = false;
            }
        } catch (IOException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void encode() throws IOException {
        byte a;
        byte b;
        if (this.bufsize == 1) {
            a = this.buffer[0];
            this.out.write(pem_array[(a >>> 2) & 63]);
            this.out.write(pem_array[((a << 4) & 48) + 0]);
        } else if (this.bufsize == 2) {
            a = this.buffer[0];
            b = this.buffer[1];
            this.out.write(pem_array[(a >>> 2) & 63]);
            this.out.write(pem_array[((a << 4) & 48) + ((b >>> 4) & 15)]);
            this.out.write(pem_array[((b << 2) & 60) + 0]);
        } else {
            a = this.buffer[0];
            b = this.buffer[1];
            byte c = this.buffer[2];
            this.out.write(pem_array[(a >>> 2) & 63]);
            this.out.write(pem_array[((a << 4) & 48) + ((b >>> 4) & 15)]);
            this.out.write(pem_array[((b << 2) & 60) + ((c >>> 6) & 3)]);
            this.out.write(pem_array[c & 63]);
            if (this.bufsize == 4) {
                this.buffer[0] = this.buffer[3];
            }
        }
    }
}
