package com.mb.bdapp.db;

public class DuAd {
    public static final String DB_CONTENT = "content";
    public static final String DB_DOWN_RETRY = "down_retry";
    public static final String DB_DOWN_URL = "down_url";
    public static final String DB_DU_URL = "du_url";
    public static final String DB_GID = "gid";
    public static final String DB_ICON = "icon";
    public static final String DB_ID = "_ID";
    public static final String DB_INSTALL_RETRY = "install_retry";
    public static final String DB_MODIFY_TIME = "modify_time";
    public static final String DB_PNAME = "pname";
    public static final String DB_REFERRER = "referrer";
    public static final String DB_STATUS = "status";
    public static final String DB_TB = "tb_dubai_adv";
    public static final String DB_TITLE = "title";
    public static final int STATUS_DOWNING = 3;
    public static final int STATUS_DOWN_FAIL = 5;
    public static final int STATUS_DOWN_INSTALLED = 6;
    public static final int STATUS_DOWN_SUCCESS = 4;
    public static final int STATUS_INSTALLED_FAIL = 7;
    private int _ID;
    private String content;
    private int downRetry;
    private String downUrl;
    private String duUrl;
    private String gid;
    private String icon;
    private int installRetry;
    private long modifyTime;
    private String pname;
    private String referrer;
    private int status;
    private String title;

    public String getGid() {
        return this.gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public int get_ID() {
        return this._ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getDownUrl() {
        return this.downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDownRetry() {
        return this.downRetry;
    }

    public void setDownRetry(int downRetry) {
        this.downRetry = downRetry;
    }

    public String getPname() {
        return this.pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDuUrl() {
        return this.duUrl;
    }

    public void setDuUrl(String duUrl) {
        this.duUrl = duUrl;
    }

    public long getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getInstallRetry() {
        return this.installRetry;
    }

    public void setInstallRetry(int installRetry) {
        this.installRetry = installRetry;
    }
}
