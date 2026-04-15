package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import java.util.Map;

@zzhb
public class zzeg {
    /* access modifiers changed from: private|final */
    public final Context mContext;
    /* access modifiers changed from: private|final */
    public final String zzAY;
    /* access modifiers changed from: private */
    public zzb<zzed> zzAZ;
    private zzb<zzed> zzBa;
    /* access modifiers changed from: private */
    public zze zzBb;
    /* access modifiers changed from: private */
    public int zzBc;
    /* access modifiers changed from: private|final */
    public final VersionInfoParcel zzpT;
    /* access modifiers changed from: private|final */
    public final Object zzpV;

    public interface zzb<T> {
        void zze(T t);
    }

    static class zza {
        static int zzBm = 60000;
        static int zzBn = 10000;
    }

    public static class zzc<T> implements zzb<T> {
        public void zze(T t) {
        }
    }

    public static class zzd extends zzjj<zzeh> {
        /* access modifiers changed from: private|final */
        public final zze zzBo;
        private boolean zzBp;
        private final Object zzpV = new Object();

        public zzd(zze zze) {
            this.zzBo = zze;
        }

        public void release() {
            synchronized (this.zzpV) {
                if (this.zzBp) {
                    return;
                }
                this.zzBp = true;
                zza(new com.google.android.gms.internal.zzji.zzc<zzeh>() {
                    /* renamed from: zzd */
                    public void zze(zzeh zzeh) {
                        zzin.v("Ending javascript session.");
                        ((zzei) zzeh).zzew();
                    }
                }, new com.google.android.gms.internal.zzji.zzb());
                zza(new com.google.android.gms.internal.zzji.zzc<zzeh>() {
                    /* renamed from: zzd */
                    public void zze(zzeh zzeh) {
                        zzin.v("Releasing engine reference.");
                        zzd.this.zzBo.zzet();
                    }
                }, new com.google.android.gms.internal.zzji.zza() {
                    public void run() {
                        zzd.this.zzBo.zzet();
                    }
                });
            }
        }
    }

    public static class zze extends zzjj<zzed> {
        /* access modifiers changed from: private */
        public zzb<zzed> zzBa;
        private boolean zzBr;
        private int zzBs;
        private final Object zzpV = new Object();

        public zze(zzb<zzed> zzb) {
            this.zzBa = zzb;
            this.zzBr = false;
            this.zzBs = 0;
        }

        public zzd zzes() {
            final zzd zzd = new zzd(this);
            synchronized (this.zzpV) {
                zza(new com.google.android.gms.internal.zzji.zzc<zzed>() {
                    /* renamed from: zza */
                    public void zze(zzed zzed) {
                        zzin.v("Getting a new session for JS Engine.");
                        zzd.zzh(zzed.zzen());
                    }
                }, new com.google.android.gms.internal.zzji.zza() {
                    public void run() {
                        zzin.v("Rejecting reference for JS Engine.");
                        zzd.reject();
                    }
                });
                zzx.zzab(this.zzBs >= 0);
                this.zzBs++;
            }
            return zzd;
        }

        /* access modifiers changed from: protected */
        public void zzet() {
            boolean z = true;
            synchronized (this.zzpV) {
                if (this.zzBs < 1) {
                    z = false;
                }
                zzx.zzab(z);
                zzin.v("Releasing 1 reference for JS Engine");
                this.zzBs--;
                zzev();
            }
        }

        public void zzeu() {
            boolean z = true;
            synchronized (this.zzpV) {
                if (this.zzBs < 0) {
                    z = false;
                }
                zzx.zzab(z);
                zzin.v("Releasing root reference. JS Engine will be destroyed once other references are released.");
                this.zzBr = true;
                zzev();
            }
        }

        /* access modifiers changed from: protected */
        public void zzev() {
            synchronized (this.zzpV) {
                zzx.zzab(this.zzBs >= 0);
                if (this.zzBr && this.zzBs == 0) {
                    zzin.v("No reference is left (including root). Cleaning up engine.");
                    zza(new com.google.android.gms.internal.zzji.zzc<zzed>() {
                        /* renamed from: zza */
                        public void zze(final zzed zzed) {
                            zzir.runOnUiThread(new Runnable() {
                                public void run() {
                                    zze.this.zzBa.zze(zzed);
                                    zzed.destroy();
                                }
                            });
                        }
                    }, new com.google.android.gms.internal.zzji.zzb());
                } else {
                    zzin.v("There are still references to the engine. Not destroying.");
                }
            }
        }
    }

    public zzeg(Context context, VersionInfoParcel versionInfoParcel, String str) {
        this.zzpV = new Object();
        this.zzBc = 1;
        this.zzAY = str;
        this.mContext = context.getApplicationContext();
        this.zzpT = versionInfoParcel;
        this.zzAZ = new zzc();
        this.zzBa = new zzc();
    }

