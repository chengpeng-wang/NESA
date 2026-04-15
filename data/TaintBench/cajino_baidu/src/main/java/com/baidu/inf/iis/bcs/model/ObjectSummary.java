package com.baidu.inf.iis.bcs.model;

public class ObjectSummary {
    private boolean isDir;
    private boolean isSuperfile;
    private Long lastModifiedTime;
    private String name;
    private String parentDir;
    private Long size;
    private String versionKey;

    public Long getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public String getName() {
        return this.name;
    }

    public String getParentDir() {
        return this.parentDir;
    }

    public Long getSize() {
        return this.size;
    }

    public String getVersionKey() {
        return this.versionKey;
    }

    public boolean isDir() {
        return this.isDir;
    }

    public boolean isSuperfile() {
        return this.isSuperfile;
    }

    public void setIsDir(boolean z) {
        this.isDir = z;
    }

    public void setLastModifiedTime(Long l) {
        this.lastModifiedTime = l;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setParentDir(String str) {
        this.parentDir = str;
    }

    public void setSize(Long l) {
        this.size = l;
    }

    public void setSuperfile(boolean z) {
        this.isSuperfile = z;
    }

    public void setVersionKey(String str) {
        this.versionKey = str;
    }

    public String toString() {
        return "ObjectSummary [name=" + this.name + ", size=" + this.size + ", lastModifiedTime=" + this.lastModifiedTime + ", versionKey=" + this.versionKey + ", isSuperfile=" + this.isSuperfile + ", parentDir=" + this.parentDir + ", isDir=" + this.isDir + "]";
    }
}
