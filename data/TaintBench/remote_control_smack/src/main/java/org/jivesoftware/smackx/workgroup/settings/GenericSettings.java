package org.jivesoftware.smackx.workgroup.settings;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class GenericSettings extends IQ {
    public static final String ELEMENT_NAME = "generic-metadata";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private Map map = new HashMap();
    private String query;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            GenericSettings setting = new GenericSettings();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && "entry".equals(parser.getName())) {
                    eventType = parser.next();
                    String name = parser.nextText();
                    eventType = parser.next();
                    setting.getMap().put(name, parser.nextText());
                } else if (eventType == 3 && GenericSettings.ELEMENT_NAME.equals(parser.getName())) {
                    done = true;
                }
            }
            return setting;
        }
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map getMap() {
        return this.map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=");
        buf.append('\"');
        buf.append("http://jivesoftware.com/protocol/workgroup");
        buf.append('\"');
        buf.append(">");
        if (ModelUtil.hasLength(getQuery())) {
            buf.append("<query>" + getQuery() + "</query>");
        }
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
