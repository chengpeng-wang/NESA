package com.google.android.gms.internal;

import android.content.Context;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@zzhb
public class zziw {
    private static zzl zzMy;
    public static final zza<Void> zzMz = new zza() {
        /* renamed from: zzhB */
        public Void zzgp() {
            return null;
        }

        /* renamed from: zzi */
        public Void zzh(InputStream inputStream) {
            return null;
        }
    };
    private static final Object zzqy = new Object();

    public interface zza<T> {
        T zzgp();

        T zzh(InputStream inputStream);
    }

    private static class zzb<T> extends zzk<InputStream> {
        private final zza<T> zzMD;
        private final com.google.android.gms.internal.zzm.zzb<T> zzaG;

        public zzb(String str, final zza<T> zza, final com.google.android.gms.internal.zzm.zzb<T> zzb) {
            super(0, str, new com.google.android.gms.internal.zzm.zza() {
                public void zze(zzr zzr) {
                    zzb.zzb(zza.zzgp());
                }
            });
            this.zzMD = zza;
            this.zzaG = zzb;
        }

        /* access modifiers changed from: protected */
        public zzm<InputStream> zza(zzi zzi) {
            return zzm.zza(new ByteArrayInputStream(zzi.data), zzx.zzb(zzi));
        }

        /* access modifiers changed from: protected */
        /* renamed from: zzj */
        public void zza(InputStream inputStream) {
            this.zzaG.zzb(this.zzMD.zzh(inputStream));
        }
    }

    private class zzc<T> extends zzjd<T> implements com.google.android.gms.internal.zzm.zzb<T> {
        private zzc() {
        }

        /* synthetic */ zzc(zziw zziw, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void zzb(T t) {
            super.zzg(t);
        }
    }

    public zziw(Context context) {
        zzMy = zzS(context);
    }

    private static zzl zzS(Context context) {
        zzl zzl;
        synchronized (zzqy) {
            if (zzMy == null) {
                zzMy = zzac.zza(context.getApplicationContext());
            }
            zzl = zzMy;
        }
        return zzl;
    }

    public <T> zzjg<T> zza(String str, zza<T> zza) {
        zzc zzc = new zzc(this, null);
        zzMy.zze(new zzb(str, zza, zzc));
        return zzc;
    }

    public zzjg<String> zzb(final String str, Map<String, String> map) {
        final zzc zzc = new zzc(this, null);
        final Map<String, String> map2 = map;
        zzMy.zze(new zzab(str, zzc, new com.google.android.gms.internal.zzm.zza() {
            public void zze(zzr zzr) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Failed to load URL: " + str + "\n" + zzr.toString());
                zzc.zzb(null);
            }
        }) {
            public Map<String, String> getHeaders() throws zza {
                return map2 == null ? super.getHeaders() : map2;
            }
        });
        return zzc;
    }
}
