package org.jivesoftware.smackx.workgroup.agent;

import java.util.Date;
import java.util.Map;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class Offer {
    private boolean accepted = false;
    private XMPPConnection connection;
    private OfferContent content;
    private Date expiresDate;
    private Map metaData;
    private boolean rejected = false;
    private AgentSession session;
    private String sessionID;
    private String userID;
    private String userJID;
    private String workgroupName;

    private class AcceptPacket extends IQ {
        AcceptPacket(String workgroup) {
            setTo(workgroup);
            setType(Type.SET);
        }

        public String getChildElementXML() {
            return "<offer-accept id=\"" + Offer.this.getSessionID() + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
        }
    }

    private class RejectPacket extends IQ {
        RejectPacket(String workgroup) {
            setTo(workgroup);
            setType(Type.SET);
        }

        public String getChildElementXML() {
            return "<offer-reject id=\"" + Offer.this.getSessionID() + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
        }
    }

    Offer(XMPPConnection conn, AgentSession agentSession, String userID, String userJID, String workgroupName, Date expiresDate, String sessionID, Map metaData, OfferContent content) {
        this.connection = conn;
        this.session = agentSession;
        this.userID = userID;
        this.userJID = userJID;
        this.workgroupName = workgroupName;
        this.expiresDate = expiresDate;
        this.sessionID = sessionID;
        this.metaData = metaData;
        this.content = content;
    }

    public void accept() {
        this.connection.sendPacket(new AcceptPacket(this.session.getWorkgroupJID()));
        this.accepted = true;
    }

    public void reject() {
        this.connection.sendPacket(new RejectPacket(this.session.getWorkgroupJID()));
        this.rejected = true;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getUserJID() {
        return this.userJID;
    }

    public String getWorkgroupName() {
        return this.workgroupName;
    }

    public Date getExpiresDate() {
        return this.expiresDate;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public Map getMetaData() {
        return this.metaData;
    }

    public OfferContent getContent() {
        return this.content;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    public boolean isRejected() {
        return this.rejected;
    }
}
