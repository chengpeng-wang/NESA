package com.baidu.android.pushservice.b;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.os.EnvironmentCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.baidu.android.common.net.ConnectManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class m {
    public static double a = 0.5d;
    public static double b = 0.2d;
    public static double c = 0.2d;
    public static double d = 0.1d;

    public static n a() {
        String str;
        try {
            byte[] bArr = new byte[1024];
            new RandomAccessFile("/proc/cpuinfo", "r").read(bArr);
            str = new String(bArr);
            int indexOf = str.indexOf(0);
            if (indexOf != -1) {
                str = str.substring(0, indexOf);
            }
        } catch (IOException e) {
            IOException iOException = e;
            str = "";
            iOException.printStackTrace();
        }
        n a = a(str);
        a.e = (long) e();
        return a;
    }

    private static n a(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        n nVar = new n();
        nVar.a = 0;
        nVar.c = 0;
        nVar.b = 1;
        nVar.d = 0.0d;
        if (str.contains("ARMv5")) {
            nVar.a = 1;
        } else if (str.contains("ARMv6")) {
            nVar.a = 16;
        } else if (str.contains("ARMv7")) {
            nVar.a = 256;
        }
        if (str.contains("neon")) {
            nVar.c |= 256;
        }
        if (str.contains("vfpv3")) {
            nVar.c |= 16;
        }
        if (str.contains(" vfp")) {
            nVar.c |= 1;
        }
        for (String str2 : str.split("\n")) {
            int indexOf;
            if (str2.contains("CPU variant")) {
                indexOf = str2.indexOf(": ");
                if (indexOf >= 0) {
                    try {
                        nVar.b = Integer.decode(str2.substring(indexOf + 2)).intValue();
                        nVar.b = nVar.b == 0 ? 1 : nVar.b;
                    } catch (NumberFormatException e) {
                        nVar.b = 1;
                    }
                }
            } else if (str2.contains("BogoMIPS")) {
                indexOf = str2.indexOf(": ");
                if (indexOf >= 0) {
                    str2.substring(indexOf + 2);
                }
            }
        }
        return nVar;
    }

    public static String a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuilder.append(readLine);
                } else {
                    try {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                try {
                    inputStream.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                throw th;
            }
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    public static int[] a(Context context) {
        int[] iArr = new int[3];
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            iArr[0] = 0;
            iArr[1] = 0;
            iArr[2] = 0;
            return iArr;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        if (i >= i2) {
            int i3 = i2;
            i2 = i;
            i = i3;
        }
        iArr[0] = i2;
        iArr[1] = i;
        iArr[2] = displayMetrics.densityDpi;
        return iArr;
    }

    public static long b() {
        long j = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                j = (long) (Integer.valueOf(readLine.split("\\s+")[1]).intValue() / 1024);
            }
            bufferedReader.close();
            return j;
        } catch (IOException e) {
            return -1;
        }
    }

    public static String b(Context context) {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            String str = null;
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    str = !inetAddress.isLoopbackAddress() ? inetAddress.getHostAddress().toString() : str;
                }
            }
            return str;
        } catch (SocketException e) {
            return null;
        }
    }

    public static String c() {
        String str = "";
        n a = a();
        return (a.a & 1) == 1 ? "armv5" : (a.a & 16) == 16 ? "armv6" : (a.a & 256) == 256 ? "armv7" : EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public static boolean c(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static String d() {
        String str = "";
        n a = a();
        return (a.c & 256) == 256 ? "neon" : (a.c & 1) == 1 ? "vfp" : (a.c & 16) == 16 ? "vfpv3" : EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public static String d(Context context) {
        ConnectManager connectManager = new ConnectManager(context);
        if (!ConnectManager.isNetworkConnected(context)) {
            return "connectionless";
        }
        String netType = connectManager.getNetType();
        return netType == null ? "connectionless" : netType;
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x0069 A:{SYNTHETIC, Splitter:B:51:0x0069} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x006e A:{SYNTHETIC, Splitter:B:54:0x006e} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0038 A:{SYNTHETIC, Splitter:B:25:0x0038} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x003d A:{SYNTHETIC, Splitter:B:28:0x003d} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0052 A:{SYNTHETIC, Splitter:B:39:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0057 A:{SYNTHETIC, Splitter:B:42:0x0057} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0038 A:{SYNTHETIC, Splitter:B:25:0x0038} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x003d A:{SYNTHETIC, Splitter:B:28:0x003d} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0052 A:{SYNTHETIC, Splitter:B:39:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0057 A:{SYNTHETIC, Splitter:B:42:0x0057} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0069 A:{SYNTHETIC, Splitter:B:51:0x0069} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x006e A:{SYNTHETIC, Splitter:B:54:0x006e} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0069 A:{SYNTHETIC, Splitter:B:51:0x0069} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x006e A:{SYNTHETIC, Splitter:B:54:0x006e} */
    private static int e() {
        /*
        r3 = 0;
        r0 = 0;
        r4 = new java.io.FileReader;	 Catch:{ FileNotFoundException -> 0x0031, IOException -> 0x004b, all -> 0x0065 }
        r1 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
        r4.<init>(r1);	 Catch:{ FileNotFoundException -> 0x0031, IOException -> 0x004b, all -> 0x0065 }
        r2 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x008a, IOException -> 0x0085 }
        r2.<init>(r4);	 Catch:{ FileNotFoundException -> 0x008a, IOException -> 0x0085 }
        r1 = r2.readLine();	 Catch:{ FileNotFoundException -> 0x008e, IOException -> 0x0087, all -> 0x007e }
        if (r1 == 0) goto L_0x001c;
    L_0x0014:
        r1 = r1.trim();	 Catch:{ FileNotFoundException -> 0x008e, IOException -> 0x0087, all -> 0x007e }
        r0 = java.lang.Integer.parseInt(r1);	 Catch:{ FileNotFoundException -> 0x008e, IOException -> 0x0087, all -> 0x007e }
    L_0x001c:
        if (r4 == 0) goto L_0x0021;
    L_0x001e:
        r4.close();	 Catch:{ IOException -> 0x0027 }
    L_0x0021:
        if (r2 == 0) goto L_0x0026;
    L_0x0023:
        r2.close();	 Catch:{ IOException -> 0x002c }
    L_0x0026:
        return r0;
    L_0x0027:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0021;
    L_0x002c:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0026;
    L_0x0031:
        r1 = move-exception;
        r2 = r3;
    L_0x0033:
        r1.printStackTrace();	 Catch:{ all -> 0x0081 }
        if (r3 == 0) goto L_0x003b;
    L_0x0038:
        r3.close();	 Catch:{ IOException -> 0x0046 }
    L_0x003b:
        if (r2 == 0) goto L_0x0026;
    L_0x003d:
        r2.close();	 Catch:{ IOException -> 0x0041 }
        goto L_0x0026;
    L_0x0041:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0026;
    L_0x0046:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x003b;
    L_0x004b:
        r1 = move-exception;
        r4 = r3;
    L_0x004d:
        r1.printStackTrace();	 Catch:{ all -> 0x007c }
        if (r4 == 0) goto L_0x0055;
    L_0x0052:
        r4.close();	 Catch:{ IOException -> 0x0060 }
    L_0x0055:
        if (r3 == 0) goto L_0x0026;
    L_0x0057:
        r3.close();	 Catch:{ IOException -> 0x005b }
        goto L_0x0026;
    L_0x005b:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0026;
    L_0x0060:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0055;
    L_0x0065:
        r0 = move-exception;
        r4 = r3;
    L_0x0067:
        if (r4 == 0) goto L_0x006c;
    L_0x0069:
        r4.close();	 Catch:{ IOException -> 0x0072 }
    L_0x006c:
        if (r3 == 0) goto L_0x0071;
    L_0x006e:
        r3.close();	 Catch:{ IOException -> 0x0077 }
    L_0x0071:
        throw r0;
    L_0x0072:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x006c;
    L_0x0077:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0071;
    L_0x007c:
        r0 = move-exception;
        goto L_0x0067;
    L_0x007e:
        r0 = move-exception;
        r3 = r2;
        goto L_0x0067;
    L_0x0081:
        r0 = move-exception;
        r4 = r3;
        r3 = r2;
        goto L_0x0067;
    L_0x0085:
        r1 = move-exception;
        goto L_0x004d;
    L_0x0087:
        r1 = move-exception;
        r3 = r2;
        goto L_0x004d;
    L_0x008a:
        r1 = move-exception;
        r2 = r3;
        r3 = r4;
        goto L_0x0033;
    L_0x008e:
        r1 = move-exception;
        r3 = r4;
        goto L_0x0033;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.b.m.e():int");
    }

    public static boolean e(Context context) {
        return ConnectManager.isNetworkConnected(context);
    }
}
