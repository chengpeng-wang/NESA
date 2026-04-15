package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.formats.zzc;
import com.google.android.gms.ads.internal.formats.zzf;
import com.google.android.gms.ads.internal.formats.zzi;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzgw implements Callable<zzif> {
    private static final long zzGF = TimeUnit.SECONDS.toMillis(60);
    private final Context mContext;
    private final zziw zzGG;
    /* access modifiers changed from: private|final */
    public final zzp zzGH;
    private final zzee zzGI;
    private boolean zzGJ;
    private List<String> zzGK;
    private JSONObject zzGL;
    private final com.google.android.gms.internal.zzif.zza zzGd;
    private int zzGu;
    private final Object zzpV = new Object();
    private final zzan zzyt;

    public interface zza<T extends com.google.android.gms.ads.internal.formats.zzh.zza> {
        T zza(zzgw zzgw, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException;
    }

    class zzb {
        public zzdf zzHb;

        zzb() {
        }
    }

    public zzgw(Context context, zzp zzp, zzee zzee, zziw zziw, zzan zzan, com.google.android.gms.internal.zzif.zza zza) {
        this.mContext = context;
        this.zzGH = zzp;
        this.zzGG = zziw;
        this.zzGI = zzee;
        this.zzGd = zza;
        this.zzyt = zzan;
        this.zzGJ = false;
        this.zzGu = -2;
        this.zzGK = null;
    }

    private com.google.android.gms.ads.internal.formats.zzh.zza zza(zzed zzed, zza zza, JSONObject jSONObject) throws ExecutionException, InterruptedException, JSONException {
        if (zzgn()) {
            return null;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("tracking_urls_and_actions");
        String[] zzc = zzc(jSONObject2, "impression_tracking_urls");
        this.zzGK = zzc == null ? null : Arrays.asList(zzc);
        this.zzGL = jSONObject2.optJSONObject("active_view");
        com.google.android.gms.ads.internal.formats.zzh.zza zza2 = zza.zza(this, jSONObject);
        if (zza2 == null) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Failed to retrieve ad assets.");
            return null;
        }
        zza2.zzb(new zzi(this.mContext, this.zzGH, zzed, this.zzyt, jSONObject, zza2, this.zzGd.zzLd.zzrl));
        return zza2;
    }

    private zzif zza(com.google.android.gms.ads.internal.formats.zzh.zza zza) {
        int i;
        synchronized (this.zzpV) {
            i = this.zzGu;
            if (zza == null && this.zzGu == -2) {
                i = 0;
            }
        }
        return new zzif(this.zzGd.zzLd.zzHt, null, this.zzGd.zzLe.zzBQ, i, this.zzGd.zzLe.zzBR, this.zzGK, this.zzGd.zzLe.orientation, this.zzGd.zzLe.zzBU, this.zzGd.zzLd.zzHw, false, null, null, null, null, null, 0, this.zzGd.zzrp, this.zzGd.zzLe.zzHS, this.zzGd.zzKY, this.zzGd.zzKZ, this.zzGd.zzLe.zzHY, this.zzGL, i != -2 ? null : zza, null, null, null, this.zzGd.zzLe.zzIm);
    }

    private zzjg<zzc> zza(JSONObject jSONObject, boolean z, boolean z2) throws JSONException {
        final CharSequence string = z ? jSONObject.getString("url") : jSONObject.optString("url");
        final double optDouble = jSONObject.optDouble("scale", 1.0d);
        if (TextUtils.isEmpty(string)) {
            zza(0, z);
            return new zzje(null);
        } else if (z2) {
            return new zzje(new zzc(null, Uri.parse(string), optDouble));
        } else {
            final boolean z3 = z;
            return this.zzGG.zza(string, new com.google.android.gms.internal.zziw.zza<zzc>() {
                /* renamed from: zzg */
                public zzc zzh(InputStream inputStream) {
                    byte[] zzk;
                    try {
                        zzk = zzna.zzk(inputStream);
                    } catch (IOException e) {
                        zzk = null;
                    }
                    if (zzk == null) {
                        zzgw.this.zza(2, z3);
                        return null;
                    }
                    Bitmap decodeByteArray = BitmapFactory.decodeByteArray(zzk, 0, zzk.length);
                    if (decodeByteArray == null) {
                        zzgw.this.zza(2, z3);
                        return null;
                    }
                    decodeByteArray.setDensity((int) (160.0d * optDouble));
                    return new zzc(new BitmapDrawable(Resources.getSystem(), decodeByteArray), Uri.parse(string), optDouble);
                }

                /* renamed from: zzgo */
                public zzc zzgp() {
                    zzgw.this.zza(2, z3);
                    return null;
                }
            });
        }
    }

    private void zza(com.google.android.gms.ads.internal.formats.zzh.zza zza, zzed zzed) {
        if (zza instanceof zzf) {
            final zzf zzf = (zzf) zza;
            zzb zzb = new zzb();
            AnonymousClass3 anonymousClass3 = new zzdf() {
                public void zza(zzjp zzjp, Map<String, String> map) {
                    zzgw.this.zzb(zzf, (String) map.get("asset"));
                }
            };
            zzb.zzHb = anonymousClass3;
            zzed.zza("/nativeAdCustomClick", (zzdf) anonymousClass3);
        }
    }

    private Integer zzb(JSONObject jSONObject, String str) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject(str);
            return Integer.valueOf(Color.rgb(jSONObject2.getInt("r"), jSONObject2.getInt("g"), jSONObject2.getInt("b")));
        } catch (JSONException e) {
            return null;
        }
    }

    private JSONObject zzb(final zzed zzed) throws TimeoutException, JSONException {
        if (zzgn()) {
            return null;
        }
        final zzjd zzjd = new zzjd();
        final zzb zzb = new zzb();
        AnonymousClass1 anonymousClass1 = new zzdf() {
            public void zza(zzjp zzjp, Map<String, String> map) {
                zzed.zzb("/nativeAdPreProcess", zzb.zzHb);
                try {
                    String str = (String) map.get("success");
                    if (!TextUtils.isEmpty(str)) {
                        zzjd.zzg(new JSONObject(str).getJSONArray("ads").getJSONObject(0));
                        return;
                    }
                } catch (JSONException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Malformed native JSON response.", e);
                }
                zzgw.this.zzF(0);
                zzx.zza(zzgw.this.zzgn(), (Object) "Unable to set the ad state error!");
                zzjd.zzg(null);
            }
        };
        zzb.zzHb = anonymousClass1;
        zzed.zza("/nativeAdPreProcess", (zzdf) anonymousClass1);
        zzed.zza("google.afma.nativeAds.preProcessJsonGmsg", new JSONObject(this.zzGd.zzLe.body));
        return (JSONObject) zzjd.get(zzGF, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: private */
    public void zzb(zzcp zzcp, String str) {
        try {
            zzct zzs = this.zzGH.zzs(zzcp.getCustomTemplateId());
            if (zzs != null) {
                zzs.zza(zzcp, str);
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to call onCustomClick for asset " + str + ".", e);
        }
    }

    private String[] zzc(JSONObject jSONObject, String str) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        String[] strArr = new String[optJSONArray.length()];
        for (int i = 0; i < optJSONArray.length(); i++) {
            strArr[i] = optJSONArray.getString(i);
        }
        return strArr;
    }

    /* access modifiers changed from: private|static */
    public static List<Drawable> zzf(List<zzc> list) throws RemoteException {
        ArrayList arrayList = new ArrayList();
        for (zzc zzdJ : list) {
            arrayList.add((Drawable) zze.zzp(zzdJ.zzdJ()));
        }
        return arrayList;
    }

    private zzed zzgm() throws CancellationException, ExecutionException, InterruptedException, TimeoutException {
        if (zzgn()) {
            return null;
        }
        zzed zzed = (zzed) this.zzGI.zza(this.mContext, this.zzGd.zzLd.zzrl, (this.zzGd.zzLe.zzEF.indexOf("https") == 0 ? "https:" : "http:") + ((String) zzbt.zzwC.get()), this.zzyt).get(zzGF, TimeUnit.MILLISECONDS);
        zzed.zza(this.zzGH, this.zzGH, this.zzGH, this.zzGH, false, null, null, null, null);
        return zzed;
    }

    public void zzF(int i) {
        synchronized (this.zzpV) {
            this.zzGJ = true;
            this.zzGu = i;
        }
    }

    public zzjg<zzc> zza(JSONObject jSONObject, String str, boolean z, boolean z2) throws JSONException {
        JSONObject jSONObject2 = z ? jSONObject.getJSONObject(str) : jSONObject.optJSONObject(str);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        return zza(jSONObject2, z, z2);
    }

    public List<zzjg<zzc>> zza(JSONObject jSONObject, String str, boolean z, boolean z2, boolean z3) throws JSONException {
        JSONArray jSONArray = z ? jSONObject.getJSONArray(str) : jSONObject.optJSONArray(str);
        ArrayList arrayList = new ArrayList();
        if (jSONArray == null || jSONArray.length() == 0) {
            zza(0, z);
            return arrayList;
        }
        int length = z3 ? jSONArray.length() : 1;
        for (int i = 0; i < length; i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            if (jSONObject2 == null) {
                jSONObject2 = new JSONObject();
            }
            arrayList.add(zza(jSONObject2, z, z2));
        }
        return arrayList;
    }

    public Future<zzc> zza(JSONObject jSONObject, String str, boolean z) throws JSONException {
        JSONObject jSONObject2 = jSONObject.getJSONObject(str);
        boolean optBoolean = jSONObject2.optBoolean("require", true);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        return zza(jSONObject2, optBoolean, z);
    }

    public void zza(int i, boolean z) {
        if (z) {
            zzF(i);
        }
    }

    /* access modifiers changed from: protected */
    public zza zze(JSONObject jSONObject) throws JSONException, TimeoutException {
        if (zzgn()) {
            return null;
        }
        String string = jSONObject.getString("template_id");
        boolean z = this.zzGd.zzLd.zzrD != null ? this.zzGd.zzLd.zzrD.zzyA : false;
        boolean z2 = this.zzGd.zzLd.zzrD != null ? this.zzGd.zzLd.zzrD.zzyC : false;
        if ("2".equals(string)) {
            return new zzgx(z, z2);
        }
        if ("1".equals(string)) {
            return new zzgy(z, z2);
        }
        if ("3".equals(string)) {
            final String string2 = jSONObject.getString("custom_template_id");
            final zzjd zzjd = new zzjd();
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    zzjd.zzg(zzgw.this.zzGH.zzbv().get(string2));
                }
            });
            if (zzjd.get(zzGF, TimeUnit.MILLISECONDS) != null) {
                return new zzgz(z);
            }
            com.google.android.gms.ads.internal.util.client.zzb.e("No handler for custom template: " + jSONObject.getString("custom_template_id"));
        } else {
            zzF(0);
        }
        return null;
    }

    public zzjg<com.google.android.gms.ads.internal.formats.zza> zzf(JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("attribution");
        if (optJSONObject == null) {
            return new zzje(null);
        }
        String optString = optJSONObject.optString("text");
        int optInt = optJSONObject.optInt("text_size", -1);
        Integer zzb = zzb(optJSONObject, "text_color");
        Integer zzb2 = zzb(optJSONObject, "bg_color");
        final int optInt2 = optJSONObject.optInt("animation_ms", 1000);
        final int optInt3 = optJSONObject.optInt("presentation_ms", 4000);
        List arrayList = new ArrayList();
        if (optJSONObject.optJSONArray("images") != null) {
            arrayList = zza(optJSONObject, "images", false, false, true);
        } else {
            arrayList.add(zza(optJSONObject, "image", false, false));
        }
        final String str = optString;
        final Integer num = zzb2;
        final Integer num2 = zzb;
        final int i = optInt;
        return zzjf.zza(zzjf.zzl(arrayList), new com.google.android.gms.internal.zzjf.zza<List<zzc>, com.google.android.gms.ads.internal.formats.zza>() {
            /* renamed from: zzh */
            public com.google.android.gms.ads.internal.formats.zza zzf(List<zzc> list) {
                com.google.android.gms.ads.internal.formats.zza zza;
                if (list != null) {
                    try {
                        if (!list.isEmpty()) {
                            zza = new com.google.android.gms.ads.internal.formats.zza(str, zzgw.zzf((List) list), num, num2, i > 0 ? Integer.valueOf(i) : null, optInt3 + optInt2);
                            return zza;
                        }
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzb("Could not get attribution icon", e);
                        return null;
                    }
                }
                zza = null;
                return zza;
            }
        });
    }

    /* renamed from: zzgl */
    public zzif call() {
        try {
            zzed zzgm = zzgm();
            JSONObject zzb = zzb(zzgm);
            com.google.android.gms.ads.internal.formats.zzh.zza zza = zza(zzgm, zze(zzb), zzb);
            zza(zza, zzgm);
            return zza(zza);
        } catch (InterruptedException | CancellationException | ExecutionException e) {
        } catch (JSONException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Malformed native JSON response.", e2);
        } catch (TimeoutException e3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Timeout when loading native ad.", e3);
        }
        if (!this.zzGJ) {
            zzF(0);
        }
        return zza(null);
    }

    public boolean zzgn() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzGJ;
        }
        return z;
    }
}
