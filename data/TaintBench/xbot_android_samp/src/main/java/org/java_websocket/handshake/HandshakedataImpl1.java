package org.java_websocket.handshake;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;

public class HandshakedataImpl1 implements HandshakeBuilder {
    private byte[] content;
    private LinkedHashMap<String, String> map = new LinkedHashMap();

    public Iterator<String> iterateHttpFields() {
        return Collections.unmodifiableSet(this.map.keySet()).iterator();
    }

    public String getFieldValue(String name) {
        String s = (String) this.map.get(name.toLowerCase(Locale.ENGLISH));
        if (s == null) {
            return "";
        }
        return s;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void put(String name, String value) {
        this.map.put(name.toLowerCase(Locale.ENGLISH), value);
    }

    public boolean hasFieldValue(String name) {
        return this.map.containsKey(name.toLowerCase(Locale.ENGLISH));
    }
}
