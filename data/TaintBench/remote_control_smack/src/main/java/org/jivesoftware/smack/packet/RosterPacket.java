package org.jivesoftware.smack.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.util.StringUtils;

public class RosterPacket extends IQ {
    private final List<Item> rosterItems = new ArrayList();

    public static class Item {
        private final Set<String> groupNames = new CopyOnWriteArraySet();
        private ItemStatus itemStatus = null;
        private ItemType itemType = null;
        private String name;
        private String user;

        public Item(String user, String name) {
            this.user = user.toLowerCase();
            this.name = name;
        }

        public String getUser() {
            return this.user;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ItemType getItemType() {
            return this.itemType;
        }

        public void setItemType(ItemType itemType) {
            this.itemType = itemType;
        }

        public ItemStatus getItemStatus() {
            return this.itemStatus;
        }

        public void setItemStatus(ItemStatus itemStatus) {
            this.itemStatus = itemStatus;
        }

        public Set<String> getGroupNames() {
            return Collections.unmodifiableSet(this.groupNames);
        }

        public void addGroupName(String groupName) {
            this.groupNames.add(groupName);
        }

        public void removeGroupName(String groupName) {
            this.groupNames.remove(groupName);
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<item jid=\"").append(this.user).append("\"");
            if (this.name != null) {
                buf.append(" name=\"").append(StringUtils.escapeForXML(this.name)).append("\"");
            }
            if (this.itemType != null) {
                buf.append(" subscription=\"").append(this.itemType).append("\"");
            }
            if (this.itemStatus != null) {
                buf.append(" ask=\"").append(this.itemStatus).append("\"");
            }
            buf.append(">");
            for (String groupName : this.groupNames) {
                buf.append("<group>").append(StringUtils.escapeForXML(groupName)).append("</group>");
            }
            buf.append("</item>");
            return buf.toString();
        }
    }

    public static class ItemStatus {
        public static final ItemStatus SUBSCRIPTION_PENDING = new ItemStatus("subscribe");
        public static final ItemStatus UNSUBCRIPTION_PENDING = new ItemStatus("unsubscribe");
        private String value;

        public static ItemStatus fromString(String value) {
            if (value == null) {
                return null;
            }
            value = value.toLowerCase();
            if ("unsubscribe".equals(value)) {
                return SUBSCRIPTION_PENDING;
            }
            if ("subscribe".equals(value)) {
                return SUBSCRIPTION_PENDING;
            }
            return null;
        }

        private ItemStatus(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

    public enum ItemType {
        none,
        to,
        from,
        both,
        remove
    }

    public void addRosterItem(Item item) {
        synchronized (this.rosterItems) {
            this.rosterItems.add(item);
        }
    }

    public int getRosterItemCount() {
        int size;
        synchronized (this.rosterItems) {
            size = this.rosterItems.size();
        }
        return size;
    }

    public Collection<Item> getRosterItems() {
        List unmodifiableList;
        synchronized (this.rosterItems) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.rosterItems));
        }
        return unmodifiableList;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:roster\">");
        synchronized (this.rosterItems) {
            for (Item entry : this.rosterItems) {
                buf.append(entry.toXML());
            }
        }
        buf.append("</query>");
        return buf.toString();
    }
}
