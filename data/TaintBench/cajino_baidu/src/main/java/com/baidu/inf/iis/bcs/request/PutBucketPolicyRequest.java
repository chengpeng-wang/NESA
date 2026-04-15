package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import com.baidu.inf.iis.bcs.policy.Policy;

public class PutBucketPolicyRequest extends BaiduBCSRequest {
    private X_BS_ACL acl;
    private Policy policy;

    public PutBucketPolicyRequest(String str, Policy policy) {
        super(str, HttpMethodName.PUT);
        this.policy = policy;
    }

    public PutBucketPolicyRequest(String str, X_BS_ACL x_bs_acl) {
        super(str, HttpMethodName.PUT);
        this.acl = x_bs_acl;
    }

    public X_BS_ACL getAcl() {
        return this.acl;
    }

    public Policy getPolicy() {
        return this.policy;
    }

    public void setAcl(X_BS_ACL x_bs_acl) {
        this.acl = x_bs_acl;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
}
