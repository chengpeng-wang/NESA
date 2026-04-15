package com.baidu.mapapi.search;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.HashMap;

public class a {
    static Context a;
    static HashMap<String, SoftReference<j>> b = new HashMap();
    public static boolean c = false;
    public static int d = 4;
    public static boolean e = false;
    public static byte f = (byte) 0;
    public static String g = "10.0.0.200";
    public static int h = 80;

    public interface a {
        void onError(int i, int i2, String str, Object obj);

        void onOk(int i, int i2, String str, Object obj);
    }

    public static HttpURLConnection a(String str) throws IOException {
        if (!c) {
            b();
            if (!c) {
                return null;
            }
        }
        if (!e) {
            return (HttpURLConnection) new URL(str).openConnection();
        }
        String substring;
        String str2;
        int indexOf = str.indexOf(47, 7);
        if (indexOf < 0) {
            substring = str.substring(7);
            str2 = "";
        } else {
            substring = str.substring(7, indexOf);
            str2 = str.substring(indexOf);
        }
        if (f == (byte) 1) {
            return (HttpURLConnection) new URL(str).openConnection(new Proxy(Type.HTTP, new InetSocketAddress(g, 80)));
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://" + g + str2).openConnection();
        httpURLConnection.setRequestProperty("X-Online-Host", substring);
        return httpURLConnection;
    }

    public static void a() {
        a = null;
        b.clear();
    }

    public static void a(int i, int i2, String str, a aVar) {
        if (str != null && str.startsWith("http://")) {
            new b(str, aVar, i, i2).start();
        }
    }

    public static void a(Context context) {
        a = context;
    }

    public static void a(NetworkInfo networkInfo, boolean z) {
        c = z;
        try {
            if (networkInfo.getType() == 1) {
                d = 4;
                e = false;
                return;
            }
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo == null) {
                d = 0;
                g = android.net.Proxy.getDefaultHost();
                h = android.net.Proxy.getDefaultPort();
                if (g == null || "".equals(g)) {
                    d = 1;
                    e = false;
                    return;
                }
                d = 2;
                e = true;
                if ("10.0.0.200".equals(g)) {
                    f = (byte) 1;
                    return;
                } else {
                    f = (byte) 0;
                    return;
                }
            }
            extraInfo = extraInfo.toLowerCase().trim();
            if (extraInfo.startsWith("cmwap") || extraInfo.startsWith("uniwap") || extraInfo.startsWith("3gwap")) {
                d = 2;
                e = true;
                f = (byte) 0;
                g = "10.0.0.172";
            } else if (extraInfo.startsWith("ctwap")) {
                d = 2;
                e = true;
                f = (byte) 1;
                g = "10.0.0.200";
            } else if (extraInfo.startsWith("cmnet") || extraInfo.startsWith("uninet") || extraInfo.startsWith("ctnet") || extraInfo.startsWith("3gnet")) {
                d = 1;
                e = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void b() {
        ConnectivityManager connectivityManager = null;
        if (a != null) {
            connectivityManager = (ConnectivityManager) a.getSystemService("connectivity");
        }
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                a(activeNetworkInfo, activeNetworkInfo.isConnected());
                return;
            } else {
                c = false;
                return;
            }
        }
        c = false;
    }
}
