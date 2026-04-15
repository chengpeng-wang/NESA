package com.google.android.gms.ads.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class InterstitialAdParameterParcel implements SafeParcelable {
    public static final zzl CREATOR = new zzl();
    public final int versionCode;
    public final boolean zzpk;
    public final boolean zzpl;

    InterstitialAdParameterParcel(int versionCode, boolean transparentBackground, boolean hideStatusBar) {
        this.versionCode = versionCode;
        this.zzpk = transparentBackground;
        this.zzpl = hideStatusBar;
    }

    public InterstitialAdParameterParcel(boolean transparentBackground, boolean hideStatusBar) {
        this.versionCode = 1;
        this.zzpk = transparentBackground;
        this.zzpl = hideStatusBar;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzl.zza(this, out, flags);
    }
}
