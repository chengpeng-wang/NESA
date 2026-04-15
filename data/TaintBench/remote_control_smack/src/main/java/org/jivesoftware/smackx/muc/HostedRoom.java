package org.jivesoftware.smackx.muc;

import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class HostedRoom {
    private String jid;
    private String name;

    public HostedRoom(Item item) {
        this.jid = item.getEntityID();
        this.name = item.getName();
    }

    public String getJid() {
        return this.jid;
    }

    public String getName() {
        return this.name;
    }
}
