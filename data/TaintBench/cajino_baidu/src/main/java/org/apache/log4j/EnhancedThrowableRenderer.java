package org.apache.log4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.spi.ThrowableRenderer;

public final class EnhancedThrowableRenderer implements ThrowableRenderer {
    static Class class$java$lang$Throwable;
    private Method getClassNameMethod;
    private Method getStackTraceMethod;

    public EnhancedThrowableRenderer() {
        try {
            Class class$;
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
            } else {
                class$ = class$java$lang$Throwable;
            }
            this.getStackTraceMethod = class$.getMethod("getStackTrace", null);
            this.getClassNameMethod = Class.forName("java.lang.StackTraceElement").getMethod("getClassName", null);
        } catch (Exception e) {
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public String[] doRender(Throwable throwable) {
        if (this.getStackTraceMethod != null) {
            try {
                Object[] elements = (Object[]) this.getStackTraceMethod.invoke(throwable, null);
                String[] strArr = new String[(elements.length + 1)];
                strArr[0] = throwable.toString();
                Map classMap = new HashMap();
                for (int i = 0; i < elements.length; i++) {
                    strArr[i + 1] = formatElement(elements[i], classMap);
                }
                return strArr;
            } catch (Exception e) {
            }
        }
        return DefaultThrowableRenderer.render(throwable);
    }

    private String formatElement(Object element, Map classMap) {
        StringBuffer buf = new StringBuffer("\tat ");
        buf.append(element);
        try {
            String className = this.getClassNameMethod.invoke(element, (Object[]) null).toString();
            Object classDetails = classMap.get(className);
            if (classDetails != null) {
                buf.append(classDetails);
            } else {
                Class cls = findClass(className);
                int detailStart = buf.length();
                buf.append('[');
                try {
                    CodeSource source = cls.getProtectionDomain().getCodeSource();
                    if (source != null) {
                        URL locationURL = source.getLocation();
                        if (locationURL != null) {
                            if ("file".equals(locationURL.getProtocol())) {
                                String path = locationURL.getPath();
                                if (path != null) {
                                    int lastSlash = path.lastIndexOf(47);
                                    int lastBack = path.lastIndexOf(File.separatorChar);
                                    if (lastBack > lastSlash) {
                                        lastSlash = lastBack;
                                    }
                                    if (lastSlash <= 0 || lastSlash == path.length() - 1) {
                                        buf.append(locationURL);
                                    } else {
                                        buf.append(path.substring(lastSlash + 1));
                                    }
                                }
                            } else {
                                buf.append(locationURL);
                            }
                        }
                    }
                } catch (SecurityException e) {
                }
                buf.append(':');
                Package pkg = cls.getPackage();
                if (pkg != null) {
                    String implVersion = pkg.getImplementationVersion();
                    if (implVersion != null) {
                        buf.append(implVersion);
                    }
                }
                buf.append(']');
                classMap.put(className, buf.substring(detailStart));
            }
        } catch (Exception e2) {
        }
        return buf.toString();
    }

    private Class findClass(String className) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e2) {
                return getClass().getClassLoader().loadClass(className);
            }
        }
    }
}
