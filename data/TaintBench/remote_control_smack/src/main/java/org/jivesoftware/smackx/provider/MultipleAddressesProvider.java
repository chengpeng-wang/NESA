package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class MultipleAddressesProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        boolean done = false;
        MultipleAddresses multipleAddresses = new MultipleAddresses();
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("address")) {
                    multipleAddresses.addAddress(parser.getAttributeValue("", "type"), parser.getAttributeValue("", "jid"), parser.getAttributeValue("", "node"), parser.getAttributeValue("", "desc"), "true".equals(parser.getAttributeValue("", MessageEvent.DELIVERED)), parser.getAttributeValue("", "uri"));
                }
            } else if (eventType == 3 && parser.getName().equals(multipleAddresses.getElementName())) {
                done = true;
            }
        }
        return multipleAddresses;
    }
}
