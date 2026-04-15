package org.jivesoftware.smackx.workgroup.agent;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import org.jivesoftware.smackx.packet.IBBExtensions.Open;

public class WorkgroupQueue {
    private int averageWaitTime = -1;
    private int currentChats = 0;
    private int maxChats = 0;
    private String name;
    private Date oldestEntry = null;
    private Status status = Status.CLOSED;
    private Set users = Collections.EMPTY_SET;

    public static class Status {
        public static final Status ACTIVE = new Status("active");
        public static final Status CLOSED = new Status("closed");
        public static final Status OPEN = new Status(Open.ELEMENT_NAME);
        private String value;

        public static Status fromString(String type) {
            if (type == null) {
                return null;
            }
            type = type.toLowerCase();
            if (OPEN.toString().equals(type)) {
                return OPEN;
            }
            if (ACTIVE.toString().equals(type)) {
                return ACTIVE;
            }
            if (CLOSED.toString().equals(type)) {
                return CLOSED;
            }
            return null;
        }

        private Status(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

    WorkgroupQueue(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    /* access modifiers changed from: 0000 */
    public void setStatus(Status status) {
        this.status = status;
    }

    public int getUserCount() {
        if (this.users == null) {
            return 0;
        }
        return this.users.size();
    }

    public Iterator getUsers() {
        if (this.users == null) {
            return Collections.EMPTY_SET.iterator();
        }
        return Collections.unmodifiableSet(this.users).iterator();
    }

    /* access modifiers changed from: 0000 */
    public void setUsers(Set users) {
        this.users = users;
    }

    public int getAverageWaitTime() {
        return this.averageWaitTime;
    }

    /* access modifiers changed from: 0000 */
    public void setAverageWaitTime(int averageTime) {
        this.averageWaitTime = averageTime;
    }

    public Date getOldestEntry() {
        return this.oldestEntry;
    }

    /* access modifiers changed from: 0000 */
    public void setOldestEntry(Date oldestEntry) {
        this.oldestEntry = oldestEntry;
    }

    public int getMaxChats() {
        return this.maxChats;
    }

    /* access modifiers changed from: 0000 */
    public void setMaxChats(int maxChats) {
        this.maxChats = maxChats;
    }

    public int getCurrentChats() {
        return this.currentChats;
    }

    /* access modifiers changed from: 0000 */
    public void setCurrentChats(int currentChats) {
        this.currentChats = currentChats;
    }
}
