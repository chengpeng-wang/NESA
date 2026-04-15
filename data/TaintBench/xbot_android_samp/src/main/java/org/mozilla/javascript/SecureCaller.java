package org.mozilla.javascript;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class SecureCaller {
    private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers = new WeakHashMap();
    /* access modifiers changed from: private|static|final */
    public static final byte[] secureCallerImplBytecode = loadBytecode();

    private static class SecureClassLoaderImpl extends SecureClassLoader {
        SecureClassLoaderImpl(ClassLoader parent) {
            super(parent);
        }

        /* access modifiers changed from: 0000 */
        public Class<?> defineAndLinkClass(String name, byte[] bytes, CodeSource cs) {
            Class<?> cl = defineClass(name, bytes, 0, bytes.length, cs);
            resolveClass(cl);
            return cl;
        }
    }

    public abstract Object call(Callable callable, Context context, Scriptable scriptable, Scriptable scriptable2, Object[] objArr);

    static Object callSecurely(final CodeSource codeSource, Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        Map<ClassLoader, SoftReference<SecureCaller>> classLoaderMap;
        SecureCaller caller;
        final Thread thread = Thread.currentThread();
        final ClassLoader classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                return thread.getContextClassLoader();
            }
        });
        synchronized (callers) {
            classLoaderMap = (Map) callers.get(codeSource);
            if (classLoaderMap == null) {
                classLoaderMap = new WeakHashMap();
                callers.put(codeSource, classLoaderMap);
            }
        }
        synchronized (classLoaderMap) {
            SoftReference<SecureCaller> ref = (SoftReference) classLoaderMap.get(classLoader);
            if (ref != null) {
                caller = (SecureCaller) ref.get();
            } else {
                caller = null;
            }
            if (caller == null) {
                try {
                    caller = (SecureCaller) AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                        public Object run() throws Exception {
                            ClassLoader effectiveClassLoader;
                            Class<?> thisClass = getClass();
                            if (classLoader.loadClass(thisClass.getName()) != thisClass) {
                                effectiveClassLoader = thisClass.getClassLoader();
                            } else {
                                effectiveClassLoader = classLoader;
                            }
                            return new SecureClassLoaderImpl(effectiveClassLoader).defineAndLinkClass(SecureCaller.class.getName() + "Impl", SecureCaller.secureCallerImplBytecode, codeSource).newInstance();
                        }
                    });
                    classLoaderMap.put(classLoader, new SoftReference(caller));
                } catch (PrivilegedActionException ex) {
                    throw new UndeclaredThrowableException(ex.getCause());
                }
            }
        }
        return caller.call(callable, cx, scope, thisObj, args);
    }

    private static byte[] loadBytecode() {
        return (byte[]) AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                return SecureCaller.loadBytecodePrivileged();
            }
        });
    }

    /* access modifiers changed from: private|static */
    public static byte[] loadBytecodePrivileged() {
        InputStream in;
        try {
            in = SecureCaller.class.getResource("SecureCallerImpl.clazz").openStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while (true) {
                int r = in.read();
                if (r == -1) {
                    byte[] toByteArray = bout.toByteArray();
                    in.close();
                    return toByteArray;
                }
                bout.write(r);
            }
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        } catch (Throwable th) {
            in.close();
        }
    }
}
