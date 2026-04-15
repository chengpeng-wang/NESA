package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.BCSServiceException;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;

public class SetObjectMetadataRequest extends BaiduBCSRequest {
    private ObjectMetadata metadata;

    public SetObjectMetadataRequest(String str, String str2, ObjectMetadata objectMetadata) {
        super(str, str2, HttpMethodName.PUT);
        if (objectMetadata == null) {
            throw new BCSServiceException("Metadata should not be null.");
        }
        this.metadata = objectMetadata;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ObjectMetadata objectMetadata) {
        this.metadata = objectMetadata;
    }
}
