package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.internal.zzbm;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzhc;
import com.google.android.gms.internal.zzhd;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.internal.zzji;
import com.google.android.gms.internal.zzji.zzc;

@zzhb
public abstract class zzd implements com.google.android.gms.ads.internal.request.zzc.zza, zzit<Void> {
    private final zzji<AdRequestInfoParcel> zzHl;
    private final com.google.android.gms.ads.internal.request.zzc.zza zzHm;
    private final Object zzpV = new Object();

    @zzhb
    public static final class zza extends zzd {
        private final Context mContext;

        public zza(Context context, zzji<AdRequestInfoParcel> zzji, com.google.android.gms.ads.internal.request.zzc.zza zza) {
            super(zzji, zza);
            this.mContext = context;
        }

        public /* synthetic */ Object zzgd() {
            return zzd.super.zzgd();
        }

        public void zzgr() {
        }

        public zzj zzgs() {
            return zzhd.zza(this.mContext, new zzbm((String) zzbt.zzvB.get()), zzhc.zzgA());
        }
    }

    @zzhb
    public static class zzb extends zzd implements ConnectionCallbacks, OnConnectionFailedListener {
        private Context mContext;
        private zzji<AdRequestInfoParcel> zzHl;
        private final com.google.android.gms.ads.internal.request.zzc.zza zzHm;
        protected zze zzHp;
        private boolean zzHq;
        private VersionInfoParcel zzpT;
        private final Object zzpV = new Object();

        public zzb(Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> zzji, com.google.android.gms.ads.internal.request.zzc.zza zza) {
            Looper zzhC;
            super(zzji, zza);
            this.mContext = context;
            this.zzpT = versionInfoParcel;
            this.zzHl = zzji;
            this.zzHm = zza;
            if (((Boolean) zzbt.zzwa.get()).booleanValue()) {
                this.zzHq = true;
                zzhC = zzr.zzbO().zzhC();
            } else {
                zzhC = context.getMainLooper();
            }
            this.zzHp = new zze(context, zzhC, this, this, this.zzpT.zzNa);
            connect();
        }

        /* access modifiers changed from: protected */
        public void connect() {
            this.zzHp.zzqG();
        }

        public void onConnected(Bundle connectionHint) {
            zzgd();
        }

        public void onConnectionFailed(@NonNull ConnectionResult result) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Cannot connect to remote service, fallback to local instance.");
            zzgt().zzgd();
            Bundle bundle = new Bundle();
            bundle.putString("action", "gms_connection_failed_fallback_to_local");
            zzr.zzbC().zzb(this.mContext, this.zzpT.afmaVersion, "gmob-apps", bundle, true);
        }

        public void onConnectionSuspended(int cause) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Disconnected from remote ad request service.");
        }

        public /* synthetic */ Object zzgd() {
            return zzd.super.zzgd();
        }

        public void zzgr() {
            synchronized (this.zzpV) {
                if (this.zzHp.isConnected() || this.zzHp.isConnecting()) {
                    this.zzHp.disconnect();
                }
                Binder.flushPendingCommands();
                if (this.zzHq) {
                    zzr.zzbO().zzhD();
                    this.zzHq = false;
                }
            }
        }

        public zzj zzgs() {
            zzj zzgw;
            synchronized (this.zzpV) {
                try {
                    zzgw = this.zzHp.zzgw();
                } catch (DeadObjectException | IllegalStateException e) {
                    zzgw = null;
                }
            }
            return zzgw;
        }

        /* access modifiers changed from: 0000 */
        public zzit zzgt() {
            return new zza(this.mContext, this.zzHl, this.zzHm);
        }
    }

    public zzd(zzji<AdRequestInfoParcel> zzji, com.google.android.gms.ads.internal.request.zzc.zza zza) {
        this.zzHl = zzji;
        this.zzHm = zza;
    }

    public void cancel() {
        zzgr();
    }

    /* access modifiers changed from: 0000 */
    public boolean zza(zzj zzj, AdRequestInfoParcel adRequestInfoParcel) {
        try {
            zzj.zza(adRequestInfoParcel, new zzg(this));
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service.", e);
            zzr.zzbF().zzb(e, true);
        } catch (NullPointerException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e2);
            zzr.zzbF().zzb(e2, true);
        } catch (SecurityException e22) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e22);
            zzr.zzbF().zzb(e22, true);
        } catch (Throwable e222) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e222);
            zzr.zzbF().zzb(e222, true);
        }
        this.zzHm.zzb(new AdResponseParcel(0));
        return false;
    }

    public void zzb(AdResponseParcel adResponseParcel) {
        synchronized (this.zzpV) {
            this.zzHm.zzb(adResponseParcel);
            zzgr();
        }
    }

    /* renamed from: zzga */
    public Void zzgd() {
        final zzj zzgs = zzgs();
        if (zzgs == null) {
            this.zzHm.zzb(new AdResponseParcel(0));
            zzgr();
        } else {
            this.zzHl.zza(new zzc<AdRequestInfoParcel>() {
                /* renamed from: zzc */
                public void zze(AdRequestInfoParcel adRequestInfoParcel) {
                    if (!zzd.this.zza(zzgs, adRequestInfoParcel)) {
                        zzd.this.zzgr();
                    }
                }
            }, new com.google.android.gms.internal.zzji.zza() {
                public void run() {
                    zzd.this.zzgr();
                }
            });
        }
        return null;
    }

    public abstract void zzgr();

    public abstract zzj zzgs();
}
