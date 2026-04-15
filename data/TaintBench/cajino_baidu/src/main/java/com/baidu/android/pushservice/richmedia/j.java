package com.baidu.android.pushservice.richmedia;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;

class j extends WebChromeClient {
    final /* synthetic */ MediaViewActivity a;

    j(MediaViewActivity mediaViewActivity) {
        this.a = mediaViewActivity;
    }

    public void onHideCustomView() {
    }

    public void onProgressChanged(WebView webView, int i) {
    }

    public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
    }
}
