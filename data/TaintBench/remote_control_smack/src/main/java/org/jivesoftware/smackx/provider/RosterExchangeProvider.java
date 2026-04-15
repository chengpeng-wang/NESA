package org.jivesoftware.smackx.provider;

import java.util.ArrayList;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.RemoteRosterEntry;
import org.jivesoftware.smackx.packet.RosterExchange;
import org.xmlpull.v1.XmlPullParser;

public class RosterExchangeProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        RosterExchange rosterExchange = new RosterExchange();
        boolean done = false;
        String jid = "";
        String name = "";
        ArrayList groupsName = new ArrayList();
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("item")) {
                    groupsName = new ArrayList();
                    jid = parser.getAttributeValue("", "jid");
                    name = parser.getAttributeValue("", "name");
                }
                if (parser.getName().equals("group")) {
                    groupsName.add(parser.nextText());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("item")) {
                    rosterExchange.addRosterEntry(new RemoteRosterEntry(jid, name, (String[]) groupsName.toArray(new String[groupsName.size()])));
                }
                if (parser.getName().equals(GroupChatInvitation.ELEMENT_NAME)) {
                    done = true;
                }
            }
        }
        return rosterExchange;
    }
}
