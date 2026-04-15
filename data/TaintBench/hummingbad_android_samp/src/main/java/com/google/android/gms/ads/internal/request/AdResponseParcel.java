package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgk;
import java.util.Collections;
import java.util.List;

@zzgk
public final class AdResponseParcel implements SafeParcelable {
    public static final zzh CREATOR = new zzh();
    public String body;
    public final int errorCode;
    public final int orientation;
    public final int versionCode;
    public final String zzAT;
    public final boolean zzDG;
    public final long zzDW;
    public final boolean zzDX;
    public final long zzDY;
    public final List<String> zzDZ;
    public final String zzEa;
    public final long zzEb;
    public final String zzEc;
    public final boolean zzEd;
    public final String zzEe;
    public final String zzEf;
    public final boolean zzEg;
    public final boolean zzEh;
    public final boolean zzEi;
    public final int zzEj;
    public LargeParcelTeleporter zzEk;
    public String zzEl;
    public final boolean zzsJ;
    public final long zzyA;
    private AdRequestInfoParcel zzyd;
    public final List<String> zzyw;
    public final List<String> zzyx;

    public AdResponseParcel(int errorCode) {
        this(13, null, null, null, errorCode, null, -1, false, -1, null, -1, -1, null, -1, null, false, null, null, false, false, false, true, false, 0, null, null);
    }

    public AdResponseParcel(int errorCode, long refreshIntervalInMillis) {
        this(13, null, null, null, errorCode, null, -1, false, -1, null, refreshIntervalInMillis, -1, null, -1, null, false, null, null, false, false, false, true, false, 0, null, null);
    }

    AdResponseParcel(int versionCode, String baseUrl, String body, List<String> clickUrls, int errorCode, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, LargeParcelTeleporter bodyTeleporter, String csiLatencyInfo) {
        this.versionCode = versionCode;
        this.zzAT = baseUrl;
        this.body = body;
        this.zzyw = clickUrls != null ? Collections.unmodifiableList(clickUrls) : null;
        this.errorCode = errorCode;
        this.zzyx = impressionUrls != null ? Collections.unmodifiableList(impressionUrls) : null;
        this.zzDW = interstitialTimeoutInMillis;
        this.zzDX = isMediation;
        this.zzDY = mediationConfigCacheTimeInMillis;
        this.zzDZ = manualTrackingUrls != null ? Collections.unmodifiableList(manualTrackingUrls) : null;
        this.zzyA = refreshIntervalInMillis;
        this.orientation = orientation;
        this.zzEa = adSizeString;
        this.zzEb = fetchTime;
        this.zzEc = debugDialog;
        this.zzEd = isJavascriptTag;
        this.zzEe = passbackUrl;
        this.zzEf = activeViewJSON;
        this.zzEg = isCustomRenderAllowed;
        this.zzsJ = isNative;
        this.zzDG = useHTTPS;
        this.zzEh = contentUrlOptedOut;
        this.zzEi = isPrefetched;
        this.zzEj = panTokenStatus;
        this.zzEk = bodyTeleporter;
        this.zzEl = csiLatencyInfo;
        if (this.body == null && this.zzEk != null) {
            StringParcel stringParcel = (StringParcel) this.zzEk.zza(StringParcel.CREATOR);
            if (stringParcel != null && !TextUtils.isEmpty(stringParcel.zzfF())) {
                this.body = stringParcel.zzfF();
            }
        }
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus) {
        this(13, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, false, null, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null);
        this.zzyd = adRequestInfo;
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus) {
        this(13, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, isJavascriptTag, passbackUrl, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null);
        this.zzyd = adRequestInfo;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (!(this.zzyd == null || this.zzyd.versionCode < 9 || TextUtils.isEmpty(this.body))) {
            this.zzEk = new LargeParcelTeleporter(new StringParcel(this.body));
            this.body = null;
        }
        zzh.zza(this, out, flags);
    }
}
