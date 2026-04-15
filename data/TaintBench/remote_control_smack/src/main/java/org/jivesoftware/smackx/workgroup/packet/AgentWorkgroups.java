package org.jivesoftware.smackx.workgroup.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentWorkgroups extends IQ {
    private String agentJID;
    private List workgroups;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            String agentJID = parser.getAttributeValue("", "jid");
            List workgroups = new ArrayList();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals(WorkgroupInformation.ELEMENT_NAME)) {
                        workgroups.add(parser.getAttributeValue("", "jid"));
                    }
                } else if (eventType == 3 && parser.getName().equals("workgroups")) {
                    done = true;
                }
            }
            return new AgentWorkgroups(agentJID, workgroups);
        }
    }

    public AgentWorkgroups(String agentJID) {
        this.agentJID = agentJID;
        this.workgroups = new ArrayList();
    }

    public AgentWorkgroups(String agentJID, List workgroups) {
        this.agentJID = agentJID;
        this.workgroups = workgroups;
    }

    public String getAgentJID() {
        return this.agentJID;
    }

    public List getWorkgroups() {
        return Collections.unmodifiableList(this.workgroups);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<workgroups xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.agentJID).append("\">");
        for (String workgroupJID : this.workgroups) {
            buf.append("<workgroup jid=\"" + workgroupJID + "\"/>");
        }
        buf.append("</workgroups>");
        return buf.toString();
    }
}
