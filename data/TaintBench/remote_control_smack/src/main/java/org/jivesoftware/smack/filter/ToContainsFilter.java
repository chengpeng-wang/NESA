package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class ToContainsFilter implements PacketFilter {
    private String to;

    public ToContainsFilter(String to) {
        if (to == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.to = to.toLowerCase();
    }

    public boolean accept(Packet packet) {
        if (packet.getTo() == null || packet.getTo().toLowerCase().indexOf(this.to) == -1) {
            return false;
        }
        return true;
    }
}
