package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.media.TransportMediator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.cmcm.adsdk.Const;
import com.google.ads.AdSize;
import com.google.android.gms.ads.internal.overlay.AdLauncherIntentInfoParcel;
import com.google.android.gms.ads.internal.overlay.AdOverlayInfoParcel;
import com.google.android.gms.ads.internal.overlay.zzg;
import com.google.android.gms.ads.internal.overlay.zzn;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.ads.internal.zzp;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.xbill.DNS.WKSRecord.Service;

@zzgk
public class zziq extends WebViewClient {
    private static final String[] zzIU = new String[]{Const.CONNECTION_TYPE_UNKNOWN, "HOST_LOOKUP", "UNSUPPORTED_AUTH_SCHEME", "AUTHENTICATION", "PROXY_AUTHENTICATION", "CONNECT", "IO", "TIMEOUT", "REDIRECT_LOOP", "UNSUPPORTED_SCHEME", "FAILED_SSL_HANDSHAKE", "BAD_URL", "FILE", "FILE_NOT_FOUND", "TOO_MANY_REQUESTS"};
    private static final String[] zzIV = new String[]{"NOT_YET_VALID", "EXPIRED", "ID_MISMATCH", "UNTRUSTED", "DATE_INVALID", "INVALID"};
    private zza zzCv;
    private final HashMap<String, List<zzdg>> zzIW;
    private zzg zzIX;
    /* access modifiers changed from: private */
    public zzb zzIY;
    private boolean zzIZ;
    private boolean zzJa;
    private zzn zzJb;
    private final zzfa zzJc;
    private boolean zzJd;
    private boolean zzJe;
    private boolean zzJf;
    private boolean zzJg;
    private int zzJh;
    protected final zzip zzoL;
    private final Object zzpc;
    private boolean zzqL;
    private com.google.android.gms.ads.internal.client.zza zzsn;
    private zzdd zzwH;
    private zzdk zzxh;
    private zze zzxj;
    private zzew zzxk;
    private zzdi zzxm;
    private zzfc zzzy;

    public interface zza {
        void zza(zzip zzip, boolean z);
    }

    public interface zzb {
        void zzbf();
    }

    private static class zzc implements zzg {
        private zzg zzIX;
        private zzip zzJj;

        public zzc(zzip zzip, zzg zzg) {
            this.zzJj = zzip;
            this.zzIX = zzg;
        }

        public void zzaV() {
            this.zzIX.zzaV();
            this.zzJj.zzgM();
        }

        public void zzaW() {
            this.zzIX.zzaW();
            this.zzJj.zzeD();
        }
    }

    private class zzd implements zzdg {
        private zzd() {
        }

        /* synthetic */ zzd(zziq zziq, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void zza(zzip zzip, Map<String, String> map) {
            if (map.keySet().contains("start")) {
                zziq.this.zzhe();
            } else if (map.keySet().contains("stop")) {
                zziq.this.zzhf();
            } else if (map.keySet().contains("cancel")) {
                zziq.this.zzhg();
            }
        }
    }

    public zziq(zzip zzip, boolean z) {
        this(zzip, z, new zzfa(zzip, zzip.zzgO(), new zzbq(zzip.getContext())), null);
    }

    zziq(zzip zzip, boolean z, zzfa zzfa, zzew zzew) {
        this.zzIW = new HashMap();
        this.zzpc = new Object();
        this.zzIZ = false;
        this.zzoL = zzip;
        this.zzqL = z;
        this.zzJc = zzfa;
        this.zzxk = zzew;
    }

    private void zza(Context context, String str, String str2, String str3) {
        if (((Boolean) zzby.zzva.get()).booleanValue()) {
            Bundle bundle = new Bundle();
            bundle.putString("err", str);
            bundle.putString(MASTNativeAdConstants.RESPONSE_ERROR_CODE, str2);
            bundle.putString("host", zzaI(str3));
            zzp.zzbx().zza(context, this.zzoL.zzgV().zzIz, "gmob-apps", bundle, true);
        }
    }

    private String zzaI(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Uri parse = Uri.parse(str);
        return parse.getHost() != null ? parse.getHost() : "";
    }

