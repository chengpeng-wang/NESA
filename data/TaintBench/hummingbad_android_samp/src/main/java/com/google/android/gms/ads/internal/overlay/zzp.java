package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzcc;
import com.google.android.gms.internal.zzcd;
import com.google.android.gms.internal.zzhx;
import com.google.android.gms.internal.zzhx.zza;
import com.google.android.gms.internal.zzhx.zzb;
import java.util.concurrent.TimeUnit;

public class zzp {
    private final Context mContext;
    private boolean zzBA;
    private long zzBB = -1;
    private final String zzBm;
    private final zzcc zzBn;
    private final zzcd zzBo;
    private final VersionInfoParcel zzBp;
    private final zzhx zzBq = new zzb().zza("min_1", Double.MIN_VALUE, 1.0d).zza("1_5", 1.0d, 5.0d).zza("5_10", 5.0d, 10.0d).zza("10_20", 10.0d, 20.0d).zza("20_30", 20.0d, 30.0d).zza("30_max", 30.0d, Double.MAX_VALUE).zzgz();
    private final long[] zzBr;
    private final String[] zzBs;
    private zzcc zzBt;
    private zzcc zzBu;
    private zzcc zzBv;
    private zzcc zzBw;
    private boolean zzBx;
    private zzi zzBy;
    private boolean zzBz;

    public zzp(Context context, VersionInfoParcel versionInfoParcel, String str, zzcd zzcd, zzcc zzcc) {
        this.mContext = context;
        this.zzBp = versionInfoParcel;
        this.zzBm = str;
        this.zzBo = zzcd;
        this.zzBn = zzcc;
        String str2 = (String) zzby.zzuq.get();
        if (str2 == null) {
            this.zzBs = new String[0];
            this.zzBr = new long[0];
            return;
        }
        String[] split = TextUtils.split(str2, ",");
        this.zzBs = new String[split.length];
        this.zzBr = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                this.zzBr[i] = Long.parseLong(split[i]);
            } catch (NumberFormatException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Unable to parse frame hash target time number.", e);
                this.zzBr[i] = -1;
            }
        }
    }

    private void zzc(zzi zzi) {
        long longValue = ((Long) zzby.zzur.get()).longValue();
        long currentPosition = (long) zzi.getCurrentPosition();
        int i = 0;
        while (i < this.zzBs.length) {
            if (this.zzBs[i] == null && longValue > Math.abs(currentPosition - this.zzBr[i])) {
                this.zzBs[i] = zza((TextureView) zzi);
                return;
            }
            i++;
        }
    }

    private void zzeX() {
        if (this.zzBv != null && this.zzBw == null) {
            this.zzBo.zza(this.zzBv, "vff");
            this.zzBo.zza(this.zzBn, "vtt");
            this.zzBw = this.zzBo.zzdl();
        }
        long nanoTime = com.google.android.gms.ads.internal.zzp.zzbB().nanoTime();
        if (this.zzBx && this.zzBA && this.zzBB != -1) {
            this.zzBq.zza(((double) TimeUnit.SECONDS.toNanos(1)) / ((double) (nanoTime - this.zzBB)));
        }
        this.zzBA = this.zzBx;
        this.zzBB = nanoTime;
    }

    public void onStop() {
        if (((Boolean) zzby.zzup.get()).booleanValue() && !this.zzBz) {
            Bundle bundle = new Bundle();
            bundle.putString("type", "native-player-metrics");
            bundle.putString("request", this.zzBm);
            bundle.putString("player", this.zzBy.zzek());
            for (zza zza : this.zzBq.getBuckets()) {
                bundle.putString("fps_c_" + zza.name, Integer.toString(zza.count));
                bundle.putString("fps_p_" + zza.name, Double.toString(zza.zzIb));
            }
            for (int i = 0; i < this.zzBr.length; i++) {
                String str = this.zzBs[i];
                if (str != null) {
                    bundle.putString("fh_" + Long.valueOf(this.zzBr[i]), str);
                }
            }
            com.google.android.gms.ads.internal.zzp.zzbx().zza(this.mContext, this.zzBp.zzIz, "gmob-apps", bundle, true);
            this.zzBz = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public String zza(TextureView textureView) {
        Bitmap bitmap = textureView.getBitmap(8, 8);
        long j = 0;
        long j2 = 63;
        int i = 0;
        while (i < 8) {
            int i2 = 0;
            long j3 = j;
            while (true) {
                j = j2;
                int i3 = i2;
                if (i3 >= 8) {
                    break;
                }
                i2 = bitmap.getPixel(i3, i);
                j3 |= (Color.green(i2) + (Color.blue(i2) + Color.red(i2)) > 128 ? 1 : 0) << ((int) j);
                i2 = i3 + 1;
                j2 = j - 1;
            }
            i++;
            j2 = j;
            j = j3;
        }
        return String.format("%016X", new Object[]{Long.valueOf(j)});
    }

    public void zza(zzi zzi) {
        this.zzBo.zza(this.zzBn, "vpc");
        this.zzBt = this.zzBo.zzdl();
        this.zzBy = zzi;
    }

    public void zzb(zzi zzi) {
        zzeX();
        zzc(zzi);
    }

    public void zzeL() {
        if (this.zzBt != null && this.zzBu == null) {
            this.zzBo.zza(this.zzBt, "vfr");
            this.zzBu = this.zzBo.zzdl();
        }
    }

    public void zzeY() {
        this.zzBx = true;
        if (this.zzBu != null && this.zzBv == null) {
            this.zzBo.zza(this.zzBu, "vfp");
            this.zzBv = this.zzBo.zzdl();
        }
    }

    public void zzeZ() {
        this.zzBx = false;
    }
}
