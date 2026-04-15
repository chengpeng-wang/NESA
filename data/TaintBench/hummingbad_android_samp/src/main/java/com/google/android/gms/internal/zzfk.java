package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.moceanmobile.mast.Defaults;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@zzgk
public class zzfk implements zzfi {
    private final Context mContext;
    final Set<WebView> zzBE = Collections.synchronizedSet(new HashSet());

    public zzfk(Context context) {
        this.mContext = context;
    }

    public void zza(String str, final String str2, final String str3) {
        zzb.zzaC("Fetching assets for the given html");
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                final WebView zzfb = zzfk.this.zzfb();
                zzfb.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        zzb.zzaC("Loading assets have finished");
                        zzfk.this.zzBE.remove(zzfb);
                    }

                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        zzb.zzaE("Loading assets have failed.");
                        zzfk.this.zzBE.remove(zzfb);
                    }
                });
                zzfk.this.zzBE.add(zzfb);
                zzfb.loadDataWithBaseURL(str2, str3, "text/html", Defaults.ENCODING_UTF_8, null);
                zzb.zzaC("Fetching assets finished.");
            }
        });
    }

    public WebView zzfb() {
        WebView webView = new WebView(this.mContext);
        webView.getSettings().setJavaScriptEnabled(true);
        return webView;
    }
}
