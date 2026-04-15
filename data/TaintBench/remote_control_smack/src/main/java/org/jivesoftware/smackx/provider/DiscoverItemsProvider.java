package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverItemsProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        DiscoverItems discoverItems = new DiscoverItems();
        boolean done = false;
        String jid = "";
        String name = "";
        String action = "";
        String node = "";
        discoverItems.setNode(parser.getAttributeValue("", "node"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2 && "item".equals(parser.getName())) {
                jid = parser.getAttributeValue("", "jid");
                name = parser.getAttributeValue("", "name");
                node = parser.getAttributeValue("", "node");
                action = parser.getAttributeValue("", "action");
            } else if (eventType == 3 && "item".equals(parser.getName())) {
                Item item = new Item(jid);
                item.setName(name);
                item.setNode(node);
                item.setAction(action);
                discoverItems.addItem(item);
            } else if (eventType == 3 && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return discoverItems;
    }
}
