package org.jivesoftware.smackx.workgroup.packet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;

public class Transcripts extends IQ {
    /* access modifiers changed from: private|static|final */
    public static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    private List<TranscriptSummary> summaries;
    private String userID;

    public static class AgentDetail {
        private String agentJID;
        private Date joinTime;
        private Date leftTime;

        public AgentDetail(String agentJID, Date joinTime, Date leftTime) {
            this.agentJID = agentJID;
            this.joinTime = joinTime;
            this.leftTime = leftTime;
        }

        public String getAgentJID() {
            return this.agentJID;
        }

        public Date getJoinTime() {
            return this.joinTime;
        }

        public Date getLeftTime() {
            return this.leftTime;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<agent>");
            if (this.agentJID != null) {
                buf.append("<agentJID>").append(this.agentJID).append("</agentJID>");
            }
            if (this.joinTime != null) {
                buf.append("<joinTime>").append(Transcripts.UTC_FORMAT.format(this.joinTime)).append("</joinTime>");
            }
            if (this.leftTime != null) {
                buf.append("<leftTime>").append(Transcripts.UTC_FORMAT.format(this.leftTime)).append("</leftTime>");
            }
            buf.append("</agent>");
            return buf.toString();
        }
    }

    public static class TranscriptSummary {
        private List<AgentDetail> agentDetails;
        private Date joinTime;
        private Date leftTime;
        private String sessionID;

        public TranscriptSummary(String sessionID, Date joinTime, Date leftTime, List<AgentDetail> agentDetails) {
            this.sessionID = sessionID;
            this.joinTime = joinTime;
            this.leftTime = leftTime;
            this.agentDetails = agentDetails;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public Date getJoinTime() {
            return this.joinTime;
        }

        public Date getLeftTime() {
            return this.leftTime;
        }

        public List<AgentDetail> getAgentDetails() {
            return this.agentDetails;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<transcript sessionID=\"").append(this.sessionID).append("\">");
            if (this.joinTime != null) {
                buf.append("<joinTime>").append(Transcripts.UTC_FORMAT.format(this.joinTime)).append("</joinTime>");
            }
            if (this.leftTime != null) {
                buf.append("<leftTime>").append(Transcripts.UTC_FORMAT.format(this.leftTime)).append("</leftTime>");
            }
            buf.append("<agents>");
            for (AgentDetail agentDetail : this.agentDetails) {
                buf.append(agentDetail.toXML());
            }
            buf.append("</agents></transcript>");
            return buf.toString();
        }
    }

    static {
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    public Transcripts(String userID) {
        this.userID = userID;
        this.summaries = new ArrayList();
    }

    public Transcripts(String userID, List<TranscriptSummary> summaries) {
        this.userID = userID;
        this.summaries = summaries;
    }

    public String getUserID() {
        return this.userID;
    }

    public List<TranscriptSummary> getSummaries() {
        return Collections.unmodifiableList(this.summaries);
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<transcripts xmlns=\"http://jivesoftware.com/protocol/workgroup\" userID=\"").append(this.userID).append("\">");
        for (TranscriptSummary transcriptSummary : this.summaries) {
            buf.append(transcriptSummary.toXML());
        }
        buf.append("</transcripts>");
        return buf.toString();
    }
}
