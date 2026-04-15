package org.jivesoftware.smackx.workgroup.ext.macros;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class Macros extends IQ {
    public static final String ELEMENT_NAME = "macros";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private static ClassLoader cl;
    private boolean personal;
    private MacroGroup personalMacroGroup;
    private MacroGroup rootGroup;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            Macros macroGroup = new Macros();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("model")) {
                        parser.nextText();
                    }
                } else if (eventType == 3 && parser.getName().equals(Macros.ELEMENT_NAME)) {
                    done = true;
                }
            }
            return macroGroup;
        }
    }

    public static void setClassLoader(ClassLoader cloader) {
        cl = cloader;
    }

    public MacroGroup getRootGroup() {
        return this.rootGroup;
    }

    public void setRootGroup(MacroGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public boolean isPersonal() {
        return this.personal;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public MacroGroup getPersonalMacroGroup() {
        return this.personalMacroGroup;
    }

    public void setPersonalMacroGroup(MacroGroup personalMacroGroup) {
        this.personalMacroGroup = personalMacroGroup;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
        if (isPersonal()) {
            buf.append("<personal>true</personal>");
        }
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
