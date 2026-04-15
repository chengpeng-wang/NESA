package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.model.Empty;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class VoidResponseHandler extends HttpResponseHandler<Empty> {
    public BaiduBCSResponse<Empty> handle(BCSHttpResponse bCSHttpResponse) {
        BaiduBCSResponse parseResponseMetadata = parseResponseMetadata(bCSHttpResponse);
        parseResponseMetadata.setResult(new Empty());
        return parseResponseMetadata;
    }
}
