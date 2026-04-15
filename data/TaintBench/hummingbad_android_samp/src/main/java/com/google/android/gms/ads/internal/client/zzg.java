package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.internal.zzgk;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@zzgk
public class zzg {
    public static final zzg zzsF = new zzg();

    protected zzg() {
    }

    public static zzg zzcA() {
        return zzsF;
    }

    public AdRequestParcel zza(Context context, zzx zzx) {
        Date birthday = zzx.getBirthday();
        long time = birthday != null ? birthday.getTime() : -1;
        String contentUrl = zzx.getContentUrl();
        int gender = zzx.getGender();
        Set keywords = zzx.getKeywords();
        List unmodifiableList = !keywords.isEmpty() ? Collections.unmodifiableList(new ArrayList(keywords)) : null;
        boolean isTestDevice = zzx.isTestDevice(context);
        int zzcP = zzx.zzcP();
        Location location = zzx.getLocation();
        Bundle networkExtrasBundle = zzx.getNetworkExtrasBundle(AdMobAdapter.class);
        boolean manualImpressionsEnabled = zzx.getManualImpressionsEnabled();
        String publisherProvidedId = zzx.getPublisherProvidedId();
        SearchAdRequest zzcM = zzx.zzcM();
        SearchAdRequestParcel searchAdRequestParcel = zzcM != null ? new SearchAdRequestParcel(zzcM) : null;
        String str = null;
        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            str = zzp.zzbx().zza(Thread.currentThread().getStackTrace(), applicationContext.getPackageName());
        }
        return new AdRequestParcel(6, time, networkExtrasBundle, gender, unmodifiableList, isTestDevice, zzcP, manualImpressionsEnabled, publisherProvidedId, searchAdRequestParcel, location, contentUrl, zzx.zzcO(), zzx.getCustomTargeting(), Collections.unmodifiableList(new ArrayList(zzx.zzcQ())), zzx.zzcL(), str);
    }

    public RewardedVideoAdRequestParcel zza(Context context, zzx zzx, String str) {
        return new RewardedVideoAdRequestParcel(zza(context, zzx), str);
    }
}
