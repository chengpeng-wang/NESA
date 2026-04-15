package org.jivesoftware.smackx.packet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.GroupChatInvitation;

public class DelayInformation implements PacketExtension {
    public static SimpleDateFormat NEW_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    private String from;
    private String reason;
    private Date stamp;

    static {
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        NEW_UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public DelayInformation(Date stamp) {
        this.stamp = stamp;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Date getStamp() {
        return this.stamp;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getElementName() {
        return GroupChatInvitation.ELEMENT_NAME;
    }

    public String getNamespace() {
        return "jabber:x:delay";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
        buf.append(" stamp=\"");
        synchronized (UTC_FORMAT) {
            buf.append(UTC_FORMAT.format(this.stamp));
        }
        buf.append("\"");
        if (this.from != null && this.from.length() > 0) {
            buf.append(" from=\"").append(this.from).append("\"");
        }
        buf.append(">");
        if (this.reason != null && this.reason.length() > 0) {
            buf.append(this.reason);
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }
}
