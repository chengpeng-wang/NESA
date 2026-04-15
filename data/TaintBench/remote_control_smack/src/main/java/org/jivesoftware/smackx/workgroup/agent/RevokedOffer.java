package org.jivesoftware.smackx.workgroup.agent;

import java.util.Date;

public class RevokedOffer {
    private String reason;
    private String sessionID;
    private Date timestamp;
    private String userID;
    private String userJID;
    private String workgroupName;

    RevokedOffer(String userJID, String userID, String workgroupName, String sessionID, String reason, Date timestamp) {
        this.userJID = userJID;
        this.userID = userID;
        this.workgroupName = workgroupName;
        this.sessionID = sessionID;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public String getUserJID() {
        return this.userJID;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getWorkgroupName() {
        return this.workgroupName;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public String getReason() {
        return this.reason;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }
}
