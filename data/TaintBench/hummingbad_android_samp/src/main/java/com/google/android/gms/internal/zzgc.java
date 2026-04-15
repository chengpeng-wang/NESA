package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzhj.zza;

@zzgk
public class zzgc extends zzgb {
    private Object zzCK = new Object();
    private PopupWindow zzCL;
    private boolean zzCM = false;

    zzgc(Context context, zza zza, zzip zzip, zzga.zza zza2) {
        super(context, zza, zzip, zza2);
    }

    private void zzfq() {
        synchronized (this.zzCK) {
            this.zzCM = true;
            if ((this.mContext instanceof Activity) && ((Activity) this.mContext).isDestroyed()) {
                this.zzCL = null;
            }
            if (this.zzCL != null) {
                if (this.zzCL.isShowing()) {
                    this.zzCL.dismiss();
                }
                this.zzCL = null;
            }
        }
    }

    public void onStop() {
        zzfq();
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void zzfp() {
        Window window = this.mContext instanceof Activity ? ((Activity) this.mContext).getWindow() : null;
        if (window != null && window.getDecorView() != null && !((Activity) this.mContext).isDestroyed()) {
            FrameLayout frameLayout = new FrameLayout(this.mContext);
            frameLayout.setLayoutParams(new LayoutParams(-1, -1));
            frameLayout.addView(this.zzoL.getWebView(), -1, -1);
            synchronized (this.zzCK) {
                if (this.zzCM) {
                    return;
                }
                this.zzCL = new PopupWindow(frameLayout, 1, 1, false);
                this.zzCL.setOutsideTouchable(true);
                this.zzCL.setClippingEnabled(false);
                zzb.zzaC("Displaying the 1x1 popup off the screen.");
                try {
                    this.zzCL.showAtLocation(window.getDecorView(), 0, -1, -1);
                } catch (Exception e) {
                    this.zzCL = null;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void zzi(zzhj zzhj) {
        zzfq();
        super.zzi(zzhj);
    }
}
