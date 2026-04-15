package com.baidu.inf.iis.bcs.auth;

public enum SigningAlgorithm {
    HmacSHA1("HmacSHA1"),
    HmacSHA256("HmacSHA256");
    
    private final String name;

    private SigningAlgorithm(String str) {
        this.name = str;
    }

    public String toString() {
        return this.name;
    }
}
