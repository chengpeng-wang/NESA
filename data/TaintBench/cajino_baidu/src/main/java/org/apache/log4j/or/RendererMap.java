package org.apache.log4j.or;

import java.util.Hashtable;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.RendererSupport;

public class RendererMap {
    static Class class$org$apache$log4j$or$ObjectRenderer;
    static ObjectRenderer defaultRenderer = new DefaultRenderer();
    Hashtable map = new Hashtable();

    public static void addRenderer(RendererSupport repository, String renderedClassName, String renderingClassName) {
        Class class$;
        LogLog.debug(new StringBuffer().append("Rendering class: [").append(renderingClassName).append("], Rendered class: [").append(renderedClassName).append("].").toString());
        if (class$org$apache$log4j$or$ObjectRenderer == null) {
            class$ = class$("org.apache.log4j.or.ObjectRenderer");
            class$org$apache$log4j$or$ObjectRenderer = class$;
        } else {
            class$ = class$org$apache$log4j$or$ObjectRenderer;
        }
        ObjectRenderer renderer = (ObjectRenderer) OptionConverter.instantiateByClassName(renderingClassName, class$, null);
        if (renderer == null) {
            LogLog.error(new StringBuffer().append("Could not instantiate renderer [").append(renderingClassName).append("].").toString());
            return;
        }
        try {
            repository.setRenderer(Loader.loadClass(renderedClassName), renderer);
        } catch (ClassNotFoundException e) {
            LogLog.error(new StringBuffer().append("Could not find class [").append(renderedClassName).append("].").toString(), e);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public String findAndRender(Object o) {
        if (o == null) {
            return null;
        }
        return get(o.getClass()).doRender(o);
    }

    public ObjectRenderer get(Object o) {
        if (o == null) {
            return null;
        }
        return get(o.getClass());
    }

    public ObjectRenderer get(Class clazz) {
        for (Class c = clazz; c != null; c = c.getSuperclass()) {
            ObjectRenderer r = (ObjectRenderer) this.map.get(c);
            if (r != null) {
                return r;
            }
            r = searchInterfaces(c);
            if (r != null) {
                return r;
            }
        }
        return defaultRenderer;
    }

    /* access modifiers changed from: 0000 */
    public ObjectRenderer searchInterfaces(Class c) {
        ObjectRenderer r = (ObjectRenderer) this.map.get(c);
        if (r != null) {
            return r;
        }
        Class[] ia = c.getInterfaces();
        for (Class searchInterfaces : ia) {
            r = searchInterfaces(searchInterfaces);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    public ObjectRenderer getDefaultRenderer() {
        return defaultRenderer;
    }

    public void clear() {
        this.map.clear();
    }

    public void put(Class clazz, ObjectRenderer or) {
        this.map.put(clazz, or);
    }
}
