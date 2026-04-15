package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

public class RosterEntry {
    private XMPPConnection connection;
    private String name;
    private ItemStatus status;
    private ItemType type;
    private String user;

    RosterEntry(String user, String name, ItemType type, ItemStatus status, XMPPConnection connection) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.status = status;
        this.connection = connection;
    }

    public String getUser() {
        return this.user;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || !name.equals(this.name)) {
            this.name = name;
            RosterPacket packet = new RosterPacket();
            packet.setType(Type.SET);
            packet.addRosterItem(toRosterItem(this));
            this.connection.sendPacket(packet);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateState(String name, ItemType type, ItemStatus status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public Collection<RosterGroup> getGroups() {
        List<RosterGroup> results = new ArrayList();
        for (RosterGroup group : this.connection.roster.getGroups()) {
            if (group.contains(this)) {
                results.add(group);
            }
        }
        return Collections.unmodifiableCollection(results);
    }

    public ItemType getType() {
        return this.type;
    }

    public ItemStatus getStatus() {
        return this.status;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (this.name != null) {
            buf.append(this.name).append(": ");
        }
        buf.append(this.user);
        Collection<RosterGroup> groups = getGroups();
        if (!groups.isEmpty()) {
            buf.append(" [");
            Iterator<RosterGroup> iter = groups.iterator();
            buf.append(((RosterGroup) iter.next()).getName());
            while (iter.hasNext()) {
                buf.append(", ");
                buf.append(((RosterGroup) iter.next()).getName());
            }
            buf.append("]");
        }
        return buf.toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !(object instanceof RosterEntry)) {
            return false;
        }
        return this.user.equals(((RosterEntry) object).getUser());
    }

    static Item toRosterItem(RosterEntry entry) {
        Item item = new Item(entry.getUser(), entry.getName());
        item.setItemType(entry.getType());
        item.setItemStatus(entry.getStatus());
        for (RosterGroup group : entry.getGroups()) {
            item.addGroupName(group.getName());
        }
        return item;
    }
}
