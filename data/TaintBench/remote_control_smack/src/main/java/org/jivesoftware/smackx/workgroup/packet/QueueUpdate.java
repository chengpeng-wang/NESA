package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class QueueUpdate implements PacketExtension {
    public static final String ELEMENT_NAME = "queue-status";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private int position;
    private int remainingTime;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            boolean done = false;
            int position = -1;
            int timeRemaining = -1;
            while (!done) {
                parser.next();
                String elementName = parser.getName();
                if (parser.getEventType() == 2 && "position".equals(elementName)) {
                    try {
                        position = Integer.parseInt(parser.nextText());
                    } catch (NumberFormatException e) {
                    }
                } else if (parser.getEventType() == 2 && "time".equals(elementName)) {
                    try {
                        timeRemaining = Integer.parseInt(parser.nextText());
                    } catch (NumberFormatException e2) {
                    }
                } else if (parser.getEventType() == 3 && QueueUpdate.ELEMENT_NAME.equals(elementName)) {
                    done = true;
                }
            }
            return new QueueUpdate(position, timeRemaining);
        }
    }

    public QueueUpdate(int position, int remainingTime) {
        this.position = position;
        this.remainingTime = remainingTime;
    }

    public int getPosition() {
        return this.position;
    }

    public int getRemaingTime() {
        return this.remainingTime;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<queue-status xmlns=\"http://jabber.org/protocol/workgroup\">");
        if (this.position != -1) {
            buf.append("<position>").append(this.position).append("</position>");
        }
        if (this.remainingTime != -1) {
            buf.append("<time>").append(this.remainingTime).append("</time>");
        }
        buf.append("</queue-status>");
        return buf.toString();
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/workgroup";
    }
}
