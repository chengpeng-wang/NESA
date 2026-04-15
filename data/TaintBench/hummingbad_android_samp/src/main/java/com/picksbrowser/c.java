package com.picksbrowser;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/* compiled from: PicksBrowser */
final class c extends WebChromeClient {
    final /* synthetic */ PicksBrowser a;

    c(PicksBrowser picksBrowser) {
        this.a = picksBrowser;
    }

    public final void onProgressChanged(WebView webView, int progress) {
        this.a.setTitle("Loading...");
        this.a.setProgress(progress * 100);
        if (progress == 100) {
            this.a.setTitle(webView.getUrl());
        }
    }
}
