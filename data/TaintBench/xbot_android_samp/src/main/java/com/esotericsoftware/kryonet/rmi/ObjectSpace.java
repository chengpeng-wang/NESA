package com.esotericsoftware.kryonet.rmi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.util.IntMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ObjectSpace {
    static ObjectSpace[] instances = new ObjectSpace[0];
    private static final Object instancesLock = new Object();
    private static final byte kReturnExMask = (byte) 64;
    private static final byte kReturnValMask = Byte.MIN_VALUE;
    private static final HashMap<Class, CachedMethod[]> methodCache = new HashMap();
    Connection[] connections;
    final Object connectionsLock;
    Executor executor;
    final IntMap idToObject;
    private final Listener invokeListener;

    static class CachedMethod {
        Method method;
        Serializer[] serializers;

        CachedMethod() {
        }
    }

    private static class RemoteInvocationHandler implements InvocationHandler {
        private final Connection connection;
        private Byte lastResponseID;
        final ReentrantLock lock = new ReentrantLock();
        private byte nextResponseNum = (byte) 1;
        private boolean nonBlocking = false;
        final int objectID;
        final Condition responseCondition = this.lock.newCondition();
        private Listener responseListener;
        final ConcurrentHashMap<Byte, InvokeMethodResult> responseTable = new ConcurrentHashMap();
        private int timeoutMillis = 3000;
        private boolean transmitExceptions = true;
        private boolean transmitReturnValue = true;

        public RemoteInvocationHandler(Connection connection, final int i) {
            this.connection = connection;
            this.objectID = i;
            this.responseListener = new Listener() {
                public void received(Connection connection, Object obj) {
                    if (obj instanceof InvokeMethodResult) {
                        InvokeMethodResult invokeMethodResult = (InvokeMethodResult) obj;
                        if (invokeMethodResult.objectID == i) {
                            RemoteInvocationHandler.this.responseTable.put(Byte.valueOf(invokeMethodResult.responseID), invokeMethodResult);
                            RemoteInvocationHandler.this.lock.lock();
                            try {
                                RemoteInvocationHandler.this.responseCondition.signalAll();
                            } finally {
                                RemoteInvocationHandler.this.lock.unlock();
                            }
                        }
                    }
                }

                public void disconnected(Connection connection) {
                    RemoteInvocationHandler.this.close();
                }
            };
            connection.addListener(this.responseListener);
        }

        public Object invoke(Object obj, Method method, Object[] objArr) throws Exception {
            byte b = (byte) 1;
            if (method.getDeclaringClass() == RemoteObject.class) {
                String name = method.getName();
                if (name.equals("close")) {
                    close();
                    return null;
                } else if (name.equals("setResponseTimeout")) {
                    this.timeoutMillis = ((Integer) objArr[0]).intValue();
                    return null;
                } else if (name.equals("setNonBlocking")) {
                    this.nonBlocking = ((Boolean) objArr[0]).booleanValue();
                    return null;
                } else if (name.equals("setTransmitReturnValue")) {
                    this.transmitReturnValue = ((Boolean) objArr[0]).booleanValue();
                    return null;
                } else if (name.equals("setTransmitExceptions")) {
                    this.transmitExceptions = ((Boolean) objArr[0]).booleanValue();
                    return null;
                } else if (name.equals("waitForLastResponse")) {
                    if (this.lastResponseID != null) {
                        return waitForResponse(this.lastResponseID.byteValue());
                    }
                    throw new IllegalStateException("There is no last response to wait for.");
                } else if (name.equals("getLastResponseID")) {
                    if (this.lastResponseID != null) {
                        return this.lastResponseID;
                    }
                    throw new IllegalStateException("There is no last response ID.");
                } else if (name.equals("waitForResponse")) {
                    if (this.transmitReturnValue || this.transmitExceptions || !this.nonBlocking) {
                        return waitForResponse(((Byte) objArr[0]).byteValue());
                    }
                    throw new IllegalStateException("This RemoteObject is currently set to ignore all responses.");
                } else if (name.equals("getConnection")) {
                    return this.connection;
                } else {
                    throw new RuntimeException("Invocation handler could not find RemoteObject method. Check ObjectSpace.java");
                }
            } else if (method.getDeclaringClass() != Object.class) {
                InvokeMethod invokeMethod = new InvokeMethod();
                invokeMethod.objectID = this.objectID;
                invokeMethod.method = method;
                invokeMethod.args = objArr;
                if (!(this.transmitReturnValue || this.transmitExceptions || !this.nonBlocking)) {
                    b = (byte) 0;
                }
                if (b != (byte) 0) {
                    synchronized (this) {
                        b = this.nextResponseNum;
                        this.nextResponseNum = (byte) (b + 1);
                        if (this.nextResponseNum == ObjectSpace.kReturnExMask) {
                            this.nextResponseNum = (byte) 1;
                        }
                    }
                    if (this.transmitReturnValue) {
                        b = (byte) (b | -128);
                    }
                    if (this.transmitExceptions) {
                        b = (byte) (b | 64);
                    }
                    invokeMethod.responseID = b;
                } else {
                    invokeMethod.responseID = (byte) 0;
                }
                this.connection.sendTCP(invokeMethod);
                if (invokeMethod.responseID != (byte) 0) {
                    this.lastResponseID = Byte.valueOf(invokeMethod.responseID);
                }
                if (this.nonBlocking) {
                    Class returnType = method.getReturnType();
                    if (returnType.isPrimitive()) {
                        if (returnType == Integer.TYPE) {
                            return Integer.valueOf(0);
                        }
                        if (returnType == Boolean.TYPE) {
                            return Boolean.FALSE;
                        }
                        if (returnType == Float.TYPE) {
                            return Float.valueOf(0.0f);
                        }
                        if (returnType == Character.TYPE) {
                            return Character.valueOf(0);
                        }
                        if (returnType == Long.TYPE) {
                            return Long.valueOf(0);
                        }
                        if (returnType == Short.TYPE) {
                            return Short.valueOf((short) 0);
                        }
                        if (returnType == Byte.TYPE) {
                            return Byte.valueOf((byte) 0);
                        }
                        if (returnType == Double.TYPE) {
                            return Double.valueOf(0.0d);
                        }
                    }
                    return null;
                }
                try {
                    Object waitForResponse = waitForResponse(invokeMethod.responseID);
                    if (waitForResponse == null || !(waitForResponse instanceof Exception)) {
                        return waitForResponse;
                    }
                    throw ((Exception) waitForResponse);
                } catch (TimeoutException e) {
                    throw new TimeoutException("Response timed out: " + method.getDeclaringClass().getName() + "." + method.getName());
                }
            } else if (method.getName().equals("toString")) {
                return "<proxy>";
            } else {
                try {
                    return method.invoke(obj, objArr);
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
        }

        private Object waitForResponse(byte b) {
            if (this.connection.getEndPoint().getUpdateThread() == Thread.currentThread()) {
                throw new IllegalStateException("Cannot wait for an RMI response on the connection's update thread.");
            }
            long currentTimeMillis = System.currentTimeMillis() + ((long) this.timeoutMillis);
            while (true) {
                long currentTimeMillis2 = currentTimeMillis - System.currentTimeMillis();
                if (this.responseTable.containsKey(Byte.valueOf(b))) {
                    InvokeMethodResult invokeMethodResult = (InvokeMethodResult) this.responseTable.get(Byte.valueOf(b));
                    this.responseTable.remove(Byte.valueOf(b));
                    this.lastResponseID = null;
                    return invokeMethodResult.result;
                } else if (currentTimeMillis2 <= 0) {
                    throw new TimeoutException("Response timed out.");
                } else {
                    this.lock.lock();
                    try {
                        this.responseCondition.await(currentTimeMillis2, TimeUnit.MILLISECONDS);
                        this.lock.unlock();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    } catch (Throwable th) {
                        this.lock.unlock();
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void close() {
            this.connection.removeListener(this.responseListener);
        }
    }

    public static class InvokeMethod implements FrameworkMessage, KryoSerializable {
        public Object[] args;
        public Method method;
        public int objectID;
        public byte responseID;

        public void write(Kryo kryo, Output output) {
            int i;
            int i2 = 0;
            output.writeInt(this.objectID, true);
            output.writeInt(kryo.getRegistration(this.method.getDeclaringClass()).getId(), true);
            CachedMethod[] methods = ObjectSpace.getMethods(kryo, this.method.getDeclaringClass());
            CachedMethod cachedMethod = null;
            int length = methods.length;
            for (i = 0; i < length; i++) {
                cachedMethod = methods[i];
                if (cachedMethod.method.equals(this.method)) {
                    output.writeByte(i);
                    break;
                }
            }
            i = cachedMethod.serializers.length;
            while (i2 < i) {
                Serializer serializer = cachedMethod.serializers[i2];
                if (serializer != null) {
                    kryo.writeObjectOrNull(output, this.args[i2], serializer);
                } else {
                    kryo.writeClassAndObject(output, this.args[i2]);
                }
                i2++;
            }
            output.writeByte(this.responseID);
        }

        public void read(Kryo kryo, Input input) {
            this.objectID = input.readInt(true);
            Class type = kryo.getRegistration(input.readInt(true)).getType();
            int readByte = input.readByte();
            try {
                CachedMethod cachedMethod = ObjectSpace.getMethods(kryo, type)[readByte];
                this.method = cachedMethod.method;
                this.args = new Object[cachedMethod.serializers.length];
                int length = this.args.length;
                for (int i = 0; i < length; i++) {
                    Serializer serializer = cachedMethod.serializers[i];
                    if (serializer != null) {
                        this.args[i] = kryo.readObjectOrNull(input, this.method.getParameterTypes()[i], serializer);
                    } else {
                        this.args[i] = kryo.readClassAndObject(input);
                    }
                }
                this.responseID = input.readByte();
            } catch (IndexOutOfBoundsException e) {
                throw new KryoException("Invalid method index " + readByte + " for class: " + type.getName());
            }
        }
    }

    public static class InvokeMethodResult implements FrameworkMessage {
        public int objectID;
        public byte responseID;
        public Object result;
    }

    public ObjectSpace() {
        this.idToObject = new IntMap();
        this.connections = new Connection[0];
        this.connectionsLock = new Object();
        this.invokeListener = new Listener() {
            public void received(final Connection connection, Object obj) {
                if (obj instanceof InvokeMethod) {
                    if (ObjectSpace.this.connections != null) {
                        int i = 0;
                        int length = ObjectSpace.this.connections.length;
                        while (i < length && connection != ObjectSpace.this.connections[i]) {
                            i++;
                        }
                        if (i == length) {
                            return;
                        }
                    }
                    final InvokeMethod invokeMethod = (InvokeMethod) obj;
                    final Object obj2 = ObjectSpace.this.idToObject.get(invokeMethod.objectID);
                    if (obj2 == null) {
                        return;
                    }
                    if (ObjectSpace.this.executor == null) {
                        ObjectSpace.this.invoke(connection, obj2, invokeMethod);
                    } else {
                        ObjectSpace.this.executor.execute(new Runnable() {
                            public void run() {
                                ObjectSpace.this.invoke(connection, obj2, invokeMethod);
                            }
                        });
                    }
                }
            }

            public void disconnected(Connection connection) {
                ObjectSpace.this.removeConnection(connection);
            }
        };
        synchronized (instancesLock) {
            ObjectSpace[] objectSpaceArr = instances;
            ObjectSpace[] objectSpaceArr2 = new ObjectSpace[(objectSpaceArr.length + 1)];
            objectSpaceArr2[0] = this;
            System.arraycopy(objectSpaceArr, 0, objectSpaceArr2, 1, objectSpaceArr.length);
            instances = objectSpaceArr2;
        }
    }

    public ObjectSpace(Connection connection) {
        this();
        addConnection(connection);
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void register(int i, Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        this.idToObject.put(i, obj);
    }

    public void remove(int i) {
        this.idToObject.remove(i);
    }

    public void remove(Object obj) {
        if (this.idToObject.containsValue(obj, true)) {
            this.idToObject.remove(this.idToObject.findKey(obj, true, -1));
        }
    }

    public void close() {
        Connection[] connectionArr = this.connections;
        for (Connection removeListener : connectionArr) {
            removeListener.removeListener(this.invokeListener);
        }
        synchronized (instancesLock) {
            ArrayList arrayList = new ArrayList(Arrays.asList(instances));
            arrayList.remove(this);
            instances = (ObjectSpace[]) arrayList.toArray(new ObjectSpace[arrayList.size()]);
        }
    }

    public void addConnection(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection cannot be null.");
        }
        synchronized (this.connectionsLock) {
            Connection[] connectionArr = new Connection[(this.connections.length + 1)];
            connectionArr[0] = connection;
            System.arraycopy(this.connections, 0, connectionArr, 1, this.connections.length);
            this.connections = connectionArr;
        }
        connection.addListener(this.invokeListener);
    }

    public void removeConnection(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection cannot be null.");
        }
        connection.removeListener(this.invokeListener);
        synchronized (this.connectionsLock) {
            ArrayList arrayList = new ArrayList(Arrays.asList(this.connections));
            arrayList.remove(connection);
            this.connections = (Connection[]) arrayList.toArray(new Connection[arrayList.size()]);
        }
    }

    /* access modifiers changed from: protected */
    public void invoke(Connection connection, Object obj, InvokeMethod invokeMethod) {
        Object obj2 = 1;
        byte b = invokeMethod.responseID;
        Object obj3 = (b & -128) == -128 ? 1 : null;
        if ((b & 64) != 64) {
            obj2 = null;
        }
        Method method = invokeMethod.method;
        try {
            obj2 = method.invoke(obj, invokeMethod.args);
        } catch (InvocationTargetException e) {
            if (obj2 != null) {
                obj2 = e.getCause();
            } else {
                throw new RuntimeException("Error invoking method: " + method.getDeclaringClass().getName() + "." + method.getName(), e);
            }
        } catch (Exception e2) {
            throw new RuntimeException("Error invoking method: " + method.getDeclaringClass().getName() + "." + method.getName(), e2);
        }
        if (b != (byte) 0) {
            InvokeMethodResult invokeMethodResult = new InvokeMethodResult();
            invokeMethodResult.objectID = invokeMethod.objectID;
            invokeMethodResult.responseID = b;
            if (obj3 != null || invokeMethod.method.getReturnType().isPrimitive()) {
                invokeMethodResult.result = obj2;
            } else {
                invokeMethodResult.result = null;
            }
            connection.sendTCP(invokeMethodResult);
        }
    }

    public static <T> T getRemoteObject(Connection connection, int i, Class<T> cls) {
        return getRemoteObject(connection, i, cls);
    }

    public static RemoteObject getRemoteObject(Connection connection, int i, Class... clsArr) {
        if (connection == null) {
            throw new IllegalArgumentException("connection cannot be null.");
        } else if (clsArr == null) {
            throw new IllegalArgumentException("ifaces cannot be null.");
        } else {
            Class[] clsArr2 = new Class[(clsArr.length + 1)];
            clsArr2[0] = RemoteObject.class;
            System.arraycopy(clsArr, 0, clsArr2, 1, clsArr.length);
            return (RemoteObject) Proxy.newProxyInstance(ObjectSpace.class.getClassLoader(), clsArr2, new RemoteInvocationHandler(connection, i));
        }
    }

    static CachedMethod[] getMethods(Kryo kryo, Class cls) {
        CachedMethod[] cachedMethodArr = (CachedMethod[]) methodCache.get(cls);
        if (cachedMethodArr != null) {
            return cachedMethodArr;
        }
        ArrayList arrayList = new ArrayList();
        Class cls2 = cls;
        while (cls2 != null && cls2 != Object.class) {
            Collections.addAll(arrayList, cls2.getDeclaredMethods());
            cls2 = cls2.getSuperclass();
        }
        PriorityQueue priorityQueue = new PriorityQueue(Math.max(1, arrayList.size()), new Comparator<Method>() {
            public int compare(Method method, Method method2) {
                int compareTo = method.getName().compareTo(method2.getName());
                if (compareTo != 0) {
                    return compareTo;
                }
                Class[] parameterTypes = method.getParameterTypes();
                Class[] parameterTypes2 = method2.getParameterTypes();
                if (parameterTypes.length > parameterTypes2.length) {
                    return 1;
                }
                if (parameterTypes.length < parameterTypes2.length) {
                    return -1;
                }
                for (compareTo = 0; compareTo < parameterTypes.length; compareTo++) {
                    int compareTo2 = parameterTypes[compareTo].getName().compareTo(parameterTypes2[compareTo].getName());
                    if (compareTo2 != 0) {
                        return compareTo2;
                    }
                }
                throw new RuntimeException("Two methods with same signature!");
            }
        });
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Method method = (Method) arrayList.get(i);
            int modifiers = method.getModifiers();
            if (!(Modifier.isStatic(modifiers) || Modifier.isPrivate(modifiers) || method.isSynthetic())) {
                priorityQueue.add(method);
            }
        }
        size = priorityQueue.size();
        CachedMethod[] cachedMethodArr2 = new CachedMethod[size];
        for (int i2 = 0; i2 < size; i2++) {
            CachedMethod cachedMethod = new CachedMethod();
            cachedMethod.method = (Method) priorityQueue.poll();
            Class[] parameterTypes = cachedMethod.method.getParameterTypes();
            cachedMethod.serializers = new Serializer[parameterTypes.length];
            int length = parameterTypes.length;
            for (int i3 = 0; i3 < length; i3++) {
                if (kryo.isFinal(parameterTypes[i3])) {
                    cachedMethod.serializers[i3] = kryo.getSerializer(parameterTypes[i3]);
                }
            }
            cachedMethodArr2[i2] = cachedMethod;
        }
        methodCache.put(cls, cachedMethodArr2);
        return cachedMethodArr2;
    }

    static Object getRegisteredObject(Connection connection, int i) {
        for (ObjectSpace objectSpace : instances) {
            Connection[] connectionArr = objectSpace.connections;
            for (Connection connection2 : connectionArr) {
                if (connection2 == connection) {
                    Object obj = objectSpace.idToObject.get(i);
                    if (obj != null) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }

    public static void registerClasses(Kryo kryo) {
        kryo.register(Object[].class);
        kryo.register(InvokeMethod.class);
        ((FieldSerializer) kryo.register(InvokeMethodResult.class).getSerializer()).getField("objectID").setClass(Integer.TYPE, new Serializer<Integer>() {
            public void write(Kryo kryo, Output output, Integer num) {
                output.writeInt(num.intValue(), true);
            }

            public Integer read(Kryo kryo, Input input, Class<Integer> cls) {
                return Integer.valueOf(input.readInt(true));
            }
        });
        kryo.register(InvocationHandler.class, new Serializer() {
            public void write(Kryo kryo, Output output, Object obj) {
                output.writeInt(((RemoteInvocationHandler) Proxy.getInvocationHandler(obj)).objectID, true);
            }

            public Object read(Kryo kryo, Input input, Class cls) {
                return ObjectSpace.getRegisteredObject((Connection) kryo.getContext().get("connection"), input.readInt(true));
            }
        });
    }
}
