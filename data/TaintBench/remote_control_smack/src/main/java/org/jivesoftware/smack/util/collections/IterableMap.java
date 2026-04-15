package org.jivesoftware.smack.util.collections;

import java.util.Map;

public interface IterableMap<K, V> extends Map<K, V> {
    MapIterator<K, V> mapIterator();
}
