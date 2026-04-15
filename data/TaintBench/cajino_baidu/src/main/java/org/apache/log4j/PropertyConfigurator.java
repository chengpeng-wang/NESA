package org.apache.log4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.spi.ThrowableRendererSupport;

public class PropertyConfigurator implements Configurator {
    static final String ADDITIVITY_PREFIX = "log4j.additivity.";
    static final String APPENDER_PREFIX = "log4j.appender.";
    private static final String APPENDER_REF_TAG = "appender-ref";
    static final String CATEGORY_PREFIX = "log4j.category.";
    static final String FACTORY_PREFIX = "log4j.factory";
    private static final String INTERNAL_ROOT_NAME = "root";
    public static final String LOGGER_FACTORY_KEY = "log4j.loggerFactory";
    static final String LOGGER_PREFIX = "log4j.logger.";
    private static final String LOGGER_REF = "logger-ref";
    static final String RENDERER_PREFIX = "log4j.renderer.";
    private static final String RESET_KEY = "log4j.reset";
    static final String ROOT_CATEGORY_PREFIX = "log4j.rootCategory";
    static final String ROOT_LOGGER_PREFIX = "log4j.rootLogger";
    private static final String ROOT_REF = "root-ref";
    static final String THRESHOLD_PREFIX = "log4j.threshold";
    private static final String THROWABLE_RENDERER_PREFIX = "log4j.throwableRenderer";
    static Class class$org$apache$log4j$Appender;
    static Class class$org$apache$log4j$Layout;
    static Class class$org$apache$log4j$spi$ErrorHandler;
    static Class class$org$apache$log4j$spi$Filter;
    static Class class$org$apache$log4j$spi$LoggerFactory;
    static Class class$org$apache$log4j$spi$ThrowableRenderer;
    protected LoggerFactory loggerFactory = new DefaultCategoryFactory();
    protected Hashtable registry = new Hashtable(11);
    private LoggerRepository repository;

