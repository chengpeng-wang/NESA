package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzrn;
import com.google.android.gms.internal.zzro;
import com.google.android.gms.signin.internal.SignInResponse;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

public class zzh implements zzk {
    /* access modifiers changed from: private|final */
    public final Context mContext;
    /* access modifiers changed from: private|final */
    public final Lock zzXG;
    /* access modifiers changed from: private|final */
    public final com.google.android.gms.common.zzc zzags;
    private final com.google.android.gms.common.api.Api.zza<? extends zzrn, zzro> zzagt;
    private final Map<Api<?>, Integer> zzahA;
    private ArrayList<Future<?>> zzahB = new ArrayList();
    /* access modifiers changed from: private|final */
    public final zzl zzahj;
    private ConnectionResult zzahm;
    private int zzahn;
    private int zzaho = 0;
    private int zzahp;
    private final Bundle zzahq = new Bundle();
    private final Set<com.google.android.gms.common.api.Api.zzc> zzahr = new HashSet();
    /* access modifiers changed from: private */
    public zzrn zzahs;
    private int zzaht;
    /* access modifiers changed from: private */
    public boolean zzahu;
    private boolean zzahv;
    /* access modifiers changed from: private */
    public zzp zzahw;
    private boolean zzahx;
    private boolean zzahy;
    private final com.google.android.gms.common.internal.zzf zzahz;

    private static class zza implements com.google.android.gms.common.api.GoogleApiClient.zza {
        private final Api<?> zzagT;
        private final int zzagU;
        private final WeakReference<zzh> zzahD;

        public zza(zzh zzh, Api<?> api, int i) {
            this.zzahD = new WeakReference(zzh);
            this.zzagT = api;
            this.zzagU = i;
        }

        public void zza(@NonNull ConnectionResult connectionResult) {
            boolean z = false;
            zzh zzh = (zzh) this.zzahD.get();
            if (zzh != null) {
                if (Looper.myLooper() == zzh.zzahj.zzagW.getLooper()) {
                    z = true;
                }
                zzx.zza(z, (Object) "onReportServiceBinding must be called on the GoogleApiClient handler thread");
                zzh.zzXG.lock();
                try {
                    if (zzh.zzbz(0)) {
                        if (!connectionResult.isSuccess()) {
                            zzh.zzb(connectionResult, this.zzagT, this.zzagU);
                        }
                        if (zzh.zzpu()) {
                            zzh.zzpv();
                        }
                        zzh.zzXG.unlock();
                    }
                } finally {
                    zzh.zzXG.unlock();
                }
            }
        }
    }

    private abstract class zzf implements Runnable {
        private zzf() {
        }

        /* synthetic */ zzf(zzh zzh, AnonymousClass1 anonymousClass1) {
            this();
        }

        @WorkerThread
        public void run() {
            zzh.this.zzXG.lock();
            try {
                if (!Thread.interrupted()) {
                    zzpt();
                    zzh.this.zzXG.unlock();
                }
            } catch (RuntimeException e) {
                zzh.this.zzahj.zza(e);
            } finally {
                zzh.this.zzXG.unlock();
            }
        }

        @WorkerThread
        public abstract void zzpt();
    }

    private class zzb extends zzf {
        private final Map<com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.api.GoogleApiClient.zza> zzahE;

        public zzb(Map<com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.api.GoogleApiClient.zza> map) {
            super(zzh.this, null);
            this.zzahE = map;
        }

        @WorkerThread
        public void zzpt() {
            int isGooglePlayServicesAvailable = zzh.this.zzags.isGooglePlayServicesAvailable(zzh.this.mContext);
            if (isGooglePlayServicesAvailable != 0) {
                final ConnectionResult connectionResult = new ConnectionResult(isGooglePlayServicesAvailable, null);
                zzh.this.zzahj.zza(new zza(zzh.this) {
                    public void zzpt() {
                        zzh.this.zzg(connectionResult);
                    }
                });
                return;
            }
            if (zzh.this.zzahu) {
                zzh.this.zzahs.connect();
            }
            for (com.google.android.gms.common.api.Api.zzb zzb : this.zzahE.keySet()) {
                zzb.zza((com.google.android.gms.common.api.GoogleApiClient.zza) this.zzahE.get(zzb));
            }
        }
    }

