package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;

public class Bytestream extends IQ {
    private Mode mode = Mode.tcp;
    private String sessionID;
    private final List<StreamHost> streamHosts = new ArrayList();
    private Activate toActivate;
    private StreamHostUsed usedHost;

    public enum Mode {
        tcp,
        udp;

        public static Mode fromName(String name) {
            try {
                return valueOf(name);
            } catch (Exception e) {
                return tcp;
            }
        }
    }

    public static class Activate implements PacketExtension {
        public static String ELEMENTNAME = "activate";
        public String NAMESPACE = "";
        private final String target;

        public Activate(String target) {
            this.target = target;
        }

        public String getTarget() {
            return this.target;
        }

        public String getNamespace() {
            return this.NAMESPACE;
        }

        public String getElementName() {
            return ELEMENTNAME;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName()).append(">");
            buf.append(getTarget());
            buf.append("</").append(getElementName()).append(">");
            return buf.toString();
        }
    }

    public static class StreamHost implements PacketExtension {
        public static String ELEMENTNAME = "streamhost";
        public static String NAMESPACE = "";
        private final String JID;
        private final String addy;
        private int port = 0;

        public StreamHost(String JID, String address) {
            this.JID = JID;
            this.addy = address;
        }

        public String getJID() {
            return this.JID;
        }

        public String getAddress() {
            return this.addy;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getPort() {
            return this.port;
        }

        public String getNamespace() {
            return NAMESPACE;
        }

        public String getElementName() {
            return ELEMENTNAME;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName()).append(" ");
            buf.append("jid=\"").append(getJID()).append("\" ");
            buf.append("host=\"").append(getAddress()).append("\" ");
            if (getPort() != 0) {
                buf.append("port=\"").append(getPort()).append("\"");
            } else {
                buf.append("zeroconf=\"_jabber.bytestreams\"");
            }
            buf.append("/>");
            return buf.toString();
        }
    }

    public static class StreamHostUsed implements PacketExtension {
        public static String ELEMENTNAME = "streamhost-used";
        private final String JID;
        public String NAMESPACE = "";

        public StreamHostUsed(String JID) {
            this.JID = JID;
        }

        public String getJID() {
            return this.JID;
        }

        public String getNamespace() {
            return this.NAMESPACE;
        }

        public String getElementName() {
            return ELEMENTNAME;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<").append(getElementName()).append(" ");
            buf.append("jid=\"").append(getJID()).append("\" ");
            buf.append("/>");
            return buf.toString();
        }
    }

    public Bytestream(String SID) {
        setSessionID(SID);
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return this.mode;
    }

    public StreamHost addStreamHost(String JID, String address) {
        return addStreamHost(JID, address, 0);
    }

    public StreamHost addStreamHost(String JID, String address, int port) {
        StreamHost host = new StreamHost(JID, address);
        host.setPort(port);
        addStreamHost(host);
        return host;
    }

    public void addStreamHost(StreamHost host) {
        this.streamHosts.add(host);
    }

    public Collection<StreamHost> getStreamHosts() {
        return Collections.unmodifiableCollection(this.streamHosts);
    }

    public StreamHost getStreamHost(String JID) {
        if (JID == null) {
            return null;
        }
        for (StreamHost host : this.streamHosts) {
            if (host.getJID().equals(JID)) {
                return host;
            }
        }
        return null;
    }

    public int countStreamHosts() {
        return this.streamHosts.size();
    }

    public void setUsedHost(String JID) {
        this.usedHost = new StreamHostUsed(JID);
    }

    public StreamHostUsed getUsedHost() {
        return this.usedHost;
    }

    public Activate getToActivate() {
        return this.toActivate;
    }

    public void setToActivate(String targetID) {
        this.toActivate = new Activate(targetID);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/bytestreams\"");
        if (getType().equals(Type.SET)) {
            if (getSessionID() != null) {
                buf.append(" sid=\"").append(getSessionID()).append("\"");
            }
            if (getMode() != null) {
                buf.append(" mode = \"").append(getMode()).append("\"");
            }
            buf.append(">");
            if (getToActivate() == null) {
                for (StreamHost streamHost : getStreamHosts()) {
                    buf.append(streamHost.toXML());
                }
            } else {
                buf.append(getToActivate().toXML());
            }
        } else if (!getType().equals(Type.RESULT)) {
            return null;
        } else {
            buf.append(">");
            if (getUsedHost() != null) {
                buf.append(getUsedHost().toXML());
            } else if (countStreamHosts() > 0) {
                for (StreamHost host : this.streamHosts) {
                    buf.append(host.toXML());
                }
            }
        }
        buf.append("</query>");
        return buf.toString();
    }
}
