package com.esotericsoftware.jsonbeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import org.objectweb.asm.signature.SignatureVisitor;

public class ObjectMap<K, V> {
    private static final int PRIME1 = -1105259343;
    private static final int PRIME2 = -1262997959;
    private static final int PRIME3 = -825114047;
    static final Random random = new Random();
    int capacity;
    private Entries entries;
    private int hashShift;
    K[] keyTable;
    private Keys keys;
    private float loadFactor;
    private int mask;
    private int pushIterations;
    public int size;
    private int stashCapacity;
    int stashSize;
    private int threshold;
    V[] valueTable;
    private Values values;

    public static class Entry<K, V> {
        public K key;
        public V value;

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    private static class MapIterator<K, V> {
        int currentIndex;
        public boolean hasNext;
        final ObjectMap<K, V> map;
        int nextIndex;

        public MapIterator(ObjectMap<K, V> objectMap) {
            this.map = objectMap;
            reset();
        }

        public void reset() {
            this.currentIndex = -1;
            this.nextIndex = -1;
            advance();
        }

        /* access modifiers changed from: 0000 */
        public void advance() {
            this.hasNext = false;
            Object[] objArr = this.map.keyTable;
            int i = this.map.capacity + this.map.stashSize;
            do {
                int i2 = this.nextIndex + 1;
                this.nextIndex = i2;
                if (i2 >= i) {
                    return;
                }
            } while (objArr[this.nextIndex] == null);
            this.hasNext = true;
        }

        public void remove() {
            if (this.currentIndex < 0) {
                throw new IllegalStateException("next must be called before remove.");
            }
            if (this.currentIndex >= this.map.capacity) {
                this.map.removeStashIndex(this.currentIndex);
            } else {
                this.map.keyTable[this.currentIndex] = null;
                this.map.valueTable[this.currentIndex] = null;
            }
            this.currentIndex = -1;
            ObjectMap objectMap = this.map;
            objectMap.size--;
        }
    }

    public static class Entries<K, V> extends MapIterator<K, V> implements Iterable<Entry<K, V>>, Iterator<Entry<K, V>> {
        Entry<K, V> entry = new Entry();

        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Entries(ObjectMap<K, V> objectMap) {
            super(objectMap);
        }

        public Entry<K, V> next() {
            if (this.hasNext) {
                Object[] objArr = this.map.keyTable;
                this.entry.key = objArr[this.nextIndex];
                this.entry.value = this.map.valueTable[this.nextIndex];
                this.currentIndex = this.nextIndex;
                advance();
                return this.entry;
            }
            throw new NoSuchElementException();
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public Iterator<Entry<K, V>> iterator() {
            return this;
        }
    }

    public static class Keys<K> extends MapIterator<K, Object> implements Iterable<K>, Iterator<K> {
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Keys(ObjectMap<K, ?> objectMap) {
            super(objectMap);
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public K next() {
            K k = this.map.keyTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            advance();
            return k;
        }

        public Iterator<K> iterator() {
            return this;
        }

        public ArrayList<K> toArray() {
            ArrayList arrayList = new ArrayList(this.map.size);
            while (this.hasNext) {
                arrayList.add(next());
            }
            return arrayList;
        }
    }

    public static class Values<V> extends MapIterator<Object, V> implements Iterable<V>, Iterator<V> {
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Values(ObjectMap<?, V> objectMap) {
            super(objectMap);
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public V next() {
            V v = this.map.valueTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            advance();
            return v;
        }

        public Iterator<V> iterator() {
            return this;
        }

        public ArrayList<V> toArray() {
            ArrayList arrayList = new ArrayList(this.map.size);
            while (this.hasNext) {
                arrayList.add(next());
            }
            return arrayList;
        }

        public void toArray(ArrayList<V> arrayList) {
            while (this.hasNext) {
                arrayList.add(next());
            }
        }
    }

    public ObjectMap() {
        this(32, 0.8f);
    }

    public ObjectMap(int i) {
        this(i, 0.8f);
    }

    public ObjectMap(int i, float f) {
        if (i < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0: " + i);
        } else if (this.capacity > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large: " + i);
        } else {
            this.capacity = nextPowerOfTwo(i);
            if (f <= 0.0f) {
                throw new IllegalArgumentException("loadFactor must be > 0: " + f);
            }
            this.loadFactor = f;
            this.threshold = (int) (((float) this.capacity) * f);
            this.mask = this.capacity - 1;
            this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
            this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log((double) this.capacity))) * 2);
            this.pushIterations = Math.max(Math.min(this.capacity, 8), ((int) Math.sqrt((double) this.capacity)) / 8);
            this.keyTable = new Object[(this.capacity + this.stashCapacity)];
            this.valueTable = new Object[this.keyTable.length];
        }
    }

