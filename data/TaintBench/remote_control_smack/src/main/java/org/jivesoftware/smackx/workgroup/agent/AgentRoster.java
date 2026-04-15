package org.jivesoftware.smackx.workgroup.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.workgroup.packet.AgentStatus;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest.Item;

public class AgentRoster {
    private static final int EVENT_AGENT_ADDED = 0;
    private static final int EVENT_AGENT_REMOVED = 1;
    private static final int EVENT_PRESENCE_CHANGED = 2;
    private XMPPConnection connection;
    /* access modifiers changed from: private */
    public List entries;
    private List listeners;
    /* access modifiers changed from: private */
    public Map presenceMap;
    boolean rosterInitialized = false;
    /* access modifiers changed from: private */
    public String workgroupJID;

    private class AgentStatusListener implements PacketListener {
        private AgentStatusListener() {
        }

        public void processPacket(Packet packet) {
            if (packet instanceof AgentStatusRequest) {
                for (Item item : ((AgentStatusRequest) packet).getAgents()) {
                    String agentJID = item.getJID();
                    if (DiscoverItems.Item.REMOVE_ACTION.equals(item.getType())) {
                        AgentRoster.this.presenceMap.remove(StringUtils.parseName(StringUtils.parseName(agentJID) + "@" + StringUtils.parseServer(agentJID)));
                        AgentRoster.this.fireEvent(1, agentJID);
                    } else {
                        AgentRoster.this.entries.add(agentJID);
                        AgentRoster.this.fireEvent(0, agentJID);
                    }
                }
                AgentRoster.this.rosterInitialized = true;
            }
        }
    }

    private class PresencePacketListener implements PacketListener {
        private PresencePacketListener() {
        }

        public void processPacket(Packet packet) {
            Presence presence = (Presence) packet;
            String from = presence.getFrom();
            if (from == null) {
                System.out.println("Presence with no FROM: " + presence.toXML());
                return;
            }
            String key = AgentRoster.this.getPresenceMapKey(from);
            Map userPresences;
            if (presence.getType() == Type.available) {
                AgentStatus agentStatus = (AgentStatus) presence.getExtension(AgentStatus.ELEMENT_NAME, "http://jabber.org/protocol/workgroup");
                if (agentStatus != null && AgentRoster.this.workgroupJID.equals(agentStatus.getWorkgroupJID())) {
                    if (AgentRoster.this.presenceMap.get(key) == null) {
                        userPresences = new HashMap();
                        AgentRoster.this.presenceMap.put(key, userPresences);
                    } else {
                        userPresences = (Map) AgentRoster.this.presenceMap.get(key);
                    }
                    synchronized (userPresences) {
                        userPresences.put(StringUtils.parseResource(from), presence);
                    }
                    synchronized (AgentRoster.this.entries) {
                        for (String entry : AgentRoster.this.entries) {
                            if (entry.toLowerCase().equals(StringUtils.parseBareAddress(key).toLowerCase())) {
                                AgentRoster.this.fireEvent(2, packet);
                            }
                        }
                    }
                }
            } else if (presence.getType() == Type.unavailable) {
                if (AgentRoster.this.presenceMap.get(key) != null) {
                    userPresences = (Map) AgentRoster.this.presenceMap.get(key);
                    synchronized (userPresences) {
                        userPresences.remove(StringUtils.parseResource(from));
                    }
                    if (userPresences.isEmpty()) {
                        AgentRoster.this.presenceMap.remove(key);
                    }
                }
                synchronized (AgentRoster.this.entries) {
                    for (String entry2 : AgentRoster.this.entries) {
                        if (entry2.toLowerCase().equals(StringUtils.parseBareAddress(key).toLowerCase())) {
                            AgentRoster.this.fireEvent(2, packet);
                        }
                    }
                }
            }
        }
    }

    AgentRoster(XMPPConnection connection, String workgroupJID) {
        this.connection = connection;
        this.workgroupJID = workgroupJID;
        this.entries = new ArrayList();
        this.listeners = new ArrayList();
        this.presenceMap = new HashMap();
        connection.addPacketListener(new AgentStatusListener(), new PacketTypeFilter(AgentStatusRequest.class));
        connection.addPacketListener(new PresencePacketListener(), new PacketTypeFilter(Presence.class));
        AgentStatusRequest request = new AgentStatusRequest();
        request.setTo(workgroupJID);
        connection.sendPacket(request);
    }

    public void reload() {
        AgentStatusRequest request = new AgentStatusRequest();
        request.setTo(this.workgroupJID);
        this.connection.sendPacket(request);
    }

    public void addListener(AgentRosterListener listener) {
        synchronized (this.listeners) {
            if (!this.listeners.contains(listener)) {
                this.listeners.add(listener);
                for (String jid : getAgents()) {
                    if (this.entries.contains(jid)) {
                        listener.agentAdded(jid);
                        Map userPresences = (Map) this.presenceMap.get(jid);
                        if (userPresences != null) {
                            for (Presence presenceChanged : userPresences.values()) {
                                listener.presenceChanged(presenceChanged);
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeListener(AgentRosterListener listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
    }

    public int getAgentCount() {
        return this.entries.size();
    }

    public Set getAgents() {
        Set agents = new HashSet();
        synchronized (this.entries) {
            for (Object add : this.entries) {
                agents.add(add);
            }
        }
        return Collections.unmodifiableSet(agents);
    }

    public boolean contains(String jid) {
        boolean z = false;
        if (jid != null) {
            synchronized (this.entries) {
                for (String entry : this.entries) {
                    if (entry.toLowerCase().equals(jid.toLowerCase())) {
                        z = true;
                        break;
                    }
                }
            }
        }
        return z;
    }

    public Presence getPresence(String user) {
        Map userPresences = (Map) this.presenceMap.get(getPresenceMapKey(user));
        Presence presence;
        if (userPresences == null) {
            presence = new Presence(Type.unavailable);
            presence.setFrom(user);
            return presence;
        }
        presence = null;
        for (Object obj : userPresences.keySet()) {
            Presence p = (Presence) userPresences.get(obj);
            if (presence == null) {
                presence = p;
            } else if (p.getPriority() > presence.getPriority()) {
                presence = p;
            }
        }
        if (presence != null) {
            return presence;
        }
        presence = new Presence(Type.unavailable);
        presence.setFrom(user);
        return presence;
    }

    /* access modifiers changed from: private */
    public String getPresenceMapKey(String user) {
        String key = user;
        if (contains(user)) {
            return key;
        }
        return StringUtils.parseBareAddress(user).toLowerCase();
    }

    /* access modifiers changed from: private */
    public void fireEvent(int eventType, Object eventObject) {
        AgentRosterListener[] listeners;
        synchronized (this.listeners) {
            listeners = new AgentRosterListener[this.listeners.size()];
            this.listeners.toArray(listeners);
        }
        for (int i = 0; i < listeners.length; i++) {
            switch (eventType) {
                case 0:
                    listeners[i].agentAdded((String) eventObject);
                    break;
                case 1:
                    listeners[i].agentRemoved((String) eventObject);
                    break;
                case 2:
                    listeners[i].presenceChanged((Presence) eventObject);
                    break;
                default:
                    break;
            }
        }
    }
}
