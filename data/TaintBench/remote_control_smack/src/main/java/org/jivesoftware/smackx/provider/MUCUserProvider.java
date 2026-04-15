package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Decline;
import org.jivesoftware.smackx.packet.MUCUser.Destroy;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.packet.MUCUser.Item;
import org.jivesoftware.smackx.packet.MUCUser.Status;
import org.jivesoftware.smackx.workgroup.packet.RoomInvitation;
import org.xmlpull.v1.XmlPullParser;

public class MUCUserProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        MUCUser mucUser = new MUCUser();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals(RoomInvitation.ELEMENT_NAME)) {
                    mucUser.setInvite(parseInvite(parser));
                }
                if (parser.getName().equals("item")) {
                    mucUser.setItem(parseItem(parser));
                }
                if (parser.getName().equals("password")) {
                    mucUser.setPassword(parser.nextText());
                }
                if (parser.getName().equals("status")) {
                    mucUser.setStatus(new Status(parser.getAttributeValue("", "code")));
                }
                if (parser.getName().equals("decline")) {
                    mucUser.setDecline(parseDecline(parser));
                }
                if (parser.getName().equals("destroy")) {
                    mucUser.setDestroy(parseDestroy(parser));
                }
            } else if (eventType == 3 && parser.getName().equals(GroupChatInvitation.ELEMENT_NAME)) {
                done = true;
            }
        }
        return mucUser;
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

    private Invite parseInvite(XmlPullParser parser) throws Exception {
        boolean done = false;
        Invite invite = new Invite();
        invite.setFrom(parser.getAttributeValue("", PrivacyRule.SUBSCRIPTION_FROM));
        invite.setTo(parser.getAttributeValue("", "to"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("reason")) {
                    invite.setReason(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals(RoomInvitation.ELEMENT_NAME)) {
                done = true;
            }
        }
        return invite;
    }

    private Decline parseDecline(XmlPullParser parser) throws Exception {
        boolean done = false;
        Decline decline = new Decline();
        decline.setFrom(parser.getAttributeValue("", PrivacyRule.SUBSCRIPTION_FROM));
        decline.setTo(parser.getAttributeValue("", "to"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("reason")) {
                    decline.setReason(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("decline")) {
                done = true;
            }
        }
        return decline;
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
