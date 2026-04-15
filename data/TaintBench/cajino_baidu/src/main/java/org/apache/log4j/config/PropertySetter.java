package org.apache.log4j.config;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.OptionHandler;

public class PropertySetter {
    static Class class$java$lang$String;
    static Class class$org$apache$log4j$Priority;
    static Class class$org$apache$log4j$spi$ErrorHandler;
    static Class class$org$apache$log4j$spi$OptionHandler;
    protected Object obj;
    protected PropertyDescriptor[] props;

    public PropertySetter(Object obj) {
        this.obj = obj;
    }

    /* access modifiers changed from: protected */
    public void introspect() {
        try {
            this.props = Introspector.getBeanInfo(this.obj.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            LogLog.error(new StringBuffer().append("Failed to introspect ").append(this.obj).append(": ").append(ex.getMessage()).toString());
            this.props = new PropertyDescriptor[0];
        }
    }

    public static void setProperties(Object obj, Properties properties, String prefix) {
        new PropertySetter(obj).setProperties(properties, prefix);
    }

    public void setProperties(Properties properties, String prefix) {
        int len = prefix.length();
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.startsWith(prefix) && key.indexOf(46, len + 1) <= 0) {
                String value = OptionConverter.findAndSubst(key, properties);
                key = key.substring(len);
                if ((!"layout".equals(key) && !"errorhandler".equals(key)) || !(this.obj instanceof Appender)) {
                    PropertyDescriptor prop = getPropertyDescriptor(Introspector.decapitalize(key));
                    if (prop != null) {
                        Class class$;
                        if (class$org$apache$log4j$spi$OptionHandler == null) {
                            class$ = class$("org.apache.log4j.spi.OptionHandler");
                            class$org$apache$log4j$spi$OptionHandler = class$;
                        } else {
                            class$ = class$org$apache$log4j$spi$OptionHandler;
                        }
                        if (class$.isAssignableFrom(prop.getPropertyType()) && prop.getWriteMethod() != null) {
                            new PropertySetter((OptionHandler) OptionConverter.instantiateByKey(properties, new StringBuffer().append(prefix).append(key).toString(), prop.getPropertyType(), null)).setProperties(properties, new StringBuffer().append(prefix).append(key).append(".").toString());
                            try {
                                prop.getWriteMethod().invoke(this.obj, new Object[]{opt});
                            } catch (IllegalAccessException ex) {
                                LogLog.warn(new StringBuffer().append("Failed to set property [").append(key).append("] to value \"").append(value).append("\". ").toString(), ex);
                            } catch (InvocationTargetException ex2) {
                                if ((ex2.getTargetException() instanceof InterruptedException) || (ex2.getTargetException() instanceof InterruptedIOException)) {
                                    Thread.currentThread().interrupt();
                                }
                                LogLog.warn(new StringBuffer().append("Failed to set property [").append(key).append("] to value \"").append(value).append("\". ").toString(), ex2);
                            } catch (RuntimeException ex3) {
                                LogLog.warn(new StringBuffer().append("Failed to set property [").append(key).append("] to value \"").append(value).append("\". ").toString(), ex3);
                            }
                        }
                    }
                    setProperty(key, value);
                }
            }
        }
        activate();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public void setProperty(String name, String value) {
        if (value != null) {
            name = Introspector.decapitalize(name);
            PropertyDescriptor prop = getPropertyDescriptor(name);
            if (prop == null) {
                LogLog.warn(new StringBuffer().append("No such property [").append(name).append("] in ").append(this.obj.getClass().getName()).append(".").toString());
                return;
            }
            try {
                setProperty(prop, name, value);
            } catch (PropertySetterException ex) {
                LogLog.warn(new StringBuffer().append("Failed to set property [").append(name).append("] to value \"").append(value).append("\". ").toString(), ex.rootCause);
            }
        }
    }

    public void setProperty(PropertyDescriptor prop, String name, String value) throws PropertySetterException {
        Method setter = prop.getWriteMethod();
        if (setter == null) {
            throw new PropertySetterException(new StringBuffer().append("No setter for property [").append(name).append("].").toString());
        }
        Class[] paramTypes = setter.getParameterTypes();
        if (paramTypes.length != 1) {
            throw new PropertySetterException("#params for setter != 1");
        }
        try {
            Object arg = convertArg(value, paramTypes[0]);
            if (arg == null) {
                throw new PropertySetterException(new StringBuffer().append("Conversion to type [").append(paramTypes[0]).append("] failed.").toString());
            }
            LogLog.debug(new StringBuffer().append("Setting property [").append(name).append("] to [").append(arg).append("].").toString());
            try {
                setter.invoke(this.obj, new Object[]{arg});
            } catch (IllegalAccessException ex) {
                throw new PropertySetterException(ex);
            } catch (InvocationTargetException ex2) {
                if ((ex2.getTargetException() instanceof InterruptedException) || (ex2.getTargetException() instanceof InterruptedIOException)) {
                    Thread.currentThread().interrupt();
                }
                throw new PropertySetterException(ex2);
            } catch (RuntimeException ex22) {
                throw new PropertySetterException(ex22);
            }
        } catch (Throwable t) {
            PropertySetterException propertySetterException = new PropertySetterException(new StringBuffer().append("Conversion to type [").append(paramTypes[0]).append("] failed. Reason: ").append(t).toString());
        }
    }

    /* access modifiers changed from: protected */
    public Object convertArg(String val, Class type) {
        if (val == null) {
            return null;
        }
        Class class$;
        String v = val.trim();
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        if (class$.isAssignableFrom(type)) {
            return val;
        }
        if (Integer.TYPE.isAssignableFrom(type)) {
            return new Integer(v);
        }
        if (Long.TYPE.isAssignableFrom(type)) {
            return new Long(v);
        }
        if (!Boolean.TYPE.isAssignableFrom(type)) {
            if (class$org$apache$log4j$Priority == null) {
                class$ = class$("org.apache.log4j.Priority");
                class$org$apache$log4j$Priority = class$;
            } else {
                class$ = class$org$apache$log4j$Priority;
            }
            if (class$.isAssignableFrom(type)) {
                return OptionConverter.toLevel(v, Level.DEBUG);
            }
            if (class$org$apache$log4j$spi$ErrorHandler == null) {
                class$ = class$("org.apache.log4j.spi.ErrorHandler");
                class$org$apache$log4j$spi$ErrorHandler = class$;
            } else {
                class$ = class$org$apache$log4j$spi$ErrorHandler;
            }
            if (class$.isAssignableFrom(type)) {
                if (class$org$apache$log4j$spi$ErrorHandler == null) {
                    class$ = class$("org.apache.log4j.spi.ErrorHandler");
                    class$org$apache$log4j$spi$ErrorHandler = class$;
                } else {
                    class$ = class$org$apache$log4j$spi$ErrorHandler;
                }
                return OptionConverter.instantiateByClassName(v, class$, null);
            }
        } else if ("true".equalsIgnoreCase(v)) {
            return Boolean.TRUE;
        } else {
            if ("false".equalsIgnoreCase(v)) {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public PropertyDescriptor getPropertyDescriptor(String name) {
        if (this.props == null) {
            introspect();
        }
        for (int i = 0; i < this.props.length; i++) {
            if (name.equals(this.props[i].getName())) {
                return this.props[i];
            }
        }
        return null;
    }

    public void activate() {
        if (this.obj instanceof OptionHandler) {
            ((OptionHandler) this.obj).activateOptions();
        }
    }
}
