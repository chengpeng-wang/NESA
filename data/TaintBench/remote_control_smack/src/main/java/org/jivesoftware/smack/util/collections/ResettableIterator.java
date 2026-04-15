package org.jivesoftware.smack.util.collections;

import java.util.Iterator;

public interface ResettableIterator<E> extends Iterator<E> {
    void reset();
}
