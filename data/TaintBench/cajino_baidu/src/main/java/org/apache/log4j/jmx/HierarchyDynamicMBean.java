package org.apache.log4j.jmx;

import java.util.Vector;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerRepository;

public class HierarchyDynamicMBean extends AbstractDynamicMBean implements HierarchyEventListener, NotificationBroadcaster {
    static final String ADD_APPENDER = "addAppender.";
    static final String THRESHOLD = "threshold";
    static Class class$org$apache$log4j$jmx$HierarchyDynamicMBean;
    private static Logger log;
    private String dClassName = getClass().getName();
    private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
    private String dDescription = "This MBean acts as a management facade for org.apache.log4j.Hierarchy.";
    private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[1];
    private LoggerRepository hierarchy = LogManager.getLoggerRepository();
    private NotificationBroadcasterSupport nbs = new NotificationBroadcasterSupport();
    private Vector vAttributes = new Vector();

    static {
        Class class$;
        if (class$org$apache$log4j$jmx$HierarchyDynamicMBean == null) {
            class$ = class$("org.apache.log4j.jmx.HierarchyDynamicMBean");
            class$org$apache$log4j$jmx$HierarchyDynamicMBean = class$;
        } else {
            class$ = class$org$apache$log4j$jmx$HierarchyDynamicMBean;
        }
        log = Logger.getLogger(class$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public HierarchyDynamicMBean() {
        buildDynamicMBeanInfo();
    }

    private void buildDynamicMBeanInfo() {
        this.dConstructors[0] = new MBeanConstructorInfo("HierarchyDynamicMBean(): Constructs a HierarchyDynamicMBean instance", getClass().getConstructors()[0]);
        this.vAttributes.add(new MBeanAttributeInfo(THRESHOLD, "java.lang.String", "The \"threshold\" state of the hiearchy.", true, true, false));
        this.dOperations[0] = new MBeanOperationInfo("addLoggerMBean", "addLoggerMBean(): add a loggerMBean", new MBeanParameterInfo[]{new MBeanParameterInfo("name", "java.lang.String", "Create a logger MBean")}, "javax.management.ObjectName", 1);
    }

    public ObjectName addLoggerMBean(String name) {
        Logger cat = LogManager.exists(name);
        if (cat != null) {
            return addLoggerMBean(cat);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public ObjectName addLoggerMBean(Logger logger) {
        JMException e;
        RuntimeException e2;
        String name = logger.getName();
        ObjectName objectName = null;
        try {
            LoggerDynamicMBean loggerMBean = new LoggerDynamicMBean(logger);
            ObjectName objectName2 = new ObjectName("log4j", "logger", name);
            try {
                if (!this.server.isRegistered(objectName2)) {
                    registerMBean(loggerMBean, objectName2);
                    NotificationFilterSupport nfs = new NotificationFilterSupport();
                    nfs.enableType(new StringBuffer().append(ADD_APPENDER).append(logger.getName()).toString());
                    log.debug(new StringBuffer().append("---Adding logger [").append(name).append("] as listener.").toString());
                    this.nbs.addNotificationListener(loggerMBean, nfs, null);
                    this.vAttributes.add(new MBeanAttributeInfo(new StringBuffer().append("logger=").append(name).toString(), "javax.management.ObjectName", new StringBuffer().append("The ").append(name).append(" logger.").toString(), true, true, false));
                }
                return objectName2;
            } catch (JMException e3) {
                e = e3;
                objectName = objectName2;
                log.error(new StringBuffer().append("Could not add loggerMBean for [").append(name).append("].").toString(), e);
                return objectName;
            } catch (RuntimeException e4) {
                e2 = e4;
                objectName = objectName2;
                log.error(new StringBuffer().append("Could not add loggerMBean for [").append(name).append("].").toString(), e2);
                return objectName;
            }
        } catch (JMException e5) {
            e = e5;
            log.error(new StringBuffer().append("Could not add loggerMBean for [").append(name).append("].").toString(), e);
            return objectName;
        } catch (RuntimeException e6) {
            e2 = e6;
            log.error(new StringBuffer().append("Could not add loggerMBean for [").append(name).append("].").toString(), e2);
            return objectName;
        }
    }

    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) {
        this.nbs.addNotificationListener(listener, filter, handback);
    }

    /* access modifiers changed from: protected */
    public Logger getLogger() {
        return log;
    }

    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[this.vAttributes.size()];
        this.vAttributes.toArray(attribs);
        return new MBeanInfo(this.dClassName, this.dDescription, attribs, this.dConstructors, this.dOperations, new MBeanNotificationInfo[0]);
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        return this.nbs.getNotificationInfo();
    }

    public Object invoke(String operationName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        if (operationName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), new StringBuffer().append("Cannot invoke a null operation in ").append(this.dClassName).toString());
        } else if (operationName.equals("addLoggerMBean")) {
            return addLoggerMBean((String) params[0]);
        } else {
            throw new ReflectionException(new NoSuchMethodException(operationName), new StringBuffer().append("Cannot find the operation ").append(operationName).append(" in ").append(this.dClassName).toString());
        }
    }

    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke a getter of ").append(this.dClassName).append(" with null attribute name").toString());
        }
        log.debug(new StringBuffer().append("Called getAttribute with [").append(attributeName).append("].").toString());
        if (attributeName.equals(THRESHOLD)) {
            return this.hierarchy.getThreshold();
        }
        if (attributeName.startsWith("logger")) {
            int k = attributeName.indexOf("%3D");
            String val = attributeName;
            if (k > 0) {
                val = new StringBuffer().append(attributeName.substring(0, k)).append('=').append(attributeName.substring(k + 3)).toString();
            }
            try {
                return new ObjectName(new StringBuffer().append("log4j:").append(val).toString());
            } catch (JMException e) {
                log.error(new StringBuffer().append("Could not create ObjectName").append(val).toString());
            } catch (RuntimeException e2) {
                log.error(new StringBuffer().append("Could not create ObjectName").append(val).toString());
            }
        }
        throw new AttributeNotFoundException(new StringBuffer().append("Cannot find ").append(attributeName).append(" attribute in ").append(this.dClassName).toString());
    }

    public void addAppenderEvent(Category logger, Appender appender) {
        log.debug(new StringBuffer().append("addAppenderEvent called: logger=").append(logger.getName()).append(", appender=").append(appender.getName()).toString());
        Notification n = new Notification(new StringBuffer().append(ADD_APPENDER).append(logger.getName()).toString(), this, 0);
        n.setUserData(appender);
        log.debug("sending notification.");
        this.nbs.sendNotification(n);
    }

    public void removeAppenderEvent(Category cat, Appender appender) {
        log.debug(new StringBuffer().append("removeAppenderCalled: logger=").append(cat.getName()).append(", appender=").append(appender.getName()).toString());
    }

    public void postRegister(Boolean registrationDone) {
        log.debug("postRegister is called.");
        this.hierarchy.addHierarchyEventListener(this);
        addLoggerMBean(this.hierarchy.getRootLogger());
    }

    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        this.nbs.removeNotificationListener(listener);
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), new StringBuffer().append("Cannot invoke a setter of ").append(this.dClassName).append(" with null attribute").toString());
        }
        String name = attribute.getName();
        Object value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke the setter of ").append(this.dClassName).append(" with null attribute name").toString());
        } else if (name.equals(THRESHOLD)) {
            this.hierarchy.setThreshold(OptionConverter.toLevel((String) value, this.hierarchy.getThreshold()));
        }
    }
}
