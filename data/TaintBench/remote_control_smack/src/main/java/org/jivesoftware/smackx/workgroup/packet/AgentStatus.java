package org.jivesoftware.smackx.workgroup.packet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentStatus implements PacketExtension {
    public static final String ELEMENT_NAME = "agent-status";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    /* access modifiers changed from: private|static|final */
    public static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    /* access modifiers changed from: private */
    public List currentChats = new ArrayList();
    /* access modifiers changed from: private */
    public int maxChats = -1;
    /* access modifiers changed from: private */
    public String workgroupJID;

    public static class ChatInfo {
        private Date date;
        private String email;
        private String question;
        private String sessionID;
        private String userID;
        private String username;

        public ChatInfo(String sessionID, String userID, Date date, String email, String username, String question) {
            this.sessionID = sessionID;
            this.userID = userID;
            this.date = date;
            this.email = email;
            this.username = username;
            this.question = question;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public String getUserID() {
            return this.userID;
        }

        public Date getDate() {
            return this.date;
        }

        public String getEmail() {
            return this.email;
        }

        public String getUsername() {
            return this.username;
        }

        public String getQuestion() {
            return this.question;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<chat ");
            if (this.sessionID != null) {
                buf.append(" sessionID=\"").append(this.sessionID).append("\"");
            }
            if (this.userID != null) {
                buf.append(" userID=\"").append(this.userID).append("\"");
            }
            if (this.date != null) {
                buf.append(" startTime=\"").append(AgentStatus.UTC_FORMAT.format(this.date)).append("\"");
            }
            if (this.email != null) {
                buf.append(" email=\"").append(this.email).append("\"");
            }
            if (this.username != null) {
                buf.append(" username=\"").append(this.username).append("\"");
            }
            if (this.question != null) {
                buf.append(" question=\"").append(this.question).append("\"");
            }
            buf.append("/>");
            return buf.toString();
        }
    }

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            AgentStatus agentStatus = new AgentStatus();
            agentStatus.workgroupJID = parser.getAttributeValue("", "jid");
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if ("chat".equals(parser.getName())) {
                        agentStatus.currentChats.add(parseChatInfo(parser));
                    } else if ("max-chats".equals(parser.getName())) {
                        agentStatus.maxChats = Integer.parseInt(parser.nextText());
                    }
                } else if (eventType == 3 && AgentStatus.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return agentStatus;
        }

        private ChatInfo parseChatInfo(XmlPullParser parser) {
            String sessionID = parser.getAttributeValue("", "sessionID");
            String userID = parser.getAttributeValue("", "userID");
            Date date = null;
            try {
                date = AgentStatus.UTC_FORMAT.parse(parser.getAttributeValue("", "startTime"));
            } catch (ParseException e) {
            }
            return new ChatInfo(sessionID, userID, date, parser.getAttributeValue("", "email"), parser.getAttributeValue("", "username"), parser.getAttributeValue("", "question"));
        }
    }

    static {
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    AgentStatus() {
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public List getCurrentChats() {
        return Collections.unmodifiableList(this.currentChats);
    }

    public int getMaxChats() {
        return this.maxChats;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/workgroup";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\"");
        if (this.workgroupJID != null) {
            buf.append(" jid=\"").append(this.workgroupJID).append("\"");
        }
        buf.append(">");
        if (this.maxChats != -1) {
            buf.append("<max-chats>").append(this.maxChats).append("</max-chats>");
        }
        if (!this.currentChats.isEmpty()) {
            buf.append("<current-chats xmlns= \"http://jivesoftware.com/protocol/workgroup\">");
            for (ChatInfo toXML : this.currentChats) {
                buf.append(toXML.toXML());
            }
            buf.append("</current-chats>");
        }
        buf.append("</").append(getElementName()).append("> ");
        return buf.toString();
    }
}
