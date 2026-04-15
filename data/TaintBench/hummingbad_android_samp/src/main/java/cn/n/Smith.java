package cn.n;

import java.lang.reflect.Field;

public class Smith<T> {
    private Field field;
    private String fieldName;
    private boolean inited;
    private Object obj;

    public Smith(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException("obj cannot be null");
        }
        this.obj = obj;
        this.fieldName = fieldName;
    }

    private void prepare() {
        if (!this.inited) {
            this.inited = true;
            Class<?> c = this.obj.getClass();
            while (c != null) {
                try {
                    Field f = c.getDeclaredField(this.fieldName);
                    f.setAccessible(true);
                    this.field = f;
                    c = c.getSuperclass();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    c = c.getSuperclass();
                } catch (Throwable th) {
                    c = c.getSuperclass();
                    throw th;
                }
            }
        }
    }

    public T get() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (this.field == null) {
            throw new NoSuchFieldException();
        }
        try {
            return this.field.get(this.obj);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
    }

    public void set(T val) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (this.field == null) {
            throw new NoSuchFieldException();
        }
        this.field.set(this.obj, val);
    }
}
