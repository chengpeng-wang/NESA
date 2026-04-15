package org.apache.log4j.helpers;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

public class Loader {
    static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
    static Class class$java$lang$Thread;
    static Class class$org$apache$log4j$helpers$Loader;
    private static boolean ignoreTCL;
    private static boolean java1;

    static {
        java1 = true;
        ignoreTCL = false;
        String prop = OptionConverter.getSystemProperty("java.version", null);
        if (prop != null) {
            int i = prop.indexOf(46);
            if (!(i == -1 || prop.charAt(i + 1) == '1')) {
                java1 = false;
            }
        }
        String ignoreTCLProp = OptionConverter.getSystemProperty("log4j.ignoreTCL", null);
        if (ignoreTCLProp != null) {
            ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
        }
    }

    public static URL getResource(String resource, Class clazz) {
        return getResource(resource);
    }

    public static URL getResource(String resource) {
        try {
            ClassLoader classLoader;
            URL url;
            Class class$;
            if (!(java1 || ignoreTCL)) {
                classLoader = getTCL();
                if (classLoader != null) {
                    LogLog.debug(new StringBuffer().append("Trying to find [").append(resource).append("] using context classloader ").append(classLoader).append(".").toString());
                    url = classLoader.getResource(resource);
                    if (url != null) {
                        return url;
                    }
                }
            }
            if (class$org$apache$log4j$helpers$Loader == null) {
                class$ = class$("org.apache.log4j.helpers.Loader");
                class$org$apache$log4j$helpers$Loader = class$;
            } else {
                class$ = class$org$apache$log4j$helpers$Loader;
            }
            classLoader = class$.getClassLoader();
            if (classLoader != null) {
                LogLog.debug(new StringBuffer().append("Trying to find [").append(resource).append("] using ").append(classLoader).append(" class loader.").toString());
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        } catch (IllegalAccessException t) {
            LogLog.warn(TSTR, t);
        } catch (InvocationTargetException t2) {
            if ((t2.getTargetException() instanceof InterruptedException) || (t2.getTargetException() instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.warn(TSTR, t2);
        } catch (Throwable t3) {
            LogLog.warn(TSTR, t3);
        }
        LogLog.debug(new StringBuffer().append("Trying to find [").append(resource).append("] using ClassLoader.getSystemResource().").toString());
        return ClassLoader.getSystemResource(resource);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static boolean isJava1() {
        return java1;
    }

    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        try {
            Class class$;
            if (class$java$lang$Thread == null) {
                class$ = class$("java.lang.Thread");
                class$java$lang$Thread = class$;
            } else {
                class$ = class$java$lang$Thread;
            }
            return (ClassLoader) class$.getMethod("getContextClassLoader", null).invoke(Thread.currentThread(), null);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Class loadClass(String clazz) throws ClassNotFoundException {
        if (java1 || ignoreTCL) {
            return Class.forName(clazz);
        }
        try {
            return getTCL().loadClass(clazz);
        } catch (InvocationTargetException e) {
            if ((e.getTargetException() instanceof InterruptedException) || (e.getTargetException() instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
        } catch (Throwable th) {
        }
        return Class.forName(clazz);
    }
}
