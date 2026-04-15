package org.jivesoftware.smackx.workgroup.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

public class Transcript extends IQ {
    private List packets;
    private String sessionID;

    public Transcript(String sessionID) {
        this.sessionID = sessionID;
        this.packets = new ArrayList();
    }

    public Transcript(String sessionID, List packets) {
        this.sessionID = sessionID;
        this.packets = packets;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public List getPackets() {
        return Collections.unmodifiableList(this.packets);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<transcript xmlns=\"http://jivesoftware.com/protocol/workgroup\" sessionID=\"").append(this.sessionID).append("\">");
        for (Packet packet : this.packets) {
            buf.append(packet.toXML());
        }
        buf.append("</transcript>");
        return buf.toString();
    }
}
