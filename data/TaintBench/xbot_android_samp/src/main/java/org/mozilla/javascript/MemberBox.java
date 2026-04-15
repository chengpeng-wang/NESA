package org.mozilla.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

final class MemberBox implements Serializable {
    private static final Class<?>[] primitives = new Class[]{Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE};
    static final long serialVersionUID = 6358550398665688245L;
    transient Class<?>[] argTypes;
    transient Object delegateTo;
    private transient Member memberObject;
    transient boolean vararg;

    MemberBox(Method method) {
        init(method);
    }

    MemberBox(Constructor<?> constructor) {
        init((Constructor) constructor);
    }

    private void init(Method method) {
        this.memberObject = method;
        this.argTypes = method.getParameterTypes();
        this.vararg = VMBridge.instance.isVarArgs(method);
    }

    private void init(Constructor<?> constructor) {
        this.memberObject = constructor;
        this.argTypes = constructor.getParameterTypes();
        this.vararg = VMBridge.instance.isVarArgs(constructor);
    }

    /* access modifiers changed from: 0000 */
    public Method method() {
        return (Method) this.memberObject;
    }

    /* access modifiers changed from: 0000 */
    public Constructor<?> ctor() {
        return (Constructor) this.memberObject;
    }

    /* access modifiers changed from: 0000 */
    public Member member() {
        return this.memberObject;
    }

    /* access modifiers changed from: 0000 */
    public boolean isMethod() {
        return this.memberObject instanceof Method;
    }

    /* access modifiers changed from: 0000 */
    public boolean isCtor() {
        return this.memberObject instanceof Constructor;
    }

    /* access modifiers changed from: 0000 */
    public boolean isStatic() {
        return Modifier.isStatic(this.memberObject.getModifiers());
    }

    /* access modifiers changed from: 0000 */
    public String getName() {
        return this.memberObject.getName();
    }

    /* access modifiers changed from: 0000 */
    public Class<?> getDeclaringClass() {
        return this.memberObject.getDeclaringClass();
    }

    /* access modifiers changed from: 0000 */
    public String toJavaDeclaration() {
        StringBuilder sb = new StringBuilder();
        if (isMethod()) {
            Method method = method();
            sb.append(method.getReturnType());
            sb.append(' ');
            sb.append(method.getName());
        } else {
            String name = ctor().getDeclaringClass().getName();
            int lastDot = name.lastIndexOf(46);
            if (lastDot >= 0) {
                name = name.substring(lastDot + 1);
            }
            sb.append(name);
        }
        sb.append(JavaMembers.liveConnectSignature(this.argTypes));
        return sb.toString();
    }

    public String toString() {
        return this.memberObject.toString();
    }

    /* access modifiers changed from: 0000 */
    public Object invoke(Object target, Object[] args) {
        Method method = method();
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException ex) {
            Method accessible = searchAccessibleMethod(method, this.argTypes);
            if (accessible != null) {
                this.memberObject = accessible;
                method = accessible;
            } else if (!VMBridge.instance.tryToMakeAccessible(method)) {
                throw Context.throwAsScriptRuntimeEx(ex);
            }
            return method.invoke(target, args);
        } catch (InvocationTargetException ite) {
            Throwable e = ite;
            do {
                e = ((InvocationTargetException) e).getTargetException();
            } while (e instanceof InvocationTargetException);
            if (e instanceof ContinuationPending) {
                throw ((ContinuationPending) e);
            }
            throw Context.throwAsScriptRuntimeEx(e);
        } catch (Exception ex2) {
            throw Context.throwAsScriptRuntimeEx(ex2);
        }
    }

    /* access modifiers changed from: 0000 */
    public Object newInstance(Object[] args) {
        Constructor<?> ctor = ctor();
        try {
            return ctor.newInstance(args);
        } catch (IllegalAccessException ex) {
            if (VMBridge.instance.tryToMakeAccessible(ctor)) {
                return ctor.newInstance(args);
            }
            throw Context.throwAsScriptRuntimeEx(ex);
        } catch (Exception ex2) {
            throw Context.throwAsScriptRuntimeEx(ex2);
        }
    }

    private static Method searchAccessibleMethod(Method method, Class<?>[] params) {
        int modifiers = method.getModifiers();
        if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
            Class<?> c = method.getDeclaringClass();
            if (!Modifier.isPublic(c.getModifiers())) {
                String name = method.getName();
                Class<?>[] intfs = c.getInterfaces();
                int i = 0;
                int N = intfs.length;
                while (i != N) {
                    Class<?> intf = intfs[i];
                    if (Modifier.isPublic(intf.getModifiers())) {
                        try {
                            return intf.getMethod(name, params);
                        } catch (NoSuchMethodException | SecurityException e) {
                        }
                    } else {
                        i++;
                    }
                }
                while (true) {
                    c = c.getSuperclass();
                    if (c == null) {
                        break;
                    } else if (Modifier.isPublic(c.getModifiers())) {
                        try {
                            Method m = c.getMethod(name, params);
                            int mModifiers = m.getModifiers();
                            if (Modifier.isPublic(mModifiers) && !Modifier.isStatic(mModifiers)) {
                                return m;
                            }
                        } catch (NoSuchMethodException e2) {
                        } catch (SecurityException e3) {
                        }
                    }
                }
            }
        }
        return null;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Member member = readMember(in);
        if (member instanceof Method) {
            init((Method) member);
        } else {
            init((Constructor) member);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        writeMember(out, this.memberObject);
    }

    private static void writeMember(ObjectOutputStream out, Member member) throws IOException {
        if (member == null) {
            out.writeBoolean(false);
            return;
        }
        out.writeBoolean(true);
        if ((member instanceof Method) || (member instanceof Constructor)) {
            out.writeBoolean(member instanceof Method);
            out.writeObject(member.getName());
            out.writeObject(member.getDeclaringClass());
            if (member instanceof Method) {
                writeParameters(out, ((Method) member).getParameterTypes());
                return;
            } else {
                writeParameters(out, ((Constructor) member).getParameterTypes());
                return;
            }
        }
        throw new IllegalArgumentException("not Method or Constructor");
    }

    private static Member readMember(ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (!in.readBoolean()) {
            return null;
        }
        boolean isMethod = in.readBoolean();
        String name = (String) in.readObject();
        Class<?> declaring = (Class) in.readObject();
        Class<?>[] parms = readParameters(in);
        if (!isMethod) {
            return declaring.getConstructor(parms);
        }
        try {
            return declaring.getMethod(name, parms);
        } catch (NoSuchMethodException e) {
            throw new IOException("Cannot find member: " + e);
        }
    }

    private static void writeParameters(ObjectOutputStream out, Class<?>[] parms) throws IOException {
        out.writeShort(parms.length);
        for (Class<?> parm : parms) {
            boolean primitive = parm.isPrimitive();
            out.writeBoolean(primitive);
            if (primitive) {
                int j = 0;
                while (j < primitives.length) {
                    if (parm.equals(primitives[j])) {
                        out.writeByte(j);
                    } else {
                        j++;
                    }
                }
                throw new IllegalArgumentException("Primitive " + parm + " not found");
            }
            out.writeObject(parm);
        }
    }

    private static Class<?>[] readParameters(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Class<?>[] result = new Class[in.readShort()];
        for (int i = 0; i < result.length; i++) {
            if (in.readBoolean()) {
                result[i] = primitives[in.readByte()];
            } else {
                result[i] = (Class) in.readObject();
            }
        }
        return result;
    }
}
