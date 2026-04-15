package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class ListObjectRequest extends BaiduBCSRequest {
    private int limit = -1;
    private int listModel = 0;
    private String prefix = null;
    private int start = -1;

    public ListObjectRequest(String str) {
        super(str, HttpMethodName.GET);
    }

    public int getLimit() {
        return this.limit;
    }

    public int getListModel() {
        return this.listModel;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int getStart() {
        return this.start;
    }

    public void setLimit(int i) {
        this.limit = i;
    }

    public void setListModel(int i) {
        this.listModel = i;
    }

    public void setPrefix(String str) {
        this.prefix = str;
    }

    public void setStart(int i) {
        this.start = i;
    }
}
