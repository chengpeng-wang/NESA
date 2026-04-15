package com.mopub.mobileads.util;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Reflection.MethodBuilder;

public class WebViews {
    @TargetApi(11)
    public static void onResume(@NonNull WebView webView) {
        if (VERSION.SDK_INT >= 11) {
            webView.onResume();
            return;
        }
        try {
            new MethodBuilder(webView, "onResume").setAccessible().execute();
        } catch (Exception e) {
        }
    }

    @TargetApi(11)
    public static void onPause(@NonNull WebView webView, boolean isFinishing) {
        if (isFinishing) {
            webView.stopLoading();
            webView.loadUrl("");
        }
        if (VERSION.SDK_INT >= 11) {
            webView.onPause();
            return;
        }
        try {
            new MethodBuilder(webView, "onPause").setAccessible().execute();
        } catch (Exception e) {
        }
    }

    public static void setDisableJSChromeClient(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {
            public final boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                MoPubLog.d(message);
                result.confirm();
                return true;
            }

            public final boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                MoPubLog.d(message);
                result.confirm();
                return true;
            }

            public final boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                MoPubLog.d(message);
                result.confirm();
                return true;
            }

            public final boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                MoPubLog.d(message);
                result.confirm();
                return true;
            }
        });
    }
}
