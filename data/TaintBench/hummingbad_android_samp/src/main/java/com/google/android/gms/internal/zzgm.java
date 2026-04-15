package com.google.android.gms.internal;

import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.request.zzj.zza;
import com.google.android.gms.ads.internal.request.zzk;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzdv.zzb;
import com.google.android.gms.internal.zzdv.zzc;
import com.google.android.gms.internal.zzdv.zzd;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public final class zzgm extends zza {
    private static zzgm zzEL;
    private static final Object zzpm = new Object();
    private final Context mContext;
    private final zzgl zzEM;
    private final zzbr zzEN;
    private final zzdv zzqU;

    zzgm(Context context, zzbr zzbr, zzgl zzgl) {
        this.mContext = context;
        this.zzEM = zzgl;
        this.zzEN = zzbr;
        this.zzqU = new zzdv(context.getApplicationContext() != null ? context.getApplicationContext() : context, new VersionInfoParcel(7895000, 7895000, true), zzbr.zzdb(), new zzb<zzbb>() {
            /* renamed from: zza */
            public void zzc(zzbb zzbb) {
                zzbb.zza("/log", zzdf.zzwP);
            }
        }, new zzc());
    }

    private static AdResponseParcel zza(Context context, zzdv zzdv, zzbr zzbr, zzgl zzgl, AdRequestInfoParcel adRequestInfoParcel) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaC("Starting ad request from service.");
        zzby.zzw(context);
        final zzcd zzcd = new zzcd(((Boolean) zzby.zzuB.get()).booleanValue(), "load_ad", adRequestInfoParcel.zzqf.zzsG);
        if (adRequestInfoParcel.versionCode > 10 && adRequestInfoParcel.zzDT != -1) {
            zzcd.zza(zzcd.zzb(adRequestInfoParcel.zzDT), "cts");
        }
        zzcc zzdl = zzcd.zzdl();
        zzgl.zzEH.init();
        zzgr zzD = zzp.zzbD().zzD(context);
        if (zzD.zzFN == -1) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Device is offline.");
            return new AdResponseParcel(2);
        }
        String uuid = adRequestInfoParcel.versionCode >= 7 ? adRequestInfoParcel.zzDQ : UUID.randomUUID().toString();
        final zzgo zzgo = new zzgo(uuid, adRequestInfoParcel.applicationInfo.packageName);
        if (adRequestInfoParcel.zzDy.extras != null) {
            String string = adRequestInfoParcel.zzDy.extras.getString("_ad");
            if (string != null) {
                return zzgn.zza(context, adRequestInfoParcel, string);
            }
        }
        Location zzc = zzgl.zzEH.zzc(250);
        String zzc2 = zzgl.zzEI.zzc(context, adRequestInfoParcel.zzDz.packageName);
        List zza = zzgl.zzEG.zza(adRequestInfoParcel);
        JSONObject zza2 = zzgn.zza(context, adRequestInfoParcel, zzD, zzgl.zzEK.zzE(context), zzc, zzbr, zzc2, zzgl.zzEJ.zzau(adRequestInfoParcel.zzDA), zza);
        if (adRequestInfoParcel.versionCode < 7) {
            try {
                zza2.put("request_id", uuid);
            } catch (JSONException e) {
            }
        }
        if (zza2 == null) {
            return new AdResponseParcel(0);
        }
        final String jSONObject = zza2.toString();
        zzcd.zza(zzdl, "arc");
        final zzcc zzdl2 = zzcd.zzdl();
        if (((Boolean) zzby.zztX.get()).booleanValue()) {
            final zzdv zzdv2 = zzdv;
            final zzgo zzgo2 = zzgo;
            final zzcd zzcd2 = zzcd;
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzd zzdL = zzdv2.zzdL();
                    zzgo2.zzb(zzdL);
                    zzcd2.zza(zzdl2, "rwc");
                    final zzcc zzdl = zzcd2.zzdl();
                    zzdL.zza(new zzij.zzc<zzbe>() {
                        /* renamed from: zzb */
                        public void zzc(zzbe zzbe) {
                            zzcd2.zza(zzdl, "jsf");
                            zzcd2.zzdm();
                            zzbe.zza("/invalidRequest", zzgo2.zzFc);
                            zzbe.zza("/loadAdURL", zzgo2.zzFd);
                            try {
                                zzbe.zza("AFMA_buildAdURL", jSONObject);
                            } catch (Exception e) {
                                com.google.android.gms.ads.internal.util.client.zzb.zzb("Error requesting an ad url", e);
                            }
                        }
                    }, new zzij.zza() {
                        public void run() {
                        }
                    });
                }
            });
        } else {
            final Context context2 = context;
            final AdRequestInfoParcel adRequestInfoParcel2 = adRequestInfoParcel;
            final zzcc zzcc = zzdl2;
            final String str = jSONObject;
            final zzbr zzbr2 = zzbr;
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzip zza = zzp.zzby().zza(context2, new AdSizeParcel(), false, false, null, adRequestInfoParcel2.zzqb);
                    if (zzp.zzbA().zzgi()) {
                        zza.getWebView().clearCache(true);
                    }
                    zza.setWillNotDraw(true);
                    zzgo.zze(zza);
                    zzcd.zza(zzcc, "rwc");
                    zziq.zza zzb = zzgm.zza(str, zzcd, zzcd.zzdl());
                    zziq zzgS = zza.zzgS();
                    zzgS.zza("/invalidRequest", zzgo.zzFc);
                    zzgS.zza("/loadAdURL", zzgo.zzFd);
                    zzgS.zza("/log", zzdf.zzwP);
                    zzgS.zza(zzb);
                    com.google.android.gms.ads.internal.util.client.zzb.zzaC("Loading the JS library.");
                    zza.loadUrl(zzbr2.zzdb());
                }
            });
        }
        AdResponseParcel adResponseParcel;
        try {
            zzgq zzgq = (zzgq) zzgo.zzfI().get(10, TimeUnit.SECONDS);
            if (zzgq == null) {
                adResponseParcel = new AdResponseParcel(0);
                return adResponseParcel;
            } else if (zzgq.getErrorCode() != -2) {
                adResponseParcel = new AdResponseParcel(zzgq.getErrorCode());
                zzhu.zzHK.post(new Runnable() {
                    public void run() {
                        zzgo.zzfJ();
                        if (zzgo.zzfH() != null) {
                            zzgo.zzfH().release();
                        }
                    }
                });
                return adResponseParcel;
            } else {
                if (zzcd.zzdo() != null) {
                    zzcd.zza(zzcd.zzdo(), "rur");
                }
                jSONObject = null;
                if (zzgq.zzfM()) {
                    jSONObject = zzgl.zzEF.zzat(adRequestInfoParcel.zzDz.packageName);
                }
                adResponseParcel = zza(adRequestInfoParcel, context, adRequestInfoParcel.zzqb.zzIz, zzgq.getUrl(), jSONObject, zzc2, zzgq, zzcd);
                if (adResponseParcel.zzEj == 1) {
                    zzgl.zzEI.clearToken(context, adRequestInfoParcel.zzDz.packageName);
                }
                zzcd.zza(zzdl, "tts");
                adResponseParcel.zzEl = zzcd.zzdn();
                zzhu.zzHK.post(/* anonymous class already generated */);
                return adResponseParcel;
            }
        } catch (Exception e2) {
            adResponseParcel = new AdResponseParcel(0);
            return adResponseParcel;
        } finally {
            zzhu.zzHK.post(/* anonymous class already generated */);
        }
    }

    /* JADX WARNING: Missing block: B:20:0x00a8, code skipped:
            r3 = r4.toString();
            r4 = com.google.android.gms.ads.internal.zzp.zzbx().zza(new java.io.InputStreamReader(r1.getInputStream()));
            zza(r3, r9, r4, r8);
            r5.zzb(r3, r9, r4);
     */
    /* JADX WARNING: Missing block: B:21:0x00c3, code skipped:
            if (r19 == null) goto L_0x00d2;
     */
    /* JADX WARNING: Missing block: B:22:0x00c5, code skipped:
            r19.zza(r2, "ufe");
     */
    /* JADX WARNING: Missing block: B:23:0x00d2, code skipped:
            r2 = r5.zzj(r6);
     */
    /* JADX WARNING: Missing block: B:25:?, code skipped:
            r1.disconnect();
     */
    /* JADX WARNING: Missing block: B:47:?, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("Received error HTTP response code: " + r8);
            r2 = new com.google.android.gms.ads.internal.request.AdResponseParcel(0);
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            r1.disconnect();
     */
    /* JADX WARNING: Missing block: B:65:?, code skipped:
            return r2;
     */
    /* JADX WARNING: Missing block: B:68:?, code skipped:
            return r2;
     */
    public static com.google.android.gms.ads.internal.request.AdResponseParcel zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel r12, android.content.Context r13, java.lang.String r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, com.google.android.gms.internal.zzgq r18, com.google.android.gms.internal.zzcd r19) {
        /*
        if (r19 == 0) goto L_0x00db;
    L_0x0002:
        r1 = r19.zzdl();
        r2 = r1;
    L_0x0007:
        r5 = new com.google.android.gms.internal.zzgp;	 Catch:{ IOException -> 0x014e }
        r5.m1572init(r12);	 Catch:{ IOException -> 0x014e }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x014e }
        r1.<init>();	 Catch:{ IOException -> 0x014e }
        r3 = "AdRequestServiceImpl: Sending request: ";
        r1 = r1.append(r3);	 Catch:{ IOException -> 0x014e }
        r1 = r1.append(r15);	 Catch:{ IOException -> 0x014e }
        r1 = r1.toString();	 Catch:{ IOException -> 0x014e }
        com.google.android.gms.ads.internal.util.client.zzb.zzaC(r1);	 Catch:{ IOException -> 0x014e }
        r3 = new java.net.URL;	 Catch:{ IOException -> 0x014e }
        r3.<init>(r15);	 Catch:{ IOException -> 0x014e }
        r1 = 0;
        r4 = com.google.android.gms.ads.internal.zzp.zzbB();	 Catch:{ IOException -> 0x014e }
        r6 = r4.elapsedRealtime();	 Catch:{ IOException -> 0x014e }
        r4 = r3;
        r3 = r1;
    L_0x0032:
        r1 = r4.openConnection();	 Catch:{ IOException -> 0x014e }
        r1 = (java.net.HttpURLConnection) r1;	 Catch:{ IOException -> 0x014e }
        r8 = com.google.android.gms.ads.internal.zzp.zzbx();	 Catch:{ all -> 0x0171 }
        r9 = 0;
        r8.zza(r13, r14, r9, r1);	 Catch:{ all -> 0x0171 }
        r8 = android.text.TextUtils.isEmpty(r16);	 Catch:{ all -> 0x0171 }
        if (r8 != 0) goto L_0x004d;
    L_0x0046:
        r8 = "x-afma-drt-cookie";
        r0 = r16;
        r1.addRequestProperty(r8, r0);	 Catch:{ all -> 0x0171 }
    L_0x004d:
        r8 = android.text.TextUtils.isEmpty(r17);	 Catch:{ all -> 0x0171 }
        if (r8 != 0) goto L_0x006d;
    L_0x0053:
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0171 }
        r8.<init>();	 Catch:{ all -> 0x0171 }
        r9 = "Bearer ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x0171 }
        r0 = r17;
        r8 = r8.append(r0);	 Catch:{ all -> 0x0171 }
        r8 = r8.toString();	 Catch:{ all -> 0x0171 }
        r9 = "Authorization";
        r1.addRequestProperty(r9, r8);	 Catch:{ all -> 0x0171 }
    L_0x006d:
        if (r18 == 0) goto L_0x0098;
    L_0x006f:
        r8 = r18.zzfL();	 Catch:{ all -> 0x0171 }
        r8 = android.text.TextUtils.isEmpty(r8);	 Catch:{ all -> 0x0171 }
        if (r8 != 0) goto L_0x0098;
    L_0x0079:
        r8 = 1;
        r1.setDoOutput(r8);	 Catch:{ all -> 0x0171 }
        r8 = r18.zzfL();	 Catch:{ all -> 0x0171 }
        r8 = r8.getBytes();	 Catch:{ all -> 0x0171 }
        r9 = r8.length;	 Catch:{ all -> 0x0171 }
        r1.setFixedLengthStreamingMode(r9);	 Catch:{ all -> 0x0171 }
        r9 = new java.io.BufferedOutputStream;	 Catch:{ all -> 0x0171 }
        r10 = r1.getOutputStream();	 Catch:{ all -> 0x0171 }
        r9.<init>(r10);	 Catch:{ all -> 0x0171 }
        r9.write(r8);	 Catch:{ all -> 0x0171 }
        r9.close();	 Catch:{ all -> 0x0171 }
    L_0x0098:
        r8 = r1.getResponseCode();	 Catch:{ all -> 0x0171 }
        r9 = r1.getHeaderFields();	 Catch:{ all -> 0x0171 }
        r10 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r8 < r10) goto L_0x00df;
    L_0x00a4:
        r10 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r8 >= r10) goto L_0x00df;
    L_0x00a8:
        r3 = r4.toString();	 Catch:{ all -> 0x0171 }
        r4 = com.google.android.gms.ads.internal.zzp.zzbx();	 Catch:{ all -> 0x0171 }
        r10 = new java.io.InputStreamReader;	 Catch:{ all -> 0x0171 }
        r11 = r1.getInputStream();	 Catch:{ all -> 0x0171 }
        r10.<init>(r11);	 Catch:{ all -> 0x0171 }
        r4 = r4.zza(r10);	 Catch:{ all -> 0x0171 }
        zza(r3, r9, r4, r8);	 Catch:{ all -> 0x0171 }
        r5.zzb(r3, r9, r4);	 Catch:{ all -> 0x0171 }
        if (r19 == 0) goto L_0x00d2;
    L_0x00c5:
        r3 = 1;
        r3 = new java.lang.String[r3];	 Catch:{ all -> 0x0171 }
        r4 = 0;
        r8 = "ufe";
        r3[r4] = r8;	 Catch:{ all -> 0x0171 }
        r0 = r19;
        r0.zza(r2, r3);	 Catch:{ all -> 0x0171 }
    L_0x00d2:
        r2 = r5.zzj(r6);	 Catch:{ all -> 0x0171 }
        r1.disconnect();	 Catch:{ IOException -> 0x014e }
        r1 = r2;
    L_0x00da:
        return r1;
    L_0x00db:
        r1 = 0;
        r2 = r1;
        goto L_0x0007;
    L_0x00df:
        r4 = r4.toString();	 Catch:{ all -> 0x0171 }
        r10 = 0;
        zza(r4, r9, r10, r8);	 Catch:{ all -> 0x0171 }
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r8 < r4) goto L_0x0125;
    L_0x00eb:
        r4 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r8 >= r4) goto L_0x0125;
    L_0x00ef:
        r4 = "Location";
        r8 = r1.getHeaderField(r4);	 Catch:{ all -> 0x0171 }
        r4 = android.text.TextUtils.isEmpty(r8);	 Catch:{ all -> 0x0171 }
        if (r4 == 0) goto L_0x010b;
    L_0x00fb:
        r2 = "No location header to follow redirect.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);	 Catch:{ all -> 0x0171 }
        r2 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0171 }
        r3 = 0;
        r2.m1360init(r3);	 Catch:{ all -> 0x0171 }
        r1.disconnect();	 Catch:{ IOException -> 0x014e }
        r1 = r2;
        goto L_0x00da;
    L_0x010b:
        r4 = new java.net.URL;	 Catch:{ all -> 0x0171 }
        r4.<init>(r8);	 Catch:{ all -> 0x0171 }
        r3 = r3 + 1;
        r8 = 5;
        if (r3 <= r8) goto L_0x0146;
    L_0x0115:
        r2 = "Too many redirects.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);	 Catch:{ all -> 0x0171 }
        r2 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0171 }
        r3 = 0;
        r2.m1360init(r3);	 Catch:{ all -> 0x0171 }
        r1.disconnect();	 Catch:{ IOException -> 0x014e }
        r1 = r2;
        goto L_0x00da;
    L_0x0125:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0171 }
        r2.<init>();	 Catch:{ all -> 0x0171 }
        r3 = "Received error HTTP response code: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0171 }
        r2 = r2.append(r8);	 Catch:{ all -> 0x0171 }
        r2 = r2.toString();	 Catch:{ all -> 0x0171 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);	 Catch:{ all -> 0x0171 }
        r2 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0171 }
        r3 = 0;
        r2.m1360init(r3);	 Catch:{ all -> 0x0171 }
        r1.disconnect();	 Catch:{ IOException -> 0x014e }
        r1 = r2;
        goto L_0x00da;
    L_0x0146:
        r5.zzh(r9);	 Catch:{ all -> 0x0171 }
        r1.disconnect();	 Catch:{ IOException -> 0x014e }
        goto L_0x0032;
    L_0x014e:
        r1 = move-exception;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Error while connecting to ad server: ";
        r2 = r2.append(r3);
        r1 = r1.getMessage();
        r1 = r2.append(r1);
        r1 = r1.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r1);
        r1 = new com.google.android.gms.ads.internal.request.AdResponseParcel;
        r2 = 2;
        r1.m1360init(r2);
        goto L_0x00da;
    L_0x0171:
        r2 = move-exception;
        r1.disconnect();	 Catch:{ IOException -> 0x014e }
        throw r2;	 Catch:{ IOException -> 0x014e }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzgm.zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel, android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.google.android.gms.internal.zzgq, com.google.android.gms.internal.zzcd):com.google.android.gms.ads.internal.request.AdResponseParcel");
    }

    public static zzgm zza(Context context, zzbr zzbr, zzgl zzgl) {
        zzgm zzgm;
        synchronized (zzpm) {
            if (zzEL == null) {
                if (context.getApplicationContext() != null) {
                    context = context.getApplicationContext();
                }
                zzEL = new zzgm(context, zzbr, zzgl);
            }
            zzgm = zzEL;
        }
        return zzgm;
    }

    /* access modifiers changed from: private|static */
    public static zziq.zza zza(final String str, final zzcd zzcd, final zzcc zzcc) {
        return new zziq.zza() {
            public void zza(zzip zzip, boolean z) {
                zzcd.zza(zzcc, "jsf");
                zzcd.zzdm();
                zzip.zza("AFMA_buildAdURL", str);
            }
        };
    }

    private static void zza(String str, Map<String, List<String>> map, String str2, int i) {
        if (com.google.android.gms.ads.internal.util.client.zzb.zzM(2)) {
            com.google.android.gms.ads.internal.util.client.zzb.v("Http Response: {\n  URL:\n    " + str + "\n  Headers:");
            if (map != null) {
                for (String str3 : map.keySet()) {
                    com.google.android.gms.ads.internal.util.client.zzb.v("    " + str3 + ":");
                    for (String str32 : (List) map.get(str32)) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("      " + str32);
                    }
                }
            }
            com.google.android.gms.ads.internal.util.client.zzb.v("  Body:");
            if (str2 != null) {
                for (int i2 = 0; i2 < Math.min(str2.length(), 100000); i2 += 1000) {
                    com.google.android.gms.ads.internal.util.client.zzb.v(str2.substring(i2, Math.min(str2.length(), i2 + 1000)));
                }
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.v("    null");
            }
            com.google.android.gms.ads.internal.util.client.zzb.v("  Response Code:\n    " + i + "\n}");
        }
    }

    public void zza(final AdRequestInfoParcel adRequestInfoParcel, final zzk zzk) {
        zzp.zzbA().zzb(this.mContext, adRequestInfoParcel.zzqb);
        zzhu.zzb(new Runnable() {
            public void run() {
                AdResponseParcel zze;
                try {
                    zze = zzgm.this.zze(adRequestInfoParcel);
                } catch (Exception e) {
                    zzp.zzbA().zzc(e, true);
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response due to an Exception.", e);
                    zze = null;
                }
                if (zze == null) {
                    zze = new AdResponseParcel(0);
                }
                try {
                    zzk.zzb(zze);
                } catch (RemoteException e2) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to forward ad response.", e2);
                }
            }
        });
    }

    public AdResponseParcel zze(AdRequestInfoParcel adRequestInfoParcel) {
        return zza(this.mContext, this.zzqU, this.zzEN, this.zzEM, adRequestInfoParcel);
    }
}
