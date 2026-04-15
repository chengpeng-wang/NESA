package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.http.client.methods.HttpTrace;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger implements Log, Serializable {
    private static final String FQCN;
    static Class class$org$apache$commons$logging$impl$Log4JLogger;
    static Class class$org$apache$log4j$Level;
    static Class class$org$apache$log4j$Priority;
    private static Priority traceLevel;
    private transient Logger logger = null;
    private String name = null;

    static {
        Class class$;
        Class class$2;
        if (class$org$apache$commons$logging$impl$Log4JLogger == null) {
            class$ = class$("org.apache.commons.logging.impl.Log4JLogger");
            class$org$apache$commons$logging$impl$Log4JLogger = class$;
        } else {
            class$ = class$org$apache$commons$logging$impl$Log4JLogger;
        }
        FQCN = class$.getName();
        if (class$org$apache$log4j$Priority == null) {
            class$ = class$("org.apache.log4j.Priority");
            class$org$apache$log4j$Priority = class$;
        } else {
            class$ = class$org$apache$log4j$Priority;
        }
        if (class$org$apache$log4j$Level == null) {
            class$2 = class$("org.apache.log4j.Level");
            class$org$apache$log4j$Level = class$2;
        } else {
            class$2 = class$org$apache$log4j$Level;
        }
        if (class$.isAssignableFrom(class$2)) {
            try {
                if (class$org$apache$log4j$Level == null) {
                    class$ = class$("org.apache.log4j.Level");
                    class$org$apache$log4j$Level = class$;
                } else {
                    class$ = class$org$apache$log4j$Level;
                }
                traceLevel = (Priority) class$.getDeclaredField(HttpTrace.METHOD_NAME).get(null);
                return;
            } catch (Exception e) {
                traceLevel = Priority.DEBUG;
                return;
            }
        }
        throw new InstantiationError("Log4J 1.2 not available");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public Log4JLogger(String name) {
        this.name = name;
        this.logger = getLogger();
    }

    public Log4JLogger(Logger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
        }
        this.name = logger.getName();
        this.logger = logger;
    }

    public void trace(Object message) {
        getLogger().log(FQCN, traceLevel, message, null);
    }

    public void trace(Object message, Throwable t) {
        getLogger().log(FQCN, traceLevel, message, t);
    }

    public void debug(Object message) {
        getLogger().log(FQCN, Priority.DEBUG, message, null);
    }

    public void debug(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.DEBUG, message, t);
    }

    public void info(Object message) {
        getLogger().log(FQCN, Priority.INFO, message, null);
    }

    public void info(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.INFO, message, t);
    }

    public void warn(Object message) {
        getLogger().log(FQCN, Priority.WARN, message, null);
    }

    public void warn(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.WARN, message, t);
    }

    public void error(Object message) {
        getLogger().log(FQCN, Priority.ERROR, message, null);
    }

    public void error(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.ERROR, message, t);
    }

    public void fatal(Object message) {
        getLogger().log(FQCN, Priority.FATAL, message, null);
    }

    public void fatal(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.FATAL, message, t);
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Logger.getLogger(this.name);
        }
        return this.logger;
    }

    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return getLogger().isEnabledFor(Priority.ERROR);
    }

    public boolean isFatalEnabled() {
        return getLogger().isEnabledFor(Priority.FATAL);
    }

    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return getLogger().isEnabledFor(traceLevel);
    }

    public boolean isWarnEnabled() {
        return getLogger().isEnabledFor(Priority.WARN);
    }
}
