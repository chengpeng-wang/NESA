package com.picksbrowser;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import com.moceanmobile.mast.Defaults;
import com.picksbrowser.utils.b;

/* compiled from: BaseWebView */
public final class a extends WebView {
    private static boolean b = false;
    protected boolean a;

    public a(Context context) {
        int i;
        super(context.getApplicationContext());
        if (b.a().b() >= b.JELLY_BEAN_MR2.b()) {
            i = true;
        } else {
            i = 0;
        }
        if (i == 0) {
            getSettings().setPluginState(PluginState.OFF);
        }
        setWebChromeClient(new j());
        if (!b) {
            Context context2 = getContext();
            if (VERSION.SDK_INT == 19) {
                WebView webView = new WebView(context2.getApplicationContext());
                webView.setBackgroundColor(0);
                webView.loadDataWithBaseURL(null, "", "text/html", Defaults.ENCODING_UTF_8, null);
                LayoutParams layoutParams = new LayoutParams();
                layoutParams.width = 1;
                layoutParams.height = 1;
                layoutParams.type = 2005;
                layoutParams.flags = 16777240;
                layoutParams.format = -2;
                layoutParams.gravity = 8388659;
                ((WindowManager) context2.getSystemService("window")).addView(webView, layoutParams);
            }
            b = true;
        }
    }

    public final void destroy() {
        this.a = true;
        if (!(this == null || getParent() == null || !(getParent() instanceof ViewGroup))) {
            ((ViewGroup) getParent()).removeView(this);
        }
        removeAllViews();
        super.destroy();
    }
}
