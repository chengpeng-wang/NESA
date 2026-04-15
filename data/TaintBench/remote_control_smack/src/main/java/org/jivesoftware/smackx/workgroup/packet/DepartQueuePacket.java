package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class DepartQueuePacket extends IQ {
    private String user;

    public DepartQueuePacket(String workgroup) {
        this(workgroup, null);
    }

    public DepartQueuePacket(String workgroup, String user) {
        this.user = user;
        setTo(workgroup);
        setType(Type.SET);
        setFrom(user);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder("<depart-queue xmlns=\"http://jabber.org/protocol/workgroup\"");
        if (this.user != null) {
            buf.append("><jid>").append(this.user).append("</jid></depart-queue>");
        } else {
            buf.append("/>");
        }
        return buf.toString();
    }
}
