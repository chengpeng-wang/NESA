package com.google.android.gms.internal;

import android.content.Context;
import android.os.Build.VERSION;
import com.google.android.gms.ads.internal.zzp;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.LinkedHashMap;
import java.util.Map;

public class zzbz {
    private Context mContext = null;
    private String zzqK = null;
    private boolean zzvf;
    private String zzvg;
    private Map<String, String> zzvh;

    public zzbz(Context context, String str) {
        this.mContext = context;
        this.zzqK = str;
        this.zzvf = ((Boolean) zzby.zzuB.get()).booleanValue();
        this.zzvg = (String) zzby.zzuC.get();
        this.zzvh = new LinkedHashMap();
        this.zzvh.put("s", "gmob_sdk");
        this.zzvh.put("v", "3");
        this.zzvh.put("os", VERSION.RELEASE);
        this.zzvh.put("sdk", VERSION.SDK);
        this.zzvh.put("device", zzp.zzbx().zzgt());
        this.zzvh.put(MASTNativeAdConstants.REQUESTPARAM_UA, zzp.zzbx().zzf(context, str));
        this.zzvh.put("app", context.getApplicationContext() != null ? context.getApplicationContext().getPackageName() : context.getPackageName());
        if (zzp.zzbx().zza(context.getPackageManager(), context.getPackageName(), "android.permission.ACCESS_NETWORK_STATE")) {
            zzgr zzD = zzp.zzbD().zzD(this.mContext);
            this.zzvh.put("network_coarse", Integer.toString(zzD.zzFN));
            this.zzvh.put("network_fine", Integer.toString(zzD.zzFO));
        }
    }

    /* access modifiers changed from: 0000 */
    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: 0000 */
    public String zzbV() {
        return this.zzqK;
    }

    /* access modifiers changed from: 0000 */
    public boolean zzdf() {
        return this.zzvf;
    }

    /* access modifiers changed from: 0000 */
    public String zzdg() {
        return this.zzvg;
    }

    /* access modifiers changed from: 0000 */
    public Map<String, String> zzdh() {
        return this.zzvh;
    }
}
