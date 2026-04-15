package org.springframework.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
    private static final int MAXIMUM_CONCURRENCY_LEVEL = 65536;
    private static final int MAXIMUM_SEGMENT_SIZE = 1073741824;
    private Set<java.util.Map.Entry<K, V>> entrySet;
    private final float loadFactor;
    /* access modifiers changed from: private|final */
    public final ReferenceType referenceType;
    /* access modifiers changed from: private|final */
    public final Segment[] segments;
    private final int shift;

    private abstract class Task<T> {
        private final EnumSet<TaskOption> options;

        public Task(TaskOption... options) {
            this.options = options.length == 0 ? EnumSet.noneOf(TaskOption.class) : EnumSet.of(options[0], options);
        }

        public boolean hasOption(TaskOption option) {
            return this.options.contains(option);
        }

        /* access modifiers changed from: protected */
        public T execute(Reference<K, V> reference, Entry<K, V> entry, Entries entries) {
            return execute(reference, entry);
        }

        /* access modifiers changed from: protected */
        public T execute(Reference<K, V> reference, Entry<K, V> entry) {
            return null;
        }
    }

    private abstract class Entries {
        public abstract void add(V v);

        private Entries() {
        }

        /* synthetic */ Entries(ConcurrentReferenceHashMap x0, AnonymousClass1 x1) {
            this();
        }
    }

    protected static final class Entry<K, V> implements java.util.Map.Entry<K, V> {
        private final K key;
        /* access modifiers changed from: private|volatile */
        public volatile V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V value) {
            V previous = this.value;
            this.value = value;
            return previous;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public final boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry otherEntry = (java.util.Map.Entry) other;
            if (ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey()) && ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue())) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            return ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value);
        }
    }

    private class EntryIterator implements Iterator<java.util.Map.Entry<K, V>> {
        private Entry<K, V> last;
        private Entry<K, V> next;
        private Reference<K, V> reference;
        private int referenceIndex;
        private Reference<K, V>[] references;
        private int segmentIndex;

        public EntryIterator() {
            moveToNextSegment();
        }

        public boolean hasNext() {
            getNextIfNecessary();
            return this.next != null;
        }

        public Entry<K, V> next() {
            getNextIfNecessary();
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.last = this.next;
            this.next = null;
            return this.last;
        }

        private void getNextIfNecessary() {
            while (this.next == null) {
                moveToNextReference();
                if (this.reference != null) {
                    this.next = this.reference.get();
                } else {
                    return;
                }
            }
        }

        private void moveToNextReference() {
            if (this.reference != null) {
                this.reference = this.reference.getNext();
            }
            while (this.reference == null && this.references != null) {
                if (this.referenceIndex >= this.references.length) {
                    moveToNextSegment();
                    this.referenceIndex = 0;
                } else {
                    this.reference = this.references[this.referenceIndex];
                    this.referenceIndex++;
                }
            }
        }

        private void moveToNextSegment() {
            this.reference = null;
            this.references = null;
            if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
                this.references = ConcurrentReferenceHashMap.this.segments[this.segmentIndex].references;
                this.segmentIndex++;
            }
        }

        public void remove() {
            Assert.state(this.last != null);
            ConcurrentReferenceHashMap.this.remove(this.last.getKey());
        }
    }

    private class EntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {
        private EntrySet() {
        }

        /* synthetic */ EntrySet(ConcurrentReferenceHashMap x0, AnonymousClass1 x1) {
            this();
        }

        public Iterator<java.util.Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object o) {
            if (o != null && (o instanceof java.util.Map.Entry)) {
                java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry) o;
                Reference<K, V> reference = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), Restructure.NEVER);
                Entry<K, V> other = reference != null ? reference.get() : null;
                if (other != null) {
                    return ObjectUtils.nullSafeEquals(entry.getValue(), other.getValue());
                }
            }
            return false;
        }

        public boolean remove(Object o) {
            if (!(o instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry) o;
            return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
        }

        public int size() {
            return ConcurrentReferenceHashMap.this.size();
        }

        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }
    }

    protected interface Reference<K, V> {
        Entry<K, V> get();

        int getHash();

        Reference<K, V> getNext();

        void release();
    }

    protected class ReferenceManager {
        private final ReferenceQueue<Entry<K, V>> queue = new ReferenceQueue();

        protected ReferenceManager() {
        }

        public Reference<K, V> createReference(Entry<K, V> entry, int hash, Reference<K, V> next) {
            if (ConcurrentReferenceHashMap.this.referenceType == ReferenceType.WEAK) {
                return new WeakEntryReference(entry, hash, next, this.queue);
            }
            return new SoftEntryReference(entry, hash, next, this.queue);
        }

        public Reference<K, V> pollForPurge() {
            return (Reference) this.queue.poll();
        }
    }

    public enum ReferenceType {
        SOFT,
        WEAK
    }

    protected enum Restructure {
        WHEN_NECESSARY,
        NEVER
    }

    protected final class Segment extends ReentrantLock {
        /* access modifiers changed from: private|volatile */
        public volatile int count = 0;
        private final int initialSize;
        /* access modifiers changed from: private|final */
        public final ReferenceManager referenceManager;
        /* access modifiers changed from: private|volatile */
        public volatile Reference<K, V>[] references;
        private int resizeThreshold;

        public Segment(int initialCapacity) {
            this.referenceManager = ConcurrentReferenceHashMap.this.createReferenceManager();
            this.initialSize = 1 << ConcurrentReferenceHashMap.calculateShift(initialCapacity, ConcurrentReferenceHashMap.MAXIMUM_SEGMENT_SIZE);
            setReferences(createReferenceArray(this.initialSize));
        }

        public Reference<K, V> getReference(Object key, int hash, Restructure restructure) {
            if (restructure == Restructure.WHEN_NECESSARY) {
                restructureIfNecessary(false);
            }
            if (this.count == 0) {
                return null;
            }
            Reference<K, V>[] references = this.references;
            return findInChain(references[getIndex(hash, references)], key, hash);
        }

        /* JADX WARNING: Failed to extract finally block: empty outs */
        /* JADX WARNING: Missing block: B:24:?, code skipped:
            return r1;
     */
        public <T> T doTask(int r10, java.lang.Object r11, org.springframework.util.ConcurrentReferenceHashMap.Task<T> r12) {
            /*
            r9 = this;
            r6 = 0;
            r1 = org.springframework.util.ConcurrentReferenceHashMap.TaskOption.RESIZE;
            r8 = r12.hasOption(r1);
            r1 = org.springframework.util.ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_BEFORE;
            r1 = r12.hasOption(r1);
            if (r1 == 0) goto L_0x0012;
        L_0x000f:
            r9.restructureIfNecessary(r8);
        L_0x0012:
            r1 = org.springframework.util.ConcurrentReferenceHashMap.TaskOption.SKIP_IF_EMPTY;
            r1 = r12.hasOption(r1);
            if (r1 == 0) goto L_0x0023;
        L_0x001a:
            r1 = r9.count;
            if (r1 != 0) goto L_0x0023;
        L_0x001e:
            r1 = r12.execute(r6, r6, r6);
        L_0x0022:
            return r1;
        L_0x0023:
            r9.lock();
            r1 = r9.references;	 Catch:{ all -> 0x0055 }
            r5 = r9.getIndex(r10, r1);	 Catch:{ all -> 0x0055 }
            r1 = r9.references;	 Catch:{ all -> 0x0055 }
            r4 = r1[r5];	 Catch:{ all -> 0x0055 }
            r7 = r9.findInChain(r4, r11, r10);	 Catch:{ all -> 0x0055 }
            if (r7 == 0) goto L_0x003a;
        L_0x0036:
            r6 = r7.get();	 Catch:{ all -> 0x0055 }
        L_0x003a:
            r0 = new org.springframework.util.ConcurrentReferenceHashMap$Segment$1;	 Catch:{ all -> 0x0055 }
            r1 = r9;
            r2 = r11;
            r3 = r10;
            r0.m4069init(r2, r3, r4, r5);	 Catch:{ all -> 0x0055 }
            r1 = r12.execute(r7, r6, r0);	 Catch:{ all -> 0x0055 }
            r9.unlock();
            r2 = org.springframework.util.ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER;
            r2 = r12.hasOption(r2);
            if (r2 == 0) goto L_0x0022;
        L_0x0051:
            r9.restructureIfNecessary(r8);
            goto L_0x0022;
        L_0x0055:
            r1 = move-exception;
            r9.unlock();
            r2 = org.springframework.util.ConcurrentReferenceHashMap.TaskOption.RESTRUCTURE_AFTER;
            r2 = r12.hasOption(r2);
            if (r2 == 0) goto L_0x0064;
        L_0x0061:
            r9.restructureIfNecessary(r8);
        L_0x0064:
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.springframework.util.ConcurrentReferenceHashMap$Segment.doTask(int, java.lang.Object, org.springframework.util.ConcurrentReferenceHashMap$Task):java.lang.Object");
        }

        public void clear() {
            if (this.count != 0) {
                lock();
                try {
                    setReferences(createReferenceArray(this.initialSize));
                    this.count = 0;
                } finally {
                    unlock();
                }
            }
        }

        /* access modifiers changed from: protected|final */
        public final void restructureIfNecessary(boolean allowResize) {
            boolean needsResize;
            if (this.count <= 0 || this.count < this.resizeThreshold) {
                needsResize = false;
            } else {
                needsResize = true;
            }
            Reference<K, V> reference = this.referenceManager.pollForPurge();
            if (reference != null || (needsResize && allowResize)) {
                lock();
                try {
                    int countAfterRestructure = this.count;
                    Set<Reference<K, V>> toPurge = Collections.emptySet();
                    if (reference != null) {
                        toPurge = new HashSet();
                        while (reference != null) {
                            toPurge.add(reference);
                            reference = this.referenceManager.pollForPurge();
                        }
                    }
                    countAfterRestructure -= toPurge.size();
                    if (countAfterRestructure <= 0 || countAfterRestructure < this.resizeThreshold) {
                        needsResize = false;
                    } else {
                        needsResize = true;
                    }
                    boolean resizing = false;
                    int restructureSize = this.references.length;
                    if (allowResize && needsResize && restructureSize < ConcurrentReferenceHashMap.MAXIMUM_SEGMENT_SIZE) {
                        restructureSize <<= 1;
                        resizing = true;
                    }
                    Reference<K, V>[] restructured = resizing ? createReferenceArray(restructureSize) : this.references;
                    for (int i = 0; i < this.references.length; i++) {
                        reference = this.references[i];
                        if (!resizing) {
                            restructured[i] = null;
                        }
                        while (reference != null) {
                            if (!(toPurge.contains(reference) || reference.get() == null)) {
                                int index = getIndex(reference.getHash(), restructured);
                                restructured[index] = this.referenceManager.createReference(reference.get(), reference.getHash(), restructured[index]);
                            }
                            reference = reference.getNext();
                        }
                    }
                    if (resizing) {
                        setReferences(restructured);
                    }
                    this.count = Math.max(countAfterRestructure, 0);
                } finally {
                    unlock();
                }
            }
        }

        private Reference<K, V> findInChain(Reference<K, V> reference, Object key, int hash) {
            while (reference != null) {
                if (reference.getHash() == hash) {
                    Entry<K, V> entry = reference.get();
                    if (entry != null) {
                        K entryKey = entry.getKey();
                        if (entryKey == key || entryKey.equals(key)) {
                            return reference;
                        }
                    } else {
                        continue;
                    }
                }
                reference = reference.getNext();
            }
            return null;
        }

        private Reference<K, V>[] createReferenceArray(int size) {
            return (Reference[]) Array.newInstance(Reference.class, size);
        }

        private int getIndex(int hash, Reference<K, V>[] references) {
            return (references.length - 1) & hash;
        }

        private void setReferences(Reference<K, V>[] references) {
            this.references = references;
            this.resizeThreshold = (int) (((float) references.length) * ConcurrentReferenceHashMap.this.getLoadFactor());
        }

        public final int getSize() {
            return this.references.length;
        }

        public final int getCount() {
            return this.count;
        }
    }

    private static final class SoftEntryReference<K, V> extends SoftReference<Entry<K, V>> implements Reference<K, V> {
        private final int hash;
        private final Reference<K, V> nextReference;

        public /* bridge */ /* synthetic */ Entry get() {
            return (Entry) super.get();
        }

        public SoftEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        public int getHash() {
            return this.hash;
        }

        public Reference<K, V> getNext() {
            return this.nextReference;
        }

        public void release() {
            enqueue();
            clear();
        }
    }

    private enum TaskOption {
        RESTRUCTURE_BEFORE,
        RESTRUCTURE_AFTER,
        SKIP_IF_EMPTY,
        RESIZE
    }

    private static final class WeakEntryReference<K, V> extends WeakReference<Entry<K, V>> implements Reference<K, V> {
        private final int hash;
        private final Reference<K, V> nextReference;

        public /* bridge */ /* synthetic */ Entry get() {
            return (Entry) super.get();
        }

        public WeakEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        public int getHash() {
            return this.hash;
        }

        public Reference<K, V> getNext() {
            return this.nextReference;
        }

        public void release() {
            enqueue();
            clear();
        }
    }

    public ConcurrentReferenceHashMap() {
        this(16, DEFAULT_LOAD_FACTOR, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, 16, referenceType);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
        boolean z;
        boolean z2 = false;
        if (initialCapacity >= 0) {
            z = true;
        } else {
            z = false;
        }
        Assert.isTrue(z, "Initial capacity must not be negative");
        if (loadFactor > 0.0f) {
            z = true;
        } else {
            z = false;
        }
        Assert.isTrue(z, "Load factor must be positive");
        if (concurrencyLevel > 0) {
            z2 = true;
        }
        Assert.isTrue(z2, "Concurrency level must be positive");
        Assert.notNull(referenceType, "Reference type must not be null");
        this.loadFactor = loadFactor;
        this.shift = calculateShift(concurrencyLevel, 65536);
        int size = 1 << this.shift;
        this.referenceType = referenceType;
        int roundedUpSegmentCapacity = (int) ((((long) (initialCapacity + size)) - 1) / ((long) size));
        this.segments = (Segment[]) Array.newInstance(Segment.class, size);
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i] = new Segment(roundedUpSegmentCapacity);
        }
    }

    /* access modifiers changed from: protected|final */
    public final float getLoadFactor() {
        return this.loadFactor;
    }

    /* access modifiers changed from: protected|final */
    public final int getSegmentsSize() {
        return this.segments.length;
    }

    /* access modifiers changed from: protected|final */
    public final Segment getSegment(int index) {
        return this.segments[index];
    }

    /* access modifiers changed from: protected */
    public ReferenceManager createReferenceManager() {
        return new ReferenceManager();
    }

    /* access modifiers changed from: protected */
    public int getHash(Object o) {
        int hash = o == null ? 0 : o.hashCode();
        hash += (hash << 15) ^ -12931;
        hash ^= hash >>> 10;
        hash += hash << 3;
        hash ^= hash >>> 6;
        hash += (hash << 2) + (hash << 14);
        return hash ^ (hash >>> 16);
    }

    public V get(Object key) {
        Entry<K, V> entry;
        Reference<K, V> reference = getReference(key, Restructure.WHEN_NECESSARY);
        if (reference != null) {
            entry = reference.get();
        } else {
            entry = null;
        }
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }

    public boolean containsKey(Object key) {
        Reference<K, V> reference = getReference(key, Restructure.WHEN_NECESSARY);
        Entry<K, V> entry = reference != null ? reference.get() : null;
        return entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key);
    }

    /* access modifiers changed from: protected|final */
    public final Reference<K, V> getReference(Object key, Restructure restructure) {
        int hash = getHash(key);
        return getSegmentForHash(hash).getReference(key, hash, restructure);
    }

    public V put(K key, V value) {
        return put(key, value, true);
    }

    public V putIfAbsent(K key, V value) {
        return put(key, value, false);
    }

    private V put(K key, final V value, final boolean overwriteExisting) {
        return doTask(key, new Task<V>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE}) {
            /* access modifiers changed from: protected */
            public V execute(Reference<K, V> reference, Entry<K, V> entry, Entries entries) {
                if (entry != null) {
                    V previousValue = entry.getValue();
                    if (!overwriteExisting) {
                        return previousValue;
                    }
                    entry.setValue(value);
                    return previousValue;
                }
                entries.add(value);
                return null;
            }
        });
    }

    public V remove(Object key) {
        return doTask(key, new Task<V>(TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY) {
            /* access modifiers changed from: protected */
            public V execute(Reference<K, V> reference, Entry<K, V> entry) {
                if (entry == null) {
                    return null;
                }
                reference.release();
                return entry.value;
            }
        });
    }

    public boolean remove(Object key, final Object value) {
        return ((Boolean) doTask(key, new Task<Boolean>(new TaskOption[]{TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY}) {
            /* access modifiers changed from: protected */
            public Boolean execute(Reference<K, V> reference, Entry<K, V> entry) {
                if (entry == null || !ObjectUtils.nullSafeEquals(entry.getValue(), value)) {
                    return Boolean.valueOf(false);
                }
                reference.release();
                return Boolean.valueOf(true);
            }
        })).booleanValue();
    }

    public boolean replace(K key, final V oldValue, final V newValue) {
        return ((Boolean) doTask(key, new Task<Boolean>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY}) {
            /* access modifiers changed from: protected */
            public Boolean execute(Reference<K, V> reference, Entry<K, V> entry) {
                if (entry == null || !ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)) {
                    return Boolean.valueOf(false);
                }
                entry.setValue(newValue);
                return Boolean.valueOf(true);
            }
        })).booleanValue();
    }

    public V replace(K key, final V value) {
        return doTask(key, new Task<V>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY}) {
            /* access modifiers changed from: protected */
            public V execute(Reference<K, V> reference, Entry<K, V> entry) {
                if (entry == null) {
                    return null;
                }
                V previousValue = entry.getValue();
                entry.setValue(value);
                return previousValue;
            }
        });
    }

    public void clear() {
        for (Segment segment : this.segments) {
            segment.clear();
        }
    }

    public void purgeUnreferencedEntries() {
        for (Segment segment : this.segments) {
            segment.restructureIfNecessary(false);
        }
    }

    public int size() {
        int size = 0;
        for (Segment segment : this.segments) {
            size += segment.getCount();
        }
        return size;
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet(this, null);
        }
        return this.entrySet;
    }

    private <T> T doTask(Object key, Task<T> task) {
        int hash = getHash(key);
        return getSegmentForHash(hash).doTask(hash, key, task);
    }

    private Segment getSegmentForHash(int hash) {
        return this.segments[(hash >>> (32 - this.shift)) & (this.segments.length - 1)];
    }

    protected static int calculateShift(int minimumValue, int maximumValue) {
        int shift = 0;
        int value = 1;
        while (value < minimumValue && value < maximumValue) {
            value <<= 1;
            shift++;
        }
        return shift;
    }
}
