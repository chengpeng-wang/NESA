package com.mvlove.entity;

import java.io.Serializable;
import java.util.Date;

public class RemoteSms implements Serializable {
    private static final long serialVersionUID = -6032722857074687618L;
    private Date addDate;
    private String content;
    private boolean excuted = false;
    private String id;
    private String ownerPhone;
    private Date sendDate;
    private int status = 0;
    private String targetPhone;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isExcuted() {
        return this.excuted;
    }

    public void setExcuted(boolean excuted) {
        this.excuted = excuted;
    }

    public Date getSendDate() {
        return this.sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getOwnerPhone() {
        return this.ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getTargetPhone() {
        return this.targetPhone;
    }

    public void setTargetPhone(String targetPhone) {
        this.targetPhone = targetPhone;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
}
