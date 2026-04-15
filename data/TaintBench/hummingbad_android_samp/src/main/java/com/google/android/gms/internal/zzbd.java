package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.overlay.zzg;
import com.google.android.gms.ads.internal.overlay.zzn;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.ads.internal.zzp;
import com.moceanmobile.mast.Defaults;
import org.json.JSONObject;

@zzgk
public class zzbd implements zzbb {
    /* access modifiers changed from: private|final */
    public final zzip zzoL;

    public zzbd(Context context, VersionInfoParcel versionInfoParcel, zzan zzan) {
        this.zzoL = zzp.zzby().zza(context, new AdSizeParcel(), false, false, zzan, versionInfoParcel);
        this.zzoL.setWillNotDraw(true);
    }

    private void runOnUiThread(Runnable runnable) {
        if (zzk.zzcE().zzgI()) {
            runnable.run();
        } else {
            zzhu.zzHK.post(runnable);
        }
    }

    public void destroy() {
        this.zzoL.destroy();
    }

    public void zza(zza zza, zzg zzg, zzdd zzdd, zzn zzn, boolean z, zzdi zzdi, zzdk zzdk, zze zze, zzfc zzfc) {
        this.zzoL.zzgS().zzb(zza, zzg, zzdd, zzn, z, zzdi, zzdk, new zze(false), zzfc);
    }

    public void zza(final zzbb.zza zza) {
        this.zzoL.zzgS().zza(new zziq.zza() {
            public void zza(zzip zzip, boolean z) {
                zza.zzcj();
            }
        });
    }

    public void zza(String str, zzdg zzdg) {
        this.zzoL.zzgS().zza(str, zzdg);
    }

    public void zza(final String str, final String str2) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzbd.this.zzoL.zza(str, str2);
            }
        });
    }

    public void zza(final String str, final JSONObject jSONObject) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzbd.this.zzoL.zza(str, jSONObject);
            }
        });
    }

    public void zzb(String str, zzdg zzdg) {
        this.zzoL.zzgS().zzb(str, zzdg);
    }

    public void zzb(String str, JSONObject jSONObject) {
        this.zzoL.zzb(str, jSONObject);
    }

    public zzbf zzci() {
        return new zzbg(this);
    }

    public void zzs(String str) {
        final String format = String.format("<!DOCTYPE html><html><head><script src=\"%s\"></script></head><body></body></html>", new Object[]{str});
        runOnUiThread(new Runnable() {
            public void run() {
                zzbd.this.zzoL.loadData(format, "text/html", Defaults.ENCODING_UTF_8);
            }
        });
    }

    public void zzt(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzbd.this.zzoL.loadUrl(str);
            }
        });
    }

    public void zzu(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                zzbd.this.zzoL.loadData(str, "text/html", Defaults.ENCODING_UTF_8);
            }
        });
    }
}
