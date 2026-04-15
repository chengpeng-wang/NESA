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
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.zzn;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.Constants;
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

@zzgk
public class zzgf implements Callable<zzhj> {
    private static final long zzCS = TimeUnit.SECONDS.toMillis(60);
    private final Context mContext;
    private final com.google.android.gms.internal.zzhj.zza zzCF;
    private int zzCI;
    private final zzhy zzCT;
    /* access modifiers changed from: private|final */
    public final zzn zzCU;
    private final zzbc zzCV;
    private boolean zzCW;
    private List<String> zzCX;
    private final Object zzpc = new Object();
    private final zzan zzwh;

    public interface zza<T extends com.google.android.gms.ads.internal.formats.zzh.zza> {
        T zza(zzgf zzgf, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException;
    }

    class zzb {
        public zzdg zzDk;

        zzb() {
        }
    }

    public zzgf(Context context, zzn zzn, zzbc zzbc, zzhy zzhy, zzan zzan, com.google.android.gms.internal.zzhj.zza zza) {
        this.mContext = context;
        this.zzCU = zzn;
        this.zzCT = zzhy;
        this.zzCV = zzbc;
        this.zzCF = zza;
        this.zzwh = zzan;
        this.zzCW = false;
        this.zzCI = -2;
        this.zzCX = null;
    }

    private com.google.android.gms.ads.internal.formats.zzh.zza zza(zzbb zzbb, zza zza, JSONObject jSONObject) throws ExecutionException, InterruptedException, JSONException {
        if (zzft()) {
            return null;
        }
        String[] zzc = zzc(jSONObject.getJSONObject("tracking_urls_and_actions"), "impression_tracking_urls");
        this.zzCX = zzc == null ? null : Arrays.asList(zzc);
        com.google.android.gms.ads.internal.formats.zzh.zza zza2 = zza.zza(this, jSONObject);
        if (zza2 == null) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Failed to retrieve ad assets.");
            return null;
        }
        zza2.zza(new zzh(this.mContext, this.zzCU, zzbb, this.zzwh, jSONObject, zza2, this.zzCF.zzGL.zzqb));
        return zza2;
    }

    private zzhj zza(com.google.android.gms.ads.internal.formats.zzh.zza zza) {
        int i;
        synchronized (this.zzpc) {
            i = this.zzCI;
            if (zza == null && this.zzCI == -2) {
                i = 0;
            }
        }
        return new zzhj(this.zzCF.zzGL.zzDy, null, this.zzCF.zzGM.zzyw, i, this.zzCF.zzGM.zzyx, this.zzCX, this.zzCF.zzGM.orientation, this.zzCF.zzGM.zzyA, this.zzCF.zzGL.zzDB, false, null, null, null, null, null, 0, this.zzCF.zzqf, this.zzCF.zzGM.zzDW, this.zzCF.zzGI, this.zzCF.zzGJ, this.zzCF.zzGM.zzEc, this.zzCF.zzGF, i != -2 ? null : zza, this.zzCF.zzGL.zzDO);
    }

    private zzih<zzc> zza(JSONObject jSONObject, final boolean z, boolean z2) throws JSONException {
        final CharSequence string = z ? jSONObject.getString(MASTNativeAdConstants.RESPONSE_URL) : jSONObject.optString(MASTNativeAdConstants.RESPONSE_URL);
        if (!TextUtils.isEmpty(string)) {
            return z2 ? new zzif(new zzc(null, Uri.parse(string))) : this.zzCT.zza(string, new com.google.android.gms.internal.zzhy.zza<zzc>() {
                /* renamed from: zzfu */
                public zzc zzfv() {
                    zzgf.this.zza(2, z);
                    return null;
                }

                /* renamed from: zzg */
                public zzc zzh(InputStream inputStream) {
                    byte[] zzk;
                    try {
                        zzk = zzlr.zzk(inputStream);
                    } catch (IOException e) {
                        zzk = null;
                    }
                    if (zzk == null) {
                        zzgf.this.zza(2, z);
                        return null;
                    }
                    Bitmap decodeByteArray = BitmapFactory.decodeByteArray(zzk, 0, zzk.length);
                    if (decodeByteArray != null) {
                        return new zzc(new BitmapDrawable(Resources.getSystem(), decodeByteArray), Uri.parse(string));
                    }
                    zzgf.this.zza(2, z);
                    return null;
                }
            });
        } else {
            zza(0, z);
            return new zzif(null);
        }
    }

