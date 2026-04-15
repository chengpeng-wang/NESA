package org.apache.log4j;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

public class Category implements AppenderAttachable {
    private static final String FQCN;
    static Class class$org$apache$log4j$Category;
    AppenderAttachableImpl aai;
    protected boolean additive = true;
    protected volatile Level level;
    protected String name;
    protected volatile Category parent;
    protected LoggerRepository repository;
    protected ResourceBundle resourceBundle;

    static {
        Class class$;
        if (class$org$apache$log4j$Category == null) {
            class$ = class$("org.apache.log4j.Category");
            class$org$apache$log4j$Category = class$;
        } else {
            class$ = class$org$apache$log4j$Category;
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

    protected Category(String name) {
        this.name = name;
    }

    public synchronized void addAppender(Appender newAppender) {
        if (this.aai == null) {
            this.aai = new AppenderAttachableImpl();
        }
        this.aai.addAppender(newAppender);
        this.repository.fireAddAppenderEvent(this, newAppender);
    }

    public void assertLog(boolean assertion, String msg) {
        if (!assertion) {
            error(msg);
        }
    }

    /* JADX WARNING: Missing block: B:14:0x001e, code skipped:
            r0 = r0.parent;
     */
    public void callAppenders(org.apache.log4j.spi.LoggingEvent r4) {
        /*
        r3 = this;
        r1 = 0;
        r0 = r3;
    L_0x0002:
        if (r0 == 0) goto L_0x0015;
    L_0x0004:
        monitor-enter(r0);
        r2 = r0.aai;	 Catch:{ all -> 0x0021 }
        if (r2 == 0) goto L_0x0010;
    L_0x0009:
        r2 = r0.aai;	 Catch:{ all -> 0x0021 }
        r2 = r2.appendLoopOnAppenders(r4);	 Catch:{ all -> 0x0021 }
        r1 = r1 + r2;
    L_0x0010:
        r2 = r0.additive;	 Catch:{ all -> 0x0021 }
        if (r2 != 0) goto L_0x001d;
    L_0x0014:
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
    L_0x0015:
        if (r1 != 0) goto L_0x001c;
    L_0x0017:
        r2 = r3.repository;
        r2.emitNoAppenderWarning(r3);
    L_0x001c:
        return;
    L_0x001d:
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
        r0 = r0.parent;
        goto L_0x0002;
    L_0x0021:
        r2 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0021 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.Category.callAppenders(org.apache.log4j.spi.LoggingEvent):void");
    }

    /* access modifiers changed from: declared_synchronized */
    public synchronized void closeNestedAppenders() {
        Enumeration enumeration = getAllAppenders();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Appender a = (Appender) enumeration.nextElement();
                if (a instanceof AppenderAttachable) {
                    a.close();
                }
            }
        }
    }

    public void debug(Object message) {
        if (!this.repository.isDisabled(Priority.DEBUG_INT) && Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, null);
        }
    }

