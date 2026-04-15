package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult.zza;
import com.google.android.gms.common.api.internal.zzb;
import java.util.ArrayList;
import java.util.List;

public final class Batch extends zzb<BatchResult> {
    /* access modifiers changed from: private */
    public int zzafZ;
    /* access modifiers changed from: private */
    public boolean zzaga;
    /* access modifiers changed from: private */
    public boolean zzagb;
    /* access modifiers changed from: private|final */
    public final PendingResult<?>[] zzagc;
    /* access modifiers changed from: private|final */
    public final Object zzpV;

    public static final class Builder {
        private GoogleApiClient zzaaj;
        private List<PendingResult<?>> zzage = new ArrayList();

        public Builder(GoogleApiClient googleApiClient) {
            this.zzaaj = googleApiClient;
        }

        public <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken batchResultToken = new BatchResultToken(this.zzage.size());
            this.zzage.add(pendingResult);
            return batchResultToken;
        }

        public Batch build() {
            return new Batch(this.zzage, this.zzaaj, null);
        }
    }

    private Batch(List<PendingResult<?>> pendingResultList, GoogleApiClient apiClient) {
        super(apiClient);
        this.zzpV = new Object();
        this.zzafZ = pendingResultList.size();
        this.zzagc = new PendingResult[this.zzafZ];
        if (pendingResultList.isEmpty()) {
            zza((Result) new BatchResult(Status.zzagC, this.zzagc));
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < pendingResultList.size()) {
                PendingResult pendingResult = (PendingResult) pendingResultList.get(i2);
                this.zzagc[i2] = pendingResult;
                pendingResult.zza(new zza() {
                    /* JADX WARNING: Missing block: B:29:?, code skipped:
            return;
     */
                    public void zzu(com.google.android.gms.common.api.Status r6) {
                        /*
                        r5 = this;
                        r0 = com.google.android.gms.common.api.Batch.this;
                        r1 = r0.zzpV;
                        monitor-enter(r1);
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r0 = r0.isCanceled();	 Catch:{ all -> 0x0039 }
                        if (r0 == 0) goto L_0x0011;
                    L_0x000f:
                        monitor-exit(r1);	 Catch:{ all -> 0x0039 }
                    L_0x0010:
                        return;
                    L_0x0011:
                        r0 = r6.isCanceled();	 Catch:{ all -> 0x0039 }
                        if (r0 == 0) goto L_0x003c;
                    L_0x0017:
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r2 = 1;
                        r0.zzagb = r2;	 Catch:{ all -> 0x0039 }
                    L_0x001d:
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r0.zzafZ = r0.zzafZ - 1;	 Catch:{ all -> 0x0039 }
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r0 = r0.zzafZ;	 Catch:{ all -> 0x0039 }
                        if (r0 != 0) goto L_0x0037;
                    L_0x002a:
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r0 = r0.zzagb;	 Catch:{ all -> 0x0039 }
                        if (r0 == 0) goto L_0x0049;
                    L_0x0032:
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        com.google.android.gms.common.api.Batch.super.cancel();	 Catch:{ all -> 0x0039 }
                    L_0x0037:
                        monitor-exit(r1);	 Catch:{ all -> 0x0039 }
                        goto L_0x0010;
                    L_0x0039:
                        r0 = move-exception;
                        monitor-exit(r1);	 Catch:{ all -> 0x0039 }
                        throw r0;
                    L_0x003c:
                        r0 = r6.isSuccess();	 Catch:{ all -> 0x0039 }
                        if (r0 != 0) goto L_0x001d;
                    L_0x0042:
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r2 = 1;
                        r0.zzaga = r2;	 Catch:{ all -> 0x0039 }
                        goto L_0x001d;
                    L_0x0049:
                        r0 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r0 = r0.zzaga;	 Catch:{ all -> 0x0039 }
                        if (r0 == 0) goto L_0x0069;
                    L_0x0051:
                        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0039 }
                        r2 = 13;
                        r0.m2652init(r2);	 Catch:{ all -> 0x0039 }
                    L_0x0058:
                        r2 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r3 = new com.google.android.gms.common.api.BatchResult;	 Catch:{ all -> 0x0039 }
                        r4 = com.google.android.gms.common.api.Batch.this;	 Catch:{ all -> 0x0039 }
                        r4 = r4.zzagc;	 Catch:{ all -> 0x0039 }
                        r3.m2631init(r0, r4);	 Catch:{ all -> 0x0039 }
                        r2.zza(r3);	 Catch:{ all -> 0x0039 }
                        goto L_0x0037;
                    L_0x0069:
                        r0 = com.google.android.gms.common.api.Status.zzagC;	 Catch:{ all -> 0x0039 }
                        goto L_0x0058;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.Batch$AnonymousClass1.zzu(com.google.android.gms.common.api.Status):void");
                    }
                });
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* synthetic */ Batch(List x0, GoogleApiClient x1, AnonymousClass1 x2) {
        this(x0, x1);
    }

    public void cancel() {
        super.cancel();
        for (PendingResult cancel : this.zzagc) {
            cancel.cancel();
        }
    }

    /* renamed from: createFailedResult */
    public BatchResult zzc(Status status) {
        return new BatchResult(status, this.zzagc);
    }
}
