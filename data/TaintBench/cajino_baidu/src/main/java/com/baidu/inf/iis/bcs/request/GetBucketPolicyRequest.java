package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class GetBucketPolicyRequest extends BaiduBCSRequest {
    public GetBucketPolicyRequest(String str) {
        super(str, HttpMethodName.GET);
    }
}
