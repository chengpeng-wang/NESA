package com.mvlove.entity;

import java.io.Serializable;
import java.util.Date;

public class Motion implements Serializable {
    private static final long serialVersionUID = -6805241828270603275L;
    private Date addDate;
    private String eContent;
    private String eid;
    private boolean executed = false;
    private String id;
    private String phone;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isExecuted() {
        return this.executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public String getEid() {
        return this.eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String geteContent() {
        return this.eContent;
    }

    public void seteContent(String eContent) {
        this.eContent = eContent;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
}
