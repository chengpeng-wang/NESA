package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.internal.zzcj.zza;
import java.util.ArrayList;
import java.util.List;

@zzgk
public class zzco extends NativeAppInstallAd {
    private final zzcn zzwx;
    private final List<Image> zzwy = new ArrayList();
    private final zzck zzwz;

    public zzco(zzcn zzcn) {
        zzck zzck;
        this.zzwx = zzcn;
        try {
            for (Object zzd : this.zzwx.getImages()) {
                zzcj zzd2 = zzd(zzd);
                if (zzd2 != null) {
                    this.zzwy.add(new zzck(zzd2));
                }
            }
        } catch (RemoteException e) {
            zzb.zzb("Failed to get image.", e);
        }
        try {
            zzcj zzds = this.zzwx.zzds();
            if (zzds != null) {
                zzck = new zzck(zzds);
                this.zzwz = zzck;
            }
        } catch (RemoteException e2) {
            zzb.zzb("Failed to get icon.", e2);
        }
        zzck = null;
        this.zzwz = zzck;
    }

    public CharSequence getBody() {
        try {
            return this.zzwx.getBody();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get body.", e);
            return null;
        }
    }

    public CharSequence getCallToAction() {
        try {
            return this.zzwx.getCallToAction();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get call to action.", e);
            return null;
        }
    }

    public Bundle getExtras() {
        try {
            return this.zzwx.getExtras();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get extras", e);
            return null;
        }
    }

    public CharSequence getHeadline() {
        try {
            return this.zzwx.getHeadline();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get headline.", e);
            return null;
        }
    }

    public Image getIcon() {
        return this.zzwz;
    }

    public List<Image> getImages() {
        return this.zzwy;
    }

    public CharSequence getPrice() {
        try {
            return this.zzwx.getPrice();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get price.", e);
            return null;
        }
    }

    public Double getStarRating() {
        try {
            double starRating = this.zzwx.getStarRating();
            return starRating == -1.0d ? null : Double.valueOf(starRating);
        } catch (RemoteException e) {
            zzb.zzb("Failed to get star rating.", e);
            return null;
        }
    }

    public CharSequence getStore() {
        try {
            return this.zzwx.getStore();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get store", e);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public zzcj zzd(Object obj) {
        return obj instanceof IBinder ? zza.zzt((IBinder) obj) : null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzdt */
    public zzd zzaH() {
        try {
            return this.zzwx.zzdt();
        } catch (RemoteException e) {
            zzb.zzb("Failed to retrieve native ad engine.", e);
            return null;
        }
    }
}
