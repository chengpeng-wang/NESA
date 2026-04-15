package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.moceanmobile.mast.Defaults;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzgk
public class zziv extends zziq {
    public zziv(zzip zzip, boolean z) {
        super(zzip, z);
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        try {
            if (!"mraid.js".equalsIgnoreCase(new File(url).getName())) {
                return super.shouldInterceptRequest(webView, url);
            }
            if (webView instanceof zzip) {
                zzip zzip = (zzip) webView;
                zzip.zzgS().zzeA();
                String str = zzip.zzaN().zzsH ? (String) zzby.zzuA.get() : zzip.zzgW() ? (String) zzby.zzuz.get() : (String) zzby.zzuy.get();
                zzb.v("shouldInterceptRequest(" + str + ")");
                return zze(zzip.getContext(), this.zzoL.zzgV().zzIz, str);
            }
            zzb.zzaE("Tried to intercept request from a WebView that wasn't an AdWebView.");
            return super.shouldInterceptRequest(webView, url);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            zzb.zzaE("Could not fetch MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        }
    }

    /* access modifiers changed from: protected */
    public WebResourceResponse zze(Context context, String str, String str2) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        HashMap hashMap = new HashMap();
        hashMap.put("User-Agent", zzp.zzbx().zzf(context, str));
        hashMap.put("Cache-Control", "max-stale=3600");
        String str3 = (String) new zzhy(context).zzb(str2, hashMap).get(60, TimeUnit.SECONDS);
        return str3 == null ? null : new WebResourceResponse("application/javascript", Defaults.ENCODING_UTF_8, new ByteArrayInputStream(str3.getBytes(Defaults.ENCODING_UTF_8)));
    }
}
