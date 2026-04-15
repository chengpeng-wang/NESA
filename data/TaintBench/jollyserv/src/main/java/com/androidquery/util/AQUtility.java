package com.androidquery.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import com.androidquery.AQuery;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AQUtility {
    private static final int IO_BUFFER_SIZE = 4096;
    private static File cacheDir;
    private static Context context;
    private static boolean debug = false;
    private static UncaughtExceptionHandler eh;
    private static Handler handler;
    private static File pcacheDir;
    private static ScheduledExecutorService storeExe;
    private static Map<String, Long> times = new HashMap();
    private static Object wait;

    public static void setDebug(boolean debug) {
        debug = debug;
    }

    public static void debugWait(long time) {
        if (debug) {
            if (wait == null) {
                wait = new Object();
            }
            synchronized (wait) {
                try {
                    wait.wait(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        return;
    }

    public static void debugNotify() {
        if (debug && wait != null) {
            synchronized (wait) {
                wait.notifyAll();
            }
        }
    }

    public static void debug(Object msg) {
        if (debug) {
            Log.w("AQuery", msg);
        }
    }

    public static void warn(Object msg, Object msg2) {
        Log.w("AQuery", msg + ":" + msg2);
    }

    public static void debug(Object msg, Object msg2) {
        if (debug) {
            Log.w("AQuery", msg + ":" + msg2);
        }
    }

    public static void debug(Throwable e) {
        if (debug) {
            Log.w("AQuery", Log.getStackTraceString(e));
        }
    }

    public static void report(Throwable e) {
        if (e != null) {
            try {
                warn("reporting", Log.getStackTraceString(e));
                if (eh != null) {
                    eh.uncaughtException(Thread.currentThread(), e);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setExceptionHandler(UncaughtExceptionHandler handler) {
        eh = handler;
    }

    public static void time(String tag) {
        times.put(tag, Long.valueOf(System.currentTimeMillis()));
    }

    public static long timeEnd(String tag, long threshold) {
        Long old = (Long) times.get(tag);
        if (old == null) {
            return 0;
        }
        long diff = System.currentTimeMillis() - old.longValue();
        if (threshold != 0 && diff <= threshold) {
            return diff;
        }
        debug(tag, Long.valueOf(diff));
        return diff;
    }

    public static Object invokeHandler(Object handler, String callback, boolean fallback, boolean report, Class<?>[] cls, Object... params) {
        return invokeHandler(handler, callback, fallback, report, cls, null, params);
    }

    public static Object invokeHandler(Object handler, String callback, boolean fallback, boolean report, Class<?>[] cls, Class<?>[] cls2, Object... params) {
        try {
            return invokeMethod(handler, callback, fallback, cls, cls2, params);
        } catch (Exception e) {
            if (report) {
                report(e);
            } else {
                debug(e);
            }
            return null;
        }
    }

    private static Object invokeMethod(Object handler, String callback, boolean fallback, Class<?>[] cls, Class<?>[] cls2, Object... params) throws Exception {
        if (handler == null || callback == null) {
            return null;
        }
        if (cls == null) {
            try {
                cls = new Class[0];
            } catch (NoSuchMethodException e) {
                if (!fallback) {
                    return null;
                }
                if (cls2 != null) {
                    return handler.getClass().getMethod(callback, cls2).invoke(handler, params);
                }
                try {
                    return handler.getClass().getMethod(callback, new Class[0]).invoke(handler, new Object[0]);
                } catch (NoSuchMethodException e2) {
                    return null;
                }
            }
        }
        return handler.getClass().getMethod(callback, cls).invoke(handler, params);
    }

    public static void transparent(View view, boolean transparent) {
        float alpha = 1.0f;
        if (transparent) {
            alpha = 0.5f;
        }
        setAlpha(view, alpha);
    }

    private static void setAlpha(View view, float alphaValue) {
        if (alphaValue == 1.0f) {
            view.clearAnimation();
            return;
        }
        AlphaAnimation alpha = new AlphaAnimation(alphaValue, alphaValue);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        view.startAnimation(alpha);
    }

    public static void ensureUIThread() {
        if (!isUIThread()) {
            report(new IllegalStateException("Not UI Thread"));
        }
    }

    public static boolean isUIThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    public static Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    public static void post(Runnable run) {
        getHandler().post(run);
    }

    public static void post(Object handler, String method) {
        post(handler, method, new Class[0], new Object[0]);
    }

    public static void post(final Object handler, final String method, final Class<?>[] sig, final Object... params) {
        post(new Runnable() {
            public void run() {
                AQUtility.invokeHandler(handler, method, false, true, sig, params);
            }
        });
    }

    public static void postAsync(Object handler, String method) {
        postAsync(handler, method, new Class[0], new Object[0]);
    }

    public static void postAsync(final Object handler, final String method, final Class<?>[] sig, final Object... params) {
        getFileStoreExecutor().execute(new Runnable() {
            public void run() {
                AQUtility.invokeHandler(handler, method, false, true, sig, params);
            }
        });
    }

    public static void removePost(Runnable run) {
        getHandler().removeCallbacks(run);
    }

    public static void postDelayed(Runnable run, long delay) {
        getHandler().postDelayed(run, delay);
    }

    public static void apply(Editor editor) {
        if (AQuery.SDK_INT >= 9) {
            invokeHandler(editor, "apply", false, true, null, null);
            return;
        }
        editor.commit();
    }

    private static String getMD5Hex(String str) {
        return new BigInteger(getMD5(str.getBytes())).abs().toString(36);
    }

    private static byte[] getMD5(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(data);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            report(e);
            return null;
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, 0, null);
    }

    public static void copy(InputStream in, OutputStream out, int max, Progress progress) throws IOException {
        debug("content header", Integer.valueOf(max));
        if (progress != null) {
            progress.reset();
            progress.setBytes(max);
        }
        byte[] b = new byte[4096];
        while (true) {
            int read = in.read(b);
            if (read == -1) {
                break;
            }
            out.write(b, 0, read);
            if (progress != null) {
                progress.increment(read);
            }
        }
        if (progress != null) {
            progress.done();
        }
    }

    public static byte[] toBytes(InputStream is) {
        byte[] result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            copy(is, baos);
            result = baos.toByteArray();
        } catch (IOException e) {
            report(e);
        }
        close(is);
        return result;
    }

    public static void write(File file, byte[] data) {
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    debug("file create fail", file);
                    report(e);
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (Exception e2) {
            report(e2);
        }
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
            }
        }
    }

    private static ScheduledExecutorService getFileStoreExecutor() {
        if (storeExe == null) {
            storeExe = Executors.newSingleThreadScheduledExecutor();
        }
        return storeExe;
    }

    public static void storeAsync(File file, byte[] data, long delay) {
        getFileStoreExecutor().schedule(new Common().method(1, file, data), delay, TimeUnit.MILLISECONDS);
    }

    public static File getCacheDir(Context context, int policy) {
        if (policy != 1) {
            return getCacheDir(context);
        }
        if (pcacheDir != null) {
            return pcacheDir;
        }
        pcacheDir = new File(getCacheDir(context), "persistent");
        pcacheDir.mkdirs();
        return pcacheDir;
    }

    public static File getCacheDir(Context context) {
        if (cacheDir == null) {
            cacheDir = new File(context.getCacheDir(), "aquery");
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public static void setCacheDir(File dir) {
        cacheDir = dir;
        if (cacheDir != null) {
            cacheDir.mkdirs();
        }
    }

    private static File makeCacheFile(File dir, String name) {
        return new File(dir, name);
    }

    private static String getCacheFileName(String url) {
        return getMD5Hex(url);
    }

    public static File getCacheFile(File dir, String url) {
        if (url == null) {
            return null;
        }
        if (url.startsWith(File.separator)) {
            return new File(url);
        }
        return makeCacheFile(dir, getCacheFileName(url));
    }

    public static File getExistedCacheByUrl(File dir, String url) {
        File file = getCacheFile(dir, url);
        if (file == null || !file.exists()) {
            return null;
        }
        return file;
    }

    public static File getExistedCacheByUrlSetAccess(File dir, String url) {
        File file = getExistedCacheByUrl(dir, url);
        if (file != null) {
            lastAccess(file);
        }
        return file;
    }

    private static void lastAccess(File file) {
        file.setLastModified(System.currentTimeMillis());
    }

    public static void store(File file, byte[] data) {
        if (file != null) {
            try {
                write(file, data);
            } catch (Exception e) {
                report(e);
            }
        }
    }

    public static void cleanCacheAsync(Context context) {
        cleanCacheAsync(context, 3000000, 2000000);
    }

    public static void cleanCacheAsync(Context context, long triggerSize, long targetSize) {
        try {
            File cacheDir = getCacheDir(context);
            getFileStoreExecutor().schedule(new Common().method(2, cacheDir, Long.valueOf(triggerSize), Long.valueOf(targetSize)), 0, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            report(e);
        }
    }

    public static void cleanCache(File cacheDir, long triggerSize, long targetSize) {
        try {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                Arrays.sort(files, new Common());
                if (testCleanNeeded(files, triggerSize)) {
                    cleanCache(files, targetSize);
                }
                File temp = getTempDir();
                if (temp != null && temp.exists()) {
                    cleanCache(temp.listFiles(), 0);
                }
            }
        } catch (Exception e) {
            report(e);
        }
    }

    public static File getTempDir() {
        File tempDir = new File(Environment.getExternalStorageDirectory(), "aquery/temp");
        tempDir.mkdirs();
        if (tempDir.exists()) {
            return tempDir;
        }
        return null;
    }

    private static boolean testCleanNeeded(File[] files, long triggerSize) {
        long total = 0;
        for (File f : files) {
            total += f.length();
            if (total > triggerSize) {
                return true;
            }
        }
        return false;
    }

    private static void cleanCache(File[] files, long maxSize) {
        long total = 0;
        int deletes = 0;
        for (File f : files) {
            if (f.isFile()) {
                total += f.length();
                if (total >= maxSize) {
                    f.delete();
                    deletes++;
                }
            }
        }
        debug("deleted", Integer.valueOf(deletes));
    }

    public static int dip2pixel(Context context, float n) {
        return (int) TypedValue.applyDimension(1, n, context.getResources().getDisplayMetrics());
    }

    public static void setContext(Application app) {
        context = app.getApplicationContext();
    }

    public static Context getContext() {
        if (context == null) {
            warn("warn", "getContext with null");
            debug(new IllegalStateException());
        }
        return context;
    }
}
