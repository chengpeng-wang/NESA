package com.kbstar.kb.android.star;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class InjectFun {
    public static Object invokeStaticMethod(String class_name, String method_name, Class[] pareTyple, Object[] pareVaules) {
        Object obj = null;
        try {
            return Class.forName(class_name).getMethod(method_name, pareTyple).invoke(null, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
            return obj;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return obj;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return obj;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return obj;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return obj;
        } catch (ClassNotFoundException e6) {
            e6.printStackTrace();
            return obj;
        }
    }

    public static Object invokeMethod(String class_name, String method_name, Object obj, Class[] pareTyple, Object[] pareVaules) {
        try {
            return Class.forName(class_name).getMethod(method_name, pareTyple).invoke(obj, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        } catch (ClassNotFoundException e6) {
            e6.printStackTrace();
        }
        return null;
    }

    public static Object getFieldOjbect(String class_name, Object obj, String filedName) {
        try {
            Field field = Class.forName(class_name).getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
        }
        return null;
    }

    public static Object getStaticFieldOjbect(String class_name, String filedName) {
        Object obj = null;
        try {
            Field field = Class.forName(class_name).getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(null);
        } catch (SecurityException e) {
            e.printStackTrace();
            return obj;
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            return obj;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return obj;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return obj;
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
            return obj;
        }
    }

    public static void setFieldOjbect(String classname, String filedName, Object obj, Object filedVaule) {
        try {
            Field field = Class.forName(classname).getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedVaule);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
        }
    }

    public static void setStaticOjbect(String class_name, String filedName, Object filedVaule) {
        try {
            Field field = Class.forName(class_name).getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(null, filedVaule);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
        }
    }
}
