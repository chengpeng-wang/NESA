package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfflineMessageRequest extends IQ {
    private boolean fetch = false;
    private List items = new ArrayList();
    private boolean purge = false;

    public static class Item {
        private String action;
        private String jid;
        private String node;

        public Item(String node) {
            this.node = node;
        }

        public String getNode() {
            return this.node;
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getJid() {
            return this.jid;
        }

        public void setJid(String jid) {
            this.jid = jid;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<item");
            if (getAction() != null) {
                buf.append(" action=\"").append(getAction()).append("\"");
            }
            if (getJid() != null) {
                buf.append(" jid=\"").append(getJid()).append("\"");
            }
            if (getNode() != null) {
                buf.append(" node=\"").append(getNode()).append("\"");
            }
            buf.append("/>");
            return buf.toString();
        }
    }

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            OfflineMessageRequest request = new OfflineMessageRequest();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("item")) {
                        request.addItem(parseItem(parser));
                    } else if (parser.getName().equals("purge")) {
                        request.setPurge(true);
                    } else if (parser.getName().equals("fetch")) {
                        request.setFetch(true);
                    }
                } else if (eventType == 3 && parser.getName().equals(MessageEvent.OFFLINE)) {
                    done = true;
                }
            }
            return request;
        }

        private Item parseItem(XmlPullParser parser) throws Exception {
            boolean done = false;
            Item item = new Item(parser.getAttributeValue("", "node"));
            item.setAction(parser.getAttributeValue("", "action"));
            item.setJid(parser.getAttributeValue("", "jid"));
            while (!done) {
                if (parser.next() == 3 && parser.getName().equals("item")) {
                    done = true;
                }
            }
            return item;
        }
    }

    public Iterator getItems() {
        Iterator it;
        synchronized (this.items) {
            it = Collections.unmodifiableList(new ArrayList(this.items)).iterator();
        }
        return it;
    }

    public void addItem(Item item) {
        synchronized (this.items) {
            this.items.add(item);
        }
    }

    public boolean isPurge() {
        return this.purge;
    }

    public void setPurge(boolean purge) {
        this.purge = purge;
    }

    public boolean isFetch() {
        return this.fetch;
    }

    public void setFetch(boolean fetch) {
        this.fetch = fetch;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<offline xmlns=\"http://jabber.org/protocol/offline\">");
        synchronized (this.items) {
            for (int i = 0; i < this.items.size(); i++) {
                buf.append(((Item) this.items.get(i)).toXML());
            }
        }
        if (this.purge) {
            buf.append("<purge/>");
        }
        if (this.fetch) {
            buf.append("<fetch/>");
        }
        buf.append(getExtensionsXML());
        buf.append("</offline>");
        return buf.toString();
    }
}
