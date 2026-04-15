package org.jivesoftware.smackx.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class UserSearchManager {
    private XMPPConnection con;
    private UserSearch userSearch = new UserSearch();

    public UserSearchManager(XMPPConnection con) {
        this.con = con;
    }

    public Form getSearchForm(String searchService) throws XMPPException {
        return this.userSearch.getSearchForm(this.con, searchService);
    }

    public ReportedData getSearchResults(Form searchForm, String searchService) throws XMPPException {
        return this.userSearch.sendSearchForm(this.con, searchForm, searchService);
    }

    public Collection getSearchServices() throws XMPPException {
        List<String> searchServices = new ArrayList();
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(this.con);
        Iterator<Item> iter = discoManager.discoverItems(this.con.getServiceName()).getItems();
        while (iter.hasNext()) {
            Item item = (Item) iter.next();
            try {
                try {
                    if (discoManager.discoverInfo(item.getEntityID()).containsFeature("jabber:iq:search")) {
                        searchServices.add(item.getEntityID());
                    }
                } catch (Exception e) {
                }
            } catch (XMPPException e2) {
            }
        }
        return searchServices;
    }
}
