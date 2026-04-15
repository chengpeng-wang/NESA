package com.duapps.ad.base;

import android.content.Context;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.GpsHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/* compiled from: HttpParamsHelper */
public final class e {
    private static final List<NameValuePair> a = new ArrayList();
    private static boolean b = false;

    private e() {
    }

    public static List<NameValuePair> a(Context context, String str) {
        String a;
        synchronized (e.class) {
            if (b) {
            } else {
                a.add(new BasicNameValuePair(MASTNativeAdConstants.NATIVE_IMAGE_H, a.b(context)));
                a.add(new BasicNameValuePair(MASTNativeAdConstants.NATIVE_IMAGE_W, a.c(context)));
                a.add(new BasicNameValuePair("model", a.b()));
                a.add(new BasicNameValuePair("vendor", a.a()));
                a.add(new BasicNameValuePair("sdk", a.c()));
                a.add(new BasicNameValuePair("dpi", a.g(context)));
                a.add(new BasicNameValuePair("sv", "1.0.5.1"));
                a.add(new BasicNameValuePair("svn", "HW-1.0.5.1"));
                a.add(new BasicNameValuePair("pkg", a.a(context)));
                a.add(new BasicNameValuePair("v", String.valueOf(a.e(context))));
                a.add(new BasicNameValuePair("vn", a.d(context)));
                a = d.a(context);
                if (!TextUtils.isEmpty(a)) {
                    a.add(new BasicNameValuePair("tk", a));
                }
                b = true;
            }
        }
        ArrayList arrayList = new ArrayList(a);
        a = a.f(context);
        if (!TextUtils.isEmpty(a)) {
            arrayList.add(new BasicNameValuePair("op", a));
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot get advertising info on main thread.");
        }
        CharSequence charSequence;
        Method a2 = d.a("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", new Class[]{Context.class});
        if (a2 == null) {
            f.c("GMS", "isGooglePlayServicesAvailable = null");
            charSequence = null;
        } else {
            Object a3 = d.a(null, a2, new Object[]{context});
            if (a3 == null || ((Integer) a3).intValue() != 0) {
                f.c("GMS", "CONNECTION_RESULT_SUCCESS =  " + a3);
                charSequence = null;
            } else {
                a2 = d.a("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", new Class[]{Context.class});
                if (a2 == null) {
                    f.c("GMS", "getAdvertisingIdInfo =  null");
                    charSequence = null;
                } else {
                    Object a4 = d.a(null, a2, new Object[]{context});
                    if (a4 == null) {
                        f.c("GMS", "advertisingInfo =  null");
                        charSequence = null;
                    } else {
                        Method a5 = d.a(a4.getClass(), "getId", new Class[0]);
                        Method a6 = d.a(a4.getClass(), GpsHelper.IS_LIMIT_AD_TRACKING_ENABLED_KEY, new Class[0]);
                        if (a5 == null || a6 == null) {
                            f.c("GMS", "getId =  null or isLimitAdTrackingEnabled = null");
                            charSequence = null;
                        } else {
                            a = (String) d.a(a4, a5, new Object[0]);
                        }
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(charSequence)) {
            arrayList.add(new BasicNameValuePair("goid", charSequence));
        }
        arrayList.add(new BasicNameValuePair("locale", a.h(context)));
        arrayList.add(new BasicNameValuePair("ntt", a.i(context)));
        arrayList.add(new BasicNameValuePair("ls", str));
        a = a(context);
        if (!TextUtils.isEmpty(a)) {
            arrayList.add(new BasicNameValuePair("aid", a));
        }
        return arrayList;
    }

    private static String a(Context context) {
        try {
            return Secure.getString(context.getContentResolver(), "android_id");
        } catch (Exception e) {
            f.b("HttpParamsHelper", "Failed to get the getAndroidId info.", e);
            return "";
        }
    }
}
