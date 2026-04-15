package org.mozilla.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Map;
import org.objectweb.asm.Opcodes;

public class Kit {
    private static Method Throwable_initCause;

    private static final class ComplexKey {
        private int hash;
        private Object key1;
        private Object key2;

        ComplexKey(Object key1, Object key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public boolean equals(Object anotherObj) {
            if (!(anotherObj instanceof ComplexKey)) {
                return false;
            }
            ComplexKey another = (ComplexKey) anotherObj;
            if (this.key1.equals(another.key1) && this.key2.equals(another.key2)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.hash == 0) {
                this.hash = this.key1.hashCode() ^ this.key2.hashCode();
            }
            return this.hash;
        }
    }

    static {
        Throwable_initCause = null;
        try {
            Throwable_initCause = classOrNull("java.lang.Throwable").getMethod("initCause", new Class[]{classOrNull("java.lang.Throwable")});
        } catch (Exception e) {
        }
    }

    public static Class<?> classOrNull(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException | IllegalArgumentException | LinkageError | SecurityException e) {
            return null;
        }
    }

    public static Class<?> classOrNull(ClassLoader loader, String className) {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException | IllegalArgumentException | LinkageError | SecurityException e) {
            return null;
        }
    }

    static Object newInstanceOrNull(Class<?> cl) {
        try {
            return cl.newInstance();
        } catch (IllegalAccessException | InstantiationException | LinkageError | SecurityException e) {
            return null;
        }
    }

    static boolean testIfCanLoadRhinoClasses(ClassLoader loader) {
        Class<?> testClass = ScriptRuntime.ContextFactoryClass;
        if (classOrNull(loader, testClass.getName()) != testClass) {
            return false;
        }
        return true;
    }

    public static RuntimeException initCause(RuntimeException ex, Throwable cause) {
        if (Throwable_initCause != null) {
            try {
                Throwable_initCause.invoke(ex, new Object[]{cause});
            } catch (Exception e) {
            }
        }
        return ex;
    }

    /* JADX WARNING: Missing block: B:3:0x0006, code skipped:
            if (r1 >= 0) goto L_0x0008;
     */
    public static int xDigitToInt(int r1, int r2) {
        /*
        r0 = 57;
        if (r1 > r0) goto L_0x000c;
    L_0x0004:
        r1 = r1 + -48;
        if (r1 < 0) goto L_0x0022;
    L_0x0008:
        r0 = r2 << 4;
        r0 = r0 | r1;
    L_0x000b:
        return r0;
    L_0x000c:
        r0 = 70;
        if (r1 > r0) goto L_0x0017;
    L_0x0010:
        r0 = 65;
        if (r0 > r1) goto L_0x0022;
    L_0x0014:
        r1 = r1 + -55;
        goto L_0x0008;
    L_0x0017:
        r0 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r1 > r0) goto L_0x0022;
    L_0x001b:
        r0 = 97;
        if (r0 > r1) goto L_0x0022;
    L_0x001f:
        r1 = r1 + -87;
        goto L_0x0008;
    L_0x0022:
        r0 = -1;
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.Kit.xDigitToInt(int, int):int");
    }

    public static Object addListener(Object bag, Object listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        } else if (listener instanceof Object[]) {
            throw new IllegalArgumentException();
        } else if (bag == null) {
            return listener;
        } else {
            if (bag instanceof Object[]) {
                Object[] array = (Object[]) bag;
                int L = array.length;
                if (L < 2) {
                    throw new IllegalArgumentException();
                }
                Object tmp = new Object[(L + 1)];
                System.arraycopy(array, 0, tmp, 0, L);
                tmp[L] = listener;
                return tmp;
            }
            return new Object[]{bag, listener};
        }
    }

    public static Object removeListener(Object bag, Object listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        } else if (listener instanceof Object[]) {
            throw new IllegalArgumentException();
        } else if (bag == listener) {
            return null;
        } else {
            if (!(bag instanceof Object[])) {
                return bag;
            }
            Object[] array = (Object[]) bag;
            int L = array.length;
            if (L < 2) {
                throw new IllegalArgumentException();
            } else if (L != 2) {
                int i = L;
                do {
                    i--;
                    if (array[i] == listener) {
                        Object tmp = new Object[(L - 1)];
                        System.arraycopy(array, 0, tmp, 0, i);
                        System.arraycopy(array, i + 1, tmp, i, L - (i + 1));
                        return tmp;
                    }
                } while (i != 0);
                return bag;
            } else if (array[1] == listener) {
                return array[0];
            } else {
                if (array[0] == listener) {
                    return array[1];
                }
                return bag;
            }
        }
    }

    public static Object getListener(Object bag, int index) {
        Object[] array;
        if (index == 0) {
            if (bag == null) {
                return null;
            }
            if (!(bag instanceof Object[])) {
                return bag;
            }
            array = (Object[]) bag;
            if (array.length >= 2) {
                return array[0];
            }
            throw new IllegalArgumentException();
        } else if (index != 1) {
            array = (Object[]) bag;
            int L = array.length;
            if (L < 2) {
                throw new IllegalArgumentException();
            } else if (index == L) {
                return null;
            } else {
                return array[index];
            }
        } else if (bag instanceof Object[]) {
            return ((Object[]) bag)[1];
        } else {
            if (bag != null) {
                return null;
            }
            throw new IllegalArgumentException();
        }
    }

    static Object initHash(Map<Object, Object> h, Object key, Object initialValue) {
        synchronized (h) {
            Object current = h.get(key);
            if (current == null) {
                h.put(key, initialValue);
            } else {
                initialValue = current;
            }
        }
        return initialValue;
    }

    public static Object makeHashKeyFromPair(Object key1, Object key2) {
        if (key1 == null) {
            throw new IllegalArgumentException();
        } else if (key2 != null) {
            return new ComplexKey(key1, key2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String readReader(Reader r) throws IOException {
        char[] buffer = new char[Opcodes.ACC_INTERFACE];
        int cursor = 0;
        while (true) {
            int n = r.read(buffer, cursor, buffer.length - cursor);
            if (n < 0) {
                return new String(buffer, 0, cursor);
            }
            cursor += n;
            if (cursor == buffer.length) {
                char[] tmp = new char[(buffer.length * 2)];
                System.arraycopy(buffer, 0, tmp, 0, cursor);
                buffer = tmp;
            }
        }
    }

    public static byte[] readStream(InputStream is, int initialBufferCapacity) throws IOException {
        if (initialBufferCapacity <= 0) {
            throw new IllegalArgumentException("Bad initialBufferCapacity: " + initialBufferCapacity);
        }
        byte[] tmp;
        byte[] buffer = new byte[initialBufferCapacity];
        int cursor = 0;
        while (true) {
            int n = is.read(buffer, cursor, buffer.length - cursor);
            if (n < 0) {
                break;
            }
            cursor += n;
            if (cursor == buffer.length) {
                tmp = new byte[(buffer.length * 2)];
                System.arraycopy(buffer, 0, tmp, 0, cursor);
                buffer = tmp;
            }
        }
        if (cursor == buffer.length) {
            return buffer;
        }
        tmp = new byte[cursor];
        System.arraycopy(buffer, 0, tmp, 0, cursor);
        return tmp;
    }

    public static RuntimeException codeBug() throws RuntimeException {
        RuntimeException ex = new IllegalStateException("FAILED ASSERTION");
        ex.printStackTrace(System.err);
        throw ex;
    }

    public static RuntimeException codeBug(String msg) throws RuntimeException {
        RuntimeException ex = new IllegalStateException("FAILED ASSERTION: " + msg);
        ex.printStackTrace(System.err);
        throw ex;
    }
}
