package com.cleanmaster.util;

/* compiled from: HexUtil */
public class j {
    public static String a(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & 255);
            if (toHexString.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(toHexString);
        }
        return stringBuilder.toString();
    }

    public static byte[] a(String str) {
        int i = 0;
        byte[] bArr = new byte[(str.length() / 2)];
        int i2 = 0;
        while (i < bArr.length) {
            int i3 = i2 + 1;
            char charAt = str.charAt(i2);
            i2 = i3 + 1;
            int a = a(charAt) << 4;
            bArr[i] = (byte) (a(str.charAt(i3)) | a);
            i++;
        }
        return bArr;
    }

    private static int a(char c) {
        if (c >= 'a') {
            return ((c - 97) + 10) & 15;
        }
        if (c >= 'A') {
            return ((c - 65) + 10) & 15;
        }
        return (c - 48) & 15;
    }
}
