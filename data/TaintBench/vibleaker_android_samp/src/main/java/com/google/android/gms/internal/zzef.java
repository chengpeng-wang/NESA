package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.overlay.zzg;
import com.google.android.gms.ads.internal.overlay.zzp;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.ads.internal.zzr;
import org.json.JSONObject;
import org.springframework.http.MediaType;

@zzhb
public class zzef implements zzed {
    /* access modifiers changed from: private|final */
    public final zzjp zzpD;

    public zzef(Context context, VersionInfoParcel versionInfoParcel, zzan zzan) {
        this.zzpD = zzr.zzbD().zza(context, new AdSizeParcel(), false, false, zzan, versionInfoParcel);
        this.zzpD.getWebView().setWillNotDraw(true);
    }

    private void runOnUiThread(Runnable runnable) {
        if (zzn.zzcS().zzhJ()) {
            runnable.run();
        } else {
            zzir.zzMc.post(runnable);
        }
    }

    public void destroy() {
        this.zzpD.destroy();
    }

    public void zzZ(String str) {
        final String format = String.format("<!DOCTYPE html><html><head><script src=\"%s\"></script></head><body></body></html>", new Object[]{str});
        runOnUiThread(new Runnable() {
            public void run() {
                zzef.this.zzpD.loadData(format, MediaType.TEXT_HTML_VALUE, "UTF-8");
            }
        });
    }

    public void zza(zza zza, zzg zzg, zzdb zzdb, zzp zzp, boolean z, zzdh zzdh, zzdj zzdj, zze zze, zzft zzft) {
        this.zzpD.zzhU().zzb(zza, zzg, zzdb, zzp, z, zzdh, zzdj, new zze(false), zzft);
    }

    public void zza(final zzed.zza zza) {
        this.zzpD.zzhU().zza(new zzjq.zza() {
            public void zza(zzjp zzjp, boolean z) {
                zza.zzeo();
            }
        });
    }

    public void zza(String str, zzdf zzdf) {
        this.zzpD.zzhU().zza(str, zzdf);
    }

    public void zza(final String str, final JSONObject jSONObject) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzef.this.zzpD.zza(str, jSONObject);
            }
        });
    }

    public void zzaa(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzef.this.zzpD.loadUrl(str);
            }
        });
    }

    public void zzab(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzef.this.zzpD.loadData(str, MediaType.TEXT_HTML_VALUE, "UTF-8");
            }
        });
    }

    public void zzb(String str, zzdf zzdf) {
        this.zzpD.zzhU().zzb(str, zzdf);
    }

    public void zzb(String str, JSONObject jSONObject) {
        this.zzpD.zzb(str, jSONObject);
    }

    public void zze(final String str, final String str2) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzef.this.zzpD.zze(str, str2);
            }
        });
    }

    public zzei zzen() {
        return new zzej(this);
    }
}
