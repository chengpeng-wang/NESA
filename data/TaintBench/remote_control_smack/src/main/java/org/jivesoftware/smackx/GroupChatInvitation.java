package org.jivesoftware.smackx;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class GroupChatInvitation implements PacketExtension {
    public static final String ELEMENT_NAME = "x";
    public static final String NAMESPACE = "jabber:x:conference";
    private String roomAddress;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            String roomAddress = parser.getAttributeValue("", "jid");
            parser.next();
            return new GroupChatInvitation(roomAddress);
        }
    }

    public GroupChatInvitation(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public String getRoomAddress() {
        return this.roomAddress;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<x xmlns=\"jabber:x:conference\" jid=\"").append(this.roomAddress).append("\"/>");
        return buf.toString();
    }
}
