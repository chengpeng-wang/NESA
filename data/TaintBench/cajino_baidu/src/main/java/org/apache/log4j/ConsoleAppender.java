package org.apache.log4j;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.helpers.LogLog;

public class ConsoleAppender extends WriterAppender {
    public static final String SYSTEM_ERR = "System.err";
    public static final String SYSTEM_OUT = "System.out";
    private boolean follow;
    protected String target;

    private static class SystemErrStream extends OutputStream {
        public void close() {
        }

        public void flush() {
            System.err.flush();
        }

        public void write(byte[] b) throws IOException {
            System.err.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            System.err.write(b, off, len);
        }

        public void write(int b) throws IOException {
            System.err.write(b);
        }
    }

    private static class SystemOutStream extends OutputStream {
        public void close() {
        }

        public void flush() {
            System.out.flush();
        }

        public void write(byte[] b) throws IOException {
            System.out.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            System.out.write(b, off, len);
        }

        public void write(int b) throws IOException {
            System.out.write(b);
        }
    }

    public ConsoleAppender() {
        this.target = SYSTEM_OUT;
        this.follow = false;
    }

    public ConsoleAppender(Layout layout) {
        this(layout, SYSTEM_OUT);
    }

    public ConsoleAppender(Layout layout, String target) {
        this.target = SYSTEM_OUT;
        this.follow = false;
        setLayout(layout);
        setTarget(target);
        activateOptions();
    }

    public void setTarget(String value) {
        String v = value.trim();
        if (SYSTEM_OUT.equalsIgnoreCase(v)) {
            this.target = SYSTEM_OUT;
        } else if (SYSTEM_ERR.equalsIgnoreCase(v)) {
            this.target = SYSTEM_ERR;
        } else {
            targetWarn(value);
        }
    }

    public String getTarget() {
        return this.target;
    }

    public final void setFollow(boolean newValue) {
        this.follow = newValue;
    }

    public final boolean getFollow() {
        return this.follow;
    }

    /* access modifiers changed from: 0000 */
    public void targetWarn(String val) {
        LogLog.warn(new StringBuffer().append("[").append(val).append("] should be System.out or System.err.").toString());
        LogLog.warn("Using previously set target, System.out by default.");
    }

    public void activateOptions() {
        if (this.follow) {
            if (this.target.equals(SYSTEM_ERR)) {
                setWriter(createWriter(new SystemErrStream()));
            } else {
                setWriter(createWriter(new SystemOutStream()));
            }
        } else if (this.target.equals(SYSTEM_ERR)) {
            setWriter(createWriter(System.err));
        } else {
            setWriter(createWriter(System.out));
        }
        super.activateOptions();
    }

    /* access modifiers changed from: protected|final */
    public final void closeWriter() {
        if (this.follow) {
            super.closeWriter();
        }
    }
}
