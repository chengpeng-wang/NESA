package com.duapps.ad.b;

import android.content.Context;
import android.webkit.WebView;

/* compiled from: WebViewWrapper */
public final class n {
    public WebView a;
    public int b;
    public boolean c = false;

    public n(Context context) {
        this.a = new WebView(context);
        this.a.getSettings().setJavaScriptEnabled(true);
        this.a.getSettings().setCacheMode(2);
        this.a.getSettings().setLoadsImagesAutomatically(true);
        this.a.getSettings().setBlockNetworkImage(false);
        this.a.setVisibility(0);
    }
}
