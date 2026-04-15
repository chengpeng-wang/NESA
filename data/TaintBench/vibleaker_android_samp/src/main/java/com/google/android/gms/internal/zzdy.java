package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzhj.zza;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

@zzhb
public class zzdy {
    private final Map<zzdz, zzea> zzAx = new HashMap();
    private final LinkedList<zzdz> zzAy = new LinkedList();
    private zzdv zzAz;

    private String[] zzY(String str) {
        try {
            String[] split = str.split("\u0000");
            for (int i = 0; i < split.length; i++) {
                split[i] = new String(Base64.decode(split[i], 0), "UTF-8");
            }
            return split;
        } catch (UnsupportedEncodingException e) {
            return new String[0];
        }
    }

    private static void zza(String str, zzdz zzdz) {
        if (zzb.zzQ(2)) {
            zzin.v(String.format(str, new Object[]{zzdz}));
        }
    }

    private String zzef() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator it = this.zzAy.iterator();
            while (it.hasNext()) {
                stringBuilder.append(Base64.encodeToString(((zzdz) it.next()).toString().getBytes("UTF-8"), 0));
                if (it.hasNext()) {
                    stringBuilder.append("\u0000");
                }
            }
            return stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /* access modifiers changed from: 0000 */
    public void flush() {
        while (this.zzAy.size() > 0) {
            zzdz zzdz = (zzdz) this.zzAy.remove();
            zzea zzea = (zzea) this.zzAx.get(zzdz);
            zza("Flushing interstitial queue for %s.", zzdz);
            while (zzea.size() > 0) {
                zzea.zzej().zzAD.zzbp();
            }
            this.zzAx.remove(zzdz);
        }
    }

    /* access modifiers changed from: 0000 */
    public void restore() {
        if (this.zzAz != null) {
            zzdz zzdz;
            SharedPreferences sharedPreferences = this.zzAz.zzed().getSharedPreferences("com.google.android.gms.ads.internal.interstitial.InterstitialAdPool", 0);
            flush();
            HashMap hashMap = new HashMap();
            for (Entry entry : sharedPreferences.getAll().entrySet()) {
                try {
                    if (!((String) entry.getKey()).equals("PoolKeys")) {
                        zzec zzec = new zzec((String) entry.getValue());
                        zzdz = new zzdz(zzec.zzqH, zzec.zzpS, zzec.zzAC);
                        if (!this.zzAx.containsKey(zzdz)) {
                            this.zzAx.put(zzdz, new zzea(zzec.zzqH, zzec.zzpS, zzec.zzAC));
                            hashMap.put(zzdz.toString(), zzdz);
                            zza("Restored interstitial queue for %s.", zzdz);
                        }
                    }
                } catch (IOException | ClassCastException e) {
                    zzb.zzd("Malformed preferences value for InterstitialAdPool.", e);
                }
            }
            for (Object obj : zzY(sharedPreferences.getString("PoolKeys", ""))) {
                zzdz = (zzdz) hashMap.get(obj);
                if (this.zzAx.containsKey(zzdz)) {
                    this.zzAy.add(zzdz);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void save() {
        if (this.zzAz != null) {
            Editor edit = this.zzAz.zzed().getSharedPreferences("com.google.android.gms.ads.internal.interstitial.InterstitialAdPool", 0).edit();
            edit.clear();
            for (Entry entry : this.zzAx.entrySet()) {
                zzdz zzdz = (zzdz) entry.getKey();
                if (zzdz.zzeh()) {
                    edit.putString(zzdz.toString(), new zzec((zzea) entry.getValue()).zzem());
                    zza("Saved interstitial queue for %s.", zzdz);
                }
            }
            edit.putString("PoolKeys", zzef());
            edit.commit();
        }
    }

    /* access modifiers changed from: 0000 */
    public zza zza(AdRequestParcel adRequestParcel, String str) {
        zzea zzea;
        int i = new zza(this.zzAz.zzed()).zzgI().zzKc;
        zzdz zzdz = new zzdz(adRequestParcel, str, i);
        zzea zzea2 = (zzea) this.zzAx.get(zzdz);
        if (zzea2 == null) {
            zza("Interstitial pool created at %s.", zzdz);
            zzea2 = new zzea(adRequestParcel, str, i);
            this.zzAx.put(zzdz, zzea2);
            zzea = zzea2;
        } else {
            zzea = zzea2;
        }
        this.zzAy.remove(zzdz);
        this.zzAy.add(zzdz);
        zzdz.zzeg();
        while (this.zzAy.size() > ((Integer) zzbt.zzwG.get()).intValue()) {
            zzdz zzdz2 = (zzdz) this.zzAy.remove();
            zzea zzea3 = (zzea) this.zzAx.get(zzdz2);
            zza("Evicting interstitial queue for %s.", zzdz2);
            while (zzea3.size() > 0) {
                zzea3.zzej().zzAD.zzbp();
            }
            this.zzAx.remove(zzdz2);
        }
        while (zzea.size() > 0) {
            zza zzej = zzea.zzej();
            if (!zzej.zzAG || zzr.zzbG().currentTimeMillis() - zzej.zzAF <= 1000 * ((long) ((Integer) zzbt.zzwI.get()).intValue())) {
                zza("Pooled interstitial returned at %s.", zzdz);
                return zzej;
            }
            zza("Expired interstitial at %s.", zzdz);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void zza(zzdv zzdv) {
        if (this.zzAz == null) {
            this.zzAz = zzdv;
            restore();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zzee() {
        if (this.zzAz != null) {
            for (Entry entry : this.zzAx.entrySet()) {
                zzdz zzdz = (zzdz) entry.getKey();
                zzea zzea = (zzea) entry.getValue();
                while (zzea.size() < ((Integer) zzbt.zzwH.get()).intValue()) {
                    zza("Pooling one interstitial for %s.", zzdz);
                    zzea.zzb(this.zzAz);
                }
            }
            save();
        }
    }
}
