package com.google.android.gms.internal;

import android.content.SharedPreferences;
import com.google.android.gms.ads.internal.zzp;

@zzgk
public abstract class zzbu<T> {
    private final String zztP;
    private final T zztQ;

    private zzbu(String str, T t) {
        this.zztP = str;
        this.zztQ = t;
        zzp.zzbF().zza(this);
    }

    /* synthetic */ zzbu(String str, Object obj, AnonymousClass1 anonymousClass1) {
        this(str, obj);
    }

    public static zzbu<String> zzP(String str) {
        zzbu zzc = zzc(str, (String) null);
        zzp.zzbF().zzb(zzc);
        return zzc;
    }

    public static zzbu<String> zzQ(String str) {
        zzbu zzc = zzc(str, (String) null);
        zzp.zzbF().zzc(zzc);
        return zzc;
    }

    public static zzbu<Integer> zza(String str, int i) {
        return new zzbu<Integer>(str, Integer.valueOf(i)) {
            /* renamed from: zzc */
            public Integer zza(SharedPreferences sharedPreferences) {
                return Integer.valueOf(sharedPreferences.getInt(getKey(), ((Integer) zzdd()).intValue()));
            }
        };
    }

    public static zzbu<Boolean> zza(String str, Boolean bool) {
        return new zzbu<Boolean>(str, bool) {
            /* renamed from: zzb */
            public Boolean zza(SharedPreferences sharedPreferences) {
                return Boolean.valueOf(sharedPreferences.getBoolean(getKey(), ((Boolean) zzdd()).booleanValue()));
            }
        };
    }

    public static zzbu<Long> zzb(String str, long j) {
        return new zzbu<Long>(str, Long.valueOf(j)) {
            /* renamed from: zzd */
            public Long zza(SharedPreferences sharedPreferences) {
                return Long.valueOf(sharedPreferences.getLong(getKey(), ((Long) zzdd()).longValue()));
            }
        };
    }

    public static zzbu<String> zzc(String str, String str2) {
        return new zzbu<String>(str, str2) {
            /* renamed from: zze */
            public String zza(SharedPreferences sharedPreferences) {
                return sharedPreferences.getString(getKey(), (String) zzdd());
            }
        };
    }

    public T get() {
        return zzp.zzbG().zzd(this);
    }

    public String getKey() {
        return this.zztP;
    }

    public abstract T zza(SharedPreferences sharedPreferences);

    public T zzdd() {
        return this.zztQ;
    }
}
