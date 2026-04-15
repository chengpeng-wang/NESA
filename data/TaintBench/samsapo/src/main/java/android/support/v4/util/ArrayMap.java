package android.support.v4.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
    MapCollections<K, V> mCollections;

    public ArrayMap() {
    }

    public ArrayMap(int i) {
        super(i);
    }

    public ArrayMap(SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }

    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            MapCollections mapCollections = r5;
            MapCollections anonymousClass1 = new MapCollections<K, V>(this) {
                final /* synthetic */ ArrayMap this$0;

                {
                    this.this$0 = r5;
                }

                /* access modifiers changed from: protected */
                public int colGetSize() {
                    return this.this$0.mSize;
                }

                /* access modifiers changed from: protected */
                public Object colGetEntry(int i, int i2) {
                    return this.this$0.mArray[(i << 1) + i2];
                }

                /* access modifiers changed from: protected */
                public int colIndexOfKey(Object obj) {
                    Object obj2 = obj;
                    return obj2 == null ? this.this$0.indexOfNull() : this.this$0.indexOf(obj2, obj2.hashCode());
                }

                /* access modifiers changed from: protected */
                public int colIndexOfValue(Object obj) {
                    return this.this$0.indexOfValue(obj);
                }

                /* access modifiers changed from: protected */
                public Map<K, V> colGetMap() {
                    return this.this$0;
                }

                /* access modifiers changed from: protected */
                public void colPut(K k, V v) {
                    Object put = this.this$0.put(k, v);
                }

                /* access modifiers changed from: protected */
                public V colSetValue(int i, V v) {
                    return this.this$0.setValueAt(i, v);
                }

                /* access modifiers changed from: protected */
                public void colRemoveAt(int i) {
                    Object removeAt = this.this$0.removeAt(i);
                }

                /* access modifiers changed from: protected */
                public void colClear() {
                    this.this$0.clear();
                }
            };
            this.mCollections = mapCollections;
        }
        return this.mCollections;
    }

    public boolean containsAll(Collection<?> collection) {
        return MapCollections.containsAllHelper(this, collection);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        Map<? extends K, ? extends V> map2 = map;
        ensureCapacity(this.mSize + map2.size());
        for (Entry entry : map2.entrySet()) {
            Object put = put(entry.getKey(), entry.getValue());
        }
    }

    public boolean removeAll(Collection<?> collection) {
        return MapCollections.removeAllHelper(this, collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return MapCollections.retainAllHelper(this, collection);
    }

    public Set<Entry<K, V>> entrySet() {
        return getCollection().getEntrySet();
    }

    public Set<K> keySet() {
        return getCollection().getKeySet();
    }

    public Collection<V> values() {
        return getCollection().getValues();
    }
}
