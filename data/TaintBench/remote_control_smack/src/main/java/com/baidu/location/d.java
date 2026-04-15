package com.baidu.location;

import android.support.v4.view.MotionEventCompat;
import java.security.MessageDigest;

class d {
    private static char[] a = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /* renamed from: if */
    private static char[] f112if = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/.".toCharArray();

    d() {
    }

    public static String a(String str) {
        int i = 0;
        try {
            int i2;
            String str2 = StringEncodings.UTF8;
            char[] a = a((str + "webgis").getBytes(str2));
            byte[] bytes = str.getBytes(str2);
            byte[] bArr = new byte[(bytes.length + 2)];
            for (i2 = 0; i2 < bytes.length; i2++) {
                bArr[i2] = bytes[i2];
            }
            bArr[bytes.length] = (byte) (Integer.parseInt(String.copyValueOf(a, 10, 2), 16) & MotionEventCompat.ACTION_MASK);
            bArr[bytes.length + 1] = (byte) (Integer.parseInt(String.copyValueOf(a, 20, 2), 16) & MotionEventCompat.ACTION_MASK);
            String str3 = (("" + ((char) (Integer.parseInt(String.copyValueOf(a, 6, 2), 16) & MotionEventCompat.ACTION_MASK))) + ((char) (Integer.parseInt(String.copyValueOf(a, 16, 2), 16) & MotionEventCompat.ACTION_MASK))) + ((char) (Integer.parseInt(String.copyValueOf(a, 26, 2), 16) & MotionEventCompat.ACTION_MASK));
            char[] a2 = a((str3 + "webgis").getBytes("iso-8859-1"));
            int length = bArr.length;
            int length2 = str3.length();
            byte[] bArr2 = new byte[(length + length2)];
            for (int i3 = 0; i3 < (length + 31) / 32; i3++) {
                int i4 = i3 * 32;
                i2 = 0;
                while (i2 < 32 && i4 + i2 < length) {
                    bArr2[i4 + i2] = (byte) ((a2[i2] & MotionEventCompat.ACTION_MASK) ^ (bArr[i4 + i2] & MotionEventCompat.ACTION_MASK));
                    i2++;
                }
            }
            while (i < length2) {
                bArr2[length + i] = (byte) str3.charAt(i);
                i++;
            }
            return new String(m816if(bArr2));
        } catch (Exception e) {
            e.printStackTrace();
            return "UnsupportedEncodingException";
        }
    }

    private static char[] a(byte[] bArr) {
        int i = 0;
        char[] cArr = new char[32];
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bArr);
            byte[] digest = instance.digest();
            int i2 = 0;
            while (i < 16) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr[i2] = a[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr[i3] = a[b & 15];
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cArr;
    }

    /* renamed from: if */
    private static char[] m816if(byte[] bArr) {
        char[] cArr = new char[(((bArr.length + 2) / 3) * 4)];
        int i = 0;
        int i2 = 0;
        while (i2 < bArr.length) {
            Object obj;
            Object obj2;
            int i3 = (bArr[i2] & MotionEventCompat.ACTION_MASK) << 8;
            if (i2 + 1 < bArr.length) {
                i3 |= bArr[i2 + 1] & MotionEventCompat.ACTION_MASK;
                obj = 1;
            } else {
                obj = null;
            }
            i3 <<= 8;
            if (i2 + 2 < bArr.length) {
                i3 |= bArr[i2 + 2] & MotionEventCompat.ACTION_MASK;
                obj2 = 1;
            } else {
                obj2 = null;
            }
            cArr[i + 3] = f112if[obj2 != null ? 63 - (i3 & 63) : 64];
            int i4 = i3 >> 6;
            cArr[i + 2] = f112if[obj != null ? 63 - (i4 & 63) : 64];
            i3 = i4 >> 6;
            cArr[i + 1] = f112if[63 - (i3 & 63)];
            cArr[i + 0] = f112if[63 - ((i3 >> 6) & 63)];
            i2 += 3;
            i += 4;
        }
        return cArr;
    }
}
