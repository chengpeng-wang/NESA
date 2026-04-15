package org.jivesoftware.smackx.workgroup.agent;

import com.baidu.location.LocationClientOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitation;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitationListener;
import org.jivesoftware.smackx.workgroup.agent.WorkgroupQueue.Status;
import org.jivesoftware.smackx.workgroup.ext.history.AgentChatHistory;
import org.jivesoftware.smackx.workgroup.ext.history.ChatMetadata;
import org.jivesoftware.smackx.workgroup.ext.macros.MacroGroup;
import org.jivesoftware.smackx.workgroup.ext.macros.Macros;
import org.jivesoftware.smackx.workgroup.ext.notes.ChatNotes;
import org.jivesoftware.smackx.workgroup.packet.AgentStatus;
import org.jivesoftware.smackx.workgroup.packet.DepartQueuePacket;
import org.jivesoftware.smackx.workgroup.packet.MonitorPacket;
import org.jivesoftware.smackx.workgroup.packet.OccupantsInfo;
import org.jivesoftware.smackx.workgroup.packet.OfferRequestProvider.OfferRequestPacket;
import org.jivesoftware.smackx.workgroup.packet.OfferRevokeProvider.OfferRevokePacket;
import org.jivesoftware.smackx.workgroup.packet.QueueDetails;
import org.jivesoftware.smackx.workgroup.packet.QueueOverview;
import org.jivesoftware.smackx.workgroup.packet.RoomInvitation;
import org.jivesoftware.smackx.workgroup.packet.RoomTransfer;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.jivesoftware.smackx.workgroup.packet.Transcript;
import org.jivesoftware.smackx.workgroup.packet.Transcripts;
import org.jivesoftware.smackx.workgroup.settings.GenericSettings;
import org.jivesoftware.smackx.workgroup.settings.SearchSettings;

public class AgentSession {
    private Agent agent;
    private AgentRoster agentRoster = null;
    private XMPPConnection connection;
    private final List<WorkgroupInvitationListener> invitationListeners;
    private int maxChats;
    private final Map metaData;
    private final List<OfferListener> offerListeners;
    private boolean online = false;
    private PacketListener packetListener;
    private Mode presenceMode;
    private final List<QueueUsersListener> queueUsersListeners;
    private Map<String, WorkgroupQueue> queues;
    private TranscriptManager transcriptManager;
    private TranscriptSearchManager transcriptSearchManager;
    private String workgroupJID;

