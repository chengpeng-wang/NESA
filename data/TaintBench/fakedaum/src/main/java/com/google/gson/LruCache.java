package com.google.gson;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

final class LruCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {
    private static final long serialVersionUID = 1;
    private final int maxCapacity;

    public LruCache(int maxCapacity) {
        super(maxCapacity, 0.7f, true);
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addElement(K key, V value) {
        put(key, value);
    }

    public synchronized V getElement(K key) {
        return get(key);
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Entry<K, V> entry) {
        return size() > this.maxCapacity;
    }
}
