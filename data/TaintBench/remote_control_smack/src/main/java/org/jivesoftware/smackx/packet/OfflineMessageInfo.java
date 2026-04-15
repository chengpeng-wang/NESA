package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfflineMessageInfo implements PacketExtension {
    private String node = null;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            OfflineMessageInfo info = new OfflineMessageInfo();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("item")) {
                        info.setNode(parser.getAttributeValue("", "node"));
                    }
                } else if (eventType == 3 && parser.getName().equals(MessageEvent.OFFLINE)) {
                    done = true;
                }
            }
            return info;
        }
    }

    public String getElementName() {
        return MessageEvent.OFFLINE;
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/offline";
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        if (getNode() != null) {
            buf.append("<item node=\"").append(getNode()).append("\"/>");
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }
}
