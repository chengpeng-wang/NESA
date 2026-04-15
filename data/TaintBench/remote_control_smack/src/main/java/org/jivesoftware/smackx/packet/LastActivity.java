package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class LastActivity extends IQ {
    public long lastActivity = -1;
    public String message;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            LastActivity lastActivity = new LastActivity();
            try {
                String seconds = parser.getAttributeValue("", "seconds");
                String message = parser.nextText();
                if (seconds != null) {
                    lastActivity.setLastActivity((long) ((int) new Double(seconds).longValue()));
                }
                if (message != null) {
                    lastActivity.setMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lastActivity;
        }
    }

    public LastActivity() {
        setType(Type.GET);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:last\"");
        if (this.lastActivity != -1) {
            buf.append(" seconds=\"").append(this.lastActivity).append("\"");
        }
        buf.append("></query>");
        return buf.toString();
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    /* access modifiers changed from: private */
    public void setMessage(String message) {
        this.message = message;
    }

    public long getIdleTime() {
        return this.lastActivity;
    }

    public String getStatusMessage() {
        return this.message;
    }

    public static LastActivity getLastActivity(XMPPConnection con, String jid) throws XMPPException {
        LastActivity activity = new LastActivity();
        activity.setTo(StringUtils.parseBareAddress(jid));
        PacketCollector collector = con.createPacketCollector(new PacketIDFilter(activity.getPacketID()));
        con.sendPacket(activity);
        LastActivity response = (LastActivity) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (response.getError() == null) {
            return response;
        } else {
            throw new XMPPException(response.getError());
        }
    }
}
