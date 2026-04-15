package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

public class TranscriptSearch extends IQ {
    public static final String ELEMENT_NAME = "transcript-search";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            TranscriptSearch answer = new TranscriptSearch();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    answer.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                } else if (eventType == 3 && parser.getName().equals(TranscriptSearch.ELEMENT_NAME)) {
                    done = true;
                }
            }
            return answer;
        }
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
        buf.append(getExtensionsXML());
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
