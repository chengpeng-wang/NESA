package com.esotericsoftware.kryo.util;

import org.objectweb.asm.signature.SignatureVisitor;

public class IntMap<V> {
    private static final int EMPTY = 0;
    private static final int PRIME1 = -1105259343;
    private static final int PRIME2 = -1262997959;
    private static final int PRIME3 = -825114047;
    int capacity;
    boolean hasZeroValue;
    private int hashShift;
    int[] keyTable;
    private float loadFactor;
    private int mask;
    private int pushIterations;
    public int size;
    private int stashCapacity;
    int stashSize;
    private int threshold;
    V[] valueTable;
    V zeroValue;

    public IntMap() {
        this(32, 0.8f);
    }

    public IntMap(int i) {
        this(i, 0.8f);
    }

    public IntMap(int i, float f) {
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
            this.keyTable = new int[(this.capacity + this.stashCapacity)];
            this.valueTable = new Object[this.keyTable.length];
        }
    }

    public V put(int i, V v) {
        if (i == 0) {
            Object obj = this.zeroValue;
            this.zeroValue = v;
            this.hasZeroValue = true;
            this.size++;
            return obj;
        }
        int[] iArr = this.keyTable;
        int i2 = i & this.mask;
        int i3 = iArr[i2];
        V v2;
        if (i3 == i) {
            v2 = this.valueTable[i2];
            this.valueTable[i2] = v;
            return v2;
        }
        int hash2 = hash2(i);
        int i4 = iArr[hash2];
        if (i4 == i) {
            v2 = this.valueTable[hash2];
            this.valueTable[hash2] = v;
            return v2;
        }
        int hash3 = hash3(i);
        int i5 = iArr[hash3];
        if (i5 == i) {
            v2 = this.valueTable[hash3];
            this.valueTable[hash3] = v;
            return v2;
        }
        int i6 = this.capacity;
        int i7 = i6 + this.stashSize;
        for (int i8 = i6; i8 < i7; i8++) {
            if (i == iArr[i8]) {
                v2 = this.valueTable[i8];
                this.valueTable[i8] = v;
                return v2;
            }
        }
        if (i3 == 0) {
            iArr[i2] = i;
            this.valueTable[i2] = v;
            i6 = this.size;
            this.size = i6 + 1;
            if (i6 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else if (i4 == 0) {
            iArr[hash2] = i;
            this.valueTable[hash2] = v;
            i6 = this.size;
            this.size = i6 + 1;
            if (i6 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else if (i5 == 0) {
            iArr[hash3] = i;
            this.valueTable[hash3] = v;
            i6 = this.size;
            this.size = i6 + 1;
            if (i6 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else {
            push(i, v, i2, i3, hash2, i4, hash3, i5);
            return null;
        }
    }

    private void putResize(int i, V v) {
        if (i == 0) {
            this.zeroValue = v;
            this.hasZeroValue = true;
            return;
        }
        int i2 = i & this.mask;
        int i3 = this.keyTable[i2];
        int i4;
        if (i3 == 0) {
            this.keyTable[i2] = i;
            this.valueTable[i2] = v;
            i4 = this.size;
            this.size = i4 + 1;
            if (i4 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int hash2 = hash2(i);
        int i5 = this.keyTable[hash2];
        if (i5 == 0) {
            this.keyTable[hash2] = i;
            this.valueTable[hash2] = v;
            i4 = this.size;
            this.size = i4 + 1;
            if (i4 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int hash3 = hash3(i);
        int i6 = this.keyTable[hash3];
        if (i6 == 0) {
            this.keyTable[hash3] = i;
            this.valueTable[hash3] = v;
            i4 = this.size;
            this.size = i4 + 1;
            if (i4 >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        push(i, v, i2, i3, hash2, i5, hash3, i6);
    }

    private void push(int i, V v, int i2, int i3, int i4, int i5, int i6, int i7) {
        int[] iArr = this.keyTable;
        Object[] objArr = this.valueTable;
        int i8 = this.mask;
        int i9 = 0;
        int i10 = this.pushIterations;
        do {
            V v2;
            switch (ObjectMap.random.nextInt(3)) {
                case 0:
                    v2 = objArr[i2];
                    iArr[i2] = i;
                    objArr[i2] = v;
                    v = v2;
                    i = i3;
                    break;
                case 1:
                    v2 = objArr[i4];
                    iArr[i4] = i;
                    objArr[i4] = v;
                    v = v2;
                    i = i5;
                    break;
                default:
                    v2 = objArr[i6];
                    iArr[i6] = i;
                    objArr[i6] = v;
                    v = v2;
                    i = i7;
                    break;
            }
            i2 = i & i8;
            i3 = iArr[i2];
            if (i3 == 0) {
                iArr[i2] = i;
                objArr[i2] = v;
                i9 = this.size;
                this.size = i9 + 1;
                if (i9 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i4 = hash2(i);
            i5 = iArr[i4];
            if (i5 == 0) {
                iArr[i4] = i;
                objArr[i4] = v;
                i9 = this.size;
                this.size = i9 + 1;
                if (i9 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i6 = hash3(i);
            i7 = iArr[i6];
            if (i7 == 0) {
                iArr[i6] = i;
                objArr[i6] = v;
                i9 = this.size;
                this.size = i9 + 1;
                if (i9 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            i9++;
        } while (i9 != i10);
        putStash(i, v);
    }

    private void putStash(int i, V v) {
        if (this.stashSize == this.stashCapacity) {
            resize(this.capacity << 1);
            put(i, v);
            return;
        }
        int i2 = this.capacity + this.stashSize;
        this.keyTable[i2] = i;
        this.valueTable[i2] = v;
        this.stashSize++;
        this.size++;
    }

    public V get(int i) {
        if (i == 0) {
            return this.zeroValue;
        }
        int i2 = this.mask & i;
        if (this.keyTable[i2] != i) {
            i2 = hash2(i);
            if (this.keyTable[i2] != i) {
                i2 = hash3(i);
                if (this.keyTable[i2] != i) {
                    return getStash(i, null);
                }
            }
        }
        return this.valueTable[i2];
    }

    public V get(int i, V v) {
        if (i == 0) {
            return this.zeroValue;
        }
        int i2 = this.mask & i;
        if (this.keyTable[i2] != i) {
            i2 = hash2(i);
            if (this.keyTable[i2] != i) {
                i2 = hash3(i);
                if (this.keyTable[i2] != i) {
                    return getStash(i, v);
                }
            }
        }
        return this.valueTable[i2];
    }

    private V getStash(int i, V v) {
        int[] iArr = this.keyTable;
        int i2 = this.capacity;
        int i3 = this.stashSize + i2;
        while (i2 < i3) {
            if (iArr[i2] == i) {
                return this.valueTable[i2];
            }
            i2++;
        }
        return v;
    }

    public V remove(int i) {
        V v;
        if (i != 0) {
            int i2 = i & this.mask;
            if (this.keyTable[i2] == i) {
                this.keyTable[i2] = 0;
                v = this.valueTable[i2];
                this.valueTable[i2] = null;
                this.size--;
                return v;
            }
            i2 = hash2(i);
            if (this.keyTable[i2] == i) {
                this.keyTable[i2] = 0;
                v = this.valueTable[i2];
                this.valueTable[i2] = null;
                this.size--;
                return v;
            }
            i2 = hash3(i);
            if (this.keyTable[i2] != i) {
                return removeStash(i);
            }
            this.keyTable[i2] = 0;
            v = this.valueTable[i2];
            this.valueTable[i2] = null;
            this.size--;
            return v;
        } else if (!this.hasZeroValue) {
            return null;
        } else {
            v = this.zeroValue;
            this.zeroValue = null;
            this.hasZeroValue = false;
            this.size--;
            return v;
        }
    }

    /* access modifiers changed from: 0000 */
    public V removeStash(int i) {
        int[] iArr = this.keyTable;
        int i2 = this.capacity;
        int i3 = i2 + this.stashSize;
        for (int i4 = i2; i4 < i3; i4++) {
            if (iArr[i4] == i) {
                V v = this.valueTable[i4];
                removeStashIndex(i4);
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
        int[] iArr = this.keyTable;
        Object[] objArr = this.valueTable;
        int i = this.capacity + this.stashSize;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                iArr[i2] = 0;
                objArr[i2] = null;
                i = i2;
            } else {
                this.size = 0;
                this.stashSize = 0;
                this.zeroValue = null;
                this.hasZeroValue = false;
                return;
            }
        }
    }

    public boolean containsValue(Object obj, boolean z) {
        Object[] objArr = this.valueTable;
        int i;
        int i2;
        if (obj == null) {
            if (!this.hasZeroValue || this.zeroValue != null) {
                int[] iArr = this.keyTable;
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (iArr[i2] != 0 && objArr[i2] == null) {
                        return true;
                    } else {
                        i = i2;
                    }
                }
            } else {
                return true;
            }
        } else if (z) {
            if (obj != this.zeroValue) {
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
            } else {
                return true;
            }
        } else if (!this.hasZeroValue || !obj.equals(this.zeroValue)) {
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
            return true;
        }
        return false;
    }

    public boolean containsKey(int i) {
        if (i == 0) {
            return this.hasZeroValue;
        }
        if (this.keyTable[this.mask & i] != i) {
            if (this.keyTable[hash2(i)] != i) {
                if (this.keyTable[hash3(i)] != i) {
                    return containsKeyStash(i);
                }
            }
        }
        return true;
    }

    private boolean containsKeyStash(int i) {
        int[] iArr = this.keyTable;
        int i2 = this.capacity;
        int i3 = this.stashSize + i2;
        while (i2 < i3) {
            if (iArr[i2] == i) {
                return true;
            }
            i2++;
        }
        return false;
    }

    public int findKey(Object obj, boolean z, int i) {
        Object[] objArr = this.valueTable;
        int i2;
        int i3;
        if (obj == null) {
            if (this.hasZeroValue && this.zeroValue == null) {
                return 0;
            }
            int[] iArr = this.keyTable;
            i2 = this.capacity + this.stashSize;
            while (true) {
                i3 = i2 - 1;
                if (i2 <= 0) {
                    return i;
                }
                if (iArr[i3] != 0 && objArr[i3] == null) {
                    return iArr[i3];
                }
                i2 = i3;
            }
        } else if (z) {
            if (obj == this.zeroValue) {
                return 0;
            }
            i2 = this.capacity + this.stashSize;
            while (true) {
                i3 = i2 - 1;
                if (i2 <= 0) {
                    return i;
                }
                if (objArr[i3] == obj) {
                    return this.keyTable[i3];
                }
                i2 = i3;
            }
        } else if (this.hasZeroValue && obj.equals(this.zeroValue)) {
            return 0;
        } else {
            i2 = this.capacity + this.stashSize;
            while (true) {
                i3 = i2 - 1;
                if (i2 <= 0) {
                    return i;
                }
                if (obj.equals(objArr[i3])) {
                    return this.keyTable[i3];
                }
                i2 = i3;
            }
        }
    }

    public void ensureCapacity(int i) {
        int i2 = this.size + i;
        if (i2 >= this.threshold) {
            resize(ObjectMap.nextPowerOfTwo((int) (((float) i2) / this.loadFactor)));
        }
    }

    private void resize(int i) {
        int i2;
        int i3 = 0;
        int i4 = this.stashSize + this.capacity;
        this.capacity = i;
        this.threshold = (int) (((float) i) * this.loadFactor);
        this.mask = i - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(i);
        this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log((double) i))) * 2);
        this.pushIterations = Math.max(Math.min(i, 8), ((int) Math.sqrt((double) i)) / 8);
        int[] iArr = this.keyTable;
        Object[] objArr = this.valueTable;
        this.keyTable = new int[(this.stashCapacity + i)];
        this.valueTable = new Object[(this.stashCapacity + i)];
        if (this.hasZeroValue) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        this.size = i2;
        this.stashSize = 0;
        while (i3 < i4) {
            i2 = iArr[i3];
            if (i2 != 0) {
                putResize(i2, objArr[i3]);
            }
            i3++;
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
            return "[]";
        }
        int i;
        StringBuilder stringBuilder = new StringBuilder(32);
        stringBuilder.append('[');
        int[] iArr = this.keyTable;
        Object[] objArr = this.valueTable;
        int length = iArr.length;
        while (true) {
            i = length;
            length = i - 1;
            if (i <= 0) {
                break;
            }
            i = iArr[length];
            if (i != 0) {
                stringBuilder.append(i);
                stringBuilder.append(SignatureVisitor.INSTANCEOF);
                stringBuilder.append(objArr[length]);
                break;
            }
        }
        while (true) {
            i = length - 1;
            if (length > 0) {
                length = iArr[i];
                if (length == 0) {
                    length = i;
                } else {
                    stringBuilder.append(", ");
                    stringBuilder.append(length);
                    stringBuilder.append(SignatureVisitor.INSTANCEOF);
                    stringBuilder.append(objArr[i]);
                    length = i;
                }
            } else {
                stringBuilder.append(']');
                return stringBuilder.toString();
            }
        }
    }
}
