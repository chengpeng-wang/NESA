package org.jivesoftware.smack.util.collections;

import java.util.Iterator;

public class EmptyIterator<E> extends AbstractEmptyIterator<E> implements ResettableIterator<E> {
    public static final Iterator INSTANCE = RESETTABLE_INSTANCE;
    public static final ResettableIterator RESETTABLE_INSTANCE = new EmptyIterator();

    public static <T> Iterator<T> getInstance() {
        return INSTANCE;
    }

    protected EmptyIterator() {
    }
}
