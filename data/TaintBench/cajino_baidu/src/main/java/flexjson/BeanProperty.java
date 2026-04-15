package flexjson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeanProperty {
    private BeanAnalyzer bean;
    private String name;
    protected Field property;
    private Class propertyType;
    protected Method readMethod;
    protected Method writeMethod;
    protected Map<Class<?>, Method> writeMethods = new HashMap();

    public BeanProperty(String name, BeanAnalyzer bean) {
        this.name = name;
        this.bean = bean;
        this.property = bean.getDeclaredField(name);
    }

    public BeanProperty(Field property, BeanAnalyzer bean) {
        this.name = property.getName();
        this.bean = bean;
        this.property = property;
        this.propertyType = property.getType();
    }

    public String getName() {
        return this.name;
    }

    public Field getProperty() {
        return this.property;
    }

    public Class getPropertyType() {
        return this.propertyType;
    }

    public Method getReadMethod() {
        if (this.readMethod == null && this.bean.getSuperBean() != null && this.bean.getSuperBean().hasProperty(this.name)) {
            return this.bean.getSuperBean().getProperty(this.name).getReadMethod();
        }
        return this.readMethod;
    }

    public Method getWriteMethod() {
        if (this.writeMethod == null) {
            this.writeMethod = (Method) this.writeMethods.get(this.propertyType);
            if (this.writeMethod == null && this.bean.getSuperBean() != null && this.bean.getSuperBean().hasProperty(this.name)) {
                return this.bean.getSuperBean().getProperty(this.name).getWriteMethod();
            }
        }
        return this.writeMethod;
    }

    public Collection<Method> getWriteMethods() {
        return this.writeMethods.values();
    }

    public void addWriteMethod(Method method) {
        Class clazz = method.getParameterTypes()[0];
        if (this.propertyType == null) {
            this.propertyType = clazz;
        }
        this.writeMethods.put(clazz, method);
        method.setAccessible(true);
    }

    public void setReadMethod(Method method) {
        if (this.propertyType == null) {
            this.propertyType = method.getReturnType();
            this.readMethod = method;
            this.readMethod.setAccessible(true);
        } else if (this.propertyType == method.getReturnType()) {
            this.readMethod = method;
            this.readMethod.setAccessible(true);
        }
    }

    public Boolean isAnnotated() {
        Method rm = getReadMethod();
        if (rm != null) {
            if (rm.isAnnotationPresent(JSON.class)) {
                return Boolean.valueOf(((JSON) rm.getAnnotation(JSON.class)).include());
            }
        } else if (this.property != null && this.property.isAnnotationPresent(JSON.class)) {
            return Boolean.valueOf(((JSON) this.property.getAnnotation(JSON.class)).include());
        }
        return null;
    }

    public Object getValue(Object instance) throws InvocationTargetException, IllegalAccessException {
        Method rm = getReadMethod();
        if (rm != null) {
            return rm.invoke(instance, (Object[]) null);
        }
        if (this.property != null) {
            return this.property.get(instance);
        }
        return null;
    }

    public Boolean isReadable() {
        Method rm = getReadMethod();
        boolean z = ((rm == null || Modifier.isStatic(rm.getModifiers())) && (this.property == null || Modifier.isStatic(this.property.getModifiers()) || Modifier.isTransient(this.property.getModifiers()))) ? false : true;
        return Boolean.valueOf(z);
    }

    public Boolean isWritable() {
        Method wm = getWriteMethod();
        boolean z = (wm != null && Modifier.isPublic(wm.getModifiers())) || !(this.property == null || !Modifier.isPublic(this.property.getModifiers()) || Modifier.isTransient(this.property.getModifiers()));
        return Boolean.valueOf(z);
    }
}
