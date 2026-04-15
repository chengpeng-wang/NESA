package org.jivesoftware.smackx;

import java.util.List;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public interface NodeInformationProvider {
    List<String> getNodeFeatures();

    List<Identity> getNodeIdentities();

    List<Item> getNodeItems();
}
