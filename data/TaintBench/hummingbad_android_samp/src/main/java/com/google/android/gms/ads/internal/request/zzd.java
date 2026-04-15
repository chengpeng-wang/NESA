package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.internal.zzbr;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzgl;
import com.google.android.gms.internal.zzgm;
import com.google.android.gms.internal.zzhq;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;

@zzgk
public abstract class zzd extends zzhq implements com.google.android.gms.ads.internal.request.zzc.zza {
    private AdResponseParcel zzCG;
    private final com.google.android.gms.ads.internal.request.zzc.zza zzDt;
    private final Object zzpc = new Object();
    private final AdRequestInfoParcel zzyd;

    @zzgk
    public static final class zza extends zzd {
        private final Context mContext;

        public zza(Context context, AdRequestInfoParcel adRequestInfoParcel, com.google.android.gms.ads.internal.request.zzc.zza zza) {
            super(adRequestInfoParcel, zza);
            this.mContext = context;
        }

        public void zzfx() {
        }

        public zzj zzfy() {
            return zzgm.zza(this.mContext, new zzbr((String) zzby.zztW.get()), zzgl.zzfG());
        }
    }

    @zzgk
    public static class zzb extends zzd implements ConnectionCallbacks, OnConnectionFailedListener {
        private Context mContext;
        private final com.google.android.gms.ads.internal.request.zzc.zza zzDt;
        protected zze zzDu;
        private boolean zzDv;
        private final Object zzpc = new Object();
        private AdRequestInfoParcel zzyd;

        public zzb(Context context, AdRequestInfoParcel adRequestInfoParcel, com.google.android.gms.ads.internal.request.zzc.zza zza) {
            Looper zzgB;
            super(adRequestInfoParcel, zza);
            this.mContext = context;
            this.zzyd = adRequestInfoParcel;
            this.zzDt = zza;
            if (((Boolean) zzby.zzuv.get()).booleanValue()) {
                this.zzDv = true;
                zzgB = zzp.zzbI().zzgB();
            } else {
                zzgB = context.getMainLooper();
            }
            this.zzDu = new zze(context, zzgB, this, this, adRequestInfoParcel.zzqb.zzIB);
            connect();
        }

        /* access modifiers changed from: protected */
        public void connect() {
            this.zzDu.zzox();
        }

        public void onConnected(Bundle connectionHint) {
            zzgn();
        }

        public void onConnectionFailed(ConnectionResult result) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Cannot connect to remote service, fallback to local instance.");
            zzfz().zzgn();
            Bundle bundle = new Bundle();
            bundle.putString("action", "gms_connection_failed_fallback_to_local");
            zzp.zzbx().zzb(this.mContext, this.zzyd.zzqb.zzIz, "gmob-apps", bundle, true);
        }

        public void onConnectionSuspended(int cause) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Disconnected from remote ad request service.");
        }

        public void zzfx() {
            synchronized (this.zzpc) {
                if (this.zzDu.isConnected() || this.zzDu.isConnecting()) {
                    this.zzDu.disconnect();
                }
                Binder.flushPendingCommands();
                if (this.zzDv) {
                    zzp.zzbI().zzgC();
                    this.zzDv = false;
                }
            }
        }

        public zzj zzfy() {
            zzj zzfC;
            synchronized (this.zzpc) {
                try {
                    zzfC = this.zzDu.zzfC();
                } catch (DeadObjectException | IllegalStateException e) {
                    zzfC = null;
                }
            }
            return zzfC;
        }

        /* access modifiers changed from: 0000 */
        public zzhq zzfz() {
            return new zza(this.mContext, this.zzyd, this.zzDt);
        }
    }

    public zzd(AdRequestInfoParcel adRequestInfoParcel, com.google.android.gms.ads.internal.request.zzc.zza zza) {
        this.zzyd = adRequestInfoParcel;
        this.zzDt = zza;
    }

    public final void onStop() {
        zzfx();
    }

    /* access modifiers changed from: 0000 */
    public boolean zza(zzj zzj, AdRequestInfoParcel adRequestInfoParcel) {
        try {
            zzj.zza(adRequestInfoParcel, new zzg(this));
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service.", e);
            zzp.zzbA().zzc(e, true);
        } catch (NullPointerException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e2);
            zzp.zzbA().zzc(e2, true);
        } catch (SecurityException e22) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e22);
            zzp.zzbA().zzc(e22, true);
        } catch (Throwable e222) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e222);
            zzp.zzbA().zzc(e222, true);
        }
        this.zzDt.zzb(new AdResponseParcel(0));
        return false;
    }

    public void zzb(AdResponseParcel adResponseParcel) {
        synchronized (this.zzpc) {
            this.zzCG = adResponseParcel;
            this.zzpc.notify();
        }
    }

    public void zzdG() {
        try {
            zzj zzfy = zzfy();
            if (zzfy == null) {
                this.zzDt.zzb(new AdResponseParcel(0));
            } else if (zza(zzfy, this.zzyd)) {
                zzi(zzp.zzbB().elapsedRealtime());
            }
            zzfx();
        } catch (Throwable th) {
            zzfx();
        }
    }

    /* access modifiers changed from: protected */
    public boolean zze(long j) {
        long elapsedRealtime = BuglyBroadcastRecevier.UPLOADLIMITED - (zzp.zzbB().elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpc.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public abstract void zzfx();

    public abstract zzj zzfy();

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            return;
     */
    public void zzi(long r5) {
        /*
        r4 = this;
        r1 = r4.zzpc;
        monitor-enter(r1);
    L_0x0003:
        r0 = r4.zzCG;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x0010;
    L_0x0007:
        r0 = r4.zzDt;	 Catch:{ all -> 0x0023 }
        r2 = r4.zzCG;	 Catch:{ all -> 0x0023 }
        r0.zzb(r2);	 Catch:{ all -> 0x0023 }
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
    L_0x000f:
        return;
    L_0x0010:
        r0 = r4.zze(r5);	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x0003;
    L_0x0016:
        r0 = r4.zzCG;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x0026;
    L_0x001a:
        r0 = r4.zzDt;	 Catch:{ all -> 0x0023 }
        r2 = r4.zzCG;	 Catch:{ all -> 0x0023 }
        r0.zzb(r2);	 Catch:{ all -> 0x0023 }
    L_0x0021:
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        goto L_0x000f;
    L_0x0023:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        throw r0;
    L_0x0026:
        r0 = r4.zzDt;	 Catch:{ all -> 0x0023 }
        r2 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x0023 }
        r3 = 0;
        r2.m1360init(r3);	 Catch:{ all -> 0x0023 }
        r0.zzb(r2);	 Catch:{ all -> 0x0023 }
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.request.zzd.zzi(long):void");
    }
}
