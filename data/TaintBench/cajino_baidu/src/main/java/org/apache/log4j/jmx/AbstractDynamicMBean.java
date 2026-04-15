package org.apache.log4j.jmx;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public abstract class AbstractDynamicMBean implements DynamicMBean, MBeanRegistration {
    String dClassName;
    private final Vector mbeanList = new Vector();
    MBeanServer server;

    public abstract Object getAttribute(String str) throws AttributeNotFoundException, MBeanException, ReflectionException;

    public abstract Logger getLogger();

    public abstract MBeanInfo getMBeanInfo();

    public abstract Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException;

    public abstract void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException;

    protected static String getAppenderName(Appender appender) {
        String name = appender.getName();
        if (name == null || name.trim().length() == 0) {
            return appender.toString();
        }
        return name;
    }

    public AttributeList getAttributes(String[] attributeNames) {
        if (attributeNames == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"), new StringBuffer().append("Cannot invoke a getter of ").append(this.dClassName).toString());
        }
        AttributeList resultList = new AttributeList();
        if (attributeNames.length != 0) {
            for (int i = 0; i < attributeNames.length; i++) {
                try {
                    resultList.add(new Attribute(attributeNames[i], getAttribute(attributeNames[i])));
                } catch (JMException e) {
                    e.printStackTrace();
                } catch (RuntimeException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return resultList;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"), new StringBuffer().append("Cannot invoke a setter of ").append(this.dClassName).toString());
        }
        AttributeList resultList = new AttributeList();
        if (!attributes.isEmpty()) {
            Iterator i = attributes.iterator();
            while (i.hasNext()) {
                Attribute attr = (Attribute) i.next();
                try {
                    setAttribute(attr);
                    String name = attr.getName();
                    resultList.add(new Attribute(name, getAttribute(name)));
                } catch (JMException e) {
                    e.printStackTrace();
                } catch (RuntimeException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return resultList;
    }

    public void postDeregister() {
        getLogger().debug("postDeregister is called.");
    }

    public void postRegister(Boolean registrationDone) {
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) {
        getLogger().debug(new StringBuffer().append("preRegister called. Server=").append(server).append(", name=").append(name).toString());
        this.server = server;
        return name;
    }

    /* access modifiers changed from: protected */
    public void registerMBean(Object mbean, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        this.server.registerMBean(mbean, objectName);
        this.mbeanList.add(objectName);
    }

    public void preDeregister() {
        getLogger().debug("preDeregister called.");
        Enumeration iterator = this.mbeanList.elements();
        while (iterator.hasMoreElements()) {
            ObjectName name = (ObjectName) iterator.nextElement();
            try {
                this.server.unregisterMBean(name);
            } catch (InstanceNotFoundException e) {
                getLogger().warn(new StringBuffer().append("Missing MBean ").append(name.getCanonicalName()).toString());
            } catch (MBeanRegistrationException e2) {
                getLogger().warn(new StringBuffer().append("Failed unregistering ").append(name.getCanonicalName()).toString());
            }
        }
    }
}
