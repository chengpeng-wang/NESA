package com.duapps.ad.b;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.duapps.ad.b.a.a;
import com.duapps.ad.base.f;
import com.moceanmobile.mast.MASTNativeAdConstants;

/* compiled from: InMobiDataExecutor */
final class i extends WebViewClient {
    private /* synthetic */ g a;

    i(g gVar) {
        this.a = gVar;
    }

    public final void onPageFinished(WebView webView, String str) {
        int i = 0;
        this.a.f = false;
        if (this.a.c != null) {
            this.a.c.c = false;
        }
        g.c(this.a);
        f.c("InMobiDataExecutor", "page finished:" + this.a.a.q);
        if (this.a.b != a.Impression) {
            i = 1;
        }
        f.c("InMobiDataExecutor", "AdOperationType==" + i);
        f.c("InMobiDataExecutor", " completed");
    }

    public final void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        f.c("InMobiDataExecutor", "page started:");
    }

    public final void onLoadResource(WebView webView, String str) {
        f.c("InMobiDataExecutor", "resource load:");
    }

    public final void onReceivedError(WebView webView, int i, String str, String str2) {
        f.c("InMobiDataExecutor", "received error:" + i + "\tdesc:" + str + MASTNativeAdConstants.NEWLINE + str2);
        this.a.f = false;
        if (this.a.c != null) {
            this.a.c.c = false;
        }
    }
}
