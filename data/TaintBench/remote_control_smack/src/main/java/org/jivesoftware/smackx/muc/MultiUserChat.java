package org.jivesoftware.smackx.muc;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.NodeInformationProvider;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.packet.MUCAdmin;
import org.jivesoftware.smackx.packet.MUCInitialPresence;
import org.jivesoftware.smackx.packet.MUCOwner;
import org.jivesoftware.smackx.packet.MUCOwner.Destroy;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Decline;
import org.jivesoftware.smackx.packet.MUCUser.Invite;

public class MultiUserChat {
    private static final String discoNamespace = "http://jabber.org/protocol/muc";
    private static final String discoNode = "http://jabber.org/protocol/muc#rooms";
    private static Map<XMPPConnection, List<String>> joinedRooms = new WeakHashMap();
    private XMPPConnection connection;
    private List<PacketListener> connectionListeners = new ArrayList();
    private final List<InvitationRejectionListener> invitationRejectionListeners = new ArrayList();
    private boolean joined = false;
    private ConnectionDetachedPacketCollector messageCollector;
    private PacketFilter messageFilter;
    /* access modifiers changed from: private */
    public String nickname = null;
    /* access modifiers changed from: private */
    public Map<String, Presence> occupantsMap = new ConcurrentHashMap();
    private final List<ParticipantStatusListener> participantStatusListeners = new ArrayList();
    private PacketFilter presenceFilter;
    private List<PacketInterceptor> presenceInterceptors = new ArrayList();
    /* access modifiers changed from: private */
    public String room;
    private RoomListenerMultiplexor roomListenerMultiplexor;
    /* access modifiers changed from: private */
    public String subject;
    private final List<SubjectUpdatedListener> subjectUpdatedListeners = new ArrayList();
    private final List<UserStatusListener> userStatusListeners = new ArrayList();

    private static class InvitationsMonitor implements ConnectionListener {
        private static final Map<XMPPConnection, WeakReference<InvitationsMonitor>> monitors = new WeakHashMap();
        private XMPPConnection connection;
        private PacketFilter invitationFilter;
        private PacketListener invitationPacketListener;
        private final List<InvitationListener> invitationsListeners = new ArrayList();

        public static InvitationsMonitor getInvitationsMonitor(XMPPConnection conn) {
            InvitationsMonitor invitationsMonitor;
            synchronized (monitors) {
                if (!monitors.containsKey(conn)) {
                    monitors.put(conn, new WeakReference(new InvitationsMonitor(conn)));
                }
                invitationsMonitor = (InvitationsMonitor) ((WeakReference) monitors.get(conn)).get();
            }
            return invitationsMonitor;
        }

        private InvitationsMonitor(XMPPConnection connection) {
            this.connection = connection;
        }

        public void addInvitationListener(InvitationListener listener) {
            synchronized (this.invitationsListeners) {
                if (this.invitationsListeners.size() == 0) {
                    init();
                }
                if (!this.invitationsListeners.contains(listener)) {
                    this.invitationsListeners.add(listener);
                }
            }
        }

        public void removeInvitationListener(InvitationListener listener) {
            synchronized (this.invitationsListeners) {
                if (this.invitationsListeners.contains(listener)) {
                    this.invitationsListeners.remove(listener);
                }
                if (this.invitationsListeners.size() == 0) {
                    cancel();
                }
            }
        }

        /* access modifiers changed from: private */
        public void fireInvitationListeners(String room, String inviter, String reason, String password, Message message) {
            synchronized (this.invitationsListeners) {
                InvitationListener[] listeners = new InvitationListener[this.invitationsListeners.size()];
                this.invitationsListeners.toArray(listeners);
            }
            for (InvitationListener listener : listeners) {
                listener.invitationReceived(this.connection, room, inviter, reason, password, message);
            }
        }

        public void connectionClosed() {
            cancel();
        }

        public void connectionClosedOnError(Exception e) {
        }

        public void reconnectingIn(int seconds) {
        }

        public void reconnectionSuccessful() {
        }

        public void reconnectionFailed(Exception e) {
        }

        private void init() {
            this.invitationFilter = new PacketExtensionFilter(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
            this.invitationPacketListener = new PacketListener() {
                public void processPacket(Packet packet) {
                    MUCUser mucUser = (MUCUser) packet.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
                    if (mucUser.getInvite() != null && ((Message) packet).getType() != Type.error) {
                        InvitationsMonitor.this.fireInvitationListeners(packet.getFrom(), mucUser.getInvite().getFrom(), mucUser.getInvite().getReason(), mucUser.getPassword(), (Message) packet);
                    }
                }
            };
            this.connection.addPacketListener(this.invitationPacketListener, this.invitationFilter);
            this.connection.addConnectionListener(this);
        }

        private void cancel() {
            this.connection.removePacketListener(this.invitationPacketListener);
            this.connection.removeConnectionListener(this);
        }
    }