    public V put(K k, V v) {
        if (k != null) {
            return put_internal(k, v);
        }
        throw new IllegalArgumentException("key cannot be null.");
    }

    private V put_internal(K k, V v) {
        Object[] objArr = this.keyTable;
        int hashCode = k.hashCode();
        int i = hashCode & this.mask;
        Object obj = objArr[i];
        V v2;
        if (k.equals(obj)) {
            v2 = this.valueTable[i];
            this.valueTable[i] = v;
            return v2;
        }
        int hash2 = hash2(hashCode);
        Object obj2 = objArr[hash2];
        if (k.equals(obj2)) {
            v2 = this.valueTable[hash2];
            this.valueTable[hash2] = v;
            return v2;
        }
        int hash3 = hash3(hashCode);
        Object obj3 = objArr[hash3];
        if (k.equals(obj3)) {
            v2 = this.valueTable[hash3];
            this.valueTable[hash3] = v;
            return v2;
        }
        hashCode = this.capacity;
        int i2 = hashCode + this.stashSize;
        for (int i3 = hashCode; i3 < i2; i3++) {
            if (k.equals(objArr[i3])) {
                v2 = this.valueTable[i3];
                this.valueTable[i3] = v;
                return v2;
            }
        }
        if (obj == null) {
            objArr[i] = k;
            this.valueTable[i] = v;
            hashCode = this.size;
            this.size = hashCode + 1;
            if (hashCode >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else if (obj2 == null) {
            objArr[hash2] = k;
            this.valueTable[hash2] = v;
            hashCode = this.size;
            this.size = hashCode + 1;
            if (hashCode >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else if (obj3 == null) {
            objArr[hash3] = k;
            this.valueTable[hash3] = v;
            hashCode = this.size;
            this.size = hashCode + 1;
            if (hashCode >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else {
            push(k, v, i, obj, hash2, obj2, hash3, obj3);
            return null;
        }
    }

    public void putAll(ObjectMap<K, V> objectMap) {
        Iterator it = objectMap.entries().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            put(entry.key, entry.value);
        }
    }

    private void putResize(K k, V v) {
        int hashCode = k.hashCode();
        int i = hashCode & this.mask;
        Object obj = this.keyTable[i];
        if (obj == null) {
            this.keyTable[i] = k;
            this.valueTable[i] = v;
            hashCode = this.size;
            this.size = hashCode + 1;
            if (hashCode >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int hash2 = hash2(hashCode);
        Object obj2 = this.keyTable[hash2];
        if (obj2 == null) {
            this.keyTable[hash2] = k;
            this.valueTable[hash2] = v;
            hashCode = this.size;
            this.size = hashCode + 1;
            if (hashCode >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int hash3 = hash3(hashCode);
        Object obj3 = this.keyTable[hash3];
        if (obj3 == null) {
            this.keyTable[hash3] = k;
            this.valueTable[hash3] = v;
            hashCode = this.size;
            this.size = hashCode + 1;
            if (hashCode >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        push(k, v, i, obj, hash2, obj2, hash3, obj3);
    }

    private void push(K k, V v, int i, K k2, int i2, K k3, int i3, K k4) {
        Object[] objArr = this.keyTable;
        Object[] objArr2 = this.valueTable;
        int i4 = this.mask;
        int i5 = 0;
        int i6 = this.pushIterations;
        do {
            V v2;
            switch (random.nextInt(3)) {
                case 0:
                    v2 = objArr2[i];
                    objArr[i] = k;
                    objArr2[i] = v;
                    v = v2;
                    k = k2;
                    break;
                case 1:
                    v2 = objArr2[i2];
                    objArr[i2] = k;
                    objArr2[i2] = v;
                    v = v2;
                    k = k3;
                    break;
                default:
                    v2 = objArr2[i3];
                    objArr[i3] = k;
                    objArr2[i3] = v;
                    v = v2;
                    k = k4;
                    break;
            }
            int hashCode = k.hashCode();
            i = hashCode & i4;
            k2 = objArr[i];
            if (k2 == null) {
                objArr[i] = k;
                objArr2[i] = v;
                i5 = this.size;
                this.size = i5 + 1;
                if (i5 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i2 = hash2(hashCode);
            k3 = objArr[i2];
            if (k3 == null) {
                objArr[i2] = k;
                objArr2[i2] = v;
                i5 = this.size;
                this.size = i5 + 1;
                if (i5 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i3 = hash3(hashCode);
            k4 = objArr[i3];
            if (k4 == null) {
                objArr[i3] = k;
                objArr2[i3] = v;
                i5 = this.size;
                this.size = i5 + 1;
                if (i5 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i5++;
        } while (i5 != i6);
        putStash(k, v);
    }

    private void putStash(K k, V v) {
        if (this.stashSize == this.stashCapacity) {
            resize(this.capacity << 1);
            put_internal(k, v);
            return;
        }
        int i = this.capacity + this.stashSize;
        this.keyTable[i] = k;
        this.valueTable[i] = v;
        this.stashSize++;
        this.size++;
    }

    public V get(K k) {
        int hashCode = k.hashCode();
        int i = this.mask & hashCode;
        if (!k.equals(this.keyTable[i])) {
            i = hash2(hashCode);
            if (!k.equals(this.keyTable[i])) {
                i = hash3(hashCode);
                if (!k.equals(this.keyTable[i])) {
                    return getStash(k);
                }
            }
        }
        return this.valueTable[i];
    }

    private V getStash(K k) {
        Object[] objArr = this.keyTable;
        int i = this.capacity;
        int i2 = this.stashSize + i;
        while (i < i2) {
            if (k.equals(objArr[i])) {
                return this.valueTable[i];
            }
            i++;
        }
        return null;
    }

    public V remove(K k) {
        int hashCode = k.hashCode();
        int i = this.mask & hashCode;
        V v;
        if (k.equals(this.keyTable[i])) {
            this.keyTable[i] = null;
            v = this.valueTable[i];
            this.valueTable[i] = null;
            this.size--;
            return v;
        }
        i = hash2(hashCode);
        if (k.equals(this.keyTable[i])) {
            this.keyTable[i] = null;
            v = this.valueTable[i];
            this.valueTable[i] = null;
            this.size--;
            return v;
        }
        i = hash3(hashCode);
        if (!k.equals(this.keyTable[i])) {
            return removeStash(k);
        }
        this.keyTable[i] = null;
        v = this.valueTable[i];
        this.valueTable[i] = null;
        this.size--;
        return v;
    }

    /* access modifiers changed from: 0000 */
    public V removeStash(K k) {
        Object[] objArr = this.keyTable;
        int i = this.capacity;
        int i2 = i + this.stashSize;
        for (int i3 = i; i3 < i2; i3++) {
            if (k.equals(objArr[i3])) {
                V v = this.valueTable[i3];
                removeStashIndex(i3);
                this.size--;
                return v;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void removeStashIndex(int i) {
        this.stashSize--;
        int i2 = this.capacity + this.stashSize;
        if (i < i2) {
            this.keyTable[i] = this.keyTable[i2];
            this.valueTable[i] = this.valueTable[i2];
            this.valueTable[i2] = null;
            return;
        }
        this.valueTable[i] = null;
    }

    public void clear() {
        Object[] objArr = this.keyTable;
        Object[] objArr2 = this.valueTable;
        int i = this.capacity + this.stashSize;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                objArr[i2] = null;
                objArr2[i2] = null;
                i = i2;
            } else {
                this.size = 0;
                this.stashSize = 0;
                return;
            }
        }
    }

    public boolean containsValue(Object obj, boolean z) {
        Object[] objArr = this.valueTable;
        int i;
        int i2;
        if (obj != null) {
            if (!z) {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (obj.equals(objArr[i2])) {
                        return true;
                    } else {
                        i = i2;
                    }
                }
            } else {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (objArr[i2] == obj) {
                        return true;
                    } else {
                        i = i2;
                    }
                }
            }
        } else {
            Object[] objArr2 = this.keyTable;
            i = this.capacity + this.stashSize;
            while (true) {
                i2 = i - 1;
                if (i <= 0) {
                    break;
                } else if (objArr2[i2] != null && objArr[i2] == null) {
                    return true;
                } else {
                    i = i2;
                }
            }
        }
        return false;
    }

    public boolean containsKey(K k) {
        int hashCode = k.hashCode();
        if (!k.equals(this.keyTable[this.mask & hashCode])) {
            if (!k.equals(this.keyTable[hash2(hashCode)])) {
                if (!k.equals(this.keyTable[hash3(hashCode)])) {
                    return containsKeyStash(k);
                }
            }
        }
        return true;
    }

    private boolean containsKeyStash(K k) {
        Object[] objArr = this.keyTable;
        int i = this.capacity;
        int i2 = this.stashSize + i;
        while (i < i2) {
            if (k.equals(objArr[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    public K findKey(Object obj, boolean z) {
        Object[] objArr = this.valueTable;
        int i;
        int i2;
        if (obj != null) {
            if (!z) {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (obj.equals(objArr[i2])) {
                        return this.keyTable[i2];
                    } else {
                        i = i2;
                    }
                }
            } else {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (objArr[i2] == obj) {
                        return this.keyTable[i2];
                    } else {
                        i = i2;
                    }
                }
            }
        } else {
            Object[] objArr2 = this.keyTable;
            i = this.capacity + this.stashSize;
            while (true) {
                i2 = i - 1;
                if (i <= 0) {
                    break;
                } else if (objArr2[i2] != null && objArr[i2] == null) {
                    return objArr2[i2];
                } else {
                    i = i2;
                }
            }
        }
        return null;
    }

    public void ensureCapacity(int i) {
        int i2 = this.size + i;
        if (i2 >= this.threshold) {
            resize(nextPowerOfTwo((int) (((float) i2) / this.loadFactor)));
        }
    }

    private void resize(int i) {
        int i2 = this.stashSize + this.capacity;
        this.capacity = i;
        this.threshold = (int) (((float) i) * this.loadFactor);
        this.mask = i - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(i);
        this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log((double) i))) * 2);
        this.pushIterations = Math.max(Math.min(i, 8), ((int) Math.sqrt((double) i)) / 8);
        Object[] objArr = this.keyTable;
        Object[] objArr2 = this.valueTable;
        this.keyTable = new Object[(this.stashCapacity + i)];
        this.valueTable = new Object[(this.stashCapacity + i)];
        this.size = 0;
        this.stashSize = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            Object obj = objArr[i3];
            if (obj != null) {
                putResize(obj, objArr2[i3]);
            }
        }
    }

    private int hash2(int i) {
        int i2 = PRIME2 * i;
        return (i2 ^ (i2 >>> this.hashShift)) & this.mask;
    }

    private int hash3(int i) {
        int i2 = PRIME3 * i;
        return (i2 ^ (i2 >>> this.hashShift)) & this.mask;
    }

    public String toString() {
        if (this.size == 0) {
            return "{}";
        }
        int i;
        StringBuilder stringBuilder = new StringBuilder(32);
        stringBuilder.append('{');
        Object[] objArr = this.keyTable;
        Object[] objArr2 = this.valueTable;
        int length = objArr.length;
        while (true) {
            i = length;
            length = i - 1;
            if (i <= 0) {
                break;
            }
            Object obj = objArr[length];
            if (obj != null) {
                stringBuilder.append(obj);
                stringBuilder.append(SignatureVisitor.INSTANCEOF);
                stringBuilder.append(objArr2[length]);
                break;
            }
        }
        while (true) {
            i = length - 1;
            if (length > 0) {
                Object obj2 = objArr[i];
                if (obj2 == null) {
                    length = i;
                } else {
                    stringBuilder.append(", ");
                    stringBuilder.append(obj2);
                    stringBuilder.append(SignatureVisitor.INSTANCEOF);
                    stringBuilder.append(objArr2[i]);
                    length = i;
                }
            } else {
                stringBuilder.append('}');
                return stringBuilder.toString();
            }
        }
    }

    public Entries<K, V> entries() {
        if (this.entries == null) {
            this.entries = new Entries(this);
        } else {
            this.entries.reset();
        }
        return this.entries;
    }

    public Values<V> values() {
        if (this.values == null) {
            this.values = new Values(this);
        } else {
            this.values.reset();
        }
        return this.values;
    }

    public Keys<K> keys() {
        if (this.keys == null) {
            this.keys = new Keys(this);
        } else {
            this.keys.reset();
        }
        return this.keys;
    }

    static int nextPowerOfTwo(int i) {
        if (i == 0) {
            return 1;
        }
        int i2 = i - 1;
        i2 |= i2 >> 1;
        i2 |= i2 >> 2;
        i2 |= i2 >> 4;
        i2 |= i2 >> 8;
        return (i2 | (i2 >> 16)) + 1;
    }
}
