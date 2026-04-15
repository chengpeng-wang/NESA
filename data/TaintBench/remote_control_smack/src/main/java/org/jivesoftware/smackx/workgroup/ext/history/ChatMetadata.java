package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;
import org.xmlpull.v1.XmlPullParser;

public class ChatMetadata extends IQ {
    public static final String ELEMENT_NAME = "chat-metadata";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private Map map = new HashMap();
    private String sessionID;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            ChatMetadata chatM = new ChatMetadata();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("sessionID")) {
                        chatM.setSessionID(parser.nextText());
                    } else if (parser.getName().equals(MetaData.ELEMENT_NAME)) {
                        chatM.setMetadata(MetaDataUtils.parseMetaData(parser));
                    }
                } else if (eventType == 3 && parser.getName().equals(ChatMetadata.ELEMENT_NAME)) {
                    done = true;
                }
            }
            return chatM;
        }
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setMetadata(Map metadata) {
        this.map = metadata;
    }

    public Map getMetadata() {
        return this.map;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
        buf.append("<sessionID>").append(getSessionID()).append("</sessionID>");
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
