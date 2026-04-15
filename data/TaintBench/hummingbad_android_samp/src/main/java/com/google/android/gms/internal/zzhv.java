package com.google.android.gms.internal;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@zzgk
public class zzhv {

    static class zza extends zzhv {
        zza() {
            super();
        }

        public boolean zza(Request request) {
            request.setShowRunningNotification(true);
            return true;
        }

        public int zzgv() {
            return 6;
        }

        public int zzgw() {
            return 7;
        }
    }

    static class zzb extends zza {
        zzb() {
        }

        public boolean zza(Request request) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(1);
            return true;
        }

        public boolean zza(Context context, WebSettings webSettings) {
            if (context.getCacheDir() != null) {
                webSettings.setAppCachePath(context.getCacheDir().getAbsolutePath());
                webSettings.setAppCacheMaxSize(0);
                webSettings.setAppCacheEnabled(true);
            }
            webSettings.setDatabasePath(context.getDatabasePath("com.google.android.gms.ads.db").getAbsolutePath());
            webSettings.setDatabaseEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            return true;
        }

        public boolean zza(Window window) {
            window.setFlags(ViewCompat.MEASURED_STATE_TOO_SMALL, ViewCompat.MEASURED_STATE_TOO_SMALL);
            return true;
        }

        public boolean zza(WebView webView) {
            webView.onPause();
            return true;
        }

        public zziq zzb(zzip zzip, boolean z) {
            return new zziv(zzip, z);
        }

        public boolean zzb(WebView webView) {
            webView.onResume();
            return true;
        }

        public WebChromeClient zzf(zzip zzip) {
            return new zziu(zzip);
        }

        public Set<String> zzf(Uri uri) {
            return uri.getQueryParameterNames();
        }

        public boolean zzk(View view) {
            view.setLayerType(0, null);
            return true;
        }

        public boolean zzl(View view) {
            view.setLayerType(1, null);
            return true;
        }
    }

    static class zzc extends zzb {
        zzc() {
        }

        public String zza(SslError sslError) {
            return sslError.getUrl();
        }

        public WebChromeClient zzf(zzip zzip) {
            return new zziw(zzip);
        }
    }

    static class zzf extends zzc {
        zzf() {
        }

        public void zza(View view, Drawable drawable) {
            view.setBackground(drawable);
        }

        public void zza(ViewTreeObserver viewTreeObserver, OnGlobalLayoutListener onGlobalLayoutListener) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }

        public void zzb(Activity activity, OnGlobalLayoutListener onGlobalLayoutListener) {
            Window window = activity.getWindow();
            if (window != null && window.getDecorView() != null && window.getDecorView().getViewTreeObserver() != null) {
                zza(window.getDecorView().getViewTreeObserver(), onGlobalLayoutListener);
            }
        }
    }

    static class zzd extends zzf {
        zzd() {
        }

        public String getDefaultUserAgent(Context context) {
            return WebSettings.getDefaultUserAgent(context);
        }

        public boolean zza(Context context, WebSettings webSettings) {
            super.zza(context, webSettings);
            webSettings.setMediaPlaybackRequiresUserGesture(false);
            return true;
        }
    }

    static class zze extends zzd {
        zze() {
        }

        public boolean isAttachedToWindow(View view) {
            return zzhv.super.isAttachedToWindow(view) || view.getWindowId() != null;
        }

        public int zzgx() {
            return 14;
        }
    }

    static class zzg extends zze {
        zzg() {
        }

        public boolean isAttachedToWindow(View view) {
            return view.isAttachedToWindow();
        }

        public LayoutParams zzgy() {
            return new LayoutParams(-1, -1);
        }
    }

    private zzhv() {
    }

    public static zzhv zzL(int i) {
        return i >= 19 ? new zzg() : i >= 18 ? new zze() : i >= 17 ? new zzd() : i >= 16 ? new zzf() : i >= 14 ? new zzc() : i >= 11 ? new zzb() : i >= 9 ? new zza() : new zzhv();
    }

    public String getDefaultUserAgent(Context context) {
        return "";
    }

    public boolean isAttachedToWindow(View view) {
        return (view.getWindowToken() == null && view.getWindowVisibility() == 8) ? false : true;
    }

    public String zza(SslError sslError) {
        return "";
    }

    public void zza(View view, Drawable drawable) {
        view.setBackgroundDrawable(drawable);
    }

    public void zza(ViewTreeObserver viewTreeObserver, OnGlobalLayoutListener onGlobalLayoutListener) {
        viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
    }

    public boolean zza(Request request) {
        return false;
    }

    public boolean zza(Context context, WebSettings webSettings) {
        return false;
    }

    public boolean zza(Window window) {
        return false;
    }

    public boolean zza(WebView webView) {
        return false;
    }

    public zziq zzb(zzip zzip, boolean z) {
        return new zziq(zzip, z);
    }

    public void zzb(Activity activity, OnGlobalLayoutListener onGlobalLayoutListener) {
        Window window = activity.getWindow();
        if (window != null && window.getDecorView() != null && window.getDecorView().getViewTreeObserver() != null) {
            zza(window.getDecorView().getViewTreeObserver(), onGlobalLayoutListener);
        }
    }

    public boolean zzb(WebView webView) {
        return false;
    }

    public WebChromeClient zzf(zzip zzip) {
        return null;
    }

    public Set<String> zzf(Uri uri) {
        if (uri.isOpaque()) {
            return Collections.emptySet();
        }
        String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery == null) {
            return Collections.emptySet();
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int i = 0;
        do {
            int indexOf = encodedQuery.indexOf(38, i);
            if (indexOf == -1) {
                indexOf = encodedQuery.length();
            }
            int indexOf2 = encodedQuery.indexOf(61, i);
            if (indexOf2 > indexOf || indexOf2 == -1) {
                indexOf2 = indexOf;
            }
            linkedHashSet.add(Uri.decode(encodedQuery.substring(i, indexOf2)));
            i = indexOf + 1;
        } while (i < encodedQuery.length());
        return Collections.unmodifiableSet(linkedHashSet);
    }

    public int zzgv() {
        return 0;
    }

    public int zzgw() {
        return 1;
    }

    public int zzgx() {
        return 5;
    }

    public LayoutParams zzgy() {
        return new LayoutParams(-2, -2);
    }

    public boolean zzk(View view) {
        return false;
    }

    public boolean zzl(View view) {
        return false;
    }
}
