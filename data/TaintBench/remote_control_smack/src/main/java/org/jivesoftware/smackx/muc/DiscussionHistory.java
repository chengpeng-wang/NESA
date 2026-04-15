package org.jivesoftware.smackx.muc;

import java.util.Date;
import org.jivesoftware.smackx.packet.MUCInitialPresence.History;

public class DiscussionHistory {
    private int maxChars = -1;
    private int maxStanzas = -1;
    private int seconds = -1;
    private Date since;

    public int getMaxChars() {
        return this.maxChars;
    }

    public int getMaxStanzas() {
        return this.maxStanzas;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public Date getSince() {
        return this.since;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }

    public void setMaxStanzas(int maxStanzas) {
        this.maxStanzas = maxStanzas;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    private boolean isConfigured() {
        return this.maxChars > -1 || this.maxStanzas > -1 || this.seconds > -1 || this.since != null;
    }

    /* access modifiers changed from: 0000 */
    public History getMUCHistory() {
        if (!isConfigured()) {
            return null;
        }
        History mucHistory = new History();
        if (this.maxChars > -1) {
            mucHistory.setMaxChars(this.maxChars);
        }
        if (this.maxStanzas > -1) {
            mucHistory.setMaxStanzas(this.maxStanzas);
        }
        if (this.seconds > -1) {
            mucHistory.setSeconds(this.seconds);
        }
        if (this.since == null) {
            return mucHistory;
        }
        mucHistory.setSince(this.since);
        return mucHistory;
    }
}
