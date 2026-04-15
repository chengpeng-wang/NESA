package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;

public class MultipleAddresses implements PacketExtension {
    public static final String BCC = "bcc";
    public static final String CC = "cc";
    public static final String NO_REPLY = "noreply";
    public static final String REPLY_ROOM = "replyroom";
    public static final String REPLY_TO = "replyto";
    public static final String TO = "to";
    private List addresses = new ArrayList();

    public static class Address {
        private boolean delivered;
        private String description;
        private String jid;
        private String node;
        private String type;
        private String uri;

        private Address(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        public String getJid() {
            return this.jid;
        }

        /* access modifiers changed from: private */
        public void setJid(String jid) {
            this.jid = jid;
        }

        public String getNode() {
            return this.node;
        }

        /* access modifiers changed from: private */
        public void setNode(String node) {
            this.node = node;
        }

        public String getDescription() {
            return this.description;
        }

        /* access modifiers changed from: private */
        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isDelivered() {
            return this.delivered;
        }

        /* access modifiers changed from: private */
        public void setDelivered(boolean delivered) {
            this.delivered = delivered;
        }

        public String getUri() {
            return this.uri;
        }

        /* access modifiers changed from: private */
        public void setUri(String uri) {
            this.uri = uri;
        }

        /* access modifiers changed from: private */
        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<address type=\"");
            buf.append(this.type).append("\"");
            if (this.jid != null) {
                buf.append(" jid=\"");
                buf.append(this.jid).append("\"");
            }
            if (this.node != null) {
                buf.append(" node=\"");
                buf.append(this.node).append("\"");
            }
            if (this.description != null && this.description.trim().length() > 0) {
                buf.append(" desc=\"");
                buf.append(this.description).append("\"");
            }
            if (this.delivered) {
                buf.append(" delivered=\"true\"");
            }
            if (this.uri != null) {
                buf.append(" uri=\"");
                buf.append(this.uri).append("\"");
            }
            buf.append("/>");
            return buf.toString();
        }
    }

    public void addAddress(String type, String jid, String node, String desc, boolean delivered, String uri) {
        Address address = new Address(type);
        address.setJid(jid);
        address.setNode(node);
        address.setDescription(desc);
        address.setDelivered(delivered);
        address.setUri(uri);
        this.addresses.add(address);
    }

    public void setNoReply() {
        this.addresses.add(new Address(NO_REPLY));
    }

    public List getAddressesOfType(String type) {
        List answer = new ArrayList(this.addresses.size());
        for (Address address : this.addresses) {
            if (address.getType().equals(type)) {
                answer.add(address);
            }
        }
        return answer;
    }

    public String getElementName() {
        return "addresses";
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/address";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName());
        buf.append(" xmlns=\"").append(getNamespace()).append("\">");
        for (Address address : this.addresses) {
            buf.append(address.toXML());
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }
}
