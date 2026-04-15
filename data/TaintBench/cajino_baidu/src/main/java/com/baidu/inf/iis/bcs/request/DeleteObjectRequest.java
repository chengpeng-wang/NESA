package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class DeleteObjectRequest extends BaiduBCSRequest {
    public DeleteObjectRequest(String str, String str2) {
        super(str, str2, HttpMethodName.DELETE);
    }
}
