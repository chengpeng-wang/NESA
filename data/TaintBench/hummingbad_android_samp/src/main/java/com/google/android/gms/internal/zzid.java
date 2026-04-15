package com.google.android.gms.internal;

import android.app.Activity;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import com.google.android.gms.ads.internal.zzp;

public final class zzid {
    private Activity zzIs;
    private boolean zzIt;
    private boolean zzIu;
    private boolean zzIv;
    private OnGlobalLayoutListener zzIw;
    private OnScrollChangedListener zzIx;

    public zzid(Activity activity, OnGlobalLayoutListener onGlobalLayoutListener, OnScrollChangedListener onScrollChangedListener) {
        this.zzIs = activity;
        this.zzIw = onGlobalLayoutListener;
        this.zzIx = onScrollChangedListener;
    }

    private void zzgF() {
        if (this.zzIs != null && !this.zzIt) {
            if (this.zzIw != null) {
                zzp.zzbx().zza(this.zzIs, this.zzIw);
            }
            if (this.zzIx != null) {
                zzp.zzbx().zza(this.zzIs, this.zzIx);
            }
            this.zzIt = true;
        }
    }

    private void zzgG() {
        if (this.zzIs != null && this.zzIt) {
            if (this.zzIw != null) {
                zzp.zzbz().zzb(this.zzIs, this.zzIw);
            }
            if (this.zzIx != null) {
                zzp.zzbx().zzb(this.zzIs, this.zzIx);
            }
            this.zzIt = false;
        }
    }

    public void onAttachedToWindow() {
        this.zzIu = true;
        if (this.zzIv) {
            zzgF();
        }
    }

    public void onDetachedFromWindow() {
        this.zzIu = false;
        zzgG();
    }

    public void zzgD() {
        this.zzIv = true;
        if (this.zzIu) {
            zzgF();
        }
    }

    public void zzgE() {
        this.zzIv = false;
        zzgG();
    }

    public void zzl(Activity activity) {
        this.zzIs = activity;
    }
}
