package org.jivesoftware.smack.util.collections;

import java.util.Map.Entry;

public final class DefaultMapEntry<K, V> extends AbstractMapEntry<K, V> {
    public DefaultMapEntry(K key, V value) {
        super(key, value);
    }

    public DefaultMapEntry(KeyValue<K, V> pair) {
        super(pair.getKey(), pair.getValue());
    }

    public DefaultMapEntry(Entry<K, V> entry) {
        super(entry.getKey(), entry.getValue());
    }
}
