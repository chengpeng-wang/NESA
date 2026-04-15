package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.policy.Policy;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class PolicyResponseHandler extends HttpResponseHandler<Policy> {
    public BaiduBCSResponse<Policy> handle(BCSHttpResponse bCSHttpResponse) {
        Policy buildJsonStr = new Policy().buildJsonStr(getResponseContentByStr(bCSHttpResponse));
        BaiduBCSResponse parseResponseMetadata = parseResponseMetadata(bCSHttpResponse);
        parseResponseMetadata.setResult(buildJsonStr);
        return parseResponseMetadata;
    }
}
