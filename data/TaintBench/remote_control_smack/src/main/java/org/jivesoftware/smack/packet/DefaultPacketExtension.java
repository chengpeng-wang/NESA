package org.jivesoftware.smack.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultPacketExtension implements PacketExtension {
    private String elementName;
    private Map<String, String> map;
    private String namespace;

    public DefaultPacketExtension(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

    public String getElementName() {
        return this.elementName;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(this.elementName).append(" xmlns=\"").append(this.namespace).append("\">");
        for (String name : getNames()) {
            String value = getValue(name);
            buf.append("<").append(name).append(">");
            buf.append(value);
            buf.append("</").append(name).append(">");
        }
        buf.append("</").append(this.elementName).append(">");
        return buf.toString();
    }

    public synchronized Collection<String> getNames() {
        Collection<String> emptySet;
        if (this.map == null) {
            emptySet = Collections.emptySet();
        } else {
            emptySet = Collections.unmodifiableSet(new HashMap(this.map).keySet());
        }
        return emptySet;
    }

    public synchronized String getValue(String name) {
        String str;
        if (this.map == null) {
            str = null;
        } else {
            str = (String) this.map.get(name);
        }
        return str;
    }

    public synchronized void setValue(String name, String value) {
        if (this.map == null) {
            this.map = new HashMap();
        }
        this.map.put(name, value);
    }
}
