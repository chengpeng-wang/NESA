package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class StringResponseHandler extends HttpResponseHandler<String> {
    public BaiduBCSResponse<String> handle(BCSHttpResponse bCSHttpResponse) {
        BaiduBCSResponse parseResponseMetadata = parseResponseMetadata(bCSHttpResponse);
        parseResponseMetadata.setResult(getResponseContentByStr(bCSHttpResponse));
        return parseResponseMetadata;
    }
}
