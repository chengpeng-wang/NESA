package com.picksbrowser;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.picksbrowser.utils.a;

/* compiled from: BrowserWebViewClient */
final class b extends WebViewClient {
    @NonNull
    private PicksBrowser a;

    public b(@NonNull PicksBrowser picksBrowser) {
        this.a = picksBrowser;
    }

    public final void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    }

    public final boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        this.a.c().loadUrl(url);
        return true;
    }

    public final void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        this.a.b().setImageDrawable(a.UNRIGHT_ARROW.a(this.a));
    }

    public final void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.a.a().setImageDrawable(view.canGoBack() ? a.LEFT_ARROW.a(this.a) : a.UNLEFT_ARROW.a(this.a));
        this.a.b().setImageDrawable(view.canGoForward() ? a.RIGHT_ARROW.a(this.a) : a.UNRIGHT_ARROW.a(this.a));
    }
}
