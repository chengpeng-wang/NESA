package com.baidu.inf.iis.bcs.model;

public enum X_BS_ACL {
    Private("private"),
    PublicRead("public-read"),
    PublicWrite("public-write"),
    PublicReadWrite("public-read-write"),
    PublicControl("public-control");
    
    private final String x_bs_acl;

    private X_BS_ACL(String str) {
        this.x_bs_acl = str;
    }

    public String toString() {
        return this.x_bs_acl;
    }
}
