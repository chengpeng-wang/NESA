package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import java.lang.ref.WeakReference;

public class zzx<R extends Result> extends TransformedResult<R> implements ResultCallback<R> {
    /* access modifiers changed from: private|final */
    public final Object zzagI = new Object();
    /* access modifiers changed from: private|final */
    public final WeakReference<GoogleApiClient> zzagK;
    /* access modifiers changed from: private */
    public ResultTransform<? super R, ? extends Result> zzaiN = null;
    /* access modifiers changed from: private */
    public zzx<? extends Result> zzaiO = null;
    private ResultCallbacks<? super R> zzaiP = null;
    private PendingResult<R> zzaiQ = null;
    private Status zzaiR = null;
    /* access modifiers changed from: private|final */
    public final zza zzaiS;

    private final class zza extends Handler {
        public zza(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    PendingResult pendingResult = (PendingResult) msg.obj;
                    synchronized (zzx.this.zzagI) {
                        if (pendingResult == null) {
                            zzx.this.zzaiO.zzy(new Status(13, "Transform returned null"));
                        } else if (pendingResult instanceof zzt) {
                            zzx.this.zzaiO.zzy(((zzt) pendingResult).getStatus());
                        } else {
                            zzx.this.zzaiO.zza(pendingResult);
                        }
                    }
                    return;
                case 1:
                    RuntimeException runtimeException = (RuntimeException) msg.obj;
                    Log.e("TransformedResultImpl", "Runtime exception on the transformation worker thread: " + runtimeException.getMessage());
                    throw runtimeException;
                default:
                    Log.e("TransformedResultImpl", "TransformationResultHandler received unknown message type: " + msg.what);
                    return;
            }
        }
    }

    public zzx(WeakReference<GoogleApiClient> weakReference) {
        com.google.android.gms.common.internal.zzx.zzb((Object) weakReference, (Object) "GoogleApiClient reference must not be null");
        this.zzagK = weakReference;
        GoogleApiClient googleApiClient = (GoogleApiClient) this.zzagK.get();
        this.zzaiS = new zza(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
    }

    /* access modifiers changed from: private */
    public void zzc(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                Log.w("TransformedResultImpl", "Unable to release " + result, e);
            }
        }
    }

    private void zzpT() {
        if (this.zzaiN != null || this.zzaiP != null) {
            GoogleApiClient googleApiClient = (GoogleApiClient) this.zzagK.get();
            if (!(this.zzaiN == null || googleApiClient == null)) {
                googleApiClient.zza(this);
            }
            if (this.zzaiR != null) {
                zzz(this.zzaiR);
            } else if (this.zzaiQ != null) {
                this.zzaiQ.setResultCallback(this);
            }
        }
    }

    private boolean zzpV() {
        return (this.zzaiP == null || ((GoogleApiClient) this.zzagK.get()) == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public void zzy(Status status) {
        synchronized (this.zzagI) {
            this.zzaiR = status;
            zzz(this.zzaiR);
        }
    }

    private void zzz(Status status) {
        synchronized (this.zzagI) {
            if (this.zzaiN != null) {
                Object onFailure = this.zzaiN.onFailure(status);
                com.google.android.gms.common.internal.zzx.zzb(onFailure, (Object) "onFailure must not return null");
                this.zzaiO.zzy(onFailure);
            } else if (zzpV()) {
                this.zzaiP.onFailure(status);
            }
        }
    }

    public void andFinally(@NonNull ResultCallbacks<? super R> callbacks) {
        boolean z = true;
        synchronized (this.zzagI) {
            com.google.android.gms.common.internal.zzx.zza(this.zzaiP == null, (Object) "Cannot call andFinally() twice.");
            if (this.zzaiN != null) {
                z = false;
            }
            com.google.android.gms.common.internal.zzx.zza(z, (Object) "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zzaiP = callbacks;
            zzpT();
        }
    }

    public void onResult(final R result) {
        synchronized (this.zzagI) {
            if (!result.getStatus().isSuccess()) {
                zzy(result.getStatus());
                zzc((Result) result);
            } else if (this.zzaiN != null) {
                zzs.zzpN().submit(new Runnable() {
                    @WorkerThread
                    public void run() {
                        GoogleApiClient googleApiClient;
                        try {
                            zzx.this.zzaiS.sendMessage(zzx.this.zzaiS.obtainMessage(0, zzx.this.zzaiN.onSuccess(result)));
                            zzx.this.zzc(result);
                            googleApiClient = (GoogleApiClient) zzx.this.zzagK.get();
                            if (googleApiClient != null) {
                                googleApiClient.zzb(zzx.this);
                            }
                        } catch (RuntimeException e) {
                            zzx.this.zzaiS.sendMessage(zzx.this.zzaiS.obtainMessage(1, e));
                            zzx.this.zzc(result);
                            googleApiClient = (GoogleApiClient) zzx.this.zzagK.get();
                            if (googleApiClient != null) {
                                googleApiClient.zzb(zzx.this);
                            }
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            zzx.this.zzc(result);
                            googleApiClient = (GoogleApiClient) zzx.this.zzagK.get();
                            if (googleApiClient != null) {
                                googleApiClient.zzb(zzx.this);
                            }
                        }
                    }
                });
            } else if (zzpV()) {
                this.zzaiP.onSuccess(result);
            }
        }
    }

    @NonNull
    public <S extends Result> TransformedResult<S> then(@NonNull ResultTransform<? super R, ? extends S> transform) {
        zzx zzx;
        boolean z = true;
        synchronized (this.zzagI) {
            com.google.android.gms.common.internal.zzx.zza(this.zzaiN == null, (Object) "Cannot call then() twice.");
            if (this.zzaiP != null) {
                z = false;
            }
            com.google.android.gms.common.internal.zzx.zza(z, (Object) "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zzaiN = transform;
            zzx = new zzx(this.zzagK);
            this.zzaiO = zzx;
            zzpT();
        }
        return zzx;
    }

    public void zza(PendingResult<?> pendingResult) {
        synchronized (this.zzagI) {
            this.zzaiQ = pendingResult;
            zzpT();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zzpU() {
        synchronized (this.zzagI) {
            this.zzaiP = null;
        }
    }
}
