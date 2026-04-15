package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class ServiceDiscoveryManager {
    private static String identityName = "Smack";
    private static String identityType = "pc";
    /* access modifiers changed from: private|static */
    public static Map<XMPPConnection, ServiceDiscoveryManager> instances = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public XMPPConnection connection;
    /* access modifiers changed from: private|final */
    public final List<String> features = new ArrayList();
    private Map<String, NodeInformationProvider> nodeInformationProviders = new ConcurrentHashMap();

    static {
        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {
            public void connectionCreated(XMPPConnection connection) {
                ServiceDiscoveryManager serviceDiscoveryManager = new ServiceDiscoveryManager(connection);
            }
        });
    }

    public ServiceDiscoveryManager(XMPPConnection connection) {
        this.connection = connection;
        init();
    }

    public static ServiceDiscoveryManager getInstanceFor(XMPPConnection connection) {
        return (ServiceDiscoveryManager) instances.get(connection);
    }

    public static String getIdentityName() {
        return identityName;
    }

    public static void setIdentityName(String name) {
        identityName = name;
    }

    public static String getIdentityType() {
        return identityType;
    }

    public static void setIdentityType(String type) {
        identityType = type;
    }

    private void init() {
        instances.put(this.connection, this);
        this.connection.addConnectionListener(new ConnectionListener() {
            public void connectionClosed() {
                ServiceDiscoveryManager.instances.remove(ServiceDiscoveryManager.this.connection);
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
        PacketFilter packetFilter = new PacketTypeFilter(DiscoverItems.class);
        this.connection.addPacketListener(new PacketListener() {
            public void processPacket(Packet packet) {
                DiscoverItems discoverItems = (DiscoverItems) packet;
                if (discoverItems != null && discoverItems.getType() == Type.GET) {
                    DiscoverItems response = new DiscoverItems();
                    response.setType(Type.RESULT);
                    response.setTo(discoverItems.getFrom());
                    response.setPacketID(discoverItems.getPacketID());
                    response.setNode(discoverItems.getNode());
                    NodeInformationProvider nodeInformationProvider = ServiceDiscoveryManager.this.getNodeInformationProvider(discoverItems.getNode());
                    if (nodeInformationProvider != null) {
                        List<Item> items = nodeInformationProvider.getNodeItems();
                        if (items != null) {
                            for (Item item : items) {
                                response.addItem(item);
                            }
                        }
                    } else if (discoverItems.getNode() != null) {
                        response.setType(Type.ERROR);
                        response.setError(new XMPPError(Condition.item_not_found));
                    }
                    ServiceDiscoveryManager.this.connection.sendPacket(response);
                }
            }
        }, packetFilter);
        packetFilter = new PacketTypeFilter(DiscoverInfo.class);
        this.connection.addPacketListener(new PacketListener() {
            public void processPacket(Packet packet) {
                DiscoverInfo discoverInfo = (DiscoverInfo) packet;
                if (discoverInfo != null && discoverInfo.getType() == Type.GET) {
                    DiscoverInfo response = new DiscoverInfo();
                    response.setType(Type.RESULT);
                    response.setTo(discoverInfo.getFrom());
                    response.setPacketID(discoverInfo.getPacketID());
                    response.setNode(discoverInfo.getNode());
                    Identity identity;
                    if (discoverInfo.getNode() == null) {
                        identity = new Identity("client", ServiceDiscoveryManager.getIdentityName());
                        identity.setType(ServiceDiscoveryManager.getIdentityType());
                        response.addIdentity(identity);
                        synchronized (ServiceDiscoveryManager.this.features) {
                            Iterator<String> it = ServiceDiscoveryManager.this.getFeatures();
                            while (it.hasNext()) {
                                response.addFeature((String) it.next());
                            }
                        }
                    } else {
                        NodeInformationProvider nodeInformationProvider = ServiceDiscoveryManager.this.getNodeInformationProvider(discoverInfo.getNode());
                        if (nodeInformationProvider != null) {
                            List<String> features = nodeInformationProvider.getNodeFeatures();
                            if (features != null) {
                                for (String feature : features) {
                                    response.addFeature(feature);
                                }
                            }
                            List<Identity> identities = nodeInformationProvider.getNodeIdentities();
                            if (identities != null) {
                                for (Identity identity2 : identities) {
                                    response.addIdentity(identity2);
                                }
                            }
                        } else {
                            response.setType(Type.ERROR);
                            response.setError(new XMPPError(Condition.item_not_found));
                        }
                    }
                    ServiceDiscoveryManager.this.connection.sendPacket(response);
                }
            }
        }, packetFilter);
    }

    /* access modifiers changed from: private */
    public NodeInformationProvider getNodeInformationProvider(String node) {
        if (node == null) {
            return null;
        }
        return (NodeInformationProvider) this.nodeInformationProviders.get(node);
    }

    public void setNodeInformationProvider(String node, NodeInformationProvider listener) {
        this.nodeInformationProviders.put(node, listener);
    }

    public void removeNodeInformationProvider(String node) {
        this.nodeInformationProviders.remove(node);
    }

    public Iterator<String> getFeatures() {
        Iterator it;
        synchronized (this.features) {
            it = Collections.unmodifiableList(new ArrayList(this.features)).iterator();
        }
        return it;
    }

    public void addFeature(String feature) {
        synchronized (this.features) {
            this.features.add(feature);
        }
    }

    public void removeFeature(String feature) {
        synchronized (this.features) {
            this.features.remove(feature);
        }
    }

    public boolean includesFeature(String feature) {
        boolean contains;
        synchronized (this.features) {
            contains = this.features.contains(feature);
        }
        return contains;
    }

    public DiscoverInfo discoverInfo(String entityID) throws XMPPException {
        return discoverInfo(entityID, null);
    }

    public DiscoverInfo discoverInfo(String entityID, String node) throws XMPPException {
        DiscoverInfo disco = new DiscoverInfo();
        disco.setType(Type.GET);
        disco.setTo(entityID);
        disco.setNode(node);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(disco.getPacketID()));
        this.connection.sendPacket(disco);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from the server.");
        } else if (result.getType() != Type.ERROR) {
            return (DiscoverInfo) result;
        } else {
            throw new XMPPException(result.getError());
        }
    }

    public DiscoverItems discoverItems(String entityID) throws XMPPException {
        return discoverItems(entityID, null);
    }

    public DiscoverItems discoverItems(String entityID, String node) throws XMPPException {
        DiscoverItems disco = new DiscoverItems();
        disco.setType(Type.GET);
        disco.setTo(entityID);
        disco.setNode(node);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(disco.getPacketID()));
        this.connection.sendPacket(disco);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from the server.");
        } else if (result.getType() != Type.ERROR) {
            return (DiscoverItems) result;
        } else {
            throw new XMPPException(result.getError());
        }
    }

    public boolean canPublishItems(String entityID) throws XMPPException {
        return discoverInfo(entityID).containsFeature("http://jabber.org/protocol/disco#publish");
    }

    public void publishItems(String entityID, DiscoverItems discoverItems) throws XMPPException {
        publishItems(entityID, null, discoverItems);
    }

    public void publishItems(String entityID, String node, DiscoverItems discoverItems) throws XMPPException {
        discoverItems.setType(Type.SET);
        discoverItems.setTo(entityID);
        discoverItems.setNode(node);
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(discoverItems.getPacketID()));
        this.connection.sendPacket(discoverItems);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from the server.");
        } else if (result.getType() == Type.ERROR) {
            throw new XMPPException(result.getError());
        }
    }
}
