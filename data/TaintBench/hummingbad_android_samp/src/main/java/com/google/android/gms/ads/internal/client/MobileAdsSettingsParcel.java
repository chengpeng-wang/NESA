package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class MobileAdsSettingsParcel implements SafeParcelable {
    public static final zzad CREATOR = new zzad();
    public final int versionCode;
    public final boolean zzty;
    public final String zztz;

    public MobileAdsSettingsParcel(int versionCode, boolean isGoogleAnalyticsEnabled, String trackingId) {
        this.versionCode = versionCode;
        this.zzty = isGoogleAnalyticsEnabled;
        this.zztz = trackingId;
    }

    public MobileAdsSettingsParcel(zzab settings) {
        this(1, settings.isGoogleAnalyticsEnabled(), settings.getTrackingId());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzad.zza(this, out, flags);
    }
}
