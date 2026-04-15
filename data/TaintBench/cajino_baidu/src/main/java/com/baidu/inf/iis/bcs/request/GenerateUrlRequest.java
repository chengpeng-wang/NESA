package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.auth.BCSSignCondition;
import com.baidu.inf.iis.bcs.http.HttpMethodName;

public class GenerateUrlRequest extends BaiduBCSRequest {
    BCSSignCondition bcsSignCondition;

    public GenerateUrlRequest(HttpMethodName httpMethodName, String str, String str2) {
        super(str, str2, httpMethodName);
    }

    public BCSSignCondition getBcsSignCondition() {
        return this.bcsSignCondition;
    }

    public void setBcsSignCondition(BCSSignCondition bCSSignCondition) {
        this.bcsSignCondition = bCSSignCondition;
    }
}
