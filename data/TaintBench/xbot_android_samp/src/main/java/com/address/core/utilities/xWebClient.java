package com.address.core.utilities;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class xWebClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:onPageStart();");
    }
}
