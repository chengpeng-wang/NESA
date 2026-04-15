package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.s;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class e {
    private static final Set<g> a = new HashSet();
    private static final Map<AdPlacementType, String> b = new ConcurrentHashMap();

    static {
        for (g gVar : g.values()) {
            Class cls;
            switch (gVar.g) {
                case BANNER:
                    cls = BannerAdapter.class;
                    break;
                case INTERSTITIAL:
                    cls = InterstitialAdapter.class;
                    break;
                case NATIVE:
                    cls = n.class;
                    break;
                default:
                    cls = null;
                    break;
            }
            if (cls != null) {
                Class cls2 = gVar.d;
                if (cls2 == null) {
                    try {
                        cls2 = Class.forName(gVar.e);
                    } catch (ClassNotFoundException e) {
                    }
                }
                if (cls2 != null && cls.isAssignableFrom(cls2)) {
                    a.add(gVar);
                }
            }
        }
    }

    public static AdAdapter a(f fVar, AdPlacementType adPlacementType) {
        try {
            g b = b(fVar, adPlacementType);
            if (b != null && a.contains(b)) {
                Class cls = b.d;
                if (cls == null) {
                    cls = Class.forName(b.e);
                }
                return (AdAdapter) cls.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AdAdapter a(String str, AdPlacementType adPlacementType) {
        return a(f.a(str), adPlacementType);
    }

    public static String a(AdPlacementType adPlacementType) {
        if (b.containsKey(adPlacementType)) {
            return (String) b.get(adPlacementType);
        }
        HashSet hashSet = new HashSet();
        for (g gVar : a) {
            if (gVar.g == adPlacementType) {
                hashSet.add(gVar.f.toString());
            }
        }
        String a = s.a(hashSet, ",");
        b.put(adPlacementType, a);
        return a;
    }

    private static g b(f fVar, AdPlacementType adPlacementType) {
        for (g gVar : a) {
            if (gVar.f == fVar && gVar.g == adPlacementType) {
                return gVar;
            }
        }
        return null;
    }
}
