package com.flurry.sdk;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.text.TextUtils;
import java.util.Locale;
import java.util.TimeZone;

public class hp implements lk {
    public static final String a = hp.class.getSimpleName();
    private static hp e;
    public boolean b;
    public Location c;
    public Location d;
    private final int f = 3;
    private final long g = 90000;
    /* access modifiers changed from: private */
    public long h = 0;
    private final LocationManager i = ((LocationManager) hz.a.b.getSystemService("location"));
    private final hr j = new hr(this);
    private boolean k = false;
    private int l = 0;
    private final ik m = new hq(this);

    private hp() {
        li a = li.a();
        this.b = ((Boolean) a.a("ReportLocation")).booleanValue();
        a.a("ReportLocation", (lk) this);
        iw.a(4, a, "initSettings, ReportLocation = " + this.b);
        this.c = (Location) a.a("ExplicitLocation");
        a.a("ExplicitLocation", (lk) this);
        iw.a(4, a, "initSettings, ExplicitLocation = " + this.c);
    }

    public static synchronized hp a() {
        hp hpVar;
        synchronized (hp.class) {
            if (e == null) {
                e = new hp();
            }
            hpVar = e;
        }
        return hpVar;
    }

    public static boolean a(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    static /* synthetic */ void b(hp hpVar) {
        if (hpVar.k) {
            hpVar.i.removeUpdates(hpVar.j);
            hpVar.l = 0;
            hpVar.h = 0;
            iw.a(4, a, "Unregister location timer");
            ln.a().b(hpVar.m);
            hpVar.k = false;
            iw.a(4, a, "LocationProvider stopped");
        }
    }

    public static boolean b(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    static /* synthetic */ int c(hp hpVar) {
        int i = hpVar.l + 1;
        hpVar.l = i;
        return i;
    }

    public static String c() {
        return "passive";
    }

    public static String d() {
        return "network";
    }

    public static String e() {
        return Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
    }

    public static String f() {
        return TimeZone.getDefault().getID();
    }

    public final Location a(String str) {
        return !TextUtils.isEmpty(str) ? this.i.getLastKnownLocation(str) : null;
    }

    public final void a(String str, Object obj) {
        Object obj2 = -1;
        switch (str.hashCode()) {
            case -864112343:
                if (str.equals("ReportLocation")) {
                    obj2 = null;
                    break;
                }
                break;
            case -300729815:
                if (str.equals("ExplicitLocation")) {
                    obj2 = 1;
                    break;
                }
                break;
        }
        switch (obj2) {
            case null:
                this.b = ((Boolean) obj).booleanValue();
                iw.a(4, a, "onSettingUpdate, ReportLocation = " + this.b);
                return;
            case 1:
                this.c = (Location) obj;
                iw.a(4, a, "onSettingUpdate, ExplicitLocation = " + this.c);
                return;
            default:
                iw.a(6, a, "LocationProvider internal error! Had to be LocationCriteria, ReportLocation or ExplicitLocation key.");
                return;
        }
    }

    public final synchronized void b() {
        iw.a(4, a, "Location update requested");
        if (this.l < 3 && !this.k && this.b && this.c == null) {
            Context context = hz.a.b;
            if (context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0 || context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
                this.l = 0;
                String str = null;
                if (a(context)) {
                    str = "passive";
                } else if (b(context)) {
                    str = "network";
                }
                if (!TextUtils.isEmpty(str)) {
                    this.i.requestLocationUpdates(str, 10000, 0.0f, this.j, Looper.getMainLooper());
                }
                this.d = a(str);
                this.h = System.currentTimeMillis() + 90000;
                iw.a(4, a, "Register location timer");
                ln.a().a(this.m);
                this.k = true;
                iw.a(4, a, "LocationProvider started");
            }
        }
    }
}
