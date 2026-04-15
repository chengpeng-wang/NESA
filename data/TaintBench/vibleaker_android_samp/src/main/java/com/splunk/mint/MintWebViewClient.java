package com.splunk.mint;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MintWebViewClient extends WebViewClient {
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        view.loadUrl(MintJavascript.loadMintJavascript());
    }
}
