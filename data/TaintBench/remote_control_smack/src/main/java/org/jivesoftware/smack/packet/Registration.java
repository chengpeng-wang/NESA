package org.jivesoftware.smack.packet;

import java.util.Map;

public class Registration extends IQ {
    private Map<String, String> attributes = null;
    private String instructions = null;

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:register\">");
        if (this.instructions != null) {
            buf.append("<instructions>").append(this.instructions).append("</instructions>");
        }
        if (this.attributes != null && this.attributes.size() > 0) {
            for (String name : this.attributes.keySet()) {
                String value = (String) this.attributes.get(name);
                buf.append("<").append(name).append(">");
                buf.append(value);
                buf.append("</").append(name).append(">");
            }
        }
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }
}
