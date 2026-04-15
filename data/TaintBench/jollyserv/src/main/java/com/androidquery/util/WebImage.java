package com.androidquery.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Picture;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;

public class WebImage extends WebViewClient {
    private static final String DOUBLE_TAP_TOAST_COUNT = "double_tap_toast_count";
    private static final String PREF_FILE = "WebViewSettings";
    private static String template;
    private int color;
    private boolean control;
    private Object progress;
    private String url;
    /* access modifiers changed from: private */
    public WebView wv;
    private boolean zoom;

    private static String getSource(Context context) {
        if (template == null) {
            try {
                template = new String(AQUtility.toBytes(context.getClassLoader().getResourceAsStream("com/androidquery/util/web_image.html")));
            } catch (Exception e) {
                AQUtility.debug(e);
            }
        }
        return template;
    }

    private static void fixWebviewTip(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
        if (prefs.getInt(DOUBLE_TAP_TOAST_COUNT, 1) > 0) {
            prefs.edit().putInt(DOUBLE_TAP_TOAST_COUNT, 0).commit();
        }
    }

    public WebImage(WebView wv, String url, Object progress, boolean zoom, boolean control, int color) {
        this.wv = wv;
        this.url = url;
        this.progress = progress;
        this.zoom = zoom;
        this.control = control;
        this.color = color;
    }

    public void load() {
        if (!this.url.equals(this.wv.getTag(Constants.TAG_URL))) {
            this.wv.setTag(Constants.TAG_URL, this.url);
            if (VERSION.SDK_INT <= 10) {
                this.wv.setDrawingCacheEnabled(true);
            }
            fixWebviewTip(this.wv.getContext());
            WebSettings ws = this.wv.getSettings();
            ws.setSupportZoom(this.zoom);
            ws.setBuiltInZoomControls(this.zoom);
            if (!this.control) {
                disableZoomControl(this.wv);
            }
            ws.setJavaScriptEnabled(true);
            this.wv.setBackgroundColor(this.color);
            if (this.progress != null) {
                Common.showProgress(this.progress, this.url, true);
            }
            if (this.wv.getWidth() > 0) {
                setup();
            } else {
                delaySetup();
            }
        }
    }

    private void delaySetup() {
        this.wv.setPictureListener(new PictureListener() {
            public void onNewPicture(WebView view, Picture picture) {
                WebImage.this.wv.setPictureListener(null);
                WebImage.this.setup();
            }
        });
        this.wv.loadData("<html></html>", "text/html", "utf-8");
        this.wv.setBackgroundColor(this.color);
    }

    /* access modifiers changed from: private */
    public void setup() {
        String html = getSource(this.wv.getContext()).replace("@src", this.url).replace("@color", Integer.toHexString(this.color));
        this.wv.setWebViewClient(this);
        this.wv.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        this.wv.setBackgroundColor(this.color);
    }

    private void done(WebView view) {
        if (this.progress != null) {
            view.setVisibility(0);
            Common.showProgress(this.progress, this.url, false);
        }
        view.setWebViewClient(null);
    }

    public void onPageFinished(WebView view, String url) {
        done(view);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        done(view);
    }

    public void onScaleChanged(WebView view, float oldScale, float newScale) {
    }

    private static void disableZoomControl(WebView wv) {
        if (VERSION.SDK_INT >= 11) {
            AQUtility.invokeHandler(wv.getSettings(), "setDisplayZoomControls", false, false, new Class[]{Boolean.TYPE}, Boolean.valueOf(false));
        }
    }
}