    public AgentSession(String workgroupJID, XMPPConnection connection) {
        if (connection.isAuthenticated()) {
            this.workgroupJID = workgroupJID;
            this.connection = connection;
            this.transcriptManager = new TranscriptManager(connection);
            this.transcriptSearchManager = new TranscriptSearchManager(connection);
            this.maxChats = -1;
            this.metaData = new HashMap();
            this.queues = new HashMap();
            this.offerListeners = new ArrayList();
            this.invitationListeners = new ArrayList();
            this.queueUsersListeners = new ArrayList();
            OrFilter filter = new OrFilter();
            filter.addFilter(new PacketTypeFilter(OfferRequestPacket.class));
            filter.addFilter(new PacketTypeFilter(OfferRevokePacket.class));
            filter.addFilter(new PacketTypeFilter(Presence.class));
            filter.addFilter(new PacketTypeFilter(Message.class));
            this.packetListener = new PacketListener() {
                public void processPacket(Packet packet) {
                    try {
                        AgentSession.this.handlePacket(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            connection.addPacketListener(this.packetListener, filter);
            this.agent = new Agent(connection, workgroupJID);
            return;
        }
        throw new IllegalStateException("Must login to server before creating workgroup.");
    }

    public void close() {
        this.connection.removePacketListener(this.packetListener);
    }

    public AgentRoster getAgentRoster() {
        if (this.agentRoster == null) {
            this.agentRoster = new AgentRoster(this.connection, this.workgroupJID);
        }
        int elapsed = 0;
        while (!this.agentRoster.rosterInitialized && elapsed <= 2000) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            elapsed += 500;
        }
        return this.agentRoster;
    }

    public Mode getPresenceMode() {
        return this.presenceMode;
    }

    public int getMaxChats() {
        return this.maxChats;
    }

    public boolean isOnline() {
        return this.online;
    }

    public void setMetaData(String key, String val) throws XMPPException {
        synchronized (this.metaData) {
            String oldVal = (String) this.metaData.get(key);
            if (oldVal == null || !oldVal.equals(val)) {
                this.metaData.put(key, val);
                setStatus(this.presenceMode, this.maxChats);
            }
        }
    }

    public void removeMetaData(String key) throws XMPPException {
        synchronized (this.metaData) {
            if (((String) this.metaData.remove(key)) != null) {
                setStatus(this.presenceMode, this.maxChats);
            }
        }
    }

    public String getMetaData(String key) {
        return (String) this.metaData.get(key);
    }

    public void setOnline(boolean online) throws XMPPException {
        if (this.online != online) {
            Presence presence;
            if (online) {
                presence = new Presence(Type.available);
                presence.setTo(this.workgroupJID);
                presence.addExtension(new DefaultPacketExtension(AgentStatus.ELEMENT_NAME, "http://jabber.org/protocol/workgroup"));
                PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID)));
                this.connection.sendPacket(presence);
                presence = (Presence) collector.nextResult(5000);
                collector.cancel();
                if (!presence.isAvailable()) {
                    throw new XMPPException("No response from server on status set.");
                } else if (presence.getError() != null) {
                    throw new XMPPException(presence.getError());
                } else {
                    this.online = online;
                    return;
                }
            }
            this.online = online;
            presence = new Presence(Type.unavailable);
            presence.setTo(this.workgroupJID);
            presence.addExtension(new DefaultPacketExtension(AgentStatus.ELEMENT_NAME, "http://jabber.org/protocol/workgroup"));
            this.connection.sendPacket(presence);
        }
    }

    public void setStatus(Mode presenceMode, int maxChats) throws XMPPException {
        setStatus(presenceMode, maxChats, null);
    }

