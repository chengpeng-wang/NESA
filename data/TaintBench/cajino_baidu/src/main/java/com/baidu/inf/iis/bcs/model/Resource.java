package com.baidu.inf.iis.bcs.model;

public class Resource {
    private String bucket;
    private String object;

    public Resource(String str, String str2) {
        this.bucket = str;
        this.object = str2;
    }

    public String getBucket() {
        return this.bucket;
    }

    public String getObject() {
        return this.object;
    }

    public void setBucket(String str) {
        this.bucket = str;
    }

    public void setObject(String str) {
        this.object = str;
    }
}
