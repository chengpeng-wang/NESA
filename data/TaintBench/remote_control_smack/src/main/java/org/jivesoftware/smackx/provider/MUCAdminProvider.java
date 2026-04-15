package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.MUCAdmin;
import org.jivesoftware.smackx.packet.MUCAdmin.Item;
import org.xmlpull.v1.XmlPullParser;

public class MUCAdminProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        MUCAdmin mucAdmin = new MUCAdmin();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("item")) {
                    mucAdmin.addItem(parseItem(parser));
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        return mucAdmin;
    }

    private Item parseItem(XmlPullParser parser) throws Exception {
        boolean done = false;
        Item item = new Item(parser.getAttributeValue("", "affiliation"), parser.getAttributeValue("", "role"));
        item.setNick(parser.getAttributeValue("", "nick"));
        item.setJid(parser.getAttributeValue("", "jid"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("actor")) {
                    item.setActor(parser.getAttributeValue("", "jid"));
                }
                if (parser.getName().equals("reason")) {
                    item.setReason(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("item")) {
                done = true;
            }
        }
        return item;
    }
}
