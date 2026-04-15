package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverInfoProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        DiscoverInfo discoverInfo = new DiscoverInfo();
        boolean done = false;
        String category = "";
        String name = "";
        String type = "";
        String variable = "";
        discoverInfo.setNode(parser.getAttributeValue("", "node"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("identity")) {
                    category = parser.getAttributeValue("", "category");
                    name = parser.getAttributeValue("", "name");
                    type = parser.getAttributeValue("", "type");
                } else if (parser.getName().equals("feature")) {
                    variable = parser.getAttributeValue("", "var");
                } else {
                    discoverInfo.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("identity")) {
                    Identity identity = new Identity(category, name);
                    identity.setType(type);
                    discoverInfo.addIdentity(identity);
                }
                if (parser.getName().equals("feature")) {
                    discoverInfo.addFeature(variable);
                }
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return discoverInfo;
    }
}
