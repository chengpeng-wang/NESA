package com.flurry.sdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class j {
    public static final String a = j.class.getSimpleName();
    private static final Set c = Collections.unmodifiableSet(new k());
    public final ik b = new l(this);

    private static void a() {
        il.a().a(new he());
    }

    private static void a(b bVar) {
        iw.a(3, "VerifyPackageLog", "onStartPrerender() Ready to pre-render.");
        bVar.d.g().d();
    }

    private static void a(b bVar, List list) {
        Object obj;
        for (a aVar : list) {
            if (aVar.a.equals(de.AC_NEXT_AD_UNIT) && bVar.b.containsValue(dg.EV_FILLED.J)) {
                obj = 1;
                break;
            }
        }
        obj = null;
        if (obj == null) {
            iw.a(3, "VerifyPackageLog", "onPackageVerified() no nextAdUnit or different originator, ready to fire PRE-RENDER. ");
            a(bVar);
        }
    }

    static /* synthetic */ void a(c cVar) {
        int i = 1;
        b bVar = cVar.a;
        String str = bVar.a.J;
        dq a = bVar.a();
        List<a> arrayList = new ArrayList();
        List<dy> list = a.e;
        String str2 = bVar.a.J;
        for (dy dyVar : list) {
            if (dyVar.a.equals(str2)) {
                for (String str3 : dyVar.b) {
                    String str32;
                    HashMap hashMap = new HashMap();
                    int indexOf = str32.indexOf(63);
                    if (indexOf != -1) {
                        String substring = str32.substring(0, indexOf);
                        str32 = str32.substring(indexOf + 1);
                        if (str32.contains("%{eventParams}")) {
                            str32 = str32.replace("%{eventParams}", "");
                            hashMap.putAll(bVar.b);
                        }
                        hashMap.putAll(lt.e(str32));
                        str32 = substring;
                    }
                    arrayList.add(new a(a.b(str32), hashMap, bVar));
                }
            }
        }
        f.a().a(str);
        n a2 = n.a();
        (a2.i != null ? a2.i.a : null).a(bVar);
        d dVar;
        ba baVar;
        switch (m.a[bVar.a.ordinal()]) {
            case 1:
                int i2 = ((String) bVar.b.remove("binding_3rd_party")) != null ? 1 : 0;
                if (((dq) bVar.e.a.d.get(0)).a != dd.e) {
                    i = i2;
                }
                if (bVar.b.remove("preRender") == null && i == 0) {
                    iw.a(3, a, "Firing onRenderFailed, adObject=" + bVar.d);
                    dVar = new d();
                    dVar.a = bVar.d;
                    dVar.b = e.kOnRenderFailed;
                    dVar.a();
                } else {
                    b(bVar, arrayList);
                }
                if (bVar.e.d()) {
                    a();
                }
                n.a().g.c(bVar.e);
                baVar = n.a().g;
                ba.a(bVar.d);
                n.a().g.c();
                break;
            case 2:
                break;
            case 3:
                iw.a(3, a, "Firing onClicked, adObject=" + bVar.d);
                if (bVar.d instanceof ag) {
                    f.a().a("nativeAdClick");
                }
                dVar = new d();
                dVar.a = bVar.d;
                dVar.b = e.kOnClicked;
                dVar.a();
                break;
            case 4:
                baVar = n.a().g;
                ba.a(bVar.d);
                n.a().g.c();
                break;
            case 5:
                c(bVar, arrayList);
                break;
            case 6:
                for (a aVar : arrayList) {
                    if (aVar.a.equals(de.AC_DIRECT_OPEN)) {
                        aVar.a("is_privacy", "true");
                    }
                }
                break;
            case 7:
                b(bVar);
                break;
            case 8:
                iw.a(3, a, "Firing onVideoClose, adObject=" + bVar.d);
                dVar = new d();
                dVar.a = bVar.d;
                dVar.b = e.kOnClose;
                dVar.a();
                break;
            case 9:
                b(bVar);
                break;
            case 10:
                iw.a(3, a, "Firing onOpen, adObject=" + bVar.d);
                dVar = new d();
                dVar.a = bVar.d;
                dVar.b = e.kOnOpen;
                dVar.a();
                break;
            case 11:
                iw.a(3, a, "Firing onAppExit, adObject=" + bVar.d);
                dVar = new d();
                dVar.a = bVar.d;
                dVar.b = e.kOnAppExit;
                dVar.a();
                a();
                break;
            case 12:
                HashMap hashMap2 = new HashMap();
                hashMap2.put("trigger:", "native impression");
                arrayList.add(new a(de.AC_SEND_AD_LOGS, hashMap2, bVar));
                iw.a(3, a, "Firing onAdImpressionLogged, adObject=" + bVar.d);
                dVar = new d();
                dVar.a = bVar.d;
                dVar.b = e.kOnImpressionLogged;
                dVar.a();
                break;
            case 13:
                if (bVar.d instanceof ag) {
                    f.a().a("nativeAdFilled");
                    break;
                }
                break;
            case 14:
                a(bVar, (List) arrayList);
                break;
            case 15:
                if (bVar.b.containsValue(dg.EV_FILLED.J)) {
                    iw.a(3, "VerifyPackageLog", "onPackageNotVerified() ready to fire PRE-RENDER.");
                    a(bVar);
                    break;
                }
                break;
            default:
                iw.a(3, a, "Event not handled: " + bVar.a + " for adSpace:" + bVar.e.a.b);
                break;
        }
        a(cVar, (List) arrayList);
    }

    private static void a(c cVar, List list) {
        a aVar = null;
        for (a aVar2 : list) {
            a aVar3;
            if (aVar2.a.equals(de.AC_LOG_EVENT)) {
                aVar2.a("__sendToServer", "true");
                aVar3 = aVar2;
            } else {
                aVar3 = aVar;
            }
            if (aVar2.a.equals(de.AC_LOAD_AD_COMPONENTS)) {
                for (Entry entry : aVar2.c.b.entrySet()) {
                    aVar2.a((String) entry.getKey(), (String) entry.getValue());
                }
            }
            iw.b(aVar2.toString());
            n.a().b().a(aVar2, cVar.b + 1);
            aVar = aVar3;
        }
        if (aVar == null) {
            HashMap hashMap = new HashMap();
            hashMap.put("__sendToServer", "false");
            aVar = new a(de.AC_LOG_EVENT, hashMap, cVar.a);
            iw.b(aVar.toString());
            n.a().b().a(aVar, cVar.b + 1);
        }
    }

    private static void b(b bVar) {
        iw.a(3, a, "Firing onClose, adObject=" + bVar.d);
        d dVar = new d();
        dVar.a = bVar.d;
        dVar.b = e.kOnClose;
        dVar.a();
        a();
    }

    private static void b(b bVar, List list) {
        Object obj;
        for (a aVar : list) {
            if (de.AC_NEXT_AD_UNIT.equals(aVar.a)) {
                obj = null;
                break;
            }
        }
        int obj2 = 1;
        if (obj2 != null) {
            iw.a(3, a, "Firing onFetchFailed, adObject=" + bVar.d);
            d dVar = new d();
            dVar.a = bVar.d;
            dVar.b = e.kOnFetchFailed;
            dVar.a();
        }
    }

    private static void c(b bVar, List list) {
        int i;
        for (a aVar : list) {
            if (c.contains(aVar.a)) {
                i = 1;
                break;
            }
        }
        i = 0;
        if (i == 0) {
            list.add(0, new a(de.AC_CLOSE_AD, Collections.emptyMap(), bVar));
            ba baVar = n.a().g;
            ba.a(bVar.d);
            n.a().g.c();
        }
    }
}
