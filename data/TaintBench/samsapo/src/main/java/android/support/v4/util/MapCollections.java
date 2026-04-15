package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class MapCollections<K, V> {
    EntrySet mEntrySet;
    KeySet mKeySet;
    ValuesCollection mValues;

    final class ArrayIterator<T> implements Iterator<T> {
        boolean mCanRemove = false;
        int mIndex;
        final int mOffset;
        int mSize;
        final /* synthetic */ MapCollections this$0;

        ArrayIterator(MapCollections mapCollections, int i) {
            MapCollections mapCollections2 = mapCollections;
            int i2 = i;
            this.this$0 = mapCollections2;
            this.mOffset = i2;
            this.mSize = mapCollections2.colGetSize();
        }

        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }

        public T next() {
            T colGetEntry = this.this$0.colGetEntry(this.mIndex, this.mOffset);
            this.mIndex++;
            this.mCanRemove = true;
            return colGetEntry;
        }

        public void remove() {
            if (this.mCanRemove) {
                this.mIndex--;
                this.mSize--;
                this.mCanRemove = false;
                this.this$0.colRemoveAt(this.mIndex);
                return;
            }
            IllegalStateException illegalStateException = r4;
            IllegalStateException illegalStateException2 = new IllegalStateException();
            throw illegalStateException;
        }
    }

    final class EntrySet implements Set<Entry<K, V>> {
        final /* synthetic */ MapCollections this$0;

        EntrySet(MapCollections mapCollections) {
            this.this$0 = mapCollections;
        }

        public boolean add(Entry<K, V> entry) {
            Entry<K, V> entry2 = entry;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public boolean addAll(Collection<? extends Entry<K, V>> collection) {
            Collection<? extends Entry<K, V>> collection2 = collection;
            int colGetSize = this.this$0.colGetSize();
            for (Entry entry : collection2) {
                this.this$0.colPut(entry.getKey(), entry.getValue());
            }
            return colGetSize != this.this$0.colGetSize();
        }

        public void clear() {
            this.this$0.colClear();
        }

        public boolean contains(Object obj) {
            Object obj2 = obj;
            if (!(obj2 instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj2;
            int colIndexOfKey = this.this$0.colIndexOfKey(entry.getKey());
            if (colIndexOfKey < 0) {
                return false;
            }
            return ContainerHelpers.equal(this.this$0.colGetEntry(colIndexOfKey, 1), entry.getValue());
        }

        public boolean containsAll(Collection<?> collection) {
            for (Object contains : collection) {
                if (!contains(contains)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return this.this$0.colGetSize() == 0;
        }

        public Iterator<Entry<K, V>> iterator() {
            MapIterator mapIterator = r4;
            MapIterator mapIterator2 = new MapIterator(this.this$0);
            return mapIterator;
        }

        public boolean remove(Object obj) {
            Object obj2 = obj;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public boolean removeAll(Collection<?> collection) {
            Collection<?> collection2 = collection;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public boolean retainAll(Collection<?> collection) {
            Collection<?> collection2 = collection;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public int size() {
            return this.this$0.colGetSize();
        }

        public Object[] toArray() {
            UnsupportedOperationException unsupportedOperationException = r3;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public <T> T[] toArray(T[] tArr) {
            T[] tArr2 = tArr;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public boolean equals(Object obj) {
            return MapCollections.equalsSetHelper(this, obj);
        }

        public int hashCode() {
            int i = 0;
            for (int colGetSize = this.this$0.colGetSize() - 1; colGetSize >= 0; colGetSize--) {
                Object colGetEntry = this.this$0.colGetEntry(colGetSize, 0);
                Object colGetEntry2 = this.this$0.colGetEntry(colGetSize, 1);
                i += (colGetEntry == null ? 0 : colGetEntry.hashCode()) ^ (colGetEntry2 == null ? 0 : colGetEntry2.hashCode());
            }
            return i;
        }
    }

    final class KeySet implements Set<K> {
        final /* synthetic */ MapCollections this$0;

        KeySet(MapCollections mapCollections) {
            this.this$0 = mapCollections;
        }

        public boolean add(K k) {
            K k2 = k;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public boolean addAll(Collection<? extends K> collection) {
            Collection<? extends K> collection2 = collection;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public void clear() {
            this.this$0.colClear();
        }

        public boolean contains(Object obj) {
            return this.this$0.colIndexOfKey(obj) >= 0;
        }

        public boolean containsAll(Collection<?> collection) {
            return MapCollections.containsAllHelper(this.this$0.colGetMap(), collection);
        }

        public boolean isEmpty() {
            return this.this$0.colGetSize() == 0;
        }

        public Iterator<K> iterator() {
            ArrayIterator arrayIterator = r5;
            ArrayIterator arrayIterator2 = new ArrayIterator(this.this$0, 0);
            return arrayIterator;
        }

        public boolean remove(Object obj) {
            int colIndexOfKey = this.this$0.colIndexOfKey(obj);
            if (colIndexOfKey < 0) {
                return false;
            }
            this.this$0.colRemoveAt(colIndexOfKey);
            return true;
        }

        public boolean removeAll(Collection<?> collection) {
            return MapCollections.removeAllHelper(this.this$0.colGetMap(), collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return MapCollections.retainAllHelper(this.this$0.colGetMap(), collection);
        }

        public int size() {
            return this.this$0.colGetSize();
        }

        public Object[] toArray() {
            return this.this$0.toArrayHelper(0);
        }

        public <T> T[] toArray(T[] tArr) {
            return this.this$0.toArrayHelper(tArr, 0);
        }

        public boolean equals(Object obj) {
            return MapCollections.equalsSetHelper(this, obj);
        }

        public int hashCode() {
            int i = 0;
            for (int colGetSize = this.this$0.colGetSize() - 1; colGetSize >= 0; colGetSize--) {
                Object colGetEntry = this.this$0.colGetEntry(colGetSize, 0);
                i += colGetEntry == null ? 0 : colGetEntry.hashCode();
            }
            return i;
        }
    }

    final class MapIterator implements Iterator<Entry<K, V>>, Entry<K, V> {
        int mEnd;
        boolean mEntryValid = false;
        int mIndex;
        final /* synthetic */ MapCollections this$0;

        MapIterator(MapCollections mapCollections) {
            MapCollections mapCollections2 = mapCollections;
            this.this$0 = mapCollections2;
            this.mEnd = mapCollections2.colGetSize() - 1;
            this.mIndex = -1;
        }

        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }

        public Entry<K, V> next() {
            this.mIndex++;
            this.mEntryValid = true;
            return this;
        }

        public void remove() {
            if (this.mEntryValid) {
                this.mIndex--;
                this.mEnd--;
                this.mEntryValid = false;
                this.this$0.colRemoveAt(this.mIndex);
                return;
            }
            IllegalStateException illegalStateException = r4;
            IllegalStateException illegalStateException2 = new IllegalStateException();
            throw illegalStateException;
        }

        public K getKey() {
            if (this.mEntryValid) {
                return this.this$0.colGetEntry(this.mIndex, 0);
            }
            IllegalStateException illegalStateException = r4;
            IllegalStateException illegalStateException2 = new IllegalStateException("This container does not support retaining Map.Entry objects");
            throw illegalStateException;
        }

        public V getValue() {
            if (this.mEntryValid) {
                return this.this$0.colGetEntry(this.mIndex, 1);
            }
            IllegalStateException illegalStateException = r4;
            IllegalStateException illegalStateException2 = new IllegalStateException("This container does not support retaining Map.Entry objects");
            throw illegalStateException;
        }

        public V setValue(V v) {
            V v2 = v;
            if (this.mEntryValid) {
                return this.this$0.colSetValue(this.mIndex, v2);
            }
            IllegalStateException illegalStateException = r5;
            IllegalStateException illegalStateException2 = new IllegalStateException("This container does not support retaining Map.Entry objects");
            throw illegalStateException;
        }

        public final boolean equals(Object obj) {
            Object obj2 = obj;
            if (!this.mEntryValid) {
                IllegalStateException illegalStateException = r7;
                IllegalStateException illegalStateException2 = new IllegalStateException("This container does not support retaining Map.Entry objects");
                throw illegalStateException;
            } else if (!(obj2 instanceof Entry)) {
                return false;
            } else {
                Entry entry = (Entry) obj2;
                boolean z = ContainerHelpers.equal(entry.getKey(), this.this$0.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(entry.getValue(), this.this$0.colGetEntry(this.mIndex, 1));
                return z;
            }
        }

        public final int hashCode() {
            if (this.mEntryValid) {
                Object colGetEntry = this.this$0.colGetEntry(this.mIndex, 0);
                Object colGetEntry2 = this.this$0.colGetEntry(this.mIndex, 1);
                return (colGetEntry == null ? 0 : colGetEntry.hashCode()) ^ (colGetEntry2 == null ? 0 : colGetEntry2.hashCode());
            }
            IllegalStateException illegalStateException = r6;
            IllegalStateException illegalStateException2 = new IllegalStateException("This container does not support retaining Map.Entry objects");
            throw illegalStateException;
        }

        public final String toString() {
            StringBuilder stringBuilder = r3;
            StringBuilder stringBuilder2 = new StringBuilder();
            return stringBuilder.append(getKey()).append("=").append(getValue()).toString();
        }
    }

    final class ValuesCollection implements Collection<V> {
        final /* synthetic */ MapCollections this$0;

        ValuesCollection(MapCollections mapCollections) {
            this.this$0 = mapCollections;
        }

        public boolean add(V v) {
            V v2 = v;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public boolean addAll(Collection<? extends V> collection) {
            Collection<? extends V> collection2 = collection;
            UnsupportedOperationException unsupportedOperationException = r4;
            UnsupportedOperationException unsupportedOperationException2 = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }

        public void clear() {
            this.this$0.colClear();
        }

        public boolean contains(Object obj) {
            return this.this$0.colIndexOfValue(obj) >= 0;
        }

        public boolean containsAll(Collection<?> collection) {
            for (Object contains : collection) {
                if (!contains(contains)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return this.this$0.colGetSize() == 0;
        }

        public Iterator<V> iterator() {
            ArrayIterator arrayIterator = r5;
            ArrayIterator arrayIterator2 = new ArrayIterator(this.this$0, 1);
            return arrayIterator;
        }

        public boolean remove(Object obj) {
            int colIndexOfValue = this.this$0.colIndexOfValue(obj);
            if (colIndexOfValue < 0) {
                return false;
            }
            this.this$0.colRemoveAt(colIndexOfValue);
            return true;
        }

        public boolean removeAll(Collection<?> collection) {
            Collection<?> collection2 = collection;
            int colGetSize = this.this$0.colGetSize();
            boolean z = false;
            int i = 0;
            while (i < colGetSize) {
                if (collection2.contains(this.this$0.colGetEntry(i, 1))) {
                    this.this$0.colRemoveAt(i);
                    i--;
                    colGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        public boolean retainAll(Collection<?> collection) {
            Collection<?> collection2 = collection;
            int colGetSize = this.this$0.colGetSize();
            boolean z = false;
            int i = 0;
            while (i < colGetSize) {
                if (!collection2.contains(this.this$0.colGetEntry(i, 1))) {
                    this.this$0.colRemoveAt(i);
                    i--;
                    colGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        public int size() {
            return this.this$0.colGetSize();
        }

        public Object[] toArray() {
            return this.this$0.toArrayHelper(1);
        }

        public <T> T[] toArray(T[] tArr) {
            return this.this$0.toArrayHelper(tArr, 1);
        }
    }

    public abstract void colClear();

    public abstract Object colGetEntry(int i, int i2);

    public abstract Map<K, V> colGetMap();

    public abstract int colGetSize();

    public abstract int colIndexOfKey(Object obj);

    public abstract int colIndexOfValue(Object obj);

    public abstract void colPut(K k, V v);

    public abstract void colRemoveAt(int i);

    public abstract V colSetValue(int i, V v);

    MapCollections() {
    }

    public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> collection) {
        Map<K, V> map2 = map;
        for (Object containsKey : collection) {
            if (!map2.containsKey(containsKey)) {
                return false;
            }
        }
        return true;
    }

    public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> collection) {
        Map<K, V> map2 = map;
        Collection<?> collection2 = collection;
        int size = map2.size();
        for (Object remove : collection2) {
            Object remove2 = map2.remove(remove);
        }
        return size != map2.size();
    }

    public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
        Map<K, V> map2 = map;
        Collection<?> collection2 = collection;
        int size = map2.size();
        Iterator it = map2.keySet().iterator();
        while (it.hasNext()) {
            if (!collection2.contains(it.next())) {
                it.remove();
            }
        }
        return size != map2.size();
    }

    public Object[] toArrayHelper(int i) {
        int i2 = i;
        int colGetSize = colGetSize();
        Object[] objArr = new Object[colGetSize];
        for (int i3 = 0; i3 < colGetSize; i3++) {
            objArr[i3] = colGetEntry(i3, i2);
        }
        return objArr;
    }

    public <T> T[] toArrayHelper(T[] tArr, int i) {
        Object obj = tArr;
        int i2 = i;
        int colGetSize = colGetSize();
        if (obj.length < colGetSize) {
            obj = (Object[]) Array.newInstance(obj.getClass().getComponentType(), colGetSize);
        }
        for (int i3 = 0; i3 < colGetSize; i3++) {
            obj[i3] = colGetEntry(i3, i2);
        }
        if (obj.length > colGetSize) {
            obj[colGetSize] = null;
        }
        return obj;
    }

    public static <T> boolean equalsSetHelper(Set<T> set, Object obj) {
        Set<T> set2 = set;
        Set<T> set3 = obj;
        if (set2 == set3) {
            return true;
        }
        if (!(set3 instanceof Set)) {
            return false;
        }
        Set set4 = set3;
        try {
            boolean z = set2.size() == set4.size() && set2.containsAll(set4);
            return z;
        } catch (NullPointerException e) {
            NullPointerException nullPointerException = e;
            return false;
        } catch (ClassCastException e2) {
            ClassCastException classCastException = e2;
            return false;
        }
    }

    public Set<Entry<K, V>> getEntrySet() {
        if (this.mEntrySet == null) {
            EntrySet entrySet = r5;
            EntrySet entrySet2 = new EntrySet(this);
            this.mEntrySet = entrySet;
        }
        return this.mEntrySet;
    }

    public Set<K> getKeySet() {
        if (this.mKeySet == null) {
            KeySet keySet = r5;
            KeySet keySet2 = new KeySet(this);
            this.mKeySet = keySet;
        }
        return this.mKeySet;
    }

    public Collection<V> getValues() {
        if (this.mValues == null) {
            ValuesCollection valuesCollection = r5;
            ValuesCollection valuesCollection2 = new ValuesCollection(this);
            this.mValues = valuesCollection;
        }
        return this.mValues;
    }
}
