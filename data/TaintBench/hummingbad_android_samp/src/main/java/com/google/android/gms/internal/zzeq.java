package com.google.android.gms.internal;

import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.internal.formats.zzc;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzel.zza;
import java.util.ArrayList;
import java.util.List;

@zzgk
public class zzeq extends zza {
    private final NativeContentAdMapper zzzb;

    public zzeq(NativeContentAdMapper nativeContentAdMapper) {
        this.zzzb = nativeContentAdMapper;
    }

    public String getAdvertiser() {
        return this.zzzb.getAdvertiser();
    }

    public String getBody() {
        return this.zzzb.getBody();
    }

    public String getCallToAction() {
        return this.zzzb.getCallToAction();
    }

    public Bundle getExtras() {
        return this.zzzb.getExtras();
    }

    public String getHeadline() {
        return this.zzzb.getHeadline();
    }

    public List getImages() {
        List<Image> images = this.zzzb.getImages();
        if (images == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Image image : images) {
            arrayList.add(new zzc(image.getDrawable(), image.getUri()));
        }
        return arrayList;
    }

    public boolean getOverrideClickHandling() {
        return this.zzzb.getOverrideClickHandling();
    }

    public boolean getOverrideImpressionRecording() {
        return this.zzzb.getOverrideImpressionRecording();
    }

    public void recordImpression() {
        this.zzzb.recordImpression();
    }

    public void zzc(zzd zzd) {
        this.zzzb.handleClick((View) zze.zzp(zzd));
    }

    public void zzd(zzd zzd) {
        this.zzzb.trackView((View) zze.zzp(zzd));
    }

    public zzcj zzdw() {
        Image logo = this.zzzb.getLogo();
        return logo != null ? new zzc(logo.getDrawable(), logo.getUri()) : null;
    }
}
