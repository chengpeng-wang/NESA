package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.SuperfileSubObject;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import java.util.List;

public class PutSuperfileRequest extends BaiduBCSRequest {
    private X_BS_ACL acl = null;
    private ObjectMetadata objectMetadata = null;
    private List<SuperfileSubObject> subObjectList = null;

    public PutSuperfileRequest(String str, String str2, List<SuperfileSubObject> list) {
        super(str, str2, HttpMethodName.PUT);
        this.subObjectList = list;
    }

    public PutSuperfileRequest(String str, String str2, ObjectMetadata objectMetadata, List<SuperfileSubObject> list) {
        super(str, str2, HttpMethodName.PUT);
        this.subObjectList = list;
        this.objectMetadata = objectMetadata;
    }

    public X_BS_ACL getAcl() {
        return this.acl;
    }

    public ObjectMetadata getObjectMetadata() {
        return this.objectMetadata;
    }

    public List<SuperfileSubObject> getSubObjectList() {
        return this.subObjectList;
    }

    public void setAcl(X_BS_ACL x_bs_acl) {
        this.acl = x_bs_acl;
    }

    public void setObjectMetadata(ObjectMetadata objectMetadata) {
        this.objectMetadata = objectMetadata;
    }

    public void setSubObjectList(List<SuperfileSubObject> list) {
        this.subObjectList = list;
    }
}
