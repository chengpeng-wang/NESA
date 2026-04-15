package com.google.android.gms.internal;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;

@zzgk
public class zzck implements Image {
    private final Drawable mDrawable;
    private final Uri mUri;
    private final zzcj zzww;

    public zzck(zzcj zzcj) {
        Drawable drawable;
        Uri uri = null;
        this.zzww = zzcj;
        try {
            zzd zzdr = this.zzww.zzdr();
            if (zzdr != null) {
                drawable = (Drawable) zze.zzp(zzdr);
                this.mDrawable = drawable;
                uri = this.zzww.getUri();
                this.mUri = uri;
            }
        } catch (RemoteException e) {
            zzb.zzb("Failed to get drawable.", e);
        }
        Object drawable2 = uri;
        this.mDrawable = drawable2;
        try {
            uri = this.zzww.getUri();
        } catch (RemoteException e2) {
            zzb.zzb("Failed to get uri.", e2);
        }
        this.mUri = uri;
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    public Uri getUri() {
        return this.mUri;
    }
}
