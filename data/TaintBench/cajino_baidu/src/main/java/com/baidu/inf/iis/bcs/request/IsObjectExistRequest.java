package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class IsObjectExistRequest extends BaiduBCSRequest {
    public IsObjectExistRequest(String str, String str2) {
        super(str, str2, HttpMethodName.HEAD);
    }
}
