package com.facebook.ads.internal.thirdparty.http;

import com.moceanmobile.mast.MASTNativeAdConstants;

public abstract class l {
    protected String a = "";
    protected j b;
    protected String c;
    protected byte[] d;

    public l(String str, o oVar) {
        if (str != null) {
            this.a = str;
        }
        if (oVar != null) {
            this.a += MASTNativeAdConstants.QUESTIONMARK + oVar.a();
        }
    }

    public String a() {
        return this.a;
    }

    public j b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    public byte[] d() {
        return this.d;
    }
}
