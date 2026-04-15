package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.Pair;

public class GetObjectRequest extends BaiduBCSRequest {
    private ObjectMetadata objectMetadata;
    private Pair<Long> range;
    private String versionKey;

    public GetObjectRequest(String str, String str2) {
        super(str, str2, HttpMethodName.GET);
    }

    public GetObjectRequest(String str, String str2, String str3) {
        super(str, str2, HttpMethodName.GET);
        this.versionKey = str3;
    }

    public ObjectMetadata getObjectMetadata() {
        return this.objectMetadata;
    }

    public void setObjectMetadata(ObjectMetadata objectMetadata) {
        this.objectMetadata = objectMetadata;
    }

    public Pair<Long> getRange() {
        return this.range;
    }

    public void setRange(Pair<Long> pair) {
        this.range = pair;
    }

    public String getVersionKey() {
        return this.versionKey;
    }

    public void setVersionKey(String str) {
        this.versionKey = str;
    }
}
