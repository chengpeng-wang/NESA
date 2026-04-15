package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.RemoteRosterEntry;

public class RosterExchange implements PacketExtension {
    private List remoteRosterEntries = new ArrayList();

    public RosterExchange(Roster roster) {
        for (RosterEntry rosterEntry : roster.getEntries()) {
            addRosterEntry(rosterEntry);
        }
    }

    public void addRosterEntry(RosterEntry rosterEntry) {
        List<String> groupNamesList = new ArrayList();
        for (RosterGroup group : rosterEntry.getGroups()) {
            groupNamesList.add(group.getName());
        }
        addRosterEntry(new RemoteRosterEntry(rosterEntry.getUser(), rosterEntry.getName(), (String[]) groupNamesList.toArray(new String[groupNamesList.size()])));
    }

    public void addRosterEntry(RemoteRosterEntry remoteRosterEntry) {
        synchronized (this.remoteRosterEntries) {
            this.remoteRosterEntries.add(remoteRosterEntry);
        }
    }

    public String getElementName() {
        return GroupChatInvitation.ELEMENT_NAME;
    }

    public String getNamespace() {
        return "jabber:x:roster";
    }

    public Iterator getRosterEntries() {
        Iterator it;
        synchronized (this.remoteRosterEntries) {
            it = Collections.unmodifiableList(new ArrayList(this.remoteRosterEntries)).iterator();
        }
        return it;
    }

    public int getEntryCount() {
        return this.remoteRosterEntries.size();
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        Iterator i = getRosterEntries();
        while (i.hasNext()) {
            buf.append(((RemoteRosterEntry) i.next()).toXML());
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }
}
