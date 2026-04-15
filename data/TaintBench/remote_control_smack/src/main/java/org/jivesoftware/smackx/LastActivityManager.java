package org.jivesoftware.smackx;

import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.LastActivity;

public class LastActivityManager {
    /* access modifiers changed from: private */
    public XMPPConnection connection;
    private long lastMessageSent;

    /* synthetic */ LastActivityManager(XMPPConnection x0, AnonymousClass1 x1) {
        this(x0);
    }

    static {
        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {
            public void connectionCreated(XMPPConnection connection) {
                LastActivityManager lastActivityManager = new LastActivityManager(connection, null);
            }
        });
    }

    private LastActivityManager(XMPPConnection connection) {
        this.connection = connection;
        connection.addPacketWriterListener(new PacketListener() {
            public void processPacket(Packet packet) {
                LastActivityManager.this.resetIdleTime();
            }
        }, null);
        connection.addPacketListener(new PacketListener() {
            public void processPacket(Packet packet) {
                LastActivity message = new LastActivity();
                message.setType(Type.RESULT);
                message.setTo(packet.getFrom());
                message.setFrom(packet.getTo());
                message.setPacketID(packet.getPacketID());
                message.setLastActivity(LastActivityManager.this.getIdleTime());
                LastActivityManager.this.connection.sendPacket(message);
            }
        }, new AndFilter(new IQTypeFilter(Type.GET), new PacketTypeFilter(LastActivity.class)));
    }

    /* access modifiers changed from: private */
    public void resetIdleTime() {
        this.lastMessageSent = System.currentTimeMillis();
    }

    /* access modifiers changed from: private */
    public long getIdleTime() {
        return (System.currentTimeMillis() - this.lastMessageSent) / 1000;
    }

    public static LastActivity getLastActivity(XMPPConnection con, String jid) throws XMPPException {
        LastActivity activity = new LastActivity();
        activity.setTo(jid);
        PacketCollector collector = con.createPacketCollector(new PacketIDFilter(activity.getPacketID()));
        con.sendPacket(activity);
        LastActivity response = (LastActivity) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }
}
