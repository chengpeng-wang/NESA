package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfferRevokeProvider implements IQProvider {

    public class OfferRevokePacket extends IQ {
        private String reason;
        private String sessionID;
        private String userID;
        private String userJID;

        public OfferRevokePacket(String userJID, String userID, String cause, String sessionID) {
            this.userJID = userJID;
            this.userID = userID;
            this.reason = cause;
            this.sessionID = sessionID;
        }

        public String getUserJID() {
            return this.userJID;
        }

        public String getUserID() {
            return this.userID;
        }

        public String getReason() {
            return this.reason;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<offer-revoke xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.userID).append("\">");
            if (this.reason != null) {
                buf.append("<reason>").append(this.reason).append("</reason>");
            }
            if (this.sessionID != null) {
                buf.append(new SessionID(this.sessionID).toXML());
            }
            if (this.userID != null) {
                buf.append(new UserID(this.userID).toXML());
            }
            buf.append("</offer-revoke>");
            return buf.toString();
        }
    }

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        String userJID = parser.getAttributeValue("", "jid");
        String userID = userJID;
        String reason = null;
        String sessionID = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2 && parser.getName().equals("reason")) {
                reason = parser.nextText();
            } else if (eventType == 2 && parser.getName().equals(SessionID.ELEMENT_NAME)) {
                sessionID = parser.getAttributeValue("", "id");
            } else if (eventType == 2 && parser.getName().equals(UserID.ELEMENT_NAME)) {
                userID = parser.getAttributeValue("", "id");
            } else if (eventType == 3 && parser.getName().equals("offer-revoke")) {
                done = true;
            }
        }
        return new OfferRevokePacket(userJID, userID, reason, sessionID);
    }
}
