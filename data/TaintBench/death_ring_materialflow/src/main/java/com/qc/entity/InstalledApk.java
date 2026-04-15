package com.qc.entity;

public class InstalledApk {
    private String createTime;
    private int id;
    private int kssiid;
    private String packageName;
    private String silencename;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKssiid() {
        return this.kssiid;
    }

    public void setKssiid(int kssiid) {
        this.kssiid = kssiid;
    }

    public String getSilencename() {
        return this.silencename;
    }

    public void setSilencename(String silencename) {
        this.silencename = silencename;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