    private static boolean zzg(Uri uri) {
        String scheme = uri.getScheme();
        return Constants.HTTP.equalsIgnoreCase(scheme) || Constants.HTTPS.equalsIgnoreCase(scheme);
    }

    /* access modifiers changed from: private */
    public void zzhe() {
        synchronized (this.zzpc) {
            this.zzJa = true;
        }
        this.zzJh++;
        zzhh();
    }

    /* access modifiers changed from: private */
    public void zzhf() {
        this.zzJh--;
        zzhh();
    }

    /* access modifiers changed from: private */
    public void zzhg() {
        this.zzJg = true;
        zzhh();
    }

    public final void onLoadResource(WebView webView, String url) {
        com.google.android.gms.ads.internal.util.client.zzb.v("Loading resource: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            zzh(parse);
        }
    }

    /* JADX WARNING: Missing block: B:10:0x001c, code skipped:
            r2.zzJf = true;
            zzhh();
     */
    /* JADX WARNING: Missing block: B:19:?, code skipped:
            return;
     */
    public final void onPageFinished(android.webkit.WebView r3, java.lang.String r4) {
        /*
        r2 = this;
        r1 = r2.zzpc;
        monitor-enter(r1);
        r0 = r2.zzJe;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x001b;
    L_0x0007:
        r0 = "about:blank";
        r0 = r0.equals(r4);	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x001b;
    L_0x000f:
        r0 = "Blank page loaded, 1...";
        com.google.android.gms.ads.internal.util.client.zzb.v(r0);	 Catch:{ all -> 0x0023 }
        r0 = r2.zzoL;	 Catch:{ all -> 0x0023 }
        r0.zzgX();	 Catch:{ all -> 0x0023 }
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
    L_0x001a:
        return;
    L_0x001b:
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        r0 = 1;
        r2.zzJf = r0;
        r2.zzhh();
        goto L_0x001a;
    L_0x0023:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zziq.onPageFinished(android.webkit.WebView, java.lang.String):void");
    }

    public final void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        String valueOf = (errorCode >= 0 || (-errorCode) - 1 >= zzIU.length) ? String.valueOf(errorCode) : zzIU[(-errorCode) - 1];
        zza(this.zzoL.getContext(), "http_err", valueOf, failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    public final void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (error != null) {
            int primaryError = error.getPrimaryError();
            String valueOf = (primaryError < 0 || primaryError >= zzIV.length) ? String.valueOf(primaryError) : zzIV[primaryError];
            zza(this.zzoL.getContext(), "ssl_err", valueOf, zzp.zzbz().zza(error));
        }
        super.onReceivedSslError(view, handler, error);
    }

    public final void reset() {
        synchronized (this.zzpc) {
            this.zzIW.clear();
            this.zzsn = null;
            this.zzIX = null;
            this.zzCv = null;
            this.zzwH = null;
            this.zzIZ = false;
            this.zzqL = false;
            this.zzJa = false;
            this.zzxm = null;
            this.zzJb = null;
            this.zzIY = null;
            if (this.zzxk != null) {
                this.zzxk.zzn(true);
                this.zzxk = null;
            }
            this.zzJd = false;
        }
    }

    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        switch (event.getKeyCode()) {
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case Service.SU_MIT_TG /*89*/:
            case AdSize.LARGE_AD_HEIGHT /*90*/:
            case Service.MIT_DOV /*91*/:
            case TransportMediator.KEYCODE_MEDIA_PLAY /*126*/:
            case 127:
            case 128:
            case Service.PWDGEN /*129*/:
            case 130:
            case 222:
                return true;
            default:
                return false;
        }
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
        com.google.android.gms.ads.internal.util.client.zzb.v("AdWebView shouldOverrideUrlLoading: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            zzh(parse);
        } else if (this.zzIZ && webView == this.zzoL && zzg(parse)) {
            if (!this.zzJd) {
                this.zzJd = true;
                if (this.zzsn != null && ((Boolean) zzby.zzuO.get()).booleanValue()) {
                    this.zzsn.onAdClicked();
                }
            }
            return super.shouldOverrideUrlLoading(webView, url);
        } else if (this.zzoL.willNotDraw()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("AdWebView unable to handle URL: " + url);
        } else {
            Uri uri;
            try {
                zzan zzgU = this.zzoL.zzgU();
                if (zzgU != null && zzgU.zzb(parse)) {
                    parse = zzgU.zza(parse, this.zzoL.getContext());
                }
                uri = parse;
            } catch (zzao e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaE("Unable to append parameter to URL: " + url);
                uri = parse;
            }
            if (this.zzxj == null || this.zzxj.zzbe()) {
                zza(new AdLauncherIntentInfoParcel("android.intent.action.VIEW", uri.toString(), null, null, null, null, null));
            } else {
                this.zzxj.zzp(url);
            }
        }
        return true;
    }

