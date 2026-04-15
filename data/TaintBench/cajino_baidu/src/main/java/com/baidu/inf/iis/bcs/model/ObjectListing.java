package com.baidu.inf.iis.bcs.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectListing {
    private String bucket;
    private int limit;
    private List<ObjectSummary> objectSummaries = new ArrayList();
    private int objectTotal;
    private String prefix;
    private int start;

    public ObjectListing addObjectSummary(ObjectSummary objectSummary) {
        this.objectSummaries.add(objectSummary);
        return this;
    }

    public String getBucket() {
        return this.bucket;
    }

    public int getLimit() {
        return this.limit;
    }

    public List<ObjectSummary> getObjectSummaries() {
        return this.objectSummaries;
    }

    public int getObjectTotal() {
        return this.objectTotal;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int getStart() {
        return this.start;
    }

    public void setBucket(String str) {
        this.bucket = str;
    }

    public void setLimit(int i) {
        this.limit = i;
    }

    public void setObjectSummaries(List<ObjectSummary> list) {
        this.objectSummaries = list;
    }

    public void setObjectTotal(int i) {
        this.objectTotal = i;
    }

    public void setPrefix(String str) {
        this.prefix = str;
    }

    public void setStart(int i) {
        this.start = i;
    }
}
