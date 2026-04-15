package com.mb.bdapp.dao;

public class AdvAction {
    private int HmDb;
    private int adsDb;
    private int click;
    private int down;
    private int downSucc;
    private int getRef;
    private int install;
    private int installSecc;
    private int isGp;
    private String pkgName;
    private int req;
    private int sendRef;
    private int show;
    private int start;

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getReq() {
        return this.req;
    }

    public void setReq(int req) {
        this.req = req;
    }

    public int getShow() {
        return this.show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getClick() {
        return this.click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getAdsDb() {
        return this.adsDb;
    }

    public void setAdsDb(int adsDb) {
        this.adsDb = adsDb;
    }

    public int getHmDb() {
        return this.HmDb;
    }

    public void setHmDb(int hmDb) {
        this.HmDb = hmDb;
    }

    public int getIsGp() {
        return this.isGp;
    }

    public void setIsGp(int isGp) {
        this.isGp = isGp;
    }

    public int getGetRef() {
        return this.getRef;
    }

    public void setGetRef(int getRef) {
        this.getRef = getRef;
    }

    public int getDown() {
        return this.down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getDownSucc() {
        return this.downSucc;
    }

    public void setDownSucc(int downSucc) {
        this.downSucc = downSucc;
    }

    public int getInstall() {
        return this.install;
    }

    public void setInstall(int install) {
        this.install = install;
    }

    public int getInstallSecc() {
        return this.installSecc;
    }

    public void setInstallSecc(int installSecc) {
        this.installSecc = installSecc;
    }

    public int getSendRef() {
        return this.sendRef;
    }

    public void setSendRef(int sendRef) {
        this.sendRef = sendRef;
    }

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getActions() {
        return "1:" + this.req + ",2:" + this.show + ",3:" + this.click + ",4:" + this.adsDb + ",5:" + this.HmDb + ",6:" + this.isGp + ",7:" + this.getRef + ",8:" + this.down + ",9:" + this.downSucc + ",10:" + this.install + ",11:" + this.installSecc + ",12:" + this.sendRef + ",13:" + this.start;
    }

    public String getJson() {
        return "{\"pkgname\":\"" + this.pkgName + "\",\"gpOptimize\":\"" + this.isGp + "\",\"action\":\"" + getActions() + "\"}";
    }

    public String toString() {
        return getJson();
    }
}
