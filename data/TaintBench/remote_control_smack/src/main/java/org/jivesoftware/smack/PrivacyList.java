package org.jivesoftware.smack;

import java.util.List;
import org.jivesoftware.smack.packet.PrivacyItem;

public class PrivacyList {
    private boolean isActiveList;
    private boolean isDefaultList;
    private List<PrivacyItem> items;
    private String listName;

    protected PrivacyList(boolean isActiveList, boolean isDefaultList, String listName, List<PrivacyItem> privacyItems) {
        this.isActiveList = isActiveList;
        this.isDefaultList = isDefaultList;
        this.listName = listName;
        this.items = privacyItems;
    }

    public boolean isActiveList() {
        return this.isActiveList;
    }

    public boolean isDefaultList() {
        return this.isDefaultList;
    }

    public List<PrivacyItem> getItems() {
        return this.items;
    }

    public String toString() {
        return this.listName;
    }
}
