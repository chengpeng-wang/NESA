package org.apache.log4j;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.QuietWriter;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;

public class WriterAppender extends AppenderSkeleton {
    protected String encoding;
    protected boolean immediateFlush;
    protected QuietWriter qw;

    public WriterAppender() {
        this.immediateFlush = true;
    }

    public WriterAppender(Layout layout, OutputStream os) {
        this(layout, new OutputStreamWriter(os));
    }

    public WriterAppender(Layout layout, Writer writer) {
        this.immediateFlush = true;
        this.layout = layout;
        setWriter(writer);
    }

    public void setImmediateFlush(boolean value) {
        this.immediateFlush = value;
    }

    public boolean getImmediateFlush() {
        return this.immediateFlush;
    }

    public void activateOptions() {
    }

    public void append(LoggingEvent event) {
        if (checkEntryConditions()) {
            subAppend(event);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkEntryConditions() {
        if (this.closed) {
            LogLog.warn("Not allowed to write to a closed appender.");
            return false;
        } else if (this.qw == null) {
            this.errorHandler.error(new StringBuffer().append("No output stream or file set for the appender named [").append(this.name).append("].").toString());
            return false;
        } else if (this.layout != null) {
            return true;
        } else {
            this.errorHandler.error(new StringBuffer().append("No layout set for the appender named [").append(this.name).append("].").toString());
            return false;
        }
    }

    public synchronized void close() {
        if (!this.closed) {
            this.closed = true;
            writeFooter();
            reset();
        }
    }

    /* access modifiers changed from: protected */
    public void closeWriter() {
        if (this.qw != null) {
            try {
                this.qw.close();
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error(new StringBuffer().append("Could not close ").append(this.qw).toString(), e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public OutputStreamWriter createWriter(OutputStream os) {
        OutputStreamWriter retval = null;
        String enc = getEncoding();
        if (enc != null) {
            try {
                retval = new OutputStreamWriter(os, enc);
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.warn("Error initializing output writer.");
                LogLog.warn("Unsupported encoding?");
            }
        }
        if (retval == null) {
            return new OutputStreamWriter(os);
        }
        return retval;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String value) {
        this.encoding = value;
    }

    public synchronized void setErrorHandler(ErrorHandler eh) {
        if (eh == null) {
            LogLog.warn("You have tried to set a null error-handler.");
        } else {
            this.errorHandler = eh;
            if (this.qw != null) {
                this.qw.setErrorHandler(eh);
            }
        }
    }

    public synchronized void setWriter(Writer writer) {
        reset();
        this.qw = new QuietWriter(writer, this.errorHandler);
        writeHeader();
    }

    /* access modifiers changed from: protected */
    public void subAppend(LoggingEvent event) {
        this.qw.write(this.layout.format(event));
        if (this.layout.ignoresThrowable()) {
            String[] s = event.getThrowableStrRep();
            if (s != null) {
                for (String write : s) {
                    this.qw.write(write);
                    this.qw.write(Layout.LINE_SEP);
                }
            }
        }
        if (shouldFlush(event)) {
            this.qw.flush();
        }
    }

    public boolean requiresLayout() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void reset() {
        closeWriter();
        this.qw = null;
    }

    /* access modifiers changed from: protected */
    public void writeFooter() {
        if (this.layout != null) {
            String f = this.layout.getFooter();
            if (f != null && this.qw != null) {
                this.qw.write(f);
                this.qw.flush();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writeHeader() {
        if (this.layout != null) {
            String h = this.layout.getHeader();
            if (h != null && this.qw != null) {
                this.qw.write(h);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldFlush(LoggingEvent event) {
        return this.immediateFlush;
    }
}
