package com.google.android.gms.ads.internal.util.client;

import android.util.Log;
import com.google.ads.AdRequest;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class zzb {
    public static void e(String msg) {
        if (zzM(6)) {
            Log.e(AdRequest.LOGTAG, msg);
        }
    }

    public static void v(String msg) {
        if (zzM(2)) {
            Log.v(AdRequest.LOGTAG, msg);
        }
    }

    public static boolean zzM(int i) {
        return (i >= 5 || Log.isLoggable(AdRequest.LOGTAG, i)) && (i != 2 || zzgJ());
    }

    public static void zza(String str, Throwable th) {
        if (zzM(3)) {
            Log.d(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzaC(String str) {
        if (zzM(3)) {
            Log.d(AdRequest.LOGTAG, str);
        }
    }

    public static void zzaD(String str) {
        if (zzM(4)) {
            Log.i(AdRequest.LOGTAG, str);
        }
    }

    public static void zzaE(String str) {
        if (zzM(5)) {
            Log.w(AdRequest.LOGTAG, str);
        }
    }

    public static void zzb(String str, Throwable th) {
        if (zzM(6)) {
            Log.e(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzc(String str, Throwable th) {
        if (zzM(4)) {
            Log.i(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzd(String str, Throwable th) {
        if (zzM(5)) {
            Log.w(AdRequest.LOGTAG, str, th);
        }
    }

    public static boolean zzgJ() {
        return ((Boolean) zzby.zzuW.get()).booleanValue();
    }
}
