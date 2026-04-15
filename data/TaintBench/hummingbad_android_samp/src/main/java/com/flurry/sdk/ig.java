package com.flurry.sdk;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class ig {
    private final Map a;

    public ig() {
        this.a = new HashMap();
    }

    public ig(Map map) {
        this.a = map;
    }

    public final Collection a() {
        ArrayList arrayList = new ArrayList();
        for (Entry entry : this.a.entrySet()) {
            for (Object simpleImmutableEntry : (List) entry.getValue()) {
                arrayList.add(new SimpleImmutableEntry(entry.getKey(), simpleImmutableEntry));
            }
        }
        return arrayList;
    }

    public final List a(Object obj) {
        if (obj == null) {
            return Collections.emptyList();
        }
        List a = a(obj, false);
        return a == null ? Collections.emptyList() : a;
    }

    public final List a(Object obj, boolean z) {
        List list = (List) this.a.get(obj);
        if (!z || list != null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        this.a.put(obj, arrayList);
        return arrayList;
    }

    public final void a(Object obj, Object obj2) {
        if (obj != null) {
            a(obj, true).add(obj2);
        }
    }

    public final Collection b() {
        ArrayList arrayList = new ArrayList();
        for (Entry value : this.a.entrySet()) {
            arrayList.addAll((Collection) value.getValue());
        }
        return arrayList;
    }

    public final boolean b(Object obj) {
        return obj == null ? false : ((List) this.a.remove(obj)) != null;
    }

    public final boolean b(Object obj, Object obj2) {
        boolean z = false;
        if (obj != null) {
            List a = a(obj, false);
            if (a != null) {
                z = a.remove(obj2);
                if (a.size() == 0) {
                    this.a.remove(obj);
                }
            }
        }
        return z;
    }
}
