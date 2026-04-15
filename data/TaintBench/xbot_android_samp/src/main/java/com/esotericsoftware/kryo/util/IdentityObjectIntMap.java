package com.esotericsoftware.kryo.util;

import org.objectweb.asm.signature.SignatureVisitor;

public class IdentityObjectIntMap<K> {
    private static final int PRIME1 = -1105259343;
    private static final int PRIME2 = -1262997959;
    private static final int PRIME3 = -825114047;
    int capacity;
    private int hashShift;
    K[] keyTable;
    private float loadFactor;
    private int mask;
    private int pushIterations;
    public int size;
    private int stashCapacity;
    int stashSize;
    private int threshold;
    int[] valueTable;

    public IdentityObjectIntMap() {
        this(32, 0.8f);
    }

    public IdentityObjectIntMap(int i) {
        this(i, 0.8f);
    }

    public IdentityObjectIntMap(int i, float f) {
        if (i < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0: " + i);
        } else if (this.capacity > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large: " + i);
        } else {
            this.capacity = ObjectMap.nextPowerOfTwo(i);
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
            this.valueTable = new int[this.keyTable.length];
        }
    }

    public void put(K k, int i) {
        if (k == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }
        Object[] objArr = this.keyTable;
        int identityHashCode = System.identityHashCode(k);
        int i2 = identityHashCode & this.mask;
        K k2 = objArr[i2];
        if (k == k2) {
            this.valueTable[i2] = i;
            return;
        }
        int hash2 = hash2(identityHashCode);
        K k3 = objArr[hash2];
        if (k == k3) {
            this.valueTable[hash2] = i;
            return;
        }
        int hash3 = hash3(identityHashCode);
        K k4 = objArr[hash3];
        if (k == k4) {
            this.valueTable[hash3] = i;
            return;
        }
        identityHashCode = this.capacity;
        int i3 = this.stashSize + identityHashCode;
        while (identityHashCode < i3) {
            if (objArr[identityHashCode] == k) {
                this.valueTable[identityHashCode] = i;
                return;
            }
            identityHashCode++;
        }
        if (k2 == null) {
            objArr[i2] = k;
            this.valueTable[i2] = i;
            identityHashCode = this.size;
            this.size = identityHashCode + 1;
            if (identityHashCode >= this.threshold) {
                resize(this.capacity << 1);
            }
        } else if (k3 == null) {
            objArr[hash2] = k;
            this.valueTable[hash2] = i;
            identityHashCode = this.size;
            this.size = identityHashCode + 1;
            if (identityHashCode >= this.threshold) {
                resize(this.capacity << 1);
            }
        } else if (k4 == null) {
            objArr[hash3] = k;
            this.valueTable[hash3] = i;
            identityHashCode = this.size;
            this.size = identityHashCode + 1;
            if (identityHashCode >= this.threshold) {
                resize(this.capacity << 1);
            }
        } else {
            push(k, i, i2, k2, hash2, k3, hash3, k4);
        }
    }

