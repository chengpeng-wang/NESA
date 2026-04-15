package com.baidu.android.pushservice.a;

public class g {
    private String a;
    private String b;

    public g(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public String a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public String toString() {
        return "BindCache [mApiKey=" + this.a + ", mContent=" + this.b + "]";
    }
}
