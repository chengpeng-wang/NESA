package org.jivesoftware.smack.packet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Privacy extends IQ {
    private String activeName;
    private boolean declineActiveList = false;
    private boolean declineDefaultList = false;
    private String defaultName;
    private Map<String, List<PrivacyItem>> itemLists = new HashMap();

    public List setPrivacyList(String listName, List<PrivacyItem> listItem) {
        getItemLists().put(listName, listItem);
        return listItem;
    }

    public List<PrivacyItem> setActivePrivacyList() {
        setActiveName(getDefaultName());
        return (List) getItemLists().get(getActiveName());
    }

    public void deletePrivacyList(String listName) {
        getItemLists().remove(listName);
        if (getDefaultName() != null && listName.equals(getDefaultName())) {
            setDefaultName(null);
        }
    }

    public List<PrivacyItem> getActivePrivacyList() {
        if (getActiveName() == null) {
            return null;
        }
        return (List) getItemLists().get(getActiveName());
    }

    public List<PrivacyItem> getDefaultPrivacyList() {
        if (getDefaultName() == null) {
            return null;
        }
        return (List) getItemLists().get(getDefaultName());
    }

    public List<PrivacyItem> getPrivacyList(String listName) {
        return (List) getItemLists().get(listName);
    }

    public PrivacyItem getItem(String listName, int order) {
        Iterator<PrivacyItem> values = getPrivacyList(listName).iterator();
        PrivacyItem itemFound = null;
        while (itemFound == null && values.hasNext()) {
            PrivacyItem element = (PrivacyItem) values.next();
            if (element.getOrder() == order) {
                itemFound = element;
            }
        }
        return itemFound;
    }

    public boolean changeDefaultList(String newDefault) {
        if (!getItemLists().containsKey(newDefault)) {
            return false;
        }
        setDefaultName(newDefault);
        return true;
    }

    public void deleteList(String listName) {
        getItemLists().remove(listName);
    }

    public String getActiveName() {
        return this.activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public Map<String, List<PrivacyItem>> getItemLists() {
        return this.itemLists;
    }

    public boolean isDeclineActiveList() {
        return this.declineActiveList;
    }

    public void setDeclineActiveList(boolean declineActiveList) {
        this.declineActiveList = declineActiveList;
    }

    public boolean isDeclineDefaultList() {
        return this.declineDefaultList;
    }

    public void setDeclineDefaultList(boolean declineDefaultList) {
        this.declineDefaultList = declineDefaultList;
    }

    public Set<String> getPrivacyListNames() {
        return this.itemLists.keySet();
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:privacy\">");
        if (isDeclineActiveList()) {
            buf.append("<active/>");
        } else if (getActiveName() != null) {
            buf.append("<active name=\"").append(getActiveName()).append("\"/>");
        }
        if (isDeclineDefaultList()) {
            buf.append("<default/>");
        } else if (getDefaultName() != null) {
            buf.append("<default name=\"").append(getDefaultName()).append("\"/>");
        }
        for (Entry<String, List<PrivacyItem>> entry : getItemLists().entrySet()) {
            String listName = (String) entry.getKey();
            List<PrivacyItem> items = (List) entry.getValue();
            if (items.isEmpty()) {
                buf.append("<list name=\"").append(listName).append("\"/>");
            } else {
                buf.append("<list name=\"").append(listName).append("\">");
            }
            for (PrivacyItem item : items) {
                buf.append(item.toXML());
            }
            if (!items.isEmpty()) {
                buf.append("</list>");
            }
        }
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }
}
