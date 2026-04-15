package org.apache.log4j.helpers;

public class LogLog {
    public static final String CONFIG_DEBUG_KEY = "log4j.configDebug";
    public static final String DEBUG_KEY = "log4j.debug";
    private static final String ERR_PREFIX = "log4j:ERROR ";
    private static final String PREFIX = "log4j: ";
    private static final String WARN_PREFIX = "log4j:WARN ";
    protected static boolean debugEnabled;
    private static boolean quietMode = false;

    static {
        debugEnabled = false;
        String key = OptionConverter.getSystemProperty(DEBUG_KEY, null);
        if (key == null) {
            key = OptionConverter.getSystemProperty(CONFIG_DEBUG_KEY, null);
        }
        if (key != null) {
            debugEnabled = OptionConverter.toBoolean(key, true);
        }
    }

    public static void setInternalDebugging(boolean enabled) {
        debugEnabled = enabled;
    }

    public static void debug(String msg) {
        if (debugEnabled && !quietMode) {
            System.out.println(new StringBuffer().append(PREFIX).append(msg).toString());
        }
    }

    public static void debug(String msg, Throwable t) {
        if (debugEnabled && !quietMode) {
            System.out.println(new StringBuffer().append(PREFIX).append(msg).toString());
            if (t != null) {
                t.printStackTrace(System.out);
            }
        }
    }

    public static void error(String msg) {
        if (!quietMode) {
            System.err.println(new StringBuffer().append(ERR_PREFIX).append(msg).toString());
        }
    }

    public static void error(String msg, Throwable t) {
        if (!quietMode) {
            System.err.println(new StringBuffer().append(ERR_PREFIX).append(msg).toString());
            if (t != null) {
                t.printStackTrace();
            }
        }
    }

    public static void setQuietMode(boolean quietMode) {
        quietMode = quietMode;
    }

    public static void warn(String msg) {
        if (!quietMode) {
            System.err.println(new StringBuffer().append(WARN_PREFIX).append(msg).toString());
        }
    }

    public static void warn(String msg, Throwable t) {
        if (!quietMode) {
            System.err.println(new StringBuffer().append(WARN_PREFIX).append(msg).toString());
            if (t != null) {
                t.printStackTrace();
            }
        }
    }
}