    public void zzF(boolean z) {
        this.zzIZ = z;
    }

    public void zza(int i, int i2, boolean z) {
        this.zzJc.zze(i, i2);
        if (this.zzxk != null) {
            this.zzxk.zza(i, i2, z);
        }
    }

    public final void zza(AdLauncherIntentInfoParcel adLauncherIntentInfoParcel) {
        zzg zzg = null;
        boolean zzgW = this.zzoL.zzgW();
        com.google.android.gms.ads.internal.client.zza zza = (!zzgW || this.zzoL.zzaN().zzsH) ? this.zzsn : null;
        if (!zzgW) {
            zzg = this.zzIX;
        }
        zza(new AdOverlayInfoParcel(adLauncherIntentInfoParcel, zza, zzg, this.zzJb, this.zzoL.zzgV()));
    }

    public void zza(AdOverlayInfoParcel adOverlayInfoParcel) {
        boolean z = false;
        boolean zzdY = this.zzxk != null ? this.zzxk.zzdY() : false;
        com.google.android.gms.ads.internal.overlay.zze zzbv = zzp.zzbv();
        Context context = this.zzoL.getContext();
        if (!zzdY) {
            z = true;
        }
        zzbv.zza(context, adOverlayInfoParcel, z);
    }

    public void zza(zza zza) {
        this.zzCv = zza;
    }

    public void zza(zzb zzb) {
        this.zzIY = zzb;
    }

    public final void zza(String str, zzdg zzdg) {
        synchronized (this.zzpc) {
            List list = (List) this.zzIW.get(str);
            if (list == null) {
                list = new CopyOnWriteArrayList();
                this.zzIW.put(str, list);
            }
            list.add(zzdg);
        }
    }

    public final void zza(boolean z, int i) {
        com.google.android.gms.ads.internal.client.zza zza = (!this.zzoL.zzgW() || this.zzoL.zzaN().zzsH) ? this.zzsn : null;
        zza(new AdOverlayInfoParcel(zza, this.zzIX, this.zzJb, this.zzoL, z, i, this.zzoL.zzgV()));
    }

    public final void zza(boolean z, int i, String str) {
        zzg zzg = null;
        boolean zzgW = this.zzoL.zzgW();
        com.google.android.gms.ads.internal.client.zza zza = (!zzgW || this.zzoL.zzaN().zzsH) ? this.zzsn : null;
        if (!zzgW) {
            zzg = new zzc(this.zzoL, this.zzIX);
        }
        zza(new AdOverlayInfoParcel(zza, zzg, this.zzwH, this.zzJb, this.zzoL, z, i, str, this.zzoL.zzgV(), this.zzxm));
    }

    public final void zza(boolean z, int i, String str, String str2) {
        boolean zzgW = this.zzoL.zzgW();
        com.google.android.gms.ads.internal.client.zza zza = (!zzgW || this.zzoL.zzaN().zzsH) ? this.zzsn : null;
        zza(new AdOverlayInfoParcel(zza, zzgW ? null : new zzc(this.zzoL, this.zzIX), this.zzwH, this.zzJb, this.zzoL, z, i, str, str2, this.zzoL.zzgV(), this.zzxm));
    }

