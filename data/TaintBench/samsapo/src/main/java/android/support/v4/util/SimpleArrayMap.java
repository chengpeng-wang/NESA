package android.support.v4.util;

import java.util.Map;

public class SimpleArrayMap<K, V> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;

    /* access modifiers changed from: 0000 */
    public int indexOf(Object obj, int i) {
        Object obj2 = obj;
        int i2 = i;
        int i3 = this.mSize;
        if (i3 == 0) {
            return -1;
        }
        int binarySearch = ContainerHelpers.binarySearch(this.mHashes, i3, i2);
        if (binarySearch < 0) {
            return binarySearch;
        }
        if (obj2.equals(this.mArray[binarySearch << 1])) {
            return binarySearch;
        }
        int i4 = binarySearch + 1;
        while (i4 < i3 && this.mHashes[i4] == i2) {
            if (obj2.equals(this.mArray[i4 << 1])) {
                return i4;
            }
            i4++;
        }
        int i5 = binarySearch - 1;
        while (i5 >= 0 && this.mHashes[i5] == i2) {
            if (obj2.equals(this.mArray[i5 << 1])) {
                return i5;
            }
            i5--;
        }
        return i4 ^ -1;
    }

    /* access modifiers changed from: 0000 */
    public int indexOfNull() {
        int i = this.mSize;
        if (i == 0) {
            return -1;
        }
        int binarySearch = ContainerHelpers.binarySearch(this.mHashes, i, 0);
        if (binarySearch < 0) {
            return binarySearch;
        }
        if (null == this.mArray[binarySearch << 1]) {
            return binarySearch;
        }
        int i2 = binarySearch + 1;
        while (i2 < i && this.mHashes[i2] == 0) {
            if (null == this.mArray[i2 << 1]) {
                return i2;
            }
            i2++;
        }
        int i3 = binarySearch - 1;
        while (i3 >= 0 && this.mHashes[i3] == 0) {
            if (null == this.mArray[i3 << 1]) {
                return i3;
            }
            i3--;
        }
        return i2 ^ -1;
    }

    /* JADX WARNING: Missing block: B:32:0x00ad, code skipped:
            r6 = r5;
     */
    private void allocArrays(int r16) {
        /*
        r15 = this;
        r0 = r15;
        r1 = r16;
        r6 = r1;
        r7 = 8;
        if (r6 != r7) goto L_0x0061;
    L_0x0008:
        r6 = android.support.v4.util.ArrayMap.class;
        r12 = r6;
        r6 = r12;
        r7 = r12;
        r2 = r7;
        monitor-enter(r6);
        r6 = mTwiceBaseCache;	 Catch:{ all -> 0x005b }
        if (r6 == 0) goto L_0x0049;
    L_0x0013:
        r6 = mTwiceBaseCache;	 Catch:{ all -> 0x005b }
        r3 = r6;
        r6 = r0;
        r7 = r3;
        r6.mArray = r7;	 Catch:{ all -> 0x005b }
        r6 = r3;
        r7 = 0;
        r6 = r6[r7];	 Catch:{ all -> 0x005b }
        r6 = (java.lang.Object[]) r6;	 Catch:{ all -> 0x005b }
        r6 = (java.lang.Object[]) r6;	 Catch:{ all -> 0x005b }
        mTwiceBaseCache = r6;	 Catch:{ all -> 0x005b }
        r6 = r0;
        r7 = r3;
        r8 = 1;
        r7 = r7[r8];	 Catch:{ all -> 0x005b }
        r7 = (int[]) r7;	 Catch:{ all -> 0x005b }
        r7 = (int[]) r7;	 Catch:{ all -> 0x005b }
        r6.mHashes = r7;	 Catch:{ all -> 0x005b }
        r6 = r3;
        r7 = 0;
        r8 = r3;
        r9 = 1;
        r10 = 0;
        r12 = r8;
        r13 = r9;
        r14 = r10;
        r8 = r14;
        r9 = r12;
        r10 = r13;
        r11 = r14;
        r9[r10] = r11;	 Catch:{ all -> 0x005b }
        r6[r7] = r8;	 Catch:{ all -> 0x005b }
        r6 = mTwiceBaseCacheSize;	 Catch:{ all -> 0x005b }
        r7 = 1;
        r6 = r6 + -1;
        mTwiceBaseCacheSize = r6;	 Catch:{ all -> 0x005b }
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x005b }
    L_0x0048:
        return;
    L_0x0049:
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x005b }
    L_0x004b:
        r6 = r0;
        r7 = r1;
        r7 = new int[r7];
        r6.mHashes = r7;
        r6 = r0;
        r7 = r1;
        r8 = 1;
        r7 = r7 << 1;
        r7 = new java.lang.Object[r7];
        r6.mArray = r7;
        goto L_0x0048;
    L_0x005b:
        r6 = move-exception;
        r4 = r6;
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x005b }
        r6 = r4;
        throw r6;
    L_0x0061:
        r6 = r1;
        r7 = 4;
        if (r6 != r7) goto L_0x004b;
    L_0x0065:
        r6 = android.support.v4.util.ArrayMap.class;
        r12 = r6;
        r6 = r12;
        r7 = r12;
        r2 = r7;
        monitor-enter(r6);
        r6 = mBaseCache;	 Catch:{ all -> 0x00a9 }
        if (r6 == 0) goto L_0x00a6;
    L_0x0070:
        r6 = mBaseCache;	 Catch:{ all -> 0x00a9 }
        r3 = r6;
        r6 = r0;
        r7 = r3;
        r6.mArray = r7;	 Catch:{ all -> 0x00a9 }
        r6 = r3;
        r7 = 0;
        r6 = r6[r7];	 Catch:{ all -> 0x00a9 }
        r6 = (java.lang.Object[]) r6;	 Catch:{ all -> 0x00a9 }
        r6 = (java.lang.Object[]) r6;	 Catch:{ all -> 0x00a9 }
        mBaseCache = r6;	 Catch:{ all -> 0x00a9 }
        r6 = r0;
        r7 = r3;
        r8 = 1;
        r7 = r7[r8];	 Catch:{ all -> 0x00a9 }
        r7 = (int[]) r7;	 Catch:{ all -> 0x00a9 }
        r7 = (int[]) r7;	 Catch:{ all -> 0x00a9 }
        r6.mHashes = r7;	 Catch:{ all -> 0x00a9 }
        r6 = r3;
        r7 = 0;
        r8 = r3;
        r9 = 1;
        r10 = 0;
        r12 = r8;
        r13 = r9;
        r14 = r10;
        r8 = r14;
        r9 = r12;
        r10 = r13;
        r11 = r14;
        r9[r10] = r11;	 Catch:{ all -> 0x00a9 }
        r6[r7] = r8;	 Catch:{ all -> 0x00a9 }
        r6 = mBaseCacheSize;	 Catch:{ all -> 0x00a9 }
        r7 = 1;
        r6 = r6 + -1;
        mBaseCacheSize = r6;	 Catch:{ all -> 0x00a9 }
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x00a9 }
        goto L_0x0048;
    L_0x00a6:
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x00a9 }
        goto L_0x004b;
    L_0x00a9:
        r6 = move-exception;
        r5 = r6;
        r6 = r2;
        monitor-exit(r6);	 Catch:{ all -> 0x00a9 }
        r6 = r5;
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.SimpleArrayMap.allocArrays(int):void");
    }

    private static void freeArrays(int[] iArr, Object[] objArr, int i) {
        Class cls;
        Class cls2;
        int[] iArr2 = iArr;
        Object[] objArr2 = objArr;
        int i2 = i;
        Class cls3;
        int i3;
        if (iArr2.length == 8) {
            cls3 = ArrayMap.class;
            cls = cls3;
            synchronized (cls3) {
                try {
                    if (mTwiceBaseCacheSize < 10) {
                        objArr2[0] = mTwiceBaseCache;
                        objArr2[1] = iArr2;
                        for (i3 = (i2 << 1) - 1; i3 >= 2; i3--) {
                            objArr2[i3] = null;
                        }
                        mTwiceBaseCache = objArr2;
                        mTwiceBaseCacheSize++;
                    }
                } catch (Throwable th) {
                    Throwable th2 = th;
                    cls2 = cls;
                    throw th2;
                }
            }
        } else if (iArr2.length == 4) {
            cls3 = ArrayMap.class;
            cls = cls3;
            synchronized (cls3) {
                try {
                    if (mBaseCacheSize < 10) {
                        objArr2[0] = mBaseCache;
                        objArr2[1] = iArr2;
                        for (i3 = (i2 << 1) - 1; i3 >= 2; i3--) {
                            objArr2[i3] = null;
                        }
                        mBaseCache = objArr2;
                        mBaseCacheSize++;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    cls2 = cls;
                    throw th4;
                }
            }
        }
    }

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int i) {
        int i2 = i;
        if (i2 == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            allocArrays(i2);
        }
        this.mSize = 0;
    }

    public SimpleArrayMap(SimpleArrayMap simpleArrayMap) {
        SimpleArrayMap simpleArrayMap2 = simpleArrayMap;
        this();
        if (simpleArrayMap2 != null) {
            putAll(simpleArrayMap2);
        }
    }

    public void clear() {
        if (this.mSize != 0) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        }
    }

    public void ensureCapacity(int i) {
        int i2 = i;
        if (this.mHashes.length < i2) {
            Object obj = this.mHashes;
            Object obj2 = this.mArray;
            allocArrays(i2);
            if (this.mSize > 0) {
                System.arraycopy(obj, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(obj2, 0, this.mArray, 0, this.mSize << 1);
            }
            freeArrays(obj, obj2, this.mSize);
        }
    }

    public boolean containsKey(Object obj) {
        Object obj2 = obj;
        boolean z = obj2 == null ? indexOfNull() >= 0 ? true : DEBUG : indexOf(obj2, obj2.hashCode()) >= 0 ? true : DEBUG;
        return z;
    }

    /* access modifiers changed from: 0000 */
    public int indexOfValue(Object obj) {
        Object obj2 = obj;
        int i = this.mSize * 2;
        Object[] objArr = this.mArray;
        int i2;
        if (obj2 == null) {
            for (i2 = 1; i2 < i; i2 += 2) {
                if (objArr[i2] == null) {
                    return i2 >> 1;
                }
            }
        } else {
            for (i2 = 1; i2 < i; i2 += 2) {
                if (obj2.equals(objArr[i2])) {
                    return i2 >> 1;
                }
            }
        }
        return -1;
    }

    public boolean containsValue(Object obj) {
        return indexOfValue(obj) >= 0 ? true : DEBUG;
    }

    public V get(Object obj) {
        Object obj2 = obj;
        int indexOfNull = obj2 == null ? indexOfNull() : indexOf(obj2, obj2.hashCode());
        return indexOfNull >= 0 ? this.mArray[(indexOfNull << 1) + 1] : null;
    }

    public K keyAt(int i) {
        return this.mArray[i << 1];
    }

    public V valueAt(int i) {
        return this.mArray[(i << 1) + 1];
    }

    public V setValueAt(int i, V v) {
        V v2 = v;
        int i2 = (i << 1) + 1;
        V v3 = this.mArray[i2];
        this.mArray[i2] = v2;
        return v3;
    }

    public boolean isEmpty() {
        return this.mSize <= 0 ? true : DEBUG;
    }

    public V put(K k, V v) {
        int i;
        int indexOfNull;
        K k2 = k;
        V v2 = v;
        if (k2 == null) {
            i = 0;
            indexOfNull = indexOfNull();
        } else {
            i = k2.hashCode();
            indexOfNull = indexOf(k2, i);
        }
        if (indexOfNull >= 0) {
            indexOfNull = (indexOfNull << 1) + 1;
            V v3 = this.mArray[indexOfNull];
            this.mArray[indexOfNull] = v2;
            return v3;
        }
        indexOfNull ^= -1;
        if (this.mSize >= this.mHashes.length) {
            int i2 = this.mSize >= 8 ? this.mSize + (this.mSize >> 1) : this.mSize >= 4 ? 8 : 4;
            int i3 = i2;
            Object obj = this.mHashes;
            Object obj2 = this.mArray;
            allocArrays(i3);
            if (this.mHashes.length > 0) {
                System.arraycopy(obj, 0, this.mHashes, 0, obj.length);
                System.arraycopy(obj2, 0, this.mArray, 0, obj2.length);
            }
            freeArrays(obj, obj2, this.mSize);
        }
        if (indexOfNull < this.mSize) {
            System.arraycopy(this.mHashes, indexOfNull, this.mHashes, indexOfNull + 1, this.mSize - indexOfNull);
            System.arraycopy(this.mArray, indexOfNull << 1, this.mArray, (indexOfNull + 1) << 1, (this.mSize - indexOfNull) << 1);
        }
        this.mHashes[indexOfNull] = i;
        this.mArray[indexOfNull << 1] = k2;
        this.mArray[(indexOfNull << 1) + 1] = v2;
        this.mSize++;
        return null;
    }

    public void putAll(SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        SimpleArrayMap<? extends K, ? extends V> simpleArrayMap2 = simpleArrayMap;
        int i = simpleArrayMap2.mSize;
        ensureCapacity(this.mSize + i);
        if (this.mSize != 0) {
            for (int i2 = 0; i2 < i; i2++) {
                Object put = put(simpleArrayMap2.keyAt(i2), simpleArrayMap2.valueAt(i2));
            }
        } else if (i > 0) {
            System.arraycopy(simpleArrayMap2.mHashes, 0, this.mHashes, 0, i);
            System.arraycopy(simpleArrayMap2.mArray, 0, this.mArray, 0, i << 1);
            this.mSize = i;
        }
    }

    public V remove(Object obj) {
        Object obj2 = obj;
        int indexOfNull = obj2 == null ? indexOfNull() : indexOf(obj2, obj2.hashCode());
        if (indexOfNull >= 0) {
            return removeAt(indexOfNull);
        }
        return null;
    }

    public V removeAt(int i) {
        int i2 = i;
        V v = this.mArray[(i2 << 1) + 1];
        if (this.mSize <= 1) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        } else if (this.mHashes.length <= 8 || this.mSize >= this.mHashes.length / 3) {
            this.mSize--;
            if (i2 < this.mSize) {
                System.arraycopy(this.mHashes, i2 + 1, this.mHashes, i2, this.mSize - i2);
                System.arraycopy(this.mArray, (i2 + 1) << 1, this.mArray, i2 << 1, (this.mSize - i2) << 1);
            }
            this.mArray[this.mSize << 1] = null;
            this.mArray[(this.mSize << 1) + 1] = null;
        } else {
            int i3 = this.mSize > 8 ? this.mSize + (this.mSize >> 1) : 8;
            Object obj = this.mHashes;
            Object obj2 = this.mArray;
            allocArrays(i3);
            this.mSize--;
            if (i2 > 0) {
                System.arraycopy(obj, 0, this.mHashes, 0, i2);
                System.arraycopy(obj2, 0, this.mArray, 0, i2 << 1);
            }
            if (i2 < this.mSize) {
                System.arraycopy(obj, i2 + 1, this.mHashes, i2, this.mSize - i2);
                System.arraycopy(obj2, (i2 + 1) << 1, this.mArray, i2 << 1, (this.mSize - i2) << 1);
            }
        }
        return v;
    }

    public int size() {
        return this.mSize;
    }

    public boolean equals(Object obj) {
        SimpleArrayMap simpleArrayMap = obj;
        if (this == simpleArrayMap) {
            return true;
        }
        if (!(simpleArrayMap instanceof Map)) {
            return DEBUG;
        }
        Map map = (Map) simpleArrayMap;
        if (size() != map.size()) {
            return DEBUG;
        }
        int i = 0;
        while (i < this.mSize) {
            try {
                Object keyAt = keyAt(i);
                Object valueAt = valueAt(i);
                Object obj2 = map.get(keyAt);
                if (valueAt == null) {
                    if (obj2 != null || !map.containsKey(keyAt)) {
                        return DEBUG;
                    }
                } else if (!valueAt.equals(obj2)) {
                    return DEBUG;
                }
                i++;
            } catch (NullPointerException e) {
                NullPointerException nullPointerException = e;
                return DEBUG;
            } catch (ClassCastException e2) {
                ClassCastException classCastException = e2;
                return DEBUG;
            }
        }
        return true;
    }

    public int hashCode() {
        int[] iArr = this.mHashes;
        Object[] objArr = this.mArray;
        int i = 0;
        int i2 = 0;
        int i3 = 1;
        int i4 = this.mSize;
        while (i2 < i4) {
            Object obj = objArr[i3];
            i += iArr[i2] ^ (obj == null ? 0 : obj.hashCode());
            i2++;
            i3 += 2;
        }
        return i;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = r9;
        StringBuilder stringBuilder2 = new StringBuilder(this.mSize * 28);
        StringBuilder stringBuilder3 = stringBuilder;
        stringBuilder = stringBuilder3.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                stringBuilder = stringBuilder3.append(", ");
            }
            SimpleArrayMap keyAt = keyAt(i);
            if (keyAt != this) {
                stringBuilder = stringBuilder3.append(keyAt);
            } else {
                stringBuilder = stringBuilder3.append("(this Map)");
            }
            stringBuilder = stringBuilder3.append('=');
            SimpleArrayMap valueAt = valueAt(i);
            if (valueAt != this) {
                stringBuilder = stringBuilder3.append(valueAt);
            } else {
                stringBuilder = stringBuilder3.append("(this Map)");
            }
        }
        stringBuilder = stringBuilder3.append('}');
        return stringBuilder3.toString();
    }
}
