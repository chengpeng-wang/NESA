package com.flurry.sdk;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class lz implements Runnable {
    private static final String kLogTag = lz.class.getSimpleName();
    private PrintStream fStream;
    private PrintWriter fWriter;

    public lz(PrintStream printStream) {
        this.fStream = printStream;
    }

    public lz(PrintWriter printWriter) {
        this.fWriter = printWriter;
    }

    public final void run() {
        try {
            safeRun();
        } catch (Throwable th) {
            if (this.fStream != null) {
                th.printStackTrace(this.fStream);
            } else if (this.fWriter != null) {
                th.printStackTrace(this.fWriter);
            } else {
                th.printStackTrace();
            }
            iw.a(6, kLogTag, "", th);
        }
    }

    public abstract void safeRun();
}
