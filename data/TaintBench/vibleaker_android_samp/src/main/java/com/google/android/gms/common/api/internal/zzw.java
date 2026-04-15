package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.zzc;
import com.google.android.gms.common.zze;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzw extends Fragment implements OnCancelListener {
    /* access modifiers changed from: private */
    public boolean mStarted;
    /* access modifiers changed from: private */
    public int zzaiA = -1;
    /* access modifiers changed from: private */
    public ConnectionResult zzaiB;
    /* access modifiers changed from: private|final */
    public final Handler zzaiC = new Handler(Looper.getMainLooper());
    protected zzn zzaiD;
    private final SparseArray<zza> zzaiE = new SparseArray();
    /* access modifiers changed from: private */
    public boolean zzaiz;

    private class zza implements OnConnectionFailedListener {
        public final int zzaiF;
        public final GoogleApiClient zzaiG;
        public final OnConnectionFailedListener zzaiH;

        public zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzaiF = i;
            this.zzaiG = googleApiClient;
            this.zzaiH = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.append(prefix).append("GoogleApiClient #").print(this.zzaiF);
            writer.println(":");
            this.zzaiG.dump(prefix + "  ", fd, writer, args);
        }

        public void onConnectionFailed(@NonNull ConnectionResult result) {
            zzw.this.zzaiC.post(new zzb(this.zzaiF, result));
        }

        public void zzpR() {
            this.zzaiG.unregisterConnectionFailedListener(this);
            this.zzaiG.disconnect();
        }
    }

    private class zzb implements Runnable {
        private final int zzaiJ;
        private final ConnectionResult zzaiK;

        public zzb(int i, ConnectionResult connectionResult) {
            this.zzaiJ = i;
            this.zzaiK = connectionResult;
        }

        @MainThread
        public void run() {
            if (zzw.this.mStarted && !zzw.this.zzaiz) {
                zzw.this.zzaiz = true;
                zzw.this.zzaiA = this.zzaiJ;
                zzw.this.zzaiB = this.zzaiK;
                if (this.zzaiK.hasResolution()) {
                    try {
                        this.zzaiK.startResolutionForResult(zzw.this.getActivity(), ((zzw.this.getActivity().getSupportFragmentManager().getFragments().indexOf(zzw.this) + 1) << 16) + 1);
                    } catch (SendIntentException e) {
                        zzw.this.zzpP();
                    }
                } else if (zzw.this.zzpQ().isUserResolvableError(this.zzaiK.getErrorCode())) {
                    zzw.this.zzb(this.zzaiJ, this.zzaiK);
                } else if (this.zzaiK.getErrorCode() == 18) {
                    zzw.this.zzc(this.zzaiJ, this.zzaiK);
                } else {
                    zzw.this.zza(this.zzaiJ, this.zzaiK);
                }
            }
        }
    }

    @Nullable
    public static zzw zza(FragmentActivity fragmentActivity) {
        zzx.zzcD("Must be called from main thread of process");
        try {
            zzw zzw = (zzw) fragmentActivity.getSupportFragmentManager().findFragmentByTag("GmsSupportLifecycleFrag");
            return (zzw == null || zzw.isRemoving()) ? null : zzw;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFrag is not a SupportLifecycleFragment", e);
        }
    }

    /* access modifiers changed from: private */
    public void zza(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Unresolved error while connecting client. Stopping auto-manage.");
        zza zza = (zza) this.zzaiE.get(i);
        if (zza != null) {
            zzbD(i);
            OnConnectionFailedListener onConnectionFailedListener = zza.zzaiH;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zzpP();
    }

    public static zzw zzb(FragmentActivity fragmentActivity) {
        zzw zza = zza(fragmentActivity);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (zza == null) {
            zza = zzpO();
            if (zza == null) {
                Log.w("GmsSupportLifecycleFrag", "Unable to find connection error message resources (Did you include play-services-base and the proper proguard rules?); error dialogs may be unavailable.");
                zza = new zzw();
            }
            supportFragmentManager.beginTransaction().add((Fragment) zza, "GmsSupportLifecycleFrag").commitAllowingStateLoss();
            supportFragmentManager.executePendingTransactions();
        }
        return zza;
    }

    private static String zzi(ConnectionResult connectionResult) {
        return connectionResult.getErrorMessage() + " (" + connectionResult.getErrorCode() + ": " + zze.getErrorString(connectionResult.getErrorCode()) + ')';
    }

    @Nullable
    private static zzw zzpO() {
        Class cls;
        try {
            cls = Class.forName("com.google.android.gms.common.api.internal.SupportLifecycleFragmentImpl");
        } catch (ClassNotFoundException | LinkageError | SecurityException e) {
            if (Log.isLoggable("GmsSupportLifecycleFrag", 3)) {
                Log.d("GmsSupportLifecycleFrag", "Unable to find SupportLifecycleFragmentImpl class", e);
            }
            cls = null;
        }
        if (cls != null) {
            try {
                return (zzw) cls.newInstance();
            } catch (ExceptionInInitializerError | IllegalAccessException | InstantiationException | RuntimeException e2) {
                if (Log.isLoggable("GmsSupportLifecycleFrag", 3)) {
                    Log.d("GmsSupportLifecycleFrag", "Unable to instantiate SupportLifecycleFragmentImpl class", e2);
                }
            }
        }
        return null;
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.zzaiE.size()) {
                ((zza) this.zzaiE.valueAt(i2)).dump(prefix, fd, writer, args);
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: Missing block: B:6:0x0018, code skipped:
            if (zzpQ().isGooglePlayServicesAvailable(getActivity()) == 0) goto L_0x0006;
     */
    public void onActivityResult(int r5, int r6, android.content.Intent r7) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        switch(r5) {
            case 1: goto L_0x001b;
            case 2: goto L_0x000c;
            default: goto L_0x0005;
        };
    L_0x0005:
        r0 = r1;
    L_0x0006:
        if (r0 == 0) goto L_0x002b;
    L_0x0008:
        r4.zzpP();
    L_0x000b:
        return;
    L_0x000c:
        r2 = r4.zzpQ();
        r3 = r4.getActivity();
        r2 = r2.isGooglePlayServicesAvailable(r3);
        if (r2 != 0) goto L_0x0005;
    L_0x001a:
        goto L_0x0006;
    L_0x001b:
        r2 = -1;
        if (r6 == r2) goto L_0x0006;
    L_0x001e:
        if (r6 != 0) goto L_0x0005;
    L_0x0020:
        r0 = new com.google.android.gms.common.ConnectionResult;
        r2 = 13;
        r3 = 0;
        r0.m2615init(r2, r3);
        r4.zzaiB = r0;
        goto L_0x0005;
    L_0x002b:
        r0 = r4.zzaiA;
        r1 = r4.zzaiB;
        r4.zza(r0, r1);
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzw.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(this.zzaiA, new ConnectionResult(13, null));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.zzaiz = savedInstanceState.getBoolean("resolving_error", false);
            this.zzaiA = savedInstanceState.getInt("failed_client_id", -1);
            if (this.zzaiA >= 0) {
                this.zzaiB = new ConnectionResult(savedInstanceState.getInt("failed_status"), (PendingIntent) savedInstanceState.getParcelable("failed_resolution"));
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resolving_error", this.zzaiz);
        if (this.zzaiA >= 0) {
            outState.putInt("failed_client_id", this.zzaiA);
            outState.putInt("failed_status", this.zzaiB.getErrorCode());
            outState.putParcelable("failed_resolution", this.zzaiB.getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (!this.zzaiz) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.zzaiE.size()) {
                    ((zza) this.zzaiE.valueAt(i2)).zzaiG.connect();
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public void onStop() {
        boolean z = false;
        super.onStop();
        this.mStarted = false;
        while (true) {
            boolean z2 = z;
            if (z2 < this.zzaiE.size()) {
                ((zza) this.zzaiE.valueAt(z2)).zzaiG.disconnect();
                z = z2 + 1;
            } else {
                return;
            }
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb((Object) googleApiClient, (Object) "GoogleApiClient instance cannot be null");
        zzx.zza(this.zzaiE.indexOfKey(i) < 0, "Already managing a GoogleApiClient with id " + i);
        this.zzaiE.put(i, new zza(i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && !this.zzaiz) {
            googleApiClient.connect();
        }
    }

    /* access modifiers changed from: protected */
    public void zzb(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Failed to connect due to user resolvable error " + zzi(connectionResult));
        zza(i, connectionResult);
    }

    public void zzbD(int i) {
        zza zza = (zza) this.zzaiE.get(i);
        this.zzaiE.remove(i);
        if (zza != null) {
            zza.zzpR();
        }
    }

    /* access modifiers changed from: protected */
    public void zzc(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Unable to connect, GooglePlayServices is updating.");
        zza(i, connectionResult);
    }

    /* access modifiers changed from: protected */
    public void zzpP() {
        boolean z = false;
        this.zzaiz = false;
        this.zzaiA = -1;
        this.zzaiB = null;
        if (this.zzaiD != null) {
            this.zzaiD.unregister();
            this.zzaiD = null;
        }
        while (true) {
            boolean z2 = z;
            if (z2 < this.zzaiE.size()) {
                ((zza) this.zzaiE.valueAt(z2)).zzaiG.connect();
                z = z2 + 1;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public zzc zzpQ() {
        return zzc.zzoK();
    }
}
