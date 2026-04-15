package com.google.android.gms.ads.internal.purchase;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.SystemClock;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzfl.zza;
import com.google.android.gms.internal.zzgk;
import com.mb.bdapp.db.DuAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzgk
public class zzd extends zza {
    private Context mContext;
    private String zzBW;
    private ArrayList<String> zzBX;
    private String zzqK;

    public zzd(String str, ArrayList<String> arrayList, Context context, String str2) {
        this.zzBW = str;
        this.zzBX = arrayList;
        this.zzqK = str2;
        this.mContext = context;
    }

    public String getProductId() {
        return this.zzBW;
    }

    public void recordPlayBillingResolution(int billingResponseCode) {
        if (billingResponseCode == 0) {
            zzfh();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("google_play_status", String.valueOf(billingResponseCode));
        hashMap.put("sku", this.zzBW);
        hashMap.put(DuAd.DB_STATUS, String.valueOf(zzy(billingResponseCode)));
        List linkedList = new LinkedList();
        Iterator it = this.zzBX.iterator();
        while (it.hasNext()) {
            linkedList.add(zza((String) it.next(), hashMap));
        }
        zzp.zzbx().zza(this.mContext, this.zzqK, linkedList);
    }

    public void recordResolution(int resolution) {
        if (resolution == 1) {
            zzfh();
        }
        HashMap hashMap = new HashMap();
        hashMap.put(DuAd.DB_STATUS, String.valueOf(resolution));
        hashMap.put("sku", this.zzBW);
        List linkedList = new LinkedList();
        Iterator it = this.zzBX.iterator();
        while (it.hasNext()) {
            linkedList.add(zza((String) it.next(), hashMap));
        }
        zzp.zzbx().zza(this.mContext, this.zzqK, linkedList);
    }

    /* access modifiers changed from: protected */
    public String zza(String str, HashMap<String, String> hashMap) {
        Object obj;
        Object obj2 = "";
        try {
            obj = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            zzb.zzd("Error to retrieve app version", e);
            obj = obj2;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime() - zzp.zzbA().zzgb().zzgl();
        for (String str2 : hashMap.keySet()) {
            str = str.replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{str2}), String.format("$1%s$2", new Object[]{hashMap.get(str2)}));
        }
        return str.replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"sessionid"}), String.format("$1%s$2", new Object[]{zzp.zzbA().getSessionId()})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{CMNativeAd.KEY_APP_ID}), String.format("$1%s$2", new Object[]{r2})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"osversion"}), String.format("$1%s$2", new Object[]{String.valueOf(VERSION.SDK_INT)})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"sdkversion"}), String.format("$1%s$2", new Object[]{this.zzqK})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"appversion"}), String.format("$1%s$2", new Object[]{obj})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"timestamp"}), String.format("$1%s$2", new Object[]{String.valueOf(elapsedRealtime)})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"[^@]+"}), String.format("$1%s$2", new Object[]{""})).replaceAll("@@", "@");
    }

    /* access modifiers changed from: 0000 */
    public void zzfh() {
        try {
            this.mContext.getClassLoader().loadClass("com.google.ads.conversiontracking.IAPConversionReporter").getDeclaredMethod("reportWithProductId", new Class[]{Context.class, String.class, String.class, Boolean.TYPE}).invoke(null, new Object[]{this.mContext, this.zzBW, "", Boolean.valueOf(true)});
        } catch (ClassNotFoundException e) {
            zzb.zzaE("Google Conversion Tracking SDK 1.2.0 or above is required to report a conversion.");
        } catch (NoSuchMethodException e2) {
            zzb.zzaE("Google Conversion Tracking SDK 1.2.0 or above is required to report a conversion.");
        } catch (Exception e3) {
            zzb.zzd("Fail to report a conversion.", e3);
        }
    }

    /* access modifiers changed from: protected */
    public int zzy(int i) {
        return i == 0 ? 1 : i == 1 ? 2 : i == 4 ? 3 : 0;
    }
}
