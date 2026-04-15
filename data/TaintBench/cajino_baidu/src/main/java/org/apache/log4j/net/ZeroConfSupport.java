package org.apache.log4j.net;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.apache.log4j.helpers.LogLog;

public class ZeroConfSupport {
    static Class class$java$lang$String;
    static Class class$java$util$Hashtable;
    static Class class$java$util$Map;
    private static Object jmDNS = initializeJMDNS();
    private static Class jmDNSClass;
    private static Class serviceInfoClass;
    Object serviceInfo;

    public ZeroConfSupport(String zone, int port, String name, Map properties) {
        boolean isVersion3 = false;
        try {
            jmDNSClass.getMethod("create", null);
            isVersion3 = true;
        } catch (NoSuchMethodException e) {
        }
        if (isVersion3) {
            LogLog.debug("using JmDNS version 3 to construct serviceInfo instance");
            this.serviceInfo = buildServiceInfoVersion3(zone, port, name, properties);
            return;
        }
        LogLog.debug("using JmDNS version 1.0 to construct serviceInfo instance");
        this.serviceInfo = buildServiceInfoVersion1(zone, port, name, properties);
    }

    public ZeroConfSupport(String zone, int port, String name) {
        this(zone, port, name, new HashMap());
    }

    private static Object createJmDNSVersion1() {
        try {
            return jmDNSClass.newInstance();
        } catch (InstantiationException e) {
            LogLog.warn("Unable to instantiate JMDNS", e);
        } catch (IllegalAccessException e2) {
            LogLog.warn("Unable to instantiate JMDNS", e2);
        }
        return null;
    }

    private static Object createJmDNSVersion3() {
        Object obj = null;
        try {
            return jmDNSClass.getMethod("create", null).invoke(null, null);
        } catch (IllegalAccessException e) {
            LogLog.warn("Unable to instantiate jmdns class", e);
            return obj;
        } catch (NoSuchMethodException e2) {
            LogLog.warn("Unable to access constructor", e2);
            return obj;
        } catch (InvocationTargetException e3) {
            LogLog.warn("Unable to call constructor", e3);
            return obj;
        }
    }

    private Object buildServiceInfoVersion1(String zone, int port, String name, Map properties) {
        Hashtable hashtableProperties = new Hashtable(properties);
        try {
            Class class$;
            Class[] args = new Class[6];
            if (class$java$lang$String == null) {
                class$ = class$("java.lang.String");
                class$java$lang$String = class$;
            } else {
                class$ = class$java$lang$String;
            }
            args[0] = class$;
            if (class$java$lang$String == null) {
                class$ = class$("java.lang.String");
                class$java$lang$String = class$;
            } else {
                class$ = class$java$lang$String;
            }
            args[1] = class$;
            args[2] = Integer.TYPE;
            args[3] = Integer.TYPE;
            args[4] = Integer.TYPE;
            if (class$java$util$Hashtable == null) {
                class$ = class$("java.util.Hashtable");
                class$java$util$Hashtable = class$;
            } else {
                class$ = class$java$util$Hashtable;
            }
            args[5] = class$;
            Object result = serviceInfoClass.getConstructor(args).newInstance(new Object[]{zone, name, new Integer(port), new Integer(0), new Integer(0), hashtableProperties});
            LogLog.debug(new StringBuffer().append("created serviceinfo: ").append(result).toString());
            return result;
        } catch (IllegalAccessException e) {
            LogLog.warn("Unable to construct ServiceInfo instance", e);
            return null;
        } catch (NoSuchMethodException e2) {
            LogLog.warn("Unable to get ServiceInfo constructor", e2);
            return null;
        } catch (InstantiationException e3) {
            LogLog.warn("Unable to construct ServiceInfo instance", e3);
            return null;
        } catch (InvocationTargetException e4) {
            LogLog.warn("Unable to construct ServiceInfo instance", e4);
            return null;
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private Object buildServiceInfoVersion3(String zone, int port, String name, Map properties) {
        try {
            Class class$;
            Class[] args = new Class[6];
            if (class$java$lang$String == null) {
                class$ = class$("java.lang.String");
                class$java$lang$String = class$;
            } else {
                class$ = class$java$lang$String;
            }
            args[0] = class$;
            if (class$java$lang$String == null) {
                class$ = class$("java.lang.String");
                class$java$lang$String = class$;
            } else {
                class$ = class$java$lang$String;
            }
            args[1] = class$;
            args[2] = Integer.TYPE;
            args[3] = Integer.TYPE;
            args[4] = Integer.TYPE;
            if (class$java$util$Map == null) {
                class$ = class$("java.util.Map");
                class$java$util$Map = class$;
            } else {
                class$ = class$java$util$Map;
            }
            args[5] = class$;
            Object result = serviceInfoClass.getMethod("create", args).invoke(null, new Object[]{zone, name, new Integer(port), new Integer(0), new Integer(0), properties});
            LogLog.debug(new StringBuffer().append("created serviceinfo: ").append(result).toString());
            return result;
        } catch (IllegalAccessException e) {
            LogLog.warn("Unable to invoke create method", e);
            return null;
        } catch (NoSuchMethodException e2) {
            LogLog.warn("Unable to find create method", e2);
            return null;
        } catch (InvocationTargetException e3) {
            LogLog.warn("Unable to invoke create method", e3);
            return null;
        }
    }

    public void advertise() {
        try {
            jmDNSClass.getMethod("registerService", new Class[]{serviceInfoClass}).invoke(jmDNS, new Object[]{this.serviceInfo});
            LogLog.debug(new StringBuffer().append("registered serviceInfo: ").append(this.serviceInfo).toString());
        } catch (IllegalAccessException e) {
            LogLog.warn("Unable to invoke registerService method", e);
        } catch (NoSuchMethodException e2) {
            LogLog.warn("No registerService method", e2);
        } catch (InvocationTargetException e3) {
            LogLog.warn("Unable to invoke registerService method", e3);
        }
    }

    public void unadvertise() {
        try {
            jmDNSClass.getMethod("unregisterService", new Class[]{serviceInfoClass}).invoke(jmDNS, new Object[]{this.serviceInfo});
            LogLog.debug(new StringBuffer().append("unregistered serviceInfo: ").append(this.serviceInfo).toString());
        } catch (IllegalAccessException e) {
            LogLog.warn("Unable to invoke unregisterService method", e);
        } catch (NoSuchMethodException e2) {
            LogLog.warn("No unregisterService method", e2);
        } catch (InvocationTargetException e3) {
            LogLog.warn("Unable to invoke unregisterService method", e3);
        }
    }

    private static Object initializeJMDNS() {
        try {
            jmDNSClass = Class.forName("javax.jmdns.JmDNS");
            serviceInfoClass = Class.forName("javax.jmdns.ServiceInfo");
        } catch (ClassNotFoundException e) {
            LogLog.warn("JmDNS or serviceInfo class not found", e);
        }
        boolean isVersion3 = false;
        try {
            jmDNSClass.getMethod("create", null);
            isVersion3 = true;
        } catch (NoSuchMethodException e2) {
        }
        if (isVersion3) {
            return createJmDNSVersion3();
        }
        return createJmDNSVersion1();
    }

    public static Object getJMDNSInstance() {
        return jmDNS;
    }
}
