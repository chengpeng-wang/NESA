package com.baidu.location;

class Jni {
    private static int a = 14;
    /* renamed from: byte */
    private static int f20byte = 2;
    /* renamed from: case */
    private static int f21case = 0;
    /* renamed from: do */
    private static int f22do = 1024;
    /* renamed from: for */
    private static int f23for = 11;
    /* renamed from: if */
    private static int f24if = 13;
    /* renamed from: int */
    private static int f25int = 12;
    /* renamed from: new */
    private static boolean f26new;
    /* renamed from: try */
    private static int f27try = 1;

    static {
        f26new = false;
        try {
            System.loadLibrary("locSDK3");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            f26new = true;
        }
    }

    Jni() {
    }

    private static native String a(byte[] bArr, int i);

    private static native String b(double d, double d2, int i, int i2);

    /* renamed from: if */
    public static String m673if(String str) {
        int i = 740;
        int i2 = 0;
        if (f26new) {
            return "err!";
        }
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[f22do];
        int length = bytes.length;
        if (length <= 740) {
            i = length;
        }
        length = 0;
        while (i2 < i) {
            if (bytes[i2] != (byte) 0) {
                bArr[length] = bytes[i2];
                length++;
            } else {
                j.a(f.v, "\\0 found in string");
            }
            i2++;
        }
        j.a(f.v, "number:" + bytes.length);
        return a(bArr, 132456) + "|tp=3";
    }

    /* renamed from: if */
    public static double[] m674if(double d, double d2, String str) {
        double[] dArr = new double[]{0.0d, 0.0d};
        if (f26new) {
            return dArr;
        }
        int i = -1;
        if (str.equals("bd09")) {
            i = f21case;
        } else if (str.equals("bd09ll")) {
            i = f27try;
        } else if (str.equals("gcj02")) {
            i = f20byte;
        } else if (str.equals("gps2gcj")) {
            i = f23for;
        } else if (str.equals("bd092gcj")) {
            i = f25int;
        } else if (str.equals("bd09ll2gcj")) {
            i = f24if;
        }
        j.a(f.v, "type:" + i);
        try {
            String[] split = b(d, d2, i, 132456).split(":");
            dArr[0] = Double.parseDouble(split[0]);
            dArr[1] = Double.parseDouble(split[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dArr;
    }
}