    static {
        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {
            public void connectionCreated(final XMPPConnection connection) {
                ServiceDiscoveryManager.getInstanceFor(connection).addFeature(MultiUserChat.discoNamespace);
                ServiceDiscoveryManager.getInstanceFor(connection).setNodeInformationProvider(MultiUserChat.discoNode, new NodeInformationProvider() {
                    public List<Item> getNodeItems() {
                        List<Item> answer = new ArrayList();
                        Iterator<String> rooms = MultiUserChat.getJoinedRooms(connection);
                        while (rooms.hasNext()) {
                            answer.add(new Item((String) rooms.next()));
                        }
                        return answer;
                    }

                    public List<String> getNodeFeatures() {
                        return null;
                    }

                    public List<Identity> getNodeIdentities() {
                        return null;
                    }
                });
            }
        });
    }

    public MultiUserChat(XMPPConnection connection, String room) {
        this.connection = connection;
        this.room = room.toLowerCase();
        init();
    }

    public static boolean isServiceEnabled(XMPPConnection connection, String user) {
        try {
            return ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(user).containsFeature(discoNamespace);
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* access modifiers changed from: private|static */
    public static Iterator<String> getJoinedRooms(XMPPConnection connection) {
        List<String> rooms = (List) joinedRooms.get(connection);
        if (rooms != null) {
            return rooms.iterator();
        }
        return new ArrayList().iterator();
    }

    public static Iterator<String> getJoinedRooms(XMPPConnection connection, String user) {
        try {
            ArrayList<String> answer = new ArrayList();
            Iterator<Item> items = ServiceDiscoveryManager.getInstanceFor(connection).discoverItems(user, discoNode).getItems();
            while (items.hasNext()) {
                answer.add(((Item) items.next()).getEntityID());
            }
            return answer.iterator();
        } catch (XMPPException e) {
            e.printStackTrace();
            return new ArrayList().iterator();
        }
    }

    public static RoomInfo getRoomInfo(XMPPConnection connection, String room) throws XMPPException {
        return new RoomInfo(ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(room));
    }

    public static Collection<String> getServiceNames(XMPPConnection connection) throws XMPPException {
        List<String> answer = new ArrayList();
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
        Iterator<Item> it = discoManager.discoverItems(connection.getServiceName()).getItems();
        while (it.hasNext()) {
            Item item = (Item) it.next();
            try {
                if (discoManager.discoverInfo(item.getEntityID()).containsFeature(discoNamespace)) {
                    answer.add(item.getEntityID());
                }
            } catch (XMPPException e) {
            }
        }
        return answer;
    }

    public static Collection<HostedRoom> getHostedRooms(XMPPConnection connection, String serviceName) throws XMPPException {
        List<HostedRoom> answer = new ArrayList();
        Iterator<Item> it = ServiceDiscoveryManager.getInstanceFor(connection).discoverItems(serviceName).getItems();
        while (it.hasNext()) {
            answer.add(new HostedRoom((Item) it.next()));
        }
        return answer;
    }

    public String getRoom() {
        return this.room;
    }

    public synchronized void create(String nickname) throws XMPPException {
        if (nickname != null) {
            if (!nickname.equals("")) {
                if (this.joined) {
                    throw new IllegalStateException("Creation failed - User already joined the room.");
                }
                Presence joinPresence = new Presence(Presence.Type.available);
                joinPresence.setTo(this.room + "/" + nickname);
                joinPresence.addExtension(new MUCInitialPresence());
                for (PacketInterceptor packetInterceptor : this.presenceInterceptors) {
                    packetInterceptor.interceptPacket(joinPresence);
                }
                PacketCollector response = this.connection.createPacketCollector(new AndFilter(new FromMatchesFilter(this.room + "/" + nickname), new PacketTypeFilter(Presence.class)));
                this.connection.sendPacket(joinPresence);
                Presence presence = (Presence) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
                response.cancel();
                if (presence == null) {
                    throw new XMPPException("No response from server.");
                } else if (presence.getError() != null) {
                    throw new XMPPException(presence.getError());
                } else {
                    this.nickname = nickname;
                    this.joined = true;
                    userHasJoined();
                    MUCUser mucUser = getMUCUserExtension(presence);
                    if (mucUser == null || mucUser.getStatus() == null || !"201".equals(mucUser.getStatus().getCode())) {
                        leave();
                        throw new XMPPException("Creation failed - Missing acknowledge of room creation.");
                    }
                }
            }
        }
        throw new IllegalArgumentException("Nickname must not be null or blank.");
    }

    public void join(String nickname) throws XMPPException {
        join(nickname, null, null, (long) SmackConfiguration.getPacketReplyTimeout());
    }

    public void join(String nickname, String password) throws XMPPException {
        join(nickname, password, null, (long) SmackConfiguration.getPacketReplyTimeout());
    }

    public synchronized void join(String nickname, String password, DiscussionHistory history, long timeout) throws XMPPException {
        if (nickname != null) {
            if (!nickname.equals("")) {
                if (this.joined) {
                    leave();
                }
                Presence joinPresence = new Presence(Presence.Type.available);
                joinPresence.setTo(this.room + "/" + nickname);
                MUCInitialPresence mucInitialPresence = new MUCInitialPresence();
                if (password != null) {
                    mucInitialPresence.setPassword(password);
                }
                if (history != null) {
                    mucInitialPresence.setHistory(history.getMUCHistory());
                }
                joinPresence.addExtension(mucInitialPresence);
                for (PacketInterceptor packetInterceptor : this.presenceInterceptors) {
                    packetInterceptor.interceptPacket(joinPresence);
                }
                PacketCollector response = null;
                try {
                    response = this.connection.createPacketCollector(new AndFilter(new FromMatchesFilter(this.room + "/" + nickname), new PacketTypeFilter(Presence.class)));
                    this.connection.sendPacket(joinPresence);
                    Presence presence = (Presence) response.nextResult(timeout);
                    if (response != null) {
                        response.cancel();
                    }
                    if (presence == null) {
                        throw new XMPPException("No response from server.");
                    } else if (presence.getError() != null) {
                        throw new XMPPException(presence.getError());
                    } else {
                        this.nickname = nickname;
                        this.joined = true;
                        userHasJoined();
                    }
                } catch (Throwable th) {
                    if (response != null) {
                        response.cancel();
                    }
                }
            }
        }
        throw new IllegalArgumentException("Nickname must not be null or blank.");
    }

    public boolean isJoined() {
        return this.joined;
    }

    public synchronized void leave() {
        if (this.joined) {
            Presence leavePresence = new Presence(Presence.Type.unavailable);
            leavePresence.setTo(this.room + "/" + this.nickname);
            for (PacketInterceptor packetInterceptor : this.presenceInterceptors) {
                packetInterceptor.interceptPacket(leavePresence);
            }
            this.connection.sendPacket(leavePresence);
            this.occupantsMap.clear();
            this.nickname = null;
            this.joined = false;
            userHasLeft();
        }
    }

    public Form getConfigurationForm() throws XMPPException {
        MUCOwner iq = new MUCOwner();
        iq.setTo(this.room);
        iq.setType(IQ.Type.GET);
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() == null) {
            return Form.getFormFrom(answer);
        } else {
            throw new XMPPException(answer.getError());
        }
    }

    public void sendConfigurationForm(Form form) throws XMPPException {
        MUCOwner iq = new MUCOwner();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        iq.addExtension(form.getDataFormToSend());
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    public Form getRegistrationForm() throws XMPPException {
        Registration reg = new Registration();
        reg.setType(IQ.Type.GET);
        reg.setTo(this.room);
        PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
        this.connection.sendPacket(reg);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        } else if (result.getType() != IQ.Type.ERROR) {
            return Form.getFormFrom(result);
        } else {
            throw new XMPPException(result.getError());
        }
    }

    public void sendRegistrationForm(Form form) throws XMPPException {
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        reg.setTo(this.room);
        reg.addExtension(form.getDataFormToSend());
        PacketCollector collector = this.connection.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
        this.connection.sendPacket(reg);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from server.");
        } else if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
    }

