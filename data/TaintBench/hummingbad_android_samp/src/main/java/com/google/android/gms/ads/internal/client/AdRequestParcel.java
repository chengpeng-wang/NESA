package com.google.android.gms.ads.internal.client;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzgk;
import java.util.List;

@zzgk
public final class AdRequestParcel implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    public final Bundle extras;
    public final int versionCode;
    public final Bundle zzsA;
    public final Bundle zzsB;
    public final List<String> zzsC;
    public final String zzsD;
    public final String zzsE;
    public final long zzsq;
    public final int zzsr;
    public final List<String> zzss;
    public final boolean zzst;
    public final int zzsu;
    public final boolean zzsv;
    public final String zzsw;
    public final SearchAdRequestParcel zzsx;
    public final Location zzsy;
    public final String zzsz;

    public AdRequestParcel(int versionCode, long birthday, Bundle extras, int gender, List<String> keywords, boolean isTestDevice, int tagForChildDirectedTreatment, boolean manualImpressionsEnabled, String publisherProvidedId, SearchAdRequestParcel searchAdRequestParcel, Location location, String contentUrl, Bundle networkExtras, Bundle customTargeting, List<String> categoryExclusions, String requestAgent, String requestPackage) {
        this.versionCode = versionCode;
        this.zzsq = birthday;
        if (extras == null) {
            extras = new Bundle();
        }
        this.extras = extras;
        this.zzsr = gender;
        this.zzss = keywords;
        this.zzst = isTestDevice;
        this.zzsu = tagForChildDirectedTreatment;
        this.zzsv = manualImpressionsEnabled;
        this.zzsw = publisherProvidedId;
        this.zzsx = searchAdRequestParcel;
        this.zzsy = location;
        this.zzsz = contentUrl;
        this.zzsA = networkExtras;
        this.zzsB = customTargeting;
        this.zzsC = categoryExclusions;
        this.zzsD = requestAgent;
        this.zzsE = requestPackage;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AdRequestParcel)) {
            return false;
        }
        AdRequestParcel adRequestParcel = (AdRequestParcel) other;
        return this.versionCode == adRequestParcel.versionCode && this.zzsq == adRequestParcel.zzsq && zzw.equal(this.extras, adRequestParcel.extras) && this.zzsr == adRequestParcel.zzsr && zzw.equal(this.zzss, adRequestParcel.zzss) && this.zzst == adRequestParcel.zzst && this.zzsu == adRequestParcel.zzsu && this.zzsv == adRequestParcel.zzsv && zzw.equal(this.zzsw, adRequestParcel.zzsw) && zzw.equal(this.zzsx, adRequestParcel.zzsx) && zzw.equal(this.zzsy, adRequestParcel.zzsy) && zzw.equal(this.zzsz, adRequestParcel.zzsz) && zzw.equal(this.zzsA, adRequestParcel.zzsA) && zzw.equal(this.zzsB, adRequestParcel.zzsB) && zzw.equal(this.zzsC, adRequestParcel.zzsC) && zzw.equal(this.zzsD, adRequestParcel.zzsD) && zzw.equal(this.zzsE, adRequestParcel.zzsE);
    }

    public int hashCode() {
        return zzw.hashCode(new Object[]{Integer.valueOf(this.versionCode), Long.valueOf(this.zzsq), this.extras, Integer.valueOf(this.zzsr), this.zzss, Boolean.valueOf(this.zzst), Integer.valueOf(this.zzsu), Boolean.valueOf(this.zzsv), this.zzsw, this.zzsx, this.zzsy, this.zzsz, this.zzsA, this.zzsB, this.zzsC, this.zzsD, this.zzsE});
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }
}
