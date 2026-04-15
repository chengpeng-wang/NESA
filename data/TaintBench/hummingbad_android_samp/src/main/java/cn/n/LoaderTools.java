package cn.n;

import android.annotation.SuppressLint;
import android.content.Context;
import dalvik.system.DexClassLoader;

public class LoaderTools {
    public static ClassLoader CUSTOM_LOADER = null;
    public static ClassLoader ORIGINAL_LOADER;
    public static DexClassLoader dcl;
    public static DexClassLoader inLoader;
    public static DexClassLoader outLoader;

    static class MyClassLoader extends ClassLoader {
        public MyClassLoader(ClassLoader parent) {
            super(parent);
        }

        @SuppressLint({"NewApi"})
        public Class<?> loadClass(String className) throws ClassNotFoundException {
            try {
                Class<?> cc = LoaderTools.outLoader.loadClass(className);
                if (cc != null) {
                    return cc;
                }
            } catch (Exception e) {
            }
            if (LoaderTools.CUSTOM_LOADER != null) {
                try {
                    Class<?> c = LoaderTools.CUSTOM_LOADER.loadClass(className);
                    if (c != null) {
                        return c;
                    }
                } catch (ClassNotFoundException e2) {
                }
            }
            return LoaderTools.ORIGINAL_LOADER.loadClass(className);
        }
    }

    public static void getClassLoader(Context context) {
        try {
            Smith<ClassLoader> sClassLoader = new Smith(new Smith((Context) new Smith(context, StrUtils.MBASE).get(), StrUtils.MPACKAGEINFO).get(), StrUtils.MCLASSLOADER);
            ClassLoader mClassLoader = (ClassLoader) sClassLoader.get();
            ORIGINAL_LOADER = mClassLoader;
            sClassLoader.set(new MyClassLoader(mClassLoader));
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }
}
