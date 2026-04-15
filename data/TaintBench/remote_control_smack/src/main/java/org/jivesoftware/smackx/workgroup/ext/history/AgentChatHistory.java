package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentChatHistory extends IQ {
    public static final String ELEMENT_NAME = "chat-sessions";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private List agentChatSessions = new ArrayList();
    private String agentJID;
    private int maxSessions;
    private long startDate;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            AgentChatHistory agentChatHistory = new AgentChatHistory();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "chat-session".equals(parser.getName())) {
                    agentChatHistory.addChatSession(parseChatSetting(parser));
                } else if (eventType == 3 && AgentChatHistory.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return agentChatHistory;
        }

        private AgentChatSession parseChatSetting(XmlPullParser parser) throws Exception {
            boolean done = false;
            Date date = null;
            long duration = 0;
            String visitorsName = null;
            String visitorsEmail = null;
            String sessionID = null;
            String question = null;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "date".equals(parser.getName())) {
                    date = new Date(Long.valueOf(parser.nextText()).longValue());
                } else if (eventType == 2 && "duration".equals(parser.getName())) {
                    duration = Long.valueOf(parser.nextText()).longValue();
                } else if (eventType == 2 && "visitorsName".equals(parser.getName())) {
                    visitorsName = parser.nextText();
                } else if (eventType == 2 && "visitorsEmail".equals(parser.getName())) {
                    visitorsEmail = parser.nextText();
                } else if (eventType == 2 && "sessionID".equals(parser.getName())) {
                    sessionID = parser.nextText();
                } else if (eventType == 2 && "question".equals(parser.getName())) {
                    question = parser.nextText();
                } else if (eventType == 3 && "chat-session".equals(parser.getName())) {
                    done = true;
                }
            }
            return new AgentChatSession(date, duration, visitorsName, visitorsEmail, sessionID, question);
        }
    }

    public AgentChatHistory(String agentJID, int maxSessions, Date startDate) {
        this.agentJID = agentJID;
        this.maxSessions = maxSessions;
        this.startDate = startDate.getTime();
    }

    public AgentChatHistory(String agentJID, int maxSessions) {
        this.agentJID = agentJID;
        this.maxSessions = maxSessions;
        this.startDate = 0;
    }

    public void addChatSession(AgentChatSession chatSession) {
        this.agentChatSessions.add(chatSession);
    }

    public Collection getAgentChatSessions() {
        return this.agentChatSessions;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=");
        buf.append('\"');
        buf.append("http://jivesoftware.com/protocol/workgroup");
        buf.append('\"');
        buf.append(" agentJID=\"" + this.agentJID + "\"");
        buf.append(" maxSessions=\"" + this.maxSessions + "\"");
        buf.append(" startDate=\"" + this.startDate + "\"");
        buf.append("></").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
