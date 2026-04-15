package org.apache.log4j.nt;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class NTEventLogAppender extends AppenderSkeleton {
    private int _handle;
    private String server;
    private String source;

    private native void deregisterEventSource(int i);

    private native int registerEventSource(String str, String str2);

    private native void reportEvent(int i, String str, int i2);

    public NTEventLogAppender() {
        this(null, null, null);
    }

    public NTEventLogAppender(String source) {
        this(null, source, null);
    }

    public NTEventLogAppender(String server, String source) {
        this(server, source, null);
    }

    public NTEventLogAppender(Layout layout) {
        this(null, null, layout);
    }

    public NTEventLogAppender(String source, Layout layout) {
        this(null, source, layout);
    }

    public NTEventLogAppender(String server, String source, Layout layout) {
        this._handle = 0;
        this.source = null;
        this.server = null;
        if (source == null) {
            source = "Log4j";
        }
        if (layout == null) {
            this.layout = new TTCCLayout();
        } else {
            this.layout = layout;
        }
        try {
            this._handle = registerEventSource(server, source);
        } catch (Exception e) {
            e.printStackTrace();
            this._handle = 0;
        }
    }

    public void close() {
    }

    public void activateOptions() {
        if (this.source != null) {
            try {
                this._handle = registerEventSource(this.server, this.source);
            } catch (Exception e) {
                LogLog.error("Could not register event source.", e);
                this._handle = 0;
            }
        }
    }

    public void append(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(this.layout.format(event));
        if (this.layout.ignoresThrowable()) {
            String[] s = event.getThrowableStrRep();
            if (s != null) {
                for (String append : s) {
                    sbuf.append(append);
                }
            }
        }
        reportEvent(this._handle, sbuf.toString(), event.getLevel().toInt());
    }

    public void finalize() {
        deregisterEventSource(this._handle);
        this._handle = 0;
    }

    public void setSource(String source) {
        this.source = source.trim();
    }

    public String getSource() {
        return this.source;
    }

    public boolean requiresLayout() {
        return true;
    }

    static {
        String[] archs;
        try {
            archs = new String[]{System.getProperty("os.arch")};
        } catch (SecurityException e) {
            archs = new String[]{"amd64", "ia64", "x86"};
        }
        boolean loaded = false;
        int i = 0;
        while (i < archs.length) {
            try {
                System.loadLibrary(new StringBuffer().append("NTEventLogAppender.").append(archs[i]).toString());
                loaded = true;
                break;
            } catch (UnsatisfiedLinkError e2) {
                loaded = false;
                i++;
            }
        }
        if (!loaded) {
            System.loadLibrary("NTEventLogAppender");
        }
    }
}
