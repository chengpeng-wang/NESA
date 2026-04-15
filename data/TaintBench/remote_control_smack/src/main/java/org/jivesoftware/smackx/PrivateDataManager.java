package org.jivesoftware.smackx;

import java.util.Hashtable;
import java.util.Map;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.DefaultPrivateData;
import org.jivesoftware.smackx.packet.PrivateData;
import org.jivesoftware.smackx.provider.PrivateDataProvider;
import org.xmlpull.v1.XmlPullParser;

public class PrivateDataManager {
    private static Map privateDataProviders = new Hashtable();
    private XMPPConnection connection;
    private String user;

    public static class PrivateDataIQProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            PrivateData privateData = null;
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    PrivateDataProvider provider = PrivateDataManager.getPrivateDataProvider(elementName, namespace);
                    if (provider != null) {
                        privateData = provider.parsePrivateData(parser);
                    } else {
                        DefaultPrivateData data = new DefaultPrivateData(elementName, namespace);
                        boolean finished = false;
                        while (!finished) {
                            int event = parser.next();
                            if (event == 2) {
                                String name = parser.getName();
                                if (parser.isEmptyElementTag()) {
                                    data.setValue(name, "");
                                } else if (parser.next() == 4) {
                                    data.setValue(name, parser.getText());
                                }
                            } else if (event == 3 && parser.getName().equals(elementName)) {
                                finished = true;
                            }
                        }
                        privateData = data;
                    }
                } else if (eventType == 3 && parser.getName().equals("query")) {
                    done = true;
                }
            }
            return new PrivateDataResult(privateData);
        }
    }

    private static class PrivateDataResult extends IQ {
        private PrivateData privateData;

        PrivateDataResult(PrivateData privateData) {
            this.privateData = privateData;
        }

        public PrivateData getPrivateData() {
            return this.privateData;
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<query xmlns=\"jabber:iq:private\">");
            if (this.privateData != null) {
                this.privateData.toXML();
            }
            buf.append("</query>");
            return buf.toString();
        }
    }

    public static PrivateDataProvider getPrivateDataProvider(String elementName, String namespace) {
        return (PrivateDataProvider) privateDataProviders.get(getProviderKey(elementName, namespace));
    }

    public static void addPrivateDataProvider(String elementName, String namespace, PrivateDataProvider provider) {
        privateDataProviders.put(getProviderKey(elementName, namespace), provider);
    }

    public static void removePrivateDataProvider(String elementName, String namespace) {
        privateDataProviders.remove(getProviderKey(elementName, namespace));
    }

    public PrivateDataManager(XMPPConnection connection) {
        if (connection.isAuthenticated()) {
            this.connection = connection;
            return;
        }
        throw new IllegalStateException("Must be logged in to XMPP server.");
    }

    public PrivateDataManager(XMPPConnection connection, String user) {
        if (connection.isAuthenticated()) {
            this.connection = connection;
            this.user = user;
            return;
        }
        throw new IllegalStateException("Must be logged in to XMPP server.");
    }

    public PrivateData getPrivateData(final String elementName, final String namespace) throws XMPPException {
        IQ privateDataGet = new IQ() {
            public String getChildElementXML() {
                StringBuilder buf = new StringBuilder();
                buf.append("<query xmlns=\"jabber:iq:private\">");
                buf.append("<").append(elementName).append(" xmlns=\"").append(namespace).append("\"/>");
                buf.append("</query>");
                return buf.toString();
            }
        };
        privateDataGet.setType(Type.GET);
        if (this.user != null) {
            privateDataGet.setTo(this.user);
        }
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(privateDataGet.getPacketID()));
        this.connection.sendPacket(privateDataGet);
        IQ response = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from the server.");
        } else if (response.getType() != Type.ERROR) {
            return ((PrivateDataResult) response).getPrivateData();
        } else {
            throw new XMPPException(response.getError());
        }
    }

    public void setPrivateData(final PrivateData privateData) throws XMPPException {
        IQ privateDataSet = new IQ() {
            public String getChildElementXML() {
                StringBuilder buf = new StringBuilder();
                buf.append("<query xmlns=\"jabber:iq:private\">");
                buf.append(privateData.toXML());
                buf.append("</query>");
                return buf.toString();
            }
        };
        privateDataSet.setType(Type.SET);
        if (this.user != null) {
            privateDataSet.setTo(this.user);
        }
        PacketCollector collector = this.connection.createPacketCollector(new PacketIDFilter(privateDataSet.getPacketID()));
        this.connection.sendPacket(privateDataSet);
        IQ response = (IQ) collector.nextResult(5000);
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from the server.");
        } else if (response.getType() == Type.ERROR) {
            throw new XMPPException(response.getError());
        }
    }

    private static String getProviderKey(String elementName, String namespace) {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(elementName).append("/><").append(namespace).append("/>");
        return buf.toString();
    }
}
