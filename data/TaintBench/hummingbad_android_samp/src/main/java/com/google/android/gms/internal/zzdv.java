package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@zzgk
public class zzdv {
    /* access modifiers changed from: private|final */
    public final Context mContext;
    /* access modifiers changed from: private|final */
    public final VersionInfoParcel zzpa;
    /* access modifiers changed from: private|final */
    public final Object zzpc;
    /* access modifiers changed from: private|final */
    public final String zzxF;
    /* access modifiers changed from: private */
    public zzb<zzbb> zzxG;
    private zzb<zzbb> zzxH;
    /* access modifiers changed from: private */
    public zze zzxI;
    /* access modifiers changed from: private */
    public int zzxJ;

    static class zza {
        static int zzxT = 60000;
        static int zzxU = 10000;
    }

    public interface zzb<T> {
        void zzc(T t);
    }

    public static class zzc<T> implements zzb<T> {
        public void zzc(T t) {
        }
    }

    public static class zzd extends zzik<zzbe> {
        private final Object zzpc = new Object();
        /* access modifiers changed from: private|final */
        public final zze zzxV;
        private boolean zzxW;

        public zzd(zze zze) {
            this.zzxV = zze;
        }

        public void release() {
            synchronized (this.zzpc) {
                if (this.zzxW) {
                    return;
                }
                this.zzxW = true;
                zza(new com.google.android.gms.internal.zzij.zzc<zzbe>() {
                    /* renamed from: zzb */
                    public void zzc(zzbe zzbe) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Ending javascript session.");
                        ((zzbf) zzbe).zzck();
                    }
                }, new com.google.android.gms.internal.zzij.zzb());
                zza(new com.google.android.gms.internal.zzij.zzc<zzbe>() {
                    /* renamed from: zzb */
                    public void zzc(zzbe zzbe) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Releasing engine reference.");
                        zzd.this.zzxV.zzdN();
                    }
                }, new com.google.android.gms.internal.zzij.zza() {
                    public void run() {
                        zzd.this.zzxV.zzdN();
                    }
                });
            }
        }
    }

    public static class zze extends zzik<zzbb> {
        private final Object zzpc = new Object();
        /* access modifiers changed from: private */
        public zzb<zzbb> zzxH;
        private boolean zzxY;
        private int zzxZ;

        public zze(zzb<zzbb> zzb) {
            this.zzxH = zzb;
            this.zzxY = false;
            this.zzxZ = 0;
        }

        public zzd zzdM() {
            final zzd zzd = new zzd(this);
            synchronized (this.zzpc) {
                zza(new com.google.android.gms.internal.zzij.zzc<zzbb>() {
                    /* renamed from: zza */
                    public void zzc(zzbb zzbb) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Getting a new session for JS Engine.");
                        zzd.zzg(zzbb.zzci());
                    }
                }, new com.google.android.gms.internal.zzij.zza() {
                    public void run() {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Rejecting reference for JS Engine.");
                        zzd.reject();
                    }
                });
                zzx.zzY(this.zzxZ >= 0);
                this.zzxZ++;
            }
            return zzd;
        }

        /* access modifiers changed from: protected */
        public void zzdN() {
            boolean z = true;
            synchronized (this.zzpc) {
                if (this.zzxZ < 1) {
                    z = false;
                }
                zzx.zzY(z);
                com.google.android.gms.ads.internal.util.client.zzb.v("Releasing 1 reference for JS Engine");
                this.zzxZ--;
                zzdP();
            }
        }

        public void zzdO() {
            boolean z = true;
            synchronized (this.zzpc) {
                if (this.zzxZ < 0) {
                    z = false;
                }
                zzx.zzY(z);
                com.google.android.gms.ads.internal.util.client.zzb.v("Releasing root reference. JS Engine will be destroyed once other references are released.");
                this.zzxY = true;
                zzdP();
            }
        }

        /* access modifiers changed from: protected */
        public void zzdP() {
            synchronized (this.zzpc) {
                zzx.zzY(this.zzxZ >= 0);
                if (this.zzxY && this.zzxZ == 0) {
                    com.google.android.gms.ads.internal.util.client.zzb.v("No reference is left (including root). Cleaning up engine.");
                    zza(new com.google.android.gms.internal.zzij.zzc<zzbb>() {
                        /* renamed from: zza */
                        public void zzc(final zzbb zzbb) {
                            zzhu.runOnUiThread(new Runnable() {
                                public void run() {
                                    zze.this.zzxH.zzc(zzbb);
                                    zzbb.destroy();
                                }
                            });
                        }
                    }, new com.google.android.gms.internal.zzij.zzb());
                } else {
                    com.google.android.gms.ads.internal.util.client.zzb.v("There are still references to the engine. Not destroying.");
                }
            }
        }
    }

    public zzdv(Context context, VersionInfoParcel versionInfoParcel, String str) {
        this.zzpc = new Object();
        this.zzxJ = 1;
        this.zzxF = str;
        this.mContext = context.getApplicationContext();
        this.zzpa = versionInfoParcel;
        this.zzxG = new zzc();
        this.zzxH = new zzc();
    }

    public zzdv(Context context, VersionInfoParcel versionInfoParcel, String str, zzb<zzbb> zzb, zzb<zzbb> zzb2) {
        this(context, versionInfoParcel, str);
        this.zzxG = zzb;
        this.zzxH = zzb2;
    }

    private zze zzdJ() {
        final zze zze = new zze(this.zzxH);
        zzhu.runOnUiThread(new Runnable() {
            public void run() {
                final zzbb zza = zzdv.this.zza(zzdv.this.mContext, zzdv.this.zzpa);
                zza.zza(new com.google.android.gms.internal.zzbb.zza() {
                    public void zzcj() {
                        new Timer().schedule(new TimerTask() {
                            /* JADX WARNING: Missing block: B:14:?, code skipped:
            return;
     */
                            public void run() {
                                /*
                                r3 = this;
                                r0 = com.google.android.gms.internal.zzdv.1.AnonymousClass1.this;
                                r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;
                                r0 = com.google.android.gms.internal.zzdv.this;
                                r1 = r0.zzpc;
                                monitor-enter(r1);
                                r0 = com.google.android.gms.internal.zzdv.1.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = r0;	 Catch:{ all -> 0x003f }
                                r0 = r0.getStatus();	 Catch:{ all -> 0x003f }
                                r2 = -1;
                                if (r0 == r2) goto L_0x0025;
                            L_0x0018:
                                r0 = com.google.android.gms.internal.zzdv.1.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = r0;	 Catch:{ all -> 0x003f }
                                r0 = r0.getStatus();	 Catch:{ all -> 0x003f }
                                r2 = 1;
                                if (r0 != r2) goto L_0x0027;
                            L_0x0025:
                                monitor-exit(r1);	 Catch:{ all -> 0x003f }
                            L_0x0026:
                                return;
                            L_0x0027:
                                r0 = com.google.android.gms.internal.zzdv.1.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x003f }
                                r0 = r0;	 Catch:{ all -> 0x003f }
                                r0.reject();	 Catch:{ all -> 0x003f }
                                r0 = new com.google.android.gms.internal.zzdv$1$1$1$1;	 Catch:{ all -> 0x003f }
                                r0.m1497init();	 Catch:{ all -> 0x003f }
                                com.google.android.gms.internal.zzhu.runOnUiThread(r0);	 Catch:{ all -> 0x003f }
                                r0 = "Could not receive loaded message in a timely manner. Rejecting.";
                                com.google.android.gms.ads.internal.util.client.zzb.v(r0);	 Catch:{ all -> 0x003f }
                                monitor-exit(r1);	 Catch:{ all -> 0x003f }
                                goto L_0x0026;
                            L_0x003f:
                                r0 = move-exception;
                                monitor-exit(r1);	 Catch:{ all -> 0x003f }
                                throw r0;
                                */
                                throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdv$1$1$AnonymousClass1.run():void");
                            }
                        }, (long) zza.zzxU);
                    }
                });
                zza.zza("/jsLoaded", (zzdg) new zzdg() {
                    /* JADX WARNING: Missing block: B:14:?, code skipped:
            return;
     */
                    public void zza(com.google.android.gms.internal.zzip r4, java.util.Map<java.lang.String, java.lang.String> r5) {
                        /*
                        r3 = this;
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;
                        r0 = com.google.android.gms.internal.zzdv.this;
                        r1 = r0.zzpc;
                        monitor-enter(r1);
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0;	 Catch:{ all -> 0x0051 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0051 }
                        r2 = -1;
                        if (r0 == r2) goto L_0x001f;
                    L_0x0014:
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0;	 Catch:{ all -> 0x0051 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0051 }
                        r2 = 1;
                        if (r0 != r2) goto L_0x0021;
                    L_0x001f:
                        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
                    L_0x0020:
                        return;
                    L_0x0021:
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzdv.this;	 Catch:{ all -> 0x0051 }
                        r2 = 0;
                        r0.zzxJ = r2;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzdv.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0.zzxG;	 Catch:{ all -> 0x0051 }
                        r2 = r0;	 Catch:{ all -> 0x0051 }
                        r0.zzc(r2);	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = r0;	 Catch:{ all -> 0x0051 }
                        r2 = r0;	 Catch:{ all -> 0x0051 }
                        r0.zzg(r2);	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r0 = com.google.android.gms.internal.zzdv.this;	 Catch:{ all -> 0x0051 }
                        r2 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0051 }
                        r2 = r0;	 Catch:{ all -> 0x0051 }
                        r0.zzxI = r2;	 Catch:{ all -> 0x0051 }
                        r0 = "Successfully loaded JS Engine.";
                        com.google.android.gms.ads.internal.util.client.zzb.v(r0);	 Catch:{ all -> 0x0051 }
                        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
                        goto L_0x0020;
                    L_0x0051:
                        r0 = move-exception;
                        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
                        throw r0;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdv$1$AnonymousClass2.zza(com.google.android.gms.internal.zzip, java.util.Map):void");
                    }
                });
                final zzic zzic = new zzic();
                AnonymousClass3 anonymousClass3 = new zzdg() {
                    public void zza(zzip zzip, Map<String, String> map) {
                        synchronized (zzdv.this.zzpc) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzaD("JS Engine is requesting an update");
                            if (zzdv.this.zzxJ == 0) {
                                com.google.android.gms.ads.internal.util.client.zzb.zzaD("Starting reload.");
                                zzdv.this.zzxJ = 2;
                                zzdv.this.zzdK();
                            }
                            zza.zzb("/requestReload", (zzdg) zzic.get());
                        }
                    }
                };
                zzic.set(anonymousClass3);
                zza.zza("/requestReload", (zzdg) anonymousClass3);
                if (zzdv.this.zzxF.endsWith(".js")) {
                    zza.zzs(zzdv.this.zzxF);
                } else if (zzdv.this.zzxF.startsWith("<html>")) {
                    zza.zzu(zzdv.this.zzxF);
                } else {
                    zza.zzt(zzdv.this.zzxF);
                }
                new Timer().schedule(new TimerTask() {
                    /* JADX WARNING: Missing block: B:14:?, code skipped:
            return;
     */
                    public void run() {
                        /*
                        r3 = this;
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;
                        r0 = com.google.android.gms.internal.zzdv.this;
                        r1 = r0.zzpc;
                        monitor-enter(r1);
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0037 }
                        r0 = r0;	 Catch:{ all -> 0x0037 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0037 }
                        r2 = -1;
                        if (r0 == r2) goto L_0x001f;
                    L_0x0014:
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0037 }
                        r0 = r0;	 Catch:{ all -> 0x0037 }
                        r0 = r0.getStatus();	 Catch:{ all -> 0x0037 }
                        r2 = 1;
                        if (r0 != r2) goto L_0x0021;
                    L_0x001f:
                        monitor-exit(r1);	 Catch:{ all -> 0x0037 }
                    L_0x0020:
                        return;
                    L_0x0021:
                        r0 = com.google.android.gms.internal.zzdv.AnonymousClass1.this;	 Catch:{ all -> 0x0037 }
                        r0 = r0;	 Catch:{ all -> 0x0037 }
                        r0.reject();	 Catch:{ all -> 0x0037 }
                        r0 = new com.google.android.gms.internal.zzdv$1$4$1;	 Catch:{ all -> 0x0037 }
                        r0.m1499init();	 Catch:{ all -> 0x0037 }
                        com.google.android.gms.internal.zzhu.runOnUiThread(r0);	 Catch:{ all -> 0x0037 }
                        r0 = "Could not receive loaded message in a timely manner. Rejecting.";
                        com.google.android.gms.ads.internal.util.client.zzb.v(r0);	 Catch:{ all -> 0x0037 }
                        monitor-exit(r1);	 Catch:{ all -> 0x0037 }
                        goto L_0x0020;
                    L_0x0037:
                        r0 = move-exception;
                        monitor-exit(r1);	 Catch:{ all -> 0x0037 }
                        throw r0;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdv$1$AnonymousClass4.run():void");
                    }
                }, (long) zza.zzxT);
            }
        });
        return zze;
    }

    /* access modifiers changed from: protected */
    public zzbb zza(Context context, VersionInfoParcel versionInfoParcel) {
        return new zzbd(context, versionInfoParcel, null);
    }

    /* access modifiers changed from: protected */
    public zze zzdK() {
        final zze zzdJ = zzdJ();
        zzdJ.zza(new com.google.android.gms.internal.zzij.zzc<zzbb>() {
            /* renamed from: zza */
            public void zzc(zzbb zzbb) {
                synchronized (zzdv.this.zzpc) {
                    zzdv.this.zzxJ = 0;
                    if (!(zzdv.this.zzxI == null || zzdJ == zzdv.this.zzxI)) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("New JS engine is loaded, marking previous one as destroyable.");
                        zzdv.this.zzxI.zzdO();
                    }
                    zzdv.this.zzxI = zzdJ;
                }
            }
        }, new com.google.android.gms.internal.zzij.zza() {
            public void run() {
                synchronized (zzdv.this.zzpc) {
                    zzdv.this.zzxJ = 1;
                    com.google.android.gms.ads.internal.util.client.zzb.v("Failed loading new engine. Marking new engine destroyable.");
                    zzdJ.zzdO();
                }
            }
        });
        return zzdJ;
    }

    public zzd zzdL() {
        zzd zzdM;
        synchronized (this.zzpc) {
            if (this.zzxI == null || this.zzxI.getStatus() == -1) {
                this.zzxJ = 2;
                this.zzxI = zzdK();
                zzdM = this.zzxI.zzdM();
            } else if (this.zzxJ == 0) {
                zzdM = this.zzxI.zzdM();
            } else if (this.zzxJ == 1) {
                this.zzxJ = 2;
                zzdK();
                zzdM = this.zzxI.zzdM();
            } else if (this.zzxJ == 2) {
                zzdM = this.zzxI.zzdM();
            } else {
                zzdM = this.zzxI.zzdM();
            }
        }
        return zzdM;
    }
}
