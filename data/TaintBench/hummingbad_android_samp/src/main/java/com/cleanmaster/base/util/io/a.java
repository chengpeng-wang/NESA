package com.cleanmaster.base.util.io;

import android.os.Build.VERSION;
import android.os.StatFs;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* compiled from: BlockUtils */
public final class a {
    static final /* synthetic */ boolean a = (!a.class.desiredAssertionStatus());

    public static long a(StatFs statFs) {
        if (!a && statFs == null) {
            throw new AssertionError();
        } else if (VERSION.SDK_INT < 18) {
            return (long) statFs.getAvailableBlocks();
        } else {
            Method method;
            try {
                method = statFs.getClass().getMethod("getAvailableBlocksLong", new Class[0]);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                method = null;
            }
            if (method == null) {
                return (long) statFs.getAvailableBlocks();
            }
            try {
                return ((Long) method.invoke(statFs, new Object[0])).longValue();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
        return (long) statFs.getAvailableBlocks();
    }

    public static long b(StatFs statFs) {
        if (!a && statFs == null) {
            throw new AssertionError();
        } else if (VERSION.SDK_INT < 18) {
            return (long) statFs.getBlockCount();
        } else {
            Method method;
            try {
                method = statFs.getClass().getMethod("getBlockCountLong", new Class[0]);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                method = null;
            }
            if (method == null) {
                return (long) statFs.getBlockCount();
            }
            try {
                return ((Long) method.invoke(statFs, new Object[0])).longValue();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
        return (long) statFs.getBlockCount();
    }

    public static long c(StatFs statFs) {
        if (!a && statFs == null) {
            throw new AssertionError();
        } else if (VERSION.SDK_INT < 18) {
            return (long) statFs.getBlockSize();
        } else {
            Method method;
            try {
                method = statFs.getClass().getMethod("getBlockSizeLong", new Class[0]);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                method = null;
            }
            if (method == null) {
                return (long) statFs.getBlockSize();
            }
            try {
                return ((Long) method.invoke(statFs, new Object[0])).longValue();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
        return (long) statFs.getBlockSize();
    }
}
