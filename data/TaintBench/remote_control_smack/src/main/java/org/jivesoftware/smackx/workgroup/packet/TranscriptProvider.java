package org.jivesoftware.smackx.workgroup.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

public class TranscriptProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        String sessionID = parser.getAttributeValue("", "sessionID");
        List packets = new ArrayList();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("message")) {
                    packets.add(PacketParserUtils.parseMessage(parser));
                } else if (parser.getName().equals("presence")) {
                    packets.add(PacketParserUtils.parsePresence(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("transcript")) {
                done = true;
            }
        }
        return new Transcript(sessionID, packets);
    }
}
