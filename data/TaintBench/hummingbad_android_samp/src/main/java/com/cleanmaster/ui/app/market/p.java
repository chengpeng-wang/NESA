package com.cleanmaster.ui.app.market;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* compiled from: ParseWebViewUrlUtils */
class p extends WebViewClient {
    final /* synthetic */ n a;

    p(n nVar) {
        this.a = nVar;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (view == null) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        view.loadUrl(url);
        return true;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (!this.a.b) {
            this.a.c.removeMessages(1);
            this.a.c.removeMessages(2);
            if (TextUtils.isEmpty(url)) {
                if (view != null) {
                    view.stopLoading();
                }
                this.a.b = true;
                this.a.c.sendMessage(this.a.c.obtainMessage(1, url));
            } else if (c.a(url)) {
                if (view != null) {
                    view.stopLoading();
                }
                this.a.b = true;
                this.a.c.sendMessage(this.a.c.obtainMessage(1, url));
            } else {
                this.a.c.sendMessageDelayed(this.a.c.obtainMessage(2, url), 10000);
                super.onPageStarted(view, url, favicon);
            }
        } else if (view != null) {
            try {
                view.stopLoading();
            } catch (Exception e) {
            }
        }
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (!this.a.b) {
            this.a.b = true;
            this.a.c.removeMessages(2);
            this.a.c.sendMessage(this.a.c.obtainMessage(1, failingUrl));
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public void onPageFinished(WebView view, String url) {
        if (!this.a.b) {
            this.a.c.removeMessages(2);
            this.a.c.sendMessageDelayed(this.a.c.obtainMessage(1, url), 4000);
            super.onPageFinished(view, url);
        }
    }
}
