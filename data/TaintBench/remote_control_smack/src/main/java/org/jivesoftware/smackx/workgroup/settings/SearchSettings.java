package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class SearchSettings extends IQ {
    public static final String ELEMENT_NAME = "search-settings";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String forumsLocation;
    private String kbLocation;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            SearchSettings settings = new SearchSettings();
            boolean done = false;
            String kb = null;
            String forums = null;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "forums".equals(parser.getName())) {
                    forums = parser.nextText();
                } else if (eventType == 2 && "kb".equals(parser.getName())) {
                    kb = parser.nextText();
                } else if (eventType == 3 && SearchSettings.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            settings.setForumsLocation(forums);
            settings.setKbLocation(kb);
            return settings;
        }
    }

    public boolean isSearchEnabled() {
        return ModelUtil.hasLength(getForumsLocation()) && ModelUtil.hasLength(getKbLocation());
    }

    public String getForumsLocation() {
        return this.forumsLocation;
    }

    public void setForumsLocation(String forumsLocation) {
        this.forumsLocation = forumsLocation;
    }

    public String getKbLocation() {
        return this.kbLocation;
    }

    public void setKbLocation(String kbLocation) {
        this.kbLocation = kbLocation;
    }

    public boolean hasKB() {
        return ModelUtil.hasLength(getKbLocation());
    }

    public boolean hasForums() {
        return ModelUtil.hasLength(getForumsLocation());
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
