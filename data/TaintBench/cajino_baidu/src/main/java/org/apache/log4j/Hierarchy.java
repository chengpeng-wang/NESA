package org.apache.log4j;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.spi.ThrowableRendererSupport;

public class Hierarchy implements LoggerRepository, RendererSupport, ThrowableRendererSupport {
    private LoggerFactory defaultFactory;
    boolean emittedNoAppenderWarning = false;
    boolean emittedNoResourceBundleWarning = false;
    Hashtable ht = new Hashtable();
    private Vector listeners = new Vector(1);
    RendererMap rendererMap;
    Logger root;
    Level threshold;
    int thresholdInt;
    private ThrowableRenderer throwableRenderer = null;

    public Hierarchy(Logger root) {
        this.root = root;
        setThreshold(Level.ALL);
        this.root.setHierarchy(this);
        this.rendererMap = new RendererMap();
        this.defaultFactory = new DefaultCategoryFactory();
    }

    public void addRenderer(Class classToRender, ObjectRenderer or) {
        this.rendererMap.put(classToRender, or);
    }

    public void addHierarchyEventListener(HierarchyEventListener listener) {
        if (this.listeners.contains(listener)) {
            LogLog.warn("Ignoring attempt to add an existent listener.");
        } else {
            this.listeners.addElement(listener);
        }
    }

    public void clear() {
        this.ht.clear();
    }

    public void emitNoAppenderWarning(Category cat) {
        if (!this.emittedNoAppenderWarning) {
            LogLog.warn(new StringBuffer().append("No appenders could be found for logger (").append(cat.getName()).append(").").toString());
            LogLog.warn("Please initialize the log4j system properly.");
            LogLog.warn("See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.");
            this.emittedNoAppenderWarning = true;
        }
    }

    public Logger exists(String name) {
        Object o = this.ht.get(new CategoryKey(name));
        if (o instanceof Logger) {
            return (Logger) o;
        }
        return null;
    }

    public void setThreshold(String levelStr) {
        Level l = Level.toLevel(levelStr, null);
        if (l != null) {
            setThreshold(l);
        } else {
            LogLog.warn(new StringBuffer().append("Could not convert [").append(levelStr).append("] to Level.").toString());
        }
    }

    public void setThreshold(Level l) {
        if (l != null) {
            this.thresholdInt = l.level;
            this.threshold = l;
        }
    }

    public void fireAddAppenderEvent(Category logger, Appender appender) {
        if (this.listeners != null) {
            int size = this.listeners.size();
            for (int i = 0; i < size; i++) {
                ((HierarchyEventListener) this.listeners.elementAt(i)).addAppenderEvent(logger, appender);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void fireRemoveAppenderEvent(Category logger, Appender appender) {
        if (this.listeners != null) {
            int size = this.listeners.size();
            for (int i = 0; i < size; i++) {
                ((HierarchyEventListener) this.listeners.elementAt(i)).removeAppenderEvent(logger, appender);
            }
        }
    }

    public Level getThreshold() {
        return this.threshold;
    }

    public Logger getLogger(String name) {
        return getLogger(name, this.defaultFactory);
    }

    public Logger getLogger(String name, LoggerFactory factory) {
        CategoryKey key = new CategoryKey(name);
        synchronized (this.ht) {
            Object o = this.ht.get(key);
            Logger logger;
            if (o == null) {
                logger = factory.makeNewLoggerInstance(name);
                logger.setHierarchy(this);
                this.ht.put(key, logger);
                updateParents(logger);
                return logger;
            } else if (o instanceof Logger) {
                Logger logger2 = (Logger) o;
                return logger2;
            } else if (o instanceof ProvisionNode) {
                logger = factory.makeNewLoggerInstance(name);
                logger.setHierarchy(this);
                this.ht.put(key, logger);
                updateChildren((ProvisionNode) o, logger);
                updateParents(logger);
                return logger;
            } else {
                return null;
            }
        }
    }

    public Enumeration getCurrentLoggers() {
        Vector v = new Vector(this.ht.size());
        Enumeration elems = this.ht.elements();
        while (elems.hasMoreElements()) {
            Object o = elems.nextElement();
            if (o instanceof Logger) {
                v.addElement(o);
            }
        }
        return v.elements();
    }

    public Enumeration getCurrentCategories() {
        return getCurrentLoggers();
    }

    public RendererMap getRendererMap() {
        return this.rendererMap;
    }

    public Logger getRootLogger() {
        return this.root;
    }

    public boolean isDisabled(int level) {
        return this.thresholdInt > level;
    }

    public void overrideAsNeeded(String override) {
        LogLog.warn("The Hiearchy.overrideAsNeeded method has been deprecated.");
    }

    public void resetConfiguration() {
        getRootLogger().setLevel(Level.DEBUG);
        this.root.setResourceBundle(null);
        setThreshold(Level.ALL);
        synchronized (this.ht) {
            shutdown();
            Enumeration cats = getCurrentLoggers();
            while (cats.hasMoreElements()) {
                Logger c = (Logger) cats.nextElement();
                c.setLevel(null);
                c.setAdditivity(true);
                c.setResourceBundle(null);
            }
        }
        this.rendererMap.clear();
        this.throwableRenderer = null;
    }

    public void setDisableOverride(String override) {
        LogLog.warn("The Hiearchy.setDisableOverride method has been deprecated.");
    }

    public void setRenderer(Class renderedClass, ObjectRenderer renderer) {
        this.rendererMap.put(renderedClass, renderer);
    }

    public void setThrowableRenderer(ThrowableRenderer renderer) {
        this.throwableRenderer = renderer;
    }

    public ThrowableRenderer getThrowableRenderer() {
        return this.throwableRenderer;
    }

    public void shutdown() {
        Logger root = getRootLogger();
        root.closeNestedAppenders();
        synchronized (this.ht) {
            Enumeration cats = getCurrentLoggers();
            while (cats.hasMoreElements()) {
                ((Logger) cats.nextElement()).closeNestedAppenders();
            }
            root.removeAllAppenders();
            cats = getCurrentLoggers();
            while (cats.hasMoreElements()) {
                ((Logger) cats.nextElement()).removeAllAppenders();
            }
        }
    }

    private final void updateParents(Logger cat) {
        String name = cat.name;
        boolean parentFound = false;
        for (int i = name.lastIndexOf(46, name.length() - 1); i >= 0; i = name.lastIndexOf(46, i - 1)) {
            CategoryKey key = new CategoryKey(name.substring(0, i));
            Object o = this.ht.get(key);
            if (o == null) {
                this.ht.put(key, new ProvisionNode(cat));
            } else if (o instanceof Category) {
                parentFound = true;
                cat.parent = (Category) o;
                break;
            } else if (o instanceof ProvisionNode) {
                ((ProvisionNode) o).addElement(cat);
            } else {
                new IllegalStateException(new StringBuffer().append("unexpected object type ").append(o.getClass()).append(" in ht.").toString()).printStackTrace();
            }
        }
        if (!parentFound) {
            cat.parent = this.root;
        }
    }

    private final void updateChildren(ProvisionNode pn, Logger logger) {
        int last = pn.size();
        for (int i = 0; i < last; i++) {
            Logger l = (Logger) pn.elementAt(i);
            if (!l.parent.name.startsWith(logger.name)) {
                logger.parent = l.parent;
                l.parent = logger;
            }
        }
    }
}
