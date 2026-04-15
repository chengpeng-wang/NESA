package com.baidu.platform.comapi.map;

import com.baidu.platform.comapi.map.q.a;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class s<E> extends ArrayList<E> {
    private a a = null;

    public void a(a aVar) {
        this.a = aVar;
    }

    public boolean add(E e) {
        if (e == null) {
            return false;
        }
        if (this.a != null) {
            this.a.a(e);
        }
        return super.add(e);
    }

    public boolean addAll(Collection<? extends E> collection) {
        for (Object next : collection) {
            if (this.a != null) {
                this.a.a(next);
            }
        }
        return super.addAll(collection);
    }

    public void clear() {
        Iterator it = iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (this.a != null) {
                this.a.b(next);
            }
        }
        super.clear();
    }

    public E remove(int i) {
        Object remove = super.remove(i);
        if (!(this.a == null || remove == null)) {
            this.a.b(remove);
        }
        return remove;
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.a != null) {
            this.a.b(obj);
        }
        return super.remove(obj);
    }

    public boolean removeAll(Collection<?> collection) {
        for (Object next : collection) {
            if (this.a != null) {
                this.a.b(next);
            }
        }
        return super.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        Iterator it = iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (!(this.a == null || collection.contains(next))) {
                this.a.b(next);
            }
        }
        return super.retainAll(collection);
    }
}
