package com.qc.entity;

import java.io.Serializable;

public class AdsInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private int alerttype;
    private String description;
    private int descriptype;
    private int dwldhint;
    private String icon;
    private int id;
    private String number;
    private String packageName;
    private String pathimage;
    private String pathurl;
    private int sound;
    private long timeout;
    private String title;
    private int type;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSound() {
        return this.sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getDwldhint() {
        return this.dwldhint;
    }

    public void setDwldhint(int dwldhint) {
        this.dwldhint = dwldhint;
    }

    public String getPathurl() {
        return this.pathurl;
    }

    public void setPathurl(String pathurl) {
        this.pathurl = pathurl;
    }

    public int getDescriptype() {
        return this.descriptype;
    }

    public void setDescriptype(int descriptype) {
        this.descriptype = descriptype;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathimage() {
        return this.pathimage;
    }

    public void setPathimage(String pathimage) {
        this.pathimage = pathimage;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getAlerttype() {
        return this.alerttype;
    }

    public void setAlerttype(int alerttype) {
        this.alerttype = alerttype;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
