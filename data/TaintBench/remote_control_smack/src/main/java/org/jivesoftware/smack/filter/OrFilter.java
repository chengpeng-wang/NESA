package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class OrFilter implements PacketFilter {
    private PacketFilter[] filters;
    private int size;

    public OrFilter() {
        this.size = 0;
        this.filters = new PacketFilter[3];
    }

    public OrFilter(PacketFilter filter1, PacketFilter filter2) {
        if (filter1 == null || filter2 == null) {
            throw new IllegalArgumentException("Parameters cannot be null.");
        }
        this.size = 2;
        this.filters = new PacketFilter[2];
        this.filters[0] = filter1;
        this.filters[1] = filter2;
    }

    public void addFilter(PacketFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        if (this.size == this.filters.length) {
            PacketFilter[] newFilters = new PacketFilter[(this.filters.length + 2)];
            for (int i = 0; i < this.filters.length; i++) {
                newFilters[i] = this.filters[i];
            }
            this.filters = newFilters;
        }
        this.filters[this.size] = filter;
        this.size++;
    }

    public boolean accept(Packet packet) {
        for (int i = 0; i < this.size; i++) {
            if (this.filters[i].accept(packet)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this.filters.toString();
    }
}
