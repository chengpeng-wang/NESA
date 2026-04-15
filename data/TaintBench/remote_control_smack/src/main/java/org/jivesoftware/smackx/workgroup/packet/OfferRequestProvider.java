package org.jivesoftware.smackx.workgroup.packet;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.agent.InvitationRequest;
import org.jivesoftware.smackx.workgroup.agent.OfferContent;
import org.jivesoftware.smackx.workgroup.agent.TransferRequest;
import org.jivesoftware.smackx.workgroup.agent.UserRequest;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;
import org.xmlpull.v1.XmlPullParser;

public class OfferRequestProvider implements IQProvider {

    public static class OfferRequestPacket extends IQ {
        private OfferContent content;
        private Map metaData;
        private String sessionID;
        private int timeout;
        private String userID;
        private String userJID;

        public OfferRequestPacket(String userJID, String userID, int timeout, Map metaData, String sessionID, OfferContent content) {
            this.userJID = userJID;
            this.userID = userID;
            this.timeout = timeout;
            this.metaData = metaData;
            this.sessionID = sessionID;
            this.content = content;
        }

        public String getUserID() {
            return this.userID;
        }

        public String getUserJID() {
            return this.userJID;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public int getTimeout() {
            return this.timeout;
        }

        public OfferContent getContent() {
            return this.content;
        }

        public Map getMetaData() {
            return this.metaData;
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<offer xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.userJID).append("\">");
            buf.append("<timeout>").append(this.timeout).append("</timeout>");
            if (this.sessionID != null) {
                buf.append('<').append(SessionID.ELEMENT_NAME);
                buf.append(" session=\"");
                buf.append(getSessionID()).append("\" xmlns=\"");
                buf.append("http://jivesoftware.com/protocol/workgroup").append("\"/>");
            }
            if (this.metaData != null) {
                buf.append(MetaDataUtils.serializeMetaData(this.metaData));
            }
            if (this.userID != null) {
                buf.append('<').append(UserID.ELEMENT_NAME);
                buf.append(" id=\"");
                buf.append(this.userID).append("\" xmlns=\"");
                buf.append("http://jivesoftware.com/protocol/workgroup").append("\"/>");
            }
            buf.append("</offer>");
            return buf.toString();
        }
    }

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        int eventType = parser.getEventType();
        String sessionID = null;
        int timeout = -1;
        OfferContent content = null;
        boolean done = false;
        Map metaData = new HashMap();
        if (eventType != 2) {
        }
        String userJID = parser.getAttributeValue("", "jid");
        String userID = userJID;
        while (!done) {
            eventType = parser.next();
            if (eventType == 2) {
                String elemName = parser.getName();
                if ("timeout".equals(elemName)) {
                    timeout = Integer.parseInt(parser.nextText());
                } else if (MetaData.ELEMENT_NAME.equals(elemName)) {
                    metaData = MetaDataUtils.parseMetaData(parser);
                } else if (SessionID.ELEMENT_NAME.equals(elemName)) {
                    sessionID = parser.getAttributeValue("", "id");
                } else if (UserID.ELEMENT_NAME.equals(elemName)) {
                    userID = parser.getAttributeValue("", "id");
                } else if ("user-request".equals(elemName)) {
                    content = UserRequest.getInstance();
                } else if (RoomInvitation.ELEMENT_NAME.equals(elemName)) {
                    RoomInvitation invitation = (RoomInvitation) PacketParserUtils.parsePacketExtension(RoomInvitation.ELEMENT_NAME, "http://jabber.org/protocol/workgroup", parser);
                    content = new InvitationRequest(invitation.getInviter(), invitation.getRoom(), invitation.getReason());
                } else if (RoomTransfer.ELEMENT_NAME.equals(elemName)) {
                    RoomTransfer transfer = (RoomTransfer) PacketParserUtils.parsePacketExtension(RoomTransfer.ELEMENT_NAME, "http://jabber.org/protocol/workgroup", parser);
                    content = new TransferRequest(transfer.getInviter(), transfer.getRoom(), transfer.getReason());
                }
            } else if (eventType == 3 && "offer".equals(parser.getName())) {
                done = true;
            }
        }
        OfferRequestPacket offerRequest = new OfferRequestPacket(userJID, userID, timeout, metaData, sessionID, content);
        offerRequest.setType(Type.SET);
        return offerRequest;
    }
}
