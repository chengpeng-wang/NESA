package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class SharedGroupsInfo extends IQ {
    private List groups = new ArrayList();

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            SharedGroupsInfo groupsInfo = new SharedGroupsInfo();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2 && parser.getName().equals("group")) {
                    groupsInfo.getGroups().add(parser.nextText());
                } else if (eventType == 3 && parser.getName().equals("sharedgroup")) {
                    done = true;
                }
            }
            return groupsInfo;
        }
    }

    public List getGroups() {
        return this.groups;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<sharedgroup xmlns=\"http://www.jivesoftware.org/protocol/sharedgroup\">");
        for (Object append : this.groups) {
            buf.append("<group>").append(append).append("</group>");
        }
        buf.append("</sharedgroup>");
        return buf.toString();
    }
}
