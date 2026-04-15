package com.sun.mail.util;

import android.support.v4.view.MotionEventCompat;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TraceOutputStream extends FilterOutputStream {
    private boolean quote = false;
    private boolean trace = false;
    private OutputStream traceOut;

    public TraceOutputStream(OutputStream out, OutputStream traceOut) {
        super(out);
        this.traceOut = traceOut;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public void setQuote(boolean quote) {
        this.quote = quote;
    }

    public void write(int b) throws IOException {
        if (this.trace) {
            if (this.quote) {
                writeByte(b);
            } else {
                this.traceOut.write(b);
            }
        }
        this.out.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (this.trace) {
            if (this.quote) {
                for (int i = 0; i < len; i++) {
                    writeByte(b[off + i]);
                }
            } else {
                this.traceOut.write(b, off, len);
            }
        }
        this.out.write(b, off, len);
    }

    private final void writeByte(int b) throws IOException {
        b &= MotionEventCompat.ACTION_MASK;
        if (b > 127) {
            this.traceOut.write(77);
            this.traceOut.write(45);
            b &= 127;
        }
        if (b == 13) {
            this.traceOut.write(92);
            this.traceOut.write(114);
        } else if (b == 10) {
            this.traceOut.write(92);
            this.traceOut.write(110);
            this.traceOut.write(10);
        } else if (b == 9) {
            this.traceOut.write(92);
            this.traceOut.write(116);
        } else if (b < 32) {
            this.traceOut.write(94);
            this.traceOut.write(b + 64);
        } else {
            this.traceOut.write(b);
        }
    }
}
