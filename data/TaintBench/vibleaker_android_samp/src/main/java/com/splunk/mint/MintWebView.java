package com.splunk.mint;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MintWebView extends WebView {
    private MintJavascript mintJavascript;

    private void init(Context context) {
        this.mintJavascript = new MintJavascript(context, this);
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(this.mintJavascript, "mintBridge");
        setWebViewClient(new MintWebViewClient());
    }

    public MintWebView(Context context) {
        super(context);
        init(context);
    }

    public MintWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MintWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}
