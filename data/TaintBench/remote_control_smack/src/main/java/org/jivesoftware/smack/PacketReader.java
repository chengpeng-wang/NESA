package org.jivesoftware.smack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class PacketReader {
    private Collection<PacketCollector> collectors = new ConcurrentLinkedQueue();
    /* access modifiers changed from: private */
    public XMPPConnection connection;
    private String connectionID = null;
    protected final Collection<ConnectionListener> connectionListeners = new CopyOnWriteArrayList();
    private Semaphore connectionSemaphore;
    private boolean done;
    private ExecutorService listenerExecutor;
    protected final Map<PacketListener, ListenerWrapper> listeners = new ConcurrentHashMap();
    private XmlPullParser parser;
    private Thread readerThread;

    private class ListenerNotification implements Runnable {
        private Packet packet;

        public ListenerNotification(Packet packet) {
            this.packet = packet;
        }

        public void run() {
            for (ListenerWrapper listenerWrapper : PacketReader.this.listeners.values()) {
                listenerWrapper.notifyListener(this.packet);
            }
        }
    }

    private static class ListenerWrapper {
        private PacketFilter packetFilter;
        private PacketListener packetListener;

        public ListenerWrapper(PacketListener packetListener, PacketFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }

        public void notifyListener(Packet packet) {
            if (this.packetFilter == null || this.packetFilter.accept(packet)) {
                this.packetListener.processPacket(packet);
            }
        }
    }

    protected PacketReader(XMPPConnection connection) {
        this.connection = connection;
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.done = false;
        this.connectionID = null;
        this.readerThread = new Thread() {
            public void run() {
                PacketReader.this.parsePackets(this);
            }
        };
        this.readerThread.setName("Smack Packet Reader (" + this.connection.connectionCounterValue + ")");
        this.readerThread.setDaemon(true);
        this.listenerExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Smack Listener Processor (" + PacketReader.this.connection.connectionCounterValue + ")");
                thread.setDaemon(true);
                return thread;
            }
        });
        resetParser();
    }

    public PacketCollector createPacketCollector(PacketFilter packetFilter) {
        PacketCollector collector = new PacketCollector(this, packetFilter);
        this.collectors.add(collector);
        return collector;
    }

    /* access modifiers changed from: protected */
    public void cancelPacketCollector(PacketCollector packetCollector) {
        this.collectors.remove(packetCollector);
    }

    public void addPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        this.listeners.put(packetListener, new ListenerWrapper(packetListener, packetFilter));
    }

    public void removePacketListener(PacketListener packetListener) {
        this.listeners.remove(packetListener);
    }

    public void startup() throws XMPPException {
        this.connectionSemaphore = new Semaphore(1);
        this.readerThread.start();
        try {
            this.connectionSemaphore.acquire();
            this.connectionSemaphore.tryAcquire((long) (SmackConfiguration.getPacketReplyTimeout() * 3), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        if (this.connectionID == null) {
            throw new XMPPException("Connection failed. No response from server.");
        }
        this.connection.connectionID = this.connectionID;
    }

    public void shutdown() {
        if (!this.done) {
            for (ConnectionListener listener : this.connectionListeners) {
                try {
                    listener.connectionClosed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.done = true;
        this.listenerExecutor.shutdown();
    }

    /* access modifiers changed from: 0000 */
    public void cleanup() {
        this.connectionListeners.clear();
        this.listeners.clear();
        this.collectors.clear();
    }

    /* access modifiers changed from: 0000 */
    public void notifyConnectionError(Exception e) {
        this.done = true;
        this.connection.shutdown(new Presence(Type.unavailable));
        e.printStackTrace();
        for (ConnectionListener listener : this.connectionListeners) {
            try {
                listener.connectionClosedOnError(e);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyReconnection() {
        for (ConnectionListener listener : this.connectionListeners) {
            try {
                listener.reconnectionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetParser() {
        try {
            this.parser = new KXmlParser();
            this.parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
            this.parser.setInput(this.connection.reader);
        } catch (XmlPullParserException xppe) {
            xppe.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void parsePackets(Thread thread) {
        try {
            int eventType = this.parser.getEventType();
            do {
                if (eventType == 2) {
                    if (this.parser.getName().equals("message")) {
                        processPacket(PacketParserUtils.parseMessage(this.parser));
                    } else if (this.parser.getName().equals("iq")) {
                        processPacket(parseIQ(this.parser));
                    } else if (this.parser.getName().equals("presence")) {
                        processPacket(PacketParserUtils.parsePresence(this.parser));
                    } else if (this.parser.getName().equals("stream")) {
                        if ("jabber:client".equals(this.parser.getNamespace(null))) {
                            for (int i = 0; i < this.parser.getAttributeCount(); i++) {
                                if (this.parser.getAttributeName(i).equals("id")) {
                                    this.connectionID = this.parser.getAttributeValue(i);
                                    if (!"1.0".equals(this.parser.getAttributeValue("", "version"))) {
                                        releaseConnectionIDLock();
                                    }
                                } else if (this.parser.getAttributeName(i).equals(PrivacyRule.SUBSCRIPTION_FROM)) {
                                    this.connection.serviceName = this.parser.getAttributeValue(i);
                                }
                            }
                        }
                    } else if (this.parser.getName().equals("error")) {
                        throw new XMPPException(parseStreamError(this.parser));
                    } else if (this.parser.getName().equals("features")) {
                        parseFeatures(this.parser);
                    } else if (this.parser.getName().equals("proceed")) {
                        this.connection.proceedTLSReceived();
                        resetParser();
                    } else if (this.parser.getName().equals("failure")) {
                        String namespace = this.parser.getNamespace(null);
                        if ("urn:ietf:params:xml:ns:xmpp-tls".equals(namespace)) {
                            throw new Exception("TLS negotiation has failed");
                        } else if ("http://jabber.org/protocol/compress".equals(namespace)) {
                            this.connection.streamCompressionDenied();
                        } else {
                            this.connection.getSASLAuthentication().authenticationFailed();
                        }
                    } else if (this.parser.getName().equals("challenge")) {
                        this.connection.getSASLAuthentication().challengeReceived(this.parser.nextText());
                    } else if (this.parser.getName().equals("success")) {
                        this.connection.packetWriter.openStream();
                        resetParser();
                        this.connection.getSASLAuthentication().authenticated();
                    } else if (this.parser.getName().equals("compressed")) {
                        this.connection.startStreamCompression();
                        resetParser();
                    }
                } else if (eventType == 3 && this.parser.getName().equals("stream")) {
                    this.connection.disconnect();
                }
                eventType = this.parser.next();
                if (this.done || eventType == 1) {
                    return;
                }
            } while (thread == this.readerThread);
        } catch (Exception e) {
            if (!this.done) {
                notifyConnectionError(e);
            }
        }
    }

    private void releaseConnectionIDLock() {
        this.connectionSemaphore.release();
    }

    private void processPacket(Packet packet) {
        if (packet != null) {
            for (PacketCollector collector : this.collectors) {
                collector.processPacket(packet);
            }
            this.listenerExecutor.submit(new ListenerNotification(packet));
        }
    }

    private StreamError parseStreamError(XmlPullParser parser) throws IOException, XmlPullParserException {
        StreamError streamError = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                streamError = new StreamError(parser.getName());
            } else if (eventType == 3 && parser.getName().equals("error")) {
                done = true;
            }
        }
        return streamError;
    }

    private void parseFeatures(XmlPullParser parser) throws Exception {
        boolean startTLSReceived = false;
        boolean startTLSRequired = false;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("starttls")) {
                    startTLSReceived = true;
                } else if (parser.getName().equals("mechanisms")) {
                    this.connection.getSASLAuthentication().setAvailableSASLMethods(parseMechanisms(parser));
                } else if (parser.getName().equals("bind")) {
                    this.connection.getSASLAuthentication().bindingRequired();
                } else if (parser.getName().equals(SessionID.ELEMENT_NAME)) {
                    this.connection.getSASLAuthentication().sessionsSupported();
                } else if (parser.getName().equals("compression")) {
                    this.connection.setAvailableCompressionMethods(parseCompressionMethods(parser));
                } else if (parser.getName().equals("register")) {
                    this.connection.getAccountManager().setSupportsAccountCreation(true);
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("starttls")) {
                    this.connection.startTLSReceived(startTLSRequired);
                } else if (parser.getName().equals("required") && startTLSReceived) {
                    startTLSRequired = true;
                } else if (parser.getName().equals("features")) {
                    done = true;
                }
            }
        }
        if (!this.connection.isSecureConnection() && !startTLSReceived && this.connection.getConfiguration().getSecurityMode() == SecurityMode.required) {
            throw new XMPPException("Server does not support security (TLS), but security required by connection configuration.", new XMPPError(Condition.forbidden));
        } else if (!startTLSReceived || this.connection.getConfiguration().getSecurityMode() == SecurityMode.disabled) {
            releaseConnectionIDLock();
        }
    }

    private Collection<String> parseMechanisms(XmlPullParser parser) throws Exception {
        List<String> mechanisms = new ArrayList();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("mechanism")) {
                    mechanisms.add(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("mechanisms")) {
                done = true;
            }
        }
        return mechanisms;
    }

    private Collection<String> parseCompressionMethods(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> methods = new ArrayList();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("method")) {
                    methods.add(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("compression")) {
                done = true;
            }
        }
        return methods;
    }

    private IQ parseIQ(XmlPullParser parser) throws Exception {
        IQ iqPacket = null;
        String id = parser.getAttributeValue("", "id");
        String to = parser.getAttributeValue("", "to");
        String from = parser.getAttributeValue("", PrivacyRule.SUBSCRIPTION_FROM);
        IQ.Type type = IQ.Type.fromString(parser.getAttributeValue("", "type"));
        XMPPError error = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals("error")) {
                    error = PacketParserUtils.parseError(parser);
                } else if (elementName.equals("query") && namespace.equals("jabber:iq:auth")) {
                    iqPacket = parseAuthentication(parser);
                } else if (elementName.equals("query") && namespace.equals("jabber:iq:roster")) {
                    iqPacket = parseRoster(parser);
                } else if (elementName.equals("query") && namespace.equals("jabber:iq:register")) {
                    iqPacket = parseRegistration(parser);
                } else if (elementName.equals("bind") && namespace.equals("urn:ietf:params:xml:ns:xmpp-bind")) {
                    iqPacket = parseResourceBinding(parser);
                } else {
                    Object provider = ProviderManager.getInstance().getIQProvider(elementName, namespace);
                    if (provider != null) {
                        if (provider instanceof IQProvider) {
                            iqPacket = ((IQProvider) provider).parseIQ(parser);
                        } else if (provider instanceof Class) {
                            iqPacket = (IQ) PacketParserUtils.parseWithIntrospection(elementName, (Class) provider, parser);
                        }
                    }
                }
            } else if (eventType == 3 && parser.getName().equals("iq")) {
                done = true;
            }
        }
        if (iqPacket == null) {
            if (IQ.Type.GET == type || IQ.Type.SET == type) {
                iqPacket = new IQ() {
                    public String getChildElementXML() {
                        return null;
                    }
                };
                iqPacket.setPacketID(id);
                iqPacket.setTo(from);
                iqPacket.setFrom(to);
                iqPacket.setType(IQ.Type.ERROR);
                iqPacket.setError(new XMPPError(Condition.feature_not_implemented));
                this.connection.sendPacket(iqPacket);
                return null;
            }
            iqPacket = new IQ() {
                public String getChildElementXML() {
                    return null;
                }
            };
        }
        iqPacket.setPacketID(id);
        iqPacket.setTo(to);
        iqPacket.setFrom(from);
        iqPacket.setType(type);
        iqPacket.setError(error);
        return iqPacket;
    }

    private Authentication parseAuthentication(XmlPullParser parser) throws Exception {
        Authentication authentication = new Authentication();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("username")) {
                    authentication.setUsername(parser.nextText());
                } else if (parser.getName().equals("password")) {
                    authentication.setPassword(parser.nextText());
                } else if (parser.getName().equals("digest")) {
                    authentication.setDigest(parser.nextText());
                } else if (parser.getName().equals("resource")) {
                    authentication.setResource(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        return authentication;
    }

    private RosterPacket parseRoster(XmlPullParser parser) throws Exception {
        RosterPacket roster = new RosterPacket();
        boolean done = false;
        Item item = null;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("item")) {
                    item = new Item(parser.getAttributeValue("", "jid"), parser.getAttributeValue("", "name"));
                    item.setItemStatus(ItemStatus.fromString(parser.getAttributeValue("", "ask")));
                    item.setItemType(ItemType.valueOf(parser.getAttributeValue("", "subscription")));
                }
                if (parser.getName().equals("group") && item != null) {
                    item.addGroupName(parser.nextText());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("item")) {
                    roster.addRosterItem(item);
                }
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return roster;
    }

    private Registration parseRegistration(XmlPullParser parser) throws Exception {
        Registration registration = new Registration();
        Map<String, String> fields = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getNamespace().equals("jabber:iq:register")) {
                    String name = parser.getName();
                    String value = "";
                    if (fields == null) {
                        fields = new HashMap();
                    }
                    if (parser.next() == 4) {
                        value = parser.getText();
                    }
                    if (name.equals("instructions")) {
                        registration.setInstructions(value);
                    } else {
                        fields.put(name, value);
                    }
                } else {
                    registration.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        registration.setAttributes(fields);
        return registration;
    }

    private Bind parseResourceBinding(XmlPullParser parser) throws IOException, XmlPullParserException {
        Bind bind = new Bind();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("resource")) {
                    bind.setResource(parser.nextText());
                } else if (parser.getName().equals("jid")) {
                    bind.setJid(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("bind")) {
                done = true;
            }
        }
        return bind;
    }
}
