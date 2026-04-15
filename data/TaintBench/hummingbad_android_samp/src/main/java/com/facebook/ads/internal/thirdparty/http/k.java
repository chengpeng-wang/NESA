package com.facebook.ads.internal.thirdparty.http;

import com.moceanmobile.mast.MASTNativeAdConstants;

public class k extends l {
    public k(String str, o oVar) {
        super(str, null);
        this.b = j.POST;
        this.a = str;
        this.c = MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE_VALUE;
        if (oVar != null) {
            this.d = oVar.b();
        }
    }
}
