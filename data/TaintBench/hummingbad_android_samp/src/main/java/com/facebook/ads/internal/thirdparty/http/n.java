package com.facebook.ads.internal.thirdparty.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class n {
    private int a;
    private String b;
    private Map<String, List<String>> c;
    private byte[] d;

    public n(HttpURLConnection httpURLConnection, byte[] bArr) {
        try {
            this.a = httpURLConnection.getResponseCode();
            this.b = httpURLConnection.getURL().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.c = httpURLConnection.getHeaderFields();
        this.d = bArr;
    }

    public int a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public Map<String, List<String>> c() {
        return this.c;
    }

    public byte[] d() {
        return this.d;
    }

    public String e() {
        return this.d != null ? new String(this.d) : null;
    }
}
