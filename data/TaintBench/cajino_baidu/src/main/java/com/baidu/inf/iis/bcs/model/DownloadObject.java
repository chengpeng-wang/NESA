package com.baidu.inf.iis.bcs.model;

import java.io.InputStream;

public class DownloadObject {
    private String bucket;
    private InputStream content;
    private String object;
    private ObjectMetadata objectMetadata = new ObjectMetadata();

    public String getBucket() {
        return this.bucket;
    }

    public InputStream getContent() {
        return this.content;
    }

    public String getObject() {
        return this.object;
    }

    public ObjectMetadata getObjectMetadata() {
        return this.objectMetadata;
    }

    public void setBucket(String str) {
        this.bucket = str;
    }

    public void setContent(InputStream inputStream) {
        this.content = inputStream;
    }

    public void setObject(String str) {
        this.object = str;
    }

    public void setObjectMetadata(ObjectMetadata objectMetadata) {
        this.objectMetadata = objectMetadata;
    }
}
