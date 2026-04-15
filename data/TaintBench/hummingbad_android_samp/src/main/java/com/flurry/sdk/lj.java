package com.flurry.sdk;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class lj {
    private final Map a = new HashMap();
    private final Map b = new HashMap();

    public final synchronized Object a(String str) {
        return this.a.get(str);
    }

    public final synchronized void a(String str, lk lkVar) {
        if (!(TextUtils.isEmpty(str) || lkVar == null)) {
            List list = (List) this.b.get(str);
            if (list == null) {
                list = new LinkedList();
            }
            list.add(lkVar);
            this.b.put(str, list);
        }
    }

    public final synchronized void a(String str, Object obj) {
        if (!TextUtils.isEmpty(str)) {
            Object obj2 = this.a.get(str);
            obj2 = (obj == obj2 || (obj != null && obj.equals(obj2))) ? 1 : null;
            if (obj2 == null) {
                if (obj == null) {
                    this.a.remove(str);
                } else {
                    this.a.put(str, obj);
                }
                if (this.b.get(str) != null) {
                    for (lk a : (List) this.b.get(str)) {
                        a.a(str, obj);
                    }
                }
            }
        }
    }
}
