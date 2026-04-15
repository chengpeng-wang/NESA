package org.apache.log4j.helpers;

import java.io.File;

public abstract class FileWatchdog extends Thread {
    public static final long DEFAULT_DELAY = 60000;
    protected long delay = DEFAULT_DELAY;
    File file;
    protected String filename;
    boolean interrupted = false;
    long lastModif = 0;
    boolean warnedAlready = false;

    public abstract void doOnChange();

    protected FileWatchdog(String filename) {
        super("FileWatchdog");
        this.filename = filename;
        this.file = new File(filename);
        setDaemon(true);
        checkAndConfigure();
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    /* access modifiers changed from: protected */
    public void checkAndConfigure() {
        try {
            if (this.file.exists()) {
                long l = this.file.lastModified();
                if (l > this.lastModif) {
                    this.lastModif = l;
                    doOnChange();
                    this.warnedAlready = false;
                }
            } else if (!this.warnedAlready) {
                LogLog.debug(new StringBuffer().append("[").append(this.filename).append("] does not exist.").toString());
                this.warnedAlready = true;
            }
        } catch (SecurityException e) {
            LogLog.warn(new StringBuffer().append("Was not allowed to read check file existance, file:[").append(this.filename).append("].").toString());
            this.interrupted = true;
        }
    }

    public void run() {
        while (!this.interrupted) {
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
            }
            checkAndConfigure();
        }
    }
}
