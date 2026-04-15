package com.qc.entity;

import java.util.List;

public class SilenceApkInfo {
    private List<MotionActive> activelist;
    private long delay;
    private int desktop;
    private int isclose;
    private long isreset;
    private int isrun;
    private long isuninstall;
    private long kssiid;
    private int location;
    private String packageName;
    private String silencename;
    private String version;
    private String visiturl;

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getKssiid() {
        return this.kssiid;
    }

    public void setKssiid(long kssiid) {
        this.kssiid = kssiid;
    }

    public String getSilencename() {
        return this.silencename;
    }

    public void setSilencename(String silencename) {
        this.silencename = silencename;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getDesktop() {
        return this.desktop;
    }

    public void setDesktop(int desktop) {
        this.desktop = desktop;
    }

    public long getIsuninstall() {
        return this.isuninstall;
    }

    public void setIsuninstall(long isuninstall) {
        this.isuninstall = isuninstall;
    }

    public long getIsreset() {
        return this.isreset;
    }

    public void setIsreset(long isreset) {
        this.isreset = isreset;
    }

    public String getVisiturl() {
        return this.visiturl;
    }

    public void setVisiturl(String visiturl) {
        this.visiturl = visiturl;
    }

    public int getIsrun() {
        return this.isrun;
    }

    public void setIsrun(int isrun) {
        this.isrun = isrun;
    }

    public int getIsclose() {
        return this.isclose;
    }

    public void setIsclose(int isclose) {
        this.isclose = isclose;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public List<MotionActive> getActivelist() {
        return this.activelist;
    }

    public void setActivelist(List<MotionActive> activelist) {
        this.activelist = activelist;
    }

    public int getLocation() {
        return this.location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
