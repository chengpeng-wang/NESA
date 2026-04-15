package com.google.android.gms.internal;

import android.location.Location;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdOptions.Builder;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

@zzgk
public final class zzer implements NativeMediationAdRequest {
    private final Date zzaT;
    private final Set<String> zzaV;
    private final boolean zzaW;
    private final Location zzaX;
    private final NativeAdOptionsParcel zzoX;
    private final List<String> zzoY;
    private final int zzsW;
    private final int zzyV;

    public zzer(Date date, int i, Set<String> set, Location location, boolean z, int i2, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) {
        this.zzaT = date;
        this.zzsW = i;
        this.zzaV = set;
        this.zzaX = location;
        this.zzaW = z;
        this.zzyV = i2;
        this.zzoX = nativeAdOptionsParcel;
        this.zzoY = list;
    }

    public Date getBirthday() {
        return this.zzaT;
    }

    public int getGender() {
        return this.zzsW;
    }

    public Set<String> getKeywords() {
        return this.zzaV;
    }

    public Location getLocation() {
        return this.zzaX;
    }

    public NativeAdOptions getNativeAdOptions() {
        return this.zzoX == null ? null : new Builder().setReturnUrlsForImageAssets(this.zzoX.zzwn).setImageOrientation(this.zzoX.zzwo).setRequestMultipleImages(this.zzoX.zzwp).build();
    }

    public boolean isAppInstallAdRequested() {
        return this.zzoY != null && this.zzoY.contains("2");
    }

    public boolean isContentAdRequested() {
        return this.zzoY != null && this.zzoY.contains("1");
    }

    public boolean isTesting() {
        return this.zzaW;
    }

    public int taggedForChildDirectedTreatment() {
        return this.zzyV;
    }
}
