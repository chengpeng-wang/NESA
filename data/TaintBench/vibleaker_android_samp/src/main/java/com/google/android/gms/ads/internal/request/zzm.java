package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.internal.zzbm;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzdf;
import com.google.android.gms.internal.zzdg;
import com.google.android.gms.internal.zzdk;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzeg;
import com.google.android.gms.internal.zzeg.zzd;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzhe;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzjp;
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

@zzhb
public class zzm extends zzim {
    /* access modifiers changed from: private|static */
    public static zzdk zzIA = null;
    private static zzdf zzIB = null;
    static final long zzIw = TimeUnit.SECONDS.toMillis(10);
    private static boolean zzIx = false;
    /* access modifiers changed from: private|static */
    public static zzeg zzIy = null;
    private static zzdg zzIz = null;
    private static final Object zzqy = new Object();
    private final Context mContext;
    private final Object zzGg = new Object();
    /* access modifiers changed from: private|final */
    public final com.google.android.gms.ads.internal.request.zza.zza zzHg;
    private final com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zzHh;
    /* access modifiers changed from: private */
    public zzd zzIC;

    public static class zza implements com.google.android.gms.internal.zzeg.zzb<zzed> {
        /* renamed from: zza */
        public void zze(zzed zzed) {
            zzm.zzd(zzed);
        }
    }

    public static class zzb implements com.google.android.gms.internal.zzeg.zzb<zzed> {
        /* renamed from: zza */
        public void zze(zzed zzed) {
            zzm.zzc(zzed);
        }
    }

    public static class zzc implements zzdf {
        public void zza(zzjp zzjp, Map<String, String> map) {
            String str = (String) map.get("request_id");
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Invalid request: " + ((String) map.get("errors")));
            zzm.zzIA.zzS(str);
        }
    }

    public zzm(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zza, com.google.android.gms.ads.internal.request.zza.zza zza2) {
        super(true);
        this.zzHg = zza2;
        this.mContext = context;
        this.zzHh = zza;
        synchronized (zzqy) {
            if (!zzIx) {
                zzIA = new zzdk();
                zzIz = new zzdg(context.getApplicationContext(), zza.zzrl);
                zzIB = new zzc();
                zzIy = new zzeg(this.mContext.getApplicationContext(), this.zzHh.zzrl, (String) zzbt.zzvB.get(), new zzb(), new zza());
                zzIx = true;
            }
        }
    }

    private JSONObject zza(AdRequestInfoParcel adRequestInfoParcel, String str) {
        JSONObject jSONObject = null;
        Bundle bundle = adRequestInfoParcel.zzHt.extras.getBundle("sdk_less_server_data");
        String string = adRequestInfoParcel.zzHt.extras.getString("sdk_less_network_id");
        if (bundle == null) {
            return jSONObject;
        }
        JSONObject zza = zzhe.zza(this.mContext, adRequestInfoParcel, zzr.zzbI().zzE(this.mContext), jSONObject, jSONObject, new zzbm((String) zzbt.zzvB.get()), jSONObject, jSONObject, new ArrayList(), jSONObject);
        if (zza == null) {
            return jSONObject;
        }
        Info advertisingIdInfo;
        try {
            advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.mContext);
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException | IOException | IllegalStateException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot get advertising id info", e);
            Object advertisingIdInfo2 = jSONObject;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("request_id", str);
        hashMap.put("network_id", string);
        hashMap.put("request_param", zza);
        hashMap.put("data", bundle);
        if (advertisingIdInfo2 != null) {
            hashMap.put("adid", advertisingIdInfo2.getId());
            hashMap.put("lat", Integer.valueOf(advertisingIdInfo2.isLimitAdTrackingEnabled() ? 1 : 0));
        }
        try {
            return zzr.zzbC().zzG(hashMap);
        } catch (JSONException e2) {
            return jSONObject;
        }
    }

    protected static void zzc(zzed zzed) {
        zzed.zza("/loadAd", (zzdf) zzIA);
        zzed.zza("/fetchHttpRequest", (zzdf) zzIz);
        zzed.zza("/invalidRequest", zzIB);
    }

    protected static void zzd(zzed zzed) {
        zzed.zzb("/loadAd", (zzdf) zzIA);
        zzed.zzb("/fetchHttpRequest", (zzdf) zzIz);
        zzed.zzb("/invalidRequest", zzIB);
    }

    private AdResponseParcel zze(AdRequestInfoParcel adRequestInfoParcel) {
        final String uuid = UUID.randomUUID().toString();
        final JSONObject zza = zza(adRequestInfoParcel, uuid);
        if (zza == null) {
            return new AdResponseParcel(0);
        }
        long elapsedRealtime = zzr.zzbG().elapsedRealtime();
        Future zzR = zzIA.zzR(uuid);
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
            public void run() {
                zzm.this.zzIC = zzm.zzIy.zzer();
                zzm.this.zzIC.zza(new com.google.android.gms.internal.zzji.zzc<zzeh>() {
                    /* renamed from: zzd */
                    public void zze(zzeh zzeh) {
                        try {
                            zzeh.zza("AFMA_getAdapterLessMediationAd", zza);
                        } catch (Exception e) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error requesting an ad url", e);
                            zzm.zzIA.zzS(uuid);
                        }
                    }
                }, new com.google.android.gms.internal.zzji.zza() {
                    public void run() {
                        zzm.zzIA.zzS(uuid);
                    }
                });
            }
        });
        try {
            JSONObject jSONObject = (JSONObject) zzR.get(zzIw - (zzr.zzbG().elapsedRealtime() - elapsedRealtime), TimeUnit.MILLISECONDS);
            if (jSONObject == null) {
                return new AdResponseParcel(-1);
            }
            AdResponseParcel zza2 = zzhe.zza(this.mContext, adRequestInfoParcel, jSONObject.toString());
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
        synchronized (this.zzGg) {
            com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
                public void run() {
                    if (zzm.this.zzIC != null) {
                        zzm.this.zzIC.release();
                        zzm.this.zzIC = null;
                    }
                }
            });
        }
    }

    public void zzbr() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("SdkLessAdLoaderBackgroundTask started.");
        AdRequestInfoParcel adRequestInfoParcel = new AdRequestInfoParcel(this.zzHh, null, -1);
        AdResponseParcel zze = zze(adRequestInfoParcel);
        AdSizeParcel adSizeParcel = null;
        final com.google.android.gms.internal.zzif.zza zza = new com.google.android.gms.internal.zzif.zza(adRequestInfoParcel, zze, null, adSizeParcel, zze.errorCode, zzr.zzbG().elapsedRealtime(), zze.zzHX, null);
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
            public void run() {
                zzm.this.zzHg.zza(zza);
                if (zzm.this.zzIC != null) {
                    zzm.this.zzIC.release();
                    zzm.this.zzIC = null;
                }
            }
        });
    }
}
