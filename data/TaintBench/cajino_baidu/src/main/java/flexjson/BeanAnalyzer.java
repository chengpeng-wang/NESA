package flexjson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BeanAnalyzer {
    private static ThreadLocal<Map<Class, BeanAnalyzer>> cache = new ThreadLocal();
    private Class clazz;
    private Map<String, BeanProperty> properties;
    private BeanAnalyzer superBean;

    public static BeanAnalyzer analyze(Class clazz) {
        if (cache.get() == null) {
            cache.set(new HashMap());
        }
        if (clazz == null) {
            return null;
        }
        if (!((Map) cache.get()).containsKey(clazz)) {
            ((Map) cache.get()).put(clazz, new BeanAnalyzer(clazz));
        }
        return (BeanAnalyzer) ((Map) cache.get()).get(clazz);
    }

    public static void clearCache() {
        cache.remove();
    }

    protected BeanAnalyzer(Class clazz) {
        this.clazz = clazz;
        this.superBean = analyze(clazz.getSuperclass());
        populateProperties();
    }

    private void populateProperties() {
        this.properties = new TreeMap();
        for (Method method : this.clazz.getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                int numberOfArgs = method.getParameterTypes().length;
                String name = method.getName();
                if (name.length() > 3 || name.startsWith("is")) {
                    String property;
                    if (numberOfArgs == 0) {
                        if (name.startsWith("get")) {
                            property = uncapitalize(name.substring(3));
                            if (!this.properties.containsKey(property)) {
                                this.properties.put(property, new BeanProperty(property, this));
                            }
                            ((BeanProperty) this.properties.get(property)).setReadMethod(method);
                        } else if (name.startsWith("is")) {
                            property = uncapitalize(name.substring(2));
                            if (!this.properties.containsKey(property)) {
                                this.properties.put(property, new BeanProperty(property, this));
                            }
                            ((BeanProperty) this.properties.get(property)).setReadMethod(method);
                        }
                    } else if (numberOfArgs == 1 && name.startsWith("set")) {
                        property = uncapitalize(name.substring(3));
                        if (!this.properties.containsKey(property)) {
                            this.properties.put(property, new BeanProperty(property, this));
                        }
                        ((BeanProperty) this.properties.get(property)).addWriteMethod(method);
                    }
                }
            }
        }
        for (Field publicProperties : this.clazz.getFields()) {
            int modifiers = publicProperties.getModifiers();
            if (!(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || this.properties.containsKey(publicProperties.getName()))) {
                this.properties.put(publicProperties.getName(), new BeanProperty(publicProperties, this));
            }
        }
    }

    public BeanAnalyzer getSuperBean() {
        return this.superBean;
    }

    private String uncapitalize(String value) {
        if (value.length() < 2) {
            return value.toLowerCase();
        }
        return (Character.isUpperCase(value.charAt(0)) && Character.isUpperCase(value.charAt(1))) ? value : Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    public BeanProperty getProperty(String name) {
        for (BeanAnalyzer current = this; current != null; current = current.superBean) {
            BeanProperty property = (BeanProperty) current.properties.get(name);
            if (property != null) {
                return property;
            }
        }
        return null;
    }

    public Collection<BeanProperty> getProperties() {
        Map<String, BeanProperty> properties = new TreeMap(this.properties);
        for (BeanAnalyzer current = this.superBean; current != null; current = current.superBean) {
            merge(properties, current.properties);
        }
        return properties.values();
    }

    private void merge(Map<String, BeanProperty> destination, Map<String, BeanProperty> source) {
        for (String key : source.keySet()) {
            if (!destination.containsKey(key)) {
                destination.put(key, source.get(key));
            }
        }
    }

    public boolean hasProperty(String name) {
        return this.properties.containsKey(name);
    }

    /* access modifiers changed from: protected */
    public Field getDeclaredField(String name) {
        try {
            return this.clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
