package com.cleanmaster.base.util.misc;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* compiled from: MapCollections */
abstract class d {
    f b;

    public abstract int a();

    public abstract int a(Object obj);

    public abstract Object a(int i, int i2);

    public abstract void a(int i);

    public abstract Map b();

    public abstract void c();

    d() {
    }

    public static boolean a(Map map, Collection collection) {
        for (Object containsKey : collection) {
            if (!map.containsKey(containsKey)) {
                return false;
            }
        }
        return true;
    }

    public static boolean b(Map map, Collection collection) {
        int size = map.size();
        for (Object remove : collection) {
            map.remove(remove);
        }
        return size != map.size();
    }

    public static boolean c(Map map, Collection collection) {
        int size = map.size();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    public Object[] b(int i) {
        int a = a();
        Object[] objArr = new Object[a];
        for (int i2 = 0; i2 < a; i2++) {
            objArr[i2] = a(i2, i);
        }
        return objArr;
    }

    public Object[] a(Object[] objArr, int i) {
        Object[] objArr2;
        int a = a();
        if (objArr.length < a) {
            objArr2 = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), a);
        } else {
            objArr2 = objArr;
        }
        for (int i2 = 0; i2 < a; i2++) {
            objArr2[i2] = a(i2, i);
        }
        if (objArr2.length > a) {
            objArr2[a] = null;
        }
        return objArr2;
    }

    public static boolean a(Set set, Object obj) {
        if (set == obj) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set2 = (Set) obj;
        try {
            if (set.size() == set2.size() && set.containsAll(set2)) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public Set d() {
        if (this.b == null) {
            this.b = new f(this);
        }
        return this.b;
    }
}
