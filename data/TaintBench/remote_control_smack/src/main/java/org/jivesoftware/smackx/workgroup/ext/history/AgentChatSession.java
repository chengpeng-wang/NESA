package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.Date;

public class AgentChatSession {
    public long duration;
    public String question;
    public String sessionID;
    public Date startDate;
    public String visitorsEmail;
    public String visitorsName;

    public AgentChatSession(Date date, long duration, String visitorsName, String visitorsEmail, String sessionID, String question) {
        this.startDate = date;
        this.duration = duration;
        this.visitorsName = visitorsName;
        this.visitorsEmail = visitorsEmail;
        this.sessionID = sessionID;
        this.question = question;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getVisitorsName() {
        return this.visitorsName;
    }

    public void setVisitorsName(String visitorsName) {
        this.visitorsName = visitorsName;
    }

    public String getVisitorsEmail() {
        return this.visitorsEmail;
    }

    public void setVisitorsEmail(String visitorsEmail) {
        this.visitorsEmail = visitorsEmail;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
    }
}
