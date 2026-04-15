package org.apache.log4j;

import org.apache.log4j.spi.LoggerFactory;

public class Logger extends Category {
    private static final String FQCN;
    static Class class$org$apache$log4j$Logger;

    static {
        Class class$;
        if (class$org$apache$log4j$Logger == null) {
            class$ = class$("org.apache.log4j.Logger");
            class$org$apache$log4j$Logger = class$;
        } else {
            class$ = class$org$apache$log4j$Logger;
        }
        FQCN = class$.getName();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    protected Logger(String name) {
        super(name);
    }

    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }

    public static Logger getLogger(Class clazz) {
        return LogManager.getLogger(clazz.getName());
    }

    public static Logger getRootLogger() {
        return LogManager.getRootLogger();
    }

    public static Logger getLogger(String name, LoggerFactory factory) {
        return LogManager.getLogger(name, factory);
    }

    public void trace(Object message) {
        if (!this.repository.isDisabled(Level.TRACE_INT) && Level.TRACE.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.TRACE, message, null);
        }
    }

    public void trace(Object message, Throwable t) {
        if (!this.repository.isDisabled(Level.TRACE_INT) && Level.TRACE.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.TRACE, message, t);
        }
    }

    public boolean isTraceEnabled() {
        if (this.repository.isDisabled(Level.TRACE_INT)) {
            return false;
        }
        return Level.TRACE.isGreaterOrEqual(getEffectiveLevel());
    }
}
