package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.RosterExchange;

public class RosterExchangeManager {
    private XMPPConnection con;
    private PacketFilter packetFilter = new PacketExtensionFilter(GroupChatInvitation.ELEMENT_NAME, "jabber:x:roster");
    private PacketListener packetListener;
    private List rosterExchangeListeners = new ArrayList();

    public RosterExchangeManager(XMPPConnection con) {
        this.con = con;
        init();
    }

    public void addRosterListener(RosterExchangeListener rosterExchangeListener) {
        synchronized (this.rosterExchangeListeners) {
            if (!this.rosterExchangeListeners.contains(rosterExchangeListener)) {
                this.rosterExchangeListeners.add(rosterExchangeListener);
            }
        }
    }

    public void removeRosterListener(RosterExchangeListener rosterExchangeListener) {
        synchronized (this.rosterExchangeListeners) {
            this.rosterExchangeListeners.remove(rosterExchangeListener);
        }
    }

    public void send(Roster roster, String targetUserID) {
        Message msg = new Message(targetUserID);
        msg.addExtension(new RosterExchange(roster));
        this.con.sendPacket(msg);
    }

    public void send(RosterEntry rosterEntry, String targetUserID) {
        Message msg = new Message(targetUserID);
        RosterExchange rosterExchange = new RosterExchange();
        rosterExchange.addRosterEntry(rosterEntry);
        msg.addExtension(rosterExchange);
        this.con.sendPacket(msg);
    }

    public void send(RosterGroup rosterGroup, String targetUserID) {
        Message msg = new Message(targetUserID);
        RosterExchange rosterExchange = new RosterExchange();
        for (RosterEntry entry : rosterGroup.getEntries()) {
            rosterExchange.addRosterEntry(entry);
        }
        msg.addExtension(rosterExchange);
        this.con.sendPacket(msg);
    }

    /* access modifiers changed from: private */
    public void fireRosterExchangeListeners(String from, Iterator remoteRosterEntries) {
        RosterExchangeListener[] listeners;
        synchronized (this.rosterExchangeListeners) {
            listeners = new RosterExchangeListener[this.rosterExchangeListeners.size()];
            this.rosterExchangeListeners.toArray(listeners);
        }
        for (RosterExchangeListener entriesReceived : listeners) {
            entriesReceived.entriesReceived(from, remoteRosterEntries);
        }
    }

    private void init() {
        this.packetListener = new PacketListener() {
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                RosterExchangeManager.this.fireRosterExchangeListeners(message.getFrom(), ((RosterExchange) message.getExtension(GroupChatInvitation.ELEMENT_NAME, "jabber:x:roster")).getRosterEntries());
            }
        };
        this.con.addPacketListener(this.packetListener, this.packetFilter);
    }

    public void destroy() {
        if (this.con != null) {
            this.con.removePacketListener(this.packetListener);
        }
    }

    public void finalize() {
        destroy();
    }
}
