package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class PacketTypeFilter implements PacketFilter {
    Class packetType;

    public PacketTypeFilter(Class packetType) {
        if (Packet.class.isAssignableFrom(packetType)) {
            this.packetType = packetType;
            return;
        }
        throw new IllegalArgumentException("Packet type must be a sub-class of Packet.");
    }

    public boolean accept(Packet packet) {
        return this.packetType.isInstance(packet);
    }

    public String toString() {
        return "PacketTypeFilter: " + this.packetType.getName();
    }
}
