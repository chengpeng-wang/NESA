package com.google.android.gms.internal;

import android.location.Location;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import java.util.Date;
import java.util.Set;

@zzgk
public final class zzem implements MediationAdRequest {
    private final Date zzaT;
    private final Set<String> zzaV;
    private final boolean zzaW;
    private final Location zzaX;
    private final int zzsW;
    private final int zzyV;

    public zzem(Date date, int i, Set<String> set, Location location, boolean z, int i2) {
        this.zzaT = date;
        this.zzsW = i;
        this.zzaV = set;
        this.zzaX = location;
        this.zzaW = z;
        this.zzyV = i2;
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

    public boolean isTesting() {
        return this.zzaW;
    }

    public int taggedForChildDirectedTreatment() {
        return this.zzyV;
    }
}
