package com.cleanmaster.ui.app.market.transport;

import android.text.TextUtils;
import com.cleanmaster.service.a;
import com.mopub.common.Constants;
import com.picksinit.c;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

/* compiled from: MarketHttpConfig */
public class b {
    public static int a = -1;
    public static int b = 80;
    public static String c = "unad.adkmob.com";
    public static String d = "unad.mobad.ijinshan.com";
    public static String e = Constants.HTTPS;
    public static String f = Constants.HTTP;
    public static String g = "cm.adkmob.com";
    public static int h = -1;
    public static String i = "/queryUpdate/";
    public static String j = ("http://" + c() + ":" + b + "/b/?action=get_config&mid=");
    private static String k = null;

    private static void a(SchemeRegistry schemeRegistry) {
        schemeRegistry.register(new Scheme(Constants.HTTPS, new c(), 443));
    }

    public static ClientConnectionManager a(HttpParams httpParams) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(Constants.HTTP, PlainSocketFactory.getSocketFactory(), 80));
        a(schemeRegistry);
        return new SingleClientConnManager(httpParams, schemeRegistry);
    }

    public static void a() {
        if (k == null) {
            try {
                a.a();
                String b = a.b();
                k = b;
                if (TextUtils.isEmpty(b)) {
                    k = com.cleanmaster.util.b.a(c.getInstance().getContext());
                    a.a();
                    a.a(k);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String b() {
        if (k == null) {
            a.a();
            k = a.b();
        }
        return k;
    }

    public static void a(String str) {
        k = str;
    }

    public static String c() {
        if (c.getInstance().getmAdResourceRp() == 1) {
            if (com.cleanmaster.ui.app.market.c.a()) {
                return "sdk.mobad.ijinshan.com";
            }
            String d = com.cleanmaster.ui.app.market.b.d();
            if (TextUtils.isEmpty(d)) {
                return "ssdk.adkmob.com";
            }
            return d;
        } else if (com.cleanmaster.ui.app.market.c.a()) {
            return d;
        } else {
            return c;
        }
    }
}
