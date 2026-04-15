package org.jivesoftware.smackx.workgroup.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MetaDataUtils {
    public static Map parseMetaData(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != 2 || !parser.getName().equals(MetaData.ELEMENT_NAME) || !parser.getNamespace().equals("http://jivesoftware.com/protocol/workgroup")) {
            return Collections.EMPTY_MAP;
        }
        Map hashtable = new Hashtable();
        int eventType = parser.nextTag();
        while (true) {
            if (eventType == 3 && parser.getName().equals(MetaData.ELEMENT_NAME)) {
                return hashtable;
            }
            String name = parser.getAttributeValue(0);
            String value = parser.nextText();
            if (hashtable.containsKey(name)) {
                ((List) hashtable.get(name)).add(value);
            } else {
                List values = new ArrayList();
                values.add(value);
                hashtable.put(name, values);
            }
            eventType = parser.nextTag();
        }
    }

    public static String serializeMetaData(Map metaData) {
        StringBuilder buf = new StringBuilder();
        if (metaData != null && metaData.size() > 0) {
            buf.append("<metadata xmlns=\"http://jivesoftware.com/protocol/workgroup\">");
            for (Object key : metaData.keySet()) {
                Object value = metaData.get(key);
                if (value instanceof List) {
                    for (String v : (List) metaData.get(key)) {
                        buf.append("<value name=\"").append(key).append("\">");
                        buf.append(StringUtils.escapeForXML(v));
                        buf.append("</value>");
                    }
                } else if (value instanceof String) {
                    buf.append("<value name=\"").append(key).append("\">");
                    buf.append(StringUtils.escapeForXML((String) value));
                    buf.append("</value>");
                }
            }
            buf.append("</metadata>");
        }
        return buf.toString();
    }
}
