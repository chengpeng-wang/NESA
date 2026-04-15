package com.picksbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.mopub.common.MoPubBrowser;
import com.picksbrowser.utils.a;

public class PicksBrowser extends Activity {
    private static final int a = 1;
    private static Object b;
    private static String c;
    private static String i;
    /* access modifiers changed from: private */
    public WebView d;
    private ImageButton e;
    private ImageButton f;
    private ImageButton g;
    private ImageButton h;

    @NonNull
    public final ImageButton a() {
        return this.e;
    }

    @NonNull
    public final ImageButton b() {
        return this.f;
    }

    @NonNull
    public final WebView c() {
        return this.d;
    }

    public static void a(Context context, String str) {
        Intent intent = new Intent(context, PicksBrowser.class);
        intent.putExtra(MoPubBrowser.DESTINATION_URL_KEY, str);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public static void a(Context context, String str, Object obj, String str2) {
        b = obj;
        i = str2;
        Intent intent = new Intent(context, PicksBrowser.class);
        intent.putExtra(MoPubBrowser.DESTINATION_URL_KEY, str);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(-1);
        getWindow().requestFeature(2);
        getWindow().setFeatureInt(2, -1);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout.setOrientation(1);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout.addView(relativeLayout);
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setId(a);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(12);
        linearLayout2.setLayoutParams(layoutParams);
        linearLayout2.setBackgroundDrawable(a.BACKGROUND.a(this));
        relativeLayout.addView(linearLayout2);
        this.e = a(a.LEFT_ARROW.a(this));
        this.f = a(a.RIGHT_ARROW.a(this));
        this.g = a(a.REFRESH.a(this));
        this.h = a(a.CLOSE.a(this));
        linearLayout2.addView(this.e);
        linearLayout2.addView(this.f);
        linearLayout2.addView(this.g);
        linearLayout2.addView(this.h);
        this.d = new a(this);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.addRule(2, a);
        this.d.setLayoutParams(layoutParams2);
        relativeLayout.addView(this.d);
        setContentView(linearLayout);
        WebSettings settings = this.d.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        if (!(b == null || TextUtils.isEmpty(i))) {
            this.d.addJavascriptInterface(b, i);
        }
        this.d.loadUrl(getIntent().getStringExtra(MoPubBrowser.DESTINATION_URL_KEY));
        if (!TextUtils.isEmpty(c)) {
            this.d.loadUrl(c);
        }
        this.d.setWebViewClient(new b(this));
        this.d.setWebChromeClient(new c(this));
        this.e.setBackgroundColor(0);
        this.e.setOnClickListener(new d(this));
        this.f.setBackgroundColor(0);
        this.f.setOnClickListener(new e(this));
        this.g.setBackgroundColor(0);
        this.g.setOnClickListener(new f(this));
        this.h.setBackgroundColor(0);
        this.h.setOnClickListener(new g(this));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
        WebView webView = this.d;
        if (isFinishing()) {
            webView.stopLoading();
            webView.loadUrl("");
        }
        if (VERSION.SDK_INT >= 11) {
            webView.onPause();
            return;
        }
        try {
            new i(webView, "onPause").a().b();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
        WebView webView = this.d;
        if (VERSION.SDK_INT >= 11) {
            webView.onResume();
            return;
        }
        try {
            new i(webView, "onResume").a().b();
        } catch (Exception e) {
        }
    }

    public void finish() {
        ((ViewGroup) getWindow().getDecorView()).removeAllViews();
        super.finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.d.destroy();
        this.d = null;
    }

    private ImageButton a(Drawable drawable) {
        ImageButton imageButton = new ImageButton(this);
        LayoutParams layoutParams = new LayoutParams(-2, -2, 1.0f);
        layoutParams.gravity = 16;
        imageButton.setLayoutParams(layoutParams);
        imageButton.setImageDrawable(drawable);
        return imageButton;
    }
}