    public void destroy(String reason, String alternateJID) throws XMPPException {
        MUCOwner iq = new MUCOwner();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        Destroy destroy = new Destroy();
        destroy.setReason(reason);
        destroy.setJid(alternateJID);
        iq.setDestroy(destroy);
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        } else {
            this.occupantsMap.clear();
            this.nickname = null;
            this.joined = false;
            userHasLeft();
        }
    }

    public void invite(String user, String reason) {
        invite(new Message(), user, reason);
    }

    public void invite(Message message, String user, String reason) {
        message.setTo(this.room);
        MUCUser mucUser = new MUCUser();
        Invite invite = new Invite();
        invite.setTo(user);
        invite.setReason(reason);
        mucUser.setInvite(invite);
        message.addExtension(mucUser);
        this.connection.sendPacket(message);
    }

    public static void decline(XMPPConnection conn, String room, String inviter, String reason) {
        Message message = new Message(room);
        MUCUser mucUser = new MUCUser();
        Decline decline = new Decline();
        decline.setTo(inviter);
        decline.setReason(reason);
        mucUser.setDecline(decline);
        message.addExtension(mucUser);
        conn.sendPacket(message);
    }

    public static void addInvitationListener(XMPPConnection conn, InvitationListener listener) {
        InvitationsMonitor.getInvitationsMonitor(conn).addInvitationListener(listener);
    }

    public static void removeInvitationListener(XMPPConnection conn, InvitationListener listener) {
        InvitationsMonitor.getInvitationsMonitor(conn).removeInvitationListener(listener);
    }

    public void addInvitationRejectionListener(InvitationRejectionListener listener) {
        synchronized (this.invitationRejectionListeners) {
            if (!this.invitationRejectionListeners.contains(listener)) {
                this.invitationRejectionListeners.add(listener);
            }
        }
    }

    public void removeInvitationRejectionListener(InvitationRejectionListener listener) {
        synchronized (this.invitationRejectionListeners) {
            this.invitationRejectionListeners.remove(listener);
        }
    }

    /* access modifiers changed from: private */
    public void fireInvitationRejectionListeners(String invitee, String reason) {
        synchronized (this.invitationRejectionListeners) {
            InvitationRejectionListener[] listeners = new InvitationRejectionListener[this.invitationRejectionListeners.size()];
            this.invitationRejectionListeners.toArray(listeners);
        }
        for (InvitationRejectionListener listener : listeners) {
            listener.invitationDeclined(invitee, reason);
        }
    }

    public void addSubjectUpdatedListener(SubjectUpdatedListener listener) {
        synchronized (this.subjectUpdatedListeners) {
            if (!this.subjectUpdatedListeners.contains(listener)) {
                this.subjectUpdatedListeners.add(listener);
            }
        }
    }

    public void removeSubjectUpdatedListener(SubjectUpdatedListener listener) {
        synchronized (this.subjectUpdatedListeners) {
            this.subjectUpdatedListeners.remove(listener);
        }
    }

    /* access modifiers changed from: private */
    public void fireSubjectUpdatedListeners(String subject, String from) {
        synchronized (this.subjectUpdatedListeners) {
            SubjectUpdatedListener[] listeners = new SubjectUpdatedListener[this.subjectUpdatedListeners.size()];
            this.subjectUpdatedListeners.toArray(listeners);
        }
        for (SubjectUpdatedListener listener : listeners) {
            listener.subjectUpdated(subject, from);
        }
    }

    public void addPresenceInterceptor(PacketInterceptor presenceInterceptor) {
        this.presenceInterceptors.add(presenceInterceptor);
    }

    public void removePresenceInterceptor(PacketInterceptor presenceInterceptor) {
        this.presenceInterceptors.remove(presenceInterceptor);
    }

    public String getSubject() {
        return this.subject;
    }

    public String getReservedNickname() {
        try {
            Iterator<Identity> identities = ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(this.room, "x-roomuser-item").getIdentities();
            if (identities.hasNext()) {
                return ((Identity) identities.next()).getName();
            }
            return null;
        } catch (XMPPException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    public void changeNickname(String nickname) throws XMPPException {
        if (nickname == null || nickname.equals("")) {
            throw new IllegalArgumentException("Nickname must not be null or blank.");
        } else if (this.joined) {
            Presence joinPresence = new Presence(Presence.Type.available);
            joinPresence.setTo(this.room + "/" + nickname);
            for (PacketInterceptor packetInterceptor : this.presenceInterceptors) {
                packetInterceptor.interceptPacket(joinPresence);
            }
            PacketCollector response = this.connection.createPacketCollector(new AndFilter(new FromMatchesFilter(this.room + "/" + nickname), new PacketTypeFilter(Presence.class)));
            this.connection.sendPacket(joinPresence);
            Presence presence = (Presence) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            response.cancel();
            if (presence == null) {
                throw new XMPPException("No response from server.");
            } else if (presence.getError() != null) {
                throw new XMPPException(presence.getError());
            } else {
                this.nickname = nickname;
            }
        } else {
            throw new IllegalStateException("Must be logged into the room to change nickname.");
        }
    }

    public void changeAvailabilityStatus(String status, Mode mode) {
        if (this.nickname == null || this.nickname.equals("")) {
            throw new IllegalArgumentException("Nickname must not be null or blank.");
        } else if (this.joined) {
            Presence joinPresence = new Presence(Presence.Type.available);
            joinPresence.setStatus(status);
            joinPresence.setMode(mode);
            joinPresence.setTo(this.room + "/" + this.nickname);
            for (PacketInterceptor packetInterceptor : this.presenceInterceptors) {
                packetInterceptor.interceptPacket(joinPresence);
            }
            this.connection.sendPacket(joinPresence);
        } else {
            throw new IllegalStateException("Must be logged into the room to change the availability status.");
        }
    }

    public void kickParticipant(String nickname, String reason) throws XMPPException {
        changeRole(nickname, PrivacyRule.SUBSCRIPTION_NONE, reason);
    }

    public void grantVoice(Collection<String> nicknames) throws XMPPException {
        changeRole(nicknames, "participant");
    }

    public void grantVoice(String nickname) throws XMPPException {
        changeRole(nickname, "participant", null);
    }

    public void revokeVoice(Collection<String> nicknames) throws XMPPException {
        changeRole(nicknames, "visitor");
    }

    public void revokeVoice(String nickname) throws XMPPException {
        changeRole(nickname, "visitor", null);
    }

    public void banUsers(Collection<String> jids) throws XMPPException {
        changeAffiliationByAdmin(jids, "outcast");
    }

    public void banUser(String jid, String reason) throws XMPPException {
        changeAffiliationByAdmin(jid, "outcast", reason);
    }

    public void grantMembership(Collection<String> jids) throws XMPPException {
        changeAffiliationByAdmin(jids, "member");
    }

    public void grantMembership(String jid) throws XMPPException {
        changeAffiliationByAdmin(jid, "member", null);
    }

    public void revokeMembership(Collection<String> jids) throws XMPPException {
        changeAffiliationByAdmin(jids, PrivacyRule.SUBSCRIPTION_NONE);
    }

    public void revokeMembership(String jid) throws XMPPException {
        changeAffiliationByAdmin(jid, PrivacyRule.SUBSCRIPTION_NONE, null);
    }

    public void grantModerator(Collection<String> nicknames) throws XMPPException {
        changeRole(nicknames, "moderator");
    }

    public void grantModerator(String nickname) throws XMPPException {
        changeRole(nickname, "moderator", null);
    }

    public void revokeModerator(Collection<String> nicknames) throws XMPPException {
        changeRole(nicknames, "participant");
    }

    public void revokeModerator(String nickname) throws XMPPException {
        changeRole(nickname, "participant", null);
    }

    public void grantOwnership(Collection<String> jids) throws XMPPException {
        changeAffiliationByOwner((Collection) jids, "owner");
    }

    public void grantOwnership(String jid) throws XMPPException {
        changeAffiliationByOwner(jid, "owner");
    }

    public void revokeOwnership(Collection<String> jids) throws XMPPException {
        changeAffiliationByOwner((Collection) jids, "admin");
    }

    public void revokeOwnership(String jid) throws XMPPException {
        changeAffiliationByOwner(jid, "admin");
    }

    public void grantAdmin(Collection<String> jids) throws XMPPException {
        changeAffiliationByOwner((Collection) jids, "admin");
    }

    public void grantAdmin(String jid) throws XMPPException {
        changeAffiliationByOwner(jid, "admin");
    }

    public void revokeAdmin(Collection<String> jids) throws XMPPException {
        changeAffiliationByOwner((Collection) jids, "member");
    }

    public void revokeAdmin(String jid) throws XMPPException {
        changeAffiliationByOwner(jid, "member");
    }

    private void changeAffiliationByOwner(String jid, String affiliation) throws XMPPException {
        MUCOwner iq = new MUCOwner();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        MUCOwner.Item item = new MUCOwner.Item(affiliation);
        item.setJid(jid);
        iq.addItem(item);
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    private void changeAffiliationByOwner(Collection<String> jids, String affiliation) throws XMPPException {
        MUCOwner iq = new MUCOwner();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        for (String jid : jids) {
            MUCOwner.Item item = new MUCOwner.Item(affiliation);
            item.setJid(jid);
            iq.addItem(item);
        }
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    private void changeAffiliationByAdmin(String jid, String affiliation, String reason) throws XMPPException {
        MUCAdmin iq = new MUCAdmin();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        MUCAdmin.Item item = new MUCAdmin.Item(affiliation, null);
        item.setJid(jid);
        item.setReason(reason);
        iq.addItem(item);
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    private void changeAffiliationByAdmin(Collection<String> jids, String affiliation) throws XMPPException {
        MUCAdmin iq = new MUCAdmin();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        for (String jid : jids) {
            MUCAdmin.Item item = new MUCAdmin.Item(affiliation, null);
            item.setJid(jid);
            iq.addItem(item);
        }
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    private void changeRole(String nickname, String role, String reason) throws XMPPException {
        MUCAdmin iq = new MUCAdmin();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        MUCAdmin.Item item = new MUCAdmin.Item(null, role);
        item.setNick(nickname);
        item.setReason(reason);
        iq.addItem(item);
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    private void changeRole(Collection<String> nicknames, String role) throws XMPPException {
        MUCAdmin iq = new MUCAdmin();
        iq.setTo(this.room);
        iq.setType(IQ.Type.SET);
        for (String nickname : nicknames) {
            MUCAdmin.Item item = new MUCAdmin.Item(null, role);
            item.setNick(nickname);
            iq.addItem(item);
        }
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        IQ answer = (IQ) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    public int getOccupantsCount() {
        return this.occupantsMap.size();
    }

    public Iterator<String> getOccupants() {
        return Collections.unmodifiableList(new ArrayList(this.occupantsMap.keySet())).iterator();
    }

    public Presence getOccupantPresence(String user) {
        return (Presence) this.occupantsMap.get(user);
    }

    public Occupant getOccupant(String user) {
        Presence presence = (Presence) this.occupantsMap.get(user);
        if (presence != null) {
            return new Occupant(presence);
        }
        return null;
    }

    public void addParticipantListener(PacketListener listener) {
        this.connection.addPacketListener(listener, this.presenceFilter);
        this.connectionListeners.add(listener);
    }

    public void removeParticipantListener(PacketListener listener) {
        this.connection.removePacketListener(listener);
        this.connectionListeners.remove(listener);
    }

    public Collection<Affiliate> getOwners() throws XMPPException {
        return getAffiliatesByOwner("owner");
    }

    public Collection<Affiliate> getAdmins() throws XMPPException {
        return getAffiliatesByOwner("admin");
    }

    public Collection<Affiliate> getMembers() throws XMPPException {
        return getAffiliatesByAdmin("member");
    }

    public Collection<Affiliate> getOutcasts() throws XMPPException {
        return getAffiliatesByAdmin("outcast");
    }

    private Collection<Affiliate> getAffiliatesByOwner(String affiliation) throws XMPPException {
        MUCOwner iq = new MUCOwner();
        iq.setTo(this.room);
        iq.setType(IQ.Type.GET);
        iq.addItem(new MUCOwner.Item(affiliation));
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        MUCOwner answer = (MUCOwner) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        } else {
            List<Affiliate> affiliates = new ArrayList();
            Iterator it = answer.getItems();
            while (it.hasNext()) {
                affiliates.add(new Affiliate((MUCOwner.Item) it.next()));
            }
            return affiliates;
        }
    }

    private Collection<Affiliate> getAffiliatesByAdmin(String affiliation) throws XMPPException {
        MUCAdmin iq = new MUCAdmin();
        iq.setTo(this.room);
        iq.setType(IQ.Type.GET);
        iq.addItem(new MUCAdmin.Item(affiliation, null));
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        MUCAdmin answer = (MUCAdmin) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        } else {
            List<Affiliate> affiliates = new ArrayList();
            Iterator it = answer.getItems();
            while (it.hasNext()) {
                affiliates.add(new Affiliate((MUCAdmin.Item) it.next()));
            }
            return affiliates;
        }
    }

    public Collection<Occupant> getModerators() throws XMPPException {
        return getOccupants("moderator");
    }

    public Collection<Occupant> getParticipants() throws XMPPException {
        return getOccupants("participant");
    }

    private Collection<Occupant> getOccupants(String role) throws XMPPException {
        MUCAdmin iq = new MUCAdmin();
        iq.setTo(this.room);
        iq.setType(IQ.Type.GET);
        iq.addItem(new MUCAdmin.Item(null, role));
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(iq.getPacketID()));
        this.connection.sendPacket(iq);
        MUCAdmin answer = (MUCAdmin) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        } else {
            List<Occupant> participants = new ArrayList();
            Iterator it = answer.getItems();
            while (it.hasNext()) {
                participants.add(new Occupant((MUCAdmin.Item) it.next()));
            }
            return participants;
        }
    }

    public void sendMessage(String text) throws XMPPException {
        Message message = new Message(this.room, Type.groupchat);
        message.setBody(text);
        this.connection.sendPacket(message);
    }

    public Chat createPrivateChat(String occupant, MessageListener listener) {
        return this.connection.getChatManager().createChat(occupant, listener);
    }

    public Message createMessage() {
        return new Message(this.room, Type.groupchat);
    }

    public void sendMessage(Message message) throws XMPPException {
        this.connection.sendPacket(message);
    }

    public Message pollMessage() {
        return (Message) this.messageCollector.pollResult();
    }

    public Message nextMessage() {
        return (Message) this.messageCollector.nextResult();
    }

    public Message nextMessage(long timeout) {
        return (Message) this.messageCollector.nextResult(timeout);
    }

    public void addMessageListener(PacketListener listener) {
        this.connection.addPacketListener(listener, this.messageFilter);
        this.connectionListeners.add(listener);
    }

    public void removeMessageListener(PacketListener listener) {
        this.connection.removePacketListener(listener);
        this.connectionListeners.remove(listener);
    }

    public void changeSubject(final String subject) throws XMPPException {
        Message message = new Message(this.room, Type.groupchat);
        message.setSubject(subject);
        PacketFilter responseFilter = new AndFilter(new FromMatchesFilter(this.room), new PacketTypeFilter(Message.class));
        PacketCollector response = this.connection.createPacketCollector(new AndFilter(responseFilter, new PacketFilter() {
            public boolean accept(Packet packet) {
                return subject.equals(((Message) packet).getSubject());
            }
        }));
        this.connection.sendPacket(message);
        Message answer = (Message) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    private synchronized void userHasJoined() {
        List<String> rooms = (List) joinedRooms.get(this.connection);
        if (rooms == null) {
            rooms = new ArrayList();
            joinedRooms.put(this.connection, rooms);
        }
        rooms.add(this.room);
    }

    private synchronized void userHasLeft() {
        List<String> rooms = (List) joinedRooms.get(this.connection);
        if (rooms != null) {
            rooms.remove(this.room);
        }
    }

    /* access modifiers changed from: private */
    public MUCUser getMUCUserExtension(Packet packet) {
        if (packet != null) {
            return (MUCUser) packet.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
        }
        return null;
    }

    public void addUserStatusListener(UserStatusListener listener) {
        synchronized (this.userStatusListeners) {
            if (!this.userStatusListeners.contains(listener)) {
                this.userStatusListeners.add(listener);
            }
        }
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        synchronized (this.userStatusListeners) {
            this.userStatusListeners.remove(listener);
        }
    }

    private void fireUserStatusListeners(String methodName, Object[] params) {
        synchronized (this.userStatusListeners) {
            UserStatusListener[] listeners = new UserStatusListener[this.userStatusListeners.size()];
            this.userStatusListeners.toArray(listeners);
        }
        Class[] paramClasses = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramClasses[i] = params[i].getClass();
        }
        try {
            Method method = UserStatusListener.class.getDeclaredMethod(methodName, paramClasses);
            for (UserStatusListener listener : listeners) {
                method.invoke(listener, params);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    public void addParticipantStatusListener(ParticipantStatusListener listener) {
        synchronized (this.participantStatusListeners) {
            if (!this.participantStatusListeners.contains(listener)) {
                this.participantStatusListeners.add(listener);
            }
        }
    }

    public void removeParticipantStatusListener(ParticipantStatusListener listener) {
        synchronized (this.participantStatusListeners) {
            this.participantStatusListeners.remove(listener);
        }
    }

    /* access modifiers changed from: private */
    public void fireParticipantStatusListeners(String methodName, List<String> params) {
        synchronized (this.participantStatusListeners) {
            ParticipantStatusListener[] listeners = new ParticipantStatusListener[this.participantStatusListeners.size()];
            this.participantStatusListeners.toArray(listeners);
        }
        try {
            Class[] classes = new Class[params.size()];
            for (int i = 0; i < params.size(); i++) {
                classes[i] = String.class;
            }
            Method method = ParticipantStatusListener.class.getDeclaredMethod(methodName, classes);
            for (ParticipantStatusListener listener : listeners) {
                method.invoke(listener, params.toArray());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    private void init() {
        this.messageFilter = new AndFilter(new FromMatchesFilter(this.room), new MessageTypeFilter(Type.groupchat));
        this.messageFilter = new AndFilter(this.messageFilter, new PacketFilter() {
            public boolean accept(Packet packet) {
                return ((Message) packet).getBody() != null;
            }
        });
        this.presenceFilter = new AndFilter(new FromMatchesFilter(this.room), new PacketTypeFilter(Presence.class));
        this.messageCollector = new ConnectionDetachedPacketCollector();
        PacketListener subjectListener = new PacketListener() {
            public void processPacket(Packet packet) {
                Message msg = (Message) packet;
                MultiUserChat.this.subject = msg.getSubject();
                MultiUserChat.this.fireSubjectUpdatedListeners(msg.getSubject(), msg.getFrom());
            }
        };
        PacketMultiplexListener packetMultiplexor = new PacketMultiplexListener(this.messageCollector, new PacketListener() {
            public void processPacket(Packet packet) {
                Presence presence = (Presence) packet;
                String from = presence.getFrom();
                String myRoomJID = MultiUserChat.this.room + "/" + MultiUserChat.this.nickname;
                boolean isUserStatusModification = presence.getFrom().equals(myRoomJID);
                List<String> params;
                if (presence.getType() == Presence.Type.available) {
                    Presence oldPresence = (Presence) MultiUserChat.this.occupantsMap.put(from, presence);
                    if (oldPresence != null) {
                        MUCUser mucExtension = MultiUserChat.this.getMUCUserExtension(oldPresence);
                        String oldAffiliation = mucExtension.getItem().getAffiliation();
                        String oldRole = mucExtension.getItem().getRole();
                        mucExtension = MultiUserChat.this.getMUCUserExtension(presence);
                        String newAffiliation = mucExtension.getItem().getAffiliation();
                        MultiUserChat.this.checkRoleModifications(oldRole, mucExtension.getItem().getRole(), isUserStatusModification, from);
                        MultiUserChat.this.checkAffiliationModifications(oldAffiliation, newAffiliation, isUserStatusModification, from);
                    } else if (!isUserStatusModification) {
                        params = new ArrayList();
                        params.add(from);
                        MultiUserChat.this.fireParticipantStatusListeners("joined", params);
                    }
                } else if (presence.getType() == Presence.Type.unavailable) {
                    MultiUserChat.this.occupantsMap.remove(from);
                    MUCUser mucUser = MultiUserChat.this.getMUCUserExtension(presence);
                    if (mucUser != null && mucUser.getStatus() != null) {
                        MultiUserChat.this.checkPresenceCode(mucUser.getStatus().getCode(), presence.getFrom().equals(myRoomJID), mucUser, from);
                    } else if (!isUserStatusModification) {
                        params = new ArrayList();
                        params.add(from);
                        MultiUserChat.this.fireParticipantStatusListeners("left", params);
                    }
                }
            }
        }, subjectListener, new PacketListener() {
            public void processPacket(Packet packet) {
                MUCUser mucUser = MultiUserChat.this.getMUCUserExtension(packet);
                if (mucUser.getDecline() != null && ((Message) packet).getType() != Type.error) {
                    MultiUserChat.this.fireInvitationRejectionListeners(mucUser.getDecline().getFrom(), mucUser.getDecline().getReason());
                }
            }
        });
        this.roomListenerMultiplexor = RoomListenerMultiplexor.getRoomMultiplexor(this.connection);
        this.roomListenerMultiplexor.addRoom(this.room, packetMultiplexor);
    }

    /* access modifiers changed from: private */
    public void checkRoleModifications(String oldRole, String newRole, boolean isUserModification, String from) {
        List<String> params;
        if (("visitor".equals(oldRole) || PrivacyRule.SUBSCRIPTION_NONE.equals(oldRole)) && "participant".equals(newRole)) {
            if (isUserModification) {
                fireUserStatusListeners("voiceGranted", new Object[0]);
            } else {
                params = new ArrayList();
                params.add(from);
                fireParticipantStatusListeners("voiceGranted", params);
            }
        } else if ("participant".equals(oldRole) && ("visitor".equals(newRole) || PrivacyRule.SUBSCRIPTION_NONE.equals(newRole))) {
            if (isUserModification) {
                fireUserStatusListeners("voiceRevoked", new Object[0]);
            } else {
                params = new ArrayList();
                params.add(from);
                fireParticipantStatusListeners("voiceRevoked", params);
            }
        }
        if (!"moderator".equals(oldRole) && "moderator".equals(newRole)) {
            if ("visitor".equals(oldRole) || PrivacyRule.SUBSCRIPTION_NONE.equals(oldRole)) {
                if (isUserModification) {
                    fireUserStatusListeners("voiceGranted", new Object[0]);
                } else {
                    params = new ArrayList();
                    params.add(from);
                    fireParticipantStatusListeners("voiceGranted", params);
                }
            }
            if (isUserModification) {
                fireUserStatusListeners("moderatorGranted", new Object[0]);
                return;
            }
            params = new ArrayList();
            params.add(from);
            fireParticipantStatusListeners("moderatorGranted", params);
        } else if ("moderator".equals(oldRole) && !"moderator".equals(newRole)) {
            if ("visitor".equals(newRole) || PrivacyRule.SUBSCRIPTION_NONE.equals(newRole)) {
                if (isUserModification) {
                    fireUserStatusListeners("voiceRevoked", new Object[0]);
                } else {
                    params = new ArrayList();
                    params.add(from);
                    fireParticipantStatusListeners("voiceRevoked", params);
                }
            }
            if (isUserModification) {
                fireUserStatusListeners("moderatorRevoked", new Object[0]);
                return;
            }
            params = new ArrayList();
            params.add(from);
            fireParticipantStatusListeners("moderatorRevoked", params);
        }
    }

    /* access modifiers changed from: private */
    public void checkAffiliationModifications(String oldAffiliation, String newAffiliation, boolean isUserModification, String from) {
        List<String> params;
        if (!"owner".equals(oldAffiliation) || "owner".equals(newAffiliation)) {
            if (!"admin".equals(oldAffiliation) || "admin".equals(newAffiliation)) {
                if ("member".equals(oldAffiliation) && !"member".equals(newAffiliation)) {
                    if (isUserModification) {
                        fireUserStatusListeners("membershipRevoked", new Object[0]);
                    } else {
                        params = new ArrayList();
                        params.add(from);
                        fireParticipantStatusListeners("membershipRevoked", params);
                    }
                }
            } else if (isUserModification) {
                fireUserStatusListeners("adminRevoked", new Object[0]);
            } else {
                params = new ArrayList();
                params.add(from);
                fireParticipantStatusListeners("adminRevoked", params);
            }
        } else if (isUserModification) {
            fireUserStatusListeners("ownershipRevoked", new Object[0]);
        } else {
            params = new ArrayList();
            params.add(from);
            fireParticipantStatusListeners("ownershipRevoked", params);
        }
        if ("owner".equals(oldAffiliation) || !"owner".equals(newAffiliation)) {
            if ("admin".equals(oldAffiliation) || !"admin".equals(newAffiliation)) {
                if (!"member".equals(oldAffiliation) && "member".equals(newAffiliation)) {
                    if (isUserModification) {
                        fireUserStatusListeners("membershipGranted", new Object[0]);
                        return;
                    }
                    params = new ArrayList();
                    params.add(from);
                    fireParticipantStatusListeners("membershipGranted", params);
                }
            } else if (isUserModification) {
                fireUserStatusListeners("adminGranted", new Object[0]);
            } else {
                params = new ArrayList();
                params.add(from);
                fireParticipantStatusListeners("adminGranted", params);
            }
        } else if (isUserModification) {
            fireUserStatusListeners("ownershipGranted", new Object[0]);
        } else {
            params = new ArrayList();
            params.add(from);
            fireParticipantStatusListeners("ownershipGranted", params);
        }
    }

    /* access modifiers changed from: private */
    public void checkPresenceCode(String code, boolean isUserModification, MUCUser mucUser, String from) {
        List<String> params;
        if ("307".equals(code)) {
            if (isUserModification) {
                this.joined = false;
                fireUserStatusListeners("kicked", new Object[]{mucUser.getItem().getActor(), mucUser.getItem().getReason()});
                this.occupantsMap.clear();
                this.nickname = null;
                userHasLeft();
                return;
            }
            params = new ArrayList();
            params.add(from);
            params.add(mucUser.getItem().getActor());
            params.add(mucUser.getItem().getReason());
            fireParticipantStatusListeners("kicked", params);
        } else if ("301".equals(code)) {
            if (isUserModification) {
                this.joined = false;
                fireUserStatusListeners("banned", new Object[]{mucUser.getItem().getActor(), mucUser.getItem().getReason()});
                this.occupantsMap.clear();
                this.nickname = null;
                userHasLeft();
                return;
            }
            params = new ArrayList();
            params.add(from);
            params.add(mucUser.getItem().getActor());
            params.add(mucUser.getItem().getReason());
            fireParticipantStatusListeners("banned", params);
        } else if ("321".equals(code)) {
            if (isUserModification) {
                this.joined = false;
                fireUserStatusListeners("membershipRevoked", new Object[0]);
                this.occupantsMap.clear();
                this.nickname = null;
                userHasLeft();
            }
        } else if ("303".equals(code)) {
            params = new ArrayList();
            params.add(from);
            params.add(mucUser.getItem().getNick());
            fireParticipantStatusListeners("nicknameChanged", params);
        }
    }

    public void finalize() throws Throwable {
        super.finalize();
        try {
            if (this.connection != null) {
                this.roomListenerMultiplexor.removeRoom(this.room);
                for (PacketListener connectionListener : this.connectionListeners) {
                    this.connection.removePacketListener(connectionListener);
                }
            }
        } catch (Exception e) {
        }
    }
}
