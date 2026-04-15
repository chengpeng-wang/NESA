package com.flurry.sdk;

import android.content.Context;
import android.location.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public final class ha {
    private static final AtomicInteger a = new AtomicInteger(0);

    public static int a() {
        return a.incrementAndGet();
    }

    public static List a(dq dqVar, b bVar) {
        ArrayList arrayList = new ArrayList();
        List<dy> list = dqVar.e;
        String str = bVar.a.J;
        for (dy dyVar : list) {
            if (dyVar.a.equals(str)) {
                for (CharSequence charSequence : dyVar.b) {
                    HashMap hashMap = new HashMap();
                    String charSequence2 = charSequence.toString();
                    int indexOf = charSequence2.indexOf(63);
                    if (indexOf != -1) {
                        String substring = charSequence2.substring(0, indexOf);
                        charSequence2 = charSequence2.substring(indexOf + 1);
                        if (charSequence2.contains("%{eventParams}")) {
                            charSequence2 = charSequence2.replace("%{eventParams}", "");
                            hashMap.putAll(bVar.b);
                        }
                        hashMap.putAll(lt.e(charSequence2));
                        charSequence2 = substring;
                    }
                    arrayList.add(new a(a.b(charSequence2), hashMap, bVar));
                }
            }
        }
        return arrayList;
    }

    public static List a(List list) {
        ArrayList arrayList = new ArrayList();
        for (cz czVar : list) {
            el elVar = new el();
            elVar.a = czVar.b;
            elVar.b = czVar.c == null ? "" : czVar.c;
            ArrayList arrayList2 = new ArrayList();
            synchronized (czVar) {
                for (cv cvVar : czVar.d) {
                    if (cvVar.b) {
                        ek ekVar = new ek();
                        ekVar.a = cvVar.a;
                        ekVar.c = cvVar.c;
                        Map map = cvVar.d;
                        HashMap hashMap = new HashMap();
                        hashMap.putAll(map);
                        ekVar.b = hashMap;
                        arrayList2.add(ekVar);
                    }
                }
            }
            elVar.c = arrayList2;
            if (arrayList2.size() > 0) {
                arrayList.add(elVar);
            }
        }
        return arrayList;
    }

    public static ej b() {
        int d = lr.d();
        return d == 1 ? ej.PORTRAIT : d == 2 ? ej.LANDSCAPE : ej.UNKNOWN;
    }

    public static ee c() {
        Location location = null;
        ee eeVar = new ee();
        hp a = hp.a();
        if (a.c != null) {
            location = a.c;
        } else {
            if (a.b) {
                Context context = hz.a.b;
                if (hp.a(context) || hp.b(context)) {
                    String c = hp.a(context) ? hp.c() : hp.b(context) ? hp.d() : null;
                    if (c != null) {
                        location = a.a(c);
                        if (location != null) {
                            a.d = location;
                        }
                        location = a.d;
                    }
                }
            }
            iw.a(4, hp.a, "getLocation() = " + location);
        }
        if (location != null) {
            double latitude = (double) ((float) location.getLatitude());
            double longitude = (double) ((float) location.getLongitude());
            eeVar.a = (float) lt.a(latitude);
            eeVar.b = (float) lt.a(longitude);
        }
        return eeVar;
    }

    public static List d() {
        ArrayList arrayList = new ArrayList();
        HashMap b = ib.a().b();
        if (!b.isEmpty()) {
            for (String add : b.keySet()) {
                arrayList.add(add);
            }
        }
        return arrayList;
    }

    public static List e() {
        ArrayList arrayList = new ArrayList();
        for (Entry entry : Collections.unmodifiableMap(hi.a().a).entrySet()) {
            dr drVar = new dr();
            drVar.a = ((hv) entry.getKey()).d;
            if (((hv) entry.getKey()).e) {
                drVar.b = new String((byte[]) entry.getValue());
            } else {
                drVar.b = lt.b((byte[]) entry.getValue());
            }
            arrayList.add(drVar);
        }
        return arrayList;
    }

    public static List f() {
        ArrayList arrayList = new ArrayList();
        n.a().f.b();
        for (dj djVar : n.a().f.a()) {
            eb ebVar = new eb();
            ebVar.a = djVar.a;
            ebVar.b = djVar.b;
            ebVar.d = djVar.d;
            ebVar.c = djVar.c;
            ebVar.e = djVar.j;
            ebVar.f = djVar.e;
            ebVar.g = djVar.i;
            ebVar.h = djVar.f;
            ebVar.i = djVar.g;
            ebVar.j = djVar.h;
            arrayList.add(ebVar);
        }
        return arrayList;
    }
}
