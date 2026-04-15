package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class NotFilter implements PacketFilter {
    private PacketFilter filter;

    public NotFilter(PacketFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.filter = filter;
    }

    public boolean accept(Packet packet) {
        return !this.filter.accept(packet);
    }
}
