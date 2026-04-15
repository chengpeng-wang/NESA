package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.internal.zzcj.zza;
import java.util.ArrayList;
import java.util.List;

@zzgk
public class zzcq extends NativeContentAd {
    private final zzcp zzwA;
    private final zzck zzwB;
    private final List<Image> zzwy = new ArrayList();

    public zzcq(zzcp zzcp) {
        zzck zzck;
        this.zzwA = zzcp;
        try {
            for (Object zzd : this.zzwA.getImages()) {
                zzcj zzd2 = zzd(zzd);
                if (zzd2 != null) {
                    this.zzwy.add(new zzck(zzd2));
                }
            }
        } catch (RemoteException e) {
            zzb.zzb("Failed to get image.", e);
        }
        try {
            zzcj zzdw = this.zzwA.zzdw();
            if (zzdw != null) {
                zzck = new zzck(zzdw);
                this.zzwB = zzck;
            }
        } catch (RemoteException e2) {
            zzb.zzb("Failed to get icon.", e2);
        }
        zzck = null;
        this.zzwB = zzck;
    }

    public CharSequence getAdvertiser() {
        try {
            return this.zzwA.getAdvertiser();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get attribution.", e);
            return null;
        }
    }

    public CharSequence getBody() {
        try {
            return this.zzwA.getBody();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get body.", e);
            return null;
        }
    }

    public CharSequence getCallToAction() {
        try {
            return this.zzwA.getCallToAction();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get call to action.", e);
            return null;
        }
    }

    public Bundle getExtras() {
        try {
            return this.zzwA.getExtras();
        } catch (RemoteException e) {
            zzb.zzd("Failed to get extras", e);
            return null;
        }
    }

    public CharSequence getHeadline() {
        try {
            return this.zzwA.getHeadline();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get headline.", e);
            return null;
        }
    }

    public List<Image> getImages() {
        return this.zzwy;
    }

    public Image getLogo() {
        return this.zzwB;
    }

    /* access modifiers changed from: 0000 */
    public zzcj zzd(Object obj) {
        return obj instanceof IBinder ? zza.zzt((IBinder) obj) : null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzdt */
    public zzd zzaH() {
        try {
            return this.zzwA.zzdt();
        } catch (RemoteException e) {
            zzb.zzb("Failed to retrieve native ad engine.", e);
            return null;
        }
    }
}
