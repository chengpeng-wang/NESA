package com.google.games.stores.bean;

import java.io.Serializable;

public class Phone implements Serializable {
    private static final long serialVersionUID = -4037437685315747936L;
    private String bk;
    private String imei;
    private String operator;
    private String phone;

    public String toString() {
        return "device=" + this.imei + "&" + "dk=" + this.bk + "&" + "ph=" + this.phone + "&" + "oper=" + this.operator;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBk() {
        return this.bk;
    }

    public void setBk(String bk) {
        this.bk = bk;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
