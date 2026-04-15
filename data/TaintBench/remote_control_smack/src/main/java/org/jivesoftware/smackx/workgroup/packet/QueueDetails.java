package org.jivesoftware.smackx.workgroup.packet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.workgroup.QueueUser;
import org.xmlpull.v1.XmlPullParser;

public class QueueDetails implements PacketExtension {
    /* access modifiers changed from: private|static|final */
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    public static final String ELEMENT_NAME = "notify-queue-details";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private Set users;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            QueueDetails queueDetails = new QueueDetails();
            int eventType = parser.getEventType();
            while (eventType != 3 && QueueDetails.ELEMENT_NAME.equals(parser.getName())) {
                eventType = parser.next();
                while (eventType == 2 && UserID.ELEMENT_NAME.equals(parser.getName())) {
                    int position = -1;
                    int time = -1;
                    Date joinTime = null;
                    String uid = parser.getAttributeValue("", "jid");
                    if (uid == null) {
                    }
                    eventType = parser.next();
                    while (true) {
                        if (eventType == 3 && UserID.ELEMENT_NAME.equals(parser.getName())) {
                            break;
                        }
                        if ("position".equals(parser.getName())) {
                            position = Integer.parseInt(parser.nextText());
                        } else if ("time".equals(parser.getName())) {
                            time = Integer.parseInt(parser.nextText());
                        } else if ("join-time".equals(parser.getName())) {
                            joinTime = QueueDetails.DATE_FORMATTER.parse(parser.nextText());
                        } else if (parser.getName().equals("waitTime")) {
                            System.out.println(QueueDetails.DATE_FORMATTER.parse(parser.nextText()));
                        }
                        eventType = parser.next();
                        if (eventType != 3) {
                        }
                    }
                    queueDetails.addUser(new QueueUser(uid, position, time, joinTime));
                    eventType = parser.next();
                }
            }
            return queueDetails;
        }
    }

    private QueueDetails() {
        this.users = new HashSet();
    }

    public int getUserCount() {
        return this.users.size();
    }

    public Set getUsers() {
        Set set;
        synchronized (this.users) {
            set = this.users;
        }
        return set;
    }

    /* access modifiers changed from: private */
    public void addUser(QueueUser user) {
        synchronized (this.users) {
            this.users.add(user);
        }
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/workgroup";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\">");
        synchronized (this.users) {
            for (QueueUser user : this.users) {
                int position = user.getQueuePosition();
                int timeRemaining = user.getEstimatedRemainingTime();
                Date timestamp = user.getQueueJoinTimestamp();
                buf.append("<user jid=\"").append(user.getUserID()).append("\">");
                if (position != -1) {
                    buf.append("<position>").append(position).append("</position>");
                }
                if (timeRemaining != -1) {
                    buf.append("<time>").append(timeRemaining).append("</time>");
                }
                if (timestamp != null) {
                    buf.append("<join-time>");
                    buf.append(DATE_FORMATTER.format(timestamp));
                    buf.append("</join-time>");
                }
                buf.append("</user>");
            }
        }
        buf.append("</").append(ELEMENT_NAME).append(">");
        return buf.toString();
    }
}
