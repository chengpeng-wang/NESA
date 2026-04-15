package com.esotericsoftware.reflectasm;

import java.lang.reflect.Method;
import java.util.ArrayList;

class AccessClassLoader extends ClassLoader {
    private static final ArrayList<AccessClassLoader> accessClassLoaders = new ArrayList();

    static AccessClassLoader get(Class cls) {
        AccessClassLoader accessClassLoader;
        ClassLoader classLoader = cls.getClassLoader();
        synchronized (accessClassLoaders) {
            int size = accessClassLoaders.size();
            for (int i = 0; i < size; i++) {
                accessClassLoader = (AccessClassLoader) accessClassLoaders.get(i);
                if (accessClassLoader.getParent() == classLoader) {
                    break;
                }
            }
            accessClassLoader = new AccessClassLoader(classLoader);
            accessClassLoaders.add(accessClassLoader);
        }
        return accessClassLoader;
    }

    private AccessClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
        Class<?> cls;
        if (str.equals(FieldAccess.class.getName())) {
            cls = FieldAccess.class;
        } else if (str.equals(MethodAccess.class.getName())) {
            cls = MethodAccess.class;
        } else if (str.equals(ConstructorAccess.class.getName())) {
            cls = ConstructorAccess.class;
        } else {
            cls = super.loadClass(str, z);
        }
        return cls;
    }

    /* access modifiers changed from: 0000 */
    public Class<?> defineClass(String str, byte[] bArr) throws ClassFormatError {
        try {
            Method declaredMethod = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE});
            declaredMethod.setAccessible(true);
            return (Class) declaredMethod.invoke(getParent(), new Object[]{str, bArr, Integer.valueOf(0), Integer.valueOf(bArr.length)});
        } catch (Exception e) {
            return defineClass(str, bArr, 0, bArr.length);
        }
    }
}
