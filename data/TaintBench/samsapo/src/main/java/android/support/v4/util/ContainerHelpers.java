package android.support.v4.util;

class ContainerHelpers {
    static final int[] EMPTY_INTS = new int[0];
    static final long[] EMPTY_LONGS = new long[0];
    static final Object[] EMPTY_OBJECTS = new Object[0];

    ContainerHelpers() {
    }

    public static int idealIntArraySize(int i) {
        return idealByteArraySize(i * 4) / 4;
    }

    public static int idealLongArraySize(int i) {
        return idealByteArraySize(i * 8) / 8;
    }

    public static int idealByteArraySize(int i) {
        int i2 = i;
        for (int i3 = 4; i3 < 32; i3++) {
            if (i2 <= (1 << i3) - 12) {
                return (1 << i3) - 12;
            }
        }
        return i2;
    }

    public static boolean equal(Object obj, Object obj2) {
        Object obj3 = obj;
        Object obj4 = obj2;
        boolean z = obj3 == obj4 || (obj3 != null && obj3.equals(obj4));
        return z;
    }

    static int binarySearch(int[] iArr, int i, int i2) {
        int[] iArr2 = iArr;
        int i3 = i2;
        int i4 = 0;
        int i5 = i - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            int i7 = iArr2[i6];
            if (i7 < i3) {
                i4 = i6 + 1;
            } else if (i7 <= i3) {
                return i6;
            } else {
                i5 = i6 - 1;
            }
        }
        return i4 ^ -1;
    }

    static int binarySearch(long[] jArr, int i, long j) {
        long[] jArr2 = jArr;
        long j2 = j;
        int i2 = 0;
        int i3 = i - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            long j3 = jArr2[i4];
            if (j3 < j2) {
                i2 = i4 + 1;
            } else if (j3 <= j2) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return i2 ^ -1;
    }
}
