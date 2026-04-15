package com.google.android.gms.ads.internal.formats;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcl.zza;
import com.google.android.gms.internal.zzhu;
import com.google.android.gms.internal.zzil;
import com.google.android.gms.internal.zzip;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class zzj extends zza implements OnClickListener, OnTouchListener, OnGlobalLayoutListener, OnScrollChangedListener {
    /* access modifiers changed from: private|final */
    public final FrameLayout zznY;
    private final Object zzpc = new Object();
    private zzh zzvT;
    private final FrameLayout zzwq;
    private final Map<String, WeakReference<View>> zzwr = new HashMap();
    private zzb zzws;
    boolean zzwt = false;

    public zzj(FrameLayout frameLayout, FrameLayout frameLayout2) {
        this.zzwq = frameLayout;
        this.zznY = frameLayout2;
        zzil.zza(this.zzwq, (OnGlobalLayoutListener) this);
        zzil.zza(this.zzwq, (OnScrollChangedListener) this);
        this.zzwq.setOnTouchListener(this);
    }

    /* access modifiers changed from: 0000 */
    public int getMeasuredHeight() {
        return this.zzwq.getMeasuredHeight();
    }

    /* access modifiers changed from: 0000 */
    public int getMeasuredWidth() {
        return this.zzwq.getMeasuredWidth();
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            return;
     */
    public void onClick(android.view.View r4) {
        /*
        r3 = this;
        r1 = r3.zzpc;
        monitor-enter(r1);
        r0 = r3.zzvT;	 Catch:{ all -> 0x0022 }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r3.zzws;	 Catch:{ all -> 0x0022 }
        if (r0 == 0) goto L_0x0025;
    L_0x000d:
        r0 = r3.zzws;	 Catch:{ all -> 0x0022 }
        r0 = r0.zzdq();	 Catch:{ all -> 0x0022 }
        r0 = r0.equals(r4);	 Catch:{ all -> 0x0022 }
        if (r0 == 0) goto L_0x0025;
    L_0x0019:
        r0 = r3.zzvT;	 Catch:{ all -> 0x0022 }
        r2 = "1007";
        r0.performClick(r2);	 Catch:{ all -> 0x0022 }
    L_0x0020:
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
        goto L_0x0008;
    L_0x0022:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
        throw r0;
    L_0x0025:
        r0 = r3.zzvT;	 Catch:{ all -> 0x0022 }
        r2 = r3.zzwr;	 Catch:{ all -> 0x0022 }
        r0.zzb(r4, r2);	 Catch:{ all -> 0x0022 }
        goto L_0x0020;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.formats.zzj.onClick(android.view.View):void");
    }

    public void onGlobalLayout() {
        synchronized (this.zzpc) {
            if (this.zzwt) {
                int measuredWidth = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();
                if (!(measuredWidth == 0 || measuredHeight == 0)) {
                    this.zznY.setLayoutParams(new LayoutParams(measuredWidth, measuredHeight));
                    this.zzwt = false;
                }
            }
            if (this.zzvT != null) {
                this.zzvT.zzi(this.zzwq);
            }
        }
    }

    public void onScrollChanged() {
        synchronized (this.zzpc) {
            if (this.zzvT != null) {
                this.zzvT.zzi(this.zzwq);
            }
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized (this.zzpc) {
            if (this.zzvT == null) {
            } else {
                Point zzc = zzc(motionEvent);
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setLocation((float) zzc.x, (float) zzc.y);
                this.zzvT.zzb(obtain);
                obtain.recycle();
            }
        }
        return false;
    }

    public zzd zzU(String str) {
        zzd zzx;
        synchronized (this.zzpc) {
            Object obj;
            WeakReference weakReference = (WeakReference) this.zzwr.get(str);
            if (weakReference == null) {
                obj = null;
            } else {
                View obj2 = (View) weakReference.get();
            }
            zzx = zze.zzx(obj2);
        }
        return zzx;
    }

    public void zza(String str, zzd zzd) {
        View view = (View) zze.zzp(zzd);
        synchronized (this.zzpc) {
            if (view == null) {
                this.zzwr.remove(str);
            } else {
                this.zzwr.put(str, new WeakReference(view));
                view.setOnTouchListener(this);
                view.setOnClickListener(this);
            }
        }
    }

    public void zzb(zzd zzd) {
        synchronized (this.zzpc) {
            this.zzwt = true;
            final zzh zzh = (zzh) zze.zzp(zzd);
            if ((this.zzvT instanceof zzg) && ((zzg) this.zzvT).zzdx()) {
                ((zzg) this.zzvT).zzb(zzh);
            } else {
                this.zzvT = zzh;
                if (this.zzvT instanceof zzg) {
                    ((zzg) this.zzvT).zzb(null);
                }
            }
            this.zznY.removeAllViews();
            this.zzws = zzf(zzh);
            if (this.zzws != null) {
                this.zznY.addView(this.zzws);
            }
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzip zzdy = zzh.zzdy();
                    if (zzdy != null) {
                        zzj.this.zznY.addView(zzdy.getWebView());
                    }
                }
            });
            zzh.zzh(this.zzwq);
        }
    }

    /* access modifiers changed from: 0000 */
    public Point zzc(MotionEvent motionEvent) {
        int[] iArr = new int[2];
        this.zzwq.getLocationOnScreen(iArr);
        return new Point((int) (motionEvent.getRawX() - ((float) iArr[0])), (int) (motionEvent.getRawY() - ((float) iArr[1])));
    }

    /* access modifiers changed from: 0000 */
    public zzb zzf(zzh zzh) {
        return zzh.zza(this);
    }
}
