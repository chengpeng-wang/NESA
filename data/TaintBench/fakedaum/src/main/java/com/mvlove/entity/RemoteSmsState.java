package com.mvlove.entity;

import java.io.Serializable;

public class RemoteSmsState implements Serializable {
    public static final int STATUS_DILIVERED = 2;
    public static final int STATUS_FAILED = 3;
    public static final int STATUS_SEND = 1;
    public static final int STATUS_UNSEND = 0;
    private static final long serialVersionUID = -5892542435473068352L;
    private String smsId;
    private int status = 1;

    public String getSmsId() {
        return this.smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
