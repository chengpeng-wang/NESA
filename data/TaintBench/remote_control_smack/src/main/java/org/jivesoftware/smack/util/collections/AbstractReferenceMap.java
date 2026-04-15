package org.jivesoftware.smack.util.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class AbstractReferenceMap<K, V> extends AbstractHashedMap<K, V> {
    public static final int HARD = 0;
    public static final int SOFT = 1;
    public static final int WEAK = 2;
    protected int keyType;
    protected boolean purgeValues;
    /* access modifiers changed from: private|transient */
    public transient ReferenceQueue queue;
    protected int valueType;

    static class ReferenceIteratorBase<K, V> {
        K currentKey;
        V currentValue;
        ReferenceEntry<K, V> entry;
        int expectedModCount;
        int index;
        K nextKey;
        V nextValue;
        final AbstractReferenceMap<K, V> parent;
        ReferenceEntry<K, V> previous;

        public ReferenceIteratorBase(AbstractReferenceMap<K, V> parent) {
            this.parent = parent;
            this.index = parent.size() != 0 ? parent.data.length : 0;
            this.expectedModCount = parent.modCount;
        }

        public boolean hasNext() {
            checkMod();
            while (nextNull()) {
                ReferenceEntry<K, V> e = this.entry;
                int i = this.index;
                while (e == null && i > 0) {
                    i--;
                    e = this.parent.data[i];
                }
                this.entry = e;
                this.index = i;
                if (e == null) {
                    this.currentKey = null;
                    this.currentValue = null;
                    return false;
                }
                this.nextKey = e.getKey();
                this.nextValue = e.getValue();
                if (nextNull()) {
                    this.entry = this.entry.next();
                }
            }
            return true;
        }

        private void checkMod() {
            if (this.parent.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        private boolean nextNull() {
            return this.nextKey == null || this.nextValue == null;
        }

        /* access modifiers changed from: protected */
        public ReferenceEntry<K, V> nextEntry() {
            checkMod();
            if (!nextNull() || hasNext()) {
                this.previous = this.entry;
                this.entry = this.entry.next();
                this.currentKey = this.nextKey;
                this.currentValue = this.nextValue;
                this.nextKey = null;
                this.nextValue = null;
                return this.previous;
            }
            throw new NoSuchElementException();
        }

        /* access modifiers changed from: protected */
        public ReferenceEntry<K, V> currentEntry() {
            checkMod();
            return this.previous;
        }

        public ReferenceEntry<K, V> superNext() {
            return nextEntry();
        }

        public void remove() {
            checkMod();
            if (this.previous == null) {
                throw new IllegalStateException();
            }
            this.parent.remove(this.currentKey);
            this.previous = null;
            this.currentKey = null;
            this.currentValue = null;
            this.expectedModCount = this.parent.modCount;
        }
    }

    static class SoftRef<T> extends SoftReference<T> {
        private int hash;

        public SoftRef(int hash, T r, ReferenceQueue q) {
            super(r, q);
            this.hash = hash;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    static class WeakRef<T> extends WeakReference<T> {
        private int hash;

        public WeakRef(int hash, T r, ReferenceQueue q) {
            super(r, q);
            this.hash = hash;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    static class ReferenceEntrySet<K, V> extends EntrySet<K, V> {
        protected ReferenceEntrySet(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        public <T> T[] toArray(T[] arr) {
            ArrayList<Entry<K, V>> list = new ArrayList();
            Iterator<Entry<K, V>> iterator = iterator();
            while (iterator.hasNext()) {
                Entry<K, V> e = (Entry) iterator.next();
                list.add(new DefaultMapEntry(e.getKey(), e.getValue()));
            }
            return list.toArray(arr);
        }
    }

    static class ReferenceEntrySetIterator<K, V> extends ReferenceIteratorBase<K, V> implements Iterator<Entry<K, V>> {
        public ReferenceEntrySetIterator(AbstractReferenceMap<K, V> abstractReferenceMap) {
            super(abstractReferenceMap);
        }

        public ReferenceEntry<K, V> next() {
            return superNext();
        }
    }

    static class ReferenceKeySet<K, V> extends KeySet<K, V> {
        protected ReferenceKeySet(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        public <T> T[] toArray(T[] arr) {
            List<K> list = new ArrayList(this.parent.size());
            Iterator<K> it = iterator();
            while (it.hasNext()) {
                list.add(it.next());
            }
            return list.toArray(arr);
        }
    }

    static class ReferenceKeySetIterator<K, V> extends ReferenceIteratorBase<K, V> implements Iterator<K> {
        ReferenceKeySetIterator(AbstractReferenceMap<K, V> parent) {
            super(parent);
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    static class ReferenceMapIterator<K, V> extends ReferenceIteratorBase<K, V> implements MapIterator<K, V> {
        protected ReferenceMapIterator(AbstractReferenceMap<K, V> parent) {
            super(parent);
        }

        public K next() {
            return nextEntry().getKey();
        }

        public K getKey() {
            HashEntry<K, V> current = currentEntry();
            if (current != null) {
                return current.getKey();
            }
            throw new IllegalStateException("getKey() can only be called after next() and before remove()");
        }

        public V getValue() {
            HashEntry<K, V> current = currentEntry();
            if (current != null) {
                return current.getValue();
            }
            throw new IllegalStateException("getValue() can only be called after next() and before remove()");
        }

        public V setValue(V value) {
            HashEntry<K, V> current = currentEntry();
            if (current != null) {
                return current.setValue(value);
            }
            throw new IllegalStateException("setValue() can only be called after next() and before remove()");
        }
    }

    static class ReferenceValues<K, V> extends Values<K, V> {
        protected ReferenceValues(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        public <T> T[] toArray(T[] arr) {
            List<V> list = new ArrayList(this.parent.size());
            Iterator<V> it = iterator();
            while (it.hasNext()) {
                list.add(it.next());
            }
            return list.toArray(arr);
        }
    }

    static class ReferenceValuesIterator<K, V> extends ReferenceIteratorBase<K, V> implements Iterator<V> {
        ReferenceValuesIterator(AbstractReferenceMap<K, V> parent) {
            super(parent);
        }

        public V next() {
            return nextEntry().getValue();
        }
    }

    protected static class ReferenceEntry<K, V> extends HashEntry<K, V> {
        protected final AbstractReferenceMap<K, V> parent;
        protected Reference<K> refKey;
        protected Reference<V> refValue;

        public ReferenceEntry(AbstractReferenceMap<K, V> parent, ReferenceEntry<K, V> next, int hashCode, K key, V value) {
            super(next, hashCode, null, null);
            this.parent = parent;
            if (parent.keyType != 0) {
                this.refKey = toReference(parent.keyType, key, hashCode);
            } else {
                setKey(key);
            }
            if (parent.valueType != 0) {
                this.refValue = toReference(parent.valueType, value, hashCode);
            } else {
                setValue(value);
            }
        }

        public K getKey() {
            return this.parent.keyType > 0 ? this.refKey.get() : super.getKey();
        }

        public V getValue() {
            return this.parent.valueType > 0 ? this.refValue.get() : super.getValue();
        }

        public V setValue(V obj) {
            V old = getValue();
            if (this.parent.valueType > 0) {
                this.refValue.clear();
                this.refValue = toReference(this.parent.valueType, obj, this.hashCode);
            } else {
                super.setValue(obj);
            }
            return old;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            if (entryKey == null || entryValue == null) {
                return false;
            }
            if (this.parent.isEqualKey(entryKey, getKey()) && this.parent.isEqualValue(entryValue, getValue())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.parent.hashEntry(getKey(), getValue());
        }

        /* access modifiers changed from: protected */
        public <T> Reference<T> toReference(int type, T referent, int hash) {
            switch (type) {
                case 1:
                    return new SoftRef(hash, referent, this.parent.queue);
                case 2:
                    return new WeakRef(hash, referent, this.parent.queue);
                default:
                    throw new Error("Attempt to create hard reference in ReferenceMap!");
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean purge(Reference ref) {
            boolean r;
            if (this.parent.keyType <= 0 || this.refKey != ref) {
                r = false;
            } else {
                r = true;
            }
            if (r || (this.parent.valueType > 0 && this.refValue == ref)) {
                r = true;
            } else {
                r = false;
            }
            if (r) {
                if (this.parent.keyType > 0) {
                    this.refKey.clear();
                }
                if (this.parent.valueType > 0) {
                    this.refValue.clear();
                } else if (this.parent.purgeValues) {
                    setValue(null);
                }
            }
            return r;
        }

        /* access modifiers changed from: protected */
        public ReferenceEntry<K, V> next() {
            return (ReferenceEntry) this.next;
        }
    }

    protected AbstractReferenceMap() {
    }

    protected AbstractReferenceMap(int keyType, int valueType, int capacity, float loadFactor, boolean purgeValues) {
        super(capacity, loadFactor);
        verify("keyType", keyType);
        verify("valueType", valueType);
        this.keyType = keyType;
        this.valueType = valueType;
        this.purgeValues = purgeValues;
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.queue = new ReferenceQueue();
    }

    private static void verify(String name, int type) {
        if (type < 0 || type > 2) {
            throw new IllegalArgumentException(name + " must be HARD, SOFT, WEAK.");
        }
    }

    public int size() {
        purgeBeforeRead();
        return super.size();
    }

    public boolean isEmpty() {
        purgeBeforeRead();
        return super.isEmpty();
    }

    public boolean containsKey(Object key) {
        purgeBeforeRead();
        Entry entry = getEntry(key);
        if (entry == null || entry.getValue() == null) {
            return false;
        }
        return true;
    }

    public boolean containsValue(Object value) {
        purgeBeforeRead();
        if (value == null) {
            return false;
        }
        return super.containsValue(value);
    }

    public V get(Object key) {
        purgeBeforeRead();
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("null keys not allowed");
        } else if (value == null) {
            throw new NullPointerException("null values not allowed");
        } else {
            purgeBeforeWrite();
            return super.put(key, value);
        }
    }

    public V remove(Object key) {
        if (key == null) {
            return null;
        }
        purgeBeforeWrite();
        return super.remove(key);
    }

    public void clear() {
        super.clear();
        do {
        } while (this.queue.poll() != null);
    }

    public MapIterator<K, V> mapIterator() {
        return new ReferenceMapIterator(this);
    }

    public Set<Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new ReferenceEntrySet(this);
        }
        return this.entrySet;
    }

    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new ReferenceKeySet(this);
        }
        return this.keySet;
    }

    public Collection<V> values() {
        if (this.values == null) {
            this.values = new ReferenceValues(this);
        }
        return this.values;
    }

    /* access modifiers changed from: protected */
    public void purgeBeforeRead() {
        purge();
    }

    /* access modifiers changed from: protected */
    public void purgeBeforeWrite() {
        purge();
    }

    /* access modifiers changed from: protected */
    public void purge() {
        Reference ref = this.queue.poll();
        while (ref != null) {
            purge(ref);
            ref = this.queue.poll();
        }
    }

    /* access modifiers changed from: protected */
    public void purge(Reference ref) {
        int index = hashIndex(ref.hashCode(), this.data.length);
        HashEntry<K, V> previous = null;
        for (HashEntry<K, V> entry = this.data[index]; entry != null; entry = entry.next) {
            if (((ReferenceEntry) entry).purge(ref)) {
                if (previous == null) {
                    this.data[index] = entry.next;
                } else {
                    previous.next = entry.next;
                }
                this.size--;
                return;
            }
            previous = entry;
        }
    }

    /* access modifiers changed from: protected */
    public HashEntry<K, V> getEntry(Object key) {
        if (key == null) {
            return null;
        }
        return super.getEntry(key);
    }

    /* access modifiers changed from: protected */
    public int hashEntry(Object key, Object value) {
        int i = 0;
        int hashCode = key == null ? 0 : key.hashCode();
        if (value != null) {
            i = value.hashCode();
        }
        return i ^ hashCode;
    }

    /* access modifiers changed from: protected */
    public boolean isEqualKey(Object key1, Object key2) {
        return key1 == key2 || key1.equals(key2);
    }

    public HashEntry<K, V> createEntry(HashEntry<K, V> next, int hashCode, K key, V value) {
        return new ReferenceEntry(this, (ReferenceEntry) next, hashCode, key, value);
    }

    /* access modifiers changed from: protected */
    public Iterator<Entry<K, V>> createEntrySetIterator() {
        return new ReferenceEntrySetIterator(this);
    }

    /* access modifiers changed from: protected */
    public Iterator<K> createKeySetIterator() {
        return new ReferenceKeySetIterator(this);
    }

    /* access modifiers changed from: protected */
    public Iterator<V> createValuesIterator() {
        return new ReferenceValuesIterator(this);
    }

    /* access modifiers changed from: protected */
    public void doWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.keyType);
        out.writeInt(this.valueType);
        out.writeBoolean(this.purgeValues);
        out.writeFloat(this.loadFactor);
        out.writeInt(this.data.length);
        MapIterator it = mapIterator();
        while (it.hasNext()) {
            out.writeObject(it.next());
            out.writeObject(it.getValue());
        }
        out.writeObject(null);
    }

    /* access modifiers changed from: protected */
    public void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.keyType = in.readInt();
        this.valueType = in.readInt();
        this.purgeValues = in.readBoolean();
        this.loadFactor = in.readFloat();
        int capacity = in.readInt();
        init();
        this.data = new HashEntry[capacity];
        while (true) {
            K key = in.readObject();
            if (key == null) {
                this.threshold = calculateThreshold(this.data.length, this.loadFactor);
                return;
            }
            put(key, in.readObject());
        }
    }
}
