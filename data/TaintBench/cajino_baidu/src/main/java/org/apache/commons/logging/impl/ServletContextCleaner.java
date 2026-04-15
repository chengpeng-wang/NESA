package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

public class ServletContextCleaner implements ServletContextListener {
    static Class class$java$lang$ClassLoader;
    private Class[] RELEASE_SIGNATURE;

    public ServletContextCleaner() {
        Class class$;
        Class[] clsArr = new Class[1];
        if (class$java$lang$ClassLoader == null) {
            class$ = class$("java.lang.ClassLoader");
            class$java$lang$ClassLoader = class$;
        } else {
            class$ = class$java$lang$ClassLoader;
        }
        clsArr[0] = class$;
        this.RELEASE_SIGNATURE = clsArr;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        Object[] params = new Object[]{tccl};
        ClassLoader loader = tccl;
        while (loader != null) {
            try {
                Class logFactoryClass = loader.loadClass(LogFactory.FACTORY_PROPERTY);
                logFactoryClass.getMethod("release", this.RELEASE_SIGNATURE).invoke(null, params);
                loader = logFactoryClass.getClassLoader().getParent();
            } catch (ClassNotFoundException e) {
                loader = null;
            } catch (NoSuchMethodException e2) {
                System.err.println("LogFactory instance found which does not support release method!");
                loader = null;
            } catch (IllegalAccessException e3) {
                System.err.println("LogFactory instance found which is not accessable!");
                loader = null;
            } catch (InvocationTargetException e4) {
                System.err.println("LogFactory instance release method failed!");
                loader = null;
            }
        }
        LogFactory.release(tccl);
    }

    public void contextInitialized(ServletContextEvent sce) {
    }
}
