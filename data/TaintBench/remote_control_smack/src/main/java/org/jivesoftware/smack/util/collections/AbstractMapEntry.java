package org.jivesoftware.smack.util.collections;

import java.util.Map.Entry;

public abstract class AbstractMapEntry<K, V> extends AbstractKeyValue<K, V> implements Entry<K, V> {
    protected AbstractMapEntry(K key, V value) {
        super(key, value);
    }

    public V setValue(V value) {
        V answer = this.value;
        this.value = value;
        return answer;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Entry)) {
            return false;
        }
        Entry other = (Entry) obj;
        if (getKey() != null ? !getKey().equals(other.getKey()) : other.getKey() != null) {
            if (getValue() == null) {
                if (other.getValue() == null) {
                    return true;
                }
            } else if (getValue().equals(other.getValue())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = getKey() == null ? 0 : getKey().hashCode();
        if (getValue() != null) {
            i = getValue().hashCode();
        }
        return hashCode ^ i;
    }
}
