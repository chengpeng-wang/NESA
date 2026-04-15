package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mb.bdapp.db.DuAd;
import java.util.Locale;

@zzgk
public final class zzgr {
    public final int zzDI;
    public final int zzDJ;
    public final float zzDK;
    public final int zzFB;
    public final boolean zzFC;
    public final boolean zzFD;
    public final String zzFE;
    public final String zzFF;
    public final boolean zzFG;
    public final boolean zzFH;
    public final boolean zzFI;
    public final boolean zzFJ;
    public final String zzFK;
    public final String zzFL;
    public final int zzFM;
    public final int zzFN;
    public final int zzFO;
    public final int zzFP;
    public final int zzFQ;
    public final int zzFR;
    public final double zzFS;
    public final boolean zzFT;
    public final boolean zzFU;
    public final int zzFV;
    public final String zzFW;

    public static final class zza {
        private int zzDI;
        private int zzDJ;
        private float zzDK;
        private int zzFB;
        private boolean zzFC;
        private boolean zzFD;
        private String zzFE;
        private String zzFF;
        private boolean zzFG;
        private boolean zzFH;
        private boolean zzFI;
        private boolean zzFJ;
        private String zzFK;
        private String zzFL;
        private int zzFM;
        private int zzFN;
        private int zzFO;
        private int zzFP;
        private int zzFQ;
        private int zzFR;
        private double zzFS;
        private boolean zzFT;
        private boolean zzFU;
        private int zzFV;
        private String zzFW;

        public zza(Context context) {
            boolean z = true;
            PackageManager packageManager = context.getPackageManager();
            zzA(context);
            zza(context, packageManager);
            zzB(context);
            Locale locale = Locale.getDefault();
            this.zzFC = zza(packageManager, "geo:0,0?q=donuts") != null;
            if (zza(packageManager, "http://www.google.com") == null) {
                z = false;
            }
            this.zzFD = z;
            this.zzFF = locale.getCountry();
            this.zzFG = zzk.zzcE().zzgH();
            this.zzFH = GooglePlayServicesUtil.zzag(context);
            this.zzFK = locale.getLanguage();
            this.zzFL = zza(packageManager);
            Resources resources = context.getResources();
            if (resources != null) {
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                if (displayMetrics != null) {
                    this.zzDK = displayMetrics.density;
                    this.zzDI = displayMetrics.widthPixels;
                    this.zzDJ = displayMetrics.heightPixels;
                }
            }
        }

        public zza(Context context, zzgr zzgr) {
            PackageManager packageManager = context.getPackageManager();
            zzA(context);
            zza(context, packageManager);
            zzB(context);
            zzC(context);
            this.zzFC = zzgr.zzFC;
            this.zzFD = zzgr.zzFD;
            this.zzFF = zzgr.zzFF;
            this.zzFG = zzgr.zzFG;
            this.zzFH = zzgr.zzFH;
            this.zzFK = zzgr.zzFK;
            this.zzFL = zzgr.zzFL;
            this.zzDK = zzgr.zzDK;
            this.zzDI = zzgr.zzDI;
            this.zzDJ = zzgr.zzDJ;
        }

        private void zzA(Context context) {
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            this.zzFB = audioManager.getMode();
            this.zzFI = audioManager.isMusicActive();
            this.zzFJ = audioManager.isSpeakerphoneOn();
            this.zzFM = audioManager.getStreamVolume(3);
            this.zzFQ = audioManager.getRingerMode();
            this.zzFR = audioManager.getStreamVolume(2);
        }

        private void zzB(Context context) {
            boolean z = false;
            Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (registerReceiver != null) {
                int intExtra = registerReceiver.getIntExtra(DuAd.DB_STATUS, -1);
                this.zzFS = (double) (((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1)));
                if (intExtra == 2 || intExtra == 5) {
                    z = true;
                }
                this.zzFT = z;
                return;
            }
            this.zzFS = -1.0d;
            this.zzFT = false;
        }

        private void zzC(Context context) {
            this.zzFW = Build.FINGERPRINT;
        }

        private static int zza(Context context, ConnectivityManager connectivityManager, PackageManager packageManager) {
            if (!zzp.zzbx().zza(packageManager, context.getPackageName(), "android.permission.ACCESS_NETWORK_STATE")) {
                return -2;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null ? activeNetworkInfo.getType() : -1;
        }

        private static ResolveInfo zza(PackageManager packageManager, String str) {
            return packageManager.resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)), 65536);
        }

        private static String zza(PackageManager packageManager) {
            ResolveInfo zza = zza(packageManager, "market://details?id=com.google.android.gms.ads");
            if (zza == null) {
                return null;
            }
            ActivityInfo activityInfo = zza.activityInfo;
            if (activityInfo == null) {
                return null;
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(activityInfo.packageName, 0);
                return packageInfo != null ? packageInfo.versionCode + "." + activityInfo.packageName : null;
            } catch (NameNotFoundException e) {
                return null;
            }
        }

        private void zza(Context context, PackageManager packageManager) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            this.zzFE = telephonyManager.getNetworkOperator();
            this.zzFN = zza(context, connectivityManager, packageManager);
            this.zzFO = telephonyManager.getNetworkType();
            this.zzFP = telephonyManager.getPhoneType();
            if (VERSION.SDK_INT >= 16) {
                this.zzFU = connectivityManager.isActiveNetworkMetered();
                if (connectivityManager.getActiveNetworkInfo() != null) {
                    this.zzFV = connectivityManager.getActiveNetworkInfo().getDetailedState().ordinal();
                    return;
                } else {
                    this.zzFV = -1;
                    return;
                }
            }
            this.zzFU = false;
            this.zzFV = -1;
        }

        public zzgr zzfN() {
            return new zzgr(this.zzFB, this.zzFC, this.zzFD, this.zzFE, this.zzFF, this.zzFG, this.zzFH, this.zzFI, this.zzFJ, this.zzFK, this.zzFL, this.zzFM, this.zzFN, this.zzFO, this.zzFP, this.zzFQ, this.zzFR, this.zzDK, this.zzDI, this.zzDJ, this.zzFS, this.zzFT, this.zzFU, this.zzFV, this.zzFW);
        }
    }

    zzgr(int i, boolean z, boolean z2, String str, String str2, boolean z3, boolean z4, boolean z5, boolean z6, String str3, String str4, int i2, int i3, int i4, int i5, int i6, int i7, float f, int i8, int i9, double d, boolean z7, boolean z8, int i10, String str5) {
        this.zzFB = i;
        this.zzFC = z;
        this.zzFD = z2;
        this.zzFE = str;
        this.zzFF = str2;
        this.zzFG = z3;
        this.zzFH = z4;
        this.zzFI = z5;
        this.zzFJ = z6;
        this.zzFK = str3;
        this.zzFL = str4;
        this.zzFM = i2;
        this.zzFN = i3;
        this.zzFO = i4;
        this.zzFP = i5;
        this.zzFQ = i6;
        this.zzFR = i7;
        this.zzDK = f;
        this.zzDI = i8;
        this.zzDJ = i9;
        this.zzFS = d;
        this.zzFT = z7;
        this.zzFU = z8;
        this.zzFV = i10;
        this.zzFW = str5;
    }
}
