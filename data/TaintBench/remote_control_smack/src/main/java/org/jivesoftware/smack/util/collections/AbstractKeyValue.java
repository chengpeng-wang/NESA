package org.jivesoftware.smack.util.collections;

public abstract class AbstractKeyValue<K, V> implements KeyValue<K, V> {
    protected K key;
    protected V value;

    protected AbstractKeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public String toString() {
        return new StringBuffer().append(getKey()).append('=').append(getValue()).toString();
    }
}
