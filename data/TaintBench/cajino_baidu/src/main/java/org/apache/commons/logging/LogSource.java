package org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import org.apache.commons.logging.impl.NoOpLog;

public class LogSource {
    protected static boolean jdk14IsAvailable;
    protected static boolean log4jIsAvailable;
    protected static Constructor logImplctor = null;
    protected static Hashtable logs = new Hashtable();

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0036 A:{Catch:{ Throwable -> 0x007e }} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x005b A:{SYNTHETIC, Splitter:B:33:0x005b} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003e A:{SYNTHETIC, Splitter:B:17:0x003e} */
    static {
        /*
        r3 = 0;
        r2 = new java.util.Hashtable;
        r2.<init>();
        logs = r2;
        log4jIsAvailable = r3;
        jdk14IsAvailable = r3;
        r2 = 0;
        logImplctor = r2;
        r2 = "org.apache.log4j.Logger";
        r2 = java.lang.Class.forName(r2);	 Catch:{ Throwable -> 0x0046 }
        if (r2 == 0) goto L_0x0042;
    L_0x0017:
        r2 = 1;
        log4jIsAvailable = r2;	 Catch:{ Throwable -> 0x0046 }
    L_0x001a:
        r2 = "java.util.logging.Logger";
        r2 = java.lang.Class.forName(r2);	 Catch:{ Throwable -> 0x004e }
        if (r2 == 0) goto L_0x004a;
    L_0x0022:
        r2 = "org.apache.commons.logging.impl.Jdk14Logger";
        r2 = java.lang.Class.forName(r2);	 Catch:{ Throwable -> 0x004e }
        if (r2 == 0) goto L_0x004a;
    L_0x002a:
        r2 = 1;
        jdk14IsAvailable = r2;	 Catch:{ Throwable -> 0x004e }
    L_0x002d:
        r0 = 0;
        r2 = "org.apache.commons.logging.log";
        r0 = java.lang.System.getProperty(r2);	 Catch:{ Throwable -> 0x007e }
        if (r0 != 0) goto L_0x003c;
    L_0x0036:
        r2 = "org.apache.commons.logging.Log";
        r0 = java.lang.System.getProperty(r2);	 Catch:{ Throwable -> 0x007e }
    L_0x003c:
        if (r0 == 0) goto L_0x005b;
    L_0x003e:
        setLogImplementation(r0);	 Catch:{ Throwable -> 0x0052 }
    L_0x0041:
        return;
    L_0x0042:
        r2 = 0;
        log4jIsAvailable = r2;	 Catch:{ Throwable -> 0x0046 }
        goto L_0x001a;
    L_0x0046:
        r1 = move-exception;
        log4jIsAvailable = r3;
        goto L_0x001a;
    L_0x004a:
        r2 = 0;
        jdk14IsAvailable = r2;	 Catch:{ Throwable -> 0x004e }
        goto L_0x002d;
    L_0x004e:
        r1 = move-exception;
        jdk14IsAvailable = r3;
        goto L_0x002d;
    L_0x0052:
        r1 = move-exception;
        r2 = "org.apache.commons.logging.impl.NoOpLog";
        setLogImplementation(r2);	 Catch:{ Throwable -> 0x0059 }
        goto L_0x0041;
    L_0x0059:
        r2 = move-exception;
        goto L_0x0041;
    L_0x005b:
        r2 = log4jIsAvailable;	 Catch:{ Throwable -> 0x0065 }
        if (r2 == 0) goto L_0x006e;
    L_0x005f:
        r2 = "org.apache.commons.logging.impl.Log4JLogger";
        setLogImplementation(r2);	 Catch:{ Throwable -> 0x0065 }
        goto L_0x0041;
    L_0x0065:
        r1 = move-exception;
        r2 = "org.apache.commons.logging.impl.NoOpLog";
        setLogImplementation(r2);	 Catch:{ Throwable -> 0x006c }
        goto L_0x0041;
    L_0x006c:
        r2 = move-exception;
        goto L_0x0041;
    L_0x006e:
        r2 = jdk14IsAvailable;	 Catch:{ Throwable -> 0x0065 }
        if (r2 == 0) goto L_0x0078;
    L_0x0072:
        r2 = "org.apache.commons.logging.impl.Jdk14Logger";
        setLogImplementation(r2);	 Catch:{ Throwable -> 0x0065 }
        goto L_0x0041;
    L_0x0078:
        r2 = "org.apache.commons.logging.impl.NoOpLog";
        setLogImplementation(r2);	 Catch:{ Throwable -> 0x0065 }
        goto L_0x0041;
    L_0x007e:
        r2 = move-exception;
        goto L_0x003c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.LogSource.m850clinit():void");
    }

    private LogSource() {
    }

    public static void setLogImplementation(String classname) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException, ClassNotFoundException {
        try {
            logImplctor = Class.forName(classname).getConstructor(new Class[]{"".getClass()});
        } catch (Throwable th) {
            logImplctor = null;
        }
    }

    public static void setLogImplementation(Class logclass) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException {
        logImplctor = logclass.getConstructor(new Class[]{"".getClass()});
    }

    public static Log getInstance(String name) {
        Log log = (Log) logs.get(name);
        if (log != null) {
            return log;
        }
        log = makeNewLogInstance(name);
        logs.put(name, log);
        return log;
    }

    public static Log getInstance(Class clazz) {
        return getInstance(clazz.getName());
    }

    public static Log makeNewLogInstance(String name) {
        Log log;
        try {
            log = (Log) logImplctor.newInstance(new Object[]{name});
        } catch (Throwable th) {
            log = null;
        }
        if (log == null) {
            return new NoOpLog(name);
        }
        return log;
    }

    public static String[] getLogNames() {
        return (String[]) logs.keySet().toArray(new String[logs.size()]);
    }
}
