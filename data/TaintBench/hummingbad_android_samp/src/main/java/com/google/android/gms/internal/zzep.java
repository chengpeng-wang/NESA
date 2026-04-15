package com.google.android.gms.internal;

import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.internal.formats.zzc;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzek.zza;
import java.util.ArrayList;
import java.util.List;

@zzgk
public class zzep extends zza {
    private final NativeAppInstallAdMapper zzza;

    public zzep(NativeAppInstallAdMapper nativeAppInstallAdMapper) {
        this.zzza = nativeAppInstallAdMapper;
    }

    public String getBody() {
        return this.zzza.getBody();
    }

    public String getCallToAction() {
        return this.zzza.getCallToAction();
    }

    public Bundle getExtras() {
        return this.zzza.getExtras();
    }

    public String getHeadline() {
        return this.zzza.getHeadline();
    }

    public List getImages() {
        List<Image> images = this.zzza.getImages();
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
        return this.zzza.getOverrideClickHandling();
    }

    public boolean getOverrideImpressionRecording() {
        return this.zzza.getOverrideImpressionRecording();
    }

    public String getPrice() {
        return this.zzza.getPrice();
    }

    public double getStarRating() {
        return this.zzza.getStarRating();
    }

    public String getStore() {
        return this.zzza.getStore();
    }

    public void recordImpression() {
        this.zzza.recordImpression();
    }

    public void zzc(zzd zzd) {
        this.zzza.handleClick((View) zze.zzp(zzd));
    }

    public void zzd(zzd zzd) {
        this.zzza.trackView((View) zze.zzp(zzd));
    }

    public zzcj zzds() {
        Image icon = this.zzza.getIcon();
        return icon != null ? new zzc(icon.getDrawable(), icon.getUri()) : null;
    }
}
