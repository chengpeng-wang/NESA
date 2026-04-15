package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.MUCOwner;
import org.jivesoftware.smackx.packet.MUCOwner.Destroy;
import org.jivesoftware.smackx.packet.MUCOwner.Item;
import org.xmlpull.v1.XmlPullParser;

public class MUCOwnerProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        MUCOwner mucOwner = new MUCOwner();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("item")) {
                    mucOwner.addItem(parseItem(parser));
                } else if (parser.getName().equals("destroy")) {
                    mucOwner.setDestroy(parseDestroy(parser));
                } else {
                    mucOwner.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        return mucOwner;
    }

    private Item parseItem(XmlPullParser parser) throws Exception {
        boolean done = false;
        Item item = new Item(parser.getAttributeValue("", "affiliation"));
        item.setNick(parser.getAttributeValue("", "nick"));
        item.setRole(parser.getAttributeValue("", "role"));
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

    private Destroy parseDestroy(XmlPullParser parser) throws Exception {
        boolean done = false;
        Destroy destroy = new Destroy();
        destroy.setJid(parser.getAttributeValue("", "jid"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("reason")) {
                    destroy.setReason(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("destroy")) {
                done = true;
            }
        }
        return destroy;
    }
}
