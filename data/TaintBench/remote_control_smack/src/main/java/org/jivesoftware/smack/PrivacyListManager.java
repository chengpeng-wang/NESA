package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Privacy;
import org.jivesoftware.smack.packet.PrivacyItem;

public class PrivacyListManager {
    /* access modifiers changed from: private|static */
    public static Map<XMPPConnection, PrivacyListManager> instances = new Hashtable();
    /* access modifiers changed from: private */
    public XMPPConnection connection;
    /* access modifiers changed from: private|final */
    public final List<PrivacyListListener> listeners;
    PacketFilter packetFilter;

    /* synthetic */ PrivacyListManager(XMPPConnection x0, AnonymousClass1 x1) {
        this(x0);
    }

    static {
        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {
            public void connectionCreated(XMPPConnection connection) {
                PrivacyListManager privacyListManager = new PrivacyListManager(connection, null);
            }
        });
    }

    private PrivacyListManager(XMPPConnection connection) {
        this.listeners = new ArrayList();
        this.packetFilter = new AndFilter(new IQTypeFilter(Type.SET), new PacketExtensionFilter("query", "jabber:iq:privacy"));
        this.connection = connection;
        init();
    }

    private String getUser() {
        return this.connection.getUser();
    }

    private void init() {
        instances.put(this.connection, this);
        this.connection.addConnectionListener(new ConnectionListener() {
            public void connectionClosed() {
                PrivacyListManager.instances.remove(PrivacyListManager.this.connection);
            }

            public void connectionClosedOnError(Exception e) {
            }

            public void reconnectionFailed(Exception e) {
            }

            public void reconnectingIn(int seconds) {
            }

            public void reconnectionSuccessful() {
            }
        });
        this.connection.addPacketListener(new PacketListener() {
            public void processPacket(Packet packet) {
                if (packet != null && packet.getError() == null) {
                    Privacy privacy = (Privacy) packet;
                    synchronized (PrivacyListManager.this.listeners) {
                        for (PrivacyListListener listener : PrivacyListManager.this.listeners) {
                            for (Entry<String, List<PrivacyItem>> entry : privacy.getItemLists().entrySet()) {
                                String listName = (String) entry.getKey();
                                List<PrivacyItem> items = (List) entry.getValue();
                                if (items.isEmpty()) {
                                    listener.updatedPrivacyList(listName);
                                } else {
                                    listener.setPrivacyList(listName, items);
                                }
                            }
                        }
                    }
                    IQ iq = new IQ() {
                        public String getChildElementXML() {
                            return "";
                        }
                    };
                    iq.setType(Type.RESULT);
                    iq.setFrom(packet.getFrom());
                    iq.setPacketID(packet.getPacketID());
                    PrivacyListManager.this.connection.sendPacket(iq);
                }
            }
        }, this.packetFilter);
    }

    public static PrivacyListManager getInstanceFor(XMPPConnection connection) {
        return (PrivacyListManager) instances.get(connection);
    }

    private Privacy getRequest(Privacy requestPrivacy) throws XMPPException {
        requestPrivacy.setType(Type.GET);
        requestPrivacy.setFrom(getUser());
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(requestPrivacy.getPacketID()));
        this.connection.sendPacket(requestPrivacy);
        Privacy privacyAnswer = (Privacy) response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (privacyAnswer == null) {
            throw new XMPPException("No response from server.");
        } else if (privacyAnswer.getError() == null) {
            return privacyAnswer;
        } else {
            throw new XMPPException(privacyAnswer.getError());
        }
    }

    private Packet setRequest(Privacy requestPrivacy) throws XMPPException {
        requestPrivacy.setType(Type.SET);
        requestPrivacy.setFrom(getUser());
        PacketCollector response = this.connection.createPacketCollector(new PacketIDFilter(requestPrivacy.getPacketID()));
        this.connection.sendPacket(requestPrivacy);
        Packet privacyAnswer = response.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        response.cancel();
        if (privacyAnswer == null) {
            throw new XMPPException("No response from server.");
        } else if (privacyAnswer.getError() == null) {
            return privacyAnswer;
        } else {
            throw new XMPPException(privacyAnswer.getError());
        }
    }

    private Privacy getPrivacyWithListNames() throws XMPPException {
        return getRequest(new Privacy());
    }

    public PrivacyList getActiveList() throws XMPPException {
        Privacy privacyAnswer = getPrivacyWithListNames();
        String listName = privacyAnswer.getActiveName();
        boolean isDefaultAndActive = (privacyAnswer.getActiveName() == null || privacyAnswer.getDefaultName() == null || !privacyAnswer.getActiveName().equals(privacyAnswer.getDefaultName())) ? false : true;
        return new PrivacyList(true, isDefaultAndActive, listName, getPrivacyListItems(listName));
    }

    public PrivacyList getDefaultList() throws XMPPException {
        Privacy privacyAnswer = getPrivacyWithListNames();
        String listName = privacyAnswer.getDefaultName();
        boolean isDefaultAndActive = (privacyAnswer.getActiveName() == null || privacyAnswer.getDefaultName() == null || !privacyAnswer.getActiveName().equals(privacyAnswer.getDefaultName())) ? false : true;
        return new PrivacyList(isDefaultAndActive, true, listName, getPrivacyListItems(listName));
    }

    private List<PrivacyItem> getPrivacyListItems(String listName) throws XMPPException {
        Privacy request = new Privacy();
        request.setPrivacyList(listName, new ArrayList());
        return getRequest(request).getPrivacyList(listName);
    }

    public PrivacyList getPrivacyList(String listName) throws XMPPException {
        return new PrivacyList(false, false, listName, getPrivacyListItems(listName));
    }

    public PrivacyList[] getPrivacyLists() throws XMPPException {
        Privacy privacyAnswer = getPrivacyWithListNames();
        Set<String> names = privacyAnswer.getPrivacyListNames();
        PrivacyList[] lists = new PrivacyList[names.size()];
        int index = 0;
        for (String listName : names) {
            lists[index] = new PrivacyList(listName.equals(privacyAnswer.getActiveName()), listName.equals(privacyAnswer.getDefaultName()), listName, getPrivacyListItems(listName));
            index++;
        }
        return lists;
    }

    public void setActiveListName(String listName) throws XMPPException {
        Privacy request = new Privacy();
        request.setActiveName(listName);
        setRequest(request);
    }

    public void declineActiveList() throws XMPPException {
        Privacy request = new Privacy();
        request.setDeclineActiveList(true);
        setRequest(request);
    }

    public void setDefaultListName(String listName) throws XMPPException {
        Privacy request = new Privacy();
        request.setDefaultName(listName);
        setRequest(request);
    }

    public void declineDefaultList() throws XMPPException {
        Privacy request = new Privacy();
        request.setDeclineDefaultList(true);
        setRequest(request);
    }

    public void createPrivacyList(String listName, List<PrivacyItem> privacyItems) throws XMPPException {
        updatePrivacyList(listName, privacyItems);
    }

    public void updatePrivacyList(String listName, List<PrivacyItem> privacyItems) throws XMPPException {
        Privacy request = new Privacy();
        request.setPrivacyList(listName, privacyItems);
        setRequest(request);
    }

    public void deletePrivacyList(String listName) throws XMPPException {
        Privacy request = new Privacy();
        request.setPrivacyList(listName, new ArrayList());
        setRequest(request);
    }

    public void addListener(PrivacyListListener listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
    }
}
