package com.baidu.inf.iis.bcs.model;

public class SuperfileSubObject {
    private String bucket;
    private String etag;
    private String object;

    public SuperfileSubObject(String str, String str2, String str3) {
        this.bucket = str;
        this.object = str2;
        this.etag = str3;
    }

    public String getBucket() {
        return this.bucket;
    }

    public String getEtag() {
        return this.etag;
    }

    public String getObject() {
        return this.object;
    }

    public void setBucket(String str) {
        this.bucket = str;
    }

    public void setEtag(String str) {
        this.etag = str;
    }

    public void setObject(String str) {
        this.object = str;
    }
}
