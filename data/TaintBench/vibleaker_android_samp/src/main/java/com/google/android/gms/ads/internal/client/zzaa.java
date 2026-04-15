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
import com.google.android.gms.internal.zzhb;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@zzhb
public final class zzaa {
    public static final String DEVICE_ID_EMULATOR = zzn.zzcS().zzaH("emulator");
    private final Date zzbf;
    private final Set<String> zzbh;
    private final Location zzbj;
    private final boolean zzpE;
    private final int zztT;
    private final int zztW;
    private final String zztX;
    private final String zztZ;
    private final Bundle zzuA;
    private final Map<Class<? extends NetworkExtras>, NetworkExtras> zzuB;
    private final SearchAdRequest zzuC;
    private final Set<String> zzuD;
    private final Set<String> zzuE;
    private final Bundle zzub;
    private final String zzud;
    private final boolean zzuf;

    public static final class zza {
        /* access modifiers changed from: private */
        public Date zzbf;
        /* access modifiers changed from: private */
        public Location zzbj;
        /* access modifiers changed from: private */
        public boolean zzpE = false;
        /* access modifiers changed from: private */
        public int zztT = -1;
        /* access modifiers changed from: private */
        public int zztW = -1;
        /* access modifiers changed from: private */
        public String zztX;
        /* access modifiers changed from: private */
        public String zztZ;
        /* access modifiers changed from: private|final */
        public final Bundle zzuA = new Bundle();
        /* access modifiers changed from: private|final */
        public final HashSet<String> zzuF = new HashSet();
        /* access modifiers changed from: private|final */
        public final HashMap<Class<? extends NetworkExtras>, NetworkExtras> zzuG = new HashMap();
        /* access modifiers changed from: private|final */
        public final HashSet<String> zzuH = new HashSet();
        /* access modifiers changed from: private|final */
        public final HashSet<String> zzuI = new HashSet();
        /* access modifiers changed from: private|final */
        public final Bundle zzub = new Bundle();
        /* access modifiers changed from: private */
        public String zzud;
        /* access modifiers changed from: private */
        public boolean zzuf;

        public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
            this.zzpE = manualImpressionsEnabled;
        }

        public void zzA(String str) {
            this.zzuF.add(str);
        }

        public void zzB(String str) {
            this.zzuH.add(str);
        }

        public void zzC(String str) {
            this.zzuH.remove(str);
        }

        public void zzD(String str) {
            this.zztZ = str;
        }

        public void zzE(String str) {
            this.zztX = str;
        }

        public void zzF(String str) {
            this.zzud = str;
        }

        public void zzG(String str) {
            this.zzuI.add(str);
        }

        @Deprecated
        public void zza(NetworkExtras networkExtras) {
            if (networkExtras instanceof AdMobExtras) {
                zza(AdMobAdapter.class, ((AdMobExtras) networkExtras).getExtras());
            } else {
                this.zzuG.put(networkExtras.getClass(), networkExtras);
            }
        }

        public void zza(Class<? extends MediationAdapter> cls, Bundle bundle) {
            this.zzuA.putBundle(cls.getName(), bundle);
        }

        public void zza(String str, String str2) {
            this.zzub.putString(str, str2);
        }

        public void zza(Date date) {
            this.zzbf = date;
        }

        public void zzb(Location location) {
            this.zzbj = location;
        }

        public void zzb(Class<? extends CustomEvent> cls, Bundle bundle) {
            if (this.zzuA.getBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter") == null) {
                this.zzuA.putBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter", new Bundle());
            }
            this.zzuA.getBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter").putBundle(cls.getName(), bundle);
        }

        public void zzk(boolean z) {
            this.zztW = z ? 1 : 0;
        }

        public void zzl(boolean z) {
            this.zzuf = z;
        }

        public void zzn(int i) {
            this.zztT = i;
        }
    }

    public zzaa(zza zza) {
        this(zza, null);
    }

    public zzaa(zza zza, SearchAdRequest searchAdRequest) {
        this.zzbf = zza.zzbf;
        this.zztZ = zza.zztZ;
        this.zztT = zza.zztT;
        this.zzbh = Collections.unmodifiableSet(zza.zzuF);
        this.zzbj = zza.zzbj;
        this.zzpE = zza.zzpE;
        this.zzuA = zza.zzuA;
        this.zzuB = Collections.unmodifiableMap(zza.zzuG);
        this.zztX = zza.zztX;
        this.zzud = zza.zzud;
        this.zzuC = searchAdRequest;
        this.zztW = zza.zztW;
        this.zzuD = Collections.unmodifiableSet(zza.zzuH);
        this.zzub = zza.zzub;
        this.zzuE = Collections.unmodifiableSet(zza.zzuI);
        this.zzuf = zza.zzuf;
    }

    public Date getBirthday() {
        return this.zzbf;
    }

    public String getContentUrl() {
        return this.zztZ;
    }

    public Bundle getCustomEventExtrasBundle(Class<? extends CustomEvent> adapterClass) {
        Bundle bundle = this.zzuA.getBundle("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter");
        return bundle != null ? bundle.getBundle(adapterClass.getClass().getName()) : null;
    }

    public Bundle getCustomTargeting() {
        return this.zzub;
    }

    public int getGender() {
        return this.zztT;
    }

    public Set<String> getKeywords() {
        return this.zzbh;
    }

    public Location getLocation() {
        return this.zzbj;
    }

    public boolean getManualImpressionsEnabled() {
        return this.zzpE;
    }

    @Deprecated
    public <T extends NetworkExtras> T getNetworkExtras(Class<T> networkExtrasClass) {
        return (NetworkExtras) this.zzuB.get(networkExtrasClass);
    }

    public Bundle getNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass) {
        return this.zzuA.getBundle(adapterClass.getName());
    }

    public String getPublisherProvidedId() {
        return this.zztX;
    }

    public boolean isDesignedForFamilies() {
        return this.zzuf;
    }

    public boolean isTestDevice(Context context) {
        return this.zzuD.contains(zzn.zzcS().zzT(context));
    }

    public String zzcZ() {
        return this.zzud;
    }

    public SearchAdRequest zzda() {
        return this.zzuC;
    }

    public Map<Class<? extends NetworkExtras>, NetworkExtras> zzdb() {
        return this.zzuB;
    }

    public Bundle zzdc() {
        return this.zzuA;
    }

    public int zzdd() {
        return this.zztW;
    }

    public Set<String> zzde() {
        return this.zzuE;
    }
}
