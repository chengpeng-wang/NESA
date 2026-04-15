package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;

public class XHTMLExtension implements PacketExtension {
    private List bodies = new ArrayList();

    public String getElementName() {
        return "html";
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/xhtml-im";
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        Iterator i = getBodies();
        while (i.hasNext()) {
            buf.append((String) i.next());
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

    public Iterator getBodies() {
        Iterator it;
        synchronized (this.bodies) {
            it = Collections.unmodifiableList(new ArrayList(this.bodies)).iterator();
        }
        return it;
    }

    public void addBody(String body) {
        synchronized (this.bodies) {
            this.bodies.add(body);
        }
    }

    public int getBodiesCount() {
        return this.bodies.size();
    }
}
