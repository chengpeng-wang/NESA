package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

public class FromMatchesFilter implements PacketFilter {
    private String address;
    private boolean matchBareJID = false;

    public FromMatchesFilter(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.address = address.toLowerCase();
        this.matchBareJID = "".equals(StringUtils.parseResource(address));
    }

    public boolean accept(Packet packet) {
        if (packet.getFrom() == null) {
            return false;
        }
        if (this.matchBareJID) {
            return packet.getFrom().toLowerCase().startsWith(this.address);
        }
        return this.address.equals(packet.getFrom().toLowerCase());
    }

    public String toString() {
        return "FromMatchesFilter: " + this.address;
    }
}
