package com.flurry.sdk;

import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public final class lt {
    private static final String a = lt.class.getSimpleName();

    public static double a(double d) {
        return ((double) Math.round(Math.pow(10.0d, 3.0d) * d)) / Math.pow(10.0d, 3.0d);
    }

    public static long a(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[1024];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read < 0) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static String a(String str) {
        return (!TextUtils.isEmpty(str) && Uri.parse(str).getScheme() == null) ? "http://" + str : str;
    }

    public static String a(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (byte b : bArr) {
            byte b2 = (byte) (b & 15);
            stringBuilder.append(cArr[(byte) ((b & 240) >> 4)]);
            stringBuilder.append(cArr[b2]);
        }
        return stringBuilder.toString();
    }

    public static void a() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Must be called from the main thread!");
        }
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    public static boolean a(long j) {
        return j == 0 || System.currentTimeMillis() <= j;
    }

    public static boolean a(Intent intent) {
        return hz.a.b.getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    public static byte[] a(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        a(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String b(String str) {
        try {
            return URLEncoder.encode(str, Defaults.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            iw.a(5, a, "Cannot encode '" + str + "'");
            return "";
        }
    }

    public static String b(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            iw.a(5, a, "Unsupported ISO-8859-1:" + e.getMessage());
            return null;
        }
    }

    public static void b() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            throw new IllegalStateException("Must be called from a background thread!");
        }
    }

    public static byte[] c(String str) {
        byte[] bArr = null;
        if (TextUtils.isEmpty(str)) {
            return bArr;
        }
        try {
            return str.getBytes(Defaults.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            iw.a(5, a, "Unsupported UTF-8: " + e.getMessage());
            return bArr;
        }
    }

    public static byte[] d(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(str.getBytes(), 0, str.length());
            return instance.digest();
        } catch (NoSuchAlgorithmException e) {
            iw.a(6, a, "Unsupported SHA1: " + e.getMessage());
            return null;
        }
    }

    public static Map e(String str) {
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            for (String split : str.split(MASTNativeAdConstants.AMPERSAND)) {
                String[] split2 = split.split(MASTNativeAdConstants.EQUAL);
                if (!split2[0].equals("event")) {
                    hashMap.put(g(split2[0]), g(split2[1]));
                }
            }
        }
        return hashMap;
    }

    public static long f(String str) {
        if (str == null) {
            return 0;
        }
        long j = 1125899906842597L;
        int i = 0;
        while (i < str.length()) {
            long charAt = ((long) str.charAt(i)) + (j * 31);
            i++;
            j = charAt;
        }
        return j;
    }

    private static String g(String str) {
        try {
            return URLDecoder.decode(str, Defaults.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            iw.a(5, a, "Cannot decode '" + str + "'");
            return "";
        }
    }
}
