package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.packet.IQ.Type;

public class Session extends IQ {
    public Session() {
        setType(Type.SET);
    }

    public String getChildElementXML() {
        return "<session xmlns=\"urn:ietf:params:xml:ns:xmpp-session\"/>";
    }
}
