package com.androidquery;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.androidquery.util.AQUtility;

public class WebDialog extends Dialog {
    /* access modifiers changed from: private */
    public WebViewClient client;
    private LinearLayout ll;
    private String message;
    private String url;
    private WebView wv;

    private class DialogWebViewClient extends WebViewClient {
        private DialogWebViewClient() {
        }

        /* synthetic */ DialogWebViewClient(WebDialog webDialog, DialogWebViewClient dialogWebViewClient) {
            this();
        }

        public void onPageFinished(WebView view, String url) {
            WebDialog.this.showProgress(false);
            WebDialog.this.client.onPageFinished(view, url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebDialog.this.client.onPageStarted(view, url, favicon);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            WebDialog.this.client.onReceivedError(view, errorCode, description, failingUrl);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return WebDialog.this.client.shouldOverrideUrlLoading(view, url);
        }
    }

    public WebDialog(Context context, String url, WebViewClient client) {
        super(context, 16973830);
        this.url = url;
        this.client = client;
    }

    public void setLoadingMessage(String message) {
        this.message = message;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setBackgroundColor(-1);
        setupWebView(layout);
        setupProgress(layout);
        addContentView(layout, new LayoutParams(-1, -1));
    }

    private void setupProgress(RelativeLayout layout) {
        Context context = getContext();
        this.ll = new LinearLayout(context);
        ProgressBar progress = new ProgressBar(context);
        int p = AQUtility.dip2pixel(context, 30.0f);
        this.ll.addView(progress, new LinearLayout.LayoutParams(p, p));
        if (this.message != null) {
            TextView tv = new TextView(context);
            LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(-2, -2);
            tlp.leftMargin = AQUtility.dip2pixel(context, 5.0f);
            tlp.gravity = 16;
            tv.setText(this.message);
            this.ll.addView(tv, tlp);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
        lp.addRule(13);
        layout.addView(this.ll, lp);
    }

    private void setupWebView(RelativeLayout layout) {
        this.wv = new WebView(getContext());
        this.wv.setVerticalScrollBarEnabled(false);
        this.wv.setHorizontalScrollBarEnabled(false);
        if (this.client == null) {
            this.client = new WebViewClient();
        }
        this.wv.setWebViewClient(new DialogWebViewClient(this, null));
        this.wv.getSettings().setJavaScriptEnabled(true);
        layout.addView(this.wv, new RelativeLayout.LayoutParams(-1, -1));
    }

    public void load() {
        if (this.wv != null) {
            this.wv.loadUrl(this.url);
        }
    }

    /* access modifiers changed from: private */
    public void showProgress(boolean show) {
        if (this.ll == null) {
            return;
        }
        if (show) {
            this.ll.setVisibility(0);
        } else {
            this.ll.setVisibility(8);
        }
    }

    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }
}
