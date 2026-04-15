package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.internal.zzgk;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@zzgk
public final class zzx {
    public static final String DEVICE_ID_EMULATOR = zzk.zzcE().zzaB("emulator");
    private final Date zzaT;
    private final Set<String> zzaV;
    private final Location zzaX;
    private final boolean zzoM;
    private final String zzsV;
    private final int zzsW;
    private final Bundle zzsX;
    private final Map<Class<? extends NetworkExtras>, NetworkExtras> zzsY;
    private final String zzsZ;
    private final String zzta;
    private final SearchAdRequest zztb;
    private final int zztc;
    private final Set<String> zztd;
    private final Bundle zzte;
    private final Set<String> zztf;

    public static final class zza {
        /* access modifiers changed from: private */
        public Date zzaT;
        /* access modifiers changed from: private */
        public Location zzaX;
        /* access modifiers changed from: private */
        public boolean zzoM = false;
        /* access modifiers changed from: private */
        public String zzsV;
        /* access modifiers changed from: private */
        public int zzsW = -1;
        /* access modifiers changed from: private|final */
        public final Bundle zzsX = new Bundle();
        /* access modifiers changed from: private */
        public String zzsZ;
        /* access modifiers changed from: private */
        public String zzta;
        /* access modifiers changed from: private */
        public int zztc = -1;
        /* access modifiers changed from: private|final */
        public final Bundle zzte = new Bundle();
        /* access modifiers changed from: private|final */
        public final HashSet<String> zztg = new HashSet();
        /* access modifiers changed from: private|final */
        public final HashMap<Class<? extends NetworkExtras>, NetworkExtras> zzth = new HashMap();
        /* access modifiers changed from: private|final */
        public final HashSet<String> zzti = new HashSet();
        /* access modifiers changed from: private|final */
        public final HashSet<String> zztj = new HashSet();

        public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
            this.zzoM = manualImpressionsEnabled;
        }

        public void zzF(String str) {
            this.zztg.add(str);
        }

        public void zzG(String str) {
            this.zzti.add(str);
        }

        public void zzH(String str) {
            this.zzti.remove(str);
        }

        public void zzI(String str) {
            this.zzsV = str;
        }

        public void zzJ(String str) {
            this.zzsZ = str;
        }

        public void zzK(String str) {
            this.zzta = str;
        }

        public void zzL(String str) {
            this.zztj.add(str);
        }

        public void zza(Location location) {
            this.zzaX = location;
        }

        @Deprecated
        public void zza(NetworkExtras networkExtras) {
            if (networkExtras instanceof AdMobExtras) {
                zza(AdMobAdapter.class, ((AdMobExtras) networkExtras).getExtras());
            } else {
                this.zzth.put(networkExtras.getClass(), networkExtras);
            }
        }

        public void zza(Class<? extends MediationAdapter> cls, Bundle bundle) {
            this.zzsX.putBundle(cls.getName(), bundle);
        }

        public void zza(Date date) {
            this.zzaT = date;
        }

        public void zzb(Class<? extends CustomEvent> cls, Bundle bundle) {
            if (this.zzsX.getBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter") == null) {
                this.zzsX.putBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter", new Bundle());
            }
            this.zzsX.getBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter").putBundle(cls.getName(), bundle);
        }

        public void zzb(String str, String str2) {
            this.zzte.putString(str, str2);
        }

        public void zzj(boolean z) {
            this.zztc = z ? 1 : 0;
        }

        public void zzm(int i) {
            this.zzsW = i;
        }
    }

    public zzx(zza zza) {
        this(zza, null);
    }

    public zzx(zza zza, SearchAdRequest searchAdRequest) {
        this.zzaT = zza.zzaT;
        this.zzsV = zza.zzsV;
        this.zzsW = zza.zzsW;
        this.zzaV = Collections.unmodifiableSet(zza.zztg);
        this.zzaX = zza.zzaX;
        this.zzoM = zza.zzoM;
        this.zzsX = zza.zzsX;
        this.zzsY = Collections.unmodifiableMap(zza.zzth);
        this.zzsZ = zza.zzsZ;
        this.zzta = zza.zzta;
        this.zztb = searchAdRequest;
        this.zztc = zza.zztc;
        this.zztd = Collections.unmodifiableSet(zza.zzti);
        this.zzte = zza.zzte;
        this.zztf = Collections.unmodifiableSet(zza.zztj);
    }

    public static void updateCorrelator() {
        zzk.zzcG().zzcK();
    }

    public Date getBirthday() {
        return this.zzaT;
    }

    public String getContentUrl() {
        return this.zzsV;
    }

    public Bundle getCustomEventExtrasBundle(Class<? extends CustomEvent> adapterClass) {
        Bundle bundle = this.zzsX.getBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter");
        return bundle != null ? bundle.getBundle(adapterClass.getClass().getName()) : null;
    }

    public Bundle getCustomTargeting() {
        return this.zzte;
    }

    public int getGender() {
        return this.zzsW;
    }

    public Set<String> getKeywords() {
        return this.zzaV;
    }

    public Location getLocation() {
        return this.zzaX;
    }

    public boolean getManualImpressionsEnabled() {
        return this.zzoM;
    }

    @Deprecated
    public <T extends NetworkExtras> T getNetworkExtras(Class<T> networkExtrasClass) {
        return (NetworkExtras) this.zzsY.get(networkExtrasClass);
    }

    public Bundle getNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass) {
        return this.zzsX.getBundle(adapterClass.getName());
    }

    public String getPublisherProvidedId() {
        return this.zzsZ;
    }

    public boolean isTestDevice(Context context) {
        return this.zztd.contains(zzk.zzcE().zzQ(context));
    }

    public String zzcL() {
        return this.zzta;
    }

    public SearchAdRequest zzcM() {
        return this.zztb;
    }

    public Map<Class<? extends NetworkExtras>, NetworkExtras> zzcN() {
        return this.zzsY;
    }

    public Bundle zzcO() {
        return this.zzsX;
    }

    public int zzcP() {
        return this.zztc;
    }

    public Set<String> zzcQ() {
        return this.zztf;
    }
}
