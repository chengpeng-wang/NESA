package org.jivesoftware.smackx.workgroup;

import java.util.Map;

public class WorkgroupInvitation {
    protected String groupChatName;
    protected String invitationSender;
    protected String issuingWorkgroupName;
    protected String messageBody;
    protected Map metaData;
    protected String sessionID;
    protected String uniqueID;

    public WorkgroupInvitation(String jid, String group, String workgroup, String sessID, String msgBody, String from) {
        this(jid, group, workgroup, sessID, msgBody, from, null);
    }

    public WorkgroupInvitation(String jid, String group, String workgroup, String sessID, String msgBody, String from, Map metaData) {
        this.uniqueID = jid;
        this.sessionID = sessID;
        this.groupChatName = group;
        this.issuingWorkgroupName = workgroup;
        this.messageBody = msgBody;
        this.invitationSender = from;
        this.metaData = metaData;
    }

    public String getUniqueID() {
        return this.uniqueID;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public String getGroupChatName() {
        return this.groupChatName;
    }

    public String getWorkgroupName() {
        return this.issuingWorkgroupName;
    }

    public String getMessageBody() {
        return this.messageBody;
    }

    public String getInvitationSender() {
        return this.invitationSender;
    }

    public Map getMetaData() {
        return this.metaData;
    }
}
