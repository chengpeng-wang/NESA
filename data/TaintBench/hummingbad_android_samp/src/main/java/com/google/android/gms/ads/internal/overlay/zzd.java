package com.google.android.gms.ads.internal.overlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzcc;
import com.google.android.gms.internal.zzcd;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhw;
import com.google.android.gms.internal.zzip;
import com.moceanmobile.mast.Defaults;
import com.mopub.common.AdType;
import java.util.Collections;

@zzgk
public class zzd extends com.google.android.gms.internal.zzfe.zza implements zzo {
    static final int zzAu = Color.argb(0, 0, 0, 0);
    private final Activity mActivity;
    zzm zzAA;
    boolean zzAB = false;
    FrameLayout zzAC;
    CustomViewCallback zzAD;
    boolean zzAE = false;
    boolean zzAF = false;
    boolean zzAG = false;
    int zzAH = 0;
    private boolean zzAI;
    private boolean zzAJ = false;
    private boolean zzAK = true;
    private final zzcd zzAv;
    private final zzcc zzAw;
    AdOverlayInfoParcel zzAx;
    zzk zzAy;
    zzc zzAz;
    zzip zzoL;
    RelativeLayout zzzA;

    @zzgk
    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    @zzgk
    static final class zzb extends RelativeLayout {
        zzhw zzqF;

        public zzb(Context context, String str) {
            super(context);
            this.zzqF = new zzhw(context, str);
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            this.zzqF.zze(event);
            return false;
        }
    }

    @zzgk
    public static class zzc {
        public final Context context;
        public final int index;
        public final LayoutParams zzAM;
        public final ViewGroup zzAN;

        public zzc(zzip zzip) throws zza {
            this.zzAM = zzip.getLayoutParams();
            ViewParent parent = zzip.getParent();
            this.context = zzip.zzgO();
            if (parent == null || !(parent instanceof ViewGroup)) {
                throw new zza("Could not get the parent of the WebView for an overlay.");
            }
            this.zzAN = (ViewGroup) parent;
            this.index = this.zzAN.indexOfChild(zzip.getWebView());
            this.zzAN.removeView(zzip.getWebView());
            zzip.zzC(true);
        }
    }

    public zzd(Activity activity) {
        this.mActivity = activity;
        this.zzAv = new zzcd(((Boolean) zzby.zzuB.get()).booleanValue(), "show_interstitial", AdType.INTERSTITIAL);
        this.zzAw = this.zzAv.zzdl();
    }

    public void close() {
        this.zzAH = 2;
        this.mActivity.finish();
    }

    public void onBackPressed() {
        this.zzAH = 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        boolean z = false;
        if (savedInstanceState != null) {
            z = savedInstanceState.getBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", false);
        }
        this.zzAE = z;
        try {
            this.zzAx = AdOverlayInfoParcel.zzb(this.mActivity.getIntent());
            if (this.zzAx == null) {
                throw new zza("Could not get info for ad overlay.");
            }
            if (this.zzAx.zzqb.zzIB > 7500000) {
                this.zzAH = 3;
            }
            if (this.mActivity.getIntent() != null) {
                this.zzAK = this.mActivity.getIntent().getBooleanExtra("shouldCallOnOverlayOpened", true);
            }
            if (this.zzAx.zzBa != null) {
                this.zzAF = this.zzAx.zzBa.zzpk;
            } else {
                this.zzAF = false;
            }
            if (savedInstanceState == null) {
                if (this.zzAx.zzAQ != null && this.zzAK) {
                    this.zzAx.zzAQ.zzaW();
                }
                if (!(this.zzAx.zzAX == 1 || this.zzAx.zzAP == null)) {
                    this.zzAx.zzAP.onAdClicked();
                }
            }
            this.zzzA = new zzb(this.mActivity, this.zzAx.zzAZ);
            switch (this.zzAx.zzAX) {
                case 1:
                    zzv(false);
                    return;
                case 2:
                    this.zzAz = new zzc(this.zzAx.zzAR);
                    zzv(false);
                    return;
                case 3:
                    zzv(true);
                    return;
                case 4:
                    if (this.zzAE) {
                        this.zzAH = 3;
                        this.mActivity.finish();
                        return;
                    } else if (!zzp.zzbu().zza(this.mActivity, this.zzAx.zzAO, this.zzAx.zzAW)) {
                        this.zzAH = 3;
                        this.mActivity.finish();
                        return;
                    } else {
                        return;
                    }
                default:
                    throw new zza("Could not determine ad overlay type.");
            }
        } catch (zza e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaE(e.getMessage());
            this.zzAH = 3;
            this.mActivity.finish();
        }
    }

