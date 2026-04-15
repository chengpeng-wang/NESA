package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.XHTMLExtension;
import org.xmlpull.v1.XmlPullParser;

public class XHTMLExtensionProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        XHTMLExtension xhtmlExtension = new XHTMLExtension();
        boolean done = false;
        StringBuilder buffer = new StringBuilder();
        int startDepth = parser.getDepth();
        int depth = parser.getDepth();
        String lastTag = "";
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("body")) {
                    buffer = new StringBuilder();
                    depth = parser.getDepth();
                }
                lastTag = parser.getText();
                buffer.append(parser.getText());
            } else if (eventType == 4) {
                if (buffer != null) {
                    buffer.append(StringUtils.escapeForXML(parser.getText()));
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("body") && parser.getDepth() <= depth) {
                    buffer.append(parser.getText());
                    xhtmlExtension.addBody(buffer.toString());
                } else if (parser.getName().equals(xhtmlExtension.getElementName()) && parser.getDepth() <= startDepth) {
                    done = true;
                } else if (!lastTag.equals(parser.getText())) {
                    buffer.append(parser.getText());
                }
            }
        }
        return xhtmlExtension;
    }
}