    private class zzc extends zzf {
        private final ArrayList<com.google.android.gms.common.api.Api.zzb> zzahH;

        public zzc(ArrayList<com.google.android.gms.common.api.Api.zzb> arrayList) {
            super(zzh.this, null);
            this.zzahH = arrayList;
        }

        @WorkerThread
        public void zzpt() {
            zzh.this.zzahj.zzagW.zzahU = zzh.this.zzpA();
            Iterator it = this.zzahH.iterator();
            while (it.hasNext()) {
                ((com.google.android.gms.common.api.Api.zzb) it.next()).zza(zzh.this.zzahw, zzh.this.zzahj.zzagW.zzahU);
            }
        }
    }

    private static class zzd extends com.google.android.gms.signin.internal.zzb {
        private final WeakReference<zzh> zzahD;

        zzd(zzh zzh) {
            this.zzahD = new WeakReference(zzh);
        }

        @BinderThread
        public void zzb(final SignInResponse signInResponse) {
            final zzh zzh = (zzh) this.zzahD.get();
            if (zzh != null) {
                zzh.zzahj.zza(new zza(zzh) {
                    public void zzpt() {
                        zzh.zza(signInResponse);
                    }
                });
            }
        }
    }

    private class zze implements ConnectionCallbacks, OnConnectionFailedListener {
        private zze() {
        }

        /* synthetic */ zze(zzh zzh, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void onConnected(Bundle connectionHint) {
            zzh.this.zzahs.zza(new zzd(zzh.this));
        }

        public void onConnectionFailed(@NonNull ConnectionResult result) {
            zzh.this.zzXG.lock();
            try {
                if (zzh.this.zzf(result)) {
                    zzh.this.zzpy();
                    zzh.this.zzpv();
                } else {
                    zzh.this.zzg(result);
                }
                zzh.this.zzXG.unlock();
            } catch (Throwable th) {
                zzh.this.zzXG.unlock();
            }
        }

        public void onConnectionSuspended(int cause) {
        }
    }

    public zzh(zzl zzl, com.google.android.gms.common.internal.zzf zzf, Map<Api<?>, Integer> map, com.google.android.gms.common.zzc zzc, com.google.android.gms.common.api.Api.zza<? extends zzrn, zzro> zza, Lock lock, Context context) {
        this.zzahj = zzl;
        this.zzahz = zzf;
        this.zzahA = map;
        this.zzags = zzc;
        this.zzagt = zza;
        this.zzXG = lock;
        this.mContext = context;
    }

    private void zzZ(boolean z) {
        if (this.zzahs != null) {
            if (this.zzahs.isConnected() && z) {
                this.zzahs.zzFG();
            }
            this.zzahs.disconnect();
            this.zzahw = null;
        }
    }

    /* access modifiers changed from: private */
    public void zza(SignInResponse signInResponse) {
        if (zzbz(0)) {
            ConnectionResult zzqY = signInResponse.zzqY();
            if (zzqY.isSuccess()) {
                ResolveAccountResponse zzFP = signInResponse.zzFP();
                ConnectionResult zzqY2 = zzFP.zzqY();
                if (zzqY2.isSuccess()) {
                    this.zzahv = true;
                    this.zzahw = zzFP.zzqX();
                    this.zzahx = zzFP.zzqZ();
                    this.zzahy = zzFP.zzra();
                    zzpv();
                    return;
                }
                Log.wtf("GoogleApiClientConnecting", "Sign-in succeeded with resolve account failure: " + zzqY2, new Exception());
                zzg(zzqY2);
            } else if (zzf(zzqY)) {
                zzpy();
                zzpv();
            } else {
                zzg(zzqY);
            }
        }
    }

