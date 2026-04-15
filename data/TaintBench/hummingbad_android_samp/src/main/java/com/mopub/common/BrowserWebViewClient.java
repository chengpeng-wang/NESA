package com.mopub.common;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mopub.common.UrlHandler.Builder;
import com.mopub.common.UrlHandler.ResultActions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Drawables;
import java.util.EnumSet;

class BrowserWebViewClient extends WebViewClient {
    private static final EnumSet<UrlAction> SUPPORTED_URL_ACTIONS = EnumSet.of(UrlAction.HANDLE_PHONE_SCHEME, new UrlAction[]{UrlAction.OPEN_APP_MARKET, UrlAction.OPEN_IN_APP_BROWSER, UrlAction.HANDLE_SHARE_TWEET, UrlAction.FOLLOW_DEEP_LINK_WITH_FALLBACK, UrlAction.FOLLOW_DEEP_LINK});
    /* access modifiers changed from: private */
    @NonNull
    public MoPubBrowser mMoPubBrowser;

    public BrowserWebViewClient(@NonNull MoPubBrowser moPubBrowser) {
        this.mMoPubBrowser = moPubBrowser;
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        MoPubLog.d("MoPubBrowser error: " + description);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return new Builder().withSupportedUrlActions(SUPPORTED_URL_ACTIONS).withoutMoPubBrowser().withResultActions(new ResultActions() {
            public void urlHandlingSucceeded(@NonNull String url, @NonNull UrlAction urlAction) {
                if (urlAction.equals(UrlAction.OPEN_IN_APP_BROWSER)) {
                    BrowserWebViewClient.this.mMoPubBrowser.getWebView().loadUrl(url);
                } else {
                    BrowserWebViewClient.this.mMoPubBrowser.finish();
                }
            }

            public void urlHandlingFailed(@NonNull String url, @NonNull UrlAction lastFailedUrlAction) {
            }
        }).build().handleResolvedUrl(this.mMoPubBrowser.getApplicationContext(), url, true, null);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        this.mMoPubBrowser.getForwardButton().setImageDrawable(Drawables.UNRIGHT_ARROW.createDrawable(this.mMoPubBrowser));
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.mMoPubBrowser.getBackButton().setImageDrawable(view.canGoBack() ? Drawables.LEFT_ARROW.createDrawable(this.mMoPubBrowser) : Drawables.UNLEFT_ARROW.createDrawable(this.mMoPubBrowser));
        this.mMoPubBrowser.getForwardButton().setImageDrawable(view.canGoForward() ? Drawables.RIGHT_ARROW.createDrawable(this.mMoPubBrowser) : Drawables.UNRIGHT_ARROW.createDrawable(this.mMoPubBrowser));
    }
}
