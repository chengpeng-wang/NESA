package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class ObjectMetadataResponseHandler extends HttpResponseHandler<ObjectMetadata> {
    public BaiduBCSResponse<ObjectMetadata> handle(BCSHttpResponse bCSHttpResponse) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        populateObjectMetadata(bCSHttpResponse, objectMetadata);
        BaiduBCSResponse parseResponseMetadata = parseResponseMetadata(bCSHttpResponse);
        parseResponseMetadata.setResult(objectMetadata);
        return parseResponseMetadata;
    }
}
