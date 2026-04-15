package com.baidu.mapapi.utils;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class i {
    public static String[] a;
    public static String[] b;
    public static String[] c;
    public static int d = 0;
    private static String e = "";
    private static ArrayList<String> f = new ArrayList();
    private static ArrayList<String> g = new ArrayList();

    private static String a(String str) {
        StatFs statFs = new StatFs(str);
        long availableBlocks = ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        DecimalFormat decimalFormat = new DecimalFormat();
        String str2 = "未知大小";
        if (availableBlocks < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return availableBlocks + "B";
        }
        if (availableBlocks < 1048576) {
            decimalFormat.applyPattern("0");
            return decimalFormat.format(((double) availableBlocks) / 1024.0d) + "K";
        } else if (availableBlocks < 1073741824) {
            decimalFormat.applyPattern("0.0");
            return decimalFormat.format(((double) availableBlocks) / 1048576.0d) + "M";
        } else {
            decimalFormat.applyPattern("0.0");
            return decimalFormat.format(((double) availableBlocks) / 1.073741824E9d) + "G";
        }
    }

    private static void a() {
        f.add(e);
        try {
            Scanner scanner = new Scanner(new File("/proc/mounts"));
            while (scanner.hasNext()) {
                String nextLine = scanner.nextLine();
                if (nextLine.startsWith("/dev/block/vold/")) {
                    nextLine = nextLine.replace(9, ' ').split(" ")[1];
                    if (!nextLine.equals(e)) {
                        f.add(nextLine);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(Context context) {
        e = Environment.getExternalStorageDirectory().getAbsolutePath();
        boolean z = false;
        if (VERSION.SDK_INT >= 14) {
            z = c(context);
        }
        if (!z) {
            a();
            b();
            c();
            d();
            b(context);
        }
    }

    private static void b() {
        g.add(e);
        File file = new File("/system/etc/vold.fstab");
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.startsWith("dev_mount")) {
                        nextLine = nextLine.replace(9, ' ').split(" ")[2];
                        if (nextLine.contains(":")) {
                            nextLine = nextLine.substring(0, nextLine.indexOf(":"));
                        }
                        if (!nextLine.equals(e)) {
                            g.add(nextLine);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void b(Context context) {
        int i = 1;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (f.size() > 0) {
            if (VERSION.SDK_INT < 9) {
                arrayList.add("Auto");
            } else if (Environment.isExternalStorageRemovable()) {
                arrayList.add("外置存储卡");
            } else {
                arrayList.add("内置存储卡");
            }
            arrayList2.add(a((String) f.get(0)));
            if (f.size() > 1) {
                while (i < f.size()) {
                    arrayList.add("外置存储卡");
                    arrayList2.add(a((String) f.get(i)));
                    i++;
                }
            }
        }
        i = 0;
        while (i < f.size()) {
            if (!c.a((String) f.get(i))) {
                arrayList.remove(i);
                arrayList2.remove(i);
                int i2 = i - 1;
                f.remove(i);
                i = i2;
            }
            i++;
        }
        a = new String[arrayList.size()];
        arrayList.toArray(a);
        b = new String[f.size()];
        f.toArray(b);
        c = new String[f.size()];
        arrayList2.toArray(c);
        d = Math.min(a.length, b.length);
        f.clear();
    }

    private static void c() {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < f.size()) {
                if (!g.contains((String) f.get(i2))) {
                    i = i2 - 1;
                    f.remove(i2);
                    i2 = i;
                }
                i = i2 + 1;
            } else {
                g.clear();
                return;
            }
        }
    }

    private static boolean c(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        if (storageManager != null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            try {
                int i;
                Class cls = Class.forName("android.os.storage.StorageVolume");
                Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
                Method method2 = storageManager.getClass().getMethod("getVolumeState", new Class[]{String.class});
                Method method3 = cls.getMethod("isRemovable", new Class[0]);
                Method method4 = cls.getMethod("getPath", new Class[0]);
                Object[] objArr = (Object[]) method.invoke(storageManager, new Object[0]);
                for (int i2 = 0; i2 < objArr.length; i2++) {
                    String str = (String) method4.invoke(objArr[i2], new Object[0]);
                    boolean booleanValue = ((Boolean) method3.invoke(objArr[i2], new Object[0])).booleanValue();
                    if (!(str == null || str.equals(e))) {
                        String str2 = (String) method2.invoke(storageManager, new Object[]{str});
                        if (str2 != null && str2.equals("mounted")) {
                            if (booleanValue) {
                                arrayList2.add(str);
                            } else {
                                arrayList.add(str);
                            }
                        }
                    }
                }
                for (i = 0; i < arrayList.size(); i++) {
                    f.add(arrayList.get(i));
                    arrayList3.add("内置存储卡");
                    arrayList4.add(a((String) arrayList.get(i)));
                }
                for (i = 0; i < arrayList2.size(); i++) {
                    f.add(arrayList2.get(i));
                    arrayList3.add("外置存储卡");
                    arrayList4.add(a((String) arrayList2.get(i)));
                }
                a = new String[arrayList3.size()];
                arrayList3.toArray(a);
                b = new String[f.size()];
                f.toArray(b);
                c = new String[f.size()];
                arrayList4.toArray(c);
                d = Math.min(a.length, b.length);
                f.clear();
                return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
        }
        return false;
    }

    private static void d() {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < f.size()) {
                File file = new File((String) f.get(i2));
                if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
                    i = i2 - 1;
                    f.remove(i2);
                    i2 = i;
                }
                i = i2 + 1;
            } else {
                return;
            }
        }
    }
}
