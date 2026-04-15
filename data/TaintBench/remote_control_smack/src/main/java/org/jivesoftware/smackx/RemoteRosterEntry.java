package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RemoteRosterEntry {
    private final List<String> groupNames = new ArrayList();
    private String name;
    private String user;

    public RemoteRosterEntry(String user, String name, String[] groups) {
        this.user = user;
        this.name = name;
        if (groups != null) {
            this.groupNames.addAll(Arrays.asList(groups));
        }
    }

    public String getUser() {
        return this.user;
    }

    public String getName() {
        return this.name;
    }

    public Iterator getGroupNames() {
        Iterator it;
        synchronized (this.groupNames) {
            it = Collections.unmodifiableList(this.groupNames).iterator();
        }
        return it;
    }

    public String[] getGroupArrayNames() {
        String[] strArr;
        synchronized (this.groupNames) {
            strArr = (String[]) Collections.unmodifiableList(this.groupNames).toArray(new String[this.groupNames.size()]);
        }
        return strArr;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<item jid=\"").append(this.user).append("\"");
        if (this.name != null) {
            buf.append(" name=\"").append(this.name).append("\"");
        }
        buf.append(">");
        synchronized (this.groupNames) {
            for (String groupName : this.groupNames) {
                buf.append("<group>").append(groupName).append("</group>");
            }
        }
        buf.append("</item>");
        return buf.toString();
    }
}
