package com.google.android.gms.internal;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzbk extends Thread {
    private boolean mStarted = false;
    private boolean zzam = false;
    private final Object zzpc;
    private final int zzrC;
    private final int zzrE;
    private boolean zzrO = false;
    private final zzbj zzrP;
    private final zzbi zzrQ;
    private final zzgj zzrR;
    private final int zzrS;
    private final int zzrT;
    private final int zzrU;

    @zzgk
    class zza {
        final int zzsb;
        final int zzsc;

        zza(int i, int i2) {
            this.zzsb = i;
            this.zzsc = i2;
        }
    }

    public zzbk(zzbj zzbj, zzbi zzbi, zzgj zzgj) {
        this.zzrP = zzbj;
        this.zzrQ = zzbi;
        this.zzrR = zzgj;
        this.zzpc = new Object();
        this.zzrC = ((Integer) zzby.zzuF.get()).intValue();
        this.zzrT = ((Integer) zzby.zzuG.get()).intValue();
        this.zzrE = ((Integer) zzby.zzuH.get()).intValue();
        this.zzrU = ((Integer) zzby.zzuI.get()).intValue();
        this.zzrS = ((Integer) zzby.zzuJ.get()).intValue();
        setName("ContentFetchTask");
    }

    public void run() {
        while (!this.zzam) {
            try {
                if (zzcu()) {
                    Activity activity = this.zzrP.getActivity();
                    if (activity == null) {
                        zzb.zzaC("ContentFetchThread: no activity");
                    } else {
                        zza(activity);
                    }
                } else {
                    zzb.zzaC("ContentFetchTask: sleeping");
                    zzcw();
                }
                Thread.sleep((long) (this.zzrS * 1000));
            } catch (Throwable th) {
                zzb.zzb("Error in ContentFetchTask", th);
                this.zzrR.zza(th, true);
            }
            synchronized (this.zzpc) {
                while (this.zzrO) {
                    try {
                        zzb.zzaC("ContentFetchTask: waiting");
                        this.zzpc.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void wakeup() {
        synchronized (this.zzpc) {
            this.zzrO = false;
            this.zzpc.notifyAll();
            zzb.zzaC("ContentFetchThread: wakeup");
        }
    }

    /* access modifiers changed from: 0000 */
    public zza zza(View view, zzbh zzbh) {
        int i = 0;
        if (view == null) {
            return new zza(0, 0);
        }
        if ((view instanceof TextView) && !(view instanceof EditText)) {
            CharSequence text = ((TextView) view).getText();
            if (TextUtils.isEmpty(text)) {
                return new zza(0, 0);
            }
            zzbh.zzw(text.toString());
            return new zza(1, 0);
        } else if ((view instanceof WebView) && !(view instanceof zzip)) {
            zzbh.zzcp();
            return zza((WebView) view, zzbh) ? new zza(0, 1) : new zza(0, 0);
        } else if (!(view instanceof ViewGroup)) {
            return new zza(0, 0);
        } else {
            ViewGroup viewGroup = (ViewGroup) view;
            int i2 = 0;
            int i3 = 0;
            while (i < viewGroup.getChildCount()) {
                zza zza = zza(viewGroup.getChildAt(i), zzbh);
                i3 += zza.zzsb;
                i2 += zza.zzsc;
                i++;
            }
            return new zza(i3, i2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void zza(Activity activity) {
        if (activity != null) {
            View view = null;
            if (!(activity.getWindow() == null || activity.getWindow().getDecorView() == null)) {
                view = activity.getWindow().getDecorView().findViewById(16908290);
            }
            if (view != null) {
                zzf(view);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void zza(zzbh zzbh, WebView webView, String str) {
        zzbh.zzco();
        try {
            if (!TextUtils.isEmpty(str)) {
                String optString = new JSONObject(str).optString(MASTNativeAdConstants.RESPONSE_TEXT);
                if (TextUtils.isEmpty(webView.getTitle())) {
                    zzbh.zzv(optString);
                } else {
                    zzbh.zzv(webView.getTitle() + MASTNativeAdConstants.NEWLINE + optString);
                }
            }
            if (zzbh.zzcl()) {
                this.zzrQ.zzb(zzbh);
            }
        } catch (JSONException e) {
            zzb.zzaC("Json string may be malformed.");
        } catch (Throwable th) {
            zzb.zza("Failed to get webview content.", th);
            this.zzrR.zza(th, true);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean zza(RunningAppProcessInfo runningAppProcessInfo) {
        return runningAppProcessInfo.importance == 100;
    }

    /* access modifiers changed from: 0000 */
    public boolean zza(final WebView webView, final zzbh zzbh) {
        if (!zzlv.zzpV()) {
            return false;
        }
        zzbh.zzcp();
        webView.post(new Runnable() {
            ValueCallback<String> zzrX = new ValueCallback<String>() {
                /* renamed from: zzy */
                public void onReceiveValue(String str) {
                    zzbk.this.zza(zzbh, webView, str);
                }
            };

            public void run() {
                if (webView.getSettings().getJavaScriptEnabled()) {
                    try {
                        webView.evaluateJavascript("(function() { return  {text:document.body.innerText}})();", this.zzrX);
                    } catch (Throwable th) {
                        this.zzrX.onReceiveValue("");
                    }
                }
            }
        });
        return true;
    }

    public void zzct() {
        synchronized (this.zzpc) {
            if (this.mStarted) {
                zzb.zzaC("Content hash thread already started, quiting...");
                return;
            }
            this.mStarted = true;
            start();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean zzcu() {
        try {
            Context context = this.zzrP.getContext();
            if (context == null) {
                return false;
            }
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
            if (activityManager == null || keyguardManager == null) {
                return false;
            }
            List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return false;
            }
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (Process.myPid() == runningAppProcessInfo.pid) {
                    if (zza(runningAppProcessInfo) && !keyguardManager.inKeyguardRestrictedInputMode() && zzr(context)) {
                        return true;
                    }
                    return false;
                }
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public zzbh zzcv() {
        return this.zzrQ.zzcs();
    }

    public void zzcw() {
        synchronized (this.zzpc) {
            this.zzrO = true;
            zzb.zzaC("ContentFetchThread: paused, mPause = " + this.zzrO);
        }
    }

    public boolean zzcx() {
        return this.zzrO;
    }

    /* access modifiers changed from: 0000 */
    public boolean zzf(final View view) {
        if (view == null) {
            return false;
        }
        view.post(new Runnable() {
            public void run() {
                zzbk.this.zzg(view);
            }
        });
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void zzg(View view) {
        try {
            zzbh zzbh = new zzbh(this.zzrC, this.zzrT, this.zzrE, this.zzrU);
            zza zza = zza(view, zzbh);
            zzbh.zzcq();
            if (zza.zzsb != 0 || zza.zzsc != 0) {
                if (zza.zzsc != 0 || zzbh.zzcr() != 0) {
                    if (zza.zzsc != 0 || !this.zzrQ.zza(zzbh)) {
                        this.zzrQ.zzc(zzbh);
                    }
                }
            }
        } catch (Exception e) {
            zzb.zzb("Exception in fetchContentOnUIThread", e);
            this.zzrR.zza(e, true);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean zzr(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        return powerManager == null ? false : powerManager.isScreenOn();
    }
}
