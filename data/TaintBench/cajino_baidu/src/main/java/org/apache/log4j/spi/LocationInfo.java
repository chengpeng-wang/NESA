package org.apache.log4j.spi;

import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;

public class LocationInfo implements Serializable {
    public static final String NA = "?";
    public static final LocationInfo NA_LOCATION_INFO = new LocationInfo(NA, NA, NA, NA);
    static Class class$java$lang$Throwable = null;
    private static Method getClassNameMethod = null;
    private static Method getFileNameMethod = null;
    private static Method getLineNumberMethod = null;
    private static Method getMethodNameMethod = null;
    private static Method getStackTraceMethod = null;
    static boolean inVisualAge = false;
    private static PrintWriter pw = new PrintWriter(sw);
    static final long serialVersionUID = -1325822038990805636L;
    private static StringWriter sw = new StringWriter();
    transient String className;
    transient String fileName;
    public String fullInfo;
    transient String lineNumber;
    transient String methodName;

    static {
        boolean z = false;
        inVisualAge = false;
        try {
            if (Class.forName("com.ibm.uvm.tools.DebugSupport") != null) {
                z = true;
            }
            inVisualAge = z;
            LogLog.debug("Detected IBM VisualAge environment.");
        } catch (Throwable th) {
        }
        try {
            Class class$;
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
            } else {
                class$ = class$java$lang$Throwable;
            }
            getStackTraceMethod = class$.getMethod("getStackTrace", null);
            Class stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
            getClassNameMethod = stackTraceElementClass.getMethod("getClassName", null);
            getMethodNameMethod = stackTraceElementClass.getMethod("getMethodName", null);
            getFileNameMethod = stackTraceElementClass.getMethod("getFileName", null);
            getLineNumberMethod = stackTraceElementClass.getMethod("getLineNumber", null);
        } catch (ClassNotFoundException e) {
            LogLog.debug("LocationInfo will use pre-JDK 1.4 methods to determine location.");
        } catch (NoSuchMethodException e2) {
            LogLog.debug("LocationInfo will use pre-JDK 1.4 methods to determine location.");
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public LocationInfo(Throwable t, String fqnOfCallingClass) {
        if (t != null && fqnOfCallingClass != null) {
            int i;
            String s;
            if (getLineNumberMethod != null) {
                try {
                    Object[] elements = (Object[]) getStackTraceMethod.invoke(t, null);
                    String prevClass = NA;
                    for (i = elements.length - 1; i >= 0; i--) {
                        String thisClass = (String) getClassNameMethod.invoke(elements[i], null);
                        if (fqnOfCallingClass.equals(thisClass)) {
                            int caller = i + 1;
                            if (caller < elements.length) {
                                this.className = prevClass;
                                this.methodName = (String) getMethodNameMethod.invoke(elements[caller], null);
                                this.fileName = (String) getFileNameMethod.invoke(elements[caller], null);
                                if (this.fileName == null) {
                                    this.fileName = NA;
                                }
                                int line = ((Integer) getLineNumberMethod.invoke(elements[caller], null)).intValue();
                                if (line < 0) {
                                    this.lineNumber = NA;
                                } else {
                                    this.lineNumber = String.valueOf(line);
                                }
                                StringBuffer buf = new StringBuffer();
                                buf.append(this.className);
                                buf.append(".");
                                buf.append(this.methodName);
                                buf.append("(");
                                buf.append(this.fileName);
                                buf.append(":");
                                buf.append(this.lineNumber);
                                buf.append(")");
                                this.fullInfo = buf.toString();
                                return;
                            }
                            return;
                        }
                        prevClass = thisClass;
                    }
                    return;
                } catch (IllegalAccessException ex) {
                    LogLog.debug("LocationInfo failed using JDK 1.4 methods", ex);
                } catch (InvocationTargetException ex2) {
                    if ((ex2.getTargetException() instanceof InterruptedException) || (ex2.getTargetException() instanceof InterruptedIOException)) {
                        Thread.currentThread().interrupt();
                    }
                    LogLog.debug("LocationInfo failed using JDK 1.4 methods", ex2);
                } catch (RuntimeException ex3) {
                    LogLog.debug("LocationInfo failed using JDK 1.4 methods", ex3);
                }
            }
            synchronized (sw) {
                t.printStackTrace(pw);
                s = sw.toString();
                sw.getBuffer().setLength(0);
            }
            int ibegin = s.lastIndexOf(fqnOfCallingClass);
            if (ibegin != -1) {
                if (fqnOfCallingClass.length() + ibegin < s.length() && s.charAt(fqnOfCallingClass.length() + ibegin) != '.') {
                    i = s.lastIndexOf(new StringBuffer().append(fqnOfCallingClass).append(".").toString());
                    if (i != -1) {
                        ibegin = i;
                    }
                }
                ibegin = s.indexOf(Layout.LINE_SEP, ibegin);
                if (ibegin != -1) {
                    ibegin += Layout.LINE_SEP_LEN;
                    int iend = s.indexOf(Layout.LINE_SEP, ibegin);
                    if (iend != -1) {
                        if (!inVisualAge) {
                            ibegin = s.lastIndexOf("at ", iend);
                            if (ibegin != -1) {
                                ibegin += 3;
                            } else {
                                return;
                            }
                        }
                        this.fullInfo = s.substring(ibegin, iend);
                    }
                }
            }
        }
    }

    private static final void appendFragment(StringBuffer buf, String fragment) {
        if (fragment == null) {
            buf.append(NA);
        } else {
            buf.append(fragment);
        }
    }

    public LocationInfo(String file, String classname, String method, String line) {
        this.fileName = file;
        this.className = classname;
        this.methodName = method;
        this.lineNumber = line;
        StringBuffer buf = new StringBuffer();
        appendFragment(buf, classname);
        buf.append(".");
        appendFragment(buf, method);
        buf.append("(");
        appendFragment(buf, file);
        buf.append(":");
        appendFragment(buf, line);
        buf.append(")");
        this.fullInfo = buf.toString();
    }

    public String getClassName() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.className == null) {
            int iend = this.fullInfo.lastIndexOf(40);
            if (iend == -1) {
                this.className = NA;
            } else {
                iend = this.fullInfo.lastIndexOf(46, iend);
                int ibegin = 0;
                if (inVisualAge) {
                    ibegin = this.fullInfo.lastIndexOf(32, iend) + 1;
                }
                if (iend == -1) {
                    this.className = NA;
                } else {
                    this.className = this.fullInfo.substring(ibegin, iend);
                }
            }
        }
        return this.className;
    }

    public String getFileName() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.fileName == null) {
            int iend = this.fullInfo.lastIndexOf(58);
            if (iend == -1) {
                this.fileName = NA;
            } else {
                this.fileName = this.fullInfo.substring(this.fullInfo.lastIndexOf(40, iend - 1) + 1, iend);
            }
        }
        return this.fileName;
    }

    public String getLineNumber() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.lineNumber == null) {
            int iend = this.fullInfo.lastIndexOf(41);
            int ibegin = this.fullInfo.lastIndexOf(58, iend - 1);
            if (ibegin == -1) {
                this.lineNumber = NA;
            } else {
                this.lineNumber = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return this.lineNumber;
    }

    public String getMethodName() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.methodName == null) {
            int iend = this.fullInfo.lastIndexOf(40);
            int ibegin = this.fullInfo.lastIndexOf(46, iend);
            if (ibegin == -1) {
                this.methodName = NA;
            } else {
                this.methodName = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return this.methodName;
    }
}
