package com.baidu.inf.iis.bcs.auth;

public class BCSCredentials {
    private String accessKey;
    private String secretKey;

    public BCSCredentials(String str, String str2) {
        this.accessKey = str;
        this.secretKey = str2;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }
}