    private void zza(com.google.android.gms.ads.internal.formats.zzh.zza zza, zzbb zzbb) {
        if (zza instanceof zzf) {
            final zzf zzf = (zzf) zza;
            zzb zzb = new zzb();
            AnonymousClass3 anonymousClass3 = new zzdg() {
                public void zza(zzip zzip, Map<String, String> map) {
                    zzgf.this.zzb(zzf, (String) map.get("asset"));
                }
            };
            zzb.zzDk = anonymousClass3;
            zzbb.zza("/nativeAdCustomClick", (zzdg) anonymousClass3);
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

    private JSONObject zzb(final zzbb zzbb) throws TimeoutException, JSONException {
        if (zzft()) {
            return null;
        }
        final zzie zzie = new zzie();
        final zzb zzb = new zzb();
        AnonymousClass1 anonymousClass1 = new zzdg() {
            public void zza(zzip zzip, Map<String, String> map) {
                zzbb.zzb("/nativeAdPreProcess", zzb.zzDk);
                try {
                    String str = (String) map.get("success");
                    if (!TextUtils.isEmpty(str)) {
                        zzie.zzf(new JSONObject(str).getJSONArray(MASTNativeAdConstants.RESPONSE_ADS).getJSONObject(0));
                        return;
                    }
                } catch (JSONException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Malformed native JSON response.", e);
                }
                zzgf.this.zzB(0);
                zzx.zza(zzgf.this.zzft(), "Unable to set the ad state error!");
                zzie.zzf(null);
            }
        };
        zzb.zzDk = anonymousClass1;
        zzbb.zza("/nativeAdPreProcess", (zzdg) anonymousClass1);
        zzbb.zza("google.afma.nativeAds.preProcessJsonGmsg", new JSONObject(this.zzCF.zzGM.body));
        return (JSONObject) zzie.get(zzCS, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: private */
    public void zzb(zzcr zzcr, String str) {
        try {
            zzcv zzr = this.zzCU.zzr(zzcr.getCustomTemplateId());
            if (zzr != null) {
                zzr.zza(zzcr, str);
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

    private zzbb zzfs() throws CancellationException, ExecutionException, InterruptedException, TimeoutException {
        if (zzft()) {
            return null;
        }
        zzbb zzbb = (zzbb) this.zzCV.zza(this.mContext, this.zzCF.zzGL.zzqb, (this.zzCF.zzGM.zzAT.indexOf(Constants.HTTPS) == 0 ? "https:" : "http:") + ((String) zzby.zzuU.get()), this.zzwh).get(zzCS, TimeUnit.MILLISECONDS);
        zzbb.zza(this.zzCU, this.zzCU, this.zzCU, this.zzCU, false, null, null, null, null);
        return zzbb;
    }

    public void zzB(int i) {
        synchronized (this.zzpc) {
            this.zzCW = true;
            this.zzCI = i;
        }
    }

    public zzih<zzc> zza(JSONObject jSONObject, String str, boolean z, boolean z2) throws JSONException {
        JSONObject jSONObject2 = z ? jSONObject.getJSONObject(str) : jSONObject.optJSONObject(str);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        return zza(jSONObject2, z, z2);
    }

    public List<zzih<zzc>> zza(JSONObject jSONObject, String str, boolean z, boolean z2, boolean z3) throws JSONException {
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
            zzB(i);
        }
    }

    /* access modifiers changed from: protected */
    public zza zzd(JSONObject jSONObject) throws JSONException, TimeoutException {
        if (zzft()) {
            return null;
        }
        String string = jSONObject.getString("template_id");
        boolean z = this.zzCF.zzGL.zzqt != null ? this.zzCF.zzGL.zzqt.zzwn : false;
        boolean z2 = this.zzCF.zzGL.zzqt != null ? this.zzCF.zzGL.zzqt.zzwp : false;
        if ("2".equals(string)) {
            return new zzgg(z, z2);
        }
        if ("1".equals(string)) {
            return new zzgh(z, z2);
        }
        if ("3".equals(string)) {
            final String string2 = jSONObject.getString("custom_template_id");
            final zzie zzie = new zzie();
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzie.zzf(zzgf.this.zzCU.zzbq().get(string2));
                }
            });
            if (zzie.get(zzCS, TimeUnit.MILLISECONDS) != null) {
                return new zzgi(z);
            }
            com.google.android.gms.ads.internal.util.client.zzb.e("No handler for custom template: " + jSONObject.getString("custom_template_id"));
        } else {
            zzB(0);
        }
        return null;
    }

    public zzih<com.google.android.gms.ads.internal.formats.zza> zze(JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("attribution");
        if (optJSONObject == null) {
            return new zzif(null);
        }
        final String optString = optJSONObject.optString(MASTNativeAdConstants.RESPONSE_TEXT);
        final int optInt = optJSONObject.optInt("text_size", -1);
        final Integer zzb = zzb(optJSONObject, "text_color");
        final Integer zzb2 = zzb(optJSONObject, "bg_color");
        return zzig.zza(zza(optJSONObject, "image", false, false), new com.google.android.gms.internal.zzig.zza<zzc, com.google.android.gms.ads.internal.formats.zza>() {
            /* renamed from: zza */
            public com.google.android.gms.ads.internal.formats.zza zze(zzc zzc) {
                com.google.android.gms.ads.internal.formats.zza zza;
                if (zzc != null) {
                    try {
                        if (!TextUtils.isEmpty(optString)) {
                            zza = new com.google.android.gms.ads.internal.formats.zza(optString, (Drawable) zze.zzp(zzc.zzdr()), zzb2, zzb, optInt > 0 ? Integer.valueOf(optInt) : null);
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

    /* renamed from: zzfr */
    public zzhj call() {
        try {
            zzbb zzfs = zzfs();
            JSONObject zzb = zzb(zzfs);
            com.google.android.gms.ads.internal.formats.zzh.zza zza = zza(zzfs, zzd(zzb), zzb);
            zza(zza, zzfs);
            return zza(zza);
        } catch (InterruptedException | CancellationException | ExecutionException e) {
        } catch (JSONException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Malformed native JSON response.", e2);
        } catch (TimeoutException e3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Timeout when loading native ad.", e3);
        }
        if (!this.zzCW) {
            zzB(0);
        }
        return zza(null);
    }

    public boolean zzft() {
        boolean z;
        synchronized (this.zzpc) {
            z = this.zzCW;
        }
        return z;
    }
}
