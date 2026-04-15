package com.flurry.sdk;

import android.content.Context;
import com.mopub.common.GpsHelper;

public class lu {
    private static final String a = lu.class.getSimpleName();

    private static String a(Object obj) {
        try {
            return (String) lw.a(obj, "getId").a();
        } catch (Exception e) {
            iw.b(a, "GOOGLE PLAY SERVICES ERROR: " + e.getMessage());
            iw.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return null;
        }
    }

    public static boolean a(Context context) {
        try {
            Object a = lw.a(null, "isGooglePlayServicesAvailable").a(Class.forName("com.google.android.gms.common.GooglePlayServicesUtil")).a(Context.class, context).a();
            return a != null && ((Integer) a).intValue() == 0;
        } catch (Exception e) {
            iw.b(a, "GOOGLE PLAY SERVICES EXCEPTION: " + e.getMessage());
            iw.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return false;
        }
    }

    public static hx b(Context context) {
        if (context == null) {
            return null;
        }
        try {
            Object a = lw.a(null, "getAdvertisingIdInfo").a(Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient")).a(Context.class, context).a();
            return new hx(a(a), b(a));
        } catch (Exception e) {
            iw.b(a, "GOOGLE PLAY SERVICES ERROR: " + e.getMessage());
            iw.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return null;
        }
    }

    private static boolean b(Object obj) {
        try {
            Boolean bool = (Boolean) lw.a(obj, GpsHelper.IS_LIMIT_AD_TRACKING_ENABLED_KEY).a();
            return bool != null ? bool.booleanValue() : false;
        } catch (Exception e) {
            iw.b(a, "GOOGLE PLAY SERVICES ERROR: " + e.getMessage());
            iw.b(a, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return false;
        }
    }
}
