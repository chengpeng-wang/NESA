package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.model.DownloadObject;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class ObjectResponseHandler extends HttpResponseHandler<DownloadObject> {
    public BaiduBCSResponse<DownloadObject> handle(BCSHttpResponse bCSHttpResponse) {
        DownloadObject downloadObject = new DownloadObject();
        downloadObject.setBucket(bCSHttpResponse.getRequest().getOriginalRequest().getBucket());
        downloadObject.setObject(bCSHttpResponse.getRequest().getOriginalRequest().getObject());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        populateObjectMetadata(bCSHttpResponse, objectMetadata);
        downloadObject.setObjectMetadata(objectMetadata);
        BaiduBCSResponse parseResponseMetadata = parseResponseMetadata(bCSHttpResponse);
        downloadObject.setContent(bCSHttpResponse.getContent());
        parseResponseMetadata.setResult(downloadObject);
        return parseResponseMetadata;
    }

    public boolean isNeedsConnectionLeftOpen() {
        return true;
    }
}
