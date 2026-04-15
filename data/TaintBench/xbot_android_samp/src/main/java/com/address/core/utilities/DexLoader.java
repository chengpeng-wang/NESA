package com.address.core.utilities;

import com.address.core.RunService;
import dalvik.system.DexClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DexLoader {
    public static Class getExternalClass(String apkPath, String classPath) {
        Class cls = null;
        try {
            return new DexClassLoader(apkPath, RunService.getService().getExternalCacheDir().getAbsolutePath(), cls, RunService.getService().getClassLoader()).loadClass(classPath);
        } catch (Exception e) {
            return cls;
        }
    }

    public static Object getExternalObject(String apkPath, String classPath) {
        Object obj = null;
        Class clazz = getExternalClass(apkPath, classPath);
        if (clazz == null) {
            return obj;
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(DexLoader.class.getName()).log(Level.SEVERE, obj, ex);
            return obj;
        } catch (IllegalAccessException ex2) {
            Logger.getLogger(DexLoader.class.getName()).log(Level.SEVERE, obj, ex2);
            return obj;
        }
    }
}
