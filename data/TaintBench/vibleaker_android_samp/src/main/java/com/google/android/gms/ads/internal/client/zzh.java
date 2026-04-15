package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.internal.zzhb;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@zzhb
public class zzh {
    public static final zzh zzug = new zzh();

    protected zzh() {
    }

    public static zzh zzcO() {
        return zzug;
    }

    public AdRequestParcel zza(Context context, zzaa zzaa) {
        Date birthday = zzaa.getBirthday();
        long time = birthday != null ? birthday.getTime() : -1;
        String contentUrl = zzaa.getContentUrl();
        int gender = zzaa.getGender();
        Set keywords = zzaa.getKeywords();
        List unmodifiableList = !keywords.isEmpty() ? Collections.unmodifiableList(new ArrayList(keywords)) : null;
        boolean isTestDevice = zzaa.isTestDevice(context);
        int zzdd = zzaa.zzdd();
        Location location = zzaa.getLocation();
        Bundle networkExtrasBundle = zzaa.getNetworkExtrasBundle(AdMobAdapter.class);
        boolean manualImpressionsEnabled = zzaa.getManualImpressionsEnabled();
        String publisherProvidedId = zzaa.getPublisherProvidedId();
        SearchAdRequest zzda = zzaa.zzda();
        SearchAdRequestParcel searchAdRequestParcel = zzda != null ? new SearchAdRequestParcel(zzda) : null;
        String str = null;
        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            str = zzn.zzcS().zza(Thread.currentThread().getStackTrace(), applicationContext.getPackageName());
        }
        return new AdRequestParcel(7, time, networkExtrasBundle, gender, unmodifiableList, isTestDevice, zzdd, manualImpressionsEnabled, publisherProvidedId, searchAdRequestParcel, location, contentUrl, zzaa.zzdc(), zzaa.getCustomTargeting(), Collections.unmodifiableList(new ArrayList(zzaa.zzde())), zzaa.zzcZ(), str, zzaa.isDesignedForFamilies());
    }

    public RewardedVideoAdRequestParcel zza(Context context, zzaa zzaa, String str) {
        return new RewardedVideoAdRequestParcel(zza(context, zzaa), str);
    }
}
