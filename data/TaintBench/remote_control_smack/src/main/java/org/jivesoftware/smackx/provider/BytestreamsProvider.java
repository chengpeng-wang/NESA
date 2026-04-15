package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.Bytestream;
import org.jivesoftware.smackx.packet.Bytestream.Activate;
import org.jivesoftware.smackx.packet.Bytestream.Mode;
import org.jivesoftware.smackx.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.packet.Bytestream.StreamHostUsed;
import org.xmlpull.v1.XmlPullParser;

public class BytestreamsProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        boolean done = false;
        Bytestream toReturn = new Bytestream();
        String id = parser.getAttributeValue("", "sid");
        String mode = parser.getAttributeValue("", "mode");
        String JID = null;
        String host = null;
        String port = null;
        while (!done) {
            int eventType = parser.next();
            String elementName = parser.getName();
            if (eventType == 2) {
                if (elementName.equals(StreamHost.ELEMENTNAME)) {
                    JID = parser.getAttributeValue("", "jid");
                    host = parser.getAttributeValue("", "host");
                    port = parser.getAttributeValue("", "port");
                } else if (elementName.equals(StreamHostUsed.ELEMENTNAME)) {
                    toReturn.setUsedHost(parser.getAttributeValue("", "jid"));
                } else if (elementName.equals(Activate.ELEMENTNAME)) {
                    toReturn.setToActivate(parser.getAttributeValue("", "jid"));
                }
            } else if (eventType == 3) {
                if (elementName.equals("streamhost")) {
                    if (port == null) {
                        toReturn.addStreamHost(JID, host);
                    } else {
                        toReturn.addStreamHost(JID, host, Integer.parseInt(port));
                    }
                    JID = null;
                    host = null;
                    port = null;
                } else if (elementName.equals("query")) {
                    done = true;
                }
            }
        }
        toReturn.setMode(Mode.fromName(mode));
        toReturn.setSessionID(id);
        return toReturn;
    }
}
