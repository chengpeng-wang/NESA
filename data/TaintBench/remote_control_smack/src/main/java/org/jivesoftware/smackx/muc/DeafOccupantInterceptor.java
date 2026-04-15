package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.GroupChatInvitation;

public class DeafOccupantInterceptor implements PacketInterceptor {

    private static class DeafExtension implements PacketExtension {
        private DeafExtension() {
        }

        public String getElementName() {
            return GroupChatInvitation.ELEMENT_NAME;
        }

        public String getNamespace() {
            return "http://jivesoftware.org/protocol/muc";
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
            buf.append("<deaf-occupant/>");
            buf.append("</").append(getElementName()).append(">");
            return buf.toString();
        }
    }

    public void interceptPacket(Packet packet) {
        Presence presence = (Presence) packet;
        if (Type.available == presence.getType() && presence.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc") != null) {
            packet.addExtension(new DeafExtension());
        }
    }
}
