package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class OfflineSettings extends IQ {
    public static final String ELEMENT_NAME = "offline-settings";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String emailAddress;
    private String offlineText;
    private String redirectURL;
    private String subject;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            OfflineSettings offlineSettings = new OfflineSettings();
            boolean done = false;
            String redirectPage = null;
            String subject = null;
            String offlineText = null;
            String emailAddress = null;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "redirectPage".equals(parser.getName())) {
                    redirectPage = parser.nextText();
                } else if (eventType == 2 && "subject".equals(parser.getName())) {
                    subject = parser.nextText();
                } else if (eventType == 2 && "offlineText".equals(parser.getName())) {
                    offlineText = parser.nextText();
                } else if (eventType == 2 && "emailAddress".equals(parser.getName())) {
                    emailAddress = parser.nextText();
                } else if (eventType == 3 && OfflineSettings.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            offlineSettings.setEmailAddress(emailAddress);
            offlineSettings.setRedirectURL(redirectPage);
            offlineSettings.setSubject(subject);
            offlineSettings.setOfflineText(offlineText);
            return offlineSettings;
        }
    }

    public String getRedirectURL() {
        if (ModelUtil.hasLength(this.redirectURL)) {
            return this.redirectURL;
        }
        return "";
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getOfflineText() {
        if (ModelUtil.hasLength(this.offlineText)) {
            return this.offlineText;
        }
        return "";
    }

    public void setOfflineText(String offlineText) {
        this.offlineText = offlineText;
    }

    public String getEmailAddress() {
        if (ModelUtil.hasLength(this.emailAddress)) {
            return this.emailAddress;
        }
        return "";
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSubject() {
        if (ModelUtil.hasLength(this.subject)) {
            return this.subject;
        }
        return "";
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean redirects() {
        return ModelUtil.hasLength(getRedirectURL());
    }

    public boolean isConfigured() {
        return ModelUtil.hasLength(getEmailAddress()) && ModelUtil.hasLength(getSubject()) && ModelUtil.hasLength(getOfflineText());
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=");
        buf.append('\"');
        buf.append("http://jivesoftware.com/protocol/workgroup");
        buf.append('\"');
        buf.append("></").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
