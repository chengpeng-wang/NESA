package com.baidu.inf.iis.bcs.model;

public class BucketSummary {
    private String bucket;
    private Long cdatatime;
    private Long totalCapacity;
    private Long usedCapacity;

    public BucketSummary(String str) {
        this.bucket = str;
    }

    public String getBucket() {
        return this.bucket;
    }

    public Long getCdatatime() {
        return this.cdatatime;
    }

    public Long getTotalCapacity() {
        return this.totalCapacity;
    }

    public Long getUsedCapacity() {
        return this.usedCapacity;
    }

    public void setBucket(String str) {
        this.bucket = str;
    }

    public void setCdatatime(Long l) {
        this.cdatatime = l;
    }

    public void setTotalCapacity(Long l) {
        this.totalCapacity = l;
    }

    public void setUsedCapacity(Long l) {
        this.usedCapacity = l;
    }

    public String toString() {
        return "BCSBucket [bucket=" + this.bucket + ", cdatatime=" + this.cdatatime + ", usedCapacity=" + this.usedCapacity + ", totalCapacity=" + this.totalCapacity + "]";
    }
}
