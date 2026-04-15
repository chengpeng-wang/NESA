package org.jivesoftware.smackx.packet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.packet.IQ;

public class DiscoverItems extends IQ {
    private final List<Item> items = new CopyOnWriteArrayList();
    private String node;

    public static class Item {
        public static final String REMOVE_ACTION = "remove";
        public static final String UPDATE_ACTION = "update";
        private String action;
        private String entityID;
        private String name;
        private String node;

        public Item(String entityID) {
            this.entityID = entityID;
        }

        public String getEntityID() {
            return this.entityID;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNode() {
            return this.node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<item jid=\"").append(this.entityID).append("\"");
            if (this.name != null) {
                buf.append(" name=\"").append(this.name).append("\"");
            }
            if (this.node != null) {
                buf.append(" node=\"").append(this.node).append("\"");
            }
            if (this.action != null) {
                buf.append(" action=\"").append(this.action).append("\"");
            }
            buf.append("/>");
            return buf.toString();
        }
    }

    public void addItem(Item item) {
        synchronized (this.items) {
            this.items.add(item);
        }
    }

    public Iterator<Item> getItems() {
        Iterator it;
        synchronized (this.items) {
            it = Collections.unmodifiableList(this.items).iterator();
        }
        return it;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/disco#items\"");
        if (getNode() != null) {
            buf.append(" node=\"");
            buf.append(getNode());
            buf.append("\"");
        }
        buf.append(">");
        synchronized (this.items) {
            for (Item item : this.items) {
                buf.append(item.toXML());
            }
        }
        buf.append("</query>");
        return buf.toString();
    }
}
