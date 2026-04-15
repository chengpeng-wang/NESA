package org.jivesoftware.smack.util.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class AbstractHashedMap<K, V> extends AbstractMap<K, V> implements IterableMap<K, V> {
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
    protected static final int DEFAULT_THRESHOLD = 12;
    protected static final String GETKEY_INVALID = "getKey() can only be called after next() and before remove()";
    protected static final String GETVALUE_INVALID = "getValue() can only be called after next() and before remove()";
    protected static final int MAXIMUM_CAPACITY = 1073741824;
    protected static final String NO_NEXT_ENTRY = "No next() entry in the iteration";
    protected static final String NO_PREVIOUS_ENTRY = "No previous() entry in the iteration";
    protected static final Object NULL = new Object();
    protected static final String REMOVE_INVALID = "remove() can only be called once after next()";
    protected static final String SETVALUE_INVALID = "setValue() can only be called after next() and before remove()";
    protected transient HashEntry<K, V>[] data;
    protected transient EntrySet<K, V> entrySet;
    protected transient KeySet<K, V> keySet;
    protected transient float loadFactor;
    protected transient int modCount;
    protected transient int size;
    protected transient int threshold;
    protected transient Values<K, V> values;

    protected static class EntrySet<K, V> extends AbstractSet<Entry<K, V>> {
        protected final AbstractHashedMap<K, V> parent;

        protected EntrySet(AbstractHashedMap<K, V> parent) {
            this.parent = parent;
        }

        public int size() {
            return this.parent.size();
        }

        public void clear() {
            this.parent.clear();
        }

        public boolean contains(Entry<K, V> entry) {
            Entry<K, V> e = entry;
            Entry<K, V> match = this.parent.getEntry(e.getKey());
            return match != null && match.equals(e);
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Entry) || !contains(obj)) {
                return false;
            }
            this.parent.remove(((Entry) obj).getKey());
            return true;
        }

        public Iterator<Entry<K, V>> iterator() {
            return this.parent.createEntrySetIterator();
        }
    }

    protected static abstract class HashIterator<K, V> {
        protected int expectedModCount;
        protected int hashIndex;
        protected HashEntry<K, V> last;
        protected HashEntry<K, V> next;
        protected final AbstractHashedMap parent;

        protected HashIterator(AbstractHashedMap<K, V> parent) {
            this.parent = parent;
            HashEntry<K, V>[] data = parent.data;
            int i = data.length;
            HashEntry<K, V> next = null;
            while (i > 0 && next == null) {
                i--;
                next = data[i];
            }
            this.next = next;
            this.hashIndex = i;
            this.expectedModCount = parent.modCount;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        /* access modifiers changed from: protected */
        public HashEntry<K, V> nextEntry() {
            if (this.parent.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            HashEntry<K, V> newCurrent = this.next;
            if (newCurrent == null) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            HashEntry<K, V>[] data = this.parent.data;
            int i = this.hashIndex;
            HashEntry<K, V> n = newCurrent.next;
            while (n == null && i > 0) {
                i--;
                n = data[i];
            }
            this.next = n;
            this.hashIndex = i;
            this.last = newCurrent;
            return newCurrent;
        }

        /* access modifiers changed from: protected */
        public HashEntry<K, V> currentEntry() {
            return this.last;
        }

        public void remove() {
            if (this.last == null) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            } else if (this.parent.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            } else {
                this.parent.remove(this.last.getKey());
                this.last = null;
                this.expectedModCount = this.parent.modCount;
            }
        }

        public String toString() {
            if (this.last != null) {
                return "Iterator[" + this.last.getKey() + "=" + this.last.getValue() + "]";
            }
            return "Iterator[]";
        }
    }

    protected static class KeySet<K, V> extends AbstractSet<K> {
        protected final AbstractHashedMap<K, V> parent;

        protected KeySet(AbstractHashedMap<K, V> parent) {
            this.parent = parent;
        }

        public int size() {
            return this.parent.size();
        }

        public void clear() {
            this.parent.clear();
        }

        public boolean contains(Object key) {
            return this.parent.containsKey(key);
        }

        public boolean remove(Object key) {
            boolean result = this.parent.containsKey(key);
            this.parent.remove(key);
            return result;
        }

        public Iterator<K> iterator() {
            return this.parent.createKeySetIterator();
        }
    }

    protected static class Values<K, V> extends AbstractCollection<V> {
        protected final AbstractHashedMap<K, V> parent;

        protected Values(AbstractHashedMap<K, V> parent) {
            this.parent = parent;
        }

        public int size() {
            return this.parent.size();
        }

        public void clear() {
            this.parent.clear();
        }

        public boolean contains(Object value) {
            return this.parent.containsValue(value);
        }

        public Iterator<V> iterator() {
            return this.parent.createValuesIterator();
        }
    }

    protected static class EntrySetIterator<K, V> extends HashIterator<K, V> implements Iterator<Entry<K, V>> {
        protected EntrySetIterator(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        public HashEntry<K, V> next() {
            return super.nextEntry();
        }
    }

    protected static class HashEntry<K, V> implements Entry<K, V>, KeyValue<K, V> {
        protected int hashCode;
        /* access modifiers changed from: private */
        public K key;
        protected HashEntry<K, V> next;
        /* access modifiers changed from: private */
        public V value;

        protected HashEntry(HashEntry<K, V> next, int hashCode, K key, V value) {
            this.next = next;
            this.hashCode = hashCode;
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
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

        public String toString() {
            return new StringBuffer().append(getKey()).append('=').append(getValue()).toString();
        }
    }

    protected static class HashMapIterator<K, V> extends HashIterator<K, V> implements MapIterator<K, V> {
        protected HashMapIterator(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        public K next() {
            return super.nextEntry().getKey();
        }

        public K getKey() {
            HashEntry<K, V> current = currentEntry();
            if (current != null) {
                return current.getKey();
            }
            throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
        }

        public V getValue() {
            HashEntry<K, V> current = currentEntry();
            if (current != null) {
                return current.getValue();
            }
            throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
        }

        public V setValue(V value) {
            HashEntry<K, V> current = currentEntry();
            if (current != null) {
                return current.setValue(value);
            }
            throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
        }
    }

    protected static class KeySetIterator<K, V> extends HashIterator<K, V> implements Iterator<K> {
        protected KeySetIterator(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        public K next() {
            return super.nextEntry().getKey();
        }
    }

    protected static class ValuesIterator<K, V> extends HashIterator<K, V> implements Iterator<V> {
        protected ValuesIterator(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        public V next() {
            return super.nextEntry().getValue();
        }
    }

    protected AbstractHashedMap() {
    }

    protected AbstractHashedMap(int initialCapacity, float loadFactor, int threshold) {
        this.loadFactor = loadFactor;
        this.data = new HashEntry[initialCapacity];
        this.threshold = threshold;
        init();
    }

    protected AbstractHashedMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    protected AbstractHashedMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0");
        } else if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be greater than 0");
        } else {
            this.loadFactor = loadFactor;
            this.threshold = calculateThreshold(initialCapacity, loadFactor);
            this.data = new HashEntry[calculateNewCapacity(initialCapacity)];
            init();
        }
    }

    protected AbstractHashedMap(Map<? extends K, ? extends V> map) {
        this(Math.max(map.size() * 2, 16), DEFAULT_LOAD_FACTOR);
        putAll(map);
    }

    /* access modifiers changed from: protected */
    public void init() {
    }

    public V get(Object key) {
        Object obj;
        if (key == null) {
            obj = NULL;
        } else {
            obj = key;
        }
        int hashCode = hash(obj);
        HashEntry<K, V> entry = this.data[hashIndex(hashCode, this.data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.key)) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean containsKey(Object key) {
        Object obj;
        if (key == null) {
            obj = NULL;
        } else {
            obj = key;
        }
        int hashCode = hash(obj);
        HashEntry entry = this.data[hashIndex(hashCode, this.data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        HashEntry entry;
        if (value == null) {
            for (HashEntry entry2 : this.data) {
                for (entry2 = this.data[i]; entry2 != null; entry2 = entry2.next) {
                    if (entry2.getValue() == null) {
                        return true;
                    }
                }
            }
        } else {
            for (HashEntry entry22 : this.data) {
                for (entry22 = this.data[i]; entry22 != null; entry22 = entry22.next) {
                    if (isEqualValue(value, entry22.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public V put(K key, V value) {
        Object obj;
        if (key == null) {
            obj = NULL;
        } else {
            K obj2 = key;
        }
        int hashCode = hash(obj2);
        int index = hashIndex(hashCode, this.data.length);
        HashEntry<K, V> entry = this.data[index];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                V oldValue = entry.getValue();
                updateEntry(entry, value);
                return oldValue;
            }
            entry = entry.next;
        }
        addMapping(index, hashCode, key, value);
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        int mapSize = map.size();
        if (mapSize != 0) {
            ensureCapacity(calculateNewCapacity((int) ((((float) (this.size + mapSize)) / this.loadFactor) + 1.0f)));
            for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public V remove(Object key) {
        Object obj;
        if (key == null) {
            obj = NULL;
        } else {
            obj = key;
        }
        int hashCode = hash(obj);
        int index = hashIndex(hashCode, this.data.length);
        HashEntry<K, V> entry = this.data[index];
        HashEntry<K, V> previous = null;
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                V oldValue = entry.getValue();
                removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    public void clear() {
        this.modCount++;
        HashEntry[] data = this.data;
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] = null;
        }
        this.size = 0;
    }

    /* access modifiers changed from: protected */
    public int hash(Object key) {
        int h = key.hashCode();
        h += (h << 9) ^ -1;
        h ^= h >>> 14;
        h += h << 4;
        return h ^ (h >>> 10);
    }

    /* access modifiers changed from: protected */
    public boolean isEqualKey(Object key1, Object key2) {
        return key1 == key2 || (key1 != null && key1.equals(key2));
    }

    /* access modifiers changed from: protected */
    public boolean isEqualValue(Object value1, Object value2) {
        return value1 == value2 || value1.equals(value2);
    }

    /* access modifiers changed from: protected */
    public int hashIndex(int hashCode, int dataSize) {
        return (dataSize - 1) & hashCode;
    }

    /* access modifiers changed from: protected */
    public HashEntry<K, V> getEntry(Object key) {
        Object obj;
        if (key == null) {
            obj = NULL;
        } else {
            obj = key;
        }
        int hashCode = hash(obj);
        HashEntry<K, V> entry = this.data[hashIndex(hashCode, this.data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void updateEntry(HashEntry<K, V> entry, V newValue) {
        entry.setValue(newValue);
    }

    /* access modifiers changed from: protected */
    public void reuseEntry(HashEntry<K, V> entry, int hashIndex, int hashCode, K key, V value) {
        entry.next = this.data[hashIndex];
        entry.hashCode = hashCode;
        entry.key = key;
        entry.value = value;
    }

    /* access modifiers changed from: protected */
    public void addMapping(int hashIndex, int hashCode, K key, V value) {
        this.modCount++;
        addEntry(createEntry(this.data[hashIndex], hashCode, key, value), hashIndex);
        this.size++;
        checkCapacity();
    }

    /* access modifiers changed from: protected */
    public HashEntry<K, V> createEntry(HashEntry<K, V> next, int hashCode, K key, V value) {
        return new HashEntry(next, hashCode, key, value);
    }

    /* access modifiers changed from: protected */
    public void addEntry(HashEntry<K, V> entry, int hashIndex) {
        this.data[hashIndex] = entry;
    }

    /* access modifiers changed from: protected */
    public void removeMapping(HashEntry<K, V> entry, int hashIndex, HashEntry<K, V> previous) {
        this.modCount++;
        removeEntry(entry, hashIndex, previous);
        this.size--;
        destroyEntry(entry);
    }

    /* access modifiers changed from: protected */
    public void removeEntry(HashEntry<K, V> entry, int hashIndex, HashEntry<K, V> previous) {
        if (previous == null) {
            this.data[hashIndex] = entry.next;
        } else {
            previous.next = entry.next;
        }
    }

    /* access modifiers changed from: protected */
    public void destroyEntry(HashEntry<K, V> entry) {
        entry.next = null;
        entry.key = null;
        entry.value = null;
    }

    /* access modifiers changed from: protected */
    public void checkCapacity() {
        if (this.size >= this.threshold) {
            int newCapacity = this.data.length * 2;
            if (newCapacity <= MAXIMUM_CAPACITY) {
                ensureCapacity(newCapacity);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void ensureCapacity(int newCapacity) {
        int oldCapacity = this.data.length;
        if (newCapacity > oldCapacity) {
            if (this.size == 0) {
                this.threshold = calculateThreshold(newCapacity, this.loadFactor);
                this.data = new HashEntry[newCapacity];
                return;
            }
            HashEntry<K, V>[] oldEntries = this.data;
            HashEntry<K, V>[] newEntries = new HashEntry[newCapacity];
            this.modCount++;
            for (int i = oldCapacity - 1; i >= 0; i--) {
                HashEntry<K, V> entry = oldEntries[i];
                if (entry != null) {
                    oldEntries[i] = null;
                    do {
                        HashEntry<K, V> next = entry.next;
                        int index = hashIndex(entry.hashCode, newCapacity);
                        entry.next = newEntries[index];
                        newEntries[index] = entry;
                        entry = next;
                    } while (entry != null);
                }
            }
            this.threshold = calculateThreshold(newCapacity, this.loadFactor);
            this.data = newEntries;
        }
    }

    /* access modifiers changed from: protected */
    public int calculateNewCapacity(int proposedCapacity) {
        int newCapacity = 1;
        if (proposedCapacity > MAXIMUM_CAPACITY) {
            return MAXIMUM_CAPACITY;
        }
        while (newCapacity < proposedCapacity) {
            newCapacity <<= 1;
        }
        if (newCapacity > MAXIMUM_CAPACITY) {
            return MAXIMUM_CAPACITY;
        }
        return newCapacity;
    }

    /* access modifiers changed from: protected */
    public int calculateThreshold(int newCapacity, float factor) {
        return (int) (((float) newCapacity) * factor);
    }

    /* access modifiers changed from: protected */
    public HashEntry<K, V> entryNext(HashEntry<K, V> entry) {
        return entry.next;
    }

    /* access modifiers changed from: protected */
    public int entryHashCode(HashEntry<K, V> entry) {
        return entry.hashCode;
    }

    /* access modifiers changed from: protected */
    public K entryKey(HashEntry<K, V> entry) {
        return entry.key;
    }

    /* access modifiers changed from: protected */
    public V entryValue(HashEntry<K, V> entry) {
        return entry.value;
    }

    public MapIterator<K, V> mapIterator() {
        if (this.size == 0) {
            return EmptyMapIterator.INSTANCE;
        }
        return new HashMapIterator(this);
    }

    public Set<Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet(this);
        }
        return this.entrySet;
    }

    /* access modifiers changed from: protected */
    public Iterator<Entry<K, V>> createEntrySetIterator() {
        if (size() == 0) {
            return EmptyIterator.INSTANCE;
        }
        return new EntrySetIterator(this);
    }

    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new KeySet(this);
        }
        return this.keySet;
    }

    /* access modifiers changed from: protected */
    public Iterator<K> createKeySetIterator() {
        if (size() == 0) {
            return EmptyIterator.INSTANCE;
        }
        return new KeySetIterator(this);
    }

    public Collection<V> values() {
        if (this.values == null) {
            this.values = new Values(this);
        }
        return this.values;
    }

    /* access modifiers changed from: protected */
    public Iterator<V> createValuesIterator() {
        if (size() == 0) {
            return EmptyIterator.INSTANCE;
        }
        return new ValuesIterator(this);
    }

    /* access modifiers changed from: protected */
    public void doWriteObject(ObjectOutputStream out) throws IOException {
        out.writeFloat(this.loadFactor);
        out.writeInt(this.data.length);
        out.writeInt(this.size);
        MapIterator it = mapIterator();
        while (it.hasNext()) {
            out.writeObject(it.next());
            out.writeObject(it.getValue());
        }
    }

    /* access modifiers changed from: protected */
    public void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.loadFactor = in.readFloat();
        int capacity = in.readInt();
        int size = in.readInt();
        init();
        this.data = new HashEntry[capacity];
        for (int i = 0; i < size; i++) {
            put(in.readObject(), in.readObject());
        }
        this.threshold = calculateThreshold(this.data.length, this.loadFactor);
    }

    /* access modifiers changed from: protected */
    public Object clone() {
        try {
            AbstractHashedMap cloned = (AbstractHashedMap) super.clone();
            cloned.data = new HashEntry[this.data.length];
            cloned.entrySet = null;
            cloned.keySet = null;
            cloned.values = null;
            cloned.modCount = 0;
            cloned.size = 0;
            cloned.init();
            cloned.putAll(this);
            return cloned;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        if (map.size() != size()) {
            return false;
        }
        MapIterator it = mapIterator();
        while (it.hasNext()) {
            try {
                Object key = it.next();
                Object value = it.getValue();
                if (value == null) {
                    if (map.get(key) != null || !map.containsKey(key)) {
                        return false;
                    }
                } else if (!value.equals(map.get(key))) {
                    return false;
                }
            } catch (ClassCastException e) {
                return false;
            } catch (NullPointerException e2) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int total = 0;
        Iterator it = createEntrySetIterator();
        while (it.hasNext()) {
            total += it.next().hashCode();
        }
        return total;
    }

    public String toString() {
        if (size() == 0) {
            return "{}";
        }
        StringBuffer buf = new StringBuffer(size() * 32);
        buf.append('{');
        MapIterator it = mapIterator();
        boolean hasNext = it.hasNext();
        while (hasNext) {
            Object key = it.next();
            Object value = it.getValue();
            if (key == this) {
                key = "(this Map)";
            }
            StringBuffer append = buf.append(key).append('=');
            if (value == this) {
                value = "(this Map)";
            }
            append.append(value);
            hasNext = it.hasNext();
            if (hasNext) {
                buf.append(',').append(' ');
            }
        }
        buf.append('}');
        return buf.toString();
    }
}