    public zzeg(Context context, VersionInfoParcel versionInfoParcel, String str, zzb<zzed> zzb, zzb<zzed> zzb2) {
        this(context, versionInfoParcel, str);
        this.zzAZ = zzb;
        this.zzBa = zzb2;
    }

    private zze zzep() {
        final zze zze = new zze(this.zzBa);
        zzir.runOnUiThread(new Runnable() {
            public void run() {
                final zzed zza = zzeg.this.zza(zzeg.this.mContext, zzeg.this.zzpT);
                zza.zza(new com.google.android.gms.internal.zzed.zza() {
                    public void zzeo() {
                        zzir.zzMc.postDelayed(new Runnable() {
                            /* JADX WARNING: Missing block: B:14:?, code skipped:
            return;
     */
                            public void run() {
                                /*
                                r3 = this;
                                r0 = com.google.android.gms.internal.zzeg.1.AnonymousClass1.this;
                                r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;
                                r0 = com.google.android.gms.internal.zzeg.this;
                                r1 = r0.zzpV;
                                monitor-enter(r1);
                                r0 = com.google.android.gms.internal.zzeg.1.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = r0;	 Catch:{ all -> 0x003f }
                                r0 = r0.getStatus();	 Catch:{ all -> 0x003f }
                                r2 = -1;
                                if (r0 == r2) goto L_0x0025;
                            L_0x0018:
                                r0 = com.google.android.gms.internal.zzeg.1.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = r0;	 Catch:{ all -> 0x003f }
                                r0 = r0.getStatus();	 Catch:{ all -> 0x003f }
                                r2 = 1;
                                if (r0 != r2) goto L_0x0027;
                            L_0x0025:
                                monitor-exit(r1);	 Catch:{ all -> 0x003f }
                            L_0x0026:
                                return;
                            L_0x0027:
                                r0 = com.google.android.gms.internal.zzeg.1.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = r0;	 Catch:{ all -> 0x003f }
                                r0.reject();	 Catch:{ all -> 0x003f }
                                r0 = new com.google.android.gms.internal.zzeg$1$1$1$1;	 Catch:{ all -> 0x003f }
                                r0.m3127init();	 Catch:{ all -> 0x003f }
                                com.google.android.gms.internal.zzir.runOnUiThread(r0);	 Catch:{ all -> 0x003f }
                                r0 = "Could not receive loaded message in a timely manner. Rejecting.";
                                com.google.android.gms.internal.zzin.v(r0);	 Catch:{ all -> 0x003f }
                                monitor-exit(r1);	 Catch:{ all -> 0x003f }
                                goto L_0x0026;
                            L_0x003f:
                                r0 = move-exception;
                                monitor-exit(r1);	 Catch:{ all -> 0x003f }
                                throw r0;
                                */
                                throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeg$1$1$AnonymousClass1.run():void");
                            }
                        }, (long) zza.zzBn);
                    }
                });
                zza.zza("/jsLoaded", (zzdf) new zzdf() {
                    /* JADX WARNING: Missing block: B:14:?, code skipped:
            return;
     */
                    public void zza(com.google.android.gms.internal.zzjp r4, java.util.Map<java.lang.String, java.lang.String> r5) {
                        /*
                        r3 = this;
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;
                        r0 = com.google.android.gms.internal.zzeg.this;
                        r1 = r0.zzpV;
                        monitor-enter(r1);
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0;	 Catch:{ all -> 0x0051 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0051 }
                        r2 = -1;
                        if (r0 == r2) goto L_0x001f;
                    L_0x0014:
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0;	 Catch:{ all -> 0x0051 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0051 }
                        r2 = 1;
                        if (r0 != r2) goto L_0x0021;
                    L_0x001f:
                        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
                    L_0x0020:
                        return;
                    L_0x0021:
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzeg.this;	 Catch:{ all -> 0x0051 }
                        r2 = 0;
                        r0.zzBc = r2;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzeg.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0.zzAZ;	 Catch:{ all -> 0x0051 }
                        r2 = r0;	 Catch:{ all -> 0x0051 }
                        r0.zze(r2);	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0;	 Catch:{ all -> 0x0051 }
                        r2 = r0;	 Catch:{ all -> 0x0051 }
                        r0.zzh(r2);	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzeg.this;	 Catch:{ all -> 0x0051 }
                        r2 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r2 = r0;	 Catch:{ all -> 0x0051 }
                        r0.zzBb = r2;	 Catch:{ all -> 0x0051 }
                        r0 = "Successfully loaded JS Engine.";
                        com.google.android.gms.internal.zzin.v(r0);	 Catch:{ all -> 0x0051 }
                        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
                        goto L_0x0020;
                    L_0x0051:
                        r0 = move-exception;
                        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
                        throw r0;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeg$1$AnonymousClass2.zza(com.google.android.gms.internal.zzjp, java.util.Map):void");
                    }
                });
                final zzja zzja = new zzja();
                AnonymousClass3 anonymousClass3 = new zzdf() {
                    public void zza(zzjp zzjp, Map<String, String> map) {
                        synchronized (zzeg.this.zzpV) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzaJ("JS Engine is requesting an update");
                            if (zzeg.this.zzBc == 0) {
                                com.google.android.gms.ads.internal.util.client.zzb.zzaJ("Starting reload.");
                                zzeg.this.zzBc = 2;
                                zzeg.this.zzeq();
                            }
                            zza.zzb("/requestReload", (zzdf) zzja.get());
                        }
                    }
                };
                zzja.set(anonymousClass3);
                zza.zza("/requestReload", (zzdf) anonymousClass3);
                if (zzeg.this.zzAY.endsWith(".js")) {
                    zza.zzZ(zzeg.this.zzAY);
                } else if (zzeg.this.zzAY.startsWith("<html>")) {
                    zza.zzab(zzeg.this.zzAY);
                } else {
                    zza.zzaa(zzeg.this.zzAY);
                }
                zzir.zzMc.postDelayed(new Runnable() {
                    /* JADX WARNING: Missing block: B:14:?, code skipped:
            return;
     */
                    public void run() {
                        /*
                        r3 = this;
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;
                        r0 = com.google.android.gms.internal.zzeg.this;
                        r1 = r0.zzpV;
                        monitor-enter(r1);
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0037 }
                        r0 = r0;	 Catch:{ all -> 0x0037 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0037 }
                        r2 = -1;
                        if (r0 == r2) goto L_0x001f;
                    L_0x0014:
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0037 }
                        r0 = r0;	 Catch:{ all -> 0x0037 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0037 }
                        r2 = 1;
                        if (r0 != r2) goto L_0x0021;
                    L_0x001f:
                        monitor-exit(r1);	 Catch:{ all -> 0x0037 }
                    L_0x0020:
                        return;
                    L_0x0021:
                        r0 = com.google.android.gms.internal.zzeg.AnonymousClass1.this;	 Catch:{ all -> 0x0037 }
                        r0 = r0;	 Catch:{ all -> 0x0037 }
                        r0.reject();	 Catch:{ all -> 0x0037 }
                        r0 = new com.google.android.gms.internal.zzeg$1$4$1;	 Catch:{ all -> 0x0037 }
                        r0.m3132init();	 Catch:{ all -> 0x0037 }
                        com.google.android.gms.internal.zzir.runOnUiThread(r0);	 Catch:{ all -> 0x0037 }
                        r0 = "Could not receive loaded message in a timely manner. Rejecting.";
                        com.google.android.gms.internal.zzin.v(r0);	 Catch:{ all -> 0x0037 }
                        monitor-exit(r1);	 Catch:{ all -> 0x0037 }
                        goto L_0x0020;
                    L_0x0037:
                        r0 = move-exception;
                        monitor-exit(r1);	 Catch:{ all -> 0x0037 }
                        throw r0;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeg$1$AnonymousClass4.run():void");
                    }
                }, (long) zza.zzBm);
            }
        });
        return zze;
    }

    /* access modifiers changed from: protected */
    public zzed zza(Context context, VersionInfoParcel versionInfoParcel) {
        return new zzef(context, versionInfoParcel, null);
    }

    /* access modifiers changed from: protected */
    public zze zzeq() {
        final zze zzep = zzep();
        zzep.zza(new com.google.android.gms.internal.zzji.zzc<zzed>() {
            /* renamed from: zza */
            public void zze(zzed zzed) {
                synchronized (zzeg.this.zzpV) {
                    zzeg.this.zzBc = 0;
                    if (!(zzeg.this.zzBb == null || zzep == zzeg.this.zzBb)) {
                        zzin.v("New JS engine is loaded, marking previous one as destroyable.");
                        zzeg.this.zzBb.zzeu();
                    }
                    zzeg.this.zzBb = zzep;
                }
            }
        }, new com.google.android.gms.internal.zzji.zza() {
            public void run() {
                synchronized (zzeg.this.zzpV) {
                    zzeg.this.zzBc = 1;
                    zzin.v("Failed loading new engine. Marking new engine destroyable.");
                    zzep.zzeu();
                }
            }
        });
        return zzep;
    }

    public zzd zzer() {
        zzd zzes;
        synchronized (this.zzpV) {
            if (this.zzBb == null || this.zzBb.getStatus() == -1) {
                this.zzBc = 2;
                this.zzBb = zzeq();
                zzes = this.zzBb.zzes();
            } else if (this.zzBc == 0) {
                zzes = this.zzBb.zzes();
            } else if (this.zzBc == 1) {
                this.zzBc = 2;
                zzeq();
                zzes = this.zzBb.zzes();
            } else if (this.zzBc == 2) {
                zzes = this.zzBb.zzes();
            } else {
                zzes = this.zzBb.zzes();
            }
        }
        return zzes;
    }
}
