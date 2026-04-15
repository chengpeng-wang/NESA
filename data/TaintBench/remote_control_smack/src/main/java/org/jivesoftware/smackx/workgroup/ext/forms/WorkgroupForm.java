package org.jivesoftware.smackx.workgroup.ext.forms;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupForm extends IQ {
    public static final String ELEMENT_NAME = "workgroup-form";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser parser) throws Exception {
            WorkgroupForm answer = new WorkgroupForm();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    answer.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                } else if (eventType == 3 && parser.getName().equals(WorkgroupForm.ELEMENT_NAME)) {
                    done = true;
                }
            }
            return answer;
        }
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
        buf.append(getExtensionsXML());
        buf.append("</").append(ELEMENT_NAME).append("> ");
        return buf.toString();
    }
}
