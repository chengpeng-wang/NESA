package com.facebook.ads.internal.thirdparty.http;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class g implements q {
    private void a(Map<String, List<String>> map) {
        if (map != null) {
            for (String str : map.keySet()) {
                for (String str2 : (List) map.get(str)) {
                    a(str + ":" + str2);
                }
            }
        }
    }

    public void a(n nVar) {
        if (nVar != null) {
            a("=== HTTP Response ===");
            a("Receive url: " + nVar.b());
            a("Status: " + nVar.a());
            a(nVar.c());
            a("Content:\n" + nVar.e());
        }
    }

    public void a(String str) {
        System.out.println(str);
    }

    public void a(HttpURLConnection httpURLConnection, Object obj) {
        a("=== HTTP Request ===");
        a(httpURLConnection.getRequestMethod() + " " + httpURLConnection.getURL().toString());
        if (obj instanceof String) {
            a("Content: " + ((String) obj));
        }
        a(httpURLConnection.getRequestProperties());
    }

    public boolean a() {
        return false;
    }
}
