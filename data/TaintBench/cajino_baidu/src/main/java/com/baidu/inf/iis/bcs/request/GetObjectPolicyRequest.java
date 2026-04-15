package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class GetObjectPolicyRequest extends BaiduBCSRequest {
    public GetObjectPolicyRequest(String str, String str2) {
        super(str, str2, HttpMethodName.GET);
    }
}
