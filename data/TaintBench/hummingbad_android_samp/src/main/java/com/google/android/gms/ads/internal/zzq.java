package com.google.android.gms.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ViewSwitcher;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzm;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.client.zzt;
import com.google.android.gms.ads.internal.client.zzu;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.purchase.zzk;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzcv;
import com.google.android.gms.internal.zzcw;
import com.google.android.gms.internal.zzfm;
import com.google.android.gms.internal.zzfq;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhj;
import com.google.android.gms.internal.zzhk;
import com.google.android.gms.internal.zzho;
import com.google.android.gms.internal.zzhq;
import com.google.android.gms.internal.zzhw;
import com.google.android.gms.internal.zzib;
import com.google.android.gms.internal.zzid;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zziq;
import com.google.android.gms.internal.zzlh;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@zzgk
public final class zzq implements OnGlobalLayoutListener, OnScrollChangedListener {
    public final Context context;
    final String zzpY;
    public String zzpZ;
    boolean zzpk;
    boolean zzqA;
    private HashSet<zzhk> zzqB;
    private int zzqC;
    private int zzqD;
    private zzib zzqE;
    final zzan zzqa;
    public final VersionInfoParcel zzqb;
    zza zzqc;
    public zzhq zzqd;
    public zzhq zzqe;
    public AdSizeParcel zzqf;
    public zzhj zzqg;
    public com.google.android.gms.internal.zzhj.zza zzqh;
    public zzhk zzqi;
    zzm zzqj;
    zzn zzqk;
    zzt zzql;
    zzu zzqm;
    zzfm zzqn;
    zzfq zzqo;
    zzct zzqp;
    zzcu zzqq;
    zzlh<String, zzcv> zzqr;
    zzlh<String, zzcw> zzqs;
    NativeAdOptionsParcel zzqt;
    zzch zzqu;
    List<String> zzqv;
    zzk zzqw;
    public zzho zzqx;
    View zzqy;
    public int zzqz;

    public static final class zza extends ViewSwitcher {
        private final zzhw zzqF;
        private final zzid zzqG;

