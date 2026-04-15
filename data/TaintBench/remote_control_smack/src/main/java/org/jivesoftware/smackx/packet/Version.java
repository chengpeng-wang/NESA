package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class Version extends IQ {
    private String name;
    private String os;
    private String version;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return this.os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:version\">");
        if (this.name != null) {
            buf.append("<name>").append(this.name).append("</name>");
        }
        if (this.version != null) {
            buf.append("<version>").append(this.version).append("</version>");
        }
        if (this.os != null) {
            buf.append("<os>").append(this.os).append("</os>");
        }
        buf.append("</query>");
        return buf.toString();
    }
}
