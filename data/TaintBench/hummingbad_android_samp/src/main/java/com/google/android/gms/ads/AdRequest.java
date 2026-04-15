package com.google.android.gms.ads;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.client.zzx.zza;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;
import java.util.Date;
import java.util.Set;

public final class AdRequest {
    public static final String DEVICE_ID_EMULATOR = zzx.DEVICE_ID_EMULATOR;
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    public static final int MAX_CONTENT_URL_LENGTH = 512;
    private final zzx zznN;

    public static final class Builder {
        /* access modifiers changed from: private|final */
        public final zza zznO = new zza();

        public Builder() {
            this.zznO.zzG(AdRequest.DEVICE_ID_EMULATOR);
        }

        public Builder addCustomEventExtrasBundle(Class<? extends CustomEvent> adapterClass, Bundle customEventExtras) {
            this.zznO.zzb((Class) adapterClass, customEventExtras);
            return this;
        }

        public Builder addKeyword(String keyword) {
            this.zznO.zzF(keyword);
            return this;
        }

        public Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.zznO.zza(networkExtras);
            return this;
        }

        public Builder addNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass, Bundle networkExtras) {
            this.zznO.zza(adapterClass, networkExtras);
            if (adapterClass.equals(AdMobAdapter.class) && networkExtras.getBoolean("_emulatorLiveAds")) {
                this.zznO.zzH(AdRequest.DEVICE_ID_EMULATOR);
            }
            return this;
        }

        public Builder addTestDevice(String deviceId) {
            this.zznO.zzG(deviceId);
            return this;
        }

        public AdRequest build() {
            return new AdRequest(this);
        }

        public Builder setBirthday(Date birthday) {
            this.zznO.zza(birthday);
            return this;
        }

        public Builder setContentUrl(String contentUrl) {
            com.google.android.gms.common.internal.zzx.zzb(contentUrl, "Content URL must be non-null.");
            com.google.android.gms.common.internal.zzx.zzh(contentUrl, "Content URL must be non-empty.");
            com.google.android.gms.common.internal.zzx.zzb(contentUrl.length() <= 512, "Content URL must not exceed %d in length.  Provided length was %d.", new Object[]{Integer.valueOf(512), Integer.valueOf(contentUrl.length())});
            this.zznO.zzI(contentUrl);
            return this;
        }

        public Builder setGender(int gender) {
            this.zznO.zzm(gender);
            return this;
        }

        public Builder setLocation(Location location) {
            this.zznO.zza(location);
            return this;
        }

        public Builder setRequestAgent(String requestAgent) {
            this.zznO.zzK(requestAgent);
            return this;
        }

        public Builder tagForChildDirectedTreatment(boolean tagForChildDirectedTreatment) {
            this.zznO.zzj(tagForChildDirectedTreatment);
            return this;
        }
    }

    private AdRequest(Builder builder) {
        this.zznN = new zzx(builder.zznO);
    }

    public Date getBirthday() {
        return this.zznN.getBirthday();
    }

    public String getContentUrl() {
        return this.zznN.getContentUrl();
    }

    public <T extends CustomEvent> Bundle getCustomEventExtrasBundle(Class<T> adapterClass) {
        return this.zznN.getCustomEventExtrasBundle(adapterClass);
    }

    public int getGender() {
        return this.zznN.getGender();
    }

    public Set<String> getKeywords() {
        return this.zznN.getKeywords();
    }

    public Location getLocation() {
        return this.zznN.getLocation();
    }

    @Deprecated
    public <T extends NetworkExtras> T getNetworkExtras(Class<T> networkExtrasClass) {
        return this.zznN.getNetworkExtras(networkExtrasClass);
    }

    public <T extends MediationAdapter> Bundle getNetworkExtrasBundle(Class<T> adapterClass) {
        return this.zznN.getNetworkExtrasBundle(adapterClass);
    }

    public boolean isTestDevice(Context context) {
        return this.zznN.isTestDevice(context);
    }

    public zzx zzaF() {
        return this.zznN;
    }
}
