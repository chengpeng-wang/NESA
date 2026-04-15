package com.google.android.gms.internal;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View.MeasureSpec;
import android.webkit.WebView;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.moceanmobile.mast.Defaults;

@zzgk
public class zzfx implements Runnable {
    /* access modifiers changed from: private|final */
    public final Handler zzCs;
    /* access modifiers changed from: private|final */
    public final long zzCt;
    /* access modifiers changed from: private */
    public long zzCu;
    /* access modifiers changed from: private */
    public com.google.android.gms.internal.zziq.zza zzCv;
    protected boolean zzCw;
    protected boolean zzCx;
    /* access modifiers changed from: private|final */
    public final int zznP;
    /* access modifiers changed from: private|final */
    public final int zznQ;
    protected final zzip zzoL;

    protected final class zza extends AsyncTask<Void, Void, Boolean> {
        private final WebView zzCy;
        private Bitmap zzCz;

        public zza(WebView webView) {
            this.zzCy = webView;
        }

        /* access modifiers changed from: protected|declared_synchronized */
        public synchronized void onPreExecute() {
            this.zzCz = Bitmap.createBitmap(zzfx.this.zznP, zzfx.this.zznQ, Config.ARGB_8888);
            this.zzCy.setVisibility(0);
            this.zzCy.measure(MeasureSpec.makeMeasureSpec(zzfx.this.zznP, 0), MeasureSpec.makeMeasureSpec(zzfx.this.zznQ, 0));
            this.zzCy.layout(0, 0, zzfx.this.zznP, zzfx.this.zznQ);
            this.zzCy.draw(new Canvas(this.zzCz));
            this.zzCy.invalidate();
        }

        /* access modifiers changed from: protected|varargs|declared_synchronized */
        /* renamed from: zza */
        public synchronized Boolean doInBackground(Void... voidArr) {
            Boolean valueOf;
            int width = this.zzCz.getWidth();
            int height = this.zzCz.getHeight();
            if (width == 0 || height == 0) {
                valueOf = Boolean.valueOf(false);
            } else {
                int i = 0;
                for (int i2 = 0; i2 < width; i2 += 10) {
                    for (int i3 = 0; i3 < height; i3 += 10) {
                        if (this.zzCz.getPixel(i2, i3) != 0) {
                            i++;
                        }
                    }
                }
                valueOf = Boolean.valueOf(((double) i) / (((double) (width * height)) / 100.0d) > 0.1d);
            }
            return valueOf;
        }

        /* access modifiers changed from: protected */
        /* renamed from: zza */
        public void onPostExecute(Boolean bool) {
            zzfx.zzc(zzfx.this);
            if (bool.booleanValue() || zzfx.this.zzfn() || zzfx.this.zzCu <= 0) {
                zzfx.this.zzCx = bool.booleanValue();
                zzfx.this.zzCv.zza(zzfx.this.zzoL, true);
            } else if (zzfx.this.zzCu > 0) {
                if (zzb.zzM(2)) {
                    zzb.zzaC("Ad not detected, scheduling another run.");
                }
                zzfx.this.zzCs.postDelayed(zzfx.this, zzfx.this.zzCt);
            }
        }
    }

    public zzfx(com.google.android.gms.internal.zziq.zza zza, zzip zzip, int i, int i2) {
        this(zza, zzip, i, i2, 200, 50);
    }

    public zzfx(com.google.android.gms.internal.zziq.zza zza, zzip zzip, int i, int i2, long j, long j2) {
        this.zzCt = j;
        this.zzCu = j2;
        this.zzCs = new Handler(Looper.getMainLooper());
        this.zzoL = zzip;
        this.zzCv = zza;
        this.zzCw = false;
        this.zzCx = false;
        this.zznQ = i2;
        this.zznP = i;
    }

    static /* synthetic */ long zzc(zzfx zzfx) {
        long j = zzfx.zzCu - 1;
        zzfx.zzCu = j;
        return j;
    }

    public void run() {
        if (this.zzoL == null || zzfn()) {
            this.zzCv.zza(this.zzoL, true);
        } else {
            new zza(this.zzoL.getWebView()).execute(new Void[0]);
        }
    }

    public void zza(AdResponseParcel adResponseParcel) {
        zza(adResponseParcel, new zzix(this, this.zzoL, adResponseParcel.zzEe));
    }

    public void zza(AdResponseParcel adResponseParcel, zzix zzix) {
        this.zzoL.setWebViewClient(zzix);
        this.zzoL.loadDataWithBaseURL(TextUtils.isEmpty(adResponseParcel.zzAT) ? null : zzp.zzbx().zzaw(adResponseParcel.zzAT), adResponseParcel.body, "text/html", Defaults.ENCODING_UTF_8, null);
    }

    public void zzfl() {
        this.zzCs.postDelayed(this, this.zzCt);
    }

    public synchronized void zzfm() {
        this.zzCw = true;
    }

    public synchronized boolean zzfn() {
        return this.zzCw;
    }

    public boolean zzfo() {
        return this.zzCx;
    }
}
