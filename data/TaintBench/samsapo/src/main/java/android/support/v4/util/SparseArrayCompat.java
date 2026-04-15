package android.support.v4.util;

public class SparseArrayCompat<E> implements Cloneable {
    private static final Object DELETED;
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    static {
        Object obj = r2;
        Object obj2 = new Object();
        DELETED = obj;
    }

    public SparseArrayCompat() {
        this(10);
    }

    public SparseArrayCompat(int i) {
        int i2 = i;
        this.mGarbage = false;
        if (i2 == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            i2 = ContainerHelpers.idealIntArraySize(i2);
            this.mKeys = new int[i2];
            this.mValues = new Object[i2];
        }
        this.mSize = 0;
    }

    public SparseArrayCompat<E> clone() {
        SparseArrayCompat<E> sparseArrayCompat = null;
        try {
            sparseArrayCompat = (SparseArrayCompat) super.clone();
            sparseArrayCompat.mKeys = (int[]) this.mKeys.clone();
            sparseArrayCompat.mValues = (Object[]) this.mValues.clone();
        } catch (CloneNotSupportedException e) {
            CloneNotSupportedException cloneNotSupportedException = e;
        }
        return sparseArrayCompat;
    }

    public E get(int i) {
        return get(i, null);
    }

    public E get(int i, E e) {
        E e2 = e;
        int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, i);
        if (binarySearch < 0 || this.mValues[binarySearch] == DELETED) {
            return e2;
        }
        return this.mValues[binarySearch];
    }

    public void delete(int i) {
        int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, i);
        if (binarySearch >= 0 && this.mValues[binarySearch] != DELETED) {
            this.mValues[binarySearch] = DELETED;
            this.mGarbage = true;
        }
    }

    public void remove(int i) {
        delete(i);
    }

    public void removeAt(int i) {
        int i2 = i;
        if (this.mValues[i2] != DELETED) {
            this.mValues[i2] = DELETED;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int i, int i2) {
        int i3 = i;
        int min = Math.min(this.mSize, i3 + i2);
        for (int i4 = i3; i4 < min; i4++) {
            removeAt(i4);
        }
    }

    private void gc() {
        int i = this.mSize;
        int i2 = 0;
        int[] iArr = this.mKeys;
        Object[] objArr = this.mValues;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (obj != DELETED) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    objArr[i2] = obj;
                    objArr[i3] = null;
                }
                i2++;
            }
        }
        this.mGarbage = false;
        this.mSize = i2;
    }

    public void put(int i, E e) {
        int i2 = i;
        E e2 = e;
        int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, i2);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = e2;
            return;
        }
        binarySearch ^= -1;
        if (binarySearch >= this.mSize || this.mValues[binarySearch] != DELETED) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
                binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, i2) ^ -1;
            }
            if (this.mSize >= this.mKeys.length) {
                int idealIntArraySize = ContainerHelpers.idealIntArraySize(this.mSize + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new Object[idealIntArraySize];
                System.arraycopy(this.mKeys, 0, obj, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, obj2, 0, this.mValues.length);
                this.mKeys = obj;
                this.mValues = obj2;
            }
            if (this.mSize - binarySearch != 0) {
                System.arraycopy(this.mKeys, binarySearch, this.mKeys, binarySearch + 1, this.mSize - binarySearch);
                System.arraycopy(this.mValues, binarySearch, this.mValues, binarySearch + 1, this.mSize - binarySearch);
            }
            this.mKeys[binarySearch] = i2;
            this.mValues[binarySearch] = e2;
            this.mSize++;
            return;
        }
        this.mKeys[binarySearch] = i2;
        this.mValues[binarySearch] = e2;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public int keyAt(int i) {
        int i2 = i;
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[i2];
    }

    public E valueAt(int i) {
        int i2 = i;
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[i2];
    }

    public void setValueAt(int i, E e) {
        int i2 = i;
        E e2 = e;
        if (this.mGarbage) {
            gc();
        }
        this.mValues[i2] = e2;
    }

    public int indexOfKey(int i) {
        int i2 = i;
        if (this.mGarbage) {
            gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, i2);
    }

    public int indexOfValue(E e) {
        E e2 = e;
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == e2) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        int i = this.mSize;
        Object[] objArr = this.mValues;
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void append(int i, E e) {
        int i2 = i;
        E e2 = e;
        if (this.mSize == 0 || i2 > this.mKeys[this.mSize - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
            }
            int i3 = this.mSize;
            if (i3 >= this.mKeys.length) {
                int idealIntArraySize = ContainerHelpers.idealIntArraySize(i3 + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new Object[idealIntArraySize];
                System.arraycopy(this.mKeys, 0, obj, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, obj2, 0, this.mValues.length);
                this.mKeys = obj;
                this.mValues = obj2;
            }
            this.mKeys[i3] = i2;
            this.mValues[i3] = e2;
            this.mSize = i3 + 1;
            return;
        }
        put(i2, e2);
    }

    public String toString() {
        if (size() <= 0) {
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
            stringBuilder = stringBuilder3.append(keyAt(i));
            stringBuilder = stringBuilder3.append('=');
            SparseArrayCompat valueAt = valueAt(i);
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
