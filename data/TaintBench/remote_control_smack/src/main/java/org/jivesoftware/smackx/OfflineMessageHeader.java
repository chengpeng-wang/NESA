package org.jivesoftware.smackx;

import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class OfflineMessageHeader {
    private String jid;
    private String stamp;
    private String user;

    public OfflineMessageHeader(Item item) {
        this.user = item.getEntityID();
        this.jid = item.getName();
        this.stamp = item.getNode();
    }

    public String getUser() {
        return this.user;
    }

    public String getJid() {
        return this.jid;
    }

    public String getStamp() {
        return this.stamp;
    }
}