    private boolean zza(int i, int i2, ConnectionResult connectionResult) {
        return (i2 != 1 || zze(connectionResult)) ? this.zzahm == null || i < this.zzahn : false;
    }

    /* access modifiers changed from: private */
    public void zzb(ConnectionResult connectionResult, Api<?> api, int i) {
        if (i != 2) {
            int priority = api.zzoP().getPriority();
            if (zza(priority, i, connectionResult)) {
                this.zzahm = connectionResult;
                this.zzahn = priority;
            }
        }
        this.zzahj.zzaio.put(api.zzoR(), connectionResult);
    }

    private String zzbA(int i) {
        switch (i) {
            case 0:
                return "STEP_SERVICE_BINDINGS_AND_SIGN_IN";
            case 1:
                return "STEP_GETTING_REMOTE_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    /* access modifiers changed from: private */
    public boolean zzbz(int i) {
        if (this.zzaho == i) {
            return true;
        }
        Log.i("GoogleApiClientConnecting", this.zzahj.zzagW.zzpH());
        Log.wtf("GoogleApiClientConnecting", "GoogleApiClient connecting is in step " + zzbA(this.zzaho) + " but received callback for step " + zzbA(i), new Exception());
        zzg(new ConnectionResult(8, null));
        return false;
    }

    private boolean zze(ConnectionResult connectionResult) {
        return connectionResult.hasResolution() || this.zzags.zzbu(connectionResult.getErrorCode()) != null;
    }

    /* access modifiers changed from: private */
    public boolean zzf(ConnectionResult connectionResult) {
        return this.zzaht != 2 ? this.zzaht == 1 && !connectionResult.hasResolution() : true;
    }

    /* access modifiers changed from: private */
    public void zzg(ConnectionResult connectionResult) {
        zzpz();
        zzZ(!connectionResult.hasResolution());
        this.zzahj.zzh(connectionResult);
        this.zzahj.zzais.zzd(connectionResult);
    }

    /* access modifiers changed from: private */
    public Set<Scope> zzpA() {
        if (this.zzahz == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet(this.zzahz.zzqs());
        Map zzqu = this.zzahz.zzqu();
        for (Api api : zzqu.keySet()) {
            if (!this.zzahj.zzaio.containsKey(api.zzoR())) {
                hashSet.addAll(((com.google.android.gms.common.internal.zzf.zza) zzqu.get(api)).zzXf);
            }
        }
        return hashSet;
    }

    /* access modifiers changed from: private */
    public boolean zzpu() {
        this.zzahp--;
        if (this.zzahp > 0) {
            return false;
        }
        if (this.zzahp < 0) {
            Log.i("GoogleApiClientConnecting", this.zzahj.zzagW.zzpH());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            zzg(new ConnectionResult(8, null));
            return false;
        } else if (this.zzahm == null) {
            return true;
        } else {
            this.zzahj.zzair = this.zzahn;
            zzg(this.zzahm);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void zzpv() {
        if (this.zzahp == 0) {
            if (!this.zzahu || this.zzahv) {
                zzpw();
            }
        }
    }

    private void zzpw() {
        ArrayList arrayList = new ArrayList();
        this.zzaho = 1;
        this.zzahp = this.zzahj.zzahT.size();
        for (com.google.android.gms.common.api.Api.zzc zzc : this.zzahj.zzahT.keySet()) {
            if (!this.zzahj.zzaio.containsKey(zzc)) {
                arrayList.add(this.zzahj.zzahT.get(zzc));
            } else if (zzpu()) {
                zzpx();
            }
        }
        if (!arrayList.isEmpty()) {
            this.zzahB.add(zzm.zzpN().submit(new zzc(arrayList)));
        }
    }

    private void zzpx() {
        this.zzahj.zzpL();
        zzm.zzpN().execute(new Runnable() {
            public void run() {
                zzh.this.zzags.zzal(zzh.this.mContext);
            }
        });
        if (this.zzahs != null) {
            if (this.zzahx) {
                this.zzahs.zza(this.zzahw, this.zzahy);
            }
            zzZ(false);
        }
        for (com.google.android.gms.common.api.Api.zzc zzc : this.zzahj.zzaio.keySet()) {
            ((com.google.android.gms.common.api.Api.zzb) this.zzahj.zzahT.get(zzc)).disconnect();
        }
        this.zzahj.zzais.zzi(this.zzahq.isEmpty() ? null : this.zzahq);
    }

    /* access modifiers changed from: private */
    public void zzpy() {
        this.zzahu = false;
        this.zzahj.zzagW.zzahU = Collections.emptySet();
        for (com.google.android.gms.common.api.Api.zzc zzc : this.zzahr) {
            if (!this.zzahj.zzaio.containsKey(zzc)) {
                this.zzahj.zzaio.put(zzc, new ConnectionResult(17, null));
            }
        }
    }

    private void zzpz() {
        Iterator it = this.zzahB.iterator();
        while (it.hasNext()) {
            ((Future) it.next()).cancel(true);
        }
        this.zzahB.clear();
    }

    public void begin() {
        this.zzahj.zzaio.clear();
        this.zzahu = false;
        this.zzahm = null;
        this.zzaho = 0;
        this.zzaht = 2;
        this.zzahv = false;
        this.zzahx = false;
        HashMap hashMap = new HashMap();
        int i = 0;
        for (Api api : this.zzahA.keySet()) {
            com.google.android.gms.common.api.Api.zzb zzb = (com.google.android.gms.common.api.Api.zzb) this.zzahj.zzahT.get(api.zzoR());
            int intValue = ((Integer) this.zzahA.get(api)).intValue();
            int i2 = (api.zzoP().getPriority() == 1 ? 1 : 0) | i;
            if (zzb.zzmE()) {
                this.zzahu = true;
                if (intValue < this.zzaht) {
                    this.zzaht = intValue;
                }
                if (intValue != 0) {
                    this.zzahr.add(api.zzoR());
                }
            }
            hashMap.put(zzb, new zza(this, api, intValue));
            i = i2;
        }
        if (i != 0) {
            this.zzahu = false;
        }
        if (this.zzahu) {
            this.zzahz.zza(Integer.valueOf(this.zzahj.zzagW.getSessionId()));
            zze zze = new zze(this, null);
            this.zzahs = (zzrn) this.zzagt.zza(this.mContext, this.zzahj.zzagW.getLooper(), this.zzahz, this.zzahz.zzqy(), zze, zze);
        }
        this.zzahp = this.zzahj.zzahT.size();
        this.zzahB.add(zzm.zzpN().submit(new zzb(hashMap)));
    }

    public void connect() {
    }

    public boolean disconnect() {
        zzpz();
        zzZ(true);
        this.zzahj.zzh(null);
        return true;
    }

    public void onConnected(Bundle connectionHint) {
        if (zzbz(1)) {
            if (connectionHint != null) {
                this.zzahq.putAll(connectionHint);
            }
            if (zzpu()) {
                zzpx();
            }
        }
    }

    public void onConnectionSuspended(int cause) {
        zzg(new ConnectionResult(8, null));
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, R extends Result, T extends com.google.android.gms.common.api.internal.zza.zza<R, A>> T zza(T t) {
        this.zzahj.zzagW.zzahN.add(t);
        return t;
    }

    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
        if (zzbz(1)) {
            zzb(connectionResult, api, i);
            if (zzpu()) {
                zzpx();
            }
        }
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, T extends com.google.android.gms.common.api.internal.zza.zza<? extends Result, A>> T zzb(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
