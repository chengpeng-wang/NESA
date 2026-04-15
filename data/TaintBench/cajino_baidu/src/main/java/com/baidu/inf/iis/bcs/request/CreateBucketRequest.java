package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;

public class CreateBucketRequest extends BaiduBCSRequest {
    private X_BS_ACL acl = null;

    public CreateBucketRequest(String str) {
        super(str, HttpMethodName.PUT);
    }

    public CreateBucketRequest(String str, X_BS_ACL x_bs_acl) {
        super(str, HttpMethodName.PUT);
        this.acl = x_bs_acl;
    }

    public X_BS_ACL getAcl() {
        return this.acl;
    }

    public void setAcl(X_BS_ACL x_bs_acl) {
        this.acl = x_bs_acl;
    }
}
