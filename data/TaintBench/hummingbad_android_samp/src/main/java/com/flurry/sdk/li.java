package com.flurry.sdk;

import android.location.Location;
import com.cleanmaster.ui.app.market.Ad;

public final class li extends lj {
    private static li a;
    private static final Integer b = Integer.valueOf(Ad.RECOMMEND_SHOW_TYPE_HIGH);
    private static final Integer c = Integer.valueOf(1);
    private static final Integer d = Integer.valueOf(0);
    private static final Integer e = Integer.valueOf(1);
    private static final String f = null;
    private static final Boolean g = Boolean.valueOf(true);
    private static final Boolean h = Boolean.valueOf(true);
    private static final String i = null;
    private static final Boolean j = Boolean.valueOf(true);
    private static final Location k = null;
    private static final Long l = Long.valueOf(10000);
    private static final Boolean m = Boolean.valueOf(true);
    private static final Long n = null;
    private static final Byte o = Byte.valueOf((byte) -1);
    private static final Boolean p = Boolean.valueOf(false);
    private static final String q = null;
    private static final Boolean r = Boolean.valueOf(true);

    private li() {
        a("AgentVersion", (Object) b);
        a("ReleaseMajorVersion", (Object) c);
        a("ReleaseMinorVersion", (Object) d);
        a("ReleasePatchVersion", (Object) e);
        a("ReleaseBetaVersion", (Object) "");
        a("VersionName", (Object) f);
        a("CaptureUncaughtExceptions", (Object) g);
        a("UseHttps", (Object) h);
        a("ReportUrl", (Object) i);
        a("ReportLocation", (Object) j);
        a("ExplicitLocation", (Object) k);
        a("ContinueSessionMillis", (Object) l);
        a("LogEvents", (Object) m);
        a("Age", (Object) n);
        a("Gender", (Object) o);
        a("UserId", (Object) "");
        a("ProtonEnabled", (Object) p);
        a("ProtonConfigUrl", (Object) q);
        a("analyticsEnabled", (Object) r);
        a("ProtonConfigUrl", (Object) Boolean.valueOf(true));
        a("analyticsEnabled", (Object) Boolean.valueOf(true));
    }

    public static synchronized li a() {
        li liVar;
        synchronized (li.class) {
            if (a == null) {
                a = new li();
            }
            liVar = a;
        }
        return liVar;
    }
}