    private void putResize(K k, int i) {
        int identityHashCode = System.identityHashCode(k);
        int i2 = identityHashCode & this.mask;
        Object obj = this.keyTable[i2];
        if (obj == null) {
            this.keyTable[i2] = k;
            this.valueTable[i2] = i;
            identityHashCode = this.size;
            this.size = identityHashCode + 1;
            if (identityHashCode >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int hash2 = hash2(identityHashCode);
        Object obj2 = this.keyTable[hash2];
        if (obj2 == null) {
            this.keyTable[hash2] = k;
            this.valueTable[hash2] = i;
            identityHashCode = this.size;
            this.size = identityHashCode + 1;
            if (identityHashCode >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int hash3 = hash3(identityHashCode);
        Object obj3 = this.keyTable[hash3];
        if (obj3 == null) {
            this.keyTable[hash3] = k;
            this.valueTable[hash3] = i;
            identityHashCode = this.size;
            this.size = identityHashCode + 1;
            if (identityHashCode >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        push(k, i, i2, obj, hash2, obj2, hash3, obj3);
    }

    private void push(K k, int i, int i2, K k2, int i3, K k3, int i4, K k4) {
        Object[] objArr = this.keyTable;
        int[] iArr = this.valueTable;
        int i5 = this.mask;
        int i6 = 0;
        int i7 = this.pushIterations;
        do {
            int i8;
            switch (ObjectMap.random.nextInt(3)) {
                case 0:
                    i8 = iArr[i2];
                    objArr[i2] = k;
                    iArr[i2] = i;
                    i = i8;
                    k = k2;
                    break;
                case 1:
                    i8 = iArr[i3];
                    objArr[i3] = k;
                    iArr[i3] = i;
                    i = i8;
                    k = k3;
                    break;
                default:
                    i8 = iArr[i4];
                    objArr[i4] = k;
                    iArr[i4] = i;
                    i = i8;
                    k = k4;
                    break;
            }
            i8 = System.identityHashCode(k);
            i2 = i8 & i5;
            k2 = objArr[i2];
            if (k2 == null) {
                objArr[i2] = k;
                iArr[i2] = i;
                i6 = this.size;
                this.size = i6 + 1;
                if (i6 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i3 = hash2(i8);
            k3 = objArr[i3];
            if (k3 == null) {
                objArr[i3] = k;
                iArr[i3] = i;
                i6 = this.size;
                this.size = i6 + 1;
                if (i6 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i4 = hash3(i8);
            k4 = objArr[i4];
            if (k4 == null) {
                objArr[i4] = k;
                iArr[i4] = i;
                i6 = this.size;
                this.size = i6 + 1;
                if (i6 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i6++;
        } while (i6 != i7);
        putStash(k, i);
    }

    private void putStash(K k, int i) {
        if (this.stashSize == this.stashCapacity) {
            resize(this.capacity << 1);
            put(k, i);
            return;
        }
        int i2 = this.capacity + this.stashSize;
        this.keyTable[i2] = k;
        this.valueTable[i2] = i;
        this.stashSize++;
        this.size++;
    }

    public int get(K k, int i) {
        int identityHashCode = System.identityHashCode(k);
        int i2 = this.mask & identityHashCode;
        if (k != this.keyTable[i2]) {
            i2 = hash2(identityHashCode);
            if (k != this.keyTable[i2]) {
                i2 = hash3(identityHashCode);
                if (k != this.keyTable[i2]) {
                    return getStash(k, i);
                }
            }
        }
        return this.valueTable[i2];
    }

    private int getStash(K k, int i) {
        Object[] objArr = this.keyTable;
        int i2 = this.capacity;
        int i3 = this.stashSize + i2;
        while (i2 < i3) {
            if (k == objArr[i2]) {
                return this.valueTable[i2];
            }
            i2++;
        }
        return i;
    }

    public int getAndIncrement(K k, int i, int i2) {
        int identityHashCode = System.identityHashCode(k);
        int i3 = this.mask & identityHashCode;
        if (k != this.keyTable[i3]) {
            i3 = hash2(identityHashCode);
            if (k != this.keyTable[i3]) {
                i3 = hash3(identityHashCode);
                if (k != this.keyTable[i3]) {
                    return getAndIncrementStash(k, i, i2);
                }
            }
        }
        identityHashCode = this.valueTable[i3];
        this.valueTable[i3] = identityHashCode + i2;
        return identityHashCode;
    }

    private int getAndIncrementStash(K k, int i, int i2) {
        Object[] objArr = this.keyTable;
        int i3 = this.capacity;
        int i4 = this.stashSize + i3;
        while (i3 < i4) {
            if (k == objArr[i3]) {
                i = this.valueTable[i3];
                this.valueTable[i3] = i + i2;
                return i;
            }
            i3++;
        }
        put(k, i + i2);
        return i;
    }

    public int remove(K k, int i) {
        int identityHashCode = System.identityHashCode(k);
        int i2 = this.mask & identityHashCode;
        if (k == this.keyTable[i2]) {
            this.keyTable[i2] = null;
            identityHashCode = this.valueTable[i2];
            this.size--;
            return identityHashCode;
        }
        i2 = hash2(identityHashCode);
        if (k == this.keyTable[i2]) {
            this.keyTable[i2] = null;
            identityHashCode = this.valueTable[i2];
            this.size--;
            return identityHashCode;
        }
        identityHashCode = hash3(identityHashCode);
        if (k != this.keyTable[identityHashCode]) {
            return removeStash(k, i);
        }
        this.keyTable[identityHashCode] = null;
        identityHashCode = this.valueTable[identityHashCode];
        this.size--;
        return identityHashCode;
    }

    /* access modifiers changed from: 0000 */
    public int removeStash(K k, int i) {
        Object[] objArr = this.keyTable;
        int i2 = this.capacity;
        int i3 = this.stashSize + i2;
        while (i2 < i3) {
            if (k == objArr[i2]) {
                i = this.valueTable[i2];
                removeStashIndex(i2);
                this.size--;
                return i;
            }
            i2++;
        }
        return i;
    }

    /* access modifiers changed from: 0000 */
    public void removeStashIndex(int i) {
        this.stashSize--;
        int i2 = this.capacity + this.stashSize;
        if (i < i2) {
            this.keyTable[i] = this.keyTable[i2];
            this.valueTable[i] = this.valueTable[i2];
        }
    }

    public void clear() {
        Object[] objArr = this.keyTable;
        int[] iArr = this.valueTable;
        int i = this.capacity + this.stashSize;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                objArr[i2] = null;
                i = i2;
            } else {
                this.size = 0;
                this.stashSize = 0;
                return;
            }
        }
    }

    public boolean containsValue(int i) {
        int[] iArr = this.valueTable;
        int i2 = this.capacity + this.stashSize;
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0) {
                return false;
            }
            if (iArr[i3] == i) {
                return true;
            }
            i2 = i3;
        }
    }

    public boolean containsKey(K k) {
        int identityHashCode = System.identityHashCode(k);
        if (k != this.keyTable[this.mask & identityHashCode]) {
            if (k != this.keyTable[hash2(identityHashCode)]) {
                if (k != this.keyTable[hash3(identityHashCode)]) {
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
            if (k == objArr[i]) {
                return true;
            }
            i++;
        }
        return false;
    }

    public K findKey(int i) {
        int[] iArr = this.valueTable;
        int i2 = this.capacity + this.stashSize;
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0) {
                return null;
            }
            if (iArr[i3] == i) {
                return this.keyTable[i3];
            }
            i2 = i3;
        }
    }

    public void ensureCapacity(int i) {
        int i2 = this.size + i;
        if (i2 >= this.threshold) {
            resize(ObjectMap.nextPowerOfTwo((int) (((float) i2) / this.loadFactor)));
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
        int[] iArr = this.valueTable;
        this.keyTable = new Object[(this.stashCapacity + i)];
        this.valueTable = new int[(this.stashCapacity + i)];
        this.size = 0;
        this.stashSize = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            Object obj = objArr[i3];
            if (obj != null) {
                putResize(obj, iArr[i3]);
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
        int[] iArr = this.valueTable;
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
                stringBuilder.append(iArr[length]);
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
                    stringBuilder.append(iArr[i]);
                    length = i;
                }
            } else {
                stringBuilder.append('}');
                return stringBuilder.toString();
            }
        }
    }
}
