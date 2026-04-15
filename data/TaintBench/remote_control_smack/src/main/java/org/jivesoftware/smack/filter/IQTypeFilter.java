package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;

public class IQTypeFilter implements PacketFilter {
    private Type type;

    public IQTypeFilter(Type type) {
        this.type = type;
    }

    public boolean accept(Packet packet) {
        return (packet instanceof IQ) && ((IQ) packet).getType().equals(this.type);
    }
}
