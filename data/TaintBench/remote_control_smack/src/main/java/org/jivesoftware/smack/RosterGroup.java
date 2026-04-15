package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.util.StringUtils;

public class RosterGroup {
    private XMPPConnection connection;
    private final List<RosterEntry> entries = new ArrayList();
    private String name;

    RosterGroup(String name, XMPPConnection connection) {
        this.name = name;
        this.connection = connection;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        synchronized (this.entries) {
            for (RosterEntry entry : this.entries) {
                RosterPacket packet = new RosterPacket();
                packet.setType(Type.SET);
                Item item = RosterEntry.toRosterItem(entry);
                item.removeGroupName(this.name);
                item.addGroupName(name);
                packet.addRosterItem(item);
                this.connection.sendPacket(packet);
            }
        }
    }

    public int getEntryCount() {
        int size;
        synchronized (this.entries) {
            size = this.entries.size();
        }
        return size;
    }

    public Collection<RosterEntry> getEntries() {
        List unmodifiableList;
        synchronized (this.entries) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.entries));
        }
        return unmodifiableList;
    }

    public RosterEntry getEntry(String user) {
        if (user == null) {
            return null;
        }
        String userLowerCase = StringUtils.parseBareAddress(user).toLowerCase();
        synchronized (this.entries) {
            for (RosterEntry entry : this.entries) {
                if (entry.getUser().equals(userLowerCase)) {
                    return entry;
                }
            }
            return null;
        }
    }

    public boolean contains(RosterEntry entry) {
        boolean contains;
        synchronized (this.entries) {
            contains = this.entries.contains(entry);
        }
        return contains;
    }

    public boolean contains(String user) {
        return getEntry(user) != null;
    }

    public void addEntry(RosterEntry entry) throws XMPPException {
        PacketCollector collector = null;
        synchronized (this.entries) {
            if (!this.entries.contains(entry)) {
                RosterPacket packet = new RosterPacket();
                packet.setType(Type.SET);
                Item item = RosterEntry.toRosterItem(entry);
                item.addGroupName(getName());
                packet.addRosterItem(item);
                collector = this.connection.createPacketCollector(new PacketIDFilter(packet.getPacketID()));
                this.connection.sendPacket(packet);
            }
        }
        if (collector != null) {
            IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            collector.cancel();
            if (response == null) {
                throw new XMPPException("No response from the server.");
            } else if (response.getType() == Type.ERROR) {
                throw new XMPPException(response.getError());
            } else {
                addEntryLocal(entry);
            }
        }
    }

    public void removeEntry(RosterEntry entry) throws XMPPException {
        PacketCollector collector = null;
        synchronized (this.entries) {
            if (this.entries.contains(entry)) {
                RosterPacket packet = new RosterPacket();
                packet.setType(Type.SET);
                Item item = RosterEntry.toRosterItem(entry);
                item.removeGroupName(getName());
                packet.addRosterItem(item);
                collector = this.connection.createPacketCollector(new PacketIDFilter(packet.getPacketID()));
                this.connection.sendPacket(packet);
            }
        }
        if (collector != null) {
            IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            collector.cancel();
            if (response == null) {
                throw new XMPPException("No response from the server.");
            } else if (response.getType() == Type.ERROR) {
                throw new XMPPException(response.getError());
            } else {
                removeEntryLocal(entry);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void addEntryLocal(RosterEntry entry) {
        synchronized (this.entries) {
            this.entries.remove(entry);
            this.entries.add(entry);
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeEntryLocal(RosterEntry entry) {
        synchronized (this.entries) {
            if (this.entries.contains(entry)) {
                this.entries.remove(entry);
            }
        }
    }
}
