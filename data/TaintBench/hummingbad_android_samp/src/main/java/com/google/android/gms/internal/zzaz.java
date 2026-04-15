package com.google.android.gms.internal;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzdv.zzd;
import com.google.android.gms.internal.zzij.zza;
import com.google.android.gms.internal.zzij.zzc;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzaz implements OnGlobalLayoutListener, OnScrollChangedListener {
    private boolean zzpB = false;
    private final Object zzpc = new Object();
    private zzib zzqE;
    private final Context zzqO;
    private final WeakReference<zzhj> zzqQ;
    private WeakReference<ViewTreeObserver> zzqR;
    private final WeakReference<View> zzqS;
    /* access modifiers changed from: private|final */
    public final zzax zzqT;
    private final zzdv zzqU;
    private final zzd zzqV;
    /* access modifiers changed from: private */
    public boolean zzqW;
    private final WindowManager zzqX;
    private final PowerManager zzqY;
    private final KeyguardManager zzqZ;
    private zzba zzra;
    private boolean zzrb;
    private boolean zzrc = false;
    private boolean zzrd;
    private boolean zzre;
    private BroadcastReceiver zzrf;
    private final HashSet<zzaw> zzrg = new HashSet();
    private final zzdg zzrh = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            if (zzaz.this.zzb((Map) map)) {
                zzaz.this.zza(zzip.getWebView(), (Map) map);
            }
        }
    };
    private final zzdg zzri = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            if (zzaz.this.zzb((Map) map)) {
                zzb.zzaC("Received request to untrack: " + zzaz.this.zzqT.zzbX());
                zzaz.this.destroy();
            }
        }
    };
    private final zzdg zzrj = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            if (zzaz.this.zzb((Map) map) && map.containsKey("isVisible")) {
                boolean z = "1".equals(map.get("isVisible")) || "true".equals(map.get("isVisible"));
                zzaz.this.zzg(Boolean.valueOf(z).booleanValue());
            }
        }
    };

    public zzaz(AdSizeParcel adSizeParcel, zzhj zzhj, VersionInfoParcel versionInfoParcel, View view, zzdv zzdv) {
        this.zzqU = zzdv;
        this.zzqQ = new WeakReference(zzhj);
        this.zzqS = new WeakReference(view);
        this.zzqR = new WeakReference(null);
        this.zzrd = true;
        this.zzqE = new zzib(200);
        this.zzqT = new zzax(UUID.randomUUID().toString(), versionInfoParcel, adSizeParcel.zzsG, zzhj.zzGF, zzhj.zzbY());
        this.zzqV = this.zzqU.zzdL();
        this.zzqX = (WindowManager) view.getContext().getSystemService("window");
        this.zzqY = (PowerManager) view.getContext().getApplicationContext().getSystemService("power");
        this.zzqZ = (KeyguardManager) view.getContext().getSystemService("keyguard");
        this.zzqO = view.getContext().getApplicationContext();
        try {
            final JSONObject zzd = zzd(view);
            this.zzqV.zza(new zzc<zzbe>() {
                /* renamed from: zzb */
                public void zzc(zzbe zzbe) {
                    zzaz.this.zza(zzd);
                }
            }, new zza() {
                public void run() {
                }
            });
        } catch (JSONException e) {
        } catch (RuntimeException e2) {
            zzb.zzb("Failure while processing active view data.", e2);
        }
        this.zzqV.zza(new zzc<zzbe>() {
            /* renamed from: zzb */
            public void zzc(zzbe zzbe) {
                zzaz.this.zzqW = true;
                zzaz.this.zza(zzbe);
                zzaz.this.zzbZ();
                zzaz.this.zzh(false);
            }
        }, new zza() {
            public void run() {
                zzaz.this.destroy();
            }
        });
        zzb.zzaC("Tracking ad unit: " + this.zzqT.zzbX());
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        synchronized (this.zzpc) {
            zzcf();
            zzca();
            this.zzrd = false;
            zzcc();
            this.zzqV.release();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isScreenOn() {
        return this.zzqY.isScreenOn();
    }

    public void onGlobalLayout() {
        zzh(false);
    }

    public void onScrollChanged() {
        zzh(true);
    }

    public void pause() {
        synchronized (this.zzpc) {
            this.zzpB = true;
            zzh(false);
        }
    }

    public void resume() {
        synchronized (this.zzpc) {
            this.zzpB = false;
            zzh(false);
        }
    }

    public void stop() {
        synchronized (this.zzpc) {
            this.zzrc = true;
            zzh(false);
        }
    }

    /* access modifiers changed from: protected */
    public int zza(int i, DisplayMetrics displayMetrics) {
        return (int) (((float) i) / displayMetrics.density);
    }

    /* access modifiers changed from: protected */
    public void zza(View view, Map<String, String> map) {
        zzh(false);
    }

    public void zza(zzaw zzaw) {
        this.zzrg.add(zzaw);
    }

    public void zza(zzba zzba) {
        synchronized (this.zzpc) {
            this.zzra = zzba;
        }
    }

    /* access modifiers changed from: protected */
    public void zza(zzbe zzbe) {
        zzbe.zza("/updateActiveView", this.zzrh);
        zzbe.zza("/untrackActiveViewUnit", this.zzri);
        zzbe.zza("/visibilityChanged", this.zzrj);
    }

    /* access modifiers changed from: protected */
    public void zza(JSONObject jSONObject) {
        try {
            JSONArray jSONArray = new JSONArray();
            final JSONObject jSONObject2 = new JSONObject();
            jSONArray.put(jSONObject);
            jSONObject2.put("units", jSONArray);
            this.zzqV.zza(new zzc<zzbe>() {
                /* renamed from: zzb */
                public void zzc(zzbe zzbe) {
                    zzbe.zza("AFMA_updateActiveView", jSONObject2);
                }
            }, new zzij.zzb());
        } catch (Throwable th) {
            zzb.zzb("Skipping active view message.", th);
        }
    }

    /* access modifiers changed from: protected */
    public boolean zzb(Map<String, String> map) {
        if (map == null) {
            return false;
        }
        String str = (String) map.get("hashCode");
        boolean z = !TextUtils.isEmpty(str) && str.equals(this.zzqT.zzbX());
        return z;
    }

    /* access modifiers changed from: protected */
    public void zzbZ() {
        synchronized (this.zzpc) {
            if (this.zzrf != null) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.zzrf = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    zzaz.this.zzh(false);
                }
            };
            this.zzqO.registerReceiver(this.zzrf, intentFilter);
        }
    }

    /* access modifiers changed from: protected */
    public void zzca() {
        synchronized (this.zzpc) {
            if (this.zzrf != null) {
                this.zzqO.unregisterReceiver(this.zzrf);
                this.zzrf = null;
            }
        }
    }

    public void zzcb() {
        synchronized (this.zzpc) {
            if (this.zzrd) {
                this.zzre = true;
                try {
                    zza(zzch());
                } catch (JSONException e) {
                    zzb.zzb("JSON failure while processing active view data.", e);
                } catch (RuntimeException e2) {
                    zzb.zzb("Failure while processing active view data.", e2);
                }
                zzb.zzaC("Untracking ad unit: " + this.zzqT.zzbX());
            }
        }
        return;
    }

    /* access modifiers changed from: protected */
    public void zzcc() {
        if (this.zzra != null) {
            this.zzra.zza(this);
        }
    }

    public boolean zzcd() {
        boolean z;
        synchronized (this.zzpc) {
            z = this.zzrd;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void zzce() {
        View view = (View) this.zzqS.get();
        if (view != null) {
            ViewTreeObserver viewTreeObserver = (ViewTreeObserver) this.zzqR.get();
            ViewTreeObserver viewTreeObserver2 = view.getViewTreeObserver();
            if (viewTreeObserver2 != viewTreeObserver) {
                zzcf();
                if (!this.zzrb || (viewTreeObserver != null && viewTreeObserver.isAlive())) {
                    this.zzrb = true;
                    viewTreeObserver2.addOnScrollChangedListener(this);
                    viewTreeObserver2.addOnGlobalLayoutListener(this);
                }
                this.zzqR = new WeakReference(viewTreeObserver2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void zzcf() {
        ViewTreeObserver viewTreeObserver = (ViewTreeObserver) this.zzqR.get();
        if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnScrollChangedListener(this);
            viewTreeObserver.removeGlobalOnLayoutListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public JSONObject zzcg() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("afmaVersion", this.zzqT.zzbV()).put("activeViewJSON", this.zzqT.zzbW()).put("timestamp", zzp.zzbB().elapsedRealtime()).put("adFormat", this.zzqT.zzbU()).put("hashCode", this.zzqT.zzbX()).put("isMraid", this.zzqT.zzbY());
        return jSONObject;
    }

    /* access modifiers changed from: protected */
    public JSONObject zzch() throws JSONException {
        JSONObject zzcg = zzcg();
        zzcg.put("doneReasonCode", "u");
        return zzcg;
    }

    /* access modifiers changed from: protected */
    public JSONObject zzd(View view) throws JSONException {
        boolean isAttachedToWindow = zzp.zzbz().isAttachedToWindow(view);
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        try {
            view.getLocationOnScreen(iArr);
            view.getLocationInWindow(iArr2);
        } catch (Exception e) {
            zzb.zzb("Failure getting view location.", e);
        }
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        Rect rect = new Rect();
        rect.left = iArr[0];
        rect.top = iArr[1];
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
        Rect rect2 = new Rect();
        rect2.right = this.zzqX.getDefaultDisplay().getWidth();
        rect2.bottom = this.zzqX.getDefaultDisplay().getHeight();
        Rect rect3 = new Rect();
        boolean globalVisibleRect = view.getGlobalVisibleRect(rect3, null);
        Rect rect4 = new Rect();
        boolean localVisibleRect = view.getLocalVisibleRect(rect4);
        Rect rect5 = new Rect();
        view.getHitRect(rect5);
        JSONObject zzcg = zzcg();
        zzcg.put("windowVisibility", view.getWindowVisibility()).put("isStopped", this.zzrc).put("isPaused", this.zzpB).put("isAttachedToWindow", isAttachedToWindow).put("viewBox", new JSONObject().put("top", zza(rect2.top, displayMetrics)).put("bottom", zza(rect2.bottom, displayMetrics)).put("left", zza(rect2.left, displayMetrics)).put("right", zza(rect2.right, displayMetrics))).put("adBox", new JSONObject().put("top", zza(rect.top, displayMetrics)).put("bottom", zza(rect.bottom, displayMetrics)).put("left", zza(rect.left, displayMetrics)).put("right", zza(rect.right, displayMetrics))).put("globalVisibleBox", new JSONObject().put("top", zza(rect3.top, displayMetrics)).put("bottom", zza(rect3.bottom, displayMetrics)).put("left", zza(rect3.left, displayMetrics)).put("right", zza(rect3.right, displayMetrics))).put("globalVisibleBoxVisible", globalVisibleRect).put("localVisibleBox", new JSONObject().put("top", zza(rect4.top, displayMetrics)).put("bottom", zza(rect4.bottom, displayMetrics)).put("left", zza(rect4.left, displayMetrics)).put("right", zza(rect4.right, displayMetrics))).put("localVisibleBoxVisible", localVisibleRect).put("hitBox", new JSONObject().put("top", zza(rect5.top, displayMetrics)).put("bottom", zza(rect5.bottom, displayMetrics)).put("left", zza(rect5.left, displayMetrics)).put("right", zza(rect5.right, displayMetrics))).put("screenDensity", (double) displayMetrics.density).put("isVisible", zze(view));
        return zzcg;
    }

    /* access modifiers changed from: protected */
    public boolean zze(View view) {
        return view.getVisibility() == 0 && view.isShown() && isScreenOn() && (!this.zzqZ.inKeyguardRestrictedInputMode() || zzp.zzbx().zzgq());
    }

    /* access modifiers changed from: protected */
    public void zzg(boolean z) {
        Iterator it = this.zzrg.iterator();
        while (it.hasNext()) {
            ((zzaw) it.next()).zza(this, z);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Missing block: B:33:?, code skipped:
            return;
     */
    public void zzh(boolean r4) {
        /*
        r3 = this;
        r2 = r3.zzpc;
        monitor-enter(r2);
        r0 = r3.zzqW;	 Catch:{ all -> 0x0019 }
        if (r0 == 0) goto L_0x000b;
    L_0x0007:
        r0 = r3.zzrd;	 Catch:{ all -> 0x0019 }
        if (r0 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
    L_0x000c:
        return;
    L_0x000d:
        if (r4 == 0) goto L_0x001c;
    L_0x000f:
        r0 = r3.zzqE;	 Catch:{ all -> 0x0019 }
        r0 = r0.tryAcquire();	 Catch:{ all -> 0x0019 }
        if (r0 != 0) goto L_0x001c;
    L_0x0017:
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        goto L_0x000c;
    L_0x0019:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        throw r0;
    L_0x001c:
        r0 = r3.zzqQ;	 Catch:{ all -> 0x0019 }
        r0 = r0.get();	 Catch:{ all -> 0x0019 }
        r0 = (com.google.android.gms.internal.zzhj) r0;	 Catch:{ all -> 0x0019 }
        r1 = r3.zzqS;	 Catch:{ all -> 0x0019 }
        r1 = r1.get();	 Catch:{ all -> 0x0019 }
        r1 = (android.view.View) r1;	 Catch:{ all -> 0x0019 }
        if (r1 == 0) goto L_0x0030;
    L_0x002e:
        if (r0 != 0) goto L_0x0038;
    L_0x0030:
        r0 = 1;
    L_0x0031:
        if (r0 == 0) goto L_0x003a;
    L_0x0033:
        r3.zzcb();	 Catch:{ all -> 0x0019 }
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        goto L_0x000c;
    L_0x0038:
        r0 = 0;
        goto L_0x0031;
    L_0x003a:
        r0 = r3.zzd(r1);	 Catch:{ RuntimeException | JSONException -> 0x0049, RuntimeException | JSONException -> 0x0049 }
        r3.zza(r0);	 Catch:{ RuntimeException | JSONException -> 0x0049, RuntimeException | JSONException -> 0x0049 }
    L_0x0041:
        r3.zzce();	 Catch:{ all -> 0x0019 }
        r3.zzcc();	 Catch:{ all -> 0x0019 }
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        goto L_0x000c;
    L_0x0049:
        r0 = move-exception;
        r1 = "Active view update failed.";
        com.google.android.gms.ads.internal.util.client.zzb.zza(r1, r0);	 Catch:{ all -> 0x0019 }
        goto L_0x0041;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzaz.zzh(boolean):void");
    }
}