    /* JADX WARNING: Removed duplicated region for block: B:26:0x007e A:{SYNTHETIC, Splitter:B:26:0x007e} */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006e A:{SYNTHETIC, Splitter:B:20:0x006e} */
    public void doConfigure(java.lang.String r8, org.apache.log4j.spi.LoggerRepository r9) {
        /*
        r7 = this;
        r4 = new java.util.Properties;
        r4.<init>();
        r2 = 0;
        r3 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0024 }
        r3.<init>(r8);	 Catch:{ Exception -> 0x0024 }
        r4.load(r3);	 Catch:{ Exception -> 0x0094, all -> 0x0091 }
        r3.close();	 Catch:{ Exception -> 0x0094, all -> 0x0091 }
        if (r3 == 0) goto L_0x0016;
    L_0x0013:
        r3.close();	 Catch:{ InterruptedIOException -> 0x001b, Throwable -> 0x008b }
    L_0x0016:
        r7.doConfigure(r4, r9);
        r2 = r3;
    L_0x001a:
        return;
    L_0x001b:
        r1 = move-exception;
        r5 = java.lang.Thread.currentThread();
        r5.interrupt();
        goto L_0x0016;
    L_0x0024:
        r0 = move-exception;
    L_0x0025:
        r5 = r0 instanceof java.io.InterruptedIOException;	 Catch:{ all -> 0x007b }
        if (r5 != 0) goto L_0x002d;
    L_0x0029:
        r5 = r0 instanceof java.lang.InterruptedException;	 Catch:{ all -> 0x007b }
        if (r5 == 0) goto L_0x0034;
    L_0x002d:
        r5 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x007b }
        r5.interrupt();	 Catch:{ all -> 0x007b }
    L_0x0034:
        r5 = new java.lang.StringBuffer;	 Catch:{ all -> 0x007b }
        r5.<init>();	 Catch:{ all -> 0x007b }
        r6 = "Could not read configuration file [";
        r5 = r5.append(r6);	 Catch:{ all -> 0x007b }
        r5 = r5.append(r8);	 Catch:{ all -> 0x007b }
        r6 = "].";
        r5 = r5.append(r6);	 Catch:{ all -> 0x007b }
        r5 = r5.toString();	 Catch:{ all -> 0x007b }
        org.apache.log4j.helpers.LogLog.error(r5, r0);	 Catch:{ all -> 0x007b }
        r5 = new java.lang.StringBuffer;	 Catch:{ all -> 0x007b }
        r5.<init>();	 Catch:{ all -> 0x007b }
        r6 = "Ignoring configuration file [";
        r5 = r5.append(r6);	 Catch:{ all -> 0x007b }
        r5 = r5.append(r8);	 Catch:{ all -> 0x007b }
        r6 = "].";
        r5 = r5.append(r6);	 Catch:{ all -> 0x007b }
        r5 = r5.toString();	 Catch:{ all -> 0x007b }
        org.apache.log4j.helpers.LogLog.error(r5);	 Catch:{ all -> 0x007b }
        if (r2 == 0) goto L_0x001a;
    L_0x006e:
        r2.close();	 Catch:{ InterruptedIOException -> 0x0072, Throwable -> 0x008d }
        goto L_0x001a;
    L_0x0072:
        r1 = move-exception;
        r5 = java.lang.Thread.currentThread();
        r5.interrupt();
        goto L_0x001a;
    L_0x007b:
        r5 = move-exception;
    L_0x007c:
        if (r2 == 0) goto L_0x0081;
    L_0x007e:
        r2.close();	 Catch:{ InterruptedIOException -> 0x0082, Throwable -> 0x008f }
    L_0x0081:
        throw r5;
    L_0x0082:
        r1 = move-exception;
        r6 = java.lang.Thread.currentThread();
        r6.interrupt();
        goto L_0x0081;
    L_0x008b:
        r5 = move-exception;
        goto L_0x0016;
    L_0x008d:
        r5 = move-exception;
        goto L_0x001a;
    L_0x008f:
        r6 = move-exception;
        goto L_0x0081;
    L_0x0091:
        r5 = move-exception;
        r2 = r3;
        goto L_0x007c;
    L_0x0094:
        r0 = move-exception;
        r2 = r3;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.PropertyConfigurator.doConfigure(java.lang.String, org.apache.log4j.spi.LoggerRepository):void");
    }

    public static void configure(String configFilename) {
        new PropertyConfigurator().doConfigure(configFilename, LogManager.getLoggerRepository());
    }

    public static void configure(URL configURL) {
        new PropertyConfigurator().doConfigure(configURL, LogManager.getLoggerRepository());
    }

    public static void configure(Properties properties) {
        new PropertyConfigurator().doConfigure(properties, LogManager.getLoggerRepository());
    }

    public static void configureAndWatch(String configFilename) {
        configureAndWatch(configFilename, FileWatchdog.DEFAULT_DELAY);
    }

    public static void configureAndWatch(String configFilename, long delay) {
        PropertyWatchdog pdog = new PropertyWatchdog(configFilename);
        pdog.setDelay(delay);
        pdog.start();
    }

    public void doConfigure(Properties properties, LoggerRepository hierarchy) {
        this.repository = hierarchy;
        String value = properties.getProperty(LogLog.DEBUG_KEY);
        if (value == null) {
            value = properties.getProperty(LogLog.CONFIG_DEBUG_KEY);
            if (value != null) {
                LogLog.warn("[log4j.configDebug] is deprecated. Use [log4j.debug] instead.");
            }
        }
        if (value != null) {
            LogLog.setInternalDebugging(OptionConverter.toBoolean(value, true));
        }
        String reset = properties.getProperty(RESET_KEY);
        if (reset != null && OptionConverter.toBoolean(reset, false)) {
            hierarchy.resetConfiguration();
        }
        String thresholdStr = OptionConverter.findAndSubst(THRESHOLD_PREFIX, properties);
        if (thresholdStr != null) {
            hierarchy.setThreshold(OptionConverter.toLevel(thresholdStr, Level.ALL));
            LogLog.debug(new StringBuffer().append("Hierarchy threshold set to [").append(hierarchy.getThreshold()).append("].").toString());
        }
        configureRootCategory(properties, hierarchy);
        configureLoggerFactory(properties);
        parseCatsAndRenderers(properties, hierarchy);
        LogLog.debug("Finished configuring.");
        this.registry.clear();
    }

    public void doConfigure(URL configURL, LoggerRepository hierarchy) {
        Properties props = new Properties();
        LogLog.debug(new StringBuffer().append("Reading configuration from URL ").append(configURL).toString());
        InputStream istream = null;
        try {
            URLConnection uConn = configURL.openConnection();
            uConn.setUseCaches(false);
            istream = uConn.getInputStream();
            props.load(istream);
            if (istream != null) {
                try {
                    istream.close();
                } catch (InterruptedIOException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException | RuntimeException e2) {
                }
            }
            doConfigure(props, hierarchy);
        } catch (Exception e3) {
            if ((e3 instanceof InterruptedIOException) || (e3 instanceof InterruptedException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error(new StringBuffer().append("Could not read configuration file from URL [").append(configURL).append("].").toString(), e3);
            LogLog.error(new StringBuffer().append("Ignoring configuration file [").append(configURL).append("].").toString());
            if (istream != null) {
                try {
                    istream.close();
                } catch (InterruptedIOException e4) {
                    Thread.currentThread().interrupt();
                } catch (IOException | RuntimeException e5) {
                }
            }
        } catch (Throwable th) {
            if (istream != null) {
                try {
                    istream.close();
                } catch (InterruptedIOException e6) {
                    Thread.currentThread().interrupt();
                } catch (IOException | RuntimeException e7) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void configureLoggerFactory(Properties props) {
        String factoryClassName = OptionConverter.findAndSubst(LOGGER_FACTORY_KEY, props);
        if (factoryClassName != null) {
            Class class$;
            LogLog.debug(new StringBuffer().append("Setting category factory to [").append(factoryClassName).append("].").toString());
            if (class$org$apache$log4j$spi$LoggerFactory == null) {
                class$ = class$("org.apache.log4j.spi.LoggerFactory");
                class$org$apache$log4j$spi$LoggerFactory = class$;
            } else {
                class$ = class$org$apache$log4j$spi$LoggerFactory;
            }
            this.loggerFactory = (LoggerFactory) OptionConverter.instantiateByClassName(factoryClassName, class$, this.loggerFactory);
            PropertySetter.setProperties(this.loggerFactory, props, "log4j.factory.");
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public void configureRootCategory(Properties props, LoggerRepository hierarchy) {
        String effectiveFrefix = ROOT_LOGGER_PREFIX;
        String value = OptionConverter.findAndSubst(ROOT_LOGGER_PREFIX, props);
        if (value == null) {
            value = OptionConverter.findAndSubst(ROOT_CATEGORY_PREFIX, props);
            effectiveFrefix = ROOT_CATEGORY_PREFIX;
        }
        if (value == null) {
            LogLog.debug("Could not find root logger information. Is this OK?");
            return;
        }
        Logger root = hierarchy.getRootLogger();
        synchronized (root) {
            parseCategory(props, root, effectiveFrefix, INTERNAL_ROOT_NAME, value);
        }
    }

    /* access modifiers changed from: protected */
    public void parseCatsAndRenderers(Properties props, LoggerRepository hierarchy) {
        Enumeration enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            if (key.startsWith(CATEGORY_PREFIX) || key.startsWith(LOGGER_PREFIX)) {
                String loggerName = null;
                if (key.startsWith(CATEGORY_PREFIX)) {
                    loggerName = key.substring(CATEGORY_PREFIX.length());
                } else if (key.startsWith(LOGGER_PREFIX)) {
                    loggerName = key.substring(LOGGER_PREFIX.length());
                }
                String value = OptionConverter.findAndSubst(key, props);
                Logger logger = hierarchy.getLogger(loggerName, this.loggerFactory);
                synchronized (logger) {
                    parseCategory(props, logger, key, loggerName, value);
                    parseAdditivityForLogger(props, logger, loggerName);
                }
            } else if (key.startsWith(RENDERER_PREFIX)) {
                String renderedClass = key.substring(RENDERER_PREFIX.length());
                String renderingClass = OptionConverter.findAndSubst(key, props);
                if (hierarchy instanceof RendererSupport) {
                    RendererMap.addRenderer((RendererSupport) hierarchy, renderedClass, renderingClass);
                }
            } else if (key.equals(THROWABLE_RENDERER_PREFIX) && (hierarchy instanceof ThrowableRendererSupport)) {
                Class class$;
                String str = THROWABLE_RENDERER_PREFIX;
                if (class$org$apache$log4j$spi$ThrowableRenderer == null) {
                    class$ = class$("org.apache.log4j.spi.ThrowableRenderer");
                    class$org$apache$log4j$spi$ThrowableRenderer = class$;
                } else {
                    class$ = class$org$apache$log4j$spi$ThrowableRenderer;
                }
                ThrowableRenderer tr = (ThrowableRenderer) OptionConverter.instantiateByKey(props, str, class$, null);
                if (tr == null) {
                    LogLog.error("Could not instantiate throwableRenderer.");
                } else {
                    new PropertySetter(tr).setProperties(props, "log4j.throwableRenderer.");
                    ((ThrowableRendererSupport) hierarchy).setThrowableRenderer(tr);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void parseAdditivityForLogger(Properties props, Logger cat, String loggerName) {
        String value = OptionConverter.findAndSubst(new StringBuffer().append(ADDITIVITY_PREFIX).append(loggerName).toString(), props);
        LogLog.debug(new StringBuffer().append("Handling log4j.additivity.").append(loggerName).append("=[").append(value).append("]").toString());
        if (value != null && !value.equals("")) {
            boolean additivity = OptionConverter.toBoolean(value, true);
            LogLog.debug(new StringBuffer().append("Setting additivity for \"").append(loggerName).append("\" to ").append(additivity).toString());
            cat.setAdditivity(additivity);
        }
    }

    /* access modifiers changed from: 0000 */
    public void parseCategory(Properties props, Logger logger, String optionKey, String loggerName, String value) {
        LogLog.debug(new StringBuffer().append("Parsing for [").append(loggerName).append("] with value=[").append(value).append("].").toString());
        StringTokenizer st = new StringTokenizer(value, ",");
        if (!(value.startsWith(",") || value.equals(""))) {
            if (st.hasMoreTokens()) {
                String levelStr = st.nextToken();
                LogLog.debug(new StringBuffer().append("Level token is [").append(levelStr).append("].").toString());
                if (!Configurator.INHERITED.equalsIgnoreCase(levelStr) && !"null".equalsIgnoreCase(levelStr)) {
                    logger.setLevel(OptionConverter.toLevel(levelStr, Level.DEBUG));
                } else if (loggerName.equals(INTERNAL_ROOT_NAME)) {
                    LogLog.warn("The root logger cannot be set to null.");
                } else {
                    logger.setLevel(null);
                }
                LogLog.debug(new StringBuffer().append("Category ").append(loggerName).append(" set to ").append(logger.getLevel()).toString());
            } else {
                return;
            }
        }
        logger.removeAllAppenders();
        while (st.hasMoreTokens()) {
            String appenderName = st.nextToken().trim();
            if (!(appenderName == null || appenderName.equals(","))) {
                LogLog.debug(new StringBuffer().append("Parsing appender named \"").append(appenderName).append("\".").toString());
                Appender appender = parseAppender(props, appenderName);
                if (appender != null) {
                    logger.addAppender(appender);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Appender parseAppender(Properties props, String appenderName) {
        Appender appender = registryGet(appenderName);
        if (appender != null) {
            LogLog.debug(new StringBuffer().append("Appender \"").append(appenderName).append("\" was already parsed.").toString());
            return appender;
        }
        Class class$;
        String prefix = new StringBuffer().append(APPENDER_PREFIX).append(appenderName).toString();
        String layoutPrefix = new StringBuffer().append(prefix).append(".layout").toString();
        if (class$org$apache$log4j$Appender == null) {
            class$ = class$("org.apache.log4j.Appender");
            class$org$apache$log4j$Appender = class$;
        } else {
            class$ = class$org$apache$log4j$Appender;
        }
        appender = (Appender) OptionConverter.instantiateByKey(props, prefix, class$, null);
        if (appender == null) {
            LogLog.error(new StringBuffer().append("Could not instantiate appender named \"").append(appenderName).append("\".").toString());
            return null;
        }
        appender.setName(appenderName);
        if (appender instanceof OptionHandler) {
            if (appender.requiresLayout()) {
                if (class$org$apache$log4j$Layout == null) {
                    class$ = class$("org.apache.log4j.Layout");
                    class$org$apache$log4j$Layout = class$;
                } else {
                    class$ = class$org$apache$log4j$Layout;
                }
                Layout layout = (Layout) OptionConverter.instantiateByKey(props, layoutPrefix, class$, null);
                if (layout != null) {
                    appender.setLayout(layout);
                    LogLog.debug(new StringBuffer().append("Parsing layout options for \"").append(appenderName).append("\".").toString());
                    PropertySetter.setProperties(layout, props, new StringBuffer().append(layoutPrefix).append(".").toString());
                    LogLog.debug(new StringBuffer().append("End of parsing for \"").append(appenderName).append("\".").toString());
                }
            }
            String errorHandlerPrefix = new StringBuffer().append(prefix).append(".errorhandler").toString();
            if (OptionConverter.findAndSubst(errorHandlerPrefix, props) != null) {
                if (class$org$apache$log4j$spi$ErrorHandler == null) {
                    class$ = class$("org.apache.log4j.spi.ErrorHandler");
                    class$org$apache$log4j$spi$ErrorHandler = class$;
                } else {
                    class$ = class$org$apache$log4j$spi$ErrorHandler;
                }
                ErrorHandler eh = (ErrorHandler) OptionConverter.instantiateByKey(props, errorHandlerPrefix, class$, null);
                if (eh != null) {
                    appender.setErrorHandler(eh);
                    LogLog.debug(new StringBuffer().append("Parsing errorhandler options for \"").append(appenderName).append("\".").toString());
                    parseErrorHandler(eh, errorHandlerPrefix, props, this.repository);
                    Properties edited = new Properties();
                    String[] keys = new String[]{new StringBuffer().append(errorHandlerPrefix).append(".").append(ROOT_REF).toString(), new StringBuffer().append(errorHandlerPrefix).append(".").append(LOGGER_REF).toString(), new StringBuffer().append(errorHandlerPrefix).append(".").append(APPENDER_REF_TAG).toString()};
                    for (Entry entry : props.entrySet()) {
                        int i = 0;
                        while (i < keys.length && !keys[i].equals(entry.getKey())) {
                            i++;
                        }
                        if (i == keys.length) {
                            edited.put(entry.getKey(), entry.getValue());
                        }
                    }
                    PropertySetter.setProperties(eh, edited, new StringBuffer().append(errorHandlerPrefix).append(".").toString());
                    LogLog.debug(new StringBuffer().append("End of errorhandler parsing for \"").append(appenderName).append("\".").toString());
                }
            }
            PropertySetter.setProperties(appender, props, new StringBuffer().append(prefix).append(".").toString());
            LogLog.debug(new StringBuffer().append("Parsed \"").append(appenderName).append("\" options.").toString());
        }
        parseAppenderFilters(props, appenderName, appender);
        registryPut(appender);
        return appender;
    }

    private void parseErrorHandler(ErrorHandler eh, String errorHandlerPrefix, Properties props, LoggerRepository hierarchy) {
        if (OptionConverter.toBoolean(OptionConverter.findAndSubst(new StringBuffer().append(errorHandlerPrefix).append(ROOT_REF).toString(), props), false)) {
            eh.setLogger(hierarchy.getRootLogger());
        }
        String loggerName = OptionConverter.findAndSubst(new StringBuffer().append(errorHandlerPrefix).append(LOGGER_REF).toString(), props);
        if (loggerName != null) {
            eh.setLogger(this.loggerFactory == null ? hierarchy.getLogger(loggerName) : hierarchy.getLogger(loggerName, this.loggerFactory));
        }
        String appenderName = OptionConverter.findAndSubst(new StringBuffer().append(errorHandlerPrefix).append(APPENDER_REF_TAG).toString(), props);
        if (appenderName != null) {
            Appender backup = parseAppender(props, appenderName);
            if (backup != null) {
                eh.setBackupAppender(backup);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void parseAppenderFilters(Properties props, String appenderName, Appender appender) {
        String key;
        String filterPrefix = new StringBuffer().append(APPENDER_PREFIX).append(appenderName).append(".filter.").toString();
        int fIdx = filterPrefix.length();
        Hashtable filters = new Hashtable();
        Enumeration e = props.keys();
        String name = "";
        while (e.hasMoreElements()) {
            key = (String) e.nextElement();
            if (key.startsWith(filterPrefix)) {
                int dotIdx = key.indexOf(46, fIdx);
                String filterKey = key;
                if (dotIdx != -1) {
                    filterKey = key.substring(0, dotIdx);
                    name = key.substring(dotIdx + 1);
                }
                Vector filterOpts = (Vector) filters.get(filterKey);
                if (filterOpts == null) {
                    filterOpts = new Vector();
                    filters.put(filterKey, filterOpts);
                }
                if (dotIdx != -1) {
                    filterOpts.add(new NameValue(name, OptionConverter.findAndSubst(key, props)));
                }
            }
        }
        Enumeration g = new SortedKeyEnumeration(filters);
        while (g.hasMoreElements()) {
            key = (String) g.nextElement();
            String clazz = props.getProperty(key);
            if (clazz != null) {
                Class class$;
                LogLog.debug(new StringBuffer().append("Filter key: [").append(key).append("] class: [").append(props.getProperty(key)).append("] props: ").append(filters.get(key)).toString());
                if (class$org$apache$log4j$spi$Filter == null) {
                    class$ = class$("org.apache.log4j.spi.Filter");
                    class$org$apache$log4j$spi$Filter = class$;
                } else {
                    class$ = class$org$apache$log4j$spi$Filter;
                }
                Filter filter = (Filter) OptionConverter.instantiateByClassName(clazz, class$, null);
                if (filter != null) {
                    PropertySetter propertySetter = new PropertySetter(filter);
                    Enumeration filterProps = ((Vector) filters.get(key)).elements();
                    while (filterProps.hasMoreElements()) {
                        NameValue kv = (NameValue) filterProps.nextElement();
                        propertySetter.setProperty(kv.key, kv.value);
                    }
                    propertySetter.activate();
                    LogLog.debug(new StringBuffer().append("Adding filter of type [").append(filter.getClass()).append("] to appender named [").append(appender.getName()).append("].").toString());
                    appender.addFilter(filter);
                }
            } else {
                LogLog.warn(new StringBuffer().append("Missing class definition for filter: [").append(key).append("]").toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void registryPut(Appender appender) {
        this.registry.put(appender.getName(), appender);
    }

    /* access modifiers changed from: 0000 */
    public Appender registryGet(String name) {
        return (Appender) this.registry.get(name);
    }
}
