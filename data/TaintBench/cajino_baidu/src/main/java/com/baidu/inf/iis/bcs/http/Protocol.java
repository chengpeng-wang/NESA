package com.baidu.inf.iis.bcs.http;

import org.apache.http.HttpHost;

public enum Protocol {
    HTTP(HttpHost.DEFAULT_SCHEME_NAME),
    HTTPS("https");
    
    private final String protocol;

    private Protocol(String str) {
        this.protocol = str;
    }

    public String toString() {
        return this.protocol;
    }
}
