package com.duapps.ad.base;

import android.content.Context;
import com.duapps.ad.b.e;
import com.duapps.ad.c.a.b;
import com.duapps.ad.entity.f;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.entity.strategy.a;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: ChannelFactory */
public final class c {
    private static String[] a = new String[]{"download", "facebook", "inmobi", "online", "dlh"};

    public static long a(Context context, int i, int i2, List<String> list, ConcurrentHashMap<String, a<NativeAd>> concurrentHashMap) {
        long j = 0;
        if (context == null || list == null || list.size() == 0 || concurrentHashMap == null) {
            return 0;
        }
        f.c("ChannelFactory", "cacheSize==" + i2);
        b a = com.duapps.ad.c.a.a.a(context).a(i, true);
        Iterator it = list.iterator();
        while (true) {
            long j2 = j;
            if (!it.hasNext()) {
                return j2;
            }
            int i3;
            String str = (String) it.next();
            long a2 = a.a(str);
            if (list == null || list.size() == 0 || str == null) {
                i3 = 1;
            } else {
                int i4;
                int i5;
                if (i2 <= 0) {
                    i4 = 1;
                } else {
                    i4 = i2;
                }
                if (i4 <= list.size() - 1 || i4 <= 5) {
                    i5 = i4;
                } else {
                    i5 = 5;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(list);
                if (arrayList.contains("download")) {
                    arrayList.remove("download");
                }
                if (arrayList.contains("dlh")) {
                    arrayList.remove("dlh");
                }
                i3 = (i5 <= arrayList.size() || !((String) arrayList.get(0)).equals(str)) ? 1 : i5 - (arrayList.size() - 1);
            }
            f.c("ChannelFactory", "Create channel:" + str + ",wt:" + a2 + ",cacheSize: " + i3);
            Object obj = null;
            if ("facebook".equals(str)) {
                obj = new f(context, i, a2, i3);
            } else if ("download".equals(str)) {
                obj = new r(context, i, a2);
            } else if ("inmobi".equals(str)) {
                obj = new e(context, i, a2, i3);
            } else if ("dlh".equals(str)) {
                obj = new com.duapps.ad.a.a(context, i, a2);
            } else if ("online".equals(str)) {
                obj = new u(context, i, a2, i3);
            } else {
                f.d("ChannelFactory", "Unsupport error channel:" + str);
            }
            if (obj != null) {
                concurrentHashMap.put(str, obj);
                j = j2 + a2;
            } else {
                j = j2;
            }
        }
    }

    public static List<String> a(List<String> list, Context context, int i) {
        boolean z;
        int i2 = 0;
        ArrayList arrayList = new ArrayList(a.length);
        List asList = Arrays.asList(a);
        List a = t.a(context).a(i);
        if (a == null || a.size() <= 0) {
            z = false;
        } else {
            z = true;
        }
        while (i2 < list.size()) {
            String str = (String) list.get(i2);
            if (asList.contains(str)) {
                if (!"facebook".equals(str) || z) {
                    arrayList.add(str);
                } else {
                    f.c("ChannelFactory", "channel:facebook, is fbids exist:" + z);
                }
            }
            i2++;
        }
        return arrayList;
    }
}
