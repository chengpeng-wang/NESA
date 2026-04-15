package com.facebook.ads.internal.view;

import android.content.Context;
import android.webkit.WebView;

public class d extends WebView {
    private boolean a;

    public d(Context context) {
        super(context);
    }

    public boolean b() {
        return this.a;
    }

    public void destroy() {
        this.a = true;
        super.destroy();
    }
}
