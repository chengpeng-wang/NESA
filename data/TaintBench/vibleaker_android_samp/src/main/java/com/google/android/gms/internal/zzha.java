package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@zzhb
public class zzha implements UncaughtExceptionHandler {
    private Context mContext;
    private VersionInfoParcel zzEZ;
    private UncaughtExceptionHandler zzHe;
    private UncaughtExceptionHandler zzHf;

    public zzha(Context context, VersionInfoParcel versionInfoParcel, UncaughtExceptionHandler uncaughtExceptionHandler, UncaughtExceptionHandler uncaughtExceptionHandler2) {
        this.zzHe = uncaughtExceptionHandler;
        this.zzHf = uncaughtExceptionHandler2;
        this.mContext = context;
        this.zzEZ = versionInfoParcel;
    }

    private static boolean zzA(Context context) {
        return ((Boolean) zzbt.zzvG.get()).booleanValue();
    }

    public static zzha zza(Context context, Thread thread, VersionInfoParcel versionInfoParcel) {
        if (context == null || thread == null || versionInfoParcel == null) {
            return null;
        }
        if (!zzA(context)) {
            return null;
        }
        UncaughtExceptionHandler uncaughtExceptionHandler = thread.getUncaughtExceptionHandler();
        zzha zzha = new zzha(context, versionInfoParcel, uncaughtExceptionHandler, Thread.getDefaultUncaughtExceptionHandler());
        if (uncaughtExceptionHandler != null && (uncaughtExceptionHandler instanceof zzha)) {
            return (zzha) uncaughtExceptionHandler;
        }
        try {
            thread.setUncaughtExceptionHandler(zzha);
            return zzha;
        } catch (SecurityException e) {
            zzb.zzc("Fail to set UncaughtExceptionHandler.", e);
            return null;
        }
    }

    private Throwable zzb(Throwable th) {
        if (((Boolean) zzbt.zzvH.get()).booleanValue()) {
            return th;
        }
        LinkedList linkedList = new LinkedList();
        Object th2;
        while (th2 != null) {
            linkedList.push(th2);
            th2 = th2.getCause();
        }
        Throwable th3 = null;
        while (!linkedList.isEmpty()) {
            Throwable th4;
            Throwable th5 = (Throwable) linkedList.pop();
            StackTraceElement[] stackTrace = th5.getStackTrace();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new StackTraceElement(th5.getClass().getName(), "<filtered>", "<filtered>", 1));
            int i = 0;
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (zzat(stackTraceElement.getClassName())) {
                    arrayList.add(stackTraceElement);
                    i = 1;
                } else if (zzau(stackTraceElement.getClassName())) {
                    arrayList.add(stackTraceElement);
                } else {
                    arrayList.add(new StackTraceElement("<filtered>", "<filtered>", "<filtered>", 1));
                }
            }
            if (i != 0) {
                th4 = th3 == null ? new Throwable(th5.getMessage()) : new Throwable(th5.getMessage(), th3);
                th4.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[0]));
            } else {
                th4 = th3;
            }
            th3 = th4;
        }
        return th3;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        if (zza(exception)) {
            if (Looper.getMainLooper().getThread() != thread) {
                zza(exception, true);
                return;
            }
            zza(exception, false);
        }
        if (this.zzHe != null) {
            this.zzHe.uncaughtException(thread, exception);
        } else if (this.zzHf != null) {
            this.zzHf.uncaughtException(thread, exception);
        }
    }

    /* access modifiers changed from: 0000 */
    public String zza(Class cls, Throwable th, boolean z) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return new Builder().scheme("https").path("//pagead2.googlesyndication.com/pagead/gen_204").appendQueryParameter("id", "gmob-apps-report-exception").appendQueryParameter("os", VERSION.RELEASE).appendQueryParameter("api", String.valueOf(VERSION.SDK_INT)).appendQueryParameter("device", zzr.zzbC().zzht()).appendQueryParameter("js", this.zzEZ.afmaVersion).appendQueryParameter("appid", this.mContext.getApplicationContext().getPackageName()).appendQueryParameter("exceptiontype", cls.getName()).appendQueryParameter("stacktrace", stringWriter.toString()).appendQueryParameter("eids", TextUtils.join(",", zzbt.zzdr())).appendQueryParameter("trapped", String.valueOf(z)).toString();
    }

    public void zza(Throwable th, boolean z) {
        if (zzA(this.mContext)) {
            Throwable zzb = zzb(th);
            if (zzb != null) {
                Class cls = th.getClass();
                List arrayList = new ArrayList();
                arrayList.add(zza(cls, zzb, z));
                zzr.zzbC().zza(this.mContext, this.zzEZ.afmaVersion, arrayList, zzr.zzbF().zzhe());
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean zza(Throwable th) {
        boolean z = true;
        if (th == null) {
            return false;
        }
        boolean z2 = false;
        boolean z3 = false;
        while (th != null) {
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                if (zzat(stackTraceElement.getClassName())) {
                    z3 = true;
                }
                if (getClass().getName().equals(stackTraceElement.getClassName())) {
                    z2 = true;
                }
            }
            th = th.getCause();
        }
        if (!z3 || z2) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean zzat(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return z;
        }
        if (str.startsWith("com.google.android.gms.ads")) {
            return true;
        }
        if (str.startsWith("com.google.ads")) {
            return true;
        }
        try {
            return Class.forName(str).isAnnotationPresent(zzhb.class);
        } catch (Exception e) {
            zzb.zza("Fail to check class type for class " + str, e);
            return z;
        }
    }

    /* access modifiers changed from: protected */
    public boolean zzau(String str) {
        return TextUtils.isEmpty(str) ? false : str.startsWith("android.") || str.startsWith("java.");
    }
}
