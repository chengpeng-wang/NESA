package com.google.android.gms.internal;

import android.content.Context;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@zzgk
public class zzhy {
    private static zzl zzIf;
    public static final zza<Void> zzIg = new zza() {
        /* renamed from: zzgA */
        public Void zzfv() {
            return null;
        }

        /* renamed from: zzi */
        public Void zzh(InputStream inputStream) {
            return null;
        }
    };
    private static final Object zzpm = new Object();

    public interface zza<T> {
        T zzfv();

        T zzh(InputStream inputStream);
    }

    private static class zzb<T> extends zzk<InputStream> {
        private final zza<T> zzIk;
        private final com.google.android.gms.internal.zzm.zzb<T> zzaG;

        public zzb(String str, final zza<T> zza, final com.google.android.gms.internal.zzm.zzb<T> zzb) {
            super(0, str, new com.google.android.gms.internal.zzm.zza() {
                public void zze(zzr zzr) {
                    zzb.zzb(zza.zzfv());
                }
            });
            this.zzIk = zza;
            this.zzaG = zzb;
        }

        /* access modifiers changed from: protected */
        public zzm<InputStream> zza(zzi zzi) {
            return zzm.zza(new ByteArrayInputStream(zzi.data), zzx.zzb(zzi));
        }

        /* access modifiers changed from: protected */
        /* renamed from: zzj */
        public void zza(InputStream inputStream) {
            this.zzaG.zzb(this.zzIk.zzh(inputStream));
        }
    }

    private class zzc<T> extends zzie<T> implements com.google.android.gms.internal.zzm.zzb<T> {
        private zzc() {
        }

        /* synthetic */ zzc(zzhy zzhy, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void zzb(T t) {
            super.zzf(t);
        }
    }

    public zzhy(Context context) {
        zzIf = zzP(context);
    }

    private static zzl zzP(Context context) {
        zzl zzl;
        synchronized (zzpm) {
            if (zzIf == null) {
                zzIf = zzac.zza(context.getApplicationContext());
            }
            zzl = zzIf;
        }
        return zzl;
    }

    public <T> zzih<T> zza(String str, zza<T> zza) {
        zzc zzc = new zzc(this, null);
        zzIf.zze(new zzb(str, zza, zzc));
        return zzc;
    }

    public zzih<String> zzb(final String str, Map<String, String> map) {
        final zzc zzc = new zzc(this, null);
        final Map<String, String> map2 = map;
        zzIf.zze(new zzab(str, zzc, new com.google.android.gms.internal.zzm.zza() {
            public void zze(zzr zzr) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaE("Failed to load URL: " + str + MASTNativeAdConstants.NEWLINE + zzr.toString());
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
