package com.google.android.gms.internal;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;

@zzhb
public class zzci extends Image {
    private final Drawable mDrawable;
    private final Uri mUri;
    private final double zzxV;
    private final zzch zzyL;

    public zzci(zzch zzch) {
        Drawable drawable;
        double d;
        Uri uri = null;
        this.zzyL = zzch;
        try {
            zzd zzdJ = this.zzyL.zzdJ();
            if (zzdJ != null) {
                drawable = (Drawable) zze.zzp(zzdJ);
                this.mDrawable = drawable;
                uri = this.zzyL.getUri();
                this.mUri = uri;
                d = 1.0d;
                d = this.zzyL.getScale();
                this.zzxV = d;
            }
        } catch (RemoteException e) {
            zzb.zzb("Failed to get drawable.", e);
        }
        Object drawable2 = uri;
        this.mDrawable = drawable2;
        try {
            uri = this.zzyL.getUri();
        } catch (RemoteException e2) {
            zzb.zzb("Failed to get uri.", e2);
        }
        this.mUri = uri;
        d = 1.0d;
        try {
            d = this.zzyL.getScale();
        } catch (RemoteException e3) {
            zzb.zzb("Failed to get scale.", e3);
        }
        this.zzxV = d;
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    public double getScale() {
        return this.zzxV;
    }

    public Uri getUri() {
        return this.mUri;
    }
}
