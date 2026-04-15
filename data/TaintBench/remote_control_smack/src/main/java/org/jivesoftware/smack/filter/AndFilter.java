package org.jivesoftware.smack.filter;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;

public class AndFilter implements PacketFilter {
    private List<PacketFilter> filters = new ArrayList();

    public AndFilter(PacketFilter... filters) {
        if (filters == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        for (PacketFilter filter : filters) {
            if (filter == null) {
                throw new IllegalArgumentException("Parameter cannot be null.");
            }
            this.filters.add(filter);
        }
    }

    public void addFilter(PacketFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.filters.add(filter);
    }

    public boolean accept(Packet packet) {
        for (PacketFilter filter : this.filters) {
            if (!filter.accept(packet)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return this.filters.toString();
    }
}
