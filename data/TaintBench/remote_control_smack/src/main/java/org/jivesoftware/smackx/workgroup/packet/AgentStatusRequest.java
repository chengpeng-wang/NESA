package org.jivesoftware.smackx.workgroup.packet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentStatusRequest extends IQ {
    public static final String ELEMENT_NAME = "agent-status-request";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    /* access modifiers changed from: private */
    public Set agents = new HashSet();

    public static class Item {
        private String jid;
        private String name;
        private String type;

        public Item(String jid, String type, String name) {
            this.jid = jid;
            this.type = type;
            this.name = name;
        }

        public String getJID() {
            return this.jid;
        }

        public String getType() {
            return this.type;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            AgentStatusRequest statusRequest = new AgentStatusRequest();
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "agent".equals(parser.getName())) {
                    statusRequest.agents.add(parseAgent(parser));
                } else if (eventType == 3 && AgentStatusRequest.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return statusRequest;
        }

        private Item parseAgent(XmlPullParser parser) throws Exception {
            boolean done = false;
            String jid = parser.getAttributeValue("", "jid");
            String type = parser.getAttributeValue("", "type");
            String name = null;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "name".equals(parser.getName())) {
                    name = parser.nextText();
                } else if (eventType == 3 && "agent".equals(parser.getName())) {
                    done = true;
                }
            }
            return new Item(jid, type, name);
        }
    }

    public int getAgentCount() {
        return this.agents.size();
    }

    public Set getAgents() {
        return Collections.unmodifiableSet(this.agents);
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/workgroup";
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\">");
        synchronized (this.agents) {
            for (Item item : this.agents) {
                buf.append("<agent jid=\"").append(item.getJID()).append("\">");
                if (item.getName() != null) {
                    buf.append("<name xmlns=\"http://jivesoftware.com/protocol/workgroup\">");
                    buf.append(item.getName());
                    buf.append("</name>");
                }
                buf.append("</agent>");
            }
        }
        buf.append("</").append(getElementName()).append("> ");
        return buf.toString();
    }
}
