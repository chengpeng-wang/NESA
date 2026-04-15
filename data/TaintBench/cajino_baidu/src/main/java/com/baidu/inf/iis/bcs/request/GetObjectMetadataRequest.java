package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class GetObjectMetadataRequest extends BaiduBCSRequest {
    public GetObjectMetadataRequest(String str, String str2) {
        super(str, str2, HttpMethodName.HEAD);
    }
}
