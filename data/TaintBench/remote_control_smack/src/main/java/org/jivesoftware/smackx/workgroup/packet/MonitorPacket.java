package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class MonitorPacket extends IQ {
    public static final String ELEMENT_NAME = "monitor";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private boolean isMonitor;
    private String sessionID;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            MonitorPacket packet = new MonitorPacket();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "isMonitor".equals(parser.getName())) {
                    if ("false".equalsIgnoreCase(parser.nextText())) {
                        packet.setMonitor(false);
                    } else {
                        packet.setMonitor(true);
                    }
                } else if (eventType == 3 && MonitorPacket.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return packet;
        }
    }

    public boolean isMonitor() {
        return this.isMonitor;
    }

    public void setMonitor(boolean monitor) {
        this.isMonitor = monitor;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jivesoftware.com/protocol/workgroup";
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=");
        buf.append('\"');
        buf.append("http://jivesoftware.com/protocol/workgroup");
        buf.append('\"');
        buf.append(">");
        if (this.sessionID != null) {
            buf.append("<makeOwner sessionID=\"" + this.sessionID + "\"></makeOwner>");
        }
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
