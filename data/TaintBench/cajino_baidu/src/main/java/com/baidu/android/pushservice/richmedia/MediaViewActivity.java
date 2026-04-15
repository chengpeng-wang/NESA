package com.baidu.android.pushservice.richmedia;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.baidu.android.pushservice.b;

public class MediaViewActivity extends Activity {
    public WebView a;
    private RelativeLayout b;
    private WebChromeClient c = new j(this);
    private WebViewClient d = new k(this);

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        getWindow().requestFeature(1);
        LayoutParams layoutParams = new LayoutParams(-1, -1, 0.0f);
        this.b = new RelativeLayout(this);
        this.b.setLayoutParams(layoutParams);
        this.b.setGravity(1);
        this.a = new WebView(this);
        this.a.requestFocusFromTouch();
        this.a.setLongClickable(true);
        WebSettings settings = this.a.getSettings();
        settings.setCacheMode(1);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLightTouchEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        this.a.setWebChromeClient(this.c);
        this.a.setWebViewClient(this.d);
        if (b.a()) {
            Log.d("MediaViewActivity", "uri=" + intent.getData().toString());
        }
        this.a.loadUrl(intent.getData().toString());
        this.b.addView(this.a);
        setContentView(this.b);
        if (this.b == null || this.a == null) {
            Log.e("MediaViewActivity", "Set up Layout error.");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (b.a()) {
            Log.d("MediaViewActivity", "uri=" + intent.getData().toString());
        }
        this.a.loadUrl(intent.getData().toString());
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }
}
