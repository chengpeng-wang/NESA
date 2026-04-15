package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.GroupChatInvitation;

class PacketMultiplexListener implements PacketListener {
    private static final PacketFilter DECLINES_FILTER = new PacketExtensionFilter(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
    private static final PacketFilter MESSAGE_FILTER = new MessageTypeFilter(Type.groupchat);
    private static final PacketFilter PRESENCE_FILTER = new PacketTypeFilter(Presence.class);
    private static final PacketFilter SUBJECT_FILTER = new PacketFilter() {
        public boolean accept(Packet packet) {
            return ((Message) packet).getSubject() != null;
        }
    };
    private PacketListener declinesListener;
    private ConnectionDetachedPacketCollector messageCollector;
    private PacketListener presenceListener;
    private PacketListener subjectListener;

    public PacketMultiplexListener(ConnectionDetachedPacketCollector messageCollector, PacketListener presenceListener, PacketListener subjectListener, PacketListener declinesListener) {
        if (messageCollector == null) {
            throw new IllegalArgumentException("MessageCollector is null");
        } else if (presenceListener == null) {
            throw new IllegalArgumentException("Presence listener is null");
        } else if (subjectListener == null) {
            throw new IllegalArgumentException("Subject listener is null");
        } else if (declinesListener == null) {
            throw new IllegalArgumentException("Declines listener is null");
        } else {
            this.messageCollector = messageCollector;
            this.presenceListener = presenceListener;
            this.subjectListener = subjectListener;
            this.declinesListener = declinesListener;
        }
    }

    public void processPacket(Packet p) {
        if (PRESENCE_FILTER.accept(p)) {
            this.presenceListener.processPacket(p);
        } else if (MESSAGE_FILTER.accept(p)) {
            this.messageCollector.processPacket(p);
            if (SUBJECT_FILTER.accept(p)) {
                this.subjectListener.processPacket(p);
            }
        } else if (DECLINES_FILTER.accept(p)) {
            this.declinesListener.processPacket(p);
        }
    }
}
