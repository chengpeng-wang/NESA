package com.google.android.gms.internal;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.request.zzj.zza;
import com.google.android.gms.ads.internal.request.zzk;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzeg.zzb;
import com.google.android.gms.internal.zzeg.zzc;
import com.google.android.gms.internal.zzeg.zzd;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzhd extends zza {
    private static zzhd zzIQ;
    private static final Object zzqy = new Object();
    private final Context mContext;
    private final zzhc zzIR;
    private final zzbm zzIS;
    private final zzeg zzIT;

    zzhd(Context context, zzbm zzbm, zzhc zzhc) {
        this.mContext = context;
        this.zzIR = zzhc;
        this.zzIS = zzbm;
        this.zzIT = new zzeg(context.getApplicationContext() != null ? context.getApplicationContext() : context, new VersionInfoParcel(8487000, 8487000, true), zzbm.zzdp(), new zzb<zzed>() {
            /* renamed from: zza */
            public void zze(zzed zzed) {
                zzed.zza("/log", zzde.zzzf);
            }
        }, new zzc());
    }

    private static AdResponseParcel zza(Context context, zzeg zzeg, zzbm zzbm, zzhc zzhc, AdRequestInfoParcel adRequestInfoParcel) {
        Bundle bundle;
        Future future;
        final zzhc zzhc2;
        final Context context2;
        final AdRequestInfoParcel adRequestInfoParcel2;
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Starting ad request from service.");
        zzbt.initialize(context);
        final zzcb zzcb = new zzcb(((Boolean) zzbt.zzwg.get()).booleanValue(), "load_ad", adRequestInfoParcel.zzrp.zzuh);
        if (adRequestInfoParcel.versionCode > 10 && adRequestInfoParcel.zzHL != -1) {
            zzcb.zza(zzcb.zzb(adRequestInfoParcel.zzHL), "cts");
        }
        zzbz zzdB = zzcb.zzdB();
        Bundle bundle2 = (adRequestInfoParcel.versionCode < 4 || adRequestInfoParcel.zzHA == null) ? null : adRequestInfoParcel.zzHA;
        if (!((Boolean) zzbt.zzwp.get()).booleanValue() || zzhc.zzIP == null) {
            bundle = bundle2;
            future = null;
        } else {
            if (bundle2 == null && ((Boolean) zzbt.zzwq.get()).booleanValue()) {
                zzin.v("contentInfo is not present, but we'll still launch the app index task");
                bundle2 = new Bundle();
            }
            if (bundle2 != null) {
                zzhc2 = zzhc;
                context2 = context;
                adRequestInfoParcel2 = adRequestInfoParcel;
                bundle = bundle2;
                future = zziq.zza(new Callable<Void>() {
                    /* renamed from: zzdt */
                    public Void call() throws Exception {
                        zzhc2.zzIP.zza(context2, adRequestInfoParcel2.zzHu.packageName, bundle2);
                        return null;
                    }
                });
            } else {
                bundle = bundle2;
                future = null;
            }
        }
        zzhc.zzIK.zzex();
        zzhj zzE = zzr.zzbI().zzE(context);
        if (zzE.zzKc == -1) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Device is offline.");
            return new AdResponseParcel(2);
        }
        String uuid = adRequestInfoParcel.versionCode >= 7 ? adRequestInfoParcel.zzHI : UUID.randomUUID().toString();
        final zzhf zzhf = new zzhf(uuid, adRequestInfoParcel.applicationInfo.packageName);
        if (adRequestInfoParcel.zzHt.extras != null) {
            String string = adRequestInfoParcel.zzHt.extras.getString("_ad");
            if (string != null) {
                return zzhe.zza(context, adRequestInfoParcel, string);
            }
        }
        Location zzd = zzhc.zzIK.zzd(250);
        String token = zzhc.zzIL.getToken(context, adRequestInfoParcel.zzrj, adRequestInfoParcel.zzHu.packageName);
        List zza = zzhc.zzII.zza(adRequestInfoParcel);
        String zzf = zzhc.zzIM.zzf(adRequestInfoParcel);
        zzhn.zza zzF = zzhc.zzIN.zzF(context);
        if (future != null) {
            try {
                zzin.v("Waiting for app index fetching task.");
                future.get(((Long) zzbt.zzwr.get()).longValue(), TimeUnit.MILLISECONDS);
                zzin.v("App index fetching task completed.");
            } catch (InterruptedException | ExecutionException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to fetch app index signal", e);
            } catch (TimeoutException e2) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaI("Timed out waiting for app index fetching task");
            }
        }
        JSONObject zza2 = zzhe.zza(context, adRequestInfoParcel, zzE, zzF, zzd, zzbm, token, zzf, zza, bundle);
        if (adRequestInfoParcel.versionCode < 7) {
            try {
                zza2.put("request_id", uuid);
            } catch (JSONException e3) {
            }
        }
        if (zza2 == null) {
            return new AdResponseParcel(0);
        }
        final String jSONObject = zza2.toString();
        zzcb.zza(zzdB, "arc");
        final zzbz zzdB2 = zzcb.zzdB();
        if (((Boolean) zzbt.zzvC.get()).booleanValue()) {
            final zzeg zzeg2 = zzeg;
            final zzhf zzhf2 = zzhf;
            final zzcb zzcb2 = zzcb;
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    zzd zzer = zzeg2.zzer();
                    zzhf2.zzb(zzer);
                    zzcb2.zza(zzdB2, "rwc");
                    final zzbz zzdB = zzcb2.zzdB();
                    zzer.zza(new zzji.zzc<zzeh>() {
                        /* renamed from: zzd */
                        public void zze(zzeh zzeh) {
                            zzcb2.zza(zzdB, "jsf");
                            zzcb2.zzdC();
                            zzeh.zza("/invalidRequest", zzhf2.zzJk);
                            zzeh.zza("/loadAdURL", zzhf2.zzJl);
                            try {
                                zzeh.zze("AFMA_buildAdURL", jSONObject);
                            } catch (Exception e) {
                                com.google.android.gms.ads.internal.util.client.zzb.zzb("Error requesting an ad url", e);
                            }
                        }
                    }, new zzji.zza() {
                        public void run() {
                        }
                    });
                }
            });
        } else {
            final Context context3 = context;
            final AdRequestInfoParcel adRequestInfoParcel3 = adRequestInfoParcel;
            final zzbz zzbz = zzdB2;
            final String str = jSONObject;
            final zzbm zzbm2 = zzbm;
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    zzjp zza = zzr.zzbD().zza(context3, new AdSizeParcel(), false, false, null, adRequestInfoParcel3.zzrl);
                    if (zzr.zzbF().zzhi()) {
                        zza.clearCache(true);
                    }
                    zza.getWebView().setWillNotDraw(true);
                    zzhf.zzh(zza);
                    zzcb.zza(zzbz, "rwc");
                    zzjq.zza zzb = zzhd.zza(str, zzcb, zzcb.zzdB());
                    zzjq zzhU = zza.zzhU();
                    zzhU.zza("/invalidRequest", zzhf.zzJk);
                    zzhU.zza("/loadAdURL", zzhf.zzJl);
                    zzhU.zza("/log", zzde.zzzf);
                    zzhU.zza(zzb);
                    com.google.android.gms.ads.internal.util.client.zzb.zzaI("Loading the JS library.");
                    zza.loadUrl(zzbm2.zzdp());
                }
            });
        }
        AdResponseParcel adResponseParcel;
        try {
            zzhi zzhi = (zzhi) zzhf.zzgC().get(10, TimeUnit.SECONDS);
            if (zzhi == null) {
                adResponseParcel = new AdResponseParcel(0);
                return adResponseParcel;
            } else if (zzhi.getErrorCode() != -2) {
                adResponseParcel = new AdResponseParcel(zzhi.getErrorCode());
                zzhc2 = zzhc;
                context2 = context;
                adRequestInfoParcel2 = adRequestInfoParcel;
                zzir.zzMc.post(new Runnable() {
                    public void run() {
                        zzhc2.zzIJ.zza(context2, zzhf, adRequestInfoParcel2.zzrl);
                    }
                });
                return adResponseParcel;
            } else {
                if (zzcb.zzdE() != null) {
                    zzcb.zza(zzcb.zzdE(), "rur");
                }
                String str2 = null;
                if (zzhi.zzgG()) {
                    str2 = zzhc.zzIH.zzaz(adRequestInfoParcel.zzHu.packageName);
                }
                adResponseParcel = zza(adRequestInfoParcel, context, adRequestInfoParcel.zzrl.afmaVersion, zzhi.getUrl(), str2, zzhi.zzgH() ? token : null, zzhi, zzcb, zzhc);
                if (adResponseParcel.zzIf == 1) {
                    zzhc.zzIL.clearToken(context, adRequestInfoParcel.zzHu.packageName);
                }
                zzcb.zza(zzdB, "tts");
                adResponseParcel.zzIh = zzcb.zzdD();
                zzhc2 = zzhc;
                context2 = context;
                adRequestInfoParcel2 = adRequestInfoParcel;
                zzir.zzMc.post(/* anonymous class already generated */);
                return adResponseParcel;
            }
        } catch (Exception e4) {
            adResponseParcel = new AdResponseParcel(0);
            return adResponseParcel;
        } finally {
            zzhc2 = zzhc;
            context2 = context;
            adRequestInfoParcel2 = adRequestInfoParcel;
            zzir.zzMc.post(/* anonymous class already generated */);
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:61:0x0132=Splitter:B:61:0x0132, B:48:0x00fc=Splitter:B:48:0x00fc, B:85:0x018f=Splitter:B:85:0x018f} */
    /* JADX WARNING: Missing block: B:29:0x00b6, code skipped:
            r6 = r7.toString();
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            r4 = new java.io.InputStreamReader(r2.getInputStream());
     */
    /* JADX WARNING: Missing block: B:34:?, code skipped:
            r5 = com.google.android.gms.ads.internal.zzr.zzbC().zza((java.io.InputStreamReader) r4);
     */
    /* JADX WARNING: Missing block: B:36:?, code skipped:
            com.google.android.gms.internal.zzna.zzb(r4);
            zza(r6, r12, r5, r9);
            r8.zzb(r6, r12, r5);
     */
    /* JADX WARNING: Missing block: B:37:0x00d5, code skipped:
            if (r20 == null) goto L_0x00e4;
     */
    /* JADX WARNING: Missing block: B:38:0x00d7, code skipped:
            r20.zza(r3, "ufe");
     */
    /* JADX WARNING: Missing block: B:39:0x00e4, code skipped:
            r3 = r8.zzj(r10);
     */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:42:0x00eb, code skipped:
            if (r21 == null) goto L_0x00f4;
     */
    /* JADX WARNING: Missing block: B:43:0x00ed, code skipped:
            r21.zzIO.zzgK();
     */
    /* JADX WARNING: Missing block: B:59:0x0130, code skipped:
            r3 = th;
     */
    /* JADX WARNING: Missing block: B:60:0x0131, code skipped:
            r4 = null;
     */
    /* JADX WARNING: Missing block: B:62:?, code skipped:
            com.google.android.gms.internal.zzna.zzb(r4);
     */
    /* JADX WARNING: Missing block: B:63:0x0135, code skipped:
            throw r3;
     */
    /* JADX WARNING: Missing block: B:70:0x0152, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("No location header to follow redirect.");
            r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel(0);
     */
    /* JADX WARNING: Missing block: B:72:?, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:73:0x0160, code skipped:
            if (r21 == null) goto L_0x0169;
     */
    /* JADX WARNING: Missing block: B:74:0x0162, code skipped:
            r21.zzIO.zzgK();
     */
    /* JADX WARNING: Missing block: B:79:0x0175, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Too many redirects.");
            r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel(0);
     */
    /* JADX WARNING: Missing block: B:81:?, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:82:0x0183, code skipped:
            if (r21 == null) goto L_0x018c;
     */
    /* JADX WARNING: Missing block: B:83:0x0185, code skipped:
            r21.zzIO.zzgK();
     */
    /* JADX WARNING: Missing block: B:86:?, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Received error HTTP response code: " + r9);
            r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel(0);
     */
    /* JADX WARNING: Missing block: B:88:?, code skipped:
            r2.disconnect();
     */
    /* JADX WARNING: Missing block: B:89:0x01ae, code skipped:
            if (r21 == null) goto L_0x01b7;
     */
    /* JADX WARNING: Missing block: B:90:0x01b0, code skipped:
            r21.zzIO.zzgK();
     */
    /* JADX WARNING: Missing block: B:99:0x01cd, code skipped:
            r3 = th;
     */
    /* JADX WARNING: Missing block: B:106:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:108:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:109:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:110:?, code skipped:
            return r3;
     */
    public static com.google.android.gms.ads.internal.request.AdResponseParcel zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel r13, android.content.Context r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, com.google.android.gms.internal.zzhi r19, com.google.android.gms.internal.zzcb r20, com.google.android.gms.internal.zzhc r21) {
        /*
        if (r20 == 0) goto L_0x00f6;
    L_0x0002:
        r2 = r20.zzdB();
        r3 = r2;
    L_0x0007:
        r8 = new com.google.android.gms.internal.zzhg;	 Catch:{ IOException -> 0x010e }
        r8.m3293init(r13);	 Catch:{ IOException -> 0x010e }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x010e }
        r2.<init>();	 Catch:{ IOException -> 0x010e }
        r4 = "AdRequestServiceImpl: Sending request: ";
        r2 = r2.append(r4);	 Catch:{ IOException -> 0x010e }
        r0 = r16;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x010e }
        r2 = r2.toString();	 Catch:{ IOException -> 0x010e }
        com.google.android.gms.ads.internal.util.client.zzb.zzaI(r2);	 Catch:{ IOException -> 0x010e }
        r4 = new java.net.URL;	 Catch:{ IOException -> 0x010e }
        r0 = r16;
        r4.<init>(r0);	 Catch:{ IOException -> 0x010e }
        r2 = 0;
        r5 = com.google.android.gms.ads.internal.zzr.zzbG();	 Catch:{ IOException -> 0x010e }
        r10 = r5.elapsedRealtime();	 Catch:{ IOException -> 0x010e }
        r6 = r2;
        r7 = r4;
    L_0x0036:
        if (r21 == 0) goto L_0x003f;
    L_0x0038:
        r0 = r21;
        r2 = r0.zzIO;	 Catch:{ IOException -> 0x010e }
        r2.zzgJ();	 Catch:{ IOException -> 0x010e }
    L_0x003f:
        r2 = r7.openConnection();	 Catch:{ IOException -> 0x010e }
        r2 = (java.net.HttpURLConnection) r2;	 Catch:{ IOException -> 0x010e }
        r4 = com.google.android.gms.ads.internal.zzr.zzbC();	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r4.zza(r14, r15, r5, r2);	 Catch:{ all -> 0x0100 }
        r4 = android.text.TextUtils.isEmpty(r17);	 Catch:{ all -> 0x0100 }
        if (r4 != 0) goto L_0x005a;
    L_0x0053:
        r4 = "x-afma-drt-cookie";
        r0 = r17;
        r2.addRequestProperty(r4, r0);	 Catch:{ all -> 0x0100 }
    L_0x005a:
        r4 = android.text.TextUtils.isEmpty(r18);	 Catch:{ all -> 0x0100 }
        if (r4 != 0) goto L_0x007a;
    L_0x0060:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0100 }
        r4.<init>();	 Catch:{ all -> 0x0100 }
        r5 = "Bearer ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0100 }
        r0 = r18;
        r4 = r4.append(r0);	 Catch:{ all -> 0x0100 }
        r4 = r4.toString();	 Catch:{ all -> 0x0100 }
        r5 = "Authorization";
        r2.addRequestProperty(r5, r4);	 Catch:{ all -> 0x0100 }
    L_0x007a:
        if (r19 == 0) goto L_0x00a6;
    L_0x007c:
        r4 = r19.zzgF();	 Catch:{ all -> 0x0100 }
        r4 = android.text.TextUtils.isEmpty(r4);	 Catch:{ all -> 0x0100 }
        if (r4 != 0) goto L_0x00a6;
    L_0x0086:
        r4 = 1;
        r2.setDoOutput(r4);	 Catch:{ all -> 0x0100 }
        r4 = r19.zzgF();	 Catch:{ all -> 0x0100 }
        r9 = r4.getBytes();	 Catch:{ all -> 0x0100 }
        r4 = r9.length;	 Catch:{ all -> 0x0100 }
        r2.setFixedLengthStreamingMode(r4);	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r4 = new java.io.BufferedOutputStream;	 Catch:{ all -> 0x00fa }
        r12 = r2.getOutputStream();	 Catch:{ all -> 0x00fa }
        r4.<init>(r12);	 Catch:{ all -> 0x00fa }
        r4.write(r9);	 Catch:{ all -> 0x01d0 }
        com.google.android.gms.internal.zzna.zzb(r4);	 Catch:{ all -> 0x0100 }
    L_0x00a6:
        r9 = r2.getResponseCode();	 Catch:{ all -> 0x0100 }
        r12 = r2.getHeaderFields();	 Catch:{ all -> 0x0100 }
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r9 < r4) goto L_0x0136;
    L_0x00b2:
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r9 >= r4) goto L_0x0136;
    L_0x00b6:
        r6 = r7.toString();	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r4 = new java.io.InputStreamReader;	 Catch:{ all -> 0x0130 }
        r7 = r2.getInputStream();	 Catch:{ all -> 0x0130 }
        r4.<init>(r7);	 Catch:{ all -> 0x0130 }
        r5 = com.google.android.gms.ads.internal.zzr.zzbC();	 Catch:{ all -> 0x01cd }
        r5 = r5.zza(r4);	 Catch:{ all -> 0x01cd }
        com.google.android.gms.internal.zzna.zzb(r4);	 Catch:{ all -> 0x0100 }
        zza(r6, r12, r5, r9);	 Catch:{ all -> 0x0100 }
        r8.zzb(r6, r12, r5);	 Catch:{ all -> 0x0100 }
        if (r20 == 0) goto L_0x00e4;
    L_0x00d7:
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ all -> 0x0100 }
        r5 = 0;
        r6 = "ufe";
        r4[r5] = r6;	 Catch:{ all -> 0x0100 }
        r0 = r20;
        r0.zza(r3, r4);	 Catch:{ all -> 0x0100 }
    L_0x00e4:
        r3 = r8.zzj(r10);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x00f4;
    L_0x00ed:
        r0 = r21;
        r2 = r0.zzIO;	 Catch:{ IOException -> 0x010e }
        r2.zzgK();	 Catch:{ IOException -> 0x010e }
    L_0x00f4:
        r2 = r3;
    L_0x00f5:
        return r2;
    L_0x00f6:
        r2 = 0;
        r3 = r2;
        goto L_0x0007;
    L_0x00fa:
        r3 = move-exception;
        r4 = r5;
    L_0x00fc:
        com.google.android.gms.internal.zzna.zzb(r4);	 Catch:{ all -> 0x0100 }
        throw r3;	 Catch:{ all -> 0x0100 }
    L_0x0100:
        r3 = move-exception;
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x010d;
    L_0x0106:
        r0 = r21;
        r2 = r0.zzIO;	 Catch:{ IOException -> 0x010e }
        r2.zzgK();	 Catch:{ IOException -> 0x010e }
    L_0x010d:
        throw r3;	 Catch:{ IOException -> 0x010e }
    L_0x010e:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Error while connecting to ad server: ";
        r3 = r3.append(r4);
        r2 = r2.getMessage();
        r2 = r3.append(r2);
        r2 = r2.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r2);
        r2 = new com.google.android.gms.ads.internal.request.AdResponseParcel;
        r3 = 2;
        r2.m2387init(r3);
        goto L_0x00f5;
    L_0x0130:
        r3 = move-exception;
        r4 = r5;
    L_0x0132:
        com.google.android.gms.internal.zzna.zzb(r4);	 Catch:{ all -> 0x0100 }
        throw r3;	 Catch:{ all -> 0x0100 }
    L_0x0136:
        r4 = r7.toString();	 Catch:{ all -> 0x0100 }
        r5 = 0;
        zza(r4, r12, r5, r9);	 Catch:{ all -> 0x0100 }
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r9 < r4) goto L_0x018f;
    L_0x0142:
        r4 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r9 >= r4) goto L_0x018f;
    L_0x0146:
        r4 = "Location";
        r4 = r2.getHeaderField(r4);	 Catch:{ all -> 0x0100 }
        r5 = android.text.TextUtils.isEmpty(r4);	 Catch:{ all -> 0x0100 }
        if (r5 == 0) goto L_0x016b;
    L_0x0152:
        r3 = "No location header to follow redirect.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r3);	 Catch:{ all -> 0x0100 }
        r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0100 }
        r4 = 0;
        r3.m2387init(r4);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x0169;
    L_0x0162:
        r0 = r21;
        r2 = r0.zzIO;	 Catch:{ IOException -> 0x010e }
        r2.zzgK();	 Catch:{ IOException -> 0x010e }
    L_0x0169:
        r2 = r3;
        goto L_0x00f5;
    L_0x016b:
        r5 = new java.net.URL;	 Catch:{ all -> 0x0100 }
        r5.<init>(r4);	 Catch:{ all -> 0x0100 }
        r4 = r6 + 1;
        r6 = 5;
        if (r4 <= r6) goto L_0x01ba;
    L_0x0175:
        r3 = "Too many redirects.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r3);	 Catch:{ all -> 0x0100 }
        r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0100 }
        r4 = 0;
        r3.m2387init(r4);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x018c;
    L_0x0185:
        r0 = r21;
        r2 = r0.zzIO;	 Catch:{ IOException -> 0x010e }
        r2.zzgK();	 Catch:{ IOException -> 0x010e }
    L_0x018c:
        r2 = r3;
        goto L_0x00f5;
    L_0x018f:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0100 }
        r3.<init>();	 Catch:{ all -> 0x0100 }
        r4 = "Received error HTTP response code: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0100 }
        r3 = r3.append(r9);	 Catch:{ all -> 0x0100 }
        r3 = r3.toString();	 Catch:{ all -> 0x0100 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r3);	 Catch:{ all -> 0x0100 }
        r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0100 }
        r4 = 0;
        r3.m2387init(r4);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x01b7;
    L_0x01b0:
        r0 = r21;
        r2 = r0.zzIO;	 Catch:{ IOException -> 0x010e }
        r2.zzgK();	 Catch:{ IOException -> 0x010e }
    L_0x01b7:
        r2 = r3;
        goto L_0x00f5;
    L_0x01ba:
        r8.zzj(r12);	 Catch:{ all -> 0x0100 }
        r2.disconnect();	 Catch:{ IOException -> 0x010e }
        if (r21 == 0) goto L_0x01c9;
    L_0x01c2:
        r0 = r21;
        r2 = r0.zzIO;	 Catch:{ IOException -> 0x010e }
        r2.zzgK();	 Catch:{ IOException -> 0x010e }
    L_0x01c9:
        r6 = r4;
        r7 = r5;
        goto L_0x0036;
    L_0x01cd:
        r3 = move-exception;
        goto L_0x0132;
    L_0x01d0:
        r3 = move-exception;
        goto L_0x00fc;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzhd.zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel, android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.google.android.gms.internal.zzhi, com.google.android.gms.internal.zzcb, com.google.android.gms.internal.zzhc):com.google.android.gms.ads.internal.request.AdResponseParcel");
    }

    public static zzhd zza(Context context, zzbm zzbm, zzhc zzhc) {
        zzhd zzhd;
        synchronized (zzqy) {
            if (zzIQ == null) {
                if (context.getApplicationContext() != null) {
                    context = context.getApplicationContext();
                }
                zzIQ = new zzhd(context, zzbm, zzhc);
            }
            zzhd = zzIQ;
        }
        return zzhd;
    }

    /* access modifiers changed from: private|static */
    public static zzjq.zza zza(final String str, final zzcb zzcb, final zzbz zzbz) {
        return new zzjq.zza() {
            public void zza(zzjp zzjp, boolean z) {
                zzcb.zza(zzbz, "jsf");
                zzcb.zzdC();
                zzjp.zze("AFMA_buildAdURL", str);
            }
        };
    }

    private static void zza(String str, Map<String, List<String>> map, String str2, int i) {
        if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
            zzin.v("Http Response: {\n  URL:\n    " + str + "\n  Headers:");
            if (map != null) {
                for (String str3 : map.keySet()) {
                    zzin.v("    " + str3 + ":");
                    for (String str32 : (List) map.get(str32)) {
                        zzin.v("      " + str32);
                    }
                }
            }
            zzin.v("  Body:");
            if (str2 != null) {
                for (int i2 = 0; i2 < Math.min(str2.length(), 100000); i2 += 1000) {
                    zzin.v(str2.substring(i2, Math.min(str2.length(), i2 + 1000)));
                }
            } else {
                zzin.v("    null");
            }
            zzin.v("  Response Code:\n    " + i + "\n}");
        }
    }

    public void zza(final AdRequestInfoParcel adRequestInfoParcel, final zzk zzk) {
        zzr.zzbF().zzb(this.mContext, adRequestInfoParcel.zzrl);
        zziq.zza(new Runnable() {
            public void run() {
                AdResponseParcel zzd;
                try {
                    zzd = zzhd.this.zzd(adRequestInfoParcel);
                } catch (Exception e) {
                    zzr.zzbF().zzb(e, true);
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response due to an Exception.", e);
                    zzd = null;
                }
                if (zzd == null) {
                    zzd = new AdResponseParcel(0);
                }
                try {
                    zzk.zzb(zzd);
                } catch (RemoteException e2) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to forward ad response.", e2);
                }
            }
        });
    }

    public AdResponseParcel zzd(AdRequestInfoParcel adRequestInfoParcel) {
        return zza(this.mContext, this.zzIT, this.zzIS, this.zzIR, adRequestInfoParcel);
    }
}
