package javax.mail.internet;

import android.support.v4.view.MotionEventCompat;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

/* compiled from: MimeUtility */
class AsciiOutputStream extends OutputStream {
    private int ascii = 0;
    private boolean badEOL = false;
    private boolean breakOnNonAscii;
    private boolean checkEOL = false;
    private int lastb = 0;
    private int linelen = 0;
    private boolean longLine = false;
    private int non_ascii = 0;
    private int ret = 0;

    public AsciiOutputStream(boolean breakOnNonAscii, boolean encodeEolStrict) {
        boolean z = false;
        this.breakOnNonAscii = breakOnNonAscii;
        if (encodeEolStrict && breakOnNonAscii) {
            z = true;
        }
        this.checkEOL = z;
    }

    public void write(int b) throws IOException {
        check(b);
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        len += off;
        for (int i = off; i < len; i++) {
            check(b[i]);
        }
    }

    private final void check(int b) throws IOException {
        b &= MotionEventCompat.ACTION_MASK;
        if (this.checkEOL && ((this.lastb == 13 && b != 10) || (this.lastb != 13 && b == 10))) {
            this.badEOL = true;
        }
        if (b == 13 || b == 10) {
            this.linelen = 0;
        } else {
            this.linelen++;
            if (this.linelen > 998) {
                this.longLine = true;
            }
        }
        if (MimeUtility.nonascii(b)) {
            this.non_ascii++;
            if (this.breakOnNonAscii) {
                this.ret = 3;
                throw new EOFException();
            }
        }
        this.ascii++;
        this.lastb = b;
    }

    public int getAscii() {
        if (this.ret != 0) {
            return this.ret;
        }
        if (this.badEOL) {
            return 3;
        }
        if (this.non_ascii == 0) {
            if (this.longLine) {
                return 2;
            }
            return 1;
        } else if (this.ascii > this.non_ascii) {
            return 2;
        } else {
            return 3;
        }
    }
}
