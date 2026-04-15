package org.jivesoftware.smackx.workgroup.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitation;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitationListener;
import org.jivesoftware.smackx.workgroup.ext.forms.WorkgroupForm;
import org.jivesoftware.smackx.workgroup.packet.DepartQueuePacket;
import org.jivesoftware.smackx.workgroup.packet.QueueUpdate;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.jivesoftware.smackx.workgroup.packet.UserID;
import org.jivesoftware.smackx.workgroup.settings.ChatSetting;
import org.jivesoftware.smackx.workgroup.settings.ChatSettings;
import org.jivesoftware.smackx.workgroup.settings.OfflineSettings;
import org.jivesoftware.smackx.workgroup.settings.SoundSettings;
import org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties;

public class Workgroup {
    /* access modifiers changed from: private */
    public XMPPConnection connection;
    /* access modifiers changed from: private */
    public boolean inQueue;
    private List invitationListeners;
    private List queueListeners;
    /* access modifiers changed from: private */
    public int queuePosition = -1;
    /* access modifiers changed from: private */
    public int queueRemainingTime = -1;
    private List siteInviteListeners;
    private String workgroupJID;

    private class JoinQueuePacket extends IQ {
        private DataForm form;
        private String userID = null;

        public JoinQueuePacket(String workgroup, Form answerForm, String userID) {
            this.userID = userID;
            setTo(workgroup);
            setType(Type.SET);
            this.form = answerForm.getDataFormToSend();
            addExtension(this.form);
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<join-queue xmlns=\"http://jabber.org/protocol/workgroup\">");
            buf.append("<queue-notifications/>");
            if (Workgroup.this.connection.isAnonymous()) {
                buf.append(new UserID(this.userID).toXML());
            }
            buf.append(this.form.toXML());
            buf.append("</join-queue>");
            return buf.toString();
        }
    }

    public Workgroup(String workgroupJID, XMPPConnection connection) {
        if (connection.isAuthenticated()) {
            this.workgroupJID = workgroupJID;
            this.connection = connection;
            this.inQueue = false;
            this.invitationListeners = new ArrayList();
            this.queueListeners = new ArrayList();
            this.siteInviteListeners = new ArrayList();
            addQueueListener(new QueueListener() {
                public void joinedQueue() {
                    Workgroup.this.inQueue = true;
                }

                public void departedQueue() {
                    Workgroup.this.inQueue = false;
                    Workgroup.this.queuePosition = -1;
                    Workgroup.this.queueRemainingTime = -1;
                }

                public void queuePositionUpdated(int currentPosition) {
                    Workgroup.this.queuePosition = currentPosition;
                }

                public void queueWaitTimeUpdated(int secondsRemaining) {
                    Workgroup.this.queueRemainingTime = secondsRemaining;
                }
            });
            MultiUserChat.addInvitationListener(connection, new InvitationListener() {
                public void invitationReceived(XMPPConnection conn, String room, String inviter, String reason, String password, Message message) {
                    Workgroup.this.inQueue = false;
                    Workgroup.this.queuePosition = -1;
                    Workgroup.this.queueRemainingTime = -1;
                }
            });
            connection.addPacketListener(new PacketListener() {
                public void processPacket(Packet packet) {
                    Workgroup.this.handlePacket(packet);
                }
            }, new PacketTypeFilter(Message.class));
            return;
        }
        throw new IllegalStateException("Must login to server before creating workgroup.");
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public boolean isInQueue() {
        return this.inQueue;
    }

    public boolean isAvailable() {
        boolean z = true;
        Presence directedPresence = new Presence(Presence.Type.available);
        directedPresence.setTo(this.workgroupJID);
        PacketFilter typeFilter = new PacketTypeFilter(Presence.class);
        PacketFilter fromFilter = new FromContainsFilter(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new AndFilter(fromFilter, typeFilter));
        this.connection.sendPacket(directedPresence);
        Presence response = (Presence) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null || response.getError() != null) {
            return false;
        }
        if (Presence.Type.available != response.getType()) {
            z = false;
        }
        return z;
    }

    public int getQueuePosition() {
        return this.queuePosition;
    }

    public int getQueueRemainingTime() {
        return this.queueRemainingTime;
    }

    public void joinQueue() throws XMPPException {
        joinQueue(null);
    }

    public void joinQueue(Form answerForm) throws XMPPException {
        joinQueue(answerForm, null);
    }

    public void joinQueue(Form answerForm, String userID) throws XMPPException {
        if (this.inQueue) {
            throw new IllegalStateException("Already in queue " + this.workgroupJID);
        }
        JoinQueuePacket joinPacket = new JoinQueuePacket(this.workgroupJID, answerForm, userID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(joinPacket.getPacketID()));
        this.connection.sendPacket(joinPacket);
        IQ response = (IQ) collector.nextResult(10000);
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from the server.");
        } else if (response.getError() != null) {
            throw new XMPPException(response.getError());
        } else {
            fireQueueJoinedEvent();
        }
    }

    public void joinQueue(Map metadata, String userID) throws XMPPException {
        if (this.inQueue) {
            throw new IllegalStateException("Already in queue " + this.workgroupJID);
        }
        Form form = new Form(Form.TYPE_SUBMIT);
        for (String name : metadata.keySet()) {
            String value = metadata.get(name).toString();
            String escapedName = StringUtils.escapeForXML(name);
            String escapedValue = StringUtils.escapeForXML(value);
            FormField field = new FormField(escapedName);
            field.setType(FormField.TYPE_TEXT_SINGLE);
            form.addField(field);
            form.setAnswer(escapedName, escapedValue);
        }
        joinQueue(form, userID);
    }

    public void departQueue() throws XMPPException {
        if (this.inQueue) {
            DepartQueuePacket departPacket = new DepartQueuePacket(this.workgroupJID);
            PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(departPacket.getPacketID()));
            this.connection.sendPacket(departPacket);
            IQ response = (IQ) collector.nextResult(5000);
            collector.cancel();
            if (response == null) {
                throw new XMPPException("No response from the server.");
            } else if (response.getError() != null) {
                throw new XMPPException(response.getError());
            } else {
                fireQueueDepartedEvent();
            }
        }
    }

    public void addQueueListener(QueueListener queueListener) {
        synchronized (this.queueListeners) {
            if (!this.queueListeners.contains(queueListener)) {
                this.queueListeners.add(queueListener);
            }
        }
    }

    public void removeQueueListener(QueueListener queueListener) {
        synchronized (this.queueListeners) {
            this.queueListeners.remove(queueListener);
        }
    }

    public void addInvitationListener(WorkgroupInvitationListener invitationListener) {
        synchronized (this.invitationListeners) {
            if (!this.invitationListeners.contains(invitationListener)) {
                this.invitationListeners.add(invitationListener);
            }
        }
    }

    public void removeQueueListener(WorkgroupInvitationListener invitationListener) {
        synchronized (this.invitationListeners) {
            this.invitationListeners.remove(invitationListener);
        }
    }

    private void fireInvitationEvent(WorkgroupInvitation invitation) {
        synchronized (this.invitationListeners) {
            for (WorkgroupInvitationListener listener : this.invitationListeners) {
                listener.invitationReceived(invitation);
            }
        }
    }

    private void fireQueueJoinedEvent() {
        synchronized (this.queueListeners) {
            for (QueueListener listener : this.queueListeners) {
                listener.joinedQueue();
            }
        }
    }

    private void fireQueueDepartedEvent() {
        synchronized (this.queueListeners) {
            for (QueueListener listener : this.queueListeners) {
                listener.departedQueue();
            }
        }
    }

    private void fireQueuePositionEvent(int currentPosition) {
        synchronized (this.queueListeners) {
            for (QueueListener listener : this.queueListeners) {
                listener.queuePositionUpdated(currentPosition);
            }
        }
    }

    private void fireQueueTimeEvent(int secondsRemaining) {
        synchronized (this.queueListeners) {
            for (QueueListener listener : this.queueListeners) {
                listener.queueWaitTimeUpdated(secondsRemaining);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlePacket(Packet packet) {
        if (packet instanceof Message) {
            Message msg = (Message) packet;
            PacketExtension pe = msg.getExtension("depart-queue", "http://jabber.org/protocol/workgroup");
            PacketExtension queueStatus = msg.getExtension(QueueUpdate.ELEMENT_NAME, "http://jabber.org/protocol/workgroup");
            if (pe != null) {
                fireQueueDepartedEvent();
            } else if (queueStatus != null) {
                QueueUpdate queueUpdate = (QueueUpdate) queueStatus;
                if (queueUpdate.getPosition() != -1) {
                    fireQueuePositionEvent(queueUpdate.getPosition());
                }
                if (queueUpdate.getRemaingTime() != -1) {
                    fireQueueTimeEvent(queueUpdate.getRemaingTime());
                }
            } else {
                MUCUser mucUser = (MUCUser) msg.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
                Invite invite = mucUser != null ? mucUser.getInvite() : null;
                if (invite != null && this.workgroupJID.equals(invite.getFrom())) {
                    String sessionID = null;
                    Map metaData = null;
                    pe = msg.getExtension(SessionID.ELEMENT_NAME, "http://jivesoftware.com/protocol/workgroup");
                    if (pe != null) {
                        sessionID = ((SessionID) pe).getSessionID();
                    }
                    pe = msg.getExtension(MetaData.ELEMENT_NAME, "http://jivesoftware.com/protocol/workgroup");
                    if (pe != null) {
                        metaData = ((MetaData) pe).getMetaData();
                    }
                    fireInvitationEvent(new WorkgroupInvitation(this.connection.getUser(), msg.getFrom(), this.workgroupJID, sessionID, msg.getBody(), msg.getFrom(), metaData));
                }
            }
        }
    }

    public ChatSetting getChatSetting(String key) throws XMPPException {
        return getChatSettings(key, -1).getFirstEntry();
    }

    public ChatSettings getChatSettings(int type) throws XMPPException {
        return getChatSettings(null, type);
    }

    public ChatSettings getChatSettings() throws XMPPException {
        return getChatSettings(null, -1);
    }

    private ChatSettings getChatSettings(String key, int type) throws XMPPException {
        ChatSettings request = new ChatSettings();
        if (key != null) {
            request.setKey(key);
        }
        if (type != -1) {
            request.setType(type);
        }
        request.setType(Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        ChatSettings response = (ChatSettings) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public boolean isEmailAvailable() {
        try {
            return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(StringUtils.parseServer(this.workgroupJID)).containsFeature("jive:email:provider");
        } catch (XMPPException e) {
            return false;
        }
    }

    public OfflineSettings getOfflineSettings() throws XMPPException {
        OfflineSettings request = new OfflineSettings();
        request.setType(Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        OfflineSettings response = (OfflineSettings) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public SoundSettings getSoundSettings() throws XMPPException {
        SoundSettings request = new SoundSettings();
        request.setType(Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        SoundSettings response = (SoundSettings) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public WorkgroupProperties getWorkgroupProperties() throws XMPPException {
        WorkgroupProperties request = new WorkgroupProperties();
        request.setType(Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        WorkgroupProperties response = (WorkgroupProperties) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public WorkgroupProperties getWorkgroupProperties(String jid) throws XMPPException {
        WorkgroupProperties request = new WorkgroupProperties();
        request.setJid(jid);
        request.setType(Type.GET);
        request.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
        this.connection.sendPacket(request);
        WorkgroupProperties response = (WorkgroupProperties) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public Form getWorkgroupForm() throws XMPPException {
        WorkgroupForm workgroupForm = new WorkgroupForm();
        workgroupForm.setType(Type.GET);
        workgroupForm.setTo(this.workgroupJID);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(workgroupForm.getPacketID()));
        this.connection.sendPacket(workgroupForm);
        WorkgroupForm response = (WorkgroupForm) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return Form.getFormFrom(response);
        } else {
            throw new XMPPException(response.getError());
        }
    }
}
