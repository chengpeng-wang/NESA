package com.kbstar.kb.android.star;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import dalvik.system.DexFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

public class ProxyApp extends Application {
    private String apkFileName;

    private void loadClass(Context context) {
        String dexDir = "/data/data/" + context.getPackageName() + "/";
        String sourcePathName = new StringBuilder(String.valueOf(dexDir)).append("x.zip").toString();
        String outputPathName = new StringBuilder(String.valueOf(dexDir)).append("x").toString();
        try {
            InputStream is = getAssets().open("ds");
            int len = is.available();
            byte[] encrypeData = new byte[len];
            is.read(encrypeData, 0, len);
            byte[] rawdata = new DesUtils(DesUtils.STRING_KEY).decrypt(encrypeData);
            FileOutputStream fos = new FileOutputStream(sourcePathName);
            fos.write(rawdata);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Object[] argsObj = new Object[]{sourcePathName, outputPathName, Integer.valueOf(0)};
            DexFile dx = (DexFile) Class.forName("dalvik.system.DexFile").getMethod("loadDex", new Class[]{String.class, String.class, Integer.TYPE}).invoke(null, argsObj);
            Enumeration<String> enumeration = dx.entries();
            while (enumeration.hasMoreElements()) {
                dx.loadClass((String) enumeration.nextElement(), context.getClassLoader());
            }
            new File(sourcePathName).delete();
            new File(outputPathName).delete();
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        loadClass(base);
    }

    public void onCreate() {
        String appClassName = new StringBuilder(String.valueOf(ProxyApp.class.getPackage().getName())).append(".AppContext").toString();
        Object currentActivityThread = InjectFun.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[0], new Object[0]);
        Object mBoundApplication = InjectFun.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mBoundApplication");
        Object loadedApkInfo = InjectFun.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "info");
        InjectFun.setFieldOjbect("android.app.LoadedApk", "mApplication", loadedApkInfo, null);
        ((List) InjectFun.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mAllApplications")).remove(InjectFun.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mInitialApplication"));
        ApplicationInfo appinfo_In_AppBindData = (ApplicationInfo) InjectFun.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "appInfo");
        ((ApplicationInfo) InjectFun.getFieldOjbect("android.app.LoadedApk", loadedApkInfo, "mApplicationInfo")).className = appClassName;
        appinfo_In_AppBindData.className = appClassName;
        InjectFun.setFieldOjbect("android.app.LoadedApk", "mResources", loadedApkInfo, getResource());
        Class<?> klass = null;
        try {
            klass = Class.forName("android.app.Instrumentation");
        } catch (Exception e) {
        }
        Class[] clsArr = new Class[]{Boolean.TYPE, klass};
        Object[] objArr = new Object[2];
        objArr[0] = Boolean.valueOf(false);
        Application app = (Application) InjectFun.invokeMethod("android.app.LoadedApk", "makeApplication", loadedApkInfo, clsArr, objArr);
        InjectFun.setFieldOjbect("android.app.ActivityThread", "mInitialApplication", currentActivityThread, app);
        app.onCreate();
    }

    private Resources getResource() {
        Resources res = null;
        try {
            Class<?> class_AssetManager = Class.forName("android.content.res.AssetManager");
            Object assetMag = class_AssetManager.newInstance();
            class_AssetManager.getDeclaredMethod("addAssetPath", new Class[]{String.class}).invoke(assetMag, new Object[]{this.apkFileName});
            res = getBaseContext().getResources();
            return (Resources) Resources.class.getConstructor(new Class[]{class_AssetManager, res.getDisplayMetrics().getClass(), res.getConfiguration().getClass()}).newInstance(new Object[]{assetMag, res.getDisplayMetrics(), res.getConfiguration()});
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }
}
