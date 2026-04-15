package org.jivesoftware.smackx.workgroup.util;

import java.util.Iterator;
import java.util.ListIterator;

/* compiled from: ModelUtil */
class ReverseListIterator implements Iterator {
    private ListIterator _i;

    ReverseListIterator(ListIterator i) {
        this._i = i;
        while (this._i.hasNext()) {
            this._i.next();
        }
    }

    public boolean hasNext() {
        return this._i.hasPrevious();
    }

    public Object next() {
        return this._i.previous();
    }

    public void remove() {
        this._i.remove();
    }
}
