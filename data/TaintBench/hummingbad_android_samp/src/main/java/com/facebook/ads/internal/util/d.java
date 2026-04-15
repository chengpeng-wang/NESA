package com.facebook.ads.internal.util;

import com.facebook.ads.internal.dto.b;
import com.facebook.ads.internal.dto.e;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class d {
    private static Map<String, Long> a = new ConcurrentHashMap();
    private static Map<String, Long> b = new ConcurrentHashMap();
    private static Map<String, String> c = new ConcurrentHashMap();

    private static long a(String str, b bVar) {
        if (a.containsKey(str)) {
            return ((Long) a.get(str)).longValue();
        }
        switch (bVar) {
            case BANNER:
                return 15000;
            default:
                return -1000;
        }
    }

    public static void a(long j, e eVar) {
        a.put(d(eVar), Long.valueOf(j));
    }

    public static void a(String str, e eVar) {
        c.put(d(eVar), str);
    }

    public static boolean a(e eVar) {
        String d = d(eVar);
        if (!b.containsKey(d)) {
            return false;
        }
        return System.currentTimeMillis() - ((Long) b.get(d)).longValue() < a(d, eVar.b());
    }

    public static void b(e eVar) {
        b.put(d(eVar), Long.valueOf(System.currentTimeMillis()));
    }

    public static String c(e eVar) {
        return (String) c.get(d(eVar));
    }

    private static String d(e eVar) {
        int i = 0;
        String str = "%s:%s:%s:%d:%d:%d";
        Object[] objArr = new Object[6];
        objArr[0] = eVar.a();
        objArr[1] = eVar.b();
        objArr[2] = eVar.e;
        objArr[3] = Integer.valueOf(eVar.c() == null ? 0 : eVar.c().getHeight());
        if (eVar.c() != null) {
            i = eVar.c().getWidth();
        }
        objArr[4] = Integer.valueOf(i);
        objArr[5] = Integer.valueOf(eVar.d());
        return String.format(str, objArr);
    }
}