    public void onDestroy() {
        if (this.zzAy != null) {
            this.zzAy.destroy();
        }
        if (this.zzoL != null) {
            this.zzzA.removeView(this.zzoL.getWebView());
        }
        zzeB();
    }

    public void onPause() {
        if (this.zzAy != null) {
            this.zzAy.pause();
        }
        zzex();
        if (this.zzoL != null && (!this.mActivity.isFinishing() || this.zzAz == null)) {
            zzp.zzbz().zza(this.zzoL.getWebView());
        }
        zzeB();
    }

    public void onRestart() {
    }

    public void onResume() {
        if (this.zzAx != null && this.zzAx.zzAX == 4) {
            if (this.zzAE) {
                this.zzAH = 3;
                this.mActivity.finish();
            } else {
                this.zzAE = true;
            }
        }
        if (this.zzoL == null || this.zzoL.isDestroyed()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("The webview does not exit. Ignoring action.");
        } else {
            zzp.zzbz().zzb(this.zzoL.getWebView());
        }
    }

    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", this.zzAE);
    }

    public void onStart() {
    }

    public void onStop() {
        zzeB();
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.mActivity.setRequestedOrientation(requestedOrientation);
    }

    public void zza(int i, int i2, int i3, int i4, int i5) {
        this.zzAv.zza(this.zzAw, "vpr");
        zzcc zzdl = this.zzAv.zzdl();
        if (this.zzAy == null) {
            this.zzAy = new zzk(this.mActivity, this.zzoL, i5, this.zzAv, zzdl);
            this.zzzA.addView(this.zzAy, 0, new LayoutParams(-1, -1));
            this.zzAy.zze(i, i2, i3, i4);
            this.zzoL.zzgS().zzF(false);
        }
    }

    public void zza(View view, CustomViewCallback customViewCallback) {
        this.zzAC = new FrameLayout(this.mActivity);
        this.zzAC.setBackgroundColor(-16777216);
        this.zzAC.addView(view, -1, -1);
        this.mActivity.setContentView(this.zzAC);
        zzaE();
        this.zzAD = customViewCallback;
        this.zzAB = true;
    }

    public void zza(boolean z, boolean z2) {
        if (this.zzAA != null) {
            this.zzAA.zza(z, z2);
        }
    }

    public void zzaE() {
        this.zzAI = true;
    }

    public void zzd(int i, int i2, int i3, int i4) {
        if (this.zzAy != null) {
            this.zzAy.zze(i, i2, i3, i4);
        }
    }

    public void zzeA() {
        this.zzzA.removeView(this.zzAA);
        zzu(true);
    }

    /* access modifiers changed from: protected */
    public void zzeB() {
        if (this.mActivity.isFinishing() && !this.zzAJ) {
            this.zzAJ = true;
            if (zzp.zzbA().zzgc() != null) {
                zzp.zzbA().zzgc().zza(this.zzAv);
            }
            if (this.zzoL != null) {
                zzv(this.zzAH);
                this.zzzA.removeView(this.zzoL.getWebView());
                if (this.zzAz != null) {
                    this.zzoL.setContext(this.zzAz.context);
                    this.zzoL.zzC(false);
                    this.zzAz.zzAN.addView(this.zzoL.getWebView(), this.zzAz.index, this.zzAz.zzAM);
                    this.zzAz = null;
                }
                this.zzoL = null;
            }
            if (this.zzAx != null && this.zzAx.zzAQ != null) {
                this.zzAx.zzAQ.zzaV();
            }
        }
    }

    public void zzeC() {
        if (this.zzAG) {
            this.zzAG = false;
            zzeD();
        }
    }

    /* access modifiers changed from: protected */
    public void zzeD() {
        this.zzoL.zzeD();
    }

    public zzk zzew() {
        return this.zzAy;
    }

    public void zzex() {
        if (this.zzAx != null && this.zzAB) {
            setRequestedOrientation(this.zzAx.orientation);
        }
        if (this.zzAC != null) {
            this.mActivity.setContentView(this.zzzA);
            zzaE();
            this.zzAC.removeAllViews();
            this.zzAC = null;
        }
        if (this.zzAD != null) {
            this.zzAD.onCustomViewHidden();
            this.zzAD = null;
        }
        this.zzAB = false;
    }

    public void zzey() {
        this.zzAH = 1;
        this.mActivity.finish();
    }

    public boolean zzez() {
        this.zzAH = 0;
        if (this.zzoL == null) {
            return true;
        }
        boolean zzgY = this.zzoL.zzgY();
        if (zzgY) {
            return zzgY;
        }
        this.zzoL.zzc("onbackblocked", Collections.emptyMap());
        return zzgY;
    }

    public void zzu(boolean z) {
        this.zzAA = new zzm(this.mActivity, z ? 50 : 32, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(z ? 11 : 9);
        this.zzAA.zza(z, this.zzAx.zzAU);
        this.zzzA.addView(this.zzAA, layoutParams);
    }

    /* access modifiers changed from: protected */
    public void zzv(int i) {
        this.zzoL.zzv(i);
    }

    /* access modifiers changed from: protected */
    public void zzv(boolean z) throws zza {
        if (!this.zzAI) {
            this.mActivity.requestWindowFeature(1);
        }
        Window window = this.mActivity.getWindow();
        if (window == null) {
            throw new zza("Invalid activity, no window available.");
        }
        if (!this.zzAF || (this.zzAx.zzBa != null && this.zzAx.zzBa.zzpl)) {
            window.setFlags(1024, 1024);
        }
        boolean zzbY = this.zzAx.zzAR.zzgS().zzbY();
        this.zzAG = false;
        if (zzbY) {
            if (this.zzAx.orientation == zzp.zzbz().zzgv()) {
                this.zzAG = this.mActivity.getResources().getConfiguration().orientation == 1;
            } else if (this.zzAx.orientation == zzp.zzbz().zzgw()) {
                this.zzAG = this.mActivity.getResources().getConfiguration().orientation == 2;
            }
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaC("Delay onShow to next orientation change: " + this.zzAG);
        setRequestedOrientation(this.zzAx.orientation);
        if (zzp.zzbz().zza(window)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Hardware acceleration on the AdActivity window enabled.");
        }
        if (this.zzAF) {
            this.zzzA.setBackgroundColor(zzAu);
        } else {
            this.zzzA.setBackgroundColor(-16777216);
        }
        this.mActivity.setContentView(this.zzzA);
        zzaE();
        if (z) {
            this.zzoL = zzp.zzby().zza(this.mActivity, this.zzAx.zzAR.zzaN(), true, zzbY, null, this.zzAx.zzqb);
            this.zzoL.zzgS().zzb(null, null, this.zzAx.zzAS, this.zzAx.zzAW, true, this.zzAx.zzAY, null, this.zzAx.zzAR.zzgS().zzhb(), null);
            this.zzoL.zzgS().zza(new com.google.android.gms.internal.zziq.zza() {
                public void zza(zzip zzip, boolean z) {
                    zzip.zzeD();
                }
            });
            if (this.zzAx.url != null) {
                this.zzoL.loadUrl(this.zzAx.url);
            } else if (this.zzAx.zzAV != null) {
                this.zzoL.loadDataWithBaseURL(this.zzAx.zzAT, this.zzAx.zzAV, "text/html", Defaults.ENCODING_UTF_8, null);
            } else {
                throw new zza("No URL or HTML to display in ad overlay.");
            }
            if (this.zzAx.zzAR != null) {
                this.zzAx.zzAR.zzb(this);
            }
        } else {
            this.zzoL = this.zzAx.zzAR;
            this.zzoL.setContext(this.mActivity);
        }
        this.zzoL.zza(this);
        ViewParent parent = this.zzoL.getParent();
        if (parent != null && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).removeView(this.zzoL.getWebView());
        }
        if (this.zzAF) {
            this.zzoL.setBackgroundColor(zzAu);
        }
        this.zzzA.addView(this.zzoL.getWebView(), -1, -1);
        if (!(z || this.zzAG)) {
            zzeD();
        }
        zzu(zzbY);
        if (this.zzoL.zzgT()) {
            zza(zzbY, true);
        }
    }
}
