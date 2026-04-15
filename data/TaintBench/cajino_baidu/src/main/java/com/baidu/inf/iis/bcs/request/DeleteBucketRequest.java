package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class DeleteBucketRequest extends BaiduBCSRequest {
    public DeleteBucketRequest(String str) {
        super(str, HttpMethodName.DELETE);
    }
}
