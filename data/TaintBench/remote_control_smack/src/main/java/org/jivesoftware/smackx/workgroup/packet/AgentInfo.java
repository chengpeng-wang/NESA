package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentInfo extends IQ {
    public static final String ELEMENT_NAME = "agent-info";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String jid;
    private String name;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            AgentInfo answer = new AgentInfo();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("jid")) {
                        answer.setJid(parser.nextText());
                    } else if (parser.getName().equals("name")) {
                        answer.setName(parser.nextText());
                    }
                } else if (eventType == 3 && parser.getName().equals(AgentInfo.ELEMENT_NAME)) {
                    done = true;
                }
            }
            return answer;
        }
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
        if (this.jid != null) {
            buf.append("<jid>").append(getJid()).append("</jid>");
        }
        if (this.name != null) {
            buf.append("<name>").append(getName()).append("</name>");
        }
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