    public void setStatus(Mode presenceMode, int maxChats, String status) throws XMPPException {
        if (this.online) {
            if (presenceMode == null) {
                presenceMode = Mode.available;
            }
            this.presenceMode = presenceMode;
            this.maxChats = maxChats;
            Presence presence = new Presence(Type.available);
            presence.setMode(presenceMode);
            presence.setTo(getWorkgroupJID());
            if (status != null) {
                presence.setStatus(status);
            }
            DefaultPacketExtension agentStatus = new DefaultPacketExtension(AgentStatus.ELEMENT_NAME, "http://jabber.org/protocol/workgroup");
            agentStatus.setValue("max-chats", "" + maxChats);
            presence.addExtension(agentStatus);
            presence.addExtension(new MetaData(this.metaData));
            PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID)));
            this.connection.sendPacket(presence);
            presence = (Presence) collector.nextResult(5000);
            collector.cancel();
            if (!presence.isAvailable()) {
                throw new XMPPException("No response from server on status set.");
            } else if (presence.getError() != null) {
                throw new XMPPException(presence.getError());
            } else {
                return;
            }
        }
        throw new IllegalStateException("Cannot set status when the agent is not online.");
    }

    public void setStatus(Mode presenceMode, String status) throws XMPPException {
        if (this.online) {
            if (presenceMode == null) {
                presenceMode = Mode.available;
            }
            this.presenceMode = presenceMode;
            Presence presence = new Presence(Type.available);
            presence.setMode(presenceMode);
            presence.setTo(getWorkgroupJID());
            if (status != null) {
                presence.setStatus(status);
            }
            presence.addExtension(new MetaData(this.metaData));
            PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID)));
            this.connection.sendPacket(presence);
            presence = (Presence) collector.nextResult(5000);
            collector.cancel();
            if (!presence.isAvailable()) {
                throw new XMPPException("No response from server on status set.");
            } else if (presence.getError() != null) {
                throw new XMPPException(presence.getError());
            } else {
                return;
            }
        }
        throw new IllegalStateException("Cannot set status when the agent is not online.");
    }

    public void dequeueUser(String userID) throws XMPPException {
        this.connection.sendPacket(new DepartQueuePacket(this.workgroupJID));
    }

    public Transcripts getTranscripts(String userID) throws XMPPException {
        return this.transcriptManager.getTranscripts(this.workgroupJID, userID);
    }

    public Transcript getTranscript(String sessionID) throws XMPPException {
        return this.transcriptManager.getTranscript(this.workgroupJID, sessionID);
    }

    public Form getTranscriptSearchForm() throws XMPPException {
        return this.transcriptSearchManager.getSearchForm(StringUtils.parseServer(this.workgroupJID));
    }

    public ReportedData searchTranscripts(Form completedForm) throws XMPPException {
        return this.transcriptSearchManager.submitSearch(StringUtils.parseServer(this.workgroupJID), completedForm);
    }

    public OccupantsInfo getOccupantsInfo(String roomID) throws XMPPException {
        OccupantsInfo request = new OccupantsInfo(roomID);
        request.setType(IQ.Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        OccupantsInfo response = (OccupantsInfo) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public WorkgroupQueue getQueue(String queueName) {
        return (WorkgroupQueue) this.queues.get(queueName);
    }

    public Iterator<WorkgroupQueue> getQueues() {
        return Collections.unmodifiableMap(new HashMap(this.queues)).values().iterator();
    }

    public void addQueueUsersListener(QueueUsersListener listener) {
        synchronized (this.queueUsersListeners) {
            if (!this.queueUsersListeners.contains(listener)) {
                this.queueUsersListeners.add(listener);
            }
        }
    }

    public void removeQueueUsersListener(QueueUsersListener listener) {
        synchronized (this.queueUsersListeners) {
            this.queueUsersListeners.remove(listener);
        }
    }

    public void addOfferListener(OfferListener offerListener) {
        synchronized (this.offerListeners) {
            if (!this.offerListeners.contains(offerListener)) {
                this.offerListeners.add(offerListener);
            }
        }
    }

    public void removeOfferListener(OfferListener offerListener) {
        synchronized (this.offerListeners) {
            this.offerListeners.remove(offerListener);
        }
    }

    public void addInvitationListener(WorkgroupInvitationListener invitationListener) {
        synchronized (this.invitationListeners) {
            if (!this.invitationListeners.contains(invitationListener)) {
                this.invitationListeners.add(invitationListener);
            }
        }
    }

    public void removeInvitationListener(WorkgroupInvitationListener invitationListener) {
        synchronized (this.invitationListeners) {
            this.invitationListeners.remove(invitationListener);
        }
    }

    private void fireOfferRequestEvent(OfferRequestPacket requestPacket) {
        Offer offer = new Offer(this.connection, this, requestPacket.getUserID(), requestPacket.getUserJID(), getWorkgroupJID(), new Date(new Date().getTime() + ((long) (requestPacket.getTimeout() * LocationClientOption.MIN_SCAN_SPAN))), requestPacket.getSessionID(), requestPacket.getMetaData(), requestPacket.getContent());
        synchronized (this.offerListeners) {
            for (OfferListener listener : this.offerListeners) {
                listener.offerReceived(offer);
            }
        }
    }

    private void fireOfferRevokeEvent(OfferRevokePacket orp) {
        RevokedOffer revokedOffer = new RevokedOffer(orp.getUserJID(), orp.getUserID(), getWorkgroupJID(), orp.getSessionID(), orp.getReason(), new Date());
        synchronized (this.offerListeners) {
            for (OfferListener listener : this.offerListeners) {
                listener.offerRevoked(revokedOffer);
            }
        }
    }

    private void fireInvitationEvent(String groupChatJID, String sessionID, String body, String from, Map metaData) {
        WorkgroupInvitation invitation = new WorkgroupInvitation(this.connection.getUser(), groupChatJID, this.workgroupJID, sessionID, body, from, metaData);
        synchronized (this.invitationListeners) {
            for (WorkgroupInvitationListener listener : this.invitationListeners) {
                listener.invitationReceived(invitation);
            }
        }
    }

    private void fireQueueUsersEvent(WorkgroupQueue queue, Status status, int averageWaitTime, Date oldestEntry, Set users) {
        synchronized (this.queueUsersListeners) {
            for (QueueUsersListener listener : this.queueUsersListeners) {
                if (status != null) {
                    listener.statusUpdated(queue, status);
                }
                if (averageWaitTime != -1) {
                    listener.averageWaitTimeUpdated(queue, averageWaitTime);
                }
                if (oldestEntry != null) {
                    listener.oldestEntryUpdated(queue, oldestEntry);
                }
                if (users != null) {
                    listener.usersUpdated(queue, users);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlePacket(Packet packet) {
        Packet anonymousClass2;
        if (packet instanceof OfferRequestPacket) {
            anonymousClass2 = new IQ() {
                public String getChildElementXML() {
                    return null;
                }
            };
            anonymousClass2.setPacketID(packet.getPacketID());
            anonymousClass2.setTo(packet.getFrom());
            anonymousClass2.setType(IQ.Type.RESULT);
            this.connection.sendPacket(anonymousClass2);
            fireOfferRequestEvent((OfferRequestPacket) packet);
        } else if (packet instanceof Presence) {
            Presence presence = (Presence) packet;
            String queueName = StringUtils.parseResource(presence.getFrom());
            WorkgroupQueue queue = (WorkgroupQueue) this.queues.get(queueName);
            if (queue == null) {
                queue = new WorkgroupQueue(queueName);
                this.queues.put(queueName, queue);
            }
            QueueOverview queueOverview = (QueueOverview) presence.getExtension(QueueOverview.ELEMENT_NAME, QueueOverview.NAMESPACE);
            if (queueOverview != null) {
                if (queueOverview.getStatus() == null) {
                    queue.setStatus(Status.CLOSED);
                } else {
                    queue.setStatus(queueOverview.getStatus());
                }
                queue.setAverageWaitTime(queueOverview.getAverageWaitTime());
                queue.setOldestEntry(queueOverview.getOldestEntry());
                fireQueueUsersEvent(queue, queueOverview.getStatus(), queueOverview.getAverageWaitTime(), queueOverview.getOldestEntry(), null);
                return;
            }
            QueueDetails queueDetails = (QueueDetails) packet.getExtension(QueueDetails.ELEMENT_NAME, "http://jabber.org/protocol/workgroup");
            if (queueDetails != null) {
                queue.setUsers(queueDetails.getUsers());
                fireQueueUsersEvent(queue, null, -1, null, queueDetails.getUsers());
                return;
            }
            DefaultPacketExtension notifyAgents = (DefaultPacketExtension) presence.getExtension("notify-agents", "http://jabber.org/protocol/workgroup");
            if (notifyAgents != null) {
                int currentChats = Integer.parseInt(notifyAgents.getValue("current-chats"));
                int maxChats = Integer.parseInt(notifyAgents.getValue("max-chats"));
                queue.setCurrentChats(currentChats);
                queue.setMaxChats(maxChats);
            }
        } else if (packet instanceof Message) {
            Message message = (Message) packet;
            MUCUser mucUser = (MUCUser) message.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
            Invite invite = mucUser != null ? mucUser.getInvite() : null;
            if (invite != null && this.workgroupJID.equals(invite.getFrom())) {
                String sessionID = null;
                Map metaData = null;
                SessionID sessionIDExt = (SessionID) message.getExtension(SessionID.ELEMENT_NAME, "http://jivesoftware.com/protocol/workgroup");
                if (sessionIDExt != null) {
                    sessionID = sessionIDExt.getSessionID();
                }
                MetaData metaDataExt = (MetaData) message.getExtension(MetaData.ELEMENT_NAME, "http://jivesoftware.com/protocol/workgroup");
                if (metaDataExt != null) {
                    metaData = metaDataExt.getMetaData();
                }
                fireInvitationEvent(message.getFrom(), sessionID, message.getBody(), message.getFrom(), metaData);
            }
        } else if (packet instanceof OfferRevokePacket) {
            anonymousClass2 = new IQ() {
                public String getChildElementXML() {
                    return null;
                }
            };
            anonymousClass2.setPacketID(packet.getPacketID());
            anonymousClass2.setType(IQ.Type.RESULT);
            this.connection.sendPacket(anonymousClass2);
            fireOfferRevokeEvent((OfferRevokePacket) packet);
        }
    }

    public void setNote(String sessionID, String note) throws XMPPException {
        note = StringUtils.escapeForXML(ChatNotes.replace(note, "\n", "\\n"));
        ChatNotes notes = new ChatNotes();
        notes.setType(IQ.Type.SET);
        notes.setTo(this.workgroupJID);
        notes.setSessionID(sessionID);
        notes.setNotes(note);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(notes.getPacketID()));
        this.connection.sendPacket(notes);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
    }

    public ChatNotes getNote(String sessionID) throws XMPPException {
        ChatNotes request = new ChatNotes();
        request.setType(IQ.Type.GET);
        request.setTo(this.workgroupJID);
        request.setSessionID(sessionID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        ChatNotes response = (ChatNotes) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public AgentChatHistory getAgentHistory(String jid, int maxSessions, Date startDate) throws XMPPException {
        AgentChatHistory request;
        if (startDate != null) {
            request = new AgentChatHistory(jid, maxSessions, startDate);
        } else {
            request = new AgentChatHistory(jid, maxSessions);
        }
        request.setType(IQ.Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        AgentChatHistory response = (AgentChatHistory) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public SearchSettings getSearchSettings() throws XMPPException {
        SearchSettings request = new SearchSettings();
        request.setType(IQ.Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        SearchSettings response = (SearchSettings) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public MacroGroup getMacros(boolean global) throws XMPPException {
        Macros request = new Macros();
        request.setType(IQ.Type.GET);
        request.setTo(this.workgroupJID);
        request.setPersonal(!global);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        Macros response = (Macros) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response.getRootGroup();
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public void saveMacros(MacroGroup group) throws XMPPException {
        Macros request = new Macros();
        request.setType(IQ.Type.SET);
        request.setTo(this.workgroupJID);
        request.setPersonal(true);
        request.setPersonalMacroGroup(group);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
    }

    public Map getChatMetadata(String sessionID) throws XMPPException {
        ChatMetadata request = new ChatMetadata();
        request.setType(IQ.Type.GET);
        request.setTo(this.workgroupJID);
        request.setSessionID(sessionID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        ChatMetadata response = (ChatMetadata) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response.getMetadata();
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public void sendRoomInvitation(RoomInvitation.Type type, String invitee, String sessionID, String reason) throws XMPPException {
        final RoomInvitation invitation = new RoomInvitation(type, invitee, sessionID, reason);
        IQ iq = new IQ() {
            public String getChildElementXML() {
                return invitation.toXML();
            }
        };
        iq.setType(IQ.Type.SET);
        iq.setTo(this.workgroupJID);
        iq.setFrom(this.connection.getUser());
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
    }

    public void sendRoomTransfer(RoomTransfer.Type type, String invitee, String sessionID, String reason) throws XMPPException {
        final RoomTransfer transfer = new RoomTransfer(type, invitee, sessionID, reason);
        IQ iq = new IQ() {
            public String getChildElementXML() {
                return transfer.toXML();
            }
        };
        iq.setType(IQ.Type.SET);
        iq.setTo(this.workgroupJID);
        iq.setFrom(this.connection.getUser());
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
    }

    public GenericSettings getGenericSettings(XMPPConnection con, String query) throws XMPPException {
        GenericSettings setting = new GenericSettings();
        setting.setType(IQ.Type.GET);
        setting.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(setting.getPacketID()));
        this.connection.sendPacket(setting);
        GenericSettings response = (GenericSettings) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public boolean hasMonitorPrivileges(XMPPConnection con) throws XMPPException {
        MonitorPacket request = new MonitorPacket();
        request.setType(IQ.Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        MonitorPacket response = (MonitorPacket) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response.isMonitor();
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public void makeRoomOwner(XMPPConnection con, String sessionID) throws XMPPException {
        MonitorPacket request = new MonitorPacket();
        request.setType(IQ.Type.SET);
        request.setTo(this.workgroupJID);
        request.setSessionID(sessionID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        Packet response = collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
    }
}
