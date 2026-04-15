package com.mvlove.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 5934188524534292814L;
    private Date addDate;
    private String clientVersion;
    private Date endTime;
    private boolean fobiddenStatus = false;
    private boolean forbidden = false;
    private boolean hasNewMsg = false;
    private long id;
    private String imeiCode;
    private String model;
    private String phone;
    private Date phoneEndTime;
    private boolean phoneForbidden = false;
    private Date phoneStartTime;
    private boolean remoteDelStatus = false;
    private Date startTime;
    private boolean visible = true;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImeiCode() {
        return this.imeiCode;
    }

    public void setImeiCode(String imeiCode) {
        this.imeiCode = imeiCode;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public boolean isForbidden() {
        return this.forbidden;
    }

    public void setForbidden(boolean forbidden) {
        this.forbidden = forbidden;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getClientVersion() {
        return this.clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public boolean isHasNewMsg() {
        return this.hasNewMsg;
    }

    public void setHasNewMsg(boolean hasNewMsg) {
        this.hasNewMsg = hasNewMsg;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isFobiddenStatus() {
        return this.fobiddenStatus;
    }

    public void setFobiddenStatus(boolean fobiddenStatus) {
        this.fobiddenStatus = fobiddenStatus;
    }

    public boolean isRemoteDelStatus() {
        return this.remoteDelStatus;
    }

    public void setRemoteDelStatus(boolean remoteDelStatus) {
        this.remoteDelStatus = remoteDelStatus;
    }

    public boolean isPhoneForbidden() {
        return this.phoneForbidden;
    }

    public void setPhoneForbidden(boolean phoneForbidden) {
        this.phoneForbidden = phoneForbidden;
    }

    public Date getPhoneStartTime() {
        return this.phoneStartTime;
    }

    public void setPhoneStartTime(Date phoneStartTime) {
        this.phoneStartTime = phoneStartTime;
    }

    public Date getPhoneEndTime() {
        return this.phoneEndTime;
    }

    public void setPhoneEndTime(Date phoneEndTime) {
        this.phoneEndTime = phoneEndTime;
    }
}
