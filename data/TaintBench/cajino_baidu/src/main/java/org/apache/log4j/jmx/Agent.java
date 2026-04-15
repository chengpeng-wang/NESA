package org.apache.log4j.jmx;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import org.apache.log4j.Logger;

public class Agent {
    static Class class$org$apache$log4j$jmx$Agent;
    static Logger log;

    static {
        Class class$;
        if (class$org$apache$log4j$jmx$Agent == null) {
            class$ = class$("org.apache.log4j.jmx.Agent");
            class$org$apache$log4j$jmx$Agent = class$;
        } else {
            class$ = class$org$apache$log4j$jmx$Agent;
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

    private static Object createServer() {
        try {
            return Class.forName("com.sun.jdmk.comm.HtmlAdapterServer").newInstance();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.toString());
        } catch (InstantiationException ex2) {
            throw new RuntimeException(ex2.toString());
        } catch (IllegalAccessException ex3) {
            throw new RuntimeException(ex3.toString());
        }
    }

    private static void startServer(Object server) {
        try {
            server.getClass().getMethod("start", new Class[0]).invoke(server, new Object[0]);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getTargetException();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause != null) {
                if ((cause instanceof InterruptedException) || (cause instanceof InterruptedIOException)) {
                    Thread.currentThread().interrupt();
                }
                throw new RuntimeException(cause.toString());
            } else {
                throw new RuntimeException();
            }
        } catch (NoSuchMethodException ex2) {
            throw new RuntimeException(ex2.toString());
        } catch (IllegalAccessException ex3) {
            throw new RuntimeException(ex3.toString());
        }
    }

    public void start() {
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        Object html = createServer();
        try {
            log.info("Registering HtmlAdaptorServer instance.");
            server.registerMBean(html, new ObjectName("Adaptor:name=html,port=8082"));
            log.info("Registering HierarchyDynamicMBean instance.");
            server.registerMBean(new HierarchyDynamicMBean(), new ObjectName("log4j:hiearchy=default"));
            startServer(html);
        } catch (JMException e) {
            log.error("Problem while registering MBeans instances.", e);
        } catch (RuntimeException e2) {
            log.error("Problem while registering MBeans instances.", e2);
        }
    }
}
