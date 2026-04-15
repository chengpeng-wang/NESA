package com.google.android.gms.ads.internal.request;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgk;
import java.util.Collections;
import java.util.List;

@zzgk
public final class AdRequestInfoParcel implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    public final ApplicationInfo applicationInfo;
    public final int versionCode;
    public final String zzDA;
    public final String zzDB;
    public final String zzDC;
    public final Bundle zzDD;
    public final int zzDE;
    public final Bundle zzDF;
    public final boolean zzDG;
    public final Messenger zzDH;
    public final int zzDI;
    public final int zzDJ;
    public final float zzDK;
    public final String zzDL;
    public final boolean zzDM;
    public final int zzDN;
    public final String zzDO;
    public final long zzDP;
    public final String zzDQ;
    public final List<String> zzDR;
    public final List<String> zzDS;
    public final long zzDT;
    public final CapabilityParcel zzDU;
    public final Bundle zzDx;
    public final AdRequestParcel zzDy;
    public final PackageInfo zzDz;
    public final String zzpY;
    public final String zzpZ;
    public final VersionInfoParcel zzqb;
    public final AdSizeParcel zzqf;
    public final NativeAdOptionsParcel zzqt;
    public final List<String> zzqv;

    @zzgk
    public static final class zza {
        public final ApplicationInfo applicationInfo;
        public final String zzDB;
        public final String zzDC;
        public final Bundle zzDD;
        public final int zzDE;
        public final Bundle zzDF;
        public final boolean zzDG;
        public final Messenger zzDH;
        public final int zzDI;
        public final int zzDJ;
        public final float zzDK;
        public final String zzDL;
        public final boolean zzDM;
        public final int zzDN;
        public final long zzDP;
        public final String zzDQ;
        public final List<String> zzDR;
        public final List<String> zzDS;
        public final CapabilityParcel zzDU;
        public final Bundle zzDx;
        public final AdRequestParcel zzDy;
        public final PackageInfo zzDz;
        public final String zzpY;
        public final String zzpZ;
        public final VersionInfoParcel zzqb;
        public final AdSizeParcel zzqf;
        public final NativeAdOptionsParcel zzqt;
        public final List<String> zzqv;

        public zza(Bundle bundle, AdRequestParcel adRequestParcel, AdSizeParcel adSizeParcel, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, VersionInfoParcel versionInfoParcel, Bundle bundle2, List<String> list, List<String> list2, Bundle bundle3, boolean z, Messenger messenger, int i, int i2, float f, String str4, boolean z2, int i3, long j, String str5, List<String> list3, String str6, NativeAdOptionsParcel nativeAdOptionsParcel, CapabilityParcel capabilityParcel) {
            this.zzDx = bundle;
            this.zzDy = adRequestParcel;
            this.zzqf = adSizeParcel;
            this.zzpZ = str;
            this.applicationInfo = applicationInfo;
            this.zzDz = packageInfo;
            this.zzDB = str2;
            this.zzDC = str3;
            this.zzqb = versionInfoParcel;
            this.zzDD = bundle2;
            this.zzDG = z;
            this.zzDH = messenger;
            this.zzDI = i;
            this.zzDJ = i2;
            this.zzDK = f;
            if (list == null || list.size() <= 0) {
                this.zzDE = 0;
                this.zzqv = null;
                this.zzDS = null;
            } else {
                this.zzDE = 3;
                this.zzqv = list;
                this.zzDS = list2;
            }
            this.zzDF = bundle3;
            this.zzDL = str4;
            this.zzDM = z2;
            this.zzDN = i3;
            this.zzDP = j;
            this.zzDQ = str5;
            this.zzDR = list3;
            this.zzpY = str6;
            this.zzqt = nativeAdOptionsParcel;
            this.zzDU = capabilityParcel;
        }
    }

    AdRequestInfoParcel(int versionCode, Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, boolean isAnalyticsInitialized, int screenId, String analyticsClientId, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptions, List<String> nativeCustomTemplateIds, long connectionStartTime, CapabilityParcel capabilityParcel) {
        this.versionCode = versionCode;
        this.zzDx = adPositionBundle;
        this.zzDy = adRequest;
        this.zzqf = adSize;
        this.zzpZ = adUnitId;
        this.applicationInfo = applicationInfo;
        this.zzDz = packageInfo;
        this.zzDA = querySpamSignals;
        this.zzDB = sequenceNumber;
        this.zzDC = sessionId;
        this.zzqb = versionInfo;
        this.zzDD = stats;
        this.zzDE = nativeVersion;
        this.zzqv = nativeTemplates;
        this.zzDS = nativeCustomTemplateIds == null ? Collections.emptyList() : Collections.unmodifiableList(nativeCustomTemplateIds);
        this.zzDF = contentInfo;
        this.zzDG = useHTTPS;
        this.zzDH = prefetchMessenger;
        this.zzDI = screenWidth;
        this.zzDJ = screenHeight;
        this.zzDK = screenDensity;
        this.zzDL = viewHierarchy;
        this.zzDM = isAnalyticsInitialized;
        this.zzDN = screenId;
        this.zzDO = analyticsClientId;
        this.zzDP = correlationId;
        this.zzDQ = requestId;
        this.zzDR = experimentIds == null ? Collections.emptyList() : Collections.unmodifiableList(experimentIds);
        this.zzpY = slotId;
        this.zzqt = nativeAdOptions;
        this.zzDT = connectionStartTime;
        this.zzDU = capabilityParcel;
    }

    public AdRequestInfoParcel(Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, List<String> nativeCustomTemplateIds, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, boolean isAnalyticsInitialized, int screenId, String analyticsClientId, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptionsParcel, long connectionStartTime, CapabilityParcel capabilityParcel) {
        this(11, adPositionBundle, adRequest, adSize, adUnitId, applicationInfo, packageInfo, querySpamSignals, sequenceNumber, sessionId, versionInfo, stats, nativeVersion, nativeTemplates, contentInfo, useHTTPS, prefetchMessenger, screenWidth, screenHeight, screenDensity, viewHierarchy, isAnalyticsInitialized, screenId, analyticsClientId, correlationId, requestId, experimentIds, slotId, nativeAdOptionsParcel, nativeCustomTemplateIds, connectionStartTime, capabilityParcel);
    }

    public AdRequestInfoParcel(zza partialAdRequestInfo, String querySpamSignals, String analyticsClientId, long connectionStartTime) {
        this(partialAdRequestInfo.zzDx, partialAdRequestInfo.zzDy, partialAdRequestInfo.zzqf, partialAdRequestInfo.zzpZ, partialAdRequestInfo.applicationInfo, partialAdRequestInfo.zzDz, querySpamSignals, partialAdRequestInfo.zzDB, partialAdRequestInfo.zzDC, partialAdRequestInfo.zzqb, partialAdRequestInfo.zzDD, partialAdRequestInfo.zzDE, partialAdRequestInfo.zzqv, partialAdRequestInfo.zzDS, partialAdRequestInfo.zzDF, partialAdRequestInfo.zzDG, partialAdRequestInfo.zzDH, partialAdRequestInfo.zzDI, partialAdRequestInfo.zzDJ, partialAdRequestInfo.zzDK, partialAdRequestInfo.zzDL, partialAdRequestInfo.zzDM, partialAdRequestInfo.zzDN, analyticsClientId, partialAdRequestInfo.zzDP, partialAdRequestInfo.zzDQ, partialAdRequestInfo.zzDR, partialAdRequestInfo.zzpY, partialAdRequestInfo.zzqt, connectionStartTime, partialAdRequestInfo.zzDU);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }
}
