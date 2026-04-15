package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import org.json.JSONObject;

@zzhb
public class zzaz extends zzau {
    private final zzeh zzsE;

    public zzaz(Context context, AdSizeParcel adSizeParcel, zzif zzif, VersionInfoParcel versionInfoParcel, zzbb zzbb, zzeh zzeh) {
        super(context, adSizeParcel, zzif, versionInfoParcel, zzbb);
        this.zzsE = zzeh;
        zzb(this.zzsE);
        zzcd();
        zzh(false);
        zzb.zzaI("Tracking ad unit: " + this.zzrZ.zzcu());
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        synchronized (this.zzpV) {
            super.destroy();
            zzc(this.zzsE);
        }
    }

    /* access modifiers changed from: protected */
    public void zzb(JSONObject jSONObject) {
        this.zzsE.zza("AFMA_updateActiveView", jSONObject);
    }

    public void zzcf() {
        destroy();
    }

    /* access modifiers changed from: protected */
    public boolean zzcl() {
        return true;
    }
}
