package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.internal.zzbb;
import com.google.android.gms.internal.zzbe;
import com.google.android.gms.internal.zzbr;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzdg;
import com.google.android.gms.internal.zzdh;
import com.google.android.gms.internal.zzdl;
import com.google.android.gms.internal.zzdv;
import com.google.android.gms.internal.zzdv.zzd;
import com.google.android.gms.internal.zzea;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzgn;
import com.google.android.gms.internal.zzhq;
import com.google.android.gms.internal.zzip;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzm extends zzhq {
    static final long zzEu = TimeUnit.SECONDS.toMillis(10);
    private static boolean zzEv = false;
    /* access modifiers changed from: private|static */
    public static zzdv zzEw = null;
    private static zzdh zzEx = null;
    /* access modifiers changed from: private|static */
    public static zzdl zzEy = null;
    private static zzdg zzEz = null;
    private static final Object zzpm = new Object();
    private final Context mContext;
    private final Object zzCE = new Object();
    /* access modifiers changed from: private|final */
    public final com.google.android.gms.ads.internal.request.zza.zza zzDp;
    private final com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zzDq;
    /* access modifiers changed from: private */
    public zzd zzEA;

    public static class zza implements com.google.android.gms.internal.zzdv.zzb<zzbb> {
        /* renamed from: zza */
        public void zzc(zzbb zzbb) {
            zzm.zzd(zzbb);
        }
    }

    public static class zzb implements com.google.android.gms.internal.zzdv.zzb<zzbb> {
        /* renamed from: zza */
        public void zzc(zzbb zzbb) {
            zzm.zzc(zzbb);
        }
    }

    public static class zzc implements zzdg {
        public void zza(zzip zzip, Map<String, String> map) {
            String str = (String) map.get("request_id");
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("Invalid request: " + ((String) map.get("errors")));
            zzm.zzEy.zzX(str);
        }
    }

    public zzm(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zza, com.google.android.gms.ads.internal.request.zza.zza zza2) {
        this.zzDp = zza2;
        this.mContext = context;
        this.zzDq = zza;
        synchronized (zzpm) {
            if (!zzEv) {
                zzEy = new zzdl();
                zzEx = new zzdh(context.getApplicationContext(), zza.zzqb);
                zzEz = new zzc();
                zzEw = new zzdv(this.mContext.getApplicationContext(), this.zzDq.zzqb, (String) zzby.zztW.get(), new zzb(), new zza());
                zzEv = true;
            }
        }
    }

    private JSONObject zza(AdRequestInfoParcel adRequestInfoParcel, String str) {
        JSONObject jSONObject = null;
        Bundle bundle = adRequestInfoParcel.zzDy.extras.getBundle("sdk_less_server_data");
        String string = adRequestInfoParcel.zzDy.extras.getString("sdk_less_network_id");
        if (bundle == null) {
            return jSONObject;
        }
        JSONObject zza = zzgn.zza(this.mContext, adRequestInfoParcel, zzp.zzbD().zzD(this.mContext), jSONObject, jSONObject, new zzbr((String) zzby.zztW.get()), jSONObject, jSONObject, new ArrayList());
        if (zza == null) {
            return jSONObject;
        }
        Info advertisingIdInfo;
        try {
            advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.mContext);
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException | IOException | IllegalStateException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot get advertising id info", e);
            advertisingIdInfo = jSONObject;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("request_id", str);
        hashMap.put("network_id", string);
        hashMap.put("request_param", zza);
        hashMap.put("data", bundle);
        if (advertisingIdInfo != null) {
            hashMap.put(MASTNativeAdConstants.RESPONSE_MEDIATION_ADID, advertisingIdInfo.getId());
            hashMap.put(MASTNativeAdConstants.REQUESTPARAM_LATITUDE, Integer.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled() ? 1 : 0));
        }
        try {
            return zzp.zzbx().zzx(hashMap);
        } catch (JSONException e2) {
            return jSONObject;
        }
    }

    protected static void zzc(zzbb zzbb) {
        zzbb.zza("/loadAd", (zzdg) zzEy);
        zzbb.zza("/fetchHttpRequest", (zzdg) zzEx);
        zzbb.zza("/invalidRequest", zzEz);
    }

    protected static void zzd(zzbb zzbb) {
        zzbb.zzb("/loadAd", (zzdg) zzEy);
        zzbb.zzb("/fetchHttpRequest", (zzdg) zzEx);
        zzbb.zzb("/invalidRequest", zzEz);
    }

    private AdResponseParcel zzf(AdRequestInfoParcel adRequestInfoParcel) {
        final String uuid = UUID.randomUUID().toString();
        final JSONObject zza = zza(adRequestInfoParcel, uuid);
        if (zza == null) {
            return new AdResponseParcel(0);
        }
        long elapsedRealtime = zzp.zzbB().elapsedRealtime();
        Future zzW = zzEy.zzW(uuid);
        com.google.android.gms.ads.internal.util.client.zza.zzIy.post(new Runnable() {
            public void run() {
                zzm.this.zzEA = zzm.zzEw.zzdL();
                zzm.this.zzEA.zza(new com.google.android.gms.internal.zzij.zzc<zzbe>() {
                    /* renamed from: zzb */
                    public void zzc(zzbe zzbe) {
                        try {
                            zzbe.zza("AFMA_getAdapterLessMediationAd", zza);
                        } catch (Exception e) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error requesting an ad url", e);
                            zzm.zzEy.zzX(uuid);
                        }
                    }
                }, new com.google.android.gms.internal.zzij.zza() {
                    public void run() {
                        zzm.zzEy.zzX(uuid);
                    }
                });
            }
        });
        try {
            JSONObject jSONObject = (JSONObject) zzW.get(zzEu - (zzp.zzbB().elapsedRealtime() - elapsedRealtime), TimeUnit.MILLISECONDS);
            if (jSONObject == null) {
                return new AdResponseParcel(-1);
            }
            AdResponseParcel zza2 = zzgn.zza(this.mContext, adRequestInfoParcel, jSONObject.toString());
            return (zza2.errorCode == -3 || !TextUtils.isEmpty(zza2.body)) ? zza2 : new AdResponseParcel(3);
        } catch (InterruptedException | CancellationException e) {
            return new AdResponseParcel(-1);
        } catch (TimeoutException e2) {
            return new AdResponseParcel(2);
        } catch (ExecutionException e3) {
            return new AdResponseParcel(0);
        }
    }

    public void onStop() {
        synchronized (this.zzCE) {
            com.google.android.gms.ads.internal.util.client.zza.zzIy.post(new Runnable() {
                public void run() {
                    if (zzm.this.zzEA != null) {
                        zzm.this.zzEA.release();
                        zzm.this.zzEA = null;
                    }
                }
            });
        }
    }

    public void zzdG() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaC("SdkLessAdLoaderBackgroundTask started.");
        AdRequestInfoParcel adRequestInfoParcel = new AdRequestInfoParcel(this.zzDq, null, null, -1);
        AdResponseParcel zzf = zzf(adRequestInfoParcel);
        AdRequestInfoParcel adRequestInfoParcel2 = adRequestInfoParcel;
        zzea zzea = null;
        AdSizeParcel adSizeParcel = null;
        final com.google.android.gms.internal.zzhj.zza zza = new com.google.android.gms.internal.zzhj.zza(adRequestInfoParcel2, zzf, zzea, adSizeParcel, zzf.errorCode, zzp.zzbB().elapsedRealtime(), zzf.zzEb, null);
        com.google.android.gms.ads.internal.util.client.zza.zzIy.post(new Runnable() {
            public void run() {
                zzm.this.zzDp.zza(zza);
                if (zzm.this.zzEA != null) {
                    zzm.this.zzEA.release();
                    zzm.this.zzEA = null;
                }
            }
        });
    }
}
