package org.apache.log4j.varia;

import org.apache.log4j.RollingFileAppender;

public class ExternallyRolledFileAppender extends RollingFileAppender {
    public static final String OK = "OK";
    public static final String ROLL_OVER = "RollOver";
    HUP hup;
    int port = 0;

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void activateOptions() {
        super.activateOptions();
        if (this.port != 0) {
            if (this.hup != null) {
                this.hup.interrupt();
            }
            this.hup = new HUP(this, this.port);
            this.hup.setDaemon(true);
            this.hup.start();
        }
    }
}
