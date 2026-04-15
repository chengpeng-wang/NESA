package com.google.android.gms.ads.internal.overlay;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.ads.internal.InterstitialAdParameterParcel;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzdd;
import com.google.android.gms.internal.zzdi;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzip;

@zzgk
public final class AdOverlayInfoParcel implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    public final int orientation;
    public final String url;
    public final int versionCode;
    public final AdLauncherIntentInfoParcel zzAO;
    public final zza zzAP;
    public final zzg zzAQ;
    public final zzip zzAR;
    public final zzdd zzAS;
    public final String zzAT;
    public final boolean zzAU;
    public final String zzAV;
    public final zzn zzAW;
    public final int zzAX;
    public final zzdi zzAY;
    public final String zzAZ;
    public final InterstitialAdParameterParcel zzBa;
    public final VersionInfoParcel zzqb;

    AdOverlayInfoParcel(int versionCode, AdLauncherIntentInfoParcel adLauncherIntentInfo, IBinder wrappedAdClickListener, IBinder wrappedAdOverlayListener, IBinder wrappedAdWebView, IBinder wrappedAppEventGmsgListener, String baseUrl, boolean customClose, String html, IBinder wrappedLeaveApplicationListener, int orientation, int overlayType, String url, VersionInfoParcel versionInfo, IBinder wrappedInAppPurchaseGmsgListener, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = versionCode;
        this.zzAO = adLauncherIntentInfo;
        this.zzAP = (zza) zze.zzp(zzd.zza.zzbk(wrappedAdClickListener));
        this.zzAQ = (zzg) zze.zzp(zzd.zza.zzbk(wrappedAdOverlayListener));
        this.zzAR = (zzip) zze.zzp(zzd.zza.zzbk(wrappedAdWebView));
        this.zzAS = (zzdd) zze.zzp(zzd.zza.zzbk(wrappedAppEventGmsgListener));
        this.zzAT = baseUrl;
        this.zzAU = customClose;
        this.zzAV = html;
        this.zzAW = (zzn) zze.zzp(zzd.zza.zzbk(wrappedLeaveApplicationListener));
        this.orientation = orientation;
        this.zzAX = overlayType;
        this.url = url;
        this.zzqb = versionInfo;
        this.zzAY = (zzdi) zze.zzp(zzd.zza.zzbk(wrappedInAppPurchaseGmsgListener));
        this.zzAZ = debugMessage;
        this.zzBa = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, zzip adWebView, int orientation, VersionInfoParcel versionInfo, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = 4;
        this.zzAO = null;
        this.zzAP = adClickListener;
        this.zzAQ = adOverlayListener;
        this.zzAR = adWebView;
        this.zzAS = null;
        this.zzAT = null;
        this.zzAU = false;
        this.zzAV = null;
        this.zzAW = leaveApplicationListener;
        this.orientation = orientation;
        this.zzAX = 1;
        this.url = null;
        this.zzqb = versionInfo;
        this.zzAY = null;
        this.zzAZ = debugMessage;
        this.zzBa = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, zzip adWebView, boolean customClose, int orientation, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzAO = null;
        this.zzAP = adClickListener;
        this.zzAQ = adOverlayListener;
        this.zzAR = adWebView;
        this.zzAS = null;
        this.zzAT = null;
        this.zzAU = customClose;
        this.zzAV = null;
        this.zzAW = leaveApplicationListener;
        this.orientation = orientation;
        this.zzAX = 2;
        this.url = null;
        this.zzqb = versionInfo;
        this.zzAY = null;
        this.zzAZ = null;
        this.zzBa = null;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzdd appEventGmsgListener, zzn leaveApplicationListener, zzip adWebView, boolean customClose, int orientation, String url, VersionInfoParcel versionInfo, zzdi inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzAO = null;
        this.zzAP = adClickListener;
        this.zzAQ = adOverlayListener;
        this.zzAR = adWebView;
        this.zzAS = appEventGmsgListener;
        this.zzAT = null;
        this.zzAU = customClose;
        this.zzAV = null;
        this.zzAW = leaveApplicationListener;
        this.orientation = orientation;
        this.zzAX = 3;
        this.url = url;
        this.zzqb = versionInfo;
        this.zzAY = inAppPurchaseGmsgListener;
        this.zzAZ = null;
        this.zzBa = null;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzdd appEventGmsgListener, zzn leaveApplicationListener, zzip adWebView, boolean customClose, int orientation, String html, String baseUrl, VersionInfoParcel versionInfo, zzdi inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzAO = null;
        this.zzAP = adClickListener;
        this.zzAQ = adOverlayListener;
        this.zzAR = adWebView;
        this.zzAS = appEventGmsgListener;
        this.zzAT = baseUrl;
        this.zzAU = customClose;
        this.zzAV = html;
        this.zzAW = leaveApplicationListener;
        this.orientation = orientation;
        this.zzAX = 3;
        this.url = null;
        this.zzqb = versionInfo;
        this.zzAY = inAppPurchaseGmsgListener;
        this.zzAZ = null;
        this.zzBa = null;
    }

    public AdOverlayInfoParcel(AdLauncherIntentInfoParcel adLauncherIntentInfo, zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzAO = adLauncherIntentInfo;
        this.zzAP = adClickListener;
        this.zzAQ = adOverlayListener;
        this.zzAR = null;
        this.zzAS = null;
        this.zzAT = null;
        this.zzAU = false;
        this.zzAV = null;
        this.zzAW = leaveApplicationListener;
        this.orientation = -1;
        this.zzAX = 4;
        this.url = null;
        this.zzqb = versionInfo;
        this.zzAY = null;
        this.zzAZ = null;
        this.zzBa = null;
    }

    public static void zza(Intent intent, AdOverlayInfoParcel adOverlayInfoParcel) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", adOverlayInfoParcel);
        intent.putExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bundle);
    }

    public static AdOverlayInfoParcel zzb(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
            bundleExtra.setClassLoader(AdOverlayInfoParcel.class.getClassLoader());
            return (AdOverlayInfoParcel) bundleExtra.getParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
        } catch (Exception e) {
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzeE() {
        return zze.zzx(this.zzAP).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzeF() {
        return zze.zzx(this.zzAQ).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzeG() {
        return zze.zzx(this.zzAR).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzeH() {
        return zze.zzx(this.zzAS).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzeI() {
        return zze.zzx(this.zzAY).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzeJ() {
        return zze.zzx(this.zzAW).asBinder();
    }
}
