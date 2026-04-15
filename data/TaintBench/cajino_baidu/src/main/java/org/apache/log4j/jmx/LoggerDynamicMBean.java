package org.apache.log4j.jmx;

import java.beans.IntrospectionException;
import java.util.Enumeration;
import java.util.Vector;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.DateLayout;
import org.apache.log4j.helpers.OptionConverter;

public class LoggerDynamicMBean extends AbstractDynamicMBean implements NotificationListener {
    private static Logger cat;
    static Class class$org$apache$log4j$Appender;
    static Class class$org$apache$log4j$jmx$LoggerDynamicMBean;
    private Vector dAttributes = new Vector();
    private String dClassName = getClass().getName();
    private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
    private String dDescription = "This MBean acts as a management facade for a org.apache.log4j.Logger instance.";
    private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[1];
    private Logger logger;

    static {
        Class class$;
        if (class$org$apache$log4j$jmx$LoggerDynamicMBean == null) {
            class$ = class$("org.apache.log4j.jmx.LoggerDynamicMBean");
            class$org$apache$log4j$jmx$LoggerDynamicMBean = class$;
        } else {
            class$ = class$org$apache$log4j$jmx$LoggerDynamicMBean;
        }
        cat = Logger.getLogger(class$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public LoggerDynamicMBean(Logger logger) {
        this.logger = logger;
        buildDynamicMBeanInfo();
    }

    public void handleNotification(Notification notification, Object handback) {
        cat.debug(new StringBuffer().append("Received notification: ").append(notification.getType()).toString());
        registerAppenderMBean((Appender) notification.getUserData());
    }

    private void buildDynamicMBeanInfo() {
        this.dConstructors[0] = new MBeanConstructorInfo("HierarchyDynamicMBean(): Constructs a HierarchyDynamicMBean instance", getClass().getConstructors()[0]);
        this.dAttributes.add(new MBeanAttributeInfo("name", "java.lang.String", "The name of this Logger.", true, false, false));
        this.dAttributes.add(new MBeanAttributeInfo(LogFactory.PRIORITY_KEY, "java.lang.String", "The priority of this logger.", true, true, false));
        this.dOperations[0] = new MBeanOperationInfo("addAppender", "addAppender(): add an appender", new MBeanParameterInfo[]{new MBeanParameterInfo("class name", "java.lang.String", "add an appender to this logger"), new MBeanParameterInfo("appender name", "java.lang.String", "name of the appender")}, "void", 1);
    }

    /* access modifiers changed from: protected */
    public Logger getLogger() {
        return this.logger;
    }

    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[this.dAttributes.size()];
        this.dAttributes.toArray(attribs);
        return new MBeanInfo(this.dClassName, this.dDescription, attribs, this.dConstructors, this.dOperations, new MBeanNotificationInfo[0]);
    }

    public Object invoke(String operationName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        if (!operationName.equals("addAppender")) {
            return null;
        }
        addAppender((String) params[0], (String) params[1]);
        return "Hello world.";
    }

    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke a getter of ").append(this.dClassName).append(" with null attribute name").toString());
        } else if (attributeName.equals("name")) {
            return this.logger.getName();
        } else {
            if (attributeName.equals(LogFactory.PRIORITY_KEY)) {
                Level l = this.logger.getLevel();
                if (l == null) {
                    return null;
                }
                return l.toString();
            }
            if (attributeName.startsWith("appender=")) {
                try {
                    return new ObjectName(new StringBuffer().append("log4j:").append(attributeName).toString());
                } catch (MalformedObjectNameException e) {
                    cat.error(new StringBuffer().append("Could not create ObjectName").append(attributeName).toString());
                } catch (RuntimeException e2) {
                    cat.error(new StringBuffer().append("Could not create ObjectName").append(attributeName).toString());
                }
            }
            throw new AttributeNotFoundException(new StringBuffer().append("Cannot find ").append(attributeName).append(" attribute in ").append(this.dClassName).toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void addAppender(String appenderClass, String appenderName) {
        Class class$;
        cat.debug(new StringBuffer().append("addAppender called with ").append(appenderClass).append(", ").append(appenderName).toString());
        if (class$org$apache$log4j$Appender == null) {
            class$ = class$("org.apache.log4j.Appender");
            class$org$apache$log4j$Appender = class$;
        } else {
            class$ = class$org$apache$log4j$Appender;
        }
        Appender appender = (Appender) OptionConverter.instantiateByClassName(appenderClass, class$, null);
        appender.setName(appenderName);
        this.logger.addAppender(appender);
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), new StringBuffer().append("Cannot invoke a setter of ").append(this.dClassName).append(" with null attribute").toString());
        }
        String name = attribute.getName();
        String value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke the setter of ").append(this.dClassName).append(" with null attribute name").toString());
        } else if (!name.equals(LogFactory.PRIORITY_KEY)) {
            throw new AttributeNotFoundException(new StringBuffer().append("Attribute ").append(name).append(" not found in ").append(getClass().getName()).toString());
        } else if (value instanceof String) {
            String s = value;
            Level p = this.logger.getLevel();
            if (s.equalsIgnoreCase(DateLayout.NULL_DATE_FORMAT)) {
                p = null;
            } else {
                p = OptionConverter.toLevel(s, p);
            }
            this.logger.setLevel(p);
        }
    }

    /* access modifiers changed from: 0000 */
    public void appenderMBeanRegistration() {
        Enumeration enumeration = this.logger.getAllAppenders();
        while (enumeration.hasMoreElements()) {
            registerAppenderMBean((Appender) enumeration.nextElement());
        }
    }

    /* access modifiers changed from: 0000 */
    public void registerAppenderMBean(Appender appender) {
        JMException e;
        IntrospectionException e2;
        RuntimeException e3;
        String name = AbstractDynamicMBean.getAppenderName(appender);
        cat.debug(new StringBuffer().append("Adding AppenderMBean for appender named ").append(name).toString());
        try {
            AppenderDynamicMBean appenderMBean = new AppenderDynamicMBean(appender);
            ObjectName objectName = new ObjectName("log4j", "appender", name);
            ObjectName objectName2;
            try {
                if (!this.server.isRegistered(objectName)) {
                    registerMBean(appenderMBean, objectName);
                    this.dAttributes.add(new MBeanAttributeInfo(new StringBuffer().append("appender=").append(name).toString(), "javax.management.ObjectName", new StringBuffer().append("The ").append(name).append(" appender.").toString(), true, true, false));
                }
                objectName2 = objectName;
            } catch (JMException e4) {
                e = e4;
                objectName2 = objectName;
                cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
            } catch (IntrospectionException e5) {
                e2 = e5;
                objectName2 = objectName;
                cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e2);
            } catch (RuntimeException e6) {
                e3 = e6;
                objectName2 = objectName;
                cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e3);
            }
        } catch (JMException e7) {
            e = e7;
            cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
        } catch (IntrospectionException e8) {
            e2 = e8;
            cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e2);
        } catch (RuntimeException e9) {
            e3 = e9;
            cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e3);
        }
    }

    public void postRegister(Boolean registrationDone) {
        appenderMBeanRegistration();
    }
}