    public void debug(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.DEBUG_INT) && Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, t);
        }
    }

    public void error(Object message) {
        if (!this.repository.isDisabled(Priority.ERROR_INT) && Level.ERROR.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.ERROR, message, null);
        }
    }

    public void error(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.ERROR_INT) && Level.ERROR.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.ERROR, message, t);
        }
    }

    public static Logger exists(String name) {
        return LogManager.exists(name);
    }

    public void fatal(Object message) {
        if (!this.repository.isDisabled(Priority.FATAL_INT) && Level.FATAL.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.FATAL, message, null);
        }
    }

    public void fatal(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.FATAL_INT) && Level.FATAL.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.FATAL, message, t);
        }
    }

    /* access modifiers changed from: protected */
    public void forcedLog(String fqcn, Priority level, Object message, Throwable t) {
        callAppenders(new LoggingEvent(fqcn, this, level, message, t));
    }

    public boolean getAdditivity() {
        return this.additive;
    }

    public synchronized Enumeration getAllAppenders() {
        Enumeration instance;
        if (this.aai == null) {
            instance = NullEnumeration.getInstance();
        } else {
            instance = this.aai.getAllAppenders();
        }
        return instance;
    }

    public synchronized Appender getAppender(String name) {
        Appender appender;
        if (this.aai == null || name == null) {
            appender = null;
        } else {
            appender = this.aai.getAppender(name);
        }
        return appender;
    }

    public Level getEffectiveLevel() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.level != null) {
                return c.level;
            }
        }
        return null;
    }

    public Priority getChainedPriority() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.level != null) {
                return c.level;
            }
        }
        return null;
    }

    public static Enumeration getCurrentCategories() {
        return LogManager.getCurrentLoggers();
    }

    public static LoggerRepository getDefaultHierarchy() {
        return LogManager.getLoggerRepository();
    }

    public LoggerRepository getHierarchy() {
        return this.repository;
    }

    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }

    public static Category getInstance(String name) {
        return LogManager.getLogger(name);
    }

    public static Category getInstance(Class clazz) {
        return LogManager.getLogger(clazz);
    }

    public final String getName() {
        return this.name;
    }

    public final Category getParent() {
        return this.parent;
    }

    public final Level getLevel() {
        return this.level;
    }

    public final Level getPriority() {
        return this.level;
    }

    public static final Category getRoot() {
        return LogManager.getRootLogger();
    }

    public ResourceBundle getResourceBundle() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.resourceBundle != null) {
                return c.resourceBundle;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getResourceBundleString(String key) {
        String str = null;
        ResourceBundle rb = getResourceBundle();
        if (rb == null) {
            return str;
        }
        try {
            return rb.getString(key);
        } catch (MissingResourceException e) {
            error(new StringBuffer().append("No resource is associated with key \"").append(key).append("\".").toString());
            return str;
        }
    }

    public void info(Object message) {
        if (!this.repository.isDisabled(Priority.INFO_INT) && Level.INFO.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.INFO, message, null);
        }
    }

    public void info(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.INFO_INT) && Level.INFO.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.INFO, message, t);
        }
    }

    public boolean isAttached(Appender appender) {
        if (appender == null || this.aai == null) {
            return false;
        }
        return this.aai.isAttached(appender);
    }

    public boolean isDebugEnabled() {
        if (this.repository.isDisabled(Priority.DEBUG_INT)) {
            return false;
        }
        return Level.DEBUG.isGreaterOrEqual(getEffectiveLevel());
    }

    public boolean isEnabledFor(Priority level) {
        if (this.repository.isDisabled(level.level)) {
            return false;
        }
        return level.isGreaterOrEqual(getEffectiveLevel());
    }

    public boolean isInfoEnabled() {
        if (this.repository.isDisabled(Priority.INFO_INT)) {
            return false;
        }
        return Level.INFO.isGreaterOrEqual(getEffectiveLevel());
    }

    public void l7dlog(Priority priority, String key, Throwable t) {
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            String msg = getResourceBundleString(key);
            if (msg == null) {
                msg = key;
            }
            forcedLog(FQCN, priority, msg, t);
        }
    }

    public void l7dlog(Priority priority, String key, Object[] params, Throwable t) {
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            String msg;
            String pattern = getResourceBundleString(key);
            if (pattern == null) {
                msg = key;
            } else {
                msg = MessageFormat.format(pattern, params);
            }
            forcedLog(FQCN, priority, msg, t);
        }
    }

    public void log(Priority priority, Object message, Throwable t) {
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, priority, message, t);
        }
    }

    public void log(Priority priority, Object message) {
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, priority, message, null);
        }
    }

    public void log(String callerFQCN, Priority level, Object message, Throwable t) {
        if (!this.repository.isDisabled(level.level) && level.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(callerFQCN, level, message, t);
        }
    }

    private void fireRemoveAppenderEvent(Appender appender) {
        if (appender == null) {
            return;
        }
        if (this.repository instanceof Hierarchy) {
            ((Hierarchy) this.repository).fireRemoveAppenderEvent(this, appender);
        } else if (this.repository instanceof HierarchyEventListener) {
            ((HierarchyEventListener) this.repository).removeAppenderEvent(this, appender);
        }
    }

    public synchronized void removeAllAppenders() {
        if (this.aai != null) {
            Vector appenders = new Vector();
            Enumeration iter = this.aai.getAllAppenders();
            while (iter != null && iter.hasMoreElements()) {
                appenders.add(iter.nextElement());
            }
            this.aai.removeAllAppenders();
            iter = appenders.elements();
            while (iter.hasMoreElements()) {
                fireRemoveAppenderEvent((Appender) iter.nextElement());
            }
            this.aai = null;
        }
    }

    public synchronized void removeAppender(Appender appender) {
        if (appender != null) {
            if (this.aai != null) {
                boolean wasAttached = this.aai.isAttached(appender);
                this.aai.removeAppender(appender);
                if (wasAttached) {
                    fireRemoveAppenderEvent(appender);
                }
            }
        }
    }

    public synchronized void removeAppender(String name) {
        if (name != null) {
            if (this.aai != null) {
                Appender appender = this.aai.getAppender(name);
                this.aai.removeAppender(name);
                if (appender != null) {
                    fireRemoveAppenderEvent(appender);
                }
            }
        }
    }

    public void setAdditivity(boolean additive) {
        this.additive = additive;
    }

    /* access modifiers changed from: final */
    public final void setHierarchy(LoggerRepository repository) {
        this.repository = repository;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setPriority(Priority priority) {
        this.level = (Level) priority;
    }

    public void setResourceBundle(ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }

    public static void shutdown() {
        LogManager.shutdown();
    }

    public void warn(Object message) {
        if (!this.repository.isDisabled(Priority.WARN_INT) && Level.WARN.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.WARN, message, null);
        }
    }

    public void warn(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.WARN_INT) && Level.WARN.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.WARN, message, t);
        }
    }
}
