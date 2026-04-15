package org.jivesoftware.smackx.packet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultPrivateData implements PrivateData {
    private String elementName;
    private Map map;
    private String namespace;

    public DefaultPrivateData(String elementName, String namespace) {
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
        Iterator i = getNames();
        while (i.hasNext()) {
            String name = (String) i.next();
            String value = getValue(name);
            buf.append("<").append(name).append(">");
            buf.append(value);
            buf.append("</").append(name).append(">");
        }
        buf.append("</").append(this.elementName).append(">");
        return buf.toString();
    }

    public synchronized Iterator getNames() {
        Iterator it;
        if (this.map == null) {
            it = Collections.EMPTY_LIST.iterator();
        } else {
            it = Collections.unmodifiableMap(new HashMap(this.map)).keySet().iterator();
        }
        return it;
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
