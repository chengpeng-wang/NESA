package org.jivesoftware.smackx.workgroup.packet;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OccupantsInfo extends IQ {
    public static final String ELEMENT_NAME = "occupants-info";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    /* access modifiers changed from: private|static|final */
    public static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    /* access modifiers changed from: private|final */
    public final Set<OccupantInfo> occupants = new HashSet();
    private String roomID;

    public static class OccupantInfo {
        private String jid;
        private Date joined;
        private String nickname;

        public OccupantInfo(String jid, String nickname, Date joined) {
            this.jid = jid;
            this.nickname = nickname;
            this.joined = joined;
        }

        public String getJID() {
            return this.jid;
        }

        public String getNickname() {
            return this.nickname;
        }

        public Date getJoined() {
            return this.joined;
        }
    }

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            OccupantsInfo occupantsInfo = new OccupantsInfo(parser.getAttributeValue("", "roomID"));
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "occupant".equals(parser.getName())) {
                    occupantsInfo.occupants.add(parseOccupantInfo(parser));
                } else if (eventType == 3 && OccupantsInfo.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return occupantsInfo;
        }

        private OccupantInfo parseOccupantInfo(XmlPullParser parser) throws Exception {
            boolean done = false;
            String jid = null;
            String nickname = null;
            Date joined = null;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "jid".equals(parser.getName())) {
                    jid = parser.nextText();
                } else if (eventType == 2 && "nickname".equals(parser.getName())) {
                    nickname = parser.nextText();
                } else if (eventType == 2 && "joined".equals(parser.getName())) {
                    joined = OccupantsInfo.UTC_FORMAT.parse(parser.nextText());
                } else if (eventType == 3 && "occupant".equals(parser.getName())) {
                    done = true;
                }
            }
            return new OccupantInfo(jid, nickname, joined);
        }
    }

    static {
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    public OccupantsInfo(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return this.roomID;
    }

    public int getOccupantsCount() {
        return this.occupants.size();
    }

    public Set<OccupantInfo> getOccupants() {
        return Collections.unmodifiableSet(this.occupants);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup");
        buf.append("\" roomID=\"").append(this.roomID).append("\">");
        synchronized (this.occupants) {
            for (OccupantInfo occupant : this.occupants) {
                buf.append("<occupant>");
                buf.append("<jid>");
                buf.append(occupant.getJID());
                buf.append("</jid>");
                buf.append("<name>");
                buf.append(occupant.getNickname());
                buf.append("</name>");
                buf.append("<joined>");
                buf.append(UTC_FORMAT.format(occupant.getJoined()));
                buf.append("</joined>");
                buf.append("</occupant>");
            }
        }
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
