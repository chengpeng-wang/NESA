package com.cleanmaster.func.process;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import com.cleanmaster.util.n;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;

/* compiled from: ProcessInfoHelperImpl */
public class c {
    private static long a = -1;
    private static Method b = null;
    private static Method c = null;

    public static long a() {
        n nVar = new n();
        nVar.a();
        long c = nVar.c() + nVar.d();
        long b = nVar.b();
        if (0 < b && a < b && b < 107374182400L) {
            a = nVar.b();
        }
        if (c > 0 && a > c) {
            return c;
        }
        MemoryInfo a = a(com.picksinit.c.getInstance().getContext());
        if (a == null || 0 >= a.availMem || a.availMem >= a) {
            return (long) (((float) a) * 0.15f);
        }
        return a.availMem;
    }

    public static long b() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String readLine = bufferedReader.readLine();
            long intValue = (long) Integer.valueOf(readLine.substring(readLine.indexOf(":") + 1, readLine.indexOf("kB")).trim()).intValue();
            bufferedReader.close();
            long j = intValue * 1024;
            if (0 >= j || a >= j || j >= 107374182400L) {
                return j;
            }
            a = j;
            return j;
        } catch (Exception e) {
            if (a < 0) {
                return 1;
            }
            return a;
        }
    }

    private static MemoryInfo a(Context context) {
        if (context == null) {
            return null;
        }
        MemoryInfo memoryInfo = new MemoryInfo();
        try {
            ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
            return memoryInfo;
        } catch (SecurityException e) {
            return memoryInfo;
        }
    }
}
