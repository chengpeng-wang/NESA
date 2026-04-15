package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class ListBucketRequest extends BaiduBCSRequest {
    public ListBucketRequest() {
        super("", "/", HttpMethodName.GET);
    }
}
