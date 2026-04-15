package com.baidu.android.common.security;

import android.support.v4.view.MotionEventCompat;
import java.io.UnsupportedEncodingException;

public final class Base64 {
    private static final byte[] MAP = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};

    private Base64() {
    }

    public static byte[] decode(byte[] bArr) {
        return decode(bArr, bArr.length);
    }

    public static byte[] decode(byte[] bArr, int i) {
        int i2 = (i / 4) * 3;
        if (i2 == 0) {
            return new byte[0];
        }
        byte b;
        int i3;
        byte[] bArr2 = new byte[i2];
        i2 = 0;
        while (true) {
            b = bArr[i - 1];
            if (!(b == (byte) 10 || b == (byte) 13 || b == (byte) 32 || b == (byte) 9)) {
                if (b != (byte) 61) {
                    break;
                }
                i2++;
            }
            i--;
        }
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i4 < i) {
            b = bArr[i4];
            if (b == (byte) 10 || b == (byte) 13 || b == (byte) 32) {
                i3 = i5;
                i5 = i7;
                i7 = i6;
            } else if (b == (byte) 9) {
                i3 = i5;
                i5 = i7;
                i7 = i6;
            } else {
                if (b >= (byte) 65 && b <= (byte) 90) {
                    i3 = b - 65;
                } else if (b >= (byte) 97 && b <= (byte) 122) {
                    i3 = b - 71;
                } else if (b >= (byte) 48 && b <= (byte) 57) {
                    i3 = b + 4;
                } else if (b == (byte) 43) {
                    i3 = 62;
                } else if (b != (byte) 47) {
                    return null;
                } else {
                    i3 = 63;
                }
                i5 = (i5 << 6) | ((byte) i3);
                if (i6 % 4 == 3) {
                    i3 = i7 + 1;
                    bArr2[i7] = (byte) ((16711680 & i5) >> 16);
                    i7 = i3 + 1;
                    bArr2[i3] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i5) >> 8);
                    i3 = i7 + 1;
                    bArr2[i7] = (byte) (i5 & MotionEventCompat.ACTION_MASK);
                } else {
                    i3 = i7;
                }
                i7 = i6 + 1;
                int i8 = i5;
                i5 = i3;
                i3 = i8;
            }
            i4++;
            i6 = i7;
            i7 = i5;
            i5 = i3;
        }
        if (i2 > 0) {
            i5 <<= i2 * 6;
            i3 = i7 + 1;
            bArr2[i7] = (byte) ((16711680 & i5) >> 16);
            if (i2 == 1) {
                i7 = i3 + 1;
                bArr2[i3] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i5) >> 8);
            } else {
                i7 = i3;
            }
        }
        byte[] bArr3 = new byte[i7];
        System.arraycopy(bArr2, 0, bArr3, 0, i7);
        return bArr3;
    }

    public static String encode(byte[] bArr, String str) throws UnsupportedEncodingException {
        int i;
        int length = (bArr.length * 4) / 3;
        byte[] bArr2 = new byte[(length + ((length / 76) + 3))];
        int length2 = bArr.length - (bArr.length % 3);
        length = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < length2; i3 += 3) {
            i = i2 + 1;
            bArr2[i2] = MAP[(bArr[i3] & MotionEventCompat.ACTION_MASK) >> 2];
            i2 = i + 1;
            bArr2[i] = MAP[((bArr[i3] & 3) << 4) | ((bArr[i3 + 1] & MotionEventCompat.ACTION_MASK) >> 4)];
            int i4 = i2 + 1;
            bArr2[i2] = MAP[((bArr[i3 + 1] & 15) << 2) | ((bArr[i3 + 2] & MotionEventCompat.ACTION_MASK) >> 6)];
            i = i4 + 1;
            bArr2[i4] = MAP[bArr[i3 + 2] & 63];
            if ((i - length) % 76 != 0 || i == 0) {
                i2 = i;
            } else {
                i2 = i + 1;
                bArr2[i] = (byte) 10;
                length++;
            }
        }
        switch (bArr.length % 3) {
            case 1:
                length = i2 + 1;
                bArr2[i2] = MAP[(bArr[length2] & MotionEventCompat.ACTION_MASK) >> 2];
                i2 = length + 1;
                bArr2[length] = MAP[(bArr[length2] & 3) << 4];
                i = i2 + 1;
                bArr2[i2] = (byte) 61;
                length = i + 1;
                bArr2[i] = (byte) 61;
                break;
            case 2:
                length = i2 + 1;
                bArr2[i2] = MAP[(bArr[length2] & MotionEventCompat.ACTION_MASK) >> 2];
                i2 = length + 1;
                bArr2[length] = MAP[((bArr[length2] & 3) << 4) | ((bArr[length2 + 1] & MotionEventCompat.ACTION_MASK) >> 4)];
                i = i2 + 1;
                bArr2[i2] = MAP[(bArr[length2 + 1] & 15) << 2];
                length = i + 1;
                bArr2[i] = (byte) 61;
                break;
            default:
                length = i2;
                break;
        }
        return new String(bArr2, 0, length, str);
    }
}
