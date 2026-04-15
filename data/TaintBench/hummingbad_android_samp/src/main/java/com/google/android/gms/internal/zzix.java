package com.google.android.gms.internal;

import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzw;
import java.net.URI;
import java.net.URISyntaxException;

@zzgk
public class zzix extends WebViewClient {
    private final String zzJB;
    private boolean zzJC = false;
    private final zzfx zzJD;
    private final zzip zzoL;

    public zzix(zzfx zzfx, zzip zzip, String str) {
        this.zzJB = zzaM(str);
        this.zzoL = zzip;
        this.zzJD = zzfx;
    }

    private String zzaM(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return str.endsWith("/") ? str.substring(0, str.length() - 1) : str;
        } catch (IndexOutOfBoundsException e) {
            zzb.e(e.getMessage());
            return str;
        }
    }

    public void onLoadResource(WebView view, String url) {
        zzb.zzaC("JavascriptAdWebViewClient::onLoadResource: " + url);
        if (!zzaL(url)) {
            this.zzoL.zzgS().onLoadResource(this.zzoL.getWebView(), url);
        }
    }

    public void onPageFinished(WebView view, String url) {
        zzb.zzaC("JavascriptAdWebViewClient::onPageFinished: " + url);
        if (!this.zzJC) {
            this.zzJD.zzfl();
            this.zzJC = true;
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        zzb.zzaC("JavascriptAdWebViewClient::shouldOverrideUrlLoading: " + url);
        if (!zzaL(url)) {
            return this.zzoL.zzgS().shouldOverrideUrlLoading(this.zzoL.getWebView(), url);
        }
        zzb.zzaC("shouldOverrideUrlLoading: received passback url");
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean zzaL(String str) {
        String zzaM = zzaM(str);
        if (TextUtils.isEmpty(zzaM)) {
            return false;
        }
        try {
            URI uri = new URI(zzaM);
            if ("passback".equals(uri.getScheme())) {
                zzb.zzaC("Passback received");
                this.zzJD.zzfm();
                return true;
            } else if (TextUtils.isEmpty(this.zzJB)) {
                return false;
            } else {
                URI uri2 = new URI(this.zzJB);
                String host = uri2.getHost();
                String host2 = uri.getHost();
                zzaM = uri2.getPath();
                String path = uri.getPath();
                if (!zzw.equal(host, host2) || !zzw.equal(zzaM, path)) {
                    return false;
                }
                zzb.zzaC("Passback received");
                this.zzJD.zzfm();
                return true;
            }
        } catch (URISyntaxException e) {
            zzb.e(e.getMessage());
            return false;
        }
    }
}
