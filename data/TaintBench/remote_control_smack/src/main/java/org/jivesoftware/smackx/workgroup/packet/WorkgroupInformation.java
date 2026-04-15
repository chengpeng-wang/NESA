package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupInformation implements PacketExtension {
    public static final String ELEMENT_NAME = "workgroup";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private String workgroupJID;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            String workgroupJID = parser.getAttributeValue("", "jid");
            parser.next();
            return new WorkgroupInformation(workgroupJID);
        }
    }

    public WorkgroupInformation(String workgroupJID) {
        this.workgroupJID = workgroupJID;
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/workgroup";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append('<').append(ELEMENT_NAME);
        buf.append(" jid=\"").append(getWorkgroupJID()).append("\"");
        buf.append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\" />");
        return buf.toString();
    }
}
