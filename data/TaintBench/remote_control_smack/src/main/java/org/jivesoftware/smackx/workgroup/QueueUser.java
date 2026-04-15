package org.jivesoftware.smackx.workgroup;

import java.util.Date;

public class QueueUser {
    private int estimatedTime;
    private Date joinDate;
    private int queuePosition;
    private String userID;

    public QueueUser(String uid, int position, int time, Date joinedAt) {
        this.userID = uid;
        this.queuePosition = position;
        this.estimatedTime = time;
        this.joinDate = joinedAt;
    }

    public String getUserID() {
        return this.userID;
    }

    public int getQueuePosition() {
        return this.queuePosition;
    }

    public int getEstimatedRemainingTime() {
        return this.estimatedTime;
    }

    public Date getQueueJoinTimestamp() {
        return this.joinDate;
    }
}
