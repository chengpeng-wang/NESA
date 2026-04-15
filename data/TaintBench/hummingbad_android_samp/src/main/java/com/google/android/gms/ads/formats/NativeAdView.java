package com.google.android.gms.ads.formats;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcl;

public abstract class NativeAdView extends FrameLayout {
    private final FrameLayout zznY;
    private final zzcl zznZ = zzaI();

    public NativeAdView(Context context) {
        super(context);
        this.zznY = zzm(context);
    }

    public NativeAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zznY = zzm(context);
    }

    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.zznY = zzm(context);
    }

    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.zznY = zzm(context);
    }

    private zzcl zzaI() {
        zzx.zzb(this.zznY, "createDelegate must be called after mOverlayFrame has been created");
        return zzk.zzcI().zza(this.zznY.getContext(), this, this.zznY);
    }

    private FrameLayout zzm(Context context) {
        FrameLayout zzn = zzn(context);
        zzn.setLayoutParams(new LayoutParams(-1, -1));
        addView(zzn);
        return zzn;
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        super.bringChildToFront(this.zznY);
    }

    public void bringChildToFront(View child) {
        super.bringChildToFront(child);
        if (this.zznY != child) {
            super.bringChildToFront(this.zznY);
        }
    }

    public void removeAllViews() {
        super.removeAllViews();
        super.addView(this.zznY);
    }

    public void removeView(View child) {
        if (this.zznY != child) {
            super.removeView(child);
        }
    }

    public void setNativeAd(NativeAd ad) {
        try {
            this.zznZ.zzb((zzd) ad.zzaH());
        } catch (RemoteException e) {
            zzb.zzb("Unable to call setNativeAd on delegate", e);
        }
    }

    /* access modifiers changed from: protected */
    public void zza(String str, View view) {
        try {
            this.zznZ.zza(str, zze.zzx(view));
        } catch (RemoteException e) {
            zzb.zzb("Unable to call setAssetView on delegate", e);
        }
    }

    /* access modifiers changed from: protected */
    public View zzm(String str) {
        try {
            zzd zzU = this.zznZ.zzU(str);
            if (zzU != null) {
                return (View) zze.zzp(zzU);
            }
        } catch (RemoteException e) {
            zzb.zzb("Unable to call getAssetView on delegate", e);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public FrameLayout zzn(Context context) {
        return new FrameLayout(context);
    }
}