        public zza(Context context, OnGlobalLayoutListener onGlobalLayoutListener, OnScrollChangedListener onScrollChangedListener) {
            super(context);
            this.zzqF = new zzhw(context);
            if (context instanceof Activity) {
                this.zzqG = new zzid((Activity) context, onGlobalLayoutListener, onScrollChangedListener);
                this.zzqG.zzgD();
                return;
            }
            this.zzqG = null;
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.zzqG != null) {
                this.zzqG.onAttachedToWindow();
            }
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.zzqG != null) {
                this.zzqG.onDetachedFromWindow();
            }
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            this.zzqF.zze(event);
            return false;
        }

        public void removeAllViews() {
            ArrayList<zzip> arrayList = new ArrayList();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= getChildCount()) {
                    break;
                }
                View childAt = getChildAt(i2);
                if (childAt != null && (childAt instanceof zzip)) {
                    arrayList.add((zzip) childAt);
                }
                i = i2 + 1;
            }
            super.removeAllViews();
            for (zzip destroy : arrayList) {
                destroy.destroy();
            }
        }

        public void zzbR() {
            zzb.v("Disable position monitoring on adFrame.");
            if (this.zzqG != null) {
                this.zzqG.zzgE();
            }
        }

        public zzhw zzbT() {
            return this.zzqF;
        }
    }

    public zzq(Context context, AdSizeParcel adSizeParcel, String str, VersionInfoParcel versionInfoParcel) {
        this(context, adSizeParcel, str, versionInfoParcel, null);
    }

    zzq(Context context, AdSizeParcel adSizeParcel, String str, VersionInfoParcel versionInfoParcel, zzan zzan) {
        this.zzqx = null;
        this.zzqy = null;
        this.zzqz = 0;
        this.zzqA = false;
        this.zzpk = false;
        this.zzqB = null;
        this.zzqC = -1;
        this.zzqD = -1;
        zzby.zzw(context);
        if (zzp.zzbA().zzgc() != null) {
            List zzde = zzby.zzde();
            if (versionInfoParcel.zzIA != 0) {
                zzde.add(Integer.toString(versionInfoParcel.zzIA));
            }
            zzp.zzbA().zzgc().zzb(zzde);
        }
        this.zzpY = UUID.randomUUID().toString();
        if (adSizeParcel.zzsH || adSizeParcel.zzsJ) {
            this.zzqc = null;
        } else {
            this.zzqc = new zza(context, this, this);
            this.zzqc.setMinimumWidth(adSizeParcel.widthPixels);
            this.zzqc.setMinimumHeight(adSizeParcel.heightPixels);
            this.zzqc.setVisibility(4);
        }
        this.zzqf = adSizeParcel;
        this.zzpZ = str;
        this.context = context;
        this.zzqb = versionInfoParcel;
        if (zzan == null) {
            zzan = new zzan(new zzh(this));
        }
        this.zzqa = zzan;
        this.zzqE = new zzib(200);
        this.zzqs = new zzlh();
    }

    private void zze(boolean z) {
        boolean z2 = true;
        if (this.zzqc != null && this.zzqg != null && this.zzqg.zzAR != null && this.zzqg.zzAR.zzgS().zzbY()) {
            if (!z || this.zzqE.tryAcquire()) {
                int[] iArr = new int[2];
                this.zzqc.getLocationOnScreen(iArr);
                int zzc = com.google.android.gms.ads.internal.client.zzk.zzcE().zzc(this.context, iArr[0]);
                int zzc2 = com.google.android.gms.ads.internal.client.zzk.zzcE().zzc(this.context, iArr[1]);
                if (zzc != this.zzqC || zzc2 != this.zzqD) {
                    this.zzqC = zzc;
                    this.zzqD = zzc2;
                    zziq zzgS = this.zzqg.zzAR.zzgS();
                    zzc = this.zzqC;
                    int i = this.zzqD;
                    if (z) {
                        z2 = false;
                    }
                    zzgS.zza(zzc, i, z2);
                }
            }
        }
    }

    public void destroy() {
        zzbR();
        this.zzqk = null;
        this.zzql = null;
        this.zzqo = null;
        this.zzqn = null;
        this.zzqu = null;
        this.zzqm = null;
        zzf(false);
        if (this.zzqc != null) {
            this.zzqc.removeAllViews();
        }
        zzbM();
        zzbO();
        this.zzqg = null;
    }

    public void onGlobalLayout() {
        zze(false);
    }

    public void onScrollChanged() {
        zze(true);
    }

    public void zza(HashSet<zzhk> hashSet) {
        this.zzqB = hashSet;
    }

    public HashSet<zzhk> zzbL() {
        return this.zzqB;
    }

    public void zzbM() {
        if (this.zzqg != null && this.zzqg.zzAR != null) {
            this.zzqg.zzAR.destroy();
        }
    }

    public void zzbN() {
        if (this.zzqg != null && this.zzqg.zzAR != null) {
            this.zzqg.zzAR.stopLoading();
        }
    }

    public void zzbO() {
        if (this.zzqg != null && this.zzqg.zzyR != null) {
            try {
                this.zzqg.zzyR.destroy();
            } catch (RemoteException e) {
                zzb.zzaE("Could not destroy mediation adapter.");
            }
        }
    }

    public boolean zzbP() {
        return this.zzqz == 0;
    }

    public boolean zzbQ() {
        return this.zzqz == 1;
    }

    public void zzbR() {
        if (this.zzqc != null) {
            this.zzqc.zzbR();
        }
    }

    public void zzbS() {
        this.zzqi.zzl(this.zzqg.zzGI);
        this.zzqi.zzm(this.zzqg.zzGJ);
        this.zzqi.zzy(this.zzqf.zzsH);
        this.zzqi.zzz(this.zzqg.zzDX);
    }

    public void zzf(boolean z) {
        if (this.zzqz == 0) {
            zzbN();
        }
        if (this.zzqd != null) {
            this.zzqd.cancel();
        }
        if (this.zzqe != null) {
            this.zzqe.cancel();
        }
        if (z) {
            this.zzqg = null;
        }
    }
}