    public void zzb(com.google.android.gms.ads.internal.client.zza zza, zzg zzg, zzdd zzdd, zzn zzn, boolean z, zzdi zzdi, zzdk zzdk, zze zze, zzfc zzfc) {
        if (zze == null) {
            zze = new zze(false);
        }
        this.zzxk = new zzew(this.zzoL, zzfc);
        zza("/appEvent", new zzdc(zzdd));
        zza("/backButton", zzdf.zzwR);
        zza("/canOpenURLs", zzdf.zzwJ);
        zza("/canOpenIntents", zzdf.zzwK);
        zza("/click", zzdf.zzwL);
        zza("/close", zzdf.zzwM);
        zza("/customClose", zzdf.zzwN);
        zza("/delayPageLoaded", new zzd(this, null));
        zza("/httpTrack", zzdf.zzwO);
        zza("/log", zzdf.zzwP);
        zza("/mraid", new zzdm(zze, this.zzxk));
        zza("/mraidLoaded", this.zzJc);
        zza("/open", new zzdn(zzdi, zze, this.zzxk));
        zza("/precache", zzdf.zzwT);
        zza("/touch", zzdf.zzwQ);
        zza("/video", zzdf.zzwS);
        if (zzdk != null) {
            zza("/setInterstitialProperties", new zzdj(zzdk));
        }
        this.zzsn = zza;
        this.zzIX = zzg;
        this.zzwH = zzdd;
        this.zzxm = zzdi;
        this.zzJb = zzn;
        this.zzxj = zze;
        this.zzzy = zzfc;
        this.zzxh = zzdk;
        zzF(z);
        this.zzJd = false;
    }

    public final void zzb(String str, zzdg zzdg) {
        synchronized (this.zzpc) {
            List list = (List) this.zzIW.get(str);
            if (list == null) {
                return;
            }
            list.remove(zzdg);
        }
    }

    public boolean zzbY() {
        boolean z;
        synchronized (this.zzpc) {
            z = this.zzqL;
        }
        return z;
    }

    public void zzd(int i, int i2) {
        if (this.zzxk != null) {
            this.zzxk.zzd(i, i2);
        }
    }

    public final void zzeA() {
        synchronized (this.zzpc) {
            this.zzIZ = false;
            this.zzqL = true;
            zzhu.runOnUiThread(new Runnable() {
                public void run() {
                    zziq.this.zzoL.zzgZ();
                    com.google.android.gms.ads.internal.overlay.zzd zzgQ = zziq.this.zzoL.zzgQ();
                    if (zzgQ != null) {
                        zzgQ.zzeA();
                    }
                    if (zziq.this.zzIY != null) {
                        zziq.this.zzIY.zzbf();
                        zziq.this.zzIY = null;
                    }
                }
            });
        }
    }

    public void zzh(Uri uri) {
        String path = uri.getPath();
        List<zzdg> list = (List) this.zzIW.get(path);
        if (list != null) {
            Map zze = zzp.zzbx().zze(uri);
            if (com.google.android.gms.ads.internal.util.client.zzb.zzM(2)) {
                com.google.android.gms.ads.internal.util.client.zzb.v("Received GMSG: " + path);
                for (String path2 : zze.keySet()) {
                    com.google.android.gms.ads.internal.util.client.zzb.v("  " + path2 + ": " + ((String) zze.get(path2)));
                }
            }
            for (zzdg zza : list) {
                zza.zza(this.zzoL, zze);
            }
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.v("No GMSG handler found for GMSG: " + uri);
    }

    public zze zzhb() {
        return this.zzxj;
    }

    public boolean zzhc() {
        boolean z;
        synchronized (this.zzpc) {
            z = this.zzJa;
        }
        return z;
    }

    public void zzhd() {
        synchronized (this.zzpc) {
            com.google.android.gms.ads.internal.util.client.zzb.v("Loading blank page in WebView, 2...");
            this.zzJe = true;
            this.zzoL.zzaF("about:blank");
        }
    }

    public final void zzhh() {
        if (this.zzCv == null) {
            return;
        }
        if ((this.zzJf && this.zzJh <= 0) || this.zzJg) {
            this.zzCv.zza(this.zzoL, !this.zzJg);
            this.zzCv = null;
        }
    }
}
