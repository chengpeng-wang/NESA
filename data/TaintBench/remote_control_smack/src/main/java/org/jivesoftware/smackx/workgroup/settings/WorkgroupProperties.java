package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupProperties extends IQ {
    public static final String ELEMENT_NAME = "workgroup-properties";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private boolean authRequired;
    private String email;
    private String fullName;
    private String jid;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            WorkgroupProperties props = new WorkgroupProperties();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "authRequired".equals(parser.getName())) {
                    props.setAuthRequired(new Boolean(parser.nextText()).booleanValue());
                } else if (eventType == 2 && "email".equals(parser.getName())) {
                    props.setEmail(parser.nextText());
                } else if (eventType == 2 && "name".equals(parser.getName())) {
                    props.setFullName(parser.nextText());
                } else if (eventType == 3 && WorkgroupProperties.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return props;
        }
    }

    public boolean isAuthRequired() {
        return this.authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=");
        buf.append('\"');
        buf.append("http://jivesoftware.com/protocol/workgroup");
        buf.append('\"');
        if (ModelUtil.hasLength(getJid())) {
            buf.append("jid=\"" + getJid() + "\" ");
        }
        buf.append("></").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
