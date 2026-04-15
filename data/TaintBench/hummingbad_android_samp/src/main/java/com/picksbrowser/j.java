package com.picksbrowser;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/* compiled from: WebViews */
final class j extends WebChromeClient {
    j() {
    }

    public final boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        result.confirm();
        return true;
    }

    public final boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        result.confirm();
        return true;
    }

    public final boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm();
        return true;
    }

    public final boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        result.confirm();
        return true;
    }
}
