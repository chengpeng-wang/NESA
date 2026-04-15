package com.google.android.gms.internal;

import android.os.Binder;

public abstract class zzlz<T> {
    /* access modifiers changed from: private|static */
    public static zza zzaiV = null;
    private static int zzaiW = 0;
    private static String zzaiX = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    private static final Object zzqy = new Object();
    private T zzSC = null;
    protected final String zzvs;
    protected final T zzvt;

    private interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Boolean zza(String str, Boolean bool);

        Float zzb(String str, Float f);

        Integer zzb(String str, Integer num);
    }

    protected zzlz(String str, T t) {
        this.zzvs = str;
        this.zzvt = t;
    }

    public static boolean isInitialized() {
        return zzaiV != null;
    }

    public static zzlz<Float> zza(String str, Float f) {
        return new zzlz<Float>(str, f) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcx */
            public Float zzct(String str) {
                return zzlz.zzaiV.zzb(this.zzvs, (Float) this.zzvt);
            }
        };
    }

    public static zzlz<Integer> zza(String str, Integer num) {
        return new zzlz<Integer>(str, num) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcw */
            public Integer zzct(String str) {
                return zzlz.zzaiV.zzb(this.zzvs, (Integer) this.zzvt);
            }
        };
    }

    public static zzlz<Long> zza(String str, Long l) {
        return new zzlz<Long>(str, l) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcv */
            public Long zzct(String str) {
                return zzlz.zzaiV.getLong(this.zzvs, (Long) this.zzvt);
            }
        };
    }

    public static zzlz<Boolean> zzk(String str, boolean z) {
        return new zzlz<Boolean>(str, Boolean.valueOf(z)) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcu */
            public Boolean zzct(String str) {
                return zzlz.zzaiV.zza(this.zzvs, (Boolean) this.zzvt);
            }
        };
    }

    public static int zzpW() {
        return zzaiW;
    }

    public static zzlz<String> zzv(String str, String str2) {
        return new zzlz<String>(str, str2) {
            /* access modifiers changed from: protected */
            /* renamed from: zzcy */
            public String zzct(String str) {
                return zzlz.zzaiV.getString(this.zzvs, (String) this.zzvt);
            }
        };
    }

    public final T get() {
        return this.zzSC != null ? this.zzSC : zzct(this.zzvs);
    }

    public abstract T zzct(String str);

    public final T zzpX() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            T t = get();
            return t;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
