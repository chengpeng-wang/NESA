package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.http.HttpHeaders;

@TargetApi(11)
@zzhb
public class zzjw extends zzjq {
    public zzjw(zzjp zzjp, boolean z) {
        super(zzjp, z);
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        try {
            if (!"mraid.js".equalsIgnoreCase(new File(url).getName())) {
                return super.shouldInterceptRequest(webView, url);
            }
            if (webView instanceof zzjp) {
                zzjp zzjp = (zzjp) webView;
                zzjp.zzhU().zzfo();
                String str = zzjp.zzaN().zzui ? (String) zzbt.zzwf.get() : zzjp.zzhY() ? (String) zzbt.zzwe.get() : (String) zzbt.zzwd.get();
                zzin.v("shouldInterceptRequest(" + str + ")");
                return zzd(zzjp.getContext(), this.zzpD.zzhX().afmaVersion, str);
            }
            zzb.zzaK("Tried to intercept request from a WebView that wasn't an AdWebView.");
            return super.shouldInterceptRequest(webView, url);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            zzb.zzaK("Could not fetch MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        }
    }

    /* access modifiers changed from: protected */
    public WebResourceResponse zzd(Context context, String str, String str2) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        HashMap hashMap = new HashMap();
        hashMap.put(HttpHeaders.USER_AGENT, zzr.zzbC().zze(context, str));
        hashMap.put(HttpHeaders.CACHE_CONTROL, "max-stale=3600");
        String str3 = (String) new zziw(context).zzb(str2, hashMap).get(60, TimeUnit.SECONDS);
        return str3 == null ? null : new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(str3.getBytes("UTF-8")));
    }
}
